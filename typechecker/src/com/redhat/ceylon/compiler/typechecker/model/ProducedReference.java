package com.redhat.ceylon.compiler.typechecker.model;

import static java.util.Collections.emptyMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.context.ProducedTypeCache;

/**
 * A produced type or produced reference to a
 * method or attribute
 *
 * @author Gavin King
 */
public abstract class ProducedReference {
    
    ProducedReference() {}

    private Map<TypeParameter, ProducedType> typeArguments = emptyMap();
    private Declaration declaration;
    private ProducedType qualifyingType;
    
    //cache
    private Map<TypeParameter, ProducedType> typeArgumentsWithDefaults;
    
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
        Declaration declaration = getDeclaration();
        if (declaration instanceof Generic) {
            if (typeArgumentsWithDefaults == null || 
                    !ProducedTypeCache.isEnabled()) {
                Map<TypeParameter, ProducedType> result = typeArguments;
                List<TypeParameter> typeParameters = 
                        ((Generic) declaration).getTypeParameters();
                for (int i=0, l=typeParameters.size(); i<l; i++) {
                    TypeParameter pt = typeParameters.get(i);
                    ProducedType dta = pt.getDefaultTypeArgument();
                    if (dta!=null) {
                        if (!typeArguments.containsKey(pt)) {
                            // only make a copy of typeArguments if required
                            if (typeArguments == result) {
                                // make a copy big enough to fit every type parameter
                                result = new HashMap<TypeParameter,ProducedType>(typeParameters.size());
                                result.putAll(typeArguments);
                            }
                            result.put(pt, dta.substitute(result));
                        }
                    }
                }
                typeArgumentsWithDefaults = result;
                return result;
            }
            else {
                return typeArgumentsWithDefaults;
            }
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
        return getDeclaration() instanceof Functional;
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
