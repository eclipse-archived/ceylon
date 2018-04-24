/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime;


import java.util.Arrays;

import org.eclipse.ceylon.common.JVMModuleUtil;
import org.eclipse.ceylon.common.ModuleSpec;
import org.eclipse.ceylon.common.ModuleSpec.Option;
import org.eclipse.ceylon.model.typechecker.model.Module;

/**
 * Same as Main except takes its main class as optional parameter.
 */
public class Main2 {

    /**
     * Same as Main except takes its main class as optional parameter.
     */
    public static void main(String[] args) {
        // WARNING: keep in sync with Main
        int idx = 0;
        boolean allowMissingModules = false;
        String overrides = null;
        String runClassOrMethod = null;
        for(int i=0;i<args.length;i++){
            String arg = args[i];
            if(!arg.startsWith("-"))
                break;
            if(arg.equals("--allow-missing-modules")){
                allowMissingModules = true;
            }else if(arg.equals("--overrides") || arg.equals("--maven-overrides")){
                if(i+1 >= args.length){
                    usage();
                }
                overrides = args[++i];
                idx++;
            }else if(arg.equals("--run")){
                if(i+1 >= args.length){
                    usage();
                }
                runClassOrMethod = args[++i];
                idx++;
            }else
                usage();
            idx++;
        }
        if(args.length < (1 + idx)){
            usage();
        }
        ModuleSpec moduleSpec = ModuleSpec.parse(args[idx], Option.VERSION_REQUIRED);
        String version;
        if(moduleSpec.getName().equals(org.eclipse.ceylon.model.typechecker.model.Module.DEFAULT_MODULE_NAME))
            version = null;
        else
            version = moduleSpec.getVersion();
        String[] moduleArgs = Arrays.copyOfRange(args, 1 + idx, args.length);
        Main.instance()
            .allowMissingModules(allowMissingModules)
            .overrides(overrides)
            .run(moduleSpec.getName(), version, runSpecToJavaClass(moduleSpec.getName(), runClassOrMethod), moduleArgs);
    }

    private static String runSpecToJavaClass(String module, String runClassOrMethod){
        String javaClass;
        if (runClassOrMethod == null || runClassOrMethod.isEmpty()) {
            // "default" is not a package name
            if (module.equals(Module.DEFAULT_MODULE_NAME)) {
                javaClass = "run";
            } else {
                javaClass = module + "." + "run";
            }
        } else {
            // replace any :: with a dot to allow for both java and ceylon-style run methods
            javaClass = runClassOrMethod.replace("::", ".");
        }
        char firstChar = javaClass.charAt(0);
        int lastDot = javaClass.lastIndexOf('.');
        if (lastDot > 0) {
            firstChar = javaClass.charAt(lastDot + 1);
            String lastPart = javaClass.substring(lastDot+1);
            String pkgPart = javaClass.substring(0, lastDot);
            // only quote the package parts
            javaClass = JVMModuleUtil.quoteJavaKeywords(pkgPart) + "." + lastPart;
        }
        // if we have no package part, we don't need quoting
        // we add _ to run class
        if(Character.isLowerCase(firstChar))
            javaClass += "_";
        return javaClass;
    }
    
    private static void usage() {
        System.err.println("Invalid arguments.");
        System.err.println("Usage: \n");
        System.err.println(Main2.class.getName()+" [--allow-missing-modules] [--overrides overridesFile.xml] [--run runClassOrMethod] moduleSpec args*");
        System.exit(1);
    }

}
