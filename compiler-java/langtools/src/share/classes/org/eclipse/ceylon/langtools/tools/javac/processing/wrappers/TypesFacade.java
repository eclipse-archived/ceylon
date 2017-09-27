package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.List;

import org.eclipse.ceylon.javax.lang.model.util.Types;


public class TypesFacade implements javax.lang.model.util.Types {

    private Types f;

    public TypesFacade(Types f) {
        this.f = f;
    }

    @Override
    public javax.lang.model.element.Element asElement(javax.lang.model.type.TypeMirror arg0) {
        return Facades.facade(f.asElement(Facades.unfacade(arg0)));
    }

    @Override
    public javax.lang.model.type.TypeMirror asMemberOf(javax.lang.model.type.DeclaredType arg0, javax.lang.model.element.Element arg1) {
        return Facades.facade(f.asMemberOf(Facades.unfacade(arg0), Facades.unfacade(arg1)));
    }

    @Override
    public javax.lang.model.element.TypeElement boxedClass(javax.lang.model.type.PrimitiveType arg0) {
        return Facades.facade(f.boxedClass(Facades.unfacade(arg0)));
    }

    @Override
    public javax.lang.model.type.TypeMirror capture(javax.lang.model.type.TypeMirror arg0) {
        return Facades.facade(f.capture(Facades.unfacade(arg0)));
    }

    @Override
    public boolean contains(javax.lang.model.type.TypeMirror arg0, javax.lang.model.type.TypeMirror arg1) {
        return f.contains(Facades.unfacade(arg0), Facades.unfacade(arg1));
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> directSupertypes(javax.lang.model.type.TypeMirror arg0) {
        return Facades.facadeTypeMirrorList(f.directSupertypes(Facades.unfacade(arg0)));
    }

    @Override
    public javax.lang.model.type.TypeMirror erasure(javax.lang.model.type.TypeMirror arg0) {
        return Facades.facade(f.erasure(Facades.unfacade(arg0)));
    }

    @Override
    public javax.lang.model.type.ArrayType getArrayType(javax.lang.model.type.TypeMirror arg0) {
        return Facades.facade(f.getArrayType(Facades.unfacade(arg0)));
    }

    @Override
    public javax.lang.model.type.DeclaredType getDeclaredType(javax.lang.model.element.TypeElement arg0, javax.lang.model.type.TypeMirror... arg1) {
        return Facades.facade(f.getDeclaredType(Facades.unfacade(arg0), Facades.unfacade(arg1)));
    }

    @Override
    public javax.lang.model.type.DeclaredType getDeclaredType(javax.lang.model.type.DeclaredType arg0, javax.lang.model.element.TypeElement arg1, javax.lang.model.type.TypeMirror... arg2) {
        return Facades.facade(f.getDeclaredType(Facades.unfacade(arg0), Facades.unfacade(arg1), Facades.unfacade(arg2)));
    }

    @Override
    public javax.lang.model.type.NoType getNoType(javax.lang.model.type.TypeKind arg0) {
        return Facades.facade(f.getNoType(Wrappers.wrap(arg0)));
    }

    @Override
    public javax.lang.model.type.NullType getNullType() {
        return Facades.facade(f.getNullType());
    }

    @Override
    public javax.lang.model.type.PrimitiveType getPrimitiveType(javax.lang.model.type.TypeKind arg0) {
        return Facades.facade(f.getPrimitiveType(Wrappers.wrap(arg0)));
    }

    @Override
    public javax.lang.model.type.WildcardType getWildcardType(javax.lang.model.type.TypeMirror arg0, javax.lang.model.type.TypeMirror arg1) {
        return Facades.facade(f.getWildcardType(Facades.unfacade(arg0), Facades.unfacade(arg1)));
    }

    @Override
    public boolean isAssignable(javax.lang.model.type.TypeMirror arg0, javax.lang.model.type.TypeMirror arg1) {
        return f.isAssignable(Facades.unfacade(arg0), Facades.unfacade(arg1));
    }

    @Override
    public boolean isSameType(javax.lang.model.type.TypeMirror arg0, javax.lang.model.type.TypeMirror arg1) {
        return f.isSameType(Facades.unfacade(arg0), Facades.unfacade(arg1));
    }

    @Override
    public boolean isSubsignature(javax.lang.model.type.ExecutableType arg0, javax.lang.model.type.ExecutableType arg1) {
        return f.isSubsignature(Facades.unfacade(arg0), Facades.unfacade(arg1));
    }

    @Override
    public boolean isSubtype(javax.lang.model.type.TypeMirror arg0, javax.lang.model.type.TypeMirror arg1) {
        return f.isSubtype(Facades.unfacade(arg0), Facades.unfacade(arg1));
    }

    @Override
    public javax.lang.model.type.PrimitiveType unboxedType(javax.lang.model.type.TypeMirror arg0) {
        return Facades.facade(f.unboxedType(Facades.unfacade(arg0)));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TypesFacade == false)
            return false;
        return f.equals(((TypesFacade)obj).f);
    }
    
    @Override
    public int hashCode() {
        return f.hashCode();
    }
    
    @Override
    public String toString() {
        return f.toString();
    }
}
