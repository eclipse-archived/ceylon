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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTests;
import com.sun.net.httpserver.HttpServer;

public class CMRHTTPTests extends CompilerTests {

    enum TimeoutIn {
        None, Head, GetInitial, GetMiddle, PutInitial, PutMiddle;
    }
    
    class RequestCounter{
        volatile int count;
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
        return startServer(port, repo, herd, rq, TimeoutIn.None);
    }

    private HttpServer startServer(int port, File repo, boolean herd, RequestCounter rq, TimeoutIn timeoutIn) throws IOException{
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 1);
        server.createContext("/repo", new RepoFileHandler(repo.getPath(), herd, rq, timeoutIn));
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
            result = getCompilerTask(Arrays.asList("-out", destDir, "-rep", repoAURL, "-verbose:cmr", "-cp", getClassPathAsPath()),
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
        
        rq.check(37);
    }

    @Test
    public void testMdlHTTPTimeout() throws IOException {
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
        String[] files = new String[]{
                "modules/depend/b/module.ceylon", 
                "modules/depend/b/package.ceylon", 
                "modules/depend/b/a.ceylon", 
                "modules/depend/b/B.ceylon"
        };
        List<String> options = Arrays.asList("-out", destDir, "-rep", repoAURL, "-verbose:cmr", 
                "-cp", getClassPathAsPath(), "-timeout", "1");
        
        // now serve the first repo over HTTP
        HttpServer server = startServer(port, repo, false, null, TimeoutIn.GetMiddle); 
        
        try{
            // then try to compile only one module (the other being loaded from its car) 
            assertErrors(files, options, null,
                         new CompilerError(24, "cannot find module artifact com.redhat.ceylon.compiler.java.test.cmr.modules.depend.a-6.6.6.car\n"+
                                 "  due to connection error: java.net.SocketTimeoutException: Timed out reading com.redhat.ceylon.compiler.java.test.cmr.modules.depend.a-6.6.6.car from "+repoAURL+"\n"+
                                 "  \t- dependency tree: com.redhat.ceylon.compiler.java.test.cmr.modules.depend.b/6.6.6 -> com.redhat.ceylon.compiler.java.test.cmr.modules.depend.a/6.6.6"));

        }finally{
            server.stop(1);
        }
        if(carFileInCache.exists())
            carFileInCache.delete();

        server = startServer(port, repo, false, null, TimeoutIn.GetInitial); 
        
        try{
            // then try to compile only one module (the other being loaded from its car) 
            assertErrors(files, options, null,
                         new CompilerError(24, "cannot find module artifact com.redhat.ceylon.compiler.java.test.cmr.modules.depend.a-6.6.6.car\n"+
                                 "  due to connection error: java.net.SocketTimeoutException: Timed out during connection to "+repoAURL+"/com/redhat/ceylon/compiler/java/test/cmr/modules/depend/a/6.6.6/com.redhat.ceylon.compiler.java.test.cmr.modules.depend.a-6.6.6.car\n"+
                                 "  \t- dependency tree: com.redhat.ceylon.compiler.java.test.cmr.modules.depend.b/6.6.6 -> com.redhat.ceylon.compiler.java.test.cmr.modules.depend.a/6.6.6"));

        }finally{
            server.stop(1);
        }
        if(carFileInCache.exists())
            carFileInCache.delete();

        server = startServer(port, repo, false, null, TimeoutIn.Head); 
        
        try{
            // then try to compile only one module (the other being loaded from its car) 
            assertErrors(files, options, null,
                         new CompilerError(24, "cannot find module artifact com.redhat.ceylon.compiler.java.test.cmr.modules.depend.a-6.6.6(.car|.jar)\n"+
                                 "  \t- dependency tree: com.redhat.ceylon.compiler.java.test.cmr.modules.depend.b/6.6.6 -> com.redhat.ceylon.compiler.java.test.cmr.modules.depend.a/6.6.6"));

        }finally{
            server.stop(1);
        }
        
        // now with put
        server = startServer(port, repo, true, null, TimeoutIn.PutInitial); 

        try{
            // then try to compile our module by outputting to HTTP 
            assertErrors("modules/single/module", Arrays.asList("-out", repoAURL, "-verbose:cmr", "-timeout", "1"), null,
                    new CompilerError(-1, "Failed to write module to repository: com.redhat.ceylon.cmr.impl.CMRException: java.net.SocketTimeoutException: Timed out writing to "+repoAURL+"/com/redhat/ceylon/compiler/java/test/cmr/modules/single/6.6.6/com.redhat.ceylon.compiler.java.test.cmr.modules.single-6.6.6.src"));

        }finally{
            server.stop(1);
        }

        // now with put
        server = startServer(port, repo, true, null, TimeoutIn.PutMiddle); 

        try{
            // then try to compile our module by outputting to HTTP 
            assertErrors("modules/single/module", Arrays.asList("-out", repoAURL, "-verbose:cmr", "-timeout", "1"), null,
                    new CompilerError(-1, "Failed to write module to repository: com.redhat.ceylon.cmr.impl.CMRException: java.net.SocketTimeoutException: Timed out writing to "+repoAURL+"/com/redhat/ceylon/compiler/java/test/cmr/modules/single/6.6.6/com.redhat.ceylon.compiler.java.test.cmr.modules.single-6.6.6.src"));

        }finally{
            server.stop(1);
        }
    }

    
    @Test
    public void testMdlHTTPOutputRepo() throws IOException{
        testMdlHTTPOutputRepo(true, 42);
        testMdlHTTPOutputRepo(false, 103);
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
        ZipEntry moduleClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/single/$module_.class");
        assertNotNull(moduleClass);
        car.close();

        File srcFile = getSourceArchive("com.redhat.ceylon.compiler.java.test.cmr.modules.single", "6.6.6", repo.getPath());
        assertTrue(srcFile.exists());
        
        rq.check(requests);
    }


    @Test
    public void testMdlHTTPMixedCompilation() throws IOException{
        testMdlHTTPMixedCompilation(false, 199);
        testMdlHTTPMixedCompilation(true, 86);
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
        ZipEntry entry = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/modules/mixed/$module_.class");
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
