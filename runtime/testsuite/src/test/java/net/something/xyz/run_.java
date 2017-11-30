/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package net.something.xyz;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run_ {
    public static void main(String[] args) {
        // test resource on_demand
        ClassLoader cl = run_.class.getClassLoader();

        try {
            // test class on_demand
            cl.loadClass("org.jboss.acme.Qwert").newInstance();
/*
            Object m = cl.loadClass("org.jboss.acme.run_").newInstance();
            Class<?> clazz = m.getClass();
            Method run_ = clazz.getMethod("main", String[].class);
            @SuppressWarnings("UnnecessaryLocalVariable")
            Object fsa = args;
            run_.invoke(m, fsa);
*/

            cl.loadClass("si.alesj.ceylon.test.Touch"); // MC currently only works on classes
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String resource = "si/alesj/ceylon/test/config.xml";
        Object url = cl.getResource(resource);
        if (url == null)
            throw new IllegalArgumentException("Null url: " + resource);
        System.out.println("URL: " + url);
    }
}
