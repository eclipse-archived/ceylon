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

    public boolean booleanField = true;
    public byte byteField = 1;
    public short shortField = 2;
    public int intField = 3;
    public long longField = 4;
    public float floatField = 1.0f;
    public double doubleField = 2.0;
    public char charField = 'a';
    public String stringField = "a";
    public Object objectField = ceylon.language.String.instance("b");
    
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

    public void method(boolean bo, byte by, short s, int i, long l, float f, double d, char c, String str, Object o){
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
    
    public boolean methodBoolean() { return true; }
    public byte methodByte() { return 1; }
    public short methodShort() { return 2; }
    public int methodInt() { return 3; }
    public long methodLong() { return 4; }
    public float methodFloat() { return (float) 1.0; }
    public double methodDouble() { return 2.0; }
    public char methodChar() { return 'a'; }
    public String methodStr() { return "a"; }
    public Object methodObject() { return ceylon.language.String.instance("b"); }

    public void methodBooleanVarargs(long count, boolean b0, boolean... b){
        assertt(b0 == true);
        assertt(b != null && b.length == count);
        for(boolean bb : b){
            assertt(bb == true);
        }
    }
    
    public void methodByteVarargs(byte b0, byte... b){
        assertt(b0 == 1);
        assertt(b != null && b.length == 1 && b[0] == 1);
    }
    
    public void methodShortVarargs(short b0, short... b){
        assertt(b0 == 2);
        assertt(b != null && b.length == 1 && b[0] == 2);
    }
    
    public void methodIntVarargs(int b0, int... b){
        assertt(b0 == 3);
        assertt(b != null && b.length == 1 && b[0] == 3);
    }
    
    public void methodLongVarargs(long b0, long... b){
        assertt(b0 == 4);
        assertt(b != null && b.length == 1 && b[0] == 4);
    }
    
    public void methodFloatVarargs(float b0, float... b){
        assertt(b0 == 1.0);
        assertt(b != null && b.length == 1 && b[0] == 1.0);
    }
    
    public void methodDoubleVarargs(double b0, double... b){
        assertt(b0 == 2.0);
        assertt(b != null && b.length == 1 && b[0] == 2.0);
    }
    
    public void methodCharVarargs(char b0, char... b){
        assertt(b0 == 'a');
        assertt(b != null && b.length == 1 && b[0] == 'a');
    }
    
    public void methodJavaStringVarargs(String b0, String... b){
        assertt(b0.equals("a"));
        assertt(b != null && b.length == 1 && b[0].equals("a"));
    }
    
    public void methodObjectVarargs(long count, Object b0, Object... b){
        assertt(b0.equals(ceylon.language.String.instance("b")));
        assertt(b != null && b.length == count);
        for(Object bb : b){
            assertt(bb.equals(ceylon.language.String.instance("b")));
        }
    }
    
    public <T extends ceylon.language.Number> void methodBoundObjectVarargs(long count, T b0, T... b){
        assertt(b0.equals(ceylon.language.Integer.instance(1)));
        assertt(b != null && b.length == count);
        for(T t : b){
            assertt(t.equals(ceylon.language.Integer.instance(1)));
        }
    }
    
    public boolean getBoolean(){ return true; }
    public void setBoolean(boolean b){
        assertt(b == true);
    }
    
    public byte getByte(){ return 1; }
    public void setByte(byte b){
        assertt(b == 1);
    }

    public short getShort(){ return 2; }
    public void setShort(short b){
        assertt(b == 2);
    }

    public int getInt(){ return 3; }
    public void setInt(int b){
        assertt(b == 3);
    }

    public long getLong(){ return 4; }
    public void setLong(long b){
        assertt(b == 4);
    }

    public float getFloat(){ return (float) 1.0; }
    public void setFloat(float b){
        assertt(b == 1.0);
    }

    public double getDouble(){ return 2.0; }
    public void setDouble(double b){
        assertt(b == 2.0);
    }

    public char getChar(){ return 'a'; }
    public void setChar(char b){
        assertt(b == 'a');
    }

    public String getStr(){ return "a"; }
    public void setStr(String b){
        assertt(b.equals("a"));
    }

    public Object getObject(){ return ceylon.language.String.instance("b"); }
    public void setObject(Object b){
        assertt(ceylon.language.String.instance("b").equals(b));
    }
    
    static void assertt(boolean test){
        if(!test)
            throw new AssertionFailedError();
    }
    
    @Override
    public String toString() {
        return "JavaType is there";
    }
    
    public class Member{
        public Member(boolean unboxed){
            assertt(unboxed == true);
        }
    }

    public class MemberVarargs{
        public MemberVarargs(long count, boolean unboxed, boolean... b){
            assertt(unboxed == true);
            assertt(b != null && b.length == count);
            for(boolean bb : b){
                assertt(bb == true);
            }
        }
    }
    
    public static int staticField = 2;

    private static int staticField2 = 2;

    public static int getStaticGetter(){
        return staticField2;
    }

    public static void setStaticGetter(int v){
        staticField2 = v;
    }
    
    public static int staticMethod(int v){
        return v;
    }
    
    public static class StaticClass{
        public final int v;
        public StaticClass(int v){
            this.v = v;
        }
    }
}
