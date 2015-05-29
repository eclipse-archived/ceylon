package com.redhat.ceylon.compiler.js.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.js.AttributeGenerator;
import com.redhat.ceylon.compiler.js.GenerateJsVisitor;
import com.redhat.ceylon.compiler.loader.MetamodelGenerator;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Generic;
import com.redhat.ceylon.model.typechecker.model.Method;
import com.redhat.ceylon.model.typechecker.model.MethodOrValue;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.UnionType;
import com.redhat.ceylon.model.typechecker.model.Unit;
import com.redhat.ceylon.model.typechecker.model.Util;
import com.redhat.ceylon.model.typechecker.model.Value;

/** A convenience class to help with the handling of certain type declarations. */
public class TypeUtils {

    static final List<String> splitMetamodelAnnotations = Arrays.asList("ceylon.language::doc",
            "ceylon.language::throws", "ceylon.language::see", "ceylon.language::by");

    /** Prints the type arguments, usually for their reification. */
    public static void printTypeArguments(final Node node, final Map<TypeParameter,ProducedType> targs,
            final GenerateJsVisitor gen, final boolean skipSelfDecl, final Map<TypeParameter, SiteVariance> overrides) {
        gen.out("{");
        boolean first = true;
        for (Map.Entry<TypeParameter,ProducedType> e : targs.entrySet()) {
            if (first) {
                first = false;
            } else {
                gen.out(",");
            }
            gen.out(e.getKey().getName(), "$", e.getKey().getDeclaration().getName(), ":");
            final ProducedType pt = e.getValue() == null ? null : e.getValue().resolveAliases();
            if (pt == null) {
                gen.out("'", e.getKey().getName(), "'");
            } else if (!outputTypeList(node, pt, gen, skipSelfDecl)) {
                boolean hasParams = pt.getTypeArgumentList() != null && !pt.getTypeArgumentList().isEmpty();
                boolean closeBracket = false;
                final TypeDeclaration d = pt.getDeclaration();
                if (pt.isTypeParameter()) {
                    resolveTypeParameter(node, (TypeParameter)d, gen, skipSelfDecl);
                } else {
                    closeBracket = !pt.isTypeAlias();
                    if (closeBracket)gen.out("{t:");
                    outputQualifiedTypename(node,
                            node != null && gen.isImported(node.getUnit().getPackage(), pt.getDeclaration()),
                            pt, gen, skipSelfDecl);
                }
                if (hasParams) {
                    gen.out(",a:");
                    printTypeArguments(node, pt.getTypeArguments(), gen, skipSelfDecl, pt.getVarianceOverrides());
                }
                SiteVariance siteVariance = overrides == null ? null : overrides.get(e.getKey());
                if (siteVariance != null) {
                    gen.out(",", MetamodelGenerator.KEY_US_VARIANCE, ":");
                    if (siteVariance == SiteVariance.IN) {
                        gen.out("'in'");
                    } else {
                        gen.out("'out'");
                    }
                }
                if (closeBracket) {
                    gen.out("}");
                }
            }
        }
        gen.out("}");
    }

    public static void outputQualifiedTypename(final Node node, final boolean imported, final ProducedType pt,
            final GenerateJsVisitor gen, final boolean skipSelfDecl) {
        TypeDeclaration t = pt.getDeclaration();
        final String qname = t.getQualifiedNameString();
        if (qname.equals("ceylon.language::Nothing")) {
            //Hack in the model means hack here as well
            gen.out(gen.getClAlias(), "Nothing");
        } else if (qname.equals("ceylon.language::null") || qname.equals("ceylon.language::Null")) {
            gen.out(gen.getClAlias(), "Null");
        } else if (Util.isTypeUnknown(pt)) {
            if (!gen.isInDynamicBlock()) {
                gen.out("/*WARNING unknown type");
                gen.location(node);
                gen.out("*/");
            }
            gen.out(gen.getClAlias(), "Anything");
        } else {
            gen.out(qualifiedTypeContainer(node, imported, t, gen));
            boolean _init = (!imported && pt.getDeclaration().isDynamic()) || t.isAnonymous();
            if (_init && !pt.getDeclaration().isToplevel()) {
                Declaration dynintc = Util.getContainingClassOrInterface(node.getScope());
                if (dynintc == null || dynintc instanceof Scope==false ||
                        !Util.contains((Scope)dynintc, pt.getDeclaration())) {
                    _init=false;
                }
            }
            if (_init) {
                gen.out("$init$");
            }

            if (!outputTypeList(null, pt, gen, skipSelfDecl)) {
                if (t.isAnonymous()) {
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
            final Scope scope = node == null ? null : Util.getContainingClassOrInterface(node.getScope());
            ClassOrInterface parent = (ClassOrInterface)t.getContainer();
            final List<ClassOrInterface> parents = new ArrayList<>(3);
            parents.add(0, parent);
            while (parent != scope && parent.getContainer() instanceof ClassOrInterface) {
                parent = (ClassOrInterface)parent.getContainer();
                parents.add(0, parent);
            }
            for (ClassOrInterface p : parents) {
                if (p==scope) {
                    if (gen.opts.isOptimize()) {
                        sb.append(gen.getNames().self(p)).append('.');
                    }
                } else {
                    sb.append(gen.getNames().name(p));
                    if (gen.opts.isOptimize()) {
                        sb.append(".$$.prototype");
                    }
                    sb.append('.');
                }
            }
        }
        return sb.toString();
    }

    /** Prints out an object with a type constructor under the property "t" and its type arguments under
     * the property "a", or a union/intersection type with "u" or "i" under property "t" and the list
     * of types that compose it in an array under the property "l", or a type parameter as a reference to
     * already existing params. */
    public static void typeNameOrList(final Node node, final ProducedType pt, final GenerateJsVisitor gen, final boolean skipSelfDecl) {
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
                    final Map<TypeParameter,ProducedType> targs;
                    if (pt.getDeclaration().isToplevel()) {
                        targs = pt.getTypeArguments();
                    } else {
                        //Gather all type parameters from containers
                        Scope scope = node.getScope();
                        final HashSet<TypeParameter> parenttp = new HashSet<>();
                        while (scope != null) {
                            if (scope instanceof Generic) {
                                for (TypeParameter tp : ((Generic)scope).getTypeParameters()) {
                                    parenttp.add(tp);
                                }
                            }
                            scope = scope.getScope();
                        }
                        targs = new HashMap<>();
                        targs.putAll(pt.getTypeArguments());
                        Declaration cd = Util.getContainingDeclaration(pt.getDeclaration());
                        while (cd != null) {
                            if (cd instanceof Generic) {
                                for (TypeParameter tp : ((Generic)cd).getTypeParameters()) {
                                    if (parenttp.contains(tp)) {
                                        targs.put(tp, tp.getType());
                                    }
                                }
                            }
                            cd = Util.getContainingDeclaration(cd);
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
    public static boolean outputTypeList(final Node node, final ProducedType pt, final GenerateJsVisitor gen, final boolean skipSelfDecl) {
        final List<ProducedType> subs;
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
            final ProducedType lastType = subs.get(subs.size()-1);
            if (pt.involvesTypeParameters()) {
                //Revert to outputting normal Tuple with its type arguments
                gen.out("{t:", gen.getClAlias(), "Tuple,a:");
                printTypeArguments(node, pt.getTypeArguments(), gen, skipSelfDecl, pt.getVarianceOverrides());
                gen.out("}");
                return true;
            }
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
                UnionType utail = new UnionType(d.getUnit());
                utail.setCaseTypes(Arrays.asList(lastType.getTypeArgumentList().get(0), lastType));
                subs.remove(subs.size()-1);
                subs.add(utail.getType());
            }
            gen.out(gen.getClAlias(), "mtt$([");
        } else {
            return false;
        }
        boolean first = true;
        for (ProducedType t : subs) {
            if (!first) gen.out(",");
            if (t==subs.get(subs.size()-1) && seq>0) {
                //The non-empty, non-tuple tail
                gen.out("{t:'u',l:[");
                typeNameOrList(node, t.getCaseTypes().get(0), gen, skipSelfDecl);
                gen.out(",");
                typeNameOrList(node, t.getCaseTypes().get(1), gen, skipSelfDecl);
                gen.out("],seq:", Integer.toString(seq), "}");
            } else {
                typeNameOrList(node, t, gen, skipSelfDecl);
            }
            first = false;
        }
        gen.out("])");
        return true;
    }

    /** Finds the owner of the type parameter and outputs a reference to the corresponding type argument. */
    static void resolveTypeParameter(final Node node, final TypeParameter tp,
            final GenerateJsVisitor gen, final boolean skipSelfDecl) {
        Scope parent = Util.getRealScope(node.getScope());
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
                    TypeDeclaration ontoy = Util.getContainingClassOrInterface(node.getScope());
                    while (ontoy.isAnonymous())ontoy=Util.getContainingClassOrInterface(ontoy.getScope());
                    gen.out(gen.getNames().self(ontoy));
                    if (ontoy == parent)outers--;
                    for (int i = 0; i < outers; i++) {
                        gen.out(".outer$");
                    }
                    gen.out(".");
                }
                gen.out("$$targs$$.", tp.getName(), "$", tp.getDeclaration().getName());
            } else {
                //This can happen in expressions such as Singleton(n) when n is dynamic
                gen.out("{/*NO PARENT*/t:", gen.getClAlias(), "Anything}");
            }
        } else if (tp.getContainer() instanceof TypeAlias) {
            if (parent == tp.getContainer()) {
                gen.out("'", tp.getName(), "$", tp.getDeclaration().getName(), "'");
            } else {
                //This can happen in expressions such as Singleton(n) when n is dynamic
                gen.out("{/*NO PARENT ALIAS*/t:", gen.getClAlias(), "Anything}");
            }
        } else {
            //it has to be a method, right?
            //We need to find the index of the parameter where the argument occurs
            //...and it could be null...
            ProducedType type = null;
            for (Iterator<ParameterList> iter0 = ((Method)tp.getContainer()).getParameterLists().iterator();
                    type == null && iter0.hasNext();) {
                for (Iterator<Parameter> iter1 = iter0.next().getParameters().iterator();
                        type == null && iter1.hasNext();) {
                    if (type == null) {
                        type = typeContainsTypeParameter(iter1.next().getType(), tp);
                    }
                }
            }
            //The ProducedType that we find corresponds to a parameter, whose type can be:
            //A type parameter in the method, in which case we just use the argument's type (may be null)
            //A component of a union/intersection type, in which case we just use the argument's type (may be null)
            //A type argument of the argument's type, in which case we must get the reified generic from the argument
            if (tp.getContainer() == parent) {
                gen.out(gen.getNames().typeArgsParamName((Method)tp.getContainer()), ".",
                        tp.getName(), "$", tp.getDeclaration().getName());
            } else {
                if (parent == null && node instanceof Tree.StaticMemberOrTypeExpression) {
                    if (tp.getContainer() == ((Tree.StaticMemberOrTypeExpression)node).getDeclaration()) {
                        type = ((Tree.StaticMemberOrTypeExpression)node).getTarget().getTypeArguments().get(tp);
                        typeNameOrList(node, type, gen, skipSelfDecl);
                        return;
                    }
                }
                gen.out("'", tp.getName(), "'");
                //gen.out.spitOut("Passing reference to " + tp.getQualifiedNameString() + " as a string...");
                //gen.out("/*Please report this to the ceylon-js team: METHOD TYPEPARM plist ", Integer.toString(plistCount), "#",
                //        tp.getName(), "*/'", type.getProducedTypeQualifiedName(), "' container " + tp.getContainer() + " at " + node);
            }
        }
    }

    static ProducedType typeContainsTypeParameter(ProducedType td, TypeParameter tp) {
        if (td.isUnion() || td.isIntersection()) {
            List<ProducedType> comps = td.getCaseTypes();
            if (comps == null) comps = td.getSatisfiedTypes();
            for (ProducedType sub : comps) {
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
                for (ProducedType sub : td.getTypeArgumentList()) {
                    if (typeContainsTypeParameter(sub, tp) != null) {
                        return td;
                    }
                }
            }
        }
        return null;
    }

    /** Find the type with the specified declaration among the specified type's supertypes, case types, satisfied types, etc. */
    public static ProducedType findSupertype(TypeDeclaration d, ProducedType pt) {
        if (pt.getDeclaration().equals(d)) {
            return pt;
        }
        List<ProducedType> list = pt.getSupertypes() == null ? pt.getCaseTypes() : pt.getSupertypes();
        for (ProducedType t : list) {
            if (t.getDeclaration().equals(d)) {
                return t;
            }
        }
        return null;
    }

    public static List<ProducedType> getDefaultTypeArguments(List<TypeParameter> tparms) {
        final ArrayList<ProducedType> targs = new ArrayList<>(tparms.size());
        for (TypeParameter tp : tparms) {
            ProducedType t = tp.getDefaultTypeArgument();
            if (t == null) {
                t = tp.getUnit().getAnythingType();
            }
            targs.add(t);
        }
        return targs;
    }

    public static Map<TypeParameter, ProducedType> matchTypeParametersWithArguments(List<TypeParameter> params, List<ProducedType> targs) {
        if (params != null) {
            if (targs == null) {
                targs = getDefaultTypeArguments(params);
            }
            if (params.size() == targs.size()) {
                HashMap<TypeParameter, ProducedType> r = new HashMap<TypeParameter, ProducedType>();
                for (int i = 0; i < targs.size(); i++) {
                    r.put(params.get(i), targs.get(i));
                }
                return r;
            }
        }
        return null;
    }

    public static Map<TypeParameter, ProducedType> wrapAsIterableArguments(ProducedType pt) {
        HashMap<TypeParameter, ProducedType> r = new HashMap<TypeParameter, ProducedType>();
        final TypeDeclaration iterable = pt.getDeclaration().getUnit().getIterableDeclaration();
        r.put(iterable.getTypeParameters().get(0), pt);
        r.put(iterable.getTypeParameters().get(1), pt.getDeclaration().getUnit().getNullType());
        return r;
    }

    public static boolean isUnknown(Declaration d) {
        return d == null || d.getQualifiedNameString().equals("UnknownType");
    }

    public static void spreadArrayCheck(final Tree.Term term, final GenerateJsVisitor gen) {
        String tmp = gen.getNames().createTempVariable();
        gen.out("(", tmp, "=");
        term.visit(gen);
        gen.out(",Array.isArray(", tmp, ")?", tmp);
        gen.out(":function(){throw new TypeError('Expected JS Array (",
                term.getUnit().getFilename(), " ", term.getLocation(), ")')}())");
    }

    /** Generates the code to throw an Exception if a dynamic object is not of the specified type. */
    public static void generateDynamicCheck(final Tree.Term term, ProducedType t,
            final GenerateJsVisitor gen, final boolean skipSelfDecl,
            final Map<TypeParameter,ProducedType> typeArguments) {
        if (t.getDeclaration().isDynamic()) {
            gen.out(gen.getClAlias(), "dre$$(");
            term.visit(gen);
            gen.out(",");
            TypeUtils.typeNameOrList(term, t, gen, skipSelfDecl);
            gen.out(",'", term.getUnit().getFilename(), " ", term.getLocation(), "')");
        } else {
            final boolean checkFloat = t.isFloat();
            final boolean checkInt = checkFloat ? false : t.isSubtypeOf(term.getUnit().getIntegerType());
            String tmp = gen.getNames().createTempVariable();
            gen.out("(", tmp, "=");
            term.visit(gen);
            final String errmsg;
            if (checkFloat) {
                gen.out(",typeof(", tmp, ")==='number'?", gen.getClAlias(), "Float(", tmp, ")");
                errmsg = "Expected Float";
            } else if (checkInt) {
                gen.out(",typeof(", tmp, ")==='number'?Math.floor(", tmp, ")");
                errmsg = "Expected Integer";
            } else {
                gen.out(",", gen.getClAlias(), "is$(", tmp, ",");
                if (t.isTypeParameter() && typeArguments != null
                        && typeArguments.containsKey(t.getDeclaration())) {
                    t = typeArguments.get(t.getDeclaration());
                }
                TypeUtils.typeNameOrList(term, t, gen, skipSelfDecl);
                gen.out(")?", tmp);
                errmsg = "Expected " + t.getProducedTypeQualifiedName();
            }
            gen.out(":function(){throw new TypeError('", errmsg, " (",
                    term.getUnit().getFilename(), " ", term.getLocation(), ")')}())");
        }
    }

    public static void encodeParameterListForRuntime(final boolean resolveTargs,
            final Node n, final ParameterList plist, final GenerateJsVisitor gen) {
        boolean first = true;
        gen.out("[");
        for (Parameter p : plist.getParameters()) {
            if (first) first=false; else gen.out(",");
            gen.out("{", MetamodelGenerator.KEY_NAME, ":'", p.getName(), "',");
            gen.out(MetamodelGenerator.KEY_METATYPE, ":'", MetamodelGenerator.METATYPE_PARAMETER, "',");
            ProducedType ptype = p.getType();
            if (p.getModel() instanceof Method) {
                gen.out("$pt:'f',");
                ptype = ((Method)p.getModel()).getTypedReference().getFullType();
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
            metamodelTypeNameOrList(resolveTargs, n, gen.getCurrentPackage(), ptype, gen);
            if (p.getModel().getAnnotations() != null && !p.getModel().getAnnotations().isEmpty()) {
                new ModelAnnotationGenerator(gen, p.getModel(), n).generateAnnotations();
            }
            gen.out("}");
        }
        gen.out("]");
    }

    private static Unit getUnit(ProducedType pt) {
        if (pt.isClassOrInterface()) {
            return pt.getDeclaration().getUnit();
        } else if (pt.isUnion()) {
            for (ProducedType ct : pt.getCaseTypes()) {
                Unit u = getUnit(ct);
                if (u != null) {
                    return u;
                }
            }
        } else if (pt.isIntersection()) {
            for (ProducedType st : pt.getSatisfiedTypes()) {
                Unit u = getUnit(st);
                if (u != null) {
                    return u;
                }
            }
        }
        return null;
    }

    /** Turns a Tuple type into a parameter list. */
    public static List<Parameter> convertTupleToParameters(ProducedType _tuple) {
        final ArrayList<Parameter> rval = new ArrayList<>();
        int pos = 0;
        final ProducedType empty = getUnit(_tuple).getEmptyType();
        while (_tuple != null && !(_tuple.isSubtypeOf(empty) || _tuple.isTypeParameter())) {
            Parameter _p = null;
            if (isTuple(_tuple)) {
                _p = new Parameter();
                _p.setModel(new Value());
                if (_tuple.isUnion()) {
                    //Handle union types for defaulted parameters
                    for (ProducedType mt : _tuple.getCaseTypes()) {
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
            } else if (isSequential(_tuple)) {
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
    private static boolean isTuple(ProducedType pt) {
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

    private static boolean isSequential(ProducedType pt) {
        return pt.isClassOrInterface() && pt.getDeclaration().inherits(
                pt.getDeclaration().getUnit().getSequentialDeclaration());
    }

    /** This method encodes the type parameters of a Tuple in the same way
     * as a parameter list for runtime. */
    private static void encodeTupleAsParameterListForRuntime(final boolean resolveTargs, final Node node,
            ProducedType _tuple, boolean nameAndMetatype, GenerateJsVisitor gen) {
        gen.out("[");
        int pos = 1;
        final ProducedType empty = node.getUnit().getEmptyType();
        while (_tuple != null && !(_tuple.isSubtypeOf(empty) || _tuple.isTypeParameter())) {
            if (pos > 1) gen.out(",");
            pos++;
            if (nameAndMetatype) {
                gen.out("{", MetamodelGenerator.KEY_NAME, ":'p", Integer.toString(pos), "',");
                gen.out(MetamodelGenerator.KEY_METATYPE, ":'", MetamodelGenerator.METATYPE_PARAMETER, "',");
                gen.out(MetamodelGenerator.KEY_TYPE, ":");
            }
            if (isTuple(_tuple)) {
                if (_tuple.isUnion()) {
                    //Handle union types for defaulted parameters
                    for (ProducedType mt : _tuple.getCaseTypes()) {
                        if (mt.isTuple()) {
                            metamodelTypeNameOrList(resolveTargs, node, gen.getCurrentPackage(),
                                    mt.getTypeArgumentList().get(1), gen);
                            _tuple = mt.getTypeArgumentList().get(2);
                            break;
                        }
                    }
                    if (nameAndMetatype) {
                        gen.out(",", MetamodelGenerator.KEY_DEFAULT,":1");
                    }
                } else {
                    metamodelTypeNameOrList(resolveTargs, node, gen.getCurrentPackage(),
                            _tuple.getTypeArgumentList().get(1), gen);
                    _tuple = _tuple.getTypeArgumentList().get(2);
                }
            } else if (isSequential(_tuple)) {
                ProducedType _t2 = _tuple.getSupertype(node.getUnit().getSequenceDeclaration());
                final int seq;
                if (_t2 == null) {
                    _t2 = _tuple.getSupertype(node.getUnit().getSequentialDeclaration());
                    seq = 1;
                } else {
                    seq = 2;
                }
                //Handle Sequence, for nonempty variadic parameters
                metamodelTypeNameOrList(resolveTargs, node, gen.getCurrentPackage(), _t2.getTypeArgumentList().get(0), gen);
                if (nameAndMetatype) {
                    gen.out(",seq:", Integer.toString(seq));
                }
                _tuple = null;
            } else if (_tuple.isUnion()) {
                metamodelTypeNameOrList(resolveTargs, node, gen.getCurrentPackage(), _tuple, gen);
                _tuple=null;
            } else {
                gen.out("\n/*WARNING3! Tuple is actually ", _tuple.getProducedTypeQualifiedName(), "*/");
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
            ProducedType _callable, GenerateJsVisitor gen) {
        if (_callable.getCaseTypes() != null) {
            for (ProducedType pt : _callable.getCaseTypes()) {
                if (pt.getProducedTypeQualifiedName().startsWith("ceylon.language::Callable<")) {
                    _callable = pt;
                    break;
                }
            }
        } else if (_callable.getSatisfiedTypes() != null) {
            for (ProducedType pt : _callable.getSatisfiedTypes()) {
                if (pt.getProducedTypeQualifiedName().startsWith("ceylon.language::Callable<")) {
                    _callable = pt;
                    break;
                }
            }
        }
        if (!_callable.getProducedTypeQualifiedName().contains("ceylon.language::Callable<")) {
            gen.out("[/*WARNING1: got ", _callable.getProducedTypeQualifiedName(), " instead of Callable*/]");
            return;
        }
        List<ProducedType> targs = _callable.getTypeArgumentList();
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

    /** Output a metamodel map for runtime use. */
    public static void encodeForRuntime(final Declaration d, final Tree.AnnotationList annotations,
            final GenerateJsVisitor gen) {
        encodeForRuntime(annotations, d, gen, new RuntimeMetamodelAnnotationGenerator() {
            @Override public void generateAnnotations() {
                outputAnnotationsFunction(annotations, d, gen);
            }
        });
    }

    /** Returns the list of keys to get from the package to the declaration, in the model. */
    public static List<String> generateModelPath(final Declaration d) {
        final ArrayList<String> sb = new ArrayList<>();
        final String pkgName = d.getUnit().getPackage().getNameAsString();
        sb.add(Module.LANGUAGE_MODULE_NAME.equals(pkgName)?"$":pkgName);
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
                sb.add(i, TypeUtils.modelName(p));
                //Build the path in reverse
                if (!p.isToplevel()) {
                    if (p instanceof Class) {
                        sb.add(i, p.isAnonymous() ? MetamodelGenerator.KEY_OBJECTS : MetamodelGenerator.KEY_CLASSES);
                    } else if (p instanceof com.redhat.ceylon.model.typechecker.model.Interface) {
                        sb.add(i, MetamodelGenerator.KEY_INTERFACES);
                    } else if (p instanceof Method) {
                        sb.add(i, MetamodelGenerator.KEY_METHODS);
                    } else if (p instanceof TypeAlias || p instanceof Setter) {
                        sb.add(i, MetamodelGenerator.KEY_ATTRIBUTES);
                    } else if (p instanceof Constructor) {
                        sb.add(i, MetamodelGenerator.KEY_CONSTRUCTORS);
                    } else { //It's a value
                        TypeDeclaration td=((TypedDeclaration)p).getTypeDeclaration();
                        sb.add(i, (td!=null&&td.isAnonymous())? MetamodelGenerator.KEY_OBJECTS
                                : MetamodelGenerator.KEY_ATTRIBUTES);
                    }
                }
                p = Util.getContainingDeclaration(p);
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
        gen.out("function(){return{mod:$CCMM$");
        List<TypeParameter> tparms = d instanceof Generic ? ((Generic)d).getTypeParameters() : null;
        List<ProducedType> satisfies = null;
        List<ProducedType> caseTypes = null;
        if (d instanceof Class) {
            Class _cd = (Class)d;
            if (_cd.getExtendedType() != null) {
                gen.out(",'super':");
                metamodelTypeNameOrList(false, that, d.getUnit().getPackage(), _cd.getExtendedType(), gen);
            }
            //Parameter types
            if (_cd.getParameterList()!=null) {
                gen.out(",", MetamodelGenerator.KEY_PARAMS, ":");
                encodeParameterListForRuntime(false, that, _cd.getParameterList(), gen);
            }
            satisfies = _cd.getSatisfiedTypes();
            caseTypes = _cd.getCaseTypes();

        } else if (d instanceof com.redhat.ceylon.model.typechecker.model.Interface) {

            satisfies = ((com.redhat.ceylon.model.typechecker.model.Interface) d).getSatisfiedTypes();
            caseTypes = ((com.redhat.ceylon.model.typechecker.model.Interface) d).getCaseTypes();

        } else if (d instanceof MethodOrValue) {

            gen.out(",", MetamodelGenerator.KEY_TYPE, ":");
            //This needs a new setting to resolve types but not type parameters
            metamodelTypeNameOrList(false, that, d.getUnit().getPackage(), ((MethodOrValue)d).getType(), gen);
            if (d instanceof Method) {
                gen.out(",", MetamodelGenerator.KEY_PARAMS, ":");
                //Parameter types of the first parameter list
                encodeParameterListForRuntime(false, that, ((Method)d).getParameterLists().get(0), gen);
                tparms = ((Method) d).getTypeParameters();
            }

        } else if (d instanceof Constructor) {
            gen.out(",", MetamodelGenerator.KEY_PARAMS, ":");
            encodeParameterListForRuntime(false, that, ((Constructor)d).getParameterLists().get(0), gen);
        }
        if (!d.isToplevel()) {
            //Find the first container that is a Declaration
            Declaration _cont = Util.getContainingDeclaration(d);
            gen.out(",$cont:");
            boolean generateName = true;
            if (_cont.getName() != null && _cont.getName().startsWith("anonymous#")) {
                //Anon functions don't have metamodel so go up until we find a non-anon container
                Declaration _supercont = Util.getContainingDeclaration(_cont);
                while (_supercont != null && _supercont.getName() != null
                        && _supercont.getName().startsWith("anonymous#")) {
                    _supercont = Util.getContainingDeclaration(_supercont);
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
                    gen.out(gen.getNames().name(_cont));
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
            for (ProducedType st : satisfies) {
                if (!first)gen.out(",");
                first=false;
                metamodelTypeNameOrList(false, that, d.getUnit().getPackage(), st, gen);
            }
            gen.out("]");
        }
        if (caseTypes != null && !caseTypes.isEmpty()) {
            gen.out(",of:[");
            boolean first = true;
            for (ProducedType st : caseTypes) {
                if (!first)gen.out(",");
                first=false;
                if (st.getDeclaration().isAnonymous()) {
                    gen.out(gen.getNames().getter(st.getDeclaration(), true));
                } else {
                    metamodelTypeNameOrList(false, that, d.getUnit().getPackage(), st, gen);
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
            gen.out(tp.getName(), "$", tp.getDeclaration().getName(), ":{");
            if (tp.isCovariant()) {
                gen.out(MetamodelGenerator.KEY_DS_VARIANCE, ":'out'");
                comma = true;
            } else if (tp.isContravariant()) {
                gen.out(MetamodelGenerator.KEY_DS_VARIANCE, ":'in'");
                comma = true;
            }
            List<ProducedType> typelist = tp.getSatisfiedTypes();
            if (typelist != null && !typelist.isEmpty()) {
                if (comma)gen.out(",");
                gen.out(MetamodelGenerator.KEY_SATISFIES, ":[");
                boolean first2 = true;
                for (ProducedType st : typelist) {
                    if (!first2)gen.out(",");
                    first2=false;
                    metamodelTypeNameOrList(false, node, d.getUnit().getPackage(), st, gen);
                }
                gen.out("]");
                comma = true;
            }
            typelist = tp.getCaseTypes();
            if (typelist != null && !typelist.isEmpty()) {
                if (comma)gen.out(",");
                gen.out("of:[");
                boolean first3 = true;
                for (ProducedType st : typelist) {
                    if (!first3)gen.out(",");
                    first3=false;
                    metamodelTypeNameOrList(false, node, d.getUnit().getPackage(), st, gen);
                }
                gen.out("]");
                comma = true;
            }
            if (tp.getDefaultTypeArgument() != null) {
                if (comma)gen.out(",");
                gen.out("def:");
                metamodelTypeNameOrList(false, node, d.getUnit().getPackage(), tp.getDefaultTypeArgument(), gen);
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
            final com.redhat.ceylon.model.typechecker.model.Package pkg,
            ProducedType pt, GenerateJsVisitor gen) {
        if (pt == null) {
            //In dynamic blocks we sometimes get a null producedType
            pt = node.getUnit().getAnythingType();
        }
        if (!outputMetamodelTypeList(resolveTargsFromScope, node, pkg, pt, gen)) {
            TypeDeclaration type = pt.getDeclaration();
            if (pt.isTypeParameter()) {
                Declaration tpowner = ((TypeParameter)type).getDeclaration();
                if (resolveTargsFromScope && Util.contains((Scope)tpowner, node.getScope())) {
                    //Attempt to resolve this to an argument if the scope allows for it
                    if (tpowner instanceof TypeDeclaration) {
                        gen.out(gen.getNames().self((TypeDeclaration)tpowner), ".",
                                type.getNameAsString(), "$", tpowner.getName());
                    } else if (tpowner instanceof Method) {
                        gen.out(gen.getNames().typeArgsParamName((Method)tpowner), ".",
                                type.getNameAsString(), "$", tpowner.getName());
                    }
                } else {
                    gen.out("'", type.getNameAsString(), "$", tpowner.getName(), "'");
                }
            } else if (pt.isTypeAlias()) {
                outputQualifiedTypename(node, gen.isImported(pkg, type), pt, gen, false);
            } else {
                gen.out("{t:");
                outputQualifiedTypename(node, gen.isImported(pkg, type), pt, gen, false);
                //Type Parameters
                if (!pt.getTypeArgumentList().isEmpty()) {
                    gen.out(",a:{");
                    boolean first = true;
                    for (Map.Entry<TypeParameter, ProducedType> e : pt.getTypeArguments().entrySet()) {
                        if (first) first=false; else gen.out(",");
                        gen.out(e.getKey().getNameAsString(), "$", e.getKey().getDeclaration().getName(), ":");
                        metamodelTypeNameOrList(resolveTargsFromScope, node, pkg, e.getValue(), gen);
                    }
                    gen.out("}");
                }
                gen.out("}");
            }
        }
    }

    /** Appends an object with the type's type and list of union/intersection types; works only with union,
     * intersection and tuple types.
     * @return true if output was generated, false otherwise (it was a regular type) */
    static boolean outputMetamodelTypeList(final boolean resolveTargs, final Node node,
            final com.redhat.ceylon.model.typechecker.model.Package pkg,
            ProducedType pt, GenerateJsVisitor gen) {
        final List<ProducedType> subs;
        if (pt.isIntersection()) {
            gen.out("{t:'i");
            subs = pt.getSatisfiedTypes();
        } else if (pt.isUnion()) {
            //It still could be a Tuple with first optional type
            List<ProducedType> cts = pt.getCaseTypes();
            if (cts.size()==2) {
                ProducedType ct1 = cts.get(0);
                ProducedType ct2 = cts.get(1);
                if (ct1.isEmpty() && ct2.isTuple() ||
                    ct2.isEmpty() && ct1.isTuple()) {
                    //yup...
                    gen.out("{t:'T',l:");
                    encodeTupleAsParameterListForRuntime(resolveTargs, node, pt,false,gen);
                    gen.out("}");
                    return true;
                }
            }
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
        for (ProducedType t : subs) {
            if (!first) gen.out(",");
            metamodelTypeNameOrList(resolveTargs, node, pkg, t, gen);
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
     * @param d The declaration to which the annotations belong.
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
                final String qn = ((Tree.StaticMemberOrTypeExpression)iter.next().getPrimary()).getDeclaration().getQualifiedNameString();
                if (qn.startsWith("ceylon.language::") && MetamodelGenerator.annotationBits.contains(qn.substring(17))) {
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
                    gen.out(gen.getClAlias(), "doc$($CCMM$,", ptmd);
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
                    final Declaration ad = d.getUnit().getPackage().getMemberOrParameter(d.getUnit(), a.getName(), null, false);
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
                if (ad instanceof Method) {
                    if (first) first=false; else gen.out(",");
                    final boolean isDoc = "ceylon.language::doc".equals(ad.getQualifiedNameString());
                    if (!isDoc) {
                        gen.qualify(node, ad);
                        gen.out(gen.getNames().name(ad), "(");
                    }
                    if (a.getPositionalArguments() == null) {
                        for (Parameter p : ((Method)ad).getParameterLists().get(0).getParameters()) {
                            String v = a.getNamedArguments().get(p.getName());
                            gen.out(v == null ? "undefined" : v);
                        }
                    } else {
                        if (isDoc) {
                            //Use ref if it's too long
                            final String ref = pathToModelDoc(d);
                            final String doc = a.getPositionalArguments().get(0);
                            if (ref != null && ref.length() < doc.length()) {
                                gen.out(gen.getClAlias(), "doc$($CCMM$,", ref);
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
     * @param left The left term of the operator
     * @param right The right term of the operator
     * @param methodName The name of the method that is to be invoked
     * @param rightTpName The name of the type argument on the right term
     * @param leftTpName The name of the type parameter on the method
     * @return A map with the type parameter of the method as key
     * and the produced type belonging to the type argument of the term on the right. */
    public static Map<TypeParameter, ProducedType> mapTypeArgument(final Tree.BinaryOperatorExpression expr,
            final String methodName, final String rightTpName, final String leftTpName) {
        Method md = (Method)expr.getLeftTerm().getTypeModel().getDeclaration().getMember(methodName, null, false);
        if (md == null) {
            expr.addUnexpectedError("Left term of intersection operator should have method named " + methodName, Backend.JavaScript);
            return null;
        }
        Map<TypeParameter, ProducedType> targs = expr.getRightTerm().getTypeModel().getTypeArguments();
        ProducedType otherType = null;
        for (TypeParameter tp : targs.keySet()) {
            if (tp.getName().equals(rightTpName)) {
                otherType = targs.get(tp);
                break;
            }
        }
        if (otherType == null) {
            expr.addUnexpectedError("Right term of intersection operator should have type parameter named " + rightTpName, Backend.JavaScript);
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
            expr.addUnexpectedError("Left term of intersection should have type parameter named " + leftTpName, Backend.JavaScript);
        }
        targs.put(mtp, otherType);
        return targs;
    }

    /** Returns the qualified name of a declaration, skipping any containing methods. */
    public static String qualifiedNameSkippingMethods(Declaration d) {
        final StringBuilder p = new StringBuilder(d.getName());
        Scope s = d.getContainer();
        while (s != null) {
            if (s instanceof com.redhat.ceylon.model.typechecker.model.Package) {
                final String pkname = ((com.redhat.ceylon.model.typechecker.model.Package)s).getNameAsString();
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
        if (dname == null && d instanceof com.redhat.ceylon.model.typechecker.model.Constructor) {
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
        return acceptNative(node.getDeclarationModel());
    }
    
    /**
     * Returns true if the declaration is:
     *  - not native or
     *  - native with a "js" argument
     *  - native with no argument and all its overloads are also native without arguments
     */
    public static boolean acceptNative(Declaration decl) {
        return !decl.isNative() || isForBackend(decl) || isNativeExternal(decl);
    }
    
    /**
     * Checks that the declaration is marked "native" and has a Ceylon implementation
     * meant for the JavaScript backend
     */
    public static boolean isForBackend(Declaration decl) {
        String backend = decl.getNative();
        return backend != null && backend.equals(Backend.JavaScript.nativeAnnotation);
    }
    
    /**
     * Checks that the given declaration is defined in an external JavaScript file.
     * It does this by assuming that an external implementation will have a declaration
     * in Ceylon code with a "native" annotation that has NO arguments. It will
     * also have no associated "overloads" (there are no other declaration found
     * in the Ceylon code with the same name)
     */
    public static boolean isNativeExternal(Declaration decl) {
        boolean isNative = decl.isNative() && decl.getNative().isEmpty();
        if (isNative) {
            if (decl.getOverloads() != null) {
                for (Declaration d : decl.getOverloads()) {
                    if (!d.getNative().isEmpty()) {
                        isNative = false;
                        break;
                    }
                }
            }
        }
        return isNative;
    }
}
