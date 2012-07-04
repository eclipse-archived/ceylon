package com.redhat.ceylon.compiler.java.codegen;

import java.util.HashSet;

import com.sun.tools.javac.parser.Token;

public class Naming {
    
    /** Quote the given name by prefixing it with a dollar ($) */
    public static String quote(String name) {
        return "$"+name;
    }

    private static final HashSet<String> tokens;
    static {
        tokens = new HashSet<String>();
        for (Token t : Token.values()) {
            tokens.add(t.name);
        }
    }
    /** Determines whether the given name is a Java keyword */
    public static boolean isJavaKeyword(String name) {
        return tokens.contains(name);
    }
    /** Prefixes the given name with a dollar ($) if it is a Java keyword */
    public static String quoteIfJavaKeyword(String name){
        if(isJavaKeyword(name))
            return quote(name);
        return name;
    }
    
    public static String getErasedMethodName(String name) {
        // ERASURE
        if ("hash".equals(name)) {
            return "hashCode";
        } else if ("string".equals(name)) {
            return "toString";
        } else if ("equals".equals(name)) {
            // This is a special case where we override the mangling of getMethodName()
            return "equals";
        } else if ("clone".equals(name)) {
            // This is a special case where we override the mangling of getMethodName()
            // FIXME we should only do this when implementing Cloneable!
            return "clone";
        } else {
            return getMethodName(name);
        }
    }

    public static String getMethodName(String name) {
        // ERASURE
        if ("hashCode".equals(name)) {
            return "$hashCode";
        } else if ("toString".equals(name)) {
            return "$toString";
        } else if ("equals".equals(name)) {
            return "$equals";
        } else if ("wait".equals(name)) {
            return "$wait";
        } else if ("notify".equals(name)) {
            return "$notify";
        } else if ("notifyAll".equals(name)) {
            return "$notifyAll";
        } else if ("getClass".equals(name)) {
            return "$getClass";
        } else if ("finalize".equals(name)) {
            return "$finalize";
        } else if ("clone".equals(name)) {
            return "$clone";
        } else {
            return quoteIfJavaKeyword(name);
        }
    }

    public static String getErasedGetterName(String property) {
        // ERASURE
        if ("hash".equals(property)) {
            return "hashCode";
        } else if ("string".equals(property)) {
            return "toString";
        }
        
        return getGetterName(property);
    }

    public static String strip(String str){
        return (str.charAt(0) == '$') ? str.substring(1) : str;
    }

    public static String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    public static String getGetterName(String property) {
        return "get"+capitalize(strip(property));
    }

    public static String getSetterName(String property){
        return "set"+capitalize(strip(property));
    }
    
}
