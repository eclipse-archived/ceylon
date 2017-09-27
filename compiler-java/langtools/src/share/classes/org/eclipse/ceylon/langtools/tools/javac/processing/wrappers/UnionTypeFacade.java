package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.List;

import org.eclipse.ceylon.javax.lang.model.type.UnionType;

public class UnionTypeFacade extends TypeMirrorFacade implements javax.lang.model.type.UnionType {

    public UnionTypeFacade(UnionType f) {
        super(f);
    }

    @Override
    public List<? extends javax.lang.model.type.TypeMirror> getAlternatives() {
        return Facades.facadeTypeMirrorList(((UnionType)f).getAlternatives());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UnionTypeFacade == false)
            return false;
        return f.equals(((UnionTypeFacade)obj).f);
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
