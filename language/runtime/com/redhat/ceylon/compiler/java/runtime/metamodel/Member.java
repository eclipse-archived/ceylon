package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Callable;
import ceylon.language.metamodel.ClassOrInterface;
import ceylon.language.metamodel.Member$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@com.redhat.ceylon.compiler.java.metadata.Class
public class Member<Type, Kind extends ceylon.language.metamodel.Declaration>
    implements ceylon.language.metamodel.Member<Type, Kind>, Callable<Kind>, ReifiedType {
    
    @Ignore
    public static final TypeDescriptor $TypeDescriptor = TypeDescriptor.klass(Member.class);

    Kind declaration;

    private ClassOrInterface container;

    public Member(ClassOrInterface container, 
                  Kind declaration) {
        this.container = container;
        this.declaration = declaration;
    }

    @Override
    @Ignore
    public Member$impl<Type, Kind> $ceylon$language$metamodel$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @TypeInfo("ceylon.language.metamodel::ClassOrInterface")
    public ClassOrInterface getDeclaringClassOrInterface() {
        return container;
    }

    @Override
    public Kind $call() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Kind $call(Object arg0) {
        // FIXME: WTF do we do with the argument?
        return declaration;
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
    public TypeDescriptor $getType() {
        return $TypeDescriptor;
    }
}
