/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package io.xov.yalp;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run_ {
    public static void main(String[] args) throws Exception {
        Class<run_> run_class = run_.class;
        ClassLoader runclassClassLoader = run_class.getClassLoader();
        System.err.println(runclassClassLoader.loadClass("org.jboss.vfs.VFS"));
        System.err.println(runclassClassLoader.loadClass("javax.net.ssl.KeyManager"));
    }
}
