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
import static org.eclipse.ceylon.compiler.java.runtime.metamodel.Metamodel.getTypeDescriptor;

import org.eclipse.ceylon.common.NonNull;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.Method;
import org.eclipse.ceylon.compiler.java.metadata.Name;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.Sequential;
import ceylon.language.meta.model.ClassModel;

@Ceylon(major = 8)
@Method
public final class type_ {
    
    private type_() {}
    
    @SuppressWarnings("unchecked")
    @TypeInfo("ceylon.language.meta.model::ClassModel<Type,ceylon.language::Nothing>")
    @TypeParameters(@TypeParameter(value = "Type", 
            variance = Variance.OUT, 
            satisfies = "ceylon.language::Anything"))
    @NonNull
    public static <Type> 
    ClassModel<? extends Type, ? super Sequential<? extends Object>>
    type(@Ignore TypeDescriptor $reifiedType,
            @Name("instance") @TypeInfo("Type") Type instance) {
        return (ClassModel<? extends Type, ? super Sequential<? extends Object>>) 
                getAppliedMetamodel(getTypeDescriptor(instance));
    }
    
}
