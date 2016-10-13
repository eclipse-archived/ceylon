package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.type.PrimitiveType;

public class PrimitiveTypeFacade extends TypeMirrorFacade implements javax.lang.model.type.PrimitiveType {

    public PrimitiveTypeFacade(PrimitiveType f) {
        super(f);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PrimitiveTypeFacade == false)
            return false;
        return f.equals(((PrimitiveTypeFacade)obj).f);
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
