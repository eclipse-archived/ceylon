package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.EMPTY_TYPE_ARG_MAP;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.contains;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.erase;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isOverloadedVersion;
import static java.util.Collections.emptyList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a named, annotated program element:
 * a class, interface, type parameter, parameter,
 * method, local, or attribute.
 *
 * @author Gavin King
 */
public abstract class Declaration 
        extends Element 
        implements Referenceable, Annotated {

	private String name;
	private String qualifier;
	private boolean shared;
	private boolean formal;
	private boolean actual;
	private boolean deprecated;
	private boolean def;
	private boolean annotation;
    private Scope visibleScope;
    private Declaration refinedDeclaration = this;
    private boolean staticallyImportable;
    private boolean protectedVisibility;
    private boolean packageVisibility;
    private String qualifiedNameAsStringCache;
	private String nativeBackend;
	private boolean otherInstanceAccess;
    private DeclarationCompleter actualCompleter;

    public Scope getVisibleScope() {
        return visibleScope;
    }

    public void setVisibleScope(Scope visibleScope) {
        this.visibleScope = visibleScope;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public boolean isParameterized() {
        return false;
    }
    
    public boolean isDeprecated() {
		return deprecated;
	}
    
    public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

    String toStringName() {
        String name = getName();
        if (name==null) name = "";
        Scope c = getContainer();
        if (c instanceof Declaration) {
            Declaration d = (Declaration) c;
            name = d.toStringName() + "." + name;
        }
        if (this instanceof Generic) {
            Generic g = (Generic) this;
            List<TypeParameter> typeParams = 
                    g.getTypeParameters();
            if (!typeParams.isEmpty()) {
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
        }
        return name;
    }
    
    @Override
    public abstract String toString();
    
    @Override
    public String getQualifiedNameString() {
        if (qualifiedNameAsStringCache == null) {
            String qualifier = getContainer().getQualifiedNameString();
            String name = getName();
            if (qualifier==null || qualifier.isEmpty()) {
                qualifiedNameAsStringCache = name; 
            }
            else if (getContainer() instanceof Package) {
                qualifiedNameAsStringCache = qualifier + "::" + name;
            }
            else {
                qualifiedNameAsStringCache = qualifier + "." + name;
            }
        }
        return qualifiedNameAsStringCache;
    }
    
    public boolean isAnnotation() {
        return annotation;
    }
    
    public void setAnnotation(boolean annotation) {
        this.annotation = annotation;
    }

    public boolean isActual() {
        if (actualCompleter != null) {
            completeActual();
        }
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    public boolean isFormal() {
        return formal;
    }
    
    public void setFormal(boolean formal) {
        this.formal = formal;
    }

    public boolean isNative() {
        return getNativeBackend() != null;
    }
    
    public String getNativeBackend() {
    	return nativeBackend;
    }
    
    public void setNativeBackend(String backend) {
    	this.nativeBackend=backend;
    }

    public boolean isDefault() {
        return def;
    }

    public void setDefault(boolean def) {
        this.def = def;
    }
    
    public Declaration getRefinedDeclaration() {
        if (actualCompleter != null) {
            completeActual();
        }
		return refinedDeclaration;
	}
    
    private void completeActual() {
        DeclarationCompleter completer = actualCompleter;
        actualCompleter = null;
        completer.completeActual(this);
    }

    public void setRefinedDeclaration(Declaration refinedDeclaration) {
		this.refinedDeclaration = refinedDeclaration;
	}
    
    /**
     * Determine if this declaration is visible
     * in the given scope, by considering if it
     * is shared or directly defined in a
     * containing scope.
     */
    public boolean isVisible(Scope scope) {
        Scope vs = getVisibleScope();
        if (vs==null) {
            return true;
        }
        else {
            return contains(vs, scope);
        }
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

    /**
     * Determine if this declaration is directly
     * defined in a containing scope of the given
     * scope.
     */
    public boolean isDefinedInScope(Scope scope) {
        while (scope!=null) {
            if (getContainer()==scope) {
                return true;
            }
            scope = scope.getContainer();
        }
        return false;
    }

    public boolean isCaptured() {
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
     * interface body. It might not be enough to make it
     * a "member" in the strict language spec sense of
     * the word.
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
    
    public boolean isStaticallyImportable() {
        return staticallyImportable;
    }
    
    public void setStaticallyImportable(boolean staticallyImportable) {
        this.staticallyImportable = staticallyImportable;
    }
    
    public boolean isProtectedVisibility() {
        return protectedVisibility;
    }
    
    public void setProtectedVisibility(boolean protectedVisibility) {
        this.protectedVisibility = protectedVisibility;
    }
    
    public boolean isPackageVisibility() {
        return packageVisibility;
    }
    
    public void setPackageVisibility(boolean packageVisibility) {
        this.packageVisibility = packageVisibility;
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
     * within its type parameters as their own arguments.
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

    public boolean isAbstraction() { 
        return false; 
    }

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
            Declaration dec = (Declaration) object;
            if (this.getModelClass() != dec.getModelClass()) {
                return false;
            }
            Declaration that = (Declaration) object;
            String thisName = getName();
            String thatName = that.getName();
            if (!Objects.equals(getQualifier(), that.getQualifier())) {
                return false;
            }
            Scope thisContainer = 
                    getAbstraction(getContainer());
            Scope thatContainer = 
                    getAbstraction(that.getContainer());
            if (thisName!=thatName && 
                    (thisName==null || thatName==null || 
                        !thisName.equals(thatName)) ||
                that.getDeclarationKind()!=getDeclarationKind() ||
                thisContainer==null || thatContainer==null ||
                    !thisContainer.equals(thatContainer)) {
                return false;
            }
            else if (this.isNative() != that.isNative() ||
                    (this.isNative() && 
                            !this.getNativeBackend()
                                .equals(that.getNativeBackend()))) {
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
                    if (thisParams.size()!=thatParams.size()) {
                        return false;
                    }
                    for (int j=0; j<thisParams.size(); j++) {
                        Parameter thisParam = thisParams.get(j);
                        Parameter thatParam = thatParams.get(j);
                        if (thisParam!=thatParam) {
                            if (thisParam!=null && thatParam!=null) {
                                Type thisParamType = 
                                        thisParam.getType();
                                Type thatParamType = 
                                        thatParam.getType();
                                if (thisParamType!=null && 
                                        thatParamType!=null) {
                                    if (!erase(thisParamType, unit)
                                            .equals(erase(thatParamType, unit))) {
                                        return false;
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
        int ret = 17;
        Scope container = getContainer();
        ret = (37 * ret) + 
                (container == null ? 0 : container.hashCode());
        String qualifier = getQualifier();
        ret = (37 * ret) + 
                (qualifier == null ? 0 : qualifier.hashCode());
        String name = getName();
        ret = (37 * ret) + (name == null ? 0 : name.hashCode());
        // make sure we don't consider getter/setter or value/anonymous-type equal
        ret = (37 * ret) + (isSetter() ? 0 : 1);
        ret = (37 * ret) + (isAnonymous() ? 0 : 1);
        return ret;
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
                return other.getName()!=null && getName()!=null &&
                        other.getName().equals(getName()) && 
                        isShared() && other.isShared() &&
                        //other.getDeclarationKind()==getDeclarationKind() &&
                        type.isMember(other);
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
    
    public String getName(Unit unit) {
    	return unit==null ? 
    	        getName() : 
    	        unit.getAliasedName(this);
    }
    
    public boolean getOtherInstanceAccess() {
    	return otherInstanceAccess;
    }

	public void setOtherInstanceAccess(boolean access) {
		otherInstanceAccess=access;
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
    
    public String getPrefixedName(){
        String qualifier = getQualifier();
        return qualifier==null || isParameter() ? 
                name : qualifier + name;
    }

    public boolean sameKind(Declaration m) {
        return m!=null && m.getModelClass()==getModelClass();
    }

    public DeclarationCompleter getActualCompleter() {
        return actualCompleter;
    }

    public void setActualCompleter(DeclarationCompleter actualCompleter) {
        this.actualCompleter = actualCompleter;
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
        if (this instanceof Generic) {
            Generic g = (Generic) this;
            List<TypeParameter> typeParameters = 
                    g.getTypeParameters();
            if (typeParameters.isEmpty()) {
                return EMPTY_TYPE_ARG_MAP;
            }
            else {
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
        }
        else {
            return EMPTY_TYPE_ARG_MAP;
        }
    }
    
}
