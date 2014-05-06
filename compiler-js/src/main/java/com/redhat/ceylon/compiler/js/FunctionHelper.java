package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

public class FunctionHelper {

    static interface ParameterListCallback {
        void completeFunction();
    }

    static void multiStmtFunction(final List<Tree.ParameterList> paramLists,
            final Tree.Block block, final Scope scope, final GenerateJsVisitor gen) {
        generateParameterLists(block, paramLists, scope, new ParameterListCallback() {
            @Override
            public void completeFunction() {
                if (paramLists.size() == 1 && scope!=null) { gen.initSelf(block); }
                gen.initParameters(paramLists.get(paramLists.size()-1),
                        scope instanceof TypeDeclaration ? (TypeDeclaration)scope : null, null);
                gen.visitStatements(block.getStatements());
            }
        }, gen);
    }
    static void singleExprFunction(final List<Tree.ParameterList> paramLists,
                                    final Tree.Expression expr, final Scope scope, final GenerateJsVisitor gen) {
        generateParameterLists(expr, paramLists, scope, new ParameterListCallback() {
            @Override
            public void completeFunction() {
                if (paramLists.size() == 1 && scope != null) { gen.initSelf(expr); }
                gen.initParameters(paramLists.get(paramLists.size()-1),
                        null, scope instanceof Method ? (Method)scope : null);
                gen.out("return ");
                expr.visit(gen);
                gen.out(";");
            }
        }, gen);
    }

    /** Generates the code for single or multiple parameter lists, with a callback function to generate the function blocks. */
    static void generateParameterLists(final Node context, final List<Tree.ParameterList> plist, final Scope scope,
                final ParameterListCallback callback, final GenerateJsVisitor gen) {
        if (plist.size() == 1) {
            gen.out(GenerateJsVisitor.function);
            Tree.ParameterList paramList = plist.get(0);
            paramList.visit(gen);
            gen.beginBlock();
            callback.completeFunction();
            gen.endBlock();
        } else {
            int count=0;
            for (Tree.ParameterList paramList : plist) {
                if (count==0) {
                    gen.out(GenerateJsVisitor.function);
                } else {
                    //TODO add metamodel
                    gen.out("return function");
                }
                paramList.visit(gen);
                if (count == 0) {
                    gen.beginBlock();
                    gen.initSelf(context);
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
        
        Tree.Block block = that.getBlock();
        Tree.SpecifierExpression specExpr = that.getSpecifierExpression();
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
        Tree.ExtendedType xt = that.getExtendedType();
        final Tree.ClassBody body = that.getClassBody();
        Tree.SatisfiedTypes sts = that.getSatisfiedTypes();
        
        final List<Declaration> superDecs = new ArrayList<Declaration>(3);
        if (!gen.opts.isOptimize()) {
            new GenerateJsVisitor.SuperVisitor(superDecs).visit(that.getClassBody());
        }
        gen.callSuperclass(xt, c, that, superDecs);
        gen.callInterfaces(sts, c, that, superDecs);
        
        body.visit(gen);
        gen.out("return ", gen.getNames().self(c), ";");
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
        generateParameterLists(that, that.getParameterLists(), that.getScope(),
                new ParameterListCallback() {
            @Override
            public void completeFunction() {
                Tree.Block block = that.getBlock();
                Tree.SpecifierExpression specExpr = that.getSpecifierExpression();
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
            singleExprFunction(that.getParameterLists(), that.getExpression(), null, gen);
        } else {
            multiStmtFunction(that.getParameterLists(), that.getBlock(), null, gen);
        }
        gen.out(")");
    }

    static void methodDeclaration(TypeDeclaration outer, Tree.MethodDeclaration that, GenerateJsVisitor gen) {
        final Method m = that.getDeclarationModel();
        if (that.getSpecifierExpression() != null) {
            // method(params) => expr
            if (outer == null) {
                // Not in a prototype definition. Null to do here if it's a
                // member in prototype style.
                if (gen.opts.isOptimize() && m.isMember()) { return; }
                gen.comment(that);
                gen.initDefaultedParameters(that.getParameterLists().get(0), m);
                gen.out("var ");
            }
            else {
                // prototype definition
                gen.comment(that);
                gen.initDefaultedParameters(that.getParameterLists().get(0), m);
                gen.out(gen.getNames().self(outer), ".");
            }
            gen.out(gen.getNames().name(m), "=");            
            singleExprFunction(that.getParameterLists(),
                    that.getSpecifierExpression().getExpression(), that.getDeclarationModel(), gen);
            gen.endLine(true);
            if (outer != null) {
                gen.out(gen.getNames().self(outer), ".");
            }
            gen.out(gen.getNames().name(m), ".$crtmm$=");
            TypeUtils.encodeForRuntime(m, that.getAnnotationList(), gen);
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
            } else if (m.getContainer() instanceof Method) {
                //Declare the function just by forcing the name we used in the param list
                final String name = gen.getNames().name(((Method)m.getContainer()).getParameter(m.getName()));
                gen.getNames().forceName(m, name);
            }
            //Only the first paramlist can have defaults
            gen.initDefaultedParameters(that.getParameterLists().get(0), m);
        } else if (m.isFormal() && m.isMember() && m == that.getScope()) {
            if (m.getContainer() instanceof TypeDeclaration) {
                gen.out(gen.getNames().self((TypeDeclaration)m.getContainer()), ".",
                        gen.getNames().name(m), "={$fml:1,$crtmm$:");
                TypeUtils.encodeForRuntime(that, m, gen);
                gen.out("};");
            }
        }
    }

    static void methodDefinition(Tree.MethodDefinition that, GenerateJsVisitor gen) {
        final Method d = that.getDeclarationModel();
        if (that.getParameterLists().size() == 1) {
            gen.out(GenerateJsVisitor.function, gen.getNames().name(d));
            Tree.ParameterList paramList = that.getParameterLists().get(0);
            paramList.visit(gen);
            gen.beginBlock();
            gen.initSelf(that);
            gen.initParameters(paramList, null, d);
            gen.visitStatements(that.getBlock().getStatements());
            gen.endBlock();
        } else {
            int count=0;
            for (Tree.ParameterList paramList : that.getParameterLists()) {
                if (count==0) {
                    gen.out(GenerateJsVisitor.function, gen.getNames().name(d));
                } else {
                    gen.out("return function");
                }
                paramList.visit(gen);
                gen.beginBlock();
                if (count==0) {
                    gen.initSelf(that);
                }
                gen.initParameters(paramList, null, d);
                count++;
            }
            gen.visitStatements(that.getBlock().getStatements());
            for (int i=0; i < count; i++) {
                gen.endBlock();
            }
        }

        if (!gen.share(d)) { gen.out(";"); }
    }

}
