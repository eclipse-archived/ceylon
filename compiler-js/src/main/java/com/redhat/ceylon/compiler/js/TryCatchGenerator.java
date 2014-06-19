package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

public class TryCatchGenerator {

    private final GenerateJsVisitor gen;
    private final Set<Declaration> directAccess;

    TryCatchGenerator(GenerateJsVisitor generator, Set<Declaration> da) {
        gen = generator;
        directAccess = da;
    }

    void generate(Tree.TryCatchStatement that) {
        List<Tree.Resource> resources = that.getTryClause().getResourceList() == null ? null :
            that.getTryClause().getResourceList().getResources();
        if (resources != null && resources.isEmpty()) {
            resources = null;
        }
        List<Res> resourceVars = null;
        String tvar = null;
        if (resources != null && !resources.isEmpty()) {
            //Declare the resource variables
            resourceVars = new ArrayList<>(resources.size());
            gen.out("var ");
            for (Tree.Resource res : resources) {
                if (!resourceVars.isEmpty()) {
                    gen.out(",");
                }
                final Res r = new Res(res);
                gen.out(r.var, "=null");
                resourceVars.add(r);
            }
            tvar = gen.getNames().createTempVariable();
            gen.out(",", tvar, "=null");
            if (tvar != null) {
            }
            gen.endLine(true);
        }
        gen.out("try");
        if (resources != null) {
            gen.out("{");
            //Initialize the resources
            for (Res resourceVar : resourceVars) {
                if (resourceVar.destroy) {
                    gen.out(resourceVar.var, "=");
                    resourceVar.r.visit(gen);
                } else {
                    gen.out(tvar, "=");
                    resourceVar.r.visit(gen);
                    gen.out(";", tvar, ".obtain();", resourceVar.var, "=", tvar);
                }
                gen.endLine(true);
            }
        }
        gen.encloseBlockInFunction(that.getTryClause().getBlock());
        if (resources != null) {
            //Destroy/release resources
            Collections.reverse(resourceVars);
            for (Res r : resourceVars) {
                gen.out(tvar,"=",r.var,";", r.var, "=null;", tvar);
                if (r.destroy) {
                    gen.out(".destroy(null);");
                } else {
                    gen.out(".release(null);");
                }
                gen.endLine();
            }
            gen.out("}");
        }

        if (!that.getCatchClauses().isEmpty() || resources != null) {
            String catchVarName = gen.getNames().createTempVariable();
            gen.out("catch(", catchVarName, ")");
            gen.beginBlock();
            //Check if it's native and if so, wrap it
            gen.out("if(", catchVarName, ".getT$name===undefined)", catchVarName, "=",
                    GenerateJsVisitor.getClAlias(), "NativeException(", catchVarName, ")");
            gen.endLine(true);
            if (resources != null) {
                for (Res r : resourceVars) {
                    gen.out("try{if(", r.var, "!==null)", r.var,
                            r.destroy?".destroy(":".release(",
                            catchVarName, ");}catch(", r.var,"e$){",
                            catchVarName, ".addSuppressed(", r.var, "e$);}");
                    gen.endLine();
                }
            }
            boolean firstCatch = true;
            for (Tree.CatchClause catchClause : that.getCatchClauses()) {
                Tree.Variable variable = catchClause.getCatchVariable().getVariable();
                if (!firstCatch) {
                    gen.out("else ");
                }
                firstCatch = false;
                gen.out("if(");
                gen.generateIsOfType(variable, catchVarName, variable.getType(), null, false);
                gen.out(")");

                if (catchClause.getBlock().getStatements().isEmpty()) {
                    gen.out("{}");
                } else {
                    gen.beginBlock();
                    directAccess.add(variable.getDeclarationModel());
                    gen.getNames().forceName(variable.getDeclarationModel(), catchVarName);

                    gen.visitStatements(catchClause.getBlock().getStatements());
                    gen.endBlock();
                }
            }
            if (!that.getCatchClauses().isEmpty()) {
                gen.out("else{throw ", catchVarName, "}");
            }
            gen.endBlockNewLine();
        }

        if (that.getFinallyClause() != null) {
            gen.out("finally");
            gen.encloseBlockInFunction(that.getFinallyClause().getBlock());
        }
    }

    private class Res {
        final boolean destroy;
        final String var;
        final Tree.Resource r;
        Res(Tree.Resource r) {
            this.r = r;
            if (r.getVariable() != null) {
                destroy = r.getVariable().getType().getTypeModel().getDeclaration().inherits(
                        r.getUnit().getDestroyableDeclaration());
                var = gen.getNames().name(r.getVariable().getDeclarationModel());
            } else {
                destroy = r.getExpression().getTypeModel().getDeclaration().inherits(
                        r.getUnit().getDestroyableDeclaration());
                var = gen.getNames().createTempVariable();
            }
        }
    }
}
