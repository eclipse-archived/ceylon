package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.EMPTY_TYPE_ARG_MAP;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.EMPTY_VARIANCE_MAP;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.NO_TYPE_ARGS;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.appliedType;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isAbstraction;
import static com.redhat.ceylon.model.typechecker.model.Type.checkDepth;
import static com.redhat.ceylon.model.typechecker.model.Type.decDepth;
import static com.redhat.ceylon.model.typechecker.model.Type.incDepth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.model.typechecker.context.TypeCache;

/**
 * An applied type or applied reference to a method or 
 * attribute. Packages a declaration, together with type
 * arguments.
 *
 * @author Gavin King
 */
public abstract class Reference {
    
    Reference() {}

    protected Map<TypeParameter, Type> typeArguments = 
            EMPTY_TYPE_ARG_MAP;
    
    protected Type qualifyingType;
    
    //cache
    private Map<TypeParameter, Type> 
    typeArgumentsWithDefaults;
    
    public Type getQualifyingType() {
        return qualifyingType;
    }
    
    void setQualifyingType(Type qualifyingType) {
        this.qualifyingType = qualifyingType;
    }
        
    public abstract Declaration getDeclaration();

    public Map<TypeParameter, Type> getTypeArguments() {
        Declaration declaration = getDeclaration();
        if (declaration instanceof Generic) {
            if (TypeCache.isEnabled()) {
                if (typeArgumentsWithDefaults==null) {
                    typeArgumentsWithDefaults = 
                            getTypeArgumentsInternal(declaration);
                }
                return typeArgumentsWithDefaults;
            }
            else {
                return getTypeArgumentsInternal(declaration);
            }
        }
        else {
            return typeArguments;
        }
    }

    private Map<TypeParameter, Type> 
    getTypeArgumentsInternal(Declaration declaration) {
        checkDepth();
        incDepth();
        try {
            return fillInDefaultTypeArguments(
                    declaration,
                    typeArguments);
        }
        finally { 
            decDepth();
        }
    }

    private static Map<TypeParameter, Type> 
    fillInDefaultTypeArguments(Declaration declaration,
            Map<TypeParameter, Type> typeArguments) {
        Map<TypeParameter, Type> typeArgs = typeArguments;
        Generic g = (Generic) declaration;
        List<TypeParameter> typeParameters = 
                g.getTypeParameters();
        for (int i=0, l=typeParameters.size(); 
                i<l; i++) {
            TypeParameter tp = typeParameters.get(i);
            Type dta = tp.getDefaultTypeArgument();
            if (dta!=null &&
                    !typeArguments.containsKey(tp)) {
                // only make a copy of typeArguments if required
                if (typeArguments == typeArgs) {
                    // make a copy big enough to fit every type parameter
                    typeArgs = 
                            new HashMap<TypeParameter,Type>
                                (typeParameters.size());
                    typeArgs.putAll(typeArguments);
                }
                typeArgs.put(tp, 
                        dta.substitute(typeArgs, 
                                EMPTY_VARIANCE_MAP));
            }
        }
        return typeArgs;
    }
    
    void setTypeArguments
        (Map<TypeParameter,Type> typeArguments) {
        this.typeArguments = typeArguments;
    }
    
    /**
     * Get the type arguments as a tuple. 
     */
    public List<Type> getTypeArgumentList() {
        Declaration declaration = getDeclaration();
        if (declaration instanceof Generic) {
            Generic g = (Generic) declaration;
            List<TypeParameter> typeParameters = 
                    g.getTypeParameters();
            if (typeParameters.isEmpty()) {
                return NO_TYPE_ARGS;
            }
            else {
    //            if (TypeCache.isEnabled()) {
    //                if (typeArgumentList==null) {
    //                    typeArgumentList = 
    //                            getTypeArgumentListInternal();
    //                }
    //                return typeArgumentList;
    //            }
    //            else {
                return getTypeArgumentList(
                        declaration.getUnit(),
                        typeParameters,
                        getTypeArguments());
            }
    //        }
        }
        else {
            return NO_TYPE_ARGS;
        }
    }

    private static List<Type> getTypeArgumentList(
            Unit unit, List<TypeParameter> typeParams, 
            Map<TypeParameter, Type> typeArgs) {
        int size = typeParams.size();
        List<Type> argList = new ArrayList<Type>(size);
        for (int i=0; i<size; i++) {
            TypeParameter tp = typeParams.get(i);
            Type arg = typeArgs.get(tp);
            if (arg==null) {
                unit.getUnknownType();
            }
            argList.add(arg);
        }
//        argList = unmodifiableList(argList);
        return argList;
    }

    /**
     * The type or return type of the referenced thing:
     * 
     * - for a value, this is its type,
     * - for a function, this is its return type, and
     * - for a class or constructor, it is the class type.
     * 
     * The "whole" type of the reference may be obtained
     * using {@link Reference#getFullType()}.
     * 
     * @see Reference#getTypedParameter(Parameter)
     * @see Reference#getFullType()
     */
    public abstract Type getType();
    
    /**
     * The type or callable type of the referenced thing:
     * 
     * - for a value, this is its type,
     * - for a function, class, or constructor, this is its 
     *   callable type.
     * 
     * This type encodes all the types you could assemble
     * using {@link Reference#getType()} and
     * {@link Reference#getTypedParameter(Parameter)}.
     *   
     *   @see Reference#getType()
     *   @see Reference#getTypedParameter(Parameter)
     */
    public Type getFullType() {
    	return getFullType(getType());
    }
    
    /**
     * @param wrappedType the return type of this member for
     *                    a ?. or *. expression, i.e.
     *                    T?, [T*], or [T+]
     */
    public Type getFullType(Type wrappedType) {
        //don't use this, because it is refined by Type
        Declaration declaration = getDeclaration();
        if (declaration instanceof Functional) {
            Unit unit = declaration.getUnit();
            if (isAbstraction(declaration)) {
                //for an unresolved overloaded method we don't 
                //know the parameter types, but we do know that
                //there is only one parameter list
                return appliedType(
                        unit.getCallableDeclaration(), 
                        wrappedType, 
                        new UnknownType(unit).getType());
            }
            else {
                return unit.getCallableType(this, 
                        wrappedType);
            }
        }
        else {
            return wrappedType;
        }
    }
    
    /**
     * Does this reference have parameters?
     */
    public boolean isFunctional() {
        return getDeclaration() instanceof Functional;
    }

    /**
     * Get the type of a parameter, after substitution of
     * type arguments.
     */
    public TypedReference getTypedParameter(Parameter p) {
        TypedReference ptr = 
                new TypedReference(false, true);
        FunctionOrValue model = p.getModel();
        if (model!=null) {
            ptr.setDeclaration(model);
        }
        ptr.setQualifyingType(getQualifyingType());
        ptr.setTypeArguments(getTypeArguments());
        return ptr;
    }
    
    public abstract String asString();
    
}
