package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import com.redhat.ceylon.javax.lang.model.type.NoType;

public class NoTypeFacade extends TypeMirrorFacade implements javax.lang.model.type.NoType {

    public NoTypeFacade(NoType f) {
        super(f);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NoTypeFacade == false)
            return false;
        return f.equals(((NoTypeFacade)obj).f);
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
