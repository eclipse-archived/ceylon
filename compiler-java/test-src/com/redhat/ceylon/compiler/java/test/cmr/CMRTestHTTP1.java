package com.redhat.ceylon.compiler.java.test.cmr;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

import org.junit.Test;

import junit.framework.Assert;

import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.java.util.Util;
import com.sun.net.httpserver.HttpServer;

public class CMRTestHTTP1 extends CompilerTest {
    
    @Test
    public void testMdlHTTPRepos() throws IOException{
        String moduleA = "com.redhat.ceylon.compiler.java.test.cmr.module.depend.a";
        int port = 18000;
        
        // Clean up any cached version
        File carFileInHomeRepo = getModuleArchive(moduleA, "6.6.6", Util.getHomeRepository());
        if(carFileInHomeRepo.exists())
            carFileInHomeRepo.delete();

        // Compile the first module in its own repo 
        File repoA = new File("build/test-cars-http1");
        cleanCars(repoA.getPath());
        repoA.mkdirs();
        
        Boolean result = getCompilerTask(Arrays.asList("-out", repoA.getPath()),
                "module/depend/a/module.ceylon", "module/depend/a/package.ceylon", "module/depend/a/A.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);
        
        File carFile = getModuleArchive(moduleA, "6.6.6", repoA.getPath());
        assertTrue(carFile.exists());

        // now serve the first repo over HTTP
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 1);
        server.createContext("/repo", new RepoFileHandler(repoA.getPath()));
        server.setExecutor(null); // creates a default executor
        server.start();
        
        String repoAURL = "http://localhost:"+port+"/repo";
        
        try{
            // then try to compile only one module (the other being loaded from its car) 
            result = getCompilerTask(Arrays.asList("-out", destDir, "-rep", repoAURL),
                    "module/depend/b/module.ceylon", "module/depend/b/package.ceylon", "module/depend/b/a.ceylon", "module/depend/b/B.ceylon").call();
            Assert.assertEquals(Boolean.TRUE, result);

        }finally{
            server.stop(0);
        }
        carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.module.depend.b", "6.6.6");
        assertTrue(carFile.exists());
        
        // make sure it cached the module in the home repo
        assertTrue(carFileInHomeRepo.exists());
        
        // make sure it didn't cache it in the output repo
        carFile = getModuleArchive(moduleA, "6.6.6");
        assertFalse(carFile.exists());
    }

}