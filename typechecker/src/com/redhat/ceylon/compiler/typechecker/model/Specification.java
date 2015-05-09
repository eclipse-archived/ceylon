package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public class Specification extends Element implements Scope {
    
    private int id;
    private TypedDeclaration declaration;
    private List<Declaration> members = new ArrayList<Declaration>(3);
    
    @Override
    public List<Declaration> getMembers() {
        return members;
    }
    
    @Override
    public void addMember(Declaration declaration) {
        members.add(declaration);
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public int hashCode() {
        int ret = 17;
        ret = (31 * ret) + getContainer().hashCode();
        ret = (31 * ret) + id;
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
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
