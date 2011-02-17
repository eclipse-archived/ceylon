package com.redhat.ceylon.compiler.model;


public class ProducedType extends MemberReference {
	
	@Override
    public TypeDeclaration getDeclaration() {
		return (TypeDeclaration) declaration;
	}
	
    public TypeDeclaration getTypeDeclaration() {
        return getDeclaration();
    }
        
	public void setTypeDeclaration(TypeDeclaration td) {
	    declaration = td;
	}
		
	@Override
	public String toString() {
		return "Type[" + getProducedTypeName() + "]";
	}

	public String getProducedTypeName() {
        if (declaration == null) {
            //unknown type
            return null;
        }
		String producedTypeName = declaration.getName();
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
	    if (that.declaration!=declaration) {
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
