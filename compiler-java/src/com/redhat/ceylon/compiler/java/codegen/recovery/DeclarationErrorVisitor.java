package com.redhat.ceylon.compiler.java.codegen.recovery;

import java.util.List;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.typechecker.analyzer.AnalysisError;
import com.redhat.ceylon.compiler.typechecker.analyzer.UsageWarning;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.loader.model.LazyClass;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Visitor for declarations which constructs a {@link TransformationPlan} for 
 * transforming a {@link Tree.Declaration} based on the errors in the tree.
 * Unlike the {@link StatementErrorVisitor} and {@link ErrorVisitor}
 * this visitor does not fail fast.
 */
class DeclarationErrorVisitor extends Visitor {
    private static final int TYPE_DECLARATION_DOES_NOT_EXIST = 102;
    private static final int FORMAL_MEMBER_UNIMPLEMENTED_IN_CLASS_HIERARCHY = 300;
    private static final int MEMBER_HAS_WRONG_NUMBER_OF_PARAMETERS = 9100;
    private static final int TYPE_OF_PARAMETER_IS_DIFFERENT_TO_CORRESPONDING_PARAMETER = 9200;
    private static final int COULD_NOT_DETERMINE_PARAMETER_TYPE_SAME_AS_CORRESPONDING_PARAMETER = 9210;
    private static final int REFINED_MEMBER_WRONG_NUM_PL = 9300;
    private static final int MISSING_PL_FUNCTION_DECL = 1000;
    private static final int PL_AND_CONSTRUCTORS = 1002;
    
    private TransformationPlan plan;
    private final ExpressionErrorVisitor expressionVisitor;
    private boolean expectingError;
    private String errMessage;
    private Declaration model;
    
    DeclarationErrorVisitor(ExpressionErrorVisitor expressionVisitor) {
        this.expressionVisitor = expressionVisitor;
    }

    public final HasErrorException getFirstErrorMessage(Tree.Declaration target) {
        TransformationPlan plan = getRecoveryPlan(target);
        if (plan instanceof Generate) {
            return null;
        } else {
            return new HasErrorException(plan.getNode(), plan.getErrorMessage());
        }
    }
    
    public final TransformationPlan getRecoveryPlan(Tree.Declaration target) {
        model = target.getDeclarationModel();
        TransformationPlan oldPlan = plan;
        try {
            plan = Errors.GENERATE;
            target.visit(this);
            return plan;
        } finally {
            plan = oldPlan;
        }
    }
    
    /**
     * Install a new plan iff it's more drastic than the existing plan
     * @param plan
     */
    private void newplan(TransformationPlan plan) {
        if (plan.replaces(this.plan)) {
            this.plan = plan;
        }
    }
    
    @Override
    public final void visitAny(Node that) {
        planAccordingToErrors(that);
        super.visitAny(that);
    }
    
    /**
     * Update the plan according to the errors on the node
     */
    private void planAccordingToErrors(Node that) {
        List<Message> errors = that.getErrors();
        
        for(Message message : errors){
            if(isError(that, message)) {
                TransformationPlan plan;
                /*if (message.getCode() == MEMBER_HAS_WRONG_NUMBER_OF_PARAMETERS
                        && model.isActual()
                        && model.isClassMember()) {
                    plan = new ThrowerMethod(that, message);
                } else if (message.getCode() == TYPE_OF_PARAMETER_IS_DIFFERENT_TO_CORRESPONDING_PARAMETER
                        && model.isActual()
                        && model.isClassMember()) {
                    plan = new ThrowerMethod(that, message);
                } else if (message.getCode() == COULD_NOT_DETERMINE_PARAMETER_TYPE_SAME_AS_CORRESPONDING_PARAMETER
                        && model.isActual()
                        && model.isClassMember()) {
                    plan = new ThrowerMethod(that, message);
                } else if ((message.getCode() == REFINED_MEMBER_WRONG_NUM_PL
                                || message.getCode() == MISSING_PL_FUNCTION_DECL)
                        && model.isActual()
                        && model.isClassMember()) {
                    plan = new ThrowerMethod(that, message);
                } else*/ if (message.getCode() == FORMAL_MEMBER_UNIMPLEMENTED_IN_CLASS_HIERARCHY
                        && (model instanceof Class 
                            || (model instanceof Value
                                    && ((Value)model).getTypeDeclaration().isAnonymous()))) {
                    plan = new ThrowerMethod(that, message);
                } 
                else if (message.getCode() == PL_AND_CONSTRUCTORS
                        && (model instanceof Class 
                            || (model instanceof Value
                                    && ((Value)model).getTypeDeclaration().isAnonymous()))) {
                    plan = new ThrowerCatchallConstructor(that, message);
                } 
                else {
                    plan = new Drop(that, message);
                } 
                newplan(plan);
            }
        }
    }
    
    /** Is the given message on the given node considered an error */
    private boolean isError(Node that, Message message) {
        if (errMessage != null
                && message.getMessage().equals(errMessage)) {
            return false;
        } else {
            return !(message instanceof UsageWarning);
        }
    }
    
    @Override
    public void visit(Tree.Annotation that) {
        // Unlike declaration bodies or specifiers, 
        // we *do* care about errors in expressions in annotations: Those are
        // considered part of the declaration
        HasErrorException error = expressionVisitor.getFirstErrorMessage(that);
        if (error != null) {
            newplan(new Drop(error.getNode(), error.getErrorMessage()));
        }
    }
    
    @Override
    public void visit(Tree.Body that) {
        // don't go there: we don't really care about block errors
    }
    
    @Override
    public void visit(Tree.SpecifierOrInitializerExpression that) {
        // don't go there: we don't really care about expression errors
    }
    
    @Override
    public void visit(Tree.Type that) {
        HasErrorException error = expressionVisitor.getFirstErrorMessage(that);
        if (error != null && isError(that, error.getErrorMessage())) {
            newplan(new Drop(error.getNode(), error.getErrorMessage()));
            return;
        }
        // type inference is used but the type of 
        // the inferred expression is unknown due to other errors
        if (that.getTypeModel().containsUnknowns()) {
            newplan(new Drop(that, new AnalysisError(that, "unknown type", Backend.Java)));
        }
    }
    
    @Override
    public void visit(Tree.InitializerParameter that) {
        planAccordingToErrors(that);
        // but don't visit children
    }
    
    public void visit(Tree.StatementOrArgument that) {
        boolean b = this.expectingError;
        initExpectingError(that.getCompilerAnnotations());
        super.visit(that);
        expectingError = b;
    }
    
    public void visit(Tree.ParameterDeclaration that) {
        boolean b = this.expectingError;
        initExpectingError(that.getTypedDeclaration().getCompilerAnnotations());
        super.visit(that);
        expectingError = b;
    }
    
    public void visit(Tree.CompilationUnit that) {
        boolean b = this.expectingError;
        initExpectingError(that.getCompilerAnnotations());
        super.visit(that);
        expectingError = b;
    }
    
    protected void initExpectingError(List<Tree.CompilerAnnotation> annotations) {
        for (Tree.CompilerAnnotation c: annotations) {
            if (c.getIdentifier().getText().equals("error")) {
                expectingError = true;
                Tree.StringLiteral sl = c.getStringLiteral();
                if (sl!=null) {
                    errMessage = sl.getText();
                }
            }
        }
    }
    
    public void visit(Tree.ExtendedType that) {
        that.getType().visit(this);
        // Don't visit the invocation expression unless our superclass is a 
        // Java class with a private ctor -- that could cause a javac 
        // error if we tried to generate a ctor for this class.
        if (that.getType().getDeclarationModel() instanceof LazyClass
                && !((LazyClass)that.getType().getDeclarationModel()).isCeylon()) {
            boolean hasPrivateCtor = false;
            List<Declaration> overloads = ((LazyClass)that.getType().getDeclarationModel()).getOverloads();
            if (overloads != null) {
                for (Declaration ctor : overloads) {
                    if (!ctor.isShared()) {
                        hasPrivateCtor = true;
                        break;
                    }
                }
            }
            if (hasPrivateCtor) {
                that.getInvocationExpression().visit(this);
            }
        }
    }
}