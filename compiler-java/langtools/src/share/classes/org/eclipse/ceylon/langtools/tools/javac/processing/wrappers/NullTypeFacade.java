package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import org.eclipse.ceylon.javax.lang.model.type.NullType;

public class NullTypeFacade extends ReferenceTypeFacade implements javax.lang.model.type.NullType {

    public NullTypeFacade(NullType f) {
        super(f);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NullTypeFacade == false)
            return false;
        return f.equals(((NullTypeFacade)obj).f);
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
