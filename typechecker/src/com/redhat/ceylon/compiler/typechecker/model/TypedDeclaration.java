package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.arguments;

import java.util.List;


/**
 * Anything which includes a type declaration: 
 * a method, attribute, parameter, or local.
 * @author Gavin King
 *
 */
public abstract class TypedDeclaration extends Declaration {
	
	ProducedType type;

	public ProducedType getType() {
		return type;
	}
	
	public void setType(ProducedType type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
	    if (type==null) {
	        return super.toString();
	    }
	    else {
	        return super.toString().replace(']', ':') + 
	        type.getProducedTypeName() + "]";
	    }
	}

    public boolean acceptsArguments(List<ProducedType> typeArguments) {
        return typeArguments.isEmpty();
    }
    
    public ProducedTypedReference getProducedTypedReference(ProducedType pt, List<ProducedType> typeArguments) {
        if (!acceptsArguments(typeArguments)) {
            throw new RuntimeException( getName() + 
                    " does not accept given type arguments");
        }
        ProducedTypedReference ptr = new ProducedTypedReference();
        ptr.setDeclaration(this);
        ptr.setDeclaringType(pt);
        ptr.setTypeArguments(arguments(this, pt, typeArguments));
        return ptr;
    }
    
    public boolean isMember() {
        return getContainer() instanceof ClassOrInterface; 
    }
    
    public boolean isVariable() {
        return false;
    }
    
}
