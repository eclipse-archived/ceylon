/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package eu.cloud.clazz;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run_ {
    public static void main(String[] args) {
        ClassLoader cl = run_.class.getClassLoader();
        try {
            cl.loadClass("org.jboss.filtered.spi.SomeSPI");

            // Not relevant anymore
            /*
            Class<?> pp = cl.loadClass("ceylon.file.parsePath_");
            Method parse = pp.getDeclaredMethod("parsePath", String.class);
            Object path = parse.invoke(null, "buuu");
            Class<?> pc = cl.loadClass("ceylon.file.Path");
            Method r = pc.getDeclaredMethod("getResource");
            Class<?> rc = cl.loadClass("ceylon.file.Resource");
            Class<?> nof = cl.loadClass("ceylon.io.newOpenFile_");
            Method newOpenFile = nof.getDeclaredMethod("newOpenFile", rc);
            Object ofi = newOpenFile.invoke(null, r.invoke(path));
            System.out.println("ofi = " + ofi);
            */
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
/*
        try {
            cl.loadClass("org.jboss.filtered.api.SomeAPI");
            throw new RuntimeException("Fail, should not be here!");
        } catch (ClassNotFoundException ignored) {
        }
        try {
            cl.loadClass("org.jboss.filtered.impl.SomeImpl");
            throw new RuntimeException("Fail, should not be here!");
        } catch (ClassNotFoundException ignored) {
        }
*/
    }
}

