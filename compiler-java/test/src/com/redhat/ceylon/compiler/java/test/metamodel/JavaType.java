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

package com.redhat.ceylon.compiler.java.test.metamodel;

import junit.framework.AssertionFailedError;

public class JavaType {
    
    public JavaType(boolean bo, byte by, short s, int i, long l, float f, double d, char c, String str, Object o){
        assertt(bo == true);
        assertt(by == 1);
        assertt(s == 2);
        assertt(i == 3);
        assertt(l == 4);
        assertt(f == 1.0);
        assertt(d == 2.0);
        assertt(c == 'a');
        assertt(str.equals("a"));
        assertt(o instanceof ceylon.language.String && o.toString().equals("b"));
    }

    public boolean getBoolean(){ return true; }
    public byte getByte(){ return 1; }
    public short getShort(){ return 2; }
    public int getInt(){ return 3; }
    public long getLong(){ return 4; }
    public float getFloat(){ return (float) 1.0; }
    public double getDouble(){ return 2.0; }
    public char getChar(){ return 'a'; }
    public String getStr(){ return "a"; }
    public Object getObject(){ return ceylon.language.String.instance("b"); }
    
    static void assertt(boolean test){
        if(!test)
            throw new AssertionFailedError();
    }
    
    @Override
    public String toString() {
        return "JavaType is there";
    }
}
