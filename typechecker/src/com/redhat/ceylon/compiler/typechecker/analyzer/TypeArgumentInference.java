package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.NO_TYPE_ARGS;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getMatchingParameter;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTupleType;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getUnspecifiedParameter;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.involvesTypeParams;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isConstructor;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isGeneric;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.spreadType;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.addToIntersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.addToUnion;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.appliedType;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.canonicalIntersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.intersectionOfSupertypes;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.intersectionType;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isTypeUnknown;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.typeParametersAsArgList;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.union;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NamedArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Generic;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.Reference;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedReference;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Value;

public class TypeArgumentInference {
    
    private Unit unit;
    
    public TypeArgumentInference(Unit unit) {
        this.unit = unit;
    }

    private Type unionOrIntersection(
            boolean findingUpperBounds,
            List<Type> inferredTypes) {
        if (findingUpperBounds) {
            return canonicalIntersection(inferredTypes, unit);
        }
        else {
            return union(inferredTypes, unit);
        }
    }
    
    private Type unionOrIntersectionOrNull(
            boolean findingUpperBounds,
            List<Type> inferredTypes) {
        if (inferredTypes.isEmpty()) {
            return null;
        }
        else {
            return unionOrIntersection(
                    findingUpperBounds, 
                    inferredTypes);
        }
    }
    
    private void addToUnionOrIntersection(
            boolean findingUpperBounds, 
            List<Type> list, 
            Type pt) {
        if (findingUpperBounds) {
            addToIntersection(list, pt, unit);
        }
        else {
            addToUnion(list, pt);
        }
    }

    private Type unionOrNull(List<Type> types) {
        if (types.isEmpty()) {
            return null;
        }
        return union(types, unit);
    }

    private Type intersectionOrNull(List<Type> types) {
        if (types.isEmpty()) {
            return null;
        }
        return canonicalIntersection(types, unit);
    }

    private Type inferTypeArg(TypeParameter tp,
            Type template, Type type, 
            boolean findingUpperBounds,
            Node node) {
        return inferTypeArg(tp, 
                template, type, 
                true, false,
                findingUpperBounds, 
                new ArrayList<TypeParameter>(),
                node);
    }

    private Type inferTypeArg(TypeParameter tp,
            Type paramType, Type argType, 
            boolean covariant, boolean contravariant,
            boolean findingUpperBounds,
            List<TypeParameter> visited, 
            Node argNode) {
        if (paramType!=null && argType!=null) {
            paramType = paramType.resolveAliases();
            argType = argType.resolveAliases();
            TypeDeclaration paramTypeDec = 
                    paramType.getDeclaration();
            Map<TypeParameter, Type> paramTypeArgs = 
                    paramType.getTypeArguments();
            Map<TypeParameter, SiteVariance> paramVariances = 
                    paramType.getVarianceOverrides();
            if (paramType.isTypeParameter() &&
                    paramTypeDec.equals(tp)) {
                if (tp.isTypeConstructor()) {
                    if (argType.isTypeConstructor()) {
                        return argType;
                    }
                    else {
                        return null;
                    }
                }
                else if (findingUpperBounds && covariant ||
                        !findingUpperBounds && contravariant) {
                    //ignore occurrences of covariant type 
                    //parameters in contravariant locations 
                    //in the parameter list, and occurrences 
                    //of contravariant type parameters in 
                    //covariant locations in the list
                    return null;
                }
                else if (argType.isUnknown()) {
                    //TODO: is this error really necessary now!?
                    if (argNode.getErrors().isEmpty()) {
                        argNode.addError("argument of unknown type assigned to inferred type parameter: '" + 
                                tp.getName() + "' of '" + 
                                tp.getDeclaration()
                                    .getName(unit) + "'");
                    }
                    //TODO: would it be better to return UnknownType here?
                    return null;
                }
                else {
                    return unit.denotableType(argType);
                }
            }
            else if (paramType.isTypeParameter() &&
                    !paramTypeDec.isParameterized()) {
                TypeParameter tp2 = 
                        (TypeParameter) 
                            paramTypeDec;
                if (!findingUpperBounds &&
                        //protect ourselves from revisiting
                        //this upper bound due to circularities
                        //in the upper bounds
                        !visited.contains(tp2)) {
                    visited.add(tp2);
                    List<Type> sts = tp2.getSatisfiedTypes();
                    List<Type> list = 
                            new ArrayList<Type>
                                (sts.size());
                    for (Type upperBound: sts) {
                        //recurse to the upper bounds
                        addToUnionOrIntersection(
                                findingUpperBounds, list,
                                inferTypeArg(tp, 
                                        upperBound, argType,
                                        covariant, contravariant,
                                        findingUpperBounds, 
                                        visited, argNode));
                    }
                    visited.remove(tp2);
                    return unionOrIntersectionOrNull(
                            findingUpperBounds, list);
                }
                else {
                    return null;
                }
            }
            else if (paramType.isUnion()) {
                //If there is more than one type parameter in
                //the union, ignore this union when inferring 
                //types. 
                //TODO: This is all a bit adhoc. The problem 
                //      is that when a parameter type involves 
                //      a union of type parameters, it in theory 
                //      imposes a compound constraint upon the 
                //      type parameters, but our algorithm 
                //      doesn't know how to deal with compound
                //      constraints
                /*Type typeParamType = null;
                boolean found = false;
                for (Type ct: 
                        paramType.getDeclaration()
                            .getCaseTypes()) {
                    TypeDeclaration ctd = 
                            ct.getDeclaration();
                    if (ctd instanceof TypeParameter) {
                        typeParamType = ct;
                    }
                    if (ct.containsTypeParameters()) { //TODO: check that they are "free" type params                        
                        if (found) {
                            //the parameter type involves two type
                            //parameters which are being inferred
                            return null;
                        }
                        else {
                            found = true;
                        }
                    }
                }*/
                Type pt = paramType;
                Type apt = argType;
                if (argType.isUnion()) {
                    for (Type act: argType.getCaseTypes()) {
                        //some element of the argument union 
                        //is already a subtype of the 
                        //parameter union, so throw it away 
                        //from both unions
                        if (!act.involvesDeclaration(tp) && //in a recursive generic function, T can get assigned to T
                                act.substitute(argType)
                                    .isSubtypeOf(paramType)) {
                            pt = pt.shallowMinus(act);
                            apt = apt.shallowMinus(act);
                        }
                    }
                }
                if (pt.isUnion())  {
                    boolean found = false;
                    for (Type ct: pt.getCaseTypes()) {
                        if (ct.isTypeParameter()) {
                            if (found) {
                                return null;
                            }
                            found = true;
                        }
                    }
                    //just one type parameter left in the union
                    Map<TypeParameter, Type> args = 
                            pt.getTypeArguments();
                    Map<TypeParameter, SiteVariance> variances = 
                            pt.getVarianceOverrides();
                    List<Type> cts = pt.getCaseTypes();
                    List<Type> list = 
                            new ArrayList<Type>
                                (cts.size());
                    for (Type ct: cts) {
                        addToUnionOrIntersection(
                                findingUpperBounds, list, 
                                inferTypeArg(tp, 
                                        ct.substitute(args, 
                                                variances), 
                                        apt, 
                                        covariant, contravariant,
                                        findingUpperBounds,
                                        visited, argNode));
                    }
                    return unionOrIntersectionOrNull(
                            findingUpperBounds, list);
                }
                else {
                    return inferTypeArg(tp, 
                            pt, apt, 
                            covariant, contravariant,
                            findingUpperBounds,
                            visited, argNode);
                }
                /*else {
                    //if the param type is of form T|A1 and the arg type is
                    //of form A2|B then constrain T by B and A1 by A2
                    Type pt = paramType.minus(typeParamType);
                    addToUnionOrIntersection(tp, list, inferTypeArg(tp, 
                            paramType.minus(pt), argType.minus(pt), visited));
                    addToUnionOrIntersection(tp, list, inferTypeArg(tp, 
                            paramType.minus(typeParamType), pt, visited));
                    //return null;
                }*/
            } 
            else if (paramType.isIntersection()) {
                List<Type> sts = 
                        paramTypeDec.getSatisfiedTypes();
                List<Type> list = 
                        new ArrayList<Type>
                            (sts.size());
                for (Type ct: sts) {
                    //recurse to intersected types
                    addToUnionOrIntersection(
                            findingUpperBounds, list, 
                            inferTypeArg(tp, 
                                    ct.substitute(
                                            paramTypeArgs,
                                            paramVariances), 
                                    argType, 
                                    covariant, contravariant,
                                    findingUpperBounds,
                                    visited, argNode));
                }
                return unionOrIntersectionOrNull(
                        findingUpperBounds, list);
            }
            else if (argType.isUnion()) {
                List<Type> cts = 
                        argType.getCaseTypes();
                List<Type> list = 
                        new ArrayList<Type>
                            (cts.size());
                for (Type ct: cts) {
                    //recurse to union types
                    addToUnion(list, 
                            inferTypeArg(tp, 
                            paramType, 
                            ct.substitute(
                                    paramTypeArgs,
                                    paramVariances),
                            covariant, contravariant,
                            findingUpperBounds,
                            visited, argNode));
                }
                return unionOrNull(list);
            }
            else if (argType.isIntersection()) {
                List<Type> sts = 
                        argType.getSatisfiedTypes();
                List<Type> list = 
                        new ArrayList<Type>
                            (sts.size());
                for (Type st: sts) {
                    //recurse to intersected types
                    addToIntersection(list, 
                            inferTypeArg(tp,
                                    paramType, 
                                    st.substitute(
                                            paramTypeArgs, 
                                            paramVariances), 
                                    covariant, contravariant,
                                    findingUpperBounds,
                                    visited, argNode), 
                            unit);
                }
                return intersectionOrNull(list);
            }
            else {
                Type supertype = 
                        argType.getSupertype(paramTypeDec);
                if (supertype!=null) {
                    List<Type> list = 
                            new ArrayList<Type>(2);
                    Type pqt = 
                            paramType.getQualifyingType();
                    Type sqt = 
                            supertype.getQualifyingType();
                    if (pqt!=null && sqt!=null) {
                        //recurse to qualifying types
                        addToUnionOrIntersection(
                                findingUpperBounds, list, 
                                inferTypeArg(tp, 
                                        pqt, sqt, 
                                        covariant, contravariant,
                                        findingUpperBounds,
                                        visited, argNode));
                    }
                    inferTypeArg(tp, 
                            paramType, supertype, 
                            covariant, contravariant,
                            findingUpperBounds,
                            list, visited, 
                            argNode);
                    return unionOrIntersectionOrNull(
                            findingUpperBounds, list);
                }
                else {
                    return null;
                }
            }
        }
        else {
            return null;
        }
    }
    
    private void inferTypeArg(TypeParameter tp,
            Type paramType, Type supertype, 
            boolean covariant, boolean contravariant,
            boolean findingUpperBounds,
            List<Type> list, 
            List<TypeParameter> visited,
            Node argNode) {
        List<TypeParameter> typeParameters = 
                paramType.getDeclaration()
                    .getTypeParameters();
        List<Type> paramTypeArgs = 
                paramType.getTypeArgumentList();
        List<Type> superTypeArgs = 
                supertype.getTypeArgumentList();
        for (int j=0; 
                j<paramTypeArgs.size() && 
                j<superTypeArgs.size() && 
                j<typeParameters.size(); 
                j++) {
            Type paramTypeArg = 
                    paramTypeArgs.get(j);
            Type argTypeArg = 
                    superTypeArgs.get(j);
            TypeParameter typeParameter = 
                    typeParameters.get(j);
            boolean co;
            boolean contra;
            if (paramType.isCovariant(typeParameter)) {
                //leave them alone
                co = covariant;
                contra = contravariant;
            }
            else if (paramType.isContravariant(typeParameter)) {
                if (covariant|contravariant) {
                    //flip them
                    co = !covariant;
                    contra = !contravariant;
                }
                else {
                    //leave them invariant
                    co = false;
                    contra = false;
                }
            }
            else { //invariant
                co = false;
                contra = false;
            }
            addToUnionOrIntersection(
                    findingUpperBounds, list, 
                    inferTypeArg(tp,
                            paramTypeArg, argTypeArg, 
                            co, contra,
                            findingUpperBounds,
                            visited, argNode));
        }
    }
    
    private Type inferTypeArgumentFromNamedArgs(
            TypeParameter tp, ParameterList parameters, 
            Type qt, Tree.NamedArgumentList nal, 
            Declaration invoked) {
        boolean findingUpperBounds = 
                isEffectivelyContravariant(tp, invoked,
                        specifiedParameters(nal, parameters));
        List<NamedArgument> namedArgs = 
                nal.getNamedArguments();
        Set<Parameter> foundParameters = 
                new HashSet<Parameter>();
        List<Type> inferredTypes =
                new ArrayList<Type>
                    (namedArgs.size());
        for (Tree.NamedArgument arg: namedArgs) {
            inferTypeArgFromNamedArg(arg, tp, qt, 
                    parameters,
                    findingUpperBounds,
                    inferredTypes, 
                    invoked,
                    foundParameters);
        }
        Parameter sp = 
                getUnspecifiedParameter(null, parameters, 
                        foundParameters);
        if (sp!=null) {
            Tree.SequencedArgument sarg = 
                    nal.getSequencedArgument();
            inferTypeArgFromSequencedArg(sarg, tp, sp, 
                    findingUpperBounds,
                    inferredTypes, sarg);
        }
        return unionOrIntersection(findingUpperBounds, 
                inferredTypes);
    }

    private void inferTypeArgFromSequencedArg(
            Tree.SequencedArgument sa, 
            TypeParameter tp, Parameter sp, 
            boolean findingUpperBounds,
            List<Type> inferredTypes, 
            Node argNode) {
        Type att;
        if (sa==null) {
            att = unit.getEmptyType();
        }
        else {
            List<Tree.PositionalArgument> args = 
                    sa.getPositionalArguments();
            att = getTupleType(args, unit, false);
        }
        Type spt = sp.getType();
        addToUnionOrIntersection(
                findingUpperBounds, 
                inferredTypes,
                inferTypeArg(tp, spt, att, 
                        findingUpperBounds, 
                        argNode));
    }

    private void inferTypeArgFromNamedArg(
            Tree.NamedArgument arg, 
            TypeParameter tp, Type receiverType, 
            ParameterList parameters, 
            boolean findingUpperBounds,
            List<Type> inferredTypes, 
            Declaration invoked, 
            Set<Parameter> foundParameters) {
        Type type = null;
        if (arg instanceof Tree.SpecifiedArgument) {
            Tree.SpecifiedArgument sa = 
                    (Tree.SpecifiedArgument) arg;
            Tree.SpecifierExpression se = 
                    sa.getSpecifierExpression();
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                type = e.getTypeModel();
            }
        }
        else if (arg instanceof Tree.TypedArgument) {
            //copy/pasted from checkNamedArgument()
            Tree.TypedArgument ta = 
                    (Tree.TypedArgument) arg;
            type = ta.getDeclarationModel()
                    .getTypedReference() //argument can't have type parameters
                    .getFullType();
        }
        if (type!=null) {
            Parameter parameter = 
                    getMatchingParameter(parameters, arg, 
                            foundParameters);
            if (parameter!=null) {
                foundParameters.add(parameter);
                Type paramType = 
                        parameterType(receiverType, 
                                parameter, invoked);
                addToUnionOrIntersection(
                        findingUpperBounds,
                        inferredTypes,
                        inferTypeArg(tp, 
                                paramType, type,
                                findingUpperBounds, 
                                arg));
            }
        }
    }
    
    private Type inferTypeArgumentFromPositionalArgs(
            TypeParameter tp, 
            ParameterList parameters, 
            Type receiverType, 
            Tree.PositionalArgumentList pal, 
            Declaration invoked) {
        boolean findingUpperBounds = 
                isEffectivelyContravariant(tp, invoked,
                        specifiedParameters(pal, parameters));
        List<Tree.PositionalArgument> args = 
                pal.getPositionalArguments();
        List<Type> inferredTypes = 
                new ArrayList<Type>
                    (args.size());
        List<Parameter> params = parameters.getParameters();
        for (int i=0; i<params.size(); i++) {
            Parameter parameter = params.get(i);
            if (args.size()>i) {
                Tree.PositionalArgument arg = args.get(i);
                Type at = arg.getTypeModel();
                if (arg instanceof Tree.SpreadArgument) {
                    at = spreadType(at, unit, true);
                    List<Parameter> subList = 
                            params.subList(i, params.size());
                    Type parameterTypeTuple = 
                            unit.getParameterTypesAsTupleType(
                                    subList, 
                                    //Note: this is an abuse
                                    //of the API - the parameters
                                    //don't really belong to
                                    //this type, they belong
                                    //to the invoked declaration
                                    receiverType);
                    addToUnionOrIntersection(
                            findingUpperBounds, 
                            inferredTypes, 
                            inferTypeArg(tp, 
                                    parameterTypeTuple, at, 
                                    findingUpperBounds, 
                                    pal));
                }
                else if (arg instanceof Tree.Comprehension) {
                    if (parameter.isSequenced()) {
                        Tree.Comprehension c = 
                                (Tree.Comprehension) arg;
                        inferTypeArgFromComprehension(tp, 
                                parameter, c, 
                                findingUpperBounds,
                                inferredTypes);
                    }
                }
                else {
                    if (parameter.isSequenced()) {
                        inferTypeArgFromPositionalArgs(tp, 
                                parameter,
                                args.subList(i, args.size()),
                                findingUpperBounds,
                                inferredTypes);
                        break;
                    }
                    else {
                        Type parameterType = 
                                parameterType(receiverType,
                                        parameter, invoked);
                        addToUnionOrIntersection(
                                findingUpperBounds, 
                                inferredTypes,
                                inferTypeArg(tp, 
                                        parameterType, at,
                                        findingUpperBounds, 
                                        pal));
                    }
                }
            }
        }
        return unionOrIntersection(findingUpperBounds, 
                inferredTypes);
    }
    
    /**
     * Compute the type of the given parameter as best we
     * can from all type arguments that we actually have at
     * our disposal. That's obviously not all of them, since
     * some of them we're trying to infer.
     * 
     * @param receiverType a qualifying type
     * @param parameter the parameter
     * @param invoked the declaration to which the parameter
     *        belongs
     *        
     * @return the type of the parameter, taking into 
     *         account type arguments available in the
     *         qualifying type.
     */
    private Type parameterType(Type receiverType, 
            Parameter parameter, Declaration invoked) {
        if (receiverType == null || 
                !invoked.isClassOrInterfaceMember() ||
                //this is probably a ref to a static method
                //method of a Java class and we don't need
                //the qualifying type
                //TODO: check getStaticMethodReference()
                unit.isCallableType(receiverType)) {
            return parameter.getModel()
                    .getReference()
                    .getFullType();
        }
        else {
            //this is sorta rubbish: we use the type that 
            //declares the member with the parameter to 
            //substitute type args into the parameter type, 
            //which I guess is an abuse of the API
            Type supertype = 
                    getDeclaringSupertype(receiverType, 
                            invoked);
            if (supertype==null) {
                return null;
            }
            else {
                return supertype.getTypedParameter(parameter)
                        .getFullType();
            }
        }
    }
    
    /**
     * Search for the supertype of the given type to which
     * the given declaration directly or indirectly belongs.
     * 
     * Since we can use arguments to qualified invocations 
     * to infer type arguments to qualifying invocations,
     * the given declaration is not necessarily a (direct)
     * member of the type.
     *  
     * @param qualifyingType some direct or indirect 
     *        qualifying type
     * @param invoked the thing being directly invoked
     * 
     * @return the supertype of the the given type in which
     *         the member is nested directly or indirectly
     */
    private static Type getDeclaringSupertype(
            Type qualifyingType,
            Declaration invoked) {
        if (isConstructor(invoked)) {
            Scope container = invoked.getContainer();
            if (container instanceof Declaration) {
                invoked = (Declaration) container;
            }
            else {
                return null;
            }
        }
        Scope supertypeDec = invoked.getContainer();
        if (supertypeDec instanceof TypeDeclaration) {
            return qualifyingType.getSupertype(
                    (TypeDeclaration) supertypeDec);
        }
        else {
            return null;
        }
        //Maybe TODO: I think something like this would
        //let you use arguments to an invocation to infer
        //type arguments to outer qualifying types!
        /*Type qualifyingType = receiverType;
        Type supertype;
        do {
            supertype = 
                    qualifyingType.getSupertype(supertypeDec);
            Scope container = supertypeDec.getContainer();
            if (container instanceof TypeDeclaration) {
                supertypeDec = (TypeDeclaration) container;
            }
            else {
                break;
            }
        }
        while (supertype==null && qualifyingType!=null);
        return supertype;*/
    }

    private Type inferTypeArgumentFromPositionalArgs(
            TypeParameter tp, 
            List<Type> paramTypes,
            Type paramTypesAsTuple,
            boolean sequenced, Type qt, 
            Tree.PositionalArgumentList pal, 
            boolean findingUpperBounds) {
        List<Tree.PositionalArgument> args = 
                pal.getPositionalArguments();
        List<Type> inferredTypes = new ArrayList<Type>();
        int paramSize = paramTypes.size();
        int argCount = args.size();

        for (int i=0; i<paramSize && i<argCount; i++) {
            Type pt = paramTypes.get(i);
            Tree.PositionalArgument arg = args.get(i);
            Type at = arg.getTypeModel();
            if (arg instanceof Tree.SpreadArgument) {
                Type tailType = 
                        unit.getTailType(paramTypesAsTuple, 
                                i);
                addToUnionOrIntersection(
                        findingUpperBounds, 
                        inferredTypes,
                        inferTypeArg(tp, tailType, at, 
                                findingUpperBounds, 
                                pal));
            }
            else if (arg instanceof Tree.Comprehension) {
                if (sequenced && i==paramSize-1) {
                    Type set = 
                            pt==null ? null : 
                                unit.getIteratedType(pt);
                    addToUnionOrIntersection(
                            findingUpperBounds, 
                            inferredTypes,
                            inferTypeArg(tp, set, at, 
                                    findingUpperBounds, 
                                    pal));
                }
                break;
            }
            else {
                if (sequenced && i==paramSize-1) {
                    for (int j=i; j<argCount; j++) {
                        Type spt = 
                                unit.getSequentialElementType(pt);
                        Type vat = 
                                args.get(j)
                                    .getTypeModel();
                        addToUnionOrIntersection(
                                findingUpperBounds, 
                                inferredTypes,
                                inferTypeArg(tp, spt, vat, 
                                        findingUpperBounds, 
                                        pal));
                    }
                }
                else {
                    addToUnionOrIntersection(
                            findingUpperBounds, 
                            inferredTypes,
                            inferTypeArg(tp, pt, at, 
                                    findingUpperBounds, 
                                    pal));
                }
            }
        }

        return unionOrIntersection(
                findingUpperBounds, inferredTypes);
    }

    private void inferTypeArgFromPositionalArgs(
            TypeParameter tp, 
            Parameter parameter, 
            List<Tree.PositionalArgument> args,
            boolean findingUpperBounds,
            List<Type> inferredTypes) {
        for (int k=0; k<args.size(); k++) {
            Tree.PositionalArgument pa = args.get(k);
            Type sat = pa.getTypeModel();
            if (sat!=null) {
                Type pt = parameter.getType();
                if (pa instanceof Tree.SpreadArgument) {
                    sat = spreadType(sat, unit, true);
                    addToUnionOrIntersection(
                            findingUpperBounds, 
                            inferredTypes,
                            inferTypeArg(tp, pt, sat, 
                                    findingUpperBounds,
                                    pa));
                }
                else {
                    Type spt = unit.getIteratedType(pt);
                    addToUnionOrIntersection(
                            findingUpperBounds, 
                            inferredTypes,
                            inferTypeArg(tp, spt, sat, 
                                    findingUpperBounds,
                                    pa));
                }
            }
        }
    }

    private void inferTypeArgFromComprehension(
            TypeParameter tp, 
            Parameter parameter, 
            Tree.Comprehension c, 
            boolean findingUpperBounds,
            List<Type> inferredTypes) {
        Type sat = c.getTypeModel();
        Type pt = parameter.getType();
        if (sat!=null && pt!=null) {
            Type spt = unit.getIteratedType(pt);
            addToUnionOrIntersection(
                    findingUpperBounds, 
                    inferredTypes,
                    inferTypeArg(tp, spt, sat, 
                            findingUpperBounds, 
                            c));
        }
    }
    
    /**
     * Infer the type arguments for a reference to a 
     * generic function that occurs as an argument in
     * an invocation.
     */
    List<Type> getInferredTypeArgsForFunctionRef(
            Tree.StaticMemberOrTypeExpression smte) {
        Tree.TypeArguments typeArguments = 
                smte.getTypeArguments();
        if (typeArguments instanceof Tree.InferredTypeArguments) {
            //the model object for the function ref 
            Declaration reference = smte.getDeclaration();
            List<TypeParameter> typeParameters = 
                    getTypeParametersAccountingForTypeConstructor(
                            reference);
            if (typeParameters==null ||
                    typeParameters.isEmpty()) {
                //nothing to infer
                return NO_TYPE_ARGS;
            }
            else {
                //set earlier in inferParameterTypes()
                TypedReference paramTypedRef = 
                        smte.getTargetParameter();
                Type paramType = smte.getParameterType();
                //the method or class to whose parameter
                //the function ref is being passed
                if (paramType==null && paramTypedRef!=null) {
                    paramType = paramTypedRef.getFullType();
                }
                if (paramType==null) {
                    return null;
                }
                if (isArgumentToGenericParameter(
                        paramTypedRef, 
                        paramType)) {
                    return null;
                }
                Declaration paramDec;
                Declaration parameterizedDec;
                if (paramTypedRef!=null) {
                    paramDec = 
                            paramTypedRef.getDeclaration();
                    parameterizedDec = 
                            (Declaration)
                                paramDec.getContainer();
                }
                else {
                    paramDec = null;
                    parameterizedDec = null;
                }
                
                Reference arg = appliedReference(smte);
                
                if (!smte.getStaticMethodReferencePrimary() &&
                        reference instanceof Functional && 
                        paramDec instanceof Functional &&
                        paramTypedRef!=null) {
                    //when we have actual individualized
                    //parameters on both the given function
                    //ref, and the callable parameter to
                    //which it is assigned, we use a more
                    //forgiving algorithm that doesn't throw
                    //away all constraints just because one
                    //constraint involves unknown type 
                    //parameters of the declaration with the
                    //callable parameter
                    Functional fun = (Functional) reference;
                    List<ParameterList> apls = 
                            fun.getParameterLists();
                    Functional pfun = (Functional) paramDec;
                    List<ParameterList> ppls = 
                            pfun.getParameterLists();
                    if (apls.isEmpty() || ppls.isEmpty()) {
                        return null; //TODO: to give a nicer error
                    }
                    else {
                        ParameterList aplf = apls.get(0);
                        ParameterList pplf = ppls.get(0);
                        List<Parameter> apl = 
                                aplf.getParameters();
                        List<Parameter> ppl = 
                                pplf.getParameters();
                        boolean[] specifiedParams = 
                                specifiedParameters(
                                        apl.size(), 
                                        ppl.size());
                        List<Type> inferredTypes = 
                                new ArrayList<Type>
                                    (typeParameters.size());
                        for (TypeParameter tp: typeParameters) {
                            boolean findUpperBounds =
                                    isEffectivelyContravariant(
                                            tp, reference, 
                                            specifiedParams);
                            Type it = 
                                    inferFunctionRefTypeArg(
                                            smte, 
                                            typeParameters,
                                            paramTypedRef, 
                                            parameterizedDec, 
                                            arg, 
                                            apl, ppl, tp,
                                            findUpperBounds);
                            inferredTypes.add(it);
                        }
                        return inferredTypes;
                    }
                }
                else {
                    //use a worse algorithm that throws 
                    //away too many constraints (I guess we
                    //should improve this, including in the
                    //spec)
                    
                    if (unit.isSequentialType(paramType)) {
                        paramType = 
                                unit.getSequentialElementType(
                                        paramType);
                    }
                    if (unit.isCallableType(paramType)) {
                        //this is the type of the parameter list
                        //of the function ref itself, which 
                        //involves the type parameters we are
                        //trying to infer
                        Type parameterListType;
                        Type fullType;
                        if (smte.getStaticMethodReferencePrimary()) {
                            Type type = arg.getType();
                            Type et = unit.getEmptyType();
                            parameterListType = appliedType(
                                    unit.getTupleDeclaration(), 
                                    type, type, et);
                            fullType = appliedType(
                                    unit.getCallableDeclaration(),
                                    type, parameterListType);
                        }
                        else {
                            fullType = arg.getFullType();
                            parameterListType = 
                                    unit.getCallableTuple(fullType);
                        }
                        //this is the type of the parameter list
                        //of the callable parameter that the 
                        //function ref is being passed to (these
                        //parameters are going to be assigned to
                        //the parameters of the function ref)
                        Type argumentListType = 
                                unit.getCallableTuple(paramType);
                        int argCount = 
                                unit.getTupleElementTypes(
                                        argumentListType)
                                    .size();
                        List<Type> inferredTypes = 
                                new ArrayList<Type>
                                    (typeParameters.size());
                        for (TypeParameter tp: typeParameters) {
                            boolean findUpperBounds = 
                                    isEffectivelyContravariant(
                                            tp, fullType, 
                                            argCount);
                            Type it = 
                                    inferFunctionRefTypeArg(
                                            smte,
                                            typeParameters, 
                                            parameterizedDec,
                                            parameterListType,
                                            argumentListType,
                                            tp,
                                            findUpperBounds);
                            inferredTypes.add(it);
                        }
                        return inferredTypes;
                    }
                    else {
                        //not a callable parameter, nor a
                        //value parameter of callable type
                        return null;
                    }
                }
            }
        }
        else {
            //not inferring
            return null;
        }
    }

    private static boolean isArgumentToGenericParameter(
            TypedReference paramTypedRef, Type paramType) {
        return paramType.resolveAliases()
                    .isTypeConstructor() ||
                paramTypedRef != null &&
                isGeneric(paramTypedRef.getDeclaration());
    }

    /**
     * Infer the type arguments for a reference to a 
     * generic function that occurs as a parameter in
     * an invocation. This implementation is used when 
     * have a direct ref to the actual declaration.
     */
    private Type inferFunctionRefTypeArg(
            Tree.StaticMemberOrTypeExpression smte,
            List<TypeParameter> typeParams, 
            TypedReference param, 
            Declaration pd, Reference arg, 
            List<Parameter> apl, List<Parameter> ppl,
            TypeParameter tp, 
            boolean findingUpperBounds) {
        List<Type> list = 
                new ArrayList<Type>();
        for (int i=0; 
                i<apl.size() && 
                i<ppl.size(); 
                i++) {
            Parameter ap = apl.get(i);
            Parameter pp = ppl.get(i);
            Type type = 
                    param.getTypedParameter(pp)
                        .getFullType();
            Type template = 
                    arg.getTypedParameter(ap)
                        .getFullType();
            Type it = 
                    inferTypeArg(tp, 
                            template, type, 
                            findingUpperBounds, 
                            smte);
            if (!(isTypeUnknown(it) ||
//                    it.involvesTypeParameters(typeParams) ||
                    involvesTypeParams(pd, it))) {
                addToUnionOrIntersection(findingUpperBounds, 
                        list, it);
            }
        }
        return unionOrIntersection(findingUpperBounds, 
                list);
    }
    
    /**
     * Infer the type arguments for a reference to a 
     * generic function that occurs as a parameter in
     * an invocation. This implementation is used when 
     * have an indirect ref whose type is a type 
     * constructor. This version is also used for static
     * references (?)
     */
    private Type inferFunctionRefTypeArg(
            Tree.StaticMemberOrTypeExpression smte, 
            List<TypeParameter> typeParams, Declaration pd, 
            Type template, Type type,
            TypeParameter tp, 
            boolean findingUpperBounds) {
        Type it = 
                inferTypeArg(tp,
                        template, type,
                        findingUpperBounds, smte);
        if (isTypeUnknown(it) ||
//                it.involvesTypeParameters(typeParams) ||
                involvesTypeParams(pd, it)) {
            return unit.getNothingType();
        }
        else {
            return it;
        }
    }

    /**
     * Infer type arguments for a generic function reference 
     * that occurs within the primary of an invocation 
     * expression.
     * 
     * @param that the invocation
     * @param receiverType the qualifying type
     * @param type the type constructor
     * @param typeParameters the type parameters to infer
     * 
     * @return the type arguments
     */
    List<Type> getInferredTypeArgsForTypeConstructor(
            Tree.InvocationExpression that,
            Type receiverType, 
            Type type,
            List<TypeParameter> typeParameters) {
        Tree.PositionalArgumentList pal = 
                that.getPositionalArgumentList();
        if (pal==null) {
            return null;
        }
        else {
            List<Type> typeArguments = 
                    new ArrayList<Type>();
            for (TypeParameter tp: typeParameters) {
                List<Type> paramTypes = 
                        unit.getCallableArgumentTypes(type);
                Type paramTypesAsTuple =
                        unit.getCallableTuple(type);
                boolean sequenced = 
                        unit.isTupleLengthUnbounded(
                                paramTypesAsTuple);
                int argCount = 
                        pal.getPositionalArguments()
                            .size();
                boolean findUpperBounds =
                        isEffectivelyContravariant(tp,
                                type, argCount);
                Type it = 
                        inferTypeArgumentFromPositionalArgs(
                                tp, paramTypes, 
                                paramTypesAsTuple, 
                                sequenced, receiverType, 
                                pal, findUpperBounds);
                typeArguments.add(it);
            }
            return typeArguments;
        }
    }
    
    /**
     * Infer type arguments for the qualifying type in a
     * static method reference that is directly invoked.
     * This method does not correctly handle stuff like
     * constructors or Java static methods.
     * 
     * @param that the invocation
     * @param type the type whose type arguments we're
     *             inferring (the qualifying type)
     * @param receiverType 
     */
    List<Type> getInferredTypeArgsForStaticReference(
            Tree.InvocationExpression that, 
            TypeDeclaration type, Type receiverType) {
        Tree.PositionalArgumentList pal = 
                that.getPositionalArgumentList();
        Tree.MemberOrTypeExpression primary = 
                (Tree.MemberOrTypeExpression) 
                    that.getPrimary();
        Declaration invoked = primary.getDeclaration();
        if (pal == null) {
            return null;
        }
        else {
            if (invoked instanceof Functional) {
                List<PositionalArgument> args = 
                        pal.getPositionalArguments();
                Functional fun = (Functional) invoked;
                List<ParameterList> parameterLists = 
                        fun.getParameterLists();
                if (args.isEmpty() || 
                        parameterLists.isEmpty()) {
                    return null;
                }
                else {
                    //a static method ref invocation has exactly
                    //one meaningful argument (the instance of
                    //the receiving type)
                    Tree.PositionalArgument arg = 
                            args.get(0);
                    if (arg == null) {
                        return null;
                    }
                    else {
                        Type at = arg.getTypeModel();
                        Type tt = type.getType();
                        List<TypeParameter> typeParams = 
                                type.getTypeParameters();
                        List<Type> typeArgs = 
                                new ArrayList<Type>
                                    (typeParams.size());
                        for (TypeParameter tp: typeParams) {
                            Type it = 
                                    inferTypeArg(tp, tt, at,
                                            false, //TODO: is this 100% correct?
                                            arg);
                            if (it==null || 
                                    it.containsUnknowns()) {
                                that.addError("could not infer type argument from given arguments: type parameter '" + 
                                        tp.getName() + 
                                        "' could not be inferred");
                            }
                            typeArgs.add(it);
                        }
                        return constrainInferredTypes(
                                typeParams, typeArgs,
                                receiverType, invoked);
                    }
                }
            }
            else {
                return null;
            }
        }
    }
    
    /**
     * Infer type arguments for a given generic declaration,
     * using the value arguments of an invocation of a given
     * declaration. 
     * 
     * @param that the invocation
     * @param invoked the thing actually being invoked
     * @param generic the thing we're inferring type 
     *        arguments for, which may not be the thing
     *        actually being invoked
     * @param receiverType 
     *        
     * @return a list of inferred type arguments
     */
    List<Type> getInferredTypeArgsForReference(
            Tree.InvocationExpression that,
            Declaration invoked,
            Generic generic, Type receiverType) {
        if (invoked instanceof Functional) {
            Functional functional = (Functional) invoked;
            List<ParameterList> parameterLists = 
                    functional.getParameterLists();
            if (parameterLists.isEmpty()) {
                return null;
            }
            else {
                List<Type> typeArgs = new ArrayList<Type>();
                List<TypeParameter> typeParameters = 
                        generic.getTypeParameters();
                for (TypeParameter tp: typeParameters) {
                    ParameterList pl = parameterLists.get(0);
                    Type it = 
                            inferTypeArgument(that, 
                                    receiverType, tp, 
                                    pl, invoked);
                    if (it==null || it.containsUnknowns()) {
                        that.addError("could not infer type argument from given arguments: type parameter '" + 
                                tp.getName() + "' could not be inferred");
                    }
                    typeArgs.add(it);
                }
                return constrainInferredTypes(
                        typeParameters, typeArgs, 
                        receiverType, invoked);
            }
        }
        else {
            return null;
        }
    }

    private Type inferTypeArgument(
            Tree.InvocationExpression that,
            Type receiverType, TypeParameter tp, 
            ParameterList parameters, 
            Declaration invoked) {
        Tree.PositionalArgumentList pal = 
                that.getPositionalArgumentList();
        Tree.NamedArgumentList nal = 
                that.getNamedArgumentList();
        if (pal!=null) {
            return inferTypeArgumentFromPositionalArgs(tp, 
                    parameters, receiverType, pal, invoked);
        }
        else if (nal!=null) {
            return inferTypeArgumentFromNamedArgs(tp, 
                    parameters, receiverType, nal, invoked);
        }
        else {
            //impossible
            return null;
        }
    }
    
    private static boolean[] specifiedParameters(
            int argumentCount, int total) {
        boolean[] specified = 
                new boolean[total];
        for (int i=0; 
                i<argumentCount && i<total; 
                i++) {
            specified[i] = true;
        }
        return specified;
    }

    private static boolean[] specifiedParameters(
            Tree.PositionalArgumentList args,
            ParameterList parameters) {
        List<Parameter> params = parameters.getParameters();
        boolean[] specified = 
                new boolean[params.size()];
        for (Tree.PositionalArgument arg: 
                args.getPositionalArguments()) {
            Parameter p = arg.getParameter();
            if (p!=null) {
                int loc = params.indexOf(p);
                if (loc>=0) {
                    specified[loc] = true;
                }
            }
        }
        return specified;
    }

    private static boolean[] specifiedParameters(
            Tree.NamedArgumentList args,
            ParameterList parameters) {
        List<Parameter> params = parameters.getParameters();
        boolean[] specified = 
                new boolean[params.size()];
        for (Tree.NamedArgument arg: 
                args.getNamedArguments()) {
            Parameter p = arg.getParameter();
            if (p!=null) {
                int loc = params.indexOf(p);
                if (loc>=0) {
                    specified[loc] = true;
                }
            }
        }
        Tree.SequencedArgument arg = 
                args.getSequencedArgument();
        if (arg!=null) {
            Parameter p = arg.getParameter();
            if (p!=null) {
                int loc = params.indexOf(p);
                if (loc>=0) {
                    specified[loc] = true;
                }
            }
        }
        return specified;
    }

    /**
     * Determine if a type parameter is contravariant for
     * the purposes of type argument inference for an 
     * indirect function reference, that is for a reference
     * to a value whose type is a type constructor for a
     * function type.
     * 
     * @param tp the type parameter
     * @param fullType the type of the reference being
     *        invoked
     * @param argumentListLength the number of args that 
     *        were given
     * 
     * @return true if we should treat it is contravariant
     */
    private boolean isEffectivelyContravariant(
            TypeParameter tp, Type fullType, 
            int argumentListLength) {
        if (tp.isCovariant()) {
            return false;
        }
        else if (tp.isContravariant()) {
            return true;
        }
        else {
            
            Type returnType = 
                    unit.getCallableReturnType(fullType);
            
            if (returnType!=null) {
                boolean occursInvariantly =
                        returnType.occursInvariantly(tp);
                boolean occursCovariantly =
                        returnType.occursCovariantly(tp);
                boolean occursContravariantly =
                        returnType.occursContravariantly(tp);
                if (occursCovariantly
                        && !occursContravariantly
                        && !occursInvariantly) {
                    //if the parameter occurs only
                    //covariantly in the return type,
                    //then treat it as 'out'
                    return false;
                }
                else if (!occursCovariantly
                        && occursContravariantly
                        && !occursInvariantly) {
                    //if the parameter occurs only
                    //contravariantly in the return type,
                    //then treat it as 'in'
                    return true;
                }
            }
            
            List<Type> parameterTypes =
                    unit.getCallableArgumentTypes(fullType);
            if (parameterTypes!=null) {
                boolean occursContravariantly = false;
                boolean occursCovariantly = false;
                boolean occursInvariantly = false;
                for (int i=0, size = parameterTypes.size(); 
                        i<size && i<argumentListLength; 
                        i++) {
                    Type pt = 
                            parameterTypes.get(i);
                    if (pt!=null) {
                        occursContravariantly = 
                                occursContravariantly || 
                                pt.occursContravariantly(tp);
                        occursCovariantly = 
                                occursCovariantly || 
                                pt.occursCovariantly(tp);
                        occursInvariantly = 
                                occursInvariantly || 
                                pt.occursInvariantly(tp);
                    }
                }
                //if the parameter occurs only contravariantly 
                //in the argument list, then treat it as 'in'
                return occursContravariantly
                        && !occursCovariantly
                        && !occursInvariantly;
            }
        }
        
        return false;
    }
    
    /**
     * Determine if a type parameter is contravariant for
     * the purposes of type argument inference for a direct
     * invocation of a function or a direct reference to 
     * the function.
     * 
     * @param tp the type parameter
     * @param invoked the declaration actually being invoked
     *        (not always the owner of the type parameter!)
     * @param specifiedArguments the args that were given
     * 
     * @return true if we should treat it is contravariant
     */
    private boolean isEffectivelyContravariant(
            TypeParameter tp, Declaration invoked, 
            boolean[] specifiedArguments) {
        if (tp.isCovariant()) {
            return false;
        }
        else if (tp.isContravariant()) {
            return true;
        }
        else {
            
            //for functions and class aliases, we need to 
            //consider how the type parameter occurs in the 
            //"return type"
            Type fullType = 
                    invoked.getReference()
                        .getFullType();
            Type returnType =
                    unit.getCallableReturnType(fullType);
            
            if (returnType!=null) {
                boolean occursInvariantly =
                        returnType.occursInvariantly(tp);
                boolean occursCovariantly =
                        returnType.occursCovariantly(tp);
                boolean occursContravariantly =
                        returnType.occursContravariantly(tp);
                if (occursCovariantly
                        && !occursContravariantly
                        && !occursInvariantly) {
                    //if the parameter occurs only
                    //covariantly in the return type,
                    //then treat it as 'out'
                    return false;
                }
                else if (!occursCovariantly
                        && occursContravariantly
                        && !occursInvariantly) {
                    //if the parameter occurs only
                    //contravariantly in the return type,
                    //then treat it as 'in'
                    return true;
                }
            }
            
            if (invoked instanceof Functional) {
                //Return type wasn't definitive, optimize 
                //variance for parameters
                Functional fun = (Functional) invoked;
                List<ParameterList> paramLists = 
                        fun.getParameterLists();
                boolean occursContravariantly = false;
                boolean occursCovariantly = false;
                boolean occursInvariantly = false;
                if (!paramLists.isEmpty()) {
                    List<Parameter> params =
                            paramLists.get(0)
                                .getParameters();
                    for (int i=0, size = params.size(); 
                            i<size; i++) {
                        //ignore parameters with no argument
                        if (specifiedArguments==null ||
                                specifiedArguments[i]) {
                            Parameter p = params.get(i);
                            FunctionOrValue model = 
                                    p.getModel();
                            if (model!=null) {
                                Type pt = 
                                        model.getTypedReference()
                                            .getFullType();
                                if (pt!=null) {
                                    occursContravariantly = 
                                            occursContravariantly || 
                                            pt.occursContravariantly(tp);
                                    occursCovariantly = 
                                            occursCovariantly || 
                                            pt.occursCovariantly(tp);
                                    occursInvariantly = 
                                            occursInvariantly || 
                                            pt.occursInvariantly(tp);
                                }
                            }
                        }
                    }
                }
                //if the parameter occurs only contravariantly 
                //in the argument list, then treat it as 'in'
                return occursContravariantly
                        && !occursCovariantly
                        && !occursInvariantly;
            }
            
            return false;
            
        }
    }
    
    private List<TypeParameter> 
    getTypeParametersAccountingForTypeConstructor(
            Declaration dec) {
        if (isGeneric(dec)) {
            Generic generic = (Generic) dec;
            return generic.getTypeParameters();
        }
        else if (dec instanceof Value) {
            Value value = (Value) dec;
            Type type = value.getType();
            if (type!=null) {
                type = type.resolveAliases();
                if (type.isTypeConstructor()) {
                    return type.getDeclaration()
                            .getTypeParameters();
                }
            }
        }
        return null;
    }

    private Reference appliedReference(
            Tree.StaticMemberOrTypeExpression smte) {
        //TODO: this might not be right for static refs
        Declaration dec = smte.getDeclaration();
        if (smte.getStaticMethodReferencePrimary()) {
            //TODO: why this special case, exactly?
            TypeDeclaration td = (TypeDeclaration) dec;
            return td.getType();
        }
        else {
            List<Type> list;
            if (isGeneric(dec)) {
                Generic generic = (Generic) dec;
                list = typeParametersAsArgList(generic);
            }
            else {
                list = NO_TYPE_ARGS;
            }
            Type qt = getQualifyingType(smte);
            return dec.appliedReference(qt, list);
        }
    }

    private static Type getQualifyingType(
            Tree.StaticMemberOrTypeExpression smte) {
        if (smte instanceof Tree.QualifiedMemberOrTypeExpression) {
            Tree.QualifiedMemberOrTypeExpression qte = 
                    (Tree.QualifiedMemberOrTypeExpression) 
                        smte;
            return qte.getPrimary().getTypeModel();
        }
        else {
            return null;
        }
    }
    
    private List<Type> constrainInferredTypes(
            List<TypeParameter> typeParameters,
            List<Type> inferredTypeArgs,
            Type qualifyingType,
            Declaration declaration) {
        int size = inferredTypeArgs.size();
        boolean found = false;
        for (int i=0; i<size; i++) {
            List<Type> bounds = 
                    typeParameters.get(i)
                        .getSatisfiedTypes();
            if (!bounds.isEmpty()) {
                found = true;
            }
        }
        if (found) {
            Reference ref;
            if (qualifyingType == null) {
                ref = declaration.appliedReference(null, 
                        inferredTypeArgs);
            }
            else {
                ref = qualifyingType.getTypedReference(
                        declaration, inferredTypeArgs);
            }
            Map<TypeParameter, Type> args =
                    ref.getTypeArguments();
            ArrayList<Type> result = 
                    new ArrayList<Type>
                        (size);
            for (int i=0; i<size; i++) {
                TypeParameter tp = typeParameters.get(i);
                Type arg = inferredTypeArgs.get(i);
                Type constrainedArg = 
                        constrainInferredType(tp, arg, args);
                result.add(constrainedArg);
            }
            return result;
        }
        else {
            return inferredTypeArgs;
        }
    }
    
    /**
     * Apply upper bound type constraints to an inferred 
     * type argument.
     *  
     * @param tp the type parameter
     * @param inferredTypeArg the inferred type argument
     * @param argMap 
     * 
     * @return the improved type argument
     */
    private Type constrainInferredType(TypeParameter tp, 
            Type inferredTypeArg,
            Map<TypeParameter, Type> argMap) {
        //Note: according to the language spec we should only 
        //      do this for contravariant  parameters, but in
        //      fact it also helps for special cases like
        //      emptyOrSingleton(null)
        //Intersect the inferred type with any upper bound 
        //constraints on the type and then substitute in all 
        //the inferred type args
        Type bounds = 
                intersectionOfSupertypes(tp)
                    .substitute(argMap, null);
        return intersectionType(bounds, inferredTypeArg, 
                unit);
    }


}
