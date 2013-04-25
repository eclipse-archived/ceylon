package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.loader.MetamodelGenerator;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

/** A convenience class to help with the handling of certain type declarations. */
public class TypeUtils {

    final TypeDeclaration tuple;
    final TypeDeclaration iterable;
    final TypeDeclaration sequential;
    final TypeDeclaration numeric;
    final TypeDeclaration _integer;
    final TypeDeclaration _float;
    final TypeDeclaration _null;
    final TypeDeclaration anything;

    TypeUtils(Module languageModule) {
        com.redhat.ceylon.compiler.typechecker.model.Package pkg = languageModule.getPackage("ceylon.language");
        tuple = (TypeDeclaration)pkg.getMember("Tuple", null, false);
        iterable = (TypeDeclaration)pkg.getMember("Iterable", null, false);
        sequential = (TypeDeclaration)pkg.getMember("Sequential", null, false);
        numeric = (TypeDeclaration)pkg.getMember("Numeric", null, false);
        _integer = (TypeDeclaration)pkg.getMember("Integer", null, false);
        _float = (TypeDeclaration)pkg.getMember("Float", null, false);
        _null = (TypeDeclaration)pkg.getMember("Null", null, false);
        anything = (TypeDeclaration)pkg.getMember("Anything", null, false);
    }

    /** Prints the type arguments, usually for their reification. */
    public static void printTypeArguments(Node node, Map<TypeParameter,ProducedType> targs, GenerateJsVisitor gen) {
        gen.out("{");
        boolean first = true;
        for (Map.Entry<TypeParameter,ProducedType> e : targs.entrySet()) {
            if (first) {
                first = false;
            } else {
                gen.out(",");
            }
            gen.out(e.getKey().getName(), ":");
            ProducedType pt = e.getValue();
            TypeDeclaration d = pt.getDeclaration();
            boolean composite = d instanceof UnionType || d instanceof IntersectionType;
            boolean hasParams = pt.getTypeArgumentList() != null && !pt.getTypeArgumentList().isEmpty();
            boolean closeBracket = false;
            if (composite) {
                outputTypeList(node, d, gen, true);
            } else if (d instanceof TypeParameter) {
                resolveTypeParameter(node, (TypeParameter)d, gen);
            } else {
                gen.out("{t:");
                outputQualifiedTypename(
                        gen.isImported(node == null ? null : node.getUnit().getPackage(), pt.getDeclaration()),
                        pt, gen);
                closeBracket = true;
            }
            if (hasParams) {
                gen.out(",a:");
                printTypeArguments(node, pt.getTypeArguments(), gen);
            }
            if (closeBracket) {
                gen.out("}");
            }
        }
        gen.out("}");
    }

    static void outputQualifiedTypename(final boolean imported, ProducedType pt, GenerateJsVisitor gen) {
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
            if (t.isAlias()) {
                t = t.getExtendedTypeDeclaration();
            }
            boolean qual = false;
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
            } else if (imported) {
                //This wasn't needed but now we seem to get imported decls with no package when compiling ceylon.language.metamodel types
                final String modAlias = gen.getNames().moduleAlias(t.getUnit().getPackage().getModule());
                if (modAlias != null && !modAlias.isEmpty()) {
                    gen.out(modAlias, ".");
                }
                qual = true;
            }

            String tname = gen.getNames().name(t);
            if (!qual && isReservedTypename(tname)) {
                gen.out(tname, "$");
            } else {
                gen.out(tname);
            }
        }
    }

    /** Prints out an object with a type constructor under the property "t" and its type arguments under
     * the property "a", or a union/intersection type with "u" or "i" under property "t" and the list
     * of types that compose it in an array under the property "l", or a type parameter as a reference to
     * already existing params. */
    static void typeNameOrList(Node node, ProducedType pt, GenerateJsVisitor gen, boolean typeReferences) {
        TypeDeclaration type = pt.getDeclaration();
        if (type.isAlias()) {
            type = type.getExtendedTypeDeclaration();
        }
        boolean unionIntersection = type instanceof UnionType
                || type instanceof IntersectionType;
        if (unionIntersection) {
            outputTypeList(node, type, gen, typeReferences);
        } else if (typeReferences) {
            if (type instanceof TypeParameter) {
                resolveTypeParameter(node, (TypeParameter)type, gen);
            } else {
                gen.out("{t:");
                outputQualifiedTypename(gen.isImported(node == null ? null : node.getUnit().getPackage(), type), pt, gen);
                if (!pt.getTypeArgumentList().isEmpty()) {
                    gen.out(",a:");
                    printTypeArguments(node, pt.getTypeArguments(), gen);
                }
                gen.out("}");
            }
        } else {
            gen.out("'", type.getQualifiedNameString(), "'");
        }
    }

    /** Appends an object with the type's type and list of union/intersection types. */
    static void outputTypeList(Node node, TypeDeclaration type, GenerateJsVisitor gen, boolean typeReferences) {
        gen.out("{ t:'");
        final List<ProducedType> subs;
        if (type instanceof IntersectionType) {
            gen.out("i");
            subs = type.getSatisfiedTypes();
        } else {
            gen.out("u");
            subs = type.getCaseTypes();
        }
        gen.out("', l:[");
        boolean first = true;
        for (ProducedType t : subs) {
            if (!first) gen.out(",");
            typeNameOrList(node, t, gen, typeReferences);
            first = false;
        }
        gen.out("]}");
    }

    /** Finds the owner of the type parameter and outputs a reference to the corresponding type argument. */
    static void resolveTypeParameter(Node node, TypeParameter tp, GenerateJsVisitor gen) {
        Scope parent = node.getScope();
        while (parent != null && parent != tp.getContainer()) {
            parent = parent.getScope();
        }
        if (tp.getContainer() instanceof ClassOrInterface) {
            if (parent == tp.getContainer()) {
                if (((ClassOrInterface)tp.getContainer()).isAlias()) {
                    //when resolving for aliases we just take the type arguments from the alias call
                    gen.out("$$targs$$.", tp.getName());
                } else {
                    gen.self((ClassOrInterface)tp.getContainer());
                    gen.out(".$$targs$$.", tp.getName());
                }
            } else {
                //This can happen in expressions such as Singleton(n) when n is dynamic
                gen.out("{/*NO PARENT*/t:", GenerateJsVisitor.getClAlias(), "Anything}");
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
                gen.out("$$$mptypes.", tp.getName());
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
        return param == null || param.getTypeDeclaration() == null || param.getTypeDeclaration().getQualifiedNameString().equals("UnknownType");
    }
    static boolean isUnknown(Declaration d) {
        return d == null || d.getQualifiedNameString().equals("UnknownType");
    }

    /** Generates the code to throw an Exception if a dynamic object is not of the specified type. */
    static void generateDynamicCheck(Tree.Term term, final ProducedType t, final GenerateJsVisitor gen) {
        String tmp = gen.getNames().createTempVariable();
        gen.out("(", tmp, "=");
        term.visit(gen);
        gen.out(",", GenerateJsVisitor.getClAlias(), "isOfType(", tmp, ",");
        TypeUtils.typeNameOrList(term, t, gen, true);
        gen.out(")?", tmp, ":", GenerateJsVisitor.getClAlias(), "throwexc('dynamic objects cannot be used here'))");
    }

    /** Output a metamodel map for runtime use. */
    static void encodeForRuntime(Declaration d, GenerateJsVisitor gen) {
        gen.out("{", MetamodelGenerator.KEY_NAME, ":'", d.getNameAsString(),
                "',", MetamodelGenerator.KEY_METATYPE, ":'");
        List<TypeParameter> tparms = null;
        List<ProducedType> satisfies = null;
        if (d instanceof com.redhat.ceylon.compiler.typechecker.model.Class) {
            gen.out(MetamodelGenerator.METATYPE_CLASS, "'");
            tparms = ((com.redhat.ceylon.compiler.typechecker.model.Class) d).getTypeParameters();
            if (((com.redhat.ceylon.compiler.typechecker.model.Class) d).getExtendedType() != null) {
                gen.out(",'super':");
                metamodelTypeNameOrList(d.getUnit().getPackage(),
                        ((com.redhat.ceylon.compiler.typechecker.model.Class) d).getExtendedType(), gen);
            }
            satisfies = ((com.redhat.ceylon.compiler.typechecker.model.Class) d).getSatisfiedTypes();

        } else if (d instanceof com.redhat.ceylon.compiler.typechecker.model.Interface) {

            gen.out(MetamodelGenerator.METATYPE_INTERFACE, "'");
            tparms = ((com.redhat.ceylon.compiler.typechecker.model.Interface) d).getTypeParameters();
            satisfies = ((com.redhat.ceylon.compiler.typechecker.model.Interface) d).getSatisfiedTypes();

        } else if (d instanceof Method) {
            
            gen.out(MetamodelGenerator.METATYPE_METHOD, "',", MetamodelGenerator.KEY_TYPE, ":");
            //This needs a new setting to resolve types but not type parameters
            metamodelTypeNameOrList(d.getUnit().getPackage(), ((Method)d).getType(), gen);
            gen.out(",", MetamodelGenerator.KEY_PARAMS, ":[");
            //TODO: parameter lists, parameters
            gen.out("]");
            tparms = ((Method) d).getTypeParameters();

        } else if (d instanceof com.redhat.ceylon.compiler.typechecker.model.Value) {

            gen.out(((com.redhat.ceylon.compiler.typechecker.model.Value) d).isTransient() ?
                    MetamodelGenerator.METATYPE_GETTER : MetamodelGenerator.METATYPE_ATTRIBUTE, "'");
        }
        if (tparms != null && !tparms.isEmpty()) {
            gen.out(",", MetamodelGenerator.KEY_TYPE_PARAMS, ":{");
            boolean first = true;
            for(TypeParameter tp : tparms) {
                if (!first)gen.out(",");
                first=false;
                gen.out(tp.getName(), ":{");
                if (tp.isCovariant()) {
                    gen.out("'var':'out',");
                } else if (tp.isContravariant()) {
                    gen.out("'var':'in',");
                }
                List<ProducedType> typelist = tp.getSatisfiedTypes();
                if (typelist != null && !typelist.isEmpty()) {
                    gen.out("'satisfies':[");
                    boolean first2 = true;
                    for (ProducedType st : typelist) {
                        if (!first2)gen.out(",");
                        first2=false;
                        metamodelTypeNameOrList(d.getUnit().getPackage(), st, gen);
                    }
                    gen.out("]");
                }
                typelist = tp.getCaseTypes();
                if (typelist != null && !typelist.isEmpty()) {
                    gen.out("'of':[");
                    boolean first3 = true;
                    for (ProducedType st : typelist) {
                        if (!first3)gen.out(",");
                        first3=false;
                        metamodelTypeNameOrList(d.getUnit().getPackage(), st, gen);
                    }
                    gen.out("]");
                }
                if (tp.getDefaultTypeArgument() != null) {
                    gen.out(",'def':");
                    metamodelTypeNameOrList(d.getUnit().getPackage(), tp.getDefaultTypeArgument(), gen);
                }
                gen.out("}");
            }
            gen.out("}");
        }
        if (satisfies != null) {
            gen.out(",'satisfies':[");
            boolean first = true;
            for (ProducedType st : satisfies) {
                if (!first)gen.out(",");
                first=false;
                metamodelTypeNameOrList(d.getUnit().getPackage(), st, gen);
            }
            gen.out("]");
        }
        gen.out("}");
    }

    /** Prints out an object with a type constructor under the property "t" and its type arguments under
     * the property "a", or a union/intersection type with "u" or "i" under property "t" and the list
     * of types that compose it in an array under the property "l", or a type parameter as a reference to
     * already existing params. */
    static void metamodelTypeNameOrList(final com.redhat.ceylon.compiler.typechecker.model.Package pkg,
            ProducedType pt, GenerateJsVisitor gen) {
        TypeDeclaration type = pt.getDeclaration();
        if (type.isAlias()) {
            type = type.getExtendedTypeDeclaration();
        }
        boolean unionIntersection = type instanceof UnionType
                || type instanceof IntersectionType;
        if (unionIntersection) {
            outputMetamodelTypeList(pkg, pt, gen);
        } else if (type instanceof TypeParameter) {
            gen.out("'", type.getNameAsString(), "'");
        } else {
            gen.out("{t:");
            outputQualifiedTypename(gen.isImported(pkg, type), pt, gen);
            //TODO type parameters
            gen.out("}");
        }
    }

    /** Appends an object with the type's type and list of union/intersection types. */
    static void outputMetamodelTypeList(final com.redhat.ceylon.compiler.typechecker.model.Package pkg,
            ProducedType pt, GenerateJsVisitor gen) {
        TypeDeclaration type = pt.getDeclaration();
        gen.out("{ t:'");
        final List<ProducedType> subs;
        if (type instanceof IntersectionType) {
            gen.out("i");
            subs = type.getSatisfiedTypes();
        } else {
            gen.out("u");
            subs = type.getCaseTypes();
        }
        gen.out("', l:[");
        boolean first = true;
        for (ProducedType t : subs) {
            if (!first) gen.out(",");
            metamodelTypeNameOrList(pkg, t, gen);
            first = false;
        }
        gen.out("]}");
    }

}
