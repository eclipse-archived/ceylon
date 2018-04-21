/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.Condition;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.ExistsCondition;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.ExistsOrNonemptyCondition;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.Expression;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.IsCase;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.IsCondition;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.MatchCase;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.NonemptyCondition;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.ConditionScope;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.Value;

import org.eclipse.ceylon.compiler.js.util.JsIdentifierNames;

/** This component is used by the main JS visitor to generate code for conditions.
 * 
 * @author Enrique Zamudio
 * @author Ivo Kasiuk
 */
public class ConditionGenerator {

    public static final String ASSERTFUNC = "asrt$(";
    private final GenerateJsVisitor gen;
    private final JsIdentifierNames names;
    private final Set<Declaration> directAccess;

    ConditionGenerator(GenerateJsVisitor owner, JsIdentifierNames names, Set<Declaration> directDeclarations) {
        gen = owner;
        this.names = names;
        directAccess = directDeclarations;
    }

    /** Generate a list of all the variables from conditions in the list.
     * @param conditions The ConditionList that may contain variable declarations.
     * @param output Whether to generate the variable declarations or not. */
    List<VarHolder> gatherVariables(Tree.ConditionList conditions, boolean output, final boolean forAssert) {
        ArrayList<VarHolder> vars = new ArrayList<>();
        boolean first = true;
        for (Condition cond : conditions.getConditions()) {
            Tree.Variable variable = null;
            Tree.Destructure destruct = null;
            if (cond instanceof ExistsOrNonemptyCondition) {
                ExistsOrNonemptyCondition enc = (ExistsOrNonemptyCondition) cond;
                if (enc.getVariable() instanceof Tree.Variable) {
                    variable = (Tree.Variable)enc.getVariable();
                } else if (enc.getVariable() instanceof Tree.Destructure) {
                    destruct = (Tree.Destructure)enc.getVariable();
                }
            } else if (cond instanceof IsCondition) {
                variable = ((IsCondition) cond).getVariable();
            } else if (!(cond instanceof Tree.BooleanCondition)) {
                cond.addUnexpectedError("No support for conditions of type " + cond.getClass().getSimpleName(), Backend.JavaScript);
                return null;
            }
            if (variable != null) {
                final Tree.Term variableRHS = variable.getSpecifierExpression().getExpression().getTerm();
                final Value vdecl = variable.getDeclarationModel();
                String varName = names.name(vdecl);
                final boolean member = ModelUtil.getRealScope(vdecl.getContainer()) instanceof ClassOrInterface;
                if (member) {
                    //Assertions and special conditions in an initializer block can declare variables
                    //that should become attributes. For some reason the typechecker doesn't do this.
                    if (vdecl.getScope() instanceof ConditionScope) {
                        vdecl.setContainer(ModelUtil.getRealScope(vdecl.getContainer()));
                        varName = names.name(vdecl);
                    }
                } else if (output) {
                    if (first) {
                        first = false;
                        gen.out("var ");
                    } else {
                        gen.out(",");
                    }
                    gen.out(varName);
                    directAccess.add(vdecl);
                }
                vars.add(new VarHolder(variable, variableRHS, varName, member));
            } else if (destruct != null) {
                final Destructurer d=new Destructurer(destruct.getPattern(), null, directAccess,
                        "", first, forAssert);
                for (Tree.Variable v : d.getVariables()) {
                    if (output) {
                        final String vname = names.name(v.getDeclarationModel());
                        if (first) {
                            first = false;
                            gen.out("var ", vname);
                        } else {
                            gen.out(",", vname);
                        }
                    }
                }
                VarHolder vh = new VarHolder(destruct, null, null, false);
                vh.vars = d.getVariables();
                vars.add(vh);
            }
        }
        if (output && !first) {
            gen.endLine(true);
        }
        return vars;
    }

    /** Generates the declaration of all the variables that were gathered by
     * gartherVariables. Useful when they weren't generated by that method. */
    void outputVariables(List<VarHolder> vars) {
        boolean first=true;
        for (VarHolder vh : vars) {
            if (vh.name != null) {
                if (first) {
                    gen.out("var ", vh.name);
                    first=false;
                } else {
                    gen.out(",", vh.name);
                }
            } else if (vh.destr != null && vh.vars != null) {
                for (Tree.Variable v : vh.vars) {
                    final String vname = names.name(v.getDeclarationModel());
                    if (first) {
                        gen.out("var ", vname);
                        first=false;
                    } else {
                        gen.out(",", vname);
                    }
                }
            }
        }
        if (!first) {
            gen.endLine(true);
        }
    }

    /** Handles the "is", "exists" and "nonempty" conditions */
    List<VarHolder> specialConditionsAndBlock(Tree.ConditionList conditions,
            Tree.Block block, String keyword, final boolean forAssert) {
        final List<VarHolder> vars = gatherVariables(conditions, true, forAssert);
        specialConditions(vars, conditions, keyword, forAssert);
        if (block != null) {
            gen.encloseBlockInFunction(block, true, getCaptured(vars));
        }
        return vars;
    }

    void specialCondition(VarHolder varHolder, Tree.Condition condition, boolean forAssert) {
        if (varHolder.destr == null) {
            if (varHolder.member) {
                String cname = gen.getNames().self(((ClassOrInterface)ModelUtil.getRealScope(
                        varHolder.var.getDeclarationModel().getContainer())));
                specialConditionCheck(condition, varHolder.term, cname + "." + varHolder.name, forAssert);
            } else {
                specialConditionCheck(condition, varHolder.term, varHolder.name, forAssert);
                directAccess.add(varHolder.var.getDeclarationModel());
            }
        } else {
            destructureCondition(condition, varHolder, forAssert);
        }

    }

    /** Handles the "is", "exists" and "nonempty" conditions, with a pre-generated
     * list of the variables from the conditions. */
    void specialConditions(final List<VarHolder> vars, Tree.ConditionList conditions, String keyword,
                           final boolean forAssert) {
        //The first pass is gathering the conditions, which we already get here
        //Second pass: generate the conditions
        if (!keyword.isEmpty()) {
            gen.out(keyword, "(");
        }
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
                specialCondition(ivars.next(), cond, forAssert);
            }
        }
        if (!keyword.isEmpty()) {
            gen.out(")");
        }
    }

    private void specialConditionCheck(Condition condition, Tree.Term variableRHS, String varName,
                                       final boolean forAssert) {
        if (condition instanceof ExistsOrNonemptyCondition) {
            ExistsOrNonemptyCondition enc = (ExistsOrNonemptyCondition) condition;
            if (enc.getNot()) {
                gen.out("!");
            }
            if (condition instanceof Tree.NonemptyCondition) {
                gen.out(gen.getClAlias(), "ne$(");
            } else { //exists
                gen.out(gen.getClAlias(), "nn$(");
            }
            specialConditionRHS(variableRHS, varName);
            gen.out(")");
        } else {
            IsCondition ic = (IsCondition) condition;
            Tree.Type type = ic.getType();
            gen.generateIsOfType(variableRHS, null, type.getTypeModel(),
                    varName, ic.getNot(), forAssert);
        }
    }

    void specialConditionRHS(Tree.Term variableRHS, String varName) {
        if (varName == null) {
            if (variableRHS!=null) {
                variableRHS.visit(gen);
            }
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
        final List<VarHolder> vars = specialConditionsAndBlock(ifClause.getConditionList(), ifBlock, "if", false);
        if (anoserque != null) {
            final Tree.Variable elsevar = anoserque.getVariable();
            final Value elseDec = elsevar != null ? elsevar.getDeclarationModel() : null;
            if (elsevar != null) {
                for (VarHolder vh : vars) {
                    if (vh.var.getDeclarationModel().getName().equals(elseDec.getName())) {
                        names.forceName(elseDec, vh.name);
                        directAccess.add(elseDec);
                        break;
                    }
                }
            }
            gen.out("else");
            gen.encloseBlockInFunction(anoserque.getBlock(), true,
                    elseDec != null && elseDec.isJsCaptured() ?
                            Collections.singleton(elseDec) : null);
            if (elseDec != null) {
                directAccess.remove(elseDec);
                names.forceName(elseDec, null);
            }
        }
        for (VarHolder v : vars) {
            v.forget();
        }
    }

    void generateIfExpression(Tree.IfExpression that, boolean nested) {
        final List<VarHolder> vars = gatherVariables(that.getIfClause().getConditionList(), false, false);
        final Tree.ElseClause anoserque = that.getElseClause();
        final Tree.Variable elsevar = anoserque == null ? null : anoserque.getVariable();
        if (elsevar != null) {
            final Value elseval = elsevar.getDeclarationModel();
            for (VarHolder vh : vars) {
                if (vh.var != null && vh.var.getDeclarationModel().getName().equals(elseval.getName())) {
                    names.forceName(elseval, vh.name);
                    directAccess.add(elseval);
                    break;
                }
            }
        }
        if (vars.isEmpty()) {
            if (nested) {
                gen.out(" return ");
            } else {
                gen.out("(");
            }
            //No special conditions means we can use a simple ternary
            specialConditions(vars, that.getIfClause().getConditionList(), "", false);
            gen.out("?");
            that.getIfClause().getExpression().visit(gen);
            gen.out(":");
            if (anoserque == null) {
                gen.out("null;");
            } else {
                anoserque.getExpression().visit(gen);
            }
            if (!nested) {
                gen.out(")");
            }
        } else {
            if (nested) {
                gen.out("{");
            } else {
                gen.out("function(){");
            }
            outputVariables(vars);
            specialConditions(vars, that.getIfClause().getConditionList(), "if", false);
            gen.out("return ");
            that.getIfClause().getExpression().visit(gen);
            for (VarHolder v : vars) {
                v.forget();
            }
            final boolean thenIf = anoserque != null &&
                    anoserque.getExpression().getTerm() instanceof Tree.IfExpression;
            if (thenIf) {
                gen.out(";else");
            } else {
                gen.out(";else return ");
            }
            if (anoserque == null) {
                gen.out("null;");
            } else if (thenIf) {
                generateIfExpression((Tree.IfExpression)anoserque.getExpression().getTerm(), true);
            } else {
                anoserque.getExpression().visit(gen);
            }
            if (nested) {
                gen.out("}");
            } else {
                gen.out("}()");
            }
        }
        if (elsevar != null) {
            directAccess.remove(elsevar.getDeclarationModel());
            names.forceName(elsevar.getDeclarationModel(), null);
        }
    }
    /** Generates JS code for a WhileStatement. */
    void generateWhile(Tree.WhileStatement that) {
        Tree.WhileClause whileClause = that.getWhileClause();
        List<VarHolder> vars = specialConditionsAndBlock(whileClause.getConditionList(),
                whileClause.getBlock(), "while", false);
        for (VarHolder v : vars) {
            v.forget();
        }
    }

    private void generateSwitch(final Tree.SwitchClause clause, final Tree.SwitchCaseList cases, final SwitchGen sgen) {
        final String expvar;
        final Expression expr;
        if (clause.getSwitched().getExpression() == null) {
            expvar = names.name(clause.getSwitched().getVariable().getDeclarationModel());
            expr = clause.getSwitched().getVariable().getSpecifierExpression().getExpression();
            directAccess.add(clause.getSwitched().getVariable().getDeclarationModel());
        } else {
            //Put the expression in a tmp var
            expr = clause.getSwitched().getExpression();
            expvar = names.createTempVariable();
        }
        sgen.gen1(expvar, expr);
        //For each case, do an if
        boolean first = true;
        for (Tree.CaseClause cc : cases.getCaseClauses()) {
            if (!first) gen.out("else ");
            caseClause(cc, expvar, expr.getTerm());
            first = false;
        }
        final Tree.ElseClause anoserque = cases.getElseClause();
        if (anoserque == null) {
            if (gen.isInDynamicBlock() && ModelUtil.isTypeUnknown(expr.getTypeModel())) {
                gen.out("else throw ", gen.getClAlias(), "Exception('Ceylon switch over unknown type does not cover all cases')");
            } else {
                gen.out("else throw ", gen.getClAlias(), "Exception('Supposedly exhaustive switch was not exhaustive')");
            }
        } else {
            final Tree.Variable elsevar = anoserque.getVariable();
            if (elsevar != null) {
                directAccess.add(elsevar.getDeclarationModel());
                names.forceName(elsevar.getDeclarationModel(), expvar);
            }
            sgen.gen2(anoserque);
            if (elsevar != null) {
                directAccess.remove(elsevar.getDeclarationModel());
                names.forceName(elsevar.getDeclarationModel(), null);
            }
        }
        sgen.gen3(expr);
        if (clause.getSwitched().getExpression() == null) {
            directAccess.remove(clause.getSwitched().getVariable().getDeclarationModel());
        }
    }

    void switchStatement(final Tree.SwitchStatement that) {
        generateSwitch(that.getSwitchClause(), that.getSwitchCaseList(), new SwitchGen() {
            public void gen1(String expvar, Expression expr) {
                gen.out("var ", expvar, "=");
                expr.visit(gen);
                gen.endLine(true);
            }
            public void gen2(Tree.ElseClause anoserque) {
                gen.out("else");
                anoserque.getBlock().visit(gen);
            }
            public void gen3(Expression expr) {}
        });
    }

    void switchExpression(final Tree.SwitchExpression that) {
        generateSwitch(that.getSwitchClause(), that.getSwitchCaseList(), new SwitchGen() {
            public void gen1(String expvar, Expression expr) {
                gen.out("function(", expvar, "){");
            }
            public void gen2(Tree.ElseClause anoserque) {
                gen.out("else return ");
                anoserque.getExpression().visit(gen);
            }
            public void gen3(Expression expr) {
                gen.out("}(");
                expr.visit(gen);
                gen.out(")");
            }
        });
    }

    /** Generates code for a case clause, as part of a switch statement. Each case
     * is rendered as an if. */
    private void caseClause(final Tree.CaseClause cc, String expvar, final Tree.Term switchTerm) {
        gen.out("if(");
        final Tree.CaseItem item = cc.getCaseItem();
        Tree.Variable caseVar = null;
        Value caseDec = null;
        if (item instanceof IsCase) {
            IsCase isCaseItem = (IsCase) item;
            Type caseType = isCaseItem.getType().getTypeModel();
			gen.generateIsOfType(item, expvar, caseType, null, false, false);
            caseVar = isCaseItem.getVariable();
            if (caseVar != null) {
                caseDec = caseVar.getDeclarationModel();
                directAccess.add(caseDec);
                names.forceName(caseDec, expvar);
            }
        } else if (item instanceof Tree.SatisfiesCase) {
            item.addError("case(satisfies) not yet supported", Backend.JavaScript);
            gen.out("true");
        } else if (item instanceof MatchCase) {
            Type switchType = switchTerm.getTypeModel();
			final boolean isNull = switchType.covers(switchTerm.getUnit().getNullType());
            boolean first = true;
            MatchCase matchCaseItem = (MatchCase) item;
            Tree.MatchList matchList = matchCaseItem.getExpressionList();
            if (!matchList.getTypes().isEmpty()) {
                first = false;
                List<Type> union = new ArrayList<Type>();
                for (Tree.Type type: matchList.getTypes()) {
                   ModelUtil.addToUnion(union, type.getTypeModel());
                }
                gen.generateIsOfType(item, expvar, 
                		ModelUtil.union(union, matchList.getUnit()), 
                		null, false, false);
            }
            for (Expression exp : matchList.getExpressions()) {
                if (!first) gen.out(" || ");
                final Tree.Term term = exp.getTerm();
                if (term instanceof Tree.NaturalLiteral 
                		|| term instanceof Tree.NegativeOp 
                		&& ((Tree.NegativeOp)term).getTerm() 
                			instanceof Tree.NaturalLiteral) {
                	if (isNull) {
                        gen.out(gen.getClAlias(), "nn$(", expvar, ")&&");
                    }
                	if (switchType.isInteger()) {
                    	gen.out(expvar, "==");
                    	exp.visit(gen);
                    }
                    else {
	                    gen.out(expvar, ".equals(");
	                    gen.box(term, true, true);
	                    gen.out(")");
                    }
                } else if (term instanceof Tree.StringLiteral) {
                    if (isNull) {
                        gen.out(gen.getClAlias(), "nn$(", expvar, ")&&");
                    }
                    gen.out(expvar, ".equals(");
                    gen.box(term, true, true);
                    gen.out(")");
                } else if (term instanceof Tree.CharLiteral) {
                    if (isNull) {
                    	gen.out(gen.getClAlias(), "nn$(", expvar, ")&&");
                    }
                    gen.out(expvar, ".equals(");
                    gen.box(term, true, true);
                    gen.out(")");
                } else if (ModelUtil.isTypeUnknown(switchType)) {
                    gen.out(expvar, "===");
//                    if (!gen.isNaturalLiteral(term)) {
                        exp.visit(gen);
//                    }
                } else if (term instanceof Tree.Tuple) {
                    if (((Tree.Tuple) term).getSequencedArgument() == null) {
                        gen.out(expvar, "===", gen.getClAlias(), "empty()");
                    } else {
                        gen.out(gen.getClAlias(), "is$(", expvar, ",{t:",gen.getClAlias(), "Tuple})&&",
                                expvar, ".equals(");
                        exp.visit(gen);
                        gen.out(")");
                    }
                } else {
                    gen.out(expvar, "===");
                    exp.visit(gen);
                }
                first = false;
            }
            caseVar = matchCaseItem.getVariable();
            if (caseVar != null) {
                caseDec = caseVar.getDeclarationModel();
                directAccess.add(caseDec);
                names.forceName(caseDec, expvar);
            }
        } else {
            cc.addUnexpectedError("support for case of type " + cc.getClass().getSimpleName() + " not yet implemented", Backend.JavaScript);
        }
        gen.out(")");
        if (cc.getBlock() == null) {
            gen.out("return ");
            cc.getExpression().visit(gen);
            gen.out(";");
        } else {
            gen.out(" ");
            Set<Value> cap = caseDec != null && caseDec.isJsCaptured() ?
                    Collections.singleton(caseDec) : null;
            gen.encloseBlockInFunction(cc.getBlock(), true, cap);
        }
        if (caseDec != null) {
            directAccess.remove(caseDec);
            names.forceName(caseDec, null);
        }
    }

    void destructureCondition(Condition cond, VarHolder vh, final boolean forAssert) {
        final String expvar = names.createTempVariable();
        gen.out("function(", expvar, "){if(");
        if (cond instanceof ExistsCondition) {
            if (!((ExistsCondition)cond).getNot()) {
                gen.out("!");
            }
            gen.out(gen.getClAlias(), "nn$(", expvar, "))return false;");
        } else if (cond instanceof NonemptyCondition) {
            if (!((NonemptyCondition)cond).getNot()) {
                gen.out("!");
            }
            gen.out(gen.getClAlias(), "ne$(", expvar, "))return false;");
        } else {
            Tree.Type type = ((IsCondition) cond).getType();
            gen.generateIsOfType(null, expvar, type.getTypeModel(),
                    null, ((IsCondition)cond).getNot(), forAssert);
            gen.out(")return false;");
        }
        gen.out("return(");
        final Destructurer d=new Destructurer(vh.destr.getPattern(), gen, directAccess, expvar,
                true, forAssert);
        gen.out(",true);}(");
        vh.destr.getSpecifierExpression().visit(gen);
        gen.out(")");
        vh.vars=d.getVariables();
        vh.captured=d.getCapturedValues();
    }

    /** Get all the captured variables from the set of VarHolders. */
    Set<Value> getCaptured(List<VarHolder> vars) {
        Set<Value> caps = new HashSet<>(3);
        for (VarHolder vh : vars) {
            Set<Value> c2 = vh.getCaptured();
            if (c2 != null && !c2.isEmpty()) {
                caps.addAll(c2);
            }
        }
        return caps;
    }

    /** Holder for a special condition's variable, its right-hand side term,
     * and its name in the generated js. */
    class VarHolder {
        final Tree.Variable var;
        final Tree.Term term;
        final String name;
        final Tree.Destructure destr;
        Set<Tree.Variable> vars;
        Set<Value> captured;
        final boolean member;
        private VarHolder(Tree.Variable v, Tree.Term rhs, String varName, boolean member) {
            this(v, null, v.getScope(), rhs, varName, member);
        }
        private VarHolder(Tree.Destructure d, Tree.Term rhs, String varName, boolean member) {
            this(null, d, d.getScope(), rhs, varName, member);
        }
        private VarHolder(Tree.Variable v, Tree.Destructure d, Scope scope, Tree.Term rhs, String varName, boolean member) {
            if (v!=null) {
                var = v;
                destr = null;
                if (var.getDeclarationModel() != null && var.getDeclarationModel().isJsCaptured()) {
                    captured = Collections.singleton(var.getDeclarationModel());
                }
            } else {
                var = null;
                destr = d;
            }
            term = rhs;
            if (scope instanceof Tree.Assertion &&
                    scope.getContainer() instanceof ClassOrInterface) {
                name = gen.getNames().self((ClassOrInterface)scope.getContainer()) + "." + varName;
                if (var != null) {
                    names.forceName(var.getDeclarationModel(), name);
                }
            } else {
                name = varName;
            }
            this.member = member;
        }
        void forget() {
            if (var != null) {
                directAccess.remove(var.getDeclarationModel());
                names.forceName(var.getDeclarationModel(), null);
            } else if (vars != null) {
                for (Tree.Variable v : vars) {
                    directAccess.remove(v.getDeclarationModel());
                    names.forceName(v.getDeclarationModel(), null);
                }
            }
        }
        public Set<Value> getCaptured() {
            return captured;
        }
        public String toString() {
            if (var != null) {
                return "VarHolder(" + var.getDeclarationModel() + ")";
            } else if (vars != null) {
                final StringBuilder sb = new StringBuilder();
                for (Tree.Variable v : vars) {
                    if (sb.length() == 0) {
                        sb.append("VarHolder[");
                    } else {
                        sb.append(",");
                    }
                    sb.append(v.getDeclarationModel());
                }
                sb.append("]");
                return sb.toString();
            }
            return "VarHolder[???]";
        }
    }

    private interface SwitchGen {
        void gen1(String expvar, Expression expr);
        void gen2(Tree.ElseClause anoserque);
        void gen3(Expression expr);
    }
}
