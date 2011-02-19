package com.redhat.ceylon.compiler.model;

import java.util.Map;


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

    public ProducedTypedReference substitute(Map<TypeParameter,ProducedType> substitutions) {
        ProducedTypedReference ptr = new ProducedTypedReference();
        ptr.setDeclaration(getDeclaration());
        ptr.setTypeArguments(sub(substitutions));
        return ptr;
    }

    public ProducedTypedReference getTypedParameter(Parameter td) {
        ProducedTypedReference ptr = new ProducedTypedReference();
        ptr.setDeclaration(td);
        ptr.setTypeArguments(getTypeArguments());
        ptr.setDeclaringType(getDeclaringType());
        return ptr.substitute(getTypeArguments());
    }
         
}
