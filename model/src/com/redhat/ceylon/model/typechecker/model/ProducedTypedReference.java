package com.redhat.ceylon.model.typechecker.model;

import java.util.Map;

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
        if (dec==null) {
            return null;
        }
        else {
            ProducedType type = dec.getType();
            if (type==null) {
                return null;
            }
            // FIXME: perhaps this should be in type.substitute?
            else if (type.isUnknown()) {
                return type;
            }
            else {
                ProducedType qt = getQualifyingType();
                if (qt!=null) {
                    type = qt.applyVarianceOverrides(type, 
                            covariant, contravariant);
                }
                //the type arguments to the member
                return type.substitute(getTypeArguments());
            }
        }
    }
    
}
