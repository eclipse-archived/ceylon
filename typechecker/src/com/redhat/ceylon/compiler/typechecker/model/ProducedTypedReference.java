package com.redhat.ceylon.compiler.typechecker.model;


/**
 * A callable member with actual type arguments.
 * 
 * @author Gavin King
 *
 */
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
            return t.substitute(getTypeArguments()); //the type arguments to the member
        }
    }

}
