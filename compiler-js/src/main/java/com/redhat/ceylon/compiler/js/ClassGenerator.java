package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.js.GenerateJsVisitor.SuperVisitor;
import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.util.NativeUtil;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;

public class ClassGenerator {
    
    static void classDefinition(final Tree.ClassDefinition that, final GenerateJsVisitor gen) {
        //Don't even bother with nodes that have errors
        if (TypeGenerator.errVisitor.hasErrors(that))return;
        final Class d = that.getDeclarationModel();
        //If it's inside a dynamic interface, don't generate anything
        if (d.isClassOrInterfaceMember() && ((ClassOrInterface)d.getContainer()).isDynamic())return;
        final Class natd = (Class)ModelUtil.getNativeDeclaration(d, Backend.JavaScript);
        if (NativeUtil.isNativeHeader(that) && natd != null) {
            // It's a native header, remember it for later when we deal with its implementation
            gen.saveNativeHeader(that);
            return;
        }
        if (!(NativeUtil.isForBackend(that, Backend.JavaScript) || NativeUtil.isHeaderWithoutBackend(that, Backend.JavaScript))) {
            return;
        }
        final Tree.ParameterList plist = that.getParameterList();
        final Tree.SatisfiedTypes sats = that.getSatisfiedTypes();
        final List<Tree.Statement> stmts;
        if (NativeUtil.isForBackend(d, Backend.JavaScript)) {
            stmts = NativeUtil.mergeStatements(that.getClassBody(), gen.getNativeHeader(d));
        } else {
            stmts = that.getClassBody().getStatements();
        }
        //Find the constructors, if any
        final List<Tree.Constructor> constructors;
        Tree.Constructor defconstr = null;
        final boolean hasConstructors = d.hasConstructors() || (natd != null && natd.hasConstructors());
        final boolean hasEnumerated = d.hasEnumerated() || (natd != null && natd.hasEnumerated());
        if (hasConstructors) {
            constructors = new ArrayList<>(3);
            for (Tree.Statement st : stmts) {
                if (st instanceof Tree.Constructor) {
                    Tree.Constructor constr = (Tree.Constructor)st;
                    constructors.add(constr);
                    if (constr.getDeclarationModel().getName() == null) {
                        defconstr = constr;
                    }
                }
            }
        } else {
            constructors = null;
        }
        gen.comment(that);
        final boolean isAbstractNative = d.isNativeHeader() && natd != null;
        final String typeName = gen.getNames().name(d);
        if (TypeUtils.isNativeExternal(d)) {
            boolean bye = false;
            if (hasConstructors && defconstr == null) {
                gen.out(GenerateJsVisitor.function, typeName);
                gen.out("(){");
                gen.generateThrow("Exception", d.getQualifiedNameString() + " has no default constructor.", that);
                gen.out(";}"); gen.endLine();
            }
            if (gen.stitchNative(d, that)) {
                if (d.isShared()) {
                    gen.share(d);
                }
                TypeGenerator.initializeType(that, gen);
                bye = true;
            }
            if (hasConstructors) {
                for (Tree.Constructor cnstr : constructors) {
                    Constructors.classConstructor(cnstr, that, constructors, gen);
                }
            }
            if (bye)return;
        }
        gen.out(GenerateJsVisitor.function, typeName);
        //If there's a default constructor, create a different function with this code
        if (hasConstructors || hasEnumerated) {
            if (defconstr == null) {
                gen.out("(){");
                gen.generateThrow("Exception", d.getQualifiedNameString() + " has no default constructor.", that);
                gen.out(";}"); gen.endLine();
                gen.out(GenerateJsVisitor.function, typeName);
            }
            gen.out("$$c");
        }
        //Force the names of the backend's native type unshared members to be the same as in the header's
        //counterparts
        if (isAbstractNative) {
            if (plist != null) {
                for (Parameter p : d.getParameterList().getParameters()) {
                    gen.getNames().forceName(natd.getParameter(p.getName()).getModel(), gen.getNames().name(p));
                }
            }
            for (Declaration hd : d.getMembers()) {
                if (!hd.isShared()) {
                    gen.getNames().forceName(natd.getMember(hd.getName(), null, false), gen.getNames().name(hd));
                }
            }
        }
        final boolean withTargs = TypeGenerator.generateParameters(that.getTypeParameterList(), plist, d, gen);
        gen.beginBlock();
        if (!hasConstructors) {
            //This takes care of top-level attributes defined before the class definition
            gen.out("$init$", typeName, "();");
            gen.endLine();
            gen.declareSelf(d);
            gen.referenceOuter(d);
        }
        final String me = gen.getNames().self(d);
        if (withTargs) {
            gen.out(gen.getClAlias(), "set_type_args(", me, ",$$targs$$);");
            gen.endLine();
        } else if (sats != null) {
            addSatisfiedTypeArguments(sats, that, me, gen);
        }
        addFunctionTypeArguments(d, me, gen);
        if (plist != null) {
            gen.initParameters(plist, d, null);
        }

        callSupertypes(that, d, typeName, gen);

        if (!gen.opts.isOptimize() && plist != null) {
            //Fix #231 for lexical scope
            for (Tree.Parameter p : plist.getParameters()) {
                if (!p.getParameterModel().isHidden()){
                    gen.generateAttributeForParameter(that, d, p.getParameterModel());
                }
            }
        }
        if (!hasConstructors) {
            if (TypeUtils.isNativeExternal(d)) {
                gen.stitchConstructorHelper(that, "_cons_before");
            }
            gen.visitStatements(stmts);
            if (TypeUtils.isNativeExternal(d)) {
                gen.stitchConstructorHelper(that, "_cons_after");
            }
            gen.out("return ", me, ";");
        }
        gen.endBlockNewLine();
        if (defconstr != null) {
            //Define a function as the class and call the default constructor in there
            String _this = "undefined";
            if (!d.isToplevel()) {
                final ClassOrInterface coi = ModelUtil.getContainingClassOrInterface(d.getContainer());
                if (coi != null) {
                    if (d.isClassOrInterfaceMember()) {
                        _this = "this";
                    } else {
                        _this = gen.getNames().self(coi);
                    }
                }
            }
            gen.out(GenerateJsVisitor.function, typeName, "(){return ",
                    typeName, "_", gen.getNames().name(defconstr.getDeclarationModel()), ".apply(",
                    _this, ",arguments);}");
            gen.endLine();
        }
        if (hasConstructors) {
            for (Tree.Constructor cnstr : constructors) {
                Constructors.classConstructor(cnstr, that, constructors, gen);
            }
        }
        if (hasEnumerated) {
            for (Tree.Statement st : stmts) {
                if (st instanceof Tree.Enumerated) {
                    Singletons.valueConstructor(that, (Tree.Enumerated)st, gen);
                }
            }
        }
        //Add reference to metamodel
        gen.out(typeName, ".$crtmm$=");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), gen);
        gen.endLine(true);
        if (!isAbstractNative) {
            gen.share(d);
        }
        TypeGenerator.initializeType(that, gen);
        if (d.isSerializable()) {
            SerializationHelper.addDeserializer(that, d, gen);
        }
    }

    public static void addSatisfiedTypeArguments(final Tree.SatisfiedTypes sats,
            final Node node, final String objname, final GenerateJsVisitor gen) {
        //Check if any of the satisfied types have type arguments
        for(Tree.StaticType sat : sats.getTypes()) {
            boolean first = true;
            Map<TypeParameter,Type> targs = sat.getTypeModel().getTypeArguments();
            if (targs != null && !targs.isEmpty()) {
                if (first) {
                    gen.out(objname, ".$$targs$$=");
                    TypeUtils.printTypeArguments(node, targs, gen, false, null);
                    gen.endLine(true);
                    first = false;
                } else {
                    gen.out("/*TODO: more type arguments 1*/");
                }
            }
        }
    }

    public static void addFunctionTypeArguments(final Class d, final String objname,
            final GenerateJsVisitor gen) {
        if (!d.isToplevel() && d.getContainer() instanceof Function
                && !((Function)d.getContainer()).getTypeParameters().isEmpty()) {
            gen.out(gen.getClAlias(), "set_type_args(", objname, ",",
                    gen.getNames().typeArgsParamName((Function)d.getContainer()), ")");
            gen.endLine(true);
        }
    }

    public static void callSupertypes(Tree.ClassDefinition that, Class d,
            String typeName, GenerateJsVisitor gen) {
        final List<Declaration> superDecs = new ArrayList<Declaration>(3);
        if (!gen.opts.isOptimize()) {
            that.getClassBody().visit(new SuperVisitor(superDecs));
        }
        final Tree.ExtendedType extendedType = that.getExtendedType();
        final Tree.SatisfiedTypes sats = that.getSatisfiedTypes();
        TypeGenerator.callSupertypes(
                sats == null ? null : TypeUtils.getTypes(sats.getTypes()),
                extendedType == null ? null : extendedType.getType(),
                d, that, superDecs, extendedType == null ? null : extendedType.getInvocationExpression(),
                extendedType == null ? null : ((Class) d.getExtendedType().getDeclaration()).getParameterList(), gen);
    }

}
