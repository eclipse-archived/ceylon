package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.model.Member$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.IN),
    @TypeParameter(value = "Kind", variance = Variance.OUT, satisfies = "ceylon.language.model::Model")
})
public abstract class AppliedMember<Type, Kind extends ceylon.language.model.Model> 
    implements ceylon.language.model.Member<Type, Kind>, ReifiedType {

    private ceylon.language.model.Type<? extends Object> container;
    @Ignore
    protected final TypeDescriptor $reifiedKind;
    @Ignore
    protected final TypeDescriptor $reifiedType;

    public AppliedMember(@Ignore TypeDescriptor $reifiedType, @Ignore TypeDescriptor $reifiedKind,
                         ceylon.language.model.Type<? extends Object> container){
        this.$reifiedType = $reifiedType;
        this.$reifiedKind = $reifiedKind;
        this.container = container;
    }
    
    @Override
    @Ignore
    public Member$impl<Type, Kind> $ceylon$language$model$Member$impl() {
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.model::Type<ceylon.language::Anything>")
    public ceylon.language.model.Type<? extends Object> getDeclaringClassOrInterface() {
        return container;
    }

    @Override
    @Ignore
    public Kind $call() {
        throw new UnsupportedOperationException();
    }

    protected abstract Kind bindTo(Object instance);
    
    @Override
    @Ignore
    public Kind $call(Object instance) {
        return bindTo(instance);
    }

    @Override
    @Ignore
    public Kind $call(Object arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Kind $call(Object arg0, Object arg1, Object arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Kind $call(Object... args) {
        throw new UnsupportedOperationException();
    }

    @Ignore
    @Override
    public short $getVariadicParameterIndex() {
        return -1;
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedMember.class, $reifiedType, $reifiedKind);
    }
}
