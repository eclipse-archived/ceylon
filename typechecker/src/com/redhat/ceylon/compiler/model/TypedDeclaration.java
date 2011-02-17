package com.redhat.ceylon.compiler.model;


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

}
