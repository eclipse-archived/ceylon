package com.redhat.ceylon.compiler.java.test.cmr;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.junit.Test;

import junit.framework.Assert;

import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.sun.net.httpserver.HttpServer;

public class CMRTestHTTPOutputWithJava extends CompilerTest {
    
    @Test
    public void testMdlHTTPOutputRepo() throws IOException{
        int port = 18002;

        // Compile the first module in its own repo 
        File repoA = new File("build/test-cars-http3");
        cleanCars(repoA.getPath());
        repoA.mkdirs();

        // now serve the first repo over HTTP
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 1);
        server.createContext("/repo", new RepoFileHandler(repoA.getPath()));
        server.setExecutor(null); // creates a default executor
        server.start();
        
        String repoAURL = "http://localhost:"+port+"/repo";
        
        try{
            // then try to compile our module by outputting to HTTP 
            Boolean result = getCompilerTask(Arrays.asList("-out", repoAURL), "module/mixed/JavaClass.java").call();
            Assert.assertEquals(Boolean.TRUE, result);
            result = getCompilerTask(Arrays.asList("-out", repoAURL), "module/mixed/CeylonClass.ceylon").call();
            Assert.assertEquals(Boolean.TRUE, result);

        }finally{
            server.stop(0);
        }
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.module.mixed", "6.6.6", repoA.getPath());
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

        File srcFile = getSourceArchive("com.redhat.ceylon.compiler.java.test.cmr.module.mixed", "6.6.6", repoA.getPath());
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