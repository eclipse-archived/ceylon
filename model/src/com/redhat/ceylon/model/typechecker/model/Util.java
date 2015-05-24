package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.SiteVariance.IN;
import static com.redhat.ceylon.model.typechecker.model.SiteVariance.OUT;
import static java.lang.Character.charCount;
import static java.lang.Character.isLowerCase;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.BackendSupport;

public class Util {
    
    static final List<ProducedType> NO_TYPE_ARGS = 
            Collections.<ProducedType>emptyList();    
    
    static final Map<TypeParameter, ProducedType> EMPTY_TYPE_ARG_MAP = 
            Collections.<TypeParameter,ProducedType>emptyMap();
    
    static final Map<TypeParameter, SiteVariance> EMPTY_VARIANCE_MAP = 
            emptyMap();
    /**
     * Is the second scope contained by the first scope?
     */
    public static boolean contains(Scope outer, Scope inner) {
        if (outer != null) {
            while (inner!=null) {
                if (inner.equals(outer)) {
                    return true;
                }
                inner = inner.getScope();
            }
        }
        return false;
    }
    
    /**
     * Get the nearest containing scope that is not a
     * ConditionScope. 
     */
    public static Scope getRealScope(Scope scope) {
        while (!(scope instanceof Package)) {
            if (!(scope instanceof ConditionScope)) {
                return scope;
            }
            scope = scope.getContainer();
        }
        return null;
    }
    
    /**
     * Get the class or interface that "this" and "super" 
     * refer to. 
     */
    public static ClassOrInterface getContainingClassOrInterface(Scope scope) {
        while (!(scope instanceof Package)) {
            if (scope instanceof ClassOrInterface) {
                return (ClassOrInterface) scope;
            }
            scope = scope.getContainer();
        }
        return null;
    }
    
    /**
     * Get the declaration that contains the specified declaration, if any.
     */
    public static Declaration getContainingDeclaration(Declaration d) {
        if (d.isToplevel()) return null;
        Scope scope = d.getContainer();
        while (!(scope instanceof Package)) {
            if (scope instanceof Declaration) {
                return (Declaration) scope;
            }
            scope = scope.getContainer();
        }
        return null;
    }

    /**
     * Get the declaration that contains the specified scope, if any.
     */
    public static Declaration getContainingDeclarationOfScope(Scope scope) {
        while (!(scope instanceof Package)) {
            if (scope instanceof Declaration) {
                return (Declaration) scope;
            }
            scope = scope.getContainer();
        }
        return null;
    }

    /**
     * Get the class or interface that "outer" refers to. 
     */
    public static ProducedType getOuterClassOrInterface(Scope scope) {
        Boolean foundInner = false;
        while (!(scope instanceof Package)) {
            if (scope instanceof ClassOrInterface) {
                if (foundInner) {
                    return ((ClassOrInterface) scope).getType();
                }
                else {
                    foundInner = true;
                }
            }
            scope = scope.getContainer();
        }
        return null;
    }
    
    /**
     * Convenience method to bind a single type argument 
     * to a toplevel type declaration.  
     */
    public static ProducedType producedType(TypeDeclaration declaration, 
            ProducedType typeArgument) {
        if (declaration==null) return null;
        return declaration.getProducedType(null, 
                singletonList(typeArgument));
    }

    /**
     * Convenience method to bind a list of type arguments
     * to a toplevel type declaration.  
     */
    public static ProducedType producedType(TypeDeclaration declaration, 
            ProducedType... typeArguments) {
        if (declaration==null) return null;
        return declaration.getProducedType(null, 
                asList(typeArguments));
    }

    public static boolean isResolvable(Declaration declaration) {
        return declaration.getName()!=null &&
            !declaration.isSetter() && //return getters, not setters
            !declaration.isAnonymous(); //don't return the type associated with an object dec 
    }
    
    public static boolean isAbstraction(Declaration d) {
        return d instanceof Overloadable && 
                ((Overloadable) d).isAbstraction();
    }

    public static boolean notOverloaded(Declaration d) {
        if(!d.isFunctional())
            return true;
        Overloadable f = (Overloadable) d;
        return  !f.isOverloaded() ||
                f.isAbstraction();
    }
    
    public static boolean isOverloadedVersion(Declaration decl) {
        return (decl instanceof Overloadable) &&
                ((Overloadable) decl).isOverloaded() &&
                !((Overloadable) decl).isAbstraction();
    }

    static boolean hasMatchingSignature(List<ProducedType> signature, 
            boolean ellipsis, Declaration d) {
        return hasMatchingSignature(signature, ellipsis, d, true);
    }
    
    static boolean hasMatchingSignature(List<ProducedType> signature, 
            boolean spread, Declaration d, 
            boolean excludeAbstractClasses) {
        if (excludeAbstractClasses && 
                d instanceof Class && 
                ((Class) d).isAbstract()) {
            return false;
        }
        if (d instanceof Functional) {
            Functional f = (Functional) d;
            if (f.isAbstraction()) {
                return false;
            }
            else {
                Unit unit = d.getUnit();
                List<ParameterList> pls = 
                        f.getParameterLists();
                if (pls!=null && !pls.isEmpty()) {
                    ParameterList pl = pls.get(0);
                    List<Parameter> params = 
                            pl.getParameters();
                    int size = params.size();
                    boolean hasSeqParam = 
                            pl.hasSequencedParameter();
                    int sigSize = signature.size();
                    if (hasSeqParam) {
                        size--;
                        if (sigSize<size) {
                            return false;
                        }
                    }
                    else if (sigSize!=size) {
                        return false;
                    }
                    for (int i=0; i<size; i++) {
                        MethodOrValue pm = 
                                params.get(i).getModel();
                        if (pm==null) return false;
                        ProducedType pdt = 
                                pm.getProducedReference(null, 
                                        NO_TYPE_ARGS)
                                    .getFullType();
                        if (pdt==null) return false;
                        ProducedType sdt = signature.get(i);
                        if (!matches(sdt, pdt, d.getUnit())) {
                            return false;
                        }
                    }
                    if (hasSeqParam) {
                        ProducedType pdt = 
                                params.get(size).getModel()
                                .getProducedReference(null, 
                                        NO_TYPE_ARGS)
                                .getFullType();
                        if (pdt==null || 
                                pdt.getTypeArgumentList().isEmpty()) {
                            return false;
                        }
                        //Note: don't use Unit.getIterableType() because this
                        //      gets called from model loader out-of-phase
                        ProducedType ipdt = 
                                pdt.getTypeArgumentList().get(0);  
                        for (int i=size; i<sigSize; i++) {
                            if (spread && i==sigSize-1) {
                                ProducedType sdt = signature.get(i);
                                ProducedType isdt = 
                                        unit.getIteratedType(sdt);
                                if (!matches(isdt, ipdt, d.getUnit())) {
                                    return false;
                                }
                            }
                            else {
                                ProducedType sdt = signature.get(i);
                                if (!matches(sdt, ipdt, d.getUnit())) {
                                    return false;
                                }
                            }
                        }
                    }
                    else if (spread) {
                        // if the method doesn't take sequenced params
                        // and we have an ellipsis let's not use it 
                        // since we expect a variadic method
                        // TODO: this is basically wrong now that we
                        //       can spread tuples
                        return false;
                    }
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        else {
            return false;
        }
    }
    
    public static boolean matches(ProducedType argType, ProducedType paramType, 
            Unit unit) {
        if (paramType==null || argType==null) {
            return false;
        }
        //Ignore optionality for resolving overloads, since
        //all Java parameters are treated as optional
        //Except in the case of primitive parameters
        ProducedType nvt = 
                unit.getNullValueDeclaration().getType();
        if (nvt.isSubtypeOf(argType) && 
                !nvt.isSubtypeOf(paramType)) {
            return false; //only for primitives
        }
        ProducedType defParamType = 
                unit.getDefiniteType(paramType);
        ProducedType defArgType = 
                unit.getDefiniteType(argType);
        ProducedType nt = 
                unit.getNullDeclaration().getType();
        if (defArgType.isSubtypeOf(nt)) {
            return true;
        }
        if (isTypeUnknown(defArgType) || 
                isTypeUnknown(defParamType)) {
            return false;
        }
        if (!erase(defArgType.getDeclaration())
                .inherits(erase(defParamType.getDeclaration())) &&
                notUnderlyingTypesEqual(defParamType, defArgType)) {
            return false;
        }
        return true;
    }

    private static boolean notUnderlyingTypesEqual(ProducedType paramType,
            ProducedType sigType) {
        String sut = sigType.getUnderlyingType();
        String put = paramType.getUnderlyingType();
        return sut==null || put==null || !sut.equals(put);
    }
    
    static boolean betterMatch(Declaration d, Declaration r, 
            List<ProducedType> signature) {
        if (d instanceof Functional && r instanceof Functional) {
            List<ParameterList> dpls = 
                    ((Functional) d).getParameterLists();
            List<ParameterList> rpls = 
                    ((Functional) r).getParameterLists();
            if (dpls!=null && !dpls.isEmpty() && 
                    rpls!=null && !rpls.isEmpty()) {
                ParameterList dpls0 = dpls.get(0);
                ParameterList rpls0 = rpls.get(0);
                List<Parameter> dpl = dpls0.getParameters();
                List<Parameter> rpl = rpls0.getParameters();
                int dplSize = dpl.size();
                int rplSize = rpl.size();
                //ignore sequenced parameters
                boolean dhsp = dpls0.hasSequencedParameter();
                boolean rhsp = rpls0.hasSequencedParameter();
                //always prefer a signature without varargs 
                //over one with a varargs parameter
                if (!dhsp && rhsp) {
                    return true;
                }
                if (dhsp && !rhsp) {
                    return false;
                }
                //ignore sequenced parameters
                if (dhsp) dplSize--;
                if (rhsp) rplSize--;
                if (dplSize==rplSize) {
                    //if all parameters are of more specific
                    //or equal type, prefer it
                    Unit unit = d.getUnit();
                    for (int i=0; i<dplSize; i++) {
                        ProducedType dplt =
                                dpl.get(i).getModel()
                                .getProducedReference(null, 
                                        NO_TYPE_ARGS)
                                .getFullType();
                        ProducedType paramType = 
                                unit.getDefiniteType(dplt);
                        ProducedType rplt = 
                                rpl.get(i).getModel()
                                .getProducedReference(null, 
                                        NO_TYPE_ARGS)
                                .getFullType();
                        ProducedType otherType = 
                                unit.getDefiniteType(rplt);
                        ProducedType argumentType = 
                                signature != null && signature.size() >= i ? 
                                        signature.get(i) : null;
                        if (isTypeUnknown(otherType) || 
                                isTypeUnknown(paramType)) {
                            return false;
                        }
                        TypeDeclaration ptd = 
                                erase(paramType.getDeclaration());
                        TypeDeclaration otd = 
                                erase(otherType.getDeclaration());
                        if(paramType.isExactly(otherType) && 
                                supportsCoercion(ptd) &&
                                // do we have different scores?
                                hasWorseScore(getCoercionScore(argumentType, paramType), 
                                        getCoercionScore(argumentType, otherType))){
                            return false;
                        }
                        if (!ptd.inherits(otd) &&
                                notUnderlyingTypesEqual(paramType, otherType)) {
                            return false;
                        }
                    }
                    // check sequenced parameters last
                    if (dhsp && rhsp){
                        ProducedType dplt = 
                                dpl.get(dplSize).getModel()
                                .getProducedReference(null, 
                                        NO_TYPE_ARGS)
                                .getFullType();
                        ProducedType paramType = unit.getDefiniteType(dplt);
                        ProducedType rplt = 
                                rpl.get(dplSize).getModel()
                                .getProducedReference(null, 
                                        NO_TYPE_ARGS)
                                .getFullType();
                        ProducedType otherType = unit.getDefiniteType(rplt);
                        if (isTypeUnknown(otherType) || 
                                isTypeUnknown(paramType)) {
                            return false;
                        }
                        paramType = unit.getIteratedType(paramType);
                        otherType = unit.getIteratedType(otherType);
                        if (isTypeUnknown(otherType) || 
                                isTypeUnknown(paramType)) {
                            return false;
                        }
                        TypeDeclaration ptd = 
                                erase(paramType.getDeclaration());
                        TypeDeclaration otd = 
                                erase(otherType.getDeclaration());
                        if (paramType.isExactly(otherType) && 
                                supportsCoercion(ptd)) {
                            ProducedType widerArgumentType = 
                                    getWiderArgumentType(paramType, 
                                            signature, dplSize);
                            // do we have different scores?
                            if (hasWorseScore(getCoercionScore(widerArgumentType, paramType), 
                                    getCoercionScore(widerArgumentType, otherType))) {
                                return false;
                            }
                        }
                        if (!ptd.inherits(otd) &&
                                notUnderlyingTypesEqual(paramType, otherType)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean supportsCoercion(TypeDeclaration decl) {
        Unit unit = decl.getUnit();
        return decl.equals(unit.getIntegerDeclaration()) || 
                decl.equals(unit.getFloatDeclaration());
    }

    private static boolean hasWorseScore(int underlyingTypeCoercionScoreA, 
            int underlyingTypeCoercionScoreB) {
        if (underlyingTypeCoercionScoreA != 
                underlyingTypeCoercionScoreB) {
            if (underlyingTypeCoercionScoreA > 0 && 
                    underlyingTypeCoercionScoreB > 0) {
                // both truncations, prefer the smaller truncation
                if (underlyingTypeCoercionScoreA > 
                underlyingTypeCoercionScoreB) {
                    return true;
                }
            }
            else if(underlyingTypeCoercionScoreA > 0) {
                // A is a truncation, B is a widening, prefer widening
                return true;
            }
            else if(underlyingTypeCoercionScoreA == 0) {
                // A is a perfect match, it's not worse
                return false;
            }
            else if(underlyingTypeCoercionScoreB == 0) {
                // B is a perfect match but A is not, so it's worse
                return true;
            }
            else if(underlyingTypeCoercionScoreB > 0) {
                // A is a widening, B is a truncation, so it's not worse
                return false;
            }
            else {
                // A is a widening and B is a widening too, A is worse than B
                // if it widens more than B
                return underlyingTypeCoercionScoreA < 
                        underlyingTypeCoercionScoreB;
            }
        }
        return false;// same score or we don't know
    }

    private static ProducedType getWiderArgumentType(ProducedType paramType, 
            List<ProducedType> signature, int startAt) {
        if (startAt >= signature.size())
            return null;
        TypeDeclaration decl = paramType.getDeclaration();
        Unit unit = decl.getUnit();
        if (decl.equals(unit.getIntegerDeclaration())) {
            int bestScore = 0;
            ProducedType ret = null;
            for(int i=startAt; i<signature.size(); i++){
                ProducedType argType = signature.get(i);
                String underlyingType = 
                        argType.getUnderlyingType();
                int score = 0;
                if (underlyingType == null || 
                        underlyingType.equals("long")) {
                    return argType; // found the wider sort
                }
                else if (underlyingType.equals("int")) {
                    score = 2;
                }
                else if (underlyingType.equals("short")) {
                    score = 1;
                }
                // keep the widest argument type
                if (score > bestScore) {
                    bestScore = score;
                    ret = argType;
                }
            }
            return ret;
        }
        else if (decl.equals(unit.getFloatDeclaration())) {
            int bestScore = 0;
            ProducedType ret = null;
            for (int i=startAt; i<signature.size(); i++) {
                ProducedType argType = signature.get(i);
                String underlyingType = 
                        argType.getUnderlyingType();
                int score = 0;
                if (underlyingType == null || 
                        underlyingType.equals("double")) {
                    return argType; // found the wider sort
                }
                else if (underlyingType.equals("float")) {
                    score = 1;
                }
                // keep the widest argument type
                if (score > bestScore){
                    bestScore = score;
                    ret = argType;
                }
            }
            return ret;
        }
        // not relevant
        return null;
    }

    /**
     * Returns 0 of there's no coercion, > 0 if we have to truncate the argument type to fit the param type,
     * the higher for the worse truncation, or < 0 if we have to widen the argument type to fit the param
     * type, the lower for the worse widening.
     */
    private static int getCoercionScore(ProducedType argumentType, ProducedType paramType) {
        if (argumentType == null) {
            return 0;
        }
        // only consider types of Integer of Float
        if (paramType.isExactly(argumentType)) {
            String aType = argumentType.getUnderlyingType();
            String pType = paramType.getUnderlyingType();
            if (aType == null && pType == null) {
                return 0;
            }
            Unit unit = argumentType.getDeclaration().getUnit();
            TypeDeclaration decl = paramType.getDeclaration();
            if (decl.equals(unit.getIntegerDeclaration())) {
                if (aType == null) {
                    aType = "long";
                }
                if (pType == null) {
                    pType = "long";
                }
                int aScore = getPrimitiveScore(aType);
                int bScore = getPrimitiveScore(pType);
                /*
                 * aType aTypeScore pType pTypeScore score
                 * short 0          short 0          0
                 * short 0          int   1          -1 (widening)
                 * short 0          long  2          -2 (widening)
                 * int   1          short 0          1 (truncation)
                 * int   1          int   1          0
                 * int   1          long  2          -1 (widening)
                 * long  2          short 0          2 (truncation)
                 * long  2          int   1          1 (truncation)
                 * long  2          long  2          0
                 */
                return aScore - bScore;
            }
            else if (decl.equals(unit.getFloatDeclaration())) {
                if (aType == null) {
                    aType = "double";
                }
                if (pType == null) {
                    pType = "double";
                }
                int aScore = getPrimitiveScore(aType);
                int bScore = getPrimitiveScore(pType);
                /*
                 * aType  aTypeScore pType  pTypeScore score
                 * float  0          float  0          0
                 * float  0          double 1          -1 (widening)
                 * double 1          float  0          1 (truncation)
                 * double 1          double 1          0
                 */
                return aScore - bScore;
            }
        }
        // no truncation for the rest
        return 0;
    }

    private static int getPrimitiveScore(String underlyingType) {
        if (underlyingType.equals("long")) {
            return 2;
        }
        if (underlyingType.equals("int") || 
                underlyingType.equals("double")) {
            return 1;
        }
        if (underlyingType.equals("short") || 
                underlyingType.equals("float")) {
            return 0;
        }
        return 0;
    }

    static boolean strictlyBetterMatch(Declaration d, Declaration r) {
        if (d instanceof Functional && r instanceof Functional) {
            List<ParameterList> dpls = 
                    ((Functional) d).getParameterLists();
            List<ParameterList> rpls = 
                    ((Functional) r).getParameterLists();
            if (dpls!=null && !dpls.isEmpty() && 
                    rpls!=null && !rpls.isEmpty()) {
                ParameterList dpls0 = dpls.get(0);
                ParameterList rpls0 = rpls.get(0);
                List<Parameter> dpl = dpls0.getParameters();
                List<Parameter> rpl = rpls0.getParameters();
                int dplSize = dpl.size();
                int rplSize = rpl.size();
                //ignore sequenced parameters
                boolean dhsp = dpls0.hasSequencedParameter();
                boolean rhsp = rpls0.hasSequencedParameter();
                //always prefer a signature without varargs 
                //over one with a varargs parameter
                if (!dhsp && rhsp) {
                    return true;
                }
                if (dhsp && !rhsp) {
                    return false;
                }
                //ignore sequenced parameters
                if (dhsp) dplSize--;
                if (rhsp) rplSize--;
                if (dplSize==rplSize) {
                    //if all parameters are of more specific
                    //or equal type, prefer it
                    boolean atLeastOneBetter = false;
                    Unit unit = d.getUnit();
                    for (int i=0; i<dplSize; i++) {
                        ProducedType dplt = 
                                dpl.get(i).getModel()
                                .getProducedReference(null, 
                                        NO_TYPE_ARGS)
                                .getFullType();
                        ProducedType paramType = 
                                unit.getDefiniteType(dplt);
                        ProducedType rplt = 
                                rpl.get(i).getModel()
                                .getProducedReference(null, 
                                        NO_TYPE_ARGS)
                                .getFullType();
                        ProducedType otherType = 
                                unit.getDefiniteType(rplt);
                        if (isTypeUnknown(otherType) || 
                                isTypeUnknown(paramType)) {
                            return false;
                        }
                        TypeDeclaration ptd = 
                                erase(paramType.getDeclaration());
                        TypeDeclaration otd = 
                                erase(otherType.getDeclaration());
                        if (!ptd.inherits(otd) &&
                                notUnderlyingTypesEqual(paramType, otherType)) {
                            return false;
                        }
                        if (ptd.inherits(otd) && !otd.inherits(ptd) &&
                                notUnderlyingTypesEqual(paramType, otherType)) {
                            atLeastOneBetter = true;
                        }
                        
                    }
                    // check sequenced parameters last
                    if (dhsp && rhsp) {
                        ProducedType dplt = 
                                dpl.get(dplSize).getModel()
                                .getProducedReference(null, 
                                        NO_TYPE_ARGS)
                                .getFullType();
                        ProducedType paramType = 
                                unit.getDefiniteType(dplt);
                        ProducedType rplt = 
                                rpl.get(dplSize).getModel()
                                .getProducedReference(null, 
                                        NO_TYPE_ARGS)
                                .getFullType();
                        ProducedType otherType = 
                                unit.getDefiniteType(rplt);
                        if (isTypeUnknown(otherType) || 
                                isTypeUnknown(paramType)) {
                            return false;
                        }
                        paramType = unit.getIteratedType(paramType);
                        otherType = unit.getIteratedType(otherType);
                        if (isTypeUnknown(otherType) || 
                                isTypeUnknown(paramType)) {
                            return false;
                        }
                        TypeDeclaration ptd = 
                                erase(paramType.getDeclaration());
                        TypeDeclaration otd = 
                                erase(otherType.getDeclaration());
                        if (!ptd.inherits(otd) &&
                                notUnderlyingTypesEqual(paramType, otherType)) {
                            return false;
                        }
                        if (ptd.inherits(otd) && !otd.inherits(ptd) &&
                                notUnderlyingTypesEqual(paramType, otherType)) {
                            atLeastOneBetter = true;
                        }
                    }
                    return atLeastOneBetter;
                }
            }
        }
        return false;
    }

    public static boolean isNamed(String name, Declaration d) {
        String dname = d.getName();
        return dname!=null && dname.equals(name);
    }
    
    static TypeDeclaration erase(TypeDeclaration paramType) {
        if (paramType instanceof TypeParameter) {
            if (paramType.getSatisfiedTypes().isEmpty()) {
                return paramType.getExtendedTypeDeclaration();
            }
            else {
                //TODO: is this actually correct? What is Java's
                //      rule here?
                return paramType.getSatisfiedTypeDeclarations().get(0);
            }
        }
        else if (paramType instanceof UnionType) {
            //TODO: this is very sucky, cos in theory a
            //      union might be assignable to the 
            //      parameter type with a typecast
            return paramType.getUnit().getObjectDeclaration();
        }
        else if (paramType instanceof IntersectionType) {
            Unit unit = paramType.getUnit();
            List<TypeDeclaration> sts = 
                    paramType.getSatisfiedTypeDeclarations();
            if (sts.size()==2) {
                //attempt to eliminate Basic from the 
                //intersection - very useful for anonymous
                //classes, whose denotableType is often an 
                //intersection with Basic
                TypeDeclaration first = sts.get(0);
                TypeDeclaration second = sts.get(1);
                if (first!=null && 
                        first.equals(unit.getBasicDeclaration())) {
                    return erase(second);
                }
                else if (second!=null && 
                        second.equals(unit.getBasicDeclaration())) {
                    return erase(first);
                }
            }
            //TODO: this is very sucky, cos in theory an
            //      intersection might be assignable to the 
            //      parameter type with a typecast
            return paramType.getUnit().getObjectDeclaration();
        }
        else {
            return paramType;
        }
    }
    
    /**
     * Match the name of the given declaration to the given
     * pattern. A name matches if:
     * 
     * - it starts with the pattern, ignoring case, or
     * - the pattern consists of all uppercase after the
     *   first character, and its uppercase "humps" match 
     *   the pattern.
     */
    public static boolean isNameMatching(String startingWith, Declaration d) {
        return isNameMatching(startingWith, d.getName());
    }
    
    public static boolean isNameMatching(String startingWith, Import i) {
        return isNameMatching(startingWith, i.getAlias());
    }
    
    public static boolean isNameMatching(String startingWith, String name) {
        if (startingWith==null || startingWith.isEmpty()) {
            return true;
        }
        if (name==null || name.isEmpty()) {
            return false;
        }
        int nameLength = name.length();
        int startingWithLength = startingWith.length();
        if (nameLength<startingWithLength) {
            return false;
        }
        if (name.regionMatches(true,0,startingWith,0,startingWithLength)) {
            return true;
        }
        int c = startingWith.codePointAt(0); 
        int d = name.codePointAt(0);
        if (c!=d) {
            return false;
        }
        //camel hump matching, starting from second character:
        int i=1, j=1;
        while (i<startingWithLength) {
            if (j>=nameLength) {
                return false;
            }
            while (i<startingWithLength && 
                    isLowerCase(c=startingWith.codePointAt(i))) {
                d = name.codePointAt(j);
                if (c==d) {
                    i+=charCount(c);
                    j+=charCount(d); 
                    if (i>=startingWithLength) {
                        return true;
                    }
                    if (j>=nameLength) {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            while (j<nameLength && 
                    isLowerCase(d=name.codePointAt(j))) {
                j+=charCount(d);
                if (j>=nameLength) {
                    return false;
                }
            }
            c = startingWith.codePointAt(i);
            d = name.codePointAt(j);
            i+=charCount(c);
            j+=charCount(d); 
            if (d!=c) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Given a declaration, a list of type arguments to the 
     * declaration, and a receiving type, collect together
     * all interesting type arguments. The resulting map 
     * includes all type arguments from the receiving type 
     * and all its qualifying types. That's useful, because
     * {@link ProducedType.Substitution} works with a single
     * aggregated map, and so for performance
     * {@link ProducedType#substitute(ProducedType)} and
     * {@link ProducedType#substitute(ProducedTypedReference)}
     * assume that the given type or reference holds such a
     * single aggregated map.
     * 
     * @return a map of type parameter to type argument
     *  
     * @param declaration a declaration
     * @param receivingType the receiving produced type 
     *        of which the declaration is a member
     * @param typeArguments all the explicit or inferred 
     *        type arguments of the declaration, including
     *        those from qualifying types
     */
    public static Map<TypeParameter,ProducedType> 
    getTypeArgumentMap(Declaration declaration, 
            ProducedType receivingType, 
            List<ProducedType> typeArguments) {        
    	List<TypeParameter> typeParameters = 
    	        getTypeParameters(declaration);
        int count = countTypeParameters(receivingType, 
                typeParameters);
		if (count==0) {
		    return EMPTY_TYPE_ARG_MAP;
		}
		else {
			return aggregateTypeArguments(receivingType, 
			        typeArguments, typeParameters, count);
		}
    }

	private static Map<TypeParameter, ProducedType> 
	aggregateTypeArguments(ProducedType receivingType, 
	        List<ProducedType> typeArguments,
	        List<TypeParameter> typeParameters, 
	        int count) {
	    Map<TypeParameter,ProducedType> map = 
	            new HashMap<TypeParameter,ProducedType>
	                (count);
        //make sure we collect all type arguments
        //from the whole qualified type!
	    if (receivingType!=null) {
	        TypeDeclaration rtd = 
	                receivingType.getDeclaration();
            if (rtd instanceof IntersectionType) {
	            for (ProducedType dt: 
	                    rtd.getSatisfiedTypes()) {
	                while (dt!=null) {
	                    map.putAll(dt.getTypeArguments());
	                    dt = dt.getQualifyingType();
	                }
	            }
	        }
	        else {
	            ProducedType dt = receivingType;
	            while (dt!=null) {
	                map.putAll(dt.getTypeArguments());
	                dt = dt.getQualifyingType();
	            }
	        }
	    }
	    //now turn the type argument tuple into a
	    //map from type parameter to argument
	    for (int i=0; 
	            i<typeParameters.size() && 
	            i<typeArguments.size(); 
	            i++) {
	    	map.put(typeParameters.get(i), 
	    	        typeArguments.get(i));
	    }
	    return map;
    }

    public static Map<TypeParameter,SiteVariance> 
    getVarianceMap(Declaration declaration, 
            ProducedType receivingType, 
            List<SiteVariance> variances) {     
        if (variances==null) {
            return EMPTY_VARIANCE_MAP;
        }
        else {
            List<TypeParameter> typeParameters = 
                    getTypeParameters(declaration);
            int count = countTypeParameters(receivingType, 
                    typeParameters);
            if (count==0) {
                return EMPTY_VARIANCE_MAP;
            }
            else {
                return aggregateVariances(receivingType, 
                        variances, typeParameters);
            }
        }
    }

    private static Map<TypeParameter, SiteVariance> 
    aggregateVariances(ProducedType receivingType, 
            List<SiteVariance> variances,
            List<TypeParameter> typeParameters) {
        Map<TypeParameter,SiteVariance> map = 
                new HashMap<TypeParameter,SiteVariance>();
        //make sure we collect all type arguments
        //from the whole qualified type!
        if (receivingType!=null) {
            TypeDeclaration rtd = 
                    receivingType.getDeclaration();
            if (rtd instanceof IntersectionType) {
                for (ProducedType dt: 
                        rtd.getSatisfiedTypes()) {
                    while (dt!=null) {
                        map.putAll(dt.getVarianceOverrides());
                        dt = dt.getQualifyingType();
                    }
                }
            }
            else {
                ProducedType dt = receivingType;
                while (dt!=null) {
                    map.putAll(dt.getVarianceOverrides());
                    dt = dt.getQualifyingType();
                }
            }
        }
        for (int i=0; 
                i<typeParameters.size() && 
                i<variances.size(); 
                i++) {
            SiteVariance var = variances.get(i);
            if (var!=null) {
                map.put(typeParameters.get(i), var);
            }
        }
        return map;
    }

	private static int countTypeParameters(
	        ProducedType receivingType,
            List<TypeParameter> typeParameters) {
        int count = typeParameters.size();
        //make sure we count all type arguments
        //from the whole qualified type!
        if (receivingType!=null) {
            TypeDeclaration rtd = 
                    receivingType.getDeclaration();
            if (rtd instanceof IntersectionType) {
                for (ProducedType dt: 
                        rtd.getSatisfiedTypes()) {
                    while (dt!=null) {
                        count += dt.getTypeArguments().size();
                        dt = dt.getQualifyingType();
                    }
                }
            }
            else {
                ProducedType dt = receivingType;
                while (dt!=null) {
                    count += dt.getTypeArguments().size();
                    dt = dt.getQualifyingType();
                }
            }
        }
	    return count;
    }

	private static List<TypeParameter> getTypeParameters(
	        Declaration declaration) {
    	if (declaration instanceof Generic) {
            Generic g = (Generic) declaration;
            return g.getTypeParameters();
        }
        else {
        	return Collections.emptyList();
        }
    }
	
    static <T> List<T> list(List<T> list, T element) {
        List<T> result = new ArrayList<T>(list.size()+1);
        result.addAll(list);
        result.add(element);
        return result;
    }

    /**
     * Helper method for eliminating duplicate types from
     * lists of types that form a union type, taking into
     * account that a subtype is a "duplicate" of its
     * supertype.
     */
    public static void addToUnion(List<ProducedType> list, 
            ProducedType pt) {
        if (pt==null || !list.isEmpty() && pt.isNothing()) {
            return;
        }
        if (pt.getDeclaration() instanceof UnionType) {
            // cheaper c-for than foreach
            List<ProducedType> caseTypes = 
                    pt.getDeclaration()
                        .getCaseTypes();
            for ( int i=0,l=caseTypes.size();i<l;i++ ) {
                ProducedType t = caseTypes.get(i);
                addToUnion(list, t.substitute(pt));
            }
        }
        else if (pt.isWellDefined()) {
            boolean add=true;
            // cheaper c-for than foreach
            for (int i=0;i<list.size();i++) {
                ProducedType t = list.get(i);
                if (pt.isSubtypeOf(t)) {
                    add=false;
                    break;
                }
                else if (pt.isSupertypeOf(t)) {
                    list.remove(i);
                    i--; // redo this index
                }
            }
            if (add) {
                list.add(pt);
            }
        }
    }
    
    public static void addToIntersection(List<ProducedType> list, 
            ProducedType pt, Unit unit) {
        addToIntersection(list, pt, unit, true);
    }
    
    /**
     * Helper method for eliminating duplicate types from
     * lists of types that form an intersection type, taking 
     * into account that a supertype is a "duplicate" of its
     * subtype.
     */
    public static void addToIntersection(List<ProducedType> list, 
            ProducedType pt, Unit unit, 
            boolean reduceDisjointTypes) {
        if (pt==null || !list.isEmpty() && pt.isAnything()) {
            return;
        }
        TypeDeclaration ptd = pt.getDeclaration();
        if (ptd instanceof IntersectionType) {
            List<ProducedType> satisfiedTypes = 
                    ptd.getSatisfiedTypes();
            // cheaper c-for than foreach
            for (int i=0,l=satisfiedTypes.size(); i<l; i++) {
                ProducedType t = satisfiedTypes.get(i);
                addToIntersection(list, 
                        t.substitute(pt), 
                        unit, reduceDisjointTypes);
            }
        }
        else {
            //implement the rule that Foo&Bar==Nothing if 
            //there exists some enumerated type Baz with
            //    Baz of Foo | Bar 
            //(the intersection of disjoint types is empty)
            ProducedType nt = 
                    nothingType(unit);
            if (!list.isEmpty() && reduceDisjointTypes) {
                List<TypeDeclaration> supertypes = 
                        ptd.getSupertypeDeclarations();
                for (int i=0, l=supertypes.size(); i<l; i++) {
                    TypeDeclaration supertype = 
                            supertypes.get(i);
                    List<TypeDeclaration> ctds =
                            supertype.getCaseTypeDeclarations();
                    if (ctds!=null) {
                        TypeDeclaration ctd=null;
                        for (int cti=0, ctl=ctds.size(); 
                                cti<ctl; 
                                cti++) {
                            TypeDeclaration ct = 
                                    ctds.get(cti);
                            if (ptd.inherits(ct)) {
                                ctd = ct;
                                break;
                            }
                        }
                        if (ctd!=null) {
                            for (int cti=0, ctl=ctds.size(); 
                                    cti<ctl; 
                                    cti++) {
                                TypeDeclaration ct = 
                                        ctds.get(cti);
                                if (ct!=ctd) {
                                    for (int ti=0, tl=list.size(); 
                                            ti<tl; 
                                            ti++) {
                                        ProducedType t = 
                                                list.get(ti);
                                        if (t.getDeclaration()
                                                .inherits(ct)) {
                                            list.clear();
                                            list.add(nt);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            Boolean add = pt.isWellDefined();
            if (add) {
                for (int i=0; i<list.size(); i++) {
                    ProducedType t = list.get(i);
                    if (pt.isSupertypeOf(t)) {
                        add = false;
                        break;
                    }
                    else if (pt.isSubtypeOf(t)) {
                        list.remove(i);
                        i--; // redo this index
                    }
                    else if (haveUninhabitableIntersection(pt, t, unit)) {
                        list.clear();
                        list.add(nt);
                        return;
                    } 
                    else {
                        TypeDeclaration td = t.getDeclaration();
                        if (ptd instanceof ClassOrInterface && 
                                td instanceof ClassOrInterface && 
                                ptd.equals(td) &&
                                !pt.containsUnknowns() &&
                                !t.containsUnknowns()) {
                            //canonicalize T<InX,OutX>&T<InY,OutY> to T<InX|InY,OutX&OutY>
                            ProducedType pi = 
                                    principalInstantiation(ptd, 
                                            pt, t, unit);
                            if (!pi.containsUnknowns()) {
                                list.remove(i);
                                list.add(pi);
                                return;
                            }
                        }
                    }
                }
            }
            if (add && list.size()>1) {
                //it is possible to have a type that is a
                //supertype of the intersection, even though
                //it is not a supertype of any of the 
                //intersected types!
                IntersectionType it = 
                        new IntersectionType(unit);
                it.setSatisfiedTypes(list);
                ProducedType type = 
                        it.canonicalize().getType();
                if (pt.isSupertypeOf(type)) {
                    add = false;
                }
            }
            if (add) {
                list.add(pt);
            }
        }
    }

    /**
     * The meet of two classes unrelated by inheritance,
     * or of Null with an interface type is empty. The meet
     * of an anonymous class with a type to which it is not
     * assignable is empty.
     */
    private static boolean emptyMeet(ProducedType p, ProducedType q, 
            Unit unit) {
        if (p==null || q==null) {
            return false;
        }
        if (p.isNothing() || q.isNothing()) {
            return true;
        }
        TypeDeclaration nd = unit.getNullDeclaration(); //TODO what about the anonymous type of null?
        TypeDeclaration pd = p.getDeclaration();
        TypeDeclaration qd = q.getDeclaration();
        if (pd instanceof TypeParameter) {
            IntersectionType it = 
                    new IntersectionType(unit);
            it.setSatisfiedTypes(pd.getSatisfiedTypes());
            p = it.canonicalize().getType();
            pd = p.getDeclaration();
        }
        if (qd instanceof TypeParameter) {
            IntersectionType it = 
                    new IntersectionType(unit);
            it.setSatisfiedTypes(qd.getSatisfiedTypes());
            q = it.canonicalize().getType();
            qd = q.getDeclaration();
        }
        if (qd instanceof IntersectionType) {
            for (ProducedType t: qd.getSatisfiedTypes()) {
                if (emptyMeet(p,t,unit)) {
                    return true;
                }
            }
            return false;
        }
        if (pd instanceof IntersectionType) {
            for (ProducedType t: pd.getSatisfiedTypes()) {
                if (emptyMeet(q,t,unit)) {
                    return true;
                }
            }
            return false;
        }
        if (qd instanceof UnionType) {
            for (ProducedType t: qd.getCaseTypes()) {
                if (!emptyMeet(p,t,unit)) {
                    return false;
                }
            }
            return true;
        }
        else if (qd.getCaseTypes()!=null) {
            boolean all = true;
            for (ProducedType t: 
                    //TODO: shouldn't this be q.getCaseTypes() ?
                    //      but that causes nontermination...
                    qd.getCaseTypes()) {
                if (t.getDeclaration().isSelfType() || 
                        !emptyMeet(p,t,unit)) {
                    all = false; 
                    break;
                }
            }
            if (all) return true;
        }
        if (pd instanceof UnionType) {
            for (ProducedType t: pd.getCaseTypes()) {
                if (!emptyMeet(q,t,unit)) {
                    return false;
                }
            }
            return true;
        }
        else if (pd.getCaseTypes()!=null) {
            boolean all = true;
            for (ProducedType t: 
                    //TODO: shouldn't this be p.getCaseTypes() ?
                    //      but that causes nontermination...
                    pd.getCaseTypes()) {
                if (t.getDeclaration().isSelfType() || 
                        !emptyMeet(q,t,unit)) {
                    all = false; 
                    break;
                }
            }
            if (all) return true;
        }
        if (pd instanceof Class && qd instanceof Class ||
            pd instanceof Interface && qd instanceof Class &&
                    qd.equals(nd) ||
            qd instanceof Interface && pd instanceof Class &&
                    pd.equals(nd)) {
            if (!qd.inherits(pd) &&
                !pd.inherits(qd)) {
                return true;
            }
        }
        if (pd.isFinal()) {
            if (pd.getTypeParameters().isEmpty() &&
                    !q.involvesTypeParameters() &&
                    !p.isSubtypeOf(q) &&
                    !(qd instanceof UnknownType)) {
                return true;
            }
            if (qd instanceof ClassOrInterface &&
                    !pd.inherits(qd)) {
                return true;
            }
        }
        if (qd.isFinal()) { 
            if (qd.getTypeParameters().isEmpty() &&
                    !p.involvesTypeParameters() &&
                    !q.isSubtypeOf(p) &&
                    !(pd instanceof UnknownType)) {
                return true;
            }
            if (pd instanceof ClassOrInterface &&
                    !qd.inherits(pd)) {
                return true;
            }
        }
//        Interface ed = unit.getEmptyDeclaration();
//        Interface id = unit.getIterableDeclaration();
//        if (pd.inherits(ed) && qd.inherits(id) &&
//                unit.isNonemptyIterableType(q) ||
//            pd.inherits(id) && qd.inherits(ed) &&
//                unit.isNonemptyIterableType(p)) {
//            return true;
//        }
        Interface st = unit.getSequentialDeclaration();
        if (qd instanceof ClassOrInterface &&
                pd.inherits(st) && !qd.inherits(st) && 
                !st.inherits(qd) ||
            pd instanceof ClassOrInterface &&
                qd.inherits(st) && !pd.inherits(st) && 
                !st.inherits(pd) && 
                !p.involvesTypeParameters()) {
            return true;
        }
        
        Interface nst = unit.getSequenceDeclaration();
        if (pd.inherits(nst) && qd.inherits(st) ||
            qd.inherits(nst) && pd.inherits(st)) {
            ProducedType pet = 
                    unit.getSequentialElementType(p);
            ProducedType qet = 
                    unit.getSequentialElementType(q);
            if (emptyMeet(pet, qet, unit)) {
                return true;
            }
        }
        Class td = unit.getTupleDeclaration();
        if (pd.inherits(td) && qd.inherits(td)) {
            List<ProducedType> pal = p.getTypeArgumentList();
            List<ProducedType> qal = q.getTypeArgumentList();
            if (pal.size()>=3 && qal.size()>=3) {
                if (emptyMeet(pal.get(1), qal.get(1), unit) ||
                    emptyMeet(pal.get(2), qal.get(2), unit)) {
                    return true;
                }
            }
        }
        if (pd.inherits(td) && qd.inherits(st)) {
            List<ProducedType> pal = 
                    p.getTypeArgumentList();
            ProducedType qet = 
                    unit.getSequentialElementType(q);
            if (pal.size()>=3) {
                if (emptyMeet(pal.get(1), qet, unit) ||
                    emptyMeet(pal.get(2), 
                            unit.getSequentialType(qet), 
                            unit)) {
                    return true;
                }
            }
        }
        if (qd.inherits(td) && pd.inherits(st)) {
            List<ProducedType> qal = 
                    q.getTypeArgumentList();
            ProducedType pet = 
                    unit.getSequentialElementType(p);
            if (qal.size()>=3) {
                if (emptyMeet(qal.get(1), pet, unit) ||
                    emptyMeet(qal.get(2), 
                            unit.getSequentialType(pet),
                            unit)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Given two instantiations of a qualified type constructor, 
     * determine the qualifying type of the principal 
     * instantiation of that type constructor for the 
     * intersection of the two types.
     */
    static ProducedType principalQualifyingType(ProducedType pt,
            ProducedType t, TypeDeclaration td, Unit unit) {
        ProducedType ptqt = pt.getQualifyingType();
        ProducedType tqt = t.getQualifyingType();
        if (ptqt!=null && tqt!=null && 
                td.getContainer() instanceof TypeDeclaration) {
            TypeDeclaration qtd = 
                    (TypeDeclaration) td.getContainer();
            ProducedType pst = ptqt.getSupertype(qtd);
            ProducedType st = tqt.getSupertype(qtd);
            if (pst!=null && st!=null) {
                return principalInstantiation(qtd, pst, st, unit);
            }
        }
        return null;
    }
    
    /**
     * Determine if a type of form X<P>&X<Q> is equivalent to
     * Nothing where X<T> is invariant in T.
     * @param p the argument type P
     * @param q the argument type Q
     */
    private static boolean haveUninhabitableIntersection
            (ProducedType p, ProducedType q, Unit unit) {
        return emptyMeet(p, q, unit) ||
                hasEmptyIntersectionOfInvariantInstantiations(p, q);
    }

    private static boolean hasEmptyIntersectionOfInvariantInstantiations(
            ProducedType p, ProducedType q) {
//        if (!p.containsTypeParameters() && !q.containsTypeParameters()) {
            List<TypeDeclaration> stds = 
                    p.getDeclaration().getSupertypeDeclarations();
            stds.retainAll(q.getDeclaration().getSupertypeDeclarations());
            for (TypeDeclaration std: stds) {
                ProducedType pst = null;
                ProducedType qst = null;
                for (TypeParameter tp: std.getTypeParameters()) {
                    if (tp.isInvariant()) {
                        if (pst==null) pst = p.getSupertype(std);
                        if (qst==null) qst = q.getSupertype(std);
                        if (pst!=null && qst!=null) {
                            ProducedType psta = pst.getTypeArguments().get(tp);
                            ProducedType qsta = qst.getTypeArguments().get(tp);
                            //TODO: why isWellDefined() instead of isTypeUnknown() ?
                            if (psta!=null && psta.isWellDefined() && 
                                    !pst.involvesTypeParameters() && 
                                qsta!=null && qsta.isWellDefined() && 
                                    !qst.involvesTypeParameters()) {
                                boolean psti = pst.isInvariant(tp);
                                boolean pstcov = pst.isCovariant(tp);
                                boolean pstcontra = pst.isContravariant(tp);
                                boolean qsti = qst.isInvariant(tp);
                                boolean qstcov = qst.isCovariant(tp);
                                boolean qstcontra = qst.isContravariant(tp);
                                if (psti && qsti && !psta.isExactly(qsta) ||
                                    pstcov && qsti && !qsta.isSubtypeOf(psta) ||
                                    qstcov && psti && !psta.isSubtypeOf(qsta) ||
                                    pstcontra && qsti && !psta.isSubtypeOf(qsta) ||
                                    qstcontra && psti && !qsta.isSubtypeOf(psta)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
//        }
        return false;
    }
    
    public static String formatPath(List<String> path, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<path.size(); i++) {
            String pathPart = path.get(i);
            if (! pathPart.isEmpty()) {
                sb.append(pathPart);
                if (i<path.size()-1) sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String formatPath(List<String> path) {
        return formatPath(path, '.');
    }
    
    public static ProducedType unionType(
            ProducedType lhst, ProducedType rhst, 
            Unit unit) {
        List<ProducedType> list = 
                new ArrayList<ProducedType>(2);
        addToUnion(list, rhst);
        addToUnion(list, lhst);
        UnionType ut = new UnionType(unit);
        ut.setCaseTypes(list);
        return ut.getType();
    }

    public static ProducedType intersectionType(
            ProducedType lhst, ProducedType rhst, 
            Unit unit) {
        ProducedType simpleIntersection = 
                getSimpleIntersection(lhst, rhst);
        if (simpleIntersection != null) {
            return simpleIntersection;
        }
        List<ProducedType> list = 
                new ArrayList<ProducedType>(2);
        addToIntersection(list, rhst, unit);
        addToIntersection(list, lhst, unit);
        IntersectionType it = new IntersectionType(unit);
        it.setSatisfiedTypes(list);
        return it.canonicalize().getType();
    }

    private static ProducedType getSimpleIntersection(
            ProducedType a, ProducedType b) {
        if (a == null || b == null) {
            return null;
        }
        TypeDeclaration aDecl = a.getDeclaration();
        TypeDeclaration bDecl = b.getDeclaration();
        if(aDecl == null || bDecl == null) {
            return null;
        }
        if (!(aDecl instanceof ClassOrInterface)) {
            if (aDecl instanceof UnionType && 
                    bDecl instanceof ClassOrInterface) {
                return getSimpleIntersection(
                        b, (ClassOrInterface) bDecl, 
                        a, (UnionType)aDecl);
            }
            return null;
        }
        if (!(bDecl instanceof ClassOrInterface)) {
            // here aDecl MUST BE a ClassOrInterface as per flow
            if (bDecl instanceof UnionType){
                return getSimpleIntersection(
                        a, (ClassOrInterface) aDecl, 
                        b, (UnionType) bDecl);
            }
            return null;
        }
        String aName = aDecl.getQualifiedNameString();
        String bName = bDecl.getQualifiedNameString();
        if (aName.equals(bName)
                && aDecl.getTypeParameters().isEmpty()
                && bDecl.getTypeParameters().isEmpty())
            return a;
        if (aName.equals("ceylon.language::Anything")) {
            // everything is an Anything
            return b;
        }
        if (bName.equals("ceylon.language::Anything")) {
            // everything is an Anything
            return a;
        }
        if (aName.equals("ceylon.language::Object")) {
            // every ClassOrInterface is an object except Null
            if (bName.equals("ceylon.language::Null")
                    || bName.equals("ceylon.language::null")) {
                return new NothingType(aDecl.getUnit()).getType();
            }
            return b;
        }
        if (bName.equals("ceylon.language::Object")){
            // every ClassOrInterface is an object except Null
            if(aName.equals("ceylon.language::Null")
                    || aName.equals("ceylon.language::null")) {
                return new NothingType(aDecl.getUnit()).getType();
            }
            return a;
        }
        if (aName.equals("ceylon.language::Null")){
            // only null is null
            if(bName.equals("ceylon.language::Null")
                    || bName.equals("ceylon.language::null")) {
                return b;
            }
            return new NothingType(aDecl.getUnit()).getType();
        }
        if (bName.equals("ceylon.language::Null")){
            // only null is null
            if(aName.equals("ceylon.language::Null")
                    || aName.equals("ceylon.language::null")) {
                return a;
            }
            return new NothingType(aDecl.getUnit()).getType();
        }
        // not simple
        return null;
    }

    private static ProducedType getSimpleIntersection(ProducedType a, 
            ClassOrInterface aDecl, ProducedType b, UnionType bDecl) {
        // we only handle Foo|Null
        if (bDecl.getCaseTypes().size() != 2) {
            return null;
        }

        String aName = aDecl.getQualifiedNameString();
        // we only handle Object and Null intersections
        if (!aName.equals("ceylon.language::Object")
                && !aName.equals("ceylon.language::Null")) {
            return null;
        }
        
        ProducedType caseA = bDecl.getCaseTypes().get(0);
        TypeDeclaration caseADecl = caseA.getDeclaration();

        ProducedType caseB = bDecl.getCaseTypes().get(1);
        TypeDeclaration caseBDecl = caseB.getDeclaration();

        boolean isANull = 
                caseADecl instanceof ClassOrInterface && 
                "ceylon.language::Null"
                    .equals(caseADecl.getQualifiedNameString());
        boolean isBNull = 
                caseBDecl instanceof ClassOrInterface && 
                "ceylon.language::Null"
                    .equals(caseBDecl.getQualifiedNameString());
        
        if (aName.equals("ceylon.language::Object")) {
            if (isANull) {
                return simpleObjectIntersection(aDecl, caseB);
            }
            if (isBNull) {
                return simpleObjectIntersection(aDecl, caseA);
            }
            // too complex
            return null;
        }
        if (aName.equals("ceylon.language::Null")) {
            if (isANull) {
                return caseA;
            }
            if (isBNull) {
                return caseB;
            }
            // too complex
            return null;
        }
        // too complex
        return null;
    }

    private static ProducedType simpleObjectIntersection(
            ClassOrInterface objectDecl, ProducedType type) {
        TypeDeclaration declaration = 
                type.getDeclaration();
        if (declaration instanceof ClassOrInterface)
            return type;
        if (declaration instanceof TypeParameter) {
            List<ProducedType> satisfiedTypes = 
                    declaration.getSatisfiedTypes();
            if (satisfiedTypes.isEmpty()) {
                // trivial intersection TP&Object
                IntersectionType it = 
                        new IntersectionType(objectDecl.getUnit());
                it.getSatisfiedTypes().add(type);
                it.getSatisfiedTypes().add(objectDecl.getType());
                return it.canonicalize().getType();
            }
            for(ProducedType sat : satisfiedTypes){
                if (sat.getDeclaration() instanceof ClassOrInterface && 
                        sat.getDeclaration().getQualifiedNameString()
                            .equals("ceylon.language::Object")) {
                    // it is already an Object
                    return type;
                }
            }
            // too complex
            return null;
        }
        // too complex
        return null;
    }

    public static boolean isElementOfUnion(UnionType ut, ClassOrInterface ci) {
        for (TypeDeclaration ct: ut.getCaseTypeDeclarations()) {
            if (ct instanceof ClassOrInterface && ct.equals(ci)) {
                return true;
            }
        }
        return false;
    }
    
    public static Declaration lookupMember(List<Declaration> members, String name,
            List<ProducedType> signature, boolean ellipsis) {
        List<Declaration> results = null;
        Declaration result = null;
        Declaration inexactMatch = null;
        for (int i = 0, l = members.size(); i < l ; i++) {
            Declaration d = members.get(i);
            if (isResolvable(d) && isNamed(name, d)) {
                if (signature==null) {
                    //no argument types: either a type 
                    //declaration, an attribute, or a method 
                    //reference - don't return overloaded
                    //forms of the declaration (instead
                    //return the "abstraction" of them)
                    if (notOverloaded(d)) {
                        return d;
                    }
                }
                else {
                    if (notOverloaded(d)) {
                        //we have found either a non-overloaded
                        //declaration, or the "abstraction" 
                        //which of all the overloaded forms 
                        //of the declaration
                        //Note: I could not do this optimization
                        //      because then it could not distinguish
                        //      between Java open() and isOpen()
                        /*if (!isAbstraction(d)) {
                            return d;
                        }*/
                        inexactMatch = d;
                    }
                    if (hasMatchingSignature(signature, ellipsis, d)) {
                        //we have found an exactly matching 
                        //overloaded declaration
                        if (result == null) {
                            result = d; // first match
                        }
                        else {
                            // more than one match, move to array
                            if (results == null) {
                                results = new ArrayList<Declaration>(2);
                                results.add(result);
                            }
                            addIfBetterMatch(results, d, signature);
                        }
                    }
                }
            }
        }
        // if we never needed a results array
        if (results == null) {
            // single result
            if (result != null) {
                return result;
            }
            // no exact match
            return inexactMatch;
        }
        switch (results.size()) {
        case 0:
            //no exact match, so return the non-overloaded
            //declaration or the "abstraction" of the 
            //overloaded declaration
            return inexactMatch;
        case 1:
            //exactly one exact match, so return it
            return results.get(0);
        default:
            //more than one matching overloaded declaration,
            //so return the "abstraction" of the overloaded
            //declaration
            return inexactMatch;
        }
    }

    private static void addIfBetterMatch(List<Declaration> results, 
            Declaration d, List<ProducedType> signature) {
        boolean add=true;
        for (Iterator<Declaration> i = results.iterator(); 
                i.hasNext();) {
            Declaration o = i.next();
            if (betterMatch(d, o, signature)) {
                i.remove();
            }
            else if (betterMatch(o, d, signature)) { //TODO: note asymmetry here resulting in nondeterminate behavior!
                add=false;
            }
        }
        if (add) results.add(d);
    }
    
    public static Declaration findMatchingOverloadedClass(Class abstractionClass, 
            List<ProducedType> signature, boolean ellipsis) {
        List<Declaration> results = 
                new ArrayList<Declaration>(1);
        if (!abstractionClass.isAbstraction()) {
            return abstractionClass;
        }
        for (Declaration overloaded: abstractionClass.getOverloads()) {
            if (hasMatchingSignature(signature, ellipsis, overloaded, false)) {
                addIfBetterMatch(results, overloaded, signature);
            }
        }
        if (results.size() == 1) {
            return results.get(0);
        }
        return abstractionClass;
    }

    public static boolean isTypeUnknown(ProducedType type) {
        return type==null || type.getDeclaration()==null ||
                type.containsUnknowns();
    }

    public static List<ProducedType> getSignature(Declaration dec) {
        if (!(dec instanceof Functional)) {
            return null;
        }
        Functional fun = (Functional)dec;
        List<ParameterList> parameterLists = 
                fun.getParameterLists();
        if (parameterLists == null || parameterLists.isEmpty()) {
            return null;
        }
        ParameterList parameterList = parameterLists.get(0);
        if (parameterList == null) {
            return null;
        }
        List<Parameter> parameters = 
                parameterList.getParameters();
        if (parameters == null) {
            return null;
        }
        List<ProducedType> signature = 
                new ArrayList<ProducedType>
                    (parameters.size());
        Unit unit = dec.getUnit();
        for (Parameter param: parameters) {
            MethodOrValue model = param.getModel();
            ProducedType t = model==null ? 
                    unknownType(unit) : model.getType();
            signature.add(t);
        }
        return signature;
    }
    
    public static boolean isCompletelyVisible(Declaration member, 
            ProducedType pt) {
        if (pt.getDeclaration() instanceof UnionType) {
            for (ProducedType ct: 
                    pt.getDeclaration().getCaseTypes()) {
                if (!isCompletelyVisible(member, 
                        ct.substitute(pt))) {
                    return false;
                }
            }
            return true;
        }
        else if (pt.getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: 
                    pt.getDeclaration().getSatisfiedTypes()) {
                if (!isCompletelyVisible(member, 
                        ct.substitute(pt))) {
                    return false;
                }
            }
            return true;
        }
        else {
            if (!isVisible(member, pt.getDeclaration())) {
                return false;
            }
            for (ProducedType at: pt.getTypeArgumentList()) {
                if (at!=null && 
                        !isCompletelyVisible(member, at)) {
                    return false;
                }
            }
            return true;
        }
    }

    static boolean isVisible(Declaration member, TypeDeclaration type) {
        return type instanceof TypeParameter || 
                type.isVisible(member.getVisibleScope()) &&
                (member.getVisibleScope()!=null || 
                !member.getUnit().getPackage().isShared() || 
                type.getUnit().getPackage().isShared());
    }

    /**
     * Given two instantiations of the same type constructor,
     * construct a principal instantiation that is a supertype
     * of both. This is impossible in the following special
     * cases:
     * 
     * - an abstract class which does not obey the principal
     *   instantiation inheritance rule
     * - an intersection between two instantiations of the
     *   same type where one argument is a type parameter
     * 
     * Nevertheless, we give it our best shot!
     */
    public static ProducedType principalInstantiation(
            TypeDeclaration dec, 
            ProducedType first, ProducedType second, 
            Unit unit) {
        List<TypeParameter> tps = dec.getTypeParameters();
        List<ProducedType> args = 
                new ArrayList<ProducedType>(tps.size());
        Map<TypeParameter,SiteVariance> 
        varianceOverrides =
                new HashMap<TypeParameter,SiteVariance>(1);
        for (TypeParameter tp: tps) {
            ProducedType arg;
            ProducedType firstArg = 
                    first.getTypeArguments().get(tp);
            ProducedType secondArg = 
                    second.getTypeArguments().get(tp);
            if (firstArg==null || secondArg==null) {
                arg = unknownType(unit);
            }
            else {
                boolean firstCo = first.isCovariant(tp);
                boolean secondCo = second.isCovariant(tp);
                boolean firstContra = first.isContravariant(tp);
                boolean secondContra = second.isContravariant(tp);
                boolean firstInv = !firstCo && !firstContra;
                boolean secondInv = !secondCo && !secondContra;
                boolean parameterized = 
                        firstArg.involvesTypeParameters() ||
                        secondArg.involvesTypeParameters();
                if (firstContra && secondContra) {
                    arg = unionType(firstArg,secondArg,unit);
                    if (!tp.isContravariant()) {
                        varianceOverrides.put(tp, IN);
                    }
                }
                else if (firstCo && secondCo) {
                    arg = intersectionType(firstArg,secondArg,unit);
                    if (!tp.isCovariant()) {
                        varianceOverrides.put(tp, OUT);
                    }
                }
                else if (firstContra && secondInv) {
                    if (firstArg.isSubtypeOf(secondArg)) {
                        arg = secondArg;
                    }
                    else if (parameterized) {
                       //irreconcilable instantiations
                       arg = unknownType(unit);
                    }
                    else {
                        return nothingType(unit);
                    }
                }
                else if (firstCo && secondInv) {
                    if (secondArg.isSubtypeOf(firstArg)) {
                        arg = secondArg;
                    }
                    else if (parameterized) {
                       //irreconcilable instantiations
                       arg = unknownType(unit);
                    }
                    else {
                        return nothingType(unit);
                    }
                }
                else if (secondCo && firstInv) {
                   if (firstArg.isSubtypeOf(secondArg)) {
                       arg = firstArg;
                   }
                   else if (parameterized) {
                      //irreconcilable instantiations
                      arg = unknownType(unit);
                   }
                   else {
                       return nothingType(unit);
                   }
                }
                else if (secondContra && firstInv) {
                    if (secondArg.isSubtypeOf(firstArg)) {
                        arg = firstArg;
                    }
                    else if (parameterized) {
                        //irreconcilable instantiations
                        arg = unknownType(unit);
                    }
                    else {
                        return nothingType(unit);
                    }
                }
                else if (firstInv && secondInv) {
                    if (firstArg.isExactly(secondArg)) {
                        arg = firstArg;
                    }
                    else if (parameterized) {
                        //type parameters that might represent 
                        //equivalent types at runtime, 
                        //irreconcilable instantiations
                        //TODO: detect cases where we know for
                        //      sure that the types are disjoint
                        //      because the type parameters only
                        //      occur as type args
                        arg = unknownType(unit);
                    }
                    else {
                        //the type arguments are distinct, and the
                        //intersection is Nothing, so there is
                        //no reasonable principal instantiation
                        return nothingType(unit);
                    }
                }
                else {
                    //opposite variances
                    //irreconcilable instantiations
                    arg = unknownType(unit);
                }
            }
            args.add(arg);
        }
        ProducedType pqt = 
                principalQualifyingType(first, second, 
                        dec, unit);
        ProducedType result = 
                dec.getProducedType(pqt, args);
        result.setVarianceOverrides(varianceOverrides);
        return result;
    }

    static ProducedType unknownType(Unit unit) {
        return new UnknownType(unit).getType();
    }

    static ProducedType nothingType(Unit unit) {
        return unit.getNothingDeclaration().getType();
    }
    
    public static boolean areConsistentSupertypes(
            ProducedType st1, ProducedType st2, Unit unit) {
        //can't inherit two instantiations of an invariant type
        //Note: I don't think we need to check type parameters of 
        //      the qualifying type, since you're not allowed to
        //      subtype an arbitrary instantiation of a nested
        //      type - only supertypes of the outer type
        //      Nor do we need to check variance overrides since
        //      supertypes can't have them.
        for (TypeParameter tp: 
                st1.getDeclaration().getTypeParameters()) {
            if (!tp.isCovariant() && !tp.isContravariant()) {
                ProducedType ta1 = 
                        st1.getTypeArguments()
                            .get(tp);
                ProducedType ta2 = 
                        st2.getTypeArguments()
                            .get(tp);
                if (ta1!=null && ta2!=null && 
                        !ta1.isExactly(ta2)) {
                    return false;
                }
            }
        }
        return !intersectionType(st1, st2, unit).isNothing();
    }
    
    /**
     * The intersection of the types inherited of the given
     * declaration. No need to worry about canonicalization
     * because:
     * 
     * 1. an inherited type can't be a union, and
     * 2. they are prevented from being disjoint types.
     */
    public static ProducedType intersectionOfSupertypes(
            TypeDeclaration td) {
        List<ProducedType> satisfiedTypes = 
                td.getSatisfiedTypes();
        int capacity = satisfiedTypes.size()+1;
        List<ProducedType> list = 
                new ArrayList<ProducedType>(capacity);
        ProducedType extendedType = td.getExtendedType();
        if (extendedType!=null) {
            list.add(extendedType);
        }
        list.addAll(satisfiedTypes);
        IntersectionType it = 
                new IntersectionType(td.getUnit());
        it.setSatisfiedTypes(list);
        return it.getType();
    }

    /**
     * The union of the case types of the given declaration.
     */
    public static ProducedType unionOfCaseTypes(
            TypeDeclaration td) {
        List<ProducedType> caseTypes = td.getCaseTypes();
        Unit unit = td.getUnit();
        if (caseTypes==null) {
            return unit.getType(unit.getAnythingDeclaration());
        }
        int capacity = caseTypes.size()+1;
        List<ProducedType> list = 
                new ArrayList<ProducedType>(capacity);
        list.addAll(caseTypes);
        UnionType it = new UnionType(unit);
        it.setCaseTypes(list);
        return it.getType();
    }

    public static int addHashForModule(int ret, Declaration decl) {
        Module module = getModule(decl);
        return (37 * ret) + 
                (module != null ? module.hashCode() : 0);
    }

    private static Module getModule(Declaration decl) {
        Scope scope = decl.getContainer();
        while (scope!=null &&
                !(scope instanceof Package)) {
            scope = scope.getContainer();
        }
        if (scope instanceof Package) {
            Package p = (Package) scope;
            return p.getModule();
        }
        else {
            return null;
        }
    }

    public static boolean sameModule(Declaration a, Declaration b) {
        Module aMod = getModule(a);
        Module bMod = getModule(b);
        return aMod.equals(bMod);
    }

    public static void clearProducedTypeCache(TypeDeclaration decl) {
        Module module = getModule(decl);
        if(module != null){
            module.clearCache(decl);
        }
    }
    
    public static List<Declaration> getInterveningRefinements(String name,
            List<ProducedType> signature, Declaration root,
            TypeDeclaration bottom, TypeDeclaration top) {
        List<Declaration> result = 
                new ArrayList<Declaration>(2);
        for (TypeDeclaration std: 
                bottom.getSupertypeDeclarations()) {
            if (std.inherits(top) && !std.equals(bottom)) {
                Declaration member = 
                        std.getDirectMember(name, 
                                signature, false);
                if (member!=null && 
                        member.isShared() && 
                        !isAbstraction(member)) {
                    TypeDeclaration td = 
                            (TypeDeclaration) 
                                member.getContainer();
                    Declaration refined = 
                            td.getRefinedMember(name, 
                                    signature, false);
                    if (refined!=null && 
                            refined.equals(root)) {
                        result.add(member);
                    }
                }
            }
        }
        return result;
    }
    
    public static List<Declaration> getInheritedDeclarations(String name,
            TypeDeclaration bottom) {
        List<Declaration> result = 
                new ArrayList<Declaration>(2);
        for (TypeDeclaration std: 
                bottom.getSupertypeDeclarations()) {
            if (!std.equals(bottom)) {
                Declaration member = 
                        std.getDirectMember(name, null, false);
                if (member!=null && 
                        member.isShared() && 
                        !isAbstraction(member)) {
                    result.add(member);
                }
            }
        }
        return result;
    }

    public static boolean isToplevelClassConstructor(TypeDeclaration td, 
            Declaration m) {
        return td.isToplevel() && 
                m instanceof Constructor;
    }

    public static boolean isToplevelAnonymousClass(Scope s) {
        if (s instanceof Class) {
            Class td = (Class) s;
            return td.isAnonymous() && td.isToplevel();
        }
        else {
            return false;
        }
    }
    
    public static boolean isNativeAbstraction(Declaration dec) {
        if (dec instanceof Overloadable) {
            Overloadable fun = (Overloadable) dec;
            List<Declaration> overloads = fun.getOverloads();
            return dec.isNative() && 
                    dec.getNative().isEmpty() && 
                    overloads != null && 
                    !overloads.isEmpty();
        }
        else {
            return false;
        }
    }
    
    public static boolean isNativeNoImpl(Declaration dec) {
        return dec.isNative() && dec.getNative().isEmpty();
    }
    
    public static boolean isNativeImplementation(Declaration dec) {
        return dec.isNative() && !dec.getNative().isEmpty();
    }
    
    public static boolean hasNativeImplementation(Declaration dec) {
        if (dec instanceof Overloadable && dec.isNative()) {
            Overloadable fun = (Overloadable) dec;
            List<Declaration> overloads = fun.getOverloads();
            if (overloads != null) {
                for (Declaration d: overloads) {
                    if (d.isNative() && 
                            !d.getNative().isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isInNativeContainer(Declaration dec) {
        Scope container = dec.getContainer();
        if (container instanceof Declaration) {
            Declaration d = (Declaration) container;
            return d.isNative();
        }
        return false;
    }
    
    public static Declaration getNativeDeclaration
            (Declaration decl, Backend backend) {
        return getNativeDeclaration(decl, 
                backend == null ? null :
                    backend.backendSupport);
    }
    
    public static Declaration getNativeDeclaration
            (Declaration dec, BackendSupport backendSupport) {
        if (dec.isNative() && 
                dec instanceof Overloadable &&
                backendSupport != null) {
            Overloadable f = (Overloadable) dec;
            Declaration abstraction = null;
            if (backendSupport.supportsBackend(Backend.fromAnnotation(dec.getNative()))) {
                abstraction = dec;
            }
            else {
                List<Declaration> overloads = f.getOverloads();
                if (overloads != null) {
                    for (Declaration d: overloads) {
                        if (backendSupport.supportsBackend(Backend.fromAnnotation(d.getNative()))) {
                            abstraction = d;
                            break;
                        }
                    }
                }
            }
            return abstraction;
        }
        else {
            return dec;
        }
    }

    public static List<ProducedType> toTypeArgs(Generic dec) {
        List<TypeParameter> params = 
                dec.getTypeParameters();
        int size = params.size();
        List<ProducedType> paramsAsArgs =
                new ArrayList<ProducedType>(size);
        for (int i=0; i<size; i++) {
            TypeParameter param = params.get(i);
            paramsAsArgs.add(param.getType());
        }
        return paramsAsArgs;
    }

}
