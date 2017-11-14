/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.jogl.ceylon;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class run_ {
    public static void main(String[] args) throws Throwable {
        Class<?> clazz = run_.class.getClassLoader().loadClass("javax.media.opengl.GLProfile");
        Method method = clazz.getMethod("getDefault");
        Object result = method.invoke(null);
        System.out.println("GL instance = " + result);
    }
}
