package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.js.GenerateJsVisitor.PrototypeInitCallback;
import com.redhat.ceylon.compiler.js.GenerateJsVisitor.SuperVisitor;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Constructor;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
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
    static void initializeType(final Node type, final GenerateJsVisitor gen) {
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
        } else if (type instanceof Tree.ObjectExpression) {
            Tree.ObjectExpression objectDef = (Tree.ObjectExpression) type;
            extendedType = objectDef.getExtendedType();
            satisfiedTypes = objectDef.getSatisfiedTypes();
            decl = (ClassOrInterface)objectDef.getAnonymousClass();
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

        final String typename = gen.getNames().name(d);
        final String initname;
        if (d.isAnonymous()) {
            final String _initname = gen.getNames().objectName(d);
            if (d.isToplevel()) {
                initname = "$init$" + _initname.substring(0, _initname.length()-2);
            } else {
                initname = "$init$" + _initname;
            }
        } else {
            initname = "$init$" + typename;
        }
        gen.out("function ", initname, "()");
        gen.beginBlock();
        gen.out("if(", typename, ".$$===undefined)");
        gen.beginBlock();
        boolean genIniter = true;
        if (d.isNative()) {
            //Allow native types to have their own initialization code
            genIniter = !gen.stitchInitializer(d);
        }
        if (genIniter) {
            final String qns = TypeUtils.qualifiedNameSkippingMethods(d);
            gen.out(gen.getClAlias(), initFuncName, "(", typename, ",'", qns, "'");
            final List<Tree.StaticType> supers = satisfiedTypes == null ? Collections.<Tree.StaticType>emptyList()
                    : new ArrayList<Tree.StaticType>(satisfiedTypes.getTypes().size());

            if (extendedType != null) {
                String fname = gen.typeFunctionName(extendedType.getType(),
                        !extendedType.getType().getDeclarationModel().isMember());
                gen.out(",", fname);
            } else if (!isInterface) {
                gen.out(",", gen.getClAlias(), "Basic");
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
            gen.out(containers.toString(), "=", typename, ";");
        }

        //The class definition needs to be inside the init function if we want forwards decls to work in prototype style
        if (gen.opts.isOptimize()) {
            gen.endLine();
            callback.addToPrototypeCallback();
        }
        gen.endBlockNewLine();
        gen.out("return ", typename, ";");
        gen.endBlockNewLine();
        //If it's nested, share the init function
        if (gen.outerSelf(d)) {
            gen.out(".", initname, "=", initname);
            gen.endLine(true);
        }
        gen.out(initname, "()");
        gen.endLine(true);
    }

    static void interfaceDefinition(final Tree.InterfaceDefinition that, final GenerateJsVisitor gen) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        final Interface d = that.getDeclarationModel();
        gen.comment(that);

        gen.out(GenerateJsVisitor.function, gen.getNames().name(d));
        final boolean withTargs = generateParameters(that.getTypeParameterList(), null, d, gen);
        gen.beginBlock();
        //declareSelf(d);
        gen.referenceOuter(d);
        final List<Declaration> superDecs = new ArrayList<Declaration>(3);
        if (!gen.opts.isOptimize()) {
            new SuperVisitor(superDecs).visit(that.getInterfaceBody());
        }
        callInterfaces(that.getSatisfiedTypes() == null ? null : that.getSatisfiedTypes().getTypes(),
                d, that, superDecs, gen);
        if (withTargs) {
            gen.out(gen.getClAlias(), "set_type_args(", gen.getNames().self(d),
                    ",$$targs$$,", gen.getNames().name(d), ")");
            gen.endLine(true);
        }
        if (!d.isToplevel() && d.getContainer() instanceof Method && !((Method)d.getContainer()).getTypeParameters().isEmpty()) {
            gen.out(gen.getClAlias(), "set_type_args(", gen.getNames().self(d), ",$$$mptypes,",
                    gen.getNames().name(d), ")");
            gen.endLine(true);
        }
        that.getInterfaceBody().visit(gen);
        //returnSelf(d);
        gen.endBlockNewLine();
        if (d.isDynamic()) {
            //Add the list of expected members here
            final List<Declaration> members = d.getMembers();
            gen.out(gen.getNames().name(d), ".dynmem$=[");
            if (members.isEmpty()) {
                gen.out("];");
            } else {
                gen.out("'");
                boolean first = true;
                for (Declaration m : members) {
                    if (first)first=false;else gen.out("','");
                    gen.out(gen.getNames().name(m));
                }
                gen.out("'];");
            }
        }
        //Add reference to metamodel
        gen.out(gen.getNames().name(d), ".$crtmm$=");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), gen);
        gen.endLine(true);
        gen.share(d);
        initializeType(that, gen);
    }

    /** Outputs the parameter list of the type's constructor, including surrounding parens.
     * Returns true if the type has type parameters. */
    static boolean generateParameters(final Tree.TypeParameterList tparms,
            final Tree.ParameterList plist, final TypeDeclaration d, final GenerateJsVisitor gen) {
        gen.out("(");
        final boolean withTargs = tparms != null &&
                !tparms.getTypeParameterDeclarations().isEmpty();
        if (plist != null) {
            for (Tree.Parameter p: plist.getParameters()) {
                p.visit(gen);
                gen.out(",");
            }
        }
        if (withTargs) {
            gen.out("$$targs$$,");
        }
        gen.out(gen.getNames().self(d), ")");
        return withTargs;
    }

    static void classDefinition(final Tree.ClassDefinition that, final GenerateJsVisitor gen) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        final Class d = that.getDeclarationModel();
        final Tree.ParameterList plist = that.getParameterList();
        final List<Tree.Constructor> constructors;
        final List<Tree.Statement> classBody;
        //Find the constructors, if any
        Tree.Constructor defconstr = null;
        if (d.hasConstructors()) {
            constructors = new ArrayList<>(3);
            classBody = new ArrayList<>(that.getClassBody().getStatements().size());
            for (Tree.Statement st : that.getClassBody().getStatements()) {
                if (st instanceof Tree.Constructor) {
                    Tree.Constructor constr = (Tree.Constructor)st;
                    constructors.add(constr);
                    if (constr.getDeclarationModel().getName().equals(d.getName())) {
                        defconstr = constr;
                    }
                } else {
                    classBody.add(st);
                }
            }
        } else {
            constructors = Collections.emptyList();
            classBody = that.getClassBody().getStatements();
        }
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
        //If there's a default constructor, create a different function with this code
        if (defconstr != null) {
            gen.out("$$c");
        }
        final boolean withTargs = generateParameters(that.getTypeParameterList(), plist, d, gen);
        gen.beginBlock();
        if (!d.hasConstructors()) {
            //This takes care of top-level attributes defined before the class definition
            gen.out("$init$", gen.getNames().name(d), "();");
            gen.endLine();
            gen.declareSelf(d);
        }
        gen.referenceOuter(d);
        final String me = gen.getNames().self(d);
        if (withTargs) {
            gen.out(gen.getClAlias(), "set_type_args(", me, ",$$targs$$)");
            gen.endLine(true);
        } else {
            //Check if any of the satisfied types have type arguments
            if (that.getSatisfiedTypes() != null) {
                for(Tree.StaticType sat : that.getSatisfiedTypes().getTypes()) {
                    boolean first = true;
                    Map<TypeParameter,ProducedType> targs = sat.getTypeModel().getTypeArguments();
                    if (targs != null && !targs.isEmpty()) {
                        if (first) {
                            gen.out(me, ".$$targs$$=");
                            TypeUtils.printTypeArguments(that, targs, gen, false, null);
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
            gen.out(gen.getClAlias(), "set_type_args(", me, ",$$$mptypes)");
            gen.endLine(true);
        }
        if (plist != null) {
            gen.initParameters(plist, d, null);
        }

        final List<Declaration> superDecs = new ArrayList<Declaration>(3);
        if (!gen.opts.isOptimize()) {
            new SuperVisitor(superDecs).visit(that.getClassBody());
        }
        if (that.getExtendedType() != null) {
            callSuperclass(that.getExtendedType().getType(), that.getExtendedType().getInvocationExpression(),
                    d, d.getExtendedTypeDeclaration().getParameterList(), that, superDecs, gen);
        }
        callInterfaces(that.getSatisfiedTypes() == null ? null : that.getSatisfiedTypes().getTypes(),
                d, that, superDecs, gen);

        if (!gen.opts.isOptimize()) {
            //Fix #231 for lexical scope
            for (Tree.Parameter p : plist.getParameters()) {
                if (!p.getParameterModel().isHidden()){
                    gen.generateAttributeForParameter(that, d, p.getParameterModel());
                }
            }
        }
        if (d.isNative()) {
            gen.stitchConstructorHelper(that, "_cons_before");
        }
        gen.visitStatements(classBody);
        if (d.isNative()) {
            gen.stitchConstructorHelper(that, "_cons_after");
        }
        if (constructors.isEmpty()) {
            gen.out("return ", me, ";");
        }
        gen.endBlockNewLine();
        if (defconstr != null) {
            //Define a function as the class and call the default constructor in there
            String _this = "undefined";
            if (!d.isToplevel()) {
                final ClassOrInterface coi = Util.getContainingClassOrInterface(d.getContainer());
                if (coi != null) {
                    if (d.isClassOrInterfaceMember()) {
                        _this = "this";
                    } else {
                        _this = gen.getNames().self(coi);
                    }
                }
            }
            gen.out(GenerateJsVisitor.function, gen.getNames().name(d), "(){return ",
                    gen.getNames().name(d), ".", gen.getNames().name(d), ".apply(",
                    _this, ",arguments);}");
            gen.endLine();
        }
        for (Tree.Constructor cnstr : constructors) {
            classConstructor(cnstr, that.getTypeParameterList(), d, gen);
        }
        //Add reference to metamodel
        gen.out(gen.getNames().name(d), ".$crtmm$=");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), gen);
        gen.endLine(true);
        gen.share(d);
        initializeType(that, gen);
        if (d.isSerializable()) {
            SerializationHelper.addDeserializer(that, d, gen);
        }
    }

    static void callSuperclass(final Tree.SimpleType extendedType, final Tree.InvocationExpression invocation,
            final Class d, final ParameterList plist, final Node that,
            final List<Declaration> superDecs, final GenerateJsVisitor gen) {
        TypeDeclaration typeDecl = extendedType.getDeclarationModel();
        if (invocation != null) {
            Tree.PositionalArgumentList argList = invocation.getPositionalArgumentList();
            final String qpath;
            if (typeDecl instanceof Constructor) {
                final String path = gen.qualifiedPath(that, (TypeDeclaration)typeDecl.getContainer(), false);
                if (path.isEmpty()) {
                    qpath = gen.getNames().name((TypeDeclaration)typeDecl.getContainer());
                } else {
                    qpath = path + "." + gen.getNames().name((TypeDeclaration)typeDecl.getContainer());
                }
            } else {
                qpath = gen.qualifiedPath(that, typeDecl, false);
            }
            gen.out(gen.memberAccessBase(extendedType, typeDecl, false, qpath),
                    (gen.opts.isOptimize() && (gen.getSuperMemberScope(extendedType) != null))
                    ? ".call(this," : "(");

            gen.getInvoker().generatePositionalArguments(invocation.getPrimary(),
                    argList, argList.getPositionalArguments(), false, false);
            if (argList.getPositionalArguments().size() > 0) {
                gen.out(",");
            }
            //There may be defaulted args we must pass as undefined
            if (plist != null && plist.getParameters().size() > argList.getPositionalArguments().size()) {
                for (int i = argList.getPositionalArguments().size(); i < plist.getParameters().size(); i++) {
                    com.redhat.ceylon.compiler.typechecker.model.Parameter p = plist.getParameters().get(i);
                    if (p.isSequenced()) {
                        gen.out(gen.getClAlias(), "getEmpty(),");
                    } else {
                        gen.out("undefined,");
                    }
                }
            }
            //If the supertype has type arguments, add them to the call
            if (typeDecl.getTypeParameters() != null && !typeDecl.getTypeParameters().isEmpty()) {
                extendedType.getTypeArgumentList().getTypeModels();
                TypeUtils.printTypeArguments(that, TypeUtils.matchTypeParametersWithArguments(typeDecl.getTypeParameters(),
                        extendedType.getTypeArgumentList().getTypeModels()), gen, false, null);
                gen.out(",");
            }
            gen.out(gen.getNames().self(d), ")");
            gen.endLine(true);
        }
        copySuperMembers(typeDecl, superDecs, d, gen);
    }

    static void callInterfaces(final List<Tree.StaticType> satisfiedTypes, ClassOrInterface d, Node that,
            final List<Declaration> superDecs, final GenerateJsVisitor gen) {
        if (satisfiedTypes!=null) {
            HashSet<String> myTypeArgs = new HashSet<>();
            for (TypeParameter tp : d.getTypeParameters()) {
                myTypeArgs.add(tp.getName());
            }
            for (Tree.StaticType st: satisfiedTypes) {
                TypeDeclaration typeDecl = st.getTypeModel().getDeclaration();
                gen.qualify(that, typeDecl);
                gen.out(gen.getNames().name((ClassOrInterface)typeDecl), "(");
                if (typeDecl.getTypeParameters() != null && !typeDecl.getTypeParameters().isEmpty()) {
                    TypeUtils.printTypeArguments(that, st.getTypeModel().getTypeArguments(), gen, d.isToplevel(), null);
                    gen.out(",");
                }
                gen.out(gen.getNames().self(d), ")");
                gen.endLine(true);
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
            gen.out(gen.getClAlias(), "copySuperAttr(", gen.getNames().self(sub), ",'",
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

    static void defineObject(final Node that, final Value d, final Tree.SatisfiedTypes sats,
            final Tree.ExtendedType superType, final Tree.ClassBody body, final Tree.AnnotationList annots,
            final GenerateJsVisitor gen) {
        boolean addToPrototype = gen.opts.isOptimize() && d != null && d.isClassOrInterfaceMember();
        final Class c = (Class)(that instanceof Tree.ObjectExpression ?
                ((Tree.ObjectExpression)that).getAnonymousClass() : d.getTypeDeclaration());

        gen.out(GenerateJsVisitor.function, gen.getNames().name(c));
        Map<TypeParameter, ProducedType> targs=new HashMap<TypeParameter, ProducedType>();
        if (sats != null) {
            for (StaticType st : sats.getTypes()) {
                Map<TypeParameter, ProducedType> stargs = st.getTypeModel().getTypeArguments();
                if (stargs != null && !stargs.isEmpty()) {
                    targs.putAll(stargs);
                }
            }
        }
        gen.out(targs.isEmpty()?"()":"($$targs$$)");
        gen.beginBlock();
        if (c.isMember()) {
            gen.initSelf(that);
        }
        gen.instantiateSelf(c);
        gen.referenceOuter(c);
        
        final List<Declaration> superDecs = new ArrayList<Declaration>();
        if (!gen.opts.isOptimize()) {
            new SuperVisitor(superDecs).visit(body);
        }
        if (!targs.isEmpty()) {
            gen.out(gen.getNames().self(c), ".$$targs$$=$$targs$$");
            gen.endLine(true);
        }
        if (superType != null) {
            TypeGenerator.callSuperclass(superType.getType(), superType.getInvocationExpression(),
                    c, c.getExtendedTypeDeclaration().getParameterList(), that, superDecs, gen);
        }
        TypeGenerator.callInterfaces(sats == null ? null : sats.getTypes(), c, that, superDecs, gen);
        
        body.visit(gen);
        gen.out("return ", gen.getNames().self(c), ";");
        gen.endBlock();
        gen.out(";", gen.getNames().name(c), ".$crtmm$=");
        TypeUtils.encodeForRuntime(that, c, gen);
        gen.endLine(true);

        TypeGenerator.initializeType(that, gen);

        if (d != null && !addToPrototype) {
            gen.out("var ", gen.getNames().name(d));
            //If it's a property, create the object here
            if (gen.defineAsProperty(d)) {
                gen.out("=", gen.getNames().name(c), "(");
                if (!targs.isEmpty()) {
                    TypeUtils.printTypeArguments(that, targs, gen, false, null);
                }
                gen.out(")");
            }
            gen.endLine(true);
        }

        if (d != null && gen.defineAsProperty(d)) {
            gen.out(gen.getClAlias(), "atr$(");
            gen.outerSelf(d);
            gen.out(",'", gen.getNames().name(d), "',function(){return ");
            if (addToPrototype) {
                gen.out("this.", gen.getNames().privateName(d));
            } else {
                gen.out(gen.getNames().name(d));
            }
            gen.out(";},undefined,");
            TypeUtils.encodeForRuntime(d, annots, gen);
            gen.out(")");
            gen.endLine(true);
        } else if (d != null) {
            final String objvar = (addToPrototype ? "this.":"")+gen.getNames().name(d);
            gen.out(GenerateJsVisitor.function, gen.getNames().getter(d), "()");
            gen.beginBlock();
            //Create the object lazily
            final String oname = gen.getNames().objectName(c);
            gen.out("if(", objvar, "===", gen.getClAlias(), "INIT$)");
            gen.generateThrow(gen.getClAlias()+"InitializationError",
                    "Cyclic initialization trying to read the value of '" +
                    d.getName() + "' before it was set", that);
            gen.endLine(true);
            gen.out("if(", objvar, "===undefined){", objvar, "=", gen.getClAlias(), "INIT$;",
                    objvar, "=$init$", oname);
            if (!oname.endsWith("()")) {
                gen.out("()");
            }
            gen.out("(");
            if (!targs.isEmpty()) {
                TypeUtils.printTypeArguments(that, targs, gen, false, null);
            }
            gen.out(");", objvar, ".$crtmm$=", gen.getNames().getter(d), ".$crtmm$;}");
            gen.endLine();
            gen.out("return ", objvar, ";");
            gen.endBlockNewLine();            
            
            if (addToPrototype || d.isShared()) {
                gen.outerSelf(d);
                gen.out(".", gen.getNames().getter(d), "=", gen.getNames().getter(d));
                gen.endLine(true);
            }
            if (!d.isToplevel()) {
                if(gen.outerSelf(d))gen.out(".");
            }
            gen.out(gen.getNames().getter(d), ".$crtmm$=");
            TypeUtils.encodeForRuntime(d, annots, gen);
            gen.endLine(true);
            if (!d.isToplevel()) {
                if (gen.outerSelf(d)) {
                    gen.out(".");
                }
            }
            gen.out("$prop$", gen.getNames().getter(d), "={get:");
            if (!d.isToplevel()) {
                if (gen.outerSelf(d)) {
                    gen.out(".");
                }
            }
            gen.out(gen.getNames().getter(d), ",$crtmm$:");
            if (!d.isToplevel()) {
                if (gen.outerSelf(d)) {
                    gen.out(".");
                }
            }
            gen.out(gen.getNames().getter(d), ".$crtmm$}");
            gen.endLine(true);
            //make available with the class name as well, for metamodel access
            gen.out(gen.getNames().getter(c), "=", gen.getNames().getter(d), ";$prop$",
                    gen.getNames().getter(c), "=", gen.getNames().getter(d));
            gen.endLine(true);
            if (d.isToplevel()) {
                gen.out("ex$.$prop$", gen.getNames().getter(d), "=$prop$",
                        gen.getNames().getter(d));
                gen.endLine(true);
            }
        } else if (that instanceof Tree.ObjectExpression) {
            gen.out("return  ", gen.getNames().name(c), "();");
        }
    }
    static void objectDefinition(final Tree.ObjectDefinition that, final GenerateJsVisitor gen) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        gen.comment(that);
        defineObject(that, that.getDeclarationModel(), that.getSatisfiedTypes(), that.getExtendedType(),
                that.getClassBody(), that.getAnnotationList(), gen);
    }

    static void classConstructor(final Tree.Constructor that,
            final Tree.TypeParameterList tparms, final Class container, final GenerateJsVisitor gen) {
        Constructor d = that.getDeclarationModel();
        gen.out(gen.getNames().name(container), ".", gen.getNames().name(d), "=function");
        final boolean withTargs = generateParameters(tparms, that.getParameterList(), container, gen);
        final String me = gen.getNames().self(container);
        gen.beginBlock();
        gen.out("$init$", gen.getNames().name(container), "();");
        gen.endLine();
        gen.declareSelf(container);
        if (that.getDelegatedConstructor() != null) {
            final TypeDeclaration superdec = that.getDelegatedConstructor().getType().getDeclarationModel();
            ParameterList plist = superdec instanceof Class ? ((Class)superdec).getParameterList() :
                ((Constructor)superdec).getParameterLists().get(0);
            callSuperclass(that.getDelegatedConstructor().getType(), that.getDelegatedConstructor().getInvocationExpression(),
                    container, plist, that, null, gen);
        }
        //Call common initializer
        //TODO always, or only when there's no delegated constructor?
        gen.out(gen.getNames().name(container), "$$c(");
        if (withTargs) {
            gen.out("$$targs$$,");
        }
        gen.out(me, ");");
        gen.endLine();
        gen.initParameters(that.getParameterList(), container, null);
        gen.visitStatements(that.getBlock().getStatements());
        gen.out("return ", me, ";");
        gen.endBlockNewLine(true);
        //TODO constructor metamodel
    }

}
