package com.redhat.ceylon.compiler.typechecker.model;

import java.util.Collections;
import java.util.HashMap;
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
    private ProducedType qualifyingType;

    public ProducedType getQualifyingType() {
        return qualifyingType;
    }
    
    void setQualifyingType(ProducedType qualifyingType) {
        this.qualifyingType = qualifyingType;
    }
    
    public Declaration getDeclaration() {
        return declaration;
    }
    
    void setDeclaration(Declaration type) {
        this.declaration = type;
    }
    
    public Map<TypeParameter, ProducedType> getTypeArguments() {
        //convoluted implementation here is because we
        //can't cache the result due to the lifecyle
        //of defaulted type arguments
        if (declaration instanceof Generic) {
            Map<TypeParameter, ProducedType> result = typeArguments;
            for (TypeParameter pt: ((Generic) declaration).getTypeParameters()) {
                ProducedType dta = pt.getDefaultTypeArgument();
                if (dta!=null) {
                    if (!typeArguments.containsKey(pt)) {
                        result = new HashMap<TypeParameter,ProducedType>(result);
                        result.put(pt, dta.substitute(result));
                    }
                }
            }
            return result;
        }
        else {
            return typeArguments;
        }
    }
    
    void setTypeArguments(Map<TypeParameter, ProducedType> typeArguments) {
        this.typeArguments = typeArguments;
    }
        
    public abstract ProducedType getType();
    
    public ProducedType getFullType() {
    	return getFullType(getType());
    }

    public ProducedType getFullType(ProducedType wrappedType) {
    	if (getDeclaration() instanceof Functional) {
    		return getDeclaration().getUnit().getCallableType(this, wrappedType);
    	}
    	else {
    		return wrappedType;
    	}
    }
    
    public boolean isFunctional() {
        return declaration instanceof Functional;
    }

    /**
     * Get the type of a parameter, after substitution of
     * type arguments.
     */
    public ProducedTypedReference getTypedParameter(Parameter p) {
        ProducedTypedReference ptr = new ProducedTypedReference();
        ptr.setDeclaration(p.getModel());
        ptr.setQualifyingType(getQualifyingType());
        ptr.setTypeArguments(getTypeArguments());
        return ptr;
    }
    
}
