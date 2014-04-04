package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Block;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassBody;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExtendedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ParameterList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SatisfiedTypes;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;

public class FunctionHelper {

    static interface ParameterListCallback {
        void completeFunction();
    }

    static void multiStmtFunction(final List<ParameterList> paramLists,
            final Block block, final Scope scope, final GenerateJsVisitor gen) {
        generateParameterLists(paramLists, scope, new ParameterListCallback() {
            @Override
            public void completeFunction() {
                gen.beginBlock();
                if (paramLists.size() == 1) { gen.initSelf(scope, false); }
                gen.initParameters(paramLists.get(paramLists.size()-1),
                        scope instanceof TypeDeclaration ? (TypeDeclaration)scope : null, null);
                gen.visitStatements(block.getStatements());
                gen.endBlock();
            }
        }, gen);
    }
    static void singleExprFunction(final List<ParameterList> paramLists,
                                    final Expression expr, final Scope scope, final GenerateJsVisitor gen) {
        generateParameterLists(paramLists, scope, new ParameterListCallback() {
            @Override
            public void completeFunction() {
                gen.beginBlock();
                if (paramLists.size() == 1) { gen.initSelf(scope, false); }
                gen.initParameters(paramLists.get(paramLists.size()-1),
                        null, scope instanceof Method ? (Method)scope : null);
                gen.out("return ");
                expr.visit(gen);
                gen.out(";");
                gen.endBlock();
            }
        }, gen);
    }

    /** Generates the code for single or multiple parameter lists, with a callback function to generate the function blocks. */
    static void generateParameterLists(final List<ParameterList> plist, final Scope scope,
                final ParameterListCallback callback, final GenerateJsVisitor gen) {
        if (plist.size() == 1) {
            gen.out(GenerateJsVisitor.function);
            ParameterList paramList = plist.get(0);
            paramList.visit(gen);
            callback.completeFunction();
        } else {
            int count=0;
            for (ParameterList paramList : plist) {
                if (count==0) {
                    gen.out(GenerateJsVisitor.function);
                } else {
                    //TODO add metamodel
                    gen.out("return function");
                }
                paramList.visit(gen);
                if (count == 0) {
                    gen.beginBlock();
                    gen.initSelf(scope, false);
                    Scope parent = scope == null ? null : scope.getContainer();
                    gen.initParameters(paramList, parent instanceof TypeDeclaration ? (TypeDeclaration)parent : null,
                            scope instanceof Method ? (Method)scope:null);
                }
                else {
                    gen.out("{");
                }
                count++;
            }
            callback.completeFunction();
            for (int i=0; i < count; i++) {
                gen.endBlock(false, i==count-1);
            }
        }
    }

    static void attributeArgument(final Tree.AttributeArgument that, final GenerateJsVisitor gen) {
        gen.out("(function()");
        gen.beginBlock();
        gen.out("//AttributeArgument ", that.getParameter().getName());
        gen.location(that);
        gen.endLine();
        
        Block block = that.getBlock();
        SpecifierExpression specExpr = that.getSpecifierExpression();
        if (specExpr != null) {
            gen.out("return ");
            specExpr.getExpression().visit(gen);
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
        ExtendedType xt = that.getExtendedType();
        final ClassBody body = that.getClassBody();
        SatisfiedTypes sts = that.getSatisfiedTypes();
        
        final List<Declaration> superDecs = new ArrayList<Declaration>(3);
        if (!gen.opts.isOptimize()) {
            new GenerateJsVisitor.SuperVisitor(superDecs).visit(that.getClassBody());
        }
        gen.callSuperclass(xt, c, that, superDecs);
        gen.callInterfaces(sts, c, that, superDecs);
        
        body.visit(gen);
        gen.returnSelf(c);
        gen.endBlock(false, true);
        //Add reference to metamodel
        gen.out(gen.getNames().name(c), ".$crtmm$=");
        TypeUtils.encodeForRuntime(c, null, gen);
        gen.endLine(true);

        gen.typeInitialization(xt, sts, c, new GenerateJsVisitor.PrototypeInitCallback() {
            @Override
            public void addToPrototypeCallback() {
                gen.addToPrototype(that, c, body.getStatements());
            }
        });
        gen.out("return ", gen.getNames().name(c), "(new ", gen.getNames().name(c), ".$$);");
        gen.endBlock();
        gen.out("())");
    }

    static void methodArgument(final Tree.MethodArgument that, final GenerateJsVisitor gen) {
        generateParameterLists(that.getParameterLists(), that.getScope(),
                new ParameterListCallback() {
            @Override
            public void completeFunction() {
                Block block = that.getBlock();
                SpecifierExpression specExpr = that.getSpecifierExpression();
                if (specExpr != null) {
                    gen.out("{return ");
                    specExpr.getExpression().visit(gen);
                    gen.out(";}");
                }
                else if (block != null) {
                    block.visit(gen);
                }
            }
        }, gen);
    }

    static void functionArgument(Tree.FunctionArgument that, final GenerateJsVisitor gen) {
        gen.out("(");
        if (that.getBlock() == null) {
            singleExprFunction(that.getParameterLists(), that.getExpression(), that.getScope(), gen);
        } else {
            multiStmtFunction(that.getParameterLists(), that.getBlock(), that.getScope(), gen);
        }
        gen.out(")");
    }

}
