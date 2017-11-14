/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.treegen;

public class Util {
    
    public static java.io.PrintStream out = System.out;

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
