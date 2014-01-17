package com.redhat.ceylon.compiler.java.loader;

import java.util.List;

import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LazySpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Primary;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierOrInitializerExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

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
            if (d==declaration) {
                if (Decl.isParameter(d)) {
                    // a reference from a default argument 
                    // expression of the same parameter 
                    // list does not capture a parameter
                    boolean sameScope = d.getContainer().equals(that.getScope());
                    if (!sameScope || methodSpecifier || inLazySpecifierExpression) {
                        ((MethodOrValue)d).setCaptured(true);
                    }
                    
                    // Accessing another instance's member passed to a class initializer
                    if (that instanceof Tree.QualifiedMemberExpression) {
                        if (decl instanceof TypedDeclaration
                                && ((TypedDeclaration)decl).getOtherInstanceAccess()) {
                            ((MethodOrValue)d).setCaptured(true);
                        }
                    }
                    
                    if (isCapturableMplParameter(d)) {
                        ((MethodOrValue)d).setCaptured(true);
                    }
                } else if (Decl.isValue(d) || Decl.isGetter(decl)) {
                    Value v = (Value) d;
                    v.setCaptured(true);
                    if (Decl.isObjectValue(d)){
                        v.setSelfCaptured(isSelfCaptured(that, d));
                    }
                    if (v.getSetter() != null) {
                        v.getSetter().setCaptured(true);
                    }
                }
                else if (d instanceof Method) {
                    ((Method) d).setCaptured(true);
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
                && scope != type)
            scope = scope.getScope();
        return scope == type;
    }

    /**
     * Because methods with MPL use nested anonymous AbstractCallables
     * if the declaration is a parameter in all but the last parameter list
     * it should be captured.
     */
    private boolean isCapturableMplParameter(Declaration d) {
        if (!(d instanceof MethodOrValue)) {
            return false;
        }
        com.redhat.ceylon.compiler.typechecker.model.Parameter param = ((MethodOrValue)d).getInitializerParameter();
        if (param == null) {
            return false;
        }
        Declaration paramDecl = param.getDeclaration();
        if (paramDecl instanceof Functional) {
            List<com.redhat.ceylon.compiler.typechecker.model.ParameterList> parameterLists = ((Functional)paramDecl).getParameterLists();
            for (int i = 0; i < parameterLists.size()-1; i++) {
                if (parameterLists.get(i).getParameters().contains(param)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void visit(Tree.QualifiedMemberExpression that) {
        super.visit(that);
        if (isSelfReference(that.getPrimary())) {
            visitReference(that);
        }
        else {
            capture(that);
        }
    }

    private boolean isSelfReference(Tree.Primary that) {
        return that instanceof Tree.This || that instanceof Tree.Outer;
    }

    @Override public void visit(Tree.Declaration that) {
        Declaration dm = that.getDeclarationModel();
        if (dm==declaration.getContainer()
                || dm==declaration
                || (dm instanceof Setter && ((Setter) dm).getGetter()==declaration)) {
            if (!isCapturableMplParameter(declaration)) {
                inCapturingScope = false;
            }
        }
        super.visit(that);
    }
    
    @Override public void visit(Tree.ClassDefinition that) {
        boolean cs = enterCapturingScope();
        super.visit(that);
        exitCapturingScope(cs);
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
            // thing you've got is a Method you can't know the
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
        that.getForComprehensionClause().visit(this);
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
}
