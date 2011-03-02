package com.redhat.ceylon.compiler.typechecker.treegen;

public class Util {
    
    static java.io.PrintStream out = System.out;

    public static String className(String nodeName) { 
        return toJavaIdentifier(nodeName, true); 
    }
    
    public static String fieldName(String nodeName) { 
        return toJavaIdentifier(nodeName, false); 
    }
    
    public static String toJavaIdentifier(String nodeName, boolean boundary) {
        StringBuilder result = new StringBuilder();
        for (char c: nodeName.toCharArray()) {
            if (c=='_') {
                boundary=true;
            }
            else if (boundary) {
                result.append(c);
                boundary=false;
            }
            else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }
    
    public static String initialUpper(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
    
    public static String description(String nodeName) {
        return nodeName.toLowerCase().replace('_', ' ');
    }
    
    public static void print(String text) {
       out.print(text); 
    }
    
    public static void println(String text) {
       out.println(text); 
    }

}
