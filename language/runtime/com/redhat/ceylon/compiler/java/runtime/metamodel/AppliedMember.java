package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.Member$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.IN),
    @TypeParameter(value = "Kind", variance = Variance.OUT, satisfies = "ceylon.language.metamodel::DeclarationType")
})
public abstract class AppliedMember<Type, Kind extends ceylon.language.metamodel.DeclarationType> 
    implements ceylon.language.metamodel.Member<Type, Kind>, ReifiedType {

//    private AppliedClassOrInterfaceType<Type> container;
    @Ignore
    private TypeDescriptor $reifiedKind;
    @Ignore
    private TypeDescriptor $reifiedType;

    public AppliedMember(@Ignore TypeDescriptor $reifiedType, @Ignore TypeDescriptor $reifiedKind/*,
                         AppliedClassOrInterfaceType<Type> container*/){
        this.$reifiedType = $reifiedType;
        this.$reifiedKind = $reifiedKind;
//        this.container = container;
    }
    
    @Override
    @Ignore
    public Member$impl<Type, Kind> $ceylon$language$metamodel$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

//    @Override
//    @TypeInfo("ceylon.language.metamodel::ClassOrInterface<Type>")
//    public ClassOrInterface<? extends Type> getDeclaringClassOrInterface() {
//        return container;
//    }

    @Override
    public Kind $call() {
        throw new UnsupportedOperationException();
    }

    protected abstract Kind bindTo(Object instance);
    
    @Override
    public Kind $call(Object instance) {
        return bindTo(instance);
    }

    @Override
    public Kind $call(Object arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Kind $call(Object arg0, Object arg1, Object arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Kind $call(Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short $getVariadicParameterIndex() {
        return -1;
    }

    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedMember.class, $reifiedType, $reifiedKind);
    }
}
