/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.language;

import org.eclipse.ceylon.compiler.java.Util;

@org.eclipse.ceylon.compiler.java.metadata.Ceylon(
        major = 8,
        minor = 1)
@org.eclipse.ceylon.compiler.java.metadata.Attribute
@org.eclipse.ceylon.compiler.java.metadata.Name("vmArguments")
final class vmArguments_ {
    
    private vmArguments_() {
    }
    
    @NativeAnnotation$annotation$(backends = "jvm")
    @org.eclipse.ceylon.common.NonNull
    @org.eclipse.ceylon.compiler.java.metadata.TypeInfo("ceylon.language::Array<ceylon.language::String>")
    @org.eclipse.ceylon.compiler.java.metadata.Transient
    static Array<String> get_() {
        java.lang.String[] args = Util.checkNull(Util.getArgs());
        String[] strings = new String[args.length];
        for (int i=0; i<args.length; i++) {
            java.lang.String arg = args[i];
            strings[i] = String.instance(arg==null ? "" : arg);
        }
        return Array.instance(strings);
    }
}
