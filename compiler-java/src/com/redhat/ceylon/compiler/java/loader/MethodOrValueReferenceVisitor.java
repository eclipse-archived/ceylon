package com.redhat.ceylon.compiler.java.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LazySpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Primary;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierOrInitializerExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Statement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Determines if a value is "captured" by 
 * block nested in the same containing scope.
 * 
 * For example, a captured value in a class
 * body is an attribute. A captured value in
 * a method body can outlive the execution of
 * the method.
 * 
 * @author Gavin King
 *
 */
public class MethodOrValueReferenceVisitor extends Visitor {
    
    private final TypedDeclaration declaration;
    private boolean inCapturingScope = false;
    private boolean inLazySpecifierExpression = false;
    private boolean defaultArgument;
    
    public MethodOrValueReferenceVisitor(TypedDeclaration declaration) {
        this.declaration = declaration;
    }
    
    private boolean enterCapturingScope() {
        boolean cs = inCapturingScope;
        inCapturingScope = true;
        return cs;
    }
    
    private void exitCapturingScope(boolean cs) {
        inCapturingScope = cs;
    }
    
    @Override public void visit(Tree.BaseMemberExpression that) {
        visitReference(that);
        /*if (that.getIdentifier()!=null) {
            TypedDeclaration d = (TypedDeclaration) getDeclaration(that.getScope(), that.getUnit(), that.getIdentifier(), context);
            visitReference(that, d);
        }*/
    }

    private void visitReference(Tree.Primary that) {
        if (inCapturingScope) {
            capture(that);
        }
    }

    private void capture(Tree.Primary that) {
        capture(that, false);
    }
    
    private void capture(Tree.Primary that, boolean methodSpecifier) {
        if (that instanceof Tree.MemberOrTypeExpression) {
            final Declaration decl = ((Tree.MemberOrTypeExpression) that).getDeclaration();
            if (!(decl instanceof TypedDeclaration)) {
                return;
            }
            TypedDeclaration d = (TypedDeclaration) decl;
            if (Decl.equal(d, declaration) || (d.isNativeHeader() && d.getOverloads().contains(declaration))) {
                d = declaration;
                if (Decl.isParameter(d)) {
                    // a reference from a default argument 
                    // expression of the same parameter 
                    // list does not capture a parameter
                    Scope s = that.getScope();
                    boolean sameScope = d.getContainer().equals(s)
                            || (s instanceof Declaration
                                    && (Decl.isParameter((Declaration)s) || (s instanceof Value && !((Value)s).isTransient()))
                                    && d.getContainer().equals(s.getScope()))
                                    ;
                    if (!sameScope || methodSpecifier || inLazySpecifierExpression) {
                        ((FunctionOrValue)d).setCaptured(true);
                    }
                    
                    // Accessing another instance's member passed to a class initializer
                    if (that instanceof Tree.QualifiedMemberExpression) {
                        if (d instanceof TypedDeclaration
                                && ((TypedDeclaration)d).getOtherInstanceAccess()) {
                            ((FunctionOrValue)d).setCaptured(true);
                        }
                    }
                    
                    if (isCapturableMplParameter(d)) {
                        ((FunctionOrValue)d).setCaptured(true);
                    }
                } else if (Decl.isValue(d) || Decl.isGetter(d)) {
                    Value v = (Value) d;
                    v.setCaptured(true);
                    if (Decl.isObjectValue(d)){
                        v.setSelfCaptured(isSelfCaptured(that, d));
                    }
                    if (v.getSetter() != null) {
                        v.getSetter().setCaptured(true);
                    }
                }
                else if (d instanceof Function) {
                    ((Function) d).setCaptured(true);
                }
                
                /*if (d.isVariable() && !d.isClassMember() && !d.isToplevel()) {
                    that.addError("access to variable local from capturing scope: " + declaration.getName());
                }*/
            }
        }
    }

    /**
     * Returns true if <code>that</code> is within the scope of the type of <code>d</code>,
     * which must be a value declaration for an object declaration.
     */
    private boolean isSelfCaptured(Primary that, TypedDeclaration d) {
        TypeDeclaration type = d.getTypeDeclaration();
        Scope scope = that.getScope();
        while(scope != null 
                && scope instanceof Package == false
                && !Decl.equalScopeDecl(scope, type)) {
            scope = scope.getScope();
        }
        return Decl.equalScopeDecl(scope, type);
    }

    /**
     * Because methods with MPL use nested anonymous AbstractCallables
     * if the declaration is a parameter in all but the last parameter list
     * it should be captured.
     */
    private boolean isCapturableMplParameter(Declaration d) {
        if (!(d instanceof FunctionOrValue)) {
            return false;
        }
        com.redhat.ceylon.model.typechecker.model.Parameter param = ((FunctionOrValue)d).getInitializerParameter();
        if (param == null) {
            return false;
        }
        Declaration paramDecl = param.getDeclaration();
        if (paramDecl instanceof Functional) {
            List<com.redhat.ceylon.model.typechecker.model.ParameterList> parameterLists = ((Functional)paramDecl).getParameterLists();
            for (int i = 0; i < parameterLists.size()-1; i++) {
                if (parameterLists.get(i).getParameters().contains(param)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    boolean invocationPrimary;
    
    @Override
    public void visit(Tree.InvocationExpression that) {
        boolean pip = this.invocationPrimary;
        this.invocationPrimary = true;
        that.getPrimary().visit(this);
        this.invocationPrimary = pip;
        if (that.getPositionalArgumentList() != null) {
            that.getPositionalArgumentList().visit(this);
        }
        if (that.getNamedArgumentList() != null) {
            that.getNamedArgumentList().visit(this);
        }
    }
    
    @Override
    public void visit(Tree.QualifiedMemberExpression that) {
        boolean cs = false;
        boolean isCallableReference = !invocationPrimary && that.getDeclaration() instanceof Functional;
        if (isCallableReference) {
            cs = enterCapturingScope();
        }
        super.visit(that);
        if (isSelfReference(that.getPrimary())) {
            visitReference(that);
        }
        else {
            capture(that);
        }
        if (isCallableReference) {
            exitCapturingScope(cs);
        }
    }

    private boolean isSelfReference(Tree.Primary that) {
        return that instanceof Tree.This || that instanceof Tree.Outer;
    }

    @Override public void visit(Tree.Declaration that) {
        Declaration dm = that.getDeclarationModel();
        if (dm==declaration.getContainer()
                || (Decl.equal(dm, declaration) && !isClassWithConstructorMember(declaration))
                || (dm instanceof Setter && ((Setter) dm).getGetter()==declaration)) {
            if (!isCapturableMplParameter(declaration)) {
                this.inCapturingScope = false;
            }
        }
        super.visit(that);
    }
    
    private boolean isClassWithConstructorMember(TypedDeclaration decl) {
        return decl.isClassMember() && ((Class)decl.getContainer()).hasConstructors();
    }

    static class ConstructorPlan {
        public List<Tree.Statement> before = new LinkedList<>();
        public List<Tree.Statement> after = new LinkedList<>();
        public boolean isDelegate;
        public Tree.Constructor constructor;
        public ConstructorPlan delegate;
    }
    
    @Override public void visit(Tree.ClassDefinition that) {
        if (!that.getDeclarationModel().hasConstructors()) {
            boolean cs = enterCapturingScope();
            super.visit(that);
            exitCapturingScope(cs);
        } else {
            // super special case for unshared members when we have constructors
            if(!declaration.isCaptured() 
                    && declaration instanceof FunctionOrValue
                    && declaration.isClassMember()){
                Map<Constructor,ConstructorPlan> constructorPlans = new HashMap<Constructor,ConstructorPlan>();
                List<Tree.Statement> statements = new ArrayList<>(that.getClassBody().getStatements().size());
                // find every constructor, and build a model of how they delegate
                for (Tree.Statement stmt : that.getClassBody().getStatements()) {
                    if (stmt instanceof Tree.Constructor) {
                        Tree.Constructor ctor = (Tree.Constructor)stmt;
                        // build a new plan for it
                        ConstructorPlan plan = new ConstructorPlan();
                        plan.constructor = ctor;
                        constructorPlans.put(ctor.getConstructor(), plan);
                        // find every constructor which delegates to another constructor
                        if (ctor.getDelegatedConstructor() != null
                                && ctor.getDelegatedConstructor().getInvocationExpression() != null) {
                            if (ctor.getDelegatedConstructor().getInvocationExpression().getPrimary() instanceof Tree.ExtendedTypeExpression) {
                                Tree.ExtendedTypeExpression ete = (Tree.ExtendedTypeExpression)ctor.getDelegatedConstructor().getInvocationExpression().getPrimary();
                                // are we delegating to a constructor (not a supertype) of the same class (this class)?
                                if (Decl.isConstructor(ete.getDeclaration())
                                        && Decl.getConstructedClass(ete.getDeclaration()).equals(that.getDeclarationModel())) {
                                    // remember the delegation
                                    Constructor delegate = Decl.getConstructor(ete.getDeclaration());
                                    ConstructorPlan delegatePlan = constructorPlans.get(delegate);
                                    plan.delegate = delegatePlan;
                                    // mark the delegate as delegated
                                    delegatePlan.isDelegate = true;
                                    // we have all the statements before us and after our delegate
                                    plan.before.addAll(delegatePlan.after);
                                }
                            }
                        }
                        // if we have no delegate, we start with every common statement
                        if(plan.delegate == null)
                            plan.before.addAll(statements);
                        // also add all the constructor's statements
                        if (ctor.getBlock() != null) {
                            plan.before.addAll(ctor.getBlock().getStatements());
                        }
                    }else{
                        statements.add(stmt);
                        // make sure all existing constructors get this statement too
                        for(ConstructorPlan constructorPlan : constructorPlans.values())
                            constructorPlan.after.add(stmt);
                    }
                }
                // try every constructor plan and see if it's used in two methods
                for(ConstructorPlan constructorPlan : constructorPlans.values()){
                    visitConstructorPlan(constructorPlan);
                    // are we done?
                    if(declaration.isCaptured())
                        break;
                }
            }
            // do regular capturing after that (for members), if required
            if(!declaration.isCaptured())
                super.visit(that);
        }
    }
    
    /**
     * Marks declarations as captured if they are used in more than one generated constructor, according
     * to the given plan and knowledge on how we split up constructor delegates.
     */
    private void visitConstructorPlan(ConstructorPlan constructorPlan) {
        // if there is no delegation all statements are put in the same method so we can't capture
        if(constructorPlan.delegate == null && !constructorPlan.isDelegate)
            return;
        boolean cs = enterCapturingScope();
        int useCount = usedIn(constructorPlan, false);
        FunctionOrValue fov = ((FunctionOrValue)declaration);
        fov.setCaptured(useCount > 1);
        if(fov instanceof Value){
            Value val = (Value) fov;
            if(val.getSetter() != null)
                val.getSetter().setCaptured(useCount > 1);
        }
        exitCapturingScope(cs);
    }

    /**
     * Count the number of different methods the declaration is captured in, for the given constructor
     * plan.
     * @param onlyBefore true if we only want to check the before statements (in case of delegation)
     */
    private int usedIn(ConstructorPlan constructorPlan, boolean onlyBefore) {
        int delegateCount = 0;
        if(constructorPlan.delegate != null)
            delegateCount = usedIn(constructorPlan.delegate, true);
        int usedBefore = usedIn(constructorPlan.before);
        int usedAfter = onlyBefore ? 0 : usedIn(constructorPlan.after);
        if(constructorPlan.isDelegate){
            // we have a split between the before/after statements
            return delegateCount + usedBefore + usedAfter;
        }else{
            // before and after are in the same method, so cap it to 1
            return delegateCount + Math.min(usedBefore + usedAfter, 1);
        }
    }

    /**
     * Returns 1 if the declaration is captured in the given statements, 0 otherwise.
     */
    private int usedIn(List<Statement> stmts) {
        for(Tree.Statement stmt : stmts){
            // count declarations as usage
            if(stmt instanceof Tree.TypedDeclaration
                    && ((Tree.TypedDeclaration) stmt).getDeclarationModel() == declaration)
                return 1;
            stmt.visit(this);
            if(declaration.isCaptured())
                break;
        }
        boolean used = declaration.isCaptured();
        FunctionOrValue fov = ((FunctionOrValue)declaration);
        fov.setCaptured(false);
        if(fov instanceof Value){
            Value val = (Value) fov;
            if(val.getSetter() != null)
                val.getSetter().setCaptured(false);
        }
        return used ? 1 : 0;
    }

    @Override public void visit(Tree.ObjectDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.MethodDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        if (Decl.withinClass(that)) {
            // This is a HACK to make sure that method definitions
            // are always seen as captured and can't be confused
            // for being part of the initializer. This is because
            // uncaptured method *declarations* can and will be
            // made local to the class initializer, but if the only
            // thing you've got is a Function you can't know the
            // difference between a definition and a declaration,
            // that's why we set the captured flag here.
            that.getDeclarationModel().setCaptured(true);
        }
        exitCapturingScope(cs);
    }
    
    
    @Override public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);
        final SpecifierOrInitializerExpression specifier = that.getSpecifierOrInitializerExpression();
        if (specifier != null && specifier instanceof Tree.LazySpecifierExpression) {
            boolean cs = enterCapturingScope();
            specifier.visit(this);
            exitCapturingScope(cs);
        }   
    }
    
    @Override public void visit(Tree.AttributeGetterDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.AttributeSetterDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.ObjectArgument that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.MethodArgument that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.AttributeArgument that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    /*@Override public void visit(Tree.FunctionalParameterDeclaration that) {
        defaultArgument = true;
        super.visit(that);
        defaultArgument = false;
    }*/
    @Override public void visit(Tree.ValueParameterDeclaration that) {
        defaultArgument = true;
        super.visit(that);
        defaultArgument = false;
    }
    /*@Override public void visit(Tree.InitializerParameter that) {
        defaultArgument = true;
        super.visit(that);
        defaultArgument = false;
    }*/
    
    @Override public void visit(Tree.SpecifierOrInitializerExpression that) {
        boolean cs = false;
        // Things in specifiers or initializers are only captured if they are
        // specifiers or initializers of parameters
        if (defaultArgument || inLazySpecifierExpression) {
            cs = enterCapturingScope();
        }
        super.visit(that);
        if (defaultArgument || inLazySpecifierExpression) {
            exitCapturingScope(cs);
        }
    }
    
    @Override public void visit(Tree.FunctionArgument that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
    
    @Override public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        final SpecifierExpression specifier = that.getSpecifierExpression();
        if (specifier != null && specifier instanceof Tree.LazySpecifierExpression) {
            boolean cs = enterCapturingScope();
            specifier.visit(this);
            exitCapturingScope(cs);
        }   
        
    }

    @Override public void visit(Tree.Comprehension that) {
        super.visit(that);
        boolean cs = enterCapturingScope();
        that.getInitialComprehensionClause().visit(this);
        exitCapturingScope(cs);
    }
    @Override public void visit(Tree.ForComprehensionClause that) {
        super.visit(that);
        final SpecifierExpression specifier = that.getForIterator().getSpecifierExpression();
        if (specifier != null) {
            
            final Expression expr = specifier.getExpression();
            final Term term = expr.getTerm();
            if (term instanceof Tree.Primary) {
                capture((Tree.Primary)term, true);
            }
        }   
        that.getComprehensionClause().visit(this);
    }
    @Override public void visit(Tree.IfComprehensionClause that) {
        super.visit(that);
        //that.getCondition().visit(this);
        that.getComprehensionClause().visit(this);
    }
    @Override public void visit(Tree.ExpressionComprehensionClause that) {
        super.visit(that);
        visitReference(that.getExpression());
    }

    @Override
    public void visit(SpecifierStatement that) {
        boolean cs = inCapturingScope;
        // refining specifiers do capture, as opposed to regular constructor specifiers
        if(that.getRefinement())
            enterCapturingScope();
        super.visit(that);
        if(that.getRefinement())
            exitCapturingScope(cs);
    }

    @Override
    public void visit(LazySpecifierExpression that) {
        boolean lse = inLazySpecifierExpression;
        inLazySpecifierExpression = true;
        super.visit(that);
        inLazySpecifierExpression = lse;
    }
    
    @Override public void visit(Tree.SequencedArgument that) {
        // Because a SequenceArgument requires an anonymous class
        // enumerated the expressions are in a different scope
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
    }
}
