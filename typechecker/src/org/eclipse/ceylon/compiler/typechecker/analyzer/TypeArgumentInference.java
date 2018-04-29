/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.analyzer;

import static java.util.Collections.singletonMap;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.NO_TYPE_ARGS;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getMatchingParameter;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTupleType;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getUnspecifiedParameter;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.involvesTypeParams;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.spreadType;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.addToIntersection;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.addToUnion;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.appliedType;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.canonicalIntersection;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.intersectionOfSupertypes;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.intersectionType;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isConstructor;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isTypeUnknown;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.typeArgumentsAsMap;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.typeParametersAsArgList;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.union;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.NamedArgument;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.Reference;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.SiteVariance;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeAlias;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedReference;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.model.Value;

public class TypeArgumentInference {
    
    private Unit unit;
    private int fid = 0;
    
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
                    if (!argNode.hasErrors()) {
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
                    List<Type> cts = pt.getCaseTypes();
                    List<Type> list = 
                            new ArrayList<Type>
                                (cts.size());
                    for (Type ct: cts) {
                        addToUnionOrIntersection(
                                findingUpperBounds, list, 
                                inferTypeArg(tp, 
                                        ct.substitute(pt), 
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
                        specifiedParameters(nal, parameters),
                        false);
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
                getUnspecifiedParameter(null, 
                        parameters, foundParameters);
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
                        specifiedParameters(pal, parameters),
                        false);
        List<Tree.PositionalArgument> args = 
                pal.getPositionalArguments();
        List<Type> inferredTypes = 
                new ArrayList<Type>
                    (args.size());
        List<Parameter> params = 
                parameters.getParameters();
        for (int i=0, len=params.size(); i<len; i++) {
            Parameter parameter = params.get(i);
            if (args.size()>i) {
                Tree.PositionalArgument arg = args.get(i);
                if (arg instanceof Tree.SpreadArgument) {
                    inferTypeArgFromSpreadArg(tp, 
                            receiverType, arg, i, invoked, 
                            findingUpperBounds, inferredTypes,
                            params, pal);
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
                        Type argType = arg.getTypeModel();
                        if (tp.isTypeConstructor()) {
                            //use a special approach to type
                            //arg inference, just especially
                            //designed for generic function
                            //args to generic function params
                            Type inferred =
                                    inferTypeConstructor(tp, 
                                            parameterType,
                                            argType);
                            if (inferred!=null) {
                                return inferred;
                            }
                        }
                        //if the above special approach fails,
                        //always do it the usual way, even if 
                        //it is a type constructor, since we
                        //might not have any useful generic
                        //function params
                        addToUnionOrIntersection(
                                findingUpperBounds, 
                                inferredTypes,
                                inferTypeArg(tp, 
                                        parameterType, argType,
                                        findingUpperBounds, 
                                        pal));
                    }
                }
            }
        }
        return unionOrIntersection(findingUpperBounds, 
                inferredTypes);
    }

    private Type inferTypeConstructor(TypeParameter tp, 
            Type parameterType, Type argType) {
        TypeDeclaration paramDec = 
                parameterType.getDeclaration();
        TypeDeclaration argDec = 
                argType.getDeclaration();
        List<TypeParameter> argTypeParams = 
                argDec.getTypeParameters();
        List<TypeParameter> paramTypeParams = 
                paramDec.getTypeParameters();
        if (parameterType.isTypeConstructor()
                && argType.isTypeConstructor()
                && paramTypeParams.size()
                    == argTypeParams.size()) {
            //eliminate the type aliases:
            Type pt = paramDec.getExtendedType();
            Type at = argDec.getExtendedType();
            //search for a type which could be 
            //used as a constructor to make the
            //two types equal
            return searchForMatch(tp, at, at, 
                        pt.substitute(
                            typeArgumentsAsMap(paramDec,
                                typeParametersAsArgList(argDec)), 
                            null),
                        argTypeParams);
        }
        else {
            return null;
        }
    }

    private Type createTypeConstructor(Type type, 
            List<TypeParameter> params) {
        TypeAlias ta = new TypeAlias();
        ta.setUnit(unit);
        ta.setShared(true);
        ta.setName("Inferred#"+fid++);
        ta.setAnonymous(true);
        ta.setExtendedType(type);
        ta.setTypeParameters(params);
        ta.setContainer(unit.getPackage());
        ta.setScope(unit.getPackage());
        Type result = ta.getType();
        result.setTypeConstructor(true);
        return result;
    }

    private void inferTypeArgFromSpreadArg(
            TypeParameter tp, 
            Type receiverType, 
            Tree.PositionalArgument arg,
            int i, 
            Declaration invoked, 
            boolean findingUpperBounds, 
            List<Type> inferredTypes, 
            List<Parameter> params,
            Node node) {

        int len = params.size();
        Type spreadType = 
                spreadType(arg.getTypeModel(), 
                        unit, true);
        if (!params.get(len-1).isSequenced()
                && !unit.isTupleLengthUnbounded(spreadType)) {
            //unpacking the spread argument tuple
            //into individual elements gives a 
            //better result for type inference 'cos
            //it doesn't get confused by the less
            //precise Element type arg of the Tuple
            List<Type> argTypes = 
                    unit.getTupleElementTypes(spreadType);
            for (int j=0, arglen=argTypes.size(); 
                    j<len-i && j<arglen; j++) {
                Parameter parameter = params.get(i+j);
                Type parameterType = 
                        parameterType(receiverType,
                                parameter, invoked);
                Type argType = argTypes.get(j);
                addToUnionOrIntersection(
                        findingUpperBounds, 
                        inferredTypes,
                        inferTypeArg(tp, 
                                parameterType, argType,
                                findingUpperBounds, 
                                node));
            }
        }
        else {
            //this approach is less precise 'cos it
            //gets thrown off by argument to Element
            Type parameterTypeTuple = 
                    unit.getParameterTypesAsTupleType(
                            params.subList(i, len), 
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
                            parameterTypeTuple, spreadType, 
                            findingUpperBounds, 
                            node));
        }
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
            Type fullType = 
                    parameter.getModel()
                        .getReference()
                        .getFullType();
            if (parameter.getModel().isParameterized()) {
                return createTypeConstructor(fullType, 
                        parameter.getModel().getTypeParameters());
            }
            return fullType;
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
                    (TypeDeclaration) 
                        supertypeDec);
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
                    if (parameter.isSequenced() 
                        && parameter.isAtLeastOne()) {
                        //minor hack to allow inference
                        //of type arg from possibly-empty
                        //arg passed to nonempty variadic
                        //parameter
                        Type et = unit.getElementType(pt);
                        pt = unit.getSequentialType(et);
                    }
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
     * @param receiverType 
     */
    List<Type> getInferredTypeArgsForFunctionRef(
            Tree.StaticMemberOrTypeExpression smte, 
            Type receiverType, boolean secondList) {
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
                
                //if both the parameter and the argument
                //have declared parameters, we can use a
                //better algorithm
                if (!smte.getStaticMethodReferencePrimary() 
                        && reference instanceof Functional 
                        && paramDec instanceof Functional 
                        && paramTypedRef!=null) {
                    //when we have actual individualized
                    //parameters on both the given function
                    //ref, and the callable parameter to
                    //which it is assigned, we use a more
                    //forgiving algorithm that doesn't throw
                    //away all constraints just because one
                    //constraint involves unknown type 
                    //parameters of the declaration with the
                    //callable parameter
                    return inferFunctionRefTypeArgs(smte, 
                            receiverType, secondList, 
                            reference, typeParameters,
                            paramTypedRef, paramDec, 
                            parameterizedDec);
                }
                else {
                    //use a worse algorithm that throws away
                    //too many constraints (I guess we should
                    //improve this, including in the spec)
                    if (unit.isSequentialType(paramType)) {
                        paramType = 
                                unit.getSequentialElementType(
                                        paramType);
                    }
                    
                    if (unit.isCallableType(paramType)) {
                        //the parameter has type Callable
                        return inferFunctionRefTypeArgs(
                                smte, receiverType, secondList, 
                                reference, typeParameters,
                                paramType, parameterizedDec);
                    }
                    else if (secondList) {
                        //the parameter doesn't have type 
                        //Callable, but it's a nullary
                        //function, so we can't use its 
                        //arguments to infer its type
                        //arguments (NOT BLESSED BY SPEC!)
                        return inferNullaryFunctionCallTypeArgs(
                                smte, receiverType, reference, 
                                typeParameters, paramType, 
                                parameterizedDec);
                    }
                    else {
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

    private Type searchForMatch(TypeParameter tp, 
            Type t, Type at, Type pt, 
            List<TypeParameter> typeParams) {
        Type tc = createTypeConstructor(t, typeParams);
        if (pt.substitute(singletonMap(tp, tc), null)
                .isSupertypeOf(at)) {
            return tc;
        }
        if (t.isTuple()) {
            for (Type te: unit.getTupleElementTypes(t)) {
                Type r = searchForMatch(tp, te, at, pt, typeParams);
                if (r!=null) return r;
            }
        }
        else {
            for (Type ta: t.getTypeArgumentList()) {
                Type r = searchForMatch(tp, ta, at, pt, typeParams);
                if (r!=null) return r;
            }
        }
        return null;
    }

    /**
     * Infer type arguments for a direct function 
     * ref (i.e. not a value ref with a type 
     * constructor type) that occurs as an argument 
     * to a callable parameter.
     */
    private List<Type> inferFunctionRefTypeArgs(
            Tree.StaticMemberOrTypeExpression smte, 
            Type receiverType, boolean secondList, 
            Declaration reference, 
            List<TypeParameter> typeParameters, 
            TypedReference paramTypedRef,
            Declaration paramDec, 
            Declaration parameterizedDec) {
        
        Reference arg = appliedReference(smte);
        
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
            ParameterList aplf = apls.get(secondList?1:0);
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
                                specifiedParams, 
                                secondList);
                Type it = 
                        inferFunctionRefTypeArg(
                                smte, 
                                tp,
                                typeParameters, 
                                paramTypedRef, 
                                parameterizedDec, 
                                arg, apl, ppl,
                                findUpperBounds);
                inferredTypes.add(it);
            }
            return constrainInferredTypes(
                    typeParameters, inferredTypes, 
                    receiverType, reference);
        }
    }

    /**
     * Infer type arguments for the invocation of a
     * nullary function that occurs as an argument.
     * The parameter to which it is an argument isn't 
     * a callable parameter. 
     */
    private List<Type> inferNullaryFunctionCallTypeArgs(
            Tree.StaticMemberOrTypeExpression smte, 
            Type receiverType, Declaration reference, 
            List<TypeParameter> typeParameters, 
            Type paramType, Declaration parameterizedDec) {
        
        Reference arg = appliedReference(smte);
        
        List<Type> inferredTypes = 
                new ArrayList<Type>
                    (typeParameters.size());
        Type argType = arg.getType();
        TypeDeclaration ptd = 
                paramType.getDeclaration();
        Type template = 
                //TODO: This is a bit crap.
                //The rest of this class assumes
                //that we're constraining supertype
                //type params by subtype type args, 
                //but here we're constraining a 
                //subtype type param by a supertype
                //type arg. Should I iterate over
                //all supertypes of argType?
                ptd instanceof ClassOrInterface ?
                        argType.getSupertype(ptd) :
                        argType;
        for (TypeParameter tp: typeParameters) {
            boolean covariant = 
                    template.occursCovariantly(tp)
                    && !template.occursContravariantly(tp);
            boolean contravariant = 
                    template.occursContravariantly(tp)
                    && !template.occursCovariantly(tp);
            
            Type it = inferNullaryFunctionCallTypeArg(
                        smte, 
                        tp, 
                        paramType, 
                        parameterizedDec, 
                        template, 
                        covariant, 
                        contravariant);
            inferredTypes.add(it);
        }
        return constrainInferredTypes(
                typeParameters, inferredTypes, 
                receiverType, reference);
    }

    private Type inferNullaryFunctionCallTypeArg(
            Tree.StaticMemberOrTypeExpression smte, 
            TypeParameter tp,
            Type paramType, 
            Declaration parameterizedDec, 
            Type template, 
            boolean covariant, 
            boolean contravariant) {
//        if (covariant) {
//            it = unit.getNothingType();
//        }
//        else if (contravariant) {
//            it = intersectionOfSupertypes(tp);
//        }
//        else {
        List<Type> list = new ArrayList<Type>(1);
        Type rt = inferTypeArg(tp, 
                    template, paramType, 
                    !covariant, !contravariant, 
                    false, 
                    new ArrayList<TypeParameter>(), 
                    smte);
        if (!isTypeUnknown(rt)
                && !involvesTypeParams(parameterizedDec, rt)) {
            addToUnionOrIntersection(contravariant, list, rt);
        }
        return unionOrIntersection(contravariant, list);
//        }
    }

    /**
     * Infer the type arguments for a reference to a 
     * generic function that occurs as a parameter in
     * an invocation. This implementation is used when
     * have an indirect ref whose type is a type
     * constructor. This version is also used for
     * static references.
     */
    private List<Type> inferFunctionRefTypeArgs(
            Tree.StaticMemberOrTypeExpression smte, 
            Type receiverType,
            boolean secondList, Declaration reference, 
            List<TypeParameter> typeParameters, 
            Type paramType, Declaration parameterizedDec) {
        
        Reference arg = appliedReference(smte);
        
        //this is the type of the parameter list
        //of the function ref itself, which 
        //involves the type parameters we are
        //trying to infer
        Type parameterListType;
        Type fullType;
        Type argType; 
        if (smte.getStaticMethodReferencePrimary()) {
            argType = arg.getType();
            parameterListType = appliedType(
                    unit.getTupleDeclaration(), 
                    argType, argType, 
                    unit.getEmptyType());
            fullType = appliedType(
                    unit.getCallableDeclaration(),
                    argType, parameterListType);
        }
        else {
            fullType = arg.getFullType();
            if (secondList) {
                fullType = 
                        unit.getCallableReturnType(
                                fullType);
            }
            parameterListType = 
                    unit.getCallableTuple(fullType);
            argType =
                    unit.getCallableReturnType(fullType);
        }
        
        //this is the type of the parameter list
        //of the callable parameter that the 
        //function ref is being passed to (these
        //parameters are going to be assigned to
        //the parameters of the function ref)
        Type argumentListType = 
                unit.getCallableTuple(paramType);
        Type returnType =
                unit.getCallableReturnType(paramType);
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
            Type type = inferFunctionRefTypeArg(
                            smte, 
                            tp, 
                            parameterizedDec, 
                            parameterListType, 
                            argumentListType,
                            argType, 
                            returnType, 
                            findUpperBounds);
            inferredTypes.add(type);
        }
        
        return constrainInferredTypes(
                typeParameters, inferredTypes, 
                receiverType, reference);
    }

    private Type inferFunctionRefTypeArg(
            Tree.StaticMemberOrTypeExpression smte, 
            TypeParameter tp,
            Declaration parameterizedDec, 
            Type parameterListType, 
            Type argumentListType, 
            Type argType, 
            Type returnType,
            boolean findUpperBounds) {
        List<Type> list = 
                new ArrayList<Type>(2);
        
        //the parameter types tuple
        Type it = inferTypeArg(tp, 
                        parameterListType, 
                        argumentListType, 
                        true, false, 
                        findUpperBounds, 
                        new ArrayList<TypeParameter>(), 
                        smte);
        if (!isTypeUnknown(it)
                && !involvesTypeParams(parameterizedDec, it)) {
            addToUnionOrIntersection(findUpperBounds, list, it);
        }
        else {
            //there's a type parameter in there somewhere, so
            //try splitting the tuples up into individual
            //elements TODO: this is a hack, it would be better
            //to somehow ignore type parameters in inferTypeArg()
            List<Type> paramTypes = 
                    unit.getTupleElementTypes(parameterListType);
            List<Type> argTypes = 
                    unit.getTupleElementTypes(argumentListType);
            for (int i=0; 
                    i<paramTypes.size() && i<argTypes.size(); 
                    i++) {
                //the parameter type
                Type ipt = inferTypeArg(tp, 
                                paramTypes.get(i), 
                                argTypes.get(i), 
                                true, false, 
                                findUpperBounds, 
                                new ArrayList<TypeParameter>(), 
                                smte);
                if (!isTypeUnknown(ipt)
                        && !involvesTypeParams(parameterizedDec, ipt)) {
                    addToUnionOrIntersection(findUpperBounds, list, ipt);
                }
            }
        }
        
        //the return type
        Type rt = inferTypeArg(tp, 
                    argType, returnType, 
                    false, true, 
                    findUpperBounds, 
                    new ArrayList<TypeParameter>(), 
                    smte);
        if (!isTypeUnknown(rt)
                && !involvesTypeParams(parameterizedDec, rt)) {
            addToUnionOrIntersection(findUpperBounds, list, rt);
        }
        return unionOrIntersection(findUpperBounds, list);
    }

    private static boolean isArgumentToGenericParameter(
            TypedReference paramTypedRef, Type paramType) {
        return paramType.resolveAliases().isTypeConstructor()
            || paramTypedRef != null
            && paramTypedRef.getDeclaration().isParameterized();
    }

    /**
     * Infer the type arguments for a reference to a 
     * generic function that occurs as a parameter in
     * an invocation. This implementation is used when 
     * have a direct ref to the actual declaration.
     */
    private Type inferFunctionRefTypeArg(
            Tree.StaticMemberOrTypeExpression smte,
            TypeParameter tp, 
            List<TypeParameter> typeParams, 
            TypedReference param, 
            Declaration pd, 
            Reference arg, 
            List<Parameter> apl,
            List<Parameter> ppl, 
            boolean findingUpperBounds) {
        
        List<Type> list = 
                new ArrayList<Type>();
        
        //first look at the parameter types
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
        
        //now look at the return type
        Type it = 
                inferTypeArg(tp, 
                        arg.getType(), 
                        param.getType(), 
                        false, true,
                        findingUpperBounds, 
                        new ArrayList<TypeParameter>(),
                        smte);
        if (!(isTypeUnknown(it) ||
//              it.involvesTypeParameters(typeParams) ||
              involvesTypeParams(pd, it))) {
          addToUnionOrIntersection(findingUpperBounds, 
                  list, it);
        }
        
        return unionOrIntersection(findingUpperBounds, 
                list);
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
            //TODO: apply type constraints!!
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
            TypeDeclaration type, Type receiverType,
            Tree.MemberOrTypeExpression primary) {
        Tree.PositionalArgumentList pal = 
                that.getPositionalArgumentList();
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
            Declaration generic, Type receiverType) {
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
        List<Parameter> params = 
                parameters.getParameters();
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
                                occursContravariantly 
                                || pt.occursContravariantly(tp);
                        occursCovariantly = 
                                occursCovariantly 
                                || pt.occursCovariantly(tp);
                        occursInvariantly = 
                                occursInvariantly 
                                || pt.occursInvariantly(tp);
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
            boolean[] specifiedArguments,
            boolean secondList) {
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
                            paramLists.get(secondList?1:0)
                                .getParameters();
                    for (int i=0; i<specifiedArguments.length; i++) {
                        //ignore parameters with no argument
                        if (specifiedArguments[i]) {
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
        if (dec==null) {
            return null;
        }
        else if (dec.isParameterized()) {
            return dec.getTypeParameters();
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
            if (dec.isParameterized()) {
                list = typeParametersAsArgList(dec);
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
            TypeParameter tp = typeParameters.get(i);
//            if (!tp.isCovariant()) {
                List<Type> bounds = 
                        tp.getSatisfiedTypes();
                if (!bounds.isEmpty()) {
                    found = true;
                }
//            }
        }
        
        if (found) {
            
            Reference ref;
            if (declaration instanceof Value) {
                Value value = (Value) declaration;
                if (value.getType()
                         .isTypeConstructor()) {
                    if (qualifyingType == null) {
                        ref = declaration.appliedReference(null, 
                                NO_TYPE_ARGS);
                    }
                    else {
                        ref = qualifyingType.getTypedReference(
                                declaration, NO_TYPE_ARGS);
                    }
                    TypeDeclaration dec = 
                            ref.getType()
                               .getDeclaration();
                    ref = dec.appliedReference(null, 
                            inferredTypeArgs);
                }
                else {
                    return inferredTypeArgs;
                }
            }
            else {
                if (qualifyingType == null) {
                    ref = declaration.appliedReference(null, 
                            inferredTypeArgs);
                }
                else {
                    ref = qualifyingType.getTypedReference(
                            declaration, inferredTypeArgs);
                }
            }
            
            Map<TypeParameter,Type> args = 
                    ref.collectTypeArguments();
            
            ArrayList<Type> result = 
                    new ArrayList<Type>
                        (size);
            for (int i=0; i<size; i++) {
                TypeParameter tp = typeParameters.get(i);
                Type arg = inferredTypeArgs.get(i);
                Type constrainedArg = 
//                        tp.isCovariant() ? arg :
                            constrainInferredType(
                                    tp, arg, args);
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
