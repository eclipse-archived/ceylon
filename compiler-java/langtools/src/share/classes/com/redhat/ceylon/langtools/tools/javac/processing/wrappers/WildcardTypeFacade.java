package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.type.WildcardType;

public class WildcardTypeFacade extends TypeMirrorFacade implements javax.lang.model.type.WildcardType {

    public WildcardTypeFacade(WildcardType f) {
        super(f);
    }

    @Override
    public javax.lang.model.type.TypeMirror getExtendsBound() {
        return Facades.facade(((WildcardType)f).getExtendsBound());
    }

    @Override
    public javax.lang.model.type.TypeMirror getSuperBound() {
        return Facades.facade(((WildcardType)f).getSuperBound());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof WildcardTypeFacade == false)
            return false;
        return f.equals(((WildcardTypeFacade)obj).f);
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
