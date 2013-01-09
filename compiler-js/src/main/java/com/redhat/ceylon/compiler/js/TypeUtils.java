package com.redhat.ceylon.compiler.js;

import java.util.Iterator;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
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

/** A convenience class to help with the handling of certain type declarations. */
public class TypeUtils {

    final TypeDeclaration tuple;
    final TypeDeclaration iterable;
    final TypeDeclaration sequential;
    final TypeDeclaration numeric;
    final TypeDeclaration _integer;
    final TypeDeclaration _float;

    TypeUtils(Module languageModule) {
        com.redhat.ceylon.compiler.typechecker.model.Package pkg = languageModule.getPackage("ceylon.language");
        tuple = (TypeDeclaration)pkg.getMember("Tuple", null, false);
        iterable = (TypeDeclaration)pkg.getMember("Iterable", null, false);
        sequential = (TypeDeclaration)pkg.getMember("Sequential", null, false);
        numeric = (TypeDeclaration)pkg.getMember("Numeric", null, false);
        _integer = (TypeDeclaration)pkg.getMember("Integer", null, false);
        _float = (TypeDeclaration)pkg.getMember("Float", null, false);
    }

    /** Prints the type arguments, usually for their reification. */
    public static void printTypeArguments(Node node, List<ProducedType> targs, GenerateJsVisitor gen) {
        gen.out("[");
        boolean first = true;
        for (ProducedType pt : targs) {
            if (first) {
                first = false;
            } else {
                gen.out(",");
            }
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
                outputQualifiedTypename(node, pt, gen);
                closeBracket = true;
            }
            if (hasParams) {
                gen.out(",a:");
                printTypeArguments(node, pt.getTypeArgumentList(), gen);
            }
            if (closeBracket) {
                gen.out("}");
            }
        }
        gen.out("]");
    }

    static void outputQualifiedTypename(Node node, ProducedType pt, GenerateJsVisitor gen) {
        TypeDeclaration t = pt.getDeclaration();
        if (t.getName().equals("Nothing")) {
            //Hack in the model means hack here as well
            gen.out(GenerateJsVisitor.getClAlias(), "Nothing");
        } else {
            if (t.isAlias()) {
                t = t.getExtendedTypeDeclaration();
            }
            boolean qual = gen.qualify(node, t);
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
                outputQualifiedTypename(node, pt, gen);
                if (!pt.getTypeArgumentList().isEmpty()) {
                    gen.out(",a:");
                    printTypeArguments(node, pt.getTypeArgumentList(), gen);
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
            int pos = ((ClassOrInterface)tp.getContainer()).getTypeParameters().indexOf(tp);
            if (parent == tp.getContainer()) {
                gen.out("this.$$targs$$[", Integer.toString(pos), "]");
            } else {
                gen.out("/*TYPE TYPEPARM ", Integer.toString(pos), parent.getQualifiedNameString(), "*/'", tp.getQualifiedNameString(), "'");
            }
        } else {
            //it has to be a method, right?
            //We need to find the index of the parameter where the argument occurs
            //...and it could be null...
            int plistCount = -1;
            int paramCount = -1;
            ProducedType type = null;
            for (Iterator<ParameterList> iter0 = ((Method)tp.getContainer()).getParameterLists().iterator();
                    type == null && iter0.hasNext();) {
                plistCount++;
                for (Iterator<Parameter> iter1 = iter0.next().getParameters().iterator();
                        type == null && iter1.hasNext();) {
                    paramCount++;
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
                gen.out("$$$mptypes[", Integer.toString(paramCount), "]");
            } else {
                gen.out("/*METHOD TYPEPARM plist ", Integer.toString(plistCount), "#", Integer.toString(paramCount), "*/'", type.getProducedTypeQualifiedName(), "'");
            }
        }
    }

    static ProducedType typeContainsTypeParameter(ProducedType td, TypeParameter tp) {
        TypeDeclaration d = td.getDeclaration();
        if (d == tp) {
            return td;
        } else if (d instanceof UnionType || d instanceof IntersectionType) {
            for (ProducedType sub : td.getCaseTypes()) {
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
                || typeName.equals("Array")) || typeName.equals("String");
    }

}
