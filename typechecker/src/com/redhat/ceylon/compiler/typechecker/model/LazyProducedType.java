package com.redhat.ceylon.compiler.typechecker.model;

import java.util.Map;

public abstract class LazyProducedType extends ProducedType {
    private boolean init;
    private Unit unit;
    public LazyProducedType(Unit unit) {
        this.unit = unit;
    }
    @Override
    public TypeDeclaration getDeclaration() {
        if (super.getDeclaration()==null) {
            if (init) {
                //reentrant!
                return new UnknownType(unit);
            }
            init=true;
            TypeDeclaration td = initDeclaration();
            if (td==null) {
                setDeclaration(new UnknownType(unit));
            }
            else {
                setDeclaration(td);
                setTypeArguments(initTypeArguments());
            }
        }
        return super.getDeclaration();
    }
    @Override
    public Map<TypeParameter, ProducedType> getTypeArguments() {
        getDeclaration();//force initialization
        return super.getTypeArguments();
    }
    public abstract Map<TypeParameter, ProducedType> initTypeArguments();
    public abstract TypeDeclaration initDeclaration();
}
