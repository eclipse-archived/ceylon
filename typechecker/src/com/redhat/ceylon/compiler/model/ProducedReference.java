package com.redhat.ceylon.compiler.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ProducedReference extends Model {
    
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
	
    Map<TypeParameter, ProducedType> sub(Map<TypeParameter, ProducedType> substitutions) {
        Map<TypeParameter, ProducedType> map = new HashMap<TypeParameter, ProducedType>();
        for (Map.Entry<TypeParameter, ProducedType> e: getTypeArguments().entrySet()) {
            map.put(e.getKey(), e.getValue().substitute(substitutions));
        }
        return map;
    }
    
    public abstract ProducedType getType();
        
    public boolean isFunctional() {
        return declaration instanceof Functional;
    }
    
    public ProducedTypedReference getTypedParameter(Parameter td) {
        ProducedTypedReference ptr = new ProducedTypedReference();
        ptr.setDeclaration(td);
        ptr.setDeclaringType(getDeclaringType());
        ptr.setTypeArguments(getTypeArguments());
        return ptr;
    }
         
}
