package com.redhat.ceylon.compiler.typechecker.model;



public class ProducedTypedReference extends ProducedReference {
    
    ProducedTypedReference() {}
    
    @Override
    public TypedDeclaration getDeclaration() {
        return (TypedDeclaration) super.getDeclaration();
    }
    
    public ProducedType getType() {
        ProducedType t = getDeclaration().getType();
        if (t==null) {
            return null;
        }
        else {
            if (getDeclaringType()!=null) {
                t = t.substitute(getDeclaringType().getTypeArguments()); //the type arguments to the declaring type
            }
            return t.substitute(getTypeArguments()); //the type arguments to the member
        }
    }

}
