package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Condition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExistsOrNonemptyCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IsCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NonemptyCondition;

/** This component is used by the main JS visitor to generate code for conditions.
 * 
 * @author Enrique Zamudio
 * @author Ivo Kasiuk
 */
public class ConditionGenerator {

    private final GenerateJsVisitor gen;
    private final JsIdentifierNames names;
    private final Set<Declaration> directAccess;

    ConditionGenerator(GenerateJsVisitor owner, JsIdentifierNames names, Set<Declaration> directDeclarations) {
        gen = owner;
        this.names = names;
        directAccess = directDeclarations;
    }

    /** Generate a list of all the variables from conditions in the list. */
    List<VarHolder> gatherVariables(Tree.ConditionList conditions) {
        ArrayList<VarHolder> vars = new ArrayList<VarHolder>();
        for (Condition cond : conditions.getConditions()) {
            Tree.Variable variable = null;
            if (cond instanceof ExistsOrNonemptyCondition) {
                variable = ((ExistsOrNonemptyCondition) cond).getVariable();
            } else if (cond instanceof IsCondition) {
                variable = ((IsCondition) cond).getVariable();
            } else if (!(cond instanceof Tree.BooleanCondition)) {
                cond.addUnexpectedError("No support for conditions of type " + cond.getClass().getSimpleName());
                return null;
            }
            if (variable != null) {
                Tree.Term variableRHS = variable.getSpecifierExpression().getExpression().getTerm();
                String varName = names.name(variable.getDeclarationModel());
                gen.out("var ", varName);
                gen.endLine(true);
                vars.add(new VarHolder(variable, variableRHS, varName));
            }
        }
        return vars;
    }

    /** Handles the "is", "exists" and "nonempty" conditions */
    void specialConditionsAndBlock(Tree.ConditionList conditions,
            Tree.Block block, String keyword) {
        final List<VarHolder> vars = gatherVariables(conditions);
        specialConditions(vars, conditions, keyword);
        if (block != null) {
            gen.encloseBlockInFunction(block);
        }
    }

    /** Handles the "is", "exists" and "nonempty" conditions, with a pre-generated
     * list of the variables from the conditions. */
    void specialConditions(final List<VarHolder> vars, Tree.ConditionList conditions, String keyword) {
        //The first pass is gathering the conditions, which we already get here
        //Second pass: generate the conditions
        gen.out(keyword);
        gen.out("(");
        boolean first = true;
        final Iterator<VarHolder> ivars = vars.iterator();
        for (Condition cond : conditions.getConditions()) {
            if (first) {
                first = false;
            } else {
                gen.out("&&");
            }
            if (cond instanceof Tree.BooleanCondition) {
                cond.visit(gen);
            } else {
                VarHolder vh = ivars.next();
                specialConditionCheck(cond, vh.term, vh.name);
                directAccess.add(vh.var.getDeclarationModel());
            }
        }
        gen.out(")");
    }

    void specialConditionCheck(Condition condition, Tree.Term variableRHS, String varName) {
        if (condition instanceof ExistsOrNonemptyCondition) {
            if (condition instanceof NonemptyCondition) {
                gen.out(GenerateJsVisitor.getClAlias(), "nonempty(");
                specialConditionRHS(variableRHS, varName);
                gen.out(")");
            } else { //exists
                specialConditionRHS(variableRHS, varName);
                gen.out("!==null");
                if (gen.isInDynamicBlock()) {
                    gen.out("&&", varName, "!==undefined");
                }
            }

        } else {
            Tree.Type type = ((IsCondition) condition).getType();
            gen.generateIsOfType(variableRHS, null, type, varName, ((IsCondition)condition).getNot());
        }
    }

    void specialConditionRHS(Tree.Term variableRHS, String varName) {
        if (varName == null) {
            variableRHS.visit(gen);
        } else {
            gen.out("(", varName, "=");
            variableRHS.visit(gen);
            gen.out(")");
        }
    }

    void specialConditionRHS(String variableRHS, String varName) {
        if (varName == null) {
            gen.out(variableRHS);
        } else {
            gen.out("(", varName, "=");
            gen.out(variableRHS);
            gen.out(")");
        }
    }

    /** Generates JS code for an "if" statement. */
    void generateIf(Tree.IfStatement that) {
        Tree.IfClause ifClause = that.getIfClause();
        Tree.Block ifBlock = ifClause.getBlock();
        specialConditionsAndBlock(ifClause.getConditionList(), ifBlock, "if");

        if (that.getElseClause() != null) {
            gen.out("else ");
            gen.encloseBlockInFunction(that.getElseClause().getBlock());
        }
    }

    /** Generates JS code for a WhileStatement. */
    void generateWhile(Tree.WhileStatement that) {
        Tree.WhileClause whileClause = that.getWhileClause();
        specialConditionsAndBlock(whileClause.getConditionList(), whileClause.getBlock(), "while");
    }

    /** Holder for a special condition's variable, its right-hand side term,
     * and its name in the generated js. */
    class VarHolder {
        final Tree.Variable var;
        final Tree.Term term;
        final String name;
        private VarHolder(Tree.Variable variable, Tree.Term rhs, String varName) {
            var = variable;
            term = rhs;
            name = varName;
        }
    }
}
