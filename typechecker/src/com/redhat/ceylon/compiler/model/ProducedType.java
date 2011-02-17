package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class ProducedType extends Model {
	
	List<ProducedType> typeArguments = new ArrayList<ProducedType>();
	TypeDeclaration genericType;
	
	public TypeDeclaration getGenericType() {
		return genericType;
	}
	
	public void setGenericType(TypeDeclaration type) {
		this.genericType = type;
	}
	
	public List<ProducedType> getTypeArguments() {
		return typeArguments;
	}
	
	@Override
	public String toString() {
		return "Type[" + getProducedTypeName() + "]";
	}

	public String getProducedTypeName() {
        if (genericType == null) {
            //unknown type
            return null;
        }
		String producedTypeName = genericType.getName();
		if (!typeArguments.isEmpty()) {
			producedTypeName+="<";
			for (ProducedType t: typeArguments) {
				producedTypeName+=t.getProducedTypeName();
			}
			producedTypeName+=">";
		}
		return producedTypeName;
	}
	
	public boolean isExactly(ProducedType that) {
	    if (that.genericType!=genericType) {
	        return false;
	    }
	    if (that.typeArguments.size()!=typeArguments.size()) {
	        return false;
	    }
	    for (int i=0; i<typeArguments.size(); i++) {
	        if ( !that.typeArguments.get(i).isExactly(typeArguments.get(i)) ) {
	            return false;
	        }
	    }
	    return true;
	}
	
}
