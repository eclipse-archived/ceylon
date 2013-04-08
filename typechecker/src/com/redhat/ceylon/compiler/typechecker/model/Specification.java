package com.redhat.ceylon.compiler.typechecker.model;

public class Specification extends Element implements Scope {
    
    private int id;
    private TypedDeclaration declaration;
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Specification) {
            Specification that = (Specification) obj;
            return id==that.id && 
                    getContainer().equals(that.getContainer());
        }
        else {
            return false;
        }
    }

    public TypedDeclaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(TypedDeclaration declaration) {
        this.declaration = declaration;
    }

}
