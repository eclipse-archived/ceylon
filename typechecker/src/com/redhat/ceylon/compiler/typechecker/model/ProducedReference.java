package com.redhat.ceylon.compiler.typechecker.model;

import java.util.Collections;
import java.util.Map;

/**
 * A produced type or produced reference to a
 * method or attribute
 *
 * @author Gavin King
 */
public abstract class ProducedReference {

    ProducedReference() {}

    private Map<TypeParameter, ProducedType> typeArguments = Collections.emptyMap();
    private Declaration declaration;
    private ProducedType declaringType;

    public ProducedType getDeclaringType() {
        return declaringType;
    }

    public void setDeclaringType(ProducedType declaringType) {
        this.declaringType = declaringType;
    }

    public Declaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(Declaration type) {
        this.declaration = type;
    }

    public Map<TypeParameter, ProducedType> getTypeArguments() {
        return typeArguments;
    }

    public void setTypeArguments(Map<TypeParameter, ProducedType> typeArguments) {
        this.typeArguments = typeArguments;
    }

    public abstract ProducedType getType();

    public boolean isFunctional() {
        return declaration instanceof Functional;
    }

    public ProducedTypedReference getTypedParameter(Parameter p) {
        ProducedTypedReference ptr = new ProducedTypedReference();
        ptr.setDeclaration(p);
        ptr.setDeclaringType(getDeclaringType());
        ptr.setTypeArguments(getTypeArguments());
        return ptr;
    }
    
}
