package com.redhat.ceylon.compiler.java.test.structure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RepoFileHandler implements HttpHandler {

    private String folder;

    public RepoFileHandler(String destdir) {
        this.folder = destdir;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        String path = t.getRequestURI().getPath();
        System.err.println("Serving URI "+path);
        if(path.startsWith("/repo/")){
           path = path.substring(6);
           File file = new File(folder, path);
           if(file.exists()){
               System.err.println("Serving file "+file.getPath());
               t.sendResponseHeaders(200, file.length());
               OutputStream os = t.getResponseBody();
               // only write the contents if it's not a directory, otherwise the CMR expects an empty 200 response
               if(!file.isDirectory()){
                   InputStream is = new FileInputStream(file);
                   byte[] data = new byte[(int) file.length()];
                   is.read(data);
                   is.close();
                   os.write(data);
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

}
