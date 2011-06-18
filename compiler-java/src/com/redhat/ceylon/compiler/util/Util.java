package com.redhat.ceylon.compiler.util;

public class Util {
    public static String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getGetterName(String property){
        return "get"+capitalize(property);
    }

    public static String getSetterName(String property){
        return "set"+capitalize(property);
    }
}
