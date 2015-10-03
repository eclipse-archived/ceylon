package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.EMPTY_VARIANCE_MAP;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.NO_TYPE_ARGS;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.addToIntersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.addToUnion;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.canonicalIntersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getTypeArgumentMap;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getVarianceMap;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.intersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.intersectionOfSupertypes;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.intersectionType;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.principalInstantiation;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.typeParametersAsArgList;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.union;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.unionOfCaseTypes;
import static com.redhat.ceylon.model.typechecker.model.SiteVariance.IN;
import static com.redhat.ceylon.model.typechecker.model.SiteVariance.OUT;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.model.loader.model.FunctionOrValueInterface;
import com.redhat.ceylon.model.typechecker.context.TypeCache;
import com.redhat.ceylon.model.typechecker.model.UnknownType.ErrorReporter;
import com.redhat.ceylon.model.typechecker.util.TypePrinter;


/**
 * An applied type - a type declaration together with actual 
 * type arguments, along with optional variance overrides in 
 * the case of use-site variance. Alternatively, a union or
 * intersection type, or a type constructor.
 * 
 * This represents anything that is actually considered a 
 * "type" in the language specification.
 *
 * @author Gavin King
 */
public class Type extends Reference {
    
    private TypeDeclaration declaration;
    private String underlyingType;
    private boolean isRaw;
    private Type resolvedAliases;
    private TypeParameter typeConstructorParameter;
    private boolean typeConstructor;
    
    // cache
    private int hashCode;
//    private List<Type> typeArgumentList;
    
    private Map<TypeParameter,SiteVariance> varianceOverrides = 
            EMPTY_VARIANCE_MAP;
    
    public Map<TypeParameter, SiteVariance> getVarianceOverrides() {
        return varianceOverrides;
    }
    
    public boolean isCovariant(TypeParameter param) {
        SiteVariance override = 
                varianceOverrides.get(param);
        if (override==null) {
            return param.isCovariant();
        }
        else {
            return override==OUT;
        }
    }
    
    public boolean isContravariant(TypeParameter param) {
        SiteVariance override = 
                varianceOverrides.get(param);
        if (override==null) {
            return param.isContravariant();
        }
        else {
            return override==IN;
        }
    }
    
    public boolean isInvariant(TypeParameter param) {
        SiteVariance override = 
                varianceOverrides.get(param);
        if (override==null) {
            return param.isInvariant();
        }
        else {
            return false;
        }
    }
    
    public void setVariance(TypeParameter param, 
            SiteVariance variance) {
        if (varianceOverrides.isEmpty()) {
            varianceOverrides = 
                    new HashMap<TypeParameter,SiteVariance>();
        }
        varianceOverrides.put(param, variance);
    }
    
    public void setVarianceOverrides(
            Map<TypeParameter,SiteVariance> varianceOverrides) {
        this.varianceOverrides = varianceOverrides;
    }
    
    Type() {}
    
    @Override
    public TypeDeclaration getDeclaration() {
        return declaration;
    }
    
    void setDeclaration(TypeDeclaration declaration) {
        this.declaration = declaration;
    }
    
    /**
     * Is this type a type constructor? Warning: this is
     * not a "deep implementation", and a type constructor
     * type can be hidden behind a type alias. Therefore,
     * it's usually necessary to call 
     * {@link Type#resolveAliases()} before testing this.
     * 
     * @return true if this type is a type constructor
     */
    public boolean isTypeConstructor() {
        return typeConstructor;
    }
    
    public TypeParameter getTypeConstructorParameter() {
        return typeConstructorParameter;
    }
    
    public void setTypeConstructor(boolean typeConstructor) {
        this.typeConstructor = typeConstructor;
    }
    
    public void setTypeConstructorParameter
            (TypeParameter typeConstructorParameter) {
        this.typeConstructorParameter = typeConstructorParameter;
    }
    
    private int exactlyNothing;
    
    public boolean isExactlyNothing() {
        if (isNothing()) {
            return true;
        }
        else {
            if (exactlyNothing==0) {
                exactlyNothing =
                        isEmptySequenceType() ||
                        isEmptyTupleType() ?
                                1 : -1;
            }
            return exactlyNothing>0;
        }
    }
    
    /**
     * Is this type exactly the same type as the
     * given type? 
     */
    public boolean isExactly(Type type) {
        return type!=null && resolveAliases()
                .isExactlyInternal(type.resolveAliases());
    }
    
    private boolean isExactlyInternal(Type type) {
        checkDepth();
        incDepth();
        try {
            if (isUnknown() || type.isUnknown()) {
                return this==type;
            }
            else if (isAnything()) {
                return type.isAnything();
            }
            else if (type.isAnything()) {
                return isAnything();
            }
            else if (isExactlyNothing()) {
                return type.isExactlyNothing();
            }
            else if (type.isExactlyNothing()) {
                return isExactlyNothing();
            }
            else if (isUnion()) {
                List<Type> cases = getCaseTypes();
                if (type.isUnion()) {
                    List<Type> otherCases = 
                            type.getCaseTypes();
                    if (cases.size()!=otherCases.size()) {
                        return false;
                    }
                    else {
                        for (Type c: cases) {
                            boolean found = false;
                            for (Type oc: otherCases) {
                                if (c.isExactlyInternal(oc)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
                else if (cases.size()==1) {
                    Type st = cases.get(0);
                    return st.isExactlyInternal(type);
                }
                else {
                    return false;
                }
            }
            else if (isIntersection()) {
                List<Type> types = getSatisfiedTypes();
                if (type.isIntersection()) {
                    List<Type> otherTypes = 
                            type.getSatisfiedTypes();
                    if (types.size()!=otherTypes.size()) {
                        return false;
                    }
                    else {
                        for (Type c: types) {
                            boolean found = false;
                            TypeDeclaration cd = 
                                    c.getDeclaration();
                            for (Type oc: otherTypes) {
                                TypeDeclaration ocd = 
                                        oc.getDeclaration();
                                if (cd.equals(ocd)) {
                                    if (c.isExactlyInternal(oc)) {
                                        found = true;
                                        break;
                                    }
                                    //given a covariant type Co, and interfaces
                                    //A satisfies Co<B&Co<A>> and 
                                    //B satisfies Co<A&Co<B>>, then
                                    //A&Co<B> is equivalent A&Co<B&Co<A>> as a 
                                    //consequence of principal instantiation 
                                    //inheritance
                                    Type cst = 
                                            getSupertype(cd);
                                    Type ocst = 
                                            type.getSupertype(ocd);
                                    if (cst.isExactly(ocst)) {
                                        found = true;
                                        break;
                                    }
                                }
                            }
                            if (!found) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
                else if (types.size()==1) {
                    Type st = types.get(0);
                    return st.isExactlyInternal(type);
                }
                else {
                    return false;
                }
            }
            else if (type.isUnion()) {
                List<Type> otherCases = 
                        type.getCaseTypes();
                if (otherCases.size()==1) {
                    Type st = otherCases.get(0);
                    return isExactlyInternal(st);
                }
                else {
                    return false;
                }
            }
            else if (type.isIntersection()) {
                List<Type> otherTypes = 
                        type.getSatisfiedTypes();
                if (otherTypes.size()==1) {
                    Type st = otherTypes.get(0);
                    return isExactlyInternal(st);
                }
                else {
                    return false;
                }
            }
            else if (isObject()) {
                return type.isObject();
            }
            else if (type.isObject()) {
                return isObject();
            }
            else if (isNull()) {
                return type.isNull();
            }
            else if (type.isNull()) {
                return isNull();
            }
            else if (type.isClass()!=isClass() ||
                    type.isInterface()!=isInterface() ||
                    type.isTypeParameter()!=isTypeParameter()) {
                return false;
            }
            else if (isTypeConstructor() && 
                    type.isTypeConstructor()) {
                return isExactlyTypeConstructor(type);
            }
            else if (isTypeConstructor() ||
                    type.isTypeConstructor()) {
                return false;
            }
            else {
                TypeDeclaration dec = 
                        getDeclaration();
                TypeDeclaration otherDec = 
                        type.getDeclaration();
                if (!otherDec.equals(dec)) {
                    return false;
                }
                else {
                    if (isTuple()) {
                        return isExactlyTuple(type);
                    }
                    Type qt = trueQualifyingType();
                    Type tqt = type.trueQualifyingType();
                    if (qt==null || tqt==null) {
                        if (qt!=tqt) {
                            return false;
                        }
                    }
                    else if (qt.isUnknown() || tqt.isUnknown()) {
                        return false;
                    }
                    else {
                        Scope odc = otherDec.getContainer();
                        Scope dc = dec.getContainer();
                        if (!(odc instanceof TypeDeclaration) || 
                            !(dc instanceof TypeDeclaration)) {
                            // one of the two must be a local type, they should both be
                            if (odc instanceof TypeDeclaration || 
                                dc instanceof TypeDeclaration)
                                return false;
                            // must be the same container
                            if (!odc.equals(dc)) {
                                return false;
                            }
                            // just delegate equality
                            if (!tqt.isExactlyInternal(qt)) {
                                return false;
                            }
                        }
                        else {
                            TypeDeclaration totd = 
                                    (TypeDeclaration) odc;
                            Type tqts = 
                                    tqt.getSupertype(totd);
                            TypeDeclaration otd = 
                                    (TypeDeclaration) dc;
                            Type qts = qt.getSupertype(otd);
                            if (!qts.isExactly(tqts)) {
                                return false;
                            }
                        }
                    }
                    return isTypeArgumentListExactly(type);
                }
            }
        }
        finally {
            decDepth();
        }
    }

    private boolean isEmptySequenceType() {
        Unit unit = getDeclaration().getUnit();
        if (unit.isSequenceType(this)) {
            Type et = unit.getSequentialElementType(this);
            return et!=null && et.isExactlyNothing();
        }
        else {
            return false;
        }
    }

    private boolean isEmptyTupleType() {
        Unit unit = getDeclaration().getUnit();
        if (unit.isTupleType(this)) {
            List<Type> tal = getTypeArgumentList();
            if (tal.size()>=1) {
                Type elem = tal.get(0);
                if (elem!=null &&
                        elem.isExactlyNothing()) {
                    return true;
                }
            }
            if (tal.size()>=2) {
                Type first = tal.get(1);
                if (first!=null &&
                        first.isExactlyNothing()) {
                    return true;
                }
            }
            if (tal.size()>=3) {
                Type rest = tal.get(2);
                if (rest!=null &&
                        rest.isExactlyNothing()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isExactlyTuple(Type type) {
        TypeDeclaration td = 
                getDeclaration()
                    .getUnit()
                    .getTupleDeclaration();
        TypeParameter elem = td.getTypeParameters().get(0);
        TypeParameter first = td.getTypeParameters().get(1);
        TypeParameter rest = td.getTypeParameters().get(2);
        Type t1 = this;
        Type t2 = type;
        while (true) {
            Map<TypeParameter, Type> t1a = 
                    t1.getTypeArguments();
            Map<TypeParameter, Type> t2a = 
                    t2.getTypeArguments();
            Type e1 = t1a.get(elem);
            Type e2 = t2a.get(elem);
            Type f1 = t1a.get(first);
            Type f2 = t2a.get(first);
            if (e1==null || e2==null || 
                f1==null || f2==null) {
                return false;
            }
            if (!f1.isExactlyInternal(f2) || 
                !e1.isExactlyInternal(e2)) {
                return false;
            }
            Type r1 = t1a.get(rest);
            Type r2 = t2a.get(rest);
            if (r1==null || r2==null) {
                return false;
            }
            if (!r1.isTuple() || !r2.isTuple()) {
                return r1.isExactlyInternal(r2); 
            }
            t1 = r1;
            t2 = r2;
        }
    }

    private boolean isTypeArgumentListExactly(Type type) {
        List<TypeParameter> typeParameters = 
                getDeclaration()
                    .getTypeParameters();
        for (TypeParameter p: typeParameters) {
            Type arg = getTypeArguments().get(p);
            Type otherArg = type.getTypeArguments().get(p);
            if (arg==null || otherArg==null) {
                return false;
            }
            else {
                boolean contravariant = 
                        isContravariant(p);
                boolean covariant = 
                        isCovariant(p);
                boolean invariant = 
                        !covariant && 
                        !contravariant;
                boolean otherCovariant = 
                        type.isCovariant(p);
                boolean otherContravariant = 
                        type.isContravariant(p);
                boolean otherInvariant = 
                        !otherCovariant && 
                        !otherContravariant;
                if (contravariant && otherCovariant) {
                    //Inv<in Nothing> == Inv<out Anything> 
                    if (!arg.isNothing() ||
                            !intersectionOfSupertypes(p)
                                .isSubtypeOf(otherArg)) {
                        return false;
                    }
                }
                else if (covariant && otherContravariant) {
                    //Inv<out Anything> == Inv<in Nothing>
                    if (!otherArg.isNothing() ||
                            !intersectionOfSupertypes(p)
                                .isSubtypeOf(arg)) {
                        return false;
                    }
                }
                else if (contravariant && otherInvariant ||
                        invariant && otherContravariant) {
                    //Inv<in Anything> == Inv<Anything> 
                    if (!arg.isAnything() || 
                        !otherArg.isAnything()) {
                        return false;
                    }
                }
                else if (covariant && otherInvariant ||
                        invariant && otherCovariant) {
                    //Inv<out nothing> == Inv<Nothing>
                    if (!arg.isNothing() || 
                        !otherArg.isNothing()) {
                        return false;
                    }
                }
                else {
                    //variances are same!
                    if (!arg.isExactlyInternal(otherArg)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Is this type a supertype of the given type? 
     */
    public boolean isSupertypeOf(Type type) {
        return type.isSubtypeOf(this);
    }

    /**
     * Is this type a subtype of the given type? 
     */
    public boolean isSubtypeOf(Type type) {
        return type!=null && resolveAliases()
                .isSubtypeOfInternal(type.resolveAliases());
    }

    /**
     * Is this type a subtype of the given type? Ignore
     * a certain self type constraint.
     */
    private boolean isSubtypeOfInternal(Type type) {
        checkDepth();
        incDepth();
        try {
            if (type.isAnything()) {
                return true;
            }
            else if (isExactlyNothing()) {
                return true;
            }
            else if (isUnknown() || type.isUnknown()) {
                return this==type;
            }
            else if (isAnything()) {
                return false;
            }
            else if (type.isExactlyNothing()) {
                return isExactlyNothing();
            }
            else if (isUnion()) {
                for (Type ct: 
                        getInternalCaseTypes()) {
                    if (ct==null || 
                            !ct.isSubtypeOfInternal(type)) {
                        return false;
                    }
                }
                return true;
            }
            else if (type.isUnion()) {
                for (Type ct: 
                        type.getInternalCaseTypes()) {
                    if (ct!=null && 
                            isSubtypeOfInternal(ct)) {
                        return true;
                    }
                }
                return false;
            }
            else if (type.isIntersection()) {
                for (Type ct: 
                        type.getInternalSatisfiedTypes()) {
                    if (ct!=null && 
                            !isSubtypeOfInternal(ct)) {
                        return false;
                    }
                }
                return true;
            }
            else if (isIntersection()) {
                if (type.isClassOrInterface()) {
                    TypeDeclaration otherDec = 
                            type.getDeclaration();
                    Type pst = getSupertype(otherDec);
                    if (pst!=null && 
                            pst.isSubtypeOfInternal(type)) {
                        return true;
                    }
                }
                for (Type ct: 
                        getInternalSatisfiedTypes()) {
                    if (ct==null || 
                            ct.isSubtypeOfInternal(type)) {
                        return true;
                    }
                }
                return false;
            }
            else if (isTypeConstructor() && 
                    type.isTypeConstructor()) {
                return isSubtypeOfTypeConstructor(type);
            }
            else if (isTypeConstructor()) {
                return type.isAnything() || type.isObject();
            }
            else if (type.isTypeConstructor()) {
                return false;
            }
            else if (isObject()) {
                return type.isObject();
            }
            else if (isNull()) {
                return type.isNull();
            }
            else if (isInterface() && type.isClass()) {
                return type.isObject();
            }
            else {
                if (isTuple() && type.isTuple()) {
                    return isSubtypeOfTuple(type);
                }
                TypeDeclaration otherDec = 
                        type.getDeclaration();
                Type supertype = getSupertype(otherDec);
                if (supertype==null) {
                    return false;
                }
                else {
                    supertype = supertype.resolveAliases();
                    Type stqt =
                            supertype.trueQualifyingType();
                    Type tqt =
                            type.trueQualifyingType();
                    if (stqt==null || tqt==null) {
                        if (tqt!=stqt) {
                            return false;
                        }
                    }
                    else if (tqt.isUnknown() || stqt.isUnknown()) {
                        return false;
                    }
                    else {
                        if (!otherDec.isMember()) {
                            //local types with a qualifying typed 
                            //declaration do not need to obtain the
                            //qualifying type's supertype
                            if (!stqt.isSubtypeOfInternal(tqt)) {
                                return false;
                            }
                        }
                        else {
                            //note that the qualifying type of the
                            //given type may be an invariant subtype
                            //of the type that declares the member
                            //type, as long as it doesn't refine the
                            //member type
                            TypeDeclaration totd = 
                                    (TypeDeclaration) 
                                        otherDec.getContainer();
                            Type tqts = tqt.getSupertype(totd);
                            if (tqts==null) {
                                return false;
                            }
                            else {
                                tqts = tqts.resolveAliases();
                                if(!stqt.isSubtypeOfInternal(tqts)) {
                                    return false;
                                }
                            }
                        }
                    }
                    return isTypeArgumentListAssignable(
                            supertype, type);
                }
            }
        }
        finally { 
            decDepth();
        }
    }

    private boolean isSubtypeOfTuple(Type type) {
        TypeDeclaration td = 
                getDeclaration()
                    .getUnit()
                    .getTupleDeclaration();
        TypeParameter elem = td.getTypeParameters().get(0);
        TypeParameter first = td.getTypeParameters().get(1);
        TypeParameter rest = td.getTypeParameters().get(2);
        Type t1 = this;
        Type t2 = type;
        while (true) {
            Map<TypeParameter, Type> t1a = 
                    t1.getTypeArguments();
            Map<TypeParameter, Type> t2a = 
                    t2.getTypeArguments();
            Type e1 = t1a.get(elem);
            Type e2 = t2a.get(elem);
            Type f1 = t1a.get(first);
            Type f2 = t2a.get(first);
            if (e1==null || e2==null || 
                f1==null || f2==null) {
                return false;
            }
            if (!f1.isSubtypeOfInternal(f2) || 
                !e1.isSubtypeOfInternal(e2)) {
                return false;
            }
            Type r1 = t1a.get(rest);
            Type r2 = t2a.get(rest);
            if (r1==null || r2==null) {
                return false;
            }
            if (!r1.isTuple() || !r2.isTuple()) {
                return r1.isSubtypeOfInternal(r2); 
            }
            t1 = r1;
            t2 = r2;
        }
    }

    private static boolean isTypeArgumentListAssignable(
            Type supertype, Type type) {
        List<TypeParameter> typeParameters = 
                type.getDeclaration()
                    .getTypeParameters();
        for (TypeParameter tp: typeParameters) {
            Type arg = supertype.getTypeArguments().get(tp);
            Type otherArg = type.getTypeArguments().get(tp);
            if (arg==null || otherArg==null) {
                return false;
            }
            else if (type.isCovariant(tp)) {
                if (supertype.isContravariant(tp)) {
                    //Inv<in T> is a subtype of Inv<out Anything>
                    if (!tp.getType()
                            .isSubtypeOf(otherArg)) {
                        return false;
                    }
                }
                else if (!arg.isSubtypeOfInternal(otherArg)) {
                    return false;
                }
            }
            else if (type.isContravariant(tp)) {
                if (supertype.isCovariant(tp)) {
                    //Inv<out T> is a subtype of Inv<in Nothing>
                    if (!otherArg.isNothing()) {
                        return false;
                    }
                }
                else if (!otherArg.isSubtypeOfInternal(arg)) {
                    return false;
                }
            }
            else {
                //type is invariant in p
                //Inv<out Nothing> is a subtype of Inv<Nothing>
                //Inv<in Anything> is a subtype of Inv<Anything>
                if (supertype.isCovariant(tp) && 
                        !arg.isNothing() ||
                    supertype.isContravariant(tp) && 
                        !arg.isAnything() ||
                    !arg.isExactlyInternal(otherArg)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * For Java's static inner types, we sometimes need to
     * ignore the qualifying type, since it doesn't affect
     * the subtyping rules.
     */
    private Type trueQualifyingType() {
        return getDeclaration()
                    .isStaticallyImportable() ? null : 
                getQualifyingType();
    }
    
    private boolean isExactlyTypeConstructor(Type type) {
        TypeDeclaration dec = getDeclaration();
        TypeDeclaration otherDec = type.getDeclaration();
        if (dec.equals(otherDec)) {
            return true;
        }
        else {
            TypeParameter typeConstructorParam = 
                    getTypeConstructorParameter();
            Type qualifyingType = getQualifyingType();
            Type otherQualifyingType = type.getQualifyingType();
            if (typeConstructorParam==null) {
                List<Type> paramsAsArgs =
                        typeParametersAsArgList(otherDec);
                Type appliedType =
                        dec.appliedType(
                                qualifyingType,
                                paramsAsArgs);
                Type otherAppliedType =
                        otherDec.appliedType(
                                otherQualifyingType,
                                paramsAsArgs);
                return appliedType.isExactly(otherAppliedType) &&
                        hasExactSameUpperBounds(type,
                                paramsAsArgs);
            }
            else {
                List<Type> paramsAsArgs = 
                        typeParametersAsArgList(
                                typeConstructorParam);
                Type appliedType = 
                        dec.appliedType(
                                qualifyingType, 
                                paramsAsArgs);
                Type otherAppliedType = 
                        otherDec.appliedType(
                                otherQualifyingType, 
                                paramsAsArgs);
                //resolves aliases:
                return appliedType.isExactly(otherAppliedType);
            }
        }
    }

    private boolean isSubtypeOfTypeConstructor(Type type) {
        TypeDeclaration dec = getDeclaration();
        TypeDeclaration otherDec = type.getDeclaration();
        if (dec.equals(otherDec)) {
            return true;
        }
        else {
            Type qualifyingType = getQualifyingType();
            Type otherQualifyingType = type.getQualifyingType();
            TypeParameter typeConstructorParam = 
                    getTypeConstructorParameter();
            if (typeConstructorParam == null) {
                // occurs as the type of something
                List<Type> paramsAsArgs = 
                        typeParametersAsArgList(otherDec);
                Type appliedType =
                        dec.appliedType(
                                qualifyingType, 
                                paramsAsArgs);
                Type otherAppliedType = 
                        otherDec.appliedType(
                                otherQualifyingType, 
                                paramsAsArgs);
                return appliedType.isSubtypeOf(otherAppliedType) &&
                        acceptsUpperBounds(type, 
                                paramsAsArgs);
            }
            else {
                // occurs as a type argument to a type 
                // constructor parameter
                List<Type> paramsAsArgs = 
                        typeParametersAsArgList(
                                typeConstructorParam);
                Type appliedType = 
                        dec.appliedType(
                                qualifyingType, 
                                paramsAsArgs);
                Type otherAppliedType = 
                        otherDec.appliedType(
                                otherQualifyingType, 
                                paramsAsArgs);
                //resolves aliases:
                return appliedType.isSubtypeOf(otherAppliedType);
            }
        }
    }

    private boolean acceptsUpperBounds(Type type, 
            List<Type> paramsAsArgs) {
        TypeDeclaration declaration = getDeclaration();
        TypeDeclaration otherDeclaration = 
                type.getDeclaration();
        List<TypeParameter> typeParameters = 
                declaration.getTypeParameters();
        List<TypeParameter> otherTypeParameters = 
                otherDeclaration.getTypeParameters();
        int size = typeParameters.size();
        int otherSize = otherTypeParameters.size();
        if (size<otherSize) {
            return false;
        }
        int required = 0;
        for (int i=0; i<size; i++) {
            TypeParameter param = 
                    typeParameters.get(i);
            if (param.isDefaulted()) {
                break;
            }
            required++;
        }
        if (required>otherSize) {
            return false;
        }
        for (int i=0; i<size && i<otherSize; i++) {
            TypeParameter param = 
                    typeParameters.get(i);
            TypeParameter otherParam = 
                    otherTypeParameters.get(i);
            Map<TypeParameter, Type> otherArgs = 
                    getTypeArgumentMap(otherDeclaration, 
                            null, paramsAsArgs);
            Map<TypeParameter, Type> args = 
                    getTypeArgumentMap(declaration, 
                            null, paramsAsArgs);
            Map<TypeParameter, SiteVariance> none = 
                    EMPTY_VARIANCE_MAP;
            Type otherBound = 
                    intersectionOfSupertypes(otherParam)
                        .substitute(otherArgs, none);
            Type bound = 
                    intersectionOfSupertypes(param)
                        .substitute(args, none);
            if (!otherBound.isSubtypeOf(bound)) {
                return false;
            }
            Type otherEnumBound = 
                    unionOfCaseTypes(otherParam)
                        .substitute(otherArgs, none);
            Type enumBound = 
                    unionOfCaseTypes(param)
                        .substitute(args, none);
            if (!otherEnumBound.isSubtypeOf(enumBound)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasExactSameUpperBounds(Type type, 
            List<Type> paramsAsArgs) {
        TypeDeclaration declaration = getDeclaration();
        TypeDeclaration otherDeclaration = 
                type.getDeclaration();
        List<TypeParameter> typeParameters = 
                declaration.getTypeParameters();
        List<TypeParameter> otherTypeParameters = 
                otherDeclaration.getTypeParameters();
        int size = typeParameters.size();
        int otherSize = otherTypeParameters.size();
        if (size!=otherSize) {
            return false;
        }
        int required = 0;
        int otherRequired = 0;
        for (int i=0; i<size; i++) {
            TypeParameter param = 
                    typeParameters.get(i);
            if (param.isDefaulted()) {
                break;
            }
            required++;
        }
        for (int i=0; i<otherSize; i++) {
            TypeParameter param = 
                    otherTypeParameters.get(i);
            if (param.isDefaulted()) {
                break;
            }
            otherRequired++;
        }
        if (required!=otherRequired) {
            return false;
        }
        for (int i=0; i<size && i<otherSize; i++) {
            TypeParameter param = 
                    typeParameters.get(i);
            TypeParameter otherParam = 
                    otherTypeParameters.get(i);
            Map<TypeParameter, Type> otherArgs = 
                    getTypeArgumentMap(otherDeclaration, 
                            null, paramsAsArgs);
            Map<TypeParameter, Type> args = 
                    getTypeArgumentMap(declaration, 
                            null, paramsAsArgs);
            Map<TypeParameter, SiteVariance> none = 
                    EMPTY_VARIANCE_MAP;
            Type otherBound = 
                    intersectionOfSupertypes(otherParam)
                        .substitute(otherArgs, none);
            Type bound = 
                    intersectionOfSupertypes(param)
                        .substitute(args, none);
            if (!otherBound.isExactly(bound)) {
                return false;
            }
            Type otherEnumBound = 
                    unionOfCaseTypes(otherParam)
                        .substitute(otherArgs, none);
            Type enumBound = 
                    unionOfCaseTypes(param)
                        .substitute(args, none);
            if (!otherEnumBound.isExactly(enumBound)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Eliminate the given type from the union type.
     * (Performs a set complement operation.) Note
     * that this operation is not robust and only 
     * works if this is a union of the given type
     * with some other types that don't involve the
     * given type.
     */
    public Type minus(Type pt) {
        //canonicalize and then remove the type
        //from the resulting union
        return resolveAliases()
                .minusInternal(pt.resolveAliases());
    }
    
    /**
     * Eliminates unioned Null from a type in a
     * very special way that the backend prefers
     */
    public Type eliminateNull() {
        TypeDeclaration dec = getDeclaration();
        Unit unit = dec.getUnit();
        if (dec.inherits(unit.getNullDeclaration())) {
            return unit.getNothingType();
        }
        else if (isUnion()) {
            List<Type> caseTypes = getCaseTypes();
            List<Type> types = 
                    new ArrayList<Type>
                        (caseTypes.size());
            for (Type ct: caseTypes) {
                addToUnion(types, ct.eliminateNull());
            }
            return union(types, unit);
        }
        else {
            return this;
        }
    }
    
    public Type shallowMinus(Type pt) {
        TypeDeclaration dec = getDeclaration();
        Unit unit = dec.getUnit();
        if (isSubtypeOf(pt)) {
            return unit.getNothingType();
        }
        else if (isUnion()) {
            List<Type> caseTypes = getCaseTypes();
            List<Type> types = 
                    new ArrayList<Type>
                        (caseTypes.size());
            for (Type ct: caseTypes) {
                addToUnion(types, ct.shallowMinus(pt));
            }
            return union(types, unit);
        }
        else {
            return this;
        }
    }
    
    private Type minusInternal(Type pt) {
        TypeDeclaration dec = getDeclaration();
        Unit unit = dec.getUnit();
        if (pt.coversInternal(this)) { //note: coversInternal() already calls getUnionOfCases()
            return unit.getNothingType();
        }
        else {
            Type ucts = getUnionOfCases();
            if (ucts.isUnion()) {
                List<Type> cts = 
                        ucts.getCaseTypes();
                List<Type> types = 
                        new ArrayList<Type>
                            (cts.size());
                for (Type ct: cts) {
                    addToUnion(types, ct.minus(pt));
                }
                Type type = union(types, unit);
                return type.coversInternal(this) ? 
                        this : type;
            }
            else if (isIntersection()) {
                List<Type> cts = 
                        ucts.getSatisfiedTypes();
                List<Type> types = 
                        new ArrayList<Type>
                            (cts.size());
                for (Type ct: cts) {
                    addToIntersection(types, ct.minus(pt), 
                            unit);
                }
                Type type = 
                        canonicalIntersection(types, unit);
                return type.coversInternal(this) ? 
                        this : type;
            }
            else if (isTypeParameter()) {
                Type upperBoundsMinus = 
                        intersectionOfSupertypes(dec)
                                .minusInternal(pt);
                Type type = 
                        intersectionType(upperBoundsMinus, 
                                this, unit);
                return type.coversInternal(this) ? 
                        this : type;
            }
            else {
                return this;
            }
        }
    }
    
    /**
     * Substitute the type arguments and use-site variances
     * given in the given type into this type, which is
     * usually a supertype of the given type.
     * 
     * @param source the type carrying type arguments and 
     *        use-site variances to apply
     *        
     * @return this type after application of use-site 
     *         variances and substitution of type arguments
     */
    public Type substitute(Type source) {
        return substitute(source.getTypeArguments(), 
                source.getVarianceOverrides());
    }
    
    /**
     * Substitute the type arguments and use-site variances
     * given in the qualifying type and type arguments of
     * the given member reference into this type (which is
     * always the type of the member reference). 
     * 
     * @param source the member reference carrying type
     *        arguments and use-site variances to apply
     *        
     * @return this type after application of use-site 
     *         variances and substitution of type arguments
     */
    public Type substitute(TypedReference source) {
        Type receiver = source.getQualifyingType();
        return substitute(source.getTypeArguments(),
                receiver==null ? null :
                    receiver.collectVarianceOverrides(),
                    source.isCovariant(),
                    source.isContravariant());
    }
    
    /**
     * Substitute the given types for the corresponding
     * given type parameters wherever they appear in the
     * type. Has the side-effect of performing disjoint
     * type analysis, simplifying union/intersection
     * types, even when there are no substitutions. 
     */
    public Type substitute(
            Map<TypeParameter, Type> substitutions, 
            Map<TypeParameter, SiteVariance> overrides) {
        return substitute(substitutions, overrides, 
                true, false);
    }
    
    private Type substitute(
            Map<TypeParameter, Type> substitutions, 
            Map<TypeParameter, SiteVariance> overrides,
            boolean covariant, boolean contravariant) {
        if (!substitutions.isEmpty()) {
            Type type = this;
            if (overrides!=null) {
                type = applyVarianceOverrides(this, 
                        covariant, contravariant, overrides);
            }
            return new Substitution(
                    substitutions, overrides)
                .substitute(type, covariant, contravariant)
                /*.simple()*/;
        }
        else {
            return this;
        }
    }
    
    /**
     * Performs substitution of type arguments and variances
     * of the given type into this supertype of the given 
     * type.
     * 
     * @param source an extended type, satisfied type, or
     *        case type
     */
    private Type substituteFromSubtype(Type source) {
        return substituteFromSubtype(
                source.getTypeArguments(), 
                source.getVarianceOverrides());
    }

    private Type substituteFromSubtype(
            Map<TypeParameter, Type> substitutions, 
            Map<TypeParameter, SiteVariance> overrides) {
        if (!substitutions.isEmpty()) {
            Type type = this;
            if (overrides!=null) {
                type = applyVarianceOverrides(this, 
                        true, false, overrides);
            }
            return new SupertypeSubstitution(
                    substitutions, overrides)
                .substitute(type, true, false);
        }
        else {
            return this;
        }
    }

    /**
     * A member or member type of the type with actual type 
     * arguments to the receiving type and invocation.
     */
    public Reference getTypedReference(
            Declaration member, 
            List<Type> typeArguments) {
        if (member instanceof TypeDeclaration) {
            TypeDeclaration td = (TypeDeclaration) member;
            return getTypeMember(td, typeArguments);
        }
        else {
            TypedDeclaration td = (TypedDeclaration) member;
            return getTypedMember(td, typeArguments);
        }
    }
    
    public TypedReference getTypedMember(
            TypedDeclaration member, 
            List<Type> typeArguments) {
        return getTypedMember(member, typeArguments, false);
    }
    
    /**
     * A member of the type with actual type arguments
     * to the receiving type and invocation.
     * 
     * @param member the declaration of a member of
     *               this type
     * @param typeArguments the type arguments of the
     *                      invocation
     * @param assigned does this reference occur on the
     *                 RHS of an assignment operator
     */
    public TypedReference getTypedMember(
            TypedDeclaration member, 
            List<Type> typeArguments, 
            boolean assigned) {
        TypeDeclaration type = 
                (TypeDeclaration) 
                    member.getContainer();
        Type declaringType = getSupertype(type);
        TypedReference ptr = 
                new TypedReference(!assigned, assigned);
        ptr.setDeclaration(member);
        ptr.setQualifyingType(declaringType);
        Map<TypeParameter, Type> map = 
                getTypeArgumentMap(member, declaringType, 
                        typeArguments);
        ptr.setTypeArguments(map);
        return ptr;
    }

    /**
     * A member type of the type with actual type arguments
     * to the receiving type and invocation.
     * 
     * @param member the declaration of a member type of
     *               this type
     * @param typeArguments the type arguments of the
     *                      invocation
     */
    public Type getTypeMember(
            TypeDeclaration member, 
            List<Type> typeArguments) {
        TypeDeclaration type = 
                (TypeDeclaration) 
                    member.getContainer();
        Type declaringType = getSupertype(type);
        return member.appliedType(declaringType, 
                typeArguments);
    }

    /**
     * Substitute invocation type arguments into an upper bound 
     * on a type parameter of the invocation, where this type 
     * represents an upper bound.
     * 
     * @param receiver the type that receives the invocation
     * @param member the invoked member
     * @param typeArguments the explicit or inferred type 
     *                      arguments of the invocation
     * 
     * @return the upper bound of a type parameter, after 
     *         performing all type argument substitution
     */
    public Type appliedType(Type receiver, 
            Declaration member, 
            List<Type> typeArguments,
            List<SiteVariance> variances) {
        Type receivingType;
        if (receiver==null) {
            receivingType = null;
        }
        else {
            TypeDeclaration type = 
                    (TypeDeclaration) 
                        member.getContainer();
            receivingType = receiver.getSupertype(type);
        }
        Map<TypeParameter, Type> typeArgMap = 
                getTypeArgumentMap(member, receivingType, 
                        typeArguments);
        Map<TypeParameter, SiteVariance> varianceMap = 
                getVarianceMap(member, receivingType, 
                        variances);
        return new Substitution(typeArgMap, varianceMap)
                    .substitute(this, true, false);
    }

    public Type getType() {
        return this;
    }
    
    /**
     * Get all supertypes of the type by traversing the
     * whole type hierarchy. Avoid using this!
     */
    public List<Type> getSupertypes() {
        if (isUnion() || isNothing()) {
            throw new UnsupportedOperationException(
                    "getSupertypes() not defined for union types or Nothing");
        }
        return getSupertypes(new ArrayList<Type>(5));
    }
    
    private List<Type> getSupertypes(
            List<Type> list) {
        if (isWellDefined() && 
                addToSupertypes(this, list)) {
            Type extendedType = getExtendedType();
            if (extendedType!=null && 
                    !extendedType.isNothing() &&
                    !extendedType.isUnion()) {
                extendedType.getSupertypes(list);
            }
            List<Type> satisfiedTypes = getSatisfiedTypes();
            for (int i=0, l=satisfiedTypes.size(); 
                    i<l; i++) {
                Type satisfiedType = satisfiedTypes.get(i);
                if (satisfiedType!=null &&
                        !satisfiedType.isNothing() &&
                        !satisfiedType.isUnion()) {
                    satisfiedType.getSupertypes(list);
                }
            }
        }
        return list;
    }
    
    private static boolean addToSupertypes(Type st, 
            List<Type> list) {
        for (Type et: list) {
            TypeDeclaration std = st.getDeclaration();
            TypeDeclaration etd = et.getDeclaration();
            if (std.equals(etd) && //return both a type and its self type
                    st.isExactlyInternal(et)) {
                return false;
            }
        }
        list.add(st);
        return true;
    }

    /**
     * Given a type declaration, return a produced type of 
     * which this type is an invariant subtype.
     * 
     * If this type is Nothing, always return null.
     * 
     * @param dec a type declaration
     * 
     * @return a produced type of the given type declaration
     *         which is a supertype of this type, or null if
     *         there is no such supertype
     */
    public Type getSupertype(TypeDeclaration dec) {
        //we don't resolve aliases here because we want to
        //try and propagate the aliased specified in the 
        //code through to the returned supertype
        
        if (dec==null) {
            return null;
        }
        if (isNothing()) {
            //this is what the backend expects, apparently
            return null;
        }
        boolean canCache = canCacheSupertype(dec);
        if (canCache) {
            TypeCache cache = dec.getUnit().getCache();
            if (cache.containsKey(this, dec)) {
                return cache.get(this, dec);
            }
        }
        
        while (dec.isAlias()) {
            Type et = dec.getExtendedType();
            if (et==null) {
                return null;
            }
            else {
                dec = et.getDeclaration();
                if (dec==null) {
                    return null;
                }
            }
        }
        
        Type superType;
        if (isSimpleSupertypeLookup(dec)) {
            //fast!
            if (getDeclaration().inherits(dec)) {
                superType = dec.getType();
            }
            else {
                superType = null;
            }
        }
        else {
            //slow:
            superType = getSupertype(new SupertypeCriteria(dec));
        }
        
        if (canCache) {
            TypeCache cache = dec.getUnit().getCache();
            cache.put(this, dec, superType);
        }
        return superType;
    }

    private boolean canCacheSupertype(TypeDeclaration dec) {
        boolean complexType = 
                dec instanceof UnionType || 
                dec instanceof IntersectionType;
        return !complexType && 
                !hasUnderlyingType() && 
                collectVarianceOverrides().isEmpty() &&
                TypeCache.isEnabled();
    }

    private boolean isSimpleSupertypeLookup(TypeDeclaration dec) {
        return dec instanceof ClassOrInterface &&
                !isUnion() && !isIntersection() &&
                dec.getTypeParameters().isEmpty() &&
                !dec.isClassOrInterfaceMember() &&
                //this is for the runtime which uses 
                //qualifying types in a strange way
                getQualifyingType()==null;
    }
    
    private boolean hasUnderlyingType() {
        if (getUnderlyingType() != null) {
            return true;
        }
        List<Type> tal = getTypeArgumentList();
        for (int i=0, size=tal.size(); 
                i<size; i++) {
            Type ta = tal.get(i);
            if (ta!=null && ta.hasUnderlyingType()) {
                return true;
            }
        }
        return false;
    }
    
    private static final class SupertypeCriteria 
            implements Criteria {
        private TypeDeclaration dec;
        private SupertypeCriteria(TypeDeclaration dec) {
            this.dec = dec;
        }
        @Override
        public boolean satisfies(TypeDeclaration type) {
            return !(type instanceof UnionType) && 
                   !(type instanceof IntersectionType) && 
                   type.equals(dec);
        }
        @Override
        public boolean isMemberLookup() {
            return false;
        }
    };

    
    /**
     * Given a predicate, return a produced type for a 
     * declaration satisfying the predicate, of which 
     * this type is an invariant subtype. 
     */
    
    static interface Criteria {
        boolean satisfies(TypeDeclaration type);
        boolean isMemberLookup();
    }
    
    /**
     * Search for the most-specialized supertype 
     * satisfying the given predicate. 
     */
    public Type getSupertype(Criteria c) {
        checkDepth();
        incDepth();
        try {
            if (c.satisfies(getDeclaration())) {
                return qualifiedByDeclaringType();
            }
            if (isWellDefined()) {
                //now let's call the two most difficult methods
                //in the whole code base:
                Type result = getPrincipalInstantiation(c);
                result = getPrincipalInstantiationFromCases(c, result);
                if (result==null || result.isNothing()) {
                    return null;
                }
                else {
                    return result;
                }
            }
            else {
                return null;
            }
        }
        finally {
            decDepth();
        }
    }
    
    private Type getPrincipalInstantiationFromCases(
            Criteria c, Type result) {
        if (isUnion()) {
            //trying to infer supertypes of algebraic
            //types from their cases was resulting in
            //stack overflows and is not currently 
            //required by the spec
            List<Type> caseTypes = getInternalCaseTypes();
            if (caseTypes!=null && !caseTypes.isEmpty()) {
                //first find a common superclass or superinterface 
                //declaration that satisfies the criteria, ignoring
                //type arguments for now
                TypeDeclaration stc = 
                        findCommonSuperclass(c, caseTypes);
                if (stc!=null) {
                    //we found the declaration, now try to construct a 
                    //produced type that is a true common supertype
                    Type candidateResult = 
                            getCommonSupertype(caseTypes, stc);
                    if (candidateResult!=null && 
                            (result==null || 
                             candidateResult.isSubtypeOfInternal(result))) {
                        result = candidateResult;
                    }
                }
            }
        }
        return result;
    }

    private TypeDeclaration findCommonSuperclass(Criteria c,
            List<Type> types) {
        TypeDeclaration result = null;
        TypeDeclaration td = 
                types.get(0)
                    .getDeclaration();
        for (TypeDeclaration std: 
                td.getSupertypeDeclarations()) {
            if (std instanceof ClassOrInterface && 
                    c.satisfies(std)) {
                for (Type ct: types) {
                    if (!ct.getDeclaration()
                            .inherits(std)) {
                        std = null;
                        break;
                    }
                }
                if (std!=null) {
                    if (result==null) {
                        result = std;
                    }
                    else if (std.inherits(result)) {
                        result = std;
                    }
                    else if (result.inherits(std)) {
                        
                    }
                    else {
                        result = null;
                        break;
                    }
                }
            }
        }
        return result;
    }
    
    private static ThreadLocal<Integer> depth = 
            new ThreadLocal<Integer>() {
        protected Integer initialValue() {
            return 0;
        }
    };
    
    public static void resetDepth(int initial) {
        depth.set(initial);
    }
    
    static void checkDepth() {
        if (depth.get()>100) {
            throw new DecidabilityException();
        }
    }
    
    static void decDepth() {
        depth.set(depth.get()-1);
    }

    static void incDepth() {
        depth.set(depth.get()+1);
    }
    
    private Type getPrincipalInstantiation(Criteria c) {
        // search for the most-specific supertype for the
        // declaration that satisfies the given Criteria
        
        Type result = null;
        Type lowerBound = null;
        
        Type extendedType = getInternalExtendedType();
        if (extendedType!=null) {
            Type possibleResult = 
                    extendedType.getSupertype(c);
            if (possibleResult!=null) {
                result = possibleResult;
                lowerBound = possibleResult;
            }
        }
        
        List<Type> satisfiedTypes = 
                getInternalSatisfiedTypes();
        for (int i=0, size=satisfiedTypes.size(); 
                i<size; i++) {
            Type satisfiedType = satisfiedTypes.get(i);
            Type possibleResult = 
                    satisfiedType.getSupertype(c);
            if (possibleResult!=null) {
                if (result==null) {
                    result = possibleResult;
                    lowerBound = possibleResult;
                }
                else if (result.isSubtypeOf(possibleResult)) {
                    //just ignore this possible result 
                }
                else if (possibleResult.isSubtypeOf(lowerBound)) {
                    result = possibleResult;
                    lowerBound = possibleResult;
                }
                else {
                    //try to find a supertype of both types
                    //and form a principal instantiation 
                    TypeDeclaration rd = 
                            result.getDeclaration();
                    TypeDeclaration prd = 
                            possibleResult.getDeclaration();
                    
                    TypeDeclaration d = null;
                    if (rd.equals(prd)) {
                        d = rd;
                    }
                    
                    Unit unit = getDeclaration().getUnit();
                    if (d!=null) {
                        result = principalInstantiation(d, 
                                possibleResult, result, unit);
                    }
                    else {
                        //ambiguous! we can't decide between 
                        //the two supertypes which both 
                        //satisfy the criteria
                        if (c.isMemberLookup() && 
                                !satisfiedTypes.isEmpty()) {
                            //for the case of a member lookup, 
                            //try to find a common supertype 
                            //by forming the union of the two 
                            //possible results (since A|B is 
                            //always a supertype of A&B)
                            List<Type> types = 
                                    new ArrayList<Type>(2);
                            types.add(lowerBound);
                            types.add(possibleResult);
                            lowerBound = 
                                    intersection(types, unit);
                            List<Type> lbsts = 
                                    lowerBound.getSatisfiedTypes();
                            List<Type> caseTypes = 
                                    new ArrayList<Type>
                                        (lbsts.size());
                            caseTypes.addAll(lbsts);
                            result = union(caseTypes, unit)
                                    .getSupertype(c);
                            if (result==null) {
                                return unit.getUnknownType();
                            }
                        }
                        else {
                            return unit.getUnknownType();
                        }
                    }
                }
            }
        }
        
        return result;
    }
    
    private Type qualifiedByDeclaringType() {
        Type qt = getQualifyingType();
        if (qt==null) {
            return this;
        }
        else {
            TypeDeclaration declaration = getDeclaration();
            if (!declaration.isMember()) {
                // local types can't have qualifying types that differ
                return this;
            }
            else{
                Type pt = new Type();
                pt.setDeclaration(declaration);
                //replace the qualifying type with
                //the supertype of the qualifying 
                //type that declares this nested
                //type, substituting type arguments
                TypeDeclaration dtd = (TypeDeclaration) 
                        declaration.getContainer();
                Type declaringType = 
                        qt.getSupertype(dtd);
                pt.setQualifyingType(declaringType);
                Map<TypeParameter, Type> tam = 
                        getTypeArgumentMap(declaration, 
                                declaringType, 
                                getTypeArgumentList());
                pt.setTypeArguments(tam);
                pt.setVarianceOverrides(getVarianceOverrides());
                return pt;
            }
        }
    }

    private Type getCommonSupertype(List<Type> caseTypes,
            TypeDeclaration dec) {
        //now try to construct a common produced
        //type that is a common supertype by taking
        //the type args and unioning them
        List<TypeParameter> typeParameters = 
                dec.getTypeParameters();
        List<Type> args = 
                new ArrayList<Type>
                    (typeParameters.size());
        Map<TypeParameter,SiteVariance> variances = 
                new HashMap<TypeParameter,SiteVariance>();
        for (TypeParameter tp: typeParameters) {
            Type result;
            Unit unit = getDeclaration().getUnit();
            if (tp.isCovariant()) {
                List<Type> union = 
                        new ArrayList<Type>
                            (caseTypes.size());
                for (Type pt: caseTypes) {
                    if (pt==null) {
                        return null;
                    }
                    if (pt.isNothing()) {
                        continue;
                    }
                    Type st = pt.getSupertype(dec);
                    if (st==null) {
                        return null;
                    }
                    addToUnion(union, 
                            st.getTypeArguments()
                                .get(tp));
                }
                result = union(union, unit);
            }
            else if (tp.isContravariant()) { 
                List<Type> intersection = 
                        new ArrayList<Type>
                            (caseTypes.size());
                for (Type pt: caseTypes) {
                    if (pt==null) {
                        return null;
                    }
                    if (pt.isNothing()) {
                        continue;
                    }
                    Type st = pt.getSupertype(dec);
                    if (st==null) {
                        return null;
                    }
                    Type arg = 
                            st.getTypeArguments()
                                .get(tp);
                    addToIntersection(intersection, arg, 
                            unit);
                }
                result = canonicalIntersection(intersection, 
                        unit);
            }
            else {
                //invariant is harder, need to account for
                //use site variances!
                List<Type> union = 
                        new ArrayList<Type>
                            (caseTypes.size());
                List<Type> intersection = 
                        new ArrayList<Type>
                            (caseTypes.size());
                boolean covariant = false;
                boolean contravariant = false;
                for (Type pt: caseTypes) {
                    if (pt==null) {
                        return null;
                    }
                    if (pt.isNothing()) {
                        continue;
                    }
                    Type st = pt.getSupertype(dec);
                    if (st==null) {
                        return null;
                    }
                    Type arg = 
                            st.getTypeArguments()
                                .get(tp);
                    if (st.isCovariant(tp)) {
                        covariant = true;
                        addToUnion(union, arg);
                    } 
                    else if (st.isContravariant(tp)) {
                        contravariant = true;
                        addToIntersection(intersection, 
                                arg, unit);
                    }
                    else {
                        addToUnion(union, arg);
                        addToIntersection(intersection, 
                                arg, unit);
                    }
                }
                Type utt = union(union, unit);
                Type itt = intersection(intersection, unit);
                if (!covariant && !contravariant) {
                    if (utt.isExactly(itt)) {
                        result = utt; //invariant!
                    }
                    else {
                        //NOTE: big asymmetry here that
                        //      privileges covariance over
                        //      contravariance. More elegant
                        //      would be to have double
                        //      bounded wildcards like
                        //      "in ITT out UTT"
                        result = utt;
                        variances.put(tp, OUT);
                    }
                }
                else if (covariant && !contravariant) {
                    result = utt;
                    variances.put(tp, OUT);
                }
                else if (contravariant && !covariant) {
                    result = itt;
                    variances.put(tp, IN);
                }
                else {
                    //we have mixed covariant and invariant
                    //instantiations - that's only OK if we
                    //have something of form
                    Type upperBound = 
                            intersectionOfSupertypes(tp);
                    result = upperBound;
                    variances.put(tp, OUT);
                    //Note: we could have used "in Nothing"
                    //      here instead
                }
            }
            if (tp.isTypeConstructor()) {
                //TODO: construct a type alias instead!
                result.setTypeConstructor(true);
                result.setTypeConstructorParameter(tp);
            }
            args.add(result);
        }
        //check that the unioned type args
        //satisfy the type constraints
        //disabled this according to #52
        /*for (int i=0; i<args.size(); i++) {
            TypeParameter tp = dec.getTypeParameters().get(i);
            for (Type ub: tp.getSatisfiedTypes()) {
                if (!args.get(i).isSubtypeOf(ub)) {
                    return null;
                }
            }
        }*/
        //recurse to the qualifying type
        Type outerType;
        if (dec.isMember() && 
                !dec.isStaticallyImportable()) {
            TypeDeclaration outer = 
                    (TypeDeclaration) 
                        dec.getContainer();
            List<Type> list = 
                    new ArrayList<Type>
                        (caseTypes.size());
            for (Type ct: caseTypes) {
                if (ct==null) {
                    return null;
                }
                List<Type> intersectedTypes;
                if (ct.isIntersection()) {
                    intersectedTypes = 
                            ct.getSatisfiedTypes();
                }
                else {
                    intersectedTypes = singletonList(ct);
                }
                for (Type it: intersectedTypes) {
                    if (it.getDeclaration().isMember()) {
                        Type st = 
                                it.getQualifyingType()
                                    .getSupertype(outer);
                        list.add(st);
                    }
                }
            }
            outerType = getCommonSupertype(list, outer);
        }
        else {
            outerType = null;
        }
        //make the resulting type
        Type candidateResult = 
                dec.appliedType(outerType, args);
        candidateResult.setVarianceOverrides(variances);
        //check the the resulting type is *really*
        //a subtype (take variance into account)
        for (Type pt: caseTypes) {
            if (!pt.isSubtypeOf(candidateResult)) {
                return null;
            }
        }
        return candidateResult;
    }
    
    
    /**
     * Get the type arguments as a tuple. 
     */
    public List<Type> getTypeArgumentList() {
        List<TypeParameter> tps = 
                getDeclaration()
                    .getTypeParameters();
        if (tps.isEmpty()) {
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
                return getTypeArgumentListInternal();
            }
//        }
    }

    private List<Type> getTypeArgumentListInternal() {
        TypeDeclaration dec = getDeclaration();
        List<TypeParameter> tps = dec.getTypeParameters();
        int size = tps.size();
        List<Type> argList = new ArrayList<Type>(size);
        Map<TypeParameter, Type> args = getTypeArguments();
        for (int i=0; i<size; i++) {
            TypeParameter tp = tps.get(i);
            Type arg = args.get(tp);
            if (arg==null) {
                arg = dec.getUnit().getUnknownType();
            }
            argList.add(arg);
        }
//        argList = unmodifiableList(argList);
        return argList;
    }

    /**
     * Determine if this is a decidable supertype, i.e. if 
     * it obeys the restriction that types with contravariant 
     * type parameters may only appear in covariant positions. 
     * 
     * @return a list of type parameters which appear in  
     *         illegal positions
     */
    public List<TypeDeclaration> checkDecidability() {
        List<TypeDeclaration> errors = 
                new ArrayList<TypeDeclaration>();
        List<TypeParameter> typeParameters = 
                getDeclaration().getTypeParameters();
        Map<TypeParameter, Type> typeArguments = 
                getTypeArguments();
        for (TypeParameter tp: typeParameters) {
            Type pt = typeArguments.get(tp);
            if (pt!=null) {
                pt.checkDecidability(tp.isCovariant(), 
                        tp.isContravariant(), errors);
            }
        }
        return errors;
    }
    
    private void checkDecidability(
            boolean covariant, 
            boolean contravariant, 
            List<TypeDeclaration> errors) {
        if (isTypeParameter()) {
            //nothing to do
        }
        else if (isUnion()) {
            for (Type ct: getCaseTypes()) {
                ct.checkDecidability(
                        covariant, contravariant, 
                        errors);
            }
        }
        else if (isIntersection()) {
            for (Type ct: getSatisfiedTypes()) {
                ct.checkDecidability(
                        covariant, contravariant, 
                        errors);
            }
        }
        else {
            TypeDeclaration declaration = getDeclaration();
            for (TypeParameter tp: 
                    declaration.getTypeParameters()) {
                if (!covariant && tp.isContravariant()) {
                    //a type with contravariant parameters appears at
                    //a contravariant location in satisfies / extends
                    if (!errors.contains(declaration)) {
                        errors.add(declaration);
                    }
                }
                Type pt = 
                        getTypeArguments().get(tp);
                if (pt!=null) {
                    if (tp.isCovariant()) {
                        pt.checkDecidability(
                                covariant, contravariant, 
                                errors);
                    }
                    else if (tp.isContravariant()) {
                        if (covariant|contravariant) { 
                            pt.checkDecidability(
                                    !covariant, !contravariant, 
                                    errors); 
                        }
                        else {
                            //else if we are in an invariant 
                            //position, it stays invariant
                            pt.checkDecidability(
                                    covariant, contravariant, 
                                    errors);
                        }
                    }
                    else {
                        pt.checkDecidability(
                                false, false, 
                                errors);
                    }
                }
            }
        }
    }
    
    /**
     * Check that this type can appear at a position,
     * given the variance of the position (covariant,
     * contravariant, or invariant.)
     * 
     * @param covariant true for a covariant position
     * @param contravariant true for a contravariant 
     *                      position
     * @param declaration TODO!
     * 
     * @return a list of type parameters which appear
     *         in illegal positions
     */
    public List<TypeParameter> checkVariance(
            boolean covariant, 
            boolean contravariant, 
            Declaration declaration) {
        List<TypeParameter> errors = 
                new ArrayList<TypeParameter>(3);
        checkVariance(covariant, contravariant, 
                declaration, errors);
        return errors;
    }
    
    private static boolean isTrulyCovariant(TypeParameter tp) {
        Scope container = tp.getContainer();
        if (container instanceof TypeParameter) {
            TypeParameter tcp = (TypeParameter) container;
            if (isTrulyCovariant(tcp)) {
                return tp.isCovariant();
            }
            else if (isTrulyContravariant(tcp)) {
                return tp.isContravariant();
            }
            else {
                return false;
            }
        }
        else {
            return tp.isCovariant();
        }
    }
    
    private static boolean isTrulyContravariant(TypeParameter tp) {
        Scope container = tp.getContainer();
        if (container instanceof TypeParameter) {
            TypeParameter tcp = (TypeParameter) container;
            if (isTrulyCovariant(tcp)) {
                return tp.isContravariant();
            }
            else if (isTrulyContravariant(tcp)) {
                return tp.isCovariant();
            }
            else {
                return false;
            }
        }
        else {
            return tp.isContravariant();
        }
    }
    
    private void checkVariance(
            boolean covariant, 
            boolean contravariant,
            Declaration declaration, 
            List<TypeParameter> errors) {
        TypeDeclaration dec = getDeclaration();
        if (isTypeParameter()) {
            TypeParameter tp = (TypeParameter) dec;
            Declaration parameterizedDec = 
                    tp.getDeclaration();
            if (!parameterizedDec.equals(declaration)) {
                if (//if a contravariant type parameter appears 
                    //in a covariant location
                    !covariant && 
                        isTrulyCovariant(tp) ||
                    //or a covariant type parameter appears in a 
                    //contravariant location
                    !contravariant && 
                        isTrulyContravariant(tp)) {
                    //add an error to the list
                    errors.add(tp);
                }
            }
        }
        else if (isUnion()) {
            for (Type ct: getCaseTypes()) {
                ct.checkVariance(
                        covariant, contravariant, 
                        declaration, errors);
            }
        }
        else if (isIntersection()) {
            for (Type ct: getSatisfiedTypes()) {
                ct.checkVariance(
                        covariant, contravariant, 
                        declaration, errors);
            }
        }
        else {
            Type qt = getQualifyingType();
            if (qt!=null) {
                qt.checkVariance(
                        covariant, contravariant, 
                        declaration, errors);
            }
            for (TypeParameter tp: dec.getTypeParameters()) {
                Type pt = getTypeArguments().get(tp);
                if (pt!=null) {
                    if (isCovariant(tp)) {
                        pt.checkVariance(
                                covariant, contravariant, 
                                declaration, errors);
                    }
                    else if (isContravariant(tp)) {
                        if (covariant|contravariant) {
                            //flip the variance
                            pt.checkVariance(
                                    !covariant, !contravariant, 
                                    declaration, errors); 
                        }
                        else {
                            //unless we are in an invariant 
                            //position, then it stays invariant
                            pt.checkVariance(
                                    covariant, contravariant, 
                                    declaration, errors);
                        }
                    }
                    else {
                        pt.checkVariance(false, false, 
                                declaration, errors);
                    }
                }
            }
        }
    }

    /**
     * Is the type well-defined? Are any of its arguments
     * garbage nulls?
     */
    public boolean isWellDefined() {
        List<TypeParameter> tps = 
                getDeclaration().getTypeParameters();
        Type qt = getQualifyingType();
        if (qt!=null && 
                !qt.isWellDefined()) {
            return false;
        }
        List<Type> tas = getTypeArgumentList();
        for (int i=0; i<tps.size(); i++) {
            Type at = tas.get(i);
            TypeParameter tp = tps.get(i);
            if ((!tp.isDefaulted() && at==null) || 
                    (at!=null && !at.isWellDefined())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is the type fully-known? Are any of its arguments
     * unknowns?
     */
    public boolean containsUnknowns() {
        if (isUnknown()) {
            return true;
        }
        else if (isUnion()) {
            for (Type ct: getCaseTypes()) {
                if (ct.containsUnknowns()) {
                    return true;
                }
            }
        }
        else if (isIntersection()) {
            for (Type st: getSatisfiedTypes()) {
                if (st.containsUnknowns()) {
                    return true;
                }
            }
        }
        else if (isNothing()) {
            return false;
        }
        else {
            Type qt = getQualifyingType();
            if (qt!=null && 
                    qt.containsUnknowns()) {
                return true;
            }
            if (!isTypeConstructor()) {
                for (Type at: getTypeArgumentList()) {
                    if (at==null || 
                            at.containsUnknowns()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public String getFirstUnknownTypeError() {
        return getFirstUnknownTypeError(false);
    }

    public String getFirstUnknownTypeError(
            boolean includeSuperTypes) {
        if (isUnknown()) {
            TypeDeclaration dec = getDeclaration();
            UnknownType ut = (UnknownType) dec;
            ErrorReporter errorReporter = 
                    ut.getErrorReporter();
            return errorReporter == null ? null : 
                errorReporter.getMessage();
        }
        else if (isUnion()) {
            for (Type ct: getCaseTypes()) {
                String error = 
                        ct.getFirstUnknownTypeError(
                                includeSuperTypes);
                if (error!=null) {
                    return error;
                }
            }
        }
        else if (isIntersection()) {
            for (Type st: getSatisfiedTypes()) {
                String error = 
                        st.getFirstUnknownTypeError(
                                includeSuperTypes);
                if (error!=null) {
                    return error;
                }
            }
        }
        else if (isNothing()) {
            return null;
        }
        else {
            if (includeSuperTypes) {
                Type et = getExtendedType();
                if (et != null) {
                    String error = 
                            et.getFirstUnknownTypeError(
                                    includeSuperTypes);
                    if (error!=null) {
                        return error;
                    }
                }
                for (Type st: getSatisfiedTypes()) {
                    String error = 
                            st.getFirstUnknownTypeError(
                                    includeSuperTypes);
                    if (error!=null) {
                        return error;
                    }
                }
            }
            Type qt = getQualifyingType();
            if (qt!=null) {
                String error = 
                        qt.getFirstUnknownTypeError(
                                includeSuperTypes);
                if (error!=null) {
                    return error;
                }
            }
            List<Type> tas = getTypeArgumentList();
            for (Type at: tas) {
                if (at!=null) {
                    String error = 
                            at.getFirstUnknownTypeError(false);
                    if (error!=null) {
                        return error;
                    }
                }
            }
        }
        return null;
    }

    public boolean involvesDeclaration(Declaration d) {
        if (d instanceof TypeDeclaration) {
            return involvesDeclaration((TypeDeclaration) d);
        }
        else {
            return false;
        }
    }

    public boolean involvesDeclaration(TypeDeclaration td) {
        return involvesDeclaration(td, 
                new ArrayList<Type>());
    }
    private boolean involvesDeclaration(TypeDeclaration td,
            List<Type> visited) {
        if (isUnknown()) {
            return false;
        }
        else if (isUnion()) {
            for (Type ct: getCaseTypes()) {
                if (ct.involvesDeclaration(td, visited)) {
                    return true;
                }
            }
        }
        else if (isIntersection()) {
            for (Type st: getSatisfiedTypes()) {
                if (st.involvesDeclaration(td, visited)) {
                    return true;
                }
            }
        }
        else if (isNothing()) {
            return false;
        }
        else {
            if (visited.contains(this)) { //might be quicker to check only for ==
                return false;
            }
            visited.add(this);
            
            if (getDeclaration().equals(td)) {
                return true;
            }
            Type qt = getQualifyingType();
            if (qt!=null && 
                    qt.involvesDeclaration(td, visited)) {
                return true;
            }
            List<Type> tas = getTypeArgumentList();
            for (Type at: tas) {
                if (at==null || //take note!
                        at.involvesDeclaration(td, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean occursInvariantly(TypeParameter tp) {
        return occursInvariantly(tp, true, false);
    }

    private boolean occursInvariantly(TypeParameter tp,
            boolean covariant, boolean contravariant) {
        if (isUnknown()) {
            return false;
        }
        else if (isUnion()) {
            for (Type ct: getCaseTypes()) {
                if (ct.occursInvariantly(tp,
                        covariant, contravariant)) {
                    return true;
                }
            }
        }
        else if (isIntersection()) {
            for (Type st: getSatisfiedTypes()) {
                if (st.occursInvariantly(tp,
                        covariant, contravariant)) {
                    return true;
                }
            }
        }
        else if (isNothing()) {
            return false;
        }
        else {
            TypeDeclaration dec = getDeclaration();
            if (!covariant && !contravariant && 
                    dec.equals(tp)) {
                return true;
            }
            Type qt = getQualifyingType();
            if (qt!=null && 
                    qt.occursInvariantly(tp,
                            covariant, contravariant)) {
                return true;
            }
            List<TypeParameter> tps = 
                    dec.getTypeParameters();
            List<Type> tas = getTypeArgumentList();
            for (int i=0; i<tps.size() && i<tas.size(); i++) {
                TypeParameter itp = tps.get(i);
                Type at = tas.get(i);
                if (at!=null) {
                    boolean co;
                    boolean contra;
                    if (!covariant && !contravariant) {
                        co = false;
                        contra = false;
                    }
                    else if (isCovariant(itp)) {
                        co = covariant;
                        contra = contravariant;
                    }
                    else if (isContravariant(itp)) {
                        co = !covariant;
                        contra = !contravariant;
                    }
                    else {
                        co = false;
                        contra = false;
                    }
                    if (at.occursInvariantly(tp, co, contra)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean occursCovariantly(TypeParameter tp) {
        return occursCovariantly(tp, true);
    }

    private boolean occursCovariantly(TypeParameter tp,
            boolean covariant) {
        if (isUnknown()) {
            return false;
        }
        else if (isUnion()) {
            for (Type ct: getCaseTypes()) {
                if (ct.occursCovariantly(tp,covariant)) {
                    return true;
                }
            }
        }
        else if (isIntersection()) {
            for (Type st: getSatisfiedTypes()) {
                if (st.occursCovariantly(tp,covariant)) {
                    return true;
                }
            }
        }
        else if (isNothing()) {
            return false;
        }
        else {
            TypeDeclaration dec = getDeclaration();
            if (covariant && dec.equals(tp)) {
                return true;
            }
            Type qt = getQualifyingType();
            if (qt!=null && 
                    qt.occursCovariantly(tp,covariant)) {
                return true;
            }
            List<TypeParameter> tps = 
                    dec.getTypeParameters();
            List<Type> tas = getTypeArgumentList();
            for (int i=0; i<tps.size() && i<tas.size(); i++) {
                TypeParameter itp = tps.get(i);
                Type at = tas.get(i);
                if (at!=null && !isInvariant(itp)) {
                    boolean co = covariant;
                    if (isContravariant(itp)) {
                        co = !co;
                    }
                    if (at.occursCovariantly(tp, co)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean occursContravariantly(TypeParameter tp) {
        return occursContravariantly(tp, true);
    }

    private boolean occursContravariantly(TypeParameter tp,
            boolean covariant) {
        if (isUnknown()) {
            return false;
        }
        else if (isUnion()) {
            for (Type ct: getCaseTypes()) {
                if (ct.occursContravariantly(tp, covariant)) {
                    return true;
                }
            }
        }
        else if (isIntersection()) {
            for (Type st: getSatisfiedTypes()) {
                if (st.occursContravariantly(tp, covariant)) {
                    return true;
                }
            }
        }
        else if (isNothing()) {
            return false;
        }
        else {
            TypeDeclaration dec = getDeclaration();
            if (!covariant && dec.equals(tp)) {
                return true;
            }
            Type qt = getQualifyingType();
            if (qt!=null && 
                    qt.occursContravariantly(tp, covariant)) {
                return true;
            }
            List<TypeParameter> tps = 
                    dec.getTypeParameters();
            List<Type> tas = getTypeArgumentList();
            for (int i=0; i<tps.size() && i<tas.size(); i++) {
                TypeParameter itp = tps.get(i);
                Type at = tas.get(i);
                if (at!=null && !isInvariant(itp)) {
                    boolean co = covariant;
                    if (isContravariant(itp)) {
                        co = !co;
                    }
                    if (at.occursContravariantly(tp, co)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private List<Type> getInternalSatisfiedTypes() {
        TypeDeclaration dec = getDeclaration();
        List<Type> sts = dec.getSatisfiedTypes();
        if (getTypeArguments().isEmpty()) {
            return sts;
        }
        List<Type> satisfiedTypes = 
                new ArrayList<Type>
                    (sts.size());
        for (Type st: sts) {
            satisfiedTypes.add(st.substituteFromSubtype(this));
        }
        return satisfiedTypes;
    }

    private Type getInternalExtendedType() {
        TypeDeclaration dec = getDeclaration();
        Type et = dec.getExtendedType();
        if (et==null) {
            return null;
        }
        else {
            if (getTypeArguments().isEmpty()) {
                return et;
            }
            return et.substituteFromSubtype(this);
        }
    }

    private List<Type> getInternalCaseTypes() {
        TypeDeclaration dec = getDeclaration();
        List<Type> cts = dec.getCaseTypes();
        if (cts==null) {
            return null;
        }
        else {
            if (getTypeArguments().isEmpty()) {
                return cts;
            }
            List<Type> caseTypes = 
                    new ArrayList<Type>
                        (cts.size());
            for (Type ct: cts) {
                caseTypes.add(ct.substituteFromSubtype(this));
            }
            return caseTypes;
        }
    }

    public List<Type> getSatisfiedTypes() {
        TypeDeclaration dec = getDeclaration();
        List<Type> sts = dec.getSatisfiedTypes();
        if (getTypeArguments().isEmpty()) {
            return sts; 
        }
        List<Type> satisfiedTypes = 
                new ArrayList<Type>
                    (sts.size());
        for (int i=0, l=sts.size(); i<l; i++) {
            Type st = sts.get(i);
            satisfiedTypes.add(st.substitute(this));
        }
        return satisfiedTypes;
    }

    public Type getExtendedType() {
        TypeDeclaration dec = getDeclaration();
        Type et = dec.getExtendedType();
        if (et==null) {
            return null;
        }
        else {
            if (getTypeArguments().isEmpty()) {
                return et;
            }
            else {
                return et.substitute(this);
            }
        }
    }

    public List<Type> getCaseTypes() {
        TypeDeclaration dec = getDeclaration();
        List<Type> cts = dec.getCaseTypes();
        if (cts==null) {
            return null;
        }
        else {
            if (getTypeArguments().isEmpty()) {
                return cts;
            }
            List<Type> caseTypes = 
                    new ArrayList<Type>
                        (cts.size());
            for (Type ct: cts) {
                caseTypes.add(ct.substitute(this));
            }
            return caseTypes;
        }
    }

    /**
     * Substitutes type arguments for type parameters.
     * This default strategy performs canonicalization and
     * eliminates duplicate types from unions after 
     * substituting type arguments.
     * 
     * @author Gavin King
     */
    private static class Substitution {
        
        Map<TypeParameter, Type> substitutions;
        Map<TypeParameter, SiteVariance> overrides;
        
        private Substitution(
                Map<TypeParameter, Type> substitutions, 
                Map<TypeParameter, SiteVariance> overrides) {
            this.substitutions = substitutions;
            this.overrides = overrides;
        }

        Type substitute(Type type, 
                boolean covariant, boolean contravariant) {
            checkDepth();
            try {
                incDepth();
            TypeDeclaration dec;
            TypeDeclaration ptd = type.getDeclaration();
            Unit unit = ptd.getUnit();
            if (type.isUnion()) {
                List<Type> cts = type.getCaseTypes();
                List<Type> types = 
                        new ArrayList<Type>
                            (cts.size());
                for (Type ct: cts) {
                    if (ct!=null) {
                        addTypeToUnion(substitute(ct,
                                covariant, contravariant),
                                covariant, contravariant,
                                types);
                    }
                }
                return union(types, unit);
            }
            else if (type.isIntersection()) {
                List<Type> sts = type.getSatisfiedTypes();
                List<Type> types = 
                        new ArrayList<Type>
                            (sts.size());
                for (Type st: sts) {
                    if (st!=null) {
                        addTypeToIntersection(substitute(st,
                                covariant, contravariant),
                                covariant, contravariant,
                                types, unit);
                    }
                }
                return canonicalIntersection(types, unit);
            }
            else if (type.isTypeParameter()) {
                TypeParameter tp = (TypeParameter) ptd;
                Type sub = substitutions.get(tp);
                if (sub==null) {
                    if (tp.isTypeConstructor()) {
                        //if this is an applied type 
                        //constructor parameter, we still 
                        //might need to substitute into its 
                        //arguments!
                        dec = tp;
                    }
                    else {
                        //a regular type parameter, easy,
                        //nothing more to do with it
                        return type;
                    }
                }
                else {
                    if (type.isTypeConstructor()) {
                        //an unapplied generic type 
                        //parameter (a type constructor 
                        //parameter) in a higher-order
                        //abstraction - in this case, the 
                        //argument itself must be an 
                        //assignable type constructor
                        return sub;
                    }
                    else if (tp.getTypeParameters().isEmpty()) {
                        //a regular type parameter
                        //easy, no type arguments or 
                        //qualifying type to substitute
                        return sub;
                    }
                    else {
                        //an applied generic type parameter 
                        //in a higher-order generic 
                        //abstraction - in this case we need
                        //to substitute both the type 
                        //constructor and its arguments
                        return substitutedAppliedTypeConstructor(
                                type, sub, tp,
                                covariant, contravariant, 
                                unit);
                    }
                }
            }
            else {
                dec = ptd;
            }
            if (type.isTypeConstructor()) {
                //a type constructor occurring in a higher
                //rank generic abstraction
                return substitutedTypeConstructor(type,
                        covariant, contravariant);
            }
            else {
                //a plain olde type
                return substitutedType(dec, type,
                        covariant, contravariant);
            }
            }
            finally {
                decDepth();
            }
        }

        private Type substitutedAppliedTypeConstructor(
                Type type, Type sub, 
                TypeParameter typeConstructorParameter,
                boolean covariant, boolean contravariant, 
                Unit unit) {
            List<Type> tal = type.getTypeArgumentList();
            List<TypeParameter> tpl = 
                    typeConstructorParameter.getTypeParameters();
            List<Type> sta = 
                    new ArrayList<Type>
                        (tal.size());
            for (int i=0; 
                    i<tal.size() && 
                    i<tpl.size(); 
                    i++) {
                Type ta = tal.get(i);
                TypeParameter itp = tpl.get(i);
                boolean co = false;
                boolean contra = false;
                if (type.isContravariant(itp)) {
                    co = contravariant;
                    contra = covariant;
                }
                else if (type.isCovariant(itp)) {
                    co = covariant;
                    contra = contravariant;
                }
                sta.add(ta.substitute(
                        substitutions,
                        overrides, 
                        co, contra));
            }
            return substituteIntoTypeConstructors(
                    sub, sta,
                    covariant, contravariant,
                    unit, type);
        }
        
        private Type substituteIntoTypeConstructors(
                Type sub, List<Type> args,
                boolean covariant, boolean contravariant,
                Unit unit, Type tc) {
            if (sub.isUnion()) {
                List<Type> list =
                        new ArrayList<Type>();
                for (Type ct: sub.getCaseTypes()) {
                    addToUnion(list, 
                            substituteIntoTypeConstructors(
                                    ct,args,
                                    covariant, contravariant, 
                                    unit, tc));
                }
                return union(list, unit);
            }
            else if (sub.isIntersection()) {
                List<Type> list =
                        new ArrayList<Type>();
                for (Type st: sub.getSatisfiedTypes()) {
                    addToIntersection(list, 
                            substituteIntoTypeConstructors(st,
                                    args,
                                    covariant, contravariant, 
                                    unit, tc),
                            unit);
                }
                return canonicalIntersection(list, unit);
            }
            else {
                Type sqt = sub.getQualifyingType();
                Type qt = sqt==null ? null : 
                    substitute(sqt,
                            covariant, contravariant);
                Type result = 
                        sub.getDeclaration()
                            .appliedType(qt, args);
                substituteVarianceOverridesInTypeConstructor(
                        tc, result);
                return result;
            }
        }

        private void substituteVarianceOverridesInTypeConstructor(
                Type typeConstructor, 
                Type result) {
            TypeDeclaration sd = result.getDeclaration();
            Map<TypeParameter, SiteVariance> map = 
                    new HashMap<TypeParameter, SiteVariance>();
            map.putAll(result.getVarianceOverrides());
            List<TypeParameter> sdtps = 
                    sd.getTypeParameters();
            List<TypeParameter> tctps = 
                    typeConstructor.getDeclaration()
                        .getTypeParameters();
            for (int i=0; 
                    i<tctps.size() && i<sdtps.size(); 
                    i++) {
                TypeParameter tctp = tctps.get(i);
                TypeParameter sdtp = sdtps.get(i);
                SiteVariance var = 
                        typeConstructor.getVarianceOverrides()
                            .get(tctp);
                if (var!=null) {
                    map.put(sdtp, var);
                }
            }
            result.setVarianceOverrides(map);
        }
        
        void addTypeToUnion(Type ct,
                boolean covariant, boolean contravariant, 
                List<Type> types) {
            addToUnion(types, ct);
        }

        void addTypeToIntersection(Type st,
                boolean covariant, boolean contravariant, 
                List<Type> types, Unit unit) {
            addToIntersection(types, st, unit);
        }
        
        private Type substitutedType(
                TypeDeclaration dec, Type type,
                boolean covariant, boolean contravariant) {
            Type result;
            Type receiverType = type.getQualifyingType();
            if (receiverType!=null) {
                receiverType = 
                        substitute(receiverType, 
                                covariant, contravariant);
                List<Type> typeArgumentList =
                        type.getTypeArgumentList();
                List<Type> args = new ArrayList<Type>();
                for (int i=0, s=typeArgumentList.size();
                        i<s; i++) {
                    args.add(substitute(
                            typeArgumentList.get(i), 
                            covariant, contravariant));
                }
                result = dec.appliedType(receiverType, args);
            }
            else {
                result = new Type();
                result.setDeclaration(dec);
                Map<TypeParameter, Type> typeArguments = 
                        type.getTypeArguments();
                Map<TypeParameter, Type> typeArgs = 
                        new HashMap<TypeParameter, Type>
                            (typeArguments.size());
                for (Map.Entry<TypeParameter, Type> e: 
                        typeArguments.entrySet()) {
                    substituteTypeArgument(typeArgs, type, 
                            covariant, contravariant, 
                            e.getKey(), e.getValue());
                }
                result.setTypeArguments(typeArgs);
            }
            result.setTypeConstructor(
                    type.isTypeConstructor());
            result.setTypeConstructorParameter(
                    type.getTypeConstructorParameter());
            result.setVarianceOverrides(
                    type.getVarianceOverrides());
            result.setUnderlyingType(
                    type.getUnderlyingType());
            return result;
        }

        private void substituteTypeArgument(
                Map<TypeParameter, Type> typeArgs, 
                Type type, 
                boolean covariant, boolean contravariant, 
                TypeParameter tp, Type arg) {
            if (arg!=null) {
                boolean co = false;
                boolean contra = false;
                if (type.isContravariant(tp)) {
                    co = contravariant;
                    contra = covariant;
                }
                else if (type.isCovariant(tp)) {
                    co = covariant;
                    contra = contravariant;
                }
                typeArgs.put(tp, 
                        substitute(arg, co, contra));
            }
        }
        
        private Type substitutedTypeConstructor(
                Type type,
                boolean covariant, boolean contravariant) {
            TypeDeclaration dec = type.getDeclaration();            
            if (dec.isAlias() && substitutions!=null) {
                //necessary for subtyping of rank > 2 
                //polymorphic types and of any higher rank
                //parameter of a generic function ... and
                //possibly also other cases
                Unit unit = dec.getUnit();
                Type et = dec.getExtendedType();
                if (et==null) {
                    return unit.getUnknownType();
                }
                TypeAlias ta = new TypeAlias();
                ta.setAnonymous(dec.isAnonymous());
                ta.setName(dec.getName());
                ta.setExtendedType(et.substitute(
                        substitutions, overrides,
                        covariant, contravariant));
                ta.setScope(dec.getScope());
                ta.setContainer(dec.getContainer());
                ta.setUnit(unit);
                List<TypeParameter> tps = 
                        new ArrayList<TypeParameter>();
                //compute the right bounds for a higher
                //rank parameter of a generic function
                //by substituting the type arguments of
                //the function into the bounds of the
                //parameter
                for (TypeParameter tp: 
                        dec.getTypeParameters()) {
                    TypeParameter stp = new TypeParameter();
                    stp.setName(tp.getName());
                    stp.setScope(tp.getScope());
                    stp.setContainer(tp.getContainer());
                    stp.setUnit(tp.getUnit());
                    stp.setDeclaration(ta);
                    List<Type> sts = tp.getSatisfiedTypes();
                    List<Type> ssts = 
                            new ArrayList<Type>
                                (sts.size());
                    for (Type st: sts) {
                        ssts.add(st.substitute(
                                substitutions, overrides,
                                covariant, contravariant));
                    }
                    stp.setSatisfiedTypes(ssts);
                    List<Type> cts = tp.getCaseTypes();
                    if (cts!=null) {
                        List<Type> scts = 
                                new ArrayList<Type>
                                    (cts.size());
                        for (Type ct: cts) {
                            scts.add(ct.substitute(
                                    substitutions, overrides,
                                    covariant, contravariant));
                        }
                        stp.setCaseTypes(scts);
                    }
                    tps.add(stp);
                }
                ta.setTypeParameters(tps);
                Type result = ta.getType();
                Type qt = type.getQualifyingType();
                if (qt!=null) {
                    result.setQualifyingType(qt.substitute(
                            substitutions, overrides,
                            covariant, contravariant));
                }
                result.setTypeConstructor(true);
                result.setTypeConstructorParameter(
                        type.getTypeConstructorParameter());
                return result;
            }
            else {
                return type;
            }
        }
    }
    
    /**
     * This special strategy for internal use by the 
     * containing class does not perform canonicalization
     * nor eliminate duplicate types from unions after 
     * substituting type arguments.
     * 
     * @author Gavin King
     */
    private static class SupertypeSubstitution extends Substitution {
    
        private SupertypeSubstitution(
                Map<TypeParameter, Type> substitutions, 
                Map<TypeParameter, SiteVariance> overrides) {
            super(substitutions, overrides);
        }
        
        @Override 
        void addTypeToUnion(Type ct,
                boolean covariant, boolean contravariant,
                List<Type> types) {
            types.add(ct);
        }

        @Override 
        void addTypeToIntersection(Type st,
                boolean covariant, boolean contravariant,
                List<Type> types, Unit unit) {
            types.add(st);
        }
        
    }

    @Override
    public String toString() {
        String result = asString();
        return isTypeConstructor() ?
                result + " (type constructor)" :
                result + " (type)";
    }
    
    @Override
    public String asString() {
        return asString(null);
    }

    public String asString(Unit unit) {
        return TypePrinter.DEFAULT.print(this, unit);
    }
    
    public String asSourceCodeString(Unit unit) {
        return TypePrinter.ESCAPED.print(this, unit);
    }
    
    public String asString(boolean abbreviate) {
        return asString(abbreviate, null);
    }

    public String asString(boolean abbreviate, Unit unit) {
        return new TypePrinter(abbreviate)
                        .print(this, unit);
    }

    private String qualifiedName() {
        StringBuilder ptn = new StringBuilder();
        if (isTypeConstructor()) {
            return asString();
        }
        Type qt = getQualifyingType();
        TypeDeclaration declaration = getDeclaration();
        if (qt!=null) {
            ptn.append(qt.asQualifiedString())
               .append(".")
               .append(declaration.getName());
        }
        //}
        else {
            ptn.append(declaration.getQualifiedNameString());
        }
        if (!getTypeArgumentList().isEmpty()) {
            ptn.append("<");
            boolean first = true;
            for (Type t: getTypeArgumentList()) {
                if (first) {
                    first = false;
                }
                else {
                    ptn.append(",");
                }
                if (t==null) {
                    ptn.append("unknown");
                }
                else {
                    ptn.append(t.asQualifiedString());
                }
            }
            ptn.append(">");
        }
        return ptn.toString();
    }

    public String asQualifiedString() {
        TypeDeclaration declaration = getDeclaration();
        if (declaration==null) {
            //unknown type
            return null;
        }
        if (isUnion()) {
            StringBuilder name = new StringBuilder();
            boolean first = true;
            for (Type pt: getCaseTypes()) {
                if (first) {
                    first = false;
                }
                else {
                    name.append("|");
                }
                if (pt==null) {
                    name.append("unknown");
                }
                else {
                    name.append(pt.asQualifiedString());
                }
            }
            return name.toString();
        }
        else if (isIntersection()) {
            StringBuilder name = new StringBuilder();
            boolean first = true;
            for (Type pt: getSatisfiedTypes()) {
                if (first) {
                    first = false;
                }
                else {
                    name.append("&");
                }
                if (pt==null) {
                    name.append("unknown");
                }
                else {
                    name.append(pt.asQualifiedString());
                }
            }
            return name.toString();
        }
        else {            
            return qualifiedName();
        }
    }

    /**
     * Form a union type of all cases of the type, 
     * recursively reducing cases to their cases
     */
    public Type getUnionOfCases() {
        Unit unit = getDeclaration().getUnit();
        //if X is an intersection type A&B, and A is an
        //enumerated type with cases U and V, then the cases
        //of X are the intersection (U|V)&B canonicalized to
        //the union U&B|V&B
        if (isIntersection()) {
            List<Type> sts = getSatisfiedTypes();
            List<Type> list = 
                    new ArrayList<Type>
                        (sts.size());
            for (Type st: sts) {
                addToIntersection(list, 
                        st.getUnionOfCases(), 
                        unit);
            }
            return canonicalIntersection(list, unit);
        }
        else {
            List<Type> cts = getCaseTypes();
            if (cts==null) {
                return narrowToUpperBounds();
            }
            //otherwise, if X is a union A|B, or an enumerated 
            //type, with cases A and B, and A is an enumerated 
            //type with cases U and V, then the cases of X are
            //the union U|V|B
            else {
                //build a union of all the cases
                List<Type> list = 
                        new ArrayList<Type>
                            (cts.size());
                for (Type ct: cts) {
                    if (ct.isExactly(this)) {
                        //we hit a self type
                        return this;
                    }
                    addToUnion(list,
                            ct.narrowToUpperBounds()
                                .getUnionOfCases()); //note recursion
                }
                return union(list, unit);
            }
        }
    }

    /**
     * If a covariant type argument is a supertype of the
     * upper bounds on the type parameter, replace it with
     * the upper bounds.
     *  
     * @return a new instantiation of the generic type that 
     *         covers this instantiation, but whose type 
     *         arguments satisfy the upper bounds
     */
    Type narrowToUpperBounds() {
        if (isClassOrInterface()) {
            TypeDeclaration declaration = getDeclaration();
            List<TypeParameter> params = 
                    declaration.getTypeParameters();
            if (!params.isEmpty()) {
                List<Type> args = getTypeArgumentList();
                List<Type> bounded =
                        new ArrayList<Type>
                            (args.size());
                boolean found = false;
                for (int i=0, 
                        s1 = params.size(),
                        s2 = args.size();
                        i<s1 && i<s2; 
                        i++) {
                    TypeParameter tp = params.get(i);
                    Type arg = args.get(i);
                    if (isCovariant(tp)) {
                        Type bound =
                                intersectionOfSupertypes(tp)
                                    .substitute(this);
                        if (!arg.isSubtypeOf(bound)) {
                            arg = bound;
                            found = true;
                        }
                    }
                    bounded.add(arg);
                }
                if (found) {
                    Type type = 
                            declaration.appliedType(
                                    getQualifyingType(), 
                                    bounded);
                    type.setVarianceOverrides(
                            getVarianceOverrides());
                    return type;
                }
            }
        }
        return this;
    }
    
    public void setUnderlyingType(String underlyingType) {
        this.underlyingType = underlyingType;
        // if we have a resolvedAliases cache, update it too
        if (resolvedAliases != null && 
            resolvedAliases != this) {
            resolvedAliases.setUnderlyingType(underlyingType);
        }
    }
    
    public String getUnderlyingType() {
        return underlyingType;
    }
    
    /**
     * Does this type cover the given type?
     */
    public boolean covers(Type st) {
        return resolveAliases()
                .coversInternal(st.resolveAliases());
    }
    
    /*public boolean coversInternal(Type t, Stack<TypeDeclaration> stack) {
        Type uoc = t.getUnionOfCases();
        //X covers Y if the union of cases of Y is 
        //a subtype of X
        if (uoc.isSubtypeOfInternal(this)) {
            return true;
        }
        TypeDeclaration dec = t.getDeclaration();
        boolean decHasEquals = 
                dec instanceof ClassOrInterface || 
                dec instanceof TypeParameter;
        Map<TypeParameter, Type> args = t.getTypeArguments();
        List<Type> cts = uoc.getCaseTypes();
        if (cts!=null && !(decHasEquals && stack.contains(dec))) {
            //X covers Y if Y has the cases A|B|C and 
            //X covers all of A, B, and C
            for (Type ct: cts) {
                if (decHasEquals) stack.push(dec);
                if (!coversInternal(ct.substituteInternal(args), stack)) {
                    if (decHasEquals) stack.pop();
                    return false;
                }
                else {
                    if (decHasEquals) stack.pop();
                }
            }
            return true;
        }
        else {
            //X covers Y if Y extends Z and X covers Z
            Type et = dec.getExtendedType();
            if (et!=null && coversInternal(et.substituteInternal(args), stack)) {
                return true;
            }
            //X covers Y if Y satisfies Z and X covers Z
            for (Type st: dec.getSatisfiedTypes()) {
                if (coversInternal(st.substituteInternal(args), stack)) {
                    return true;
                }
            }
            return false;
        }
    }*/
    
    //This alternative algorithm, without the stack
    //fails in one tiny little corner case which we
    //might be able to disallow - Algebraic.ceylon:345
    private boolean coversInternal(Type type) {
        Type uoc = type.getUnionOfCases();
        //X covers Y if the union of cases of Y is 
        //a subtype of X
        if (uoc.isSubtypeOfInternal(this)) {
            return true;
        }
        if (uoc.isUnion()) {
            //X covers Y if Y has the cases A|B|C and 
            //X covers all of A, B, and C
            for (Type ct: uoc.getCaseTypes()) {
                if (!coversInternal(ct)) {
                    return false;
                }
            }
            return true;
        }
        else {
            TypeDeclaration dec = type.getDeclaration();
            //X covers Y if Y extends Z and X covers Z
            Type et = type.getExtendedType();
            Unit unit = dec.getUnit();
            if (et!=null) {
//                if (coversInternal(et)) {
//                    return true;
//                }
                //decompose a type T extends U for an 
                //enumerated type U of A|B|... to 
                //the union type T&A|T&B|...
                Type stu = et.getUnionOfCases();
                if (stu.isUnion() && 
                        intersectionType(stu, type, unit)
                            .isSubtypeOf(this)) {
                  return true;
               }
            }
            //X covers Y if Y satisfies Z and X covers Z
            for (Type st: type.getSatisfiedTypes()) {
//                if (coversInternal(st)) {
//                    return true;
//                }
                //decompose a type T satisfies U
                //for an enumerated type U of A|B|... to 
                //the union type T&A|T&B|...
                Type stu = st.getUnionOfCases();
                if (stu.isUnion() && 
                        intersectionType(stu, type, unit)
                            .isSubtypeOf(this)) {
                  return true;
               }
            }
            return false;
        }
    }
    
    public Type withoutUnderlyingType() {
        Type pt = new Type();
        pt.setDeclaration(getDeclaration());
        pt.setQualifyingType(getQualifyingType());
        pt.setTypeArguments(getTypeArguments());
        pt.setVarianceOverrides(getVarianceOverrides());
        pt.setTypeConstructor(isTypeConstructor());
        pt.setTypeConstructorParameter(getTypeConstructorParameter());
        return pt;
    }

    public boolean isRaw() {
        return isRaw;
    }

    public void setRaw(boolean isRaw) {
        this.isRaw = isRaw;
        // if we have a resolvedAliases cache, update it too
        if (resolvedAliases != null && 
            resolvedAliases != this) {
            resolvedAliases.setRaw(isRaw);
        }
    }
    
    /**
     * Eliminate type aliases from this type, recursively
     * expanding them into their aliased types. Canonicalize
     * the type by side-effect!
     * 
     * @return this type, with type aliases expanded, and 
     *         then canonicalized
     */
    public Type resolveAliases() {
        if (resolvedAliases == null) {
            // really compute it
            checkDepth();
            if (!isTuple()) {
                incDepth();
            }
            try {
                resolvedAliases = resolveAliasesInternal();
                /*if (resolvedAliases.isExactlyNothing()) {
                    resolvedAliases = 
                            getDeclaration().getUnit()
                                .getNothingType();
                }*/
            }
            finally {
                if (!isTuple()) {
                    decDepth();
                }
            }
            // mark it as resolved so it doesn't get resolved again
            resolvedAliases.resolvedAliases = resolvedAliases;
            if (resolvedAliases != this) {
                // inherit whatever underlying type we had
                resolvedAliases.underlyingType = underlyingType;
                resolvedAliases.isRaw = isRaw;
            }
        }
        return resolvedAliases;
    }

    private Type resolveAliasesInternal() {
        TypeDeclaration dec = getDeclaration();
        Unit unit = dec.getUnit();
        if (isClassOrInterface() &&
                getQualifyingType()==null &&
                !dec.isAlias() &&
                dec.getTypeParameters().isEmpty()) {
            return this;
        }
        else if (isTypeConstructor()) {
            return this;
        }
        else if (isUnion()) {
            List<Type> caseTypes = 
                    getCaseTypes();
            List<Type> list = 
                    new ArrayList<Type>
                        (caseTypes.size());
            for (Type pt: caseTypes) {
                addToUnion(list, 
                        pt.resolveAliases());
            }
            return union(list, unit);
        }
        else if (isIntersection()) {
            List<Type> satisfiedTypes = 
                    getSatisfiedTypes();
            List<Type> list = 
                    new ArrayList<Type>
                        (satisfiedTypes.size());
            for (Type pt: satisfiedTypes) {
                addToIntersection(list, 
                        pt.resolveAliases(), 
                        unit);
            }
            return canonicalIntersection(list, unit);
        }
        else {
            Type qt = getQualifyingType();
            Type aliasedQualifyingType = 
                    qt==null ? null : 
                        qt.resolveAliases();
            
            List<Type> args = getTypeArgumentList();
            List<Type> aliasedArgs;
            if (args.isEmpty()) {
                aliasedArgs = NO_TYPE_ARGS;
            }
            else {
                aliasedArgs = 
                        new ArrayList<Type>
                            (args.size());
                for (Type arg: args) {
                    Type aliasedArg = 
                            arg==null ? null : 
                                arg.resolveAliases();
                    aliasedArgs.add(aliasedArg);
                }
            }
            if (dec.isAlias()) {
                Type et = dec.getExtendedType();
                if (et == null) {
                    return unit.getUnknownType();
                }
                else {
                    return et.resolveAliases()
                            .substitute(getTypeArgumentMap(
                                    dec, 
                                    aliasedQualifyingType, 
                                    aliasedArgs),
                                    getVarianceOverrides());
                }
            }
            else {
                Type result = 
                        dec.appliedType(
                                aliasedQualifyingType, 
                                aliasedArgs);
                result.setVarianceOverrides(
                        getVarianceOverrides());
                return result;
            }
        }
    }
    
    /*private Type simple() {
        TypeDeclaration d = getDeclaration();
        if (d instanceof UnionType) {
            List<Type> cts = d.getCaseTypes();
            if (cts.size()==1) {
                return cts.get(0);
            }
        }
        if (d instanceof IntersectionType) {
            List<Type> sts = d.getSatisfiedTypes();
            if (sts.size()==1) {
                return sts.get(0);
            }
        }
        List<Type> args = getTypeArgumentList();
        List<Type> simpleArgs;
        Type qt = getQualifyingType();
        if (args.isEmpty()) {
            if (qt == null) {
                return this; // we have nothing to simplify
            }
            simpleArgs = NO_TYPE_ARGS;
        }
        else {
            simpleArgs = 
                    new ArrayList<Type>
                        (args.size());
            for (Type arg: args) {
                Type sarg = 
                        arg==null ? null : arg.simple();
                simpleArgs.add(sarg);
            }
        }
        Type sqt = 
                qt==null ? null : qt.simple();
        Type ret = 
                d.getProducedType(sqt, simpleArgs);
        ret.setUnderlyingType(getUnderlyingType());
        ret.setTypeConstructor(isTypeConstructor());
        ret.setTypeConstructorParameter(getTypeConstructorParameter());
        ret.setRaw(isRaw());
        ret.setVarianceOverrides(getVarianceOverrides());
        return ret;
    }*/
    
    public boolean involvesTypeParameters() {
        if (isTypeParameter()) {
            return true;
        }
        else if (isUnion()) {
            for (Type ct: getCaseTypes()) {
                if (ct.involvesTypeParameters()) {
                    return true;
                }
            }
        }
        else if (isIntersection()) {
            for (Type st: getSatisfiedTypes()) {
                if (st.involvesTypeParameters()) {
                    return true;
                }
            }
        }
        else {
            for (Type at: getTypeArgumentList()) {
                if (at!=null && 
                        at.involvesTypeParameters()) {
                    return true;
                }
            }
            Type qt = getQualifyingType();
            if (qt!=null && 
                    qt.involvesTypeParameters()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean involvesTypeParameters(Generic g) {
        return involvesTypeParameters(g.getTypeParameters());
    }
    
    public boolean involvesTypeParameters(
            Collection<TypeParameter> params) {
        TypeDeclaration d = getDeclaration();
        if (isTypeParameter()) {
            if (params.contains(d)) {
                return true;
            }
        }
        else if (isUnion()) {
            for (Type ct: getCaseTypes()) {
                if (ct.involvesTypeParameters(params)) {
                    return true;
                }
            }
        }
        else if (isIntersection()) {
            for (Type st: getSatisfiedTypes()) {
                if (st.involvesTypeParameters(params)) {
                    return true;
                }
            }
        }
        else {
            for (Type at: getTypeArgumentList()) {
                if (at!=null && 
                        at.involvesTypeParameters(params)) {
                    return true;
                }
            }
            Type qt = getQualifyingType();
            if (qt!=null && 
                    qt.involvesTypeParameters(params)) {
                return true;
            }
        }
        return false;
    }
    
    /*public boolean containsTypeAliases() {
        TypeDeclaration d = getDeclaration();
        if (d instanceof TypeAlias||
            d instanceof ClassAlias||
            d instanceof InterfaceAlias) {
            return true;
        }
        else if (d instanceof UnionType) {
            for (Type ct: getCaseTypes()) {
                if (ct.containsTypeAliases()) return true;
            }
        }
        else if (d instanceof IntersectionType) {
            for (Type st: getSatisfiedTypes()) {
                if (st.containsTypeAliases()) return true;
            }
        }
        else {
            for (Type at: getTypeArgumentList()) {
                if (at!=null && at.containsTypeAliases()) return true;
            }
            Type qt = getQualifyingType();
            if (qt!=null && qt.containsTypeAliases()) return true;
        }
        return false;
    }*/
    
    private Set<TypeDeclaration> extend(TypeDeclaration td, 
            Set<TypeDeclaration> visited) {
        HashSet<TypeDeclaration> set = 
                new HashSet<TypeDeclaration>(visited);
        set.add(td);
        return set;
    }
    
    private List<TypeDeclaration> extend(TypeDeclaration td, 
            List<TypeDeclaration> results) {
        if (!results.contains(td)) {
            results.add(td);
        }
        return results;
    }
    
    public List<TypeDeclaration> isRecursiveTypeAliasDefinition(
            Set<TypeDeclaration> visited) {
        TypeDeclaration dec = getDeclaration();
        if (dec.isAlias()) {
            if (visited.contains(dec)) {
                return new ArrayList<TypeDeclaration>(
                        singletonList(dec));
            }
            Type et = dec.getExtendedType();
            if (et!=null) {
                List<TypeDeclaration> l = 
                        et.isRecursiveTypeAliasDefinition(
                                extend(dec, visited));
                if (!l.isEmpty()) {
                    return extend(dec, l);
                }
            }
            for (Type bt: dec.getBrokenSupertypes()) {
                List<TypeDeclaration> l = 
                        bt.isRecursiveTypeAliasDefinition(
                                extend(dec, visited));
                if (!l.isEmpty()) {
                    return extend(dec, l);
                }
            }
        }
        else if (isUnion()) {
            for (Type ct: getCaseTypes()) {
                List<TypeDeclaration> l = 
                        ct.isRecursiveTypeAliasDefinition(
                                visited);
                if (!l.isEmpty()) return l;
            }
        }
        else if (isIntersection()) {
            for (Type st: getSatisfiedTypes()) {
                List<TypeDeclaration> l = 
                        st.isRecursiveTypeAliasDefinition(
                                visited);
                if (!l.isEmpty()) return l;
            }
        }
        else {
            for (Type at: getTypeArgumentList()) {
                if (at!=null) {
                    List<TypeDeclaration> l = 
                            at.isRecursiveTypeAliasDefinition(
                                    visited);
                    if (!l.isEmpty()) return l;
                }
            }
            Type qt = getQualifyingType();
            if (qt!=null) {
                List<TypeDeclaration> l = 
                        qt.isRecursiveTypeAliasDefinition(
                                visited);
                if (!l.isEmpty()) return l;
            }
        }
        return emptyList();
    }
    
    public List<TypeDeclaration> isRecursiveRawTypeDefinition(
            Set<TypeDeclaration> visited) {
        TypeDeclaration dec = getDeclaration();
        if (dec.isAlias()) {
            if (visited.contains(dec)) {
                return new ArrayList<TypeDeclaration>(
                        singletonList(dec));
            }
            Type et = dec.getExtendedType();
            if (et!=null) {
                List<TypeDeclaration> l = 
                        et.isRecursiveRawTypeDefinition(
                                extend(dec, visited));
                if (!l.isEmpty()) {
                    return extend(dec, l);
                }
            }
            for (Type bt: dec.getBrokenSupertypes()) {
                List<TypeDeclaration> l = 
                        bt.isRecursiveRawTypeDefinition(
                                extend(dec, visited));
                if (!l.isEmpty()) {
                    return extend(dec, l);
                }
            }
        }
        else if (isUnion()) {
            for (Type ct: getCaseTypes()) {
                List<TypeDeclaration> l = 
                        ct.isRecursiveRawTypeDefinition(
                                visited);
                if (!l.isEmpty()) return l;
            }
        }
        else if (isIntersection()) {
            for (Type st: getSatisfiedTypes()) {
                List<TypeDeclaration> l = 
                        st.isRecursiveRawTypeDefinition(
                                visited);
                if (!l.isEmpty()) return l;
            }
        }
        else {
            if (visited.contains(dec)) {
                return new ArrayList<TypeDeclaration>(
                        singletonList(dec));
            }
            Type et = dec.getExtendedType();
            if (et!=null) {
                List<TypeDeclaration> i = 
                        et.isRecursiveRawTypeDefinition(
                                extend(dec, visited));
                if (!i.isEmpty()) {
                    i.add(0, dec);
                    return i;
                }
            }
            for (Type bt: dec.getBrokenSupertypes()) {
                List<TypeDeclaration> l = 
                        bt.isRecursiveRawTypeDefinition(
                                extend(dec, visited));
                if (!l.isEmpty()) {
                    return extend(dec, l);
                }
            }
            for (Type st: getSatisfiedTypes()) {
                List<TypeDeclaration> l = 
                        st.isRecursiveRawTypeDefinition(
                                extend(dec, visited));
                if (!l.isEmpty()) {
                    return extend(dec, l);
                }
            }
        }
        return emptyList();
    }
    
    public boolean isUnknown() {
        return getDeclaration() instanceof UnknownType;
    }
    
    public boolean isNothing() {
        return getDeclaration() instanceof NothingType;
    }
    
    public boolean isUnion() {
        return getDeclaration() instanceof UnionType;
    }
    
    public boolean isIntersection() {
        return getDeclaration() instanceof IntersectionType;
    }
    
    public boolean isClassOrInterface() {
        return getDeclaration() instanceof ClassOrInterface;
    }

    public boolean isFunctionOrValueInterface() {
        return getDeclaration() instanceof FunctionOrValueInterface;
    }

    public boolean isClass() {
        return getDeclaration() instanceof Class;
    }
    
    public boolean isInterface() {
        return getDeclaration() instanceof Interface;
    }
    
    public boolean isTypeParameter() {
        return getDeclaration() instanceof TypeParameter;
    }
    
    public boolean isTypeAlias() {
        return getDeclaration() instanceof TypeAlias;
    }
    
    public boolean isAnything() {
        return getDeclaration().isAnything();
    }
    
    public boolean isObject() {
        return getDeclaration().isObject();
    }
    
    public boolean isNull() {
        return getDeclaration().isNull();
    }
    
    public boolean isNullValue() {
        return getDeclaration().isNullValue();
    }
    
    public boolean isBasic() {
        return getDeclaration().isBasic();
    }
    
    public boolean isBoolean() {
        return getDeclaration().isBoolean();
    }
    
    public boolean isString() {
        return getDeclaration().isString();
    }
    
    public boolean isCharacter() {
        return getDeclaration().isCharacter();
    }
    
    public boolean isFloat() {
        return getDeclaration().isFloat();
    }
    
    public boolean isInteger() {
        return getDeclaration().isInteger();
    }
    
    public boolean isByte() {
        return getDeclaration().isByte();
    }
    
    public boolean isIterable() {
        return getDeclaration().isIterable();
    }
    
    public boolean isSequence() {
        return getDeclaration().isSequence();
    }
    
    public boolean isSequential() {
        return getDeclaration().isSequential();
    }
    
    public boolean isRange() {
        return getDeclaration().isRange();
    }
    
    public boolean isEmpty() {
        return getDeclaration().isEmpty();
    }
    
    public boolean isEmptyValue() {
        return getDeclaration().isEmptyValue();
    }
    
    public boolean isTuple() {
        return getDeclaration().isTuple();
    }
    
    public boolean isEntry() {
        return getDeclaration().isEntry();
    }
    
    public int getMemoisedHashCode() {
        if (hashCode == 0) {
            int ret = 17;
            Type qualifyingType = 
                    getQualifyingType();
            ret = (37 * ret) + 
                    (qualifyingType != null ? 
                            qualifyingType.hashCode() : 0);
            TypeDeclaration declaration = 
                    getDeclaration();
            ret = (37 * ret) + 
                    declaration.hashCodeForCache();
            
            Map<TypeParameter, Type> typeArguments = 
                    getTypeArguments();
            if (!typeArguments.isEmpty()) {
                List<TypeParameter> typeParameters = 
                        declaration.getTypeParameters();
                for (int i=0, l=typeParameters.size(); 
                        i<l; i++) {
                    TypeParameter typeParameter = 
                            typeParameters.get(i);
                    Type typeArgument = 
                            typeArguments.get(typeParameter);
                    ret = (37 * ret) + 
                            (typeArgument != null ? 
                                    typeArgument.hashCode() : 0);
                }
            }
            ret = (37 * ret) + 
                    varianceOverrides.hashCode();

            hashCode = ret;
        }
        return hashCode;
    }
    
    private Map<TypeParameter,SiteVariance> collectVarianceOverrides() {
        Type qt = getQualifyingType();
        Map<TypeParameter,SiteVariance> qualifyingOverrides = 
                qt==null ? EMPTY_VARIANCE_MAP :
                    qt.collectVarianceOverrides();
        if (varianceOverrides.isEmpty()) {
            return qualifyingOverrides;
        }
        else if (qualifyingOverrides.isEmpty()) {
            return varianceOverrides;
        }
        else {
            Map<TypeParameter,SiteVariance> overrides = 
                    new HashMap<TypeParameter,SiteVariance>
                        (varianceOverrides);
            overrides.putAll(qualifyingOverrides);
            return overrides;
        }
    }
    
    /**
     * Given a set of use site variance overrides, adjust 
     * the given type to account for these variances.
     * 
     * Performs replacement of type parameters with their
     * bounds when the use-site variance is opposite to the
     * variance of the position in which the type parameter
     * occurs, adds use-site variance annotations to the 
     * given type, etc.
     * 
     * This operation must happen before any substitution 
     * operation, as a pre-processing phase before we
     * actually subtitute the type arguments.
     * 
     * @param type the type to which we're applying the 
     *        variance overrides
     * @param covariant true if the given type occurs in a 
     *        covariant position
     * @param contravariant false if the given type occurs 
     *        in a contravariant position
     * @param overrides the variance overrides
     *        
     * @return the new type after application of the rules
     *         for use-site variance substitution
     */
    private static Type applyVarianceOverrides(
            Type type, 
            boolean covariant, boolean contravariant, 
            Map<TypeParameter, SiteVariance> overrides) {
        if (overrides.isEmpty()) {
            return type;
        }
        TypeDeclaration dec = type.getDeclaration();
        Unit unit = dec.getUnit();
        if (type.isTypeParameter()) {
            SiteVariance siteVariance = overrides.get(dec);
            if (contravariant && siteVariance==OUT) {
                //if a type parameter occurs in a 
                //contravariant position, and the specified
                //use-site variance is "out", replace the
                //type parameter with its lower bound
                //Nothing, throwing away the use-site 
                //upper bound
                return unit.getNothingType();
            }
            else if (covariant && siteVariance==IN) {
                TypeParameter tp = (TypeParameter) dec;
                //if a type parameter occurs in a 
                //covariant position, and the specified
                //use-site variance is "in", replace the
                //type parameter with its upper bounds,
                //throwing away the use-site lower bound
                List<Type> bounds = tp.getSatisfiedTypes();
                List<Type> list = 
                        new ArrayList<Type>
                            (bounds.size()+1);
                for (Type bound: bounds) {
                    //ignore bounds in which the type 
                    //parameter itself occurs covariantly
                    //because they would result in a 
                    //stack overflow here
                    //TODO: perhaps we should just 
                    //      substitute Anything for the
                    //      type parameter in such bounds,
                    //      which would be a little more
                    //      precise
                    if (!bound.occursCovariantly(tp)) {
                        Type applied = 
                                applyVarianceOverrides(
                                        bound, 
                                        covariant, contravariant, 
                                        overrides);
                        addToIntersection(list, applied, unit);
                    }
                }
                return canonicalIntersection(list, unit);
            }
            else {
                return type;
            }
        }
        else if (type.isUnion()) {
            List<Type> list = 
                    new ArrayList<Type>();
            for (Type ut: type.getCaseTypes()) {
                addToUnion(list, 
                        applyVarianceOverrides(ut, 
                                covariant, contravariant,
                                overrides));
            }
            return union(list, unit);
        }
        else if (type.isIntersection()) {
            List<Type> list = 
                    new ArrayList<Type>();
            for (Type it: type.getSatisfiedTypes()) {
                addToIntersection(list, 
                        applyVarianceOverrides(it, 
                                covariant, contravariant,
                                overrides),
                        unit);
            }
            return canonicalIntersection(list, unit);
        }
        else {
            List<Type> args = 
                    type.getTypeArgumentList();
            List<TypeParameter> params = 
                    dec.getTypeParameters();
            if (params.isEmpty()) {
                // we have variance overrides from a 
                // qualifying type 
                // optimize, since no work to do
                return type;
            }
            List<Type> resultArgs = 
                    new ArrayList<Type>
                        (args.size());
            Map<TypeParameter,SiteVariance> varianceResults = 
                    new HashMap<TypeParameter,SiteVariance>
                            (type.getVarianceOverrides());
            for (int i = 0; i<args.size(); i++) {
                Type arg = args.get(i);
                if (arg==null) {
                    resultArgs.add(null);
                    continue;
                }
                TypeDeclaration argDec = 
                        arg.getDeclaration();
                TypeParameter param = params.get(i);
                if (type.isCovariant(param)) {
                    resultArgs.add(
                            applyVarianceOverrides(arg, 
                                    covariant, contravariant,
                                    overrides));
                }
                else if (type.isContravariant(param)) {
                    resultArgs.add(
                            applyVarianceOverrides(arg, 
                                    !covariant, !contravariant,
                                    overrides));
                }
                else {
                    if (contravariant) {
                        if (arg.isTypeParameter() &&
                                overrides.containsKey(argDec)) {
                            return unit.getNothingType();
                        }
                    }
                    else if (covariant) {
                        if (arg.isTypeParameter() &&
                                overrides.containsKey(argDec)) {
                            varianceResults.put(param,
                                    overrides.get(argDec));
                            resultArgs.add(arg);
                            continue;
                        }
                    }
                    Type resultArg = 
                            applyVarianceOverrides(arg, 
                                    covariant, contravariant,
                                    overrides);
                    if (resultArg.isNothing()) {
                        return resultArg;
                    }
                    resultArgs.add(resultArg);
                    if (arg.involvesTypeParameters(
                            overrides.keySet())) {
                        varianceResults.put(param, OUT);
                    }
                }
            }
            Type result = 
                    dec.appliedType(
                            type.getQualifyingType(), 
                            resultArgs);
            result.setVarianceOverrides(varianceResults);
            result.setUnderlyingType(type.getUnderlyingType());
            return result;
        }
    }
    
    @Override
    public int hashCode() {
        return getMemoisedHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) {
            return true;
        }
        if (obj == null || 
                !(obj instanceof Type)) {
            return false;
        }
        Type other = (Type) obj;
        Type qA = getQualifyingType();
        Type qB = other.getQualifyingType();
        if (qA!=qB && (qA==null || qB==null || !qA.equals(qB))) {
            return false;
        }
        TypeDeclaration aDecl = getDeclaration();
        TypeDeclaration bDecl = other.getDeclaration();
        if (!aDecl.equalsForCache(bDecl)) {
            return false;
        }
        
        Map<TypeParameter, Type> typeArgumentsA = 
                getTypeArguments();
        Map<TypeParameter, Type> typeArgumentsB = 
                other.getTypeArguments();
        if (typeArgumentsA.size() != typeArgumentsB.size()) {
            return false;
        }
        if (!typeArgumentsA.isEmpty()) {
            List<TypeParameter> typeParametersA = 
                    aDecl.getTypeParameters();
            for (int i=0, l=typeParametersA.size(); i<l; i++) {
                TypeParameter typeParameter = 
                        typeParametersA.get(i);
                Type typeArgumentA = 
                        typeArgumentsA.get(typeParameter);
                Type typeArgumentB = 
                        typeArgumentsB.get(typeParameter);
                if (typeArgumentA!=typeArgumentB &&
                        (typeArgumentA==null || 
                         typeArgumentB==null || 
                            !typeArgumentA.equals(typeArgumentB))) {
                    return false;
                }
            }
        }
        return getVarianceOverrides()
                .equals(other.getVarianceOverrides());
    }
    
    @Override
    public Type getFullType(Type wrappedType) {
        //TODO: put this back in, and find another way to
        //      propagate the type in static method refs
        //      of form Interface.member in
        //      visitQualifiedMemberExpression() and
        //      visitQualifiedTypeExpression()
//        TypeDeclaration declaration = getDeclaration();
//        if (!(declaration instanceof Class)) {           
//            return unknownType();
//        }
//        else {
            return super.getFullType(wrappedType);
//        }
    }

}
