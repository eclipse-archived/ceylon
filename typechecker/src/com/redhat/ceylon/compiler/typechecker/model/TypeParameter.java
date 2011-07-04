package com.redhat.ceylon.compiler.typechecker.model;

import java.util.Collections;
import java.util.List;

public class TypeParameter extends TypeDeclaration implements Functional {

    boolean covariant;
    boolean contravariant;
    Declaration declaration;
    ParameterList parameterList;

    public boolean isCovariant() {
        return covariant;
    }

    public void setCovariant(boolean covariant) {
        this.covariant = covariant;
    }

    public boolean isContravariant() {
        return contravariant;
    }

    public void setContravariant(boolean contravariant) {
        this.contravariant = contravariant;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    public ParameterList getParameterList() {
        return parameterList;
    }

    public void setParameterList(ParameterList parameterList) {
        this.parameterList = parameterList;
    }

    @Override
    public List<ParameterList> getParameterLists() {
        if (parameterList==null) {
            return Collections.emptyList();
        }
        else {
            return Collections.singletonList(parameterList);
        }
    }

    @Override
    public void addParameterList(ParameterList pl) {
        parameterList = pl;
    }

}
