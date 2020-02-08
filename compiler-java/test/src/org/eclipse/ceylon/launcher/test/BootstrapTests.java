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
package org.eclipse.ceylon.launcher.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.ceylon.common.Constants;
import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.eclipse.ceylon.compiler.java.test.RunSingleThreaded;
import org.eclipse.ceylon.launcher.Bootstrap;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@RunSingleThreaded
public class BootstrapTests {

    private static File distZip;
    
    @BeforeClass
    public static void checkPreConditions() {
        Assume.assumeTrue(CompilerTests.allowNetworkTests());
    }
    
    /**
     * Make the zip file for the server to serve
     * @throws IOException
     */
    @BeforeClass
    public static void makeZip() throws IOException {
        distZip = zip(new byte[8*1024]);
    }
    
    @AfterClass
    public static void deleteZip() {
        delete(distZip);
    }
    
    private static File zip(byte[] buf) throws IOException, FileNotFoundException {
        File tmp = File.createTempFile("ceylon-dist", ".zip");
        tmp.deleteOnExit();
        try (ZipOutputStream zf = new ZipOutputStream(new FileOutputStream(tmp))) {
            File root = new File("../dist/dist");
            zip(buf, zf, root, root);
        }
        return tmp;
    }

    private static void zip(byte[] buf, ZipOutputStream zf, File root, File f) throws IOException, FileNotFoundException {
        if (f.getAbsolutePath().equals(root.getAbsolutePath())) {
            for (File x : f.listFiles()) {
                zip(buf, zf, root, x);
            }
            return;
        }
        String entryName = "ceylon-1.x.x/"+f.getAbsolutePath().substring(root.getAbsolutePath().length()+1);
        if (f.isDirectory() && !f.getName().endsWith("/")) {
            entryName = entryName + "/";
        }
        //System.err.println(entryName);
        ZipEntry ze = new ZipEntry(entryName);
        ze.setTime(f.lastModified());
        zf.putNextEntry(ze);
        if (f.isDirectory()) {
            zf.closeEntry();
            for (File x : f.listFiles()) {
                zip(buf, zf, root, x);
            }
        } else {
            try (FileInputStream in = new FileInputStream(f)) {
                int read;
                while ((read = in.read(buf)) != -1) {
                    zf.write(buf, 0, read);
                }
            }
        }
    }
    
    private static void delete(File tmp) {
        if (tmp.isDirectory()) {
            for (File f : tmp.listFiles()) {
                delete(f);
            }
        }
        tmp.delete();
    }
    
    private class BootstrapHttpHandler implements HttpHandler {
        public final int[] handlerInvoked;

        private BootstrapHttpHandler() {
            this.handlerInvoked = new int[]{0};
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            handlerInvoked[0]++; 
            System.err.println(exchange.getRequestMethod()+" " + exchange.getRequestURI().getPath());
            for (Map.Entry<String, List<String>> s : exchange.getRequestHeaders().entrySet()) {
                for (String h : s.getValue()) {
                    System.err.println(s.getKey()+": " + h);
                }
            }
            Assert.assertEquals("GET", exchange.getRequestMethod());
            Assert.assertEquals("/blah/ceylon-test.zip", exchange.getRequestURI().getPath());
            
            byte[] buf = new byte[8*1024];
            long bytesWritten = 0;
            
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, distZip.length());
            OutputStream response = exchange.getResponseBody();
            try (FileInputStream in = new FileInputStream(distZip)) {
                int read;
                while ((read = in.read(buf)) != -1) {
                    preWrite(bytesWritten);
                    response.write(buf, 0, read);
                    bytesWritten+=read;
                }
            }
            response.close();
            exchange.close();
        }
        
        protected void preWrite(long bytesWritten) {
            // subclasses may override this to break things 
            // part way through the download
        }
    }

    /**
     * Subclass of {@link Bootstrap} which uses our a HTTP server we 
     * start for the test.
     */
    static class BootstrapTester extends Bootstrap {
        private final Config config;
        public BootstrapTester(InetSocketAddress address) {
            config = new Config();
            try {
                File f = Files.createTempDirectory("BootstrapTest.distDir").toFile();
                delete(f);
                config.distributionDir = f;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            
            try {
                config.distribution = new URI("http:/"+ address + "/blah/ceylon-test.zip");
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            
            try {
                File f = Files.createTempDirectory("BootstrapTest.resolvedInstallation").toFile();
                delete(f);
                config.resolvedInstallation = f;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        protected void setupDistHome(Config cfg) throws Exception {
            super.setupDistHome(cfg);
            // Launcher does that, so it effectively caches previous tests' repos unless we reset it to the
            // current test repo
            System.setProperty(Constants.PROP_CEYLON_SYSTEM_REPO, new File(cfg.distributionDir, "repo").getAbsolutePath());
        }

        @Override
        protected boolean isDistBootstrap() throws URISyntaxException {
            return true;
        }
        
        @Override
        protected Config loadBootstrapConfig() {
            return config;
        }
        
        @Override
        protected int getConnectTimeout() {
            return 2000;
        }
        
        @Override
        protected int getReadTimeout() {
            return 2000;
        }
        
        public void deleteTmpFiles() {
            delete(config.distributionDir);
            delete(config.resolvedInstallation);
        }
    }
    
    private HttpServer startServer(HttpHandler handler) throws IOException{
        InetSocketAddress addr = new InetSocketAddress(InetAddress.getLoopbackAddress(), 0);
        HttpServer server = HttpServer.create(addr, 1);
        server.createContext("/blah", handler);
        // make sure we serve at least two concurrent connections, as each one might take a few ms to close
        ThreadPoolExecutor tpool = (ThreadPoolExecutor)Executors.newFixedThreadPool(2);
        server.setExecutor(tpool);
        server.start();
        return server;
    }
    
    /** Test the download when everything works OK */
    @Test
    public void testBootstrapDownloadOk() throws Throwable {
        Assert.assertTrue(distZip.exists());
        BootstrapHttpHandler reliableHandler = new BootstrapHttpHandler();
        HttpServer server = startServer(reliableHandler);
        BootstrapTester bootstrap = null;
        try {
            bootstrap = new BootstrapTester(server.getAddress());
            Assert.assertEquals(0, bootstrap.runInternal(""));
        } finally {
            server.stop(1);
            if (bootstrap != null) {
                bootstrap.deleteTmpFiles();
            }
        }
        
        // Should just be called once
        Assert.assertEquals(1, reliableHandler.handlerInvoked[0]);
    }
    
    /** Test the download when the first connection to the server experiences 
     * a read timeout from the client side after 0 bytes served */
    @Test
    public void testBootstrapDownloadReadTimeoutInitial() throws Throwable {
        Assert.assertTrue(distZip.exists());
        BootstrapHttpHandler reliableHandler = new BootstrapHttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (handlerInvoked[0] == 0) {
                    handlerInvoked[0]++; 
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    super.handle(exchange);
                } else {
                    super.handle(exchange);
                }
            }
        };
        HttpServer server = startServer(reliableHandler);
        BootstrapTester bootstrap = null;
        try {
            bootstrap = new BootstrapTester(server.getAddress());
            Assert.assertEquals(0, bootstrap.runInternal(""));
        } finally {
            server.stop(1);
            if (bootstrap != null) {
                bootstrap.deleteTmpFiles();
            }
        }
        // First connection should've read timed-out
        Assert.assertEquals(2, reliableHandler.handlerInvoked[0]);
    }
    
    @Test
    public void testBootstrapDownloadReadTimeoutLater() throws Throwable {
        Assert.assertTrue(distZip.exists());
        BootstrapHttpHandler reliableHandler = new BootstrapHttpHandler() {
            boolean slept = false;
            protected void preWrite(long bytesWritten) {
                if (!slept && 
                        bytesWritten > 8*1024) {
                    slept = true;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
        HttpServer server = startServer(reliableHandler);
        BootstrapTester bootstrap = null;
        try {
            bootstrap = new BootstrapTester(server.getAddress());
            Assert.assertEquals(0, bootstrap.runInternal(""));
        } finally {
            server.stop(1);
            if (bootstrap != null) {
                bootstrap.deleteTmpFiles();
            }
        }
        // First connection should've read timed-out
        Assert.assertEquals(2, reliableHandler.handlerInvoked[0]);
    }
    
    /** 
     * Test the download when the first connection to the server experiences 
     * serves some data, then closes the connection */
    @Test
    public void testBootstrapDownloadCloseConnection() throws Throwable {
        Assert.assertTrue(distZip.exists());
        BootstrapHttpHandler reliableHandler = new BootstrapHttpHandler() {
            boolean thrown = false;
            
            protected void preWrite(long bytesWritten) {
                if (!thrown && 
                        bytesWritten > 8*1024) {
                    thrown = true;
                    throw new RuntimeException();
                }
            }
        };
        HttpServer server = startServer(reliableHandler);
        BootstrapTester bootstrap = null;
        try {
            bootstrap = new BootstrapTester(server.getAddress());
            Assert.assertEquals(0, bootstrap.runInternal(""));
        } finally {
            server.stop(1);
            if (bootstrap != null) {
                bootstrap.deleteTmpFiles();
            }
        }
        // First connection should've read timed-out
        Assert.assertEquals(2, reliableHandler.handlerInvoked[0]);
    }
}
