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

import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CaseClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CaseItem;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CatchClause;
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
        return block == null ? null : at(block).Block(0, transformStmts(block.getStatements()));
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

    List<JCStatement> transform(Tree.IfStatement stmt) {
        Tree.Block thenPart = stmt.getIfClause().getBlock();
        Tree.Block elsePart = stmt.getElseClause() != null ? stmt.getElseClause().getBlock() : null;
        return transformCondition(stmt.getIfClause().getCondition(), JCTree.IF, thenPart, elsePart);
    }

    List<JCStatement> transform(Tree.WhileStatement stmt) {
        Name tempForFailVariable = currentForFailVariable;
        currentForFailVariable = null;
        
        Tree.Block thenPart = stmt.getWhileClause().getBlock();
        List<JCStatement> res = transformCondition(stmt.getWhileClause().getCondition(), JCTree.WHILELOOP, thenPart, null);
        
        currentForFailVariable = tempForFailVariable;
        
        return res;
    }

    /** Transforms just the specified condition, optionally transforming a block if specified.
     * @param cond The condition to transform
     * @param thenPart An optional block (usually the one to execute if the condition is true)
     * @return A list with up to 3 elements: one element means it's just the condition; two elements
     * means it's the condition and a block; three elements means it's
     * a two declarations and the condition, or a declaration, the condition and a block. */
    private List<JCTree> transformCondition(Tree.Condition cond, Tree.Block thenPart) {
        JCExpression test;
        JCVariableDecl decl = null;
        JCVariableDecl decl2 = null;
        JCBlock thenBlock = null;
        if ((cond instanceof Tree.IsCondition) || (cond instanceof Tree.NonemptyCondition) || (cond instanceof Tree.ExistsCondition)) {
            String name;
            ProducedType toType;
            Expression specifierExpr;
            boolean omit = false;
            if (cond instanceof Tree.IsCondition) {
                Tree.IsCondition isdecl = (Tree.IsCondition) cond;
                name = isdecl.getVariable().getIdentifier().getText();
                // use the type of the variable, which is more precise than the type we test for
                toType = isdecl.getVariable().getType().getTypeModel();
                specifierExpr = isdecl.getVariable().getSpecifierExpression().getExpression();
                omit = isdecl.getVariable().getType() instanceof Tree.SyntheticVariable;
            } else if (cond instanceof Tree.NonemptyCondition) {
                Tree.NonemptyCondition nonempty = (Tree.NonemptyCondition) cond;
                name = nonempty.getVariable().getIdentifier().getText();
                toType = nonempty.getVariable().getType().getTypeModel();
                specifierExpr = nonempty.getVariable().getSpecifierExpression().getExpression();
                omit = nonempty.getVariable().getType() instanceof Tree.SyntheticVariable;
            } else {
                Tree.ExistsCondition exists = (Tree.ExistsCondition) cond;
                name = exists.getVariable().getIdentifier().getText();
                toType = simplifyType(exists.getVariable().getType().getTypeModel());
                specifierExpr = exists.getVariable().getSpecifierExpression().getExpression();
                omit = exists.getVariable().getType() instanceof Tree.SyntheticVariable;
            }
            
            // no need to cast for erasure here
            JCExpression expr = expressionGen().transformExpression(specifierExpr);
            
            // IsCondition with Nothing as ProducedType transformed to " == null" 
            at(cond);
            if (cond instanceof Tree.IsCondition && isNothing(toType)) {
                test = make().Binary(JCTree.EQ, expr, makeNull());
            } else {
                JCExpression toTypeExpr = makeJavaType(toType);
    
                Naming.SyntheticName tmpVarName = naming.alias(name);
    
                ProducedType tmpVarType = specifierExpr.getTypeModel();
                JCExpression tmpVarTypeExpr;
                // Want raw type for instanceof since it can't be used with generic types
                JCExpression rawToTypeExpr = makeJavaType(toType, JT_NO_PRIMITIVES | JT_RAW);

                // Substitute variable with the correct type to use in the rest of the code block
                JCExpression tmpVarExpr = tmpVarName.makeIdent();
                if (cond instanceof Tree.ExistsCondition) {
                    tmpVarExpr = unboxType(tmpVarExpr, toType);
                    tmpVarTypeExpr = makeJavaType(tmpVarType, JT_NO_PRIMITIVES);
                } else if(cond instanceof Tree.IsCondition){
                    JCExpression castedExpr = at(cond).TypeCast(rawToTypeExpr, tmpVarExpr);
                    if(canUnbox(toType))
                        tmpVarExpr = unboxType(castedExpr, toType);
                    else
                        tmpVarExpr = castedExpr;
                    tmpVarTypeExpr = make().Type(syms().objectType);
                } else {
                    tmpVarExpr = at(cond).TypeCast(toTypeExpr, tmpVarExpr);
                    tmpVarTypeExpr = makeJavaType(tmpVarType, JT_NO_PRIMITIVES);
                }
                
                // Temporary variable holding the result of the expression/variable to test
                decl = makeVar(tmpVarName, tmpVarTypeExpr, null);

                if (thenPart == null) {
                    // The variable holding the result for the code inside the code block
                    if (!omit) {
                        decl2 = at(cond).VarDef(make().Modifiers(FINAL), names().fromString(name), toTypeExpr, tmpVarExpr);
                    }
                } else {
                    Name substVarName = naming.aliasName(name);
                    // The variable holding the result for the code inside the code block
                    decl2 = at(cond).VarDef(make().Modifiers(FINAL), substVarName, toTypeExpr, tmpVarExpr);
                    // Prepare for variable substitution in the following code block
                    final String origVarName = name;
                    String prevSubst = naming.addVariableSubst(origVarName, substVarName.toString());
                    thenBlock = transform(thenPart);
                    List<JCStatement> stats = List.<JCStatement> of(decl2);
                    stats = stats.appendList(thenBlock.getStatements());
                    thenBlock = at(cond).Block(0, stats);
                    // Deactivate the above variable substitution
                    naming.removeVariableSubst(origVarName, prevSubst);
                    decl2 = null;
                }
                
                at(cond);
                // Assign the expression to test to the temporary variable
                JCExpression firstTimeTestExpr = make().Assign(tmpVarName.makeIdent(), expr);
                
                // Test on the tmpVar in the following condition
                if (cond instanceof Tree.ExistsCondition) {
                    test = make().Binary(JCTree.NE, firstTimeTestExpr, makeNull());                
                } else if (cond instanceof Tree.NonemptyCondition){
                    test = makeNonEmptyTest(firstTimeTestExpr, tmpVarName);
                } else {
                    // is
                    test = makeTypeTest(firstTimeTestExpr, tmpVarName,
                            // only test the types we're testing for, not the type of
                            // the variable (which can be more precise)
                            ((Tree.IsCondition)cond).getType().getTypeModel());
                }
            }
        } else if (cond instanceof Tree.BooleanCondition) {
            Tree.BooleanCondition booleanCondition = (Tree.BooleanCondition) cond;
            // booleans can't be erased
            test = expressionGen().transformExpression(booleanCondition.getExpression(), 
                    BoxingStrategy.UNBOXED, null);
        } else {
            throw new RuntimeException("Not implemented: " + cond.getNodeType());
        }
        
        at(cond);

        ListBuffer<JCTree> parts = new ListBuffer<JCTree>();
        if (decl != null) {
            parts.add(decl);
        }
        if (decl2 != null) {
            parts.add(decl2);
        }
        parts.add(test);
        if (thenBlock != null) {
            parts.add(thenBlock);
        }
        return parts.toList();
    }

    private List<JCStatement> transformCondition(Tree.Condition cond, int tag, Tree.Block thenPart, Tree.Block elsePart) {
        JCVariableDecl decl = null;
        JCBlock thenBlock = null;
        JCBlock elseBlock = null;

        final List<JCTree> parts = transformCondition(cond, thenPart);
        final JCExpression test;
        if (parts.size() == 1) {
            test = (JCExpression)parts.get(0);
        } else if (parts.size() == 3) {
            decl = (JCVariableDecl)parts.get(0);
            if (thenPart == null) {
                test = (JCExpression)parts.get(2);
            } else {
                test = (JCExpression)parts.get(1);
                thenBlock = (JCBlock)parts.get(2);
            }
        } else {
            if (parts.get(0) instanceof JCVariableDecl) {
                decl = (JCVariableDecl)parts.get(0);
                test = (JCExpression)parts.get(1);
            } else {
                test = (JCExpression)parts.get(0);
                thenBlock = (JCBlock)parts.get(1);
            }
        }
        // Convert the code blocks (if not already done so above)
        if (thenPart != null && thenBlock == null) {
            thenBlock = transform(thenPart);
        }
        if (elsePart != null && elseBlock == null) {
            elseBlock = transform(elsePart);
        }

        JCStatement cond1;
        switch (tag) {
        case JCTree.IF:
            cond1 = make().If(test, thenBlock, elseBlock);
            break;
        case JCTree.WHILELOOP:
            cond1 = make().WhileLoop(makeLetExpr(make().TypeIdent(TypeTags.BOOLEAN), test), thenBlock);
            break;
        default:
            throw new RuntimeException();
        }

        if (decl != null) {
            return List.<JCStatement> of(decl, cond1);
        } else {
            return List.<JCStatement> of(cond1);
        }
    }
    
    public List<JCStatement> transform(Tree.Assertion ass) {
        final List<JCTree> parts = transformCondition(ass.getCondition(), null);
        final JCExpression test;
        ListBuffer<JCStatement> rval = new ListBuffer<JCStatement>();
        if (parts.size() == 1) {
            test = (JCExpression)parts.get(0);
        } else {
            rval.add((JCStatement)parts.get(0));
            test = (JCExpression)parts.get(parts.size()-1);
        }
        //Get the custom message
        String message = buildAssertionMessage(ass);
        
        //TODO proper exception type, include expression, etc
        JCExpression t = make().NewClass(null, null,
                makeIdent(syms().ceylonExceptionType),
                List.<JCExpression>of(boxType(make().Literal(message), typeFact().getStringDeclaration().getType()), makeNull()),
                null);
        rval.add(make().If(make().Unary(JCTree.NOT, test), make().Throw(t), null));
        if (parts.size() == 3) {
            rval.add((JCStatement)parts.get(1));
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
        String location = ass.getUnit().getFilename() + ":" + ass.getCondition().getLocation();
        sb.append("\tat ").append(location).append("\n");
        sb.append("\texpression ").append(getSourceCode(ass.getCondition()));
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
