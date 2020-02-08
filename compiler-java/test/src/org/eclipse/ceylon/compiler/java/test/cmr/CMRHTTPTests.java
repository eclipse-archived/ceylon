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
package org.eclipse.ceylon.compiler.java.test.cmr;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.eclipse.ceylon.compiler.java.launcher.Main.ExitState;
import org.eclipse.ceylon.compiler.java.test.CompilerError;
import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.eclipse.ceylon.compiler.java.test.ErrorCollector;
import org.eclipse.ceylon.compiler.java.tools.CeyloncTaskImpl;
import org.eclipse.ceylon.javax.tools.Diagnostic;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;

import com.sun.net.httpserver.HttpServer;

public class CMRHTTPTests extends CompilerTests {

    interface ExpectedError {}
    
    enum TimeoutIn implements ExpectedError {
        None, Head, GetInitial, GetMiddle, PutInitial, PutMiddle;
    }

    enum HttpError implements ExpectedError {
        FORBIDDEN;
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
    public static void checkPreConditions() {
        Assume.assumeTrue(allowNetworkTests());
    }
    
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

    private HttpServer startServer(int port, File repo, boolean herd, RequestCounter rq, ExpectedError error) throws IOException{
        HttpServer server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), 1);
        server.createContext("/repo", new RepoFileHandler(repo.getPath(), herd, rq, error));
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
        String moduleA = "org.eclipse.ceylon.compiler.java.test.cmr.modules.depend.a";
        
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
        carFile = getModuleArchive("org.eclipse.ceylon.compiler.java.test.cmr.modules.depend.b", "6.6.6");
        assertTrue(carFile.exists());
        
        // make sure it cached the module in the cache repo
        assertTrue(carFileInCache.exists());
        
        // make sure it didn't cache it in the output repo
        carFile = getModuleArchive(moduleA, "6.6.6");
        assertFalse(carFile.exists());
        
        rq.check(5);
    }

    @Test
    public void testMdlHTTPTimeout() throws IOException {
        String moduleA = "org.eclipse.ceylon.compiler.java.test.cmr.modules.depend.a";
        
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
            assertTimeout(files, options);

        }finally{
            server.stop(1);
        }
        if(carFileInCache.exists())
            carFileInCache.delete();

        server = startServer(port, repo, false, null, TimeoutIn.GetInitial); 
        
        try{
            // then try to compile only one module (the other being loaded from its car) 
            assertTimeout(files, options);

        }finally{
            server.stop(1);
        }
        if(carFileInCache.exists())
            carFileInCache.delete();

        server = startServer(port, repo, false, null, TimeoutIn.Head); 
        
        try{
            // then try to compile only one module (the other being loaded from its car) 
            assertTimeout(files, options);

        }finally{
            server.stop(1);
        }
        
        // now with put
        server = startServer(port, repo, true, null, TimeoutIn.PutInitial); 

        try{
            // then try to compile our module by outputting to HTTP 
            assertTimeout(new String[] {"modules/single/module.ceylon"}, Arrays.asList("-out", repoAURL, "-verbose:cmr", "-timeout", "1"));

        }finally{
            server.stop(1);
        }

        // now with put
        server = startServer(port, repo, true, null, TimeoutIn.PutMiddle); 

        try{
            // then try to compile our module by outputting to HTTP 
            assertTimeout(new String[] {"modules/single/module.ceylon"}, Arrays.asList("-out", repoAURL, "-verbose:cmr", "-timeout", "1"));

        }finally{
            server.stop(1);
        }
    }

    private void assertTimeout(String[] files, List<String> options) {
        ErrorCollector collector = compileErrorTest(files, options, null, false, true);
        Set<CompilerError> errors = collector.get(Diagnostic.Kind.ERROR);
        Assert.assertTrue("Expecting a single error, got " + errors.size(), errors.size() == 1);
        CompilerError err = errors.iterator().next();
        Assert.assertTrue("Expecting a java.net.SocketTimeoutException, got " + err, err.toString().contains("java.net.SocketTimeoutException"));
    }
    
    @Test
    public void testMdlHTTPOutputRepo() throws IOException{
        testMdlHTTPOutputRepo(true, 9);
        testMdlHTTPOutputRepo(false, 70);
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
        
        File carFile = getModuleArchive("org.eclipse.ceylon.compiler.java.test.cmr.modules.single", "6.6.6", repo.getPath());
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        // make sure it's not empty
        ZipEntry moduleClass = car.getEntry("org/eclipse/ceylon/compiler/java/test/cmr/modules/single/$module_.class");
        assertNotNull(moduleClass);
        car.close();

        File srcFile = getSourceArchive("org.eclipse.ceylon.compiler.java.test.cmr.modules.single", "6.6.6", repo.getPath());
        assertTrue(srcFile.exists());
        
        rq.check(requests);
    }


    @Ignore("Temporarily disabling for work on 1.3.4; fails sometimes with 132!=133, perhaps only on clean builds?")
    @Test
    public void testMdlHTTPMixedCompilation() throws IOException{
        testMdlHTTPMixedCompilation(false, 133);
        testMdlHTTPMixedCompilation(true, 20);
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

        File carFile = getModuleArchive("org.eclipse.ceylon.compiler.java.test.cmr.modules.mixed", "6.6.6", repo.getPath());
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        // make sure it's not empty
        ZipEntry entry = car.getEntry("org/eclipse/ceylon/compiler/java/test/cmr/modules/mixed/$module_.class");
        assertNotNull(entry);
        entry = car.getEntry("org/eclipse/ceylon/compiler/java/test/cmr/modules/mixed/CeylonClass.class");
        assertNotNull(entry);
        entry = car.getEntry("org/eclipse/ceylon/compiler/java/test/cmr/modules/mixed/JavaClass.class");
        assertNotNull(entry);
        car.close();

        File srcFile = getSourceArchive("org.eclipse.ceylon.compiler.java.test.cmr.modules.mixed", "6.6.6", repo.getPath());
        assertTrue(srcFile.exists());

        JarFile src = new JarFile(srcFile);

        // make sure it's not empty
        entry = src.getEntry("org/eclipse/ceylon/compiler/java/test/cmr/modules/mixed/module.ceylon");
        assertNotNull(entry);
        entry = src.getEntry("org/eclipse/ceylon/compiler/java/test/cmr/modules/mixed/CeylonClass.ceylon");
        assertNotNull(entry);
        entry = src.getEntry("org/eclipse/ceylon/compiler/java/test/cmr/modules/mixed/JavaClass.java");
        assertNotNull(entry);
        src.close();

        rq.check(requests);
    }

    @Test
    public void testMdlHTTPForbidden() throws IOException {
        File repo = makeRepo();
        
        final int port = allocPortForTest();
        final String repoAURL = getRepoUrl(port);
        HttpServer server = startServer(port, repo, true, null, HttpError.FORBIDDEN); 
        try{
            // then try to compile our module by outputting to HTTP 
            assertErrors("modules/single/module", Arrays.asList("-out", repoAURL), null, 
                    new CompilerError(-1, "Failed to write module to repository: authentication failed on repository "+repoAURL));
        }finally{
            server.stop(1);
        }
    }
}
