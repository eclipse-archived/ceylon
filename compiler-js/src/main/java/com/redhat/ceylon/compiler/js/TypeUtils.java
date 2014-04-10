package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.loader.MetamodelGenerator;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Util;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

/** A convenience class to help with the handling of certain type declarations. */
public class TypeUtils {

    final TypeDeclaration tuple;
    final TypeDeclaration iterable;
    final TypeDeclaration sequential;
    final TypeDeclaration numeric;
    final TypeDeclaration _integer;
    final TypeDeclaration _boolean;
    final TypeDeclaration _float;
    final TypeDeclaration _null;
    final TypeDeclaration anything;
    final TypeDeclaration callable;
    final TypeDeclaration empty;
    final TypeDeclaration metaClass;
    final TypeDeclaration destroyable;

    TypeUtils(Module languageModule) {
        com.redhat.ceylon.compiler.typechecker.model.Package pkg = languageModule.getPackage("ceylon.language");
        tuple = (TypeDeclaration)pkg.getDirectMember("Tuple", null, false);
        iterable = (TypeDeclaration)pkg.getDirectMember("Iterable", null, false);
        sequential = (TypeDeclaration)pkg.getDirectMember("Sequential", null, false);
        numeric = (TypeDeclaration)pkg.getDirectMember("Numeric", null, false);
        _integer = (TypeDeclaration)pkg.getDirectMember("Integer", null, false);
        _boolean = (TypeDeclaration)pkg.getDirectMember("Boolean", null, false);
        _float = (TypeDeclaration)pkg.getDirectMember("Float", null, false);
        _null = (TypeDeclaration)pkg.getDirectMember("Null", null, false);
        anything = (TypeDeclaration)pkg.getDirectMember("Anything", null, false);
        callable = (TypeDeclaration)pkg.getDirectMember("Callable", null, false);
        empty = (TypeDeclaration)pkg.getDirectMember("Empty", null, false);
        destroyable = (TypeDeclaration)pkg.getDirectMember("Destroyable", null, false);
        pkg = languageModule.getPackage("ceylon.language.meta.model");
        metaClass = (TypeDeclaration)pkg.getDirectMember("Class", null, false);
    }

    /** Prints the type arguments, usually for their reification. */
    public static void printTypeArguments(final Node node, final Map<TypeParameter,ProducedType> targs,
            final GenerateJsVisitor gen, final boolean skipSelfDecl) {
        gen.out("{");
        boolean first = true;
        for (Map.Entry<TypeParameter,ProducedType> e : targs.entrySet()) {
            if (first) {
                first = false;
            } else {
                gen.out(",");
            }
            gen.out(e.getKey().getName(), "$", e.getKey().getDeclaration().getName(), ":");
            final ProducedType pt = e.getValue();
            if (pt == null) {
                gen.out("'", e.getKey().getName(), "'");
            } else if (!outputTypeList(node, pt, gen, skipSelfDecl)) {
                boolean hasParams = pt.getTypeArgumentList() != null && !pt.getTypeArgumentList().isEmpty();
                boolean closeBracket = false;
                final TypeDeclaration d = pt.getDeclaration();
                if (d instanceof TypeParameter) {
                    resolveTypeParameter(node, (TypeParameter)d, gen, skipSelfDecl);
                } else {
                    closeBracket = pt.getDeclaration() instanceof TypeAlias==false;
                    if (closeBracket)gen.out("{t:");
                    outputQualifiedTypename(
                            gen.isImported(node == null ? null : node.getUnit().getPackage(), pt.getDeclaration()),
                            pt, gen, skipSelfDecl);
                }
                if (hasParams) {
                    gen.out(",a:");
                    printTypeArguments(node, pt.getTypeArguments(), gen, skipSelfDecl);
                }
                if (closeBracket) {
                    gen.out("}");
                }
            }
        }
        gen.out("}");
    }

    static void outputQualifiedTypename(final boolean imported, final ProducedType pt,
            final GenerateJsVisitor gen, final boolean skipSelfDecl) {
        TypeDeclaration t = pt.getDeclaration();
        final String qname = t.getQualifiedNameString();
        if (qname.equals("ceylon.language::Nothing")) {
            //Hack in the model means hack here as well
            gen.out(GenerateJsVisitor.getClAlias(), "Nothing");
        } else if (qname.equals("ceylon.language::null") || qname.equals("ceylon.language::Null")) {
            gen.out(GenerateJsVisitor.getClAlias(), "Null");
        } else if (pt.isUnknown()) {
            gen.out(GenerateJsVisitor.getClAlias(), "Anything");
        } else {
            //This wasn't needed but now we seem to get imported decls with no package when compiling ceylon.language.model types
            boolean qual = imported;
            final String modAlias = imported ? gen.getNames().moduleAlias(t.getUnit().getPackage().getModule()) : null;
            if (modAlias != null && !modAlias.isEmpty()) {
                gen.out(modAlias, ".");
                qual = true;
            }
            if (t.getScope() instanceof ClassOrInterface) {
                List<ClassOrInterface> parents = new ArrayList<>();
                ClassOrInterface parent = (ClassOrInterface)t.getScope();
                parents.add(0, parent);
                while (parent.getScope() instanceof ClassOrInterface) {
                    parent = (ClassOrInterface)parent.getScope();
                    parents.add(0, parent);
                }
                qual = true;
                for (ClassOrInterface p : parents) {
                    gen.out(gen.getNames().name(p), ".");
                }
            }

            if (!outputTypeList(null, pt, gen, skipSelfDecl)) {
                String tname = gen.getNames().name(t);
                if (!qual && isReservedTypename(tname)) {
                    gen.out(tname, "$");
                } else {
                    gen.out(tname);
                }
            }
        }
    }

    /** Prints out an object with a type constructor under the property "t" and its type arguments under
     * the property "a", or a union/intersection type with "u" or "i" under property "t" and the list
     * of types that compose it in an array under the property "l", or a type parameter as a reference to
     * already existing params. */
    static void typeNameOrList(final Node node, final ProducedType pt, final GenerateJsVisitor gen, final boolean skipSelfDecl) {
        TypeDeclaration type = pt.getDeclaration();
        if (!outputTypeList(node, pt, gen, skipSelfDecl)) {
            if (type instanceof TypeParameter) {
                resolveTypeParameter(node, (TypeParameter)type, gen, skipSelfDecl);
            } else if (type instanceof TypeAlias) {
                outputQualifiedTypename(gen.isImported(node == null ? null : node.getUnit().getPackage(), type), pt, gen, skipSelfDecl);
            } else {
                gen.out("{t:");
                outputQualifiedTypename(gen.isImported(node == null ? null : node.getUnit().getPackage(), type), pt, gen, skipSelfDecl);
                if (!pt.getTypeArgumentList().isEmpty()) {
                    gen.out(",a:");
                    printTypeArguments(node, pt.getTypeArguments(), gen, skipSelfDecl);
                }
                gen.out("}");
            }
        }
    }

    /** Appends an object with the type's type and list of union/intersection types. */
    static boolean outputTypeList(final Node node, final ProducedType pt, final GenerateJsVisitor gen, final boolean skipSelfDecl) {
        TypeDeclaration d = pt.getDeclaration();
        final List<ProducedType> subs;
        if (d instanceof IntersectionType) {
            gen.out("{t:'i");
            subs = d.getSatisfiedTypes();
        } else if (d instanceof UnionType) {
            gen.out("{t:'u");
            subs = d.getCaseTypes();
        } else if ("ceylon.language::Tuple".equals(d.getQualifiedNameString())) {
            gen.out("{t:'T");
            subs = getTupleTypes(pt);
        } else {
            return false;
        }
        gen.out("',l:[");
        boolean first = true;
        for (ProducedType t : subs) {
            if (!first) gen.out(",");
            typeNameOrList(node, t, gen, skipSelfDecl);
            first = false;
        }
        gen.out("]}");
        return true;
    }

    /** Finds the owner of the type parameter and outputs a reference to the corresponding type argument. */
    static void resolveTypeParameter(final Node node, final TypeParameter tp,
            final GenerateJsVisitor gen, final boolean skipSelfDecl) {
        Scope parent = node.getScope();
        int outers = 0;
        while (parent != null && parent != tp.getContainer()) {
            if (parent instanceof TypeDeclaration) {
                outers++;
            }
            parent = parent.getScope();
        }
        if (tp.getContainer() instanceof ClassOrInterface) {
            if (parent == tp.getContainer()) {
                if (!skipSelfDecl) {
                    ClassOrInterface ontoy = Util.getContainingClassOrInterface(node.getScope());
                    if (((TypeDeclaration)ontoy).isAnonymous()) {
                        gen.out(gen.getNames().self((TypeDeclaration)parent));
                    } else {
                        gen.out(gen.getNames().self((TypeDeclaration)ontoy));
                        for (int i = 0; i < outers; i++) {
                            gen.out(".$$outer");
                        }
                    }
                    gen.out(".");
                }
                gen.out("$$targs$$.", tp.getName(), "$", tp.getDeclaration().getName());
            } else {
                //This can happen in expressions such as Singleton(n) when n is dynamic
                gen.out("{/*NO PARENT*/t:", GenerateJsVisitor.getClAlias(), "Anything}");
            }
        } else if (tp.getContainer() instanceof TypeAlias) {
            if (parent == tp.getContainer()) {
                gen.out("'", tp.getName(), "$", tp.getDeclaration().getName(), "'");
            } else {
                //This can happen in expressions such as Singleton(n) when n is dynamic
                gen.out("{/*NO PARENT ALIAS*/t:", GenerateJsVisitor.getClAlias(), "Anything}");
            }
        } else {
            //it has to be a method, right?
            //We need to find the index of the parameter where the argument occurs
            //...and it could be null...
            int plistCount = -1;
            ProducedType type = null;
            for (Iterator<ParameterList> iter0 = ((Method)tp.getContainer()).getParameterLists().iterator();
                    type == null && iter0.hasNext();) {
                plistCount++;
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
                gen.out("$$$mptypes.", tp.getName(), "$", tp.getDeclaration().getName());
            } else {
                gen.out("/*METHOD TYPEPARM plist ", Integer.toString(plistCount), "#",
                        tp.getName(), "*/'", type.getProducedTypeQualifiedName(), "'");
            }
        }
    }

    static ProducedType typeContainsTypeParameter(ProducedType td, TypeParameter tp) {
        TypeDeclaration d = td.getDeclaration();
        if (d == tp) {
            return td;
        } else if (d instanceof UnionType || d instanceof IntersectionType) {
            List<ProducedType> comps = td.getCaseTypes();
            if (comps == null) comps = td.getSupertypes();
            for (ProducedType sub : comps) {
                td = typeContainsTypeParameter(sub, tp);
                if (td != null) {
                    return td;
                }
            }
        } else if (d instanceof ClassOrInterface) {
            for (ProducedType sub : td.getTypeArgumentList()) {
                if (typeContainsTypeParameter(sub, tp) != null) {
                    return td;
                }
            }
        }
        return null;
    }

    static boolean isReservedTypename(String typeName) {
        return JsCompiler.compilingLanguageModule && (typeName.equals("Object") || typeName.equals("Number")
                || typeName.equals("Array")) || typeName.equals("String") || typeName.equals("Boolean");
    }

    /** Find the type with the specified declaration among the specified type's supertypes, case types, satisfied types, etc. */
    static ProducedType findSupertype(TypeDeclaration d, ProducedType pt) {
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

    static Map<TypeParameter, ProducedType> matchTypeParametersWithArguments(List<TypeParameter> params, List<ProducedType> targs) {
        if (params != null && targs != null && params.size() == targs.size()) {
            HashMap<TypeParameter, ProducedType> r = new HashMap<TypeParameter, ProducedType>();
            for (int i = 0; i < targs.size(); i++) {
                r.put(params.get(i), targs.get(i));
            }
            return r;
        }
        return null;
    }

    Map<TypeParameter, ProducedType> wrapAsIterableArguments(ProducedType pt) {
        HashMap<TypeParameter, ProducedType> r = new HashMap<TypeParameter, ProducedType>();
        r.put(iterable.getTypeParameters().get(0), pt);
        r.put(iterable.getTypeParameters().get(1), _null.getType());
        return r;
    }

    static boolean isUnknown(ProducedType pt) {
        return pt == null || pt.isUnknown();
    }
    static boolean isUnknown(Parameter param) {
        return param == null || isUnknown(param.getType());
    }
    static boolean isUnknown(Declaration d) {
        return d == null || d.getQualifiedNameString().equals("UnknownType");
    }

    /** Generates the code to throw an Exception if a dynamic object is not of the specified type. */
    static void generateDynamicCheck(final Tree.Term term, final ProducedType t, final GenerateJsVisitor gen, final boolean skipSelfDecl) {
        String tmp = gen.getNames().createTempVariable();
        gen.out("(", tmp, "=");
        term.visit(gen);
        gen.out(",", GenerateJsVisitor.getClAlias(), "$is(", tmp, ",");
        TypeUtils.typeNameOrList(term, t, gen, skipSelfDecl);
        gen.out(")?", tmp, ":");
        gen.generateThrow("dynamic objects cannot be used here", term);
        gen.out(")");
    }

    static void encodeParameterListForRuntime(Node n, ParameterList plist, GenerateJsVisitor gen) {
        boolean first = true;
        gen.out("[");
        for (Parameter p : plist.getParameters()) {
            if (first) first=false; else gen.out(",");
            gen.out("{", MetamodelGenerator.KEY_NAME, ":'", p.getName(), "',");
            gen.out(MetamodelGenerator.KEY_METATYPE, ":'", MetamodelGenerator.METATYPE_PARAMETER, "',");
            if (p.getModel() instanceof Method) {
                gen.out("$pt:'f',");
            }
            if (p.isSequenced()) {
                gen.out("seq:1,");
            }
            if (p.isDefaulted()) {
                gen.out(MetamodelGenerator.KEY_DEFAULT, ":1,");
            }
            gen.out(MetamodelGenerator.KEY_TYPE, ":");
            metamodelTypeNameOrList(gen.getCurrentPackage(), p.getType(), gen);
            new ModelAnnotationGenerator(gen, p.getModel(), n).generateAnnotations();
            gen.out("}");
        }
        gen.out("]");
    }

    /** Turns a Tuple type into a parameter list. */
    List<Parameter> convertTupleToParameters(ProducedType _tuple) {
        ArrayList<Parameter> rval = new ArrayList<>();
        int pos = 0;
        TypeDeclaration tdecl = _tuple.getDeclaration();
        while (!(empty.equals(tdecl) || tdecl instanceof TypeParameter)) {
            Parameter _p = null;
            if (tuple.equals(tdecl) || (tdecl.getCaseTypeDeclarations() != null
                    && tdecl.getCaseTypeDeclarations().size()==2
                    && tdecl.getCaseTypeDeclarations().contains(tuple))) {
                _p = new Parameter();
                _p.setModel(new Value());
                if (tuple.equals(tdecl)) {
                    _p.getModel().setType(_tuple.getTypeArgumentList().get(1));
                    _tuple = _tuple.getTypeArgumentList().get(2);
                } else {
                    //Handle union types for defaulted parameters
                    for (ProducedType mt : _tuple.getCaseTypes()) {
                        if (tuple.equals(mt.getDeclaration())) {
                            _p.getModel().setType(mt.getTypeArgumentList().get(1));
                            _tuple = mt.getTypeArgumentList().get(2);
                            break;
                        }
                    }
                    _p.setDefaulted(true);
                }
            } else if (tdecl.inherits(sequential)) {
                //Handle Sequence, for nonempty variadic parameters
                _p = new Parameter();
                _p.setModel(new Value());
                _p.getModel().setType(_tuple.getTypeArgumentList().get(0));
                _p.setSequenced(true);
                _tuple = empty.getType();
            }
            else {
                if (pos > 100) {
                    return rval;
                }
            }
            if (_tuple != null) tdecl = _tuple.getDeclaration();
            if (_p != null) {
                _p.setName("arg" + pos);
                rval.add(_p);
            }
            pos++;
        }
        return rval;
    }

    /** This method encodes the type parameters of a Tuple in the same way
     * as a parameter list for runtime. */
    private static void encodeTupleAsParameterListForRuntime(ProducedType _tuple, boolean nameAndMetatype, GenerateJsVisitor gen) {
        gen.out("[");
        int pos = 1;
        TypeDeclaration tdecl = _tuple.getDeclaration();
        while (!(gen.getTypeUtils().empty.equals(tdecl) || tdecl instanceof TypeParameter)) {
            if (pos > 1) gen.out(",");
            gen.out("{");
            pos++;
            if (nameAndMetatype) {
                gen.out(MetamodelGenerator.KEY_NAME, ":'p", Integer.toString(pos), "',");
                gen.out(MetamodelGenerator.KEY_METATYPE, ":'", MetamodelGenerator.METATYPE_PARAMETER, "',");
            }
            gen.out(MetamodelGenerator.KEY_TYPE, ":");
            if (gen.getTypeUtils().tuple.equals(tdecl) || (tdecl.getCaseTypeDeclarations() != null
                    && tdecl.getCaseTypeDeclarations().size()==2
                    && tdecl.getCaseTypeDeclarations().contains(gen.getTypeUtils().tuple))) {
                if (gen.getTypeUtils().tuple.equals(tdecl)) {
                    metamodelTypeNameOrList(gen.getCurrentPackage(), _tuple.getTypeArgumentList().get(1), gen);
                    _tuple = _tuple.getTypeArgumentList().get(2);
                } else {
                    //Handle union types for defaulted parameters
                    for (ProducedType mt : _tuple.getCaseTypes()) {
                        if (gen.getTypeUtils().tuple.equals(mt.getDeclaration())) {
                            metamodelTypeNameOrList(gen.getCurrentPackage(), mt.getTypeArgumentList().get(1), gen);
                            _tuple = mt.getTypeArgumentList().get(2);
                            break;
                        }
                    }
                    gen.out(",", MetamodelGenerator.KEY_DEFAULT,":1");
                }
            } else if (tdecl.inherits(gen.getTypeUtils().sequential)) {
                //Handle Sequence, for nonempty variadic parameters
                metamodelTypeNameOrList(gen.getCurrentPackage(), _tuple.getTypeArgumentList().get(0), gen);
                gen.out(",seq:1");
                _tuple = gen.getTypeUtils().empty.getType();
            } else if (tdecl instanceof UnionType) {
                metamodelTypeNameOrList(gen.getCurrentPackage(), _tuple, gen);
                tdecl = gen.getTypeUtils().empty; _tuple=null;
            } else {
                gen.out("\n/*WARNING3! Tuple is actually ", _tuple.getProducedTypeQualifiedName(), ", ", tdecl.getName(),"*/");
                if (pos > 100) {
                    break;
                }
            }
            gen.out("}");
            if (_tuple != null) tdecl = _tuple.getDeclaration();
        }
        gen.out("]");
    }
    /** This method encodes the Arguments type argument of a Callable the same way
     * as a parameter list for runtime. */
    static void encodeCallableArgumentsAsParameterListForRuntime(ProducedType _callable, GenerateJsVisitor gen) {
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
        encodeTupleAsParameterListForRuntime(targs.get(1), true, gen);
    }

    static void encodeForRuntime(Node that, final Declaration d, final GenerateJsVisitor gen) {
        if (d.getAnnotations() == null || d.getAnnotations().isEmpty()) {
            encodeForRuntime(that, d, gen, null);
        } else {
            encodeForRuntime(that, d, gen, new ModelAnnotationGenerator(gen, d, that));
        }
    }

    /** Output a metamodel map for runtime use. */
    static void encodeForRuntime(final Declaration d, final Tree.AnnotationList annotations, final GenerateJsVisitor gen) {
        final boolean include = annotations != null && !(annotations.getAnnotations().isEmpty() && annotations.getAnonymousAnnotation()==null);
        encodeForRuntime(annotations, d, gen, include ? new RuntimeMetamodelAnnotationGenerator() {
            @Override public void generateAnnotations() {
                gen.out(",", MetamodelGenerator.KEY_ANNOTATIONS, ":");
                outputAnnotationsFunction(annotations, d, gen);
            }
        } : null);
    }

    static List<String> generateModelPath(final Declaration d) {
        final ArrayList<String> sb = new ArrayList<>();
        final String pkgName = d.getUnit().getPackage().getNameAsString();
        sb.add("ceylon.language".equals(pkgName)?"$":pkgName);
        if (d.isToplevel()) {
            sb.add(d.getName());
        } else {
            Declaration p = d;
            final int i = sb.size();
            while (p instanceof Declaration) {
                if (p instanceof Setter) {
                    sb.add(i, "$set");
                }
                sb.add(i, p.getName());
                //Build the path in reverse
                if (!p.isToplevel()) {
                    if (p instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
                        sb.add(i, p.isAnonymous() ? MetamodelGenerator.KEY_OBJECTS : MetamodelGenerator.KEY_CLASSES);
                    } else if (p instanceof com.redhat.ceylon.compiler.typechecker.model.Interface) {
                        sb.add(i, MetamodelGenerator.KEY_INTERFACES);
                    } else if (p instanceof Method) {
                        sb.add(i, MetamodelGenerator.KEY_METHODS);
                    } else if (p instanceof TypeAlias || p instanceof Setter) {
                        sb.add(i, MetamodelGenerator.KEY_ATTRIBUTES);
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
            if (first)first=false;else gen.out(",");
            gen.out("'", p, "'");
        }
        gen.out("]");
    }

    static void encodeForRuntime(final Node that, final Declaration d, final GenerateJsVisitor gen,
            final RuntimeMetamodelAnnotationGenerator annGen) {
        gen.out("function(){return{mod:$CCMM$");
        List<TypeParameter> tparms = d instanceof TypeDeclaration ? ((TypeDeclaration)d).getTypeParameters() : null;
        List<ProducedType> satisfies = null;
        List<ProducedType> caseTypes = null;
        if (d instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
            com.redhat.ceylon.compiler.typechecker.model.Class _cd = (com.redhat.ceylon.compiler.typechecker.model.Class)d;
            if (_cd.getExtendedType() != null) {
                gen.out(",'super':");
                metamodelTypeNameOrList(d.getUnit().getPackage(), _cd.getExtendedType(), gen);
            }
            //Parameter types
            if (_cd.getParameterList()!=null) {
                gen.out(",", MetamodelGenerator.KEY_PARAMS, ":");
                encodeParameterListForRuntime(that, _cd.getParameterList(), gen);
            }
            satisfies = _cd.getSatisfiedTypes();
            caseTypes = _cd.getCaseTypes();

        } else if (d instanceof com.redhat.ceylon.compiler.typechecker.model.Interface) {

            satisfies = ((com.redhat.ceylon.compiler.typechecker.model.Interface) d).getSatisfiedTypes();
            caseTypes = ((com.redhat.ceylon.compiler.typechecker.model.Interface) d).getCaseTypes();

        } else if (d instanceof MethodOrValue) {

            gen.out(",", MetamodelGenerator.KEY_TYPE, ":");
            //This needs a new setting to resolve types but not type parameters
            metamodelTypeNameOrList(d.getUnit().getPackage(), ((MethodOrValue)d).getType(), gen);
            if (d instanceof Method) {
                gen.out(",", MetamodelGenerator.KEY_PARAMS, ":");
                //Parameter types of the first parameter list
                encodeParameterListForRuntime(that, ((Method)d).getParameterLists().get(0), gen);
                tparms = ((Method) d).getTypeParameters();
            }

        }
        if (!d.isToplevel()) {
            //Find the first container that is a Declaration
            final Declaration _cont = Util.getContainingDeclaration(d);
            gen.out(",$cont:");
            if (_cont instanceof Value) {
                if (gen.defineAsProperty(_cont)) {
                    gen.qualify(that, _cont);
                    gen.out("$prop$");
                }
                gen.out(gen.getNames().getter(_cont));
            } else if (_cont instanceof Setter) {
                gen.out("{setter:");
                if (gen.defineAsProperty(_cont)) {
                    gen.qualify(that, _cont);
                    gen.out("$prop$", gen.getNames().getter(((Setter) _cont).getGetter()), ".set");
                } else {
                    gen.out(gen.getNames().setter(((Setter) _cont).getGetter()));
                }
                gen.out("}");
            } else {
                gen.out(gen.getNames().name(_cont));
            }
        }
        if (tparms != null && !tparms.isEmpty()) {
            gen.out(",", MetamodelGenerator.KEY_TYPE_PARAMS, ":{");
            boolean first = true;
            for(TypeParameter tp : tparms) {
                boolean comma = false;
                if (!first)gen.out(",");
                first=false;
                gen.out(tp.getName(), "$", tp.getDeclaration().getName(), ":{");
                if (tp.isCovariant()) {
                    gen.out("'var':'out'");
                    comma = true;
                } else if (tp.isContravariant()) {
                    gen.out("'var':'in'");
                    comma = true;
                }
                List<ProducedType> typelist = tp.getSatisfiedTypes();
                if (typelist != null && !typelist.isEmpty()) {
                    if (comma)gen.out(",");
                    gen.out("'satisfies':[");
                    boolean first2 = true;
                    for (ProducedType st : typelist) {
                        if (!first2)gen.out(",");
                        first2=false;
                        metamodelTypeNameOrList(d.getUnit().getPackage(), st, gen);
                    }
                    gen.out("]");
                    comma = true;
                }
                typelist = tp.getCaseTypes();
                if (typelist != null && !typelist.isEmpty()) {
                    if (comma)gen.out(",");
                    gen.out("'of':[");
                    boolean first3 = true;
                    for (ProducedType st : typelist) {
                        if (!first3)gen.out(",");
                        first3=false;
                        metamodelTypeNameOrList(d.getUnit().getPackage(), st, gen);
                    }
                    gen.out("]");
                    comma = true;
                }
                if (tp.getDefaultTypeArgument() != null) {
                    if (comma)gen.out(",");
                    gen.out("'def':");
                    metamodelTypeNameOrList(d.getUnit().getPackage(), tp.getDefaultTypeArgument(), gen);
                }
                gen.out("}");
            }
            gen.out("}");
        }
        if (satisfies != null && !satisfies.isEmpty()) {
            gen.out(",satisfies:[");
            boolean first = true;
            for (ProducedType st : satisfies) {
                if (!first)gen.out(",");
                first=false;
                metamodelTypeNameOrList(d.getUnit().getPackage(), st, gen);
            }
            gen.out("]");
        }
        if (caseTypes != null && !caseTypes.isEmpty()) {
            gen.out(",of:[");
            boolean first = true;
            for (ProducedType st : caseTypes) {
                if (!first)gen.out(",");
                first=false;
                metamodelTypeNameOrList(d.getUnit().getPackage(), st, gen);
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

    /** Prints out an object with a type constructor under the property "t" and its type arguments under
     * the property "a", or a union/intersection type with "u" or "i" under property "t" and the list
     * of types that compose it in an array under the property "l", or a type parameter as a reference to
     * already existing params. */
    static void metamodelTypeNameOrList(final com.redhat.ceylon.compiler.typechecker.model.Package pkg,
            ProducedType pt, GenerateJsVisitor gen) {
        if (pt == null) {
            //In dynamic blocks we sometimes get a null producedType
            pt = ((TypeDeclaration)pkg.getModule().getLanguageModule().getDirectPackage(
                    "ceylon.language").getDirectMember("Anything", null, false)).getType();
        }
        if (!outputMetamodelTypeList(pkg, pt, gen)) {
            TypeDeclaration type = pt.getDeclaration();
            if (type instanceof TypeParameter) {
                gen.out("'", type.getNameAsString(), "$", ((TypeParameter)type).getDeclaration().getName(), "'");
            } else if (type instanceof TypeAlias) {
                outputQualifiedTypename(gen.isImported(pkg, type), pt, gen, false);
            } else {
                gen.out("{t:");
                outputQualifiedTypename(gen.isImported(pkg, type), pt, gen, false);
                //Type Parameters
                if (!pt.getTypeArgumentList().isEmpty()) {
                    gen.out(",a:{");
                    boolean first = true;
                    for (Map.Entry<TypeParameter, ProducedType> e : pt.getTypeArguments().entrySet()) {
                        if (first) first=false; else gen.out(",");
                        gen.out(e.getKey().getNameAsString(), "$", e.getKey().getDeclaration().getName(), ":");
                        metamodelTypeNameOrList(pkg, e.getValue(), gen);
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
    static boolean outputMetamodelTypeList(final com.redhat.ceylon.compiler.typechecker.model.Package pkg,
            ProducedType pt, GenerateJsVisitor gen) {
        TypeDeclaration type = pt.getDeclaration();
        final List<ProducedType> subs;
        if (type instanceof IntersectionType) {
            gen.out("{t:'i");
            subs = type.getSatisfiedTypes();
        } else if (type instanceof UnionType) {
            //It still could be a Tuple with first optional type
            List<TypeDeclaration> cts = type.getCaseTypeDeclarations();
            if (cts.size()==2 && cts.contains(gen.getTypeUtils().empty) && cts.contains(gen.getTypeUtils().tuple)) {
                //yup...
                gen.out("{t:'T',l:");
                encodeTupleAsParameterListForRuntime(pt,false,gen);
                gen.out("}");
                return true;
            }
            gen.out("{t:'u");
            subs = type.getCaseTypes();
        } else if (type.getQualifiedNameString().equals("ceylon.language::Tuple")) {
            gen.out("{t:'T',l:");
            encodeTupleAsParameterListForRuntime(pt,false, gen);
            gen.out("}");
            return true;
        } else {
            return false;
        }
        gen.out("',l:[");
        boolean first = true;
        for (ProducedType t : subs) {
            if (!first) gen.out(",");
            metamodelTypeNameOrList(pkg, t, gen);
            first = false;
        }
        gen.out("]}");
        return true;
    }

    ProducedType tupleFromParameters(List<com.redhat.ceylon.compiler.typechecker.model.Parameter> params) {
        if (params == null || params.isEmpty()) {
            return empty.getType();
        }
        ProducedType tt = empty.getType();
        ProducedType et = null;
        for (int i = params.size()-1; i>=0; i--) {
            com.redhat.ceylon.compiler.typechecker.model.Parameter p = params.get(i);
            if (et == null) {
                et = p.getType();
            } else {
                UnionType ut = new UnionType(p.getModel().getUnit());
                ArrayList<ProducedType> types = new ArrayList<>();
                if (et.getCaseTypes() == null || et.getCaseTypes().isEmpty()) {
                    types.add(et);
                } else {
                    types.addAll(et.getCaseTypes());
                }
                types.add(p.getType());
                ut.setCaseTypes(types);
                et = ut.getType();
            }
            Map<TypeParameter,ProducedType> args = new HashMap<>();
            for (TypeParameter tp : tuple.getTypeParameters()) {
                if ("First".equals(tp.getName())) {
                    args.put(tp, p.getType());
                } else if ("Element".equals(tp.getName())) {
                    args.put(tp, et);
                } else if ("Rest".equals(tp.getName())) {
                    args.put(tp, tt);
                }
            }
            if (i == params.size()-1) {
                tt = tuple.getType();
            }
            tt = tt.substitute(args);
        }
        return tt;
    }

    static String pathToModelDoc(final Declaration d) {
        if (d == null)return null;
        final StringBuilder sb = new StringBuilder();
        for (String p : generateModelPath(d)) {
            if (sb.length()==0) {
                sb.append("$CCMM$");
                if ("$".equals(p)) {
                    p = "ceylon.language";
                }
                if (p.isEmpty() || p.indexOf('.') >= 0) {
                    sb.append("['").append(p).append("']");
                } else {
                    sb.append(".").append(p);
                }
            } else {
                sb.append(".").append(p);
            }
        }
        sb.append(".$an.doc[0]");
        return sb.toString();
    }

    /** Outputs a function that returns the specified annotations, so that they can be loaded lazily.
     * @param annotations The annotations to be output.
     * @param d The declaration to which the annotations belong.
     * @param gen The generator to use for output. */
    static void outputAnnotationsFunction(final Tree.AnnotationList annotations, final Declaration d,
            final GenerateJsVisitor gen) {
        if (annotations == null || (annotations.getAnnotations().isEmpty() && annotations.getAnonymousAnnotation()==null)) {
            gen.out("[]");
        } else {
            gen.out("function(){return[");
            boolean first = true;
            //Leave the annotation but remove the doc from runtime for brevity
            if (annotations.getAnonymousAnnotation() != null) {
                first = false;
                final Tree.StringLiteral lit = annotations.getAnonymousAnnotation().getStringLiteral();
                gen.out(GenerateJsVisitor.getClAlias(), "doc(");
                final String sb = pathToModelDoc(d);
                if (sb != null && sb.length() < lit.getText().length()) {
                    gen.out(sb);
                } else {
                    lit.visit(gen);
                }
                gen.out(")");
            }
            for (Tree.Annotation a : annotations.getAnnotations()) {
                if (first) first=false; else gen.out(",");
                gen.getInvoker().generateInvocation(a);
            }
            gen.out("];}");
        }
    }

    /** Abstraction for a callback that generates the runtime annotations list as part of the metamodel. */
    static interface RuntimeMetamodelAnnotationGenerator {
        public void generateAnnotations();
    }

    static class ModelAnnotationGenerator implements RuntimeMetamodelAnnotationGenerator {
        private final GenerateJsVisitor gen;
        private final Declaration d;
        private final Node node;
        private boolean includeAnnotationKey=true;
        ModelAnnotationGenerator(GenerateJsVisitor generator, Declaration decl, Node n) {
            gen = generator;
            d = decl;
            node = n;
        }
        public ModelAnnotationGenerator omitKey() {
            includeAnnotationKey=false;
            return this;
        }
        @Override public void generateAnnotations() {
            if (includeAnnotationKey) {
                gen.out(",", MetamodelGenerator.KEY_ANNOTATIONS, ":");
            }
            gen.out("function(){return[");
            boolean first = true;
            for (Annotation a : d.getAnnotations()) {
                if (first) first=false; else gen.out(",");
                Declaration ad = d.getUnit().getPackage().getMemberOrParameter(d.getUnit(), a.getName(), null, false);
                if (ad instanceof Method) {
                    gen.qualify(node, ad);
                    gen.out(gen.getNames().name(ad), "(");
                    if (a.getPositionalArguments() == null) {
                        for (Parameter p : ((Method)ad).getParameterLists().get(0).getParameters()) {
                            String v = a.getNamedArguments().get(p.getName());
                            gen.out(v == null ? "undefined" : v);
                        }
                    } else {
                        if ("ceylon.language::doc".equals(ad.getQualifiedNameString())) {
                            //Use ref if it's too long
                            final String ref = pathToModelDoc(d);
                            final String doc = a.getPositionalArguments().get(0);
                            if (ref != null && ref.length() < doc.length()) {
                                gen.out(ref);
                            } else {
                                gen.out("\"", gen.escapeStringLiteral(doc), "\"");
                            }
                        } else {
                            boolean farg = true;
                            for (String s : a.getPositionalArguments()) {
                                if (farg)farg=false; else gen.out(",");
                                gen.out("\"", gen.escapeStringLiteral(s), "\"");
                            }
                        }
                    }
                    gen.out(")");
                } else {
                    gen.out("null/*MISSING DECLARATION FOR ANNOTATION ", a.getName(), "*/");
                }
            }
            gen.out("];}");
        }
    }

    private static List<ProducedType> getTupleTypes(ProducedType pt) {
        final ArrayList<ProducedType> ts=new ArrayList<>();
        do {
            if (pt.getProducedTypeQualifiedName().equals("ceylon.language::Empty")) {
                pt = null;
            } else {
                String tname=pt.getProducedTypeQualifiedName();
                if (tname.startsWith("ceylon.language::Tuple")) {
                    ts.add(pt.getTypeArgumentList().get(1));
                    pt = pt.getTypeArgumentList().get(2);
                } else if (tname.startsWith("ceylon.language::Sequen")) {
                    ts.add(pt);
                    pt = null;
                } else if (pt.getDeclaration() instanceof UnionType) {
                    for (ProducedType ct : pt.getCaseTypes()) {
                        if (ct.getProducedTypeQualifiedName().startsWith("ceylon.language::Tuple")) {
                            ts.add(pt);
                            pt=null;
                            break;
                        }
                    }
                } else { //just cut it short
                    pt = null;
                }
            }
        } while (pt != null);
        return ts;
    }

    /** Generates the right type arguments for operators that are sugar for method calls.
     * @param left The left term of the operator
     * @param right The right term of the operator
     * @param methodName The name of the method that is to be invoked
     * @param rightTpName The name of the type argument on the right term
     * @param leftTpName The name of the type parameter on the method
     * @return A map with the type parameter of the method as key
     * and the produced type belonging to the type argument of the term on the right. */
    static Map<TypeParameter, ProducedType> mapTypeArgument(final Tree.BinaryOperatorExpression expr,
            final String methodName, final String rightTpName, final String leftTpName) {
        Method md = (Method)expr.getLeftTerm().getTypeModel().getDeclaration().getMember(methodName, null, false);
        if (md == null) {
            expr.addUnexpectedError("Left term of intersection operator should have method named " + methodName);
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
            expr.addUnexpectedError("Right term of intersection operator should have type parameter named " + rightTpName);
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
            expr.addUnexpectedError("Left term of intersection should have type parameter named " + leftTpName);
        }
        targs.put(mtp, otherType);
        return targs;
    }

    /** Returns the qualified name of a declaration, skipping any containing methods. */
    public static String qualifiedNameSkippingMethods(Declaration d) {
        final StringBuilder p = new StringBuilder(d.getName());
        Scope s = d.getContainer();
        while (s != null) {
            if (s instanceof com.redhat.ceylon.compiler.typechecker.model.Package) {
                final String pkname = ((com.redhat.ceylon.compiler.typechecker.model.Package)s).getNameAsString();
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

}
