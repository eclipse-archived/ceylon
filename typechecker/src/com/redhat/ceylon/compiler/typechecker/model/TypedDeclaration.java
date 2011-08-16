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
     * @param qualifyingType the qualifying produced
     *        type or null if this is not a
     *        nested type declaration
     * @param typeArguments arguments to the type
     *        parameters of this declaration
     */
    public ProducedTypedReference getProducedTypedReference(ProducedType qualifyingType,
            List<ProducedType> typeArguments) {
        ProducedTypedReference ptr = new ProducedTypedReference();
        ptr.setDeclaration(this);
        ptr.setQualifyingType(qualifyingType);
        ptr.setTypeArguments(arguments(this, qualifyingType, typeArguments));
        return ptr;
    }

    @Override
    public ProducedReference getProducedReference(ProducedType pt,
            List<ProducedType> typeArguments) {
        return getProducedTypedReference(pt, typeArguments);
    }

    @Override
    public boolean isMember() {
        return getContainer() instanceof ClassOrInterface;
    }

    public boolean isVariable() {
        return false;
    }

}
