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
package com.redhat.ceylon.ant;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;


public class ModuleDescriptorReaderTest {

    @Test
    public void testReadDescriptorA() {
        ModuleDescriptorReader r = new ModuleDescriptorReader("com.redhat.ceylon.ant.modules.a", new File("test/src"));
        Assert.assertEquals("1.0", r.getModuleVersion());
        Assert.assertEquals("com.redhat.ceylon.ant.modules.a", r.getModuleName());
        Assert.assertEquals("http://example.com/license", r.getModuleLicense());
        List<String> authors = r.getModuleAuthors();
        Assert.assertEquals(2, authors.size());
        Assert.assertEquals("Tom", authors.get(0));
        Assert.assertEquals("and Tom", authors.get(1));
    }
    
}
