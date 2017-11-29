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

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Method;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 8)
@Method
@SharedAnnotation$annotation$
@NativeAnnotation$annotation$(backends={})
public final class className_ {
    
    private className_() {}
    
    public static java.lang.String className(@Name("obj")
    @TypeInfo("ceylon.language::Anything")
    final java.lang.Object object) {
        return object==null ? 
                "ceylon.language.null_" : 
                object.getClass().getName();
    }
    
}
