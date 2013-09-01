package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ControlBlock extends Element implements Scope {
    
    private List<Declaration> members = new ArrayList<Declaration>(3);
    
    @Override
    public List<Declaration> getMembers() {
        return members;
    }
    
    private Set<Value> specifiedValues;
    
    private int id;
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ControlBlock) {
            ControlBlock that = (ControlBlock) obj;
            return id==that.id && 
                    getContainer().equals(that.getContainer());
        }
        else {
            return false;
        }
    }

    public Set<Value> getSpecifiedValues() {
        return specifiedValues;
    }

    public void setSpecifiedValues(Set<Value> assigned) {
        this.specifiedValues = assigned;
    }
    
    
}
