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
import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StaticType;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Method;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Util;
import com.redhat.ceylon.model.typechecker.model.Value;

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

        final boolean isInterface = d instanceof com.redhat.ceylon.model.typechecker.model.Interface;
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
        if (TypeUtils.isNativeExternal(d)) {
            //Allow native types to have their own initialization code
            genIniter = !gen.stitchInitializer(d);
        }
        if (genIniter) {
            final String qns = TypeUtils.qualifiedNameSkippingMethods(d);
            gen.out(gen.getClAlias(), initFuncName, "(", typename, ",'", qns, "'");
            final List<Tree.StaticType> supers = satisfiedTypes == null ? Collections.<Tree.StaticType>emptyList()
                    : new ArrayList<Tree.StaticType>(satisfiedTypes.getTypes().size()+1);

            if (extendedType != null) {
                if (satisfiedTypes == null) {
                    String fname = gen.typeFunctionName(extendedType.getType(),
                            !extendedType.getType().getDeclarationModel().isMember());
                    gen.out(",", fname);
                } else {
                    supers.add(extendedType.getType());
                }
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
        //If it's inside a dynamic interface, don't generate anything
        if (d.isClassOrInterfaceMember() && ((ClassOrInterface)d.getContainer()).isDynamic())return;
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
        final Tree.SatisfiedTypes sats = that.getSatisfiedTypes();
        callSupertypes(sats == null ? null : sats.getTypes(), null, d, that, superDecs, null, null, gen);
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
        //If it's inside a dynamic interface, don't generate anything
        if (d.isClassOrInterfaceMember() && ((ClassOrInterface)d.getContainer()).isDynamic())return;
        final Tree.ParameterList plist = that.getParameterList();
        final List<Tree.Constructor> constructors;
        final Tree.SatisfiedTypes sats = that.getSatisfiedTypes();
        //Find the constructors, if any
        Tree.Constructor defconstr = null;
        if (d.hasConstructors()) {
            constructors = new ArrayList<>(3);
            for (Tree.Statement st : that.getClassBody().getStatements()) {
                if (st instanceof Tree.Constructor) {
                    Tree.Constructor constr = (Tree.Constructor)st;
                    constructors.add(constr);
                    if (constr.getDeclarationModel().getName() == null) {
                        defconstr = constr;
                    }
                }
            }
        } else {
            constructors = Collections.emptyList();
        }
        gen.comment(that);
        if (gen.shouldStitch(d)) {
            boolean bye = false;
            if (d.hasConstructors() && defconstr == null) {
                gen.out(GenerateJsVisitor.function, gen.getNames().name(d));
                gen.out("(){");
                gen.generateThrow("Exception", d.getQualifiedNameString() + " has no default constructor.", that);
                gen.out(";}"); gen.endLine();
            }
            if (gen.stitchNative(d, that)) {
                if (d.isShared()) {
                    gen.share(d);
                }
                initializeType(that, gen);
                bye = true;
            }
            if (d.hasConstructors()) {
                for (Tree.Constructor cnstr : constructors) {
                    classConstructor(cnstr, that, constructors, gen);
                }
            }
            if (bye)return;
        }
        gen.out(GenerateJsVisitor.function, gen.getNames().name(d));
        //If there's a default constructor, create a different function with this code
        if (d.hasConstructors()) {
            if (defconstr == null) {
                gen.out("(){");
                gen.generateThrow("Exception", d.getQualifiedNameString() + " has no default constructor.", that);
                gen.out(";}"); gen.endLine();
                gen.out(GenerateJsVisitor.function, gen.getNames().name(d));
            }
            gen.out("$$c");
        }
        final boolean withTargs = generateParameters(that.getTypeParameterList(), plist, d, gen);
        gen.beginBlock();
        if (!d.hasConstructors()) {
            //This takes care of top-level attributes defined before the class definition
            gen.out("$init$", gen.getNames().name(d), "();");
            gen.endLine();
            gen.declareSelf(d);
            gen.referenceOuter(d);
        }
        final String me = gen.getNames().self(d);
        if (withTargs) {
            gen.out(gen.getClAlias(), "set_type_args(", me, ",$$targs$$)");
            gen.endLine(true);
        } else {
            //Check if any of the satisfied types have type arguments
            if (sats != null) {
                for(Tree.StaticType sat : sats.getTypes()) {
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
        if (!d.isToplevel() && d.getContainer() instanceof Method
                && !((Method)d.getContainer()).getTypeParameters().isEmpty()) {
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
        final Tree.ExtendedType extendedType = that.getExtendedType();
        callSupertypes(sats == null ? null : sats.getTypes(), extendedType == null ? null : extendedType.getType(),
                d, that, superDecs, extendedType == null ? null : extendedType.getInvocationExpression(),
                extendedType == null ? null : d.getExtendedTypeDeclaration().getParameterList(), gen);

        if (!gen.opts.isOptimize() && plist != null) {
            //Fix #231 for lexical scope
            for (Tree.Parameter p : plist.getParameters()) {
                if (!p.getParameterModel().isHidden()){
                    gen.generateAttributeForParameter(that, d, p.getParameterModel());
                }
            }
        }
        if (!d.hasConstructors()) {
            if (TypeUtils.isNativeExternal(d)) {
                gen.stitchConstructorHelper(that, "_cons_before");
            }
            gen.visitStatements(that.getClassBody().getStatements());
            if (d.isNative()) {
                gen.stitchConstructorHelper(that, "_cons_after");
            }
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
                    gen.getNames().name(d), "_", gen.getNames().name(defconstr.getDeclarationModel()), ".apply(",
                    _this, ",arguments);}");
            gen.endLine();
        }
        for (Tree.Constructor cnstr : constructors) {
            classConstructor(cnstr, that, constructors, gen);
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
            final Class d, final ParameterList plist, final Node that, final boolean pseudoAbstractConstructor,
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
            if (pseudoAbstractConstructor) {
                if (typeDecl instanceof Constructor) {
                    gen.out(gen.memberAccessBase(extendedType, typeDecl, false, qpath), "$$a(");
                } else {
                    gen.out(gen.memberAccessBase(extendedType, typeDecl, false, qpath), "_$c$$$a(");
                }
            } else {
                gen.out(gen.memberAccessBase(extendedType, typeDecl, false, qpath),
                        (gen.opts.isOptimize() && (gen.getSuperMemberScope(extendedType) != null))
                        ? ".call(this," : "(");
            }

            gen.getInvoker().generatePositionalArguments(invocation.getPrimary(),
                    argList, argList.getPositionalArguments(), false, false);
            if (argList.getPositionalArguments().size() > 0) {
                gen.out(",");
            }
            //There may be defaulted args we must pass as undefined
            if (plist != null && plist.getParameters().size() > argList.getPositionalArguments().size()) {
                for (int i = argList.getPositionalArguments().size(); i < plist.getParameters().size(); i++) {
                    com.redhat.ceylon.model.typechecker.model.Parameter p = plist.getParameters().get(i);
                    if (p.isSequenced()) {
                        gen.out(gen.getClAlias(), "empty(),");
                    } else {
                        gen.out("undefined,");
                    }
                }
            }
            //If the supertype has type arguments, add them to the call
            if (typeDecl.getTypeParameters() != null && !typeDecl.getTypeParameters().isEmpty()) {
                List<ProducedType> typeArgs = null;
                if (extendedType.getTypeArgumentList() != null) {
                    typeArgs = extendedType.getTypeArgumentList().getTypeModels();
                }
                TypeUtils.printTypeArguments(that,
                        TypeUtils.matchTypeParametersWithArguments(typeDecl.getTypeParameters(), typeArgs),
                        gen, false, null);
                gen.out(",");
            }
            gen.out(gen.getNames().self(d), ")");
            gen.endLine(true);
        }
        copySuperMembers(typeDecl, superDecs, d, gen);
    }

    static void callSupertypes(final List<Tree.StaticType> sats, final Tree.SimpleType supertype,
            final ClassOrInterface d, final Node that, final List<Declaration> superDecs,
            final Tree.InvocationExpression invoke, final ParameterList plist, final GenerateJsVisitor gen) {
        if (sats != null) {
            final ArrayList<Tree.StaticType> supers = new ArrayList<>(sats.size()+1);
            supers.addAll(sats);
            if (supertype != null) {
                supers.add(supertype);
            }
            Collections.sort(supers, new StaticTypeComparator());
            HashSet<String> myTypeArgs = new HashSet<>();
            for (TypeParameter tp : d.getTypeParameters()) {
                myTypeArgs.add(tp.getName());
            }
            for (Tree.StaticType st: supers) {
                if (st == supertype) {
                    callSuperclass(supertype, invoke, (Class)d, plist, that, false, superDecs, gen);
                } else {
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
        } else if (supertype != null) {
            callSuperclass(supertype, invoke, (Class)d, plist, that, false, superDecs, gen);
        }
    }

    private static void copySuperMembers(final TypeDeclaration typeDecl, final List<Declaration> decs,
            final ClassOrInterface d, final GenerateJsVisitor gen) {
        if (!gen.opts.isOptimize() && decs != null) {
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
        if (AttributeGenerator.defineAsProperty(d)) {
            gen.out(gen.getClAlias(), "copySuperAttr(", gen.getNames().self(sub), ",'",
                    gen.getNames().name(d), "','", parentSuffix, "')");
        }
        else {
            gen.out(gen.getNames().self(sub), ".", gen.getNames().getter(d, false), parentSuffix, "=",
                    gen.getNames().self(sub), ".", gen.getNames().getter(d, false));
        }
        gen.endLine(true);
    }

    private static void superSetterRef(final Declaration d, final ClassOrInterface sub,
            final String parentSuffix, final GenerateJsVisitor gen) {
        if (!AttributeGenerator.defineAsProperty(d)) {
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
        final boolean addToPrototype = gen.opts.isOptimize() && d != null && d.isClassOrInterfaceMember();
        final boolean isObjExpr = that instanceof Tree.ObjectExpression;
        final Class c = (Class)(isObjExpr ? ((Tree.ObjectExpression)that).getAnonymousClass() : d.getTypeDeclaration());
        final String className = gen.getNames().name(c);
        final String objectName = gen.getNames().name(d);
        final String selfName = gen.getNames().self(c);

        gen.out(GenerateJsVisitor.function, className);
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
        if (isObjExpr) {
            gen.out("var ", selfName, "=new ", className, ".$$;");
            final ClassOrInterface coi = Util.getContainingClassOrInterface(c.getContainer());
            if (coi != null) {
                gen.out(selfName, ".outer$=", gen.getNames().self(coi));
                gen.endLine(true);
            }
        } else {
            if (c.isMember()) {
                gen.initSelf(that);
            }
            gen.instantiateSelf(c);
            gen.referenceOuter(c);
        }
        
        final List<Declaration> superDecs = new ArrayList<Declaration>();
        if (!gen.opts.isOptimize()) {
            new SuperVisitor(superDecs).visit(body);
        }
        if (!targs.isEmpty()) {
            gen.out(selfName, ".$$targs$$=$$targs$$");
            gen.endLine(true);
        }
        TypeGenerator.callSupertypes(sats == null ? null : sats.getTypes(),
                superType == null ? null : superType.getType(), c, that, superDecs,
                superType == null ? null : superType.getInvocationExpression(),
                superType == null ? null : c.getExtendedTypeDeclaration().getParameterList(), gen);
        
        body.visit(gen);
        gen.out("return ", selfName, ";");
        gen.endBlock();
        gen.out(";", className, ".$crtmm$=");
        TypeUtils.encodeForRuntime(that, c, gen);
        gen.endLine(true);
        TypeGenerator.initializeType(that, gen);
        final String objvar = (addToPrototype ? "this.":"")+gen.getNames().createTempVariable();

        if (d != null && !addToPrototype) {
            gen.out("var ", objvar);
            //If it's a property, create the object here
            if (AttributeGenerator.defineAsProperty(d)) {
                gen.out("=", className, "(");
                if (!targs.isEmpty()) {
                    TypeUtils.printTypeArguments(that, targs, gen, false, null);
                }
                gen.out(")");
            }
            gen.endLine(true);
        }

        if (d != null && AttributeGenerator.defineAsProperty(d)) {
            gen.out(gen.getClAlias(), "atr$(");
            gen.outerSelf(d);
            gen.out(",'", objectName, "',function(){return ");
            if (addToPrototype) {
                gen.out("this.", gen.getNames().privateName(d));
            } else {
                gen.out(objvar);
            }
            gen.out(";},undefined,");
            TypeUtils.encodeForRuntime(d, annots, gen);
            gen.out(")");
            gen.endLine(true);
        } else if (d != null) {
            final String objectGetterName = gen.getNames().getter(d, false);
            gen.out(GenerateJsVisitor.function, objectGetterName, "()");
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
            gen.out(");", objvar, ".$crtmm$=", objectGetterName, ".$crtmm$;}");
            gen.endLine();
            gen.out("return ", objvar, ";");
            gen.endBlockNewLine();            
            
            if (addToPrototype || d.isShared()) {
                gen.outerSelf(d);
                gen.out(".", objectGetterName, "=", objectGetterName);
                gen.endLine(true);
            }
            if (!d.isToplevel()) {
                if(gen.outerSelf(d))gen.out(".");
            }
            gen.out(objectGetterName, ".$crtmm$=");
            TypeUtils.encodeForRuntime(d, annots, gen);
            gen.endLine(true);
            gen.out(gen.getNames().getter(c, true), "=", objectGetterName);
            gen.endLine(true);
            if (d.isToplevel()) {
                final String objectGetterNameMM = gen.getNames().getter(d, true);
                gen.out("ex$.", objectGetterNameMM, "=", objectGetterNameMM);
                gen.endLine(true);
            }
        } else if (that instanceof Tree.ObjectExpression) {
            gen.out("return ", className, "();");
        }
    }
    static void objectDefinition(final Tree.ObjectDefinition that, final GenerateJsVisitor gen) {
        //Don't even bother with nodes that have errors
        if (errVisitor.hasErrors(that))return;
        gen.comment(that);
        defineObject(that, that.getDeclarationModel(), that.getSatisfiedTypes(), that.getExtendedType(),
                that.getClassBody(), that.getAnnotationList(), gen);
        //Objects defined inside methods need their init sections are exec'd
        if (!that.getDeclarationModel().isToplevel() && !that.getDeclarationModel().isClassOrInterfaceMember()) {
            gen.out(gen.getNames().objectName(that.getDeclarationModel()), "();");
        }
    }

    static boolean localConstructorDelegation(final TypeDeclaration that, final List<Tree.Constructor> constructors) {
        if (that.isAbstract()) {
            return false;
        }
        for (Tree.Constructor cnst : constructors) {
            if (cnst.getDelegatedConstructor() != null) {
                final TypeDeclaration superdec = cnst.getDelegatedConstructor().getType().getDeclarationModel();
                if (superdec instanceof Class && that.getName()==null) {
                    //Default constructor
                    return true;
                } else if (superdec.equals(that)) {
                    return true;
                }
            }
        }
        return false;
    }

    static void classConstructor(final Tree.Constructor that, final Tree.ClassDefinition cdef,
            final List<Tree.Constructor> constructors, final GenerateJsVisitor gen) {
        gen.comment(that);
        Constructor d = that.getDeclarationModel();
        final Class container = cdef.getDeclarationModel();
        final String fullName = gen.getNames().name(container) + "_" + gen.getNames().name(d);
        if (!TypeUtils.isNativeExternal(d) || !gen.stitchNative(d, that)) {
            gen.out("function ", fullName);
            boolean forceAbstract = localConstructorDelegation(that.getDeclarationModel(), constructors) || d.isAbstract();
            if (forceAbstract) {
                gen.out("$$a");
            }
            final boolean withTargs = generateParameters(cdef.getTypeParameterList(), that.getParameterList(),
                    container, gen);
            final String me = gen.getNames().self(container);
            gen.beginBlock();
            if (forceAbstract) {
                if (that.getDelegatedConstructor() != null) {
                    final TypeDeclaration superdec = that.getDelegatedConstructor().getType().getDeclarationModel();
                    ParameterList plist = superdec instanceof Class ? ((Class)superdec).getParameterList() :
                        ((Constructor)superdec).getParameterLists().get(0);
                    callSuperclass(that.getDelegatedConstructor().getType(),
                            that.getDelegatedConstructor().getInvocationExpression(),
                            container, plist, that, false, null, gen);
                }
                gen.generateClassStatements(cdef, that, null, 1);
                gen.out("return ", me, ";");
                gen.endBlockNewLine(true);
                gen.out("function ", fullName);
                generateParameters(cdef.getTypeParameterList(), that.getParameterList(),
                        container, gen);
                gen.beginBlock();
            }
            if (!d.isAbstract()) {
                gen.out("$init$", gen.getNames().name(container), "();");
                gen.endLine();
                gen.declareSelf(container);
                gen.referenceOuter(container);
            }
            boolean pseudoAbstract = false;
            if (forceAbstract) {
                gen.out(fullName, "$$a");
                generateParameters(cdef.getTypeParameterList(), that.getParameterList(),
                        container, gen);
                gen.endLine(true);
            } else if (that.getDelegatedConstructor() != null) {
                final TypeDeclaration superdec = that.getDelegatedConstructor().getType().getDeclarationModel();
                pseudoAbstract = superdec instanceof Class ? superdec==container :
                    ((Constructor)superdec).getContainer()==container;
                ParameterList plist = superdec instanceof Class ? ((Class)superdec).getParameterList() :
                    ((Constructor)superdec).getParameterLists().get(0);
                callSuperclass(that.getDelegatedConstructor().getType(),
                        that.getDelegatedConstructor().getInvocationExpression(),
                        container, plist, that, pseudoAbstract, null, gen);
            }
            if (!d.isAbstract()) {
                //Call common initializer
                gen.out(gen.getNames().name(container), "$$c(");
                if (withTargs) {
                    gen.out("$$targs$$,");
                }
                gen.out(me, ");");
                gen.endLine();
            }
            gen.initParameters(that.getParameterList(), container, null);
            if (d.isNative()) {
                gen.stitchConstructorHelper(cdef, "_cons_before");
            }
            if (forceAbstract) {
                gen.generateClassStatements(cdef, that, null, 2);
            } else if (pseudoAbstract) {
                //Pass the delegated constructor
                final TypeDeclaration superdec = that.getDelegatedConstructor().getType().getDeclarationModel();
                Tree.Constructor pseudo1 = null;
                if (superdec instanceof Class) {
                    for (Tree.Constructor _cns : constructors) {
                        if (_cns.getDeclarationModel().getName() == null) {
                            pseudo1 = _cns;
                        }
                    }
                } else {
                    for (Tree.Constructor _cns : constructors) {
                        if (_cns.getDeclarationModel() == superdec) {
                            pseudo1 = _cns;
                        }
                    }
                }
                gen.generateClassStatements(cdef, pseudo1, that, 2);
            } else {
                gen.generateClassStatements(cdef, that, null, 0);
            }
            if (d.isNative()) {
                gen.stitchConstructorHelper(cdef, "_cons_after");
            }
            gen.out("return ", me, ";");
            gen.endBlockNewLine(true);
        }
        //Add reference to metamodel
        gen.out(fullName, ".$crtmm$=");
        TypeUtils.encodeForRuntime(d, that.getAnnotationList(), gen);
        gen.endLine(true);
        gen.out(gen.getNames().name(container), ".", fullName, "=", fullName);
        gen.endLine(true);
        if (gen.outerSelf(container)) {
            gen.out(".", fullName, "=", fullName);
            gen.endLine(true);
        }
    }

}
