/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.util;

/**
 * This distance is computed as levenshtein distance divided by the length of 
 * the longest string. The resulting value is always in the interval [0.0 1.0] 
 * but it is not a metric anymore!
 * The similarity is computed as 1 - normalized distance.
 * @author Thibault Debatty
 */
public class NormalizedLevenshtein {


    public static void main(String[] args) {
        NormalizedLevenshtein l = new NormalizedLevenshtein();
        
        System.out.println(l.distance("My string", "My $tring"));
        System.out.println(l.distance("My string", "M string2"));
        System.out.println(l.distance("My string", "abcd"));
    }
    
    private final Levenshtein l = new Levenshtein();

    public double distance(String s1, String s2) {
        return l.distance(s1, s2) / Math.max(s1.length(), s2.length());
    }

    public double similarity(String s1, String s2) {
        return 1.0 - distance(s1, s2);
    }
    
}
