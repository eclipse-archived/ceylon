package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import org.eclipse.ceylon.javax.lang.model.type.ErrorType;

public class ErrorTypeFacade extends DeclaredTypeFacade implements javax.lang.model.type.ErrorType {

    public ErrorTypeFacade(ErrorType f) {
        super(f);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ErrorTypeFacade == false)
            return false;
        return f.equals(((ErrorTypeFacade)obj).f);
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
