package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.js.GenerateJsVisitor.PrototypeInitCallback;
import com.redhat.ceylon.compiler.js.GenerateJsVisitor.SuperVisitor;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Util;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StaticType;

public class TypeGenerator {

    private static final ErrorVisitor errVisitor = new ErrorVisitor();

    /** Generates a function to initialize the specified type. */
    static void initializeType(final Tree.Declaration type, final GenerateJsVisitor gen) {
        Tree.ExtendedType extendedType = null;
        Tree.SatisfiedTypes satisfiedTypes = null;
        final ClassOrInterface decl;
        final List<Tree.Statement> stmts;
        if (type instanceof Tree.ClassDefinition) {
            Tree.ClassDefinition classDef = (Tree.ClassDefinition) type;
            extendedType = classDef.getExtendedType();
            satisfiedTypes = classDef.getSatisfiedTypes();
            decl = classDef.getDeclarationModel();
            stmts = classDef.getClassBody().getStatements();
        } else if (type instanceof Tree.InterfaceDefinition) {
            satisfiedTypes = ((Tree.InterfaceDefinition) type).getSatisfiedTypes();
            decl = ((Tree.InterfaceDefinition) type).getDeclarationModel();
            stmts = ((Tree.InterfaceDefinition) type).getInterfaceBody().getStatements();
        } else if (type instanceof Tree.ObjectDefinition) {
            Tree.ObjectDefinition objectDef = (Tree.ObjectDefinition) type;
            extendedType = objectDef.getExtendedType();
            satisfiedTypes = objectDef.getSatisfiedTypes();
            decl = (ClassOrInterface)objectDef.getDeclarationModel().getTypeDeclaration();
            stmts = objectDef.getClassBody().getStatements();
        } else {
            stmts = null;
            decl = null;
        }
        final PrototypeInitCallback callback = new PrototypeInitCallback() {
            @Override
            public void addToPrototypeCallback() {
                if (decl != null) {
                    gen.addToPrototype(type, decl, stmts);
                }
            }
        };
        typeInitialization(extendedType, satisfiedTypes, decl, callback, gen);
    }

    /** This is now the main method to generate the type initialization code.
     * @param extendedType The type that is being extended.
     * @param satisfiedTypes The types satisfied by the type being initialized.
     * @param d The declaration for the type being initialized
     * @param callback A callback to add something more to the type initializer in prototype style.
     */
    static void typeInitialization(final Tree.ExtendedType extendedType, final Tree.SatisfiedTypes satisfiedTypes,
            final ClassOrInterface d, PrototypeInitCallback callback, final GenerateJsVisitor gen) {

        final boolean isInterface = d instanceof com.redhat.ceylon.compiler.typechecker.model.Interface;
        String initFuncName = isInterface ? "initTypeProtoI" : "initTypeProto";

        gen.out("function $init$", gen.getNames().name(d), "()");
        gen.beginBlock();
        gen.out("if(", gen.getNames().name(d), ".$$===undefined)");
        gen.beginBlock();
        boolean genIniter = true;
        if (d.isNative()) {
            //Allow native types to have their own initialization code
            genIniter = !gen.stitchInitializer(d);
        }
        if (genIniter) {
            final String qns = TypeUtils.qualifiedNameSkippingMethods(d);
            gen.out(GenerateJsVisitor.getClAlias(), initFuncName, "(", gen.getNames().name(d), ",'", qns, "'");
            final List<Tree.StaticType> supers = satisfiedTypes == null ? Collections.<Tree.StaticType>emptyList()
                    : new ArrayList<Tree.StaticType>(satisfiedTypes.getTypes().size());

            if (extendedType != null) {
                String fname = gen.typeFunctionName(extendedType.getType(),
                        !extendedType.getType().getDeclarationModel().isMember());
                gen.out(",", fname);
            } else if (!isInterface) {
                gen.out(",", GenerateJsVisitor.getClAlias(), "Basic");
            }
            if (satisfiedTypes != null) {
                supers.addAll(satisfiedTypes.getTypes());
                Collections.sort(supers, new StaticTypeComparator());
                for (Tree.StaticType satType : supers) {
                    String fname = gen.typeFunctionName(satType, true);
                    gen.out(",", fname);
                }
            }
            gen.out(");");
        }
        //Add ref to outer type
        if (d.isMember()) {
            StringBuilder containers = new StringBuilder();
            Scope _d2 = d;
            while (_d2 instanceof ClassOrInterface) {
                if (containers.length() > 0) {
                    containers.insert(0, '.');
                }
                containers.insert(0, gen.getNames().name((Declaration)_d2));
                _d2 = _d2.getContainer();
            }
            gen.endLine();
            gen.out(containers.toString(), "=", gen.getNames().name(d), ";");
        }

        //The class definition needs to be inside the init function if we want forwards decls to work in prototype style
        if (gen.opts.isOptimize()) {
            gen.endLine();
            callback.addToPrototypeCallback();
        }
        gen.endBlockNewLine();
        gen.out("return ", gen.getNames().name(d), ";");
        gen.endBlockNewLine();
        //If it's nested, share the init function
        if (gen.outerSelf(d)) {
            gen.out(".$init$", gen.getNames().name(d), "=$init$", gen.getNames().name(d));
            gen.endLine(true);
        }
        gen.out("$init$", gen.getNames().name(d), "()");
        gen.endLine(true);
    }

    static void interfaceDefinition(final Tree.InterfaceDefinition that, final GenerateJsVisitor gen) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        final Interface d = that.getDeclarationModel();
        gen.comment(that);

        gen.out(GenerateJsVisitor.function, gen.getNames().name(d));
        final boolean withTargs = generateParameters(that, gen);
        gen.beginBlock();
        //declareSelf(d);
        gen.referenceOuter(d);
        final List<Declaration> superDecs = new ArrayList<Declaration>(3);
        if (!gen.opts.isOptimize()) {
            new SuperVisitor(superDecs).visit(that.getInterfaceBody());
        }
        callInterfaces(that.getSatisfiedTypes(), d, that, superDecs, gen);
        if (withTargs) {
            gen.out(GenerateJsVisitor.getClAlias(), "set_type_args(", gen.getNames().self(d), ",$$targs$$)");
            gen.endLine(true);
        }
        if (!d.isToplevel() && d.getContainer() instanceof Method && !((Method)d.getContainer()).getTypeParameters().isEmpty()) {
            gen.out(GenerateJsVisitor.getClAlias(), "set_type_args(", gen.getNames().self(d), ",$$$mptypes)");
            gen.endLine(true);
        }
        that.getInterfaceBody().visit(gen);
        //returnSelf(d);
        gen.endBlockNewLine();
        //Add reference to metamodel
        gen.out(gen.getNames().name(d), ".$crtmm$=");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), gen);
        gen.endLine(true);
        gen.share(d);
        initializeType(that, gen);
    }

    /** Outputs the parameter list of the type's constructor, including surrounding parens.
     * Returns true if the type has type parameters. */
    static boolean generateParameters(final Tree.ClassOrInterface that, final GenerateJsVisitor gen) {
        gen.out("(");
        final boolean withTargs = that.getTypeParameterList() != null &&
                !that.getTypeParameterList().getTypeParameterDeclarations().isEmpty();
        if (that instanceof Tree.ClassDefinition) {
            for (Tree.Parameter p: ((Tree.ClassDefinition)that).getParameterList().getParameters()) {
                p.visit(gen);
                gen.out(",");
            }
        }
        if (withTargs) {
            gen.out("$$targs$$,");
        }
        gen.out(gen.getNames().self(that.getDeclarationModel()), ")");
        return withTargs;
    }

    static void classDefinition(final Tree.ClassDefinition that, final GenerateJsVisitor gen) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        final Class d = that.getDeclarationModel();
        gen.comment(that);
        if (gen.shouldStitch(d)) {
            if (gen.stitchNative(d, that)) {
                if (d.isShared()) {
                    gen.share(d);
                }
                initializeType(that, gen);
                return;
            }
        }
        gen.out(GenerateJsVisitor.function, gen.getNames().name(d));
        final boolean withTargs = generateParameters(that, gen);
        gen.beginBlock();
        //This takes care of top-level attributes defined before the class definition
        gen.out("$init$", gen.getNames().name(d), "()");
        gen.endLine(true);
        gen.declareSelf(d);
        gen.referenceOuter(d);
        if (withTargs) {
            gen.out(GenerateJsVisitor.getClAlias(), "set_type_args(", gen.getNames().self(d), ",$$targs$$)");
            gen.endLine(true);
        } else {
            //Check if any of the satisfied types have type arguments
            if (that.getSatisfiedTypes() != null) {
                for(Tree.StaticType sat : that.getSatisfiedTypes().getTypes()) {
                    boolean first = true;
                    Map<TypeParameter,ProducedType> targs = sat.getTypeModel().getTypeArguments();
                    if (targs != null && !targs.isEmpty()) {
                        if (first) {
                            gen.out(gen.getNames().self(d), ".$$targs$$=");
                            TypeUtils.printTypeArguments(that, targs, gen, false);
                            gen.endLine(true);
                            first = false;
                        } else {
                            gen.out("/*TODO: more type arguments*/");
                        }
                    }
                }
            }
        }
        if (!d.isToplevel() && d.getContainer() instanceof Method && !((Method)d.getContainer()).getTypeParameters().isEmpty()) {
            gen.out(GenerateJsVisitor.getClAlias(), "set_type_args(", gen.getNames().self(d), ",$$$mptypes)");
            gen.endLine(true);
        }
        gen.initParameters(that.getParameterList(), d, null);

        final List<Declaration> superDecs = new ArrayList<Declaration>(3);
        if (!gen.opts.isOptimize()) {
            new SuperVisitor(superDecs).visit(that.getClassBody());
        }
        callSuperclass(that.getExtendedType(), d, that, superDecs, gen);
        callInterfaces(that.getSatisfiedTypes(), d, that, superDecs, gen);

        if (!gen.opts.isOptimize()) {
            //Fix #231 for lexical scope
            for (Tree.Parameter p : that.getParameterList().getParameters()) {
                if (!p.getParameterModel().isHidden()){
                    gen.generateAttributeForParameter(that, d, p.getParameterModel());
                }
            }
        }
        if (d.isNative()) {
            gen.stitchConstructorHelper(that, "_cons_before");
        }
        that.getClassBody().visit(gen);
        if (d.isNative()) {
            gen.stitchConstructorHelper(that, "_cons_after");
        }
        gen.out("return ", gen.getNames().self(d), ";");
        gen.endBlockNewLine();
        //Add reference to metamodel
        gen.out(gen.getNames().name(d), ".$crtmm$=");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), gen);
        gen.endLine(true);
        gen.share(d);
        initializeType(that, gen);
    }

    static void callSuperclass(final Tree.ExtendedType extendedType, final Class d, final Node that,
            final List<Declaration> superDecs, final GenerateJsVisitor gen) {
        if (extendedType!=null) {
            Tree.PositionalArgumentList argList = extendedType.getInvocationExpression()
                    .getPositionalArgumentList();
            TypeDeclaration typeDecl = extendedType.getType().getDeclarationModel();
            gen.out(gen.memberAccessBase(extendedType.getType(), typeDecl, false, gen.qualifiedPath(that, typeDecl)),
                    (gen.opts.isOptimize() && (gen.getSuperMemberScope(extendedType.getType()) != null))
                    ? ".call(this," : "(");

            gen.getInvoker().generatePositionalArguments(extendedType.getInvocationExpression().getPrimary(),
                    argList, argList.getPositionalArguments(), false, false);
            if (argList.getPositionalArguments().size() > 0) {
                gen.out(",");
            }
            //There may be defaulted args we must pass as undefined
            final List<com.redhat.ceylon.compiler.typechecker.model.Parameter> superParams =
                    d.getExtendedTypeDeclaration().getParameterList().getParameters();
            if (superParams.size() > argList.getPositionalArguments().size()) {
                for (int i = argList.getPositionalArguments().size(); i < superParams.size(); i++) {
                    com.redhat.ceylon.compiler.typechecker.model.Parameter p = superParams.get(i);
                    if (p.isSequenced()) {
                        gen.out(GenerateJsVisitor.getClAlias(), "getEmpty(),");
                    } else {
                        gen.out("undefined,");
                    }
                }
            }
            //If the supertype has type arguments, add them to the call
            if (typeDecl.getTypeParameters() != null && !typeDecl.getTypeParameters().isEmpty()) {
                extendedType.getType().getTypeArgumentList().getTypeModels();
                TypeUtils.printTypeArguments(that, TypeUtils.matchTypeParametersWithArguments(typeDecl.getTypeParameters(),
                        extendedType.getType().getTypeArgumentList().getTypeModels()), gen, false);
                gen.out(",");
            }
            gen.out(gen.getNames().self(d), ")");
            gen.endLine(true);

            copySuperMembers(typeDecl, superDecs, d, gen);
        }
    }

    static void callInterfaces(final Tree.SatisfiedTypes satisfiedTypes, ClassOrInterface d, Tree.StatementOrArgument that,
            final List<Declaration> superDecs, final GenerateJsVisitor gen) {
        if (satisfiedTypes!=null) {
            HashSet<String> myTypeArgs = new HashSet<>();
            for (TypeParameter tp : d.getTypeParameters()) {
                myTypeArgs.add(tp.getName());
            }
            for (Tree.StaticType st: satisfiedTypes.getTypes()) {
                TypeDeclaration typeDecl = st.getTypeModel().getDeclaration();
                gen.qualify(that, typeDecl);
                gen.out(gen.getNames().name((ClassOrInterface)typeDecl), "(");
                if (typeDecl.getTypeParameters() != null && !typeDecl.getTypeParameters().isEmpty()) {
                    TypeUtils.printTypeArguments(that, st.getTypeModel().getTypeArguments(), gen, d.isToplevel());
                    gen.out(",");
                }
                gen.out(gen.getNames().self(d), ")");
                gen.endLine(true);
                //Set the reified types from interfaces
                Map<TypeParameter, ProducedType> reifs = st.getTypeModel().getTypeArguments();
                if (reifs != null && !reifs.isEmpty()) {
                    for (Map.Entry<TypeParameter, ProducedType> e : reifs.entrySet()) {
                        if (e.getValue().getDeclaration() instanceof ClassOrInterface) {
                            gen.out(GenerateJsVisitor.getClAlias(), "add_type_arg(", gen.getNames().self(d), ",'",
                                    e.getKey().getName(), "$", e.getKey().getDeclaration().getName(), "',");
                            TypeUtils.typeNameOrList(that, e.getValue(), gen, false);
                            gen.out(")");
                            gen.endLine(true);
                        }
                    }
                }
                copySuperMembers(typeDecl, superDecs, d, gen);
            }
        }
    }

    private static void copySuperMembers(final TypeDeclaration typeDecl, final List<Declaration> decs,
            final ClassOrInterface d, final GenerateJsVisitor gen) {
        if (!gen.opts.isOptimize()) {
            for (Declaration dec: decs) {
                if (!typeDecl.isMember(dec)) { continue; }
                String suffix = gen.getNames().scopeSuffix(dec.getContainer());
                if (dec instanceof Value && ((Value)dec).isTransient()) {
                    superGetterRef(dec,d,suffix, gen);
                    if (((Value) dec).isVariable()) {
                        superSetterRef(dec,d,suffix, gen);
                    }
                }
                else {
                    gen.out(gen.getNames().self(d), ".", gen.getNames().name(dec), suffix, "=",
                            gen.getNames().self(d), ".", gen.getNames().name(dec));
                    gen.endLine(true);
                }
            }
        }
    }

    private static void superGetterRef(final Declaration d, final ClassOrInterface sub,
            final String parentSuffix, final GenerateJsVisitor gen) {
        if (gen.defineAsProperty(d)) {
            gen.out(GenerateJsVisitor.getClAlias(), "copySuperAttr(", gen.getNames().self(sub), ",'",
                    gen.getNames().name(d), "','", parentSuffix, "')");
        }
        else {
            gen.out(gen.getNames().self(sub), ".", gen.getNames().getter(d), parentSuffix, "=",
                    gen.getNames().self(sub), ".", gen.getNames().getter(d));
        }
        gen.endLine(true);
    }

    private static void superSetterRef(final Declaration d, final ClassOrInterface sub,
            final String parentSuffix, final GenerateJsVisitor gen) {
        if (!gen.defineAsProperty(d)) {
            gen.out(gen.getNames().self(sub), ".", gen.getNames().setter(d), parentSuffix, "=",
                    gen.getNames().self(sub), ".", gen.getNames().setter(d));
            gen.endLine(true);
        }
    }

    public static class StaticTypeComparator implements Comparator<Tree.StaticType> {

        @Override
        public int compare(StaticType o1, StaticType o2) {
            final ProducedType t1 = o1.getTypeModel();
            final ProducedType t2 = o2.getTypeModel();
            if (t1.isUnknown()) {
                return t2.isUnknown() ? 0 : -1;
            }
            if (t2.isUnknown()) {
                return t1.isUnknown() ? 0 : -1;
            }
            if (t1.isSubtypeOf(t2)) {
                return 1;
            }
            if (t2.isSubtypeOf(t1)) {
                return -1;
            }
            //Check the members
            for (Declaration d : t1.getDeclaration().getMembers()) {
                if (d instanceof TypedDeclaration || d instanceof ClassOrInterface) {
                    Declaration d2 = t2.getDeclaration().getMember(d.getName(), null, false);
                    if (d2 != null) {
                        final Declaration dd2 = Util.getContainingDeclaration(d2);
                        if (dd2 instanceof TypeDeclaration && t1.getDeclaration().inherits((TypeDeclaration)dd2)) {
                            return 1;
                        }
                    }
                }
            }
            for (Declaration d : t2.getDeclaration().getMembers()) {
                if (d instanceof TypedDeclaration || d instanceof ClassOrInterface) {
                    Declaration d2 = t1.getDeclaration().getMember(d.getName(), null, false);
                    if (d2 != null) {
                        final Declaration dd2 = Util.getContainingDeclaration(d2);
                        if (dd2 instanceof TypeDeclaration && t2.getDeclaration().inherits((TypeDeclaration)dd2)) {
                            return -1;
                        }
                    }
                }
            }
            return 0;
        }
    }

}
