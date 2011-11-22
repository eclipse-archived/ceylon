/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.test.ceylon;

import org.junit.Assert;

import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public class Test {
    public void fail(){
        Assert.fail();
    }
    public void assertEquals(@TypeInfo("ceylon.language.Object") Object reference, 
            @TypeInfo("ceylon.language.Object") Object value){
        if(reference == null){
            if(value == null)
                return;
            Assert.fail("Expected "+reference+" but got null");
        }
        if(value == null)
            Assert.fail("Expected null, got "+value);
        if(!reference.equals(value))
            Assert.fail("Expected "+reference+", got "+value);
    }
    public void assertTrue(boolean condition){
        Assert.assertTrue(condition);
    }
    public void assertFalse(boolean condition){
        Assert.assertFalse(condition);
    }
}
