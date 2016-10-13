package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.type.ReferenceType;

public class ReferenceTypeFacade extends TypeMirrorFacade implements javax.lang.model.type.ReferenceType {

    public ReferenceTypeFacade(ReferenceType f) {
        super(f);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ReferenceTypeFacade == false)
            return false;
        return f.equals(((ReferenceTypeFacade)obj).f);
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
