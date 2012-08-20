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

public class CeyloncScriptTest extends AntBasedTest {

    public CeyloncScriptTest() throws Exception {
        super("test/src/com/redhat/ceylon/itest/ceylonc-script.xml");
    }
    
    private void assertExecutedOk(AntResult result) {
        assertNotContainsMatch(result.getStdout(), "\\[exec\\] Result: [0-9]+");
    }

    @Test
    public void testVersion() throws Exception {
        AntResult result = ant("version");
        Assert.assertEquals(0, result.getStatusCode());
        assertContainsMatch(result.getStdout(), "\\[exec\\] Version: ceylonc [0-9.]+");
    }
    
    @Test
    public void testHelp() throws Exception {
        AntResult result = ant("help");
        Assert.assertEquals(0, result.getStatusCode());
        assertContains(result.getStdout(), "[exec] Usage: ceylonc <options> <source files> <module names>");
    }
    
    @Test
    public void testH() throws Exception {
        AntResult result = ant("h");
        assertContains(result.getStdout(), "[exec] Usage: ceylonc <options> <source files> <module names>");
        assertContains(result.getStdout(), "[exec] use -help for a list of possible options");
    }
    
    @Test
    public void testJhelp() throws Exception {
        AntResult result = ant("jhelp");
        Assert.assertEquals(0, result.getStatusCode());
        assertExecutedOk(result);
    }
    
    @Test
    public void testFoo() throws Exception {
        AntResult result = ant("foo");
        Assert.assertEquals(0, result.getStatusCode());
        assertExecutedOk(result);
        assertNotContains(result.getStdout(), "[exec] Model tree for");
    }
    
    @Test
    public void testFooVerbose() throws Exception {
        AntResult result = ant("foo-verbose");
        Assert.assertEquals(0, result.getStatusCode());
        assertExecutedOk(result);
        assertContains(result.getStdout(), "[exec] Model tree for");
    }
    
}
