package com.redhat.ceylon.compiler.codegen;

import java.util.Iterator;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.AnyAttribute;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.MethodDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Variable;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.util.Util;

public class BoxingDeclarationVisitor extends Visitor {
    
    public final static Object IS_UNBOXED = new Object(){ public String toString() {return "IS_UNBOXED";};};
    private AbstractTransformer transformer;
    
    public BoxingDeclarationVisitor(AbstractTransformer transformer){
        this.transformer = transformer;
    }
    
    @Override
    public void visit(MethodDefinition that) {
        super.visit(that);
        // FIXME: we need to set those in the model loader as well
        Method method = that.getDeclarationModel();
        Method refinedMethod = (Method) Util.getTopmostRefinedDeclaration(method);
        if(isPrimitive(method, refinedMethod))
            method.setAttribute(IS_UNBOXED, true);
        Iterator<Parameter> parameters = method.getParameterLists().get(0).getParameters().iterator();
        for(Parameter refinedParam : refinedMethod.getParameterLists().get(0).getParameters()){
            Parameter param = parameters.next();
            if(isPrimitive(param, refinedParam))
                param.setAttribute(IS_UNBOXED, true);
        }
    }
    
    @Override
    public void visit(ClassDefinition that) {
        super.visit(that);
        Class klass = that.getDeclarationModel();
        List<Parameter> parameters = klass.getParameterLists().get(0).getParameters();
        for(Parameter param : parameters){
            if(isPrimitive(param, param))
                param.setAttribute(IS_UNBOXED, true);
        }
    }
    
    private boolean isPrimitive(TypedDeclaration declaration, TypedDeclaration refinedDeclaration) {
        return transformer.isCeylonBasicType(declaration.getType())
                && !(refinedDeclaration.getTypeDeclaration() instanceof TypeParameter);
    }

    @Override
    public void visit(AnyAttribute that) {
        super.visit(that);
        TypedDeclaration declaration = that.getDeclarationModel();
        TypedDeclaration refinedDeclaration = Util.getTopmostRefinedDeclaration(declaration);
        if(isPrimitive(declaration, refinedDeclaration))
            declaration.setAttribute(IS_UNBOXED, true);
    }

    @Override
    public void visit(Variable that) {
        super.visit(that);
        TypedDeclaration declaration = that.getDeclarationModel();
        if(isPrimitive(declaration, declaration))
            declaration.setAttribute(IS_UNBOXED, true);
    }
}
