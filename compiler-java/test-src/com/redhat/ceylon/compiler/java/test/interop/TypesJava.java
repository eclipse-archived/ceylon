package com.redhat.ceylon.compiler.java.test.interop;

public class TypesJava {
    boolean return_boolean() { return true; };
    Boolean return_Boolean() { return true; };
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
}
