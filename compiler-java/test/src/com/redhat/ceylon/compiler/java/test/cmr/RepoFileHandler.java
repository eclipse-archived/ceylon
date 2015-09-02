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
package com.redhat.ceylon.compiler.java.test.cmr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.redhat.ceylon.compiler.java.test.cmr.CMRHTTPTests.RequestCounter;
import com.redhat.ceylon.compiler.java.test.cmr.CMRHTTPTests.TimeoutIn;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RepoFileHandler implements HttpHandler {

    enum Method {
        Get, Put;
    }
    
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
    private RequestCounter rq;
    private boolean herd;
    private TimeoutIn timeoutIn;

    public RepoFileHandler(String destdir, boolean herd, RequestCounter rq, TimeoutIn timeoutIn) {
        this.folder = destdir;
        this.rq = rq;
        this.herd = herd;
        this.timeoutIn = timeoutIn;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        if(rq != null)
            rq.add();
        String path = t.getRequestURI().getPath();
        String method = t.getRequestMethod();
        
        log("Serving URI "+method+" "+path);

        // filter on our prefix
        if(path.equals("/repo") || path.startsWith("/repo/")){
            // ignore this if not Herd, to simulate a non-responsive server
            if(herd && "OPTIONS".equals(method)){
                t.getResponseHeaders().add("X-Herd-Version", "1");
                t.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
                t.close();
                return;
            }

            if("LOCK".equals(method)){
                // we need to send a lock response: http://www.webdav.org/specs/rfc2518.html#rfc.figure.u.26
                byte[] bytes = DAV_LOCK_RESPONSE.getBytes();
                t.sendResponseHeaders(HttpURLConnection.HTTP_OK, bytes.length);
                OutputStream response = t.getResponseBody();
                response.write(bytes);
                t.close();
                return;
            }

            File file;
            if(path.equals("/repo")){
                file = new File(folder);
            }else{
                path = path.substring(6);
                file = new File(folder, path);
            }

            if("UNLOCK".equals(method)){
                t.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
                t.close();
                return;
            }

            if("MKCOL".equals(method)){
                file.mkdirs();
                // OK
                t.sendResponseHeaders(HttpURLConnection.HTTP_CREATED, -1);
                t.close();
                return;
            }
            
            if("PUT".equals(method)){
                if(timeoutIn == TimeoutIn.PutInitial)
                    timeout();
                // make sure parents exist
                file.getParentFile().mkdirs();
                // save the file
                FileOutputStream os = new FileOutputStream(file);
                InputStream body = t.getRequestBody();
                copy(body, os, Method.Put);
                body.close();
                os.close();
                // OKGet
                t.sendResponseHeaders(HttpURLConnection.HTTP_CREATED, -1);
                t.close();
                return;
            }

            if(file.exists()){
                if("GET".equals(method)){
                    log("Serving file "+file.getPath());
                    if(timeoutIn == TimeoutIn.GetInitial)
                        timeout();
                    t.sendResponseHeaders(HttpURLConnection.HTTP_OK, file.length());
                    OutputStream os = t.getResponseBody();
                    // only write the contents if it's not a directory, otherwise the CMR expects an empty 200 response
                    if(!file.isDirectory()){
                        InputStream is = new FileInputStream(file);
                        copy(is, os, Method.Get);
                    }
                    t.close();
                    return;
                }
                if("HEAD".equals(method)){
                    if(timeoutIn == TimeoutIn.Head)
                        timeout();
                    t.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
                    t.close();
                    return;
                }
                if("PROPFIND".equals(method)){
                    String ret = propfind(file);
                    t.sendResponseHeaders(207, ret.length()); // MULTI STATUS
                    t.getResponseBody().write(ret.getBytes());
                    t.close();
                    return;
                }
            }else
                log("File does not exist: "+file.getAbsolutePath());
        }
        log("Returning 404");
        t.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, -1);
        t.close();
    }

    private String propfind(File file) throws IOException {
        StringBuilder ret = new StringBuilder("<?xml version='1.0' encoding='utf-8' ?>\n");
        ret.append("<multistatus xmlns='DAV:'>\n");
        if(file.isFile())
            propfindFile(file, ret);
        else{
            // first one is the folder itself
            propfindFile(file, ret);
            for(File child : file.listFiles())
                propfindFile(child, ret);
        }
        ret.append("</multistatus>\n");
        return ret.toString();
    }
    
    private void propfindFile(File file, StringBuilder xml) throws IOException {
        String path = file.getCanonicalPath();
        path = path.substring(folder.length());
        
        xml.append("<response>\n");
        xml.append("  <href>/").append(path).append("</href>\n");
        xml.append("  <propstat>\n");
        xml.append("    <prop>\n");
        if(file.isDirectory())
            xml.append("      <resourcetype><collection/></resourcetype>\n");
        else
            xml.append("      <resourcetype/>\n");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        xml.append("      <getlastmodified>").append(format.format(new Date(file.lastModified()))).append("</getlastmodified>\n");

        xml.append("    </prop>\n");
        xml.append("    <status>HTTP/1.1 200 OK</status>\n");
        xml.append("  </propstat>\n");
        xml.append("</response>\n");
    }

    private void copy(InputStream in, OutputStream out, Method method) throws IOException {
        byte[] buf = new byte[1024];
        int read;
        while((read = in.read(buf)) != -1){
            out.write(buf, 0, read);
            if((timeoutIn == TimeoutIn.GetMiddle && method == Method.Get)
                    || (timeoutIn == TimeoutIn.PutMiddle && method == Method.Put))
                timeout();
        }
        out.flush();
    }

    private void timeout() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void log(String string) {
        System.err.println(string);
    }

}
