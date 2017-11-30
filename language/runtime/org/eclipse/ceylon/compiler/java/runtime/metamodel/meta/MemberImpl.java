/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.runtime.metamodel.meta;

import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;
import org.eclipse.ceylon.compiler.java.metadata.Variance;
import org.eclipse.ceylon.compiler.java.runtime.model.ReifiedType;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.runtime.metamodel.meta.MemberImpl;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Kind", variance = Variance.OUT, satisfies = "ceylon.language.meta.model::Model")
})
public abstract class MemberImpl<Container, Kind extends ceylon.language.meta.model.Model> 
    implements ceylon.language.meta.model.Member<Container, Kind>, ReifiedType {

    private final ceylon.language.meta.model.Type<? extends Object> container;
    @Ignore
    protected final TypeDescriptor $reifiedKind;
    @Ignore
    protected final TypeDescriptor $reifiedContainer;

    public MemberImpl(@Ignore TypeDescriptor $reifiedContainer, @Ignore TypeDescriptor $reifiedKind,
                         ceylon.language.meta.model.Type<? extends Object> container){
        this.$reifiedContainer = $reifiedContainer;
        this.$reifiedKind = $reifiedKind;
        this.container = container;
    }
    
    @Override
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>")
    public ceylon.language.meta.model.Type<? extends Object> getDeclaringType() {
        return container;
    }

    @Override
    @Ignore
    public Kind $call$() {
        throw new UnsupportedOperationException();
    }

    protected abstract Kind bindTo(Object instance);
    
    @Override
    @Ignore
    public Kind $call$(Object instance) {
        return bindTo(instance);
    }

    @Override
    @Ignore
    public Kind $call$(Object arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Kind $call$(Object arg0, Object arg1, Object arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Kind $call$(Object... args) {
        throw new UnsupportedOperationException();
    }

    @Ignore
    @Override
    public short $getVariadicParameterIndex$() {
        return -1;
    }

    @Ignore
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(MemberImpl.class, $reifiedContainer, $reifiedKind);
    }
}
