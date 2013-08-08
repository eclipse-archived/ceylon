package com.redhat.ceylon.compiler.java.codegen;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.util.ListBuffer;

/**
 * The invocation of an annotation constructor or annocation class
 */
public class AnnotationInvocation {
    
    public final RuntimeException init = new RuntimeException();
    
    private Method constructorDeclaration;
    
    private List<AnnotationConstructorParameter> constructorParameters = new ArrayList<AnnotationConstructorParameter>();
    
    private Declaration primary;
    
    private List<AnnotationArgument> annotationArguments = new ArrayList<AnnotationArgument>();
    
    private boolean interop;
    
    public AnnotationInvocation() {
    }
    
    /**
     * The annotation constructor, if this is the invocation of an annotation constructor
     */
    public Method getConstructorDeclaration() {
        return constructorDeclaration;
    }
    
    public void setConstructorDeclaration(Method constructorDeclaration) {
        this.constructorDeclaration = constructorDeclaration;
    }
    
    /**
     * The parameters of the annotation constructor
     */
    public List<AnnotationConstructorParameter> getConstructorParameters() {
        return constructorParameters;
    }
    
    public int indexOfConstructorParameter(Parameter parameter) {
        int index = 0;
        for (AnnotationConstructorParameter acp : getConstructorParameters()) {
            if (acp.getParameter().equals(parameter)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /** 
     * The primary of the invocation: 
     * Either an annotation constructor ({@code Method}) 
     * or an annotation class ({@code Class}). 
     */
    public Declaration getPrimary() {
        return primary;
    }

    public void setPrimary(Class primary) {
        this.primary = primary;
    }
    
    public void setPrimary(Method primary) {
        this.primary = primary;
    }
    
    /**
     * The type of the annotation class ultimately being instantiated
     */
    public ProducedType getAnnotationClassType() {
        if (getPrimary() instanceof Class) {
            return ((Class)getPrimary()).getType();
        } else {
            // TODO Method may not be declared to return this!
            return ((Method)getPrimary()).getType();
        }
    }
    
    /**
     * The parameters of the primary
     */
    public List<Parameter> getParameters() {
        return ((Functional)primary).getParameterLists().get(0).getParameters();
    }
    
    /**
     * The parameters of the class ultimately being instantiated
     */
    public List<Parameter> getClassParameters() {
        return ((Class)getAnnotationClassType().getDeclaration()).getParameterList().getParameters();
    }

    /** 
     * The arguments of the invocation 
     */
    public List<AnnotationArgument> getAnnotationArguments() {
        return annotationArguments;
    }
    
    /**
     * True if this is an interop annotation constructor 
     * (i.e. one invented by the model loader for the purposes of being able 
     * to use a Java annotation).
     */
    public boolean isInterop() {
        return interop;
    }

    public void setInterop(boolean interop) {
        this.interop = interop;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getConstructorDeclaration() != null) {
            sb.append(getConstructorDeclaration().getName()).append("(");
            List<AnnotationConstructorParameter> ctorParams = getConstructorParameters();
            for (AnnotationConstructorParameter param : ctorParams) {
                sb.append(param).append(", ");
            }
            if (!ctorParams.isEmpty()) {
                sb.setLength(sb.length()-2);
            }
            sb.append(")\n\t=> ");
        }
        sb.append(primary != null ? primary.getName() : "NULL").append("{");
        for (AnnotationArgument argument : annotationArguments) {
            sb.append(argument).append(";\n");
        }
        return sb.append("}").toString();
    }
    
    /**
     * Make a type expression for the underlying annotation class
     */
    public JCExpression makeAnnotationType(ExpressionTransformer exprGen) {
        ProducedType type = getAnnotationClassType();
        if (isInterop()) {
            return exprGen.makeJavaType(type.getSatisfiedTypes().get(0));
        } else {
            return exprGen.makeJavaType(type, ExpressionTransformer.JT_ANNOTATION);
        }
    }
    
    public JCExpression makeAnnotation(
            ExpressionTransformer exprGen, 
            AnnotationInvocation ai, 
            com.sun.tools.javac.util.List<AnnotationFieldName> parameterPath) {
        ListBuffer<JCExpression> args = ListBuffer.<JCExpression>lb();
        for (AnnotationArgument aa : getAnnotationArguments()) {
            Parameter name = aa.getParameter();
            if (getPrimary() instanceof Method) {
                AnnotationInvocation annotationInvocation = (AnnotationInvocation)getConstructorDeclaration().getAnnotationConstructor();
                for (AnnotationArgument a2 : annotationInvocation.getAnnotationArguments()) {
                    if (a2.getTerm() instanceof ParameterAnnotationTerm) {
                        if (((ParameterAnnotationTerm)a2.getTerm()).getSourceParameter().equals(aa.getParameter())) {
                            name = a2.getParameter();
                            break;
                        }
                    }
                }
            }
            args.append(makeAnnotationArgument(exprGen, ai,
                    name,
                    parameterPath.append(aa), aa.getTerm()));
        }
        return exprGen.make().Annotation(makeAnnotationType(exprGen),
                args.toList());
    }

    /**
     * Make an annotation argument (@code member = value) for the given term
     */
    public JCExpression makeAnnotationArgument(ExpressionTransformer exprGen, AnnotationInvocation ai,
            Parameter bind,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath, AnnotationTerm term) {
        
        return exprGen.make().Assign(
                exprGen.naming.makeName(bind.getModel(), 
                        Naming.NA_ANNOTATION_MEMBER | Naming.NA_MEMBER),
                term.makeAnnotationArgumentValue(exprGen, ai,
                        fieldPath));
    }
    
    /**
     * Encode this invocation into a {@code @AnnotationInstantiation} 
     * (if the annotation constructors just calls the annotation class)
     * or {@code @AnnotationInstantiationTree} 
     * (if the annotation constructor calls another annotation constructor)
     */
    public JCAnnotation encode(AbstractTransformer gen, ListBuffer<JCExpression> instantiations) {
        ListBuffer<JCExpression> arguments = ListBuffer.lb();
        for (AnnotationArgument argument : getAnnotationArguments()) {
            arguments.append(gen.make().Literal(
                    argument.getTerm().encode(gen, instantiations)));
        }
        JCAnnotation atInstantiation = gen.make().Annotation(
                gen.make().Type(gen.syms().ceylonAtAnnotationInstantiationType),
                com.sun.tools.javac.util.List.<JCExpression>of(
                        gen.make().Assign(
                                gen.naming.makeUnquotedIdent("arguments"),
                                gen.make().NewArray(null, null, arguments.toList())),
                        gen.make().Assign(
                                gen.naming.makeUnquotedIdent("annotationClass"),
                                gen.naming.makeQualIdent(gen.makeJavaType(getAnnotationClassType()), "class"))
                ));
        if (instantiations.isEmpty()) {
            return atInstantiation;
        } else {
            return gen.make().Annotation(
                    gen.make().Type(gen.syms().ceylonAtAnnotationInstantiationTreeType),
                    com.sun.tools.javac.util.List.<JCExpression>of(
                            gen.make().NewArray(null, null, instantiations.prepend(atInstantiation).toList())));
        }
    }

    public void makeLiteralAnnotationFields(
            ExpressionTransformer exprGen,
            AnnotationInvocation toplevel,
            com.sun.tools.javac.util.List<AnnotationFieldName> fieldPath,
            ListBuffer<JCStatement> staticArgs) {
        for (AnnotationConstructorParameter acp : getConstructorParameters()) {
            AnnotationTerm defaultTerm = acp.getDefaultArgument();
            if (defaultTerm != null && 
                    this.equals(toplevel)) {
                defaultTerm.makeLiteralAnnotationFields(exprGen, toplevel, fieldPath.append(acp), staticArgs, acp.getParameter().getType());
            }
        }
        for (AnnotationArgument aa : getAnnotationArguments()) {
            aa.getTerm().makeLiteralAnnotationFields(exprGen, toplevel, fieldPath.append(aa), staticArgs, aa.getParameter().getType());
        }
    }
    
}
