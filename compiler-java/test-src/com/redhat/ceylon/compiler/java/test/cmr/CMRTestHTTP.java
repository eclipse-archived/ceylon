package com.redhat.ceylon.compiler.java.test.cmr;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.java.util.Util;
import com.sun.net.httpserver.HttpServer;

public class CMRTestHTTP extends CompilerTest {

    int port = 18000;
    String repoAURL = "http://localhost:"+port+"/repo";

    private HttpServer startServer(int port, File repo) throws IOException{
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 1);
        server.createContext("/repo", new RepoFileHandler(repo.getPath()));
        // make sure we serve at least two concurrent connections, as each one might take a few ms to close
        ThreadPoolExecutor tpool = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
        server.setExecutor(tpool);
        server.start();
        return server;
    }

    private File makeRepo() {
        File repo = new File("build/test-cars-http");
        cleanCars(repo.getPath());
        repo.mkdirs();
        return repo;
    }

    @Test
    public void testMdlHTTPRepos() throws IOException{
        String moduleA = "com.redhat.ceylon.compiler.java.test.cmr.module.depend.a";
        
        // Clean up any cached version
        File carFileInHomeRepo = getModuleArchive(moduleA, "6.6.6", Util.getHomeRepository());
        if(carFileInHomeRepo.exists())
            carFileInHomeRepo.delete();

        // Compile the first module in its own repo 
        File repo = makeRepo();
        
        Boolean result = getCompilerTask(Arrays.asList("-out", repo.getPath()),
                "module/depend/a/module.ceylon", "module/depend/a/package.ceylon", "module/depend/a/A.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);
        
        File carFile = getModuleArchive(moduleA, "6.6.6", repo.getPath());
        assertTrue(carFile.exists());

        // now serve the first repo over HTTP
        HttpServer server = startServer(port, repo); 
        
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

    
    @Test
    public void testMdlHTTPOutputRepo() throws IOException{
        // Compile the module in its own repo
        File repo = makeRepo();

        // now serve the first repo over HTTP
        HttpServer server = startServer(port, repo); 
        
        try{
            // then try to compile our module by outputting to HTTP 
            Boolean result = getCompilerTask(Arrays.asList("-out", repoAURL), "module/single/module.ceylon").call();
            Assert.assertEquals(Boolean.TRUE, result);

        }finally{
            server.stop(0);
        }
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.module.single", "6.6.6", repo.getPath());
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        // make sure it's not empty
        ZipEntry moduleClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/module/single/module.class");
        assertNotNull(moduleClass);
        car.close();

        File srcFile = getSourceArchive("com.redhat.ceylon.compiler.java.test.cmr.module.single", "6.6.6", repo.getPath());
        assertTrue(srcFile.exists());
    }


    @Test
    public void testMdlHTTPMixedCompilation() throws IOException{
        // Compile the first module in its own repo 
        File repo = makeRepo();

        // now serve the first repo over HTTP
        HttpServer server = startServer(port, repo); 
        
        try{
            // then try to compile our module by outputting to HTTP 
            Boolean result = getCompilerTask(Arrays.asList("-out", repoAURL), "module/mixed/JavaClass.java").call();
            Assert.assertEquals(Boolean.TRUE, result);
            result = getCompilerTask(Arrays.asList("-out", repoAURL), "module/mixed/CeylonClass.ceylon").call();
            Assert.assertEquals(Boolean.TRUE, result);

        }finally{
            server.stop(0);
        }
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.module.mixed", "6.6.6", repo.getPath());
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        // make sure it's not empty
        ZipEntry entry = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/module/mixed/module.class");
        assertNotNull(entry);
        entry = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/module/mixed/CeylonClass.class");
        assertNotNull(entry);
        entry = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/module/mixed/JavaClass.class");
        assertNotNull(entry);
        car.close();

        File srcFile = getSourceArchive("com.redhat.ceylon.compiler.java.test.cmr.module.mixed", "6.6.6", repo.getPath());
        assertTrue(srcFile.exists());

        JarFile src = new JarFile(srcFile);

        // make sure it's not empty
        entry = src.getEntry("com/redhat/ceylon/compiler/java/test/cmr/module/mixed/module.ceylon");
        assertNotNull(entry);
        entry = src.getEntry("com/redhat/ceylon/compiler/java/test/cmr/module/mixed/CeylonClass.ceylon");
        assertNotNull(entry);
        entry = src.getEntry("com/redhat/ceylon/compiler/java/test/cmr/module/mixed/JavaClass.java");
        assertNotNull(entry);
        src.close();
}
}
