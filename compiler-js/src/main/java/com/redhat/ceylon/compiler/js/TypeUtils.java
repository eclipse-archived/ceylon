package com.redhat.ceylon.compiler.js;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.tree.Node;

/** A convenience class to help with the handling of certain type declarations. */
public class TypeUtils {

    final TypeDeclaration tuple;
    final TypeDeclaration iterable;
    final TypeDeclaration sequential;

    TypeUtils(Module languageModule) {
        com.redhat.ceylon.compiler.typechecker.model.Package pkg = languageModule.getPackage("ceylon.language");
        tuple = (TypeDeclaration)pkg.getMember("Tuple", null, false);
        iterable = (TypeDeclaration)pkg.getMember("Iterable", null, false);
        sequential = (TypeDeclaration)pkg.getMember("Sequential", null, false);
    }

    public static void printTypeArguments(Node node, List<ProducedType> targs, GenerateJsVisitor gen) {
        gen.out("[");
        boolean first = true;
        for (ProducedType pt : targs) {
            if (first) {
                first = false;
            } else {
                gen.out(",");
            }
            if (pt.getDeclaration() instanceof UnionType || pt.getDeclaration() instanceof IntersectionType) {
                outputTypeList(node, pt.getDeclaration(), gen, true);
            } else if (pt.getDeclaration() instanceof TypeParameter) {
                gen.out("/*TYPE PARAM PENDING*/'", pt.getProducedTypeQualifiedName(), "'");
            } else {
                outputQualifiedTypename(node, pt.getDeclaration(), gen);
            }
            if (pt.getTypeArgumentList() != null && !pt.getTypeArgumentList().isEmpty()) {
                gen.out(",");
                printTypeArguments(node, pt.getTypeArgumentList(), gen);
            }
        }
        gen.out("]");
    }

    static void outputQualifiedTypename(Node node, TypeDeclaration t, GenerateJsVisitor gen) {
        if (t.getName().equals("Bottom")) {
            //Hack in the model means hack here as well
            gen.out(GenerateJsVisitor.getClAlias(), "Bottom");
        } else {
            gen.qualify(node, t);
            gen.out(gen.getNames().name(t));
        }
    }

    static void typeNameOrList(Node node, TypeDeclaration type, GenerateJsVisitor gen, boolean typeReferences) {
        if (type.isAlias()) {
            type = type.getExtendedTypeDeclaration();
        }
        boolean unionIntersection = type instanceof com.redhat.ceylon.compiler.typechecker.model.UnionType
                || type instanceof com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
        if (unionIntersection) {
            outputTypeList(node, type, gen, typeReferences);
        } else if (typeReferences) {
            if (type instanceof TypeParameter) {
                gen.out("/*TYPE PARAM PENDING*/'", type.getQualifiedNameString(), "'");
            } else {
                outputQualifiedTypename(node, type, gen);
            }
        } else {
            gen.out("'", type.getQualifiedNameString(), "'");
        }
    }

    /** Appends an object with the type's type and list of union/intersection types. */
    static void outputTypeList(Node node, TypeDeclaration type, GenerateJsVisitor gen, boolean typeReferences) {
        gen.out("{ t:'");
        final List<TypeDeclaration> subs;
        if (type instanceof com.redhat.ceylon.compiler.typechecker.model.IntersectionType) {
            gen.out("i");
            subs = type.getSatisfiedTypeDeclarations();
        } else {
            gen.out("u");
            subs = type.getCaseTypeDeclarations();
        }
        gen.out("', l:[");
        boolean first = true;
        for (TypeDeclaration t : subs) {
            if (!first) gen.out(",");
            typeNameOrList(node, t, gen, typeReferences);
            first = false;
        }
        gen.out("]}");
    }

}
