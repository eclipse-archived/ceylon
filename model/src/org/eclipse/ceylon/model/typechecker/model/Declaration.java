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

import static java.util.Collections.emptyList;
import static org.eclipse.ceylon.model.typechecker.model.DeclarationFlags.*;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.EMPTY_TYPE_ARG_MAP;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.contains;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.erase;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getRealScope;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isOverloadedVersion;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.ceylon.common.Backends;

/**
 * Represents a named, annotated program element:
 * a class, interface, type parameter, parameter,
 * method, local, or attribute.
 *
 * @author Gavin King
 */
public abstract class Declaration 
        extends Element 
        implements Referenceable, Annotated, Named {
    
    private String name;
    /** See {@link DeclarationFlags} */
    protected long flags;
    private String qualifier;
    private Scope visibleScope;
    private Declaration refinedDeclaration = this;
    private String qualifiedNameAsStringCache;
    private Backends nativeBackends = Backends.ANY;
    private boolean otherInstanceReadAccess;
    private boolean otherInstanceWriteAccess;
    protected DeclarationCompleter actualCompleter;
    private List<String> aliases;
    private List<String> restrictions;
    private int memoizedHash;

    public Scope getVisibleScope() {
        return visibleScope;
    }

    public void setVisibleScope(Scope visibleScope) {
        this.visibleScope = visibleScope;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShared() {
        return (flags&SHARED)!=0;
    }

    public void setShared(boolean shared) {
        if (shared) {
            flags|=SHARED;
        }
        else {
            flags&=(~SHARED);
        }
    }

    public boolean isParameterized() {
        return false;
    }
    
    public boolean isDeprecated() {
        return (flags&DEPRECATED)!=0;
    }
    
    public void setDeprecated(boolean deprecated) {
        if (deprecated) {
            flags|=DEPRECATED;
        }
        else {
            flags&=(~DEPRECATED);
        }
    }
    
    /**
     * Whether this declaration is dynamic, that is, outside of Ceylon's control
     * (such as a dynamic interface).
     * Not to be confused with {@link TypedDeclaration#isDynamicallyTyped()}.
     */
    public boolean isDynamic() {
        return (flags&DYNAMIC)!=0;
    }
    
    /**
     * @see #isDynamic()
     */
    public void setDynamic(boolean dynamic) {
        if (dynamic) {
            flags|=DYNAMIC;
        }
        else {
            flags&=(~DYNAMIC);
        }
    }
    
    public boolean isConstructor() {
        return false;
    }

    String toStringName() {
        String name = getName();
        if (name==null) name = "";
        Scope c = getContainer();
        if (c instanceof Declaration) {
            Declaration d = (Declaration) c;
            name = d.toStringName() + "." + name;
        }
        if (isParameterized()) {
            List<TypeParameter> typeParams = 
                    getTypeParameters();
            StringBuilder params = new StringBuilder();
            params.append("<");
            boolean first = true;
            for (TypeParameter tp: typeParams) {
                if (first) {
                    first = false;
                }
                else {
                    params.append(",");
                }
                params.append(tp.getName());
            }
            params.append(">");
            name += params;
        }
        return name;
    }
    
    @Override
    public abstract String toString();
    
    @Override
    public String getQualifiedNameString() {
        if (qualifiedNameAsStringCache == null) {
            Scope container = getContainer();
            String qualifier = 
                    container.getQualifiedNameString();
            String name = getName();
            if (name==null) {
                name = "";
            }
            if (qualifier==null || qualifier.isEmpty()) {
                qualifiedNameAsStringCache = name; 
            }
            else if (container instanceof Package) {
                qualifiedNameAsStringCache = 
                        qualifier + "::" + name;
            }
            else {
                qualifiedNameAsStringCache = 
                        qualifier + "." + name;
            }
        }
        return qualifiedNameAsStringCache;
    }
    
    public boolean isAnnotation() {
        return (flags&ANNOTATION)!=0;
    }
    
    public void setAnnotation(boolean annotation) {
        if (annotation) {
            flags|=ANNOTATION;
        }
        else {
            flags&=(~ANNOTATION);
        }
    }

    public boolean isActual() {
        if (actualCompleter != null) {
            completeActual();
        }
        return (flags&ACTUAL)!=0;
    }

    public void setActual(boolean actual) {
        if (actual) {
            flags|=ACTUAL;
        }
        else {
            flags&=(~ACTUAL);
        }
    }

    public boolean isFormal() {
        return (flags&FORMAL)!=0;
    }
    
    public void setFormal(boolean formal) {
        if (formal) {
            flags|=FORMAL;
        }
        else {
            flags&=(~FORMAL);
        }
    }

    public boolean isNative() {
        return !getNativeBackends().none();
    }
    
    public boolean isNativeHeader() {
        return getNativeBackends().header();
    }
    
    public boolean isNativeImplementation() {
        return isNative() && !isNativeHeader();
    }
    
    public Backends getNativeBackends() {
        return nativeBackends;
    }
    
    public void setNativeBackends(Backends backends) {
        this.nativeBackends=backends;
    }
    
    /** 
     * @returns true if the JVM backend has not 
     * emitted code for this declaration due to 
     * an error in the declaration, or when the 
     * typechecker instructs the backend to not 
     * emit code for some inscrutible reason of 
     * its own.
     */
    public boolean isDropped() {
        return (flags&DROPPED)!=0;
    }
    
    public void setDropped(boolean dropped) {
        if (dropped) {
            flags|=DROPPED;
        }
        else {
            flags&=(~DROPPED);
        }
    }

    public Backends getScopedBackends() {
        Backends backends = getNativeBackends();
        if (backends.none()) {
            return getScope().getScopedBackends();
        } else {
            return backends;
        }
    }

    public boolean isDefault() {
        return (flags&DEFAULT)!=0;
    }

    public void setDefault(boolean def) {
        if (def) {
            flags|=DEFAULT;
        }
        else {
            flags&=(~DEFAULT);
        }
    }
    
    public Declaration getRefinedDeclaration() {
        if (actualCompleter != null) {
            completeActual();
        }
        return refinedDeclaration;
    }
    
    protected void completeActual() {
        DeclarationCompleter completer = actualCompleter;
        actualCompleter = null;
        completer.completeActual(this);
    }

    public void setRefinedDeclaration(Declaration refinedDeclaration) {
        this.refinedDeclaration = refinedDeclaration;
    }
    
    /**
     * Determine if this declaration is visible in the 
     * given scope, by considering if it is shared or 
     * directly defined in a containing scope.
     */
    public boolean isVisible(Scope scope) {
        Scope vs = getVisibleScope();
        return (vs==null || contains(vs, scope)) 
            && withinRestrictions(scope);
        /*
        * Note that this implementation is not quite
        * right, since for a shared member
        * declaration it does not check if the
        * containing declaration is also visible in
        * the given scope, but this is okay for now
        * because of how this method is used.
        */
        /*if (isShared()) {
            return true;
        }
        else {
            return isDefinedInScope(scope);
        }*/
    }

    private boolean withinRestrictions(Scope scope) {
        if (scope==null) {
            //TODO: this is kinda rubbish
            return true; //getRestrictions()==null;
        }
        else if (scope instanceof Package) {
            return withinRestrictions((Package) scope);
        }
        else {
            return withinRestrictions(scope.getUnit());
        }
    }

    public boolean withinRestrictions(Unit unit) {
        return withinRestrictions(unit.getPackage());
    }

    private boolean withinRestrictions(Package scope) {
        return getUnit().getPackage().equals(scope)
            || withinRestrictions(scope.getModule());
    }
    
    private boolean withinRestrictions(Module module) {
        List<String> restrictions = getRestrictions();
        if (restrictions==null) {
            return true;
        }
        else {
            String name = module.getNameAsString();
            for (String mod: restrictions) {
                if (name.equals(mod)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Determine if this declaration is directly defined in 
     * the given scope or in a containing scope of the given 
     * scope (and is not visible only due to inheritance). 
     */
    public boolean isDefinedInScope(Scope scope) {
        return contains(
                // call getRealScope() to
                // account for weird visibility 
                // rules for ConditionScopes, 
                // i.e. this declaration might
                // be visible from outside its
                // own ConditionScope
                getRealScope(getContainer()),
                scope);
    }
    
    public boolean isCaptured() {
        return false;
    }

    public boolean isJsCaptured() {
        return false;
    }

    public boolean isToplevel() {
        return getContainer() instanceof Package;
    }

    public boolean isClassMember() {
        return getContainer() instanceof Class;
    }

    public boolean isInterfaceMember() {
        return getContainer() instanceof Interface;
    }

    /**
     * Is this declaration nested inside a class or 
     * interface body. It might not be enough to make it a 
     * "member" in the strict language spec sense of the 
     * word.
     *  
     * @see Declaration#isMember()
     */
    public boolean isClassOrInterfaceMember() {
        return getContainer() instanceof ClassOrInterface;
    }
    
    /**
     * Is this declaration a member in the strict language
     * spec sense of the word, that is, is it a polymorphic
     * kind of declaration (an attribute, method, or class)
     * 
     * @see Declaration#isClassOrInterfaceMember()
     */
    public boolean isMember() {
        return false;
    }
    
    @Override
    public List<Annotation> getAnnotations() {
        return emptyList();
    }
    
    @Deprecated
    public boolean isStaticallyImportable() {
        return isStatic();
    }
    
    public boolean isStatic() {
        return (flags&STATIC)!=0;
    }
    
    public void setStatic(boolean staticallyImportable) {
        if (staticallyImportable) {
            flags|=STATIC;
        }
        else {
            flags&=(~STATIC);
        }
    }
    
    public boolean isProtectedVisibility() {
        return (flags&PROTECTED)!=0;
    }
    
    public void setProtectedVisibility(boolean protectedVisibility) {
        if (protectedVisibility) {
            flags|=PROTECTED;
        }
        else {
            flags&=(~PROTECTED);
        }
    }
    
    public boolean isPackageVisibility() {
        return (flags&PACKAGE)!=0;
    }
    
    public void setPackageVisibility(boolean packageVisibility) {
        if (packageVisibility) {
            flags|=PACKAGE;
        }
        else {
            flags&=(~PACKAGE);
        }
    }

    public boolean isVariadic() {
        return false;
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
    public abstract Reference appliedReference(Type pt, 
            List<Type> typeArguments);
    
    /**
     * Obtain a reference to this declaration, as seen 
     * within the body of the declaration itself. That is,
     * with its type parameters as their own arguments.
     */
    public abstract Reference getReference();
    
    protected java.lang.Class<?> getModelClass() {
        return getClass();
    }
    
    private Scope getAbstraction(Scope container) {
        if (container instanceof Class) {
            Class c = (Class) container;
            if (isOverloadedVersion(c)) {
                return c.getExtendedType()
                        .getDeclaration();
            }
        }
        return container;
    }
    
    Type getMemberContainerType() {
        if (isMember()) {
            ClassOrInterface container = 
                    (ClassOrInterface) 
                        getContainer();
            return container.getType();
        }
        else {
            return null;
        }
    }
    
    /**
     * Does this model object "abstract" over several
     * overloaded declarations with the same name?
     * 
     * Always returns false for Ceylon declarations.
     */
    public boolean isAbstraction() { 
        return false; 
    }

    /**
     * Is this model object an overloaded declaration 
     * which shared a name with other declarations in
     * the same scope?
     * 
     * Always returns false for Ceylon declarations.
     * 
     * Always false for "abstractions" of overloaded
     * declarations.
     */
    public boolean isOverloaded() {
        return false;
    }

    public List<Declaration> getOverloads() {
        return Collections.emptyList();
    }
    
    @Override
    public boolean equals(Object object) {
        if (this==object) {
            return true;
        }
        
        if (object == null) {
            return false;
        }
        
        if (object instanceof Declaration) {
            Declaration that = (Declaration) object;
            String thisName = getName();
            String thatName = that.getName();
            if (!Objects.equals(getQualifier(), that.getQualifier())) {
                return false;
            }
            if (getModelClass() != that.getModelClass()) {
                return false;
            }
            Scope thisContainer = 
                    getAbstraction(getContainer());
            Scope thatContainer = 
                    getAbstraction(that.getContainer());
            if (thisName!=thatName && 
                    (thisName==null || thatName==null || 
                        !thisName.equals(thatName)) ||
                that.getDeclarationKind() != getDeclarationKind() ||
                thisContainer==null || thatContainer==null ||
                    !thisContainer.equals(thatContainer)) {
                return false;
            }
            else if (this instanceof Functional && 
                    that instanceof Functional) {
                boolean thisIsAbstraction = 
                        this.isAbstraction();
                boolean thatIsAbstraction = 
                        that.isAbstraction();
                boolean thisIsOverloaded = 
                        this.isOverloaded();
                boolean thatIsOverloaded = 
                        that.isOverloaded();
                if (thisIsAbstraction!=thatIsAbstraction ||
                    thisIsOverloaded!=thatIsOverloaded) {
                    return false;
                }
                if (!thisIsOverloaded && !thatIsOverloaded) {
                    return true;
                }
                if (thisIsAbstraction && thatIsAbstraction) {
                    return true;
                }
                Functional thisFunction = (Functional) this;
                Functional thatFunction = (Functional) that;
                List<ParameterList> thisParamLists = 
                        thisFunction.getParameterLists();
                List<ParameterList> thatParamLists = 
                        thatFunction.getParameterLists();
                if (thisParamLists.size()!=thatParamLists.size()) {
                    return false;
                }
                for (int i=0; i<thisParamLists.size(); i++) {
                    List<Parameter> thisParams = 
                            thisParamLists.get(i)
                                .getParameters();
                    List<Parameter> thatParams = 
                            thatParamLists.get(i)
                                .getParameters();
                    if (thisParams.size() != thatParams.size()) {
                        return false;
                    }
                    for (int j=0; j<thisParams.size(); j++) {
                        Parameter thisParam = thisParams.get(j);
                        Parameter thatParam = thatParams.get(j);
                        if (thisParam!=thatParam) {
                            if (thisParam!=null && 
                                thatParam!=null) {
                                Type thisParamType = 
                                        thisParam.getType();
                                Type thatParamType = 
                                        thatParam.getType();
                                if (thisParamType!=null && 
                                    thatParamType!=null) {
                                    thisParamType = 
                                            unit.getDefiniteType(thisParamType);
                                    thatParamType = 
                                            unit.getDefiniteType(thatParamType);
                                    TypeDeclaration thisErasedType = 
                                            erase(thisParamType, unit);
                                    TypeDeclaration thatErasedType = 
                                            erase(thatParamType, unit);
                                    if (!thisErasedType.equals(thatErasedType)) {
                                        return false;
                                    }
                                    TypeDeclaration oa = 
                                            unit.getJavaObjectArrayDeclaration();
                                    if (oa!=null 
                                            && thisErasedType.equals(oa) 
                                            && thatErasedType.equals(oa)) {
                                        Type thisElementType = 
                                                unit.getJavaArrayElementType(
                                                        thisParamType);
                                        Type thatElementType = 
                                                unit.getJavaArrayElementType(
                                                        thatParamType);
                                        if (!thisElementType.equals(thatElementType)) {
                                            return false;
                                        }
                                    }
                                }
                                else if (thisParamType!=thatParamType) {
                                    return false;
                                }
                            }
                            else if (thisParam!=thatParam) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        if (memoizedHash == 0) {
            memoizedHash = 17;
            Scope container = getContainer();
            memoizedHash = (37 * memoizedHash) + 
                    (container == null ? 0 : container.hashCode());
            String qualifier = getQualifier();
            memoizedHash = (37 * memoizedHash) + 
                    (qualifier == null ? 0 : qualifier.hashCode());
            String name = getName();
            memoizedHash = (37 * memoizedHash) + (name == null ? 0 : name.hashCode());
            // make sure we don't consider getter/setter or value/anonymous-type equal
            memoizedHash = (37 * memoizedHash) + (isSetter() ? 0 : 1);
            memoizedHash = (37 * memoizedHash) + (isAnonymous() ? 0 : 1);
        }
        return memoizedHash;
    }
    
    /**
     * Does this declaration refine the given declaration?
     * @deprecated does not take overloading into account
     */
    public boolean refines(Declaration other) {
        if (equals(other)) {
            return true;
        }
        else {
            if (isClassOrInterfaceMember()) {
                ClassOrInterface type = 
                        (ClassOrInterface) 
                            getContainer();
                return other.getName()!=null && getName()!=null 
                    && other.getName().equals(getName()) 
                    && isShared() && other.isShared() 
                    //&& other.getDeclarationKind()==getDeclarationKind()
                    && type.isMember(other);
            }
            else {
                return false;
            }
        }
    }
    
    /**
     * @return true if this is an anonymous class or 
     * anything with a rubbish system-generated name.
     * 
     * @see Declaration#isNamed()
     */
    public boolean isAnonymous() {
        return false;
    }
    
    /**
     * @return false if this declaration has a system-generated 
     * name, rather than a user-generated name. At the moment 
     * only object expressions, anonymous function expressions 
     * and anonymous type constructors are not named. This 
     * is different to {@link Declaration#isAnonymous()} 
     * because named object classes are considered anonymous 
     * but do have a user-generated name.
     */
    public boolean isNamed() {
        return true;
    }
    
    /**
     * Return true IFF this is not a real type but a 
     * pseudo-type generated by the model loader to pretend 
     * that Java enum values have an anonymous type. This is 
     * overridden and implemented in Class.
     */
    public boolean isJavaEnum() {
        return false;
    }
    
    public abstract DeclarationKind getDeclarationKind();
    
    @Override
    public String getNameAsString() {
        return getName();
    }
    
    @Override
    public String getName(Unit unit) {
        return unit==null ? 
                getName() : 
                unit.getAliasedName(this);
    }
    
    public boolean getOtherInstanceAccess() {
        return otherInstanceReadAccess
            || otherInstanceWriteAccess;
    }

    public boolean getOtherInstanceReadAccess() {
        return otherInstanceReadAccess;
    }
    
    public void setOtherInstanceReadAccess(boolean otherInstanceReadAccess) {
        this.otherInstanceReadAccess = otherInstanceReadAccess;
    }
    
    public boolean getOtherInstanceWriteAccess() {
        return otherInstanceWriteAccess;
    }
    
    public void setOtherInstanceWriteAccess(boolean otherInstanceWriteAccess) {
        this.otherInstanceWriteAccess = otherInstanceWriteAccess;
    }
    
    public boolean isParameter() {
        return false;
    }

    public boolean isSetter() {
        return false;
    }

    public boolean isFunctional() {
        return false;
    }

    protected abstract int hashCodeForCache();

    protected abstract boolean equalsForCache(Object o);

    public String getQualifier() {
        return isParameter() ? null : qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }
    
    public String getPrefixedName() {
        String qualifier = getQualifier();
        return qualifier==null || isParameter() ? 
                name : qualifier + name;
    }

    public boolean sameKind(Declaration m) {
        return m!=null 
            && m.getModelClass()==getModelClass();
    }

    public DeclarationCompleter getActualCompleter() {
        return actualCompleter;
    }

    public void setActualCompleter(DeclarationCompleter actualCompleter) {
        this.actualCompleter = actualCompleter;
    }
    
    public List<TypeParameter> getTypeParameters() {
        return emptyList();
    }
    
    /**
     * Get the type parameters of this declaration as their
     * own arguments. Note: what is returned is different to 
     * {@link Util#getTypeArgumentMap(Declaration, Type, List)},
     * which also includes type arguments from qualifying 
     * types. In this case we assume they're uninteresting.
     * 
     * We do need to compensate for this in 
     * {@link Type.Substitution#substitutedType(TypeDeclaration, Type, boolean, boolean)}
     * By expanding out the type arguments of the qualifying
     * type. An alternative solution would be to just expand
     * out all the type args of the qualifying type here. 
     * 
     * @return a map from each type parameter of this 
     *         declaration to its own type
     *         
     * @see ModelUtil#typeParametersAsArgList
     */
    Map<TypeParameter, Type> getTypeParametersAsArguments() {
        if (isParameterized()) {
            List<TypeParameter> typeParameters = 
                    getTypeParameters();
            Map<TypeParameter,Type> typeArgs = 
                    new HashMap<TypeParameter,Type>();
            for (TypeParameter p: typeParameters) {
                Type pta = new Type();
                if (p.isTypeConstructor()) {
                    pta.setTypeConstructor(true);
                    pta.setTypeConstructorParameter(p);
                }
                pta.setDeclaration(p);
                typeArgs.put(p, pta);
            }
            return typeArgs;
        }
        else {
            return EMPTY_TYPE_ARG_MAP;
        }
    }
    
    public List<String> getRestrictions() {
        return restrictions;
    }
    
    public void setRestrictions(List<String> restrictions) {
        this.restrictions = restrictions;
    }

    public List<String> getAliases() {
        return aliases != null ? aliases : 
            Collections.<String>emptyList();
    }
    
    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
    
    /**
     * To be overridden in sub types
     * @return true if this is a Java declaration
     */
    public boolean isJava(){
        return false;
    }

    /** 
     * true if the JVM backend generated this method 
     * in the model loader to mark coercion points,
     * but it does not exist.
     */
    public boolean isCoercionPoint() {
        return (flags&COERCION_POINT)!=0;
    }
    
    public void setCoercionPoint(boolean coercionPoint) {
        if (coercionPoint) {
            flags|=COERCION_POINT;
        }
        else {
            flags&=(~COERCION_POINT);
        }
    }

    public boolean isJavaNative() {
        return false;
    }
}
