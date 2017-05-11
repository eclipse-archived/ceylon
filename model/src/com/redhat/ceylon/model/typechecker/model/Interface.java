package com.redhat.ceylon.model.typechecker.model;

import java.util.List;

public class Interface extends ClassOrInterface {

    private static final int ITERABLE = 1<<1;
    private static final int EMPTY = 1<<2;
    private static final int SEQUENCE = 1<<3;
    private static final int SEQUENTIAL = 1<<4;
    private static final int CALLABLE = 1<<5;
    private static final int IDENTIFIABLE = 1<<6;
    
    private String javaCompanionClassName;
    private Boolean companionClassNeeded;
    
    @Override
    public boolean isAbstract() {
        return true;
    }
    
    private int code;
    
    private void setCode() {
        if (code==0) {
            Scope scope = getContainer();
            if (scope instanceof Package) {
                Package p = (Package) scope;
                if (p.isLanguagePackage()) {
                    String name = getName();
                    if (name!=null) {
                        switch (name) {
                        case "Iterable":
                            code = ITERABLE; break;
                        case "Empty":
                            code = EMPTY; break;
                        case "Sequence":
                            code = SEQUENCE; break;
                        case "Sequential":
                            code = SEQUENTIAL; break;
                        case "Callable":
                            code = CALLABLE; break;
                        case "Identifiable":
                            code = IDENTIFIABLE; break;
                        default:
                            code = -1;
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public boolean isEmpty() {
        setCode();
        return code == EMPTY;
    }

    @Override
    public boolean isSequence() {
        setCode();
        return code == SEQUENCE;
    }

    @Override
    public boolean isSequential() {
        setCode();
        return code == SEQUENTIAL;
    }

    @Override
    public boolean isIterable() {
        setCode();
        return code == ITERABLE;
    }

    @Override
    public boolean isCallable() {
        setCode();
        return code == CALLABLE;
    }
    
    @Override
    public boolean isIdentifiable() {
        setCode();
        return code == IDENTIFIABLE;
    }
    
    @Override
    public boolean inherits(TypeDeclaration dec) {
        if (dec==null) {
            return false;
        }
        else if (dec.isAnything() || dec.isObject()) {
            return true;
        }
        else if (dec instanceof Class) {
            //interface can't inherit any other class
            return false;
        }
        else if (dec instanceof Interface && equals(dec)) {
            return true;
        }
        else {
            //TODO: optimize this to avoid walking the
            //      same supertypes multiple times
            if (dec instanceof Interface) {
                List<Type> sts = getSatisfiedTypes();
                for (int i=0, s=sts.size(); i<s; i++) {
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
    public boolean isEmptyType() {
        return isEmpty();
    }
    
    @Override
    public boolean isSequentialType() {
        return isSequential() || isSequence() || isEmpty();
    }
    
    @Override
    public boolean isSequenceType() {
        return isSequence();
    }
    
    @Override
    public String toString() {
        return "interface " + toStringName();
    }
    
}
