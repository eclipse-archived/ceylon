package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.ProducedType.checkDepth;
import static com.redhat.ceylon.model.typechecker.model.ProducedType.decDepth;
import static com.redhat.ceylon.model.typechecker.model.ProducedType.incDepth;
import static com.redhat.ceylon.model.typechecker.model.Util.EMPTY_TYPE_ARG_MAP;
import static com.redhat.ceylon.model.typechecker.model.Util.EMPTY_VARIANCE_MAP;
import static com.redhat.ceylon.model.typechecker.model.Util.isAbstraction;
import static com.redhat.ceylon.model.typechecker.model.Util.producedType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.model.typechecker.context.ProducedTypeCache;

/**
 * A produced type or produced reference to a
 * method or attribute
 *
 * @author Gavin King
 */
public abstract class ProducedReference {
    
    ProducedReference() {}

    private Map<TypeParameter, ProducedType> typeArguments = 
            EMPTY_TYPE_ARG_MAP;
    
    private ProducedType qualifyingType;
    
    //cache
    private Map<TypeParameter, ProducedType> 
    typeArgumentsWithDefaults;
    
    public ProducedType getQualifyingType() {
        return qualifyingType;
    }
    
    void setQualifyingType(ProducedType qualifyingType) {
        this.qualifyingType = qualifyingType;
    }
    
    public abstract Declaration getDeclaration();

    public Map<TypeParameter, ProducedType> getTypeArguments() {
        Declaration declaration = getDeclaration();
        if (declaration instanceof Generic) {
            if (ProducedTypeCache.isEnabled()) {
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

    private Map<TypeParameter, ProducedType> 
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

    private static Map<TypeParameter, ProducedType> 
    fillInDefaultTypeArguments(Declaration declaration,
            Map<TypeParameter, ProducedType> typeArguments) {
        Map<TypeParameter, ProducedType> typeArgs = 
                typeArguments;
        Generic g = (Generic) declaration;
        List<TypeParameter> typeParameters = 
                g.getTypeParameters();
        for (int i=0, l=typeParameters.size(); 
                i<l; i++) {
            TypeParameter pt = typeParameters.get(i);
            ProducedType dta = pt.getDefaultTypeArgument();
            if (dta!=null &&
                    !typeArguments.containsKey(pt)) {
                // only make a copy of typeArguments if required
                if (typeArguments == typeArgs) {
                    // make a copy big enough to fit every type parameter
                    typeArgs = new HashMap
                            <TypeParameter,ProducedType>
                                (typeParameters.size());
                    typeArgs.putAll(typeArguments);
                }
                typeArgs.put(pt, 
                        dta.substitute(typeArgs, 
                                EMPTY_VARIANCE_MAP));
            }
        }
        return typeArgs;
    }
    
    void setTypeArguments
        (Map<TypeParameter,ProducedType> typeArguments) {
        this.typeArguments = typeArguments;
    }
    
    /**
     * The type or return type of the referenced thing:
     * 
     * - for a value, this is its type,
     * - for a function, this is its return type, and
     * - for a class or constructor, it is the class type.
     * 
     * The "whole" type of the reference may be obtained
     * using {@link ProducedReference#getFullType()}.
     * 
     * @see ProducedReference#getTypedParameter(Parameter)
     * @see ProducedReference#getFullType()
     */
    public abstract ProducedType getType();
    
    /**
     * The type or callable type of the referenced thing:
     * 
     * - for a value, this is its type,
     * - for a function, class, or constructor, this is its 
     *   callable type.
     * 
     * This type encodes all the types you could assemble
     * using {@link ProducedReference#getType()} and
     * {@link ProducedReference#getTypedParameter(Parameter)}.
     *   
     *   @see ProducedReference#getType()
     *   @see ProducedReference#getTypedParameter(Parameter)
     */
    public ProducedType getFullType() {
    	return getFullType(getType());
    }
    
    /**
     * @param wrappedType the return type of this member for
     *                    a ?. or *. expression, i.e.
     *                    T?, [T*], or [T+]
     */
    public ProducedType getFullType(ProducedType wrappedType) {
        //don't use this, because it is refined by ProducedType
        Declaration declaration = getDeclaration();
        if (declaration instanceof Functional) {
            Unit unit = declaration.getUnit();
            if (isAbstraction(declaration)) {
                //for an unresolved overloaded method we don't 
                //know the parameter types, but we do know that
                //there is only one parameter list
                return producedType(
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
    public ProducedTypedReference getTypedParameter(Parameter p) {
        ProducedTypedReference ptr = 
                new ProducedTypedReference(false, true);
        MethodOrValue model = p.getModel();
        if (model!=null) {
            ptr.setDeclaration(model);
        }
        ptr.setQualifyingType(getQualifyingType());
        ptr.setTypeArguments(getTypeArguments());
        return ptr;
    }
    
    public abstract String getProducedName();
    
}
