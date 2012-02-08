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
    
    boolean[] array_boolean() { return new boolean[] { true }; };
    Boolean[] array_Boolean() { return new Boolean[] { true }; };
    int[] array_int() { return new int[] { 1 }; };
    Integer[] array_Integer() { return new Integer[] { 1 }; };
    long[] array_long() { return new long[] { 1L }; };
    Long[] array_Long() { return new Long[] { 1L }; };
    float[] array_float() { return new float[] { 1.0f }; };
    Float[] array_Float() { return new Float[] { 1.0f }; };
    double[] array_double() { return new double[] { 1.0d }; };
    Double[] array_Double() { return new Double[] { 1.0d }; };
    char[] array_char() { return new char[] { 'a' }; };
    Character[] array_Character() { return new Character[] { 'a' }; };
    String[] array_String() { return new String[] { "" }; };
    Object[] array_Object() { return new String[] { "" }; };
    
    public byte byte_attr;
    public byte getByte(){return 1;}
    public void setByte(byte b){}
    public int int_attr;
}
