package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.js.loader.MetamodelGenerator;
import com.redhat.ceylon.compiler.js.util.TypeUtils;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;

public class FunctionHelper {

    static interface ParameterListCallback {
        void completeFunction();
    }

    static void multiStmtFunction(final List<Tree.ParameterList> paramLists,
            final Tree.Block block, final Scope scope, final boolean initSelf, final GenerateJsVisitor gen) {
        generateParameterLists(block, paramLists, scope, new ParameterListCallback() {
            @Override
            public void completeFunction() {
                gen.beginBlock();
                if (paramLists.size() == 1 && scope!=null && initSelf) { gen.initSelf(block); }
                if (scope instanceof Function) {
                    addParentMethodTypeParameters((Function)scope, gen);
                }
                gen.initParameters(paramLists.get(paramLists.size()-1),
                        scope instanceof TypeDeclaration ? (TypeDeclaration)scope : null, null);
                gen.visitStatements(block.getStatements());
                gen.endBlock();
            }
        }, true, gen);
    }
    static void singleExprFunction(final List<Tree.ParameterList> paramLists,
                                    final Tree.Expression expr, final Scope scope, final boolean initSelf,
                                    final boolean emitFunctionKeyword, final GenerateJsVisitor gen) {
        generateParameterLists(expr, paramLists, scope, new ParameterListCallback() {
            @Override
            public void completeFunction() {
                gen.out("{");
                if (paramLists.size() == 1 && scope != null && initSelf) { gen.initSelf(expr); }
                if (scope instanceof Function) {
                    addParentMethodTypeParameters((Function)scope, gen);
                }
                gen.initParameters(paramLists.get(paramLists.size()-1),
                        null, scope instanceof Function ? (Function)scope : null);
                gen.out("return ");
                if (!gen.isNaturalLiteral(expr.getTerm())) {
                    expr.visit(gen);
                }
                gen.out(";}");
            }
        }, emitFunctionKeyword, gen);
    }

    /** Generates the code for single or multiple parameter lists, with a callback function to generate the function blocks. */
    static void generateParameterLists(final Node context, final List<Tree.ParameterList> plist, final Scope scope,
                final ParameterListCallback callback, final boolean emitFunctionKeyword, final GenerateJsVisitor gen) {
        if (plist.size() == 1) {
            if (emitFunctionKeyword) {
                gen.out("function");
            }
            Tree.ParameterList paramList = plist.get(0);
            paramList.visit(gen);
            callback.completeFunction();
        } else {
            List<MplData> metas = new ArrayList<>(plist.size());
            Function m = scope instanceof Function ? (Function)scope : null;
            for (Tree.ParameterList paramList : plist) {
                final MplData mpl = new MplData();
                metas.add(mpl);
                mpl.n=context;
                if (metas.size()==1) {
                    if (emitFunctionKeyword) {
                        gen.out("function");
                    }
                } else {
                    mpl.name=gen.getNames().createTempVariable();
                    mpl.params=paramList;
                    gen.out("var ",  mpl.name, "=function");
                }
                paramList.visit(gen);
                if (metas.size()==1) {
                    gen.beginBlock();
                    gen.initSelf(context);
                    Scope parent = scope == null ? null : scope.getContainer();
                    gen.initParameters(paramList, parent instanceof TypeDeclaration ? (TypeDeclaration)parent : null, m);
                }
                else {
                    gen.out("{");
                }
            }
            callback.completeFunction();
            closeMPL(metas, m.getType(), gen);
            gen.endBlock();
        }
    }

    static void attributeArgument(final Tree.AttributeArgument that, final GenerateJsVisitor gen) {
        gen.out("(function()");
        gen.beginBlock();
        if (gen.opts.isVerbose() && that.getParameter() != null) {
            gen.out("//AttributeArgument ", that.getParameter().getName());
            gen.location(that);
            gen.endLine();
        }
        
        Tree.Block block = that.getBlock();
        Tree.SpecifierExpression specExpr = that.getSpecifierExpression();
        if (specExpr != null) {
            gen.out("return ");
            if (!gen.isNaturalLiteral(specExpr.getExpression().getTerm())) {
                specExpr.getExpression().visit(gen);
            }
            gen.out(";");
        }
        else if (block != null) {
            gen.visitStatements(block.getStatements());
        }
        
        gen.endBlock();
        gen.out("())");
    }

    static void objectArgument(final Tree.ObjectArgument that, final GenerateJsVisitor gen) {
        final Class c = (Class)that.getDeclarationModel().getTypeDeclaration();

        gen.out("(function()");
        gen.beginBlock();
        gen.out("//ObjectArgument ", that.getIdentifier().getText());
        gen.location(that);
        gen.endLine();
        gen.out(GenerateJsVisitor.function, gen.getNames().name(c), "()");
        gen.beginBlock();
        gen.instantiateSelf(c);
        gen.referenceOuter(c);
        Tree.ExtendedType xt = that.getExtendedType();
        final Tree.ClassBody body = that.getClassBody();
        final Tree.SatisfiedTypes sts = that.getSatisfiedTypes();
        
        final List<Declaration> superDecs = new ArrayList<Declaration>(3);
        if (!gen.opts.isOptimize()) {
            new GenerateJsVisitor.SuperVisitor(superDecs).visit(that.getClassBody());
        }
        TypeGenerator.callSupertypes(sts == null ? null : TypeUtils.getTypes(sts.getTypes()),
                xt == null? null : xt.getType(), c,
                that, superDecs, xt == null ? null : xt.getInvocationExpression(),
                xt == null ? null : c.getParameterList(), gen);
        
        body.visit(gen);
        gen.out("return ", gen.getNames().self(c), ";");
        gen.endBlock(false, true);
        //Add reference to metamodel
        gen.out(gen.getNames().name(c), ".$crtmm$=");
        TypeUtils.encodeForRuntime(c, null, gen);
        gen.endLine(true);

        TypeGenerator.typeInitialization(xt, sts, c, new GenerateJsVisitor.PrototypeInitCallback() {
            @Override
            public void addToPrototypeCallback() {
                gen.addToPrototype(that, c, body.getStatements());
            }
        }, gen, null, null);
        gen.out("return ", gen.getNames().name(c), "(new ", gen.getNames().name(c), ".$$);");
        gen.endBlock();
        gen.out("())");
    }

    static void methodArgument(final Tree.MethodArgument that, final GenerateJsVisitor gen) {
        gen.out("(");
        if (that.getBlock() == null) {
            singleExprFunction(that.getParameterLists(), that.getSpecifierExpression().getExpression(),
                    that.getScope(), false, true, gen);
        } else {
            multiStmtFunction(that.getParameterLists(), that.getBlock(), that.getScope(), false, gen);
        }
        gen.out(")");
    }

    static void functionArgument(Tree.FunctionArgument that, final GenerateJsVisitor gen) {
        gen.out("(");
        if (that.getBlock() == null) {
            singleExprFunction(that.getParameterLists(), that.getExpression(), that.getScope(), false, true, gen);
        } else {
            multiStmtFunction(that.getParameterLists(), that.getBlock(), that.getScope(), false, gen);
        }
        gen.out(")");
    }

    static void methodDeclaration(TypeDeclaration outer, Tree.MethodDeclaration that, GenerateJsVisitor gen, boolean verboseStitcher) {
        final Function m = that.getDeclarationModel();
        if (that.getSpecifierExpression() != null) {
            // method(params) => expr
            if (outer == null) {
                // Not in a prototype definition. Null to do here if it's a
                // member in prototype style.
                if (gen.opts.isOptimize() && m.isMember()) { return; }
                gen.comment(that);
                gen.initDefaultedParameters(that.getParameterLists().get(0), m);
                if (!(gen.opts.isOptimize() && m.isClassOrInterfaceMember()) && TypeUtils.isNativeExternal(m)) {
                    if (gen.stitchNative(m, that)) {
                        if (verboseStitcher) {
                            gen.spitOut("Stitching in native method " + m.getQualifiedNameString() + ", ignoring Ceylon declaration");
                        }
                        if (m.isShared()) {
                            gen.share(m);
                        }
                        return;
                    }
                }
                gen.out(m.isToplevel() ? GenerateJsVisitor.function : "var ");
            }
            else {
                // prototype definition
                gen.comment(that);
                gen.initDefaultedParameters(that.getParameterLists().get(0), m);
                gen.out(gen.getNames().self(outer), ".");
            }
            gen.out(gen.getNames().name(m));
            if (!m.isToplevel())gen.out("=");
            singleExprFunction(that.getParameterLists(),
                    that.getSpecifierExpression().getExpression(), m, true, !m.isToplevel(), gen);
            gen.endLine(true);
            if (outer != null) {
                gen.out(gen.getNames().self(outer), ".");
            }
            gen.out(gen.getNames().name(m), ".$crtmm$=");
            TypeUtils.encodeMethodForRuntime(that, gen);
            gen.endLine(true);
            gen.share(m);
        }
        else if (outer == null // don't do the following in a prototype definition
                && m == that.getScope()) { //Check for refinement of simple param declaration
            
            if (m.getContainer() instanceof Class && m.isClassOrInterfaceMember()) {
                //Declare the method just by pointing to the param function
                final String name = gen.getNames().name(((Class)m.getContainer()).getParameter(m.getName()));
                if (name != null) {
                    gen.out(gen.getNames().self((Class)m.getContainer()), ".", gen.getNames().name(m), "=", name);
                    gen.endLine(true);
                }
            } else if (m.getContainer() instanceof Function) {
                //Declare the function just by forcing the name we used in the param list
                final String name = gen.getNames().name(((Function)m.getContainer()).getParameter(m.getName()));
                gen.getNames().forceName(m, name);
            }
            //Only the first paramlist can have defaults
            gen.initDefaultedParameters(that.getParameterLists().get(0), m);
            if (!(gen.opts.isOptimize() && m.isClassOrInterfaceMember()) && TypeUtils.isNativeExternal(m)) {
                if (gen.stitchNative(m, that)) {
                    if (verboseStitcher) {
                        gen.spitOut("Stitching in native method " + m.getQualifiedNameString()
                                + ", ignoring Ceylon declaration");
                    }
                    if (m.isShared()) {
                        gen.share(m);
                    }
                }
            }
        } else if (m == that.getScope() && m.getContainer() instanceof TypeDeclaration && m.isMember()
                && (m.isFormal() || TypeUtils.isNativeExternal(m))) {
            gen.out(gen.getNames().self((TypeDeclaration)m.getContainer()), ".",
                    gen.getNames().name(m), "=");
            if (m.isFormal()) {
                gen.out("{$fml:1,$crtmm$:");
                TypeUtils.encodeForRuntime(that, m, gen);
                gen.out("};");
            } else if (TypeUtils.isNativeExternal(m)) {
                if (gen.stitchNative(m, that)) {
                    if (verboseStitcher) {
                        gen.spitOut("Stitching in native method " + m.getQualifiedNameString()
                                + ", ignoring Ceylon declaration");
                    }
                    if (m.isShared()) {
                        gen.share(m);
                    }
                }
            }
        }
    }

    static void methodDefinition(final Tree.MethodDefinition that, final GenerateJsVisitor gen, final boolean needsName, final boolean verboseStitcher) {
        final Function d = that.getDeclarationModel();
        if (TypeUtils.isNativeExternal(d)) {
            if (gen.stitchNative(d, that)) {
                if (verboseStitcher) {
                    gen.spitOut("Stitching in native method " + d.getQualifiedNameString() + ", ignoring Ceylon definition");
                }
                if (d.isShared()) {
                    gen.share(d);
                }
                return;
            }
        }
        if (that.getParameterLists().size() == 1) {
            if (needsName) {
                gen.out(GenerateJsVisitor.function, gen.getNames().name(d));
            } else {
                gen.out("function");
            }
            Tree.ParameterList paramList = that.getParameterLists().get(0);
            paramList.visit(gen);
            gen.beginBlock();
            if (d.getContainer() instanceof TypeDeclaration) {
                gen.initSelf(that);
            }
            addParentMethodTypeParameters(d, gen);
            gen.initParameters(paramList, null, d);
            gen.visitStatements(that.getBlock().getStatements());
            gen.endBlock();
        } else {
            List<MplData> metas = new ArrayList<>(that.getParameterLists().size());
            for (Tree.ParameterList paramList : that.getParameterLists()) {
                final MplData mpl = new MplData();
                mpl.n=that;
                metas.add(mpl);
                if (metas.size()==1) {
                    if (needsName) {
                        gen.out(GenerateJsVisitor.function, gen.getNames().name(d));
                    } else {
                        gen.out("function");
                    }
                } else {
                    mpl.name=gen.getNames().createTempVariable();
                    mpl.params=paramList;
                    gen.out("var ", mpl.name, "=function");
                }
                paramList.visit(gen);
                gen.beginBlock();
                if (metas.size()==1 && d.getContainer() instanceof TypeDeclaration) {
                    gen.initSelf(that);
                }
                gen.initParameters(paramList, null, d);
            }
            gen.visitStatements(that.getBlock().getStatements());
            closeMPL(metas, d.getType(), gen);
            gen.endBlock();
        }

        if (!gen.share(d)) { gen.out(";"); }
    }

    static void generateLet(final Tree.LetExpression that, final Set<Declaration> directs, final GenerateJsVisitor gen) {
        gen.out("function(){var ");
        boolean first=true;
        HashSet<Declaration> decs2 = new HashSet<>();
        for (Tree.Statement st : that.getLetClause().getVariables()) {
            if (!first)gen.out(",");
            if (st instanceof Tree.Variable) {
                final Tree.Variable var = (Tree.Variable)st;
                gen.out(gen.getNames().name(var.getDeclarationModel()), "=");
                var.getSpecifierExpression().getExpression().visit(gen);
                directs.add(var.getDeclarationModel());
                decs2.add(var.getDeclarationModel());
            } else if (st instanceof Tree.Destructure) {
                final String expvar = gen.getNames().createTempVariable();
                gen.out(expvar, "=");
                ((Tree.Destructure)st).getSpecifierExpression().visit(gen);
                decs2.addAll(new Destructurer(((Tree.Destructure)st).getPattern(),
                        gen, directs, expvar, false).getDeclarations());
            }
            first=false;
        }
        gen.out(";return ");
        that.getLetClause().getExpression().visit(gen);
        gen.out(";}()");
        directs.removeAll(decs2);
    }

    private static void closeMPL(List<MplData> mpl, Type rt, GenerateJsVisitor gen) {
        for (int i=mpl.size()-1; i>0; i--) {
            final MplData pl = mpl.get(i);
            gen.endBlock(true,true);
            pl.outputMetamodelAndReturn(gen, rt);
            if (i>1) {
                rt = ModelUtil.appliedType(pl.n.getUnit().getCallableDeclaration(), rt, pl.tupleFromParameterList());
            }
        }
    }

    private static class MplData {
        String name;
        Node n;
        Tree.ParameterList params;
        void outputMetamodelAndReturn(GenerateJsVisitor gen, Type t) {
            gen.out(name,  ".$crtmm$=function(){return{", MetamodelGenerator.KEY_PARAMS,":");
            TypeUtils.encodeParameterListForRuntime(true, n, params.getModel(), gen);
            if (t != null) {
                //Add the type to the innermost method
                gen.out(",", MetamodelGenerator.KEY_TYPE, ":");
                TypeUtils.typeNameOrList(n, t, gen, false);
            }
            gen.out("};};return ", gen.getClAlias(), "JsCallable(0,", name, ");");
        }
        Type tupleFromParameterList() {
            if (params.getParameters().isEmpty()) {
                return n.getUnit().getEmptyType();
            }
            List<Type> types = new ArrayList<>(params.getParameters().size());
            int firstDefaulted=-1;
            int count = 0;
            for (Tree.Parameter p : params.getParameters()) {
                types.add(p.getParameterModel().getType());
                if (p.getParameterModel().isDefaulted())firstDefaulted=count;
                count++;
            }
            return n.getUnit().getTupleType(types,
                    params.getParameters().get(params.getParameters().size()-1).getParameterModel().isSequenced(),
                    params.getParameters().get(params.getParameters().size()-1).getParameterModel().isAtLeastOne(),
                    firstDefaulted);
        }
    }

    private static boolean copyMissingTypeParameters(final Function child, final Function parent,
            final int idx, final boolean firstOne, final GenerateJsVisitor gen) {
        //We already know the kid has type parameters
        List<TypeParameter> ctp = child.getTypeParameters();
        List<TypeParameter> ptp = parent.getTypeParameters();
        String tpaname = gen.getNames().typeArgsParamName(child) + ".";
        TypeParameter dad = ptp.get(idx);
        TypeParameter kid = ctp.get(idx);
        if (!dad.getName().equals(kid.getName())) {
            String pname = tpaname + dad.getName() + "$" + child.getName();
            if (firstOne) {
                String cname = tpaname + kid.getName() + "$" + child.getName();
                gen.out("if(", cname, "===undefined)", cname, "=", pname);
            } else {
                gen.out("||", pname);
            }
            return true;
        }
        return false;
    }

    /** See #547 - a generic actual method that has different names in its type parameters as the one
     * it's refining, can receive the type arguments of the parent instead. */
    static void addParentMethodTypeParameters(final Function m, final GenerateJsVisitor gen) {
        List<TypeParameter> tps = m.getTypeParameters();
        if (m.isActual() && tps != null && !tps.isEmpty()) {
            //This gives us the root declaration
            Function sm = (Function)m.getRefinedDeclaration();
            for (int i = 0; i < tps.size(); i++) {
                boolean end = false;
                end |= copyMissingTypeParameters(m, sm, i, true, gen);
                //We still need to find intermediate declarations
                if (m.isClassOrInterfaceMember()) {
                    final Set<Declaration> decs = new HashSet<Declaration>();
                    decs.add(sm);
                    decs.add(m);
                    //This gives us the containing type
                    TypeDeclaration cont = ModelUtil.getContainingClassOrInterface(m);
                    for (TypeDeclaration sup : cont.getSupertypeDeclarations()) {
                        Declaration d = sup.getDirectMember(m.getName(), null, false);
                        if (d instanceof Function && !decs.contains(d)) {
                            decs.add(d);
                            end |= copyMissingTypeParameters(m, (Function)d, i, false, gen);
                        }
                    }
                    for (Type sup : cont.getSatisfiedTypes()) {
                        Declaration d = sup.getDeclaration().getDirectMember(m.getName(), null, false);
                        if (d instanceof Function && !decs.contains(d)) {
                            decs.add(d);
                            end |= copyMissingTypeParameters(m, (Function)d, i, false, gen);
                        }
                    }
                }
                if (end) {
                    gen.endLine(true);
                }
            }
        }
    }

    static void generateCallable(final Tree.QualifiedMemberOrTypeExpression that, String name,
            final GenerateJsVisitor gen) {
        final Declaration d = that.getDeclaration();
        if (that.getPrimary() instanceof Tree.BaseTypeExpression) {
            //it's a static method ref
            if (name == null) {
                name = gen.memberAccess(that, "");
            }
            if (TypeUtils.isConstructor(d)) {
                Constructor cd = TypeUtils.getConstructor(d);
                final boolean hasTargs = BmeGenerator.hasTypeParameters(
                        (Tree.BaseTypeExpression)that.getPrimary());
                if (hasTargs) {
                    if (that.getDirectlyInvoked()) {
                        gen.out(gen.qualifiedPath(that, cd), "_", gen.getNames().name(cd));
                    } else {
                        BmeGenerator.printGenericMethodReference(gen,
                                (Tree.BaseTypeExpression)that.getPrimary(), "0",
                                gen.qualifiedPath(that, cd) + "_" + gen.getNames().name(cd));
                    }
                } else {
                    gen.qualify(that, cd);
                    gen.out(gen.getNames().name(cd));
                }
            } else {
                gen.out("function(x){return ");
                if (BmeGenerator.hasTypeParameters(that)) {
                    BmeGenerator.printGenericMethodReference(gen, that, "x", "x."+name);
                } else {
                    gen.out(gen.getClAlias(), "JsCallable(x,x.", name, ")");
                }
                gen.out(";}");
            }
            return;
        }
        if (d.isToplevel() && d instanceof Function) {
            //Just output the name
            gen.out(gen.getNames().name(d));
            return;
        }
        String primaryVar = gen.createRetainedTempVar();
        gen.out("(", primaryVar, "=");
        that.getPrimary().visit(gen);
        gen.out(",");
        final String member = (name == null) ? gen.memberAccess(that, primaryVar) : (primaryVar+"."+name);
        if (that.getDeclaration() instanceof Function
                && !((Function)that.getDeclaration()).getTypeParameters().isEmpty()) {
            //Function ref with type parameters
            BmeGenerator.printGenericMethodReference(gen, that, primaryVar, member);
        } else {
            if (that.getUnit().isOptionalType(that.getPrimary().getTypeModel())) {
                gen.out(gen.getClAlias(), "JsCallable(", primaryVar, ",", gen.getClAlias(),
                        "nn$(", primaryVar, ")?", member, ":null)");
            } else {
                gen.out(gen.getClAlias(), "JsCallable(", primaryVar, ",", member, ")");
            }
        }
        gen.out(")");
    }
    
}
