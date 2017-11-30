/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration { Module }

"""Represents the list of Ceylon modules currently loaded at runtime.
   
   Note that this contains all loaded modules, including those that are
   not imported by your module.
   
   Since Ceylon supports module isolation at runtime, it is possible that
   there are more than one version of a given module loaded at the same time.
   
   ### Usage example
   
   Here's how you would iterate all the loaded modules and print their name and version:
   
       import ceylon.language.meta { modules }
   
       for(mod in modules.list){
           print("Module: ``mod.name``/``mod.version``");
       }
 """
shared native object modules {
    
    "Returns the list of all currently loaded modules. This may include modules that
     were not imported directly by your module, and multiple versions of the same
     module."
    shared native Module[] list;
    
    "Finds a module by name and version, returns `null` if not found."
    shared native Module? find(String name, String version);
    
    // FIXME: can we really not have a default module?
    "Returns the default module, if there is one. This is only the case when
     you are running the default module."
    shared native Module? default;
    
    // FIXME: add load/unload
}