package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Callable;
import ceylon.language.metamodel.untyped.Member$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel.untyped::Declaration")
})
public class FreeMember<Kind extends ceylon.language.metamodel.untyped.Declaration>
    implements ceylon.language.metamodel.untyped.Member<Kind>, Callable<Kind>, ReifiedType {
    
    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(FreeMember.class);

    Kind declaration;

    private FreeClassOrInterface container;

    public FreeMember(FreeClassOrInterface container, 
                  Kind declaration) {
        this.container = container;
        this.declaration = declaration;
    }

    @Override
    @Ignore
    public Member$impl<Kind> $ceylon$language$metamodel$untyped$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel.untyped::ClassOrInterface")
    public ceylon.language.metamodel.untyped.ClassOrInterface getDeclaringClassOrInterface() {
        return container;
    }

    @Override
    public Kind $call() {
        return declaration;
    }

    @Override
    public Kind $call(Object arg0) {
        throw new UnsupportedOperationException();
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
        // TODO Auto-generated method stub
        return -1;
    }

    @Override
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
