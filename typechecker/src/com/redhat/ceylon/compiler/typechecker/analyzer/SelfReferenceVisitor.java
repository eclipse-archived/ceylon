package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getLastExecutableStatement;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getSuper;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
/**
 * Validates that the initializer of a class does
 * not leak self-references to the instance being
 * initialized.
 * 
 * @author Gavin King
 *
 */
public class SelfReferenceVisitor extends Visitor {
    
    private final TypeDeclaration typeDeclaration;
    private Tree.Statement lastExecutableStatement;
    private boolean declarationSection = false;
    private int nestedLevel = -1;
    
    public SelfReferenceVisitor(TypeDeclaration td) {
        typeDeclaration = td;
    }
    
    private void visitExtendedType(Tree.ExtendedTypeExpression that) {
        Declaration member = that.getDeclaration();
        if (member!=null && !typeDeclaration.isAlias()) {
            if ( !declarationSection && isInherited(that, member) ) {
                that.addError("inherited member class may not be extended in initializer: " + 
                        member.getName());
            }
        }
    }

    private void visitReference(Tree.Primary that) {
        if (that instanceof Tree.MemberOrTypeExpression) {
            Declaration member = ((Tree.MemberOrTypeExpression) that).getDeclaration();
            if (member!=null) {
                if ( !declarationSection && isInherited(that, member)) {
                    that.addError("inherited member may not be used in initializer: " + 
                                member.getName());
                }
            }
        }
    }
    
    private boolean isInherited(Tree.Primary that, Declaration member) {
        return that.getScope().getInheritingDeclaration(member)==typeDeclaration;
    }

    @Override
    public void visit(Tree.AnnotationList that) {}

    @Override
    public void visit(Tree.ExtendedTypeExpression that) {
        super.visit(that);
        visitExtendedType(that);
    }

    @Override
    public void visit(Tree.BaseMemberExpression that) {
        super.visit(that);
        visitReference(that);
    }

    @Override
    public void visit(Tree.BaseTypeExpression that) {
        super.visit(that);
        visitReference(that);
    }

    @Override
    public void visit(Tree.QualifiedMemberExpression that) {
        super.visit(that);
        if (isSelfReference(that.getPrimary())) {
            visitReference(that);
        }
    }

    @Override
    public void visit(Tree.QualifiedTypeExpression that) {
        super.visit(that);
        if (isSelfReference(that.getPrimary())) {
            visitReference(that);
        }
    }

    private boolean isSelfReference(Tree.Term that) {
        return (directlyInBody() && (that instanceof Tree.This || that instanceof Tree.Super))
            || (directlyInNestedBody() && that instanceof Tree.Outer);
    }

    @Override
    public void visit(Tree.IsCondition that) {
        super.visit(that);
        if ( inBody() ) {
            Tree.Variable v = that.getVariable();
            if (v!=null && v.getSpecifierExpression()!=null) {
                Tree.Term term = v.getSpecifierExpression()
                        .getExpression().getTerm();
                if (directlyInBody() && term instanceof Tree.Super) {
                    term.addError("narrows super");
                }
                else if (mayNotLeakThis() && term instanceof Tree.This) {
                    term.addError("narrows this in initializer: " + 
                            typeDeclaration.getName());
                }
                else if (mayNotLeakOuter() && term instanceof Tree.Outer) {
                    term.addError("narrows outer in initializer: " + 
                            typeDeclaration.getName());
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.ObjectDefinition that) {
        if (that.getAnonymousClass()==typeDeclaration) {
            nestedLevel=0;
            super.visit(that);
            nestedLevel=-1;
        }
        else if (inBody()){
            nestedLevel++;
            super.visit(that);
            nestedLevel--;
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.ObjectArgument that) {
        if (that.getAnonymousClass()==typeDeclaration) {
            nestedLevel=0;
            super.visit(that);
            nestedLevel=-1;
        }
        else if (inBody()){
            nestedLevel++;
            super.visit(that);
            nestedLevel--;
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.TypeDeclaration that) {
        if (that.getDeclarationModel()==typeDeclaration) {
            nestedLevel=0;
            declarationSection = false;
            super.visit(that);
            nestedLevel=-1;
        }
        else if (inBody()){
            nestedLevel++;
            super.visit(that);
            nestedLevel--;
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.InterfaceBody that) {
        if (directlyInBody()) {
            declarationSection = true;
            lastExecutableStatement = null;
            super.visit(that);
            declarationSection = false;
        }
        else {
            super.visit(that);
        }
    }

    private boolean directlyInBody() {
        return nestedLevel==0;
    }
    
    @Override
    public void visit(Tree.ClassBody that) {
        if (directlyInBody()) {
            Tree.Statement les = getLastExecutableStatement(that);
            declarationSection = les==null;
            lastExecutableStatement = les;
            super.visit(that);
            lastExecutableStatement = null;
            declarationSection = false;
        }
        else {
            super.visit(that);
        }
    }

    boolean mayNotLeakThis() {
        return !declarationSection && directlyInBody();
    }
    
    boolean mayNotLeakOuter() {
        return !declarationSection && directlyInNestedBody();
    }

    private boolean directlyInNestedBody() {
        return nestedLevel==1;
    }
    
    boolean inBody() {
        return nestedLevel>=0;
    }
    
    @Override
    public void visit(Tree.Statement that) {
        super.visit(that);
        if (directlyInBody()) {
            declarationSection = declarationSection || 
                    that==lastExecutableStatement;
        }
    }
    
    private void checkSelfReference(Node that, Tree.Term term) {
        Tree.Term t = getSuper(term);
        if (directlyInBody() && t instanceof Tree.Super) {
            that.addError("leaks super reference in body: " + 
                    typeDeclaration.getName());
        }    
        if (mayNotLeakThis() && t instanceof Tree.This) {
            that.addError("leaks this reference in initializer: " + 
                    typeDeclaration.getName());
        }    
        if (mayNotLeakOuter() && t instanceof Tree.Outer) {
            that.addError("leaks outer reference in initializer: " + 
                    typeDeclaration.getName());
        }
    }

    @Override
    public void visit(Tree.Return that) {
        super.visit(that);
        Tree.Expression e = that.getExpression();
        if ( e!=null && inBody() ) {
            checkSelfReference(that, e.getTerm());    
        }
    }

    @Override
    public void visit(Tree.Throw that) {
        super.visit(that);
        Tree.Expression e = that.getExpression();
        if ( e!=null && inBody() ) {
            checkSelfReference(that, e.getTerm());    
        }
    }

    @Override
    public void visit(Tree.SpecifierOrInitializerExpression that) {
        super.visit(that);
        Tree.Expression e = that.getExpression();
        if ( e!=null && inBody() ) {
            checkSelfReference(that, e.getTerm());    
        }
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
    	if ( inBody() ) {
    		Tree.Term lt = that.getBaseMemberExpression();
			Tree.SpecifierExpression se = that.getSpecifierExpression();
    		if (lt instanceof Tree.MemberOrTypeExpression && se!=null) {
    			Tree.Expression e = se.getExpression();
    			if (e!=null) {
    				if (e.getTerm() instanceof Tree.This) {
    					Declaration d = ((Tree.MemberOrTypeExpression) lt).getDeclaration();
    					if (d instanceof MethodOrValue) {
    						if (((MethodOrValue) d).isLate()) {
    							lt.visit(this);
    							return; //NOTE: EARLY EXIT!!
    						}
    					}
    				}
    			}
    		}
    	}
    	super.visit(that);
    }

    @Override
    public void visit(Tree.AssignmentOp that) {
        super.visit(that);
        if ( inBody() ) {
        	Tree.Term lt = that.getLeftTerm();
			if (lt instanceof Tree.MemberOrTypeExpression &&
					that.getRightTerm() instanceof Tree.This) {
        		Declaration d = ((Tree.MemberOrTypeExpression) lt).getDeclaration();
        		if (d instanceof MethodOrValue) {
        			if (((MethodOrValue) d).isLate()) {
        				return; //NOTE: EARLY EXIT!!
        			}
        		}
        	}
            checkSelfReference(that, that.getRightTerm());    
        }
    }

    /*@Override
    public void visit(Tree.PositionalArgumentList that) {
        super.visit(that);
        if ( inBody() ) {
            for (Tree.PositionalArgument arg: that.getPositionalArguments()) {
                Expression e = arg.getExpression();
                if (e!=null) {
                    checkSelfReference(arg, e.getTerm());
                }
            }
        }
    }*/

    @Override
    public void visit(Tree.ListedArgument that) {
        super.visit(that);
        if ( inBody() ) {
            Tree.Expression e = that.getExpression();
            if (e!=null) {
                checkSelfReference(that, e.getTerm());
            }
        }
    }

    @Override
    public void visit(Tree.BinaryOperatorExpression that) {
        super.visit(that);
        if ( inBody() && !(that instanceof Tree.AssignmentOp) ) {
            checkSelfReference(that, that.getLeftTerm());
            checkSelfReference(that, that.getRightTerm());
        }
    }

    @Override
    public void visit(Tree.UnaryOperatorExpression that) {
        super.visit(that);
        if ( inBody() && !(that instanceof Tree.OfOp) ) {
            checkSelfReference(that, that.getTerm());
        }
    }

    @Override
    public void visit(Tree.WithinOp that) {
        super.visit(that);
        if ( inBody() ) {
            checkSelfReference(that, that.getTerm());
            checkSelfReference(that, that.getLowerBound());
            checkSelfReference(that, that.getUpperBound());
        }
    }

    @Override
    public void visit(Tree.SpreadArgument that) {
        super.visit(that);
        if ( inBody() ) {
            Tree.Expression e = that.getExpression();
            if (e!=null) {
            	checkSelfReference(that, e.getTerm());
            }
        }
    }

    @Override
    public void visit(Tree.NamedArgumentList that) {
        super.visit(that);
        if ( inBody() ) {
            for (Tree.NamedArgument arg: that.getNamedArguments()) {
                if (arg instanceof Tree.SpecifiedArgument) {
                    Tree.SpecifierExpression se = ((Tree.SpecifiedArgument) arg).getSpecifierExpression();
                    Tree.Expression e = se.getExpression();
                    if (e!=null) {
                        checkSelfReference(se, e.getTerm());
                    }
                }
            }
        }
    }

}
