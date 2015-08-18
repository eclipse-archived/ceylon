package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import com.redhat.ceylon.compiler.js.GenerateJsVisitor.PrototypeInitCallback;
import com.redhat.ceylon.compiler.js.GenerateJsVisitor.SuperVisitor;
import com.redhat.ceylon.compiler.js.util.TypeComparator;
import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StaticType;
import com.redhat.ceylon.compiler.typechecker.util.NativeUtil;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Value;

public class TypeGenerator {

    static final ErrorVisitor errVisitor = new ErrorVisitor();

    /** Generates a function to initialize the specified type. */
    static void initializeType(final Node type, final GenerateJsVisitor gen) {
        Tree.ExtendedType extendedType = null;
        Tree.SatisfiedTypes satisfiedTypes = null;
        final ClassOrInterface decl;
        final List<Tree.Statement> stmts;
        Value objDecl = null;
        if (type instanceof Tree.ClassDefinition) {
            Tree.ClassDefinition classDef = (Tree.ClassDefinition) type;
            extendedType = classDef.getExtendedType();
            satisfiedTypes = classDef.getSatisfiedTypes();
            decl = classDef.getDeclarationModel();
            stmts = NativeUtil.mergeStatements(classDef.getClassBody(), gen.getNativeHeader(decl));
        } else if (type instanceof Tree.InterfaceDefinition) {
            satisfiedTypes = ((Tree.InterfaceDefinition) type).getSatisfiedTypes();
            decl = ((Tree.InterfaceDefinition) type).getDeclarationModel();
            stmts = ((Tree.InterfaceDefinition) type).getInterfaceBody().getStatements();
        } else if (type instanceof Tree.ObjectDefinition) {
            Tree.ObjectDefinition objectDef = (Tree.ObjectDefinition) type;
            extendedType = objectDef.getExtendedType();
            satisfiedTypes = objectDef.getSatisfiedTypes();
            decl = (ClassOrInterface)objectDef.getDeclarationModel().getTypeDeclaration();
            objDecl = objectDef.getDeclarationModel();
            stmts = NativeUtil.mergeStatements(objectDef.getClassBody(), gen.getNativeHeader(objDecl));
        } else if (type instanceof Tree.ObjectExpression) {
            Tree.ObjectExpression objectDef = (Tree.ObjectExpression) type;
            extendedType = objectDef.getExtendedType();
            satisfiedTypes = objectDef.getSatisfiedTypes();
            decl = (ClassOrInterface)objectDef.getAnonymousClass();
            stmts = objectDef.getClassBody().getStatements();
        } else if (type instanceof Tree.Enumerated) {
            Tree.Enumerated vc = (Tree.Enumerated)type;
            stmts = vc.getBlock().getStatements();
            decl = (ClassOrInterface)vc.getDeclarationModel().getTypeDeclaration().getContainer();
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
        typeInitialization(extendedType, satisfiedTypes, decl, callback, gen, objDecl);
    }

    /** This is now the main method to generate the type initialization code.
     * @param extendedType The type that is being extended.
     * @param satisfiedTypes The types satisfied by the type being initialized.
     * @param d The declaration for the type being initialized
     * @param callback A callback to add something more to the type initializer in prototype style.
     */
    static void typeInitialization(final Tree.ExtendedType extendedType, final Tree.SatisfiedTypes satisfiedTypes,
            final ClassOrInterface d, PrototypeInitCallback callback, final GenerateJsVisitor gen,
            final Value objectDeclaration) {

        final boolean isInterface = d instanceof com.redhat.ceylon.model.typechecker.model.Interface;
        String initFuncName = isInterface ? "initTypeProtoI" : "initTypeProto";

        final String typename = gen.getNames().name(d);
        final String initname;
        if (d.isAnonymous()) {
            String _initname = gen.getNames().objectName(d);
            if (objectDeclaration != null
                    && objectDeclaration.isNativeHeader()
                    && TypeUtils.makeAbstractNative(objectDeclaration)
                    && !(_initname.endsWith("$$N") || _initname.endsWith("$$N()"))) {
                if (_initname.endsWith("()")) {
                    _initname = _initname.substring(0, _initname.length()-2) + "$$N()";
                } else {
                    _initname += "$$N";
                }
            }
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
                    String fname = typeFunctionName(extendedType.getType(), d, gen);
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
                    String fname = typeFunctionName(satType, d, gen);
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

    /** Returns the name of the type or its $init$ function if it's local. */
    static String typeFunctionName(final Tree.StaticType type, final ClassOrInterface coi,
            final GenerateJsVisitor gen) {
        TypeDeclaration d = type.getTypeModel().getDeclaration();
        final boolean removeAlias = d == null || !d.isClassOrInterfaceMember() || d instanceof Interface;
        if ((removeAlias && d.isAlias()) || d instanceof Constructor) {
            Type extendedType = d.getExtendedType();
            d = extendedType==null ? null : extendedType.getDeclaration();
        }
        final boolean inProto = gen.opts.isOptimize()
                && (type.getScope().getContainer() instanceof TypeDeclaration);
        final boolean imported = gen.isImported(type.getUnit().getPackage(), d);
        final String initName = "$init$" + gen.getNames().name(d) + "()";
        if (!imported && !d.isClassOrInterfaceMember()) {
            return initName;
        }
        if (inProto && coi.isMember() && !d.isAlias() && (coi.getContainer() == ModelUtil.getContainingDeclaration(d)
                || ModelUtil.contains(d, coi))) {
            //A member class that extends or satisfies another member of its same container,
            //use its $init$ function
            return initName;
        }
        String tfn = gen.memberAccessBase(type, d, false, gen.qualifiedPath(type, d, inProto));
        if (removeAlias && !imported) {
            int idx = tfn.lastIndexOf('.');
            if (idx > 0) {
                tfn = tfn.substring(0, idx+1) + initName;
            } else {
                tfn = initName;
            }
        }
        return tfn;
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
        if (withTargs) {
            gen.out(gen.getClAlias(), "set_type_args(", gen.getNames().self(d),
                    ",$$targs$$,", gen.getNames().name(d), ")");
            gen.endLine(true);
        }
        callSupertypes(sats == null ? null : TypeUtils.getTypes(sats.getTypes()),
                null, d, that, superDecs, null, null, gen);
        if (!d.isToplevel() && d.getContainer() instanceof Function && !((Function)d.getContainer()).getTypeParameters().isEmpty()) {
            gen.out(gen.getClAlias(), "set_type_args(", gen.getNames().self(d),
                    ",", gen.getNames().typeArgsParamName((Function)d.getContainer()), ",",
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
            List<TypeParameter> typeParams;
            if (typeDecl instanceof Constructor) {
                //Output the type arguments to the constructor,
                //UNLESS you're in the same class, then just pass the type arguments object
                typeParams = ((Class)typeDecl.getContainer()).getTypeParameters();
                if (typeParams != null && !typeParams.isEmpty()) {
                    typeParams = null;
                    if (ModelUtil.contains(d, typeDecl)) {
                        gen.out("$$targs$$,");
                    } else {
                        TypeUtils.printTypeArguments(that,
                                extendedType.getTypeModel().getQualifyingType().getTypeArguments(),
                                gen, false, null);
                        gen.out(",");
                    }
                }
            } else {
                typeParams = typeDecl.getTypeParameters();
            }
            if (typeParams != null && !typeParams.isEmpty()) {
                List<Type> typeArgs = null;
                if (extendedType.getTypeArgumentList() != null) {
                    typeArgs = extendedType.getTypeArgumentList().getTypeModels();
                }
                TypeUtils.printTypeArguments(that,
                        TypeUtils.matchTypeParametersWithArguments(typeParams, typeArgs),
                        gen, false, null);
                gen.out(",");
            }
            gen.out(gen.getNames().self(d), ")");
            gen.endLine(true);
        }
        copySuperMembers(typeDecl, superDecs, d, gen);
    }

    static void callSupertypes(final List<Type> sats, final Tree.SimpleType supertype,
            final ClassOrInterface d, final Node that, final List<Declaration> superDecs,
            final Tree.InvocationExpression invoke, final ParameterList plist, final GenerateJsVisitor gen) {
        if (sats != null) {
            final ArrayList<Type> supers = new ArrayList<>(sats.size()+1);
            supers.addAll(sats);
            if (supertype != null) {
                supers.add(supertype.getTypeModel());
            }
            Collections.sort(supers, new TypeComparator());
            HashSet<String> myTypeArgs = new HashSet<>();
            for (TypeParameter tp : d.getTypeParameters()) {
                myTypeArgs.add(tp.getName());
            }
            for (Type st: supers) {
                if (supertype != null && st == supertype.getTypeModel()) {
                    callSuperclass(supertype, invoke, (Class)d, plist, that, false, superDecs, gen);
                } else {
                    TypeDeclaration typeDecl = st.getDeclaration();
                    gen.qualify(that, typeDecl);
                    gen.out(gen.getNames().name((ClassOrInterface)typeDecl), "(");
                    if (typeDecl.getTypeParameters() != null && !typeDecl.getTypeParameters().isEmpty()) {
                        TypeUtils.printTypeArguments(that, st.getTypeArguments(), gen, d.isToplevel(), null);
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
            final Type t1 = o1.getTypeModel();
            final Type t2 = o2.getTypeModel();
            if (ModelUtil.isTypeUnknown(t1)) {
                return ModelUtil.isTypeUnknown(t2) ? 0 : -1;
            }
            if (ModelUtil.isTypeUnknown(t2)) {
                return ModelUtil.isTypeUnknown(t1) ? 0 : -1;
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
                        final Declaration dd2 = ModelUtil.getContainingDeclaration(d2);
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
                        final Declaration dd2 = ModelUtil.getContainingDeclaration(d2);
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
