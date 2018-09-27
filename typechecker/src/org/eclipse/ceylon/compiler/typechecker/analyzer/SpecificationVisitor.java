/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.analyzer;

import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getLastConstructor;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getLastExecutableStatement;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getLastStatic;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isAlwaysSatisfied;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isAtLeastOne;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isNeverSatisfied;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.message;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.isEffectivelyBaseMemberExpression;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.isSelfReference;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getContainingClassOrInterface;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isNativeHeader;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.Term;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Value;

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
    
    private boolean specificationDisabled = false;
    private boolean withinDeclaration = false;
    private int loopDepth = 0;
    private int brokenLoopDepth = 0;
    private boolean allOuterLoopsBreak = true;
    private boolean declared = false;
    private boolean hasParameter = false;
    private Tree.Statement lastExecutableStatement;
    private Tree.Declaration lastConstructor;
    private boolean declarationSection = false;
    private boolean endsInReturnThrow = false;
    private boolean endsInBreak = false;
    private boolean inExtends = false;
    private boolean inParameter = false;
    private boolean inDelegatedContructor = false;
    private boolean inLazyExpression = false;
    private Parameter parameter = null;
    private boolean usedInDeclarationSection = false;
    private boolean definedInDeclarationSection = false;
    private boolean hasNonStatic = false; 
    
    private boolean definitely = false;
    private boolean possibly = false;
    private boolean possiblyExited = false;
    private boolean definitelyExited = false;
    private boolean definitelyByLoopBreaks = true;
    private boolean possiblyByLoopBreaks = false;
    private boolean possiblyBreaks = false;
    
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
    
    public SpecificationVisitor(Declaration declaration) {
        this.declaration = declaration;
    }
    
    private void declare() {
        declared = true;
    }
    
    private void specify() {
        definitely = true;
        possibly = true;
    }
    
    private void exit() {
        possiblyExited = true;
        definitelyExited = true;
    }
    
    private void beginSpecificationScope() {
        possiblyExited = false;
        definitelyExited = false;
        definitelyByLoopBreaks = true;
        possiblyByLoopBreaks = false;
        possiblyBreaks = false;
    }
    
    private boolean isVariable() {
        if (declaration instanceof TypedDeclaration) {
            TypedDeclaration td = 
                    (TypedDeclaration) 
                        declaration;
            return td.isVariable();
        }
        else {
            return false;
        }
    }
    
    private boolean isLate() {
        if (declaration instanceof FunctionOrValue) {
            FunctionOrValue fov = 
                    (FunctionOrValue) 
                        declaration;
            return fov.isLate();
        }
        else {
            return false;
        }
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
    public void visit(Tree.TypeConstraint that) {
        //the SatisfiedTypes are just upper bounds
    }
    
    @Override
    public void visit(Tree.SatisfiedTypes that) {
        for (Tree.Type type: that.getTypes()) {
            if (type instanceof Tree.SimpleType) {
                Tree.SimpleType st = (Tree.SimpleType) type;
                checkReference(type, st.getDeclarationModel(), 
                        false, false);
            }
        }
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
        if (that.getStaticMethodReference()) {
            visitReference(that);
        }
    }

    @Override
    public void visit(Tree.QualifiedTypeExpression that) {
        super.visit(that);
        if (isSelfReference(that.getPrimary())) {
            visitReference(that);
        }
        if (that.getStaticMethodReference()) {
            visitReference(that);
        }
    }
    
    private String name() {
        String name = declaration.getName();
        return name==null ? 
                "default constructor" : 
                "'" + name + "'";
    }

    private void visitReference(Tree.Primary that) {
        Declaration member;
        boolean assigned;
        boolean metamodel;
        if (that instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) that;
            if (that instanceof Tree.BaseTypeExpression || 
                    that instanceof Tree.QualifiedTypeExpression) {
                if (mte.getStaticMethodReferencePrimary()
                        && !hasConstructors(mte.getDeclaration())) {
                    return;
                }
            }
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

        checkReference(that, member, assigned, metamodel);
    }

    private void checkReference(Node that, Declaration member, 
            boolean assigned, boolean metamodel) {
        Scope scope = that.getScope();
        if ((member==declaration 
                || isDelegationToDefaultConstructor(member)) 
                && declaration.isDefinedInScope(scope) 
                //TODO: THIS IS TERRIBLE!!!!!
                && !isReferenceToNativeHeaderMember(scope)) {
            if (!declared) {
                if (!metamodel && 
                        !isForwardReferenceable() && 
                        !hasParameter) {
                    Scope container = 
                            declaration.getContainer();
                    if (container instanceof Class) {
                        that.addError("forward reference to class member in initializer: " + 
                                name() + 
                                " is not yet declared (forward references must occur in declaration section)");
                    }
                    else {
                        that.addError("forward reference to local declaration: " + 
                                name() + 
                                " is not yet declared");
                    }
                }
            }
            else if (!definitely 
                    || declaration.isFormal()) {
                //you are allowed to refer to formal
                //declarations in a class declaration
                //section or interface
                if (declaration.isFormal()) {
                    if (!isForwardReferenceable()) {
                        that.addError("formal member may not be used in initializer: " + 
                                name() +
                                " is declared 'formal'");                    
                    }
                }
                else if (!metamodel 
                        && !isNativeHeader(declaration) 
                        && !isLate()) {
                    String message = 
                            "not definitely "
                            + (isVariable() ? 
                                    "initialized" : 
                                    "specified")
                            + ": " + name() 
                            + " has not been assigned "
                            + (declaration instanceof Value ? 
                                    "a value" : 
                                    "a definition")
                            + " by every conditional branch";
                    that.addError(message);
                }
            }
            else if (parameter!=null) {
                Declaration paramDec =
                        parameter.getDeclaration();
                if (paramDec.isConstructor() 
                        && !declaration.isStatic() 
                        && paramDec.getContainer()
                            .equals(declaration.getContainer())) {
                  that.addError("default argument to constructor parameter is a member of the constructed class");
               }
            }
            if (!assigned 
                    && declaration.isDefault() 
                    && !isForwardReferenceable()) {
                that.addError("default member may not be used in initializer: " + 
                        name() +
                        " is declared 'default'"); 
            }
            if (definitely 
                    && isVariable() 
                    && inLazyExpression) {
                if (inParameter) {
                    Declaration paramDec =
                            parameter.getDeclaration();
                    if (paramDec.equals(declaration.getContainer())) {
                        that.addError("value may not be captured by lazy expression in default argument: " +
                                name() +
                                " is declared 'variable'");
                    }
                }
                if (inExtends) {
                    if (declaration.isClassOrInterfaceMember()
                            && getContainingClassOrInterface(scope)
                                .equals(declaration.getContainer())) {
                        that.addError("value may not be captured by lazy expression in extends clause: " +
                                name() +
                                " is declared 'variable'");
                    }
                }
            }
        }
    }

    private static boolean hasConstructors(Declaration td) {
        return td instanceof Class 
            && ((Class) td).hasConstructors();
    }

    private boolean isDelegationToDefaultConstructor(Declaration member) {
        return inDelegatedContructor 
            && member instanceof Class 
            && declaration == ((Class) member).getDefaultConstructor();
    }
    
    private boolean isReferenceToNativeHeaderMember(Scope scope) {
        if (declaration.isClassOrInterfaceMember()) {
            ClassOrInterface container = 
                    (ClassOrInterface) 
                        declaration.getContainer();
            return container.isNativeHeader() 
                && !scope.getScopedBackends().none();
        }
        else {
            return false;
        }
    }

    private boolean isForwardReferenceable() {
        // we are permitted to refer to a later 
        // declaration:
        // - in a class declaration section or
        //   interface, 
        // - if it is toplevel
        // - if it is a constructor, and we're
        //   not in an extends clause
        
        return declarationSection 
            || declaration.isToplevel()
            || !inDelegatedContructor 
                && declaration.isConstructor();
    }
    
    @Override
    public void visit(Tree.LogicalOp that) {
        that.getLeftTerm().visit(this);
        boolean odefinitely = definitely;
        boolean opossibly = possibly;
        boolean opossiblyExited = possiblyExited;
        boolean odefinitelyExited = definitelyExited;
        boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
        boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
        beginSpecificationScope();
        that.getRightTerm().visit(this);
        definitely = odefinitely;
        possibly = opossibly;
        possiblyExited = opossiblyExited;
        definitelyExited = odefinitelyExited;
        definitelyByLoopBreaks = odefinitelyByLoopBreaks;
        possiblyByLoopBreaks = opossiblyByLoopBreaks;
    }
    
    @Override
    public void visit(Tree.IfExpression that) {
        //TODO: reproduce the logic for IfStatement!
        Tree.IfClause ifClause = that.getIfClause();
        if (ifClause!=null) {
            boolean odefinitely = definitely;
            boolean opossibly = possibly;
            boolean opossiblyExited = possiblyExited;
            boolean odefinitelyExited = definitelyExited;
            boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
            boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
            beginSpecificationScope();
            ifClause.visit(this);
            definitely = odefinitely;
            possibly = opossibly;
            possiblyExited = opossiblyExited;
            definitelyExited = odefinitelyExited;
            definitelyByLoopBreaks = odefinitelyByLoopBreaks;
            possiblyByLoopBreaks = opossiblyByLoopBreaks;
        }
        Tree.ElseClause elseClause = that.getElseClause();
        if (elseClause!=null) {
            boolean odefinitely = definitely;
            boolean opossibly = possibly;
            boolean opossiblyExited = possiblyExited;
            boolean odefinitelyExited = definitelyExited;
            boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
            boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
            beginSpecificationScope();
            elseClause.visit(this);
            definitely = odefinitely;
            possibly = opossibly;
            possiblyExited = opossiblyExited;
            definitelyExited = odefinitelyExited;
            definitelyByLoopBreaks = odefinitelyByLoopBreaks;
            possiblyByLoopBreaks = opossiblyByLoopBreaks;
        }
    }
    
    @Override
    public void visit(Tree.SwitchExpression that) {
      //TODO: reproduce the logic for SwitchStatement!
        Tree.SwitchClause switchClause = 
                that.getSwitchClause();
        if (switchClause!=null) {
            switchClause.visit(this);
        }
        Tree.SwitchCaseList switchCaseList = 
                that.getSwitchCaseList();
        if (switchCaseList!=null) {
            for (Tree.CaseClause caseClause: 
                    switchCaseList.getCaseClauses()) {
                boolean odefinitely = definitely;
                boolean opossibly = possibly;
                boolean opossiblyExited = possiblyExited;
                boolean odefinitelyExited = definitelyExited;
                boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
                boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
                beginSpecificationScope();
                caseClause.visit(this);
                definitely = odefinitely;
                possibly = opossibly;
                possiblyExited = opossiblyExited;
                definitelyExited = odefinitelyExited;
                definitelyByLoopBreaks = odefinitelyByLoopBreaks;
                possiblyByLoopBreaks = opossiblyByLoopBreaks;
            }
            Tree.ElseClause elseClause = 
                    switchCaseList.getElseClause();
            if (elseClause!=null) {
                boolean odefinitely = definitely;
                boolean opossibly = possibly;
                boolean opossiblyExited = possiblyExited;
                boolean odefinitelyExited = definitelyExited;
                boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
                boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
                beginSpecificationScope();
                elseClause.visit(this);
                definitely = odefinitely;
                possibly = opossibly;
                possiblyExited = opossiblyExited;
                definitelyExited = odefinitelyExited;
                definitelyByLoopBreaks = odefinitelyByLoopBreaks;
                possiblyByLoopBreaks = opossiblyByLoopBreaks;
            }
        }
    }
    
    @Override
    public void visit(Tree.SequenceEnumeration that) {
        boolean odefinitely = definitely;
        boolean oile = inLazyExpression;
        inLazyExpression = declared&&(inExtends||inParameter);
        super.visit(that);
        definitely = odefinitely;
        inLazyExpression = oile;
    }
    
    @Override
    public void visit(Tree.NamedArgumentList that) {
        for (Tree.NamedArgument na: that.getNamedArguments()) {
            na.visit(this);
        }
        Tree.SequencedArgument sa = that.getSequencedArgument();
        if (sa!=null) {
            boolean odefinitely = definitely;
            boolean oile = inLazyExpression;
            inLazyExpression = declared&&(inExtends||inParameter);
            sa.visit(this);
            definitely = odefinitely;
            inLazyExpression = oile;
        }
    }
    
    @Override
    public void visit(Tree.Comprehension that) {
        boolean odefinitely = definitely;
        super.visit(that);
        definitely = odefinitely;
    }
    
    @Override
    public void visit(Tree.LazySpecifierExpression that) {
        boolean oile = inLazyExpression;
        inLazyExpression = declared&&(inExtends||inParameter);
        super.visit(that);
        inLazyExpression = oile;
    }
    
    @Override
    public void visit(Tree.FunctionArgument that) {
        boolean c = specificationDisabled;
        specificationDisabled = true;
        boolean oile = inLazyExpression;
        inLazyExpression = declared&&(inExtends||inParameter);
        boolean odefinitely = definitely;
        boolean opossibly = possibly;
        boolean opossiblyExited = possiblyExited;
        boolean odefinitelyExited = definitelyExited;
        boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
        boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
        beginSpecificationScope();
        super.visit(that);
        definitely = odefinitely;
        possibly = opossibly;
        possiblyExited = opossiblyExited;
        definitelyExited = odefinitelyExited;
        definitelyByLoopBreaks = odefinitelyByLoopBreaks;
        possiblyByLoopBreaks = opossiblyByLoopBreaks;
        inLazyExpression = oile;
        specificationDisabled = c;
    }
    
    @Override
    public void visit(Tree.ObjectExpression that) {
        boolean c = specificationDisabled;
        specificationDisabled = true;
        boolean oile = inLazyExpression;
        inLazyExpression = declared&&(inExtends||inParameter);
        boolean odefinitely = definitely;
        boolean opossibly = possibly;
        boolean opossiblyExited = possiblyExited;
        boolean odefinitelyExited = definitelyExited;
        boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
        boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
        beginSpecificationScope();
        super.visit(that);
        definitely = odefinitely;
        possibly = opossibly;
        possiblyExited = opossiblyExited;
        definitelyExited = odefinitelyExited;
        definitelyByLoopBreaks = odefinitelyByLoopBreaks;
        possiblyByLoopBreaks = opossiblyByLoopBreaks;
        inLazyExpression = oile;
        specificationDisabled = c;
    }
    
    @Override
    public void visit(Tree.AssignOp that) {
        Tree.Term lt = that.getLeftTerm();
        if (lt instanceof Tree.IndexExpression) {
            Tree.IndexExpression ie =
                    (Tree.IndexExpression) lt;
            Tree.Term p = ie.getPrimary();
            if (p!=null) {
                p.visit(this);
            }
            ie.getElementOrRange().visit(this);
        }
        else if (isEffectivelyBaseMemberExpression(lt)) {
            Tree.StaticMemberOrTypeExpression me = 
                    (Tree.StaticMemberOrTypeExpression) lt;
            Declaration member = me.getDeclaration();
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
        super.visit(that);
        checkVariable(that.getLeftTerm(), that);
    }

    @Override
    public void visit(Tree.PostfixOperatorExpression that) {
        super.visit(that);
        checkVariable(that.getTerm(), that);
    }
    
    @Override
    public void visit(Tree.PrefixOperatorExpression that) {
        super.visit(that);
        checkVariable(that.getTerm(), that);
    }
    
    private void checkVariable(Tree.Term term, Node node) {
        if (isEffectivelyBaseMemberExpression(term)) {  //Note: other cases handled in ExpressionVisitor
            Tree.StaticMemberOrTypeExpression mte = 
                    (Tree.StaticMemberOrTypeExpression) term;
            Declaration member = mte.getDeclaration();
            if (member==declaration) {
                boolean isFormal = declaration.isFormal();
                boolean isDefault = declaration.isDefault();
                if ((isFormal || isDefault) && 
                         !isForwardReferenceable()) {
                    term.addError("member may not be assigned here: " +
                            name() + " is declared '" +
                            (isFormal ? "formal" : "default") + 
                            "'");
                }
                else if (!(member instanceof Value)) {
                    term.addError("not a variable value: " +
                            name());
                }
                else if (node instanceof Tree.AssignOp) {
                    if (!isVariable() && !isLate()) {
                        term.addError("value is already assigned: " +
                                name() + " is neither 'variable' nor 'late'", 
                                800);
                    }
                }
                else {
                    if (!isVariable()) {
                        term.addError("value may not be mutated: " +
                                name() + " is not 'variable'",
                                800);
                    }
                }
            }
        }
    }

    private Tree.Continue lastContinue;
    private Tree.Statement lastContinueStatement;
    
    @Override
    public void visit(Tree.Block that) {
        Scope scope = that.getScope();
        if (scope instanceof Constructor) {
            if (definitelyInitedBy.contains(delegatedConstructor)) {
                definitely = true;
            }
            if (possiblyInitedBy.contains(delegatedConstructor)) {
                possibly = true;
            }
            delegatedConstructor = null;
        }
        
        boolean of = endsInBreak;
        boolean oe = endsInReturnThrow;
        Tree.Continue olc = lastContinue;
        Tree.Statement olcs = lastContinueStatement;
        //rather nasty way of detecting that the continue
        //occurs in another conditional branch of the
        //statement containing this block, even though we
        //did not find it in _this_ branch
        boolean continueInSomeBranchOfCurrentConditional = 
                lastContinue!=null && 
                lastContinueStatement==null;
        boolean blockEndsInReturnThrow = 
                blockEndsInReturnThrow(that);
        boolean blockEndsInBreak = 
                blockEndsInBreak(that);
        endsInBreak = endsInBreak || 
                blockEndsInBreak;
        endsInReturnThrow = endsInReturnThrow || 
                blockEndsInReturnThrow;
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
        if (blockEndsInReturnThrow || blockEndsInBreak ||
                continueInSomeBranchOfCurrentConditional) {
            lastContinue = last;
            lastContinueStatement = lastStatement;
        }
        super.visit(that);
        endsInBreak = of;
        endsInReturnThrow = oe;
        lastContinue = olc;
        lastContinueStatement = olcs;
        
        if (scope instanceof Constructor) {
            Constructor c = (Constructor) scope;
            if (definitely) {
                definitelyInitedBy.add(c);
            }
            if (possibly) {
                possiblyInitedBy.add(c);
            }
        }
        if (isNonPartialConstructor(scope) &&
                declaration.getContainer()==scope.getContainer()) {
            if (!definitely) {
                initedByEveryConstructor = false;
            }
        }
    }
    
    @Override 
    public void visit(Tree.DelegatedConstructor that) {
        boolean odc = inDelegatedContructor;
        inDelegatedContructor = true;
        super.visit(that);
        inDelegatedContructor = odc;
                
        Tree.SimpleType type = that.getType();
        if (type!=null) {
            delegatedConstructor = 
                    type.getDeclarationModel();
            if (delegatedConstructor instanceof Class) {
                //this case is not actually legal
                Class c = (Class) delegatedConstructor;
                delegatedConstructor = 
                        c.getDefaultConstructor();
            }
        }
    }
    
    private TypeDeclaration delegatedConstructor;
    
    private List<Constructor> definitelyInitedBy = 
            new ArrayList<Constructor>();
    private List<Constructor> possiblyInitedBy = 
            new ArrayList<Constructor>();
    
    private boolean initedByEveryConstructor = true;

    /*private boolean blockEndsInBreak(Tree.Block that) {
        if (that==null) {
            return false;
        }
        int size = that.getStatements().size();
        if (size>0) {
            Tree.Statement s = 
                    that.getStatements()
                        .get(size-1);
            if (s instanceof Tree.IfStatement) {
                Tree.IfStatement is = (Tree.IfStatement) s;
                Tree.IfClause ic = is.getIfClause();
                Tree.ElseClause ec = is.getElseClause();
                if (ic!=null) {
                    Tree.ConditionList cl = 
                            ic.getConditionList();
                    if (cl!=null) {
                        if (isAlwaysSatisfied(cl)) {
                            return blockEndsInBreak(
                                    ic.getBlock());
                        }
                        else if (ec!=null && isNeverSatisfied(cl)) {
                            return blockEndsInBreak(
                                    ec.getBlock());
                        }
                    }
                    if (ec!=null) {
                        return blockEndsInBreak(ic.getBlock())
                                && blockEndsInBreak(ec.getBlock());
                    }
                }
            }
            else if (s instanceof Tree.SwitchStatement) {
                Tree.SwitchStatement ss = (Tree.SwitchStatement) s;
                Tree.SwitchCaseList scl = ss.getSwitchCaseList();
                for (Tree.CaseClause cc: scl.getCaseClauses()){
                    if (!blockEndsInBreak(cc.getBlock())) {
                        return false;
                    }
                }
                Tree.ElseClause ec = scl.getElseClause();
                if (ec!=null) {
                    return blockEndsInBreak(ec.getBlock());
                }
            }
            else if (s instanceof Tree.ForStatement) {
                Tree.ForStatement fs = (Tree.ForStatement) s;
                Tree.ForClause fc = fs.getForClause();
                Tree.ElseClause ec = fs.getElseClause();
                if (fc!=null) {
                    if (isAtLeastOne(fc)) {
                        return blockEndsInBreak(fc.getBlock());
                    }
                    if (ec!=null) {
                        return blockEndsInBreak(fc.getBlock())
                                && blockEndsInBreak(ec.getBlock());
                    }
                }
            }
            return s instanceof Tree.Break;
        }
        else {
            return false;
        }
    }*/
    
    private boolean blockEndsInBreak(Tree.Block that) {
        return blockEndsInReturnThrowBreak(that) 
            && !blockEndsInReturnThrow(that);
    }
    
    private boolean blockEndsInReturnThrow(Tree.Block that) {
        if (that==null) {
            return false;
        }
        int size = that.getStatements().size();
        if (size>0) {
            Tree.Statement s = 
                    that.getStatements()
                        .get(size-1);
            if (s instanceof Tree.IfStatement) {
                Tree.IfStatement is = (Tree.IfStatement) s;
                Tree.IfClause ic = is.getIfClause();
                Tree.ElseClause ec = is.getElseClause();
                if (ic!=null) {
                    Tree.ConditionList cl = 
                            ic.getConditionList();
                    if (cl!=null) {
                        if (isAlwaysSatisfied(cl)) {
                            return blockEndsInReturnThrow(
                                    ic.getBlock());
                        }
                        else if (ec!=null && isNeverSatisfied(cl)) {
                            return blockEndsInReturnThrow(
                                    ec.getBlock());
                        }
                    }
                    if (ec!=null) {
                        return blockEndsInReturnThrow(ic.getBlock())
                            && blockEndsInReturnThrow(ec.getBlock());
                    }
                }
            }
            else if (s instanceof Tree.SwitchStatement) {
                Tree.SwitchStatement ss = (Tree.SwitchStatement) s;
                Tree.SwitchCaseList scl = ss.getSwitchCaseList();
                for (Tree.CaseClause cc: scl.getCaseClauses()){
                    if (!blockEndsInReturnThrow(cc.getBlock())) {
                        return false;
                    }
                }
                Tree.ElseClause ec = scl.getElseClause();
                if (ec!=null) {
                    return blockEndsInReturnThrow(ec.getBlock());
                }
            }
            else if (s instanceof Tree.ForStatement) {
                Tree.ForStatement fs = (Tree.ForStatement) s;
                Tree.ForClause fc = fs.getForClause();
                Tree.ElseClause ec = fs.getElseClause();
                if (fc!=null) {
                    if (isAtLeastOne(fc)) {
                        return blockEndsInReturnThrow(fc.getBlock());
                    }
                    if (ec!=null) {
                        return blockEndsInReturnThrow(fc.getBlock())
                            && blockEndsInReturnThrow(ec.getBlock());
                    }
                }
            }
            return s instanceof Tree.Return 
                || s instanceof Tree.Throw;
        }
        else {
            return false;
        }
    }
    
    private boolean blockEndsInReturnThrowBreak(Tree.Block that) {
        if (that==null) {
            return false;
        }
        int size = that.getStatements().size();
        if (size>0) {
            Tree.Statement s = 
                    that.getStatements()
                        .get(size-1);
            if (s instanceof Tree.IfStatement) {
                Tree.IfStatement is = (Tree.IfStatement) s;
                Tree.IfClause ic = is.getIfClause();
                Tree.ElseClause ec = is.getElseClause();
                if (ic!=null) {
                    Tree.ConditionList cl = 
                            ic.getConditionList();
                    if (cl!=null) {
                        if (isAlwaysSatisfied(cl)) {
                            return blockEndsInReturnThrowBreak(
                                    ic.getBlock());
                        }
                        else if (ec!=null && isNeverSatisfied(cl)) {
                            return blockEndsInReturnThrowBreak(
                                    ec.getBlock());
                        }
                    }
                    if (ec!=null) {
                        return blockEndsInReturnThrowBreak(ic.getBlock())
                            && blockEndsInReturnThrowBreak(ec.getBlock());
                    }
                }
            }
            else if (s instanceof Tree.SwitchStatement) {
                Tree.SwitchStatement ss = (Tree.SwitchStatement) s;
                Tree.SwitchCaseList scl = ss.getSwitchCaseList();
                for (Tree.CaseClause cc: scl.getCaseClauses()){
                    if (!blockEndsInReturnThrowBreak(cc.getBlock())) {
                        return false;
                    }
                }
                Tree.ElseClause ec = scl.getElseClause();
                if (ec!=null) {
                    return blockEndsInReturnThrowBreak(ec.getBlock());
                }
            }
            else if (s instanceof Tree.ForStatement) {
                Tree.ForStatement fs = (Tree.ForStatement) s;
                Tree.ForClause fc = fs.getForClause();
                Tree.ElseClause ec = fs.getElseClause();
                if (fc!=null) {
                    if (isAtLeastOne(fc)) {
                        return blockEndsInReturnThrowBreak(fc.getBlock());
                    }
                    if (ec!=null) {
                        return blockEndsInReturnThrowBreak(fc.getBlock())
                            && blockEndsInReturnThrowBreak(ec.getBlock());
                    }
                }
            }
            return s instanceof Tree.Return 
                || s instanceof Tree.Throw 
                || s instanceof Tree.Break;
        }
        else {
            return false;
        }
    }
    
    @Override
    public void visit(Tree.ForClause that) {
        boolean of = endsInBreak;
        boolean oe = endsInReturnThrow;
        boolean ob = possiblyBreaks;
        Tree.Continue olc = lastContinue;
        lastContinue = null;
        endsInBreak = false;
        endsInReturnThrow = false;
        possiblyBreaks = false;
        super.visit(that);
        endsInBreak = of;
        endsInReturnThrow = oe;
        possiblyBreaks = ob;
        lastContinue = olc;
    }
    
    @Override
    public void visit(Tree.WhileClause that) {
        boolean of = endsInBreak;
        boolean oe = endsInReturnThrow;
        boolean ob = possiblyBreaks;
        Tree.Continue olc = lastContinue;
        lastContinue = null;
        endsInBreak = false;
        endsInReturnThrow = false;
        possiblyBreaks = false;
        super.visit(that);
        endsInBreak = of;
        endsInReturnThrow = oe;
        possiblyBreaks = ob;
        lastContinue = olc;
    }
    
    @Override
    public void visit(Tree.Body that) {
        if (hasParameter &&
                that.getScope()==declaration.getContainer()) {
            hasParameter = false;
        }
        super.visit(that);
    }

    private static boolean isNonPartialConstructor(Scope scope) {
        if (scope instanceof Constructor) {
            Constructor constructor = (Constructor) scope;
            return !constructor.isAbstract();
        }
        else {
            return false;
        }
    }
    
    private String longdesc() {
        if (declaration instanceof Value) {
            return "value is neither variable nor late and";
        }
        else if (declaration instanceof Function) {
            return "function";
        }
        else {
            return "declaration";
        }
    }
    
    private String shortdesc() {
        if (declaration instanceof Value) {
            return "value";
        }
        else if (declaration instanceof Function) {
            return "function";
        }
        else {
            return "declaration";
        }
    }
    
    @Override
    public void visit(Tree.SpecifierStatement that) {
        Term lhs = that.getBaseMemberExpression();
        Tree.Term term = lhs;
        boolean parameterized = false;
        while (term instanceof Tree.ParameterizedExpression) {
            Tree.ParameterizedExpression pe = 
                    (Tree.ParameterizedExpression) term;
            term = pe.getPrimary();
            parameterized = true;
        }
        if (term instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression bme = 
                    (Tree.StaticMemberOrTypeExpression) 
                        term;
            Declaration member = bme.getDeclaration();
            if (member==declaration) {
                if (!isForwardReferenceable() 
                        && !lhs.hasErrors()) {
                    if (declaration.isFormal()) {
                        bme.addError("member is formal and may not be specified: " +
                                name() + " is declared 'formal'");
                    }
                    else if (declaration.isDefault()) {
                        bme.addError("member is default and may not be specified except in its declaration: " +
                                name() + " is declared 'default'");
                    }
                }
                if (that.getRefinement()) {
                    declare();
                }
                Tree.SpecifierExpression se = 
                        that.getSpecifierExpression();
                boolean lazy = se instanceof 
                        Tree.LazySpecifierExpression;
                checkSpecifiedValue(se);
                if (!lazy || !parameterized) {
                    se.visit(this);
                }
                
                if (that.getRefinement()) {
                    specify();
                    term.visit(this);
                }
                else {
                    specification(that, bme);
                }
                if (lazy && parameterized) {
                    se.visit(this);
                }
                checkDeclarationSection(that);
            }
            else {
                super.visit(that);
            }
        }
        else {
            super.visit(that);
        }
    }
    
    private void checkSpecifiedValue(Tree.SpecifierExpression se) {
        if (declaration instanceof Value) {
            boolean lazy = se instanceof 
                    Tree.LazySpecifierExpression;
            Value value = (Value) declaration;
            if (!value.isVariable() 
                    && lazy!=value.isTransient()) {
                // check that all assignments to a non-variable, in
                // different paths of execution, all use the same
                // kind of specifier, all =>, or all =
                // TODO: sometimes this error appears only because 
                //       of a later line which illegally reassigns
                se.addError("value must be specified using => lazy specifier: " +
                        name());
            }
            if (lazy) {
                if (value.isVariable()) {
                    se.addError("variable value may not be specified using => lazy specifier: " +
                            name());
                }
                else if (value.isLate()) {
                    se.addError("late reference may not be specified using => lazy specifier: " +
                            name());
                }
            }
        }
    }
    
    private void specification(Tree.SpecifierStatement that, 
            Tree.StaticMemberOrTypeExpression bme) {
        boolean constant = !isVariable() && !isLate();
        Scope scope = that.getScope();
        if (constant 
                && (!declaration.isDefinedInScope(scope) 
                    || declaration instanceof FunctionOrValue 
                        && ((FunctionOrValue) declaration)
                            .isShortcutRefinement())) {
            //this error is added by ExpressionVisitor
//          that.addError("inherited member is not variable and may not be specified here: " + 
//                  name());
        }
        else if (!declared && constant) {
            bme.addError(shortdesc() + 
                    " is not yet declared: " + name());
        }
        else if (loopDepth>0 
                && constant  
                && !(endsInReturnThrow 
                        && lastContinue==null 
                    || endsInBreak 
                        && allOuterLoopsBreak 
                        && lastContinue==null)) {
            if (definitely) {
                bme.addError(longdesc() + 
                        " is aready definitely specified: " + 
                        name(), 
                        803);
            }
            else {
                bme.addError(longdesc() + 
                        " is not definitely unspecified in loop: " + 
                        name(), 
                        803);
                specify(); //to eliminate dupe error
            }
        }
        else if (specificationDisabled && constant) {
            if (withinDeclaration) {
                bme.addError("cannot specify " + 
                        shortdesc() + 
                        " from within its own body: " + 
                        name());
            }
            else {
                bme.addError("cannot specify " + 
                        shortdesc() + 
                        " declared in outer scope: " + 
                        name(), 
                        803);
            }
        }
        else if (possibly && constant) {
            if (definitely) {
                bme.addError(longdesc() + 
                        " is aready definitely specified: " + 
                        name(),
                        803);
            }
            else {
                bme.addError(longdesc() + 
                        " is not definitely unspecified: " + 
                        name(),
                        803);
                specify(); //to eliminate dupe error
            }
        }
        else {
            specify();
            bme.visit(this);
        }
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        boolean oe = endsInReturnThrow;
        boolean of = endsInBreak;
        Tree.Continue olc = lastContinue;
        lastContinue = null;
        endsInReturnThrow = false;
        endsInBreak = false;
        if (isSameDeclaration(that)) {
            loopDepth = 0;
            brokenLoopDepth = 0;
            specificationDisabled = true;
            withinDeclaration = true;
            declare();
            super.visit(that);
            withinDeclaration = false;
            specificationDisabled = false;
            loopDepth = 0;
            brokenLoopDepth = 0;
        }
        else {
            int l = loopDepth;
            int bl = brokenLoopDepth;
            loopDepth = 0;
            brokenLoopDepth = 0;
            Scope scope = that.getScope();
            boolean constructor = 
                    scope instanceof Constructor;
            boolean valueWithInitializer =
                    scope instanceof Value 
                    && !((Value) scope).isTransient();
            boolean c = false;
            if (!constructor) {
                c = specificationDisabled;
                specificationDisabled = true;
            }
            boolean d = declared;
            if (valueWithInitializer) {
                super.visit(that);
            }
            else {
                boolean odefinitely = definitely;
                boolean opossibly = possibly;
                boolean opossiblyExited = possiblyExited;
                boolean odefinitelyExited = definitelyExited;
                boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
                boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
                beginSpecificationScope();
                super.visit(that);
                definitely = odefinitely;
                possibly = opossibly;
                possiblyExited = opossiblyExited;
                definitelyExited = odefinitelyExited;
                definitelyByLoopBreaks = odefinitelyByLoopBreaks;
                possiblyByLoopBreaks = opossiblyByLoopBreaks;
            }
            declared = d;
            if (!constructor) {
                specificationDisabled = c;
            }
            loopDepth = l;
            brokenLoopDepth = bl;
        }
        endsInReturnThrow = oe;
        endsInBreak = of;
        lastContinue = olc;
    }

    private boolean isSameDeclaration(Tree.Declaration that) {
        Declaration dec = that.getDeclarationModel();
        if (dec instanceof Class 
                && dec.isAbstraction()) {
            return dec==declaration
                || dec.getOverloads().contains(declaration);
        }
        else {
            return dec==declaration;
        }
    }
    
    private boolean isSameDeclaration(Tree.TypedArgument that) {
        return that.getDeclarationModel()==declaration;
    }
    
    @Override
    public void visit(Tree.Constructor that) {
        Function f = that.getDeclarationModel();
        Constructor c = that.getConstructor();
        if (f==declaration || c==declaration) {
            declare();
            specify();
        }
        super.visit(that);
        if (declaration.getContainer()==c.getContainer() 
                && that==lastConstructor 
                && initedByEveryConstructor) {
            definitely = true;
        }
    }

    @Override
    public void visit(Tree.Enumerated that) {
        Value v = that.getDeclarationModel();
        Constructor e = that.getEnumerated();
        if (v==declaration || e==declaration) {
            declare();
            specify();
        }
        super.visit(that);
        if (declaration.getContainer()==e.getContainer() 
                && that==lastConstructor 
                && initedByEveryConstructor) {
            definitely = true;
        }
    }

    @Override
    public void visit(Tree.TypedArgument that) {
        boolean oile = inLazyExpression;
        inLazyExpression = declared 
                && (inExtends || inParameter);
        if (isSameDeclaration(that)) {
            loopDepth = 0;
            brokenLoopDepth = 0;
            specificationDisabled = true;
            withinDeclaration = true;
            super.visit(that);
            declare();
            withinDeclaration = false;
            specificationDisabled = false;
            loopDepth = 0;
            brokenLoopDepth = 0;
        }
        else {
            int l = loopDepth;
            int bl = brokenLoopDepth;
            loopDepth = 0;
            brokenLoopDepth = 0;
            boolean c = specificationDisabled;
            specificationDisabled = true;
            boolean d = declared;
            boolean odefinitely = definitely;
            boolean opossibly = possibly;
            boolean opossiblyExited = possiblyExited;
            boolean odefinitelyExited = definitelyExited;
            boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
            boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
            beginSpecificationScope();
            super.visit(that);
            specificationDisabled = c;
            declared = d;
            definitely = odefinitely;
            possibly = opossibly;
            possiblyExited = opossiblyExited;
            definitelyExited = odefinitelyExited;
            definitelyByLoopBreaks = odefinitelyByLoopBreaks;
            possiblyByLoopBreaks = opossiblyByLoopBreaks;
            loopDepth = l;
            brokenLoopDepth = bl;
        }
        inLazyExpression = oile;
    }
    
    @Override
    public void visit(Tree.MethodDeclaration that) {
        if (isSameDeclaration(that)) {
            if (that.getSpecifierExpression()!=null) {
                specify();
                super.visit(that);
            }
            else {
                super.visit(that);
                if (declaration.isToplevel() 
                        && !isNativeHeader(declaration) 
                        && !declaration.isJavaNative()) {
                    that.addError("toplevel function must be specified: " +
                            name() + 
                            " may not be forward declared");
                }
                else if (declaration.isStatic() 
                        && !isNativeHeader(declaration)) {
                    that.addError("static function must be specified: " +
                            name() + 
                            " may not be forward declared");
                }
                else if (declaration.isClassMember() 
                        && !isNativeHeader(declaration) 
                        && !declaration.isFormal() 
                        && !declaration.isJavaNative() 
                        && that.getDeclarationModel()
                               .getInitializerParameter()
                                   ==null 
                        && declarationSection) {
                    that.addError("forward declaration may not occur in declaration section: " +
                                name(), 
                                1450);
                }
                else if (declaration.isInterfaceMember() 
                        && !isNativeHeader(declaration) 
                        && !declaration.isFormal() 
                        && !declaration.isJavaNative()) {
                    that.addError("interface method must be formal or specified: " +
                            name(), 
                            1400);
                }
            }
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.MethodDefinition that) {
        if (isSameDeclaration(that)) {
            declare();
            specify();
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.MethodArgument that) {
        if (isSameDeclaration(that)) {
            declare();
            specify();
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.Variable that) {
        super.visit(that);
        if (isSameDeclaration(that)) {
            specify();
        }
    }
    
    @Override
    public void visit(Tree.Parameter that) {
        Parameter p = that.getParameterModel();
        boolean oip = inParameter;
        inParameter = true;
        Parameter op = parameter;
        parameter = p;
        super.visit(that);
        parameter = op;
        inParameter = oip;
        if (p!=null && p.getModel()==declaration) {
            specify();
        }
    }
    
    @Override
    public void visit(Tree.InitializerParameter that) {
        super.visit(that);
        Parameter p = that.getParameterModel();
        if (p!=null) {
            Declaration a = 
                    that.getScope()
                        .getDirectMember(p.getName(), 
                                null, false);
            if (a!=null && a==declaration) {
                specify();
                hasParameter = true;
            }
        }
    }
    
    @Override
    public void visit(Tree.TypeParameterDeclaration that) {
        super.visit(that);
        if (isSameDeclaration(that)) {
            specify();
        }
    }
    
    @Override
    public void visit(Tree.AttributeDeclaration that) {
        if (isSameDeclaration(that)) {
            Tree.SpecifierOrInitializerExpression sie = 
                    that.getSpecifierOrInitializerExpression();
            if (sie!=null) {
                super.visit(that);
                specify();
            }
            else {
                super.visit(that);
                if (declaration.isToplevel() 
                        && !isNativeHeader(declaration) 
                        && !isLate()
                        && !declaration.isJavaNative()) {
                    if (isVariable()) {
                        that.addError("toplevel variable value must be initialized: " +
                                name());
                    }
                    else {
                        that.addError("toplevel value must be specified: " +
                                name());
                    }
                }
                else if (declaration.isStatic()
                        && !isNativeHeader(declaration)
                        && !isLate()) {
                    if (isVariable()) {
                        that.addError("static variable value must be initialized: " +
                                name());
                    }
                    else {
                        that.addError("static value must be specified: " +
                                name());
                    }
                }
                else if (declaration.isClassOrInterfaceMember() 
                        && !isNativeHeader(declaration) 
                        && !declaration.isFormal() 
                        && !declaration.isJavaNative() 
                        && that.getDeclarationModel()
                               .getInitializerParameter()
                                   ==null 
                        && !that.getDeclarationModel().isLate() 
                        && declarationSection) {
                    that.addError("forward declaration may not occur in declaration section: " +
                            name(), 
                            1450);
                }
            }
        }
        else {
            super.visit(that);
        }
    }
    
    @Override
    public void visit(Tree.AttributeGetterDefinition that) {
        if (isSameDeclaration(that)) {
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
        Setter d = that.getDeclarationModel();
        if (d==declaration ||
            d.getParameter().getModel()==declaration) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    @Override
    public void visit(Tree.AttributeArgument that) {
        if (isSameDeclaration(that)) {
            declare();
            specify();
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ObjectDefinition that) {
        if (isSameDeclaration(that)) {
            declare();
            specify();
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.ObjectArgument that) {
        if (isSameDeclaration(that)) {
            declare();
            specify();
        }
        super.visit(that);
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
    public void visit(Tree.InterfaceBody that) {
        if (that.getScope()==declaration.getContainer()) {
            Tree.Statement les = getLastStatic(that);
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
    public void visit(Tree.ClassBody that) {
        if (that.getScope()==declaration.getContainer()) {
            Tree.Statement les = getLastExecutableStatement(that);
            Tree.Declaration lc = getLastConstructor(that);
            declarationSection = les==null;
            lastExecutableStatement = les;
            lastConstructor = lc;
            
            new Visitor() {
                boolean declarationSection = false;
                @Override
                public void visit(Tree.ExecutableStatement that) {
                    super.visit(that);
                    if (that==lastExecutableStatement) {
                        declarationSection = true;
                    }
                }
                @Override
                public void visit(Tree.Declaration that) {
                    super.visit(that);
                    if (declarationSection 
                            && isSameDeclaration(that)) {
                        definedInDeclarationSection = true;
                    }
                    if (that==lastExecutableStatement) {
                        declarationSection = true;
                    }
                }
                @Override
                public void visit(Tree.StaticMemberOrTypeExpression that) {
                    super.visit(that);
                    if (declarationSection 
                            && declaration instanceof FunctionOrValue 
                            && that.getDeclaration()==declaration) {
                        usedInDeclarationSection = true;
                    }
                }
            }.visit(that);
            
            super.visit(that);
            declarationSection = false;
            lastExecutableStatement = null;
            lastConstructor = null;

            if (!declaration.isAnonymous()) {
                if (isSharedDeclarationUninitialized()) {
                    Node d = getDeclaration(that);
                    if (d==null) d = that;
                    d.addError("must be definitely specified by class initializer: " 
                                + message(declaration) + explanation(), 
                                1401);
                }
            }
        }
        else {
            super.visit(that);
        }
    }

    private String explanation() {
        return declaration.isShared() ? 
                " is shared" : 
                " is captured";
    }
    
    @Override
    public void visit(Tree.Statement that) {
        if (that instanceof Tree.TypeParameterDeclaration 
         || that instanceof Tree.TypeConstraint) {
            //ignore
        }
        else if (that instanceof Tree.Declaration) {
            Tree.Declaration dec = (Tree.Declaration) that;
            Declaration model = dec.getDeclarationModel();
            if (model.isStatic()) {
                if (hasNonStatic && model==declaration) {
                    that.addError("static member must occur before all non-static members and initializer statements");
                }
            }
            else {
                hasNonStatic = true;
            }
        }
        else if (that instanceof Tree.ExecutableStatement) {
            hasNonStatic = true;
        }
        boolean ohs = hasNonStatic;
        hasNonStatic = false;
        super.visit(that);
        hasNonStatic = ohs;
        checkDeclarationSection(that);
    }

    private void checkDeclarationSection(Tree.Statement that) {
        declarationSection = 
                declarationSection 
                || that==lastExecutableStatement;
    }
    
    @Override
    public void visit(Tree.ClassOrInterface that) {
        if (isSameDeclaration(that)) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    @Override
    public void visit(Tree.TypeAliasDeclaration that) {
        if (isSameDeclaration(that)) {
            declare();
            specify();
        }
        super.visit(that);        
    }
    
    public void visit(Tree.Return that) {
        super.visit(that);
        if (!specificationDisabled 
                && isSharedDeclarationUninitialized()) {
            that.addError("must be definitely specified by class initializer: " +
                    message(declaration) + explanation());
        }
        else if (that.getDeclaration()==declaration.getContainer() 
                && isCapturedDeclarationUninitialized()) {
            that.addError("must be definitely specified by class initializer: " +
                    message(declaration) + explanation());
        }
        exit();
    }

    private boolean isSharedDeclarationUninitialized() {
        return (declaration.isShared() 
                || declaration.getOtherInstanceAccess()) 
            && !declaration.isFormal() 
            && !declaration.isJavaNative() 
            && !isNativeHeader(declaration) 
            && !isLate() 
            && !definitely;
    }
    
    private boolean isCapturedDeclarationUninitialized() {
        return (declaration.isShared() 
                || declaration.getOtherInstanceAccess() 
                || usedInDeclarationSection) 
            && !definedInDeclarationSection 
            && !declaration.isFormal() 
            && !isNativeHeader(declaration) 
            && !isLate() 
            && !definitely;
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
        if (!definitely) {
            definitelyByLoopBreaks = false;
        }
        if (possibly) {
            possiblyByLoopBreaks = true;
        }
        possiblyBreaks = true;
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
        Tree.ConditionList conditionList = 
                ifClause.getConditionList();
        if (ifClause!=null && conditionList!=null) {
            conditionList.visit(this);
        }
        
        boolean d = declared;
        boolean odefinitely = definitely;
        boolean opossibly = possibly;
        boolean opossiblyExited = possiblyExited;
        boolean odefinitelyExited = definitelyExited;
        boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
        boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
        beginSpecificationScope();
        if (ifClause!=null) {
            Tree.Block block = ifClause.getBlock();
            if (block!=null) {
                block.visit(this);
            }
        }
        boolean definitelyAssignedByIfClause = 
                definitely 
                || definitelyExited;
        boolean possiblyAssignedByIfClause = 
                possibly;
        boolean possiblyExitedFromIfClause = 
                possiblyExited;
        boolean definitelyExitedFromIfClause = 
                definitelyExited;
        boolean definitelySpecifiedByExitsFromIfClause = 
                definitelyByLoopBreaks;
        boolean possiblySpecifiedByExitsFromIfClause = 
                possiblyByLoopBreaks;
        declared = d;
        definitely = odefinitely;
        possibly = opossibly;
        possiblyExited = opossiblyExited;
        definitelyExited = odefinitelyExited;
        definitelyByLoopBreaks = odefinitelyByLoopBreaks;
        possiblyByLoopBreaks = opossiblyByLoopBreaks;
        
        boolean definitelyAssignedByElseClause;
        boolean possiblyAssignedByElseClause;
        boolean definitelyExitedFromElseClause;
        boolean possiblyExitedFromElseClause;
        boolean definitelySpecifiedByExitsFromElseClause;
        boolean possiblySpecifiedByExitsFromElseClause;
        Tree.ElseClause elseClause = that.getElseClause();
        if (elseClause!=null) {
            d = declared;
            boolean pdefinitely = definitely;
            boolean ppossibly = possibly;
            boolean ppossiblyExited = possiblyExited;
            boolean pdefinitelyExited = definitelyExited;
            boolean pdefinitelyByLoopBreaks = definitelyByLoopBreaks;
            boolean ppossiblyByLoopBreaks = possiblyByLoopBreaks;
            beginSpecificationScope();
            elseClause.visit(this);
            definitelyAssignedByElseClause = 
                    definitely 
                    || definitelyExited;
            possiblyAssignedByElseClause = 
                    possibly;
            definitelyExitedFromElseClause =
                    definitelyExited;
            possiblyExitedFromElseClause = 
                    possiblyExited;
            definitelySpecifiedByExitsFromElseClause = 
                    definitelyByLoopBreaks;
            possiblySpecifiedByExitsFromElseClause = 
                    possiblyByLoopBreaks;
            declared = d;
            definitely = pdefinitely;
            possibly = ppossibly;
            possiblyExited = ppossiblyExited;
            definitelyExited = pdefinitelyExited;
            definitelyByLoopBreaks = pdefinitelyByLoopBreaks;
            possiblyByLoopBreaks = ppossiblyByLoopBreaks;
        }
        else {
            definitelyAssignedByElseClause = false;
            possiblyAssignedByElseClause = false;
            definitelyExitedFromElseClause = false;
            possiblyExitedFromElseClause = false;
            definitelySpecifiedByExitsFromElseClause = true;
            possiblySpecifiedByExitsFromElseClause = false;
        }
        
        if (isAlwaysSatisfied(conditionList)) {
            definitely = definitely 
                    || definitelyAssignedByIfClause;
            possibly = possibly 
                    || possiblyAssignedByIfClause 
                    && !definitelyExitedFromIfClause;
            definitelyExited = definitelyExited 
                    || definitelyExitedFromIfClause;
            possiblyExited = possiblyExited 
                    || possiblyExitedFromIfClause;
            definitelyByLoopBreaks = definitelyByLoopBreaks 
                    && definitelySpecifiedByExitsFromIfClause;
            possiblyByLoopBreaks = possiblyByLoopBreaks 
                    || possiblySpecifiedByExitsFromIfClause;
        } 
        else if (isNeverSatisfied(conditionList)) {
            definitely = definitely 
                    || definitelyAssignedByElseClause;
            possibly = possibly 
                    || possiblyAssignedByElseClause 
                    && !definitelyExitedFromElseClause;
            definitelyExited = definitelyExited 
                    || definitelyExitedFromElseClause;
            possiblyExited = possiblyExited 
                    || possiblyExitedFromElseClause;
            definitelyByLoopBreaks = definitelyByLoopBreaks 
                    && definitelySpecifiedByExitsFromElseClause;
            possiblyByLoopBreaks = possiblyByLoopBreaks 
                    || possiblySpecifiedByExitsFromElseClause;
        }
        else {
            definitely = definitely 
                    || definitelyAssignedByIfClause 
                    && definitelyAssignedByElseClause;
            possibly = possibly 
                    || possiblyAssignedByIfClause 
                    && !definitelyExitedFromIfClause 
                    || possiblyAssignedByElseClause 
                    && !definitelyExitedFromElseClause;
            definitelyExited = definitelyExited 
                    || definitelyExitedFromIfClause 
                    && definitelyExitedFromElseClause;
            possiblyExited = possiblyExited 
                    || possiblyExitedFromIfClause 
                    || possiblyExitedFromElseClause;
            definitelyByLoopBreaks = definitelyByLoopBreaks 
                    && definitelySpecifiedByExitsFromIfClause 
                    && definitelySpecifiedByExitsFromElseClause;
            possiblyByLoopBreaks = possiblyByLoopBreaks 
                    || possiblySpecifiedByExitsFromIfClause 
                    || possiblySpecifiedByExitsFromIfClause;
        }
        
        checkDeclarationSection(that);
    }
    
    @Override
    public void visit(Tree.TryCatchStatement that) {
        if (that==lastContinueStatement) {
            lastContinueStatement=null;
        }
        
        boolean d = declared;
        boolean odefinitely = definitely;
        boolean opossibly = possibly;
        boolean opossiblyExited = possiblyExited;
        boolean odefinitelyExited = definitelyExited;
        boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
        boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
        boolean opossiblyBreaks = possiblyBreaks;
        beginSpecificationScope();
        Tree.TryClause tryClause = that.getTryClause();
        if (tryClause!=null) {
            tryClause.visit(this);
        }
        boolean definitelyAssignedByTryClause = 
                definitely 
                || definitelyExited;
        boolean possiblyAssignedByTryClause = 
                possibly;
        boolean possiblyExitedFromTryClause = 
                possiblyExited;
        boolean definitelySpecifiedByExitsFromTryClause = 
                definitelyByLoopBreaks;
        boolean possiblySpecifiedByExitsFromTryClause = 
                possiblyByLoopBreaks;
        declared = d;
        definitely = odefinitely;
        possibly = opossibly;
        possiblyExited = opossiblyExited;
        definitelyExited = odefinitelyExited;
        definitelyByLoopBreaks = odefinitelyByLoopBreaks;
        possiblyByLoopBreaks = opossiblyByLoopBreaks;
        possiblyBreaks = opossiblyBreaks;
        possibly = possibly 
                || possiblyAssignedByTryClause;
        possiblyExited = possiblyExited 
                || possiblyExitedFromTryClause;
        
        boolean definitelyAssignedByEveryCatchClause = true;
        boolean possiblyAssignedBySomeCatchClause = false;
        boolean definitelyExitedFromEveryCatchClause = true;
        boolean possiblyExitedFromSomeCatchClause = false;
        boolean definitelySpecifiedByExitsFromEveryCatchClause = true;
        boolean possiblySpecifiedByExitsFromSomeCatchClause = false;
        boolean possiblyBreaksInSomeCatchClause = false;
        for (Tree.CatchClause cc: that.getCatchClauses()) {
            d = declared;
            boolean pdefinitely = definitely;
            boolean ppossibly = possibly;
            boolean ppossiblyExited = possiblyExited;
            boolean pdefinitelyExited = definitelyExited;
            boolean pdefinitelyByLoopBreaks = definitelyByLoopBreaks;
            boolean ppossiblyByLoopBreaks = possiblyByLoopBreaks;
            boolean ppossiblyBreaks = possiblyBreaks;
            beginSpecificationScope();
            cc.visit(this);
            definitelyAssignedByEveryCatchClause = 
                    definitelyAssignedByEveryCatchClause 
                    && (definitely || definitelyExited);
            possiblyAssignedBySomeCatchClause = 
                    possiblyAssignedBySomeCatchClause 
                    || possibly;
            definitelyExitedFromEveryCatchClause = 
                    definitelyExitedFromEveryCatchClause 
                    && definitelyExited;
            possiblyExitedFromSomeCatchClause = 
                    possiblyExitedFromSomeCatchClause 
                    || possiblyExited;
            definitelySpecifiedByExitsFromEveryCatchClause = 
                    definitelySpecifiedByExitsFromEveryCatchClause 
                    && definitelyByLoopBreaks;
            possiblySpecifiedByExitsFromSomeCatchClause =
                    possiblySpecifiedByExitsFromSomeCatchClause 
                    || possiblyByLoopBreaks;
            possiblyBreaksInSomeCatchClause = 
                    possiblyBreaksInSomeCatchClause
                    || possiblyBreaks;
            declared = d;
            definitely = pdefinitely;
            possibly = ppossibly;
            possiblyExited = ppossiblyExited;
            definitelyExited = pdefinitelyExited;
            definitelyByLoopBreaks = pdefinitelyByLoopBreaks;
            possiblyByLoopBreaks = ppossiblyByLoopBreaks;
            possiblyBreaks = ppossiblyBreaks;
        }
        possibly = possibly 
                || possiblyAssignedBySomeCatchClause;
        possiblyExited = possiblyExited 
                || possiblyExitedFromSomeCatchClause;
        
        boolean definitelyAssignedByFinallyClause;
        boolean possiblyAssignedByFinallyClause;
        boolean definitelyExitedFromFinallyClause;
        boolean possiblyExitedFromFinallyClause;
        boolean definitelySpecifiedByExitsFromFinallyClause;
        boolean possiblySpecifiedByExitsFromFinallyClause;
        boolean possiblyBreaksInFinallyClause;
        Tree.FinallyClause finallyClause = 
                that.getFinallyClause();
        if (finallyClause!=null) {
            d = declared;
            boolean pdefinitely = definitely;
            boolean ppossibly = possibly;
            boolean ppossiblyExited = possiblyExited;
            boolean pdefinitelyExited = definitelyExited;
            boolean pdefinitelyByLoopBreaks = definitelyByLoopBreaks;
            boolean ppossiblyByLoopBreaks = possiblyByLoopBreaks;
            boolean ppossiblyBreaks = possiblyBreaks;
            beginSpecificationScope();
            finallyClause.visit(this);
            definitelyAssignedByFinallyClause = 
                    definitely 
                    || definitelyExited;
            possiblyAssignedByFinallyClause = 
                    possibly;
            definitelyExitedFromFinallyClause = 
                    definitelyExited;
            possiblyExitedFromFinallyClause = 
                    possiblyExited;
            definitelySpecifiedByExitsFromFinallyClause = 
                    definitelyByLoopBreaks;
            possiblySpecifiedByExitsFromFinallyClause =
                    possiblyByLoopBreaks;
            possiblyBreaksInFinallyClause =
                    possiblyBreaks;
            declared = d;
            definitely = pdefinitely;
            possibly = ppossibly;
            possiblyExited = ppossiblyExited;
            definitelyExited = pdefinitelyExited;
            definitelyByLoopBreaks = pdefinitelyByLoopBreaks;
            possiblyByLoopBreaks = ppossiblyByLoopBreaks;
            possiblyBreaks = ppossiblyBreaks;
        }
        else {
            definitelyAssignedByFinallyClause = false;
            possiblyAssignedByFinallyClause = false;
            definitelyExitedFromFinallyClause = false;
            possiblyExitedFromFinallyClause = false;
            definitelySpecifiedByExitsFromFinallyClause = true;
            possiblySpecifiedByExitsFromFinallyClause = false;
            possiblyBreaksInFinallyClause = false;
        }
        possibly = possibly 
                || possiblyAssignedByFinallyClause;
        definitely = definitely 
                || definitelyAssignedByTryClause 
                && definitelyAssignedByEveryCatchClause
                && !possiblyBreaksInFinallyClause
                && !possiblyBreaksInSomeCatchClause
                || definitelyAssignedByFinallyClause
                && !possiblyBreaksInFinallyClause;
        definitelyExited = definitelyExited 
                && definitelyExitedFromFinallyClause;
        possiblyExited = possiblyExited 
                || possiblyExitedFromFinallyClause;
        definitelyByLoopBreaks = definitelyByLoopBreaks 
                && definitelySpecifiedByExitsFromEveryCatchClause 
                && definitelySpecifiedByExitsFromTryClause
                && definitelySpecifiedByExitsFromFinallyClause;
        possiblyByLoopBreaks = possiblyByLoopBreaks 
                || possiblySpecifiedByExitsFromFinallyClause 
                || possiblySpecifiedByExitsFromSomeCatchClause 
                || possiblySpecifiedByExitsFromTryClause;
        
        checkDeclarationSection(that);
    }
    
    @Override
    public void visit(Tree.SwitchStatement that) {
        if (that==lastContinueStatement) {
            lastContinueStatement=null;
        }
        
        Tree.SwitchClause switchClause = 
                that.getSwitchClause();
        if (switchClause!=null) {
            switchClause.visit(this);
        }
        boolean definitelyAssignedByEveryCaseClause = true;
        boolean possiblyAssignedBySomeCaseClause = false;
        boolean definitelyExitedFromEveryCaseClause = false;
        boolean possiblyExitedFromSomeCaseClause = false;
        boolean specifiedByExitsFromEveryCaseClause = true; 
        boolean specifiedByExitsFromSomeCaseClause = false; 
        boolean possiblyAssignedAndNotDefinitelyExitedFromSomeCaseClause = false; 
        
        Tree.SwitchCaseList switchCaseList = 
                that.getSwitchCaseList();
        for (Tree.CaseClause cc: 
                switchCaseList.getCaseClauses()) {
            boolean d = declared;
            boolean odefinitely = definitely;
            boolean opossibly = possibly;
            boolean opossiblyExited = possiblyExited;
            boolean odefinitelyExited = definitelyExited;
            boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
            boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
            beginSpecificationScope();
            cc.visit(this);
            definitelyAssignedByEveryCaseClause = 
                    definitelyAssignedByEveryCaseClause 
                    && (definitely || definitelyExited);
            possiblyAssignedBySomeCaseClause = 
                    possiblyAssignedBySomeCaseClause 
                    || possibly;
            definitelyExitedFromEveryCaseClause = 
                    definitelyExitedFromEveryCaseClause 
                    && definitelyExited;
            possiblyExitedFromSomeCaseClause = 
                    possiblyExitedFromSomeCaseClause 
                    || possiblyExited;
            specifiedByExitsFromEveryCaseClause = 
                    specifiedByExitsFromEveryCaseClause 
                    && definitelyByLoopBreaks;
            specifiedByExitsFromSomeCaseClause = 
                    specifiedByExitsFromSomeCaseClause 
                    || possiblyByLoopBreaks;
            possiblyAssignedAndNotDefinitelyExitedFromSomeCaseClause =
                    possiblyAssignedAndNotDefinitelyExitedFromSomeCaseClause 
                    || possibly && !definitelyExited;
            declared = d;
            definitely = odefinitely;
            possibly = opossibly;
            possiblyExited = opossiblyExited;
            definitelyExited = odefinitelyExited;
            definitelyByLoopBreaks = odefinitelyByLoopBreaks;
            possiblyByLoopBreaks = opossiblyByLoopBreaks;
        }
        
        Tree.ElseClause elseClause = 
                switchCaseList.getElseClause();
        if (elseClause!=null) {
            boolean d = declared;
            boolean odefinitely = definitely;
            boolean opossibly = possibly;
            boolean opossiblyExited = possiblyExited;
            boolean odefinitelyExited = definitelyExited;
            boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
            boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
            beginSpecificationScope();
            elseClause.visit(this);
            definitelyAssignedByEveryCaseClause = 
                    definitelyAssignedByEveryCaseClause 
                    && (definitely || definitelyExited);
            possiblyAssignedBySomeCaseClause = 
                    possiblyAssignedBySomeCaseClause 
                    || possibly;
            definitelyExitedFromEveryCaseClause = 
                    definitelyExitedFromEveryCaseClause 
                    && definitelyExited;
            possiblyExitedFromSomeCaseClause = 
                    possiblyExitedFromSomeCaseClause 
                    || possiblyExited;
            specifiedByExitsFromEveryCaseClause = 
                    specifiedByExitsFromEveryCaseClause 
                    && definitelyByLoopBreaks;
            specifiedByExitsFromSomeCaseClause = 
                    specifiedByExitsFromSomeCaseClause 
                    || possiblyByLoopBreaks;
            possiblyAssignedAndNotDefinitelyExitedFromSomeCaseClause =
                    possiblyAssignedAndNotDefinitelyExitedFromSomeCaseClause 
                    || possibly && !definitelyExited;
            declared = d;
            definitely = odefinitely;
            possibly = opossibly;
            possiblyExited = opossiblyExited;
            definitelyExited = odefinitelyExited;
            definitelyByLoopBreaks = odefinitelyByLoopBreaks;
            possiblyByLoopBreaks = opossiblyByLoopBreaks;
        }

        possibly = possibly 
                || possiblyAssignedAndNotDefinitelyExitedFromSomeCaseClause;
        definitely = definitely 
                || definitelyAssignedByEveryCaseClause;
        definitelyExited = definitelyExited 
                && definitelyExitedFromEveryCaseClause;
        possiblyExited = possiblyExited 
                || possiblyExitedFromSomeCaseClause;
        definitelyByLoopBreaks = definitelyByLoopBreaks 
                && specifiedByExitsFromEveryCaseClause;
        possiblyByLoopBreaks = possiblyByLoopBreaks 
                && specifiedByExitsFromSomeCaseClause;
        
        checkDeclarationSection(that);
    }
    
    @Override
    public void visit(Tree.WhileStatement that) {
        Tree.WhileClause whileClause = 
                that.getWhileClause();
        Tree.ConditionList conditionList = 
                whileClause.getConditionList();
        if (conditionList!=null) {
            conditionList.visit(this);
        }
        
        boolean d = declared;
        boolean odefinitely = definitely;
        boolean opossibly = possibly;
        boolean opossiblyExited = possiblyExited;
        boolean odefinitelyExited = definitelyExited;
        boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
        boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
        beginSpecificationScope();
        Tree.Block block = whileClause.getBlock();
        if (block!=null) {
            if (isVariable() || isLate()) {
                block.visit(this);
            }
            else {
                boolean aolb = allOuterLoopsBreak;
                allOuterLoopsBreak = 
                        loopDepth==brokenLoopDepth;
                boolean broken = blockEndsInBreak(block);
                if (broken) {
                    brokenLoopDepth++;
                }
                loopDepth++;
                block.visit(this);
                if (broken) {
                    brokenLoopDepth--;
                }
                loopDepth--;
                allOuterLoopsBreak = aolb;
            }
        }
        
        boolean definitelyAssignedByWhileClause = 
                definitely 
                || definitelyExited;
        boolean possiblyAssignedByWhileClause = 
                possibly;
        boolean definitelySpecifiedByLoopBreaks = 
                definitelyByLoopBreaks;
        boolean possiblySpecifiedByLoopBreaks = 
                possiblyByLoopBreaks;
        
        declared = d;
        definitely = odefinitely;
        possibly = opossibly;
        possiblyExited = opossiblyExited;
        definitelyExited = odefinitelyExited;
        definitelyByLoopBreaks = odefinitelyByLoopBreaks;
        possiblyByLoopBreaks = opossiblyByLoopBreaks;
        
        if (isAlwaysSatisfied(conditionList)) {
            definitely = definitely 
                    || definitelySpecifiedByLoopBreaks 
                    || definitelyAssignedByWhileClause;
            possibly = possibly 
                    || possiblyAssignedByWhileClause 
                    || possiblySpecifiedByLoopBreaks;
        }
        else if (isNeverSatisfied(conditionList)) {
            //nothing to do
        }
        else {
            possibly = possibly 
                    || possiblyAssignedByWhileClause 
                    || possiblySpecifiedByLoopBreaks;
        }
        
        checkDeclarationSection(that);
    }
    
    @Override
    public void visit(Tree.ForStatement that) {
        boolean d = declared;
        boolean odefinitely = definitely;
        boolean opossibly = possibly;
        boolean opossiblyExited = possiblyExited;
        boolean odefinitelyExited = definitelyExited;
        boolean odefinitelyByLoopBreaks = definitelyByLoopBreaks;
        boolean opossiblyByLoopBreaks = possiblyByLoopBreaks;
        beginSpecificationScope();
        boolean atLeastOneIteration = false;
        Tree.ForClause forClause = that.getForClause();
        Tree.Block block = forClause.getBlock();
        if (block!=null) {
            if (isVariable() || isLate()) {
                forClause.visit(this);
            }
            else {
                boolean aolb = allOuterLoopsBreak;
                allOuterLoopsBreak = 
                        loopDepth==brokenLoopDepth;
                boolean broken = blockEndsInBreak(block);
                if (broken) {
                    brokenLoopDepth++;
                }
                loopDepth++;
                forClause.visit(this);
                if (broken) {
                    brokenLoopDepth--;
                }
                loopDepth--;
                allOuterLoopsBreak = aolb;
            }
        }
        atLeastOneIteration = isAtLeastOne(forClause);
        boolean possiblyAssignedByForClause = 
                possibly;
        boolean definitelyAssignedByForClause = 
                definitely;
        boolean possiblyExitedFromForClause = 
                possiblyExited;
        boolean definitelyExitedFromForClause = 
                definitelyExited;
        boolean definitelySpecifiedByLoopBreaks = 
                definitelyByLoopBreaks;
        boolean possiblySpecifiedByLoopBreaks = 
                possiblyByLoopBreaks;
        
        declared = d;
        definitely = odefinitely;
        possibly = opossibly;
        possiblyExited = opossiblyExited;
        definitelyExited = odefinitelyExited;
        definitelyByLoopBreaks = odefinitelyByLoopBreaks;
        possiblyByLoopBreaks = opossiblyByLoopBreaks;

        boolean definitelyAssignedByElseClause;
        boolean possiblyAssignedByElseClause;
        boolean definitelyExitedFromElseClause;
        Tree.ElseClause elseClause = that.getElseClause();
        if (elseClause!=null) {
            d = declared;
            boolean pdefinitely = definitely;
            boolean ppossibly = possibly;
            boolean ppossiblyExited = possiblyExited;
            boolean pdefinitelyExited = definitelyExited;
            boolean pdefinitelyByLoopBreaks = definitelyByLoopBreaks;
            boolean ppossiblyByLoopBreaks = possiblyByLoopBreaks;
            beginSpecificationScope();
            elseClause.visit(this);
            definitelyAssignedByElseClause = 
                    definitely 
                    || definitelyExited;
            possiblyAssignedByElseClause = 
                    possibly;
            definitelyExitedFromElseClause =
                    definitelyExited;
            declared = d;
            definitely = pdefinitely;
            possibly = ppossibly;
            possiblyExited = ppossiblyExited;
            definitelyExited = pdefinitelyExited;
            definitelyByLoopBreaks = pdefinitelyByLoopBreaks;
            possiblyByLoopBreaks = ppossiblyByLoopBreaks;
        }
        else {
            definitelyAssignedByElseClause = false;
            possiblyAssignedByElseClause = false;
            definitelyExitedFromElseClause = false;
        }

        definitely = definitely 
                || !possiblyExitedFromForClause 
                && definitelyAssignedByElseClause 
                || atLeastOneIteration 
                && definitelyAssignedByForClause 
                && definitelySpecifiedByLoopBreaks 
                || definitelyAssignedByElseClause 
                && definitelySpecifiedByLoopBreaks;
        possibly = possibly 
                || possiblyAssignedByForClause 
                && !definitelyExitedFromForClause 
                || possiblyAssignedByElseClause 
                && !definitelyExitedFromElseClause 
                || possiblySpecifiedByLoopBreaks;
        
        checkDeclarationSection(that);
    }
      
}
