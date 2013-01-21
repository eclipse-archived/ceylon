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

import java.util.Collections;
import java.util.LinkedHashMap;

import com.redhat.ceylon.compiler.java.codegen.Naming.CName;
import com.redhat.ceylon.compiler.java.codegen.Naming.SubstitutedName;
import com.redhat.ceylon.compiler.java.codegen.Naming.Substitution;
import com.redhat.ceylon.compiler.java.codegen.Naming.SyntheticName;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
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
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SatisfiesCase;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SwitchCaseList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SwitchClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SwitchStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Throw;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TryCatchStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.TryClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ValueIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCBreak;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCForLoop;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCThrow;
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

    private JCThrow makeThrowException(Type exceptionType, JCExpression expr) {
        JCExpression exception = make().NewClass(null, null,
                makeIdent(exceptionType),
                List.<JCExpression>of(boxType(expr, typeFact().getStringDeclaration().getType()), makeNull()),
                null);
        return make().Throw(exception);
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
        
        public abstract List<JCStatement> getResult();
    }
    
    abstract class BlockCondList extends CondList {

        public BlockCondList(java.util.List<Condition> conditions,
                Block thenPart) {
            super(conditions, thenPart);
        }
        
        @Override
        protected final List<JCStatement> transformInnermost(Condition condition) {
            Cond transformedCond = transformCondition(condition, thenPart);
            // Note: The innermost test happens outside the substitution scope
            JCExpression test = transformedCond.makeTest();
            java.util.List<Condition> rest = Collections.<Condition>emptyList();
            JCStatement elseBlock = transformInnermostElse(transformedCond, rest);
            Substitution subs = getSubstitution(transformedCond);
            List<JCStatement> stmts = transformInnermostThen(transformedCond);
            stmts = transformCommon(transformedCond, rest, test, 
                    stmts, elseBlock);
            if (subs != null) {
                subs.close();
            }
            return stmts;
        }
        
        protected abstract List<JCStatement> transformInnermostThen(Cond transformedCond);

        protected abstract JCStatement transformInnermostElse(Cond transformedCond, java.util.List<Condition> rest);

        protected Substitution getSubstitution(Cond cond) {
            Substitution subs;
            if (cond.hasResultDecl()) {
                subs = naming.substituteAlias(cond.getVariable().getDeclarationModel());
            } else {
                subs = null;
            }
            return subs;
        }
        
        @Override
        protected List<JCStatement> transformIntermediate(Condition condition, java.util.List<Condition> rest) {
            Cond intermediate = transformCondition(condition, null);
            JCExpression test = intermediate.makeTest();
            Substitution subs = getSubstitution(intermediate);
            List<JCStatement> stmts = transformList(rest);
            JCStatement intermediateElse = transformIntermediateElse(intermediate, rest);
            stmts = transformCommon(intermediate, rest, test, 
                    stmts, intermediateElse);
            if (subs != null) {
                subs.close();
            }
            return stmts;
        }

        protected abstract JCStatement transformIntermediateElse(Cond transformedCond, java.util.List<Condition> rest);
        
        protected abstract List<JCStatement> transformCommon(Cond transformedCond, java.util.List<Condition> rest, JCExpression test, List<JCStatement> stmts, JCStatement elseBlock);
    }
    
    class IfCondList extends BlockCondList {

        final ListBuffer<JCStatement> varDecls = ListBuffer.lb();
        final SyntheticName ifVar = naming.temp("if");
        private LinkedHashMap<Cond, CName> unassignedResultVars = new LinkedHashMap<Cond, CName>();
        private JCBlock thenBlock;
        private Block elsePart;
        
        public IfCondList(java.util.List<Condition> conditions, Block thenPart,
                Block elsePart) {
            super(conditions, thenPart);
            this.elsePart = elsePart;
        }     

        /** 
         * If the "if" statement has > 1 condition and an else block, then 
         * we need to use one set of nested "if"s to do the narrowing and 
         * determine whether the overall condition is satisfied, and a separate 
         * if/else block to actually hold the transformed blocks.
         */
        private boolean isDeferred() {
            return conditions.size() > 1 && elsePart != null;
        }
        
        @Override
        protected JCBreak transformIntermediateElse(Cond transformedCond, java.util.List<Condition> rest) {
            return null;
        }

        @Override
        protected List<JCStatement> transformInnermostThen(Cond transformedCond) {
            List<JCStatement> stmts;
            if (isDeferred()) {
                stmts = List.<JCStatement>of(make().Exec(make().Assign(ifVar.makeIdent(), makeBoolean(true))));
                thenBlock = makeThenBlock(transformedCond, thenPart, null);
            } else {
                stmts = makeThenBlock(transformedCond, thenPart, null).getStatements();   
            }
            return stmts;
        }

        @Override
        protected JCStatement transformInnermostElse(Cond transformedCond, java.util.List<Condition> rest) {
            JCBlock elseBlock = null;
            if (!isDeferred()) {
                elseBlock = transform(this.elsePart);
            }
            return elseBlock;
        }
        
        @Override
        protected List<JCStatement> transformCommon(Cond transformedCond, 
                java.util.List<Condition> rest, JCExpression test, List<JCStatement> stmts, JCStatement elseBlock) {
            JCStatement testVarDecl = transformedCond.makeTestVarDecl(0, false);
            if (testVarDecl != null) {
                varDecls.append(testVarDecl);
            }
            if (transformedCond.hasResultDecl()) {
                JCVariableDecl resultVarDecl = make().VarDef(make().Modifiers(Flags.FINAL), 
                        transformedCond.getVariableName().asName(), 
                        transformedCond.makeTypeExpr(), 
                        isDeferred() ? null : transformedCond.makeResultExpr());
                if (isDeferred()) {
                    // Note we capture the substitution here, because it won't be 
                    // in scope when we generate the default assignment branches.
                    unassignedResultVars.put(transformedCond, 
                            transformedCond.getVariableName().capture());
                    varDecls.append(resultVarDecl);
                    stmts = stmts.prepend(make().Exec(make().Assign(transformedCond.getVariableName().makeIdent(), transformedCond.makeResultExpr())));
                } else {
                    stmts = stmts.prepend(resultVarDecl);
                }
            }
            JCStatement elsePart;
            if (isDeferred()) {
                List<JCStatement> assignDefault = List.<JCStatement>nil();
                for (Cond unassigned : unassignedResultVars.keySet()) {
                    assignDefault = assignDefault.append(
                            make().Exec(make().Assign(unassignedResultVars.get(unassigned).makeIdent(), 
                            ((SpecialFormCond)unassigned).makeDefaultExpr())));
                }
                elsePart = assignDefault.isEmpty() ? null : make().Block(0, assignDefault);
            } else {
                elsePart = elseBlock;
            }
            stmts = List.<JCStatement>of(make().If(
                    test, 
                    make().Block(0, stmts), 
                    elsePart));
            return stmts;
        }
        
        @Override
        public List<JCStatement> getResult() {
            List<JCStatement> stmts = transformList(conditions);
            ListBuffer<JCStatement> result = ListBuffer.lb();
            if (isDeferred()) {
                result.append(makeVar(ifVar, make().Type(syms().booleanType), makeBoolean(false)));
            }
            result.appendList(varDecls);
            result.appendList(stmts);
            if (isDeferred()) {
                result.append(make().If(ifVar.makeIdent(), thenBlock, StatementTransformer.this.transform(elsePart)));
            }
            return result.toList();   
        } 
    }
    
    List<JCStatement> transform(Tree.IfStatement stmt) {
        Tree.Block thenPart = stmt.getIfClause().getBlock();
        Tree.Block elsePart = stmt.getElseClause() != null ? stmt.getElseClause().getBlock() : null;
        java.util.List<Condition> conditions = stmt.getIfClause().getConditionList().getConditions();
        return new IfCondList(conditions, thenPart, elsePart).getResult();    
    }

    private JCBlock makeThenBlock(Cond cond, Block thenPart, Substitution subs) {
        List<JCStatement> blockStmts = statementGen().transformBlock(thenPart);
        if (subs != null) {
            // The variable holding the result for the code inside the code block
            blockStmts = blockStmts.prepend(at(cond.getCondition()).VarDef(make().Modifiers(FINAL), names().fromString(subs.substituted), 
                    cond.makeTypeExpr(), cond.makeResultExpr()));
        }
        JCBlock thenBlock = at(cond.getCondition()).Block(0, blockStmts);
        return thenBlock;
    }
    
    List<JCStatement> transform(Tree.WhileStatement stmt) {
        Name tempForFailVariable = currentForFailVariable;
        currentForFailVariable = null;
        final List<JCStatement> res;
        Tree.Block thenPart = stmt.getWhileClause().getBlock();
        java.util.List<Condition> conditions = stmt.getWhileClause().getConditionList().getConditions();
        res =  new WhileCondList(conditions, thenPart).getResult();    
        currentForFailVariable = tempForFailVariable;
        
        return res;
    }
    
    class WhileCondList extends BlockCondList {

        private final ListBuffer<JCStatement> varDecls = ListBuffer.lb();
        public WhileCondList(java.util.List<Condition> conditions, Block thenPart) {
            super(conditions, thenPart);
        }
        
        @Override
        protected JCBreak transformIntermediateElse(Cond transformedCond, java.util.List<Condition> rest) {
            return make().Break(null);
        }

        @Override
        protected List<JCStatement> transformInnermostThen(Cond transformedCond) {
            return makeThenBlock(transformedCond, thenPart, null).getStatements();   
        }

        @Override
        protected JCStatement transformInnermostElse(Cond transformedCond, java.util.List<Condition> rest) {
            return make().Break(null);
        }
        
        @Override
        protected List<JCStatement> transformCommon(Cond transformedCond, 
                java.util.List<Condition> rest, JCExpression test, List<JCStatement> stmts, JCStatement elseBlock) {
            if (transformedCond.makeTestVarDecl(0, false) != null) {
                varDecls.append(transformedCond.makeTestVarDecl(0, false));
            }
            if (transformedCond.hasResultDecl()) {
                JCVariableDecl resultVarDecl = make().VarDef(make().Modifiers(Flags.FINAL), 
                        transformedCond.getVariableName().asName(), 
                        transformedCond.makeTypeExpr(), 
                        transformedCond.makeResultExpr());
                stmts = stmts.prepend(resultVarDecl);
            }
            JCStatement elsePart = elseBlock;
            stmts = List.<JCStatement>of(make().If(
                    test, 
                    make().Block(0, stmts), 
                    elsePart));
            return stmts;
        }
        
        @Override
        public List<JCStatement> getResult() {
            List<JCStatement> stmts = transformList(conditions);
            ListBuffer<JCStatement> result = ListBuffer.lb();
            result.appendList(varDecls);
            result.appendList(stmts);
            return List.<JCStatement>of(make().WhileLoop(makeBoolean(true), 
                    make().Block(0, result.toList())));
        }

    }
    
    List<JCStatement> transform(Tree.Assertion ass) {
        return new AssertCondList(ass).getResult();        
    }
    
    class AssertCondList extends BlockCondList {
        private final Tree.Assertion ass;
        private final ListBuffer<JCStatement> varDecls = ListBuffer.lb();
        private final SyntheticName messageSb = naming.temp("assert");
        private LinkedHashMap<Cond, CName> unassignedResultVars = new LinkedHashMap<Cond, CName>();
        
        public AssertCondList(Tree.Assertion ass) {
            super(ass.getConditionList().getConditions(), null);
            this.ass = ass;
        }
        
        private boolean isMulti() {
            return this.conditions.size() > 1;
        }
        
        protected Substitution getSubstitution(Cond cond) {
            Substitution subs = super.getSubstitution(cond);
            if (subs == null) {
                return subs;
            }
            return naming.new Substitution(subs.original, subs.substituted) {
                public void close() {
                    // Don't delegate close(): We need the substitution to 
                    // live until the end of the declaration's scope
                }
            };
        }
        
        @Override
        protected List<JCStatement> transformCommon(Cond cond, 
                java.util.List<Condition> rest, 
                JCExpression test, List<JCStatement> stmts, JCStatement elseBlock) {
            if (cond.hasResultDecl()) {
                JCVariableDecl resultVarDecl = make().VarDef(make().Modifiers(Flags.FINAL), 
                        cond.getVariableName().asName(), 
                        cond.makeTypeExpr(), 
                        null);
                // Note we capture the substitution here, because it won't be 
                // in scope when we generate the default assignment branches.
                unassignedResultVars.put(cond, 
                        cond.getVariableName().capture());
                varDecls.append(resultVarDecl);
                stmts = stmts.prepend(make().Exec(make().Assign(cond.getVariableName().makeIdent(), cond.makeResultExpr())));
            }
            
            if (isMulti()) {
                List<JCStatement> elseStmts = ((JCBlock)elseBlock).getStatements();
                for (Cond unassigned : unassignedResultVars.keySet()) {
                    elseStmts = elseStmts.prepend(
                            make().Exec(make().Assign(unassignedResultVars.get(unassigned).makeIdent(), 
                            ((SpecialFormCond)unassigned).makeDefaultExpr())));
                }
                elseBlock = make().Block(0, elseStmts);
            }
            stmts = List.<JCStatement>of(make().If(
                    test, 
                    make().Block(0, stmts), 
                    elseBlock));
            
            if (cond.makeTestVarDecl(0, true) != null) {
                stmts = stmts.prepend(cond.makeTestVarDecl(0, true));
            }
            return stmts;
        }
        
        @Override
        public List<JCStatement> getResult() {
            List<JCStatement> stmts = transformList(conditions);
            ListBuffer<JCStatement> result = ListBuffer.lb();
            if (isMulti()) {
                result.append(makeVar(messageSb, make().Type(syms().stringType), makeNull()));
            }
            result.appendList(varDecls);
            result.appendList(stmts);
            JCExpression message = prependFailureMessage(appendLocation(messageSb.makeIdent(), ass), ass);
            JCThrow throw_ = makeThrowAssertionFailure(message);
            if (isMulti()) {
                result.append(make().If(
                        make().Binary(JCTree.NE, messageSb.makeIdent(), makeNull()), 
                        throw_, null));
            }
            return result.toList();   
        }

        private JCStatement transformCommonElse(Cond cond, java.util.List<Condition> rest) {
            if (!isMulti()) {
                return null;
            }
            JCExpression expr = null;
            boolean seen = false;
            for (Tree.Condition condition : this.conditions) {
                if (cond.getCondition() == condition) {
                    expr = appendViolation(expr, "violated", condition);
                    seen = true;
                    continue;
                }
                expr = appendViolation(expr, seen ? "untested" : "unviolated", condition);
            }
            return make().Block(0, List.<JCStatement>of( 
                    make().Exec(make().Assign(messageSb.makeIdent(), expr))));
        }
        
        @Override
        protected List<JCStatement> transformInnermostThen(Cond cond) {
            return List.nil();
        }

        @Override
        protected JCStatement transformInnermostElse(Cond cond, java.util.List<Condition> rest) {
            if (!isMulti()) {
                return makeThrowAssertionFailure(prependFailureMessage(
                        appendLocation(
                                appendViolation(
                                        null, 
                                        "violated",
                                        cond.getCondition()), 
                                ass), 
                        ass));
            }
            return transformCommonElse(cond, rest);
        }

        private JCExpression cat(JCExpression str1, JCExpression str2) {
            return make().Binary(JCTree.PLUS, str1, str2);
        }
        
        private JCExpression cat(JCExpression strExpr, String literal) {
            return cat(strExpr, make().Literal(literal));
        }
        
        private JCExpression newline() {
            return make().Apply(null, 
                    makeQualIdent(makeIdent(syms().systemType), "lineSeparator"), 
                    List.<JCExpression>nil());   
        }
        
        private JCExpression prependFailureMessage(JCExpression expr, Tree.Assertion ass) {
            JCExpression p = make().Literal("Assertion failed");
            String docText = getDocAnnotationText(ass);
            if (docText != null) {
                p = cat(p, ": " + docText);
            }
            p = cat(p, newline());
            return cat(p, expr);
        }
        
        private JCExpression appendViolation(JCExpression expr, String state, Tree.Condition condition) {
            JCExpression result;
            if (expr != null) {
                result = cat(expr, make().Literal("\t" + state+ " "));
            } else {
                result = make().Literal("\t" + state + " ");
            }
            result = cat(result, make().Literal(getSourceCode(condition)));
            result = cat(result, newline());
            return result;
        }
        
        private JCExpression appendLocation(JCExpression expr, Tree.Assertion ass) {
            String location = ass.getUnit().getFilename() + ":" + ass.getLocation();
            JCExpression result = cat(expr, "\tat ");
            return cat(result, location);
        }
        
        private JCThrow makeThrowAssertionFailure(JCExpression expr) {
            //TODO proper exception type
            return makeThrowException(syms().ceylonExceptionType, expr);
        }
        
        @Override
        protected JCStatement transformIntermediateElse(Cond cond, java.util.List<Condition> rest) {
            return transformCommonElse(cond, rest);
        }
    }
    
    interface Cond {
        
        public Tree.Condition getCondition();
        
        public Tree.Variable getVariable();
        
        public SubstitutedName getVariableName();
        
        public boolean hasResultDecl();
        public boolean hasAliasedVariable();
        
        public JCExpression makeTypeExpr();
        
        public JCExpression makeResultExpr();
    
        public JCStatement makeTestVarDecl(int flags, boolean init);
    
        public JCExpression makeTest();
    }
    
    abstract class SpecialFormCond<C extends Tree.Condition> implements Cond {
        protected final C cond;
        protected final ProducedType toType;
        protected final Expression specifierExpr;
        protected final Naming.SyntheticName testVar;
        protected final Tree.Variable variable;
        SpecialFormCond(
                C cond,
                ProducedType toType, 
                Expression specifierExpr, 
                Tree.Variable variable) {
            this.cond = cond;
            this.toType = toType;
            this.specifierExpr = specifierExpr;
            this.testVar = naming.alias(variable.getIdentifier().getText());
            this.variable = variable;
        }
        
        @Override
        public final C getCondition() {
            return cond;
        }
        
        @Override
        public final Tree.Variable getVariable() {
            return variable;
        }
        
        @Override
        public final SubstitutedName getVariableName() {
            return naming.substituted(variable.getDeclarationModel());
        }
        
        @Override
        public boolean hasResultDecl() {
            return true;
        }
        
        @Override
        public boolean hasAliasedVariable() {
            return !(getVariable().getType() instanceof Tree.SyntheticVariable);
        }
        
        @Override
        public final JCExpression makeTypeExpr() {
            return makeJavaType(toType);
        }

        /**
         * Generates a default value for result variables.
         * When transforming {@code if/else if} the variable holding the 
         * type-narrowed variable must be declared final so it can be captured
         * but in the else blocks we don't have anything we can safely 
         * initialise it with in the model. So we generate default values 
         * here, which cannot actually be seen from the ceylon code.
         * @return
         */
        protected JCExpression makeDefaultExpr() {
            at(cond);
            if (canUnbox(toType)) {
                if (isCeylonBoolean(toType)) {
                    return makeBoolean(false);
                } else if (isCeylonFloat(toType)) {
                    return make().Literal(0.0);
                } else if (isCeylonInteger(toType)) {
                    return makeLong(0);
                } else if (isCeylonCharacter(toType)) {
                    return make().Literal(0);
                }
            }
            // The default value cannot be seen from the Ceylon code, so it's
            // OK to assign it to null even though it may not be an 
            // optional type
            return makeNull();
        }
        
        @Override
        public JCStatement makeTestVarDecl(int flags, boolean init) {
            // Temporary variable holding the result of the expression/variable to test
            return make().VarDef(make().Modifiers(flags), testVar.asName(), makeResultType(), init ? makeNull() : null);
        }

        protected abstract JCExpression makeResultType();
        
    }
    
    class IsCond extends SpecialFormCond<Tree.IsCondition> {
        private final boolean negate;
        private IsCond(Tree.IsCondition isdecl) {
            super(isdecl, 
                    // use the type of the variable, which is more precise than the type we test for
                    isdecl.getVariable().getType().getTypeModel(), 
                    isdecl.getVariable().getSpecifierExpression().getExpression(),
                    isdecl.getVariable());
            negate = isdecl.getNot();
        }
        
        @Override
        public boolean hasResultDecl() {
            return isErasedToObjectOptimization() || isNothingOptimization() ? false : super.hasResultDecl();
        }        

        /** 
         * We can optimize "is Nothing x" (but not "is Nothing y = x")
         * because there can be no unboxing or typecasting of the result
         */
        private boolean isNothingOptimization() {
            return toType.isExactly(typeFact().getNothingDeclaration().getType()) 
                    && ! hasAliasedVariable();
        }
        
        /**
         * Optimization: if no typecast will be required, and there's no 
         * aliasing then we don't need to declare a test var, and the result var
         * is simply the variable name.
         */
        private boolean isErasedToObjectOptimization() {
            return !typecastRequired() && !hasAliasedVariable();
        }
        
        /** 
         * Optimization: if the type of the expression and toType
         * both erase to Object then we can avoid the typecast
         */
        private boolean typecastRequired() {
            // TODO: In fact it should be possible to avoid declaring a test
            // var this case, but it complicates the test when dealing with unions and intersections
            return !willEraseToObject(toType);
        }
        
        @Override
        public JCStatement makeTestVarDecl(int flags, boolean init) {
            // We can optimize "is Nothing x" (but not "is Nothing y = x")
            // because there can be no unboxing or typecasting of the result
            return isErasedToObjectOptimization() || isNothingOptimization() ? null : super.makeTestVarDecl(flags, init);
        }
        
        @Override
        public JCExpression makeTest() {
            // no need to cast for erasure here
            JCExpression expr = expressionGen().transformExpression(specifierExpr);
            at(cond);
            // Assign the expression to test to the temporary variable
            if (!isErasedToObjectOptimization() && !isNothingOptimization()) {
                expr = make().Assign(testVar.makeIdent(), expr);
            }
            
            // Test on the tmpVar in the following condition
            expr = makeTypeTest(expr, isErasedToObjectOptimization() ? getVariableName() : testVar,
                    // only test the types we're testing for, not the type of
                    // the variable (which can be more precise)
                    cond.getType().getTypeModel());
            if (negate) {
                expr = make().Unary(JCTree.NOT, expr);
            }
            return expr;
        }
        
        @Override
        protected JCExpression makeResultType() {
            at(cond);
            return make().Type(syms().objectType);
        }
        
        @Override
        public JCExpression makeResultExpr() {
            at(cond);
            JCExpression expr = testVar.makeIdent();
            
            if (typecastRequired()) {
                // Want raw type for instanceof since it can't be used with generic types
                JCExpression rawToTypeExpr = makeJavaType(toType, JT_NO_PRIMITIVES | JT_RAW);
                // Substitute variable with the correct type to use in the rest of the code block
                expr = at(cond).TypeCast(rawToTypeExpr, expr);
                if (canUnbox(toType)) {
                    expr = unboxType(expr, toType);
                } 
            }
            return expr;
        }

    }
    
    class ExistsCond extends SpecialFormCond<Tree.ExistsCondition> {

        private ExistsCond(Tree.ExistsCondition exists) {
            super(exists, 
                    simplifyType(exists.getVariable().getType().getTypeModel()),
                    exists.getVariable().getSpecifierExpression().getExpression(), 
                    exists.getVariable());    
        }
        
        @Override
        public JCExpression makeResultExpr() {
            Value decl = this.cond.getVariable().getDeclarationModel();
            ProducedType exprType = this.specifierExpr.getTypeModel();
            if (isOptional(exprType)) {
                exprType = typeFact().getDefiniteType(exprType);
            }
            return expressionGen().applyErasureAndBoxing(testVar.makeIdent(),
                    exprType, true,
                    CodegenUtil.getBoxingStrategy(decl),
                    decl.getType());
        }
        
        @Override
        protected JCExpression makeResultType() {
            ProducedType tmpVarType = specifierExpr.getTypeModel();
            return makeJavaType(tmpVarType, JT_NO_PRIMITIVES);
        }
        
        @Override
        public JCExpression makeTest() {
            // for the purpose of checking if something is null, we need it boxed and optional, otherwise
            // for some Java calls if we consider it non-optional we will get an unwanted null check
            ProducedType specifierType = this.specifierExpr.getTypeModel();
            if(!typeFact().isOptionalType(specifierType)){
                specifierType = typeFact().getOptionalType(specifierType);
            }
            JCExpression expr = expressionGen().transformExpression(specifierExpr, BoxingStrategy.BOXED, specifierType);
            at(cond);
            // Assign the expression to test to the temporary variable
            JCExpression firstTimeTestExpr = make().Assign(testVar.makeIdent(), expr);
            // Test on the tmpVar in the following condition
            return make().Binary(JCTree.NE, firstTimeTestExpr, makeNull());
        }
    }
    
    class NonemptyCond extends SpecialFormCond<Tree.NonemptyCondition> {

        private NonemptyCond(Tree.NonemptyCondition nonempty) {
            super(nonempty, 
                    nonempty.getVariable().getType().getTypeModel(), 
                    nonempty.getVariable().getSpecifierExpression().getExpression(),
                    nonempty.getVariable());
        }
        
        @Override
        protected JCExpression makeDefaultExpr() {
            return makeNull();
        }
        
        @Override
        public JCExpression makeResultExpr() {
            Value decl = this.cond.getVariable().getDeclarationModel();
            return expressionGen().applyErasureAndBoxing(testVar.makeIdent(),
                    typeFact().getDefiniteType(this.specifierExpr.getTypeModel()), false, true,
                    BoxingStrategy.BOXED,
                    decl.getType(),
                    ExpressionTransformer.EXPR_DOWN_CAST);
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
            JCExpression firstTimeTestExpr = make().Assign(testVar.makeIdent(), expr);
            // Test on the tmpVar in the following condition
            return makeNonEmptyTest(firstTimeTestExpr);
        }
    }
    
    class BooleanCond implements Cond {
        private final BooleanCondition cond;
        
        private BooleanCond(Tree.BooleanCondition booleanCondition) {
            super();
            this.cond = booleanCondition;
        }

        @Override
        public JCStatement makeTestVarDecl(int flags, boolean init) {
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

        @Override
        public Condition getCondition() {
            return cond;
        }

        @Override
        public Variable getVariable() {
            return null;
        }
        
        @Override
        public final SubstitutedName getVariableName() {
            return null;
        }

        @Override
        public JCExpression makeTypeExpr() {
            return null;
        }

        @Override
        public JCExpression makeResultExpr() {
            return null;
        }
        
        @Override
        public boolean hasAliasedVariable() {
            return false;
        }

    }
    
    Cond transformCondition(Tree.Condition cond, Tree.Block thenPart) {
        if (cond instanceof Tree.IsCondition) {
            Tree.IsCondition is = (Tree.IsCondition)cond;
            return new IsCond(is);
        } else if (cond instanceof Tree.ExistsCondition) {
            Tree.ExistsCondition exists = (Tree.ExistsCondition)cond;
            return new ExistsCond(exists);
        } else if (cond instanceof Tree.NonemptyCondition) {
            Tree.NonemptyCondition nonempty = (Tree.NonemptyCondition)cond;
            return new NonemptyCond(nonempty);
        } else if (cond instanceof Tree.BooleanCondition) {
            return new BooleanCond((Tree.BooleanCondition)cond);
        }
        throw new RuntimeException();
    }

    private String getDocAnnotationText(Tree.Assertion ass) {
        String docText = null;
        Tree.Annotation doc = getAnnotation(ass.getAnnotationList(), "doc");
        if (doc != null) {
            Tree.Expression expression = null;
            if (doc.getPositionalArgumentList() != null) {
                Tree.PositionalArgument arg = doc.getPositionalArgumentList().getPositionalArguments().get(0);
                if(arg instanceof Tree.ListedArgument)
                    expression = ((Tree.ListedArgument) arg).getExpression();
                else
                    throw new RuntimeException("Argument to doc annotation cannot be a spread argument or comprehension: " + arg);
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
        ForStatementTransformation transformation = rangeOpIteration(stmt);
        if (transformation == null) {
            transformation = new ForStatementTransformation(stmt);
        }
        return transformation.transform();
    }
    
    private boolean isRangeOf(Tree.RangeOp range, ProducedType ofType) {
        ProducedType rangeType = range.getTypeModel();
        return typeFact().getRangeType(ofType).isExactly(rangeType);
    }

    /**
     * Returns null but logs an error with the given reason if an optimization 
     * asserted using {@code @requireOptimization["optName"]} couldn't be 
     * performed.
     * 
     * @param stmt The thing with the {@code @requireOptimization} compiler 
     * annotation 
     * @param optName The name of the optimization
     * @param reason The reason the optimization could not be used
     * @return null
     */
    private <T,S extends Tree.StatementOrArgument> T optimizationFailed(S stmt, String optName, String reason) {
        if (CodegenUtil.hasCompilerAnnotationWithArgument(stmt, 
                        "requireOptimization", optName)) {
            log.error(getPosition(stmt), "ceylon.optim.failed", optName, reason);
        }
        return null;
    }
    
    /**
     * Determines whether the given optimization has been disabled on the 
     * given statement. 
     * @param stmt The thing with the {@code @requireOptimization} compiler 
     * annotation.
     * @param optName The name of the optimization
     * @return
     */
    private <S extends Tree.StatementOrArgument> boolean optimizationDisabled(S stmt, String optName) {
        return CodegenUtil.hasCompilerAnnotation(stmt, "disableOptimization")
                || CodegenUtil.hasCompilerAnnotationWithArgument(stmt, 
                        "disableOptimization", optName);
    }
    
    /**
     * Returns a {@link RangeOpIterationOptimization} is that optimization applies
     * to the given {@code for} statement, otherwise null.
     * @param stmt The for statement
     * @return a {@link RangeOpIterationOptimization} or null.
     */
    private ForStatementTransformation rangeOpIteration(Tree.ForStatement stmt) {
        final String optName = RangeOpIterationOptimization.OPT_NAME;
        if (optimizationDisabled(stmt, optName)) {
            return optimizationFailed(stmt, optName, 
                    "optimization explicitly disabled by @disableOptimization");
        }
        
        ForIterator iterator = stmt.getForClause().getForIterator();
        if (!(iterator instanceof ValueIterator)) {
            return optimizationFailed(stmt, optName, 
                    "optimization applies only to ValueIterators");            
        }
        ValueIterator vi = (ValueIterator)iterator;
        SpecifierExpression specifier = vi.getSpecifierExpression();
        Term term = specifier.getExpression().getTerm();
        final Tree.Term increment;
        final Tree.RangeOp range;
        if (term instanceof Tree.RangeOp) {
            // So it's a for (i in (lhs..rhs)) { ... }
            increment = null;
            range = (Tree.RangeOp)term;
        } else if (term instanceof Tree.InvocationExpression) {
            Tree.InvocationExpression inv = (Tree.InvocationExpression)term;
            if (inv.getPrimary() instanceof Tree.QualifiedMemberExpression) {
                Tree.QualifiedMemberExpression prim = (Tree.QualifiedMemberExpression)inv.getPrimary();
                if ("by".equals(prim.getIdentifier().getText())
                        && prim.getPrimary() instanceof Tree.Expression
                        && (((Tree.Expression)(prim.getPrimary())).getTerm() instanceof Tree.RangeOp)) {
                    // So it's a for (i in (lhs..rhs).by(increment)) { ... }
                    range = (Tree.RangeOp)((Tree.Expression)(prim.getPrimary())).getTerm();                    
                    if (inv.getPositionalArgumentList() != null) {
                        PositionalArgument a = inv.getPositionalArgumentList().getPositionalArguments().get(0);
                        if(a instanceof Tree.ListedArgument)
                            increment = ((Tree.ListedArgument)a).getExpression().getTerm();
                        else
                            return optimizationFailed(stmt, optName, 
                                    "Unable to determine expression for argument to by(): appears spread or comprehension");
                    } else if (inv.getNamedArgumentList() != null) {
                        Tree.SpecifiedArgument sarg = null;
                        for (Tree.NamedArgument arg : inv.getNamedArgumentList().getNamedArguments()) {
                            if ("step".equals(arg.getIdentifier().getText())) {
                                if (arg instanceof Tree.SpecifiedArgument) {
                                    sarg = ((Tree.SpecifiedArgument)arg);
                                    break;
                                }
                                // TODO In theory we could support Tree.AttributeArgument too
                            }
                        }
                        if (sarg != null) {
                            increment = sarg.getSpecifierExpression().getExpression().getTerm();
                        } else {
                            return optimizationFailed(stmt, optName, 
                                    "Unable to determine expression for argument to by{}");
                        }
                    } else {
                        return optimizationFailed(stmt, optName, 
                                "Unable to get arguments to by()");
                    }
                } else {
                    return optimizationFailed(stmt, optName, 
                            "Only applies to Iterables of the form 'lhs..rhs' or '(lhs..rhs).by(step)'");
                }
            } else {
                return optimizationFailed(stmt, optName, 
                        "Only applies to Iterables of the form 'lhs..rhs' or '(lhs..rhs).by(step)'");
            }
        } else {
            return optimizationFailed(stmt, optName, 
                    "Only applies to Iterables of the form 'lhs..rhs' or '(lhs..rhs).by(step)'");
        }
        
        Type type;
        ProducedType integerType = typeFact().getIntegerDeclaration().getType();
        ProducedType characterType = typeFact().getCharacterDeclaration().getType();
        if (isRangeOf(range, integerType)) {
            type = syms().longType;
        } else if (isRangeOf(range, characterType)) {
            type = syms().intType;
        } else {
            return optimizationFailed(stmt, optName, "The RangeOp doesn't produce a Range<Integer>/Range<Character>");
        }
        return new RangeOpIterationOptimization(stmt, 
                range.getLeftTerm(), range.getRightTerm(), 
                increment, type);
    }
    
    class ForStatementTransformation {
        
        protected Tree.ForStatement stmt; 
        
        ForStatementTransformation(Tree.ForStatement stmt) {
            this.stmt = stmt;
        }
        
        protected List<JCStatement> transform() {
            at(stmt);
            ListBuffer<JCStatement> outer = ListBuffer.<JCStatement> lb();
            Name tempForFailVariable = currentForFailVariable;
            try {
                if (needsFailVar()) {
                    // boolean $doforelse$X = true;
                    JCVariableDecl failtest_decl = make().VarDef(make().Modifiers(0), naming.aliasName("doforelse"), make().TypeIdent(TypeTags.BOOLEAN), make().Literal(TypeTags.BOOLEAN, 1));
                    outer = outer.append(failtest_decl);
                    currentForFailVariable = failtest_decl.getName();
                } else {
                    currentForFailVariable = null;
                }
        
                outer = outer.appendList(transformForClause());
        
                if (stmt.getElseClause() != null) {
                    // The user-supplied contents of fail block
                    List<JCStatement> failblock = transformStmts(stmt.getElseClause().getBlock().getStatements());
                    
                    if (needsFailVar()) {
                        // if ($doforelse$X) ...
                        JCIdent failtest_id = at(stmt).Ident(currentForFailVariable);
                        outer = outer.append(at(stmt).If(failtest_id, at(stmt).Block(0, failblock), null));
                    } else {
                        outer = outer.appendList(failblock);
                    }
                }
            } finally {
                currentForFailVariable = tempForFailVariable;
            }
    
            return outer.toList();
        }

        private boolean needsFailVar() {
            return stmt.getExits() && stmt.getElseClause() != null;
        }

        protected ListBuffer<JCStatement> transformForClause() {
            Naming.SyntheticName elem_name = naming.alias("elem");    
            
            ForIterator iterDecl = stmt.getForClause().getForIterator();
            Variable variable;
            Variable valueVariable;
            if (iterDecl instanceof ValueIterator) {
                variable = ((ValueIterator) iterDecl).getVariable();
                valueVariable = null;
            } else if (iterDecl instanceof KeyValueIterator) {
                variable = ((KeyValueIterator) iterDecl).getKeyVariable();
                valueVariable = ((KeyValueIterator) iterDecl).getValueVariable();
            } else {
                throw new RuntimeException("Unknown ForIterator");
            }
            
            final Naming.SyntheticName loopVarName = naming.synthetic(variable.getIdentifier().getText());
            Expression specifierExpression = iterDecl.getSpecifierExpression().getExpression();
            ProducedType sequenceElementType;
            if(valueVariable == null)
                sequenceElementType = variable.getType().getTypeModel();
            else{
                // Entry<V1,V2>
                sequenceElementType = typeFact().getEntryType(variable.getType().getTypeModel(), 
                        valueVariable.getType().getTypeModel());
            }
            JCExpression castElem = at(stmt).TypeCast(makeJavaType(sequenceElementType, CeylonTransformer.JT_NO_PRIMITIVES), elem_name.makeIdent());
            List<JCAnnotation> annots = makeJavaTypeAnnotations(variable.getDeclarationModel());

            // ceylon.language.Iterator<T> $V$iter$X = ITERABLE.getIterator();
            // We don't need to unerase here as anything remotely a sequence will be erased to Iterable, which has getIterator()
            JCExpression containment = expressionGen().transformExpression(specifierExpression, BoxingStrategy.BOXED, null);
            
            // final U n = $elem$X;
            // or
            // final U n = $elem$X.getKey();
            JCExpression loopVarInit;
            ProducedType loopVarType;
            if (valueVariable == null) {
                loopVarType = sequenceElementType;
                loopVarInit = castElem;
            } else {
                loopVarType = actualType(variable);
                loopVarInit = at(stmt).Apply(null, makeSelect(castElem, Naming.getGetterName("key")), List.<JCExpression> nil());
            }
            JCVariableDecl itemOrKeyDecl = at(stmt).VarDef(make().Modifiers(FINAL, annots), loopVarName.asName(), makeJavaType(loopVarType), 
                    boxUnboxIfNecessary(loopVarInit, true, loopVarType, CodegenUtil.getBoxingStrategy(variable.getDeclarationModel())));
            final SyntheticName iteratorVarName = loopVarName.suffixedBy("$iter").alias();
            List<JCStatement> itemDecls = List.<JCStatement> of(itemOrKeyDecl);

            if (valueVariable != null) {
                // final V n = $elem$X.getElement();
                ProducedType valueVarType = actualType(valueVariable);
                JCExpression valueVarTypeExpr = makeJavaType(valueVarType);
                JCExpression valueVarInitExpr = at(stmt).Apply(null, makeSelect(castElem, Naming.getGetterName("item")), List.<JCExpression> nil());
                String valueVarName = valueVariable.getIdentifier().getText();
                JCVariableDecl valueDecl = at(stmt).VarDef(make().Modifiers(FINAL, annots), names().fromString(valueVarName), valueVarTypeExpr, 
                        boxUnboxIfNecessary(valueVarInitExpr, true, valueVarType, CodegenUtil.getBoxingStrategy(valueVariable.getDeclarationModel())));
                itemDecls = itemDecls.append(valueDecl);
            }

            return ListBuffer.<JCStatement>lb().appendList(transformIterableIteration(stmt,
                    elem_name, 
                    iteratorVarName,
                    sequenceElementType,
                    containment,
                    itemDecls,
                    transformStmts(stmt.getForClause().getBlock().getStatements())));
        }
    }
    
    /**
     * The transformation of a ceylon {@code for} loop:
     * 
     * <pre>
     *     java.lang.Object ITERATION_VAR_NAME;
     *     for (Iterator<ITERATOR_ELEMENT_TYPE> ITERATOR_VAR_NAME = ITERABLE.getIterator();
     *             !((ITERATION_VAR_NAME = ITERATOR_VAR_NAME.getNext()) instanceof ceylon.language.Finished;
     *         ) {
     *         ITEM_DECLS;
     *         BODY_STMTS;
     *     }
     * </pre>
     * 
     * @param iterationVarName The iteration variable (which recieves the value of {@code Iterator.next()})
     * @param iteratorVarName The name of the {@code Iterator} variable
     * @param iteratorElementType The type argument of the {@code Iterator}
     * @param iterableExpr The {@code Iterable} expression
     * @param itemDecls variable declarations for the iteration variables which 
     * begin the loop body (these depend on {@code iterationVarName} and may 
     * typecast or destructure it). May be null.
     * @param bodyStmts Other statements in the loop body
     * @return
     */
    List<JCStatement> transformIterableIteration(Node node,
            Naming.SyntheticName iterationVarName,
            Naming.SyntheticName iteratorVarName,
            ProducedType iteratorElementType,
            JCExpression iterableExpr,
            List<JCStatement> itemDecls,
            List<JCStatement> bodyStmts) {
        List<JCStatement> result = List.<JCStatement>nil();
        // java.lang.Object ELEM_NAME;
        JCVariableDecl elemDecl = make().VarDef(make().Modifiers(0), iterationVarName.asName(), make().Type(syms().objectType), null);
        result = result.append(elemDecl);
        
        SyntheticName iterName = iteratorVarName;
        
        ProducedType iteratorType = typeFact().getIteratorType(iteratorElementType);
        JCExpression iteratorTypeExpr = makeJavaType(iteratorType, CeylonTransformer.JT_TYPE_ARGUMENT);
        
        // ceylon.language.Iterator<T> LOOP_VAR_NAME$iter$X = ITERABLE.getIterator();
        // We don't need to unerase here as anything remotely a sequence will be erased to Iterable, which has getIterator()
        JCExpression getIter = at(node).Apply(null, makeSelect(iterableExpr, "getIterator"), List.<JCExpression> nil());
        getIter = gen().expressionGen().applyErasureAndBoxing(getIter, iteratorType, true, BoxingStrategy.BOXED, iteratorType);
        JCVariableDecl iteratorDecl = at(node).VarDef(make().Modifiers(0), iterName.asName(), iteratorTypeExpr, getIter);
        
        List<JCStatement> loopBody = List.<JCStatement>nil();
        if (itemDecls != null) {
            loopBody = loopBody.appendList(itemDecls);
        }

        // The user-supplied contents of the loop
        loopBody = loopBody.appendList(bodyStmts);
        
        // ELEM_NAME = LOOP_VAR_NAME$iter$X.next()
        JCExpression iter_elem = make().Apply(null, makeSelect(iterName.makeIdent(), "next"), List.<JCExpression> nil());
        JCExpression elem_assign = make().Assign(iterationVarName.makeIdent(), iter_elem);
        // !((ELEM_NAME = LOOP_VAR_NAME$iter$X.next()) instanceof Finished)
        JCExpression instof = make().TypeTest(elem_assign, makeIdent(syms().ceylonFinishedType));
        JCExpression loopCond = make().Unary(JCTree.NOT, instof);

        // for (.ceylon.language.Iterator<T> LOOP_VAR_NAME$iter$X = ITERABLE.getIterator(); 
        //         !(($elem$X = $V$iter$X.next()) instanceof Finished); ) {
        JCForLoop forLoop = at(node).ForLoop(
            List.<JCStatement>of(iteratorDecl), 
            loopCond, 
            List.<JCExpressionStatement>nil(), // No step necessary
            at(node).Block(0, loopBody));
        return result.append(forLoop);
    }
    
    
    /**
     * <p>Transformation of {@code for} loops over {@code Range<Integer>} 
     * or {@code Range<Character>} which avoids allocating a {@code Range} and
     * using an {@code Iterator} like 
     * {@link #ForStatementTransformation} but instead outputs a C-style 
     * {@code for} loop. Because a Range is never empty we can also omit
     * code for handling {@code else} clauses of {@code for} statements when 
     * we know the {@code for} block returns normally</p>
     * 
     * <p>This is able to optimize statements like the following:</p>
     * <ul>
     * <li>{@code for (i in lhs..rhs) ... }</li>
     * <li>{@code for (i in (lhs..rhs).by(increment)) ... }</li>
     * <ul>
     * <p>where {@code lhs}, {@code rhs} and {@code increment} are 
     * expressions (not necessarily literals or compile-time constants).</p>
     * 
     * <p>Given a statement like {@code for (i in (lhs..rhs).by(increment) ...} 
     * we generate something like this:</p>
     * <pre>
     *  long by$ = by;
     *  if (by$ <= 0) {
     *      throw new Exception(ceylon.language.String.instance("step size must be greater than zero"));
     *  }
     *  final long start$ = lhs;
     *  final long end$ = rhs;
     *  final boolean increasing$ = start <= end;
     *  final long inc$ = (increasing$ ? by$ : -by$);
     *  for (long i = start$; (increasing$ ? i-end$ <= 0 : i-end$ >= 0); i+=inc$) {
     *      USERBLOCK
     *  }
     * </pre>
     * 
     * <p>In the case where we have a simple range with no {@code by()} 
     * invocation then the test for negative step size is omitted.</p>
     * 
     * <p>The transformation is complicated by:</p>
     * <ul>
     *   <li>Not knowing at compile-time whether {@code lhs < rhs}, which 
     *       complicated the {@code for} termination condition</li>
     *   <li>Needing to worry about {@code int} or {@code long} overflow
     *       (hence the {@code i-end$ <= 0} rather than the more natural
     *       {@code i <= end}.</li>
     * </ul>
     */
    class RangeOpIterationOptimization extends ForStatementTransformation {
        public static final String OPT_NAME = "RangeOpIteration";
        private final Tree.Term lhs;
        private final Tree.Term rhs;
        private final Term increment;// if null then increment is +/-1
        private final Type type;
        private final ProducedType pt;
        public RangeOpIterationOptimization(
                Tree.ForStatement stmt,
                Tree.Term lhs, Tree.Term rhs,
                Tree.Term increment,
                Type type) {
            super(stmt);
            this.lhs = lhs;
            this.rhs = rhs;
            this.increment = increment;
            this.type = type;
            if (type.tag == syms().intType.tag) {
                this.pt = typeFact().getCharacterDeclaration().getType();
            } else if (type.tag == syms().longType.tag) {
                this.pt = typeFact().getIntegerDeclaration().getType();
            } else {
                throw new RuntimeException();
            }
        }
        private Tree.Variable getVariable() {
            return ((ValueIterator)stmt.getForClause().getForIterator()).getVariable();
        }
        private Tree.Block getBlock() {
            return stmt.getForClause().getBlock();
        }
        private JCExpression makeType() {
            return make().Type(type);
        }
        private ProducedType getType() {
            return pt;
        }
        @Override
        protected ListBuffer<JCStatement> transformForClause() {
            ListBuffer<JCStatement> result = ListBuffer.<JCStatement>lb();
            
            // Note: Must invoke lhs, rhs and increment in the correct order!
            // long start = <lhs>
            SyntheticName start = naming.temp("start");
            result.append(make().VarDef(make().Modifiers(FINAL), start.asName(), makeType(), 
                    expressionGen().transformExpression(lhs, BoxingStrategy.UNBOXED, getType())));
            // long end = <rhs>
            SyntheticName end = naming.temp("end");
            result.append(make().VarDef(make().Modifiers(FINAL), end.asName(), makeType(), 
                    expressionGen().transformExpression(rhs, BoxingStrategy.UNBOXED, getType())));
            
            final SyntheticName by;
            if (increment != null) {
                by = naming.temp("by");
                // by = increment;
                result.append(make().VarDef(make().Modifiers(FINAL), by.asName(), makeType(), 
                        expressionGen().transformExpression(increment, BoxingStrategy.UNBOXED, getType())));
                // if (by <= 0) throw Exception("step size must be greater than zero");
                result.append(make().If(
                        make().Binary(JCTree.LE, by.makeIdent(), make().Literal(0)), 
                        makeThrowException(syms().ceylonExceptionType, 
                                make().Literal("step size must be greater than zero")),
                        null));
            } else {
                by = null;
            }
            
            SyntheticName increasing = naming.temp("increasing");
            // boolean increasing = start < end;
            result.append(make().VarDef(make().Modifiers(FINAL), increasing.asName(), make().Type(syms().booleanType), 
                    make().Binary(JCTree.LE, start.makeIdent(), end.makeIdent())));
            
            SyntheticName incr = naming.temp("incr");
            
            result.append(make().VarDef(make().Modifiers(FINAL), incr.asName(), makeType(), 
                    make().Conditional(
                            increasing.makeIdent(), 
                            makeIncreasingIncrement(by), makeDecreasingIncrement(by))));
            
            SyntheticName varname = naming.alias(getVariable().getIdentifier().getText());
            JCVariableDecl init = make().VarDef(make().Modifiers(0), varname.asName(), makeType(), start.makeIdent());
            List<JCStatement> blockStatements = transformStmts(getBlock().getStatements());
            blockStatements = blockStatements.prepend(make().VarDef(make().Modifiers(FINAL), 
                    names().fromString(getVariable().getIdentifier().getText()), 
                    makeType(),
                    varname.makeIdent()));
            
            // for (long i = start; (increasing ? i -end <= 0 : i -end >= 0); i+=inc) {
            JCConditional cond = make().Conditional(increasing.makeIdent(), 
                    make().Binary(JCTree.LE, make().Binary(JCTree.MINUS, varname.makeIdent(), end.makeIdent()), makeZero()), 
                    make().Binary(JCTree.GE, make().Binary(JCTree.MINUS, varname.makeIdent(), end.makeIdent()), makeZero()));
            List<JCExpressionStatement> step = List.<JCExpressionStatement>of(make().Exec(make().Assignop(JCTree.PLUS_ASG, varname.makeIdent(), incr.makeIdent())));
            result.append(make().ForLoop(
                    List.<JCStatement>of(init), 
                    cond, 
                    step, 
                    make().Block(0, blockStatements)));
            
            return result;
        }
        private JCExpression makeIncreasingIncrement(SyntheticName by) {
            if (increment != null) {
                // long incr = increasing ? by : -by;
                return by.makeIdent();
            } else if (type.tag == syms().intType.tag) {
                // long incr = start < end ? 1 : -1
                return make().Literal(1);
            } else if (type.tag == syms().longType.tag) {
                return make().Literal(1L);
            } else {
                return make().Erroneous();
            }
        }
        private JCExpression makeDecreasingIncrement(SyntheticName by) {
            if (increment != null) {
                // long incr = increasing ? by : -by;
                return make().Unary(JCTree.NEG, by.makeIdent());
            } else if (type.tag == syms().intType.tag) {
                // long incr = start < end ? 1 : -1
                return make().Literal(-1);
            } else if (type.tag == syms().longType.tag) {
                return make().Literal(-1L);
            } else {
                return make().Erroneous();
            }
        }
        private JCExpression makeZero() {
            if (type.tag == syms().intType.tag) {
                return make().Literal(0);
            } else if (type.tag == syms().longType.tag) {
                return make().Literal(0L);
            } else {
                return make().Erroneous();
            }
        }
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
                // If this is a return statement in a MPL method we want to know 
                // the non-widening type of the innermost callable
                if (declaration instanceof Functional
                        && Decl.isMpl((Functional)declaration)) {
                    for (int i = ((Functional)declaration).getParameterLists().size(); i > 1; i--) {
                        nonWideningType = getReturnTypeOfCallable(nonWideningType);
                    }
                }
                // respect the refining definition of optionality
                nonWideningType = propagateOptionality(declaration.getType(), nonWideningType);
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

    private ProducedType propagateOptionality(ProducedType type, ProducedType nonWideningType) {
        if(!isNull(type)){
            if(isOptional(type)){
                if(!isOptional(nonWideningType)){
                    return typeFact().getOptionalType(nonWideningType);
                }
            }else{
                if(isOptional(nonWideningType)){
                    return typeFact().getDefiniteType(nonWideningType);
                }
            }
        }
        return nonWideningType;
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
        Substitution prevSubst = naming.addVariableSubst(isCase.getVariable().getDeclarationModel(), substVarName.toString());

        JCBlock block = transform(caseClause.getBlock());
        List<JCStatement> stats = List.<JCStatement> of(decl2);
        stats = stats.appendList(block.getStatements());
        block = at(isCase).Block(0, stats);

        // Deactivate the above variable substitution
        prevSubst.close();

        last = make().If(cond, block, last);
        return last;
    }
}
