/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.java.codegen;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyAttribute;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyMethod;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AttributeSetterDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ForComprehensionClause;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ForIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FunctionArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.FunctionalParameterDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.LazySpecifierExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PatternIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.SpecifierStatement;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.StatementOrArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ValueIterator;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;

public abstract class BoxingDeclarationVisitor extends Visitor {

    protected abstract boolean isCeylonBasicType(Type type);
    protected abstract boolean isNull(Type type);
    protected abstract boolean isObject(Type type);
    protected abstract boolean isCallable(Type type);
    protected abstract boolean hasErasure(Type type);
    protected abstract boolean willEraseToObject(Type type);
    protected abstract boolean isRaw(Type type);
    protected abstract boolean isWideningTypedDeclaration(TypedDeclaration typedDeclaration);
    protected abstract boolean hasSubstitutedBounds(Type type);

    /**
     * This is used to keep track of some optimisations we do, such as inlining the following shortcuts:
     * class X() extends T(){ m = function() => e; } and we need to be able to map back the lambda with
     * the method it specifies, for boxing and all
     */
    private Map<Function,Function> optimisedMethodSpecifiersToMethods = new HashMap<Function, Function>();
    
    // This is mostly just for testing purposes. Using -Dceylon.compiler.forceBoxedLocals=true
    // on the command line you can force all locals to be boxed (instead of the default unboxed)
    private static final boolean forceBoxedLocals = Boolean.getBoolean("ceylon.compiler.forceBoxedLocals");
    
    @Override
    public void visit(FunctionArgument that) {
        super.visit(that);
        that.getDeclarationModel().setUnboxed(false);
    }
    
    @Override
    public void visit(MethodArgument that) {
        super.visit(that);
        that.getDeclarationModel().setUnboxed(false);
    }
    
    @Override
    public void visit(AnyMethod that) {
        super.visit(that);
        visitMethod(that.getDeclarationModel(), that);
    }

    private void visitMethod(Function method, Node that) {
        boxMethod(method, that);
        rawTypedDeclaration(method);
        setErasureState(method);
    }
    
    @Override
    public void visit(FunctionalParameterDeclaration that) {
        if (Strategy.createMethod(that.getParameterModel())) {
            // Box the functional parameter as if it were a method
            visitMethod((Function)that.getParameterModel().getModel(), that);
            // Visit the parameters of the functional parameter
            that.visitChildren(this);
        } else {
            super.visit(that);
        }
    }
    
    private void setErasureState(TypedDeclaration decl) {
        // deal with invalid input
        if(decl == null)
            return;

        Type type = decl.getType();
        if(type != null){
            if(hasErasure(type) || hasSubstitutedBounds(type) || type.isTypeConstructor()){
                decl.setTypeErased(true);
            }
            if(decl.isActual()
                    && decl.getContainer() instanceof ClassOrInterface
                    // make sure we did not lose type information due to non-widening
                    && isWideningTypedDeclaration(decl)){
                // widening means not trusting the type, otherwise we end up thinking that the type is
                // something it's not and regular erasure rules don't apply there
                decl.setUntrustedType(true);
                decl.setTypeErased(true);
            }
        }
    }

    private void rawTypedDeclaration(TypedDeclaration decl) {
        // deal with invalid input
        if(decl == null)
            return;

        Type type = decl.getType();
        if(type != null){
            if(isRaw(type))
                type.setRaw(true);
        }
    }

    private void boxMethod(Function method, Node that) {
        // deal with invalid input
        if(method == null)
            return;
        Declaration refined = CodegenUtil.getTopmostRefinedDeclaration(method, optimisedMethodSpecifiersToMethods);
        // deal with invalid input
        if(refined == null
                || (!(refined instanceof Function)))
            return;
        TypedDeclaration refinedMethod = (TypedDeclaration)refined;
        if (method.getName() != null) {
            // A Callable, which never have primitive parameters
            setBoxingState(method, refinedMethod, that);
        } else {
            // Anonymous methods are always boxed
            method.setUnboxed(false);
        }
    }

    private void setBoxingState(TypedDeclaration declaration, TypedDeclaration refinedDeclaration, Node that) {
        Type type = declaration.getType();
        if(type == null){
            // an error must have already been reported
            return;
        }
        // fetch the real refined declaration if required
        if (Decl.equal(declaration, refinedDeclaration)
                && declaration instanceof FunctionOrValue
                && ((FunctionOrValue)declaration).isParameter()
                && declaration.getContainer() instanceof Class){
            // maybe it is really inherited from a field?
            FunctionOrValue methodOrValueForParam = (FunctionOrValue)declaration;
            if(methodOrValueForParam != null){
                // make sure we get the refined version of that member
                refinedDeclaration = (TypedDeclaration) methodOrValueForParam.getRefinedDeclaration();
            }
        }
        
        // inherit underlying type constraints
        if(!Decl.equal(refinedDeclaration, declaration) && type.getUnderlyingType() == null
                && refinedDeclaration.getType() != null)
            type.setUnderlyingType(refinedDeclaration.getType().getUnderlyingType());
        
        // abort if our boxing state has already been set
        if(declaration.getUnboxed() != null)
            return;
        
        // functional parameter return values are always boxed if we're not creating a method for them
        if(declaration instanceof Function 
                && ((Function)declaration).isParameter()
                && !JvmBackendUtil.createMethod((Function)declaration)){
            declaration.setUnboxed(false);
            return;
        }
        
        if (!Decl.equal(refinedDeclaration, declaration)) {
            // make sure refined declarations have already been set
            if(refinedDeclaration.getUnboxed() == null)
                setBoxingState(refinedDeclaration, refinedDeclaration, that);
            // inherit
            declaration.setUnboxed(refinedDeclaration.getUnboxed());
        } else if (declaration instanceof Function
                && CodegenUtil.isVoid(declaration.getType())
                && Strategy.useBoxedVoid((Function)declaration)
                && !(refinedDeclaration.getTypeDeclaration() instanceof TypeParameter)
                && !CodegenUtil.isContainerFunctionalParameter(refinedDeclaration)
                && !(refinedDeclaration instanceof Functional && Decl.isMpl((Functional)refinedDeclaration))){
            declaration.setUnboxed(false);
        } else if((isCeylonBasicType(type) || Decl.isUnboxedVoid(declaration))
           && !(refinedDeclaration.getTypeDeclaration() instanceof TypeParameter)
           && (refinedDeclaration.getContainer() instanceof Declaration == false || !CodegenUtil.isContainerFunctionalParameter(refinedDeclaration))
           && !(refinedDeclaration instanceof Functional && Decl.isMpl((Functional)refinedDeclaration))){
            boolean unbox = !forceBoxedLocals || !(declaration instanceof Value) || !Decl.isLocal(declaration) || Decl.isParameter(declaration) || Decl.isTransient(declaration);
            declaration.setUnboxed(unbox);
        } else if (Decl.isValueParameter(declaration)
                && CodegenUtil.isContainerFunctionalParameter(declaration)
                && JvmBackendUtil.createMethod((FunctionOrValue)declaration.getContainer())) {
            Function functionalParameter = (Function)declaration.getContainer();
            TypedDeclaration refinedFrom = (TypedDeclaration)CodegenUtil.getTopmostRefinedDeclaration(functionalParameter, optimisedMethodSpecifiersToMethods);
            if (Decl.equal(refinedFrom, functionalParameter) ) {
                // Don't consider Anything to be unboxed, since this is a parameter
                // not a method return type (where void would be considered unboxed).
                if (declaration.getUnit().getAnythingType().isExactly(declaration.getType())
                        || declaration.getUnit().isOptionalType(declaration.getType())) {
                    declaration.setUnboxed(false);
                } else {
                    declaration.setUnboxed(true);
                }
            } else {
                // make sure refined declarations have already been set
                if(refinedFrom.getUnboxed() == null)
                    setBoxingState(refinedFrom, refinedFrom, that);
                // inherit
                declaration.setUnboxed(refinedFrom.getUnboxed());
            }
        } else {   
            declaration.setUnboxed(false);
        }
        
        // Any "@boxed" or "@unboxed" compiler annotation overrides
        boxFromAnnotation(declaration, that);
    }

    private void boxAttribute(TypedDeclaration declaration, Node that) {
        // deal with invalid input
        if(declaration == null)
            return;
        TypedDeclaration refinedDeclaration = null;
        refinedDeclaration = (TypedDeclaration)CodegenUtil.getTopmostRefinedDeclaration(declaration, optimisedMethodSpecifiersToMethods);
        // deal with invalid input
        if(refinedDeclaration == null)
            return;
        setBoxingState(declaration, refinedDeclaration, that);
    }
    
    private void boxFromAnnotation(TypedDeclaration declaration, Node that) {
        // Let's see if the attribute has a "boxed" or "unboxed" annotation
        // and set its state accordingly. NB this is not checked for validity!
        if(that instanceof StatementOrArgument) {
            if(CodegenUtil.hasCompilerAnnotation((StatementOrArgument)that, "boxed")) {
                declaration.setUnboxed(false);
            } else if(CodegenUtil.hasCompilerAnnotation((StatementOrArgument)that, "unboxed")) {
                declaration.setUnboxed(true);
            }
        }
    }
    
    @Override
    public void visit(Tree.Parameter that) {
        super.visit(that);
        TypedDeclaration declaration = that.getParameterModel().getModel();
        visitAttributeOrParameter(declaration, that);
    }
    
    @Override
    public void visit(Tree.ValueParameterDeclaration that) {
        super.visit(that);
    }
    
    @Override
    public void visit(AnyAttribute that) {
        super.visit(that);
        TypedDeclaration declaration = that.getDeclarationModel();
        visitAttributeOrParameter(declaration, that);
    }
    
    private void visitAttributeOrParameter(TypedDeclaration declaration, Node that) {
        boxAttribute(declaration, that);
        rawTypedDeclaration(declaration);
        setErasureState(declaration);
    }
    
    @Override
    public void visit(AttributeDeclaration that) {
        if(that.getSpecifierOrInitializerExpression() != null
                && that.getDeclarationModel() != null
                && that.getType() instanceof Tree.ValueModifier
                && that.getDeclarationModel().getType().equals(that.getSpecifierOrInitializerExpression().getExpression().getTypeModel())){
            that.getDeclarationModel().setType(that.getDeclarationModel().getType().withoutUnderlyingType());
        }
        super.visit(that);
    }

    @Override
    public void visit(AttributeArgument that) {
        super.visit(that);
        boxAttribute(that.getDeclarationModel(), that);
    }

    @Override
    public void visit(AttributeSetterDefinition that) {
        super.visit(that);
        Setter declaration = that.getDeclarationModel();
        // deal with invalid input
        if(declaration == null)
            return;
        // To determine boxing for a setter we use its parameter
        TypedDeclaration paramDeclaration = declaration.getParameter().getModel();
        boxAttribute(paramDeclaration, that);
        // Now copy the settings from the parameter to the setter itself
        declaration.setUnboxed(paramDeclaration.getUnboxed());
        // Then we check if there are any overriding compiler annotations
        boxFromAnnotation(declaration, that);
        // And finally we copy the setting back again to make sure they're really the same
        paramDeclaration.setUnboxed(declaration.getUnboxed());
    }

    @Override
    public void visit(Variable that) {
        super.visit(that);
        TypedDeclaration declaration = that.getDeclarationModel();
        // deal with invalid input
        if(declaration == null)
            return;
        setBoxingState(declaration, declaration, that);
        rawTypedDeclaration(declaration);
        setErasureState(declaration);
    }

    @Override
    public void visit(SpecifierStatement that) {
        TypedDeclaration declaration = that.getDeclaration();
        Function optimisedDeclaration = null;
        // make sure we detect the shortcut refinement inlining cases
        if(declaration instanceof Function){
            if(that.getSpecifierExpression() != null
                    && that.getSpecifierExpression() instanceof LazySpecifierExpression == false){
                Tree.Term term = Decl.unwrapExpressionsUntilTerm(that.getSpecifierExpression().getExpression());
                if(term != null
                        && term instanceof Tree.FunctionArgument){
                    optimisedDeclaration = ((Tree.FunctionArgument)term).getDeclarationModel();
                    this.optimisedMethodSpecifiersToMethods.put(optimisedDeclaration, (Function) declaration);
                }
            }
        }
        try{
            super.visit(that);
        }finally{
            if(optimisedDeclaration != null)
                this.optimisedMethodSpecifiersToMethods.remove(optimisedDeclaration);
        }
        if(declaration == null)
            return;
        if(declaration instanceof Function){
            visitMethod((Function) declaration, that);
        }else if(declaration instanceof Value)
            visitAttributeOrParameter(declaration, that);
    }

    @Override
    public void visit(ForComprehensionClause that) {
        super.visit(that);
        // sort of a hack, because normal visiting rules would declare iterator variables to be potentially
        // unboxed, but the implementation always boxes them for now, so override it after we visit the comprehension
        ForIterator iter = that.getForIterator();
        if (iter instanceof ValueIterator) {
            ((ValueIterator) iter).getVariable().getDeclarationModel().setUnboxed(false);
        } else if (iter instanceof PatternIterator) {
            boxPattern(((PatternIterator) iter).getPattern());
        }
    }
    
    private void boxPattern(Tree.Pattern pattern) {
        if (pattern instanceof Tree.KeyValuePattern) {
            boxPattern(((Tree.KeyValuePattern)pattern).getKey());
            boxPattern(((Tree.KeyValuePattern)pattern).getValue());
        } else if (pattern instanceof Tree.TuplePattern) {
            for (Tree.Pattern p : ((Tree.TuplePattern)pattern).getPatterns()) {
                boxPattern(p);
            }
        } else if (pattern instanceof Tree.VariablePattern) {
            ((Tree.VariablePattern)pattern).getVariable().getDeclarationModel().setUnboxed(false);
        } else {
            throw BugException.unhandledCase(pattern);
        }
    }
    
    @Override
    public void visit(Tree.TypeParameterDeclaration that) {
        super.visit(that);
        TypeParameter typeParameter = that.getDeclarationModel();
        if(typeParameter != null){
            visitTypeParameter(typeParameter);
        }
    }
    
    private void visitTypeParameter(TypeParameter typeParameter) {
        if(typeParameter.hasNonErasedBounds() != null)
            return;
        for(Type pt : typeParameter.getSatisfiedTypes()){
            if(!willEraseToObject(pt)){
                typeParameter.setNonErasedBounds(true);
                return;
            }
        }
        typeParameter.setNonErasedBounds(false);
        return;
    }

    // The following are not really necessary under normal circumstances,
    // they explicitly set what is already set by default, but when using
    // the -Dceylon.compiler.forceBoxedLocals option they are needed
    
    @Override
    public void visit(ValueIterator that) {
        super.visit(that);
        // The variable in a for over a Range is always unboxed
        if (that.getVariable() != null && that.getSpecifierExpression().getExpression().getTerm() instanceof Tree.RangeOp) {
            that.getVariable().getDeclarationModel().setUnboxed(true);
        }
    }

}
