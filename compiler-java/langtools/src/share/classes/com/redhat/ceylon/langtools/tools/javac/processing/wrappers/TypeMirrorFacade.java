package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.type.TypeMirror;

public class TypeMirrorFacade implements javax.lang.model.type.TypeMirror {

    protected TypeMirror f;

    public TypeMirrorFacade(TypeMirror f) {
        this.f = f;
    }

    @Override
    public <R, P> R accept(javax.lang.model.type.TypeVisitor<R, P> v, P p) {
        return f.accept(Wrappers.wrap(v), p);
    }

    @Override
    public javax.lang.model.type.TypeKind getKind() {
        return Facades.facade(f.getKind());
    }

    @Override
    public String toString() {
        return f.toString();
    }
}
