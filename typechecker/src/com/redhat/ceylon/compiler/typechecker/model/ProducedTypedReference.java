package com.redhat.ceylon.compiler.typechecker.model;


/**
 * A produced reference to a method or 
 * attribute with actual type arguments.
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
        TypedDeclaration d = getDeclaration();
        ProducedType t = d==null ? null : d.getType();
        if (t==null) {
            return null;
        }
        else {
            return t.substitute(getTypeArguments()); //the type arguments to the member
        }
    }
    
}
