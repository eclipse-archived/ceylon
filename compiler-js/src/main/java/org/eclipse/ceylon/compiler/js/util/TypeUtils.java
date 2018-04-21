/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.util.NativeUtil;
import org.eclipse.ceylon.model.typechecker.model.Annotation;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Generic;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.NothingType;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.SiteVariance;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeAlias;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.model.UnknownType;
import org.eclipse.ceylon.model.typechecker.model.Value;

import org.eclipse.ceylon.compiler.js.AttributeGenerator;
import org.eclipse.ceylon.compiler.js.GenerateJsVisitor;
import org.eclipse.ceylon.compiler.js.loader.MetamodelGenerator;

/** A convenience class to help with the handling of certain type declarations. */
public class TypeUtils {

    /** Prints the type arguments, usually for their reification. */
    public static void printTypeArguments(final Node node, final Map<TypeParameter,Type> targs,
            final GenerateJsVisitor gen, final boolean skipSelfDecl,
            final Map<TypeParameter, SiteVariance> overrides) {
        if (targs==null) return;
        gen.out("{");
        boolean first = true;
        for (Map.Entry<TypeParameter,Type> e : targs.entrySet()) {
            if (first) {
                first = false;
            } else {
                gen.out(",");
            }
            gen.out(gen.getNames().typeParameterName(e.getKey()), ":");
            final Type pt = e.getValue() == null ? null : e.getValue().resolveAliases();
            if (pt == null) {
                gen.out("'", e.getKey().getName(), "'");
            } else if (!outputTypeList(node, pt, gen, skipSelfDecl)) {
                boolean hasParams = pt.getTypeArgumentList() != null && !pt.getTypeArgumentList().isEmpty();
                boolean closeBracket = false;
                final TypeDeclaration d = pt.getDeclaration();
                if (pt.isTypeParameter()) {
                    resolveTypeParameter(node, (TypeParameter)d, gen, skipSelfDecl);
//                    if (((TypeParameter)d).isInvariant() &&
//                            (e.getKey().isCovariant() || e.getKey().isContravariant())) {
//                        gen.out("/*Warning:",d.getQualifiedNameString()," inv but ",
//                                e.getKey().getQualifiedNameString(),
//                                e.getKey().isCovariant()?" out":" in", "*/");
//                    }
                } else {
                    closeBracket = !pt.isTypeAlias();
                    if (closeBracket)gen.out("{t:");
                    outputQualifiedTypename(node,
                            node != null && gen.isImported(node.getUnit().getPackage(), pt.getDeclaration()),
                            pt, gen, skipSelfDecl);
                }
                if (hasParams) {
                    gen.out(",a:");
                    printTypeArguments(node, pt.getTypeArguments(), gen, skipSelfDecl,
                            pt.getVarianceOverrides());
                }
                SiteVariance siteVariance = overrides == null ? null : overrides.get(e.getKey());
                printSiteVariance(siteVariance, gen);
                if (closeBracket) {
                    gen.out("}");
                }
            }
        }
        gen.out("}");
    }

    public static void outputQualifiedTypename(final Node node, final boolean imported, final Type pt,
            final GenerateJsVisitor gen, final boolean skipSelfDecl) {
        TypeDeclaration t = pt.getDeclaration();
        if (t instanceof NothingType) {
            //Hack in the model means hack here as well
            gen.out(gen.getClAlias(), "Nothing");
        } else if (t.isNull()) {
            gen.out(gen.getClAlias(), "Null");
        } else if (t.isAnything()) {
            gen.out(gen.getClAlias(), "Anything");
        } else if (ModelUtil.isTypeUnknown(pt)) {
            if (!gen.isInDynamicBlock()) {
                gen.out("/*WARNING unknown type");
                gen.location(node);
                gen.out("*/");
            }
            gen.out(gen.getClAlias(), "Anything");
        } else {
            if (t.isValueConstructor()) {
                t = (TypeDeclaration)t.getContainer();
            }
            gen.out(qualifiedTypeContainer(node, imported, t, gen));
            boolean isAnonCallable = t.isAnonymous() 
                    && t.getExtendedType() != null 
                    && t.getExtendedType().isCallable();
            boolean _init = !imported && pt.getDeclaration().isDynamic() || t.isAnonymous();
            if (_init && !pt.getDeclaration().isToplevel()) {
                Declaration dynintc = ModelUtil.getContainingClassOrInterface(node.getScope());
                if (dynintc == null 
                        || !(dynintc instanceof Scope) 
                        || !ModelUtil.contains((Scope)dynintc, pt.getDeclaration())) {
                    _init=false;
                }
            }
            if (_init && !isAnonCallable) {
                gen.out("$i$");
            }

            if (!outputTypeList(null, pt, gen, skipSelfDecl)) {
                if (isAnonCallable) {
                    gen.out("{t:");
                    outputQualifiedTypename(node, true, pt.getExtendedType(), gen, skipSelfDecl);
                    gen.out("}");
                    return;
                } else if (t.isAnonymous()) {
                    gen.out(gen.getNames().objectName(t));
                } else {
                    gen.out(gen.getNames().name(t));
                }
            }
            if (_init && !(t.isAnonymous() && t.isToplevel())) {
                gen.out("()");
            }
        }
    }

    static String qualifiedTypeContainer(final Node node, final boolean imported, final TypeDeclaration t,
            final GenerateJsVisitor gen) {
        final String modAlias = imported ? gen.getNames().moduleAlias(t.getUnit().getPackage().getModule()) : null;
        final StringBuilder sb = new StringBuilder();
        if (modAlias != null && !modAlias.isEmpty()) {
            sb.append(modAlias).append('.');
        }
        if (t.getContainer() instanceof ClassOrInterface) {
            final Scope scope = node == null ? null : ModelUtil.getContainingClassOrInterface(node.getScope());
            ClassOrInterface parent = (ClassOrInterface)t.getContainer();
            final List<ClassOrInterface> parents = new ArrayList<>(3);
            parents.add(0, parent);
            while (parent != scope && parent.isClassOrInterfaceMember()) {
                parent = (ClassOrInterface)parent.getContainer();
                parents.add(0, parent);
            }
            boolean first = true;
            for (ClassOrInterface p : parents) {
                if (p==scope) {
                    if (gen.opts.isOptimize()) {
                        sb.append(gen.getNames().self(p)).append('.');
                    }
                } else {
                    if (!first) {
                        if (p.isStatic()) {
                            sb.append("$st$.");
                        } else if (p.getContainer() != scope 
                                && p.isClassOrInterfaceMember() 
                                && gen.opts.isOptimize()) {
                            sb.append("$$.prototype.");
                        }
                    }
                    sb.append(gen.getNames().name(p)).append('.');
                }
                first = false;
            }
            if (t.isStatic()) {
                sb.append("$st$.");
            } else if (t.getContainer() != scope 
                    && t.isClassOrInterfaceMember() 
                    && gen.opts.isOptimize()) {
                sb.append("$$.prototype.");
            }
        }
        return sb.toString();
    }

    /** Prints out an object with a type constructor under the property "t" and its type arguments under
     * the property "a", or a union/intersection type with "u" or "i" under property "t" and the list
     * of types that compose it in an array under the property "l", or a type parameter as a reference to
     * already existing params. */
    public static void typeNameOrList(final Node node, final Type pt,
            final GenerateJsVisitor gen, final boolean skipSelfDecl) {
        TypeDeclaration type = pt.getDeclaration();
        if (!outputTypeList(node, pt, gen, skipSelfDecl)) {
            if (pt.isTypeParameter()) {
                resolveTypeParameter(node, (TypeParameter)type, gen, skipSelfDecl);
            } else if (pt.isTypeAlias()) {
                outputQualifiedTypename(node, node != null && gen.isImported(node.getUnit().getPackage(), type),
                        pt, gen, skipSelfDecl);
            } else {
                gen.out("{t:");
                outputQualifiedTypename(node, node != null && gen.isImported(node.getUnit().getPackage(), type),
                        pt, gen, skipSelfDecl);
                if (!pt.getTypeArgumentList().isEmpty()) {
                    final Map<TypeParameter,Type> targs;
                    if (pt.getDeclaration().isToplevel()) {
                        targs = pt.getTypeArguments();
                    } else {
                        //Gather all type parameters from containers
                        Scope scope = node.getScope();
                        final HashSet<TypeParameter> parenttp = new HashSet<>();
                        while (scope != null) {
                            if (scope instanceof Generic) {
                                Generic g = (Generic) scope;
                                for (TypeParameter tp : g.getTypeParameters()) {
                                    parenttp.add(tp);
                                }
                            }
                            scope = scope.getScope();
                        }
                        targs = new HashMap<>();
                        targs.putAll(pt.getTypeArguments());
                        Declaration cd = ModelUtil.getContainingDeclaration(pt.getDeclaration());
                        while (cd != null) {
                            for (TypeParameter tp : cd.getTypeParameters()) {
                                if (parenttp.contains(tp)) {
                                    targs.put(tp, tp.getType());
                                }
                            }
                            cd = ModelUtil.getContainingDeclaration(cd);
                        }
                    }
                    gen.out(",a:");
                    printTypeArguments(node, targs, gen, skipSelfDecl, pt.getVarianceOverrides());
                }
                gen.out("}");
            }
        }
    }

    /** Appends an object with the type's type and list of union/intersection types. */
    public static boolean outputTypeList(final Node node, final Type pt,
            final GenerateJsVisitor gen, final boolean skipSelfDecl) {
        final List<Type> subs;
        int seq=0;
        if (pt.isIntersection()) {
            gen.out(gen.getClAlias(), "mit$([");
            subs = pt.getSatisfiedTypes();
        } else if (pt.isUnion()) {
            gen.out(gen.getClAlias(), "mut$([");
            subs = pt.getCaseTypes();
        }
        else if (pt.isTuple()) {
            TypeDeclaration d = pt.getDeclaration();
            subs = d.getUnit().getTupleElementTypes(pt);
            final Type lastType = subs.get(subs.size()-1);
            if (pt.involvesTypeParameters() && !d.getUnit().isHomogeneousTuple(pt)) {
                //Revert to outputting normal Tuple with its type arguments
                gen.out("{t:", gen.getClAlias(), "Tuple,a:");
                printTypeArguments(node, pt.getTypeArguments(), gen, skipSelfDecl, pt.getVarianceOverrides());
                gen.out("}");
                return true;
            }
            final int tupleMinLength = pt.getDeclaration().getUnit().getTupleMinimumLength(pt);
            if (!lastType.isEmpty()) {
                if (lastType.isSequential()) {
                    seq = 1;
                }
                if (lastType.isSequence()) {
                    seq = 2;
                }
            }
            if (seq > 0) {
                //Non-empty, non-tuple tail; union it with its type parameter
                Type utail = ModelUtil.unionType(lastType.getTypeArgumentList().get(0), lastType, d.getUnit());
                subs.remove(subs.size()-1);
                subs.add(utail);
            }
            gen.out(gen.getClAlias(), "mtt$([");
            if (tupleMinLength < (subs.size() - (seq>0?1:0))) {
                int limit = subs.size();
                if (seq > 0) {
                    limit--;
                }
                for (int i = tupleMinLength; i < limit; i++) {
                    subs.set(i, ModelUtil.unionType(d.getUnit().getEmptyType(), subs.get(i), node.getUnit()));
                }
            }
        } else {
            return false;
        }
        final Type lastSub = subs.isEmpty() ? null : subs.get(subs.size()-1);
        int index = 0;
        for (Type t : subs) {
            if (index>0) gen.out(",");
            if (t==lastSub && seq>0 && t.getCaseTypes() != null) {
                //The non-empty, non-tuple tail
                gen.out("{t:'u',l:[");
                typeNameOrList(node, t.getCaseTypes().get(0), gen, skipSelfDecl);
                gen.out(",");
                typeNameOrList(node, t.getCaseTypes().get(1), gen, skipSelfDecl);
                gen.out("],seq:", Integer.toString(seq), "}");
            } else {
                typeNameOrList(node, t, gen, skipSelfDecl);
            }
            index++;
        }
        gen.out("])");
        return true;
    }

    /** Finds the owner of the type parameter and outputs a reference to the corresponding type argument. */
    static void resolveTypeParameter(final Node node, final TypeParameter tp,
            final GenerateJsVisitor gen, final boolean skipSelfDecl) {
        Scope parent = ModelUtil.getRealScope(node.getScope());
        int outers = 0;
        while (parent != null && parent != tp.getContainer()) {
            if (parent instanceof TypeDeclaration &&
                    !(parent instanceof Constructor || ((TypeDeclaration) parent).isAnonymous())) {
                outers++;
            }
            parent = parent.getScope();
        }
        if (tp.getContainer() instanceof ClassOrInterface) {
            if (parent == tp.getContainer()) {
                if (!skipSelfDecl) {
                    TypeDeclaration ontoy = ModelUtil.getContainingClassOrInterface(node.getScope());
                    while (ontoy.isAnonymous())ontoy=ModelUtil.getContainingClassOrInterface(ontoy.getScope());
                    gen.out(gen.getNames().self(ontoy));
                    if (ontoy == parent)outers--;
                    for (int i = 0; i < outers; i++) {
                        gen.out(".outer$");
                    }
                    gen.out(".");
                }
                gen.out("$a$.", gen.getNames().typeParameterName(tp));
            } else {
                //This can happen in expressions such as Singleton(n) when n is dynamic
                gen.out("{/*NO PARENT*/t:", gen.getClAlias(), "Anything}");
            }
        } else if (tp.getContainer() instanceof TypeAlias) {
            if (parent == tp.getContainer()) {
                gen.out("'", gen.getNames().typeParameterName(tp), "'");
            } else {
                //This can happen in expressions such as Singleton(n) when n is dynamic
                gen.out("{/*NO PARENT ALIAS*/t:", gen.getClAlias(), "Anything}");
            }
        } else {
            //it has to be a method, right?
            //We need to find the index of the parameter where the argument occurs
            //...and it could be null...
            Type type = null;
            for (Iterator<ParameterList> iter0 = ((Function)tp.getContainer()).getParameterLists().iterator();
                    type == null && iter0.hasNext();) {
                for (Iterator<Parameter> iter1 = iter0.next().getParameters().iterator();
                        iter1.hasNext();) {
                    type = typeContainsTypeParameter(iter1.next().getType(), tp);
                }
            }
            //The Type that we find corresponds to a parameter, whose type can be:
            //A type parameter in the method, in which case we just use the argument's type (may be null)
            //A component of a union/intersection type, in which case we just use the argument's type (may be null)
            //A type argument of the argument's type, in which case we must get the reified generic from the argument
            if (tp.getContainer() == parent) {
                gen.out(gen.getNames().typeArgsParamName((Function)tp.getContainer()), ".",
                        gen.getNames().typeParameterName(tp));
            } else {
                if (parent == null && node instanceof Tree.StaticMemberOrTypeExpression) {
                    if (tp.getContainer() == ((Tree.StaticMemberOrTypeExpression)node).getDeclaration()) {
                        type = ((Tree.StaticMemberOrTypeExpression)node).getTarget().getTypeArguments().get(tp);
                        typeNameOrList(node, type, gen, skipSelfDecl);
                        return;
                    }
                }
                gen.out("'", gen.getNames().typeParameterName(tp), "'");
                //gen.out.spitOut("Passing reference to " + tp.getQualifiedNameString() + " as a string...");
                //gen.out("/*Please report this to the ceylon-js team: METHOD TYPEPARM plist ",
                //Integer.toString(plistCount), "#", tp.getName(), "*/'", type.getProducedTypeQualifiedName(),
                //"' container " + tp.getContainer() + " at " + node);
            }
        }
    }

    static Type typeContainsTypeParameter(Type td, TypeParameter tp) {
        if (td.isUnion() || td.isIntersection()) {
            List<Type> comps = td.getCaseTypes();
            if (comps == null) comps = td.getSatisfiedTypes();
            for (Type sub : comps) {
                td = typeContainsTypeParameter(sub, tp);
                if (td != null) {
                    return td;
                }
            }
        }
        else {
            TypeDeclaration d = td.getDeclaration();
            if (d == tp) {
                return td;
            } else if (d instanceof ClassOrInterface) {
                for (Type sub : td.getTypeArgumentList()) {
                    if (typeContainsTypeParameter(sub, tp) != null) {
                        return td;
                    }
                }
            }
        }
        return null;
    }

    /** Find the type with the specified declaration among the specified type's supertypes,
     * case types, satisfied types, etc. */
    public static Type findSupertype(TypeDeclaration d, Type pt) {
        if (pt.getDeclaration().equals(d)) {
            return pt;
        }
        List<Type> list = pt.getSupertypes() == null ? pt.getCaseTypes() : pt.getSupertypes();
        for (Type t : list) {
            if (t.getDeclaration().equals(d)) {
                return t;
            }
        }
        return null;
    }

    public static List<Type> getDefaultTypeArguments(List<TypeParameter> tparms) {
        final ArrayList<Type> targs = new ArrayList<>(tparms.size());
        for (TypeParameter tp : tparms) {
            Type t = tp.getDefaultTypeArgument();
            if (t == null) {
                t = tp.getUnit().getAnythingType();
            }
            targs.add(t);
        }
        return targs;
    }

    public static Map<TypeParameter, Type> matchTypeParametersWithArguments(
            List<TypeParameter> params, List<Type> targs) {
        if (params != null) {
            if (targs == null) {
                targs = getDefaultTypeArguments(params);
            }
            if (params.size() == targs.size()) {
                HashMap<TypeParameter, Type> r = new HashMap<TypeParameter, Type>();
                for (int i = 0; i < targs.size(); i++) {
                    r.put(params.get(i), targs.get(i));
                }
                return r;
            }
        }
        return null;
    }

    public static Map<TypeParameter, Type> wrapAsIterableArguments(Type pt) {
        HashMap<TypeParameter, Type> r = new HashMap<TypeParameter, Type>();
        final TypeDeclaration iterable = pt.getDeclaration().getUnit().getIterableDeclaration();
        List<TypeParameter> typeParameters = iterable.getTypeParameters();
        r.put(typeParameters.get(0), pt);
        r.put(typeParameters.get(1), pt.getDeclaration().getUnit().getNullType());
        return r;
    }

    public static boolean isUnknown(Declaration d) {
        return d == null || d instanceof UnknownType;
    }

    public static void spreadArrayCheck(final Tree.Term term, final GenerateJsVisitor gen) {
        String tmp = gen.getNames().createTempVariable();
        gen.out("(", tmp, "=");
        term.visit(gen);
        gen.out(",Array.isArray(", tmp, ")?", tmp);
        gen.out(":function(){throw new TypeError('Expected JS Array (",
                term.getUnit().getFilename(), " ", term.getLocation(), ")')}())");
    }

    public static Type extractDynamic(Type t) {
        if (t != null) {
            if (t.isUnion()) {
                for (Type ct : t.getCaseTypes()) {
                    ct = extractDynamic(ct);
                    if (ct != null) {
                        return ct;
                    }
                }
            } else if (t.isIntersection()) {
                for (Type st : t.getSatisfiedTypes()) {
                    st = extractDynamic(st);
                    if (st != null) {
                        return st;
                    }
                }
            } else if (t.getDeclaration() != null && t.getDeclaration().isDynamic()) {
                return t;
            }
        }
        return null;
    }

    /** Generates the code to throw an Exception if a dynamic object is not of the specified type. */
    public static void generateDynamicCheck(final Tree.Term term, Type t,
            final GenerateJsVisitor gen, final boolean skipSelfDecl,
            final Map<TypeParameter,Type> typeArguments) {
        Type dyntype = extractDynamic(t);
        if (dyntype != null) {
            gen.out(gen.getClAlias(), "dre$$(");
            term.visit(gen);
            gen.out(",");
            TypeUtils.typeNameOrList(term, dyntype, gen, skipSelfDecl);
            gen.out(",'", term.getUnit().getFilename(), " ", term.getLocation(), "')");
        } else {
            if (t.isFloat() || t.isInteger()) {
                //Check that it's a number or a Float or an Integer
                gen.out(gen.getClAlias(), "ndnc$(");
                term.visit(gen);
                gen.out(",'", t.isFloat() ? "f" : "i", "','",
                        term.getUnit().getFilename(), " ", term.getLocation(), "')");
            } else if (t.getDeclaration() != null &&
                    t.getDeclaration().isArray()) {
                gen.out(gen.getClAlias(), "natc$(");
                term.visit(gen);
                gen.out(",");
                t = t.getTypeArgumentList().get(0);
                if (t.isTypeParameter() && typeArguments != null
                        && typeArguments.containsKey(t.getDeclaration())) {
                    t = typeArguments.get(t.getDeclaration());
                }
                TypeUtils.typeNameOrList(term, t, gen, skipSelfDecl);
                gen.out(",'", term.getUnit().getFilename(), " ", term.getLocation(), "')");
            } else {
                gen.out(gen.getClAlias(), "ndtc$(");
                term.visit(gen);
                gen.out(",");
                if (t.isTypeParameter() && typeArguments != null
                        && typeArguments.containsKey(t.getDeclaration())) {
                    t = typeArguments.get(t.getDeclaration());
                }
                TypeUtils.typeNameOrList(term, t, gen, skipSelfDecl);
                gen.out(",'", term.getUnit().getFilename(), " ", term.getLocation(), "')");
            }
        }
    }

    public static void encodeParameterListForRuntime(final boolean resolveTargs,
            final Node n, final ParameterList plist, final GenerateJsVisitor gen) {
        boolean first = true;
        gen.out("[");
        Map<String,Tree.Parameter> treeParams = new HashMap<>();
        //Map of the parameters in the tree
        if (n instanceof Tree.AnyMethod) {
            for (Tree.ParameterList tplist : ((Tree.AnyMethod)n).getParameterLists()) {
                for (Tree.Parameter tparm : tplist.getParameters()) {
                    if (tparm.getParameterModel() != null && tparm.getParameterModel().getName() != null) {
                        treeParams.put(tparm.getParameterModel().getName(), tparm);
                    }
                }
            }
        }
        for (Parameter p : plist.getParameters()) {
            if (first) first=false; else gen.out(",");
            gen.out("{", MetamodelGenerator.KEY_NAME, ":'", p.getName(), "',");
            gen.out(MetamodelGenerator.KEY_METATYPE, ":'", MetamodelGenerator.METATYPE_PARAMETER, "',");
            Type ptype = p.getType();
            if (p.getModel() instanceof Function) {
                gen.out("$pt:'f',");
                ptype = ((Function)p.getModel()).getTypedReference().getFullType();
            }
            if (p.isSequenced()) {
                if (p.isAtLeastOne()) {
                    gen.out("seq:2,");
                } else {
                    gen.out("seq:1,");
                }
            }
            if (p.isDefaulted()) {
                gen.out(MetamodelGenerator.KEY_DEFAULT, ":1,");
            }
            gen.out(MetamodelGenerator.KEY_TYPE, ":");
            metamodelTypeNameOrList(resolveTargs, n, gen.getCurrentPackage(), ptype, null, gen);
            if(p.getModel() instanceof Function){
                gen.out(",", MetamodelGenerator.KEY_RETURN_TYPE, ":");
                metamodelTypeNameOrList(resolveTargs, n, gen.getCurrentPackage(), p.getType(), null, gen);
                gen.out(",", MetamodelGenerator.KEY_PARAMS, ":");
                encodeParameterListForRuntime(resolveTargs, n, ((Function)p.getModel()).getFirstParameterList(), gen);
            }
            Tree.Parameter tparm = p.getName() == null ? null : treeParams.get(p.getName());
            if (tparm == null) {
                if (p.getModel().getAnnotations() != null && !p.getModel().getAnnotations().isEmpty()) {
                    new ModelAnnotationGenerator(gen, p.getModel(), n).generateAnnotations();
                }
            } else if (tparm instanceof Tree.ParameterDeclaration) {
                Tree.TypedDeclaration tdec = ((Tree.ParameterDeclaration)tparm).getTypedDeclaration();
                if (tdec.getAnnotationList() != null && !tdec.getAnnotationList().getAnnotations().isEmpty()) {
                    outputAnnotationsFunction(tdec.getAnnotationList(), p.getDeclaration(), gen);
                }
            }
            gen.out("}");
        }
        gen.out("]");
    }

    private static Unit getUnit(Type pt) {
        if (pt.isClassOrInterface()) {
            return pt.getDeclaration().getUnit();
        } else if (pt.isUnion()) {
            for (Type ct : pt.getCaseTypes()) {
                Unit u = getUnit(ct);
                if (u != null) {
                    return u;
                }
            }
        } else if (pt.isIntersection()) {
            for (Type st : pt.getSatisfiedTypes()) {
                Unit u = getUnit(st);
                if (u != null) {
                    return u;
                }
            }
        }
        return null;
    }

    /** Turns a Tuple type into a parameter list. */
    public static List<Parameter> convertTupleToParameters(Type _tuple) {
        final ArrayList<Parameter> rval = new ArrayList<>();
        int pos = 0;
        final Unit unit = getUnit(_tuple);
        final Type empty = unit.getEmptyType();
        while (_tuple != null && !(_tuple.isSubtypeOf(empty) || _tuple.isTypeParameter())) {
            Parameter _p = null;
            if (isTuple(_tuple)) {
                _p = new Parameter();
                _p.setModel(new Value());
                if (_tuple.isUnion()) {
                    //Handle union types for defaulted parameters
                    for (Type mt : _tuple.getCaseTypes()) {
                        if (mt.isTuple()) {
                            _p.getModel().setType(mt.getTypeArgumentList().get(1));
                            _tuple = mt.getTypeArgumentList().get(2);
                            break;
                        }
                    }
                    _p.setDefaulted(true);
                } else {
                    _p.getModel().setType(_tuple.getTypeArgumentList().get(1));
                    _tuple = _tuple.getTypeArgumentList().get(2);
                }
            } else if (unit.isSequentialType(_tuple)) {
                //Handle Sequence, for nonempty variadic parameters
                _p = new Parameter();
                _p.setModel(new Value());
                _p.getModel().setType(_tuple.getTypeArgumentList().get(0));
                _p.setSequenced(true);
                _tuple = null;
            }
            else {
                if (pos > 100) {
                    return rval;
                }
            }
            if (_p != null) {
                _p.setName("arg" + pos);
                rval.add(_p);
            }
            pos++;
        }
        return rval;
    }

    /** Check if a type is a Tuple, or a union of 2 types one of which is a Tuple. */
    private static boolean isTuple(Type pt) {
        if (pt.isClass() && pt.getDeclaration().equals(pt.getDeclaration().getUnit().getTupleDeclaration())) {
            return true;
        } else if (pt.isUnion() && pt.getCaseTypes().size() == 2) {
            Class tuple = pt.getCaseTypes().get(0).isClassOrInterface() ?
                    pt.getCaseTypes().get(0).getDeclaration().getUnit().getTupleDeclaration() :
                        pt.getCaseTypes().get(1).isClassOrInterface() ?
                                pt.getCaseTypes().get(1).getDeclaration().getUnit().getTupleDeclaration() : null;
            return tuple != null && (tuple.equals(pt.getCaseTypes().get(0).getDeclaration())
                    || tuple.equals(pt.getCaseTypes().get(1).getDeclaration()));
        }
        return false;
    }

    /** This method encodes the type parameters of a Tuple in the same way
     * as a parameter list for runtime. */
    private static void encodeTupleAsParameterListForRuntime(final boolean resolveTargs, final Node node,
            Type _tuple, boolean nameAndMetatype, GenerateJsVisitor gen) {
        gen.out("[");
        int pos = 1;
//        int minTuple = node.getUnit().getTupleMinimumLength(_tuple);
        final Type empty = node.getUnit().getEmptyType();
        while (_tuple != null && !(_tuple.isExactly(empty) || _tuple.isTypeParameter())) {
            if (pos > 1) gen.out(",");
            pos++;
            if (nameAndMetatype) {
                gen.out("{", MetamodelGenerator.KEY_NAME, ":'p", Integer.toString(pos), "',");
                gen.out(MetamodelGenerator.KEY_METATYPE, ":'", MetamodelGenerator.METATYPE_PARAMETER, "',");
                gen.out(MetamodelGenerator.KEY_TYPE, ":");
            }
            if (isTuple(_tuple)) {
                if (_tuple.isUnion() && _tuple.getCaseTypes().contains(node.getUnit().getEmptyType())) {
                    //Handle union types for defaulted parameters
                    metamodelTypeNameOrList(resolveTargs, node, gen.getCurrentPackage(), _tuple, null, gen);
                    if (nameAndMetatype) {
                        gen.out(",", MetamodelGenerator.KEY_DEFAULT,":1");
                    }
                    _tuple = null;
                } else {
                    metamodelTypeNameOrList(resolveTargs, node, gen.getCurrentPackage(),
                            _tuple.getTypeArgumentList().get(1), null, gen);
                    _tuple = _tuple.getTypeArgumentList().get(2);
                }
            } else if (node.getUnit().isSequentialType(_tuple)) {
                Type _t2 = _tuple.getSupertype(node.getUnit().getSequenceDeclaration());
                final int seq;
                if (_t2 == null) {
                    _t2 = _tuple.getSupertype(node.getUnit().getSequentialDeclaration());
                    seq = 1;
                } else {
                    seq = 2;
                }
                //Handle Sequence, for nonempty variadic parameters
                if (nameAndMetatype) {
                    metamodelTypeNameOrList(resolveTargs, node, gen.getCurrentPackage(),
                            _t2.getTypeArgumentList().get(0), null, gen);
                    gen.out(",seq:", Integer.toString(seq));
                } else {
                    gen.out(gen.getClAlias(), "mkseq$(");
                    metamodelTypeNameOrList(resolveTargs, node, gen.getCurrentPackage(),
                            _t2.getTypeArgumentList().get(0), null, gen);
                    gen.out(",", Integer.toString(seq), ")");
                }
                _tuple = null;
            } else if (_tuple.isUnion()) {
                metamodelTypeNameOrList(resolveTargs, node, gen.getCurrentPackage(), _tuple, null, gen);
                _tuple=null;
            } else {
                gen.out("\n/*WARNING3! Tuple is actually ", _tuple.asString(), "*/");
                if (pos > 100) {
                    break;
                }
            }
            if (nameAndMetatype) {
                gen.out("}");
            }
        }
        gen.out("]");
    }

    /** This method encodes the Arguments type argument of a Callable the same way
     * as a parameter list for runtime. */
    public static void encodeCallableArgumentsAsParameterListForRuntime(final Node node,
            Type _callable, GenerateJsVisitor gen) {
        if (_callable.getCaseTypes() != null) {
            for (Type pt : _callable.getCaseTypes()) {
                if (pt.isCallable()) {
                    _callable = pt;
                    break;
                }
            }
        } else if (_callable.getSatisfiedTypes() != null) {
            for (Type pt : _callable.getSatisfiedTypes()) {
                if (pt.isCallable()) {
                    _callable = pt;
                    break;
                }
            }
        }
        if (!_callable.isCallable()) {
            gen.out("[/*WARNING1: got ", _callable.asString(), " instead of Callable*/]");
            return;
        }
        List<Type> targs = _callable.getTypeArgumentList();
        if (targs == null || targs.size() != 2) {
            gen.out("[/*WARNING2: missing argument types for Callable*/]");
            return;
        }
        encodeTupleAsParameterListForRuntime(true, node, targs.get(1), true, gen);
    }

    public static void encodeForRuntime(Node that, final Declaration d, final GenerateJsVisitor gen) {
        if (d.getAnnotations() == null || d.getAnnotations().isEmpty() ||
                (d instanceof Class && d.isAnonymous())) {
            encodeForRuntime(that, d, gen, null);
        } else {
            encodeForRuntime(that, d, gen, new ModelAnnotationGenerator(gen, d, that));
        }
    }

    public static void encodeMethodForRuntime(final Tree.AnyMethod that, final GenerateJsVisitor gen) {
        encodeForRuntime(that, that.getDeclarationModel(), gen, new RuntimeMetamodelAnnotationGenerator() {
            @Override public void generateAnnotations() {
                outputAnnotationsFunction(that.getAnnotationList(), that.getDeclarationModel(), gen);
            }
        });
    }

    /** Output a metamodel map for runtime use. */
    public static void encodeForRuntime(final Node node, final Declaration d,
            final Tree.AnnotationList annotations, final GenerateJsVisitor gen) {
        encodeForRuntime(node, d, gen, new RuntimeMetamodelAnnotationGenerator() {
            @Override public void generateAnnotations() {
                outputAnnotationsFunction(annotations, d, gen);
            }
        });
    }

    /** Returns the list of keys to get from the package to the declaration, in the model. */
    public static List<String> generateModelPath(final Declaration d) {
        final ArrayList<String> sb = new ArrayList<>();
        final Package pkg = d.getUnit().getPackage();
        sb.add(pkg.isLanguagePackage()?"$":pkg.getNameAsString());
        if (d.isToplevel()) {
            sb.add(d.getName());
            if (d instanceof Setter) {
                sb.add("$set");
            }
        } else {
            Declaration p = d;
            final int i = sb.size();
            while (p instanceof Declaration) {
                if (p instanceof Setter) {
                    sb.add(i, "$set");
                }
                final String mname = TypeUtils.modelName(p);
                if (!(mname.startsWith("anon$") || mname.startsWith("anonymous#"))) {
                    sb.add(i, mname);
                    //Build the path in reverse
                    if (!p.isToplevel()) {
                        if (p instanceof Class) {
                            sb.add(i, p.isAnonymous() ? MetamodelGenerator.KEY_OBJECTS : MetamodelGenerator.KEY_CLASSES);
                        } else if (p instanceof org.eclipse.ceylon.model.typechecker.model.Interface) {
                            sb.add(i, MetamodelGenerator.KEY_INTERFACES);
                        } else if (p instanceof Function) {
                            if (!p.isAnonymous()) {
                                sb.add(i, MetamodelGenerator.KEY_METHODS);
                            }
                        } else if (p instanceof TypeAlias || p instanceof Setter) {
                            sb.add(i, MetamodelGenerator.KEY_ATTRIBUTES);
                        } else if (p instanceof Constructor || ModelUtil.isConstructor(p)) {
                            sb.add(i, MetamodelGenerator.KEY_CONSTRUCTORS);
                        } else { //It's a value
                            TypeDeclaration td=((TypedDeclaration)p).getTypeDeclaration();
                            sb.add(i, (td!=null&&td.isAnonymous())? MetamodelGenerator.KEY_OBJECTS
                                    : MetamodelGenerator.KEY_ATTRIBUTES);
                        }
                    }
                }
                p = ModelUtil.getContainingDeclaration(p);
                while (p != null  && p instanceof ClassOrInterface == false &&
                        !(p.isToplevel() || p.isAnonymous() || p.isClassOrInterfaceMember() || p.isJsCaptured())) {
                    p = ModelUtil.getContainingDeclaration(p);
                }
            }
        }
        return sb;
    }

    static void outputModelPath(final Declaration d, GenerateJsVisitor gen) {
        List<String> parts = generateModelPath(d);
        gen.out("[");
        boolean first = true;
        for (String p : parts) {
            if (p.startsWith("anon$") || p.startsWith("anonymous#"))continue;
            if (first)first=false;else gen.out(",");
            gen.out("'", p, "'");
        }
        gen.out("]");
    }

    public static void encodeForRuntime(final Node that, final Declaration d, final GenerateJsVisitor gen,
            final RuntimeMetamodelAnnotationGenerator annGen) {
        gen.out("function(){return{mod:$M$");
        List<TypeParameter> tparms = d instanceof Generic ? d.getTypeParameters() : null;
        List<Type> satisfies = null;
        List<Type> caseTypes = null;
        if (d instanceof Class) {
            Class _cd = (Class)d;
            if (_cd.getExtendedType() != null) {
                gen.out(",'super':");
                metamodelTypeNameOrList(false, that, d.getUnit().getPackage(),
                        _cd.getExtendedType(), null, gen);
            }
            //Parameter types
            if (_cd.getParameterList()!=null) {
                gen.out(",", MetamodelGenerator.KEY_PARAMS, ":");
                encodeParameterListForRuntime(false, that, _cd.getParameterList(), gen);
            }
            satisfies = _cd.getSatisfiedTypes();
            caseTypes = _cd.getCaseTypes();

        } else if (d instanceof Interface) {
            Interface _id = (Interface) d;
            satisfies = _id.getSatisfiedTypes();
            caseTypes = _id.getCaseTypes();
            if (_id.isAlias()) {
                ArrayList<Type> s2 = new ArrayList<>(satisfies.size()+1);
                s2.add(_id.getExtendedType());
                s2.addAll(satisfies);
                satisfies = s2;
            }

        } else if (d instanceof FunctionOrValue) {

            gen.out(",", MetamodelGenerator.KEY_TYPE, ":");
            if (d instanceof Function && ((Function)d).getParameterLists().size() > 1) {
                Type callableType = ((Function)d).getTypedReference().getFullType();
                //This needs a new setting to resolve types but not type parameters
                metamodelTypeNameOrList(false, that, d.getUnit().getPackage(),
                        that.getUnit().getCallableReturnType(callableType), null, gen);
            } else {
                //This needs a new setting to resolve types but not type parameters
                metamodelTypeNameOrList(false, that, d.getUnit().getPackage(),
                        ((FunctionOrValue)d).getType(), null, gen);
            }
            if (d instanceof Function) {
                gen.out(",", MetamodelGenerator.KEY_PARAMS, ":");
                //Parameter types of the first parameter list
                encodeParameterListForRuntime(false, that, ((Function)d).getFirstParameterList(), gen);
                tparms = d.getTypeParameters();
            }

        } else if (d instanceof Constructor) {
            gen.out(",", MetamodelGenerator.KEY_PARAMS, ":");
            encodeParameterListForRuntime(false, that, ((Constructor)d).getFirstParameterList(), gen);
        }
        if (!d.isToplevel()) {
            //Find the first container that is a Declaration
            Declaration _cont = ModelUtil.getContainingDeclaration(d);
            //Skip over anonymous types/funs as well as local non-captured fields
            while (_cont.isAnonymous() || !(_cont.isToplevel() || _cont.isClassOrInterfaceMember()
                    || _cont instanceof Value == false)) {
                //Captured values will have a metamodel so we don't skip those
                //Neither do we skip classes, even if they're anonymous
                if ((_cont instanceof Value && (((Value)_cont).isJsCaptured())) || _cont instanceof Class) {
                    break;
                }
                Declaration __d = ModelUtil.getContainingDeclaration(_cont);
                if (__d==null)break;
                _cont=__d;
            }
            gen.out(",$cont:");
            boolean generateName = true;
            if ((_cont.getName() != null && _cont.isAnonymous() && _cont instanceof Function)
                    || (_cont instanceof Value && !((Value)_cont).isTransient())) {
                //Anon functions don't have metamodel so go up until we find a non-anon container
                Declaration _supercont = ModelUtil.getContainingDeclaration(_cont);
                while (_supercont != null && _supercont.getName() != null
                        && _supercont.isAnonymous()) {
                    _supercont = ModelUtil.getContainingDeclaration(_supercont);
                }
                if (_supercont == null) {
                    //If the container is a package, add it because this isn't really toplevel
                    generateName = false;
                    gen.out("0");
                } else {
                    _cont = _supercont;
                }
            }
            if (generateName) {
                if (_cont instanceof Value) {
                    if (AttributeGenerator.defineAsProperty(_cont)) {
                        gen.qualify(that, _cont);
                    }
                    gen.out(gen.getNames().getter(_cont, true));
                } else if (_cont instanceof Setter) {
                    gen.out("{setter:");
                    if (AttributeGenerator.defineAsProperty(_cont)) {
                        gen.qualify(that, _cont);
                        gen.out(gen.getNames().getter(((Setter) _cont).getGetter(), true), ".set");
                    } else {
                        gen.out(gen.getNames().setter(((Setter) _cont).getGetter()));
                    }
                    gen.out("}");
                } else {
                    boolean inProto = gen.opts.isOptimize()
                            && (_cont.getContainer() instanceof TypeDeclaration);
                    final String path = gen.qualifiedPath(that, _cont, inProto);
                    if (path != null && !path.isEmpty()) {
                        gen.out(path, ".");
                    }
                    final String contName = gen.getNames().name(_cont);
                    gen.out(contName);
                }
            }
        }
        if (tparms != null && !tparms.isEmpty()) {
            gen.out(",", MetamodelGenerator.KEY_TYPE_PARAMS, ":{");
            encodeTypeParametersForRuntime(that, d, tparms, true, gen);
            gen.out("}");
        }
        if (satisfies != null && !satisfies.isEmpty()) {
            gen.out(",", MetamodelGenerator.KEY_SATISFIES, ":[");
            boolean first = true;
            for (Type st : satisfies) {
                if (!first)gen.out(",");
                first=false;
                metamodelTypeNameOrList(false, that, d.getUnit().getPackage(), st, null, gen);
            }
            gen.out("]");
        }
        if (caseTypes != null && !caseTypes.isEmpty()) {
            gen.out(",of:[");
            boolean first = true;
            for (Type st : caseTypes) {
                final TypeDeclaration std = st.getDeclaration(); //teeheehee
                if (!first)gen.out(",");
                first=false;
                if (ModelUtil.isConstructor(std)) {
                    if (std.isAnonymous()) {
                        //Value constructor
                        gen.out(gen.getNames().name(d), ".", gen.getNames().valueConstructorName(std));
                    } else {
                        gen.out("/*TODO callable constructor*/");
                    }
                } else if (std.isAnonymous()) {
                    if (std.isStatic()) {
                        gen.out(gen.getNames().name(ModelUtil.getContainingDeclaration(std)), ".$st$.", gen.getNames().objectName(std));
                    } else {
                        gen.out(gen.getNames().getter(std, true));
                    }
                } else {
                    metamodelTypeNameOrList(false, that, d.getUnit().getPackage(), st, null, gen);
                }
            }
            gen.out("]");
        }
        if (annGen != null) {
            annGen.generateAnnotations();
        }
        //Path to its model
        gen.out(",d:");
        outputModelPath(d, gen);
        gen.out("};}");
    }

    static boolean encodeTypeParametersForRuntime(final Node node, final Declaration d,
            final List<TypeParameter> tparms, boolean first, final GenerateJsVisitor gen) {
        for(TypeParameter tp : tparms) {
            boolean comma = false;
            if (!first)gen.out(",");
            first=false;
            gen.out(gen.getNames().typeParameterName(tp), ":{");
            if (tp.isCovariant()) {
                gen.out(MetamodelGenerator.KEY_DS_VARIANCE, ":'out'");
                comma = true;
            } else if (tp.isContravariant()) {
                gen.out(MetamodelGenerator.KEY_DS_VARIANCE, ":'in'");
                comma = true;
            }
            List<Type> typelist = tp.getSatisfiedTypes();
            if (typelist != null && !typelist.isEmpty()) {
                if (comma)gen.out(",");
                gen.out(MetamodelGenerator.KEY_SATISFIES, ":[");
                boolean first2 = true;
                for (Type st : typelist) {
                    if (!first2)gen.out(",");
                    first2=false;
                    metamodelTypeNameOrList(false, node, d.getUnit().getPackage(), st, null, gen);
                }
                gen.out("]");
                comma = true;
            }
            typelist = tp.getCaseTypes();
            if (typelist != null && !typelist.isEmpty()) {
                if (comma)gen.out(",");
                gen.out("of:[");
                boolean first3 = true;
                for (Type st : typelist) {
                    if (!first3)gen.out(",");
                    first3=false;
                    metamodelTypeNameOrList(false, node, d.getUnit().getPackage(), st, null, gen);
                }
                gen.out("]");
                comma = true;
            }
            if (tp.getDefaultTypeArgument() != null) {
                if (comma)gen.out(",");
                gen.out("def:");
                metamodelTypeNameOrList(false, node, d.getUnit().getPackage(),
                        tp.getDefaultTypeArgument(), null, gen);
            }
            gen.out("}");
        }
        return first;
    }

    /** Prints out an object with a type constructor under the property "t" and its type arguments under
     * the property "a", or a union/intersection type with "u" or "i" under property "t" and the list
     * of types that compose it in an array under the property "l", or a type parameter as a reference to
     * already existing params.
     * @param resolveTargsFromScope Indicates whether to resolve a type argument if it's within reach in the
     * node's scope. This is useful for parameters of JsCallables but must be disabled for metamodel functions.
     * @param node The node to use as starting point for resolution of other references.
     * @param pkg The package of the current declaration
     * @param pt The produced type for which a name must be output.
     * @param gen The generator to use for output. */
    static void metamodelTypeNameOrList(final boolean resolveTargsFromScope, final Node node,
            final org.eclipse.ceylon.model.typechecker.model.Package pkg,
            Type pt, SiteVariance useSiteVariance, GenerateJsVisitor gen) {
        if (pt == null) {
            //In dynamic blocks we sometimes get a null producedType
            gen.out("'$U'");
            return;
        }
        if (!outputMetamodelTypeList(resolveTargsFromScope, node, pkg, pt, gen)) {
            TypeDeclaration type = pt.getDeclaration();
            if (pt.isTypeParameter()) {
                final TypeParameter tparm = (TypeParameter)type;
                final Declaration tpowner = tparm.getDeclaration();
                final boolean nodeIsDecl = node instanceof Tree.Declaration;
                boolean rtafs = tpowner instanceof TypeDeclaration == false &&
                        (nodeIsDecl ? ((Tree.Declaration)node).getDeclarationModel() != tpowner : true);
                if (rtafs && ModelUtil.contains((Scope)tpowner, node.getScope())) {
                    //Attempt to resolve this to an argument if the scope allows for it
                    if (tpowner instanceof TypeDeclaration) {
                        gen.out(gen.getNames().self((TypeDeclaration)tpowner), ".$a$.",
                                gen.getNames().typeParameterName(tparm));
                    } else if (tpowner instanceof Function) {
                        gen.out(gen.getNames().typeArgsParamName((Function)tpowner), ".",
                                gen.getNames().typeParameterName(tparm));
                    }
                } else if (resolveTargsFromScope && tpowner instanceof TypeDeclaration && (nodeIsDecl ? ((Tree.Declaration)node).getDeclarationModel() == tpowner : true)  && ModelUtil.contains((Scope)tpowner, node.getScope())) {
                    typeNameOrList(node, tparm.getType(), gen, false);
                } else {
                    gen.out("'", gen.getNames().typeParameterName(tparm), "'");
                }
            } else if (pt.isTypeAlias()) {
                outputQualifiedTypename(node, gen.isImported(pkg, type), pt, gen, false);
            } else {
                gen.out("{t:");
                //For constructors, output the type of the class
                final Type qt = type instanceof Constructor ? pt.getQualifyingType() : pt;
                outputQualifiedTypename(node, gen.isImported(pkg, type), qt, gen, false);
                //Type Parameters
                if (!pt.getTypeArguments().isEmpty()) {
                    gen.out(",a:{");
                    boolean first = true;
                    for (Map.Entry<TypeParameter, Type> e : pt.getTypeArguments().entrySet()) {
                        if (first) first=false; else gen.out(",");
                        gen.out(gen.getNames().typeParameterName(e.getKey()), ":");
                        metamodelTypeNameOrList(resolveTargsFromScope, node, pkg, e.getValue(),
                                pt.getVarianceOverrides().get(e.getKey()), gen);
                    }
                    gen.out("}");
                }
                printSiteVariance(useSiteVariance, gen);
                gen.out("}");
            }
        }
    }

    private static void printSiteVariance(SiteVariance sv, GenerateJsVisitor gen) {
        if (sv != null) {
            gen.out(",", MetamodelGenerator.KEY_US_VARIANCE, ":");
            if (sv == SiteVariance.IN) {
                gen.out("'in'");
            } else {
                gen.out("'out'");
            }
        }
    }
    /** Appends an object with the type's type and list of union/intersection types; works only with union,
     * intersection and tuple types.
     * @return true if output was generated, false otherwise (it was a regular type) */
    static boolean outputMetamodelTypeList(final boolean resolveTargs, final Node node,
            final org.eclipse.ceylon.model.typechecker.model.Package pkg,
            Type pt, GenerateJsVisitor gen) {
        final List<Type> subs;
        if (pt.isIntersection()) {
            gen.out("{t:'i");
            subs = pt.getSatisfiedTypes();
        } else if (pt.isUnion()) {
            gen.out("{t:'u");
            subs = pt.getCaseTypes();
        } else if (pt.isTuple()) {
            if (pt.involvesTypeParameters() && resolveTargs) {
                gen.out("{t:", gen.getClAlias(), "Tuple,a:");
                printTypeArguments(node, pt.getTypeArguments(), gen, false, pt.getVarianceOverrides());
            } else {
                gen.out("{t:'T',l:");
                encodeTupleAsParameterListForRuntime(resolveTargs, node, pt,false, gen);
            }
            gen.out("}");
            return true;
        } else {
            return false;
        }
        gen.out("',l:[");
        boolean first = true;
        for (Type t : subs) {
            if (!first) gen.out(",");
            metamodelTypeNameOrList(resolveTargs, node, pkg, t, null, gen);
            first = false;
        }
        gen.out("]}");
        return true;
    }

    static String pathToModelDoc(final Declaration d) {
        if (d == null)return null;
        final StringBuilder sb = new StringBuilder();
        for (String p : generateModelPath(d)) {
            sb.append(sb.length() == 0 ? '\'' : ':').append(p);
        }
        sb.append('\'');
        return sb.toString();
    }

    /** Helper interface for generating annotations function. */
    public static interface AnnotationFunctionHelper {
        /** Get the key for packed annotations */
        String getPackedAnnotationsKey();
        /** Get the key for regular annotations */
        String getAnnotationsKey();
        /** Get the annotation source (the declaration) */
        Object getAnnotationSource();
        /** Get the annotations from the declaration */
        List<Annotation> getAnnotations();
        /** Get the path to the declaration in the model that holds the doc text */
        String getPathToModelDoc();
        /** Get the key to the third-to-last part of the doc path, if other than 'an'.
         * (it's usually 'an.doc[0]') */
        String getAnPath();
    }

    /** Outputs a function that returns the specified annotations, so that they can be loaded lazily.
     * @param annotations The annotations to be output.
     * @param gen The generator to use for output. */
    public static void outputAnnotationsFunction(final Tree.AnnotationList annotations,
            final AnnotationFunctionHelper helper, final GenerateJsVisitor gen) {
        List<Tree.Annotation> anns = annotations == null ? null : annotations.getAnnotations();
        int mask = 0;
        if (helper.getPackedAnnotationsKey() != null) {
            mask = MetamodelGenerator.encodeAnnotations(helper.getAnnotations(),
                    helper.getAnnotationSource(), null);
            if (mask > 0) {
                gen.out(",", helper.getPackedAnnotationsKey(), ":", Integer.toString(mask));
            }
            if (annotations == null || (anns.isEmpty() && annotations.getAnonymousAnnotation() == null)) {
                return;
            }
            anns = new ArrayList<>(annotations.getAnnotations().size());
            anns.addAll(annotations.getAnnotations());
            for (Iterator<Tree.Annotation> iter = anns.iterator(); iter.hasNext();) {
                final String qn = ((Tree.StaticMemberOrTypeExpression)iter.next().getPrimary()
                        ).getDeclaration().getQualifiedNameString();
                if (!qn.equals("ceylon.language::native") && qn.startsWith("ceylon.language::")
                        && MetamodelGenerator.annotationBits.contains(qn.substring(17))) {
                    iter.remove();
                }
            }
            if (anns.isEmpty() && annotations.getAnonymousAnnotation() == null) {
                return;
            }
        }
        if (helper.getAnnotationsKey() != null) {
            gen.out(",", helper.getAnnotationsKey(), ":");
        }
        if (annotations == null || (anns.isEmpty() && annotations.getAnonymousAnnotation()==null)) {
            gen.out("[]");
        } else {
            gen.out("function(){return[");
            boolean first = true;
            //Leave the annotation but remove the doc from runtime for brevity
            if (annotations.getAnonymousAnnotation() != null) {
                first = false;
                final Tree.StringLiteral lit = annotations.getAnonymousAnnotation().getStringLiteral();
                final String ptmd = helper.getPathToModelDoc();
                if (ptmd != null && ptmd.length() < lit.getText().length()) {
                    gen.out(gen.getClAlias(), "doc$($M$,", ptmd);
                    if (helper.getAnPath() != null) {
                        gen.out(",", helper.getAnPath());
                    }
                } else {
                    gen.out(gen.getClAlias(), "doc(");
                    lit.visit(gen);
                }
                gen.out(")");
            }
            for (Tree.Annotation a : anns) {
                if (first) first=false; else gen.out(",");
                gen.getInvoker().generateInvocation(a);
            }
            gen.out("];}");
        }
    }

    /** Outputs a function that returns the specified annotations, so that they can be loaded lazily.
     * @param annotations The annotations to be output.
     * @param d The declaration to which the annotations belong.
     * @param gen The generator to use for output. */
    public static void outputAnnotationsFunction(final Tree.AnnotationList annotations, final Declaration d,
            final GenerateJsVisitor gen) {
        outputAnnotationsFunction(annotations, new AnnotationFunctionHelper() {
            @Override
            public String getPathToModelDoc() {
                return pathToModelDoc(d);
            }
            @Override
            public String getPackedAnnotationsKey() {
                return MetamodelGenerator.KEY_PACKED_ANNS;
            }
            @Override
            public String getAnnotationsKey() {
                return MetamodelGenerator.KEY_ANNOTATIONS;
            }
            @Override
            public List<Annotation> getAnnotations() {
                return d.getAnnotations();
            }
            @Override
            public Object getAnnotationSource() {
                return d;
            }
            @Override
            public String getAnPath() {
                return null;
            }
        }, gen);
    }

    /** Abstraction for a callback that generates the runtime annotations list as part of the metamodel. */
    public static interface RuntimeMetamodelAnnotationGenerator {
        public void generateAnnotations();
    }

    static class ModelAnnotationGenerator implements RuntimeMetamodelAnnotationGenerator {
        private final GenerateJsVisitor gen;
        private final Declaration d;
        private final Node node;
        ModelAnnotationGenerator(GenerateJsVisitor generator, Declaration decl, Node n) {
            gen = generator;
            d = decl;
            node = n;
        }
        @Override public void generateAnnotations() {
            List<Annotation> anns = d.getAnnotations();
            final int bits = MetamodelGenerator.encodeAnnotations(anns, d, null);
            if (bits > 0) {
                gen.out(",", MetamodelGenerator.KEY_PACKED_ANNS, ":", Integer.toString(bits));
                //Remove these annotations from the list
                anns = new ArrayList<Annotation>(d.getAnnotations().size());
                anns.addAll(d.getAnnotations());
                for (Iterator<Annotation> iter = anns.iterator(); iter.hasNext();) {
                    final Annotation a = iter.next();
                    final Declaration ad = d.getUnit().getPackage().getMemberOrParameter(
                            d.getUnit(), a.getName(), null, false);
                    final String qn = ad.getQualifiedNameString();
                    if (qn.startsWith("ceylon.language::") && MetamodelGenerator.annotationBits.contains(qn.substring(17))) {
                        iter.remove();
                    }
                }
                if (anns.isEmpty()) {
                    return;
                }
            }
            gen.out(",", MetamodelGenerator.KEY_ANNOTATIONS, ":function(){return[");
            boolean first = true;
            for (Annotation a : anns) {
                Declaration ad = d.getUnit().getPackage().getMemberOrParameter(d.getUnit(), a.getName(), null, false);
                if (ad instanceof Function) {
                    if (first) first=false; else gen.out(",");
                    final boolean isDoc = "ceylon.language::doc".equals(ad.getQualifiedNameString());
                    if (!isDoc) {
                        gen.qualify(node, ad);
                        gen.out(gen.getNames().name(ad), "(");
                    }
                    if (a.getPositionalArguments() == null) {
                        for (Parameter p : ((Function)ad).getFirstParameterList().getParameters()) {
                            String v = a.getNamedArguments().get(p.getName());
                            gen.out(v == null ? "undefined" : v);
                        }
                    } else {
                        if (isDoc) {
                            //Use ref if it's too long
                            final String ref = pathToModelDoc(d);
                            final String doc = a.getPositionalArguments().get(0);
                            if (ref != null && ref.length() < doc.length()) {
                                gen.out(gen.getClAlias(), "doc$($M$,", ref);
                            } else {
                                gen.out(gen.getClAlias(), "doc(\"", JsUtils.escapeStringLiteral(doc), "\"");
                            }
                        } else {
                            boolean farg = true;
                            for (String s : a.getPositionalArguments()) {
                                if (farg)farg=false; else gen.out(",");
                                gen.out("\"", JsUtils.escapeStringLiteral(s), "\"");
                            }
                        }
                    }
                    gen.out(")");
                } else {
                    gen.out("/*MISSING DECLARATION FOR ANNOTATION ", a.getName(), "*/");
                }
            }
            gen.out("];}");
        }
    }

    /** Generates the right type arguments for operators that are sugar for method calls.
     * @param methodName The name of the method that is to be invoked
     * @param rightTpName The name of the type argument on the right term
     * @param leftTpName The name of the type parameter on the method
     * @return A map with the type parameter of the method as key
     * and the produced type belonging to the type argument of the term on the right. */
    public static Map<TypeParameter, Type> mapTypeArgument(final Tree.BinaryOperatorExpression expr,
            final String methodName, final String rightTpName, final String leftTpName) {
        Function md = (Function)expr.getLeftTerm().getTypeModel().getDeclaration().getMember(methodName, null, false);
        if (md == null) {
            expr.addUnexpectedError("Left term of intersection operator should have method named " +
                    methodName, Backend.JavaScript);
            return null;
        }
        Map<TypeParameter, Type> targs = expr.getRightTerm().getTypeModel().getTypeArguments();
        Type otherType = null;
        for (TypeParameter tp : targs.keySet()) {
            if (tp.getName().equals(rightTpName)) {
                otherType = targs.get(tp);
                break;
            }
        }
        if (otherType == null) {
            expr.addUnexpectedError("Right term of intersection operator should have type parameter named " +
                    rightTpName, Backend.JavaScript);
            return null;
        }
        targs = new HashMap<>();
        TypeParameter mtp = null;
        for (TypeParameter tp : md.getTypeParameters()) {
            if (tp.getName().equals(leftTpName)) {
                mtp = tp;
                break;
            }
        }
        if (mtp == null) {
            expr.addUnexpectedError("Left term of intersection should have type parameter named " +
                        leftTpName, Backend.JavaScript);
        }
        targs.put(mtp, otherType);
        return targs;
    }

    /** Returns the qualified name of a declaration, skipping any containing methods. */
    public static String qualifiedNameSkippingMethods(Declaration d) {
        final StringBuilder p = new StringBuilder(d.getName());
        Scope s = d.getContainer();
        while (s != null) {
            if (s instanceof org.eclipse.ceylon.model.typechecker.model.Package) {
                final String pkname = ((org.eclipse.ceylon.model.typechecker.model.Package)s).getNameAsString();
                if (!pkname.isEmpty()) {
                    p.insert(0, "::");
                    p.insert(0, pkname);
                }
            } else if (s instanceof TypeDeclaration) {
                p.insert(0, '.');
                p.insert(0, ((TypeDeclaration)s).getName());
            }
            s = s.getContainer();
        }
        return p.toString();
    }

    public static String modelName(Declaration d) {
        String dname = d.getName();
        if (dname == null && d instanceof org.eclipse.ceylon.model.typechecker.model.Constructor) {
            dname = "$def";
        }
        if (dname.startsWith("anonymous#")) {
            dname = "anon$" + dname.substring(10);
        }
        if (d.isToplevel() || d.isShared()) {
            return dname;
        }
        if (d instanceof Setter) {
            d = ((Setter)d).getGetter();
        }
        return dname+"$"+Long.toString(Math.abs((long)d.hashCode()), 36);
    }
    
    public static boolean acceptNative(Tree.Declaration node) {
        return node.getDeclarationModel() == null || acceptNative(node.getDeclarationModel());
    }
    
    /**
     * Returns true if the declaration is:
     *  - not native or
     *  - native with a "js" argument
     *  - native header and no overloads
     *  - native header with a default implementation
     *    and no implementation for this backend 
     */
    public static boolean acceptNative(Declaration decl) {
        return (!decl.isNative())
                || NativeUtil.isForBackend(decl, Backend.JavaScript)
                || isNativeExternal(decl)
                || (NativeUtil.isHeaderWithoutBackend(decl, Backend.JavaScript)
                    && ModelUtil.isImplemented(decl));
    }

    public static boolean isCallable(Type t) {
        return t != null && !t.isUnknown() && t.isCallable();
    }

    /**
     * Checks that the given declaration is defined in an external JavaScript file.
     * It does this by assuming that an external implementation will have a declaration
     * in Ceylon code with a "native" annotation that has NO arguments. It will
     * also have no associated "overloads" (there are no other declaration found
     * in the Ceylon code with the same name)
     */
    public static boolean isNativeExternal(Declaration decl) {
        return decl.isNativeHeader()
                && (decl.getOverloads() == null || decl.getOverloads().isEmpty());
    }

    public static List<Type> getTypes(List<Tree.StaticType> treeTypes) {
        if (treeTypes == null) {
            return null;
        }
        if (treeTypes.isEmpty()) {
            return Collections.emptyList();
        }
        final ArrayList<Type> r = new ArrayList<>(treeTypes.size());
        for (Tree.StaticType st : treeTypes) {
            r.add(st.getTypeModel().resolveAliases());
        }
        return r;
    }

    public static Constructor getConstructor(Declaration d) {
        if (d instanceof Constructor) {
            return (Constructor)d;
        }
        if (d instanceof FunctionOrValue && ((FunctionOrValue)d).getTypeDeclaration() instanceof Constructor) {
            return (Constructor)((FunctionOrValue)d).getTypeDeclaration();
        }
        if (d instanceof Class) {
            //Look for the default constructor
            return ((Class)d).getDefaultConstructor();
        }
        return null;
    }

    /** Tells whether the declaration is a native header with a native implementation for this backend. */
    public static boolean makeAbstractNative(Declaration d) {
        return d.isNativeHeader() &&
                ModelUtil.getNativeDeclaration(d, Backend.JavaScript) != null;
    }

    public static Declaration getToplevel(Declaration d) {
        while (d != null && !d.isToplevel()) {
            Scope s = d.getContainer();
            // Skip any non-declaration elements
            while (s != null && !(s instanceof Declaration)) {
                s = s.getContainer();
            }
            d = (Declaration) s;
        }
        return d;
    }

    /** Returns true if the top-level declaration for the term is annotated "nativejs" */
    public static boolean isNativeJs(final Tree.Term t) {
        if (t instanceof Tree.MemberOrTypeExpression) {
            return isNativeJs(((Tree.MemberOrTypeExpression)t).getDeclaration());
        }
        return false;
    }

    /** Returns true if the declaration is annotated "nativejs" */
    public static boolean isNativeJs(Declaration d) {
        return hasAnnotationByName(TypeUtils.getToplevel(d), "nativejs") 
    		|| TypeUtils.isUnknown(d);
    }

    private static boolean hasAnnotationByName(Declaration d, String name){
        if (d != null) {
            for(Annotation annotation : d.getAnnotations()) {
                if (annotation.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isStaticWithGenericContainer(Declaration d) {
        if (d != null 
                && d.isStatic() 
                && d.getContainer() instanceof Generic) {
            Generic c = (Generic) d.getContainer();
            return c.isParameterized();
        }
        return false;
    }

    public static boolean intsOrFloats(Type t1, Type t2) {
        return t1 != null && t2 != null 
    		&& (t1.isInteger() || t1.isFloat()) 
    		&& (t2.isInteger() || t2.isFloat());
    }

    public static boolean bothInts(Type t1, Type t2) {
        return t1 != null && t2 != null 
    		&& t1.isInteger() && t2.isInteger();
    }
    public static boolean bothFloats(Type t1, Type t2) {
        return t1 != null && t2 != null 
    		&& t1.isFloat() && t2.isFloat();
    }
}
