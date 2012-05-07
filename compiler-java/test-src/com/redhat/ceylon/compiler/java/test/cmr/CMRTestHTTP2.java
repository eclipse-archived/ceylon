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

public class CMRTestHTTP2 extends CompilerTest {
    
    @Test
    public void testMdlHTTPOutputRepo() throws IOException{
        int port = 18001;

        // Compile the first module in its own repo 
        File repoA = new File("build/test-cars-http2");
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
            Boolean result = getCompilerTask(Arrays.asList("-out", repoAURL), "module/single/module.ceylon").call();
            Assert.assertEquals(Boolean.TRUE, result);

        }finally{
            server.stop(0);
        }
        
        File carFile = getModuleArchive("com.redhat.ceylon.compiler.java.test.cmr.module.single", "6.6.6", repoA.getPath());
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        // make sure it's not empty
        ZipEntry moduleClass = car.getEntry("com/redhat/ceylon/compiler/java/test/cmr/module/single/module.class");
        assertNotNull(moduleClass);
        car.close();

        File srcFile = getSourceArchive("com.redhat.ceylon.compiler.java.test.cmr.module.single", "6.6.6", repoA.getPath());
        assertTrue(srcFile.exists());
    }

}