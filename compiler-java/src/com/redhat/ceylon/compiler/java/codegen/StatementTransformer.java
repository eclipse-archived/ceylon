/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.java.codegen;

import static com.sun.tools.javac.code.Flags.FINAL;

import java.util.ArrayList;
import java.util.Collections;

import com.redhat.ceylon.compiler.java.codegen.Naming.SyntheticName;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Block;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BooleanCondition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CaseClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CaseItem;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CatchClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Condition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ElseClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FinallyClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ForIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.IsCase;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.KeyValueIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MatchCase;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SatisfiesCase;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SwitchCaseList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SwitchClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SwitchStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Throw;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TryCatchStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TryClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ValueIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.DiagnosticSource;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;

/**
 * This transformer deals with statements only
 */
public class StatementTransformer extends AbstractTransformer {

    // Used to hold the name of the variable associated with the fail-block if the innermost for-loop
    // Is null if we're currently in a while-loop or not in any loop at all
    private Name currentForFailVariable = null;
    
    /**
     * If false generating plain {@code return;} statements is OK.
     * If true then generate {@code return null;} statements instead of 
     * expressionless {@code return;}.
     */
    boolean noExpressionlessReturn = false;
    
    public static StatementTransformer getInstance(Context context) {
        StatementTransformer trans = context.get(StatementTransformer.class);
        if (trans == null) {
            trans = new StatementTransformer(context);
            context.put(StatementTransformer.class, trans);
        }
        return trans;
    }

    private StatementTransformer(Context context) {
        super(context);
    }

    public JCBlock transform(Tree.Block block) {
        return block == null ? null : at(block).Block(0, transformBlock(block));
    }
    
    public List<JCStatement> transformBlock(Tree.Block block) {
        if (block == null) {
            return List.<JCStatement>nil();
        }
        at(block);
        return transformStmts(block.getStatements());
    }

    @SuppressWarnings("unchecked")
    List<JCStatement> transformStmts(java.util.List<Tree.Statement> list) {
        CeylonVisitor v = gen().visitor;
        final ListBuffer<JCTree> prevDefs = v.defs;
        final boolean prevInInitializer = v.inInitializer;
        final ClassDefinitionBuilder prevClassBuilder = v.classBuilder;
        List<JCStatement> childDefs;
        try {
            v.defs = new ListBuffer<JCTree>();
            v.inInitializer = false;
            v.classBuilder = current();
            for (Tree.Statement stmt : list)
                stmt.visit(v);
            childDefs = (List<JCStatement>)v.getResult().toList();
        } finally {
            v.classBuilder = prevClassBuilder;
            v.inInitializer = prevInInitializer;
            v.defs = prevDefs;
        }

        return childDefs;
    }

    abstract class CondList {
        protected final Tree.Block thenPart;
        protected final java.util.List<Condition> conditions;
        public CondList(java.util.List<Condition> conditions, Tree.Block thenPart) {
            this.conditions = conditions;
            this.thenPart = thenPart;
        }
        
        protected List<JCStatement> transformList(java.util.List<Condition> conditions) {
            Condition condition = conditions.get(0);
            if (conditions.size() == 1) {
                return transformInnermost(condition);
            } else {
                return transformIntermediate(condition, conditions.subList(1, conditions.size()));
            }
        }

        protected abstract List<JCStatement> transformInnermost(Condition condition);
        
        protected List<JCStatement> transformIntermediate(Condition condition, java.util.List<Condition> rest) {
            return transformList(rest);
        }
    }
    
    class IfCondList extends CondList {

        final ListBuffer<JCStatement> varDecls = ListBuffer.lb();
        final SyntheticName ifVar = naming.temp("if");
        List<Cond> unassignedResultVars = List.<Cond>nil();
        private JCBlock thenBlock;
        
        public IfCondList(java.util.List<Condition> conditions, Block thenPart) {
            super(conditions, thenPart);
        }     

        @Override
        protected List<JCStatement> transformInnermost(Condition condition) {
            Cond transformedCond = transformedCondition(condition, thenPart);
            thenBlock = transformedCond.makeThenBlock();
            List<JCStatement> stmts = List.<JCStatement>of(make().Exec(make().Assign(ifVar.makeIdent(), makeBoolean(true))));
            return transformCommon(transformedCond, stmts);
        }
        
        protected List<JCStatement> transformIntermediate(Condition condition, java.util.List<Condition> rest) {
            return transformCommon(transformedCondition(condition, null), transformList(rest));
        }
        
        protected List<JCStatement> transformCommon(Cond transformedCond, List<JCStatement> stmts) {
            if (transformedCond.hasResultDecl()) {
                unassignedResultVars = unassignedResultVars.append(transformedCond);
            }
            if (transformedCond.makeTestVarDecl(true) != null) {
                varDecls.append(transformedCond.makeTestVarDecl(true));
            }
            JCVariableDecl resultVarDecl = transformedCond.makeResultVarDecl(false);
            
            if (resultVarDecl != null) {
                varDecls.append(resultVarDecl);
                stmts = stmts.prepend(transformedCond.makeResultVarAssignment());
                
            }
            List<JCStatement> assignDefault = List.<JCStatement>nil();
            for (Cond unassigned : unassignedResultVars) {
                assignDefault = assignDefault.append(unassigned.makeResultVarDefaultAssignment());    
            }
            stmts = List.<JCStatement>of(make().If(
                    transformedCond.makeTest(), 
                    make().Block(0, stmts), 
                    assignDefault.isEmpty() ? null : make().Block(0, assignDefault)));
            return stmts;
        }
        
        public List<JCStatement> getResult(Tree.Block elsePart) {
            List<JCStatement> stmts = transformList(conditions);
            ListBuffer<JCStatement> result = ListBuffer.lb();
            result.append(makeVar(ifVar, make().Type(syms().booleanType), makeBoolean(false)));
            result.appendList(varDecls);
            result.appendList(stmts);
            result.append(make().If(ifVar.makeIdent(), thenBlock, StatementTransformer.this.transform(elsePart)));
            return result.toList();   
        }
    }
    
    List<JCStatement> transform(Tree.IfStatement stmt) {
        Tree.Block thenPart = stmt.getIfClause().getBlock();
        Tree.Block elsePart = stmt.getElseClause() != null ? stmt.getElseClause().getBlock() : null;
        
        java.util.List<Condition> conditions = stmt.getIfClause().getConditionList().getConditions();
        if (conditions.size() == 1) {
            return transformSimpleIf(thenPart, elsePart, conditions);
        } else {
            return new IfCondList(conditions, thenPart).getResult(elsePart);
        }
        
    }

    private List<JCStatement> transformSimpleIf(Tree.Block thenPart, Tree.Block elsePart,
            java.util.List<Condition> conditions) {
        ListBuffer<JCStatement> result = ListBuffer.lb();
        final Cond transformedCond = transformedCondition(conditions.get(0), thenPart);
        JCStatement cond1 = make().If(transformedCond.makeTest(), transformedCond.makeThenBlock(), transform(elsePart));
        if (transformedCond.makeTestVarDecl(false) != null) {
            result.append(transformedCond.makeTestVarDecl(false));
        }
        result.append(cond1);
        return result.toList();
    }

    List<JCStatement> transform(Tree.WhileStatement stmt) {
        Name tempForFailVariable = currentForFailVariable;
        currentForFailVariable = null;
        final List<JCStatement> res;
        Tree.Block thenPart = stmt.getWhileClause().getBlock();
        java.util.List<Condition> conditions = stmt.getWhileClause().getConditionList().getConditions();
        if (conditions.size() == 1) {
            final Cond transformedCond = transformedCondition(conditions.get(0), thenPart);
            JCStatement cond1 = make().WhileLoop(makeLetExpr(make().TypeIdent(TypeTags.BOOLEAN), transformedCond.makeTest()), transformedCond.makeThenBlock());
            if (transformedCond.makeTestVarDecl(false) != null) {
                res = List.<JCStatement> of(transformedCond.makeTestVarDecl(false), cond1);
            } else {
                res = List.<JCStatement> of(cond1);
            }
        } else {
            List<JCStatement> loopBody = new WhileCondList(conditions, thenPart).getResult();
            JCStatement cond1 = make().WhileLoop(makeBoolean(true), 
                    make().Block(0, loopBody));
            res = List.<JCStatement> of(cond1);
            
        }
        currentForFailVariable = tempForFailVariable;
        
        return res;
    }
    
    class WhileCondList extends CondList {

        final ListBuffer<JCStatement> varDecls = ListBuffer.lb();
        final SyntheticName ifVar = naming.temp("while");
        
        public WhileCondList(java.util.List<Condition> conditions, Block thenPart) {
            super(conditions, thenPart);
        }     

        @Override
        protected List<JCStatement> transformInnermost(Condition condition) {
            Cond transformedCond = transformedCondition(condition, thenPart);
            JCBlock thenBlock = transformedCond.makeThenBlock();
            List<JCStatement> stmts = List.<JCStatement>of(
                    //make().Exec(make().Assign(ifVar.makeIdent(), makeBoolean(true))), 
                    thenBlock);
            return transformCommon(transformedCond, thenBlock.getStatements());
        }
        
        protected List<JCStatement> transformIntermediate(Condition condition, java.util.List<Condition> rest) {
            return transformCommon(transformedCondition(condition, null), transformList(rest));
        }
        
        protected List<JCStatement> transformCommon(Cond transformedCond, List<JCStatement> stmts) {
            if (transformedCond.makeTestVarDecl(true) != null) {
                varDecls.append(transformedCond.makeTestVarDecl(true));
            }
            JCVariableDecl resultVarDecl = transformedCond.makeResultVarDecl(false);
            
            if (resultVarDecl != null) {
                varDecls.append(resultVarDecl);
                stmts = stmts.prepend(transformedCond.makeResultVarAssignment());
            }
            
            stmts = List.<JCStatement>of(make().If(
                    transformedCond.makeTest(), 
                    make().Block(0, stmts), 
                    make().Break(null)));
            return stmts;
        }
        
        public List<JCStatement> getResult() {
            List<JCStatement> stmts = transformList(conditions);
            ListBuffer<JCStatement> result = ListBuffer.lb();
            result.append(makeVar(ifVar, make().Type(syms().booleanType), makeBoolean(false)));
            result.appendList(varDecls);
            result.appendList(stmts);
            //result.append(make().If(ifVar.makeIdent(), thenBlock, StatementTransformer.this.transform(elsePart)));
            return result.toList();   
        }
    }

    interface Cond {

        public JCVariableDecl makeResultVarDecl(boolean init);
        
        public boolean hasResultDecl();

        public JCStatement makeResultVarDefaultAssignment();

        public JCStatement makeResultVarAssignment();

        public JCStatement makeTestVarDecl(boolean init);

        public JCExpression makeTest();
        
        public JCBlock makeThenBlock();
    }
    
    abstract class SpecialFormCond<C extends Tree.Condition> implements Cond {
        protected final C cond;
        protected final Tree.Block thenPart;
        protected final String name;
        protected final ProducedType toType;
        protected final Expression specifierExpr;
        protected final Naming.SyntheticName tmpVarName;
        protected final Tree.Variable variable;
        SpecialFormCond(
                C cond,
                Tree.Block thenPart, 
                String name, 
                ProducedType toType, 
                Expression specifierExpr, 
                Naming.SyntheticName tmpVarName, Tree.Variable variable) {
            this.cond = cond;
            this.thenPart = thenPart;
            this.name = name;
            this.toType = toType;
            this.specifierExpr = specifierExpr;
            this.tmpVarName = tmpVarName;
            this.variable = variable;
        }
        
        @Override
        public final boolean hasResultDecl() {
            return !(isNothing(toType) 
                    || thenPart != null 
                    || variable.getType() instanceof Tree.SyntheticVariable);
        }
        
        protected JCExpression makeTypeExpr() {
            return makeJavaType(toType);
        }
        
        /** 
         * Generates an assigment of the result var to it's 'refined' value
         */
        @Override
        public final JCStatement makeResultVarAssignment() {
            return make().Exec(make().Assign(makeResultVarName(), makeResultExpr()));   
        }

        /** 
         * Generates an assigment of the result var to an appropriate 
         * default value (which is only used to satisfy Java's definite 
         * assignment analysis)
         */
        @Override
        public final JCStatement makeResultVarDefaultAssignment() {
            at(cond);
            return make().Exec(make().Assign(makeResultVarName(), makeDefaultExpr()));   
        }
        
        protected abstract JCExpression makeDefaultExpr();

        protected final JCExpression makeResultVarName() {
            at(cond);
            return makeUnquotedIdent(name);
        }
        
        @Override
        public JCBlock makeThenBlock() {
            at(cond);
            if (thenPart == null) {
                return transform(thenPart);   
            }
            Name substVarName = naming.aliasName(name);
            // Prepare for variable substitution in the following code block
            final String origVarName = name;
            String prevSubst = naming.addVariableSubst(origVarName, substVarName.toString());
            List<JCStatement> blockStmts = transformBlock(thenPart);
            // The variable holding the result for the code inside the code block
            blockStmts = blockStmts.prepend(at(cond).VarDef(make().Modifiers(FINAL), substVarName, makeTypeExpr(), makeResultExpr()));
            JCBlock thenBlock = at(cond).Block(0, blockStmts);
            // Deactivate the above variable substitution
            naming.removeVariableSubst(origVarName, prevSubst);
            return thenBlock;
        }

        protected abstract JCExpression makeResultExpr();
        
        @Override
        public JCStatement makeTestVarDecl(boolean init) {
            // Temporary variable holding the result of the expression/variable to test
            return makeVar(tmpVarName, makeResultType(), init ? makeNull() : null);
        }

        protected abstract JCExpression makeResultType();
        
        @Override
        public final JCVariableDecl makeResultVarDecl(boolean init) {
            if (!hasResultDecl()) {
                return null;
            }
            at(cond);
            return at(cond).VarDef(make().Modifiers(FINAL), 
                    names().fromString(name), 
                    makeTypeExpr(), init ? makeResultExpr() : null);
        }
    }
    
    class IsCond extends SpecialFormCond<Tree.IsCondition> {
        
        IsCond(Tree.IsCondition isdecl, Tree.Block thenPart) {
            super(isdecl, thenPart, 
                    isdecl.getVariable().getIdentifier().getText(),
                    // use the type of the variable, which is more precise than the type we test for
                    isdecl.getVariable().getType().getTypeModel(), 
                    isdecl.getVariable().getSpecifierExpression().getExpression(),
                    isNothing(isdecl.getVariable().getType().getTypeModel()) ? null 
                            : naming.alias(isdecl.getVariable().getIdentifier().getText()),
                    isdecl.getVariable());
        }
        
        protected JCExpression makeTypeExpr() {
            return isNothing(toType) ? null : super.makeTypeExpr();
        }
        
        @Override
        public JCExpression makeTest() {
         // no need to cast for erasure here
            JCExpression expr = expressionGen().transformExpression(specifierExpr);
            at(cond);
            if (isNothing(toType)) {
                return make().Binary(JCTree.EQ, expr, makeNull());
            } else {
                // Assign the expression to test to the temporary variable
                JCExpression firstTimeTestExpr = make().Assign(tmpVarName.makeIdent(), expr);
                
                // Test on the tmpVar in the following condition
                return makeTypeTest(firstTimeTestExpr, tmpVarName,
                        // only test the types we're testing for, not the type of
                        // the variable (which can be more precise)
                        cond.getType().getTypeModel());
            }
        }
        
        @Override
        protected JCExpression makeResultType() {
            at(cond);
            return make().Type(syms().objectType);
        }
        
        @Override
        protected JCExpression makeDefaultExpr() {
            at(cond);
            return canUnbox(toType) ? makeLong(0) : makeNull();
        }
        
        @Override
        protected JCExpression makeResultExpr() {
            at(cond);
            // Want raw type for instanceof since it can't be used with generic types
            JCExpression rawToTypeExpr = makeJavaType(toType, JT_NO_PRIMITIVES | JT_RAW);
            // Substitute variable with the correct type to use in the rest of the code block
            JCExpression castedExpr = at(cond).TypeCast(rawToTypeExpr, tmpVarName.makeIdent());
            if (canUnbox(toType)) {
                return unboxType(castedExpr, toType);
            } 
            return castedExpr;
        }
        
        @Override
        public JCBlock makeThenBlock() {
            if (isNothing(toType)) {
                return transform(thenPart);   
            }
            return super.makeThenBlock();
        }
        
        @Override
        public JCStatement makeTestVarDecl(boolean init) {
            if (isNothing(toType)) {
                return null;
            }
            return super.makeTestVarDecl(init);
        }
    }
    
    class ExistsCond extends SpecialFormCond<Tree.ExistsCondition> {

        public ExistsCond(Tree.ExistsCondition exists, Tree.Block thenPart) {
            super(exists, thenPart, exists.getVariable().getIdentifier().getText(), 
                    simplifyType(exists.getVariable().getType().getTypeModel()),
                    exists.getVariable().getSpecifierExpression().getExpression(), 
                    naming.alias(exists.getVariable().getIdentifier().getText()),
                    exists.getVariable());    
        }
        
        @Override
        protected JCExpression makeResultExpr() {
            return unboxType(tmpVarName.makeIdent(), toType);
        }
        
        @Override
        protected JCExpression makeResultType() {
            ProducedType tmpVarType = specifierExpr.getTypeModel();
            return makeJavaType(tmpVarType, JT_NO_PRIMITIVES);
        }
        
        @Override
        protected JCExpression makeDefaultExpr() {
            return makeLong(0);
        }

        @Override
        public JCExpression makeTest() {
            // no need to cast for erasure here
            JCExpression expr = expressionGen().transformExpression(specifierExpr);
            at(cond);
            // Assign the expression to test to the temporary variable
            JCExpression firstTimeTestExpr = make().Assign(tmpVarName.makeIdent(), expr);
            // Test on the tmpVar in the following condition
            return make().Binary(JCTree.NE, firstTimeTestExpr, makeNull());
        }
    }
    
    class NonemptyCond extends SpecialFormCond<Tree.NonemptyCondition> {

        public NonemptyCond(Tree.NonemptyCondition nonempty, Tree.Block thenPart) {
            super(nonempty, thenPart, 
                    nonempty.getVariable().getIdentifier().getText(), 
                    nonempty.getVariable().getType().getTypeModel(), 
                    nonempty.getVariable().getSpecifierExpression().getExpression(),
                    naming.alias(nonempty.getVariable().getIdentifier().getText()),
                    nonempty.getVariable());
        }
        
        @Override
        protected JCExpression makeDefaultExpr() {
            return makeNull();
        }
        
        @Override
        protected JCExpression makeResultExpr() {
            return make().TypeCast(makeTypeExpr(), tmpVarName.makeIdent());
        }
        
        @Override
        protected JCExpression makeResultType() {
            ProducedType tmpVarType = specifierExpr.getTypeModel();
            return makeJavaType(tmpVarType, JT_NO_PRIMITIVES);
        }

        @Override
        public JCExpression makeTest() {
            // no need to cast for erasure here
            JCExpression expr = expressionGen().transformExpression(specifierExpr);
            at(cond);
            // Assign the expression to test to the temporary variable
            JCExpression firstTimeTestExpr = make().Assign(tmpVarName.makeIdent(), expr);
            // Test on the tmpVar in the following condition
            return makeNonEmptyTest(firstTimeTestExpr, tmpVarName);
        }
    }
    
    class BooleanCond implements Cond {
        private final BooleanCondition cond;
        private final Block thenPart;
        
        public BooleanCond(Tree.BooleanCondition booleanCondition, Tree.Block thenPart) {
            super();
            this.cond = booleanCondition;
            this.thenPart = thenPart;
        }
        
        @Override
        public JCBlock makeThenBlock() {
            return transform(thenPart);
        }

        @Override
        public JCStatement makeResultVarDefaultAssignment() {
            return null;
        }

        @Override
        public JCStatement makeResultVarAssignment() {
            return null;
        }

        @Override
        public JCVariableDecl makeResultVarDecl(boolean init) {
            return null;
        }

        @Override
        public JCStatement makeTestVarDecl(boolean init) {
            return null;
        }

        @Override
        public JCExpression makeTest() {
            at(cond);
            // booleans can't be erased
            return expressionGen().transformExpression(cond.getExpression(), 
                    BoxingStrategy.UNBOXED, null);
        }

        @Override
        public boolean hasResultDecl() {
            return false;
        }
    }
    
    Cond transformedCondition(Tree.Condition cond, Tree.Block thenPart) {
        if (cond instanceof Tree.IsCondition) {
            return new IsCond((Tree.IsCondition)cond, thenPart);
        } else if (cond instanceof Tree.ExistsCondition) {
            return new ExistsCond((Tree.ExistsCondition)cond, thenPart);
        } else if (cond instanceof Tree.NonemptyCondition) {
            return new NonemptyCond((Tree.NonemptyCondition)cond, thenPart);
        } else if (cond instanceof Tree.BooleanCondition) {
            return new BooleanCond((Tree.BooleanCondition)cond, thenPart);
        }
        throw new RuntimeException();
    }
    
    List<JCStatement> transform(Tree.Assertion ass) {
        ListBuffer<JCStatement> rval = new ListBuffer<JCStatement>();
        java.util.List<Condition> conditions = ass.getConditionList().getConditions();
        if (conditions.size() == 1) {
            final Cond transformedCond = transformedCondition(conditions.get(0), null);
            if (transformedCond.makeTestVarDecl(false) != null) {
                rval.add(transformedCond.makeTestVarDecl(false));
            }
            //Get the custom message
            String message = buildAssertionMessage(ass);
            
            //TODO proper exception type, include expression, etc
            JCExpression t = make().NewClass(null, null,
                    makeIdent(syms().ceylonExceptionType),
                    List.<JCExpression>of(boxType(make().Literal(message), typeFact().getStringDeclaration().getType()), makeNull()),
                    null);
            rval.add(make().If(make().Unary(JCTree.NOT, transformedCond.makeTest()), make().Throw(t), null));
            if (transformedCond.makeResultVarDecl(true) != null) {
                rval.add(transformedCond.makeResultVarDecl(true));
            }
        } else {
            
        }
        return rval.toList();
    }

    private String buildAssertionMessage(Tree.Assertion ass) {
        StringBuilder sb = new StringBuilder();
        sb.append("Assertion failed");
        String docText = getDocAnnotationText(ass);
        if (docText != null) {
            sb.append(": ").append(docText);
        }
        sb.append("\n");
        String location = ass.getUnit().getFilename() + ":" + ass.getLocation();
        sb.append("\tat ").append(location).append("\n");
        String custom = sb.toString();
        return custom;
    }

    private String getDocAnnotationText(Tree.Assertion ass) {
        String docText = null;
        Tree.Annotation doc = getAnnotation(ass.getAnnotationList(), "doc");
        if (doc != null) {
            Tree.Expression expression = null;
            if (doc.getPositionalArgumentList() != null) {
                Tree.PositionalArgument arg = doc.getPositionalArgumentList().getPositionalArguments().get(0);
                expression = arg.getExpression();
            } else if (doc.getNamedArgumentList() != null) {
                Tree.SpecifiedArgument arg = (Tree.SpecifiedArgument)doc.getNamedArgumentList().getNamedArguments().get(0);
                expression = arg.getSpecifierExpression().getExpression();
            }
            Tree.Literal literal = (Tree.Literal)expression.getTerm();
            docText = literal.getText();
        }
        return docText;
    }

    private Tree.Annotation getAnnotation(Tree.AnnotationList al, String name) {
        if (al!=null) {
            for (Tree.Annotation a: al.getAnnotations()) {
                Tree.BaseMemberExpression p = (Tree.BaseMemberExpression) a.getPrimary();
                if (p!=null) {
                    if ( p.getIdentifier().getText().equals(name) ) {
                        return a;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Gets the source code corresponding to the given node
     */
    private String getSourceCode(Node node) {
        StringBuilder sb = new StringBuilder();
        DiagnosticSource source = new DiagnosticSource(gen().getFileObject(), Log.instance(gen().getContext()));
        int startLine = node.getToken().getLine();
        int endLine = node.getEndToken().getLine();
        for (int lineNumber = startLine; lineNumber <= endLine; lineNumber++) {
            int startPos = gen().getMap().getPosition(lineNumber, 1);
            String line = source.getLine(startPos);
            if (lineNumber == endLine) {
                line = line.substring(0,  node.getEndToken().getCharPositionInLine() + node.getEndToken().getText().length());
            }
            if (lineNumber == startLine) {
                line = line.substring(node.getToken().getCharPositionInLine());
            }
            sb.append(line).append("\n");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private ProducedType actualType(Tree.TypedDeclaration decl) {
        return decl.getType().getTypeModel();
    }
    
    List<JCStatement> transform(Tree.ForStatement stmt) {
        Name tempForFailVariable = currentForFailVariable;
        
        at(stmt);
        List<JCStatement> outer = List.<JCStatement> nil();
        if (stmt.getExits() && stmt.getElseClause() != null) {
            // boolean $doforelse$X = true;
            JCVariableDecl failtest_decl = make().VarDef(make().Modifiers(0), naming.aliasName("doforelse"), make().TypeIdent(TypeTags.BOOLEAN), make().Literal(TypeTags.BOOLEAN, 1));
            outer = outer.append(failtest_decl);
            
            currentForFailVariable = failtest_decl.getName();
        } else {
            currentForFailVariable = null;
        }

        // java.lang.Object $elem$X;
        Naming.SyntheticName elem_name = naming.alias("elem");
        JCVariableDecl elem_decl = make().VarDef(make().Modifiers(0), elem_name.asName(), make().Type(syms().objectType), null);
        outer = outer.append(elem_decl);
        
        ForIterator iterDecl = stmt.getForClause().getForIterator();
        Variable variable;
        Variable variable2;
        if (iterDecl instanceof ValueIterator) {
            variable = ((ValueIterator) iterDecl).getVariable();
            variable2 = null;
        } else if (iterDecl instanceof KeyValueIterator) {
            variable = ((KeyValueIterator) iterDecl).getKeyVariable();
            variable2 = ((KeyValueIterator) iterDecl).getValueVariable();
        } else {
            throw new RuntimeException("Unknown ForIterator");
        }
        
        String loop_var_name = variable.getIdentifier().getText();
        Expression specifierExpression = iterDecl.getSpecifierExpression().getExpression();
        ProducedType sequence_element_type;
        if(variable2 == null)
            sequence_element_type = variable.getType().getTypeModel();
        else{
            // Entry<V1,V2>
            sequence_element_type = typeFact().getEntryType(variable.getType().getTypeModel(), 
                    variable2.getType().getTypeModel());
        }
        ProducedType iter_type = typeFact().getIteratorType(sequence_element_type);
        JCExpression iter_type_expr = makeJavaType(iter_type, CeylonTransformer.JT_TYPE_ARGUMENT);
        JCExpression cast_elem = at(stmt).TypeCast(makeJavaType(sequence_element_type, CeylonTransformer.JT_NO_PRIMITIVES), elem_name.makeIdent());
        List<JCAnnotation> annots = makeJavaTypeAnnotations(variable.getDeclarationModel());

        // ceylon.language.Iterator<T> $V$iter$X = ITERABLE.getIterator();
        // We don't need to unerase here as anything remotely a sequence will be erased to Iterable, which has getIterator()
        JCExpression containment = expressionGen().transformExpression(specifierExpression, BoxingStrategy.BOXED, null);
        JCExpression getIter = at(stmt).Apply(null, makeSelect(containment, "getIterator"), List.<JCExpression> nil());
        getIter = gen().expressionGen().applyErasureAndBoxing(getIter, specifierExpression.getTypeModel(), true, BoxingStrategy.BOXED, iter_type);
        JCVariableDecl iter_decl = at(stmt).VarDef(make().Modifiers(0), naming.aliasName(loop_var_name + "$iter"), iter_type_expr, getIter);
        String iter_id = iter_decl.getName().toString();
        
        // final U n = $elem$X;
        // or
        // final U n = $elem$X.getKey();
        JCExpression loop_var_init;
        ProducedType loop_var_type;
        if (variable2 == null) {
            loop_var_type = sequence_element_type;
            loop_var_init = cast_elem;
        } else {
            loop_var_type = actualType(variable);
            loop_var_init = at(stmt).Apply(null, makeSelect(cast_elem, Naming.getGetterName("key")), List.<JCExpression> nil());
        }
        JCVariableDecl item_decl = at(stmt).VarDef(make().Modifiers(FINAL, annots), names().fromString(loop_var_name), makeJavaType(loop_var_type), 
                boxUnboxIfNecessary(loop_var_init, true, loop_var_type, CodegenUtil.getBoxingStrategy(variable.getDeclarationModel())));
        List<JCStatement> for_loop = List.<JCStatement> of(item_decl);

        if (variable2 != null) {
            // final V n = $elem$X.getElement();
            ProducedType item_type2 = actualType(variable2);
            JCExpression item_type_expr2 = makeJavaType(item_type2);
            JCExpression loop_var_init2 = at(stmt).Apply(null, makeSelect(cast_elem, Naming.getGetterName("item")), List.<JCExpression> nil());
            String loop_var_name2 = variable2.getIdentifier().getText();
            JCVariableDecl item_decl2 = at(stmt).VarDef(make().Modifiers(FINAL, annots), names().fromString(loop_var_name2), item_type_expr2, 
                    boxUnboxIfNecessary(loop_var_init2, true, item_type2, CodegenUtil.getBoxingStrategy(variable2.getDeclarationModel())));
            for_loop = for_loop.append(item_decl2);
        }

        // The user-supplied contents of the loop
        for_loop = for_loop.appendList(transformStmts(stmt.getForClause().getBlock().getStatements()));
        
        // $elem$X = $V$iter$X.next()
        JCExpression iter_elem = make().Apply(null, makeSelect(iter_id, "next"), List.<JCExpression> nil());
        JCExpression elem_assign = make().Assign(elem_name.makeIdent(), iter_elem);
        // !(($elem$X = $V$iter$X.next()) instanceof Finished)
        JCExpression instof = make().TypeTest(elem_assign, make().Type(syms().ceylonFinishedType));
        JCExpression cond = make().Unary(JCTree.NOT, instof);

        // No step necessary
        List<JCExpressionStatement> step = List.<JCExpressionStatement>nil();
        
        // for (.ceylon.language.Iterator<T> $V$iter$X = ITERABLE.iterator(); !(($elem$X = $V$iter$X.next()) instanceof Finished); ) {
        outer = outer.append(at(stmt).ForLoop(
            List.<JCStatement>of(iter_decl), 
	        cond, 
	        step, 
	        at(stmt).Block(0, for_loop)));

        if (stmt.getElseClause() != null) {
            // The user-supplied contents of fail block
            List<JCStatement> failblock = transformStmts(stmt.getElseClause().getBlock().getStatements());
            
            if (stmt.getExits()) {
                // if ($doforelse$X) ...
                JCIdent failtest_id = at(stmt).Ident(currentForFailVariable);
                outer = outer.append(at(stmt).If(failtest_id, at(stmt).Block(0, failblock), null));
            } else {
                outer = outer.appendList(failblock);
            }
        }
        currentForFailVariable = tempForFailVariable;

        return outer;
    }

    // FIXME There is a similar implementation in ClassGen!
    public List<JCStatement> transform(AttributeDeclaration decl) {
        // If the attribute is really from a parameter then don't generate a local variable
        Parameter parameter = CodegenUtil.findParamForDecl(decl);
        if (parameter == null) {
            Name atrrName = names().fromString(decl.getIdentifier().getText());
            ProducedType t = actualType(decl);
            
            JCExpression initialValue = null;
            if (decl.getSpecifierOrInitializerExpression() != null) {
                initialValue = expressionGen().transformExpression(decl.getSpecifierOrInitializerExpression().getExpression(), 
                        CodegenUtil.getBoxingStrategy(decl.getDeclarationModel()), 
                        decl.getDeclarationModel().getType());
            }
    
            JCExpression type = makeJavaType(t);
            List<JCAnnotation> annots = makeJavaTypeAnnotations(decl.getDeclarationModel());
    
            int modifiers = transformLocalFieldDeclFlags(decl);
            return List.<JCStatement> of(at(decl).VarDef(at(decl).Modifiers(modifiers, annots), atrrName, type, initialValue));
        } else {
            return List.<JCStatement> nil();
        }
    }
    
    List<JCStatement> transform(Tree.Break stmt) {
        // break;
        JCStatement brk = at(stmt).Break(null);
        
        if (currentForFailVariable != null) {
            JCIdent failtest_id = at(stmt).Ident(currentForFailVariable);
            List<JCStatement> list = List.<JCStatement> of(at(stmt).Exec(at(stmt).Assign(failtest_id, make().Literal(TypeTags.BOOLEAN, 0))));
            list = list.append(brk);
            return list;
        } else {
            return List.<JCStatement> of(brk);
        }
    }

    JCStatement transform(Tree.Continue stmt) {
        // continue;
        return at(stmt).Continue(null);
    }

    JCStatement transform(Tree.Return ret) {
        Tree.Expression expr = ret.getExpression();
        JCExpression returnExpr = null;
        at(ret);
        if (expr != null) {
            boolean prevNoExpressionlessReturn = noExpressionlessReturn;
            try {
                noExpressionlessReturn = false;
                // we can cast to TypedDeclaration here because return with expressions are only in Method or Value
                TypedDeclaration declaration = (TypedDeclaration)ret.getDeclaration();
                // make sure we use the best declaration for boxing and type
                ProducedTypedReference typedRef = getTypedReference(declaration);
                ProducedTypedReference nonWideningTypedRef = nonWideningTypeDecl(typedRef);
                ProducedType nonWideningType = nonWideningType(typedRef, nonWideningTypedRef);
                returnExpr = expressionGen().transformExpression(expr.getTerm(), 
                        CodegenUtil.getBoxingStrategy(nonWideningTypedRef.getDeclaration()),
                        nonWideningType);
            } finally {
                noExpressionlessReturn = prevNoExpressionlessReturn;
            }
        } else if (noExpressionlessReturn) {
            returnExpr = makeNull();
        }
        return at(ret).Return(returnExpr);
    }

    public JCStatement transform(Throw t) {
        at(t);
        Expression expr = t.getExpression();
        final JCExpression exception;
        if (expr == null) {// bare "throw;" stmt
            exception = make().NewClass(null, null,
                    makeIdent(syms().ceylonExceptionType),
                    List.<JCExpression>of(makeNull(), makeNull()),
                    null);
        } else {
            // we must unerase the exception to Throwable
            ProducedType exceptionType = expr.getTypeModel().getSupertype(t.getUnit().getExceptionDeclaration());
            exception = gen().expressionGen().transformExpression(expr, BoxingStrategy.UNBOXED, exceptionType);
        }
        return make().Throw(exception);
    }
    
    public JCStatement transform(TryCatchStatement t) {
        // TODO Support resources -- try(Usage u = ...) { ...
        TryClause tryClause = t.getTryClause();
        at(tryClause);
        JCBlock tryBlock = transform(tryClause.getBlock());

        final ListBuffer<JCCatch> catches = ListBuffer.<JCCatch>lb();
        for (CatchClause catchClause : t.getCatchClauses()) {
            at(catchClause);
            java.util.List<ProducedType> exceptionTypes;
            Variable variable = catchClause.getCatchVariable().getVariable();
            ProducedType exceptionType = variable.getDeclarationModel().getType();
            if (typeFact().isUnion(exceptionType)) {
                exceptionTypes = exceptionType.getDeclaration().getCaseTypes();
            } else {
                exceptionTypes = List.<ProducedType>of(exceptionType);
            }
            for (ProducedType type : exceptionTypes) {
                // catch blocks for each exception in the union
                JCVariableDecl param = make().VarDef(make().Modifiers(Flags.FINAL), names().fromString(variable.getIdentifier().getText()),
                        makeJavaType(type, JT_CATCH), null);
                catches.add(make().Catch(param, transform(catchClause.getBlock())));
            }
        }

        final JCBlock finallyBlock;
        FinallyClause finallyClause = t.getFinallyClause();
        if (finallyClause != null) {
            at(finallyClause);
            finallyBlock = transform(finallyClause.getBlock());
        } else {
            finallyBlock = null;
        }

        return at(t).Try(tryBlock, catches.toList(), finallyBlock);
    }

    private int transformLocalFieldDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= cdecl.getDeclarationModel().isVariable() ? 0 : FINAL;

        return result;
    }
    
    /**
     * Transforms a Ceylon switch to a Java {@code if/else if} chain.
     * @param stmt The Ceylon switch
     * @return The Java tree
     */
    JCStatement transform(SwitchStatement stmt) {

        SwitchClause switchClause = stmt.getSwitchClause();
        JCExpression selectorExpr = expressionGen().transformExpression(switchClause.getExpression(), BoxingStrategy.BOXED, switchClause.getExpression().getTypeModel());
        Naming.SyntheticName selectorAlias = naming.alias("sel");
        JCVariableDecl selector = makeVar(selectorAlias, make().Type(syms().objectType), selectorExpr);
        SwitchCaseList caseList = stmt.getSwitchCaseList();

        JCStatement last = null;
        ElseClause elseClause = caseList.getElseClause();
        if (elseClause != null) {
            last = transform(elseClause.getBlock());
        } else {
            // To avoid possible javac warnings about uninitialized vars we
            // need to have an 'else' clause, even if the ceylon code doesn't
            // require one. 
            // This exception could be thrown for example if an enumerated 
            // type is recompiled after having a subclass added, but the 
            // switch is not recompiled.
            last = make().Throw(
                        make().NewClass(null, List.<JCExpression>nil(), 
                                makeIdent(syms().ceylonEnumeratedTypeErrorType), 
                                List.<JCExpression>of(make().Literal(
                                        "Supposedly exhaustive switch was not exhaustive")), null));
        }

        java.util.List<CaseClause> caseClauses = caseList.getCaseClauses();
        for (int ii = caseClauses.size() - 1; ii >= 0; ii--) {// reverse order
            CaseClause caseClause = caseClauses.get(ii);
            CaseItem caseItem = caseClause.getCaseItem();
            if (caseItem instanceof IsCase) {
                last = transformCaseIs(selectorAlias, caseClause, (IsCase)caseItem, last);
            } else if (caseItem instanceof SatisfiesCase) {
                // TODO Support for 'case (satisfies ...)' is not implemented yet
                return make().Exec(makeErroneous(caseItem, "switch/satisfies not implemented yet"));
            } else if (caseItem instanceof MatchCase) {
                last = transformCaseMatch(selectorAlias, caseClause, (MatchCase)caseItem, last);
            } else {
                return make().Exec(makeErroneous(caseItem, "unknown switch case clause: "+caseItem));
            }
        }
        return at(stmt).Block(0, List.of(selector, last));
    }

    private JCStatement transformCaseMatch(Naming.SyntheticName selectorAlias, CaseClause caseClause, MatchCase matchCase, JCStatement last) {
        at(matchCase);
        
        JCExpression tests = null;
        java.util.List<Tree.Expression> expressions = matchCase.getExpressionList().getExpressions();
        for(Tree.Expression expr : expressions){
            JCExpression transformedExpression = expressionGen().transformExpression(expr);
            JCBinary test = make().Binary(JCTree.EQ, selectorAlias.makeIdent(), transformedExpression);
            if(tests == null)
                tests = test;
            else
                tests = make().Binary(JCTree.OR, tests, test);
        }
        return at(caseClause).If(tests, transform(caseClause.getBlock()), last);
    }

    /**
     * Transform a "case(is ...)"
     * @param selectorAlias
     * @param caseClause
     * @param isCase
     * @param last
     * @return
     */
    private JCStatement transformCaseIs(Naming.SyntheticName selectorAlias,
            CaseClause caseClause, IsCase isCase, JCStatement last) {
        at(isCase);
        ProducedType type = isCase.getType().getTypeModel();
        JCExpression cond = makeTypeTest(null, selectorAlias, type);
        
        JCExpression toTypeExpr = makeJavaType(type);
        String name = isCase.getVariable().getIdentifier().getText();

        Naming.SyntheticName tmpVarName = selectorAlias;
        Name substVarName = naming.aliasName(name);

        // Want raw type for instanceof since it can't be used with generic types
        JCExpression rawToTypeExpr = makeJavaType(type, JT_NO_PRIMITIVES | JT_RAW);

        // Substitute variable with the correct type to use in the rest of the code block
        JCExpression tmpVarExpr = tmpVarName.makeIdent();

        tmpVarExpr = unboxType(at(isCase).TypeCast(rawToTypeExpr, tmpVarExpr), type);
        
        // The variable holding the result for the code inside the code block
        JCVariableDecl decl2 = at(isCase).VarDef(make().Modifiers(FINAL), substVarName, toTypeExpr, tmpVarExpr);

        // Prepare for variable substitution in the following code block
        String prevSubst = naming.addVariableSubst(name, substVarName.toString());

        JCBlock block = transform(caseClause.getBlock());
        List<JCStatement> stats = List.<JCStatement> of(decl2);
        stats = stats.appendList(block.getStatements());
        block = at(isCase).Block(0, stats);

        // Deactivate the above variable substitution
        naming.removeVariableSubst(name, prevSubst);

        last = make().If(cond, block, last);
        return last;
    }
}
