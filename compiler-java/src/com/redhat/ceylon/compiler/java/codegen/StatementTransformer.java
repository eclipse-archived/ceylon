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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.common.BooleanUtil;
import com.redhat.ceylon.compiler.java.codegen.Naming.CName;
import com.redhat.ceylon.compiler.java.codegen.Naming.Substitution;
import com.redhat.ceylon.compiler.java.codegen.Naming.SyntheticName;
import com.redhat.ceylon.compiler.java.codegen.recovery.HasErrorException;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Break;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CaseClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Condition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Continue;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ForStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.RangeOp;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Return;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierOrInitializerExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Statement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SwitchCaseList;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SwitchClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Switched;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.loader.NamingBase.Suffix;
import com.redhat.ceylon.model.loader.NamingBase.Unfix;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.ConditionScope;
import com.redhat.ceylon.model.typechecker.model.ControlBlock;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;
import com.sun.tools.javac.code.Flags;
//import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.main.OptionName;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCase;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCConditional;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCForLoop;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCIf;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.tree.JCTree.JCTry;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.DiagnosticSource;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Options;

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

    private final Set<Optimization> disabledOptimizations;
    
    private final Transformer<JCStatement, Tree.Return> defaultReturnTransformer = new DefaultReturnTransformer();
    private Transformer<JCStatement, Tree.Return> returnTransformer = defaultReturnTransformer;
    private final Transformer<List<JCStatement>, Tree.Break> defaultBreakTransformer = new DefaultBreakTransformer();
    private final Transformer<JCStatement, Tree.Continue> defaultContinueTransformer = new DefaultContinueTransformer();
    private Transformer<JCStatement, Tree.Continue> continueTransformer = defaultContinueTransformer;
    
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
        Options options = context.get(Options.optionsKey);
        if (options.isSet(OptionName.CEYLONDISABLEOPT)) {
            disabledOptimizations = EnumSet.allOf(Optimization.class);
        } else if (options.isSet(OptionName.CEYLONDISABLEOPT_CUSTOM)) {
            disabledOptimizations = new HashSet<Optimization>();
            for (String name : options.get(OptionName.CEYLONDISABLEOPT_CUSTOM).split(",")) {
                disabledOptimizations.add(Optimization.valueOf(name));
            }
        } else {
            disabledOptimizations = EnumSet.noneOf(Optimization.class);
        }
        
    }

    public JCBlock transform(Tree.Block block) {
        return block == null ? null : at(block).Block(0, transformBlock(block));
    }
    
    public List<JCStatement> transformBlock(Tree.Block block) {
        return transformBlock(block, false);
    }
    public List<JCStatement> transformBlock(Tree.Block block, boolean revertRet) {
        if (block == null) {
            return List.<JCStatement>nil();
        }
        at(block);
        CeylonVisitor v = gen().visitor;
        final ListBuffer<JCTree> prevDefs = v.defs;
        final boolean prevInInitializer = v.inInitializer;
        final ClassDefinitionBuilder prevClassBuilder = v.classBuilder;
        List<JCStatement> result;
        try {
            v.defs = new ListBuffer<JCTree>();
            v.inInitializer = false;
            v.classBuilder = current();
            java.util.Iterator<Statement> statements = block.getStatements().iterator();
            while (statements.hasNext()) {
                Tree.Statement stmt = statements.next();
                Transformer<JCStatement, Return> returnTransformer;
                if (revertRet 
                        && stmt instanceof Tree.Declaration) {
                    returnTransformer = returnTransformer(defaultReturnTransformer);
                } else {
                    returnTransformer = this.returnTransformer;
                }
                try {
                    HasErrorException error = errors().getFirstErrorBlock(stmt);
                    if (error == null) {
                        stmt.visit(v);
                    } else {
                        v.append(this.makeThrowUnresolvedCompilationError(error));
                        break;
                    }
                } finally {
                    returnTransformer(returnTransformer);
                }
            }
            result = (List<JCStatement>)v.getResult().toList();
        } finally {
            v.classBuilder = prevClassBuilder;
            v.inInitializer = prevInInitializer;
            v.defs = prevDefs;
            // Close Substitutions which were scoped to this block
            Scope scope = block.getScope();
            while (scope instanceof ConditionScope) {
                scope = scope.getScope();
            }
            naming.closeScopedSubstitutions(scope);
        }
        return result;
    }
    
    /**
     * Helper for constructing {@code throw AssertionException()} statements
     * @author tom
     */
    class AssertionExceptionMessageBuilder {
        
        private JCExpression expr;
        
        private JCExpression cat(JCExpression str1, JCExpression str2) {
            if (str2 == null) {
                return str1;
            }
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
        
        public AssertionExceptionMessageBuilder(JCExpression expr) {
            this.expr = expr;
        }
        
        public AssertionExceptionMessageBuilder prependAssertionDoc(Tree.Assertion ass) {
            return prependAssertionDoc(getDocAnnotationText(ass));
        }
        
        public AssertionExceptionMessageBuilder prependAssertionDoc(String docText) {
            JCExpression p = make().Literal("Assertion failed");
            if (docText != null) {
                p = cat(p, ": " + docText);
            }
            this.expr = cat(p, expr);
            return this;
        }
        
        private AssertionExceptionMessageBuilder appendCondition(String state, String sourceCode) {
            JCExpression m = cat(newline(), make().Literal("\t" + state+ " "));
            if (expr != null) {
                expr = cat(expr, m);
            } else {
                expr = m;
            }
            expr = cat(expr, make().Literal(sourceCode));
            
            return this;
        }
        
        public AssertionExceptionMessageBuilder appendViolatedCondition(String sourceCode) {
            return appendCondition("violated", sourceCode);
        }
        
        public AssertionExceptionMessageBuilder appendViolatedCondition(Tree.Condition condition) {
            return appendViolatedCondition(getSourceCode(condition));
        }
        
        public AssertionExceptionMessageBuilder appendUnviolatedCondition(Tree.Condition condition) {
            return appendCondition("unviolated", getSourceCode(condition));
        }
        
        public AssertionExceptionMessageBuilder appendUntestedCondition(Tree.Condition condition) {
            return appendCondition("untested", getSourceCode(condition));
        }
        
        public JCExpression build() {
            return expr;
        }
    }
    
    abstract class CondList {
        protected final Node thenPart;
        protected final java.util.List<Tree.Condition> conditions;
        
        public CondList(java.util.List<Tree.Condition> conditions, Tree.Block thenPart) {
            this.conditions = conditions;
            this.thenPart = thenPart;
        }
        public CondList(java.util.List<Tree.Condition> conditions, Tree.Expression thenPart) {
            this.conditions = conditions;
            this.thenPart = thenPart;
        }
        

        protected Cond getConditionTransformer(Tree.Condition cond) {
            return getConditionTransformer(cond, null);
        }

        protected Cond getConditionTransformer(Tree.Condition cond, Tree.Variable elseVariable) {
            if (cond instanceof Tree.IsCondition) {
                Tree.IsCondition is = (Tree.IsCondition)cond;
                IsVarTrans var = new IsVarTrans(is.getVariable());
                IsVarTrans elseVar = (elseVariable != null) ? new IsVarTrans(elseVariable, var.getTestVariableName()) : null;
                return new IsCond(is, var, elseVar);
            } else if (cond instanceof Tree.ExistsCondition) {
                Tree.ExistsCondition exists = (Tree.ExistsCondition)cond;
                ExistsVarTrans var = new ExistsVarTrans(exists.getVariable());
                ExistsVarTrans elseVar = (elseVariable != null) ? new ExistsVarTrans(elseVariable, var.getTestVariableName()) : null;
                return new ExistsCond(exists, var, elseVar);
            } else if (cond instanceof Tree.NonemptyCondition) {
                Tree.NonemptyCondition nonempty = (Tree.NonemptyCondition)cond;
                NonemptyVarTrans var = new NonemptyVarTrans(nonempty.getVariable());
                NonemptyVarTrans elseVar = (elseVariable != null) ? new NonemptyVarTrans(elseVariable, var.getTestVariableName()) : null;
                return new NonemptyCond(nonempty, var, elseVar);
            } else if (cond instanceof Tree.BooleanCondition) {
                return new BooleanCond((Tree.BooleanCondition)cond);
            }
            throw BugException.unhandledNodeCase(cond);
        }
        
        protected List<JCStatement> transformList(java.util.List<Tree.Condition> conditions) {
            Tree.Condition condition = conditions.get(0);
            at(condition);
            if (conditions.size() == 1) {
                return transformInnermost(condition);
            } else {
                return transformIntermediate(condition, conditions.subList(1, conditions.size()));
            }
        }

        protected abstract List<JCStatement> transformInnermost(Tree.Condition condition);
        
        protected List<JCStatement> transformIntermediate(Tree.Condition condition, java.util.List<Tree.Condition> rest) {
            return transformList(rest);
        }
        
        public abstract List<JCStatement> getResult();
    }
    
    abstract class BlockCondList extends CondList {

        /* Name of the variable in which to store the result of the blocks if thenPart is a Tree.Expression */
        protected final String tmpVar;
        /* The outer expression that we are evaluating, to get boxing/erasure info from */
        protected final Tree.Term outerExpression;
        /* The expected type that we are evaluating, to get nullable info from */
        protected final Type expectedType;
        
        public BlockCondList(java.util.List<Tree.Condition> conditions,
                Tree.Block thenPart) {
            super(conditions, thenPart);
            tmpVar = null;
            outerExpression = null;
            expectedType = null;
        }

        public BlockCondList(java.util.List<Tree.Condition> conditions,
                Tree.Expression thenPart, String tmpVar, Tree.Term outerExpression, Type expectedType) {
            super(conditions, thenPart);
            this.tmpVar = tmpVar;
            this.outerExpression = outerExpression;
            this.expectedType = expectedType;
        }

        @Override
        protected final List<JCStatement> transformInnermost(Tree.Condition condition) {
            Cond transformedCond = getConditionTransformer(condition);
            // Note: The innermost test happens outside the substitution scope
            JCExpression test = transformedCond.makeTest();
            
            List<JCStatement> elseStmts;
            java.util.List<Tree.Condition> rest = Collections.<Tree.Condition>emptyList();
            if (transformedCond.getElseVarTrans() != null) {
                List<Substitution> subs = getSubstitutions(transformedCond.getElseVarTrans());
                elseStmts = transformInnermostElse(transformedCond, rest);
                elseStmts = transformCommonResultDecl(transformedCond.getElseVarTrans(), elseStmts);
                closeSubstitutions(subs);
            } else {
                elseStmts = transformInnermostElse(transformedCond, rest);
            }

            List<Substitution> subs = getSubstitutions(transformedCond.getVarTrans());
            List<JCStatement> stmts = transformInnermostThen(transformedCond);
            stmts = transformCommonResultDecl(transformedCond.getVarTrans(), stmts);
            closeSubstitutions(subs);
            
            stmts = transformCommon(transformedCond, rest, test, stmts, elseStmts);
            
            return stmts;
        }
        
        protected List<Substitution> getSubstitutions(VarTrans vartrans) {
            if (vartrans.hasResultDecl()) {
                List<Substitution> subs = List.nil();
                List<VarDefBuilder> vars = vartrans.getVarDefBuilders();
                for (VarDefBuilder v : vars) {
                    subs = subs.append(naming.substituteAlias(v.var.getDeclarationModel()));
                }
                return subs;
            }
            return null;
        }
        
        protected void closeSubstitutions(List<Substitution> subs) {
            if (subs != null) {
                for (Substitution s : subs) {
                    s.close();
                }
            }
        }
        
        @Override
        protected List<JCStatement> transformIntermediate(Tree.Condition condition, java.util.List<Tree.Condition> rest) {
            Cond intermediate = getConditionTransformer(condition);
            JCExpression test = intermediate.makeTest();
            List<Substitution> subs = getSubstitutions(intermediate.getVarTrans());
            List<JCStatement> stmts = transformList(rest);
            stmts = transformCommonResultDecl(intermediate.getVarTrans(), stmts);
            List<JCStatement> intermediateElse = transformIntermediateElse(intermediate, rest);
            stmts = transformCommon(intermediate, rest, test, 
                    stmts, intermediateElse);
            closeSubstitutions(subs);
            return stmts;
        }

        protected JCStatement makeDefaultAssignment(Type type, CName name) {
            return make().Exec(make().Assign(name.makeIdent(), 
                    makeDefaultExprForType(type)));        
        }
        
        protected abstract List<JCStatement> transformInnermostThen(Cond cond);
        protected abstract List<JCStatement> transformInnermostElse(Cond cond, java.util.List<Tree.Condition> rest);
        protected abstract List<JCStatement> transformIntermediateElse(Cond cond, java.util.List<Tree.Condition> rest);
        protected abstract List<JCStatement> transformCommon(Cond cond, java.util.List<Tree.Condition> rest, JCExpression test, List<JCStatement> stmts, List<JCStatement> elseStmts);
        protected abstract List<JCStatement> transformCommonResultDecl(VarTrans var, List<JCStatement> stmts);
    }
    
    class IfCondList extends BlockCondList {

        final ListBuffer<JCStatement> varDecls = ListBuffer.lb();
        final SyntheticName ifVar = naming.temp("if");
        private List<JCStatement> unassignedResultVars = List.nil();
        private JCBlock thenBlock;
        private Tree.Variable elseVar;
        private Node elsePart;
        
        public IfCondList(java.util.List<Tree.Condition> conditions, Tree.Block thenPart,
                Tree.Variable elseVar, Tree.Block elsePart) {
            super(conditions, thenPart);
            this.elseVar = elseVar;
            this.elsePart = elsePart;
        }

        public IfCondList(java.util.List<Tree.Condition> conditions, Tree.Expression thenPart,
                Tree.Variable elseVar, Tree.Expression elsePart, String tmpVar, Tree.Term outerExpression, Type expectedType) {
            super(conditions, thenPart, tmpVar, outerExpression, expectedType);
            this.elseVar = elseVar;
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
        protected Cond getConditionTransformer(Condition cond) {
            return getConditionTransformer(cond, elseVar);
        }

        @Override
        protected List<JCStatement> transformIntermediateElse(Cond cond, java.util.List<Tree.Condition> rest) {
            return null;
        }

        private boolean isThenDefinitelyReturns(){
            return isDefinitelyReturns(thenPart);
        }

        private boolean isElseDefinitelyReturns(){
            return isDefinitelyReturns(elsePart);
        }

        private boolean isDefinitelyReturns(Node thenPart){
            if(thenPart instanceof Tree.Block)
                return ((Tree.Block)thenPart).getDefinitelyReturns();
            else if(thenPart instanceof Tree.Expression)
                return false; // I guess?
            else
                return false;
        }

        @Override
        protected List<JCStatement> transformInnermostThen(Cond cond) {
            List<JCStatement> stmts;
            if (definitelyNotSatisfied(conditions)
                    && !isThenDefinitelyReturns()
                    && (elsePart != null && isElseDefinitelyReturns())) {
                stmts = List.<JCStatement>of(makeFlowAppeaser(conditions.get(0)));
            } else if (isDeferred()) {
                stmts = List.<JCStatement>of(make().Exec(make().Assign(ifVar.makeIdent(), makeBoolean(true))));
                thenBlock = makeThenBlock(cond, thenPart, null, tmpVar, outerExpression, expectedType);
            } else {
                stmts = makeThenBlock(cond, thenPart, null, tmpVar, outerExpression, expectedType).getStatements();
            }
            return stmts;
        }

        @Override
        protected List<JCStatement> transformInnermostElse(Cond cond, java.util.List<Tree.Condition> rest) {
            List<JCStatement> stmts = null;
            if (elsePart != null && !isDeferred()) {
                if (this.elsePart instanceof Tree.Block) {
                    stmts = transformBlock((Tree.Block)this.elsePart);
                } else if (this.elsePart instanceof Tree.Expression) {
                    stmts = evaluateAndAssign(tmpVar, (Tree.Expression)this.elsePart, outerExpression, expectedType);
                } else {
                    stmts = List.<JCStatement>of(make().Exec(makeErroneous(thenPart, "Only block or expression allowed")));
                }
            }
            return stmts;
        }
        
        @Override
        protected List<JCStatement> transformCommon(Cond cond, 
                java.util.List<Tree.Condition> rest, JCExpression test, List<JCStatement> stmts, List<JCStatement> elseStmts) {
            JCStatement testVarDecl = cond.getVarTrans().makeTestVarDecl(0, false);
            if (testVarDecl == null && cond.getElseVarTrans() != null) {
                testVarDecl = cond.getElseVarTrans().makeTestVarDecl(0, false);
            }
            if (testVarDecl != null) {
                varDecls.prepend(testVarDecl);
            }
            if (isDeferred()) {
                elseStmts = unassignedResultVars.isEmpty() ? null : unassignedResultVars;
            }
            stmts = List.<JCStatement>of(make().If(
                    test, 
                    make().Block(0, stmts), 
                    makeElseBlock(elseStmts)));
            return stmts;
        }

        protected List<JCStatement> transformCommonResultDecl(
                VarTrans vartrans, List<JCStatement> stmts) {
            if (vartrans.hasResultDecl()) {
                List<VarDefBuilder> vars = vartrans.getVarDefBuilders();
                for (VarDefBuilder vdb : vars) {
                    if (isDeferred()) {
                        unassignedResultVars = unassignedResultVars.prepend(make().Exec(vdb.buildDefaultAssign()));
                        varDecls.prepend(vdb.buildDefOnly());
                        stmts = stmts.prepend(make().Exec(vdb.buildAssign()));
                    } else {
                        stmts = stmts.prepend(vdb.build());
                    }
                }
            }
            return stmts;
        }
        
        @Override
        public List<JCStatement> getResult() {
            List<JCStatement> stmts = transformList(conditions);
            if (definitelySatisfied(conditions)
                    && isThenDefinitelyReturns() 
                    && (elsePart == null || !isElseDefinitelyReturns())) {
                stmts = stmts.append(makeFlowAppeaser(conditions.get(0)));
            }
            ListBuffer<JCStatement> result = ListBuffer.lb();
            if (isDeferred()) {
                result.append(makeVar(ifVar, make().Type(syms().booleanType), makeBoolean(false)));
            }
            result.appendList(varDecls);
            result.appendList(stmts);
            if (isDeferred()) {
                JCBlock elseBlock;
                if(elsePart instanceof Tree.Block)
                    elseBlock = StatementTransformer.this.transform((Tree.Block)elsePart);
                else if(elsePart instanceof Tree.Expression)
                    elseBlock = at(elsePart).Block(0, evaluateAndAssign(tmpVar, (Tree.Expression)elsePart, outerExpression, expectedType));
                else if(elsePart == null)
                    elseBlock = null;
                else
                    elseBlock = at(elsePart).Block(0, List.<JCStatement>of(make().Exec(makeErroneous(thenPart, "Only block or expression allowed"))));
                    
                result.append(make().If(ifVar.makeIdent(), thenBlock, elseBlock));
            }
            return result.toList();   
        }

         
    }
    
    private boolean definitelySatisfiedOrNot(java.util.List<Tree.Condition> conditions, boolean satisfied) {
        if (conditions.size() != 1) {
            return false;
        }
        Tree.Condition condition = conditions.get(0);
        if (!(condition instanceof Tree.BooleanCondition)) {
            return false;
        }
        Tree.Term term = ((Tree.BooleanCondition)condition).getExpression().getTerm();
        if (!(term instanceof Tree.BaseMemberExpression)) {
            return false;
        }
        Declaration declaration = ((Tree.BaseMemberExpression)term).getDeclaration();
        return declaration instanceof Value
                && satisfied ? isBooleanTrue(declaration) : isBooleanFalse(declaration);
    }
    
    boolean definitelySatisfied(java.util.List<Tree.Condition> conditions) {
        return definitelySatisfiedOrNot(conditions, true);
    }
    
    boolean definitelyNotSatisfied(java.util.List<Tree.Condition> conditions) {
        return definitelySatisfiedOrNot(conditions, false);
    }
    
    /**
     * Sometimes we need something to appease javacs flow analysis.
     */
    JCStatement makeFlowAppeaser(Node node) {
        at(node);
        return make().Throw(make().NewClass(null, List.<JCExpression>nil(), 
                make().Type(syms().errorType), 
                List.<JCExpression>of(make().Literal("Ceylon flow error")), 
                        null));
    }
    
    List<JCStatement> transform(Tree.IfStatement stmt) {
        Tree.Block thenPart = stmt.getIfClause().getBlock();
        Tree.Block elsePart = (stmt.getElseClause() != null) ? stmt.getElseClause().getBlock() : null;
        java.util.List<Tree.Condition> conditions = stmt.getIfClause().getConditionList().getConditions();
        Tree.Variable elseVar = (stmt.getElseClause() != null) ? stmt.getElseClause().getVariable() : null;
        return transformIf(conditions, thenPart, elseVar, elsePart);
    }

    List<JCStatement> transformIf(java.util.List<Condition> conditions, Tree.Block thenPart, Tree.Variable elseVar, Tree.Block elsePart) {
        return new IfCondList(conditions, thenPart, elseVar, elsePart).getResult();
    }

    List<JCStatement> transformIf(java.util.List<Condition> conditions, Tree.Expression thenPart, Tree.Variable elseVar, Tree.Expression elsePart, String tmpVar, Tree.Term outerExpression, Type expectedType) {
        return new IfCondList(conditions, thenPart, elseVar, elsePart, tmpVar, outerExpression, expectedType).getResult();
    }

    private List<JCStatement> evaluateAndAssign(String tmpVar, Tree.Expression expr, Tree.Term outerExpression, Type expectedType){
        at(expr);
        if(expectedType == null)
            expectedType = outerExpression.getTypeModel();
        if (!expectedType.getDeclaration().isAnonymous()) {
            expectedType = typeFact().denotableType(expectedType);
        }
        BoxingStrategy boxingStrategy = CodegenUtil.getBoxingStrategy(outerExpression);
        
        return List.<JCStatement>of(make().Exec(make().Assign(
                makeUnquotedIdent(tmpVar), 
                expressionGen().transformExpression(expr, boxingStrategy, 
                        expectedType))));
    }
    
    private JCBlock makeThenBlock(Cond cond, Node thenPart, Substitution subs, String tmpVar, Tree.Term outerExpression, Type expectedType) {
        List<JCStatement> blockStmts;
        if(thenPart instanceof Tree.Block)
            blockStmts = statementGen().transformBlock((Tree.Block)thenPart);
        else if(thenPart instanceof Tree.Expression){
            blockStmts = evaluateAndAssign(tmpVar, (Tree.Expression)thenPart, outerExpression, expectedType);
        }else if(thenPart == null){
            blockStmts = List.<JCStatement>nil();
        }else{
            blockStmts = List.<JCStatement>of(make().Exec(makeErroneous(thenPart, "Only block or expression allowed")));
        }
        if (subs != null) {
            // The variable holding the result for the code inside the code block
            blockStmts = blockStmts.prepend(at(cond.getCondition()).VarDef(make().Modifiers(FINAL), names().fromString(subs.substituted), 
                    cond.getVarTrans().makeTypeExpr(), cond.getVarTrans().makeResultExpr()));
        }
        JCBlock thenBlock = at(cond.getCondition()).Block(0, blockStmts);
        return thenBlock;
    }
    
    protected JCStatement makeElseBlock(List<JCStatement> stmts) {
        if (stmts != null) {
            return make().Block(0, stmts);
        } else {
            return null;
        }
    }
    
    List<JCStatement> transform(Tree.WhileStatement stmt) {
        Name tempForFailVariable = currentForFailVariable;
        currentForFailVariable = null;
        final List<JCStatement> res;
        res =  new WhileCondList(stmt.getWhileClause()).getResult();
        currentForFailVariable = tempForFailVariable;
        
        return res;
    }
    
    class WhileCondList extends BlockCondList {

        private final ListBuffer<JCStatement> varDecls = ListBuffer.lb();
        private final Name label;
        public WhileCondList(Tree.WhileClause whileClause) {
            super(whileClause.getConditionList().getConditions(), whileClause.getBlock());
            this.label = getLabel(whileClause.getControlBlock());
        }
        
        @Override
        protected List<JCStatement> transformIntermediateElse(Cond cond, java.util.List<Tree.Condition> rest) {
            return List.<JCStatement>of(make().Break(label));
        }

        @Override
        protected List<JCStatement> transformInnermostThen(Cond cond) {
            return makeThenBlock(cond, thenPart, null, null /* while is not an expression yet */, null, null).getStatements();   
        }

        @Override
        protected List<JCStatement> transformInnermostElse(Cond cond, java.util.List<Tree.Condition> rest) {
            return List.<JCStatement>of(make().Break(label));
        }
        
        @Override
        protected List<JCStatement> transformCommon(Cond cond, 
                java.util.List<Tree.Condition> rest, JCExpression test, List<JCStatement> stmts, List<JCStatement> elseStmts) {
            if (cond.getVarTrans().makeTestVarDecl(0, false) != null) {
                varDecls.append(cond.getVarTrans().makeTestVarDecl(0, false));
            }
            stmts = List.<JCStatement>of(make().If(
                    test, 
                    make().Block(0, stmts),
                    makeElseBlock(elseStmts)));
            return stmts;
        }

        protected List<JCStatement> transformCommonResultDecl(
                VarTrans vartrans, List<JCStatement> stmts) {
            if (vartrans.hasResultDecl()) {
                List<VarDefBuilder> vars = vartrans.getVarDefBuilders();
                for (VarDefBuilder v : vars) {
                    stmts = stmts.prepend(v.build());
                }
            }
            return stmts;
        }
        
        @Override
        public List<JCStatement> getResult() {
            List<JCStatement> stmts = transformList(conditions);
            ListBuffer<JCStatement> loopStmts = ListBuffer.lb();
            loopStmts.appendList(varDecls);
            loopStmts.appendList(stmts);
            List<JCStatement> result = List.nil(); 
            if (definitelySatisfied(conditions)) {
                BreakVisitor v = new BreakVisitor();
                thenPart.visit(v);
                if (!v.breaks) {
                    result = result.prepend(makeFlowAppeaser(conditions.get(0)));
                }
            }
            
            result = result.prepend(
                    make().Labelled(label, make().WhileLoop(makeBoolean(true), 
                    make().Block(0, loopStmts.toList()))));
            return result;
        }

    }
    
    class BreakVisitor extends Visitor {
        private boolean breaks = false;
        
        
        public void visit(Tree.WhileStatement stmt) {
            // We're not interested in breaks in that loop
        }
        
        public void visit(Tree.ForStatement stmt) {
            // We're not interested in breaks in that loop
        }
        
        public void visit(Tree.Declaration stmt) {
            // We're not interested in breaks in loops within that declaration
        }
        
        public void visit(Tree.Break stmt) {
            breaks = true;
        }
    }
    
    List<JCStatement> transform(Tree.Assertion ass) {
        return new AssertCondList(ass).getResult();
    }
    
    class AssertCondList extends BlockCondList {
        private final Tree.Assertion ass;
        private final ListBuffer<JCStatement> varDecls = ListBuffer.lb();
        private final ListBuffer<JCStatement> fieldDecls = ListBuffer.lb();
        private final SyntheticName messageSb = naming.temp("assert");
        private List<JCStatement> unassignedResultVars = List.nil();
        
        public AssertCondList(Tree.Assertion ass) {
            super(ass.getConditionList().getConditions(), (Tree.Block)null);
            this.ass = ass;
        }
        
        /** Determines whether there's more than one Condition in the ConditionList */
        private boolean isMulti() {
            return this.conditions.size() > 1;
        }
        
        protected List<Substitution> getSubstitutions(VarTrans vartrans) {
            List<Substitution> subs = super.getSubstitutions(vartrans);
            if (subs == null) {
                return subs;
            }
            List<VarDefBuilder> vars = vartrans.getVarDefBuilders();
            Set<Scope> scopes = new HashSet<Scope>();
            for (VarDefBuilder v : vars) {
                Scope scope = v.var.getScope().getScope();
                while (scope instanceof ConditionScope) {
                    scope = scope.getScope();
                }
                scopes.add(scope);
                // make sure we get a variable name now, and that it doesn't change over time, because
                // we will need this variable name in transformCommonResultDecl(), which declares it,
                // and it runs after we process inner conditions, and if we are an assert, inner conditions
                // may declare new substitutions, which do not close until the end of the outer scope,
                // so if we use substitutions we will get substituted names, rather than the name we should
                // be declaring. See https://github.com/ceylon/ceylon-compiler/issues/1532
                v.name();
            }
            for (Scope scope : scopes) {
                for (Substitution s : subs) {
                    s.scopeClose(scope);
                }
            }
            return subs;
        }
        
        @Override
        protected List<JCStatement> transformCommon(Cond cond, 
                java.util.List<Tree.Condition> rest, 
                JCExpression test, List<JCStatement> stmts, List<JCStatement> elseStmts) {
            
            if (isMulti()) {
                elseStmts = elseStmts.prependList(unassignedResultVars);
            }
            stmts = List.<JCStatement>of(make().If(
                    test, 
                    make().Block(0, stmts), 
                    makeElseBlock(elseStmts)));
            
            JCStatement testVarDecl = cond.getVarTrans().makeTestVarDecl(0, true);
            if (testVarDecl != null) {
                stmts = stmts.prepend(testVarDecl);
            }
            return stmts;
        }

        protected List<JCStatement> transformCommonResultDecl(VarTrans vartrans,
                List<JCStatement> stmts) {
            if (vartrans.hasResultDecl()) {
                List<VarDefBuilder> vars = vartrans.getVarDefBuilders();
                for (VarDefBuilder v : vars) {
                    unassignedResultVars = unassignedResultVars.prepend(make().Exec(v.buildDefaultAssign()));
                    (Decl.getNonConditionScope(ass.getScope()) instanceof ClassOrInterface
                            && v.var.getDeclarationModel().isCaptured() ? fieldDecls : varDecls).append(v.buildDefOnly());
                    stmts = stmts.prepend(make().Exec(v.buildAssign()));
                }
            }
            return stmts;
        }
        
        @Override
        public List<JCStatement> getResult() {
            if (definitelyNotSatisfied(conditions)) {
                return List.<JCTree.JCStatement>of(makeThrowAssertionFailure(conditions.get(0)));
            }
            List<JCStatement> stmts = transformList(conditions);
            at(this.ass);
            ListBuffer<JCStatement> result = ListBuffer.lb();
            if (isMulti()) {
                result.append(makeVar(messageSb, make().Type(syms().stringType), makeNull()));
            }
            result.appendList(varDecls);
            current().defs((List)fieldDecls.toList());
            result.appendList(stmts);
            JCExpression message = new AssertionExceptionMessageBuilder(
                    messageSb.makeIdent()).prependAssertionDoc(ass).build();
            JCThrow throw_ = makeThrowAssertionException(message);
            if (isMulti()) {
                result.append(make().If(
                        make().Binary(JCTree.NE, messageSb.makeIdent(), makeNull()), 
                        throw_, null));
            }
            return result.toList();
        }

        private List<JCStatement> transformCommonElse(Cond cond, java.util.List<Tree.Condition> rest) {
            if (!isMulti()) {
                return null;
            }
            AssertionExceptionMessageBuilder msg = new AssertionExceptionMessageBuilder(null);
            boolean seen = false;
            for (Tree.Condition condition : this.conditions) {
                if (cond.getCondition() == condition) {
                    msg.appendViolatedCondition(condition);
                    seen = true;
                    continue;
                }
                if (seen) {
                    msg.appendUntestedCondition(condition);
                } else {
                    msg.appendUnviolatedCondition(condition);
                }
            }
            return List.<JCStatement>of( 
                    make().Exec(make().Assign(messageSb.makeIdent(), msg.build())));
        }
        
        @Override
        protected List<JCStatement> transformInnermostThen(Cond cond) {
            return List.nil();
        }

        @Override
        protected List<JCStatement> transformInnermostElse(Cond cond, java.util.List<Tree.Condition> rest) {
            if (!isMulti()) {
                return List.<JCStatement>of(makeThrowAssertionFailure(cond.getCondition()));
            }
            return transformCommonElse(cond, rest);
        }

        private JCStatement makeThrowAssertionFailure(Tree.Condition condition) {
            at(condition);
            AssertionExceptionMessageBuilder msg = new AssertionExceptionMessageBuilder(null);
            msg.appendViolatedCondition(condition);
            msg.prependAssertionDoc(ass);
            return makeThrowAssertionException(msg.build());
        }
        
        @Override
        protected List<JCStatement> transformIntermediateElse(Cond cond, java.util.List<Tree.Condition> rest) {
            return transformCommonElse(cond, rest);
        }
    }
    
    interface VarTrans {
        
        public Tree.Variable getVariable();
        public List<VarDefBuilder> getVarDefBuilders();
        
        public CName getVariableName();
        public CName getTestVariableName();
        
        public Tree.Expression getExpression();
        
        public boolean hasResultDecl();
        public boolean hasAliasedVariable();
        public boolean isDestructure();
        
        public JCExpression makeTypeExpr();
        public JCExpression makeResultExpr();
        public Type getType();
        public Type getResultType();
        
        public JCStatement makeTestVarDecl(int flags, boolean init);
    }
    
    abstract class BaseVarTransImpl implements VarTrans {
        protected final Type toType;
        private final boolean toTypeBoxed;
        private final Tree.Expression specifierExpr;
        private final Tree.Statement varOrDes;
        private final CName testVarName;
        private final List<VarDefBuilder> vdBuilders;
        
        private CName variableName;
        
        BaseVarTransImpl(Tree.Statement varOrDes) {
            this(varOrDes, naming.syntheticDestructure(varOrDes).alias());
        }
        
        BaseVarTransImpl(Tree.Statement varOrDes, CName testVarName) {
            this.specifierExpr = getDestructureExpression(varOrDes);
            this.varOrDes = varOrDes;
            this.testVarName = testVarName;
            Tree.Type type;
            if (varOrDes instanceof Tree.Variable) {
                Tree.Variable var = (Tree.Variable)varOrDes;
                type = var.getType();
                toTypeBoxed = !CodegenUtil.isUnBoxed(var.getDeclarationModel());
            } else if (varOrDes instanceof Tree.Destructure) {
                type = ((Tree.Destructure)varOrDes).getType();
                toTypeBoxed = false;
            } else {
                throw BugException.unhandledCase(varOrDes);
            }
            if (type != null) {
                this.toType = type.getTypeModel();
            } else {
                this.toType = specifierExpr.getTypeModel();
            }
            vdBuilders = transformDestructure(varOrDes, getTestVariableName().makeIdent(), getResultType(), true);
        }
        
        @Override
        public final Tree.Variable getVariable() {
            return (Tree.Variable)varOrDes;
        }
        
        @Override
        public List<VarDefBuilder> getVarDefBuilders() {
            return vdBuilders;
        }
        
        @Override
        public final boolean isDestructure() {
            return varOrDes instanceof Tree.Destructure;
        }
        
        @Override
        public final CName getVariableName() {
            // make sure once we get a variable it never changes name, because the whole code
            // generation of Cond depends on being able to call this method multiple times and
            // get the same result. See https://github.com/ceylon/ceylon-compiler/issues/1532
            if(variableName == null)
                variableName = naming.substituted(getVariable().getDeclarationModel()).capture();
            return variableName;
        }

        @Override
        public final CName getTestVariableName() {
            return testVarName;
        }
        
        @Override
        public final Tree.Expression getExpression() {
            return specifierExpr;
        }
        
        @Override
        public boolean hasResultDecl() {
            return true;
        }
        
        @Override
        public boolean hasAliasedVariable() {
            return !(getVariable().getType() instanceof Tree.SyntheticVariable)
                    || naming.isSubstituted(getVariable().getDeclarationModel().getOriginalDeclaration());
        }
        
        @Override
        public final Type getType() {
            return toType;
        }

        @Override
        public final JCExpression makeTypeExpr() {
            return makeJavaType(toType, (toTypeBoxed) ? AbstractTransformer.JT_NO_PRIMITIVES : 0);
        }

        @Override
        public JCStatement makeTestVarDecl(int flags, boolean init) {
            // Temporary variable holding the result of the expression/variable to test
            return make().VarDef(make().Modifiers(flags), testVarName.asName(), makeResultType(), init ? makeNull() : null);
        }

        protected JCExpression makeResultType() {
            return makeJavaType(getResultType(), JT_NO_PRIMITIVES);
        }

        @Override
        public Type getResultType() {
            Type exprType = getExpression().getTypeModel();
            if (isOptional(exprType)) {
                exprType = typeFact().getDefiniteType(exprType);
            }
            return exprType;
        }
        
    }
    
    class IsVarTrans extends BaseVarTransImpl {
        
        private IsVarTrans(Tree.Variable var) {
            super(var);
        }
        
        private IsVarTrans(Tree.Variable var, CName testVarName) {
            super(var, testVarName);
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
            return toType.isExactly(typeFact().getNothingType()) 
                    && ! hasAliasedVariable();
        }
        
        /**
         * Optimization: if no typecast will be required, and there's no 
         * aliasing then we don't need to declare a test var, and the result var
         * is simply the variable name.
         */
        private boolean isErasedToObjectOptimization() {
            return !typecastRequired() 
                    && !hasAliasedVariable() 
                    && !canUnbox(getExpression().getTypeModel());
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
        protected JCExpression makeResultType() {
            at(getVariable());
            return make().Type(syms().objectType);
        }
        
        @Override
        public Type getResultType() {
            return typeFact().getObjectType();
        }

        @Override
        public JCExpression makeResultExpr() {
            at(getVariable());
            JCExpression expr = getTestVariableName().makeIdent();
            
            if (typecastRequired()) {
                // Want raw type for instanceof since it can't be used with generic types
                JCExpression rawToTypeExpr = makeJavaType(toType, JT_NO_PRIMITIVES | JT_RAW);
                // Substitute variable with the correct type to use in the rest of the code block
                expr = at(getVariable()).TypeCast(rawToTypeExpr, expr);
                if ((getVariable().getDeclarationModel().getUnboxed() == true) && canUnbox(toType)) {
                    expr = unboxType(expr, toType);
                } 
            }
            return expr;
        }
    }
    
    class ExistsVarTrans extends BaseVarTransImpl {

        private ExistsVarTrans(Tree.Statement varOrDes) {
            super(varOrDes);
        }
        
        private ExistsVarTrans(Tree.Statement varOrDes, CName testVarName) {
            super(varOrDes, testVarName);
        }
        
        @Override
        public JCExpression makeResultExpr() {
            BoxingStrategy boxing;
            if (isDestructure()) {
                boxing = BoxingStrategy.BOXED;
            } else {
                Value decl = getVariable().getDeclarationModel();
                boxing = CodegenUtil.getBoxingStrategy(decl);
            }
            return expressionGen().applyErasureAndBoxing(getTestVariableName().makeIdent(),
                    getResultType(), willEraseToObject(toType), true,
                    boxing, toType, 0);
        }
    }
    
    class NonemptyVarTrans extends BaseVarTransImpl {

        private NonemptyVarTrans(Tree.Statement varOrDes) {
            super(varOrDes);
        }
        
        private NonemptyVarTrans(Tree.Statement varOrDes, CName testVarName) {
            super(varOrDes, testVarName);
        }
        
        @Override
        public JCExpression makeResultExpr() {
            Type exprType = getExpression().getTypeModel();
            if (isOptional(exprType)) {
                exprType = typeFact().getDefiniteType(exprType);
            }
            Type expectedType = getVariable().getDeclarationModel().getType();
            return expressionGen().applyErasureAndBoxing(getTestVariableName().makeIdent(),
                    exprType, false, true,
                    BoxingStrategy.BOXED,
                    expectedType,
                    ExpressionTransformer.EXPR_DOWN_CAST);
        }
        
    }
    
    interface Cond {
        
        public Tree.Condition getCondition();
        public VarTrans getVarTrans();
        public VarTrans getElseVarTrans();
        
        public JCExpression makeTest();
    }
    
    abstract class SpecialFormCond<C extends Tree.Condition, V extends VarTrans> implements Cond {
        protected final C cond;
        protected final V var;
        protected final V elseVar;
        protected final boolean negate;
        
        SpecialFormCond(C cond, V var, V elseVar, boolean negate) {
            this.cond = cond;
            this.var = var;
            this.elseVar = elseVar;
            this.negate = negate;
        }
        
        @Override
        public final C getCondition() {
            return cond;
        }
        
        @Override
        public final V getVarTrans() {
            return var;
        }
        
        @Override
        public final V getElseVarTrans() {
            return elseVar;
        }
        
    }
    
    class IsCond extends SpecialFormCond<Tree.IsCondition, IsVarTrans> {
        
        private IsCond(Tree.IsCondition isdecl, IsVarTrans var, IsVarTrans elseVar) {
            super(isdecl, var, elseVar, isdecl.getNot());
        }
        
        @Override
        public JCExpression makeTest() {
            Type expressionType;
            if(cond.getVariable().getSpecifierExpression() != null)
                expressionType = cond.getVariable().getSpecifierExpression().getExpression().getTypeModel();
            else
                expressionType = cond.getVariable().getDeclarationModel().getOriginalDeclaration().getType();

            // make sure we do not insert null checks if we're going to allow testing for null
            Type specifierType = negate ? 
                    expressionType : getOptionalTypeForInteropIfAllowed(cond.getType().getTypeModel(), expressionType, var.getExpression());
            // no need to cast for erasure here
            JCExpression expr = expressionGen().transformExpression(var.getExpression(), BoxingStrategy.BOXED, specifierType);
            at(cond);
            // Assign the expression to test to the temporary variable
            boolean useTempVar = !var.isErasedToObjectOptimization() && !var.isNothingOptimization();
            if (elseVar != null) {
                useTempVar = useTempVar || (!elseVar.isErasedToObjectOptimization() && !elseVar.isNothingOptimization());
            }
            if (useTempVar) {
                expr = make().Assign(var.getTestVariableName().makeIdent(), expr);
            }
            
            // Test on the tmpVar in the following condition
            expr = makeOptimizedTypeTest(expr, var.isErasedToObjectOptimization() ? var.getVariableName() : var.getTestVariableName(),
                    // only test the types we're testing for, not the type of
                    // the variable (which can be more precise)
                    cond.getType().getTypeModel(), expressionType);
            if (negate) {
                expr = make().Unary(JCTree.NOT, expr);
            }
            return expr;
        }

    }
    
    class ExistsCond extends SpecialFormCond<Tree.ExistsCondition, ExistsVarTrans> {

        private ExistsCond(Tree.ExistsCondition exists, ExistsVarTrans var, ExistsVarTrans elseVar) {
            super(exists, var, elseVar, exists.getNot());
        }
        
        @Override
        public JCExpression makeTest() {
            // for the purpose of checking if something is null, we need it boxed and optional, otherwise
            // for some Java calls if we consider it non-optional we will get an unwanted null check
            Type specifierType = var.getExpression().getTypeModel();
            if(!typeFact().isOptionalType(specifierType)){
                specifierType = typeFact().getOptionalType(specifierType);
            }
            JCExpression expr = expressionGen().transformExpression(var.getExpression(), BoxingStrategy.BOXED, specifierType);
            at(cond);
            // Assign the expression to test to the temporary variable
            expr = make().Assign(var.getTestVariableName().makeIdent(), expr);
            // Test on the tmpVar in the following condition
            expr = make().Binary(JCTree.NE, expr, makeNull());
            if (negate) {
                expr = make().Unary(JCTree.NOT, expr);
            }
            return expr;
        }
    }
    
    class NonemptyCond extends SpecialFormCond<Tree.NonemptyCondition, NonemptyVarTrans> {

        private NonemptyCond(Tree.NonemptyCondition nonempty, NonemptyVarTrans var, NonemptyVarTrans elseVar) {
            super(nonempty, var, elseVar, nonempty.getNot());
        }
        
        @Override
        public JCExpression makeTest() {
            // no need to cast for erasure here
            JCExpression expr = expressionGen().transformExpression(var.getExpression());
            at(cond);
            // Assign the expression to test to the temporary variable
            expr = make().Assign(var.getTestVariableName().makeIdent(), expr);
            // Test on the tmpVar in the following condition
            expr = makeNonEmptyTest(expr);
            if (negate) {
                expr = make().Unary(JCTree.NOT, expr);
            }
            return expr;
        }
    }
    
    class BooleanCond implements Cond {
        private final Tree.BooleanCondition cond;
        private final VarTrans var;
        
        private BooleanCond(Tree.BooleanCondition booleanCondition) {
            super();
            this.cond = booleanCondition;
            this.var = new VarTrans() {

                @Override
                public Variable getVariable() {
                    return null;
                }

                @Override
                public List<VarDefBuilder> getVarDefBuilders() {
                    return null;
                }
                
                @Override
                public final boolean isDestructure() {
                    return false;
                }
                
                @Override
                public CName getVariableName() {
                    return null;
                }

                @Override
                public CName getTestVariableName() {
                    return null;
                }

                @Override
                public Expression getExpression() {
                    return null;
                }

                @Override
                public boolean hasResultDecl() {
                    return false;
                }

                @Override
                public boolean hasAliasedVariable() {
                    return false;
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
                public Type getType() {
                    return null;
                }

                @Override
                public Type getResultType() {
                    return null;
                }

                @Override
                public JCStatement makeTestVarDecl(int flags, boolean init) {
                    return null;
                }
                
            };
        }

        @Override
        public JCExpression makeTest() {
            at(cond);
            return expressionGen().transformExpression(cond.getExpression(), 
                    BoxingStrategy.UNBOXED, typeFact().getBooleanType());
        }

        @Override
        public Tree.Condition getCondition() {
            return cond;
        }

        @Override
        public VarTrans getVarTrans() {
            return var;
        }

        @Override
        public VarTrans getElseVarTrans() {
            return var;
        }

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
                    throw new BugException(arg, "argument to doc annotation cannot be a spread argument or comprehension: " + arg);
            } else if (doc.getNamedArgumentList() != null) {
                Tree.SpecifiedArgument arg = (Tree.SpecifiedArgument)doc.getNamedArgumentList().getNamedArguments().get(0);
                expression = arg.getSpecifierExpression().getExpression();
            } else {
                // Impossible on a well-formed tree
                return null;
            }
            Tree.Literal literal = (Tree.Literal)expression.getTerm();
            docText = literal.getText();
        } else if (ass.getAnnotationList() != null
                && ass.getAnnotationList().getAnonymousAnnotation() != null) {
            docText = ass.getAnnotationList().getAnonymousAnnotation().getStringLiteral().getText();
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

    private Type actualType(Tree.TypedDeclaration decl) {
        return decl.getType().getTypeModel();
    }
    
    List<JCStatement> transform(Tree.ForStatement stmt) {
        Tree.Term iterableTerm = ExpressionTransformer.eliminateParens(stmt.getForClause().getForIterator().getSpecifierExpression().getExpression().getTerm());
        Tree.Term baseIterable = iterableTerm;
        Tree.Term step = null;
        if (iterableTerm instanceof Tree.InvocationExpression) {
            Tree.InvocationExpression invocation = (Tree.InvocationExpression)iterableTerm;
            if (invocation.getPrimary() instanceof Tree.QualifiedMemberExpression) {
                Tree.QualifiedMemberExpression qme = (Tree.QualifiedMemberExpression)invocation.getPrimary();
                Type primaryType = qme.getPrimary().getTypeModel();
                Type iterableType = primaryType.getSupertype(typeFact().getIterableDeclaration());
                if (iterableType != null) {
                    if ("by".equals(qme.getIdentifier().getText())) {
                        if (invocation.getPositionalArgumentList() != null) {
                            Tree.PositionalArgument positionalArgument = invocation.getPositionalArgumentList().getPositionalArguments().get(0);
                            if (positionalArgument instanceof Tree.ListedArgument) {
                                step = ((Tree.ListedArgument)positionalArgument).getExpression().getTerm();
                                baseIterable = ExpressionTransformer.eliminateParens(qme.getPrimary());
                            }
                        } else if (invocation.getNamedArgumentList() != null) {
                            Tree.NamedArgument positionalArgument = invocation.getNamedArgumentList().getNamedArguments().get(0);
                            if (positionalArgument instanceof Tree.SpecifiedArgument) {
                                step = ((Tree.SpecifiedArgument)positionalArgument).getSpecifierExpression().getExpression().getTerm();
                                baseIterable = ExpressionTransformer.eliminateParens(qme.getPrimary());
                            }
                        }
                    }
                }
            }
        }
        
        
        ForStatementTransformation transformation;
        
        transformation = stringIteration(stmt, baseIterable, step);
        if (transformation == null) {
            transformation = arrayIteration(stmt, baseIterable, step);
        }
        if (transformation == null) {
            transformation = segmentOpIteration(stmt, baseIterable, step);
        }
        if (transformation == null) {
            transformation = spanOpIteration(stmt);
        }
        if (transformation == null) {
            transformation = new ForStatementTransformation(stmt);
        }
        return transformation.transform();
    }
    
    /** 
     * Loop transformation when the iterated expression is statically known 
     * to be a {@code String}.
     * <pre>
        java.lang.String s = ITERABLE.value;
        int sz = s.codePointCount(0, s.length());
        for (int index = 0; index < sz; ) {
            int ITEM = s.codePointAt(index);
            index+= java.lang.Character.charCount(ITEM);
            
        }
       </pre>
       
     */
    class StringIterationOptimization extends ForStatementTransformation {

        private Tree.Term baseIterable;

        StringIterationOptimization(Tree.ForStatement stmt, Tree.Term baseIterable, Tree.Term step) {
            super(stmt);
            this.baseIterable = baseIterable;
        }
        
        protected ListBuffer<JCStatement> transformForClause() {
            ListBuffer<JCStatement> stmts = ListBuffer.<JCStatement>lb();
            
            SyntheticName stringName = naming.alias("s");
            stmts.append(makeVar(stringName, make().Type(syms().stringType), 
                    expressionGen().transformExpression(baseIterable, BoxingStrategy.UNBOXED, baseIterable.getTypeModel())));
            
            SyntheticName lengthName = naming.alias("length");
            stmts.append(makeVar(lengthName, make().Type(syms().intType),
                
                                make().Apply(null,
                                        makeQualIdent(stringName.makeIdent(), "length"),
                                        List.<JCExpression>nil())));
            
            SyntheticName indexName = naming.alias("index");
            
            List<JCStatement> transformedBlock = transformBlock(getBlock());
            
            Type charType = typeFact().getCharacterType();
            boolean elemBoxed = BooleanUtil.isFalse(getElementOrKeyVariable().getDeclarationModel().getUnboxed());
            
            JCExpression elemNameExpr = naming.makeQuotedIdent(Naming.getVariableName(getElementOrKeyVariable()));
            if (elemBoxed) {
                elemNameExpr = unboxType(elemNameExpr, charType);
            }
            transformedBlock = transformedBlock.prepend(make().Exec(
                    make().Assignop(JCTree.PLUS_ASG, indexName.makeIdent(), 
                        make().Apply(null, 
                                naming.makeQualIdent(make().Type(syms().characterObjectType), "charCount"), 
                                List.<JCExpression>of(elemNameExpr)))));
            
            JCExpression typeExpr = makeJavaType(charType, elemBoxed ? JT_NO_PRIMITIVES : 0);
            JCExpression codePointAtCallExpr = make().Apply(null, 
                    naming.makeQualIdent(stringName.makeIdent(), "codePointAt"), 
                    List.<JCExpression>of(indexName.makeIdent()));
            if (elemBoxed) {
                codePointAtCallExpr = boxType(codePointAtCallExpr, charType);
            }
            transformedBlock = transformedBlock.prepend(makeVar(FINAL,
                    Naming.getVariableName(getElementOrKeyVariable()), 
                    typeExpr, 
                    codePointAtCallExpr));
            
            JCStatement block = make().Block(0, transformedBlock);
            
            
            JCForLoop loop = make().ForLoop(
                    List.<JCStatement>of(makeVar(indexName, make().Type(syms().intType), make().Literal(0))), 
                    make().Binary(JCTree.LT, indexName.makeIdent(), lengthName.makeIdent()), 
                    List.<JCExpressionStatement>nil(), 
                    block);
            stmts.add(make().Labelled(this.label, loop));
            
            return stmts;
        }
    }
    
    private ForStatementTransformation stringIteration(Tree.ForStatement stmt,
            Tree.Term baseIterable, Tree.Term step) {
        if (step == null &&
                baseIterable.getTypeModel().getSupertype(typeFact().getStringDeclaration()) != null) {
            return new StringIterationOptimization(stmt, baseIterable, step);
        }
        return null;
    }

    /**
     * Optimized transformation for a {@code for} loop where the iterable is 
     * statically known to be immutable and support efficient indexed access, 
     * and therefore can be 
     * iterated using a C-style {@code for}.
     */
    abstract class IndexedAccessIterationOptimization extends ForStatementTransformation {
        
        protected final Type elementType;
        /** The name of the indexable variable */
        protected final SyntheticName indexableName;
        /** The name of the length variable */
        protected final SyntheticName lengthName;
        /** The name of the index variable */
        protected final SyntheticName indexName;
        protected final Tree.Term baseIterable;
        protected final Tree.Term step;
        protected final SyntheticName stepName;
        IndexedAccessIterationOptimization(Tree.ForStatement stmt, Tree.Term baseIterable, Tree.Term step, Type elementType, String indexableName) {
            this(stmt, baseIterable, step, elementType, indexableName, "length", "i");
        }
        
        IndexedAccessIterationOptimization(Tree.ForStatement stmt, Tree.Term baseIterable, Tree.Term step, Type elementType, String indexableName, String lengthName, String indexName) {
            super(stmt);
            this.baseIterable = baseIterable;
            this.step = step;
            if (step != null) {
                stepName = naming.alias("step");
            } else {
                stepName = null;
            }
            this.elementType = elementType;
            this.indexableName = naming.alias(indexableName);
            this.lengthName = naming.alias(lengthName);
            this.indexName = naming.alias(indexName);
        }
        
        protected final Tree.Term getIterable() {
            return baseIterable;
        }
        
        protected ListBuffer<JCStatement> transformForClause() {
            ListBuffer<JCStatement> result = ListBuffer.<JCStatement>lb();
            
            // java.lang.Object array = ITERABLE.toArray();
            result.add(makeVar(FINAL, indexableName,
                    makeIndexableType(),
                    makeIndexable()));
            
            // int length = java.lang.reflect.Array.getLength(array);
            JCExpression lengthExpr = makeLengthExpr();
            if (lengthExpr != null) {
                result.add(makeVar(FINAL, lengthName, 
                        makeIndexType(), 
                        lengthExpr));
            }
            
            // int step = ...
            
            if (this.step != null) {
                JCExpression stepExpr = makeStepExpr();
                
                result.add(makeVar(FINAL, stepName, 
                        makeIndexType(), 
                        stepExpr));
                result.add(
                make().If(
                        stepCheck(stepName),
                        makeThrowAssertionException(
                                new AssertionExceptionMessageBuilder(null)
                                    .appendViolatedCondition("step > 0")
                                    .prependAssertionDoc("step size must be greater than zero")
                                    .build()),
                        null));
                
            }
            
            // int i = 0;
            JCStatement iVar = makeVar(indexName, 
                    makeIndexType(), 
                    makeIndexInit());
            // i < length;
            JCExpression iCond = makeCondition();
            // i++
            JCExpression iIncr = makeIncrement(stepName);
            
            Tree.ControlClause prevControlClause = currentForClause;
            currentForClause = stmt.getForClause();
            List<JCStatement> transformedBlock = transformBlock(getBlock());
            currentForClause = prevControlClause;
            
            // FOO element = java.lang.reflect.Array.get(array, i);
            JCExpression elementGet = makeIndexedAccess();
            
            Tree.ForIterator forIterator = getForIterator();
            if (forIterator instanceof Tree.ValueIterator) {
                Tree.ValueIterator valIter = (Tree.ValueIterator)forIterator;
                JCStatement variable = transformVariable(valIter.getVariable(), elementGet, elementType, isIndexedAccessBoxed()).build();
                // Prepend to the block
                transformedBlock = transformedBlock.prepend(variable);
            } else if (forIterator instanceof Tree.PatternIterator) {
                Tree.PatternIterator patIter = (Tree.PatternIterator)forIterator;
                Tree.KeyValuePattern pat = (Tree.KeyValuePattern)patIter.getPattern();
                // FIXME DESCTRUCTURE
                SyntheticName entryName = naming.alias("entry");
                JCStatement entryVariable = makeVar(FINAL, entryName,
                        makeJavaType(typeFact().getEntryType(typeFact().getAnythingType(), typeFact().getAnythingType()), JT_RAW),
                        elementGet);
                Type entryType = elementType.getSupertype(typeFact().getEntryDeclaration());
                Type keyType = entryType.getTypeArgumentList().get(0);
                Tree.Variable keyVar = ((Tree.VariablePattern)pat.getKey()).getVariable();
                String keyName = Naming.getVariableName(keyVar);
                Boolean keyUnboxed = keyVar.getDeclarationModel().getUnboxed();
                BoxingStrategy keyBoxStrat = CodegenUtil.getBoxingStrategy(keyVar.getDeclarationModel());
                JCStatement keyVariable = makeVar(FINAL,
                        keyName,
                        makeJavaType(keyType, BooleanUtil.isFalse(keyUnboxed) ? JT_NO_PRIMITIVES : 0),
                        expressionGen().applyErasureAndBoxing(
                                make().Apply(null, naming.makeQualIdent(entryName.makeIdent(), "getKey"), List.<JCExpression>nil()),
                                typeFact().getAnythingType(), true, keyBoxStrat, keyType));
                Type valueType = entryType.getTypeArgumentList().get(1);
                Tree.Variable valueVar = ((Tree.VariablePattern)pat.getValue()).getVariable();
                String valueName = Naming.getVariableName(valueVar);
                Boolean valueUnboxed = keyVar.getDeclarationModel().getUnboxed();
                BoxingStrategy valueBoxStrat = CodegenUtil.getBoxingStrategy(valueVar.getDeclarationModel());
                JCStatement valueVariable = makeVar(FINAL,
                        valueName,
                        makeJavaType(valueType, BooleanUtil.isFalse(valueUnboxed) ? JT_NO_PRIMITIVES : 0),
                        expressionGen().applyErasureAndBoxing(
                                make().Apply(null, naming.makeQualIdent(entryName.makeIdent(), "getItem"), List.<JCExpression>nil()),
                                typeFact().getAnythingType(), true, valueBoxStrat, valueType));
                // Prepend to the block
                transformedBlock = transformedBlock.prepend(valueVariable);
                transformedBlock = transformedBlock.prepend(keyVariable);
                transformedBlock = transformedBlock.prepend(entryVariable);
            } else {
                throw BugException.unhandledCase(forIterator);
            }
            
            JCStatement block = make().Block(0, transformedBlock);
            result.add(make().Labelled(this.label, make().ForLoop(
                    List.<JCStatement>of(iVar), 
                    iCond,
                    List.<JCExpressionStatement>of(make().Exec(iIncr)),
                    block)));
            return result;
        }

        protected JCBinary stepCheck(final SyntheticName stepName) {
            return make().Binary(JCTree.LE, stepName.makeIdent(), make().Literal(0));
        }

        protected JCExpression makeIndexType() {
            return make().Type(syms().intType);
        }

        /** Makes the expression for incrementing the index */
        protected JCExpression makeStepExpr() {
            Type intType = typeFact().getIntegerType();
            intType.setUnderlyingType("int");
            return expressionGen().transformExpression(step, BoxingStrategy.UNBOXED, 
                    intType);
        }
        
        protected JCExpression makeIncrement(SyntheticName stepName) {
            if (stepName == null) {
                return make().Unary(JCTree.POSTINC, indexName.makeIdent());
            } else {
                return make().Assignop(JCTree.PLUS_ASG, indexName.makeIdent(), stepName.makeIdent());
            }
        }
        
        protected JCExpression makeCondition() {
            return make().Binary(JCTree.LT, indexName.makeIdent(), lengthName.makeIdent());
        }
        
        protected abstract JCExpression makeIndexableType();
        
        protected JCExpression makeIndexInit() {
            return make().Literal(0);
        }
        
        /** Makes the expression for the accessing the indexable at the current index */
        protected abstract JCExpression makeIndexedAccess();
        
        /** To determine if the makeIndexedAccess() returns a boxed value or not */
        protected abstract boolean isIndexedAccessBoxed();
        
        /** Makes the expression for the length of the iteration */
        protected abstract JCExpression makeLengthExpr();
        
        /** Makes the expression for the thing to be iterated over */
        protected abstract JCExpression makeIndexable();
    }
    
    /**
     * Optimized transformation for a {@code for} loop where the iterable is 
     * statically known to be an {@code Array}, and therefore can be 
     * iterated using a C-style {@code for}.
     */
    class ArrayIterationOptimization extends IndexedAccessIterationOptimization {
        
        public static final String OPT_NAME = "ArrayIterationStatic";
        private final boolean unboxed;
        
        ArrayIterationOptimization(Tree.ForStatement stmt, 
                Tree.Term baseIterable, Tree.Term step,
                Type arrayType) {
            super(stmt, baseIterable, step, typeFact().getArrayElementType(arrayType), "array");
            unboxed = typeFact().getArrayType(typeFact().getBooleanType()).isExactly(arrayType)
                    || typeFact().getArrayType(typeFact().getByteType()).isExactly(arrayType)
                    || typeFact().getArrayType(typeFact().getIntegerType()).isExactly(arrayType)
                    || typeFact().getArrayType(typeFact().getCharacterType()).isExactly(arrayType)
                    || typeFact().getArrayType(typeFact().getFloatType()).isExactly(arrayType)
                    || typeFact().getArrayType(typeFact().getStringType()).isExactly(arrayType);;
        }
        
        @Override
        protected JCExpression makeIndexableType() {
            if(unboxed)
                return make().Type(syms().objectType);
            return makeJavaType(typeFact().getArrayType(elementType));
        }
        
        @Override
        protected JCExpression makeIndexable() {
            JCExpression iterableExpr = expressionGen().transformExpression(getIterable());
            if(unboxed)
                return make().Apply(null,
                                    naming.makeQualIdent(iterableExpr, "toArray"),
                                    List.<JCExpression>nil());
            return iterableExpr;
        }
        
        protected JCExpression makeCondition() {
            return make().Binary(JCTree.LT, indexName.makeIdent(), lengthName.makeIdent());
        }
        
        @Override
        protected JCExpression makeLengthExpr() {
            if(unboxed)
                return utilInvocation().arrayLength(indexableName.makeIdent());
            else
                return make().TypeCast(make().Type(syms().intType), 
                                       make().Apply(null, 
                                                    naming.makeQualIdent(indexableName.makeIdent(), "getSize"), 
                                                    List.<JCExpression>nil()));
        }
        
        @Override
        protected JCExpression makeIndexedAccess() {
            Type gotType = null;
            JCExpression elementGet = null;
            boolean typeErased = false;
            boolean exprBoxed = false;
            if (isCeylonBoolean(elementType)) {
                elementGet = utilInvocation().getBooleanArray(
                        indexableName.makeIdent(), indexName.makeIdent());
                gotType = elementType;
            } else if (isCeylonFloat(elementType)) {
                elementGet = utilInvocation().getFloatArray(
                        indexableName.makeIdent(), indexName.makeIdent());
                gotType = elementType;
            } else if (isCeylonInteger(elementType)) {
                elementGet = utilInvocation().getIntegerArray(
                        indexableName.makeIdent(), indexName.makeIdent());
                gotType = elementType;
            } else if (isCeylonCharacter(elementType)) {
                elementGet = utilInvocation().getCharacterArray( 
                        indexableName.makeIdent(), indexName.makeIdent());
                gotType = elementType;
            } else if (isCeylonByte(elementType)) {
                elementGet = utilInvocation().getByteArray( 
                        indexableName.makeIdent(), indexName.makeIdent());
                gotType = elementType;
            } else if (isCeylonString(elementType)) {
                elementGet = utilInvocation().getStringArray( 
                        indexableName.makeIdent(), indexName.makeIdent());
                gotType = elementType;
            }
            
            if(elementGet == null){
                elementGet = make().Apply(null, 
                                          naming.makeQualIdent(indexableName.makeIdent(), "unsafeItem"),
                                          List.<JCExpression>of(indexName.makeIdent()));
                gotType = typeFact().getObjectType();
                typeErased = true;
                exprBoxed = true;
            }
            elementGet = expressionGen().applyErasureAndBoxing(
                    elementGet, gotType, typeErased, exprBoxed, 
                    CodegenUtil.getBoxingStrategy(getElementOrKeyVariable().getDeclarationModel()), 
                    elementType, 0);
            return elementGet;
        }
        
        @Override
        protected boolean isIndexedAccessBoxed() {
            return getElementOrKeyVariable().getDeclarationModel().getUnboxed() == false;
        }
    }
    
    /**
     * Optimized transformation for a {@code for} loop where the iterable is 
     * statically known to be a Java array (int[], long[] Object[] etc)
     * and therefore can be 
     * iterated using a C-style {@code for}.
     */
    class JavaArrayIterationOptimization extends IndexedAccessIterationOptimization {
        
        public static final String OPT_NAME = "JavaArrayIterationStatic";
        /** this is the IntArray, ObjectArray or whatever */
        private final Type javaArrayType;
        
        JavaArrayIterationOptimization(Tree.ForStatement stmt,
                Tree.Term baseIterable, Tree.Term step,
                Type elementType, Type javaArrayType) {
            super(stmt, baseIterable, step, elementType, "array");
            this.javaArrayType = javaArrayType;
        }
        
        @Override
        protected JCExpression makeIndexableType() {
            return makeJavaType(javaArrayType);
        }
        
        @Override
        protected JCExpression makeIndexable() {
            Tree.QualifiedMemberExpression expr = (Tree.QualifiedMemberExpression)getIterable();
            
            return expressionGen().transformExpression(expr.getPrimary());
        }
        
        protected JCExpression makeCondition() {
            JCExpression lengthExpr = naming.makeQualIdent(indexableName.makeIdent(), "length");
            return make().Binary(JCTree.LT, indexName.makeIdent(), lengthExpr);
        }
        
        @Override
        protected JCExpression makeLengthExpr() {
            return null;
        }
        
        @Override
        protected JCExpression makeIndexedAccess() {
            return make().Indexed(indexableName.makeIdent(), indexName.makeIdent());
        }
        
        @Override
        protected boolean isIndexedAccessBoxed() {
            return false;
        }
    }
    
    private ForStatementTransformation arrayIteration(Tree.ForStatement stmt, 
            Tree.Term baseIterable, 
            Tree.Term step) {
        
        Type ceylonArrayType = baseIterable.getTypeModel();
        Type elementType = typeFact().getArrayElementType(ceylonArrayType);
        if (elementType == null) {
            // Check for "for (x in javaArray.iterable)" where javaArray is e.g. IntArray
            
            if (baseIterable instanceof Tree.QualifiedMemberExpression) {
                Tree.QualifiedMemberExpression expr = (Tree.QualifiedMemberExpression)baseIterable;
                if ("iterable".equals(expr.getIdentifier().getText())) {
                    if (Decl.isJavaArray(expr.getPrimary().getTypeModel().getDeclaration())) {// What's this for
                        if (isOptimizationDisabled(stmt, Optimization.JavaArrayIterationStatic)) {
                            return optimizationDisabled(stmt, Optimization.JavaArrayIterationStatic);
                        }
                        elementType = typeFact().getIteratedType(ceylonArrayType);
                        return new JavaArrayIterationOptimization(stmt, baseIterable, step, elementType, expr.getPrimary().getTypeModel());
                    }
                }
            }
        } else {
            if (isOptimizationRequired(stmt, Optimization.JavaArrayIterationStatic)) {
                return optimizationFailed(stmt, Optimization.JavaArrayIterationStatic, "iterable expression wasn't of form javaArray.array");
            }
            if (isOptimizationDisabled(stmt, Optimization.ArrayIterationStatic)) {
                return optimizationDisabled(stmt, Optimization.ArrayIterationStatic);
            }
            // it's an Ceylon Array
            return new ArrayIterationOptimization(stmt, baseIterable, step, ceylonArrayType);
        }
        if (isOptimizationRequired(stmt, Optimization.JavaArrayIterationStatic)) {
            return optimizationFailed(stmt, Optimization.JavaArrayIterationStatic, "iterable expression wasn't of form javaArray.iterable");
        }
        if (isOptimizationRequired(stmt, Optimization.ArrayIterationStatic)) {
            return optimizationFailed(stmt, Optimization.ArrayIterationStatic, "static type of iterable in for statement is not Array");
        }
        return null;
    }

    private boolean isSpanOf(Tree.RangeOp range, Type ofType) {
        Type rangeType = range.getTypeModel();
        return typeFact().getSpanType(ofType).isExactly(rangeType);
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
    private <T,S extends Tree.StatementOrArgument> T optimizationFailed(S stmt, Optimization optName, String reason) {
        return optimizationFailed(stmt, new Optimization[]{optName}, reason);
    }
    private <T,S extends Tree.StatementOrArgument> T optimizationFailed(S stmt, Optimization[] optNames, String reason) {
        for (Optimization optName : optNames) {
            if (CodegenUtil.hasCompilerAnnotationWithArgument(stmt, 
                            "requireOptimization", optName.toString())) {
                log.error(getPosition(stmt), "ceylon.optim.failed", optName, reason);
            }
        }
        return null;
    }
    
    /**
     * Returns null but logs an error the given optimization 
     */
    private <T,S extends Tree.StatementOrArgument> T optimizationDisabled(S stmt, Optimization optName) {
        return optimizationFailed(stmt, optName, 
                "optimization explicitly disabled by @disableOptimization");
    }
    
    /**
     * Determines whether the given optimization has been disabled on the 
     * given statement. 
     * @param stmt The thing with the {@code @requireOptimization} compiler 
     * annotation.
     * @param optName The name of the optimization
     * @return
     */
    private boolean isOptimizationDisabled(Tree.StatementOrArgument stmt, Optimization optName) {
        return this.disabledOptimizations.contains(optName)
                || CodegenUtil.hasCompilerAnnotationNoArgument(stmt, "disableOptimization")
                || CodegenUtil.hasCompilerAnnotationWithArgument(stmt, 
                        "disableOptimization", optName.toString());
    }
    
    private boolean isOptimizationRequired(Tree.StatementOrArgument stmt, Optimization optName) {
        return optName == null ? CodegenUtil.hasCompilerAnnotationNoArgument(stmt, "requireOptimization")
                : CodegenUtil.hasCompilerAnnotationWithArgument(stmt, 
                        "requireOptimization", optName.toString());
    }
    
    /**
     * Returns a {@link SpanOpIterationOptimization} if that optimization applies
     * to the given {@code for} statement, otherwise null.
     * @param stmt The for statement
     * @return a {@link SpanOpIterationOptimization} or null.
     */
    private ForStatementTransformation spanOpIteration(Tree.ForStatement stmt) {
        if (isOptimizationDisabled(stmt, Optimization.SpanOpIteration)) {
            return optimizationFailed(stmt, Optimization.SpanOpIteration, 
                    "optimization explicitly disabled by @disableOptimization");
        }
        
        Tree.ForIterator iterator = stmt.getForClause().getForIterator();
        if (!(iterator instanceof Tree.ValueIterator)) {
            return optimizationFailed(stmt, Optimization.SpanOpIteration, 
                    "optimization applies only to ValueIterators");
        }
        Tree.ValueIterator vi = (Tree.ValueIterator)iterator;
        Tree.SpecifierExpression specifier = vi.getSpecifierExpression();
        Tree.Term term = specifier.getExpression().getTerm();
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
                        Tree.PositionalArgument a = inv.getPositionalArgumentList().getPositionalArguments().get(0);
                        if(a instanceof Tree.ListedArgument)
                            increment = ((Tree.ListedArgument)a).getExpression().getTerm();
                        else
                            return optimizationFailed(stmt, Optimization.SpanOpIteration, 
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
                            return optimizationFailed(stmt, Optimization.SpanOpIteration, 
                                    "Unable to determine expression for argument to by{}");
                        }
                    } else {
                        return optimizationFailed(stmt, Optimization.SpanOpIteration, 
                                "Unable to get arguments to by()");
                    }
                } else {
                    return optimizationFailed(stmt, Optimization.SpanOpIteration, 
                            "Only applies to Iterables of the form 'lhs..rhs' or '(lhs..rhs).by(step)'");
                }
            } else {
                return optimizationFailed(stmt, Optimization.SpanOpIteration, 
                        "Only applies to Iterables of the form 'lhs..rhs' or '(lhs..rhs).by(step)'");
            }
        } else {
            return optimizationFailed(stmt, Optimization.SpanOpIteration, 
                    "Only applies to Iterables of the form 'lhs..rhs' or '(lhs..rhs).by(step)'");
        }
        
        com.sun.tools.javac.code.Type type;
        Type integerType = typeFact().getIntegerType();
        Type characterType = typeFact().getCharacterType();
        if (isSpanOf(range, integerType)) {
            type = syms().longType;
        } else if (isSpanOf(range, characterType)) {
            type = syms().intType;
        } else {
            return optimizationFailed(stmt, Optimization.SpanOpIteration, "The RangeOp doesn't produce a Range<Integer>/Range<Character>");
        }
        return increment == null ?
                new SpanOpIterationOptimization(stmt, range, increment, type) : 
                new SpanOpWithStepIterationOptimization(stmt, range, increment, type);
    }
    
    /**
     * <p>Transformation of {@code for} loops over {@code Range<Integer>} 
     * or {@code Range<Character>} which avoids allocating a {@code Range} and
     * using an {@code Iterator} like 
     * {@link #ForStatementTransformation} but instead outputs a C-style 
     * {@code for} loop.</p> 
     */
    private class SegmentOpIteration extends IndexedAccessIterationOptimization {

        private final Tree.Term start;
        private final Tree.Term length;

        SegmentOpIteration(Tree.ForStatement stmt, Tree.SegmentOp op, Tree.Term step, Tree.Term start, Tree.Term length) {
            super(stmt, op, step, typeFact().getIteratedType(op.getTypeModel()), "start", "length", "i");
            this.start = start;
            this.length = length;
            // TODO If length if < 0 we need to not loop at all
        }
        
        @Override
        protected JCBinary stepCheck(final SyntheticName stepName) {
            return make().Binary(JCTree.AND, 
                    make().Binary(JCTree.GT, lengthName.makeIdent(), make().Literal(0)),
                    super.stepCheck(stepName));
        }
        
        @Override
        protected JCExpression makeIndexableType() {
            return makeJavaType(this.elementType);
        }
        
        @Override
        protected JCExpression makeIndexable() {
            return expressionGen().transformExpression(start, BoxingStrategy.UNBOXED, length.getTypeModel());
        }

        protected JCExpression makeIndexType() {
            return make().Type(syms().longType);
        }
        
        protected JCExpression makeIndexInit() {
            return make().Literal(0);
        }
        
        @Override
        protected JCExpression makeIndexedAccess() {
            if (elementType.isExactly(typeFact().getIntegerType())) {
                if (step == null) {
                    return make().Binary(JCTree.PLUS, indexName.makeIdent(), indexableName.makeIdent());
                } else{ 
                    return make().Conditional(make().Binary(JCTree.EQ, stepName.makeIdent(), make().Literal(1L)),
                            make().Binary(JCTree.PLUS, indexName.makeIdent(), indexableName.makeIdent()),
                            make().Apply(null,
                                naming.makeSelect(makeJavaType(elementType, JT_NO_PRIMITIVES), "neighbour"),
                                List.<JCExpression>of(
                                        indexableName.makeIdent(), indexName.makeIdent())));
                }
            } else {// must be character
                if (step == null) {
                    return make().Apply(null,
                            naming.makeSelect(makeJavaType(elementType, JT_NO_PRIMITIVES), "neighbour"),
                            List.<JCExpression>of(
                                    indexableName.makeIdent(), indexName.makeIdent()));
                } else {
                    return make().Conditional(make().Binary(JCTree.EQ, stepName.makeIdent(), make().Literal(1L)),
                            make().Apply(null,
                                    naming.makeSelect(makeJavaType(elementType, JT_NO_PRIMITIVES), "codepoint"),
                                    List.<JCExpression>of(
                            make().Binary(JCTree.PLUS, indexName.makeIdent(), indexableName.makeIdent()))),
                            make().Apply(null,
                                naming.makeSelect(makeJavaType(elementType, JT_NO_PRIMITIVES), "neighbour"),
                                List.<JCExpression>of(
                                        indexableName.makeIdent(), indexName.makeIdent())));
                }
            }
        }
        
        @Override
        protected boolean isIndexedAccessBoxed() {
            return false;
        }

        @Override
        protected JCExpression makeLengthExpr() {
            JCExpression result = expressionGen().transformExpression(length, 
                    BoxingStrategy.UNBOXED, length.getTypeModel());
            //if (isCeylonCharacter(elementType)) {
                // This cannot be correct!
              //  result = make().TypeCast(syms().intType, result);
            //}
            return result;
        }
        
        protected JCExpression makeStepExpr() {
            return expressionGen().transformExpression(step, BoxingStrategy.UNBOXED, 
                    elementType);
        }
        
        protected JCExpression makeIncrement(SyntheticName stepName) {
            if (stepName == null) {
                return make().Unary(JCTree.POSTINC, indexName.makeIdent());
            } else {
                return make().Assign(indexName.makeIdent(),
                        make().Conditional(make().Binary(JCTree.EQ, stepName.makeIdent(), make().Literal(1L)),
                                make().Binary(JCTree.PLUS, indexName.makeIdent(), make().Literal(1L)),
                            make().Apply(null,
                                    naming.makeSelect(make().Type(syms().ceylonIntegerType), "neighbour"),
                                    List.<JCExpression>of(
                                            indexName.makeIdent(), stepName.makeIdent()))));
            }
        }
    }
    
    private ForStatementTransformation segmentOpIteration(Tree.ForStatement stmt, 
            Tree.Term baseIterable, Tree.Term step) {
        if (!(baseIterable instanceof Tree.SegmentOp)) {
            return optimizationFailed(stmt, Optimization.SegmentOpIteration, 
                    "base iterable is no a segment op");
        }
        
        
        final Tree.SegmentOp op = (Tree.SegmentOp)baseIterable;
        Type iteratedType = typeFact().getIteratedType(op.getTypeModel()); 
        if (iteratedType.isExactly(typeFact().getIntegerType())) {
            
        } else if (iteratedType.isExactly(typeFact().getCharacterType())) {
            
        } else {
            return optimizationFailed(stmt, Optimization.SegmentOpIteration, 
                    "base iterable is neither Range<Integer> not Range<Character>");
        }
        
        Tree.Term start = op.getLeftTerm();
        Tree.Term length = op.getRightTerm();
        
        if (isOptimizationDisabled(stmt, Optimization.SegmentOpIteration)) {
            return optimizationDisabled(stmt, Optimization.SegmentOpIteration);
        }
        
        return new SegmentOpIteration(stmt, op, step, start, length);
    }
    
    private Tree.ControlClause currentForClause = null;
    
    class ForStatementTransformation {
        
        protected Tree.ForStatement stmt;
        protected final Name label;
        protected final Name failVar;
        
        ForStatementTransformation(Tree.ForStatement stmt) {
            this.stmt = stmt;
            this.label = getLabel(stmt.getForClause().getControlBlock());
            if (needsFailVar()) {
                // boolean $doforelse$X = true;
                failVar = naming.aliasName("doforelse");
            } else {
                failVar = null;
            }
        }
        
        protected final Tree.ForIterator getForIterator() {
            Tree.ForIterator forIterator = stmt.getForClause().getForIterator();
            return forIterator;
        }
        
        protected Tree.Term getIterable() {
            return getForIterator().getSpecifierExpression().getExpression().getTerm();
        }
        
        protected final boolean isValueIterator() {
            return getForIterator() instanceof Tree.ValueIterator;
        }

        protected final Tree.Variable getElementOrKeyVariable() {
            Tree.ForIterator forIterator = getForIterator();
            if (forIterator instanceof Tree.ValueIterator) {
                return ((Tree.ValueIterator)forIterator).getVariable();
            } else if (forIterator instanceof Tree.PatternIterator) {
                Tree.PatternIterator patIter = (Tree.PatternIterator)forIterator;
                Tree.Pattern pat = patIter.getPattern();
                // FIXME DESCTRUCTURE
                return ((Tree.VariablePattern)((Tree.KeyValuePattern)pat).getKey()).getVariable();
            }
            return null;
        }
        
        protected final Tree.Variable getValueVariable() {
            Tree.ForIterator forIterator = getForIterator();
            if (forIterator instanceof Tree.PatternIterator) {
                Tree.PatternIterator patIter = (Tree.PatternIterator)forIterator;
                Tree.Pattern pat = patIter.getPattern();
                // FIXME DESCTRUCTURE
                return ((Tree.VariablePattern)((Tree.KeyValuePattern)pat).getKey()).getVariable();
            }
            return null;
        }
        
        protected List<JCStatement> transform() {
            at(stmt);
            ListBuffer<JCStatement> outer = ListBuffer.<JCStatement> lb();
            Name tempForFailVariable = currentForFailVariable;
            try {
                // Install the outer substitutions
                Iterable<Value> deferredSpecifiedInFor = stmt.getForClause().getControlBlock().getSpecifiedValues();
                if (deferredSpecifiedInFor != null) { 
                    for (Value value : deferredSpecifiedInFor) {
                        DeferredSpecification ds  = StatementTransformer.this.deferredSpecifications.get(value);
                        ds.installOuterSubstitution();
                    }
                }

                if (needsFailVar()) {
                    // boolean $doforelse$X = true;
                    JCVariableDecl failtest_decl = make().VarDef(make().Modifiers(0), failVar, make().TypeIdent(TypeTags.BOOLEAN), make().Literal(TypeTags.BOOLEAN, 1));
                    outer.append(failtest_decl);
                    currentForFailVariable = failtest_decl.getName();
                } else {
                    currentForFailVariable = null;
                }
                
                outer.appendList(transformForClause());
                
                if (stmt.getElseClause() != null) {
                    // The user-supplied contents of fail block
                    List<JCStatement> failblock = transformBlock(stmt.getElseClause().getBlock());
                    // Close the inner substitutions of the else block
                    closeInnerSubstituionsForSpecifiedValues(stmt.getElseClause());
                    if (needsFailVar()) {
                        // if ($doforelse$X) ...
                        JCIdent failtest_id = at(stmt).Ident(currentForFailVariable);
                        outer.append(at(stmt).If(failtest_id, at(stmt).Block(0, failblock), null));
                    } else {
                        outer.appendList(failblock);
                    }
                }
                
                // Close the outer substitutions
                if (deferredSpecifiedInFor != null) { 
                    for (Value value : deferredSpecifiedInFor) {
                        DeferredSpecification ds  = StatementTransformer.this.deferredSpecifications.get(value);
                        outer.append(ds.closeOuterSubstitution());
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
            
            Tree.ForIterator forIterator = stmt.getForClause().getForIterator();
            List<JCStatement> itemDecls = List.nil();
            final Naming.SyntheticName iteratorVarName;
            if (forIterator instanceof Tree.ValueIterator) {
                Tree.Variable variable = ((Tree.ValueIterator) forIterator).getVariable();
                JCVariableDecl varExpr = transformVariable(variable, elem_name.makeIdent()).build();
                itemDecls = itemDecls.append(varExpr);
                iteratorVarName = naming.synthetic(variable.getDeclarationModel()).suffixedBy(Suffix.$iterator$).alias();
            } else if (forIterator instanceof Tree.PatternIterator) {
                Tree.PatternIterator patIter = (Tree.PatternIterator)forIterator;
                Tree.Pattern pat = patIter.getPattern();
                List<VarDefBuilder> varsDefs = transformPattern(pat, elem_name.makeIdent());
                for (VarDefBuilder vdb : varsDefs) {
                    itemDecls = itemDecls.append(vdb.build());
                }
                iteratorVarName = elem_name.suffixedBy(Suffix.$iterator$);
            } else {
                throw BugException.unhandledNodeCase(forIterator);
            }
            
            Tree.Expression specifierExpression = forIterator.getSpecifierExpression().getExpression();
            Type sequenceElementType = typeFact().getIteratedType(specifierExpression.getTypeModel());
            Type sequenceType = specifierExpression.getTypeModel().getSupertype(typeFact().getIterableDeclaration());
            Type expectedIterableType = typeFact().isNonemptyIterableType(sequenceType)
                    ? typeFact().getNonemptyIterableType(sequenceElementType)
                    : typeFact().getIterableType(sequenceElementType);

            // ceylon.language.Iterator<T> $V$iter$X = ITERABLE.getIterator();
            JCExpression containment = expressionGen().transformExpression(specifierExpression, BoxingStrategy.BOXED, expectedIterableType);
            
            Tree.ControlClause prevControlClause = currentForClause;
            currentForClause = stmt.getForClause();
            List<JCStatement> stmts = transformBlock(stmt.getForClause().getBlock());
            currentForClause = prevControlClause;
            
            return ListBuffer.<JCStatement>lb().appendList(transformIterableIteration(stmt,
                    this.label,
                    elem_name, 
                    iteratorVarName,
                    forIterator.getSpecifierExpression().getExpression().getTypeModel(),
                    sequenceElementType,
                    containment,
                    itemDecls,
                    stmts, 
                    !isOptimizationDisabled(stmt, Optimization.ArrayIterationDynamic),
                    !isOptimizationDisabled(stmt, Optimization.TupleIterationDynamic)));
        }
        
        protected final Tree.Block getBlock() {
            return stmt.getForClause().getBlock();
        }
    }
    
    /**
     * The transformation of a ceylon {@code for} loop:
     * 
     * <pre>
     *     java.lang.Object ITERATION_VAR_NAME;
     *     Iterator<ITERATOR_ELEMENT_TYPE> ITERATOR_VAR_NAME = ITERABLE.getIterator();
     *     while (
     *             !((ITERATION_VAR_NAME = ITERATOR_VAR_NAME.getNext()) instanceof ceylon.language.Finished;
     *         ) {
     *         ITEM_DECLS;
     *         BODY_STMTS;
     *     }
     * </pre>
     * @param label 
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
            Name label, Naming.SyntheticName iterationVarName,
            Naming.SyntheticName iteratorVarName,
            Type iterableType, Type iteratedType, 
            JCExpression iterableExpr,
            List<JCStatement> itemDecls,
            List<JCStatement> bodyStmts,
            boolean allowArrayOpt, boolean allowArraySeqOpt) {
        Type iteratorElementType = iteratedType;
        ListBuffer<JCStatement> result = ListBuffer.<JCStatement>lb();
        
        // TODO Only when the iterable *could be* an array (e.g. if static type is Iterable, but not if static type is Sequence)
        // TODO Need to use naming.Infix for the hidden members of Array
        boolean optForArray = allowArrayOpt && typeFact().getArrayType(iteratedType).isSubtypeOf(iterableType);
        boolean optForTuple = allowArraySeqOpt && typeFact().getTupleType(Collections.singletonList(iteratedType), true, false, -1).isSubtypeOf(iterableType);
        
        SyntheticName iterableName = optForArray || optForTuple ? naming.alias("iterable") : null;
        SyntheticName isArrayName = optForArray ? naming.alias("isArray") : null;
        SyntheticName isTupleName = optForTuple ? naming.alias("isTuple") : null;
        SyntheticName arrayName = optForArray || optForTuple ? naming.alias("array") : null;
        SyntheticName arrayIndex = optForArray || optForTuple ? naming.alias("i") : null;
        SyntheticName arrayLength = optForArray || optForTuple ? naming.alias("length") : null;
        if (optForArray || optForTuple) {
            result.append(makeVar(FINAL, iterableName, makeJavaType(typeFact().getIterableType(iteratorElementType)), iterableExpr));
        }
        if (optForArray) {
            result.append(makeVar(FINAL, isArrayName, 
                    make().Type(syms().booleanType), 
                    make().TypeTest(iterableName.makeIdent(), 
                            makeJavaType(typeFact().getArrayType(iteratorElementType), JT_RAW))));
        }
        if (optForTuple) {
            result.append(makeVar(FINAL, isTupleName, 
                    make().Type(syms().booleanType), 
                    make().Binary(JCTree.AND, 
                            make().TypeTest(iterableName.makeIdent(), 
                                    make().QualIdent(syms().ceylonTupleType.tsym)),
                            make().Binary(JCTree.NE, 
                                    make().Apply(null, 
                                            naming.makeQualIdent(
                                                    make().TypeCast(make().QualIdent(syms().ceylonTupleType.tsym), iterableName.makeIdent()),
                                                    Unfix.$getArray$.toString()),
                                                List.<JCExpression>nil()),
                                    makeNull()))));
        }
        
        // java.lang.Object ELEM_NAME;
        JCVariableDecl elemDecl = makeVar(iterationVarName, make().Type(syms().objectType), optForArray || optForTuple ? makeNull() : null);
        result.append(elemDecl);
        
        SyntheticName iterName = iteratorVarName;
        
        Type iteratorType = typeFact().getIteratorType(iteratorElementType);
        JCExpression iteratorTypeExpr = makeJavaType(iteratorType, CeylonTransformer.JT_TYPE_ARGUMENT);
        
        // ceylon.language.Iterator<T> LOOP_VAR_NAME$iter$X = ITERABLE.getIterator();
        // We don't need to unerase here as anything remotely a sequence will be erased to Iterable, which has getIterator()
        JCExpression getIter;
        if (optForArray || optForTuple) {
            at(node);
            result.append(makeVar(FINAL, arrayName, make().Type(syms().objectType), null));
            result.append(makeVar(arrayIndex, make().Type(syms().intType), make().Literal(0)));
            result.append(makeVar(FINAL, arrayLength, make().Type(syms().intType), null));
            ListBuffer<JCStatement> whenTuple = ListBuffer.<JCTree.JCStatement>lb();
            whenTuple.append(make().Exec(make().Assign(
                    arrayName.makeIdent(), 
                    make().Apply(null, 
                            naming.makeQualIdent(
                                    make().TypeCast(make().QualIdent(syms().ceylonTupleType.tsym), iterableName.makeIdent()),
                                    Unfix.$getArray$.toString()),
                            List.<JCExpression>nil()))));
            whenTuple.append(make().Exec(make().Assign(
                    arrayIndex.makeIdent(),
                    make().Apply(null, 
                            naming.makeQualIdent(
                                    make().TypeCast(make().QualIdent(syms().ceylonTupleType.tsym), iterableName.makeIdent()),
                                    Unfix.$getFirst$.toString()),
                            List.<JCExpression>nil()))));
            whenTuple.append(make().Exec(make().Assign(
                    arrayLength.makeIdent(),
                    make().Binary(JCTree.PLUS, arrayIndex.makeIdent(), make().Apply(null, 
                            naming.makeQualIdent(
                                    make().TypeCast(make().QualIdent(syms().ceylonTupleType.tsym), iterableName.makeIdent()),
                                    Unfix.$getLength$.toString()),
                            List.<JCExpression>nil())))));
            
            ListBuffer<JCStatement> whenArray = ListBuffer.<JCTree.JCStatement>lb();
            whenArray.append(make().Exec(make().Assign(
                    arrayName.makeIdent(), 
                    make().Apply(null, 
                            naming.makeQualIdent(
                                    make().TypeCast(makeJavaType(typeFact().getArrayType(typeFact().getAnythingType()), JT_RAW), iterableName.makeIdent()),
                                    "toArray"),
                            List.<JCExpression>nil()))));
            whenArray.append(make().Exec(make().Assign(
                    arrayLength.makeIdent(),
                    make().Apply(null, 
                            naming.makeQuotedFQIdent("com.redhat.ceylon.compiler.java.Util.arrayLength"),
                            List.<JCExpression>of(arrayName.makeIdent())))));
            ListBuffer<JCStatement> whenIterable = ListBuffer.<JCTree.JCStatement>lb();
            whenIterable.append(make().Exec(make().Assign(
                    arrayName.makeIdent(),
                    makeNull())));
            whenIterable.append(make().Exec(make().Assign(
                    arrayLength.makeIdent(),
                    make().Literal(0))));
            if (optForArray && optForTuple) {
                result.append(make().If(isTupleName.makeIdent(),
                        make().Block(0, whenTuple.toList()),
                        make().If(isArrayName.makeIdent(),
                            make().Block(0, whenArray.toList()),
                            make().Block(0, whenIterable.toList()))));
            } else {
                result.append(make().If((optForArray ? isArrayName : isTupleName).makeIdent(),
                        make().Block(0, (optForArray ? whenArray : whenTuple).toList()),
                        make().Block(0, whenIterable.toList())));
            }
            
            getIter = make().Conditional(
                    optForArray && optForTuple ? make().Binary(JCTree.OR, isTupleName.makeIdent(), isArrayName.makeIdent()): optForArray ? isArrayName.makeIdent() : isTupleName.makeIdent(), 
                    makeNull(), 
                    make().Apply(null, makeSelect(iterableName.makeIdent(), "iterator"), List.<JCExpression> nil()));
        } else {
            getIter = at(node).Apply(null, makeSelect(iterableExpr, "iterator"), List.<JCExpression> nil());
        }
        getIter = gen().expressionGen().applyErasureAndBoxing(getIter, iteratorType, true, BoxingStrategy.BOXED, iteratorType);
        JCVariableDecl iteratorDecl = at(node).VarDef(make().Modifiers(0), iterName.asName(), iteratorTypeExpr, getIter);
        // .ceylon.language.Iterator<T> LOOP_VAR_NAME$iter$X = ITERABLE.getIterator();
        result.append(iteratorDecl);
        
        ListBuffer<JCStatement> loopBody = ListBuffer.<JCStatement>lb();
        
        if(optForArray || optForTuple) {
            JCExpression cond;
            if (optForArray && optForTuple) {
                cond = make().Binary(JCTree.OR, isTupleName.makeIdent(), isArrayName.makeIdent());
            } else if (optForArray) {
                cond = isArrayName.makeIdent();
            } else {
                cond = isTupleName.makeIdent();
            }
            loopBody.append(make().If(cond,
                    make().Exec(make().Assign(iterationVarName.makeIdent(),
                            make().Apply(null, 
                                    naming.makeQuotedFQIdent("com.redhat.ceylon.compiler.java.Util.getObjectArray"), 
                                    List.<JCExpression>of(arrayName.makeIdent(), 
                                            make().Unary(JCTree.POSTINC, arrayIndex.makeIdent()))))),
                    null));
        }
        
        if (itemDecls != null) {
            loopBody.appendList(itemDecls);
        }

        // The user-supplied contents of the loop
        loopBody.appendList(bodyStmts);
        
        // ELEM_NAME = LOOP_VAR_NAME$iter$X.next()
        JCExpression iter_elem = make().Apply(null, makeSelect(iterName.makeIdent(), "next"), List.<JCExpression> nil());
        JCExpression elem_assign = make().Assign(iterationVarName.makeIdent(), iter_elem);
        // !((ELEM_NAME = LOOP_VAR_NAME$iter$X.next()) instanceof Finished)
        JCExpression instof = make().TypeTest(elem_assign, makeIdent(syms().ceylonFinishedType));
        JCExpression loopCond = make().Unary(JCTree.NOT, instof);
        if (optForArray || optForTuple) {
            JCExpression cond;
            if (optForArray && optForTuple) {
                cond = make().Binary(JCTree.OR, isTupleName.makeIdent(), isArrayName.makeIdent());
            } else if (optForArray) {
                cond = isArrayName.makeIdent();
            } else {
                cond = isTupleName.makeIdent();
            }
            loopCond = make().Conditional(cond,
                    make().Binary(JCTree.LT, arrayIndex.makeIdent(), arrayLength.makeIdent()), 
                    make().Unary(JCTree.NOT, instof));
        }
        
        // while (!(($elem$X = $V$iter$X.next()) instanceof Finished); ) {
        JCStatement whileLoop = at(node).WhileLoop(loopCond, at(node).Block(0, loopBody.toList()));
        if (label != null) {
            whileLoop = make().Labelled(label, whileLoop);
        }
        return result.append(whileLoop).toList();
    }
    
    
    /**
     * <p>Transformation of {@code for} loops over {@code Span<Integer>} 
     * or {@code Span<Character>} which avoids allocating a {@code Span} and
     * using an {@code Iterator} like 
     * {@link #ForStatementTransformation} but instead outputs a C-style 
     * {@code while} loop. Because a Span is never empty we can also omit
     * code for handling {@code else} clauses of {@code for} statements when 
     * we know the {@code for} block returns normally</p>
     * 
     * <p>This is able to optimize statements like {@code for (i in first..last) ... },
     * where {@code first}, {@code last} are 
     * expressions (not necessarily literals or compile-time constants).
     * See {@link SpanOpWithStepIterationOptimization} for optimization of 
     * statements like {@code for (i in (first..last).by(step)) ... }
     * 
     * <p>The transformation is complicated by:</p>
     * <ul>
     *   <li>Not knowing at compile-time whether {@code lhs < rhs}, which 
     *       complicates the {@code for} termination condition</li>
     *   <li>Needing to worry about {@code int} or {@code long} overflow
     *       (hence the {@code i-end$ <= 0} rather than the more natural
     *       {@code i <= end}.</li>
     * </ul>
     */
    class SpanOpIterationOptimization extends ForStatementTransformation {
        public static final String OPT_NAME = "RangeOpIteration";
        protected final Tree.RangeOp span;
        protected final Tree.Term first;
        protected final Tree.Term last;
        protected final Tree.Term step;// if null then increment is +/-1
        protected final com.sun.tools.javac.code.Type type;
        protected final Type pt;
        /** the name of the statement label for continue jumps */
        protected final SyntheticName continueName = naming.temp("continue");
        /** the variable holding the Span.first */
        protected final SyntheticName firstName = naming.temp("first");
        /** the variable holding the Span.last */
        protected final SyntheticName lastName = naming.temp("last");
        /** whether this is an increasing span */
        protected final SyntheticName increasingName = naming.temp("increasing");
        /** the increment +/-1 for iteration with a step */
        protected final SyntheticName incrementName = naming.temp("incr");
        /** f: whether this is the first iteration */
        protected final SyntheticName fName = naming.temp("f");
        /** the current element */
        protected final SyntheticName elementName = naming.temp("element");
        /** The (aliased) iteration variable, that is the {@code i} in {@code for (i in first..last) */
        protected final SyntheticName varname;
        
        public SpanOpIterationOptimization(
                Tree.ForStatement stmt,
                Tree.RangeOp range,
                Tree.Term step,
                com.sun.tools.javac.code.Type type) {
            super(stmt);
            this.span = range;
            this.first = range.getLeftTerm();
            this.last = range.getRightTerm();
            this.step = step;
            this.type = type;
            if (isCharacterSpan()) {
                this.pt = typeFact().getCharacterType();
            } else if (isIntegerSpan()) {
                this.pt = typeFact().getIntegerType();
            } else {
                throw new BugException(range, "unhandled Range type: " + type.tag); 
            }
            varname = naming.alias(getVariable().getIdentifier().getText());
        }
        
        protected final boolean isIntegerSpan() {
            return type.tag == syms().longType.tag;
        }
        
        protected final boolean isCharacterSpan() {
            return type.tag == syms().intType.tag;
        }
        
        protected Tree.Variable getVariable() {
            return ((Tree.ValueIterator)stmt.getForClause().getForIterator()).getVariable();
        }
        
        protected JCExpression makeType(boolean boxed) {
            return makeJavaType(pt, boxed ? JT_NO_PRIMITIVES : 0);
        }
        
        protected Type getType() {
            return pt;
        }
        
        @Override
        protected ListBuffer<JCStatement> transformForClause() {
            ListBuffer<JCStatement> result = ListBuffer.<JCStatement>lb();
            at(span);
            // Generate variable decls we'll need
            prelude(result);
            
            /* We're transforming a Ceylon for clause to a Java while clause
               and we want to execute POST-STATEMENTS before the while CONDITION is 
               tested. That means we need to change how we will 
               transform continue statements occurring in the TRANSFORMED-BODY, 
               so we still execute those POST-STATEMENTS statements. 
               We generate this:
                   do {
                     PRE-STATEMENTS 
                     label: do { 
                       TRANSFORMED-BODY 
                     } while(false);
                     POST-STATEMENTS
                   } while(CONDITION);
               and continues in that body becomes a break: label
            */
            Tree.ControlClause prevForclause = currentForClause;
            currentForClause = stmt.getForClause();
            Transformer<JCStatement, Continue> currentContinueTransformer = continueTransformer;
            continueTransformer = new Transformer<JCTree.JCStatement, Tree.Continue>() {
                
                @Override
                public JCStatement transform(Continue tree) {
                    return at(tree).Break(continueName.asName());
                }
            };
            List<JCStatement> blockStatements = List.<JCStatement>of(make().Labelled(
                continueName.asName(),
                // The inner do {...} while (false) used purely as a jump target for continues
                make().DoLoop(
                    make().Block(0, transformBlock(getBlock())),
                    make().Literal(false)
                )));
            continueTransformer = currentContinueTransformer;
            currentForClause = prevForclause;
            
            // Now add the PRE- and POST-STATEMENTS
            blockStatements = decorateBlock(blockStatements);
            
            // The actual loop
            result.append(make().Labelled(this.label, makeLoop(blockStatements)));
            
            
            return result;
        }
        
        /** 
         * Appends statements to appear before the loop, 
         * typically adding variables 
         */
        protected void prelude(ListBuffer<JCStatement> result) {
            
            // Note: Must invoke lhs, rhs and increment in the correct order!
            // long start = <lhs>
            at(first);
            result.append(makeVar(FINAL, firstName, makeType(false), 
                    expressionGen().transformExpression(first, BoxingStrategy.UNBOXED, getType())));
            // long end = <rhs>
            at(last);
            result.append(makeVar(FINAL, lastName, makeType(false), 
                    expressionGen().transformExpression(last, BoxingStrategy.UNBOXED, getType())));
            at(span);
            result.append(makeVar(FINAL, increasingName, make().Type(syms().booleanType),
                    makeIncreasingExpr()));
                    
            if (isCharacterSpan()) {
                /* Because Character.successor and .predecessor can throw when 
                   a Span is constructed, we need to replicate the following logic here:
                       Boolean recursive = first.offsetSign(first.successor) > 0 
                           && last.predecessor.offsetSign(last) > 0;
                 */
                at(span);
                result.append(makeVar(naming.temp(), make().Type(syms().booleanType), 
                    make().Binary(JCTree.AND,
                        make().Binary(JCTree.GT,
                            make().Apply(null,
                                naming.makeQualIdent(make().QualIdent(syms().ceylonCharacterType.tsym), "offsetSign"),
                                List.<JCExpression>of(
                                    firstName.makeIdent(),
                                    make().Apply(null,
                                        naming.makeQualIdent(make().QualIdent(syms().ceylonCharacterType.tsym), "getSuccessor"),
                                            List.<JCExpression>of(firstName.makeIdent())))),
                            make().Literal(0L)),
                        make().Binary(JCTree.GT,
                            make().Apply(null,
                                naming.makeQualIdent(make().QualIdent(syms().ceylonCharacterType.tsym), "offsetSign"),
                                List.<JCExpression>of(
                                    make().Apply(null,
                                        naming.makeQualIdent(make().QualIdent(syms().ceylonCharacterType.tsym), "getPredecessor"),
                                        List.<JCExpression>of(lastName.makeIdent())), 
                                    lastName.makeIdent())), 
                            make().Literal(0L))
                    )
                ));
            }
            at(span);
            result.append(makeVar(FINAL, incrementName, make().Type(syms().longType), 
                    makeIncrementExpr()));
            result.append(makeVar(varname, makeType(false), firstName.makeIdent()));
            result.append(makeVar(elementName, makeType(false), firstName.makeIdent()));
            result.append(makeVar(fName, make().Type(syms().booleanType), make().Literal(false)));
        }
        
        /** The initial value of the {@code long $increment} variable */
        protected JCExpression makeIncrementExpr() {
            return at(span).Conditional(
                    increasingName.makeIdent(), 
                    makeIncreasingIncrement(), makeDecreasingIncrement());
        }
        
        /** The initial value of the {@code boolean $increasing} variable */
        protected JCExpression makeIncreasingExpr() {
            JCBinary incrExpr = at(span).Binary(JCTree.GE, make().Apply(null,
                    naming.makeSelect(makeType(true), "offset"),
                    List.<JCExpression>of(lastName.makeIdent(), firstName.makeIdent())),
                    make().Literal(0));
            return incrExpr;
        }
        
        /** The expression for an increasing increment */
        protected JCExpression makeIncreasingIncrement() {
            if (isCharacterSpan()) {
                // long incr = start < end ? 1 : -1
                return make().Literal(1);
            } else if (isIntegerSpan()) {
                return make().Literal(1L);
            } else {
                return makeErroneous(span, "unhandled Span type: " + type.tag);
            }
        }
        
        /** The expression for a decreasing increment */
        protected JCExpression makeDecreasingIncrement() {
            if (isCharacterSpan()) {
                // long incr = start < end ? 1 : -1
                return make().Literal(-1);
            } else if (isIntegerSpan()) {
                return make().Literal(-1L);
            } else {
                return makeErroneous(span, "unhandled Span type: " + type.tag);
            }
        }
        
        /** Prepend statements to the transformed block */
        protected List<JCStatement> decorateBlock(
                List<JCStatement> blockStatements) {
            blockStatements = blockStatements.prepend(makeDeclareOrAssignTheirLoopVar());
            blockStatements = blockStatements.prepend(make().Exec(makeAssignOurVar()));
            blockStatements = blockStatements.prepend(
                make().Exec(make().Assign(fName.makeIdent(), make().Literal(true))));
            blockStatements = blockStatements.prepend(
                make().If(fName.makeIdent(), make().Exec(makeIncrementElement()), null));
            return blockStatements;
        }
        
        /** Declare or assign the loop variable {@code i = <whatever>} */
        protected JCStatement makeDeclareOrAssignTheirLoopVar() {
            boolean elemBoxed = BooleanUtil.isFalse(getVariable().getDeclarationModel().getUnboxed());
            JCExpression elemInit = varname.makeIdent();
            if (elemBoxed) {
                elemInit = boxType(elemInit, getType());
            }
            return make().VarDef(make().Modifiers(FINAL), 
                    names().fromString(Naming.getVariableName(getVariable())), 
                    makeType(elemBoxed),
                    elemInit);
        }
        
        protected final JCExpression makeAssignOurVar() {
            return make().Assign(varname.makeIdent(), elementName.makeIdent());
        }
        
        /** 
         * The expression used to increment {@code $element} 
         */
        protected JCExpression makeIncrementElement() {
            JCExpression stepExpr;
            if (isCharacterSpan()) {
                stepExpr = at(span).Assign(elementName.makeIdent(), make().Apply(null,
                        naming.makeSelect(makeType(true), "neighbour"),
                        List.<JCExpression>of(elementName.makeIdent(), incrementName.makeIdent())));
            } else {
                stepExpr = at(span).Assignop(JCTree.PLUS_ASG, elementName.makeIdent(), incrementName.makeIdent());
            }
            return stepExpr;
        }
        
        protected JCStatement makeLoop(
                List<JCStatement> blockStatements) {
            return make().DoLoop(make().Block(0, blockStatements), makeLoopCondition(varname));
        }
        
        /** The expression used for the condition on the {@code while} loop */
        protected JCExpression makeLoopCondition(SyntheticName varname) {
            JCExpression cond = at(span).Conditional(increasingName.makeIdent(),
                        make().Binary(JCTree.NE, make().Binary(JCTree.MINUS, varname.makeIdent(), lastName.makeIdent()), makeZero()), 
                        make().Binary(JCTree.NE, make().Binary(JCTree.MINUS, varname.makeIdent(), lastName.makeIdent()), makeZero()));
            return cond;
        }
        
        protected JCExpression makeZero() {
            if (isCharacterSpan()) {
                return make().Literal(0);
            } else if (isIntegerSpan()) {
                return make().Literal(0L);
            } else {
                return makeErroneous(span, "unhandled Range type: " + type.tag);
            }
        }
    }
    
    /**
     * Optimization for statements like 
     * {@code for (i in (lhs..rhs).by(increment)) ... }
     * This differs somewhat from the case without an increment because the 
     * semantics of the iterator we're effectively inlining is quite different.
     * Annoyingly {@code Span.by} returns {@code this} when {@code step==1},
     * which is behaviour we have to replicate. That means that the 
     * {@code for} loop has to be structurally the same as the 
     * step-less {@code for} loop.
     */
    class SpanOpWithStepIterationOptimization extends SpanOpIterationOptimization {
        /** The variable holding the Span.by(step) */
        protected final SyntheticName stepName = naming.temp("step");
        
        public SpanOpWithStepIterationOptimization(ForStatement stmt,
                RangeOp range, Term increment,
                com.sun.tools.javac.code.Type type) {
            super(stmt, range, increment, type);
        }
        
        @Override
        protected void prelude(ListBuffer<JCStatement> result) {
            // $step = increment;
            result.append(at(step).VarDef(make().Modifiers(FINAL), stepName.asName(), make().Type(syms().longType), 
                    expressionGen().transformExpression(step, BoxingStrategy.UNBOXED, getType())));
            // if ($step <= 0) throw Exception("step size must be greater than zero");
            result.append(at(step).If(
                    make().Binary(JCTree.LE, stepName.makeIdent(), make().Literal(0)),
                    makeThrowAssertionException(
                            new AssertionExceptionMessageBuilder(null)
                                .appendViolatedCondition("step > 0")
                                .prependAssertionDoc("step size must be greater than zero")
                                .build()),
                    null));
                    
            super.prelude(result);
        }
        
        @Override
        protected final List<JCStatement> decorateBlock(
                List<JCStatement> blockStatements) {
            // i = $i
            blockStatements = blockStatements.prepend(makeDeclareOrAssignTheirLoopVar());
            // $i = $n
            blockStatements = blockStatements.prepend(make().Exec(makeAssignOurVar()));
            // $f = true;
            blockStatements = blockStatements.prepend(
                make().Exec(make().Assign(fName.makeIdent(), make().Literal(true))));
            // if ($f && step == 1) n=neighbour($i, $step);
            blockStatements = blockStatements.prepend(
                make().If(
                    make().Binary(JCTree.AND, 
                        fName.makeIdent(), 
                        make().Binary(JCTree.EQ, 
                            stepName.makeIdent(), 
                            make().Literal(1L))),
                    make().Exec(makeIncrementElement()), 
                    null));
            
            if (!getBlock().getDefinitelyReturns()) {
                // if (step != 1) $n=neighbour($i, $step)
                blockStatements = blockStatements.append(
                    make().If(
                        make().Binary(JCTree.NE, 
                            stepName.makeIdent(), 
                            make().Literal(1L)),
                        make().Exec(makeIncrementElement()), null));
            }
            
            return blockStatements;
        }
        
        @Override
        protected JCStatement makeLoop(
                List<JCStatement> blockStatements) {
                // loop condition is on varname if step == 1 otherwise on n 
            return make().DoLoop(make().Block(0, blockStatements),
            make().Conditional(make().Binary(JCTree.EQ, 
                        stepName.makeIdent(), 
                        make().Literal(1L)),
            makeLoopCondition(varname), makeLoopCondition(elementName)));
        }
        
        @Override
        protected JCExpression makeIncrementExpr() {
            JCConditional l = make().Conditional(
                    increasingName.makeIdent(), 
                    makeIncreasingIncrement(), super.makeDecreasingIncrement());
            return unitStep(stepName, l,
                    at(span).Conditional(
                            increasingName.makeIdent(), 
                            makeIncreasingIncrement(), makeDecreasingIncrement()));
        }
        
        @Override
        protected JCExpression makeIncreasingExpr() {
            return unitStep(stepName, 
                    super.makeIncreasingExpr(), 
                    at(span).Binary(JCTree.GE, 
                            make().Apply(null,
                                    naming.makeSelect(makeType(true), "offsetSign"),
                                    List.<JCExpression>of(lastName.makeIdent(), firstName.makeIdent())),
                        make().Literal(0)));
        }
        
        private  JCExpression unitStep(Naming.SyntheticName by, JCExpression withoutBy, JCExpression withBy) {
            if (by == null) {
                return withoutBy;
            } else {
                return make().Conditional(make().Binary(JCTree.EQ, by.makeIdent(), make().Literal(1)),
                        withoutBy, withBy);
            }
        }
        
        @Override
        protected JCExpression makeIncreasingIncrement() {
            // long incr = increasing ? by : -by;
            return stepName.makeIdent();
        }
        
        @Override
        protected JCExpression makeDecreasingIncrement() {
            // long incr = increasing ? by : -by;
            return make().Unary(JCTree.NEG, stepName.makeIdent());
        }
        
        @Override
        protected JCExpression makeLoopCondition(SyntheticName varname) {
            JCExpression cond = unitStep(stepName, super.makeLoopCondition(varname),
                    at(span).Conditional(increasingName.makeIdent(),
                        make().Binary(JCTree.AND, 
                            make().Binary(JCTree.LE, make().Apply(null,
                                    naming.makeSelect(makeType(true), "offsetSign"),
                                    List.<JCExpression>of(varname.makeIdent(), lastName.makeIdent())), 
                                    makeZero()),
                            make().Binary(JCTree.GE, make().Apply(null,
                                    naming.makeSelect(makeType(true), "offsetSign"),
                                    List.<JCExpression>of(varname.makeIdent(), firstName.makeIdent())), 
                                    makeZero())
                        ),
                        make().Binary(JCTree.AND, 
                                make().Binary(JCTree.GE, make().Apply(null,
                                        naming.makeSelect(makeType(true), "offsetSign"),
                                        List.<JCExpression>of(varname.makeIdent(), lastName.makeIdent())), 
                                        makeZero()),
                                make().Binary(JCTree.LE, make().Apply(null,
                                        naming.makeSelect(makeType(true), "offsetSign"),
                                        List.<JCExpression>of(varname.makeIdent(), firstName.makeIdent())), 
                                        makeZero())
                        )
                    )
                );
            return cond;
        }
        
        @Override
        protected JCExpression makeIncrementElement() {
            JCExpression stepExpr  = unitStep(stepName, super.makeIncrementElement(), make().Assign(elementName.makeIdent(),
                    at(step).Apply(null,
                            naming.makeSelect(makeType(true), "neighbour"),
                            List.<JCExpression>of(elementName.makeIdent(), incrementName.makeIdent()))));
            return stepExpr;
        }
    }

    /**
     * <p>In some cases (#1227) we can have a deferred specification of a
     * non-variable value <i>x</i> which is assigned in a control structure
     * (for/else) in such a way that javac cannot prove the transformed 
     * variable is assigned exactly once.</p>
     * 
     * <p>In these cases the transformation proceeds thusly:</p>
     * <ul>
     * <li>The AttributeDeclaration is transformed to a final var <tt>x</tt>
     * and a non-final var <tt>x$</tt> and the "outer substitution"
     * {@code x->x$} is installed.</li>
     * <li>SpecificationStatements to <i>x</i> within the control structure 
     * are transformed assignments to <tt>x$</tt>. At these points we
     * also generate a final <tt>x$$</tt> and install an "inner substitution"
     * {@code x->x$$} so that captured references within the control
     * structure still work.</li>
     * <li>At end end of the else Blocks of for/else and at 
     * Break, Continue, Throw and Return statements within the
     * control structure we remove the "inner substitution" {@code x->x$$}
     * <li>At the end of the control structure we assign <tt>x = x$</tt>
     * and remove the "outer substitution" {@code x->x$}</li>
     * </ul>
     */
    class DeferredSpecification {
        
        private final Type type;
        private final long modifiers;
        private final Value value;
        private final List<JCAnnotation> annots;
        private SyntheticName outerAlias;
        private Naming.Substitution outerSubst = null;
        private SyntheticName innerAlias;
        private Naming.Substitution innerSubst = null;
        
        public DeferredSpecification(Value value, int modifiers, List<JCAnnotation> annots, Type type) {
            this.value = value;
            this.modifiers = modifiers;
            this.annots = annots;
            this.type = type;
        }
        
        /**
         * Installs the "outer substitution" and 
         * makes a non-{@code final} variable declaration for use where 
         * the ceylon value is declared:
         * <pre>
         *     TYPE NAME$1 = DEFAULT_FOR_TYPE; 
         * </pre>
         * The caller is expected to generate the corresponding 
         * {@code final} declaration:
         * <pre>
         *     final TYPE NAME;
         * </pre>
         */
        public JCStatement openOuterSubstitution() {
            if (outerSubst != null || outerAlias != null) {
                throw new BugException("An Outer substitution (" + outerSubst + ") is already open");
            }
            this.outerAlias = naming.alias(value.getName());
            // TODO Annots
            try (SavedPosition pos = noPosition()) {
                JCExpression valueExpr = makeDefaultExprForType(type);
                JCExpression typeExpr;
                if (value.getUnboxed() == false) {
                    typeExpr = makeJavaType(type, AbstractTransformer.JT_NO_PRIMITIVES); 
                    valueExpr = boxType(valueExpr, type);
                } else {
                    typeExpr = makeJavaType(type); 
                }
                return make().VarDef(
                        make().Modifiers(modifiers & ~FINAL, annots), 
                        outerAlias.asName(), 
                        typeExpr, 
                        valueExpr);
            }
        }

        /**
         * Installs an outer substitution in Naming, we can't do this in openOuterSubstitution since that
         * would also make the substitution visible outside the "for" block. This is meant to be done when
         * we enter the "for" block.
         */
        public void installOuterSubstitution(){
            this.outerSubst = naming.addVariableSubst(value, outerAlias.getName());
        }
        
        /**
         * Installs the "inner substitution" and makes a final 
         * variable declaration:
         * <pre> 
         *   final TYPE NAME$2 = NAME$1;
         * </pre>
         * The caller is expected to generare the preceeding assignment:
         * <pre>
         *   NAME$1 = WHATEVER;
         * </pre>
         */
        public JCStatement openInnerSubstitution() {
            if (innerSubst != null || innerAlias != null) {
                throw new BugException("An inner substitution (" + innerSubst + ") is already open");
            }
            try (SavedPosition pos = noPosition()) {
                innerAlias = naming.alias(value.getName());
                JCStatement result = makeVar(
                        modifiers, 
                        innerAlias.getName(), 
                        makeJavaType(type, (value.getUnboxed() == false) ? AbstractTransformer.JT_NO_PRIMITIVES : 0), 
                        naming.makeName(value, Naming.NA_IDENT));
                innerSubst = naming.addVariableSubst(value, innerAlias.getName());
                return result;
            }
        }
        
        /**
         * Removes the "inner substitution" installed by {@link #openInnerSubstitution()}
         */
        public void closeInnerSubstitution() {
            if (innerSubst == null || innerAlias == null) {
                throw new BugException("No inner substitution to close");
            }
            innerSubst.close();
            innerSubst = null;
            innerAlias = null;
        }
        
        /**
         * Removes the "outer substitution" and returns an assignment 
         * to the {@code final} variable after the control structure
         * <pre>
         *     NAME = NAME$1;
         * </pre>
         */
        public JCStatement closeOuterSubstitution() {
            if (outerSubst == null) {
                throw new BugException("No outer substitution to close");
            }
            try (SavedPosition pos = noPosition()) {
                JCExpression alias = naming.makeName(value, Naming.NA_IDENT);
                outerSubst.close();
                outerSubst = null;
                JCExpression var = naming.makeName(value, Naming.NA_IDENT);
                return make().Exec(make().Assign(var, alias));
            }
        }
    }
    
    private HashMap<Value, DeferredSpecification> deferredSpecifications = new HashMap<Value, DeferredSpecification>();
    
    public DeferredSpecification getDeferredSpecification(Declaration value) {
        return deferredSpecifications.get(value);
    }
    
    /**
     * Removes the "inner substitutions" for any deferred values specified 
     * in the given control block
     */
    private void closeInnerSubstituionsForSpecifiedValues(Tree.ControlClause contolClause) {
        if (contolClause != null) {
            ControlBlock controlBlock = contolClause.getControlBlock();
            java.util.Set<Value> assigned = controlBlock.getSpecifiedValues();
            if (assigned != null) {
                for (Value value : assigned) {
                    DeferredSpecification ds = statementGen().getDeferredSpecification(value);
                    if (ds != null) {
                        ds.closeInnerSubstitution();
                    }
                }
            }
        }
    }
    
    // FIXME There is a similar implementation in ClassGen!
    public List<JCStatement> transform(Tree.AttributeDeclaration decl) {
        ListBuffer<JCStatement> result = ListBuffer.<JCStatement> lb();
        // If the attribute is really from a parameter then don't generate a local variable
        Parameter parameter = CodegenUtil.findParamForDecl(decl);
        if (parameter == null) {
            
            final Name attrName = names().fromString(naming.substitute(decl.getDeclarationModel()));
            
            Type t = decl.getDeclarationModel().getType();
            
            JCExpression initialValue = null;
            SpecifierOrInitializerExpression initOrSpec = decl.getSpecifierOrInitializerExpression();
            if (initOrSpec != null) {
                HasErrorException error = errors().getFirstExpressionErrorAndMarkBrokenness(initOrSpec.getExpression().getTerm());
                if (error != null) {
                    return List.<JCStatement>of(this.makeThrowUnresolvedCompilationError(error));
                }
                initialValue = expressionGen().transformExpression(initOrSpec.getExpression(), 
                        CodegenUtil.getBoxingStrategy(decl.getDeclarationModel()), 
                        decl.getDeclarationModel().getType());
            } else if (decl.getDeclarationModel().isVariable()) {
                // Java's definite initialization doesn't always work 
                // so give variable attribute declarations without 
                // initializers a default value. See #1153.
                initialValue = makeDefaultExprForType(t);
                if (CodegenUtil.getBoxingStrategy(decl.getDeclarationModel()) == BoxingStrategy.BOXED && canUnbox(t)) {
                    initialValue = boxType(initialValue, t);
                }
            }
            
            List<JCAnnotation> annots = List.<JCAnnotation>nil();
            
            int modifiers = transformLocalFieldDeclFlags(decl);
            JCExpression typeExpr = makeJavaType(decl.getDeclarationModel(), t, modifiers);
            result.append(at(decl.getIdentifier()).VarDef(at(decl.getIdentifier()).Modifiers(modifiers, annots), attrName, typeExpr, initialValue));
            
            JCStatement outerSubs = openOuterSubstitutionIfNeeded(
                    decl.getDeclarationModel(), t, annots, modifiers);
            if (outerSubs != null) {
                result.append(outerSubs);
            }
        }
        return result.toList();
    }

    JCStatement openOuterSubstitutionIfNeeded(
            Value value, Type t,
            List<JCAnnotation> annots, int modifiers) {
        JCStatement result = null;
        if (value.isSpecifiedInForElse()) {
            DeferredSpecification d = new DeferredSpecification(value, modifiers, annots, t);
            deferredSpecifications.put(value, d);
            result = d.openOuterSubstitution();
        }
        return result;
    }
    
    List<JCStatement> transform(Tree.Break stmt) {
        // break;
        return defaultBreakTransformer.transform(stmt);
    }
    class DefaultBreakTransformer implements Transformer<List<JCStatement>, Tree.Break> {

        @Override
        public List<JCStatement> transform(Break stmt) {
           // Remove the inner substitutions for any deferred values specified 
            // in the control block
            closeInnerSubstituionsForSpecifiedValues(currentForClause);
            
            JCStatement brk = at(stmt).Break(getLabel(stmt));
        
            if (currentForFailVariable != null) {
                JCIdent failtest_id = at(stmt).Ident(currentForFailVariable);
                List<JCStatement> list = List.<JCStatement> of(at(stmt).Exec(at(stmt).Assign(failtest_id, make().Literal(TypeTags.BOOLEAN, 0))));
                list = list.append(brk);
                return list;
            } else {
                return List.<JCStatement> of(brk);
            }
        }
    }

    JCStatement transform(Tree.Continue stmt) {
        // continue;
        return continueTransformer.transform(stmt);
    }
    class DefaultContinueTransformer implements Transformer<JCStatement, Tree.Continue> {

        @Override
        public JCStatement transform(Continue stmt) {
            return at(stmt).Continue(getLabel(stmt));
        }
        
    }
    
    Transformer<JCStatement, Tree.Return> returnTransformer(Transformer<JCStatement, Tree.Return> tx) {
        Transformer<JCStatement, Tree.Return> result = this.returnTransformer;
        this.returnTransformer = tx;
        return result;
        
    }
    JCStatement transform(Tree.Return ret) {
        return returnTransformer.transform(ret);
    }
    
    class ConstructorReturnTransformer implements Transformer<JCStatement, Tree.Return> {
        private final Name label;

        public ConstructorReturnTransformer(Name label) {
            this.label = label;
        }
        
        public JCStatement transform(Tree.Return ret) {
            // Remove the inner substitutions for any deferred values specified 
            // in the control block
            closeInnerSubstituionsForSpecifiedValues(currentForClause);
            at(ret);
            return at(ret).Break(label);
        }
    }
    
    class DefaultReturnTransformer implements Transformer<JCStatement, Tree.Return> {
        public JCStatement transform(Tree.Return ret) {
            // Remove the inner substitutions for any deferred values specified 
            // in the control block
            closeInnerSubstituionsForSpecifiedValues(currentForClause);
            Tree.Expression expr = ret.getExpression();
            JCExpression returnExpr = null;
            at(ret);
            if (expr != null) {
                boolean prevNoExpressionlessReturn = noExpressionlessReturn;
                try {
                    noExpressionlessReturn = false;
                    // we can cast to TypedDeclaration here because return with expressions are only in Function or Value
                    TypedDeclaration declaration = (TypedDeclaration)ret.getDeclaration();
                    returnExpr = expressionGen().transformExpression(declaration, expr.getTerm());
                    // make sure all returns from hash are properly turned into ints
                    returnExpr = convertToIntIfHashAttribute(declaration, returnExpr);
                } finally {
                    noExpressionlessReturn = prevNoExpressionlessReturn;
                }
            } else if (noExpressionlessReturn) {
                returnExpr = makeNull();
            }
            return at(ret).Return(returnExpr);
        }
    }

    public JCStatement transform(Tree.Throw t) {
        // Remove the inner substitutions for any deferred values specified 
        // in the control block
        closeInnerSubstituionsForSpecifiedValues(currentForClause);
        at(t);
        Tree.Expression expr = t.getExpression();
        final JCExpression exception;
        if (expr == null) {// bare "throw;" stmt
            exception = make().NewClass(null, null,
                    makeIdent(syms().ceylonExceptionType),
                    List.<JCExpression>of(makeNull(), makeNull()),
                    null);
        } else {
            // we must unerase the exception to Throwable
            Type exceptionType = expr.getTypeModel().getSupertype(t.getUnit().getThrowableDeclaration());
            exception = gen().expressionGen().transformExpression(expr, BoxingStrategy.UNBOXED, exceptionType);
        }
        return make().Throw(exception);
    }
    
    public JCStatement transform(Tree.TryCatchStatement t) {
        Tree.TryClause tryClause = t.getTryClause();
        at(tryClause);
        
        JCBlock tryBlock = transform(tryClause.getBlock());

        Tree.ResourceList resList = tryClause.getResourceList();
        if (resList != null) {
            ArrayList<Tree.Resource> resources = new ArrayList<Tree.Resource>(resList.getResources());
            Collections.reverse(resources);
            for (Tree.Resource res : resources) {
                List<JCStatement> stats = List.nil();
                
                Tree.Expression resExpr;
                String resVarName;
                if (res.getExpression() != null) {
                    resExpr = res.getExpression();
                    resVarName = naming.newTemp("try");
                } else if (res.getVariable() != null) {
                    Tree.Variable var = res.getVariable();
                    resExpr = var.getSpecifierExpression().getExpression();
                    resVarName = var.getIdentifier().getText();
                } else {
                    throw new BugException(res, "missing resource expression");
                }
                boolean isDestroyable = typeFact().getDestroyableType().isSupertypeOf(resExpr.getTypeModel());
                Type resVarType = resExpr.getTypeModel();
                Type resVarExpectedType = isDestroyable 
                        ? typeFact().getDestroyableType()
                        : typeFact().getObtainableType();
                
                // CloseableType $var = resource-expression
                JCExpression expr = expressionGen().transformExpression(resExpr);
                JCExpression javaType = makeJavaType(resVarType);
                JCVariableDecl var = makeVar(FINAL, resVarName, javaType, expr);
                stats = stats.append(var);
                
                // $var.open() /// ((Closeable)$var).open()
                
                if (!isDestroyable) {
                    JCExpression resVar0 = expressionGen().applyErasureAndBoxing(makeUnquotedIdent(resVarName), resVarType, true, BoxingStrategy.BOXED, resVarExpectedType);
                    JCMethodInvocation openCall = make().Apply(null, makeQualIdent(resVar0, "obtain"), List.<JCExpression>nil());
                    stats = stats.append(make().Exec(openCall));
                }
                
                // Exception $tpmex = null;
                String innerExTmpVarName = naming.newTemp("ex");
                JCExpression innerExType = makeJavaType(typeFact().getThrowableType(), JT_CATCH);
                JCVariableDecl innerExTmpVar = makeVar(innerExTmpVarName, innerExType, makeNull());
                stats = stats.append(innerExTmpVar);
                
                // $tmpex = ex;
                List<JCStatement> innerCatchStats = List.nil();
                Name innerCatchVarName = naming.tempName("ex");
                JCAssign exTmpAssign = make().Assign(makeUnquotedIdent(innerExTmpVarName), make().Ident(innerCatchVarName));
                innerCatchStats = innerCatchStats.append(make().Exec(exTmpAssign));
                
                // throw ex;
                JCThrow innerCatchThrow = make().Throw(make().Ident(innerCatchVarName));
                innerCatchStats = innerCatchStats.append(innerCatchThrow);
                JCBlock innerCatchBlock = make().Block(0, innerCatchStats);
                
                // $var.close() /// ((Closeable)$var).close()
                JCExpression exarg = makeUnquotedIdent(innerExTmpVarName);
                JCExpression resVar1 = expressionGen().applyErasureAndBoxing(makeUnquotedIdent(resVarName), resVarType, true, BoxingStrategy.BOXED, resVarExpectedType);
                JCMethodInvocation closeCall = make().Apply(null, makeQualIdent(resVar1, isDestroyable ? "destroy" : "release"), List.<JCExpression>of(exarg));
                JCBlock closeTryBlock = make().Block(0, List.<JCStatement>of(make().Exec(closeCall)));
                
                // try { $var.close() } catch (Exception closex) { $tmpex.addSuppressed(closex); }
                Name closeCatchVarName = naming.tempName("closex");
                JCExpression closeCatchExType = makeJavaType(typeFact().getThrowableType(), JT_CATCH);
                JCVariableDecl closeCatchVar = make().VarDef(make().Modifiers(Flags.FINAL), closeCatchVarName, closeCatchExType, null);
                JCExpression addarg = make().Ident(closeCatchVarName);
                JCMethodInvocation addSuppressedCall = make().Apply(null, makeQualIdent(makeUnquotedIdent(innerExTmpVarName), "addSuppressed"), List.<JCExpression>of(addarg));
                JCCatch closeCatch = make().Catch(closeCatchVar, make().Block(0, List.<JCStatement>of(make().Exec(addSuppressedCall))));
                JCTry closeTry = at(res).Try(closeTryBlock, List.<JCCatch>of(closeCatch), null);
                
                // $var.close() /// ((Closeable)$var).close()
                JCExpression exarg2 = makeUnquotedIdent(innerExTmpVarName);
                JCExpression resVar2 = expressionGen().applyErasureAndBoxing(makeUnquotedIdent(resVarName), resVarType, true, BoxingStrategy.BOXED, resVarExpectedType);
                JCMethodInvocation closeCall2 = make().Apply(null, makeQualIdent(resVar2, isDestroyable ? "destroy" : "release"), List.<JCExpression>of(exarg2));
                
                // if ($tmpex != null) { ... } else { ... }
                JCBinary closeCatchCond = make().Binary(JCTree.NE, makeUnquotedIdent(innerExTmpVarName), makeNull());
                JCIf closeCatchIf = make().If(closeCatchCond, closeTry, make().Exec(closeCall2));
    
                // try { .... } catch (Exception ex) { $tmpex=ex; throw ex; }
                // finally { try { $var.close() } catch (Exception closex) { } }
                JCExpression innerCatchExType = makeJavaType(typeFact().getThrowableType(), JT_CATCH);
                JCVariableDecl innerCatchVar = make().VarDef(make().Modifiers(Flags.FINAL), innerCatchVarName, innerCatchExType, null);
                JCCatch innerCatch = make().Catch(innerCatchVar, innerCatchBlock);
                JCBlock innerFinallyBlock = make().Block(0, List.<JCStatement>of(closeCatchIf));
                JCTry innerTry = at(res).Try(tryBlock, List.<JCCatch>of(innerCatch), innerFinallyBlock);
                stats = stats.append(innerTry);
                
                tryBlock = at(res).Block(0, stats);
            }
        }
        
        final List<JCCatch> catches;
        if (usePolymorphicCatches(t.getCatchClauses())) {
            catches = transformCatchesPolymorphic(t.getCatchClauses());
        } else {
            catches = transformCatchesIfElseIf(t.getCatchClauses());
        }
        
        final JCBlock finallyBlock;
        Tree.FinallyClause finallyClause = t.getFinallyClause();
        if (finallyClause != null) {
            at(finallyClause);
            finallyBlock = transform(finallyClause.getBlock());
        } else {
            finallyBlock = null;
        }
        
        if (!catches.isEmpty() || finallyBlock != null) {
            return at(t).Try(tryBlock, catches, finallyBlock);
        } else {
            return tryBlock;
        }
        
        
    }

    /**
     * Determines whether the {@code CatchClause}s contain any 
     * intersections or parameterised exception types. If so, the
     * we have to transform the {@code CatchClause}s using 
     * {@link #transformCatchesIfElseIf(java.util.List)}, otherwise we can
     * let the JVM do the heavy lifting an use 
     * {@link #transformCatchesPolymorphic(java.util.List)}.
     */
    boolean usePolymorphicCatches(Iterable<Tree.CatchClause> catchClauses) {
        for (Tree.CatchClause catchClause : catchClauses) {
            Type type = catchClause.getCatchVariable().getVariable().getType().getTypeModel();
            if (isOrContainsError(type)) {
                return false;
            }
            if (type.getDeclaration().isParameterized()) {
                // e.g. E<T>
                return false;
            } else if (typeFact().isIntersection(type)) {
                // e.g. E&Interface
                return false;
            } else if (typeFact().isUnion(type)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Transforms a list of {@code CatchClause}s to a corresponding list 
     * of {@code JCCatch}.
     * @see #transformCatchesIfElseIf(java.util.List)
     */
    private List<JCCatch> transformCatchesPolymorphic(
            java.util.List<Tree.CatchClause> catchClauses) {
        final ListBuffer<JCCatch> catches = ListBuffer.<JCCatch>lb();
        for (Tree.CatchClause catchClause : catchClauses) {
            at(catchClause);
            Tree.Variable variable = catchClause.getCatchVariable().getVariable();
            Type exceptionType = variable.getDeclarationModel().getType();
            JCExpression type = makeJavaType(exceptionType, JT_CATCH);
            JCVariableDecl param = make().VarDef(
                    make().Modifiers(Flags.FINAL), 
                    names().fromString(variable.getIdentifier().getText()),
                    type, null);
            catches.add(make().Catch(param, transform(catchClause.getBlock())));
            
        }
        return catches.toList();
    }


    private boolean isOrContainsError(Type exceptionType) {
        if (exceptionType.isTypeAlias()) {
            return isOrContainsError(exceptionType.resolveAliases());
        } else if (exceptionType.isUnion()) {
            for (Type t : exceptionType.getCaseTypes()) {
                if (isOrContainsError(t)) {
                    return true;
                }
            }
            return false;
        } else if (exceptionType.isIntersection()) {
            for (Type t : exceptionType.getSatisfiedTypes()) {
                if (isOrContainsError(t)) {
                    return true;
                }
            }
            return false;
        } else {
            TypeDeclaration declaration = exceptionType.getDeclaration();
            return "java.lang::Error".equals(declaration.getQualifiedNameString());
        }
    }

    /**
     * Transforms a list of {@code CatchClause}s to a single {@code JCCatch} 
     * containing and if/else if chain for finding the appropriate catch block.
     * @see #transformCatchesPolymorphic(java.util.List)
     */
    private List<JCCatch> transformCatchesIfElseIf(
            java.util.List<Tree.CatchClause> catchClauses) {
        Type supertype = intersectionOfCatchClauseTypes(catchClauses);
        JCExpression exceptionType = makeJavaType(supertype, JT_CATCH | JT_RAW);
        SyntheticName exceptionVar = naming.alias("exception");
        JCVariableDecl param = make().VarDef(
                make().Modifiers(Flags.FINAL), 
                exceptionVar.asName(),
                exceptionType, null);
        
        ArrayList<Tree.CatchClause> reversed = new ArrayList<Tree.CatchClause>(catchClauses);
        Collections.reverse(reversed);
        
        JCStatement elsePart = make().Throw(exceptionVar.makeIdent());
        
        for (Tree.CatchClause catchClause : reversed) {
            Tree.Variable caughtVar = catchClause.getCatchVariable().getVariable();
            Type caughtType = caughtVar.getType().getTypeModel();
            List<JCStatement> catchBlock = transformBlock(catchClause.getBlock());
            catchBlock = catchBlock.prepend(
                    makeVar(FINAL,
                        caughtVar.getIdentifier().getText(), 
                        makeJavaType(caughtType), 
                        expressionGen().applyErasureAndBoxing(exceptionVar.makeIdent(), 
                                supertype, true, true, BoxingStrategy.BOXED, caughtType, 0)));
            elsePart = make().If(makeOptimizedTypeTest(null, exceptionVar, caughtType, supertype), 
                    make().Block(0, catchBlock), 
                    elsePart);
        }
        return List.of(make().Catch(param, make().Block(0, List.<JCStatement>of(elsePart))));
    }
    
    /**
     * <p>When transforming {@code CatchClause}s using 
     * {@link #transformCatchesIfElseIf(java.util.List)} we want the single 
     * {@code catch} clause to have the most specific Java Exception type
     * applicable to all the Ceylon Exception types in the list.</p>
     * 
     * <p><strong>Note:</strong> This method can return parameterised types
     * whose parameters are taken from their declaration (i.e. there's 
     * no corresponding Java type parameter in scope). That should be OK, 
     * because we're only interested in catching the raw type.</p>
     */
    private Type intersectionOfCatchClauseTypes(
            java.util.List<Tree.CatchClause> catchClauses) {
        Type result = typeFact().getNothingType();
        for (Tree.CatchClause catchClause : catchClauses) {
            Type pt = catchClause.getCatchVariable().getVariable().getType().getTypeModel();
            result = ModelUtil.unionType(result, exceptionSupertype(pt), typeFact());
        }
        if (typeFact().isUnion(result)) {
            return result.getSupertype(typeFact().getThrowableDeclaration());
        }
        return result;
    }

    private Type exceptionSupertype(Type pt) {
        Type result = typeFact().getNothingType();
        if (typeFact().isUnion(pt)) {
            for (Type t : pt.getCaseTypes()) {
                result = ModelUtil.unionType(result, exceptionSupertype(t), typeFact());
            }
        } else if (typeFact().isIntersection(pt)) {
            for (Type t : pt.getSatisfiedTypes()) {
                if (t.isSubtypeOf(typeFact().getThrowableType())) {
                    result = ModelUtil.unionType(result, exceptionSupertype(t), typeFact());
                }
            }
        } else if (pt.isSubtypeOf(typeFact().getThrowableType())) {
            if (pt.getDeclaration().isParameterized()) {
                // We do this to avoid ending up with a union ExG<Foo>|ExG<Bar>
                // when we're actually going to go raw, so ExG would be fine
                return pt.getDeclaration().getType();
            }
            return pt;
        } 
        return result;
    }
    
    private int transformLocalFieldDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= cdecl.getDeclarationModel().isVariable() ? 0 : FINAL;

        return result;
    }

    protected JCBlock transformElseClauseBlock(Tree.ElseClause elseClause, String tmpVar, Tree.Term outerExpression, Type expectedType){
        return make().Block(0, transformElseClause(elseClause, tmpVar, outerExpression, expectedType));
    }

    protected List<JCStatement> transformElseClause(Tree.ElseClause elseClause, String tmpVar, Tree.Term outerExpression, Type expectedType){
        if(elseClause.getBlock() != null)
            return transformBlock(elseClause.getBlock());
        if(elseClause.getExpression() != null)
            return evaluateAndAssign(tmpVar, elseClause.getExpression(), outerExpression, expectedType);
        return List.<JCStatement>of(make().Exec(makeErroneous(elseClause, "Only block or expression allowed")));
    }

    protected JCBlock transformCaseClauseBlock(Tree.CaseClause caseClause, String tmpVar, Tree.Term outerExpression, Type expectedType){
        return make().Block(0, transformCaseClause(caseClause, tmpVar, outerExpression, expectedType));
    }

    protected List<JCStatement> transformCaseClause(Tree.CaseClause caseClause, String tmpVar, Tree.Term outerExpression, Type expectedType){
        int p = unblock();
        List<JCStatement> result;
        if(caseClause.getBlock() != null)
            result = transformBlock(caseClause.getBlock());
        else if(caseClause.getExpression() != null)
            result = evaluateAndAssign(tmpVar, caseClause.getExpression(), outerExpression, expectedType);
        else
            result = List.<JCStatement>of(make().Exec(makeErroneous(caseClause, "Only block or expression allowed")));
        _at(p);
        block();
        return result;
    }

    protected Type switchExpressionType(Tree.SwitchClause switchClause) {
        Switched sw = switchClause.getSwitched();
        if (sw.getExpression() != null) {
            return sw.getExpression().getTypeModel();
        } else if (sw.getVariable() != null) {
            return sw.getVariable().getType().getTypeModel();
        }
        throw new BugException("Switch should have expression or variable");
    }
    
    protected Boolean switchExpressionUnboxed(Tree.SwitchClause switchClause) {
        Switched sw = switchClause.getSwitched();
        if (sw.getExpression() != null) {
            return sw.getExpression().getUnboxed();
        } else if (sw.getVariable() != null) {
            return sw.getVariable().getDeclarationModel().getUnboxed();
        }
        throw new BugException("Switch should have expression or variable");
    }
    
    abstract class SwitchTransformation {
        public SwitchTransformation() {
        }
        protected boolean hasVariable(Tree.SwitchClause switchClause) {
            return switchClause.getSwitched().getVariable() != null;
        }
        protected Expression getSwitchExpression(Tree.SwitchClause switchClause) {
            Switched sw = switchClause.getSwitched();
            if (sw.getExpression() != null) {
                return sw.getExpression();
            } else if (sw.getVariable() != null) {
                return sw.getVariable().getSpecifierExpression().getExpression();
            }
            throw new BugException("Switch should have expression or variable");
        }
        protected Type getSwitchExpressionType(Tree.SwitchClause switchClause) {
            return switchExpressionType(switchClause);
        }
        protected Boolean getSwitchExpressionUnboxed(Tree.SwitchClause switchClause) {
            return switchExpressionUnboxed(switchClause);
        }
        protected Type getDefiniteSwitchExpressionType(Tree.SwitchClause switchClause) {
            return typeFact().getDefiniteType(getSwitchExpressionType(switchClause));
        }
        protected java.util.List<CaseClause> getCaseClauses(Tree.SwitchClause switchClause, Tree.SwitchCaseList caseList) {
            return caseList.getCaseClauses();
        }
        protected JCStatement transformElse(Naming.SyntheticName selectorAlias, Tree.SwitchCaseList caseList, String tmpVar, 
                Tree.Term outerExpression, Type expectedType, boolean primitiveSelector) {
            Tree.ElseClause elseClause = caseList.getElseClause();
            if (elseClause != null) {
                if (elseClause.getVariable() != null && selectorAlias != null) {
                    at(elseClause);
                    // Use the type of the variable, which is more precise than the type we test for.
                    Type varType = elseClause.getVariable().getDeclarationModel().getType();
                    
                    String name = elseClause.getVariable().getIdentifier().getText();
                    TypedDeclaration varDecl = elseClause.getVariable().getDeclarationModel();
    
                    Naming.SyntheticName tmpVarName = selectorAlias;
                    Name substVarName;
                    List<JCStatement> stats;
                    if(primitiveSelector){
                        substVarName = tmpVarName.asName();
                        stats = List.<JCStatement> nil();
                    }else{
                        substVarName = naming.aliasName(name);

                        // Want raw type for instanceof since it can't be used with generic types
                        JCExpression rawToTypeExpr = makeJavaType(varType, JT_NO_PRIMITIVES | JT_RAW);

                        // Substitute variable with the correct type to use in the rest of the code block

                        JCExpression tmpVarExpr = at(elseClause).TypeCast(rawToTypeExpr, tmpVarName.makeIdent());
                        JCExpression toTypeExpr;
                        if (isCeylonBasicType(varType) && BooleanUtil.isTrue(varDecl.getUnboxed())) {
                            toTypeExpr = makeJavaType(varType);
                            tmpVarExpr = unboxType(tmpVarExpr, varType);
                        } else {
                            toTypeExpr = makeJavaType(varType, JT_NO_PRIMITIVES);
                            if (BooleanUtil.isTrue(varDecl.getUnboxed())) {
                                tmpVarExpr = boxType(tmpVarExpr, varType);
                            } else if (varDecl.getOriginalDeclaration() != null && BooleanUtil.isTrue(varDecl.getOriginalDeclaration().getUnboxed())) {
                                tmpVarExpr = boxType(tmpVarName.makeIdent(), varType);
                            }
                        }

                        // The variable holding the result for the code inside the code block
                        JCVariableDecl decl2 = at(elseClause).VarDef(make().Modifiers(FINAL), substVarName, toTypeExpr, tmpVarExpr);
                        
                        stats = List.<JCStatement> of(decl2);
                    }
    
                    // Prepare for variable substitution in the following code block
                    Substitution prevSubst = naming.addVariableSubst(varDecl, substVarName.toString());
    
                    stats = stats.appendList(transformElseClause(elseClause, tmpVar, outerExpression, expectedType));
                    JCBlock block = at(elseClause).Block(0, stats);
    
                    // Deactivate the above variable substitution
                    prevSubst.close();
    
                    return block;
                } else {
                    return transformElseClauseBlock(elseClause, tmpVar, outerExpression, expectedType);
                }
            } else {
                // To avoid possible javac warnings about uninitialized vars we
                // need to have an 'else' clause, even if the ceylon code doesn't
                // require one. 
                // This exception could be thrown for example if an enumerated 
                // type is recompiled after having a subclass added, but the 
                // switch is not recompiled.
                if(outerExpression != null){
                    // Actually this only works for statements. For expressions we're not allowed to throw,
                    // we get a verifier error at runtime otherwise: https://github.com/ceylon/ceylon-compiler/issues/2276
                    
                    List<JCStatement> stmts = List.<JCStatement>of(make().Exec(make().Assign(
                            makeUnquotedIdent(tmpVar), 
                            expressionGen().applyErasureAndBoxing(makeDefaultExprForType(expectedType),
                                    expectedType,
                                    !canUnbox(expectedType),
                                    CodegenUtil.getBoxingStrategy(outerExpression),
                                    expectedType))));
                    stmts = stmts.prepend(make().Exec(utilInvocation().rethrow(makeNewEnumeratedTypeError())));
                    return make().Block(0L, stmts);
                }else{
                    return makeThrowEnumeratedTypeError();
                }
            }
        }
        protected JCStatement makeThrowEnumeratedTypeError() {
            return make().Throw(makeNewEnumeratedTypeError());
        }
        protected JCExpression makeNewEnumeratedTypeError() {
            return make().NewClass(null, List.<JCExpression>nil(), 
                            makeIdent(syms().ceylonEnumeratedTypeErrorType), 
                            List.<JCExpression>of(make().Literal(
                                    "Supposedly exhaustive switch was not exhaustive")), null);
        }
        public abstract JCStatement transformSwitch(Node node, Tree.SwitchClause switchClause, Tree.SwitchCaseList caseList, 
                                                    String tmpVar, Tree.Term outerExpression, Type expectedType);
        
        protected boolean isDefinitelyReturns(CaseClause caseClause) {
            if(caseClause.getBlock() != null)
                return caseClause.getBlock().getDefinitelyReturns();
            // expressions don't return
            return false;
        }
    }
    /**
     * Switch transformation which produces a Java {@code switch},
     * suitable for a switch whose cases are all String literals,
     * or all Character literals.
     */
    class Switch extends SwitchTransformation {
        public Switch() {
        }
        @Override
        public JCStatement transformSwitch(Node node, Tree.SwitchClause switchClause, Tree.SwitchCaseList caseList, 
                String tmpVar, Tree.Term outerExpression, Type expectedType) {
            JCExpression switchExpr = expressionGen().transformExpression(
                    getSwitchExpression(switchClause), 
                    BoxingStrategy.UNBOXED, 
                    getSwitchExpressionType(switchClause));

            JCVariableDecl selector;
            if (hasVariable(switchClause)) {
                String name = switchClause.getSwitched().getVariable().getIdentifier().getText();
                selector = makeVar(name, makeJavaType(getSwitchExpressionType(switchClause)), switchExpr);
                JCStatement sw = transformSwitch(switchClause, caseList, tmpVar, outerExpression, expectedType, naming.makeQuotedIdent(name));
                return at(node).Block(0, List.of(selector, sw));
            } else {
                return transformSwitch(switchClause, caseList, tmpVar, outerExpression, expectedType, switchExpr);
            }
        }
        JCStatement transformSwitch(Tree.SwitchClause switchClause, Tree.SwitchCaseList caseList,
                String tmpVar, Tree.Term outerExpression, Type expectedType, 
                JCExpression switchExpr) {
            Name label = names().fromString("switch_" + gen().visitor.lv.getSwitchId(switchClause));
            ListBuffer<JCCase> cases = ListBuffer.<JCCase>lb();
            for (Tree.CaseClause caseClause : getCaseClauses(switchClause, caseList)) {
                if (getSingletonNullCase(caseClause) != null) {
                    continue;
                }
                Tree.MatchCase match = (Tree.MatchCase)caseClause.getCaseItem();
                
                java.util.List<Tree.Expression> exprs = match.getExpressionList().getExpressions();
                for (int ii = 0; ii < exprs.size()-1; ii++) {
                    Tree.Term term = ExpressionTransformer.eliminateParens(exprs.get(ii).getTerm());
                    at(term);
                    cases.add(make().Case(transformCaseExpr(term), 
                            List.<JCStatement>nil()));
                }
                Tree.Term term = exprs.get(exprs.size()-1).getTerm();
                JCBlock block = transformCaseClauseBlock(caseClause, tmpVar, outerExpression, expectedType);
                List<JCStatement> stmts = List.<JCStatement>nil();
                if (!isDefinitelyReturns(caseClause)) {
                    stmts = stmts.prepend(make().Break(label));
                }
                stmts = stmts.prepend(block);
                cases.add(make().Case(transformCaseExpr(term), 
                        stmts));
            }
            Naming.SyntheticName elseSelectorAlias = null;
            if(caseList.getElseClause() != null
                    && caseList.getElseClause().getVariable() != null){
                Value elseVar = caseList.getElseClause().getVariable().getDeclarationModel();
                if (hasVariable(switchClause)) {
                    Type switchVarType = switchClause.getSwitched().getVariable().getDeclarationModel().getType();
                    Type elseVarType = elseVar.getType();
                    if(!elseVarType.isExactly(switchVarType)){
                        elseSelectorAlias = naming.synthetic(elseVar);
                    }
                } else if (CodegenUtil.getBoxingStrategy(elseVar) != CodegenUtil.getBoxingStrategy(elseVar.getOriginalDeclaration())) {
                    elseSelectorAlias = naming.synthetic(elseVar);
                }
            }
            cases.add(make().Case(null, List.of(transformElse(elseSelectorAlias, caseList, tmpVar, outerExpression, expectedType, false))));
            
            
            JCStatement last = make().Switch(switchExpr, cases.toList());
            last = make().Labelled(label, last);
            return last;
        }
        private JCExpression transformCaseExpr(Tree.Term term) {
            if (term instanceof Tree.BaseMemberExpression
                    && ((Tree.BaseMemberExpression)term).getDeclaration() instanceof Value
                    && ((Value)((Tree.BaseMemberExpression)term).getDeclaration()).isEnumValue()) {
                // A case(enumValue) must use the unqualified name
                return naming.makeName((Value)((Tree.BaseMemberExpression)term).getDeclaration(), Naming.NA_MEMBER);
            }
            return expressionGen().transformExpression(term, 
                    BoxingStrategy.UNBOXED, term.getTypeModel());
        }
    }
    Tree.Term getSingletonNullCase(Tree.CaseClause caseClause) {
        Tree.CaseItem caseItem = caseClause.getCaseItem();
        if (caseItem instanceof Tree.MatchCase) {
            java.util.List<Expression> expressions = ((Tree.MatchCase)caseItem).getExpressionList().getExpressions();
            for (Tree.Expression expr : expressions) {
                Tree.Term term = ExpressionTransformer.eliminateParens(expr.getTerm());
                if (term instanceof Tree.BaseMemberExpression
                        && isNullValue(((Tree.BaseMemberExpression)term).getDeclaration())
                        && expressions.size() == 1) {
                    return term;
                }
            }
        }
        return null;
    }
    
    /**
     * Switch transformation which produces a Java 
     * <code>if (selector == null) {...} else { switch() {...} }</code>,
     * suitable for a switch whose cases include a singleton case for null
     * (i.e. <code>case (null) {}</code>, but not <code>case("foo", null) {}</code>) 
     * with the remaining cases are all String literals
     * or all Character literals.
     */
    class IfNullElseSwitch extends SwitchTransformation {
        
        @Override
        public JCStatement transformSwitch(Node node, Tree.SwitchClause switchClause, Tree.SwitchCaseList caseList,
                String tmpVar, Tree.Term outerExpression, Type expectedType) {
            Type switchExpressionType = getSwitchExpressionType(switchClause);
            Type switchDefiniteExpressionType = getDefiniteSwitchExpressionType(switchClause);
            JCExpression selectorExpr = expressionGen().transformExpression(getSwitchExpression(switchClause), BoxingStrategy.BOXED, getSwitchExpressionType(switchClause));
            JCVariableDecl selector;
            JCIdent ident;
            if (hasVariable(switchClause)) {
                String name = switchClause.getSwitched().getVariable().getIdentifier().getText();
                selector = makeVar(name, makeJavaType(switchExpressionType), selectorExpr);
                ident = naming.makeQuotedIdent(name);
            } else {
                Naming.SyntheticName selectorAlias = naming.alias("sel");
                selector = makeVar(selectorAlias, makeJavaType(switchExpressionType), selectorExpr);
                ident = selectorAlias.makeIdent();
            }
            // Make a switch out of the non-null cases
            JCStatement switch_ = new Switch().transformSwitch(switchClause, caseList, tmpVar, outerExpression, expectedType,
                    expressionGen().applyErasureAndBoxing(ident, 
                            switchDefiniteExpressionType,
                            true,
                            BoxingStrategy.UNBOXED,
                            switchDefiniteExpressionType));
            // Now wrap it with a null test
            JCIf ifElse = null;
            for (Tree.CaseClause caseClause : getCaseClauses(switchClause, caseList)) {
                Tree.Term term = getSingletonNullCase(caseClause);
                if (term != null) {
                    ifElse  = make().If(make().Binary(JCTree.EQ, ident, makeNull()),
                            transformCaseClauseBlock(caseClause, tmpVar, outerExpression, expectedType), 
                            make().Block(0, List.<JCStatement>of(switch_)));
                    break;
                }
            }
            return at(node).Block(0, List.of(selector, ifElse));
        }
        
    }
    /**
     * The default switch transformation which produces a 
     * {@code if/else if} chain.
     */
    class IfElseChain extends SwitchTransformation {

        @Override
        protected java.util.List<Tree.CaseClause> getCaseClauses(Tree.SwitchClause switchClause, Tree.SwitchCaseList caseList) {
            // If all the cases are "case (is ...)" we can try to avoid 
            // the expense of testing reified is by putting all the cheap tests first
            // We respect the relative order of all the cheap cases and all 
            // the expensive cases though on the basis that that might be a 
            // hint
            // about which are more common
            java.util.List<CaseClause> list = super.getCaseClauses(switchClause, caseList);
            java.util.ArrayList<CaseClause> cheap = new ArrayList<CaseClause>(list.size());
            int lastCheap = 0;
            // The dummy isn't actually used for anything, it just has to be non-null
            SyntheticName dummy = naming.synthetic(Naming.Unfix.$annotationSequence$);
            // if one of the match expressions is null that 
            // test must be first to avoid NPE.
            Tree.CaseClause containsNull = null;
            outer: for (Tree.CaseClause clause : list) {
                Tree.CaseItem item = clause.getCaseItem();
                boolean isCheap;
                if (item instanceof Tree.IsCase) {
                    isCheap = isTypeTestCheap(null, dummy, 
                            ((Tree.IsCase) item).getType().getTypeModel(), 
                            getSwitchExpressionType(switchClause));
                } else if (item instanceof Tree.MatchCase) {
                    // will be primitive equality test
                    for (Tree.Expression expr : ((Tree.MatchCase) item).getExpressionList().getExpressions()) {
                        if (isNull(expr.getTypeModel())) {
                            containsNull = clause;
                            continue outer;
                        }
                    }
                    isCheap = true;
                } else {
                    // should never get here, but we can just return the unsorted list
                    return list;
                }
                int index = isCheap ? lastCheap : cheap.size();
                cheap.add(index, clause);
                if (isCheap) {
                    lastCheap = index+1;
                }
            }
            if (containsNull != null) {
                cheap.add(0, containsNull);
            }
            return cheap;
        }
        
        @Override
        public JCStatement transformSwitch(Node node, Tree.SwitchClause switchClause, Tree.SwitchCaseList caseList, 
                                           String tmpVar, Tree.Term outerExpression, Type expectedType) {
            Naming.SyntheticName selectorAlias = naming.alias("sel");
            JCStatement last;
            final BoxingStrategy bs;
            final JCExpression selectorType;
            Type switchExpressionType = getSwitchExpressionType(switchClause);
            Boolean switchUnboxed = getSwitchExpressionUnboxed(switchClause);
            boolean allMatches = isSwitchAllMatchCases(caseList);
            boolean primitiveSelector = allMatches && isCeylonBasicType(switchExpressionType) && BooleanUtil.isNotFalse(switchUnboxed);
            if (primitiveSelector) {
                bs = BoxingStrategy.UNBOXED;
                selectorType = makeJavaType(switchExpressionType);
            } else {
                bs = BoxingStrategy.BOXED;
                selectorType = makeJavaType(switchExpressionType, JT_NO_PRIMITIVES|JT_RAW);
            }
            last = transformElse(selectorAlias, caseList, tmpVar, outerExpression, expectedType, primitiveSelector);
            JCExpression selectorExpr = expressionGen().transformExpression(getSwitchExpression(switchClause), bs, switchExpressionType);
            
            JCVariableDecl selector = makeVar(selectorAlias, selectorType, selectorExpr);
            
            java.util.List<Tree.CaseClause> caseClauses = getCaseClauses(switchClause, caseList);
            for (int ii = caseClauses.size() - 1; ii >= 0; ii--) {// reverse order
                Tree.CaseClause caseClause = caseClauses.get(ii);
                Tree.CaseItem caseItem = caseClause.getCaseItem();
                if (caseItem instanceof Tree.IsCase) {
                    last = transformCaseIs(selectorAlias, caseClause, tmpVar, outerExpression, expectedType, (Tree.IsCase)caseItem, last, switchExpressionType);
                } else if (caseItem instanceof Tree.SatisfiesCase) {
                    // TODO Support for 'case (satisfies ...)' is not implemented yet
                    return make().Exec(makeErroneous(caseItem, "compiler bug: switch/satisfies not implemented yet"));
                } else if (caseItem instanceof Tree.MatchCase) {
                    last = transformCaseMatch(selectorAlias, switchClause, caseClause, tmpVar,outerExpression, expectedType, (Tree.MatchCase)caseItem, last, switchExpressionType, primitiveSelector);
                } else {
                    return make().Exec(makeErroneous(caseItem, "compiler bug: unknown switch case clause: "+caseItem));
                }
            }
            return at(node).Block(0, List.of(selector, last));
        }
        
    }
    
    private boolean isJavaSwitchableType(Type type, Boolean switchUnboxed) {
        return BooleanUtil.isNotFalse(switchUnboxed) && (type.isExactly(typeFact().getCharacterType())
                    || type.isExactly(typeFact().getStringType()))
                || isJavaEnumType(type);
    }
    
    /**
     * Transforms a Ceylon switch to a Java {@code if/else if} chain.
     * @param stmt The Ceylon switch
     * @return The Java tree
     */
    JCStatement transform(Tree.SwitchStatement stmt) {
        return transform(stmt, stmt.getSwitchClause(), stmt.getSwitchCaseList(), null, null, null);
    }

    JCStatement transform(Node node, Tree.SwitchClause switchClause, Tree.SwitchCaseList caseList, String tmpVar, 
            Tree.Term outerExpression, Type expectedType) {
        at(switchClause);
        block();
        SwitchTransformation transformation = null;
        Type exprType = switchExpressionType(switchClause);
        Boolean switchUnboxed = switchExpressionUnboxed(switchClause);
        // Are we switching with just String literal or Character literal match cases? 
        if (isJavaSwitchableType(exprType, switchUnboxed)) {
            boolean canUseSwitch = true;
            caseStmts: for (Tree.CaseClause clause : caseList.getCaseClauses()) {
                if (clause.getCaseItem() instanceof Tree.MatchCase) {
                    java.util.List<Expression> caseExprs = ((Tree.MatchCase)clause.getCaseItem()).getExpressionList().getExpressions();
                    caseExpr: for (Tree.Expression expr : caseExprs) {
                        Tree.Term e = ExpressionTransformer.eliminateParens(expr);
                        if (e instanceof Tree.StringLiteral
                                || e instanceof Tree.CharLiteral) {
                            continue caseExpr;
                        } else if (e instanceof Tree.BaseMemberExpression
                                && ((Tree.BaseMemberExpression)e).getDeclaration() instanceof Value
                                && ((Value)((Tree.BaseMemberExpression)e).getDeclaration()).isEnumValue()) {
                            continue caseExpr;
                        } else {
                            canUseSwitch = false;
                            break caseStmts;
                        }
                    }
                } else {
                    canUseSwitch = false;
                    break caseStmts;
                }
            }
            if (canUseSwitch) {
                // yes, so use a Java Switch
                transformation = new Switch();
            }
        }
        if (transformation == null
                && isOptional(exprType)) {
            // Are we switching with just String literal or Character literal plus null 
            // match cases?
            Type definiteType = typeFact().getDefiniteType(exprType);
            if (isJavaSwitchableType(definiteType, switchUnboxed)) {
                boolean canUseIfElseSwitch = true;
                boolean hasSingletonNullCase = false;
                caseStmts: for (Tree.CaseClause clause : caseList.getCaseClauses()) {
                    if (clause.getCaseItem() instanceof Tree.MatchCase) {
                        if (getSingletonNullCase(clause) != null) {
                            hasSingletonNullCase = true;
                        }
                        java.util.List<Expression> caseExprs = ((Tree.MatchCase)clause.getCaseItem()).getExpressionList().getExpressions();
                        caseExpr: for (Tree.Expression expr : caseExprs) {
                            Tree.Term e = ExpressionTransformer.eliminateParens(expr);
                            if (e instanceof Tree.StringLiteral
                                    || e instanceof Tree.CharLiteral) {
                                continue caseExpr;
                            } else if (e instanceof Tree.BaseMemberExpression
                                    && isNullValue(((Tree.BaseMemberExpression)e).getDeclaration())
                                    && caseExprs.size() == 1) {
                                continue caseExpr;
                            } else if (e instanceof Tree.BaseMemberExpression
                                    && ((Tree.BaseMemberExpression)e).getDeclaration() instanceof Value
                                    && ((Value)((Tree.BaseMemberExpression)e).getDeclaration()).isEnumValue()) {
                                continue caseExpr;
                            } else {
                                canUseIfElseSwitch = false;
                                break caseStmts;
                            }
                        }
                    } else {
                        canUseIfElseSwitch = false;
                        break caseStmts;
                    }
                }
                canUseIfElseSwitch &= hasSingletonNullCase;
                if (canUseIfElseSwitch) {
                 // yes, so use a If
                    transformation = new IfNullElseSwitch();
                }
            }
        }
        // The default transformation
        if (transformation == null) {
            transformation = new IfElseChain();
        }
        JCStatement result = transformation.transformSwitch(node, switchClause, caseList, tmpVar, outerExpression, expectedType);
        unblock();
        return result;
    }

    private boolean isSwitchAllMatchCases(Tree.SwitchCaseList caseList) {
        for (Tree.CaseClause caseClause : caseList.getCaseClauses()) {
            if (!(caseClause.getCaseItem() instanceof Tree.MatchCase)) {
                return false;
            }
        }
        return true;
    }

    private JCStatement transformCaseMatch(Naming.SyntheticName selectorAlias,
            Tree.SwitchClause switchClause, Tree.CaseClause caseClause, String tmpVar,
            Tree.Term outerExpression, Type expectedType, Tree.MatchCase matchCase,
            JCStatement last, Type switchType, boolean primitiveSelector) {
        at(matchCase);
        
        JCExpression tests = null;
        java.util.List<Tree.Expression> expressions = matchCase.getExpressionList().getExpressions();
        for(Tree.Expression expr : expressions){
            Tree.Term term = ExpressionTransformer.eliminateParens(expr.getTerm());
            boolean unboxedEquality = primitiveSelector || isCeylonBasicType(typeFact().getDefiniteType(switchType));
            JCExpression transformedExpression = expressionGen().transformExpression(term, 
                    unboxedEquality ? BoxingStrategy.UNBOXED: BoxingStrategy.BOXED, 
                    term.getTypeModel());
            JCExpression test;
            if (term instanceof Tree.Literal || term instanceof Tree.NegativeOp) {
                if (unboxedEquality) {
                    if (term instanceof Tree.StringLiteral) {
                        test = make().Apply(null, 
                                makeSelect(unboxType(selectorAlias.makeIdent(), term.getTypeModel()), "equals"), List.<JCExpression>of(transformedExpression));
                    } else {
                        test = make().Binary(JCTree.EQ, 
                                primitiveSelector ? selectorAlias.makeIdent() : unboxType(selectorAlias.makeIdent(), term.getTypeModel()), 
                                transformedExpression);
                    }
                } else {
                    test = make().Apply(null, makeSelect(selectorAlias.makeIdent(), "equals"), List.<JCExpression>of(transformedExpression));
                }
                if (isOptional(switchType)) {
                    test = make().Binary(JCTree.AND, make().Binary(JCTree.NE, selectorAlias.makeIdent(), makeNull()), test);
                }
            } else {
                JCExpression selectorExpr;
                if (!primitiveSelector && isCeylonBasicType(typeFact().getDefiniteType(switchType))) {
                    selectorExpr = unboxType(selectorAlias.makeIdent(), term.getTypeModel());
                } else {
                    selectorExpr = selectorAlias.makeIdent();
                }
                test = make().Binary(JCTree.EQ, selectorExpr, transformedExpression);
            }
            if(tests == null)
                tests = test;
            else if (isNull(term.getTypeModel())) {
                // ensure we do any null check as the first operation in the ||-ed expression
                tests = make().Binary(JCTree.OR, test, tests);
            } else {
                tests = make().Binary(JCTree.OR, tests, test);
            }
        }
        
        Substitution prevSubst = null;
        if (switchClause.getSwitched().getVariable() != null) {
            // Prepare for variable substitution in the following code block
            prevSubst = naming.addVariableSubst(switchClause.getSwitched().getVariable().getDeclarationModel(), selectorAlias.toString());
        }
        
        JCBlock block = transformCaseClauseBlock(caseClause, tmpVar, outerExpression, expectedType);
        
        if (prevSubst != null) {
            // Deactivate the above variable substitution
            prevSubst.close();
        }
        
        return at(caseClause).If(tests, block, last);
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
            Tree.CaseClause caseClause, String tmpVar, Tree.Term outerExpression, Type expectedType, 
            Tree.IsCase isCase, 
            JCStatement last, Type expressionType) {
        at(isCase);
        // Use the type of the variable, which is more precise than the type we test for.
        Type varType = isCase.getVariable().getDeclarationModel().getType();
        Type caseType = isCase.getType().getTypeModel();
        // note: There's no point using makeOptimizedTypeTest() because cases are disjoint
        // anyway and the cheap cases get evaluated first.
        JCExpression cond = makeTypeTest(null, selectorAlias, caseType , expressionType);
        
        String name = isCase.getVariable().getIdentifier().getText();
        TypedDeclaration varDecl = isCase.getVariable().getDeclarationModel();

        Naming.SyntheticName tmpVarName = selectorAlias;
        Name substVarName = naming.aliasName(name);

        // Want raw type for instanceof since it can't be used with generic types
        JCExpression rawToTypeExpr = makeJavaType(varType, JT_NO_PRIMITIVES | JT_RAW);

        // Substitute variable with the correct type to use in the rest of the code block
        
        JCExpression tmpVarExpr = at(isCase).TypeCast(rawToTypeExpr, tmpVarName.makeIdent());
        JCExpression toTypeExpr;
        if (isCeylonBasicType(varType) && varDecl.getUnboxed() == true) {
            toTypeExpr = makeJavaType(varType);
            tmpVarExpr = unboxType(tmpVarExpr, varType);
        } else {
            toTypeExpr = makeJavaType(varType, JT_NO_PRIMITIVES);
        }
        
        // The variable holding the result for the code inside the code block
        JCVariableDecl decl2 = at(isCase).VarDef(make().Modifiers(FINAL), substVarName, toTypeExpr, tmpVarExpr);

        // Prepare for variable substitution in the following code block
        Substitution prevSubst = naming.addVariableSubst(varDecl , substVarName.toString());

        List<JCStatement> stats = List.<JCStatement> of(decl2);
        stats = stats.appendList(transformCaseClause(caseClause, tmpVar, outerExpression, expectedType));
        JCBlock block = at(isCase).Block(0, stats);

        // Deactivate the above variable substitution
        prevSubst.close();

        last = make().If(cond, block, last);
        return last;
    }
    
    private Name getLabel(Tree.Directive dir) {
        Scope scope = dir.getScope();
        while (!(scope instanceof Package)) {
            if (scope instanceof ControlBlock) {
                Integer loopId = gen().visitor.lv.getLoopId((ControlBlock)scope);
                if (loopId != null) {
                    return names().fromString("loop_"+loopId);
                }
            }
            scope = scope.getContainer();
        }
        throw new BugException(dir, "failed to find label");
    }
    
    public Name getLabel(Tree.Break brk) {
        return getLabel((Tree.Directive)brk);
    }
    
    public Name getLabel(Tree.Continue cont) {
        return getLabel((Tree.Directive)cont);
    }
    
    public Name getLabel(Tree.WhileClause loop) {
        return getLabel(loop.getControlBlock());
    }
    
    private Name getLabel(ControlBlock block) {
        Integer i = gen().visitor.lv.getLoopId(block);
        return names().fromString("loop_"+i);
    }
    
    public Name getLabel(Tree.ForClause loop) {
        return getLabel(loop.getControlBlock());
    }
    
    public List<JCVariableDecl> transformVariableOrDestructure(Tree.Statement varOrDes) {
        List<JCVariableDecl> vars = List.<JCVariableDecl>nil();
        if (varOrDes instanceof Tree.Variable) {
            Tree.Variable var = (Tree.Variable)varOrDes;
            Expression expr = var.getSpecifierExpression().getExpression();
            BoxingStrategy boxingStrategy = CodegenUtil.getBoxingStrategy(var.getDeclarationModel());
            JCExpression init = expressionGen().transformExpression(expr, boxingStrategy, var.getType().getTypeModel());
            vars = vars.append(transformVariable(var, init, expr.getTypeModel(), boxingStrategy == BoxingStrategy.BOXED).build());
        } else if (varOrDes instanceof Tree.Destructure) {
            Tree.Destructure des = (Tree.Destructure)varOrDes;
            vars = vars.appendList(transform(des));
        } else {
            throw BugException.unhandledCase(varOrDes);
        }
        return vars;
    }

    /**
     * Transforms a Ceylon destructuring assignment to Java code.
     * @param stmt The Ceylon destructure
     * @return The Java tree
     */
    List<JCVariableDecl> transform(Tree.Destructure stmt) {
        List<JCVariableDecl> result = List.nil();
        
        // Create temp var to hold result of expression
        Tree.Pattern pat = stmt.getPattern();
        Naming.SyntheticName tmpVarName = naming.synthetic(pat);
        Expression destExpr = stmt.getSpecifierExpression().getExpression();
        JCExpression typeExpr = makeJavaType(destExpr.getTypeModel());
        JCExpression expr = expressionGen().transformExpression(destExpr);
        at(stmt);
        JCVariableDecl tmpVar = makeVar(Flags.FINAL, tmpVarName, typeExpr, expr);
        result = result.append(tmpVar);
        
        // Now add the destructured variables
        List<JCVariableDecl> vars = VarDefBuilder.buildAll(transformPattern(pat, tmpVarName.makeIdent()));
        result = result.appendList(vars);
        
        return result;
    }

    static class VarDefBuilder {
        private final ExpressionTransformer gen;
        private final Variable var;
        private final JCExpression initExpr;
        private SyntheticName name;
        private boolean built;
        
        public VarDefBuilder(ExpressionTransformer gen, Variable var, JCExpression initExpr) {
            this.gen = gen;
            this.var = var;
            this.initExpr = initExpr;
        }
        
        private Type model() {
            return var.getType().getTypeModel();
        }
        
        private SyntheticName name() {
            if (name == null) {
                name = gen.naming.substituted(var.getDeclarationModel()).capture();
            }
            return name;
        }
        
        private JCExpression type() {
            BoxingStrategy boxingStrategy = CodegenUtil.getBoxingStrategy(var.getDeclarationModel());
            return gen.makeJavaType(model(), (boxingStrategy == BoxingStrategy.BOXED) ? JT_NO_PRIMITIVES : 0);
        }
        
        private JCExpression expr() {
            if (built) {
                throw new BugException(var, "Variable expression can only be used once");
            }
            return initExpr;
        }
        
        Substitution alias() {
            return gen.naming.substituteAlias(var.getDeclarationModel());
        }
        
        JCVariableDecl buildInternal() {
            gen.at(var);
            JCVariableDecl def = gen.makeVar(Flags.FINAL, name(), type(), expr());
            return def;
        }
        
        JCVariableDecl build() {
            JCVariableDecl def = buildInternal();
            built = true;
            return def;
        }
        
        JCVariableDecl buildDefOnly() {
            gen.at(var);
            JCVariableDecl def = gen.makeVar(Flags.FINAL, name(), type(), null);
            return def;
        }
        
        JCVariableDecl buildField() {
            gen.at(var);
            JCVariableDecl def = gen.makeVar(Flags.PRIVATE, name(), type(), null);
            return def;
        }
        
        JCVariableDecl buildFromField() {
            gen.at(var);
            JCVariableDecl def = gen.makeVar(Flags.FINAL, name(), type(), name().makeIdentWithThis());
            return def;
        }
        
        JCAssign buildAssign() {
            gen.at(var);
            JCAssign def = gen.make().Assign(name().makeIdent(), expr());
            built = true;
            return def;
        }
        
        JCAssign buildDefaultAssign() {
            gen.at(var);
            JCAssign def = gen.make().Assign(name().makeIdent(), gen.makeDefaultExprForType(model()));
            return def;
        }
        
        static List<JCVariableDecl> buildAll(List<VarDefBuilder> vars) {
            List<JCVariableDecl> result = List.nil();
            for (VarDefBuilder vdb : vars) {
                result = result.append(vdb.build());
            }
            return result;
        }

        @Override
        public String toString() {
            return "VarDefBuilder [build()=" + buildInternal() + "]";
        }
    }
    
    List<VarDefBuilder> transformDestructure(Tree.Statement stmt, JCExpression varAccessExpr, Type exprType, boolean exprBoxed) {
        List<VarDefBuilder> result = List.nil();
        if (stmt instanceof Tree.Variable) {
            result = result.append(transformVariable((Tree.Variable)stmt, varAccessExpr, exprType, exprBoxed));
        } else if (stmt instanceof Tree.Destructure) {
            result = result.appendList(transformPattern(((Tree.Destructure)stmt).getPattern(), varAccessExpr));
        } else {
            throw BugException.unhandledCase(stmt);
        }
        return result;
    }
    
    List<VarDefBuilder> transformPattern(Tree.Pattern pat, JCExpression varAccessExpr) {
        List<VarDefBuilder> result = List.nil();
        
        if (pat instanceof Tree.TuplePattern) {
            // For a Tuple we get the value of each of its items and assign it to a local value
            int idx = 0;
            Tree.TuplePattern tuple = (Tree.TuplePattern)pat;
            for (Tree.Pattern p : tuple.getPatterns()) {
                JCExpression idxExpr = makeInteger(idx++);
                Type ot = typeFact().getObjectType();
                JCExpression fullGetExpr;
                if (varAccessExpr != null) {
                    JCExpression seqVarAccessExpr = make().TypeCast(makeJavaType(typeFact().getSequenceType(ot), JT_RAW), varAccessExpr);
                    JCExpression tupleAccessExpr;
                    if (isVariadicVariable(p)) {
                        tupleAccessExpr = makeQualIdent(seqVarAccessExpr, "skip");
                    } else {
                        tupleAccessExpr = makeQualIdent(seqVarAccessExpr, "getFromFirst");
                    }
                    fullGetExpr = make().Apply(null, tupleAccessExpr, List.of(idxExpr));
                    if (isVariadicVariable(p)) {
                        fullGetExpr = make().Apply(null, makeQualIdent(fullGetExpr, "sequence"), List.<JCExpression>nil());
                    }
                } else {
                    fullGetExpr = null;
                }
                result = result.appendList(transformPattern(p, fullGetExpr));
            }
        } else if (pat instanceof Tree.KeyValuePattern) {
            // For an Entry we create two local values, one for the key and one for the value
            Tree.KeyValuePattern entry = (Tree.KeyValuePattern)pat;
            Type ot = typeFact().getObjectType();
            JCExpression getItemExpr;
            if (varAccessExpr != null) {
                JCExpression entryVarAccessExpr = make().TypeCast(makeJavaType(typeFact().getEntryType(ot , ot), JT_RAW), varAccessExpr);
                JCExpression getKeyExpr = make().Apply(null, makeQualIdent(entryVarAccessExpr, "getKey"), List.<JCExpression>nil());
                result = result.appendList(transformPattern(entry.getKey(), getKeyExpr));
                getItemExpr = make().Apply(null, makeQualIdent(entryVarAccessExpr, "getItem"), List.<JCExpression>nil());
            } else {
                getItemExpr = null;
            }
            result = result.appendList(transformPattern(entry.getValue(), getItemExpr));
        } else if (pat instanceof Tree.VariablePattern) {
            Tree.VariablePattern var = (Tree.VariablePattern)pat;
            result = result.append(transformVariable(var.getVariable(), varAccessExpr));
        } else {
            throw BugException.unhandledCase(pat);
        }
        
        return result;
    }

    private boolean isVariadicVariable(Tree.Pattern pat) {
        if (pat instanceof Tree.VariablePattern) {
            Tree.VariablePattern var = (Tree.VariablePattern)pat;
            return var.getVariable().getType() instanceof Tree.SequencedType;
        }
        return false;
    }

    VarDefBuilder transformVariable(Variable var, JCExpression initExpr) {
        return transformVariable(var, initExpr, typeFact().getObjectType(), true);
    }

    VarDefBuilder transformVariable(Variable var, JCExpression initExpr, Type exprType, boolean exprBoxed) {
        BoxingStrategy boxingStrategy = CodegenUtil.getBoxingStrategy(var.getDeclarationModel());
        JCExpression expr = initExpr;
        if (expr != null) {
            Type type;
            if (var.getType().getTypeModel().getDeclaration().isAnonymous()) {
                type = var.getType().getTypeModel();
            } else {
                type = simplifyType(typeFact().denotableType(var.getType().getTypeModel()));
            }
            expr = expressionGen().applyErasureAndBoxing(
                    expr, exprType, false, exprBoxed,
                    boxingStrategy, type,
                    ExpressionTransformer.EXPR_DOWN_CAST);
        }
        return new VarDefBuilder(expressionGen(), var, expr);
    }
    
    Expression getDestructureExpression(Tree.Statement varOrDes) {
        Tree.SpecifierExpression specExpr;
        if (varOrDes instanceof Tree.Variable) {
            specExpr = ((Tree.Variable)varOrDes).getSpecifierExpression();
        } else if (varOrDes instanceof Tree.Destructure) {
            specExpr = ((Tree.Destructure)varOrDes).getSpecifierExpression();
        } else {
            throw BugException.unhandledCase(varOrDes);
        }
        return (specExpr != null) ? specExpr.getExpression() : null;
    }
}
