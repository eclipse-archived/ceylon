package com.redhat.ceylon.compiler.java.test.runtime;

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
public class VariadicJava {
    private void assertEquals(Object expected, Object test) {
        if(!expected.equals(test))
            fail("Got "+test+" instead of "+expected);
    }

    private void assertEquals(float expected, float test) {
        if(expected != test)
            fail("Got "+test+" instead of "+expected);
    }

    private void assertEquals(double expected, double test) {
        if(expected != test)
            fail("Got "+test+" instead of "+expected);
    }

    private void fail(String string) {
        throw new RuntimeException(string);
    }

    public void testVarargsByte0(byte... params){
        assertEquals(0, params.length);
    }
    
    public void testVarargsByte3(byte... params){
        assertEquals(3, params.length);
        assertEquals((byte)1, params[0]);
        assertEquals((byte)2, params[1]);
        assertEquals((byte)3, params[2]);
    }

    public void testVarargsShort0(short... params){
        assertEquals(0, params.length);
    }
    
    public void testVarargsShort3(short... params){
        assertEquals(3, params.length);
        assertEquals((short)1, params[0]);
        assertEquals((short)2, params[1]);
        assertEquals((short)3, params[2]);
    }

    public void testVarargsInt0(int... params){
        assertEquals(0, params.length);
    }
    
    public void testVarargsInt3(int... params){
        assertEquals(3, params.length);
        assertEquals(1, params[0]);
        assertEquals(2, params[1]);
        assertEquals(3, params[2]);
    }

    public void testVarargsLong0(long... params){
        assertEquals(0, params.length);
    }
    
    public void testVarargsLong3(long... params){
        assertEquals(3, params.length);
        assertEquals(1l, params[0]);
        assertEquals(2l, params[1]);
        assertEquals(3l, params[2]);
    }

    public void testVarargsFloat0(float... params){
        assertEquals(0, params.length);
    }
    
    public void testVarargsFloat3(float... params){
        assertEquals(3, params.length);
        assertEquals(1.0, params[0]);
        assertEquals(2.0, params[1]);
        assertEquals(3.0, params[2]);
    }

    public void testVarargsDouble0(double... params){
        assertEquals(0, params.length);
    }
    
    public void testVarargsDouble3(double... params){
        assertEquals(3, params.length);
        assertEquals(1.0d, params[0]);
        assertEquals(2.0d, params[1]);
        assertEquals(3.0d, params[2]);
    }

    public void testVarargsBoolean0(boolean... params){
        assertEquals(0, params.length);
    }
    
    public void testVarargsBoolean3(boolean... params){
        assertEquals(3, params.length);
        assertEquals(true, params[0]);
        assertEquals(false, params[1]);
        assertEquals(false, params[2]);
    }

    public void testVarargsChar0(char... params){
        assertEquals(0, params.length);
    }
    
    public void testVarargsChar3(char... params){
        assertEquals(3, params.length);
        assertEquals('a', params[0]);
        assertEquals('b', params[1]);
        assertEquals('c', params[2]);
    }

    public void testVarargsObject0(java.lang.Object... params){
        assertEquals(0, params.length);
    }
    
    public void testVarargsObject3(java.lang.Object... params){
        assertEquals(3, params.length);
        assertEquals(ceylon.language.Integer.instance(1), params[0]);
        assertEquals(ceylon.language.Integer.instance(2), params[1]);
        assertEquals(ceylon.language.Integer.instance(3), params[2]);
    }

    public void testVarargsCeylonInteger0(ceylon.language.Integer... params){
        assertEquals(0, params.length);
    }
    
    public void testVarargsCeylonInteger3(ceylon.language.Integer... params){
        assertEquals(3, params.length);
        assertEquals(ceylon.language.Integer.instance(1), params[0]);
        assertEquals(ceylon.language.Integer.instance(2), params[1]);
        assertEquals(ceylon.language.Integer.instance(3), params[2]);
    }
}
