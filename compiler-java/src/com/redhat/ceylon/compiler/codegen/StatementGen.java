package com.redhat.ceylon.compiler.codegen;

import static com.sun.tools.javac.code.Flags.FINAL;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Type;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassOrInterface;
import com.sun.tools.javac.code.TypeTags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

public class StatementGen extends GenPart {

	// Used to hold the name of the variable associated with the fail-block if the innermost for-loop
	// Is null if we're currently in a while-loop or not in any loop at all
    private Name currentForFailVariable = null;
    
	public StatementGen(Gen2 gen) {
        super(gen);
    }

    class StatementVisitor extends Visitor implements NaturalVisitor {
        final ListBuffer<JCStatement> stmts;
        final Tree.ClassOrInterface cdecl;

        StatementVisitor(Tree.ClassOrInterface cdecl, ListBuffer<JCStatement> stmts) {
            this.stmts = stmts;
            this.cdecl = cdecl;
        }

        public ListBuffer<JCStatement> stmts() {
            return stmts;
        }

        public void visit(Tree.InvocationExpression expr) {
            append(at(expr).Exec(gen.expressionGen.convert(expr)));
        }

        public void visit(Tree.Return ret) {
            append(convert(ret));
        }

        public void visit(Tree.IfStatement stat) {
            append(convert(cdecl, stat));
        }

        public void visit(Tree.WhileStatement stat) {
            append(convert(cdecl, stat));
        }

        public void visit(Tree.DoWhileStatement stat) {
            append(convert(cdecl, stat));
        }

        public void visit(Tree.ForStatement stat) {
            append(convert(cdecl, stat));
        }

        public void visit(Tree.Break stat) {
            append(convert(cdecl, stat));
        }

        public void visit(Tree.AttributeDeclaration decl) {
        	append(convert(cdecl, decl));
        }

        public void visit(Tree.SpecifierStatement op) {
            append(convert(op));
        }

        // FIXME: not sure why we don't have just an entry for Tree.Term here...
        public void visit(Tree.OperatorExpression op) {
            append(at(op).Exec(gen.expressionGen.convertExpression(op)));
        }

        public void visit(Tree.Expression tree) {
            append(at(tree).Exec(gen.expressionGen.convertExpression(tree)));
        }

        public void visit(Tree.MethodDefinition decl) {
            final ListBuffer<JCTree> defs = new ListBuffer<JCTree>();
            gen.classGen.methodClass(cdecl, decl, defs, false);
            for (JCTree def : defs.toList()) {
                JCClassDecl innerDecl = (JCClassDecl) def;
                stmts.append(innerDecl);
                JCExpression id = make().Ident(innerDecl.name);
                stmts.append(at(decl).VarDef(make().Modifiers(FINAL), names().fromString(decl.getIdentifier().getText()), id, at(decl).NewClass(null, null, id, List.<JCExpression> nil(), null)));
            }
        }

        // FIXME: I think those should just go in convertExpression no?
        public void visit(Tree.PostfixOperatorExpression expr) {
            append(at(expr).Exec(gen.expressionGen.convert(expr)));
        }

        public void visit(Tree.PrefixOperatorExpression expr) {
            append(at(expr).Exec(gen.expressionGen.convert(expr)));
        }

        public void visit(Tree.ExpressionStatement tree) {
            append(at(tree).Exec(gen.expressionGen.convertExpression(tree.getExpression())));
        }
        
        private void append(JCStatement stmt) {
        	stmts.append(stmt);
        }
        
        private void append(List<JCStatement> list) {
            stmts.appendList(list);
        }
    }

    public JCBlock convert(Tree.ClassOrInterface cdecl, Tree.Block block) {
        return block == null ? null : at(block).Block(0, convertStmts(cdecl, block.getStatements()));
    }

    private List<JCStatement> convertStmts(Tree.ClassOrInterface cdecl, java.util.List<Tree.Statement> list) {
        final ListBuffer<JCStatement> buf = new ListBuffer<JCStatement>();

        StatementVisitor v = new StatementVisitor(cdecl, buf);

        for (Tree.Statement stmt : list)
            stmt.visit(v);

        return buf.toList();
    }

    private List<JCStatement> convert(Tree.ClassOrInterface cdecl, Tree.IfStatement stmt) {
    	Tree.Block thenPart = stmt.getIfClause().getBlock();
    	Tree.Block elsePart = stmt.getElseClause() != null ? stmt.getElseClause().getBlock() : null;
        return convertCondition(cdecl, stmt.getIfClause().getCondition(), JCTree.IF, thenPart, elsePart);
    }

    private List<JCStatement> convert(Tree.ClassOrInterface cdecl, Tree.WhileStatement stmt) {
        Name tempForFailVariable = currentForFailVariable;
        currentForFailVariable = null;
        
        Tree.Block thenPart = stmt.getWhileClause().getBlock();
        List<JCStatement> res = convertCondition(cdecl, stmt.getWhileClause().getCondition(), JCTree.WHILELOOP, thenPart, null);
        
        currentForFailVariable = tempForFailVariable;
        
        return res;
    }

    private List<JCStatement> convert(Tree.ClassOrInterface cdecl, Tree.DoWhileStatement stmt) {
        Name tempForFailVariable = currentForFailVariable;
        currentForFailVariable = null;
        
        Tree.Block thenPart = stmt.getDoClause().getBlock();
        List<JCStatement> res = convertCondition(cdecl, stmt.getDoClause().getCondition(), JCTree.DOLOOP, thenPart, null);
        
        currentForFailVariable = tempForFailVariable;
        
        return res;
    }

    private List<JCStatement> convertCondition(Tree.ClassOrInterface cdecl, Tree.Condition cond, int tag, Tree.Block thenPart, Tree.Block elsePart) {
    	JCExpression test;
    	JCVariableDecl decl = null;
    	JCBlock thenBlock = null;
    	JCBlock elseBlock = null;
        if (cond instanceof Tree.ExistsCondition) {
            Tree.ExistsCondition exists = (Tree.ExistsCondition) cond;
            Tree.Identifier name = exists.getVariable().getIdentifier();

            JCExpression expr;
            if (exists.getExpression() == null) {
                Name tmp = names().fromString(name.getText());
                expr = at(cond).Ident(tmp);
            } else {
                expr = gen.expressionGen.convertExpression(exists.getExpression());
            }

            test = at(cond).Binary(JCTree.NE, expr, make().Literal(TypeTags.BOT, null));
            thenBlock = convert(cdecl, thenPart);
        } else if (cond instanceof Tree.NonemptyCondition) {
            Tree.NonemptyCondition nonempty = (Tree.NonemptyCondition) cond;
            Tree.Identifier name = nonempty.getVariable().getIdentifier();
            throw new RuntimeException();
        } else if (cond instanceof Tree.IsCondition) {
            Tree.IsCondition isExpr = (Tree.IsCondition) cond;
            Tree.Identifier name = isExpr.getVariable().getIdentifier();
            JCExpression type = gen.variableType(isExpr.getType(), null);

            Name tmpVarName = names().fromString(aliasName(name.getText()));
            Name origVarName = names().fromString(name.getText());
            Name substVarName = names().fromString(aliasName(name.getText()));

            JCExpression expr;
            ProducedType tmpVarType;
            if (isExpr.getExpression() == null) {
            	if (isExpr.getVariable().getSpecifierExpression() == null) {
            		expr = convert(name);
            		tmpVarType = isExpr.getVariable().getType().getTypeModel();
            	} else {
                    expr = gen.expressionGen.convertExpression(isExpr.getVariable().getSpecifierExpression().getExpression());
                    tmpVarType = isExpr.getVariable().getSpecifierExpression().getExpression().getTypeModel();
            	}
            } else {
                expr = gen.expressionGen.convertExpression(isExpr.getExpression());
                tmpVarType = isExpr.getExpression().getTypeModel();
            }

            // Temporary variable holding the result of the expression/variable to test
            decl = at(cond).VarDef(make().Modifiers(FINAL), tmpVarName, makeIdent(tmpVarType.getProducedTypeName()), expr);
            // Substitute variable with the correct type to use in the rest of the code block
            JCVariableDecl decl2 = at(cond).VarDef(make().Modifiers(FINAL), substVarName, type, at(cond).TypeCast(type, at(cond).Ident(tmpVarName)));
            
            // Prepare for variable substitution in the following code block
            String prevSubst = gen.addVariableSubst(origVarName.toString(), substVarName.toString());
            
            thenBlock = convert(cdecl, thenPart);
            thenBlock = at(cond).Block(0, List.<JCStatement> of(decl2, thenBlock));
            
            // Deactivate the above variable substitution
            gen.removeVariableSubst(origVarName.toString(), prevSubst);
            
            if (elsePart != null) {
            	elseBlock = convert(cdecl, elsePart);
            }

            test = at(cond).TypeTest(make().Ident(decl.name), type);
        } else if (cond instanceof Tree.BooleanCondition) {
            Tree.BooleanCondition booleanCondition = (Tree.BooleanCondition) cond;
            test = gen.expressionGen.convertExpression(booleanCondition.getExpression());
            JCExpression trueValue = at(cond).Apply(List.<JCTree.JCExpression>nil(), 
                    makeIdent("ceylon", "language", "$true", "getTrue"), List.<JCTree.JCExpression>nil());
            test = at(cond).Binary(JCTree.EQ, test, trueValue);
            thenBlock = convert(cdecl, thenPart);
        } else {
            throw new RuntimeException("Not implemented: " + cond.getNodeType());
        }
        
        JCStatement cond1;
        switch (tag) {
        case JCTree.IF:
            cond1 = at(cond).If(test, thenBlock, elseBlock);
            break;
        case JCTree.WHILELOOP:
            assert elsePart == null;
            cond1 = at(cond).WhileLoop(test, thenBlock);
            break;
        case JCTree.DOLOOP:
            assert elsePart == null;
            cond1 = at(cond).DoLoop(thenBlock, test);
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

    private List<JCStatement> convert(Tree.ClassOrInterface cdecl, Tree.ForStatement stmt) {
        class ForVisitor extends Visitor {
            Tree.Variable variable = null;

            public void visit(Tree.ValueIterator valueIterator) {
                assert variable == null;
                variable = valueIterator.getVariable();
            }

            public void visit(Tree.KeyValueIterator keyValueIterator) {
                assert variable == null;
                // FIXME: implement this
                throw new RuntimeException("Not implemented: " + keyValueIterator.getNodeType());
            }
        }

        Name tempForFailVariable = currentForFailVariable;
        
        List<JCStatement> outer = List.<JCStatement> nil();
        if (stmt.getFailClause() != null) {
        	// boolean $ceylontmpX = true;
            JCVariableDecl failtest_decl = at(stmt).VarDef(make().Modifiers(0), names().fromString(tempName()), makeIdent("boolean"), makeIdent("true"));
            outer = outer.append(failtest_decl);
            
        	currentForFailVariable = failtest_decl.getName();
        } else {
        	currentForFailVariable = null;
        }

        ForVisitor visitor = new ForVisitor();
        stmt.getForClause().getForIterator().visit(visitor);
        JCExpression item_type = gen.variableType(visitor.variable.getType(), null);

        // ceylon.language.Iterator<T> $ceylontmpX = ITERABLE.iterator();
        JCExpression containment = gen.expressionGen.convertExpression(stmt.getForClause().getForIterator().getSpecifierExpression().getExpression());
        JCVariableDecl iter_decl = at(stmt).VarDef(make().Modifiers(0), names().fromString(tempName()), gen.iteratorType(item_type), at(stmt).Apply(null, at(stmt).Select(containment, names().fromString("iterator")), List.<JCExpression> nil()));
        outer = outer.append(iter_decl);
        JCIdent iter_id = at(stmt).Ident(iter_decl.getName());

        // ceylon.language.Optional<T> $ceylontmpY = $ceylontmpX.head();
        JCVariableDecl optional_item_decl = at(stmt).VarDef(make().Modifiers(FINAL), names().fromString(tempName()), gen.optionalType(item_type), at(stmt).Apply(null, at(stmt).Select(iter_id, names().fromString("head")), List.<JCExpression> nil()));
        List<JCStatement> while_loop = List.<JCStatement> of(optional_item_decl);
        JCIdent optional_item_id = at(stmt).Ident(optional_item_decl.getName());

        // T n = $ceylontmpY.t;
        JCVariableDecl item_decl = at(stmt).VarDef(make().Modifiers(0), names().fromString(visitor.variable.getIdentifier().getText()), item_type, at(stmt).Apply(null, at(stmt).Select(optional_item_id, names().fromString("$internalErasedExists")), List.<JCExpression> nil()));
        List<JCStatement> inner = List.<JCStatement> of(item_decl);

        // The user-supplied contents of the loop
        inner = inner.appendList(convertStmts(cdecl, stmt.getForClause().getBlock().getStatements()));

        // if ($ceylontmpY != null) ... else break;
        JCStatement test = at(stmt).If(at(stmt).Binary(JCTree.NE, optional_item_id, make().Literal(TypeTags.BOT, null)), at(stmt).Block(0, inner), at(stmt).Block(0, List.<JCStatement> of(at(stmt).Break(null))));
        while_loop = while_loop.append(test);

        // $ceylontmpX = $ceylontmpX.tail();
        JCExpression next = at(stmt).Assign(iter_id, at(stmt).Apply(null, at(stmt).Select(iter_id, names().fromString("tail")), List.<JCExpression> nil()));
        while_loop = while_loop.append(at(stmt).Exec(next));

        // while (True)...
        outer = outer.append(at(stmt).WhileLoop(at(stmt).Literal(TypeTags.BOOLEAN, 1), at(stmt).Block(0, while_loop)));

        if (stmt.getFailClause() != null) {
            // The user-supplied contents of fail block
        	List<JCStatement> failblock = convertStmts(cdecl, stmt.getFailClause().getBlock().getStatements());
        	
        	// if ($ceylontmpX) ...
            JCIdent failtest_id = at(stmt).Ident(currentForFailVariable);
            outer = outer.append(at(stmt).If(failtest_id, at(stmt).Block(0, failblock), null));
        }
        currentForFailVariable = tempForFailVariable;

        return outer;
    }

    // FIXME There is a similar implementation in ClassGen!
	public JCStatement convert(ClassOrInterface cdecl, AttributeDeclaration decl) {
    	Name atrrName = names().fromString(decl.getIdentifier().getText());
    	
    	JCExpression initialValue = null;
        if (decl.getSpecifierOrInitializerExpression() != null) {
        	// The attribute's initializer gets moved to the constructor (why?)
        	initialValue = gen.expressionGen.convertExpression(decl.getSpecifierOrInitializerExpression().getExpression());
        }

        final ListBuffer<JCAnnotation> langAnnotations = new ListBuffer<JCAnnotation>();

        JCExpression type = gen.convert(decl.getType());

        if (gen.isOptional(decl.getType())) {
            type = gen.optionalType(type);
        }
        
        int modifiers = convertLocalFieldDeclFlags(decl);
        return at(decl).VarDef(at(decl).Modifiers(modifiers, langAnnotations.toList()), atrrName, type, initialValue);
	}
	
    private List<JCStatement> convert(Tree.ClassOrInterface cdecl, Tree.Break stmt) {
    	// break;
    	JCStatement brk = at(stmt).Break(null);
    	
    	if (currentForFailVariable != null) {
            JCIdent failtest_id = at(stmt).Ident(currentForFailVariable);
            List<JCStatement> list = List.<JCStatement> of(at(stmt).Exec(at(stmt).Assign(failtest_id, makeIdent("false"))));
    		list = list.append(brk);
            return list;
    	} else {
    		return List.<JCStatement> of(brk);
    	}
    }

    private JCStatement convert(Tree.Return ret) {
        Tree.Expression expr = ret.getExpression();
        JCExpression returnExpr = expr != null ? gen.expressionGen.convertExpression(expr) : null;
        return at(ret).Return(returnExpr);
    }

    private JCIdent convert(Tree.Identifier identifier) {
		return at(identifier).Ident(names().fromString(gen.substitute(identifier.getText())));
    }

    private JCStatement convert(Tree.SpecifierStatement op) {
        return at(op).Exec(gen.expressionGen.convertAssignment(op, op.getBaseMemberExpression(), op.getSpecifierExpression().getExpression()));
    }

    private int convertLocalFieldDeclFlags(Tree.AttributeDeclaration cdecl) {
        int result = 0;

        result |= isMutable(cdecl) ? 0 : FINAL;

        return result;
    }

    private boolean isMutable(Tree.AttributeDeclaration decl) {
        // FIXME
        return hasCompilerAnnotation(decl, "variable");
    }
}
