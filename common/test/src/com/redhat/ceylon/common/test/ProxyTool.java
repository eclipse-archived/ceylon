package com.redhat.ceylon.common.test;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.redhat.ceylon.common.config.Authentication;
import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.CeylonConfigFinder;
import com.redhat.ceylon.common.config.Proxies;
import com.redhat.ceylon.common.config.Proxies.Proxy;

public class ProxyTool {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            usage("Wrong number of arguments");
        }
        String mode = args[0];
        
        File configFile = new File(args[1]);
        if (!configFile.exists()) {
            usage("Config file doesn't exist");
            return;
        }
        
        final URL url; 
        try {
            url  = new URL(args[2]);
        } catch (MalformedURLException e) {
            usage("Malformed URL");
            return;
        }
        
        CeylonConfig config = CeylonConfigFinder.loadConfigFromFile(configFile);
        Authentication auth = Authentication.fromConfig(config);
        Proxy proxy = Proxies.withConfig(config).getProxy();
        final URLConnection conn;
        switch (mode){
        case "proxy":
            java.net.Proxy netProxy = auth.getProxy();
            if (netProxy != null) {
                conn = url.openConnection(netProxy);
            } else {
                conn = url.openConnection();
            }
            break;
        case "install":
            auth.installProxy();
            conn = url.openConnection();
            break;
        default:
            throw new RuntimeException("Unsupported mode");
        }
        
        InputStream in = conn.getInputStream();
        try {
            int ch = in.read();
            while (ch != -1) {
                System.out.append((char)ch);
                ch = in.read();
            }
        } finally {
            in.close();
        }
        
    }

    private static void usage(String error) {
        if (error != null) {
            System.err.println(error);
        }
        System.err.println("Expect 3 arguments: proxy|install <configfile> <url>");
        System.exit(1);
    }
    
}
