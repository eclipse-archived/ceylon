package com.redhat.ceylon.compiler.analyzer;

import com.redhat.ceylon.compiler.model.GenericType;
import com.redhat.ceylon.compiler.model.Scope;
import com.redhat.ceylon.compiler.model.Type;
import com.redhat.ceylon.compiler.model.Typed;
import com.redhat.ceylon.compiler.tree.Tree;
import com.redhat.ceylon.compiler.tree.Tree.MemberOrType;
import com.redhat.ceylon.compiler.tree.Visitor;

public class ExpressionVisitor extends Visitor {
	
	Type receivingType;
	//Primaries:
	
	@Override public void visit(Tree.MemberExpression that) {
        that.getPrimary().visit(this);
        GenericType gt = receivingType.getGenericType();
        if (gt instanceof Scope) {
            MemberOrType mt = that.getMemberOrType();
            if (mt instanceof Tree.Member) {
            	Typed member = Util.getDeclaration((Scope) gt, (Tree.Member) mt);
            	receivingType = member.getType();
                //TODO: handle type arguments by substitution
            	mt.setModelNode(member);
            	System.out.println(((Tree.Member) mt).getIdentifier().getText() + 
            			":" + receivingType.getProducedTypeName());
            }
            else if (mt instanceof Tree.Type) {
            	GenericType member = Util.getDeclaration((Scope) gt, (Tree.Type) mt);
            	receivingType = new Type();
            	receivingType.setGenericType(member);
            	receivingType.setTreeNode(that);
                //TODO: handle type arguments by substitution
            	mt.setModelNode(member);
            	System.out.println(((Tree.Type) mt).getIdentifier().getText() + 
            			":" + receivingType.getProducedTypeName());
            }
            else {
            	//TODO: handle type parameters by looking at
            	//      their upper bound constraints 
            	throw new RuntimeException("Not yet supported");
            }
        }
	}
	
	@Override public void visit(Tree.Annotation that) {
		//TODO: ignore annotations for now
	}
	
	@Override public void visit(Tree.InvocationExpression that) {
		super.visit(that);
		//TODO!
	}
	
	@Override public void visit(Tree.IndexExpression that) {
		super.visit(that);
		//TODO!
	}
	
	@Override public void visit(Tree.PostfixOperatorExpression that) {
		super.visit(that);
		//TODO!
	}
		
	//Atoms:
	
	@Override public void visit(Tree.Member that) {
		//TODO: this does not correctly handle methods
		//      and classes which are not subsequently 
		//      invoked (should return the callable type)
		receivingType = Util.getDeclaration(that).getType();
		System.out.println(that.getIdentifier().getText() + 
				":" + receivingType.getProducedTypeName());
	}
	
	@Override public void visit(Tree.Type that) {
		//TODO: this does not correctly handle methods
		//      and classes which are not subsequently 
		//      invoked (should return the callable type)
		receivingType = (Type) that.getModelNode();
		System.out.println(that.getIdentifier().getText() + 
				":" + receivingType.getProducedTypeName());
	}
	
	@Override public void visit(Tree.Expression that) {
		//TODO!
		//i.e. this is a parenthesized expression
		super.visit(that);
	}
	
	@Override public void visit(Tree.Outer that) {
		//TODO!
	}
	
	@Override public void visit(Tree.Super that) {
		//TODO!
	}
	
	@Override public void visit(Tree.This that) {
		//TODO!
	}
	
	@Override public void visit(Tree.Subtype that) {
		//TODO!
	}
	
	@Override public void visit(Tree.StringTemplate that) {
		//TODO!
	}
	
	@Override public void visit(Tree.SequenceEnumeration that) {
		super.visit(that);
		//TODO!
	}
	
	@Override public void visit(Tree.NaturalLiteral that) {
		//TODO!
	}
	
	@Override public void visit(Tree.FloatLiteral that) {
		//TODO!
	}
	
	@Override public void visit(Tree.CharLiteral that) {
		//TODO!
	}
	
	@Override public void visit(Tree.QuotedLiteral that) {
		//TODO!
	}
	
}
