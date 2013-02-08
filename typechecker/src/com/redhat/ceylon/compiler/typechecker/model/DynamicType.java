package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.producedType;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.Arrays;
import java.util.List;

public class DynamicType extends Class {
    
    public DynamicType(Unit unit) {
        this.unit = unit;
    }
    
    @Override
    public String getName() {
        return "Dynamic";
    }
    
    @Override
    public Scope getContainer() {
    	return unit.getAnythingDeclaration().getContainer();
    }
    
    @Override @Deprecated
    public List<String> getQualifiedName() {
        return Arrays.asList("ceylon","language","Dynamic");
    }
    
    @Override
    public String getQualifiedNameString() {
        return "ceylon.language::Dynamic";
    }
    
    @Override
    public ParameterList getParameterList() {
        return new ParameterList();
    }
    
    @Override
    public List<ParameterList> getParameterLists() {
        return singletonList(getParameterList());
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
    @Override
    public boolean isShared() {
        return true;
    }
    
    @Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.TYPE;
    }
    
    @Override
    public boolean equals(Object object) {
    	return object instanceof DynamicType;
    }
    
    private Value value(String name) {
        Value value = new Value();
        value.setName(name);
        value.setVariable(true);
        value.setShared(true);
        value.setContainer(this);
        value.setType(unit.getDynamicDeclaration().getType());
        return value;
    }
    
    @Override
    public Declaration getDirectMember(String name,
            List<ProducedType> signature, boolean ellipsis) {
        return value(name);
    }
    
    @Override
    public Declaration getDirectMemberOrParameter(String name,
            List<ProducedType> signature, boolean ellipsis) {
        return value(name);
    }
    
    @Override
    public ProducedType getExtendedType() {
        return unit.getObjectDeclaration().getType();
    }
    
    @Override
    public List<ProducedType> getSatisfiedTypes() {
        ProducedType dt = unit.getDynamicDeclaration().getType();
        ProducedType ct = producedType(unit.getCallableDeclaration(), 
                dt, unit.getTupleType(singletonList(dt), true, false, -1));
        ProducedType at = producedType(unit.getArrayDeclaration(), dt);
        return asList(ct, at);
    }
    
}
