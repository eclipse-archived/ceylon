package com.redhat.ceylon.compiler.model;

import com.redhat.ceylon.compiler.tree.Tree;

import java.util.ArrayList;
import java.util.List;

public class Type extends Model {
	
	List<Type> typeArguments = new ArrayList<Type>();
	GenericType genericType;
	
	public GenericType getGenericType() {
		return genericType;
	}
	
	public void setGenericType(GenericType type) {
		this.genericType = type;
	}
	
	public List<Type> getTypeArguments() {
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
			for (Type t: typeArguments) {
				producedTypeName+=t.getProducedTypeName();
			}
			producedTypeName+=">";
		}
		return producedTypeName;
	}
	
}
