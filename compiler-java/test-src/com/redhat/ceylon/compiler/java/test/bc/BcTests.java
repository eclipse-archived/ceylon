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
package com.redhat.ceylon.compiler.java.test.bc;

import java.io.File;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class BcTests extends CompilerTest {
    
    private final String providerModuleSrc = "provider/module.ceylon";
    private final String providerPackageSrc = "provider/package.ceylon";
    private final String clientModuleSrc = "client/module.ceylon";
    private final String clientModuleName = "com.redhat.ceylon.compiler.java.test.bc.client";
    private final String providerModuleName = "com.redhat.ceylon.compiler.java.test.bc.provider";
    
    /**
     * Checks that we can still compile a client after a change
     * @param ceylon
     */
    protected void source(String ceylon) {
        String providerPreSrc = "provider/" + ceylon + "_pre.ceylon";
        String providerPostSrc = "provider/" + ceylon + "_post.ceylon";
        String clientSrc = "client/" + ceylon + "_client.ceylon";
        
        // Compile provider
        compile(providerPreSrc,
                providerModuleSrc, providerPackageSrc);
        
        // Compile client
        compile(clientSrc,
                clientModuleSrc);
        
        // New version of provider
        compile(providerPostSrc,
                providerModuleSrc, providerPackageSrc);
        
        // Check the client still compilers
        compile(clientSrc,
                clientModuleSrc);
    }
    
    /**
     * Checks that we can still link an existing client after a change
     * @param ceylon
     */
    protected void binary(String main, String ceylon) {
        String providerPreSrc = "provider/" + ceylon + "_pre.ceylon";
        String providerPostSrc = "provider/" + ceylon + "_post.ceylon";
        String clientSrc = "client/" + ceylon + "_client.ceylon";
        
        // Compile provider
        compile(providerPreSrc,
                providerModuleSrc, providerPackageSrc);

        // Compile client
        compile(clientSrc,
                clientModuleSrc);
        
        // New version of provider
        compile(providerPostSrc,
                providerModuleSrc, providerPackageSrc);
        
        // Now try running the client
        File clientCar = getModuleArchive(clientModuleName, "0.1");
        File providerCar = getModuleArchive(providerModuleName, "0.1");
        run(clientModuleName + "." + main, 
                clientCar, providerCar);
    }
    
    @Test
    public void testClassMethAddDefaultedParam() {
        source("ClassMethAddDefaultedParam");
        binary("classMethAddDefaultedParam",
                "ClassMethAddDefaultedParam");
    }
    
    @Test
    public void testFunctionAddDefaultedParam() {
        source("FunctionAddDefaultedParam");
        binary("functionAddDefaultedParam_client",
                "FunctionAddDefaultedParam");
    }
    
    @Test
    public void testClassInitAddDefaultedParam() {
        source("ClassInitAddDefaultedParam");
        binary("classInitAddDefaultedParam",
                "ClassInitAddDefaultedParam");
    }

}
