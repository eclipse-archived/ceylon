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
package org.eclipse.ceylon.tools;

import static org.eclipse.ceylon.common.ModuleSpec.DEFAULT_MODULE;
import static org.eclipse.ceylon.common.ModuleSpec.parse;

import org.eclipse.ceylon.common.ModuleSpec.Option;
import org.junit.Assert;
import org.junit.Test;

public class ModuleSpecTest {

    @Test
    public void nameOnly() {
        parse("org.example");
        parse("org.example", Option.VERSION_PROHIBITED);
        try {
            parse("org.example", Option.VERSION_REQUIRED);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        parse("org.example/");
        
        try {
            parse("org.example/", Option.VERSION_PROHIBITED);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            parse("org.example/", Option.VERSION_REQUIRED);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void nameAndVersion() {
        parse("org.example/1.0");
        parse("org.example/1.0", Option.VERSION_REQUIRED);
        
        try {
            parse("org.example/1.0", Option.VERSION_PROHIBITED);    
            Assert.fail();
        } catch (IllegalArgumentException e) {}   
    }
    
    @Test
    public void missingName() {
        try {
            parse("/org.example");
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            parse("/org.example", Option.VERSION_PROHIBITED);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
        
        try {
            parse("/org.example", Option.VERSION_REQUIRED);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void defaultModule() {
        DEFAULT_MODULE.equals(parse("DEFAULT"));
        DEFAULT_MODULE.equals(parse("DEFAULT", Option.VERSION_PROHIBITED));
        DEFAULT_MODULE.equals(parse("DEFAULT", Option.VERSION_REQUIRED));
        try {
            parse("DEFAULT", Option.DEFAULT_MODULE_PROHIBITED);
            Assert.fail();
        } catch (IllegalArgumentException e) {}
    }
    
}
