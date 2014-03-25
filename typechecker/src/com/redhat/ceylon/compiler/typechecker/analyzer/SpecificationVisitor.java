package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getLastExecutableStatement;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.isAlwaysSatisfied;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.isNeverSatisfied;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
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
    private boolean withinDeclaration = true;
    private boolean inLoop = false;
    private boolean declared = false;
    private boolean hasParameter = false;
    private Tree.Statement lastExecutableStatement;
    private boolean declarationSection = false;
    private boolean endsInBreakReturnThrow = false;
    private boolean inExtends = false;
    private boolean inAnonFunctionOrComprehension = false;
    
    @Override
    public void visit(Tree.ExtendedType that) {
        boolean oie = inExtends;
        inExtends = declared;
        super.visit(that);
        inExtends = oie;
    }
    
    private final class ContinueVisitor extends Visitor {
        Tree.Continue node;
        boolean found;
        Tree.Statement lastContinue;
        ContinueVisitor(Tree.Statement lastContinue) {
           this.lastContinue = lastContinue;
           node = null;
           found = false;
        }
        @Override
        public void visit(Tree.Declaration that) {}
        @Override
        public void visit(Tree.WhileStatement that) {}
        @Override
        public void visit(Tree.ForStatement that) {}
        @Override
        public void visit(Tree.Continue that) {
            node = that;
            if (that==lastContinue) {
                found = true;
            }
        }
    }

    class SpecificationState {
        boolean definitely;
        boolean possibly;
        boolean exited;
        boolean byExits;
        SpecificationState(boolean definitely, boolean possibly) {
            this.definitely = definitely;
            this.possibly = possibly;
            this.exited = false;
            this.byExits = true;
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
        boolean ca = withinDeclaration;
        withinDeclaration = true;
        return ca;
    }
    
    private void endDisabledSpecificationScope(boolean ca) {
        withinDeclaration = ca;
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
    public void visit(Tree.MetaLiteral that) {
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
        Declaration member;
        boolean assigned;
        boolean metamodel;
        if (that instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression) that;
            member = mte.getDeclaration();
            assigned = mte.getAssigned();
            metamodel = false;
        }
        else if (that instanceof Tree.MetaLiteral) {
            Tree.MetaLiteral ml = (Tree.MetaLiteral) that;
            member = ml.getDeclaration();
            assigned = false;
            metamodel = true;
        }
        else {
            return;
        }
        //Declaration member = getDeclaration(that.getScope(), that.getUnit(), id, context);
        //TODO: check superclass members are not in declaration section!
        if ( member==declaration && 
                member.isDefinedInScope(that.getScope()) ) {
            if (!declared) {
                //you are allowed to refer to later 
                //declarations in a class declaration
                //section or interface
                if (!metamodel && !isForwardReferenceable() && !hasParameter) {
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
                else if (!declaration.isNative() && !metamodel) {
                    if (!isLate() || !isForwardReferenceable()) {
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
            if (!assigned && member.isDefault() && 
                    !isForwardReferenceable()) {
                that.addError("default member may not be used in initializer: " + 
                        member.getName());                    
            }
            if (inAnonFunctionOrComprehension && 
                specified.definitely && 
                isVariable()) {
                that.addError("variable member may not be captured by comprehension or function in extends clause: "+
                        member.getName());
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
    public void visit(Tree.Comprehension that) {
        boolean oicoaf = inAnonFunctionOrComprehension;
        inAnonFunctionOrComprehension = declared&&inExtends;
        super.visit(that);
        inAnonFunctionOrComprehension = oicoaf;
    }
    
    @Override
    public void visit(Tree.FunctionArgument that) {
    	boolean c = beginDisabledSpecificationScope();
        boolean oicoaf = inAnonFunctionOrComprehension;
        inAnonFunctionOrComprehension = declared&&inExtends;
    	SpecificationState ss = beginSpecificationScope();
    	super.visit(that);
    	endSpecificationScope(ss);
        inAnonFunctionOrComprehension = oicoaf;
        endDisabledSpecificationScope(c);
    }
    
    @Override
    public void visit(Tree.AssignOp that) {
        Tree.Term lt = that.getLeftTerm();
        assign(lt);
        if (isEffectivelyBaseMemberExpression(lt)) {
            Tree.StaticMemberOrTypeExpression m = 
                    (Tree.StaticMemberOrTypeExpression) lt;
//            Declaration member = getTypedDeclaration(m.getScope(), 
//                    name(m.getIdentifier()), null, false, m.getUnit());
            Declaration member = m.getDeclaration();
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
        //TODO: sometimes we get a dupe error b/w here and
        //      ExpressionVisitor.checkAssignable()
        if (isEffectivelyBaseMemberExpression(term)) {
            Tree.StaticMemberOrTypeExpression m = 
                    (Tree.StaticMemberOrTypeExpression) term;
//            Declaration member = getTypedDeclaration(m.getScope(), 
//                    name(m.getIdentifier()), null, false, m.getUnit());
            Declaration member = m.getDeclaration();
            if (member==declaration) {
            	if ((declaration.isFormal()||declaration.isDefault()) && 
            	        !isForwardReferenceable()) {
	        		node.addError("member is formal and may not be assigned: " +
	        				member.getName());
            	}
            	else if (!isVariable() && !isLate()) {
                    if (node instanceof Tree.AssignOp) {
                        node.addError("cannot assign non-variable value here: " +
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

    private boolean isEffectivelyBaseMemberExpression(Tree.Term term) {
        return term instanceof Tree.BaseMemberExpression ||
                term instanceof Tree.QualifiedMemberExpression &&
                isSelfReference(((Tree.QualifiedMemberExpression)term).getPrimary());
    }
    
    private Tree.Continue lastContinue;
    private Tree.Statement lastContinueStatement;
    
    @Override
    public void visit(Tree.Block that) {
        boolean oe = endsInBreakReturnThrow;
        Tree.Continue olc = lastContinue;
        Tree.Statement olcs = lastContinueStatement;
        //rather nasty way of detecting that the continue
        //occurs in another conditional branch of the
        //statement containing this block, even though we
        //did not find it in _this_ branch
        boolean continueInSomeBranchOfCurrentConditional = 
                lastContinue!=null && 
                lastContinueStatement==null;
        boolean blockEndsInBreakReturnThrow = blockEndsInBreakReturnThrow(that);
        endsInBreakReturnThrow = endsInBreakReturnThrow || 
                blockEndsInBreakReturnThrow;
        Tree.Continue last = null;
        Tree.Statement lastStatement = null;
        for (Tree.Statement st: that.getStatements()) {
            ContinueVisitor cv = new ContinueVisitor(olc);
            st.visit(cv);
            if (cv.node!=null) {
                last = cv.node;
                lastStatement = st;
            }
            if (cv.found) {
                olc = null;
                olcs = null;
            }
        }
        if (blockEndsInBreakReturnThrow || 
                continueInSomeBranchOfCurrentConditional) {
            lastContinue = last;
            lastContinueStatement = lastStatement;
        }
        super.visit(that);
        endsInBreakReturnThrow = oe;
        lastContinue = olc;
        lastContinueStatement = olcs;
    }

    private boolean blockEndsInBreakReturnThrow(Tree.Block that) {
        int size = that.getStatements().size();
        if (size>0) {
            Tree.Statement s = that.getStatements().get(size-1);
            return s instanceof Tree.Break ||
                    s instanceof Tree.Return ||
                    s instanceof Tree.Throw;
        }
        else {
            return false;
        }
    }
    
    @Override
    public void visit(Tree.ForClause that) {
        boolean oe = endsInBreakReturnThrow;
        Tree.Continue olc = lastContinue;
        lastContinue = null;
        endsInBreakReturnThrow = false;
        super.visit(that);
        endsInBreakReturnThrow = oe;
        lastContinue = olc;
    }
    
    @Override
    public void visit(Tree.WhileClause that) {
        boolean oe = endsInBreakReturnThrow;
        Tree.Continue olc = lastContinue;
        lastContinue = null;
        endsInBreakReturnThrow = false;
        super.visit(that);
        endsInBreakReturnThrow = oe;
        lastContinue = olc;
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
        Tree.Term m = that.getBaseMemberExpression();
        while (m instanceof Tree.ParameterizedExpression) {
        	m = ((Tree.ParameterizedExpression) m).getPrimary();
        }
        assign(m);
        if (m instanceof Tree.BaseMemberExpression) {
	        BaseMemberExpression bme = (Tree.BaseMemberExpression) m;
//            Declaration member = getTypedDeclaration(bme.getScope(), 
//                    name(bme.getIdentifier()), null, false, bme.getUnit());
	        Declaration member = bme.getDeclaration();
	        if (member==declaration) {
	        	if ((declaration.isFormal()||declaration.isDefault()) && 
	        	        !isForwardReferenceable()) {
	        		that.addError("member is formal and may not be specified: " +
	        				member.getName());
	        	}
                boolean lazy = that.getSpecifierExpression() 
                		instanceof Tree.LazySpecifierExpression;
            	if (declaration instanceof Value) {
            		Value value = (Value) declaration;
            	    if (!value.isVariable() && lazy!=value.isTransient()) {
	            		// check that all assignments to a non-variable, in
            	    	// different paths of execution, all use the same
            	    	// kind of specifier, all =>, or all =
            	    	// TODO: sometimes this error appears only because 
            	    	//       of a later line which illegally reassigns
	            		that.addError("value must be specified using => lazy specifier: " +
	            		        member.getName());
            	    }
            	    if (value.isVariable() && lazy) {
            	    	that.addError("variable value may not be specified using => lazy specifier: " +
            	    			member.getName());
            	    }
            	}
	            if (!lazy) {
	            	that.getSpecifierExpression().visit(this);
	            }
	            boolean constant = !isVariable() && !isLate();
				if (!declared && constant) {
                    that.addError("value is not yet declared: " + 
                            member.getName());
	            }
	            else if (inLoop && constant && 
	                    !(endsInBreakReturnThrow && lastContinue==null)) {
	            	if (specified.definitely) {
	            		that.addError("value is not variable and is aready definitely specified: " + 
	            				member.getName(), 803);
	            	}
	            	else {
	            		that.addError("value is not variable and is not definitely unspecified in loop: " + 
	            				member.getName(), 803);
	            	}
	            }
                else if (withinDeclaration && constant) {
                    that.addError("cannot specify value being declared: " + 
                            member.getName(), 803);
                }
	            else if (specified.possibly && constant) {
	            	if (specified.definitely) {
	            		that.addError("value is not variable and is aready definitely specified: " + 
	            				member.getName(), 803);
	            	}
	            	else {
	            		that.addError("value is not variable and is not definitely unspecified: " + 
	            				member.getName(), 803);
	            	}
	            }
	            else {
	                specify();
	                m.visit(this);
	            }
	            if (lazy) {
	                that.getSpecifierExpression().visit(this);
	            }
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
        boolean oe = endsInBreakReturnThrow;
        Tree.Continue olc = lastContinue;
        lastContinue = null;
        endsInBreakReturnThrow = false;
        if (that.getDeclarationModel()==declaration) {
            inLoop = false;
            beginDisabledSpecificationScope();
            declare();
            super.visit(that);
            endDisabledSpecificationScope(false);
            inLoop = false;
        }
        else {
            boolean l = inLoop;
            inLoop = false;
            boolean c = beginDisabledSpecificationScope();
            boolean d = beginDeclarationScope();
            SpecificationState as = beginSpecificationScope();
            super.visit(that);
            endDisabledSpecificationScope(c);
            endDeclarationScope(d);
            endSpecificationScope(as);
            inLoop = l;
        }
        endsInBreakReturnThrow = oe;
        lastContinue = olc;
    }

    @Override
    public void visit(Tree.TypedArgument that) {
        if (that.getDeclarationModel()==declaration) {
            inLoop = false;
            beginDisabledSpecificationScope();
            super.visit(that);
            declare();
            endDisabledSpecificationScope(false);
            inLoop = false;
        }
        else {
            boolean l = inLoop;
            inLoop = false;
            boolean c = beginDisabledSpecificationScope();
            boolean d = beginDeclarationScope();
            SpecificationState as = beginSpecificationScope();
            super.visit(that);
            endDisabledSpecificationScope(c);
            endDeclarationScope(d);
            endSpecificationScope(as);
            inLoop = l;
        }
    }
    
    @Override
    public void visit(Tree.MethodDeclaration that) {
        if (that.getDeclarationModel()==declaration) {
            if (that.getSpecifierExpression()!=null) {
                specify();
                super.visit(that);
            }
            else {
                super.visit(that);
            	if (declaration.isToplevel() && 
	                    !declaration.isNative()) {
	                that.addError("toplevel function must be specified: " +
	                        declaration.getName());
	            }
	            else if (declaration.isClassMember() && 
	                    !declaration.isNative() && 
	                    !declaration.isFormal() && 
	                    that.getDeclarationModel().getInitializerParameter()==null &&
	                    declarationSection) {
	                that.addError("forward declaration may not occur in declaration section: " +
	                            declaration.getName(), 1450);
	            }
	            else if (declaration.isInterfaceMember() && 
	                    !declaration.isNative() &&
	            		!declaration.isFormal()) {
	                that.addError("interface method must be formal or specified: " +
	                        declaration.getName(), 1400);
	            }
            }
        }
        else {
            super.visit(that);
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
        if (that.getParameterModel().getModel()==declaration) {
            specify();
        }
    }
    
    @Override
    public void visit(Tree.InitializerParameter that) {
        super.visit(that);
        Parameter d = that.getParameterModel();
        Declaration a = that.getScope().getDirectMember(d.getName(), null, false);
        if (a!=null && a==declaration) {
            specify();
            hasParameter = true;
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
        if (that.getDeclarationModel()==declaration) {
        	Tree.SpecifierOrInitializerExpression sie = that.getSpecifierOrInitializerExpression();
            if (sie!=null) {
                super.visit(that);
                specify();
            }
            else {
            	super.visit(that);
            	if (declaration.isToplevel() && 
	                    !declaration.isNative() &&
	                    !isLate()) {
	                if (isVariable()) {
	                    that.addError("toplevel variable value must be initialized: " +
	                            declaration.getName());
	                }
	                else {
	                    that.addError("toplevel value must be specified: " +
	                            declaration.getName());
	                }
	            }
	            else if (declaration.isClassOrInterfaceMember() && 
	                    !declaration.isNative() &&
	                    !declaration.isFormal() &&
	                    that.getDeclarationModel().getInitializerParameter()==null &&
	                    !that.getDeclarationModel().isLate() &&
	                    declarationSection) {
	                that.addError("forward declaration may not occur in declaration section: " +
	                            declaration.getName(), 1450);
	            }
            }
        }
        else {
        	super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        if (that.getDeclarationModel()==declaration) {
            declare();
            super.visit(that);        
            specify();
        }
        else {
            super.visit(that);        
        }
    }
    
    @Override
    public void visit(Tree.AttributeSetterDefinition that) {
        if (that.getDeclarationModel()==declaration ||
            that.getDeclarationModel().getParameter().getModel()==declaration) {
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
        if (!withinDeclaration) {
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
    public void visit(Tree.Assertion that) {
        super.visit(that);
        if (isNeverSatisfied(that.getConditionList())) {
            exit();
        }
    }
    
    @Override
    public void visit(Tree.Break that) {
        super.visit(that);
        exit();
        if (!specified.definitely) {
            specified.byExits = false;
        }
    }

    @Override
    public void visit(Tree.Continue that) {
        super.visit(that);
        exit();
        if (lastContinue==that) {
            lastContinue=null;
        }
    }
    
    @Override
    public void visit(Tree.IfStatement that) {        
        if (that==lastContinueStatement) {
            lastContinueStatement=null;
        }
        
        Tree.IfClause ifClause = that.getIfClause();
        Tree.ConditionList cl = ifClause==null ? null : ifClause.getConditionList();
        if (cl!=null) {
            cl.visit(this);
        }
        
        boolean d = beginDeclarationScope();
        SpecificationState as = beginSpecificationScope();
        if (ifClause!=null) {
            ifClause.visit(this);
        }
        boolean definitelyAssignedByIfClause = specified.definitely || specified.exited;
        boolean possiblyAssignedByIfClause = specified.possibly;
        boolean possiblyExitedFromIfClause = specified.exited;
        boolean specifiedByExitsFromIfClause = specified.byExits;
        endDeclarationScope(d);
        endSpecificationScope(as);
        
        boolean definitelyAssignedByElseClause;
        boolean possiblyAssignedByElseClause;
        boolean possiblyExitedFromElseClause;
        boolean specifiedByExitsFromElseClause;
        if (that.getElseClause()!=null) {
            d = beginDeclarationScope();
            as = beginSpecificationScope();
            that.getElseClause().visit(this);
            definitelyAssignedByElseClause = specified.definitely || specified.exited;
            possiblyAssignedByElseClause = specified.possibly;
            possiblyExitedFromElseClause = specified.exited;
            specifiedByExitsFromElseClause = specified.byExits;
            endDeclarationScope(d);
            endSpecificationScope(as);
        }
        else {
            definitelyAssignedByElseClause = false;
            possiblyAssignedByElseClause = false;
            possiblyExitedFromElseClause = false;
            specifiedByExitsFromElseClause = true;
        }
        
        if (isAlwaysSatisfied(cl)) {
            specified.definitely = specified.definitely || definitelyAssignedByIfClause;
            specified.possibly = specified.possibly || possiblyAssignedByIfClause;
            specified.exited = specified.exited || possiblyExitedFromIfClause;
            specified.byExits = specified.byExits && specifiedByExitsFromIfClause;
        } 
        else if (isNeverSatisfied(cl)) {
            specified.definitely = specified.definitely || definitelyAssignedByElseClause;
            specified.possibly = specified.possibly || possiblyAssignedByElseClause;
            specified.exited = specified.exited || possiblyExitedFromElseClause;
            specified.byExits = specified.byExits && specifiedByExitsFromElseClause;
        }
        else {
            specified.definitely = specified.definitely || (definitelyAssignedByIfClause && definitelyAssignedByElseClause);
            specified.possibly = specified.possibly || possiblyAssignedByIfClause || possiblyAssignedByElseClause;
            specified.exited = specified.exited || possiblyExitedFromIfClause || possiblyExitedFromElseClause;
            specified.byExits = specified.byExits && specifiedByExitsFromIfClause && specifiedByExitsFromElseClause;
        }
        
        checkDeclarationSection(that);
    }
    
    @Override
    public void visit(Tree.TryCatchStatement that) {
        if (that==lastContinueStatement) {
            lastContinueStatement=null;
        }
        
        boolean d = beginDeclarationScope();
        SpecificationState as = beginSpecificationScope();
        if( that.getTryClause()!=null ) {
            that.getTryClause().visit(this);
        }
        boolean definitelyAssignedByTryClause = specified.definitely || specified.exited;
        boolean possiblyAssignedByTryClause = specified.possibly;
        boolean possiblyExitedFromTryClause = specified.exited;
        boolean specifiedByExitsFromTryClause = specified.byExits;
        endDeclarationScope(d);
        endSpecificationScope(as);
        specified.possibly = specified.possibly || possiblyAssignedByTryClause;
        specified.exited = specified.exited || possiblyExitedFromTryClause;
        
        boolean definitelyAssignedByEveryCatchClause = true;
        boolean possiblyAssignedBySomeCatchClause = false;
        boolean possiblyExitedFromSomeCatchClause = false;
        boolean specifiedByExitsFromEveryCatchClause = true;
        for (Tree.CatchClause cc: that.getCatchClauses()) {
            d = beginDeclarationScope();
            as = beginSpecificationScope();
            cc.visit(this);
            definitelyAssignedByEveryCatchClause = definitelyAssignedByEveryCatchClause && (specified.definitely || specified.exited);
            possiblyAssignedBySomeCatchClause = possiblyAssignedBySomeCatchClause || specified.possibly;
            possiblyExitedFromSomeCatchClause = possiblyExitedFromSomeCatchClause || specified.exited;
            specifiedByExitsFromEveryCatchClause = specifiedByExitsFromEveryCatchClause && specified.byExits;
            endDeclarationScope(d);
            endSpecificationScope(as);
        }
        specified.possibly = specified.possibly || possiblyAssignedBySomeCatchClause;
        specified.exited = specified.exited || possiblyExitedFromSomeCatchClause;
        
        boolean definitelyAssignedByFinallyClause;
        boolean possiblyAssignedByFinallyClause;
        boolean possiblyExitedFromFinallyClause;
        boolean specifiedByExitsFromFinallyClause;
        if (that.getFinallyClause()!=null) {
            d = beginDeclarationScope();
            as = beginSpecificationScope();
            that.getFinallyClause().visit(this);
            definitelyAssignedByFinallyClause = specified.definitely || specified.exited;
            possiblyAssignedByFinallyClause = specified.possibly;
            possiblyExitedFromFinallyClause = specified.exited;
            specifiedByExitsFromFinallyClause = specified.byExits;
            endDeclarationScope(d);
            endSpecificationScope(as);
        }
        else {
            definitelyAssignedByFinallyClause = false;
            possiblyAssignedByFinallyClause = false;
            possiblyExitedFromFinallyClause = false;
            specifiedByExitsFromFinallyClause = true;
        }
        specified.possibly = specified.possibly || possiblyAssignedByFinallyClause;
        specified.definitely = specified.definitely || definitelyAssignedByFinallyClause
                || (definitelyAssignedByTryClause && definitelyAssignedByEveryCatchClause);
        specified.exited = specified.exited || possiblyExitedFromFinallyClause;
        specified.byExits = specified.byExits ||  specifiedByExitsFromFinallyClause || 
                (specifiedByExitsFromEveryCatchClause && specifiedByExitsFromTryClause);
        
        checkDeclarationSection(that);
    }
    
    @Override
    public void visit(Tree.SwitchStatement that) {
        if (that==lastContinueStatement) {
            lastContinueStatement=null;
        }
        
        if (that.getSwitchClause()!=null) {
            that.getSwitchClause().visit(this);
        }
        boolean definitelyAssignedByEveryCaseClause = true;
        boolean possiblyAssignedBySomeCaseClause = false;
        boolean possiblyExitedFromSomeCaseClause = false;
        boolean specifiedByExitsFromEveryCaseClause = true; 
        
        for (Tree.CaseClause cc: that.getSwitchCaseList().getCaseClauses()) {
            boolean d = beginDeclarationScope();
            SpecificationState as = beginSpecificationScope();
            cc.visit(this);
            definitelyAssignedByEveryCaseClause = definitelyAssignedByEveryCaseClause && (specified.definitely || specified.exited);
            possiblyAssignedBySomeCaseClause = possiblyAssignedBySomeCaseClause || specified.possibly;
            possiblyExitedFromSomeCaseClause = possiblyExitedFromSomeCaseClause || specified.exited;
            specifiedByExitsFromEveryCaseClause = specifiedByExitsFromEveryCaseClause && specified.byExits;
            endDeclarationScope(d);
            endSpecificationScope(as);
        }
        
        if (that.getSwitchCaseList().getElseClause()!=null) {
            boolean d = beginDeclarationScope();
            SpecificationState as = beginSpecificationScope();
            that.getSwitchCaseList().getElseClause().visit(this);
            definitelyAssignedByEveryCaseClause = definitelyAssignedByEveryCaseClause && (specified.definitely || specified.exited);
            possiblyAssignedBySomeCaseClause = possiblyAssignedBySomeCaseClause || specified.possibly;
            possiblyExitedFromSomeCaseClause = possiblyExitedFromSomeCaseClause || specified.exited;
            specifiedByExitsFromEveryCaseClause = specifiedByExitsFromEveryCaseClause && specified.byExits;
            endDeclarationScope(d);
            endSpecificationScope(as);
        }

        specified.possibly = specified.possibly || possiblyAssignedBySomeCaseClause;
        specified.definitely = specified.definitely || definitelyAssignedByEveryCaseClause;
        specified.exited = specified.exited || possiblyExitedFromSomeCaseClause;
        specified.byExits = specified.byExits && specifiedByExitsFromEveryCaseClause;
        
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
            boolean c = inLoop;
            inLoop = true;
            that.getWhileClause().visit(this);
            inLoop = c;
        }
        boolean possiblyAssignedByWhileClause = specified.possibly;
        boolean definitelyAssignedByWhileClause = specified.definitely;
        
        endDeclarationScope(d);
        endSpecificationScope(as);
        
        specified.definitely = specified.definitely || (definitelyAssignedByWhileClause && 
                 isAlwaysSatisfied(that.getWhileClause().getConditionList()));
        specified.possibly = specified.possibly || (possiblyAssignedByWhileClause && 
                 !isNeverSatisfied(that.getWhileClause().getConditionList()));
        
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
                boolean c = inLoop;
                inLoop = true;
                that.getForClause().visit(this);
                inLoop = c;
            }
        }
        boolean possiblyExitedFromForClause = specified.exited && !specified.byExits;
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
