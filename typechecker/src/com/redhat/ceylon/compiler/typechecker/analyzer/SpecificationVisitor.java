package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getBaseDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getLastExecutableStatement;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
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
    private boolean hasParameter = false;
    private Tree.Statement lastExecutableStatement;
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
    
    public SpecificationVisitor(Declaration declaration) {
        this.declaration = declaration;
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
    
    private boolean isVariable() {
        return (declaration instanceof TypedDeclaration)
            && ((TypedDeclaration) declaration).isVariable();
    }
    
    private boolean isLate() {
        return (declaration instanceof MethodOrValue)
            && ((MethodOrValue) declaration).isLate();
    }
    
    @Override
    public void visit(Tree.AnnotationList that) {}
    
    @Override
    public void visit(Tree.BaseMemberExpression that) {
        super.visit(that);
        visitReference(that);
    }

    @Override
    public void visit(Tree.ExtendedTypeExpression that) {
        super.visit(that);
        visitReference(that);
    }

    @Override
    public void visit(Tree.CaseTypes that) {
        //the BaseMemberExpressions in the CaseTypes
        //list are actually types, not value refs!
    }
    
    @Override
    public void visit(Tree.SatisfiedTypes that) {
        //unnecessary ... for consistency nothing else
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

    private boolean isSelfReference(Tree.Primary that) {
        return that instanceof Tree.This || that instanceof Tree.Outer;
    }

    private void visitReference(Tree.Primary that) {
        if (that instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression) that;
            Declaration member = mte.getDeclaration();
            //Declaration member = getDeclaration(that.getScope(), that.getUnit(), id, context);
            //TODO: check superclass members are not in declaration section!
            if ( member==declaration && 
                    member.isDefinedInScope(that.getScope()) ) {
                if (!declared) {
                    //you are allowed to refer to later 
                    //declarations in a class declaration
                    //section or interface
                    if (!isForwardReferenceable() && !hasParameter) {
                        if (declaration.getContainer() instanceof Class) {
                            that.addError("forward reference to class member in initializer: " + 
                                    member.getName() + " is not yet declared (forward references must occur in declaration section)");
                        }
                        else {
                            that.addError("forward reference to local declaration: " + 
                                    member.getName() + " is not yet declared");
                        }
                    }
                }
                else if (!specified.definitely) {
                    //you are allowed to refer to formal
                    //declarations in a class declaration
                    //section or interface
                    if (declaration.isFormal()) {
						if (!isForwardReferenceable()) {
						    that.addError("formal member may not be used in initializer: " + 
						            member.getName());                    
						}
					}
                    else if (!declaration.isNative()) {
                        if (isVariable()) {
                            that.addError("not definitely initialized: " + 
                                    member.getName());                    
                        }
                        else {
                            if (!isLate() || !cannotSpecify) {
                                that.addError("not definitely specified: " + 
                                        member.getName());
                            }
                        }
                    }
                }
                if (!mte.getAssigned() && member.isDefault() && 
                        !isForwardReferenceable()) {
                    that.addError("default member may not be used in initializer: " + 
                            member.getName());                    
                }
            }
        }
    }

    private boolean isForwardReferenceable() {
        return declarationSection ||
                declaration.isToplevel() ||
                declaration.isInterfaceMember();
    }
    
    private void assign(Tree.Term term) {
        if (term instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression m = (Tree.MemberOrTypeExpression) term;
            m.setAssigned(true);
        }
    }
    
    @Override
    public void visit(Tree.LogicalOp that) {
        that.getLeftTerm().visit(this);
    	SpecificationState ss = beginSpecificationScope();
        that.getRightTerm().visit(this);
    	endSpecificationScope(ss);
    }
    
    @Override
    public void visit(Tree.FunctionArgument that) {
    	SpecificationState ss = beginSpecificationScope();
    	super.visit(that);
    	endSpecificationScope(ss);
    }
    
    @Override
    public void visit(Tree.AssignOp that) {
        Tree.Term lt = that.getLeftTerm();
        assign(lt);
        if (lt instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression m = (Tree.BaseMemberExpression) lt;
            Declaration member = getBaseDeclaration(m, null, false);
            if (member==declaration) {
                if (that.getRightTerm()!=null) {
                    that.getRightTerm().visit(this);
                }
                checkVariable(lt, that);
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
        assign(that.getLeftTerm());
        super.visit(that);
        checkVariable(that.getLeftTerm(), that);
    }

    @Override
    public void visit(Tree.PostfixOperatorExpression that) {
        assign(that.getTerm());
        super.visit(that);
        checkVariable(that.getTerm(), that);
    }
    
    @Override
    public void visit(Tree.PrefixOperatorExpression that) {
        assign(that.getTerm());
        super.visit(that);
        checkVariable(that.getTerm(), that);
    }
    
    private void checkVariable(Tree.Term term, Node node) {
        //TODO: we don't really need this error check here,
        //      since it duplicates a check done more 
        //      completely in ExpressionVisitor.checkAssignable()
        if (term instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression m = (Tree.BaseMemberExpression) term;
            Declaration member = getBaseDeclaration(m, null, false);
            if (member==declaration) {
                if (!isVariable() && !isLate()) {
                    if (node instanceof Tree.AssignOp) {
                        node.addError("cannot specify non-variable value here: " +
                                        member.getName(), 803);
                    }
                    else {
                        term.addError("not a variable: " +
                                member.getName(), 800);
                    }
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
        Tree.Term m = that.getBaseMemberExpression();
        while (m instanceof Tree.ParameterizedExpression) {
        	m = ((Tree.ParameterizedExpression) m).getPrimary();
        }
        assign(m);
        if (m instanceof Tree.BaseMemberExpression) {
	        Declaration member = getBaseDeclaration((Tree.BaseMemberExpression)m, null, false);
	        if (member==declaration) {
            	boolean lazy = that.getSpecifierExpression() instanceof Tree.LazySpecifierExpression;
            	if (declaration instanceof Value && lazy!=((Value)declaration).isTransient()) {
            		that.addError("value must be specified using =>");
            	}
	            if (!lazy) that.getSpecifierExpression().visit(this);
	            boolean constant = !isVariable() && !isLate();
				if (!declared && constant) {
                    that.addError("specified value is not yet declared: " + 
                            member.getName());
	            }
	            else if (cannotSpecify && constant) {
	                that.addError("cannot specify non-variable value here: " + 
	                        member.getName(), 803);
	            }
	            else if (specified.possibly && constant) {
	                that.addError("specified non-variable value is not definitely unspecified: " + 
	                        member.getName(), 803);
	            }
	            else {
	                specify();
	                m.visit(this);
	            }
	            if (lazy) that.getSpecifierExpression().visit(this);
	        }
	        else {
	            super.visit(that);
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
        super.visit(that);
        if (that.getDeclarationModel()==declaration) {
            if (that.getSpecifierExpression()!=null) {
                specify();
            }
            else if (declaration.isToplevel() && !declaration.isNative()) {
                that.addError("toplevel function must be specified: " +
                        declaration.getName());
            }
            else if (declaration.isInterfaceMember() && 
            		!declaration.isFormal() && !declaration.isNative()) {
                that.addError("interface method must be formal or specified: " +
                        declaration.getName(), 1400);
            }
        }
    }
    
    @Override
    public void visit(Tree.MethodDefinition that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.MethodArgument that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
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
    public void visit(Tree.ValueParameterDeclaration that) {
        super.visit(that);
        if (that.getType() instanceof Tree.LocalModifier) {
            ValueParameter d = that.getDeclarationModel();
            Declaration a = that.getScope().getDirectMember(d.getName(), null, false);
            if (a!=null && a==declaration) {
                specify();
                hasParameter = true;
            }
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
        if (that.getDeclarationModel()==declaration) {
        	Tree.SpecifierOrInitializerExpression sie = that.getSpecifierOrInitializerExpression();
            if (sie!=null) {
                specify();
            }
            else if (declaration.isToplevel() && !isLate()) {
                if (isVariable()) {
                    that.addError("toplevel variable value must be initialized: " +
                            declaration.getName());
                }
                else {
                    that.addError("toplevel value must be specified: " +
                            declaration.getName());
                }
            }
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
    public void visit(Tree.AttributeSetterDefinition that) {
        if (that.getDeclarationModel()==declaration ||
            that.getDeclarationModel().getParameter()==declaration) {
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
    public void visit(Tree.AnyClass that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ClassBody that) {
        Tree.Declaration d = getDeclaration(that);
        if (d!=null) {
            Tree.Statement les = getLastExecutableStatement(that);
            declarationSection = les==null;
            lastExecutableStatement = les;
            super.visit(that);        
            declarationSection = false;
            lastExecutableStatement = null;
            if (isSharedDeclarationUninitialized()) {
                d.addError("must be definitely specified by class initializer: " + 
                        d.getDeclarationModel().getName(that.getUnit()), 1401);
            }
        }
        else {
            super.visit(that);
        }
    }

    private Tree.Declaration getDeclaration(Tree.ClassBody that) {
        for (Tree.Statement s: that.getStatements()) {
            if (s instanceof Tree.Declaration) {
                Tree.Declaration d = (Tree.Declaration) s;
                if (d.getDeclarationModel()==declaration) {
                    return d;
                }
            }
        }
        return null;
    }
    
    @Override
    public void visit(Tree.Statement that) {
        super.visit(that);
        checkDeclarationSection(that);
    }

    private void checkDeclarationSection(Tree.Statement that) {
        declarationSection = declarationSection || 
                that==lastExecutableStatement;
    }
    
    @Override
    public void visit(Tree.AnyInterface that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    @Override
    public void visit(Tree.TypeAliasDeclaration that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    public void visit(Tree.Return that) {
        super.visit(that);
        if (!cannotSpecify) {
            if (isSharedDeclarationUninitialized()) {
                that.addError("must be definitely specified by class initializer: " +
                        declaration.getName(that.getUnit()));
            }
        }
        exit();
    }

    private boolean isSharedDeclarationUninitialized() {
        return (declaration.isShared() || 
        		declaration.getOtherInstanceAccess()) && 
                !declaration.isFormal() && 
                !declaration.isNative() &&
                !isLate() &&
                !specified.definitely;
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
        
        if (that.getIfClause()!=null) {
        	if (that.getIfClause().getConditionList()!=null) {
        		that.getIfClause().getConditionList().visit(this);
        	}
        }
        
        boolean d = beginDeclarationScope();
        SpecificationState as = beginSpecificationScope();
        if (that.getIfClause()!=null) {
            that.getIfClause().visit(this);
        }
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
        
        checkDeclarationSection(that);
    }
    
    @Override
    public void visit(Tree.TryCatchStatement that) {
        boolean d = beginDeclarationScope();
        SpecificationState as = beginSpecificationScope();
        if( that.getTryClause()!=null ) {
            that.getTryClause().visit(this);
        }
        boolean definitelyAssignedByTryClause = specified.definitely || specified.exited;
        boolean possiblyAssignedByTryClause = specified.possibly;
        endDeclarationScope(d);
        endSpecificationScope(as);
        specified.possibly = specified.possibly || possiblyAssignedByTryClause;
        
        boolean definitelyAssignedByEveryCatchClause = true;
        boolean possiblyAssignedBySomeCatchClause = false;
        for (Tree.CatchClause cc: that.getCatchClauses()) {
            d = beginDeclarationScope();
            as = beginSpecificationScope();
            cc.visit(this);
            definitelyAssignedByEveryCatchClause = definitelyAssignedByEveryCatchClause && (specified.definitely || specified.exited);
            possiblyAssignedBySomeCatchClause = possiblyAssignedBySomeCatchClause || specified.possibly;
            endDeclarationScope(d);
            endSpecificationScope(as);
        }
        specified.possibly = specified.possibly || possiblyAssignedBySomeCatchClause;
        
        boolean definitelyAssignedByFinallyClause;
        boolean possiblyAssignedByFinallyClause;
        if (that.getFinallyClause()!=null) {
            d = beginDeclarationScope();
            as = beginSpecificationScope();
            that.getFinallyClause().visit(this);
            definitelyAssignedByFinallyClause = specified.definitely || specified.exited;
            possiblyAssignedByFinallyClause = specified.possibly;
            endDeclarationScope(d);
            endSpecificationScope(as);
        }
        else {
            definitelyAssignedByFinallyClause = false;
            possiblyAssignedByFinallyClause = false;
        }
        specified.possibly = specified.possibly || possiblyAssignedByFinallyClause;
        specified.definitely = specified.definitely || definitelyAssignedByFinallyClause
                || (definitelyAssignedByTryClause && definitelyAssignedByEveryCatchClause);
        
        checkDeclarationSection(that);
    }
    
    @Override
    public void visit(Tree.SwitchStatement that) {
        if (that.getSwitchClause()!=null) {
            that.getSwitchClause().visit(this);
        }
        boolean definitelyAssignedByEveryCaseClause = true;
        boolean possiblyAssignedBySomeCaseClause = false;
        
        for (Tree.CaseClause cc: that.getSwitchCaseList().getCaseClauses()) {
            boolean d = beginDeclarationScope();
            SpecificationState as = beginSpecificationScope();
            cc.visit(this);
            definitelyAssignedByEveryCaseClause = definitelyAssignedByEveryCaseClause && (specified.definitely || specified.exited);
            possiblyAssignedBySomeCaseClause = possiblyAssignedBySomeCaseClause || specified.possibly;
            endDeclarationScope(d);
            endSpecificationScope(as);
        }
        
        if (that.getSwitchCaseList().getElseClause()!=null) {
            boolean d = beginDeclarationScope();
            SpecificationState as = beginSpecificationScope();
            that.getSwitchCaseList().getElseClause().visit(this);
            definitelyAssignedByEveryCaseClause = definitelyAssignedByEveryCaseClause && (specified.definitely || specified.exited);
            possiblyAssignedBySomeCaseClause = possiblyAssignedBySomeCaseClause || specified.possibly;
            endDeclarationScope(d);
            endSpecificationScope(as);
        }

        specified.possibly = specified.possibly || possiblyAssignedBySomeCaseClause;
        specified.definitely = specified.definitely || definitelyAssignedByEveryCaseClause;
        
        checkDeclarationSection(that);
    }
    
    @Override
    public void visit(Tree.WhileStatement that) {
    	if (that.getWhileClause()!=null) {
    		if (that.getWhileClause().getConditionList()!=null) {
    			that.getWhileClause().getConditionList().visit(this);
    		}
    	}
    	
        boolean d = beginDeclarationScope();
        SpecificationState as = beginSpecificationScope();
        if (isVariable() || isLate()) {
            that.getWhileClause().visit(this);
        }
        else {
            boolean c = beginDisabledSpecificationScope();
            that.getWhileClause().visit(this);
            endDisabledSpecificationScope(c);
        }
        boolean possiblyAssignedByWhileClause = specified.possibly;
        
        endDeclarationScope(d);
        endSpecificationScope(as);
        
        specified.possibly = specified.possibly || possiblyAssignedByWhileClause;
        
        checkDeclarationSection(that);
    }
    
    /*@Override
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
    }*/

    @Override
    public void visit(Tree.ForStatement that) {
        boolean d = beginDeclarationScope();
        SpecificationState as = beginSpecificationScope();
        if (that.getForClause()!=null) {
            if (isVariable() || isLate()) {
                that.getForClause().visit(this);
            }
            else {
                boolean c = beginDisabledSpecificationScope();
                that.getForClause().visit(this);
                endDisabledSpecificationScope(c);
            }
        }
        boolean possiblyExitedFromForClause = specified.exited;
        boolean possiblyAssignedByForClause = specified.possibly;

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
        
        specified.definitely = specified.definitely || (!possiblyExitedFromForClause && definitelyAssignedByElseClause);
        specified.possibly = specified.possibly || possiblyAssignedByForClause || possiblyAssignedByElseClause;
        
        checkDeclarationSection(that);
    }
      
}
