package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;

/**
 * Validates that the initializer of a class does
 * not leak self-references to the instance being
 * initialized.
 * 
 * @author Gavin King
 *
 */
public class SelfReferenceVisitor extends AbstractVisitor {
    
    private final TypeDeclaration typeDeclaration;
    private Tree.Statement lastExecutableStatement;
    private boolean declarationSection = false;
    private int nestedLevel = -1;
    private Context context;
    
    public SelfReferenceVisitor(TypeDeclaration td, Context context) {
        typeDeclaration = td;
        this.context = context;
    }

    @Override
    protected Context getContext() {
        return context;
    }
    
    private void visitExtendedType(Tree.ExtendedTypeExpression that) {
        Declaration member = that.getDeclaration();
        if (member!=null) {
            if ( !declarationSection && isInherited(that, member) ) {
                that.addError("inherited member class may not be extended in initializer: " + 
                        member.getName());
            }
        }
    }

    private void visitReference(Tree.Primary that) {
        Declaration member  = that.getDeclaration();
        if (member!=null) {
            if ( !declarationSection && isInherited(that, member)) {
                that.addError("inherited member may not be used in initializer: " + 
                            member.getName());
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
            if (that.getVariable().getSpecifierExpression()!=null) {
                Tree.Term term = that.getVariable().getSpecifierExpression()
                        .getExpression().getTerm();
                if (directlyInBody() && term instanceof Tree.Super) {
                    term.addError("narrows super");
                }
                else if (mayNotLeakThis() && term instanceof Tree.This) {
                    term.addError("narrows this in initializer: " + 
                            typeDeclaration.getName());
                }
                else if (mayNotLeakOuter() && term instanceof Tree.Outer) {
                    term.addError("narrows outer in initializer of : " + 
                            typeDeclaration.getName());
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.ObjectDefinition that) {
        TypeDeclaration dec = that.getDeclarationModel().getTypeDeclaration();
        if (dec==typeDeclaration) {
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
        if (that.getDeclarationModel().getTypeDeclaration()==typeDeclaration) {
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
        if (directlyInBody() && term instanceof Tree.Super) {
            that.addError("leaks super reference in body of: " + 
                    typeDeclaration.getName());
        }    
        if (mayNotLeakThis() && term instanceof Tree.This) {
            that.addError("leaks this reference in initializer of: " + 
                    typeDeclaration.getName());
        }    
        if (mayNotLeakOuter() && term instanceof Tree.Outer) {
            that.addError("leaks outer reference in initializer of: " + 
                    typeDeclaration.getName());
        }
    }

    @Override
    public void visit(Tree.Return that) {
        super.visit(that);
        if ( that.getExpression()!=null && inBody() ) {
            checkSelfReference(that, that.getExpression().getTerm());    
        }
    }

    @Override
    public void visit(Tree.SpecifierOrInitializerExpression that) {
        super.visit(that);
        if ( inBody() ) {
            checkSelfReference(that, that.getExpression().getTerm());    
        }
    }

    @Override
    public void visit(Tree.AssignmentOp that) {
        super.visit(that);
        if ( inBody() ) {
            checkSelfReference(that, that.getRightTerm());    
        }
    }

    @Override
    public void visit(Tree.PositionalArgumentList that) {
        super.visit(that);
        if ( inBody() ) {
            for ( Tree.PositionalArgument arg: that.getPositionalArguments()) {
                checkSelfReference(arg, arg.getExpression().getTerm());    
            }
        }
    }

    @Override
    public void visit(Tree.NamedArgumentList that) {
        super.visit(that);
        if ( inBody() ) {
            for ( Tree.NamedArgument arg: that.getNamedArguments()) {
                if (arg instanceof Tree.SpecifiedArgument) {
                    Tree.SpecifierExpression se = ((Tree.SpecifiedArgument) arg).getSpecifierExpression();
                    checkSelfReference(se, se.getExpression().getTerm());    
                }
            }
        }
    }

}
