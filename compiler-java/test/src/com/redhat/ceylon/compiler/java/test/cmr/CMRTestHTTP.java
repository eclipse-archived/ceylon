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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.ceylon.common.config.Repositories;
import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.sun.net.httpserver.HttpServer;

public class CMRTestHTTP extends CompilerTest {

    class RequestCounter{
        int count;
        synchronized void add(){
            count++;
        }
        synchronized void check(int count){
            Assert.assertEquals(count, this.count);
        }
    }

    private static AtomicInteger PORT_NUM_ALLOCATOR;
    
    @BeforeClass
    public static void initPortAllocator() {
        PORT_NUM_ALLOCATOR = new AtomicInteger(18000);
    }
    
    @AfterClass
    public static void cleanupPortAllocator() {
        PORT_NUM_ALLOCATOR = null;
    }
    
    private int allocPortForTest() {
        return PORT_NUM_ALLOCATOR.getAndIncrement();
    }
    
    private String getRepoUrl(int allocatedPort) {
        return "http://localhost:"+allocatedPort+"/repo";
    }
    
    private HttpServer startServer(int port, File repo, boolean herd, RequestCounter rq) throws IOException{
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 1);
        server.createContext("/repo", new RepoFileHandler(repo.getPath(), herd, rq));
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
    public void testMdlHTTPRepos() throws IOException {
        RequestCounter rq = new RequestCounter();
        String moduleA = "com.redhat.ceylon.compiler.java.test.cmr.modules.depend.a";
        
        // Clean up any cached version
        File carFileInCache = getModuleArchive(moduleA, "6.6.6", cacheDir);
        if(carFileInCache.exists())
            carFileInCache.delete();

        // Compile the first module in its own repo 
        File repo = makeRepo();
        
        Boolean result = getCompilerTask(Arrays.asList("-out", repo.getPath()),
                "modules/depend/a/module.ceylon", "modules/depend/a/package.ceylon", "modules/depend/a/A.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);
        
        File carFile = getModuleArchive(moduleA, "6.6.6", repo.getPath());
        assertTrue(carFile.exists());

        final int port = allocPortForTest();
        final String repoAURL = getRepoUrl(port);
        
        // now serve the first repo over HTTP
        HttpServer server = startServer(port, repo, false, rq); 
        
        try{
            // then try to compile only one module (the other being loaded from its car) 
            result = getCompilerTask(Arrays.asList("-out", destDir, "-rep", repoAURL, "-verbose:cmr", "-cp", getClasspath()),
                    "modules/depend/b/module.ceylon", "modules/depend/b/package.ceylon", "modules/depend/b/a.ceylon", "modules/depend/b/B.ceylon").call();
            Assert.assertEquals(Boolean.TRUE, result);

        }finally{
            server.stop(1);
        }
        carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.depend.b", "6.6.6");
        assertTrue(carFile.exists());
        
        // make sure it cached the module in the cache repo
        assertTrue(carFileInCache.exists());
        
        // make sure it didn't cache it in the output repo
        carFile = getModuleArchive(moduleA, "6.6.6");
        assertFalse(carFile.exists());
        
        rq.check(6);
    }

    
    @Test
    public void testMdlHTTPOutputRepo() throws IOException{
        testMdlHTTPOutputRepo(true, 11);
        testMdlHTTPOutputRepo(false, 72);
    }
    
    private void testMdlHTTPOutputRepo(boolean herd, int requests) throws IOException{
        RequestCounter rq = new RequestCounter();
        
        // Compile the module in its own repo
        File repo = makeRepo();

        final int port = allocPortForTest();
        final String repoAURL = getRepoUrl(port);
        
        // now serve the first repo over HTTP
        HttpServer server = startServer(port, repo, herd, rq); 
        
        try{
            // then try to compile our module by outputting to HTTP 
            Boolean result = getCompilerTask(Arrays.asList("-out", repoAURL, "-verbose:cmr"), "modules/single/module.ceylon").call();
            Assert.assertEquals(Boolean.TRUE, result);

        }finally{
            server.stop(1);
        }
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6", repo.getPath());
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        // make sure it's not empty
        ZipEntry moduleClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/module_.class");
        assertNotNull(moduleClass);
        car.close();

        File srcFile = getSourceArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6", repo.getPath());
        assertTrue(srcFile.exists());
        
        rq.check(requests);
    }


    @Test
    public void testMdlHTTPMixedCompilation() throws IOException{
        testMdlHTTPMixedCompilation(false, 137);
        testMdlHTTPMixedCompilation(true, 24);
    }
    
    private void testMdlHTTPMixedCompilation(boolean herd, int requests) throws IOException{
        RequestCounter rq = new RequestCounter();
        // Compile the first module in its own repo 
        File repo = makeRepo();

        final int port = allocPortForTest();
        final String repoAURL = getRepoUrl(port);
        
        // now serve the first repo over HTTP
        HttpServer server = startServer(port, repo, herd, rq); 
        
        try{
            // then try to compile our module by outputting to HTTP 
            Boolean result = getCompilerTask(Arrays.asList("-out", repoAURL, "-verbose:cmr"), "modules/mixed/JavaClass.java").call();
            Assert.assertEquals(Boolean.TRUE, result);
            result = getCompilerTask(Arrays.asList("-out", repoAURL, "-verbose:cmr"), "modules/mixed/CeylonClass.ceylon").call();
            Assert.assertEquals(Boolean.TRUE, result);

        }finally{
            server.stop(1);
        }

        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.mixed", "6.6.6", repo.getPath());
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        // make sure it's not empty
        ZipEntry entry = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/mixed/module_.class");
        assertNotNull(entry);
        entry = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/mixed/CeylonClass.class");
        assertNotNull(entry);
        entry = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/mixed/JavaClass.class");
        assertNotNull(entry);
        car.close();

        File srcFile = getSourceArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.mixed", "6.6.6", repo.getPath());
        assertTrue(srcFile.exists());

        JarFile src = new JarFile(srcFile);

        // make sure it's not empty
        entry = src.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/mixed/module.ceylon");
        assertNotNull(entry);
        entry = src.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/mixed/CeylonClass.ceylon");
        assertNotNull(entry);
        entry = src.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/mixed/JavaClass.java");
        assertNotNull(entry);
        src.close();

        rq.check(requests);
    }
}
