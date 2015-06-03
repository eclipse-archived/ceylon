package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getTypeArgumentMap;

import java.util.List;
import java.util.Objects;


/**
 * Anything which includes a type declaration:
 * a method, attribute, parameter, or local.
 *
 * @author Gavin King
 */
public abstract class TypedDeclaration extends Declaration {

    private Type type;
    private boolean uncheckedNullType = false;
    private Boolean unboxed;
    private boolean typeErased;
    private boolean untrustedType;
    private boolean dynamicallyTyped;
    
    private TypedDeclaration originalDeclaration;
    
    public boolean isDynamicallyTyped() {
        return dynamicallyTyped;
    }
    
    public void setDynamicallyTyped(boolean isDynamicallyTyped) {
        this.dynamicallyTyped = isDynamicallyTyped;
    }
        
    public TypeDeclaration getTypeDeclaration() {
    	if (type==null) {
    	    return null;
    	}
    	else {
            return type.getDeclaration();
    	}
    }

    public Type getType() {
        return type;
    }

    public void setType(Type t) {
        this.type = t;
    }

    public TypedReference appliedTypedReference(
            Type qualifyingType,
            List<Type> typeArguments) {
        return appliedTypedReference(qualifyingType, 
                typeArguments, false);
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
     * @param assignment the reference occurs on the
     *                   LHS of an assignment
     */
    public TypedReference appliedTypedReference(
            Type qualifyingType,
            List<Type> typeArguments, 
            boolean assignment) {
        TypedReference ptr = 
                new TypedReference(!assignment, 
                        assignment);
        ptr.setDeclaration(this);
        ptr.setQualifyingType(qualifyingType);
        ptr.setTypeArguments(getTypeArgumentMap(this, 
                qualifyingType, typeArguments));
        return ptr;
    }
    
    /**
     * The type of this declaration, as viewed from within
     * itself.
     */
    public TypedReference getTypedReference() {
        TypedReference ptr =
                new TypedReference(true, false);
        ptr.setQualifyingType(getMemberContainerType());
        ptr.setDeclaration(this);
        ptr.setTypeArguments(getTypeParametersAsArguments());
        return ptr;
    }

    @Override
    public Reference appliedReference(Type pt, 
            List<Type> typeArguments) {
        return appliedTypedReference(pt, typeArguments);
    }
    
    @Override
    public final Reference getReference() {
    	return getTypedReference();
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

    public Boolean getUntrustedType() { 
        return untrustedType; 
    }

    public void setUntrustedType(Boolean untrustedType) { 
        this.untrustedType = untrustedType; 
    }

    public boolean hasUncheckedNullType() {
        return uncheckedNullType;
    }
    
    public void setUncheckedNullType(boolean uncheckedNullType) {
        this.uncheckedNullType = uncheckedNullType;
    }

    @Override
    protected int hashCodeForCache() {
        int ret = 17;
        Scope container = getContainer();
        if (container instanceof Declaration) {
            ret = (37 * ret) + ((Declaration) container).hashCodeForCache();
        }
        else {
            ret = (37 * ret) + container.hashCode();
        }
        String qualifier = getQualifier();
        ret = (37 * ret) + (qualifier == null ? 0 : qualifier.hashCode());
        ret = (37 * ret) + Objects.hashCode(getName());
        return ret;
    }

    @Override
    protected boolean equalsForCache(Object o) {
        if (o == null || o instanceof TypedDeclaration == false) {
            return false;
        }
        TypedDeclaration b = (TypedDeclaration) o;
        Scope container = getContainer();
        if (container instanceof Declaration) {
            if (!((Declaration) container).equalsForCache(b.getContainer())) {
                return false;
            }
        }
        else {
            if (!container.equals(b.getContainer())) {
                return false;
            }
        }
        if (!Objects.equals(getQualifier(), b.getQualifier())) {
            return false;
        }
        return Objects.equals(getName(), b.getName());
    }

    // implemented in Value
    public boolean isSelfCaptured() {
        return false;
    }
}
