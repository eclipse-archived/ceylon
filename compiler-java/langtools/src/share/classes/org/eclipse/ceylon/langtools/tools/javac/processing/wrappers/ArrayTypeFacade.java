package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import org.eclipse.ceylon.javax.lang.model.type.ArrayType;

public class ArrayTypeFacade extends ReferenceTypeFacade implements javax.lang.model.type.ArrayType {

    public ArrayTypeFacade(ArrayType f) {
        super(f);
    }

    @Override
    public javax.lang.model.type.TypeMirror getComponentType() {
        return Facades.facade(((ArrayType)f).getComponentType());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ArrayTypeFacade == false)
            return false;
        return f.equals(((ArrayTypeFacade)obj).f);
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
