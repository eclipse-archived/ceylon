/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.java.test.structure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RepoFileHandler implements HttpHandler {

    private static final String DAV_LOCK_RESPONSE = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?>"
            +"<prop xmlns='DAV:'>"
                +"<lockdiscovery>"
                 +"<activelock>"
                      +"<locktype><write/></locktype>"
                      +"<lockscope><exclusive/></lockscope>"
                      +"<depth>Infinity</depth>"
                      +"<timeout>Second-604800</timeout>"
                      +"<locktoken>"
                           +"<href>"
                      +"opaquelocktoken:e71d4fae-5dec-22d6-fea5-00a0c91e6be4"
                           +"</href>"
                      +"</locktoken>"
                 +"</activelock>"
            +"</lockdiscovery>"
          +"</prop>";
    private String folder;

    public RepoFileHandler(String destdir) {
        this.folder = destdir;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String path = t.getRequestURI().getPath();
        String method = t.getRequestMethod();
        
        System.err.println("Serving URI "+method+" "+path);

        // filter on our prefix
        if(path.startsWith("/repo/")){
            if("LOCK".equals(method)){
                // we need to send a lock response: http://www.webdav.org/specs/rfc2518.html#rfc.figure.u.26
                t.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream response = t.getResponseBody();
                response.write(DAV_LOCK_RESPONSE.getBytes());
                response.close();
                return;
            }

            path = path.substring(6);
            File file = new File(folder, path);

            if("PUT".equals(method)){
                // make sure parents exist
                file.getParentFile().mkdirs();
                // save the file
                FileOutputStream os = new FileOutputStream(file);
                InputStream body = t.getRequestBody();
                copy(body, os);
                body.close();
                os.close();
                // OK
                t.sendResponseHeaders(HttpURLConnection.HTTP_CREATED, 0);
                t.getResponseBody().close();
                return;
            }

            if(file.exists()){
                System.err.println("Serving file "+file.getPath());
                t.sendResponseHeaders(HttpURLConnection.HTTP_OK, file.length());
                OutputStream os = t.getResponseBody();
                // only write the contents if it's not a directory, otherwise the CMR expects an empty 200 response
                if(!file.isDirectory()){
                    InputStream is = new FileInputStream(file);
                    copy(is, os);
                }
                os.close();
                return;
            }else
                System.err.println("File does not exist: "+file.getAbsolutePath());
        }
        System.err.println("Returning 404");
        t.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
        t.getResponseBody().close();
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        int read;
        while((read = in.read(buf)) != -1){
            out.write(buf, 0, read);
        }
        out.flush();
    }

}
