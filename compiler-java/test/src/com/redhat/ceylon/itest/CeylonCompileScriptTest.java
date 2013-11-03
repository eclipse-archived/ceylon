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
import org.junit.Test;

import com.redhat.ceylon.common.tools.CeylonTool;

public class CeylonCompileScriptTest extends AntBasedTest {

    public CeylonCompileScriptTest() throws Exception {
        super("test/src/com/redhat/ceylon/itest/ceylon-compile-script.xml");
    }
    
    private void assertExecutedOk(AntResult result) {
        assertNotContainsMatch(result.getStdout(), "\\[exec\\] Result: [0-9]+");
    }

    @Test
    public void testVersion() throws Exception {
        AntResult result = ant("version");
        Assert.assertEquals(1, result.getStatusCode());
        assertContains(result.getStdout(), "[exec] ceylon compile: ");
        assertContains(result.getStdout(), "exec returned: " + CeylonTool.SC_ARGS);
    }
    
    @Test
    public void testHelp() throws Exception {
        AntResult result = ant("help");
        Assert.assertEquals(CeylonTool.SC_OK, result.getStatusCode());
        assertContains(result.getStdout(), "[exec] DESCRIPTION");
    }
    
    @Test
    public void testH() throws Exception {
        AntResult result = ant("h");
        Assert.assertEquals(1, result.getStatusCode());
        assertContains(result.getStdout(), "[exec] ceylon compile: ");
        assertContains(result.getStdout(), "exec returned: " + CeylonTool.SC_ARGS);
        
    }
    
    @Test
    public void testJhelp() throws Exception {
        AntResult result = ant("jhelp");
        Assert.assertEquals(1, result.getStatusCode());
        assertContains(result.getStdout(), "[exec] ceylon compile: ");
        assertContains(result.getStdout(), "exec returned: " + CeylonTool.SC_ARGS);
    }
    
    @Test
    public void testFoo() throws Exception {
        AntResult result = ant("foo");
        Assert.assertEquals(CeylonTool.SC_OK, result.getStatusCode());
        assertExecutedOk(result);
        assertNotContains(result.getStdout(), "[exec] Model tree for");
    }
    
    @Test
    public void testFooVerbose() throws Exception {
        AntResult result = ant("foo-verbose");
        Assert.assertEquals(CeylonTool.SC_OK, result.getStatusCode());
        assertExecutedOk(result);
        assertContains(result.getStdout(), "[exec] Model tree for");
    }
    
}
