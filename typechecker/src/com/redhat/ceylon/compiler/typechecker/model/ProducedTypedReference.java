package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.isAbstraction;
import static com.redhat.ceylon.compiler.typechecker.model.Util.producedType;



/**
 * A produced reference to a method or 
 * attribute with actual type arguments.
 * 
 * @author Gavin King
 *
 */
public class ProducedTypedReference extends ProducedReference {
    
    private final boolean covariant;
    private final boolean contravariant;

    ProducedTypedReference(boolean covariant, boolean contravariant) {
        this.covariant = covariant;
        this.contravariant = contravariant;
    }
    
    @Override
    public TypedDeclaration getDeclaration() {
        return (TypedDeclaration) super.getDeclaration();
    }
    
    @Override
    void setDeclaration(Declaration declaration) {
        if (declaration instanceof TypedDeclaration) {
            super.setDeclaration(declaration);
        }
        else {
            throw new IllegalArgumentException("not a TypedDeclaration");
        }
    }
    
    public ProducedType getType() {
        TypedDeclaration dec = getDeclaration();
        ProducedType type = dec==null ? null : dec.getType();
        if (type==null) {
            return null;
        }
        else {
            ProducedType qt = getQualifyingType();
            if (qt!=null) {
                type = qt.applyVarianceOverrides(type, 
                        covariant, contravariant);
            }
            return type.substitute(getTypeArguments()); //the type arguments to the member
        }
    }
    
    @Override
    public ProducedType getFullType(ProducedType wrappedType) {
        if (isAbstraction(super.getDeclaration())) {
            Unit unit = getDeclaration().getUnit();
            if (getDeclaration() instanceof Functional) {
                return producedType(unit.getCallableDeclaration(), getType(),
                        new UnknownType(unit).getType());
            }
            else {
                return getType();
            }
        }
        else {
            return super.getFullType(wrappedType);
        }
    }
    
}
