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

import com.redhat.ceylon.itest.AntBasedTest.AntResult;

public class CeylonScriptTest extends AntBasedTest {

    public CeylonScriptTest() throws Exception {
        super("test-src/com/redhat/ceylon/itest/ceylon-script.xml");
    }
    
    private void assertExecutedOk(AntResult result) {
        Assert.assertEquals(0, result.getStatusCode());
        assertNotContainsMatch(result.getStdout(), "\\[exec\\] Result: [0-9]+");
    }

    @Test
    public void testVersion() throws Exception {
        AntResult result = ant("version");
        assertExecutedOk(result);
        assertContains(result.getStdout(), "java version \"1.6");
        // TODO Assert something about the ceylon version
    }
    
    @Test
    public void testHelp() throws Exception {
        AntResult result = ant("help");
        assertExecutedOk(result);
        // TODO Assert something about the ceylon help
    }
    
    @Test
    public void testH() throws Exception {
        AntResult result = ant("h");
        assertExecutedOk(result);
        // TODO Assert something about the ceylon help
    }
    
    
    @Test
    public void testExecHelloMethodCompiled() throws Exception {
        AntResult result = ant("exec-hello-compiled");
        Assert.assertEquals(0, result.getStatusCode());
        assertContains(result.getStdout(), "Hello, world");
    }
    
    @Test
    public void testExecGoodbyeClassCompiled() throws Exception {
        AntResult result = ant("exec-goodbye-compiled");
        Assert.assertEquals(0, result.getStatusCode());
        assertContains(result.getStdout(), "Goodbye cruel world");
    }
    
    @Test
    public void testExecFooModuleCompiled() throws Exception {
        AntResult result = ant("exec-foo-compiled");
        Assert.assertEquals(0, result.getStatusCode());
        assertContains(result.getStdout(), "Hello, world");
    }
    
    @Test
    public void testExecHelloMethodFromSource() throws Exception {
        AntResult result = ant("exec-hello-source");
        Assert.assertEquals(0, result.getStatusCode());
        assertContains(result.getStdout(), "Hello, world");
    }
    
    @Test
    public void testExecGoodbyeClassFromSource() throws Exception {
        AntResult result = ant("exec-goodbye-source");
        Assert.assertEquals(0, result.getStatusCode());
        assertContains(result.getStdout(), "Goodbye cruel world");
    }
    
    @Test
    public void testExecFooModuleFromSource() throws Exception {
        AntResult result = ant("exec-foo-compiled");
        Assert.assertEquals(0, result.getStatusCode());
        assertContains(result.getStdout(), "Hello, world");
    }
    
}
