package com.redhat.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Condition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ExistsOrNonemptyCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IsCase;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IsCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MatchCase;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;

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
        boolean first = true;
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
                if (first) {
                    first = false;
                    gen.out("var ");
                } else {
                    gen.out(",");
                }
                gen.out(varName);
                vars.add(new VarHolder(variable, variableRHS, varName));
            }
        }
        if (!first) {
            gen.endLine(true);
        }
        return vars;
    }

    /** Handles the "is", "exists" and "nonempty" conditions */
    List<VarHolder> specialConditionsAndBlock(Tree.ConditionList conditions,
            Tree.Block block, String keyword) {
        final List<VarHolder> vars = gatherVariables(conditions);
        specialConditions(vars, conditions, keyword);
        if (block != null) {
            gen.encloseBlockInFunction(block, true);
        }
        return vars;
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
            if (((ExistsOrNonemptyCondition) condition).getNot()) {
                gen.out("!");
            }
            if (condition instanceof Tree.NonemptyCondition) {
                gen.out(gen.getClAlias(), "ne$(");
                specialConditionRHS(variableRHS, varName);
                gen.out(")");
            } else { //exists
                gen.out(gen.getClAlias(), "nn$(");
                specialConditionRHS(variableRHS, varName);
                gen.out(")");
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
        final Tree.IfClause ifClause = that.getIfClause();
        final Tree.Block ifBlock = ifClause.getBlock();
        final Tree.ElseClause anoserque = that.getElseClause();
        final List<VarHolder> vars = specialConditionsAndBlock(ifClause.getConditionList(), ifBlock, "if");
        if (anoserque != null) {
            final Tree.Variable elsevar = anoserque.getVariable();
            if (elsevar != null) {
                for (VarHolder vh : vars) {
                    if (vh.var.getDeclarationModel().getName().equals(elsevar.getDeclarationModel().getName())) {
                        gen.getNames().forceName(elsevar.getDeclarationModel(), vh.name);
                        directAccess.add(elsevar.getDeclarationModel());
                        break;
                    }
                }
            }
            gen.out("else");
            gen.encloseBlockInFunction(anoserque.getBlock(), true);
            if (elsevar != null) {
                directAccess.remove(anoserque.getVariable().getDeclarationModel());
            }
        }
        for (VarHolder v : vars) {
            directAccess.remove(v.var.getDeclarationModel());
            gen.getNames().forceName(v.var.getDeclarationModel(), null);
        }
    }

    void generateIfExpression(Tree.IfExpression that) {
        gen.out("function(){");
        final List<VarHolder> vars = gatherVariables(that.getIfClause().getConditionList());
        if (vars.isEmpty()) {
            //TODO optimize to a simple ternary statement when there are no special conditions
            gen.out("return");
            specialConditions(vars, that.getIfClause().getConditionList(), "");
            gen.out("?");
            that.getIfClause().getExpression().visit(gen);
            gen.out(":");
            if (that.getElseClause() == null) {
                gen.out("null;");
            } else {
                that.getElseClause().getExpression().visit(gen);
            }
        } else {
            specialConditions(vars, that.getIfClause().getConditionList(), "if");
            gen.out("return ");
            that.getIfClause().getExpression().visit(gen);
            gen.out(";else return ");
            if (that.getElseClause() == null) {
                gen.out("null;");
            } else {
                that.getElseClause().getExpression().visit(gen);
            }
            for (VarHolder v : vars) {
                directAccess.remove(v.var.getDeclarationModel());
                gen.getNames().forceName(v.var.getDeclarationModel(), null);
            }
        }
        gen.out("}()");
    }
    /** Generates JS code for a WhileStatement. */
    void generateWhile(Tree.WhileStatement that) {
        Tree.WhileClause whileClause = that.getWhileClause();
        List<VarHolder> vars = specialConditionsAndBlock(whileClause.getConditionList(),
                whileClause.getBlock(), "while");
        for (VarHolder v : vars) {
            directAccess.remove(v.var.getDeclarationModel());
            gen.getNames().forceName(v.var.getDeclarationModel(), null);
        }
    }

    void generateSwitch(Tree.SwitchStatement that) {
        final String expvar;
        final Expression expr;
        if (that.getSwitchClause().getSwitched().getExpression() == null) {
            expvar = names.name(that.getSwitchClause().getSwitched().getVariable().getDeclarationModel());
            expr = that.getSwitchClause().getSwitched().getVariable().getSpecifierExpression().getExpression();
            directAccess.add(that.getSwitchClause().getSwitched().getVariable().getDeclarationModel());
        } else {
            //Put the expression in a tmp var
            expr = that.getSwitchClause().getSwitched().getExpression();
            expvar = names.createTempVariable();
        }
        gen.out("var ", expvar, "=");
        expr.visit(gen);
        gen.endLine(true);
        //For each case, do an if
        boolean first = true;
        for (Tree.CaseClause cc : that.getSwitchCaseList().getCaseClauses()) {
            if (!first) gen.out("else ");
            caseClause(cc, expvar, expr.getTerm());
            first = false;
        }
        final Tree.ElseClause anoserque = that.getSwitchCaseList().getElseClause();
        if (anoserque == null) {
            if (gen.isInDynamicBlock() && expr.getTypeModel().getDeclaration() instanceof UnknownType) {
                gen.out("else throw ", gen.getClAlias(), "Exception('Ceylon switch over unknown type does not cover all cases')");
            }
        } else {
            gen.out("else");
            final Variable elsevar = anoserque.getVariable();
            if (elsevar != null) {
                directAccess.add(elsevar.getDeclarationModel());
                names.forceName(elsevar.getDeclarationModel(), expvar);
            }
            anoserque.getBlock().visit(gen);
            if (elsevar != null) {
                directAccess.remove(elsevar.getDeclarationModel());
            }
        }
        if (that.getSwitchClause().getSwitched().getExpression() == null) {
            directAccess.remove(that.getSwitchClause().getSwitched().getVariable().getDeclarationModel());
        }
    }

    void generateSwitchExpression(Tree.SwitchExpression that) {
        final String expvar;
        final Expression expr;
        if (that.getSwitchClause().getSwitched().getExpression() == null) {
            expvar = names.name(that.getSwitchClause().getSwitched().getVariable().getDeclarationModel());
            expr = that.getSwitchClause().getSwitched().getVariable().getSpecifierExpression().getExpression();
            directAccess.add(that.getSwitchClause().getSwitched().getVariable().getDeclarationModel());
        } else {
            //Put the expression in a tmp var
            expr = that.getSwitchClause().getSwitched().getExpression();
            expvar = names.createTempVariable();
        }
        gen.out("function(", expvar, "){");
        //For each case, do an if
        boolean first = true;
        for (Tree.CaseClause cc : that.getSwitchCaseList().getCaseClauses()) {
            if (!first) gen.out("else ");
            caseClause(cc, expvar, expr.getTerm());
            first = false;
        }
        final Tree.ElseClause anoserque = that.getSwitchCaseList().getElseClause();
        if (anoserque == null) {
            if (gen.isInDynamicBlock() && expr.getTypeModel().getDeclaration() instanceof UnknownType) {
                gen.out("else throw ", gen.getClAlias(), "Exception('Ceylon switch over unknown type does not cover all cases')");
            }
        } else {
            gen.out("else return ");
            anoserque.getExpression().visit(gen);
        }
        gen.out("}(");
        expr.visit(gen);
        gen.out(")");
    }

    /** Generates code for a case clause, as part of a switch statement. Each case
     * is rendered as an if. */
    private void caseClause(final Tree.CaseClause cc, String expvar, final Tree.Term switchTerm) {
        gen.out("if(");
        final Tree.CaseItem item = cc.getCaseItem();
        Tree.Variable caseVar = null;
        if (item instanceof IsCase) {
            IsCase isCaseItem = (IsCase) item;
            gen.generateIsOfType(item, expvar, isCaseItem.getType(), null, false);
            caseVar = isCaseItem.getVariable();
            if (caseVar != null) {
                directAccess.add(caseVar.getDeclarationModel());
                names.forceName(caseVar.getDeclarationModel(), expvar);
            }
        } else if (item instanceof Tree.SatisfiesCase) {
            item.addError("case(satisfies) not yet supported");
            gen.out("true");
        } else if (item instanceof MatchCase) {
            boolean first = true;
            for (Expression exp : ((MatchCase)item).getExpressionList().getExpressions()) {
                if (!first) gen.out(" || ");
                if (exp.getTerm() instanceof Tree.StringLiteral || exp.getTerm() instanceof Tree.NaturalLiteral
                        || switchTerm.getTypeModel().isUnknown()) {
                    gen.out(expvar, "===");
                    if (!gen.isNaturalLiteral(exp.getTerm())) {
                        exp.visit(gen);
                    }
                } else if (exp.getTerm() instanceof Tree.Literal) {
                    if (switchTerm.getUnit().isOptionalType(switchTerm.getTypeModel())) {
                        gen.out(expvar,"!==null&&");
                    }
                    gen.out(expvar, ".equals(");
                    exp.visit(gen);
                    gen.out(")");
                } else {
                    gen.out(expvar, "===");
                    exp.visit(gen);
                }
                first = false;
            }
        } else {
            cc.addUnexpectedError("support for case of type " + cc.getClass().getSimpleName() + " not yet implemented");
        }
        gen.out(")");
        if (cc.getBlock() == null) {
            gen.out("return ");
            cc.getExpression().visit(gen);
            gen.out(";");
        } else {
            gen.out(" ");
            gen.encloseBlockInFunction(cc.getBlock(), true);
        }
        if (caseVar != null) {
            directAccess.remove(caseVar.getDeclarationModel());
            gen.getNames().forceName(caseVar.getDeclarationModel(), null);
        }
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
