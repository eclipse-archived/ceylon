/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.typechecker.model;

import static org.eclipse.ceylon.model.typechecker.model.DeclarationFlags.TypedDeclarationFlags.*;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getTypeArgumentMap;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import org.eclipse.ceylon.model.loader.model.AnnotationTarget;


/**
 * Anything which includes a type declaration:
 * a method, attribute, parameter, or local.
 *
 * @author Gavin King
 */
public abstract class TypedDeclaration 
        extends Declaration implements Typed {
    
    private Type type;
    
    private TypedDeclaration originalDeclaration;
    
    /**
     * Whether this declaration is dynamically typed, that is,
     * has the {@code dynamic} keyword as return type.
     */
    public boolean isDynamicallyTyped() {
        return (flags&DYNAMICALLY_TYPED)!=0;
    }
    
    /**
     * @see #isDynamicallyTyped()
     */
    public void setDynamicallyTyped(boolean dynamicallyTyped) {
        if (dynamicallyTyped) {
            flags|=DYNAMICALLY_TYPED;
        }
        else {
            flags&=(~DYNAMICALLY_TYPED);
        }
    }

    public TypeDeclaration getTypeDeclaration() {
        return type==null ? null : type.getDeclaration();
    }
    
    @Override
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
    
    public void setOriginalDeclaration(
            TypedDeclaration originalDeclaration) {
        this.originalDeclaration = originalDeclaration;
    }

    public Boolean getUnboxed() {
        if ((flags&UNBOXED_KNOWN)==0) {
            return null;
        }
        else {
            return (flags&UNBOXED)!=0;
        }
    }

    public void setUnboxed(Boolean value) { 
        if (value==null) {
            flags&=(~UNBOXED_KNOWN);
        }
        else {
            flags|=UNBOXED_KNOWN;
            if (value) {
                flags|=UNBOXED;
            }
            else {
                flags&=(~UNBOXED);
            }
        }
    }

    public Boolean getTypeErased() { 
        return (flags&TYPE_ERASED)!=0; 
    }

    public void setTypeErased(Boolean typeErased) { 
        if (typeErased) {
           flags|=TYPE_ERASED; 
        }
        else {
            flags&=(~TYPE_ERASED);
        }
    }

    public Boolean getUntrustedType() { 
        if ((flags&UNTRUSTED_KNOWN)==0) {
            return null;
        }
        return (flags&UNTRUSTED_TYPE)!=0; 
    }

    public void setUntrustedType(Boolean untrustedType) { 
        if (untrustedType==null) {
            flags&=(~UNTRUSTED_KNOWN);
        }else{
            flags|=UNTRUSTED_KNOWN;
            if (untrustedType) {
                flags|=UNTRUSTED_TYPE; 
            }
            else {
                flags&=(~UNTRUSTED_TYPE);
            }
        }
    }

    public boolean hasUncheckedNullType() {
        return (flags&UNCHECKED_NULL)!=0;
    }
    
    public void setUncheckedNullType(boolean uncheckedNullType) {
        if (uncheckedNullType) {
            flags|=UNCHECKED_NULL; 
         }
         else {
             flags&=(~UNCHECKED_NULL);
         }
    }

    @Override
    protected int hashCodeForCache() {
        int ret = 17;
        Scope container = getContainer();
        if (container instanceof Declaration) {
            Declaration dec = (Declaration) container;
            ret = (37 * ret) + dec.hashCodeForCache();
        }
        else {
            ret = (37 * ret) + container.hashCode();
        }
        String qualifier = getQualifier();
        ret = (37 * ret) + (qualifier == null ? 
                0 : qualifier.hashCode());
        ret = (37 * ret) + Objects.hashCode(getName());
        return ret;
    }

    @Override
    protected boolean equalsForCache(Object o) {
        if (o==this) {
            return true;
        }
        if (o == null || !(o instanceof TypedDeclaration)) {
            return false;
        }
        if (isOverloaded() && o!=this) {
            //necessary, since we don't want to
            //go comparing parameter lists!
            return false;
        }
        TypedDeclaration b = (TypedDeclaration) o;
        Scope container = getContainer();
        if (container instanceof Declaration) {
            Declaration dec = (Declaration) container;
            if (!dec.equalsForCache(b.getContainer())) {
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
    
    public EnumSet<AnnotationTarget> getAnnotationTargets() {
        return null;
    }
}
