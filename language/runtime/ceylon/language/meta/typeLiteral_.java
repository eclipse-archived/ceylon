/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package ceylon.language.meta;

import static org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel.getAppliedMetamodel;

import org.eclipse.ceylon.common.NonNull;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Method;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 8)
@Method
public final class typeLiteral_ {
    
    private typeLiteral_() {}
    
    @SuppressWarnings({ "unchecked" })
    @TypeInfo("ceylon.language.meta.model::Type<Type>")
    @TypeParameters(@TypeParameter(value = "Type", 
            variance = Variance.OUT, 
            satisfies = "ceylon.language::Anything"))
    @NonNull
    public static <Type> ceylon.language.meta.model.Type<? extends Type> 
    typeLiteral(@Ignore TypeDescriptor $reifiedType) {
        return (ceylon.language.meta.model.Type<? extends Type>) 
                getAppliedMetamodel($reifiedType);
    }
}
