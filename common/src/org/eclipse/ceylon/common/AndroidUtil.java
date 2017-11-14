/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.common;

import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class AndroidUtil {

    private static Boolean isRunningAndroid;
    
    public static boolean isRunningAndroid(){
        if(isRunningAndroid != null)
            return isRunningAndroid;
        Object app;
        try {
            app = Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication").invoke(null);
            isRunningAndroid = app != null;
        } catch (IllegalAccessException e) {
            isRunningAndroid = false;
        } catch (IllegalArgumentException e) {
            isRunningAndroid = false;
        } catch (InvocationTargetException e) {
            isRunningAndroid = false;
        } catch (NoSuchMethodException e) {
            isRunningAndroid = false;
        } catch (SecurityException e) {
            isRunningAndroid = false;
        } catch (ClassNotFoundException e) {
            isRunningAndroid = false;
        }
        return isRunningAndroid;
    }
    
    public static List<String> getDexEntries() {
        try {
            Object app = Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication").invoke(null);
            Object codePath = Class.forName("android.app.Application").getMethod("getPackageCodePath").invoke(app);
            Class<?> dexFileClass = Class.forName("dalvik.system.DexFile");
            Object dexFile = dexFileClass.getConstructor(String.class).newInstance(codePath);
            Enumeration<String> entries = (Enumeration<String>) dexFileClass.getMethod("entries").invoke(dexFile);
            List<String> dexEntries = new LinkedList<String>();
            while(entries.hasMoreElements()){
                String entry = entries.nextElement();
                entry = entry.replace('.', '/')+".class";
                dexEntries.add(entry);
            }
            dexFileClass.getMethod("close").invoke(dexFile);
            return dexEntries;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
    
}
