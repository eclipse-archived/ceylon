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
package com.redhat.ceylon.tools.new_;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.tools.new_.Environment;
import com.redhat.ceylon.tools.new_.Template;

public class TemplateTest {

    Environment env(String... keyVals) {
        if (keyVals.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        Environment result = new Environment();
        for (int ii = 0; ii < keyVals.length; ii+=2) {
            result.put(keyVals[ii], keyVals[ii+1]);
        }
        return result;
    }
    
    @Test
    public void testSimple() {
        Template t = new Template("This is a test @[foo]\n@[bar]");
        Assert.assertEquals("This is a test FOO\nBAR", t.eval(env(
                "foo", "FOO", 
                "bar", "BAR")));
    }
    
    @Test
    public void testJustPlaceholder() {
        Template t = new Template("@[foo]");
        Assert.assertEquals("FOO", t.eval(env(
                "foo", "FOO")));
    }
    
    @Test
    public void testSimpleMissing() {
        Template t = new Template("This is a test @[foo]\n@[bar]");
        try {
            t.eval(env(
                    "foo", "FOO"));
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("No replacement for bar at line 2", e.getMessage());
        }
        t = new Template("This is a test @[foo]\n\r\n@[bar]");
        try {
            t.eval(env(
                    "foo", "FOO"));
            Assert.fail();
        } catch (RuntimeException e) {
            Assert.assertEquals("No replacement for bar at line 3", e.getMessage());
        }
    }
}
