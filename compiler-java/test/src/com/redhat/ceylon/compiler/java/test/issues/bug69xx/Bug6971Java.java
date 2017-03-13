package com.redhat.ceylon.compiler.java.test.issues.bug69xx;

public class Bug6971Java {
    static String getStringOrNull(int r){ return r == 1 ? null : "foo"; }
    static void soWithCharSequence(CharSequence cs){}
    static CharSequence getCharSequenceOrNull(int r){ return r == 1 ? null : "foo"; }
    static void soWithString(String cs){}

}