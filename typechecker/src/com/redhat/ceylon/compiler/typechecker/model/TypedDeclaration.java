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

    private ProducedType type;
    private boolean uncheckedNullType = false;
    private Boolean unboxed;
    private boolean typeErased;
    
    private TypedDeclaration originalDeclaration;
    
    public TypeDeclaration getTypeDeclaration() {
    	if (type==null) {
    	    return null;
    	}
    	else {
            return type.getDeclaration();
    	}
    }

    public ProducedType getType() {
        return type;
    }

    public void setType(ProducedType t) {
        this.type = t;
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
     *                       type or null if this is 
     *                       not a nested type dec
     * @param typeArguments arguments to the type
     *                      parameters of this 
     *                      declaration
     */
    public ProducedTypedReference getProducedTypedReference(ProducedType qualifyingType,
            List<ProducedType> typeArguments) {
    	//TODO: take into account sequenced type arguments
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
    
    public boolean isLate() {
    	return false;
    }
    
    public TypedDeclaration getOriginalDeclaration() {
        return originalDeclaration;
    }
    
    public void setOriginalDeclaration(TypedDeclaration originalDeclaration) {
        this.originalDeclaration = originalDeclaration;
    }

    public Boolean getUnboxed() { 
        return unboxed; 
    }

    public void setUnboxed(Boolean value) { 
        unboxed = value; 
    }

    public Boolean getTypeErased() { 
        return typeErased; 
    }

    public void setTypeErased(Boolean typeErased) { 
        this.typeErased = typeErased; 
    }

    public boolean hasUncheckedNullType() {
        return uncheckedNullType;
    }
    
    public void setUncheckedNullType(boolean uncheckedNullType) {
        this.uncheckedNullType = uncheckedNullType;
    }
    
}
