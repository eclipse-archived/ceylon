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
package com.redhat.ceylon.compiler.java.test.interop;

public class TypesJava {
    boolean return_boolean() { return true; };
    Boolean return_Boolean() { return true; };
    byte return_byte() { return 1; };
    Byte return_Byte() { return 1; };
    short return_short() { return 1; };
    Short return_Short() { return 1; };
    int return_int() { return 1; };
    Integer return_Integer() { return 1; };
    long return_long() { return 1L; };
    Long return_Long() { return 1L; };
    float return_float() { return 1.0f; };
    Float return_Float() { return 1.0f; };
    double return_double() { return 1.0d; };
    Double return_Double() { return 1.0d; };
    char return_char() { return 'a'; };
    Character return_Character() { return 'a'; };
    String return_String() { return ""; };
    Object return_Object() { return ""; };

    void booleanParams(boolean p, java.lang.Boolean j, ceylon.language.Boolean c){}
    void byteParams(byte p, java.lang.Byte j){}
    void shortParams(short p, java.lang.Short j){}
    void intParams(int p, java.lang.Integer j){}
    void longParams(long p, java.lang.Long j, ceylon.language.Integer c){}
    void floatParams(float p, java.lang.Float j){}
    void doubleParams(double p, java.lang.Double j, ceylon.language.Float c){}
    void charParams(char p, java.lang.Character j, ceylon.language.Character c){}
    void stringParams(java.lang.String j, ceylon.language.String c){}
    void objectParams(java.lang.Object j){}
    
    boolean[] return_booleans() { return new boolean[] { true, false }; };
    Boolean[] return_Booleans() { return new Boolean[] { true, false }; };
    byte[] return_bytes() { return new byte[] { 1, 2, 3 }; };
    Byte[] return_Bytes() { return new Byte[] { 1, 2, 3 }; };
    short[] return_shorts() { return new short[] { 1, 2, 3 }; };
    Short[] return_Shorts() { return new Short[] { 1, 2, 3 }; };
    int[] return_ints() { return new int[] { 1, 2, 3 }; };
    Integer[] return_Integers() { return new Integer[] { 1, 2, 3 }; };
    long[] return_longs() { return new long[] { 1L, 2L, 3L }; };
    Long[] return_Longs() { return new Long[] { 1L, 2L, 3L }; };
    float[] return_floats() { return new float[] { 1.0f, 1.5f, 2.0f }; };
    Float[] return_Floats() { return new Float[] { 1.0f, 1.5f, 2.0f }; };
    double[] return_doubles() { return new double[] { 1.0d, 1.5d, 2.0d }; };
    Double[] return_Doubles() { return new Double[] { 1.0d, 1.5d, 2.0d }; };
    char[] return_chars() { return new char[] { 'a', 'b', 'z' }; };
    Character[] return_Characters() { return new Character[] { 'a', 'b', 'z' }; };
    String[] return_Strings() { return new String[] { "aap", "noot", "mies", "" }; };
    Object[] return_Objects() { return new Object[] { "aap", 'b', 1.5d, 3 }; };

    java.util.List<Object[]> return_ObjectArrayList() { return null; }
    
    void take_booleans(boolean[] val) { };
    void take_Booleans(Boolean[] val) { };
    void take_bytes(byte[] val) { };
    void take_Bytes(Byte[] val) { };
    void take_shorts(short[] val) { };
    void take_Shorts(Short[] val) { };
    void take_ints(int[] val) { };
    void take_Integers(Integer[] val) { };
    void take_longs(long[] val) { };
    void take_Longs(Long[] val) { };
    void take_floats(float[] val) { };
    void take_Floats(Float[] val) { };
    void take_doubles(double[] val) { };
    void take_Doubles(Double[] val) { };
    void take_chars(char[] val) { };
    void take_Characters(Character[] val) { };
    void take_Strings(String[] val) { };
    void take_Objects(Object[] val) { };

    public byte byte_attr;
    public byte getByte(){return 1;}
    public void setByte(byte b){}
    public int int_attr;
    
    java.util.List<ceylon.language.String> stringList(){ return null; }
    java.util.List<java.lang.String> jstringList(){ return null; }
    java.util.List<java.lang.Object> objectList(){ return null; }
    
    void variadicBoolean(boolean... vars){}
    void variadicCeylonBoolean(ceylon.language.Boolean... vars){}

    void variadicChar(char... vars){}

    void variadicByte(byte... vars){}
    void variadicShort(short... vars){}
    void variadicInt(int... vars){}
    void variadicLong(long... vars){}
    void variadicCeylonInteger(ceylon.language.Integer... vars){}
    
    void variadicFloat(float... vars){}
    void variadicDouble(double... vars){}
    void variadicCeylonFloat(ceylon.language.Float... vars){}
    
    void variadicJavaString(java.lang.String... vars){}
    void variadicCeylonString(ceylon.language.String... vars){}
    
    void variadicObject(java.lang.Object... vars){}
    <T> void variadicT(T... vars){}

    public void variadicObjectArray(Object[]... arr){}
    
    void takeAll(boolean b, byte by, short s, int i, long l, 
            float f, double d, char c, 
            java.lang.String str, java.lang.Object o){}
    void takeAllVariadic(boolean b, byte by, char... c){}
}
