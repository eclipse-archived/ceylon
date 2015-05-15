package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.SiteVariance.IN;
import static com.redhat.ceylon.model.typechecker.model.SiteVariance.OUT;
import static com.redhat.ceylon.model.typechecker.model.Unit.getAbstraction;
import static com.redhat.ceylon.model.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.model.typechecker.model.Util.addToSupertypes;
import static com.redhat.ceylon.model.typechecker.model.Util.addToUnion;
import static com.redhat.ceylon.model.typechecker.model.Util.getTypeArgumentMap;
import static com.redhat.ceylon.model.typechecker.model.Util.intersectionOfSupertypes;
import static com.redhat.ceylon.model.typechecker.model.Util.intersectionType;
import static com.redhat.ceylon.model.typechecker.model.Util.involvesTypeParameters;
import static com.redhat.ceylon.model.typechecker.model.Util.principalInstantiation;
import static com.redhat.ceylon.model.typechecker.model.Util.toTypeArgs;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.model.typechecker.context.ProducedTypeCache;
import com.redhat.ceylon.model.typechecker.model.UnknownType.ErrorReporter;
import com.redhat.ceylon.model.typechecker.util.ProducedTypeNamePrinter;


/**
 * A produced type with actual type arguments.
 * This represents something that is actually
 * considered a "type" in the language
 * specification.
 *
 * @author Gavin King
 */
public class ProducedType extends ProducedReference {
    
    private String underlyingType;
    private boolean isRaw;
    private ProducedType resolvedAliases;
    private TypeParameter typeConstructorParameter;
    
    // cache
    private int hashCode;
    private List<ProducedType> typeArgumentList;
    
    private Map<TypeParameter,SiteVariance> varianceOverrides = 
            Collections.emptyMap();
    
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
    
    ProducedType() {}
    
    @Override
    public TypeDeclaration getDeclaration() {
        return (TypeDeclaration) 
                getAbstraction(super.getDeclaration());
    }
    
    @Override
    void setDeclaration(Declaration declaration) {
        if (declaration instanceof TypeDeclaration) {
            super.setDeclaration(declaration);
        }
        else {
            throw new IllegalArgumentException("not a TypeDeclaration");
        }
    }
    
    public boolean isTypeConstructor() {
        return typeConstructorParameter!=null;
    }
    
    public TypeParameter getTypeConstructorParameter() {
        return typeConstructorParameter;
    }
    
    public void setTypeConstructor(TypeParameter typeConstructorParameter) {
        this.typeConstructorParameter = typeConstructorParameter;
    }
    
    /**
     * Is this type exactly the same type as the
     * given type? 
     */
    public boolean isExactly(ProducedType type) {
        return resolveAliases()
                .isExactlyInternal(type.resolveAliases());
    }
    
    public boolean isExactlyInternal(ProducedType type) {
        if (depth.get()>50) {
            throw new RuntimeException("undecidable subtyping");
        }
        depth.set(depth.get()+1);
        try {
            TypeDeclaration dec = getDeclaration();
            TypeDeclaration otherDec = type.getDeclaration();
            if (dec instanceof NothingType) {
                return type.isNothing();
            }
            else if (dec instanceof UnionType) {
                List<ProducedType> cases = getCaseTypes();
                if (otherDec instanceof UnionType) {
                    List<ProducedType> otherCases = 
                            type.getCaseTypes();
                    if (cases.size()!=otherCases.size()) {
                        return false;
                    }
                    else {
                        for (ProducedType c: cases) {
                            boolean found = false;
                            for (ProducedType oc: otherCases) {
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
                    ProducedType st = cases.get(0);
                    return st.isExactlyInternal(type);
                }
                else {
                    return false;
                }
            }
            else if (dec instanceof IntersectionType) {
                List<ProducedType> types = getSatisfiedTypes();
                if (otherDec instanceof IntersectionType) {
                    List<ProducedType> otherTypes = 
                            type.getSatisfiedTypes();
                    if (types.size()!=otherTypes.size()) {
                        return false;
                    }
                    else {
                        for (ProducedType c: types) {
                            boolean found = false;
                            TypeDeclaration cd = 
                                    c.getDeclaration();
                            for (ProducedType oc: otherTypes) {
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
                                    ProducedType cst = 
                                            getSupertypeInternal(cd);
                                    ProducedType ocst = 
                                            type.getSupertypeInternal(ocd);
                                    if (cst.isExactlyInternal(ocst)) {
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
                    ProducedType st = types.get(0);
                    return st.isExactlyInternal(type);
                }
                else {
                    return false;
                }
            }
            else if (otherDec instanceof UnionType) {
                List<ProducedType> otherCases = 
                        type.getCaseTypes();
                if (otherCases.size()==1) {
                    ProducedType st = otherCases.get(0);
                    return this.isExactlyInternal(st);
                }
                else {
                    return false;
                }
            }
            else if (otherDec instanceof IntersectionType) {
                List<ProducedType> otherTypes = 
                        type.getSatisfiedTypes();
                if (otherTypes.size()==1) {
                    ProducedType st = otherTypes.get(0);
                    return this.isExactlyInternal(st);
                }
                else {
                    return false;
                }
            }
            else if (isTypeConstructor() && 
                    type.isTypeConstructor()) {
                if (dec.equals(otherDec)) {
                    return true;
                }
                else {
                    List<ProducedType> paramsAsArgs = 
                            toTypeArgs(getTypeConstructorParameter());
                    ProducedType rt = 
                            dec.getProducedType(
                                    getQualifyingType(), 
                                    paramsAsArgs);
                    ProducedType ort = 
                            otherDec.getProducedType(
                                    type.getQualifyingType(), 
                                    paramsAsArgs);
                    //resolves aliases:
                    return rt.isExactly(ort);
                }
            }
            else if (isTypeConstructor() ||
                    type.isTypeConstructor()) {
                return false;
            }
            else {
                if (!otherDec.equals(dec)) {
                    return false;
                }
                else {
                    //for Java's static inner types, ignore the
                    //qualifying type
                    ProducedType qt = 
                            dec.isStaticallyImportable() ?
                                    null : 
                                    getQualifyingType();
                    ProducedType tqt = 
                            otherDec.isStaticallyImportable() ? 
                                    null : 
                                    type.getQualifyingType();
                    if (qt==null || tqt==null) {
                        if (qt!=tqt) {
                            return false;
                        }
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
                            if (!tqt.isExactly(qt)) {
                                return false;
                            }
                        }
                        else {
                            TypeDeclaration totd = 
                                    (TypeDeclaration) odc;
                            ProducedType tqts = 
                                    tqt.getSupertypeInternal(totd);
                            TypeDeclaration otd = 
                                    (TypeDeclaration) dc;
                            ProducedType qts = 
                                    qt.getSupertypeInternal(otd);
                            if (!qts.isExactlyInternal(tqts)) {
                                return false;
                            }
                        }
                    }
                    for (TypeParameter p: 
                            dec.getTypeParameters()) {
                        ProducedType arg = 
                                getTypeArguments()
                                    .get(p);
                        ProducedType otherArg = 
                                type.getTypeArguments()
                                    .get(p);
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
                                        !getUpperBoundIntersection(p)
                                            .isSubtypeOf(otherArg)) {
                                    return false;
                                }
                            }
                            else if (covariant && otherContravariant) {
                                //Inv<out Anything> == Inv<in Nothing>
                                if (!otherArg.isNothing() ||
                                        !getUpperBoundIntersection(p)
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
            }
        }
        finally {
            depth.set(depth.get()-1);
        }
    }

    /**
     * Is this type a supertype of the given type? 
     */
    public boolean isSupertypeOf(ProducedType type) {
        return type.isSubtypeOf(this);
    }

    /**
     * Is this type a subtype of the given type? 
     */
    public boolean isSubtypeOf(ProducedType type) {
        return type!=null && resolveAliases()
                .isSubtypeOfInternal(type.resolveAliases());
    }

    /**
     * Is this type a subtype of the given type? Ignore
     * a certain self type constraint.
     */
    public boolean isSubtypeOfInternal(final ProducedType type) {
        if (depth.get()>50) {
            throw new RuntimeException("undecidable subtyping");
        }
        depth.set(depth.get()+1);
        try {
            TypeDeclaration dec = getDeclaration();
            TypeDeclaration otherDec = type.getDeclaration();
            if (isNothing()) {
                return true;
            }
            else if (type.isNothing()) {
                return false;
            }
            else if (dec instanceof UnionType) {
                for (ProducedType ct: 
                        getInternalCaseTypes()) {
                    if (ct==null || 
                            !ct.isSubtypeOfInternal(type)) {
                        return false;
                    }
                }
                return true;
            }
            else if (otherDec instanceof UnionType) {
                for (ProducedType ct: 
                        type.getInternalCaseTypes()) {
                    if (ct!=null && 
                            isSubtypeOfInternal(ct)) {
                        return true;
                    }
                }
                return false;
            }
            else if (otherDec instanceof IntersectionType) {
                for (ProducedType ct: 
                        type.getInternalSatisfiedTypes()) {
                    if (ct!=null && 
                            !isSubtypeOfInternal(ct)) {
                        return false;
                    }
                }
                return true;
            }
            else if (dec instanceof IntersectionType) {
                if (otherDec instanceof ClassOrInterface) {
                    ProducedType pst = 
                            getSupertypeInternal(otherDec);
                    if (pst!=null && 
                            pst.isSubtypeOfInternal(type)) {
                        return true;
                    }
                }
                for (ProducedType ct: 
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
                if (dec.equals(otherDec)) {
                    return true;
                }
                else {
                    List<ProducedType> paramsAsArgs = 
                            toTypeArgs(getTypeConstructorParameter());
                    ProducedType qt = 
                            getQualifyingType();
                    ProducedType oqt = 
                            type.getQualifyingType();
                    ProducedType rt = 
                            dec.getProducedType(qt, 
                                    paramsAsArgs);
                    ProducedType ort = 
                            otherDec.getProducedType(oqt, 
                                    paramsAsArgs);
                    //resolves aliases:
                    return rt.isSubtypeOf(ort);
                }
            }
            else if (isTypeConstructor() || 
                    type.isTypeConstructor()) {
                return false;
            }
            else {
                ProducedType supertype = 
                        getSupertypeInternal(otherDec);
                if (supertype==null) {
                    return false;
                }
                else {
                    supertype = supertype.resolveAliases();
                    TypeDeclaration superDec = 
                            supertype.getDeclaration();
                    ProducedType stqt = 
                            superDec.isStaticallyImportable() ?
                                    null : 
                                    supertype.getQualifyingType();
                    ProducedType tqt =
                            otherDec.isStaticallyImportable() ? 
                                    null : 
                                    type.getQualifyingType();
                    if (stqt==null) {
                        if (tqt!=null) {
                            //probably extraneous!
                            return false;
                        }
                    }
                    else {
                        if (tqt==null) {
                            //probably extraneous!
                            return false;
                        }
                        else if (!otherDec.isMember()) {
                            //local types with a qualifying typed declaration do not need to obtain the
                            //qualifying type's supertype
                            if (!stqt.isSubtypeOf(tqt)) {
                                return false;
                            }
                        }
                        else {
                            //note that the qualifying type of the
                            //given type may be an invariant subtype
                            //of the type that declares the member
                            //type, as long as it doesn't refine the
                            //member type
                            TypeDeclaration totd = (TypeDeclaration) 
                                    otherDec.getContainer();
                            ProducedType tqts = 
                                    tqt.getSupertypeInternal(totd);
                            if (!stqt.isSubtypeOf(tqts)) {
                                return false;
                            }
                        }
                    }
                    for (TypeParameter p: 
                            otherDec.getTypeParameters()) {
                        ProducedType arg = 
                                supertype.getTypeArguments()
                                    .get(p);
                        ProducedType otherArg = 
                                type.getTypeArguments()
                                    .get(p);
                        if (arg==null || otherArg==null) {
                            return false;
                        }
                        else if (type.isCovariant(p)) {
                            if (supertype.isContravariant(p)) {
                                //Inv<in T> is a subtype of Inv<out Anything>
                                if (!p.getType()
                                        .isSubtypeOf(otherArg)) {
                                    return false;
                                }
                            }
                            else if (!arg.isSubtypeOfInternal(otherArg)) {
                                return false;
                            }
                        }
                        else if (type.isContravariant(p)) {
                            if (supertype.isCovariant(p)) {
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
                            if (supertype.isCovariant(p) && 
                                    !arg.isNothing() ||
                                supertype.isContravariant(p) && 
                                    !arg.isAnything() ||
                                !arg.isExactlyInternal(otherArg)) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }
        }
        finally { 
            depth.set(depth.get()-1);
        }
    }

    /**
     * Eliminate the given type from the union type.
     * (Performs a set complement operation.) Note
     * that this operation is not robust and only 
     * works if this is a union of the given type
     * with some other types that don't involve the
     * given type.
     */
    public ProducedType minus(ProducedType pt) {
        //canonicalize and then remove the type
        //from the resulting union
        return resolveAliases()
                .minusInternal(pt.resolveAliases());
    }
    
    /**
     * Eliminates unioned Null from a type in a
     * very special way that the backend prefers
     */
    public ProducedType eliminateNull() {
        TypeDeclaration dec = getDeclaration();
        Unit unit = dec.getUnit();
        Class nd = unit.getNullDeclaration();
        if (getDeclaration().inherits(nd)) {
            return unit.getNothingDeclaration().getType();
        }
        else if (dec instanceof UnionType) {
            List<ProducedType> caseTypes = getCaseTypes();
            List<ProducedType> types = 
                    new ArrayList<ProducedType>
                        (caseTypes.size());
            for (ProducedType ct: caseTypes) {
//                if (!ct.getDeclaration().inherits(nd)) {
                    addToUnion(types, ct.eliminateNull());
//                }
            }
            UnionType ut = new UnionType(unit);
            ut.setCaseTypes(types);
            return ut.getType();
        }
        else {
            return this;
        }
    }
    
    public ProducedType shallowMinus(ProducedType pt) {
        TypeDeclaration dec = getDeclaration();
        Unit unit = dec.getUnit();
        if (isSubtypeOf(pt)) {
            return unit.getNothingDeclaration().getType();
        }
        else if (dec instanceof UnionType) {
            List<ProducedType> caseTypes = getCaseTypes();
            List<ProducedType> types = 
                    new ArrayList<ProducedType>
                        (caseTypes.size());
            for (ProducedType ct: caseTypes) {
//                if (!ct.getDeclaration().inherits(nd)) {
                    addToUnion(types, ct.shallowMinus(pt));
//                }
            }
            UnionType ut = new UnionType(unit);
            ut.setCaseTypes(types);
            return ut.getType();
        }
        else {
            return this;
        }
    }
    
    private ProducedType minusInternal(ProducedType pt) {
        TypeDeclaration dec = getDeclaration();
        Unit unit = dec.getUnit();
        if (pt.coversInternal(this)) { //note: coversInternal() already calls getUnionOfCases()
            return unit.getNothingDeclaration().getType();
        }
        else {
            ProducedType ucts = getUnionOfCases();
            if (ucts.getDeclaration() instanceof UnionType) {
                List<ProducedType> cts = 
                        ucts.getCaseTypes();
                List<ProducedType> types = 
                        new ArrayList<ProducedType>
                            (cts.size());
                for (ProducedType ct: cts) {
                    addToUnion(types, ct.minus(pt));
                }
                UnionType ut = new UnionType(unit);
                ut.setCaseTypes(types);
                ProducedType type = ut.getType();
                return type.coversInternal(this) ? 
                        this : type;
            }
            else if (dec instanceof IntersectionType) {
                List<ProducedType> cts = 
                        ucts.getSatisfiedTypes();
                List<ProducedType> types = 
                        new ArrayList<ProducedType>
                            (cts.size());
                for (ProducedType ct: cts) {
                    addToIntersection(types, ct.minus(pt), 
                            unit);
                }
                IntersectionType ut = 
                        new IntersectionType(unit);
                ut.setSatisfiedTypes(types);
                ProducedType type = 
                        ut.canonicalize().getType();
                return type.coversInternal(this) ? 
                        this : type;
            }
            else if (dec instanceof TypeParameter) {
                ProducedType upperBoundsMinus = 
                        intersectionOfSupertypes(dec)
                                .minusInternal(pt);
                ProducedType type = 
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
     * Substitute the given types for the corresponding
     * given type parameters wherever they appear in the
     * type. Has the side-effect of performing disjoint
     * type analysis, simplifying union/intersection
     * types, even when there are no substitutions. 
     */
    public ProducedType substitute(
            Map<TypeParameter,ProducedType> substitutions) {
        return new Substitution()
            .substitute(this, substitutions)
            .simple();
    }

    private ProducedType substituteInternal(
            Map<TypeParameter,ProducedType> substitutions) {
        return new InternalSubstitution()
            .substitute(this, substitutions);
    }

    /**
     * A member or member type of the type with actual type 
     * arguments to the receiving type and invocation.
     */
    public ProducedReference getTypedReference(
            Declaration member, 
            List<ProducedType> typeArguments) {
        if (member instanceof TypeDeclaration) {
            TypeDeclaration td = (TypeDeclaration) member;
            return getTypeMember(td, typeArguments);
        }
        else {
            TypedDeclaration td = (TypedDeclaration) member;
            return getTypedMember(td, typeArguments);
        }
    }
    
    public ProducedTypedReference getTypedMember(
            TypedDeclaration member, 
            List<ProducedType> typeArguments) {
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
    public ProducedTypedReference getTypedMember(TypedDeclaration member, 
            List<ProducedType> typeArguments, boolean assigned) {
        TypeDeclaration type = 
                (TypeDeclaration) member.getContainer();
        ProducedType declaringType = getSupertype(type);
        /*if (declaringType==null) {
            return null;
        }
        else {*/
        ProducedTypedReference ptr = 
                new ProducedTypedReference(!assigned, assigned);
        ptr.setDeclaration(member);
        ptr.setQualifyingType(declaringType);
        Map<TypeParameter, ProducedType> map = 
                getTypeArgumentMap(member, declaringType, 
                        typeArguments);
        //map.putAll(sub(map));
        ptr.setTypeArguments(map);
        return ptr;
        //}
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
    public ProducedType getTypeMember(TypeDeclaration member, 
            List<ProducedType> typeArguments) {
        TypeDeclaration type = 
                (TypeDeclaration) member.getContainer();
        ProducedType declaringType = getSupertype(type);
        return member.getProducedType(declaringType, 
                typeArguments);
        /*ProducedType pt = new ProducedType();
        pt.setDeclaration(member);
        pt.setQualifyingType(declaringType);
        Map<TypeParameter, ProducedType> map = arguments(member, declaringType, typeArguments);
        //map.putAll(sub(map));
        pt.setTypeArguments(map);
        return pt;*/
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
    public ProducedType getProducedType(ProducedType receiver, 
            Declaration member, 
            List<ProducedType> typeArguments) {
        ProducedType receivingType;
        if (receiver==null) {
            receivingType = null;
        }
        else {
            TypeDeclaration type = 
                    (TypeDeclaration) member.getContainer();
            receivingType = receiver.getSupertype(type);
        }
        Map<TypeParameter, ProducedType> tam = 
                getTypeArgumentMap(member, receivingType, 
                        typeArguments);
        return new Substitution().substitute(this, tam);
    }

    public ProducedType getType() {
        return this;
    }
    
    /**
     * Get all supertypes of the type by traversing the whole
     * type hierarchy. Avoid using this!
     */
    public List<ProducedType> getSupertypes() {
        return getSupertypes(new ArrayList<ProducedType>(5));
    }
    
    private List<ProducedType> getSupertypes(List<ProducedType> list) {
        TypeDeclaration dec = getDeclaration();
        if (dec instanceof UnionType ||
            dec instanceof NothingType) {
            throw new RuntimeException("getSupertypes() not defined for union types or Nothing");
        }
        if (isWellDefined() && 
                addToSupertypes(list, this)) {
            ProducedType extendedType = 
                    getExtendedType();
            if (extendedType!=null) {
                extendedType.getSupertypes(list);
            }
            List<ProducedType> satisfiedTypes = 
                    getSatisfiedTypes();
            for (int i=0, l=satisfiedTypes.size(); 
                    i<l; i++) {
                satisfiedTypes.get(i)
                    .getSupertypes(list);
            }
        }
        return list;
    }
    
    /**
     * Given a type declaration, return a produced type of 
     * which this type is an invariant subtype.
     * 
     * @param dec a type declaration
     * 
     * @return a produced type of the given type declaration
     *         which is a supertype of this type, or null if
     *         there is no such supertype
     */
    public ProducedType getSupertype(TypeDeclaration dec) {
        //TODO: do I need to call resolveAliases() here?
        return /*resolveAliases().*/getSupertypeInternal(dec);
    }
    
    private ProducedType getSupertypeInternal(TypeDeclaration dec) {
        if (dec==null) {
            return null;
        }
        boolean complexType = 
                dec instanceof UnionType || 
                dec instanceof IntersectionType;
        boolean canCache = !complexType && 
                !hasUnderlyingType() && 
                collectVarianceOverrides().isEmpty() &&
                ProducedTypeCache.isEnabled();
        ProducedTypeCache cache = dec.getUnit().getCache();
        if (canCache && 
                cache.containsKey(this, dec)) {
            return cache.get(this, dec);
        }
        SupertypeCheck check = 
                checkSupertype(getDeclaration(), dec);
        ProducedType superType;
        if (check == SupertypeCheck.NO) {
            superType = null;
        }
        else if (check == SupertypeCheck.YES && 
                dec instanceof ClassOrInterface && 
                dec.isToplevel() && 
                dec.getTypeParameters().isEmpty()) {
            superType = dec.getType();
        }
        else if (check == SupertypeCheck.YES && 
                dec == getDeclaration() && 
                dec.isToplevel()) {
            superType = this;
        }
        else {
            superType = getSupertype(new SupertypeCriteria(dec));
        }
        if (canCache) {
            cache.put(this, dec, superType);
        }
        return superType;
    }
    
    private boolean hasUnderlyingType() {
        if (getUnderlyingType() != null) {
            return true;
        }
        List<ProducedType> tal = getTypeArgumentList();
        for (int i=0, l=tal.size(); 
                i<l; i++) {
            ProducedType ta = tal.get(i);
            if (ta != null && 
                    ta.hasUnderlyingType()) {
                return true;
            }
        }
        return false;
    }

    enum SupertypeCheck {
        YES, NO, MAYBE;
    }
    
    private static SupertypeCheck checkSupertype(
            TypeDeclaration declaration, 
            TypeDeclaration supertype) {
        // fail-fast: there are only two classes that can 
        // be supertypes of an interface
        if (declaration instanceof Interface && 
                supertype instanceof Class) {
            String supertypeName = 
                    supertype.getQualifiedNameString();
            if (supertypeName.equals("ceylon.language::Object") || 
                supertypeName.equals("ceylon.language::Anything")) {
                return SupertypeCheck.YES;
            }
            else {
                return SupertypeCheck.NO;
            }
        }
        // we don't know how to look for non-simple supertypes
        if (!(supertype instanceof Class) && 
            !(supertype instanceof Interface)) {
            return SupertypeCheck.MAYBE;
        }
        if (declaration instanceof Class || 
            declaration instanceof Interface) {
            if (declaration.equals(supertype)) {
                return SupertypeCheck.YES;
            }
            ClassOrInterface etd = 
                    declaration.getExtendedTypeDeclaration();
            if (etd!=null) {
                SupertypeCheck extended = 
                        checkSupertype(etd, supertype);
                if (extended == SupertypeCheck.YES) {
                    return extended;
                }
                // keep looking
            }
            List<ProducedType> sts = 
                    declaration.getSatisfiedTypes();
            for (ProducedType satisfiedType: sts) {
                TypeDeclaration std = 
                        satisfiedType.getDeclaration();
                SupertypeCheck satisfied = 
                        checkSupertype(std, supertype);
                if (satisfied == SupertypeCheck.YES) {
                    return satisfied;
                }
                // keep looking
            }
            // not in the interfaces, not in the extended type
            return SupertypeCheck.NO;
        }
        if (declaration instanceof UnionType) {
            List<ProducedType> cts = 
                    declaration.getCaseTypes();
            if (cts.isEmpty()) {
                return SupertypeCheck.NO;
            }
            // every case must have that supertype
            for (ProducedType caseType: cts) {
                TypeDeclaration ctd = 
                        caseType.getDeclaration();
                SupertypeCheck satisfied = 
                        checkSupertype(ctd, supertype);
                if (satisfied != SupertypeCheck.YES) {
                    return satisfied;
                }
                // keep looking
            }
            // in every case
            return SupertypeCheck.YES;
        }
        if (declaration instanceof IntersectionType) {
            List<ProducedType> sts = 
                    declaration.getSatisfiedTypes();
            if (sts.isEmpty()) {
                return SupertypeCheck.NO;
            }
            boolean perhaps = false;
            // any satisfied type will do
            for (ProducedType satisfiedType: sts) {
                TypeDeclaration std = 
                        satisfiedType.getDeclaration();
                SupertypeCheck satisfied = 
                        checkSupertype(std, supertype);
                if (satisfied == SupertypeCheck.YES) {
                    return satisfied;
                }
                else if (satisfied == SupertypeCheck.MAYBE) {
                    perhaps = true;
                }
                // keep looking
            }
            // did not find it, but perhaps it's in there?
            return perhaps ? 
                    SupertypeCheck.MAYBE : 
                    SupertypeCheck.NO;
        }
        return SupertypeCheck.MAYBE;
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
    public ProducedType getSupertype(Criteria c) {
        if (depth.get()>50) {
            throw new RuntimeException("undecidable canonicalization");
        }
        depth.set(depth.get()+1);
        try {
            if (c.satisfies(getDeclaration())) {
                return qualifiedByDeclaringType();
            }
            if (isWellDefined()) {
                //now let's call the two most difficult methods
                //in the whole code base:
                ProducedType result = 
                        getPrincipalInstantiation(c);
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
            depth.set(depth.get()-1);
        }
    }
    
    private ProducedType getPrincipalInstantiationFromCases(Criteria c,
            ProducedType result) {
        if (getDeclaration() instanceof UnionType) {
            //trying to infer supertypes of algebraic
            //types from their cases was resulting in
            //stack overflows and is not currently 
            //required by the spec
            final List<ProducedType> caseTypes = 
                    getInternalCaseTypes();
            if (caseTypes!=null && !caseTypes.isEmpty()) {
                //first find a common superclass or superinterface 
                //declaration that satisfies the criteria, ignoring
                //type arguments for now
                TypeDeclaration stc = 
                        findCommonSuperclass(c, caseTypes);
                if (stc!=null) {
                    //we found the declaration, now try to construct a 
                    //produced type that is a true common supertype
                    ProducedType candidateResult = 
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
            List<ProducedType> types) {
        TypeDeclaration result = null;
        TypeDeclaration td = 
                types.get(0).getDeclaration();
        for (TypeDeclaration std: 
                td.getSupertypeDeclarations()) {
            if (std instanceof ClassOrInterface && 
                    c.satisfies(std)) {
                for (ProducedType ct: types) {
                    TypeDeclaration ctd = 
                            ct.getDeclaration();
                    if (!ctd.inherits(std)) {
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
    
    public static ThreadLocal<Integer> depth = 
            new ThreadLocal<Integer>() {
        protected Integer initialValue() {
            return 0;
        }
    };
    
    private ProducedType getPrincipalInstantiation(Criteria c) {
        //search for the most-specific supertype 
        //for the given declaration
        
        ProducedType result = null;
        
        ProducedType extendedType = 
                getInternalExtendedType();
        if (extendedType!=null) {
            ProducedType possibleResult = 
                    extendedType.getSupertype(c);
            if (possibleResult!=null) {
                result = possibleResult;
            }
        }
        
        List<ProducedType> satisfiedTypes = 
                getInternalSatisfiedTypes();
        // cheaper iteration
        for (int i=0, l=satisfiedTypes.size(); 
                i<l; i++) {
            ProducedType satisfiedType = 
                    satisfiedTypes.get(i);
            ProducedType possibleResult = 
                    satisfiedType.getSupertype(c);
            if (possibleResult!=null) {
                if (result==null || 
                        possibleResult.isSubtypeOf(result)) {
                    result = possibleResult;
                }
                else if (!result.isSubtypeOf(possibleResult)) {
                    //TODO: this is still needed even though we keep intersections 
                    //      in canonical form because you can have stuff like
                    //      empty of Iterable<String>&Sized
                    TypeDeclaration rd = 
                            result.getDeclaration();
                    TypeDeclaration prd = 
                            possibleResult.getDeclaration();
                    
                    //Resolve ambiguities in favor of
                    //the most-refined declaration
                    /*if (rd.equals(prd)) {
                        List<ProducedType> args = constructPrincipalInstantiation(
                                rd, result, possibleResult);
                        //TODO: broken for member types! ugh :-(
                        result = rd.getProducedType(result.getQualifyingType(), args);
                    }
                    else if (rd.inherits(prd)) {
                    }
                    else if (prd.inherits(rd)) {
                        result = possibleResult;
                    }*/
                    
                    TypeDeclaration d = null;
                    if (rd.equals(prd)) {
                        d = rd;
                    }
                    //Resolve ambiguities in favor of 
                    //least-refined declaration (in
                    //order to take advantage of most
                    //specific type arguments)
                    /*else if (rd.inherits(prd)) {
                        d=prd;
                        result=result.getSupertype(d);
                    }
                    else if (prd.inherits(rd)) {
                        d=rd;
                        possibleResult=possibleResult.getSupertype(d);
                    }*/
                    
                    Unit unit = getDeclaration().getUnit();
                    if (d!=null) {
                        result = principalInstantiation(d, 
                                possibleResult, result, unit);
                    }
                    else {
                        //ambiguous! we can't decide between the two 
                        //supertypes which both satisfy the criteria
                        if (c.isMemberLookup() && 
                                !satisfiedTypes.isEmpty()) {
                            //for the case of a member lookup, try to find
                            //a common supertype by forming the union of 
                            //the two possible results (since A|B is always
                            //a supertype of A&B)
                            UnionType ut = 
                                    new UnionType(unit);
                            List<ProducedType> caseTypes = 
                                    new ArrayList<ProducedType>(2);
                            //if (extendedType!=null) caseTypes.add(extendedType);
                            //caseTypes.addAll(satisfiedTypes);
                            caseTypes.add(result);
                            caseTypes.add(possibleResult);
                            ut.setCaseTypes(caseTypes);
                            result = ut.getType().getSupertype(c);
                            if (result==null) {
                                return new UnknownType(unit).getType();
                            }
                        }
                        else {
                            return new UnknownType(unit).getType();
                        }
                    }
                }
            }
        }
        
        return result;
    }

    private ProducedType qualifiedByDeclaringType() {
        ProducedType qt = getQualifyingType();
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
                ProducedType pt = new ProducedType();
                pt.setDeclaration(declaration);
                //replace the qualifying type with
                //the supertype of the qualifying 
                //type that declares this nested
                //type, substituting type arguments
                TypeDeclaration dtd = (TypeDeclaration) 
                        declaration.getContainer();
                ProducedType declaringType = 
                        qt.getSupertypeInternal(dtd);
                pt.setQualifyingType(declaringType);
                Map<TypeParameter, ProducedType> tam = 
                        getTypeArgumentMap(declaration, 
                                declaringType, 
                                getTypeArgumentList());
                pt.setTypeArguments(tam);
                pt.setVarianceOverrides(getVarianceOverrides());
                return pt;
            }
        }
    }

    private ProducedType getCommonSupertype(
            List<ProducedType> caseTypes,
            TypeDeclaration dec) {
        //now try to construct a common produced
        //type that is a common supertype by taking
        //the type args and unioning them
        List<TypeParameter> typeParameters = 
                dec.getTypeParameters();
        List<ProducedType> args = 
                new ArrayList<ProducedType>
                    (typeParameters.size());
        Map<TypeParameter,SiteVariance> variances = 
                new HashMap<TypeParameter,SiteVariance>();
        for (TypeParameter tp: typeParameters) {
            ProducedType result;
            Unit unit = getDeclaration().getUnit();
            if (tp.isCovariant()) {
                List<ProducedType> union = 
                        new ArrayList<ProducedType>
                            (caseTypes.size());
                for (ProducedType pt: caseTypes) {
                    if (pt==null) {
                        return null;
                    }
                    ProducedType st = 
                            pt.getSupertypeInternal(dec);
                    if (st==null) {
                        return null;
                    }
                    addToUnion(union, 
                            st.getTypeArguments()
                                .get(tp));
                }
                UnionType ut = 
                        new UnionType(unit);
                ut.setCaseTypes(union);
                result = ut.getType();
            }
            else if (tp.isContravariant()) { 
                List<ProducedType> intersection = 
                        new ArrayList<ProducedType>
                            (caseTypes.size());
                for (ProducedType pt: caseTypes) {
                    if (pt==null) {
                        return null;
                    }
                    ProducedType st = 
                            pt.getSupertypeInternal(dec);
                    if (st==null) {
                        return null;
                    }
                    ProducedType arg = 
                            st.getTypeArguments()
                                .get(tp);
                    addToIntersection(intersection, 
                            arg, unit, false);
                }
                IntersectionType it = 
                        new IntersectionType(unit);
                it.setSatisfiedTypes(intersection);
                result = it.canonicalize(false).getType();
            }
            else {
                //invariant is harder, need to account for
                //use site variances!
                List<ProducedType> union = 
                        new ArrayList<ProducedType>
                            (caseTypes.size());
                List<ProducedType> intersection = 
                        new ArrayList<ProducedType>
                            (caseTypes.size());
                boolean covariant = false;
                boolean contravariant = false;
                for (ProducedType pt: caseTypes) {
                    if (pt==null) {
                        return null;
                    }
                    ProducedType st = 
                            pt.getSupertypeInternal(dec);
                    if (st==null) {
                        return null;
                    }
                    ProducedType arg = 
                            st.getTypeArguments()
                                .get(tp);
                    if (st.isCovariant(tp)) {
                        covariant = true;
                        addToUnion(union, arg);
                    } 
                    else if (st.isContravariant(tp)) {
                        contravariant = true;
                        addToIntersection(intersection, 
                                arg, unit, false);
                    }
                    else {
                        addToUnion(union, arg);
                        addToIntersection(intersection, 
                                arg, unit, false);
                    }
                }
                UnionType ut = 
                        new UnionType(unit);
                ut.setCaseTypes(union);
                ProducedType utt = ut.getType();
                IntersectionType it = 
                        new IntersectionType(unit);
                it.setSatisfiedTypes(intersection);
                ProducedType itt = it.getType();
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
                    ProducedType upperBound = 
                            intersectionOfSupertypes(tp);
                    result = upperBound;
                    variances.put(tp, OUT);
                    //Note: we could have used "in Nothing"
                    //      here instead
                }
            }
            if (tp.isTypeConstructor()) {
                result.setTypeConstructor(tp);
            }
            args.add(result);
        }
        //check that the unioned type args
        //satisfy the type constraints
        //disabled this according to #52
        /*for (int i=0; i<args.size(); i++) {
            TypeParameter tp = dec.getTypeParameters().get(i);
            for (ProducedType ub: tp.getSatisfiedTypes()) {
                if (!args.get(i).isSubtypeOf(ub)) {
                    return null;
                }
            }
        }*/
        //recurse to the qualifying type
        ProducedType outerType;
        if (dec.isMember() && 
                !dec.isStaticallyImportable()) {
            TypeDeclaration outer = 
                    (TypeDeclaration) dec.getContainer();
            List<ProducedType> list = 
                    new ArrayList<ProducedType>
                        (caseTypes.size());
            for (ProducedType ct: caseTypes) {
                if (ct==null) {
                    return null;
                }
                List<ProducedType> intersectedTypes;
                if (ct.getDeclaration() 
                        instanceof IntersectionType) {
                    intersectedTypes = 
                            ct.getSatisfiedTypes();
                }
                else {
                    intersectedTypes = singletonList(ct);
                }
                for (ProducedType it: intersectedTypes) {
                    if (it.getDeclaration().isMember()) {
                        ProducedType st = 
                                it.getQualifyingType()
                                    .getSupertypeInternal(outer);
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
        ProducedType candidateResult = 
                dec.getProducedType(outerType, args);
        candidateResult.setVarianceOverrides(variances);
        //check the the resulting type is *really*
        //a subtype (take variance into account)
        for (ProducedType pt: caseTypes) {
            if (!pt.isSubtypeOf(candidateResult)) {
                return null;
            }
        }
        return candidateResult;
    }
    
    
    /**
     * Get the type arguments as a tuple. 
     */
    public List<ProducedType> getTypeArgumentList() {
        if (typeArgumentList==null || 
                !ProducedTypeCache.isEnabled()) {
            TypeDeclaration dec = getDeclaration();
            List<TypeParameter> tps = 
                    dec.getTypeParameters();
            if (tps.isEmpty()) {
                return emptyList();
            }
            else {
                List<ProducedType> argList = 
                        new ArrayList<ProducedType>
                            (tps.size());
                Map<TypeParameter, ProducedType> args = 
                        getTypeArguments();
                // cheaper c-for than foreach
                for (int i=0, l=tps.size(); i<l; i++) {
                    TypeParameter tp = tps.get(i);
                    ProducedType arg = args.get(tp);
                    if (arg==null) {
                        Unit unit = dec.getUnit();
                        arg = new UnknownType(unit).getType();
                    }
                    argList.add(arg);
                }
                argList = unmodifiableList(argList);
                typeArgumentList = argList;
                return argList;
            }
        }
        else {
            return typeArgumentList;
        }
    }

    /**
     * Determine if this is a decidable supertype, i.e. if it
     * obeys the restriction that types with contravariant 
     * type parameters may only appear in covariant positions. 
     * 
     * @return a list of type parameters which appear in illegal 
     *         positions
     */
    public List<TypeDeclaration> checkDecidability() {
        List<TypeDeclaration> errors = 
                new ArrayList<TypeDeclaration>();
        for (TypeParameter tp: 
                getDeclaration().getTypeParameters()) {
            ProducedType pt = getTypeArguments().get(tp);
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
        TypeDeclaration declaration = getDeclaration();
        if (declaration instanceof TypeParameter) {
            //nothing to do
        }
        else if (declaration instanceof UnionType) {
            for (ProducedType ct: getCaseTypes()) {
                ct.checkDecidability(covariant, contravariant, 
                        errors);
            }
        }
        else if (declaration instanceof IntersectionType) {
            for (ProducedType ct: getSatisfiedTypes()) {
                ct.checkDecidability(covariant, contravariant, 
                        errors);
            }
        }
        else {
            for (TypeParameter tp: 
                    declaration.getTypeParameters()) {
                if (!covariant && tp.isContravariant()) {
                    //a type with contravariant parameters appears at
                    //a contravariant location in satisfies / extends
                    if (!errors.contains(declaration)) {
                        errors.add(declaration);
                    }
                }
                ProducedType pt = 
                        getTypeArguments().get(tp);
                if (pt!=null) {
                    if (tp.isCovariant()) {
                        pt.checkDecidability(covariant, 
                                contravariant, errors);
                    }
                    else if (tp.isContravariant()) {
                        if (covariant|contravariant) { 
                            pt.checkDecidability(!covariant, 
                                    !contravariant, errors); 
                        }
                        else {
                            //else if we are in an invariant 
                            //position, it stays invariant
                            pt.checkDecidability(covariant, 
                                    contravariant, errors);
                        }
                    }
                    else {
                        pt.checkDecidability(false, false, 
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
                new ArrayList<TypeParameter>();
        checkVariance(covariant, contravariant, 
                declaration, errors);
        return errors;
    }
    
    private void checkVariance(
            boolean covariant, 
            boolean contravariant,
            Declaration declaration, 
            List<TypeParameter> errors) {
        TypeDeclaration dec = getDeclaration();
        if (dec instanceof TypeParameter) {
            TypeParameter tp = (TypeParameter) dec;
            Declaration parameterizedDec = 
                    tp.getDeclaration();
            boolean ok = 
                    parameterizedDec.equals(declaration) ||
                    ((covariant || !tp.isCovariant()) && 
                    (contravariant || !tp.isContravariant()));
            if (!ok) {
                //a covariant type parameter appears in a 
                //contravariant location, or a contravariant 
                //type parameter appears in a covariant location.
                errors.add(tp);
            }
        }
        else if (dec instanceof UnionType) {
            for (ProducedType ct: getCaseTypes()) {
                ct.checkVariance(
                        covariant, contravariant, 
                        declaration, errors);
            }
        }
        else if (dec instanceof IntersectionType) {
            for (ProducedType ct: getSatisfiedTypes()) {
                ct.checkVariance(
                        covariant, contravariant, 
                        declaration, errors);
            }
        }
        else {
            ProducedType qt = getQualifyingType();
            if (qt!=null) {
                qt.checkVariance(
                        covariant, contravariant, 
                        declaration, errors);
            }
            for (TypeParameter tp: 
                    dec.getTypeParameters()) {
                ProducedType pt = 
                        getTypeArguments().get(tp);
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
        ProducedType qt = getQualifyingType();
        if (qt!=null && 
                !qt.isWellDefined()) {
            return false;
        }
        List<ProducedType> tas = getTypeArgumentList();
        for (int i=0; i<tps.size(); i++) {
            ProducedType at = tas.get(i);
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
        TypeDeclaration d = getDeclaration();
        if (d instanceof UnknownType) {
            return true;
        }
        else if (d instanceof UnionType) {
            for (ProducedType ct: d.getCaseTypes()) {
                if (ct.containsUnknowns()) return true;
            }
        }
        else if (d instanceof IntersectionType) {
            for (ProducedType st: d.getSatisfiedTypes()) {
                if (st.containsUnknowns()) return true;
            }
        }
        else if (d instanceof NothingType) {
            return false;
        }
        else {
            ProducedType qt = getQualifyingType();
            if (qt!=null && 
                    qt.containsUnknowns()) {
                return true;
            }
            if (!isTypeConstructor()) {
                List<ProducedType> tas = getTypeArgumentList();
                for (ProducedType at: tas) {
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

    public String getFirstUnknownTypeError(boolean includeSuperTypes) {
        TypeDeclaration d = getDeclaration();
        if (d instanceof UnknownType) {
            ErrorReporter errorReporter = 
                    ((UnknownType) d).getErrorReporter();
            return errorReporter != null ? 
                    errorReporter.getMessage() : null;
        }
        else if (d instanceof UnionType) {
            for (ProducedType ct: 
                    getDeclaration().getCaseTypes()) {
                String ret = 
                        ct.getFirstUnknownTypeError(includeSuperTypes);
                if (ret != null) {
                    return ret;
                }
            }
        }
        else if (d instanceof IntersectionType) {
            for (ProducedType st: 
                    getDeclaration().getSatisfiedTypes()) {
                String ret = 
                        st.getFirstUnknownTypeError(includeSuperTypes);
                if (ret != null) {
                    return ret;
                }
            }
        }
        else if (d instanceof NothingType) {
            return null;
        }
        else {
            if (includeSuperTypes) {
                ProducedType et = d.getExtendedType();
                if (et != null) {
                    String ret = 
                            et.getFirstUnknownTypeError(includeSuperTypes);
                    if (ret != null) {
                        return ret;
                    }
                }
                for (ProducedType st: d.getSatisfiedTypes()) {
                    String ret = 
                            st.getFirstUnknownTypeError(includeSuperTypes);
                    if (ret != null) {
                        return ret;
                    }
                }
            }
            ProducedType qt = getQualifyingType();
            if (qt!=null) {
                String ret = 
                        qt.getFirstUnknownTypeError(includeSuperTypes);
                if (ret != null) {
                    return ret;
                }
            }
            List<ProducedType> tas = getTypeArgumentList();
            for (ProducedType at: tas) {
                if (at!=null) {
                    String ret = 
                            at.getFirstUnknownTypeError(false);
                    if (ret != null) {
                        return ret;
                    }
                }
            }
        }
        return null;
    }

    public boolean containsDeclaration(Declaration td) {
        TypeDeclaration d = getDeclaration();
        if (d instanceof UnknownType) {
            return false;
        }
        else if (d instanceof UnionType) {
            for (ProducedType ct: d.getCaseTypes()) {
                if (ct.containsDeclaration(td)) {
                    return true;
                }
            }
        }
        else if (d instanceof IntersectionType) {
            for (ProducedType st: d.getSatisfiedTypes()) {
                if (st.containsDeclaration(td)) {
                    return true;
                }
            }
        }
        else if (d instanceof NothingType) {
            return false;
        }
        else {
            if (d.equals(td)) {
                return true;
            }
            ProducedType qt = getQualifyingType();
            if (qt!=null && 
                    qt.containsDeclaration(td)) {
                return true;
            }
            List<ProducedType> tas = getTypeArgumentList();
            for (ProducedType at: tas) {
                if (at==null || 
                        at.containsDeclaration(td)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private ProducedType withVarianceOverrides(
            Map<TypeParameter,SiteVariance> varianceOverrides) {
        TypeDeclaration declaration = getDeclaration();
        if (declaration.isParameterized()) {
            ProducedType result = new ProducedType();
            result.setDeclaration(declaration);
            result.setQualifyingType(getQualifyingType());
            Map<TypeParameter, ProducedType> typeArguments = 
                    getTypeArguments();
            Map<TypeParameter,SiteVariance> variances = 
                    new HashMap<TypeParameter,SiteVariance>
                        (varianceOverrides.size());
            Map<TypeParameter,ProducedType> args = 
                    new HashMap<TypeParameter,ProducedType>
                        (typeArguments.size());
            for (Map.Entry<TypeParameter,ProducedType> entry: 
                    typeArguments.entrySet()) {
                TypeParameter param = entry.getKey();
                ProducedType arg = entry.getValue();
                TypeDeclaration d = arg.getDeclaration();
                if (d instanceof TypeParameter) {
                    TypeParameter p = (TypeParameter) d;
                    SiteVariance var = 
                            varianceOverrides.get(p);
                    if (var!=null) {
                        if (p.isInvariant()) {
                            variances.put(param, var);
                        }
                        else if (p.isCovariant() 
                                    && var==IN) {
                            //simplify Co<in T> to Co<Anything> 
                            // TODO: should it be getUpperBoundIntersection(p)
                            args.put(param, 
                                    getUpperBoundIntersection(param));
                            continue;
                        }
                        else if (p.isContravariant() 
                                    && var==OUT) {
                            //simplify Contra<out T> to Contra<Nothing>
                            ProducedType nothing = 
                                    declaration.getUnit()
                                        .getNothingDeclaration()
                                        .getType();
                            args.put(param, nothing);
                            continue;
                        }
                    }
                }
                args.put(param, arg);
            }
            result.setTypeArguments(args);
            result.setVarianceOverrides(variances);
            result.setRaw(isRaw());
            result.setUnderlyingType(getUnderlyingType());
            return result;
        }
        else {
            return this;
        }
    }
    
    private List<ProducedType> getInternalSatisfiedTypes() {
        TypeDeclaration dec = getDeclaration();
        List<ProducedType> sts = dec.getSatisfiedTypes();
        Map<TypeParameter, ProducedType> args = 
                getTypeArguments();
        if (args.isEmpty()) {
            return sts;
        }
        List<ProducedType> satisfiedTypes = 
                new ArrayList<ProducedType>
                    (sts.size());
        for (ProducedType st: sts) {
            Map<TypeParameter, SiteVariance> vos = 
                    getVarianceOverrides();
            ProducedType t = 
                    st.withVarianceOverrides(vos)
                        .substituteInternal(args);
            satisfiedTypes.add(t);
        }
        return satisfiedTypes;
    }

    private ProducedType getInternalExtendedType() {
        TypeDeclaration dec = getDeclaration();
        ProducedType et = dec.getExtendedType();
        if (et==null) {
            return null;
        }
        else {
            Map<TypeParameter, ProducedType> args = 
                    getTypeArguments();
            if (args.isEmpty()) {
                return et;
            }
            Map<TypeParameter, SiteVariance> vos = 
                    getVarianceOverrides();
            return et.withVarianceOverrides(vos)
                    .substituteInternal(args);
        }
    }

    private List<ProducedType> getInternalCaseTypes() {
        TypeDeclaration dec = getDeclaration();
        List<ProducedType> cts = dec.getCaseTypes();
        if (cts==null) {
            return null;
        }
        else {
            Map<TypeParameter, ProducedType> args = 
                    getTypeArguments();
            if (args.isEmpty()) {
                return cts;
            }
            List<ProducedType> caseTypes = 
                    new ArrayList<ProducedType>
                        (cts.size());
            Map<TypeParameter, SiteVariance> vos = 
                    getVarianceOverrides();
            for (ProducedType ct: cts) {
                ProducedType t = 
                        ct.withVarianceOverrides(vos)
                            .substituteInternal(args);
                caseTypes.add(t);
            }
            return caseTypes;
        }
    }

    public List<ProducedType> getSatisfiedTypes() {
        TypeDeclaration dec = getDeclaration();
        List<ProducedType> sts = dec.getSatisfiedTypes();
        Map<TypeParameter,ProducedType> args = 
                getTypeArguments();
        if (args.isEmpty()) {
            return sts; 
        }
        List<ProducedType> satisfiedTypes = 
                new ArrayList<ProducedType>
                    (sts.size());
        Map<TypeParameter, SiteVariance> vos = 
                getVarianceOverrides();
        for (int i=0, l=sts.size(); i<l; i++) {
            ProducedType st = sts.get(i);
            ProducedType t = 
                    st.withVarianceOverrides(vos)
                            .substitute(args);
            satisfiedTypes.add(t);
        }
        return satisfiedTypes;
    }

    public ProducedType getExtendedType() {
        TypeDeclaration dec = getDeclaration();
        ProducedType et = dec.getExtendedType();
        if (et==null) {
            return null;
        }
        else {
            Map<TypeParameter,ProducedType> args = 
                    getTypeArguments();
            if (args.isEmpty()) {
                return et;
            }
            else {
                Map<TypeParameter, SiteVariance> vos = 
                        getVarianceOverrides();
                return et.withVarianceOverrides(vos)
                        .substitute(args);
            }
        }
    }

    public List<ProducedType> getCaseTypes() {
        TypeDeclaration dec = getDeclaration();
        List<ProducedType> cts = dec.getCaseTypes();
        if (cts==null) {
            return null;
        }
        else {
            Map<TypeParameter,ProducedType> args = 
                    getTypeArguments();
            if (args.isEmpty()) {
                return cts;
            }
            List<ProducedType> caseTypes = 
                    new ArrayList<ProducedType>
                        (cts.size());
            for (ProducedType ct: cts) {
                Map<TypeParameter, SiteVariance> vos = 
                        getVarianceOverrides();
                ProducedType t = 
                        ct.withVarianceOverrides(vos)
                            .substitute(args);
                caseTypes.add(t);
            }
            return caseTypes;
        }
    }

    /**
     * Substitutes type arguments for type parameters.
     * This default strategy eliminates duplicate types
     * from unions after substituting arguments.
     * @author Gavin King
     */
    static class Substitution {
        
        ProducedType substitute(final ProducedType pt, 
                Map<TypeParameter, ProducedType> substitutions) {
            Declaration dec;
            TypeDeclaration ptd = pt.getDeclaration();
            if (ptd instanceof UnionType) {
                UnionType ut = 
                        new UnionType(ptd.getUnit());
                List<ProducedType> cts = 
                        ptd.getCaseTypes();
                List<ProducedType> types = 
                        new ArrayList<ProducedType>
                            (cts.size());
                for (ProducedType ct: cts) {
                    addTypeToUnion(ct, substitutions, types);
                }
                ut.setCaseTypes(types);
                dec = ut;
            }
            else if (ptd instanceof IntersectionType) {
                IntersectionType it = 
                        new IntersectionType(ptd.getUnit());
                List<ProducedType> sts = 
                        ptd.getSatisfiedTypes();
                List<ProducedType> types = 
                        new ArrayList<ProducedType>
                            (sts.size());
                for (ProducedType ct: sts) {
                    addTypeToIntersection(ct, substitutions, 
                            types);
                }
                it.setSatisfiedTypes(types);
                dec = it.canonicalize();
            }
            else {
                if (ptd instanceof TypeParameter) {
                    ProducedType sub = 
                            substitutions.get(ptd);
                    if (sub!=null) {
                        if (ptd.getTypeParameters().isEmpty() || 
                            pt.isTypeConstructor()) {
                            return sub;
                        }
                        else {
                            //needed for higher-order generics
                            List<ProducedType> tal = 
                                    pt.getTypeArgumentList();
                            List<ProducedType> sta = 
                                    new ArrayList<ProducedType>
                                        (tal.size());
                            for (ProducedType ta: tal) {
                                sta.add(ta.substitute(substitutions));
                            }
                            return substituteIntoTypeConstructors(sub, 
                                    sta, substitutions, ptd.getUnit());
                        }
                    }
                }
                dec = ptd;
            }
            return substitutedType(dec, pt, substitutions);
        }
        
        private ProducedType substituteIntoTypeConstructors(
                ProducedType sub, List<ProducedType> sta,
                Map<TypeParameter, ProducedType> substitutions,
                Unit unit) {
            TypeDeclaration sd = sub.getDeclaration();
            if (sd instanceof UnionType) {
                List<ProducedType> list =
                        new ArrayList<ProducedType>();
                for (ProducedType ct: sd.getCaseTypes()) {
                    addToUnion(list, 
                            substituteIntoTypeConstructors(ct,
                                    sta, substitutions, unit));
                }
                UnionType ut = 
                        new UnionType(unit);
                ut.setCaseTypes(list);
                return ut.getType();
            }
            else if (sd instanceof IntersectionType) {
                List<ProducedType> list =
                        new ArrayList<ProducedType>();
                for (ProducedType st: sd.getSatisfiedTypes()) {
                    addToIntersection(list, 
                            substituteIntoTypeConstructors(st,
                                    sta, substitutions, unit),
                            unit);
                }
                IntersectionType it = 
                        new IntersectionType(unit);
                it.setSatisfiedTypes(list);
                return it.canonicalize().getType();
            }
            else {
                ProducedType sqt = sub.getQualifyingType();
                ProducedType qt = 
                        sqt==null ? null : 
                        substitute(sqt, substitutions);
                return sd.getProducedType(qt, sta);
            }
        }
        
        void addTypeToUnion(ProducedType ct, 
                Map<TypeParameter, ProducedType> substitutions, 
                List<ProducedType> types) {
            if (ct==null) {
                types.add(null);
            }
            else {
                addToUnion(types, 
                        substitute(ct, substitutions));
            }
        }

        void addTypeToIntersection(ProducedType ct, 
                Map<TypeParameter, ProducedType> substitutions, 
                List<ProducedType> types) {
            if (ct==null) {
                types.add(null);
            }
            else {
                addToIntersection(types, 
                        substitute(ct, substitutions), 
                        ct.getDeclaration().getUnit());
            }
        }

        private Map<TypeParameter, ProducedType> 
        substitutedTypeArguments(ProducedType pt, 
                Map<TypeParameter, ProducedType> substitutions) {
            Map<TypeParameter, ProducedType> typeArguments = 
                    pt.getTypeArguments();
            if (substitutions.isEmpty() && 
                typeArguments.isEmpty()) {
                return Collections.emptyMap();
            }
            Map<TypeParameter, ProducedType> map = 
                    new HashMap<TypeParameter, ProducedType>
                        (typeArguments.size());
            for (Map.Entry<TypeParameter, ProducedType> e: 
                    typeArguments.entrySet()) {
                if (e.getValue()!=null) {
                    map.put(e.getKey(), 
                            substitute(e.getValue(), 
                                    substitutions));
                }
            }
            /*ProducedType dt = pt.getDeclaringType();
            if (dt!=null) {
                map.putAll(substituted(dt, substitutions));
            }*/
            return map;
        }

        private ProducedType substitutedType(Declaration dec, 
                ProducedType pt,
                Map<TypeParameter, ProducedType> substitutions) {
            ProducedType type = new ProducedType();
            type.setDeclaration(dec);
            type.setTypeConstructor(pt.getTypeConstructorParameter());
            type.setUnderlyingType(pt.getUnderlyingType());
            ProducedType qt = pt.getQualifyingType();
            if (qt!=null) {
                type.setQualifyingType(substitute(qt, substitutions));
            }
            type.setTypeArguments(substitutedTypeArguments(pt, substitutions));
            type.setVarianceOverrides(pt.getVarianceOverrides());
            return type;
        }
            
    }
    
    /**
     * This special strategy for internal use by the 
     * containing class does not eliminate duplicate 
     * types from unions after substituting arguments.
     * This is to avoid a stack overflow that otherwise
     * results! (Determining if a union contains 
     * duplicates requires recursion to the argument
     * substitution code via some very difficult-to-
     * understand flow.)
     * @author Gavin King
     */
    static class InternalSubstitution extends Substitution {
    
        private void addType(ProducedType ct,
                Map<TypeParameter, ProducedType> substitutions,
                List<ProducedType> types) {
            if (ct!=null) {
                types.add(substitute(ct, substitutions));
            }
        }
        
        @Override void addTypeToUnion(ProducedType ct,
                Map<TypeParameter, ProducedType> substitutions,
                List<ProducedType> types) {
            addType(ct, substitutions, types);
        }

        @Override void addTypeToIntersection(ProducedType ct,
                Map<TypeParameter, ProducedType> substitutions,
                List<ProducedType> types) {
            addType(ct, substitutions, types);
        }
        
    }

    @Override
    public String toString() {
        return isTypeConstructor() ?
                "TypeConstructor[" + getProducedTypeName() + "]" :
                "Type[" + getProducedTypeName() + "]";
    }
    
    public String getProducedTypeName() {
        return getProducedTypeName(null);
    }

    public String getProducedTypeName(Unit unit) {
        return ProducedTypeNamePrinter.DEFAULT
                .getProducedTypeName(this, unit);
    }
    
    public String getProducedTypeNameInSource(Unit unit) {
        return ProducedTypeNamePrinter.ESCAPED
                .getProducedTypeName(this, unit);
    }
    
    public String getProducedTypeName(boolean abbreviate) {
        return getProducedTypeName(abbreviate, null);
    }

    public String getProducedTypeName(boolean abbreviate, Unit unit) {
        return new ProducedTypeNamePrinter(abbreviate)
                        .getProducedTypeName(this, unit);
    }

    private String getSimpleProducedTypeQualifiedName() {
        StringBuilder ptn = new StringBuilder();
        //if (getDeclaration().isMember()) {
        ProducedType qt = getQualifyingType();
        if (qt!=null) {
            ptn.append(qt.getProducedTypeQualifiedName())
               .append(".")
               .append(getDeclaration().getName());
        }
        //}
        else {
            ptn.append(getDeclaration().getQualifiedNameString());
        }
        if (!getTypeArgumentList().isEmpty()) {
            ptn.append("<");
            boolean first = true;
            for (ProducedType t: getTypeArgumentList()) {
                if (first) {
                    first = false;
                }
                else {
                    ptn.append(",");
                }
                if (t==null) {
                    ptn.append("?");
                }
                else {
                    ptn.append(t.getProducedTypeQualifiedName());
                }
            }
            ptn.append(">");
        }
        return ptn.toString();
    }

    public String getProducedTypeQualifiedName() {
        if (getDeclaration()==null) {
            //unknown type
            return null;
        }
        if (getDeclaration() instanceof UnionType) {
            StringBuilder name = new StringBuilder();
            for (ProducedType pt: getCaseTypes()) {
                if (pt==null) {
                    name.append("unknown");
                }
                else {
                    name.append(pt.getProducedTypeQualifiedName());
                }
                name.append("|");
            }
            return name.substring(0,name.length()>0?name.length()-1:0);
        }
        else if (getDeclaration() instanceof IntersectionType) {
            StringBuilder name = new StringBuilder();
            for (ProducedType pt: getSatisfiedTypes()) {
                if (pt==null) {
                    name.append("unknown");
                }
                else {
                    name.append(pt.getProducedTypeQualifiedName());
                }
                name.append("&");
            }
            return name.substring(0,name.length()>0?name.length()-1:0);
        }
        else {            
            return getSimpleProducedTypeQualifiedName();
        }
    }

    /**
     * Form a union type of all cases of the type, 
     * recursively reducing cases to their cases
     */
    public ProducedType getUnionOfCases() {
        TypeDeclaration sdt = getDeclaration();
        Unit unit = getDeclaration().getUnit();
        //if X is an intersection type A&B, and A is an
        //enumerated type with cases U and V, then the cases
        //of X are the intersection (U|V)&B canonicalized to
        //the union U&B|V&B
        if (sdt instanceof IntersectionType) {
            List<ProducedType> sts = 
                    sdt.getSatisfiedTypes();
            List<ProducedType> list = 
                    new ArrayList<ProducedType>
                        (sts.size());
            for (ProducedType st: sts) {
                addToIntersection(list, 
                        st.getUnionOfCases()
                          .withVarianceOverrides(getVarianceOverrides()),
                            //argument substitution is unnecessary
                            //.substitute(getTypeArguments()), 
                        unit);
            }
            IntersectionType it = 
                    new IntersectionType(unit);
            it.setSatisfiedTypes(list);
            return it.canonicalize().getType();
        }
        else {
            List<ProducedType> cts = 
                    sdt.getCaseTypes();
            if (cts==null) {
                return this;
            }
            //otherwise, if X is a union A|B, or an enumerated 
            //type, with cases A and B, and A is an enumerated 
            //type with cases U and V, then the cases of X are
            //the union U|V|B
            else {
                //build a union of all the cases
                List<ProducedType> list = 
                        new ArrayList<ProducedType>
                            (cts.size());
                for (ProducedType ct: cts) {
                    addToUnion(list, 
                            ct.withVarianceOverrides(getVarianceOverrides())
                              .substitute(getTypeArguments())
                              .getUnionOfCases()); //note recursion
                }
                UnionType ut = new UnionType(unit);
                ut.setCaseTypes(list);
                return ut.getType();
            }
        }
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
    public boolean covers(ProducedType st) {
        return resolveAliases()
                .coversInternal(st.resolveAliases());
    }
    
    /*public boolean coversInternal(ProducedType t, Stack<TypeDeclaration> stack) {
        ProducedType uoc = t.getUnionOfCases();
        //X covers Y if the union of cases of Y is 
        //a subtype of X
        if (uoc.isSubtypeOfInternal(this)) {
            return true;
        }
        TypeDeclaration dec = t.getDeclaration();
        boolean decHasEquals = 
                dec instanceof ClassOrInterface || 
                dec instanceof TypeParameter;
        Map<TypeParameter, ProducedType> args = t.getTypeArguments();
        List<ProducedType> cts = uoc.getCaseTypes();
        if (cts!=null && !(decHasEquals && stack.contains(dec))) {
            //X covers Y if Y has the cases A|B|C and 
            //X covers all of A, B, and C
            for (ProducedType ct: cts) {
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
            ProducedType et = dec.getExtendedType();
            if (et!=null && coversInternal(et.substituteInternal(args), stack)) {
                return true;
            }
            //X covers Y if Y satisfies Z and X covers Z
            for (ProducedType st: dec.getSatisfiedTypes()) {
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
    private boolean coversInternal(ProducedType t) {
        ProducedType uoc = t.getUnionOfCases();
        //X covers Y if the union of cases of Y is 
        //a subtype of X
        if (uoc.isSubtypeOfInternal(this)) {
            return true;
        }
        TypeDeclaration dec = t.getDeclaration();
        if (dec instanceof UnionType) {
            //X covers Y if Y has the cases A|B|C and 
            //X covers all of A, B, and C
            for (ProducedType ct: uoc.getCaseTypes()) {
                if (!coversInternal(ct)) {
                    return false;
                }
            }
            return true;
        }
        else {
            //X covers Y if Y extends Z and X covers Z
            ProducedType et = t.getExtendedType();
            if (et!=null) {
//                if (coversInternal(et)) {
//                    return true;
//                }
                //decompose a type T extends U for an 
                //enumerated type U of A|B|... to 
                //the union type T&A|T&B|...
                ProducedType stu = et.getUnionOfCases();
                if (stu.getDeclaration() instanceof UnionType) {
                    ProducedType it = 
                            intersectionType(stu, 
                                    t, dec.getUnit());
                    if (it.isSubtypeOf(this)) {
                        return true;
                    }
                }
            }
            //X covers Y if Y satisfies Z and X covers Z
            for (ProducedType st: t.getSatisfiedTypes()) {
//                if (coversInternal(st)) {
//                    return true;
//                }
                //decompose a type T satisfies U
                //for an enumerated type U of A|B|... to 
                //the union type T&A|T&B|...
                ProducedType stu = st.getUnionOfCases();
                if (stu.getDeclaration() instanceof UnionType) {
                    ProducedType it = 
                            intersectionType(stu, 
                                    t, dec.getUnit());
                    if (it.isSubtypeOf(this)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    public ProducedType withoutUnderlyingType() {
        ProducedType pt = new ProducedType();
        pt.setDeclaration(getDeclaration());
        pt.setQualifyingType(getQualifyingType());
        pt.setTypeArguments(getTypeArguments());
        pt.setVarianceOverrides(getVarianceOverrides());
        pt.setTypeConstructor(getTypeConstructorParameter());
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
    
    public ProducedType resolveAliases() {
        if (isTypeConstructor()) {
            return this;
        }
        // cache the resolved version
        if (resolvedAliases == null) {
            // really compute it
            if (depth.get()>50) {
                throw new RuntimeException("undecidable canonicalization");
            }
            depth.set(depth.get()+1);
            try {
                resolvedAliases = curriedResolveAliases();
            }
            finally { 
                depth.set(depth.get()-1);
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
        //return curriedResolveAliases();
    }
    
    private ProducedType curriedResolveAliases() {
        TypeDeclaration d = getDeclaration();
        Unit unit = d.getUnit();
        if (d instanceof UnionType) {
            List<ProducedType> caseTypes = 
                    d.getCaseTypes();
            List<ProducedType> list = 
                    new ArrayList<ProducedType>
                        (caseTypes.size());
            for (ProducedType pt: caseTypes) {
                addToUnion(list, 
                        pt.resolveAliases());
            }
            UnionType ut = new UnionType(unit);
            ut.setCaseTypes(list);
            return ut.getType();
        }
        if (d instanceof IntersectionType) {
            List<ProducedType> satisfiedTypes = 
                    d.getSatisfiedTypes();
            List<ProducedType> list = 
                    new ArrayList<ProducedType>
                        (satisfiedTypes.size());
            for (ProducedType pt: satisfiedTypes) {
                addToIntersection(list, 
                        pt.resolveAliases(), 
                        unit);
            }
            IntersectionType ut = 
                    new IntersectionType(unit);
            ut.setSatisfiedTypes(list);
            return ut.canonicalize().getType();
        }
        
        ProducedType qt = getQualifyingType();
        ProducedType aliasedQualifyingType = 
                qt==null ? null : 
                    qt.resolveAliases();
        if (isTypeConstructor()) {
            TypeDeclaration ud;
            if (d.isAlias()) {
                ud = d.getExtendedTypeDeclaration();
            }
            else {
                ud = d;
            }
            ProducedType rt = ud.getType();
            rt.setQualifyingType(aliasedQualifyingType);
            rt.setTypeConstructor(getTypeConstructorParameter());
            return rt;
        }
        else {
            List<ProducedType> args = 
                    getTypeArgumentList();
            List<ProducedType> aliasedArgs = 
                    args.isEmpty() ? 
                        Collections.<ProducedType>emptyList() : 
                        new ArrayList<ProducedType>(args.size());
            for (ProducedType arg: args) {
                aliasedArgs.add(arg==null ? 
                        null : arg.resolveAliases());
            }
            if (d.isAlias()) {
                ProducedType et = d.getExtendedType();
                if (et == null) {
                    return new UnknownType(d.getUnit()).getType();
                }
                return et.resolveAliases()
                        .substitute(getTypeArgumentMap(d, 
                                aliasedQualifyingType, 
                                aliasedArgs));
            }
            else {
                ProducedType result = 
                        d.getProducedType(aliasedQualifyingType, 
                                aliasedArgs);
                result.setVarianceOverrides(getVarianceOverrides());
                return result;
            }
        }
    }
    
    private ProducedType simple() {
        TypeDeclaration d = getDeclaration();
        if (d instanceof UnionType && 
                d.getCaseTypes().size()==1) {
            return d.getCaseTypes().get(0);
        }
        if (d instanceof IntersectionType && 
                d.getSatisfiedTypes().size()==1) {
            return d.getSatisfiedTypes().get(0);
        }
        List<ProducedType> args = getTypeArgumentList();
        List<ProducedType> simpleArgs;
        ProducedType qt = getQualifyingType();
        if (args.isEmpty()) {
            if (qt == null) {
                return this; // we have nothing to simplify
            }
            simpleArgs = emptyList();
        }
        else {
            simpleArgs = 
                    new ArrayList<ProducedType>
                        (args.size());
            for (ProducedType arg: args) {
                simpleArgs.add(arg==null ? 
                            null : arg.simple());
            }
        }
        ProducedType ret = 
                d.getProducedType(qt==null ? 
                            null : qt.simple(), 
                        simpleArgs);
        ret.setUnderlyingType(getUnderlyingType());
        ret.setTypeConstructor(getTypeConstructorParameter());
        ret.setRaw(isRaw());
        ret.setVarianceOverrides(getVarianceOverrides());
        return ret;
    }
    
    public boolean containsTypeParameters() {
        TypeDeclaration d = getDeclaration();
        if (d instanceof TypeParameter) {
            return true;
        }
        else if (d instanceof UnionType) {
            for (ProducedType ct: getCaseTypes()) {
                if (ct.containsTypeParameters()) {
                    return true;
                }
            }
        }
        else if (d instanceof IntersectionType) {
            for (ProducedType st: getSatisfiedTypes()) {
                if (st.containsTypeParameters()) {
                    return true;
                }
            }
        }
        else {
            for (ProducedType at: getTypeArgumentList()) {
                if (at!=null && 
                        at.containsTypeParameters()) {
                    return true;
                }
            }
            ProducedType qt = getQualifyingType();
            if (qt!=null && 
                    qt.containsTypeParameters()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsTypeParameters(List<TypeParameter> params) {
        TypeDeclaration d = getDeclaration();
        if (d instanceof TypeParameter) {
            if (params.contains(d)) {
                return true;
            }
        }
        else if (d instanceof UnionType) {
            for (ProducedType ct: getCaseTypes()) {
                if (ct.containsTypeParameters(params)) {
                    return true;
                }
            }
        }
        else if (d instanceof IntersectionType) {
            for (ProducedType st: getSatisfiedTypes()) {
                if (st.containsTypeParameters(params)) {
                    return true;
                }
            }
        }
        else {
            for (ProducedType at: getTypeArgumentList()) {
                if (at!=null && 
                        at.containsTypeParameters(params)) {
                    return true;
                }
            }
            ProducedType qt = getQualifyingType();
            if (qt!=null && 
                    qt.containsTypeParameters(params)) {
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
            for (ProducedType ct: getCaseTypes()) {
                if (ct.containsTypeAliases()) return true;
            }
        }
        else if (d instanceof IntersectionType) {
            for (ProducedType st: getSatisfiedTypes()) {
                if (st.containsTypeAliases()) return true;
            }
        }
        else {
            for (ProducedType at: getTypeArgumentList()) {
                if (at!=null && at.containsTypeAliases()) return true;
            }
            ProducedType qt = getQualifyingType();
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
        TypeDeclaration d = getDeclaration();
        if (d instanceof TypeAlias||
            d instanceof ClassAlias||
            d instanceof InterfaceAlias) {
            if (visited.contains(d)) {
                return new ArrayList<TypeDeclaration>(singletonList(d));
            }
            if (d.getExtendedType()!=null) {
                List<TypeDeclaration> l = d.getExtendedType()
                        .isRecursiveTypeAliasDefinition(extend(d, visited));
                if (!l.isEmpty()) {
                    return extend(d, l);
                }
            }
            for (ProducedType bt: d.getBrokenSupertypes()) {
                List<TypeDeclaration> l = 
                        bt.isRecursiveTypeAliasDefinition(extend(d, visited));
                if (!l.isEmpty()) {
                    return extend(d, l);
                }
            }
        }
        else if (d instanceof UnionType) {
            for (ProducedType ct: getCaseTypes()) {
                List<TypeDeclaration> l = 
                        ct.isRecursiveTypeAliasDefinition(visited);
                if (!l.isEmpty()) return l;
            }
        }
        else if (d instanceof IntersectionType) {
            for (ProducedType st: getSatisfiedTypes()) {
                List<TypeDeclaration> l = 
                        st.isRecursiveTypeAliasDefinition(visited);
                if (!l.isEmpty()) return l;
            }
        }
        else {
            for (ProducedType at: getTypeArgumentList()) {
                if (at!=null) {
                    List<TypeDeclaration> l = 
                            at.isRecursiveTypeAliasDefinition(visited);
                    if (!l.isEmpty()) return l;
                }
            }
            ProducedType qt = getQualifyingType();
            if (qt!=null) {
                List<TypeDeclaration> l = 
                        qt.isRecursiveTypeAliasDefinition(visited);
                if (!l.isEmpty()) return l;
            }
        }
        return emptyList();
    }
    
    public List<TypeDeclaration> isRecursiveRawTypeDefinition(
            Set<TypeDeclaration> visited) {
        TypeDeclaration d = getDeclaration();
        if (d instanceof TypeAlias||
            d instanceof ClassAlias||
            d instanceof InterfaceAlias) {
            if (visited.contains(d)) {
                return new ArrayList<TypeDeclaration>(singletonList(d));
            }
            if (d.getExtendedType()!=null) {
                List<TypeDeclaration> l = d.getExtendedType()
                        .isRecursiveRawTypeDefinition(extend(d, visited));
                if (!l.isEmpty()) {
                    return extend(d, l);
                }
            }
            for (ProducedType bt: d.getBrokenSupertypes()) {
                List<TypeDeclaration> l = 
                        bt.isRecursiveRawTypeDefinition(extend(d, visited));
                if (!l.isEmpty()) {
                    return extend(d, l);
                }
            }
        }
        else if (d instanceof UnionType) {
            for (ProducedType ct: getCaseTypes()) {
                List<TypeDeclaration> l = 
                        ct.isRecursiveRawTypeDefinition(visited);
                if (!l.isEmpty()) return l;
            }
        }
        else if (d instanceof IntersectionType) {
            for (ProducedType st: getSatisfiedTypes()) {
                List<TypeDeclaration> l = 
                        st.isRecursiveRawTypeDefinition(visited);
                if (!l.isEmpty()) return l;
            }
        }
        else {
            if (visited.contains(d)) {
                return new ArrayList<TypeDeclaration>(singletonList(d));
            }
            if (d.getExtendedType()!=null) {
                List<TypeDeclaration> i = d.getExtendedType()
                        .isRecursiveRawTypeDefinition(extend(d, visited));
                if (!i.isEmpty()) {
                    i.add(0, d);
                    return i;
                }
            }
            for (ProducedType bt: d.getBrokenSupertypes()) {
                List<TypeDeclaration> l = 
                        bt.isRecursiveRawTypeDefinition(extend(d, visited));
                if (!l.isEmpty()) {
                    return extend(d, l);
                }
            }
            for (ProducedType st: getSatisfiedTypes()) {
                List<TypeDeclaration> l = 
                        st.isRecursiveRawTypeDefinition(extend(d, visited));
                if (!l.isEmpty()) {
                    return extend(d, l);
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
    
    public boolean isAnything() {
        TypeDeclaration d = getDeclaration();
        return d instanceof Class && 
                d.equals(d.getUnit().getAnythingDeclaration());
    }
    
    public int getMemoisedHashCode() {
        if (hashCode == 0) {
            int ret = 17;
            ProducedType qualifyingType = 
                    getQualifyingType();
            ret = (37 * ret) + 
                    (qualifyingType != null ? 
                            qualifyingType.hashCode() : 0);
            TypeDeclaration declaration = 
                    getDeclaration();
            ret = (37 * ret) + 
                    declaration.hashCodeForCache();
            
            Map<TypeParameter, ProducedType> typeArguments = 
                    getTypeArguments();
            if (!typeArguments.isEmpty()) {
                List<TypeParameter> typeParameters = 
                        declaration.getTypeParameters();
                for (int i=0, l=typeParameters.size(); 
                        i<l; i++) {
                    TypeParameter typeParameter = 
                            typeParameters.get(i);
                    ProducedType typeArgument = 
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
        ProducedType qt = getQualifyingType();
        Map<TypeParameter,SiteVariance> qualifyingOverrides = 
                qt==null ? 
                        Collections.<TypeParameter,SiteVariance>emptyMap() :
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
    
    ProducedType applyVarianceOverrides(ProducedType type,
            boolean covariant, boolean contravariant) {
        Map<TypeParameter, SiteVariance> overrides = 
                collectVarianceOverrides();
        if (overrides.isEmpty()) {
            return type;
        }
        TypeDeclaration dec = type.getDeclaration();
        Unit unit = dec.getUnit();
        if (dec instanceof TypeParameter) {
            SiteVariance siteVariance = overrides.get(dec);
            if (contravariant && siteVariance==OUT) {
                return unit.getNothingDeclaration().getType();
            }
            else if (covariant && siteVariance==IN) {
                return type.getUpperBoundIntersection((TypeParameter) dec);
            }
            else {
                return type;
            }
        }
        else if (dec instanceof UnionType) {
            List<ProducedType> list = 
                    new ArrayList<ProducedType>();
            for (ProducedType ut: type.getCaseTypes()) {
                addToUnion(list, 
                        applyVarianceOverrides(ut, 
                                covariant, contravariant));
            }
            UnionType unionType = 
                    new UnionType(unit);
            unionType.setCaseTypes(list);
            return unionType.getType();
        }
        else if (dec instanceof IntersectionType) {
            List<ProducedType> list = 
                    new ArrayList<ProducedType>();
            for (ProducedType it: type.getSatisfiedTypes()) {
                addToIntersection(list, 
                        applyVarianceOverrides(it, 
                                covariant, contravariant),
                        unit);
            }
            IntersectionType intersectionType = 
                    new IntersectionType(unit);
            intersectionType.setSatisfiedTypes(list);
            return intersectionType.canonicalize().getType();
        }
        else {
            List<ProducedType> args = 
                    type.getTypeArgumentList();
            List<TypeParameter> params = 
                    dec.getTypeParameters();
            if (params.isEmpty()) {
                // we have variance overrides from a 
                // qualifying type 
                // optimize, since no work to do
                return type;
            }
            List<ProducedType> resultArgs = 
                    new ArrayList<ProducedType>
                        (args.size());
            Map<TypeParameter,SiteVariance> varianceResults = 
                    new HashMap<TypeParameter,SiteVariance>
                            (type.getVarianceOverrides());
            for (int i = 0; i<args.size(); i++) {
                ProducedType arg = args.get(i);
                if (arg==null) {
                    resultArgs.add(null);
                    continue;
                }
                TypeDeclaration argDec = 
                        arg.getDeclaration();
                TypeParameter param = params.get(i);
                if (type.isCovariant(param)) {
                    resultArgs.add(applyVarianceOverrides(arg, 
                            covariant, contravariant));
                }
                else if (type.isContravariant(param)) {
//                    if (covariant||contravariant) {
                        resultArgs.add(applyVarianceOverrides(arg, 
                                !covariant, !contravariant));
//                    }
//                    else {
//                        resultArgs.add(applyVarianceOverrides(arg, 
//                                covariant, contravariant));
//                    }
                }
                else {
                    if (contravariant) {
                        if (argDec instanceof TypeParameter &&
                                overrides.containsKey(argDec)) {
                            return unit.getNothingDeclaration().getType();
                        }
                    }
                    else if (covariant) {
                        if (argDec instanceof TypeParameter &&
                                overrides.containsKey(argDec)) {
                            varianceResults.put(param,
                                    overrides.get(argDec));
                            resultArgs.add(arg);
                            continue;
                        }
                    }
                    ProducedType resultArg = 
                            applyVarianceOverrides(arg, 
                                    covariant, contravariant);
                    if (resultArg.isNothing()) {
                        return resultArg;
                    }
                    resultArgs.add(resultArg);
                    if (involvesTypeParameters(arg, 
                            overrides.keySet())) {
                        varianceResults.put(param, OUT);
                    }
                }
            }
            ProducedType result = 
                    dec.getProducedType(type.getQualifyingType(), 
                            resultArgs);
            result.setVarianceOverrides(varianceResults);
            result.setUnderlyingType(getUnderlyingType());
            return result;
        }
    }

    private ProducedType getUpperBoundIntersection(TypeParameter tp) {
        List<ProducedType> sts = tp.getSatisfiedTypes();
        List<ProducedType> list = 
                new ArrayList<ProducedType>
                    (sts.size());
        Unit unit = tp.getUnit();
        for (ProducedType st: sts) {
            addToIntersection(list, st, unit);
        }
        IntersectionType it = 
                new IntersectionType(unit);
        it.setSatisfiedTypes(list);
        return it.getType();
    }
    
    @Override
    public int hashCode() {
        return getMemoisedHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || 
                !(obj instanceof ProducedType)) {
            return false;
        }
        ProducedType other = (ProducedType) obj;
        ProducedType qA = getQualifyingType();
        ProducedType qB = other.getQualifyingType();
        if (qA!=qB && (qA==null || qB==null || !qA.equals(qB))) {
            return false;
        }
        TypeDeclaration aDecl = getDeclaration();
        TypeDeclaration bDecl = other.getDeclaration();
        if (!aDecl.equalsForCache(bDecl)) {
            return false;
        }
        
        Map<TypeParameter, ProducedType> typeArgumentsA = 
                getTypeArguments();
        Map<TypeParameter, ProducedType> typeArgumentsB = 
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
                ProducedType typeArgumentA = 
                        typeArgumentsA.get(typeParameter);
                ProducedType typeArgumentB = 
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
    public ProducedType getFullType(ProducedType wrappedType) {
        //TODO: put this back in, and find another way to
        //      propagate the type in static method refs
        //      of form Interface.member in
        //      visitQualifiedMemberExpression() and
        //      visitQualifiedTypeExpression()
//        TypeDeclaration declaration = getDeclaration();
//        if (!(declaration instanceof Class)) {           
//            return new UnknownType(declaration.getUnit()).getType();
//        }
//        else {
            return super.getFullType(wrappedType);
//        }
    }
    
}
