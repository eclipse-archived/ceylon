package com.redhat.ceylon.model.typechecker.model;

import java.util.List;
import java.util.Objects;

public class Interface extends ClassOrInterface {

    private String javaCompanionClassName;
    private Boolean companionClassNeeded;
    
    @Override
    public boolean isAbstract() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Empty");
    }

    @Override
    public boolean isSequence() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Sequence");
    }

    @Override
    public boolean isSequential() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Sequential");
    }

    @Override
    public boolean isIterable() {
        return Objects.equals(getQualifiedNameString(),
                "ceylon.language::Iterable");
    }

    @Override
    public boolean inherits(TypeDeclaration dec) {
        if (dec.isAnything() || dec.isObject()) {
            return true;
        }
        if (dec instanceof Interface && equals(dec)) {
            return true;
        }
        else {
            //TODO: optimize this to avoid walking the
            //      same supertypes multiple times
            if (dec instanceof Interface) {
                List<Type> sts = getSatisfiedTypes();
                for (int i = 0, s=sts.size(); i<s; i++) {
                    Type st = sts.get(i);
                    if (st.getDeclaration().inherits(dec)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public void setJavaCompanionClassName(String name) {
        this.javaCompanionClassName = name;
    }
    
    public String getJavaCompanionClassName() {
        return javaCompanionClassName;
    }

    public Boolean isCompanionClassNeeded() {
        return companionClassNeeded;
    }

    public void setCompanionClassNeeded(Boolean companionClassNeeded) {
        this.companionClassNeeded = companionClassNeeded;
    }
    
    @Override
    public String toString() {
        return "interface " + toStringName();
    }
    
}
