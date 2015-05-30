package com.redhat.ceylon.model.typechecker.model;

import java.util.Map;

public abstract class LazyProducedType extends Type {
    
    private boolean initialized;
    private Unit unit;
    
    public LazyProducedType(Unit unit) {
        this.unit = unit;
    }
    
    @Override
    public TypeDeclaration getDeclaration() {
        if (initialized) {
            TypeDeclaration dec = super.getDeclaration();
            if (dec == null) {
                //reentrant during lazy initialization!
                return new UnknownType(unit);
            }
            else {
                return dec;
            }
        }
        else {
            initialized=true;
            TypeDeclaration dec = initDeclaration();
            if (dec==null) {
                return new UnknownType(unit);
            }
            else {
                setDeclaration(dec);
                setTypeArguments(initTypeArguments());
                setQualifyingType(initQualifyingType());
                return dec; 
            }
        }
    }
    
    @Override
    public Map<TypeParameter, Type> getTypeArguments() {
        getDeclaration();//force initialization
        return super.getTypeArguments();
    }
    
    public abstract Map<TypeParameter, Type> initTypeArguments();
    
    public abstract TypeDeclaration initDeclaration();
    
    public Type initQualifyingType() {
        return null;
    }
    
}
