package com.redhat.ceylon.compiler.js;

import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Block;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Condition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExistsOrNonemptyCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IfClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IfStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IsCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.NonemptyCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Type;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.WhileClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.WhileStatement;

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

    /** Handles the "is", "exists" and "nonempty" conditions */
    void specialConditionAndBlock(Condition condition,
            Block block, String keyword) {
        Variable variable = null;
        if (condition instanceof ExistsOrNonemptyCondition) {
            variable = ((ExistsOrNonemptyCondition) condition).getVariable();
        } else if (condition instanceof IsCondition) {
            variable = ((IsCondition) condition).getVariable();
        } else {
            condition.addUnexpectedError("No support for conditions of type " + condition.getClass().getSimpleName());
            return;
        }
        Term variableRHS = variable.getSpecifierExpression().getExpression().getTerm();

        String varName = names.name(variable.getDeclarationModel());
        gen.out("var ", varName, ";");
        gen.endLine();

        gen.out(keyword);
        gen.out("(");
        specialConditionCheck(condition, variableRHS, varName);
        gen.out(")");
        directAccess.add(variable.getDeclarationModel());
        if (block != null) {
            gen.encloseBlockInFunction(block);
        }
    }

    void specialConditionCheck(Condition condition, Term variableRHS, String varName) {
        if (condition instanceof ExistsOrNonemptyCondition) {
            if (condition instanceof NonemptyCondition) {
                gen.out(GenerateJsVisitor.getClAlias(), ".nonempty(");
                specialConditionRHS(variableRHS, varName);
                gen.out(")");
            } else {
                specialConditionRHS(variableRHS, varName);
                gen.out("!==null");
            }

        } else {
            Type type = ((IsCondition) condition).getType();
            gen.generateIsOfType(variableRHS, null, type, varName);
        }
    }

    void specialConditionRHS(Term variableRHS, String varName) {
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
    void generateIf(IfStatement that) {
        IfClause ifClause = that.getIfClause();
        Block ifBlock = ifClause.getBlock();
        Condition condition = ifClause.getCondition();
        if ((condition instanceof ExistsOrNonemptyCondition)
                || (condition instanceof IsCondition)) {
            // if (is/exists/nonempty ...)
            specialConditionAndBlock(condition, ifBlock, "if");
        } else {
            gen.out("if (");
            condition.visit(gen);
            gen.out(")");
            if (ifBlock != null) {
                gen.encloseBlockInFunction(ifBlock);
            }
        }

        if (that.getElseClause() != null) {
            gen.out("else ");
            gen.encloseBlockInFunction(that.getElseClause().getBlock());
        }
    }

    /** Generates JS code for a WhileStatement. */
    void generateWhile(WhileStatement that) {
        WhileClause whileClause = that.getWhileClause();
        Condition condition = whileClause.getCondition();
        if ((condition instanceof ExistsOrNonemptyCondition)
                || (condition instanceof IsCondition)) {
            // while (is/exists/nonempty...)
            specialConditionAndBlock(condition, whileClause.getBlock(), "while");
        } else {
            gen.out("while (");
            condition.visit(gen);
            gen.out(")");
            gen.encloseBlockInFunction(whileClause.getBlock());
        }
    }

}
