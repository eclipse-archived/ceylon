package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getDeclaration;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Statement;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Validates that non-variable values are well-defined
 * within the local scope in which they occur. Checks
 * that they are not used before they are defined, that
 * they are always specified before they are used, and
 * that they are never specified twice.
 * 
 * @author Gavin King
 *
 */
public class SpecificationVisitor extends Visitor {
    
    private final Declaration declaration;
    
    private SpecificationState specified = new SpecificationState(false, false);
    private boolean cannotSpecify = true;
    private boolean declared = false;
    private Context context;
    private Statement lastExecutableStatement;
    private boolean declarationSection = false;

    class SpecificationState {
        boolean definitely;
        boolean possibly;
        boolean exited;
        SpecificationState(boolean definitely, boolean possibly) {
            this.definitely = definitely;
            this.possibly = possibly;
            this.exited = false;
        }
    }
    
    public SpecificationVisitor(Declaration declaration, Context context) {
        this.declaration = declaration;
        this.context = context;
    }
    
    private void declare() {
        declared = true;
    }
    
    private boolean beginDeclarationScope() {
        return declared;
    }
    
    private void endDeclarationScope(boolean d) {
        declared = d;
    }
    
    private boolean beginDisabledSpecificationScope() {
        boolean ca = cannotSpecify;
        cannotSpecify = true;
        return ca;
    }
    
    private void endDisabledSpecificationScope(boolean ca) {
        cannotSpecify = ca;
    }
    
    private void specify() {
        specified.definitely = true;
        specified.possibly = true;
    }
    
    private void exit() {
        specified.exited = true;
    }
    
    private SpecificationState beginSpecificationScope() {
        SpecificationState as = specified;
        specified = new SpecificationState(specified.definitely, specified.possibly);
        return as;
    }
    
    private void endSpecificationScope(SpecificationState as) {
        specified = as;
    }
    
    private boolean beginIndefiniteSpecificationScope() {
        return specified.definitely;
    }
    
    private void endIndefiniteSpecificationScope(boolean da) {
        specified.definitely = da;
    }
    
    private boolean isVariable() {
        return (declaration instanceof TypedDeclaration)
            && ((TypedDeclaration) declaration).isVariable();
    }
    
    @Override
    public void visit(Tree.AnnotationList that) {}
    
    @Override
    public void visit(Tree.BaseMemberExpression that) {
        visitReference(that, that.getDeclaration());
    }

    @Override
    public void visit(Tree.BaseTypeExpression that) {
        visitReference(that, that.getDeclaration());
    }

    @Override
    public void visit(Tree.QualifiedMemberExpression that) {
        if (isSelfReference(that.getPrimary())) {
            visitReference(that, that.getDeclaration());
        }
    }

    @Override
    public void visit(Tree.QualifiedTypeExpression that) {
        if (isSelfReference(that.getPrimary())) {
            visitReference(that, that.getDeclaration());
        }
    }

    private boolean isSelfReference(Tree.Primary that) {
        return that instanceof Tree.This || that instanceof Tree.Outer;
    }

    private void visitReference(Node that, Declaration member) {
        //Declaration member = getDeclaration(that.getScope(), that.getUnit(), id, context);
        //TODO: check superclass members are not in declaration section!
        if ( member==declaration && isDefinedInContainingScope(that, member) && !inDeclarationSection() ) {
            if (!declared) {
                that.addError("not yet declared: " + 
                        member.getName());
            }
            else if (!specified.definitely) {
                if (isVariable()) {
                    that.addError("not definitely initialized: " + 
                            member.getName());                    
                }
                else {
                    that.addError("not definitely specified: " + 
                            member.getName());
                }
            }
        }
    }

    private boolean isDefinedInContainingScope(Node node, Declaration member) {
        Scope scope = node.getScope();
        while (scope!=null) {
            if (scope.getMembers().contains(member)) {
                return true;
            }
            scope = scope.getContainer();
        }
        return false;
    }

    private boolean inDeclarationSection() {
        return declarationSection || 
            declaration.getContainer() instanceof Interface;
    }
    
    @Override
    public void visit(Tree.AssignOp that) {
        Tree.Term lt = that.getLeftTerm();
        if (lt instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression m = (Tree.BaseMemberExpression) lt;
            Declaration member = getDeclaration(m.getScope(), m.getUnit(), m.getIdentifier(), context);
            if (member==declaration) {
                that.getRightTerm().visit(this);
                checkVariable(lt);
                specify();
                lt.visit(this);
            }
            else {
                super.visit(that);
            }
        }
    }
    
    @Override
    public void visit(Tree.AssignmentOp that) {
        super.visit(that);
        checkVariable(that.getLeftTerm());
    }

    @Override
    public void visit(Tree.PostfixOperatorExpression that) {
        super.visit(that);
        checkVariable(that.getPrimary());
    }
    
    @Override
    public void visit(Tree.PrefixOperatorExpression that) {
        super.visit(that);
        checkVariable(that.getTerm());
    }
    
    private void checkVariable(Tree.Term term) {
        if (term instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression m = (Tree.BaseMemberExpression) term;
            Declaration member = getDeclaration(m.getScope(), m.getUnit(), m.getIdentifier(), context);
            if (member==declaration) {
                if (!isVariable()) {
                    term.addError("is not a variable: " +
                            member.getName());
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
        Tree.BaseMemberExpression m = that.getBaseMemberExpression();
        Declaration member = getDeclaration(m.getScope(), m.getUnit(), m.getIdentifier(), context);
        if (member==declaration) {
            that.getSpecifierExpression().visit(this);
            /*if (!declared) {
                m.addError("not yet declared: " + 
                        m.getIdentifier().getText());
            }
            else*/ if (isVariable()) {
                that.addError("variable values must be assigned using \":=\": " +
                        member.getName());
            }
            else if (cannotSpecify) {
                that.addError("cannot specify value from here: " + 
                        member.getName());
            }
            else if (specified.possibly) {
                that.addError("not definitely unspecified: " + 
                        member.getName());
            }
            else {
                specify();
                m.visit(this);
            }
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        if (that.getDeclarationModel()==declaration) {
            beginDisabledSpecificationScope();
            super.visit(that);
            declare();
            endDisabledSpecificationScope(false);
        }
        else {
            boolean c = beginDisabledSpecificationScope();
            boolean d = beginDeclarationScope();
            SpecificationState as = beginSpecificationScope();
            super.visit(that);
            endDisabledSpecificationScope(c);
            endDeclarationScope(d);
            endSpecificationScope(as);
        }
    }

    @Override
    public void visit(Tree.TypedArgument that) {
        if (that.getDeclarationModel()==declaration) {
            beginDisabledSpecificationScope();
            super.visit(that);
            declare();
            endDisabledSpecificationScope(false);
        }
        else {
            boolean c = beginDisabledSpecificationScope();
            boolean d = beginDeclarationScope();
            SpecificationState as = beginSpecificationScope();
            super.visit(that);
            endDisabledSpecificationScope(c);
            endDeclarationScope(d);
            endSpecificationScope(as);
        }
    }
    
    @Override
    public void visit(Tree.MethodDeclaration that) {
        //TODO: allow references to un-assigned things
        //      in interface bodies or the declaration
        //      section of class bodies.
        super.visit(that);
        if (that.getDeclarationModel()==declaration &&
                that.getSpecifierExpression()!=null) {
            specify();
        }
    }
    
    @Override
    public void visit(Tree.MethodDefinition that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        //TODO: allow references to un-assigned things
        //      in interface bodies or the declaration
        //      section of class bodies.
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.MethodArgument that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        //TODO: allow references to un-assigned things
        //      in interface bodies or the declaration
        //      section of class bodies.
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.Variable that) {
        super.visit(that);
        if (that.getDeclarationModel()==declaration) {
            specify();
        }
    }
    
    @Override
    public void visit(Tree.Parameter that) {
        super.visit(that);
        if (that.getDeclarationModel()==declaration) {
            specify();
        }
    }
    
    @Override
    public void visit(Tree.TypeParameterDeclaration that) {
        super.visit(that);
        if (that.getDeclarationModel()==declaration) {
            specify();
        }
    }
    
    @Override
    public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);        
        if (that.getDeclarationModel()==declaration &&
                that.getSpecifierOrInitializerExpression()!=null) {
            if (isVariable()) {
                if (that.getSpecifierOrInitializerExpression() instanceof Tree.SpecifierExpression) {
                    that.addError("variable values must be initialized using \":=\": " + that.getIdentifier().getText());
                }
            }
            else {
                if (that.getSpecifierOrInitializerExpression() instanceof Tree.InitializerExpression) {
                    that.addError("non-variable values must be specified using \"=\": " + that.getIdentifier().getText());
                }
            }
            specify();
        }
    }
    
    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    @Override
    public void visit(Tree.AttributeArgument that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    @Override
    public void visit(Tree.ObjectDefinition that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ObjectArgument that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ClassDefinition that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ClassBody that) {
        Tree.Statement les = null;
        boolean found = false;
        for (Tree.Statement s: that.getStatements()) {
            if (s instanceof Tree.ExecutableStatement) {
                les = s;
            }
            else {
                Tree.Declaration d = (Tree.Declaration) s;
                if (s instanceof Tree.AttributeDeclaration) {
                    if ( ((Tree.AttributeDeclaration) s).getSpecifierOrInitializerExpression()!=null ) {
                        les = s;
                    }
                }
                if (s instanceof Tree.MethodDeclaration) {
                    if ( ((Tree.MethodDeclaration) s).getSpecifierExpression()!=null ) {
                        les = s;
                    }
                }
                if (d.getDeclarationModel()==declaration) {
                    found = true;
                }
            }
        }
        if (found) {
            declarationSection = les==null;
            lastExecutableStatement = les;
            super.visit(that);        
            declarationSection = false;
            lastExecutableStatement = null;
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.Statement that) {
        super.visit(that);
        declarationSection = declarationSection || (that==lastExecutableStatement);
    }
    
    @Override
    public void visit(Tree.InterfaceDefinition that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    @Override
    public void visit(Tree.Return that) {
        super.visit(that);
        exit();
        //TODO: MOVE TO DIFFERENT VISITOR!
        if (lastExecutableStatement!=null && 
                that.getExpression().getTerm() instanceof Tree.This 
                && !inDeclarationSection()) {
            that.addError("leaks this reference");
        }
        //TODO: specifications and assignments
    }

    @Override
    public void visit(Tree.PositionalArgumentList that) {
        super.visit(that);
        //TODO: MOVE TO DIFFERENT VISITOR!
        if (lastExecutableStatement!=null && !inDeclarationSection()) {
            for ( Tree.PositionalArgument arg: that.getPositionalArguments()) {
                if (arg.getExpression().getTerm() instanceof Tree.This) {
                    arg.addError("leaks this reference");
                }
            }
        }
    }

    @Override
    public void visit(Tree.NamedArgumentList that) {
        super.visit(that);
        //TODO: MOVE TO DIFFERENT VISITOR!
        if (lastExecutableStatement!=null && !inDeclarationSection()) {
            for ( Tree.NamedArgument arg: that.getNamedArguments()) {
                if (arg instanceof Tree.SpecifiedArgument) {
                    if (((Tree.SpecifiedArgument)arg).getSpecifierExpression().getExpression().getTerm() instanceof Tree.This) {
                        arg.addError("leaks this reference");
                    }
                }
            }
        }
    }

    @Override
    public void visit(Tree.Throw that) {
        super.visit(that);
        exit();
    }
    
    @Override
    public void visit(Tree.Break that) {
        super.visit(that);
        exit();
    }

    @Override
    public void visit(Tree.Continue that) {
        super.visit(that);
        exit();
    }
    
    @Override
    public void visit(Tree.IfStatement that) {
        
        boolean d = beginDeclarationScope();
        SpecificationState as = beginSpecificationScope();
        that.getIfClause().visit(this);
        boolean definitelyAssignedByIfClause = specified.definitely || specified.exited;
        boolean possiblyAssignedByIfClause = specified.possibly;
        endDeclarationScope(d);
        endSpecificationScope(as);
        
        boolean definitelyAssignedByElseClause;
        boolean possiblyAssignedByElseClause;
        if (that.getElseClause()!=null) {
            d = beginDeclarationScope();
            as = beginSpecificationScope();
            that.getElseClause().visit(this);
            definitelyAssignedByElseClause = specified.definitely || specified.exited;
            possiblyAssignedByElseClause = specified.possibly;
            endDeclarationScope(d);
            endSpecificationScope(as);
        }
        else {
            definitelyAssignedByElseClause = false;
            possiblyAssignedByElseClause = false;
        }
        
        specified.definitely = specified.definitely || (definitelyAssignedByIfClause && definitelyAssignedByElseClause);
        specified.possibly = specified.possibly || possiblyAssignedByIfClause || possiblyAssignedByElseClause;
    }
    
    @Override
    public void visit(Tree.SwitchStatement that) {
        //TODO!!!
        //if every case and the default case definitely
        //assigns, then it is definitely assigned after
        //the switch statement
    }
    
    @Override
    public void visit(Tree.WhileClause that) {
        if (isVariable()) {
            boolean d = beginDeclarationScope();
            super.visit(that);
            endDeclarationScope(d);
        }
        else {
            boolean c = beginDisabledSpecificationScope();
            boolean d = beginDeclarationScope();
            super.visit(that);
            endDisabledSpecificationScope(c);
            endDeclarationScope(d);
        }
    }
    
    @Override
    public void visit(Tree.DoClause that) {
        if (isVariable()) {
            boolean d = beginDeclarationScope();
            super.visit(that);
            endDeclarationScope(d);
        }
        else {
            boolean c = beginDisabledSpecificationScope();
            boolean d = beginDeclarationScope();
            super.visit(that);
            endDisabledSpecificationScope(c);
            endDeclarationScope(d);
        }
    }

    @Override
    public void visit(Tree.ForClause that) {
        if (isVariable()) {
            boolean d = beginDeclarationScope();
            super.visit(that);
            endDeclarationScope(d);
        }
        else {
            boolean c = beginDisabledSpecificationScope();
            boolean d = beginDeclarationScope();        
            super.visit(that);
            endDisabledSpecificationScope(c);
            endDeclarationScope(d);
        }
    }
    

    @Override
    public void visit(Tree.FailClause that) {
        boolean o = beginIndefiniteSpecificationScope();
        boolean d = beginDeclarationScope();
        super.visit(that);
        endIndefiniteSpecificationScope(o);
        endDeclarationScope(d);
    }

    @Override
    public void visit(Tree.TryClause that) {
        //TODO: this isn't correct - if there are 
        //      no catch clauses, and the try clause 
        //      definitely assigns, it is definitely
        //      assigned after the try
        boolean o = beginIndefiniteSpecificationScope();
        boolean d = beginDeclarationScope();
        super.visit(that);
        endIndefiniteSpecificationScope(o);
        endDeclarationScope(d);
    }

    @Override
    public void visit(Tree.CatchClause that) {
        boolean o = beginIndefiniteSpecificationScope();
        boolean d = beginDeclarationScope();
        super.visit(that);
        endIndefiniteSpecificationScope(o);
        endDeclarationScope(d);
    }
    
    @Override
    public void visit(Tree.FinallyClause that) {
        boolean d = beginDeclarationScope();
        super.visit(that);
        endDeclarationScope(d);
    }
      
}
