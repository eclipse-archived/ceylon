package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.arguments;

import java.util.List;


/**
 * Anything which includes a type declaration:
 * a method, attribute, parameter, or local.
 *
 * @author Gavin King
 */
public abstract class TypedDeclaration extends Declaration {

    ProducedType type;

    public TypeDeclaration getTypeDeclaration() {
        return type.getDeclaration();
    }

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

    /**
     * Get a produced reference for this declaration
     * by binding explicit or inferred type arguments
     * and type arguments of the type of which this
     * declaration is a member, in the case that this
     * is a member.
     *
     * @param outerType the qualifying produced
     * type or null if this is not a
     * nested type declaration
     * @param typeArguments arguments to the type
     * parameters of this declaration
     */
    public ProducedTypedReference getProducedTypedReference(ProducedType pt,
            List<ProducedType> typeArguments) {
        ProducedTypedReference ptr = new ProducedTypedReference();
        ptr.setDeclaration(this);
        ptr.setDeclaringType(pt);
        ptr.setTypeArguments(arguments(this, pt, typeArguments));
        return ptr;
    }

    @Override
    public ProducedReference getProducedReference(ProducedType pt,
            List<ProducedType> typeArguments) {
        return getProducedTypedReference(pt, typeArguments);
    }

    public boolean isMember() {
        return getContainer() instanceof ClassOrInterface;
    }

    public boolean isVariable() {
        return false;
    }

}
