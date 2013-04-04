package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Comprehension;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExpressionComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ForComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ForIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IfComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.KeyValueIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ValueIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;

/** This component is used by the main JS visitor to generate code for comprehensions.
 * 
 * @author Enrique Zamudio
 * @author Ivo Kasiuk
 */
class ComprehensionGenerator {

    private final GenerateJsVisitor gen;
    private final JsIdentifierNames names;
    private final RetainedVars retainedVars = new RetainedVars();
    private final String finished;
    private final Set<Declaration> directAccess;

    ComprehensionGenerator(GenerateJsVisitor gen, JsIdentifierNames names, Set<Declaration> directDeclarations) {
        this.gen = gen;
        finished = String.format("%sgetFinished()", GenerateJsVisitor.getClAlias());
        this.names = names;
        directAccess = directDeclarations;
    }

    void generateComprehension(Comprehension that) {
        gen.out(GenerateJsVisitor.getClAlias(), "Comprehension(function()");
        gen.beginBlock();
        if (gen.opts.isComment()) {
            gen.out("//Comprehension"); gen.location(that); gen.endLine();
        }

        // gather information about all loops and conditions in the comprehension
        List<ComprehensionLoopInfo> loops = new ArrayList<ComprehensionLoopInfo>();
        Expression expression = null;
        ForComprehensionClause forClause = that.getForComprehensionClause();
        while (forClause != null) {
            ComprehensionLoopInfo loop = new ComprehensionLoopInfo(that, forClause.getForIterator());
            ComprehensionClause clause = forClause.getComprehensionClause();
            while ((clause != null) && !(clause instanceof ForComprehensionClause)) {
                if (clause instanceof IfComprehensionClause) {
                    IfComprehensionClause ifClause = ((IfComprehensionClause) clause);
                    loop.conditions.add(ifClause.getConditionList());
                    loop.conditionVars.add(gen.conds.gatherVariables(ifClause.getConditionList()));
                    clause = ifClause.getComprehensionClause();

                } else if (clause instanceof ExpressionComprehensionClause) {
                    expression = ((ExpressionComprehensionClause) clause).getExpression();
                    clause = null;
                } else {
                    that.addError("No support for comprehension clause of type "
                                  + clause.getClass().getName());
                    return;
                }
            }
            loops.add(loop);
            forClause = (ForComprehensionClause) clause;
        }

        // generate variables and "next" function for each for loop
        for (int loopIndex=0; loopIndex<loops.size(); loopIndex++) {
            ComprehensionLoopInfo loop = loops.get(loopIndex);

            // iterator variable
            gen.out("var ", loop.itVarName);
            if (loopIndex == 0) {
                gen.out("=");
                loop.forIterator.getSpecifierExpression().visit(gen);
                gen.out(".iterator()");
            }
            gen.out(";"); gen.endLine();

            // value or key/value variables
            if (loop.keyVarName == null) {
                gen.out("var ", loop.valueVarName, "=", finished, ";");
                gen.endLine();
            } else {
                gen.out("var ", loop.keyVarName, ",", loop.valueVarName, ";");
                gen.endLine();
            }

            // variables for is/exists/nonempty conditions
            for (List<ConditionGenerator.VarHolder> condVarList : loop.conditionVars) {
                for (ConditionGenerator.VarHolder condVar : condVarList) {
                    gen.out("var ", condVar.name, ";"); gen.endLine();
                    directAccess.add(condVar.var.getDeclarationModel());
                }
            }

            // generate the "next" function for this loop
            boolean isLastLoop = (loopIndex == (loops.size()-1));
            if (isLastLoop && loop.conditions.isEmpty() && (loop.keyVarName == null)) {
                // simple case: innermost loop without conditions, no key/value iterator
                gen.out("var next$", loop.valueVarName, "=function(){return ",
                        loop.valueVarName, "=", loop.itVarName, ".next();}");
                gen.endLine();
            }
            else {
                gen.out("var next$", loop.valueVarName, "=function()");
                gen.beginBlock();

                // extra entry variable for key/value iterators
                String elemVarName = loop.valueVarName;
                if (loop.keyVarName != null) {
                    elemVarName = names.createTempVariable("entry");
                    gen.out("var ", elemVarName, ";"); gen.endLine();
                }

                // if/while ((elemVar=it.next()!==$finished)
                gen.out(loop.conditions.isEmpty()?"if":"while", "((", elemVarName, "=",
                        loop.itVarName, ".next())!==", finished, ")");
                gen.beginBlock();

                // get key/value if necessary
                if (loop.keyVarName != null) {
                    gen.out(loop.keyVarName, "=", elemVarName, ".key;"); gen.endLine();
                    gen.out(loop.valueVarName, "=", elemVarName, ".item;"); gen.endLine();
                }

                // generate conditions as nested ifs
                for (int i=0; i<loop.conditions.size(); i++) {
                    gen.conds.specialConditions(loop.conditionVars.get(i), loop.conditions.get(i), "if");
                    gen.beginBlock();
                }

                // initialize iterator of next loop and get its first element
                if (!isLastLoop) {
                    ComprehensionLoopInfo nextLoop = loops.get(loopIndex+1);
                    gen.out(nextLoop.itVarName, "=");
                    nextLoop.forIterator.getSpecifierExpression().visit(gen);
                    gen.out(".iterator();"); gen.endLine();
                    gen.out("next$", nextLoop.valueVarName, "();"); gen.endLine();
                }

                gen.out("return ", elemVarName, ";"); gen.endLine();
                for (int i=0; i<=loop.conditions.size(); i++) { gen.endBlockNewLine(); }
                retainedVars.emitRetainedVars(gen);

                // for key/value iterators, value==undefined indicates that the iterator is finished
                if (loop.keyVarName != null) {
                    gen.out(loop.valueVarName, "=undefined;"); gen.endLine();
                }

                gen.out("return ", finished, ";");
                gen.endBlockNewLine();
            }
        }

        // get the first element
        gen.out("next$", loops.get(0).valueVarName, "();"); gen.endLine();

        // generate the "next" function for the comprehension
        gen.out("return function()");
        gen.beginBlock();

        // start a do-while block for all except the innermost loop
        for (int i=1; i<loops.size(); i++) {
            gen.out("do"); gen.beginBlock();
        }

        // Check if another element is available on the innermost loop.
        // If yes, evaluate the expression, advance the iterator and return the result.
        ComprehensionLoopInfo lastLoop = loops.get(loops.size()-1);
        gen.out("if(", lastLoop.valueVarName, "!==", (lastLoop.keyVarName==null)
                ? finished : "undefined", ")");
        gen.beginBlock();
        declareExternalLoopVars(lastLoop);
        String tempVarName = names.createTempVariable();
        gen.out("var ", tempVarName, "=");
        expression.visit(gen);
        gen.endLine(true);
        retainedVars.emitRetainedVars(gen);
        gen.out("next$", lastLoop.valueVarName, "();"); gen.endLine();
        gen.out("return ", tempVarName, ";");
        gen.endBlockNewLine();

        // "while" part of the do-while loops
        for (int i=loops.size()-2; i>=0; i--) {
            gen.endBlock();
            gen.out("while(next$", loops.get(i).valueVarName, "()!==", finished, ");");
            gen.endLine();
        }

        gen.out("return ", finished, ";");
        gen.endBlockNewLine();
        gen.endBlock(); gen.out(",");
        TypeUtils.printTypeArguments(that, gen.getTypeUtils().wrapAsIterableArguments(that.getTypeModel()), gen);
        gen.out(")");
    }

    private void declareExternalLoopVars(ComprehensionLoopInfo loop) {
        if (loop.keyVarName != null) {
            String tk = names.createTempVariable(loop.keyVarName);
            gen.out("var ", tk, "=", loop.keyVarName, ";"); gen.endLine();
            names.forceName(loop.keyDecl, tk);
        }
        String tv = names.createTempVariable(loop.valueVarName);
        gen.out("var ", tv, "=", loop.valueVarName, ";"); gen.endLine();
        names.forceName(loop.valDecl, tv);
    }

    /** Represents one of the for loops of a comprehension including the associated conditions */
    private class ComprehensionLoopInfo {
        public final ForIterator forIterator;
        public final List<Tree.ConditionList> conditions = new ArrayList<Tree.ConditionList>();
        public final List<List<ConditionGenerator.VarHolder>> conditionVars = new ArrayList<List<ConditionGenerator.VarHolder>>();
        public final String itVarName;
        public final String valueVarName;
        public final String keyVarName;
        public final Declaration keyDecl;
        public final Declaration valDecl;

        public ComprehensionLoopInfo(Comprehension that, ForIterator forIterator) {
            this.forIterator = forIterator;
            itVarName = names.createTempVariable("it");
            Variable valueVar = null;
            Variable keyVar = null;
            if (forIterator instanceof ValueIterator) {
                valueVar = ((ValueIterator) forIterator).getVariable();
            } else if (forIterator instanceof KeyValueIterator) {
                KeyValueIterator kvit = (KeyValueIterator) forIterator;
                valueVar = kvit.getValueVariable();
                keyVar = kvit.getKeyVariable();
            } else {
                that.addError("No support yet for iterators of type "
                              + forIterator.getClass().getName());
                valueVarName = null;
                keyVarName = null;
                keyDecl = null;
                valDecl = null;
                return;
            }
            if (keyVar == null) {
                keyVarName = null;
                keyDecl = null;
            } else {
                keyDecl = keyVar.getDeclarationModel();
                keyVarName = names.name(keyDecl);
                directAccess.add(keyDecl);
            }
            valDecl = valueVar.getDeclarationModel();
            this.valueVarName = names.name(valDecl);
            directAccess.add(valDecl);
        }
    }
}
