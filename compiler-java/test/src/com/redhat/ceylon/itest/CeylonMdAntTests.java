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
package com.redhat.ceylon.itest;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class CeylonMdAntTests extends AntBasedTests {

    public CeylonMdAntTests() throws Exception {
        super("test/src/com/redhat/ceylon/itest/ceylonmd-ant.xml");
    }
    
    @Test
    public void testFoo() throws Exception {
        AntResult result = ant("foo");
        Assert.assertEquals(0, result.getStatusCode());
        assertContains(result.getStdout(), "RESULT version: 1.0");
        assertContains(result.getStdout(), "RESULT name: com.example.foo");
    }
    
    @Test
    public void testBar() throws Exception {
        AntResult result = ant("bar");
        Assert.assertEquals(0, result.getStatusCode());
        assertContains(result.getStdout(), "RESULT version: 1.0");
        assertContains(result.getStdout(), "RESULT name: com.example.bar");
    }
    
    @Test
    public void testBad() throws Exception {
        AntResult result = ant("bad");
        Assert.assertEquals(0, result.getStatusCode());
        assertContains(result.getStdout(), "RESULT version: 1.0");
        assertContains(result.getStdout(), "RESULT name: com.example.bad");
    }
    
    @Test
    @Ignore("We don't want this to break when we change the sdk tests")
    public void testSdkTest() throws Exception {
        AntResult result = ant("sdk-test");
        Assert.assertEquals(0, result.getStatusCode());
        assertContains(result.getStdout(), "RESULT version: 0.1.1");
        assertContains(result.getStdout(), "RESULT name: com.redhat.ceylon.sdk.test");
    }

}
