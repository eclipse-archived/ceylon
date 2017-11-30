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
package org.eclipse.ceylon.compiler.java.codegen;

import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCAnnotation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;
import org.eclipse.ceylon.langtools.tools.javac.util.ListBuffer;
import org.eclipse.ceylon.model.loader.NamingBase.Prefix;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Parameter;

/**
 * An argument to an annotation class instantiation that is a parameter
 * of the annotation constructor.
 */
public class ParameterAnnotationTerm extends AnnotationTerm implements AnnotationFieldName {
    
    private Parameter sourceParameter;
    private boolean spread;

    /**
     * The annotation constructor parameter
     */
    public Parameter getSourceParameter() {
        return sourceParameter;
    }

    public void setSourceParameter(Parameter sourceParameter) {
        this.sourceParameter = sourceParameter;
    }

    /**
     * Whether the argument is spread
     */
    public boolean isSpread() {
        return spread;
    }

    public void setSpread(boolean spread) {
        this.spread = spread;
    }
    
    public String toString() {
        return (isSpread() ? "*" : "") + getSourceParameter().getName();
    }
    
    @Override
    public String getFieldName() {
        return sourceParameter.getName();
    }
    
    @Override
    public Prefix getFieldNamePrefix() {
        return Prefix.$default$;
    }

    @Override
    public Parameter getAnnotationField() {
        return getSourceParameter();
    }

    @Override
    public int encode(AbstractTransformer gen, ListBuffer<JCExpression> instantiations) {
        Parameter parameter = getSourceParameter();
        int index = ((Functional)parameter.getDeclaration()).getFirstParameterList().getParameters().indexOf(parameter);
        if (isSpread()) {
            index += 256;
        }
        return index;
    }

    @Override
    public JCExpression makeAnnotationArgumentValue(
            ExpressionTransformer exprGen, AnnotationInvocation ai,
            org.eclipse.ceylon.langtools.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        // The value of the argument is the value of the caller's argument
        // TODO WTF is going on here? surely this first loop should return, or do *something*?
        AnnotationTerm defaultArgument = null;
        for (AnnotationArgument aa : ai.getAnnotationArguments()) {
            if (aa.getParameter().equals(this.getSourceParameter())) {
                aa.getTerm().makeAnnotationArgumentValue(exprGen, ai, fieldPath);
            }
        }
        
        AnnotationConstructorParameter p = ai.findConstructorParameter(getSourceParameter());
        if (p != null) {
            defaultArgument = p.getDefaultArgument();
            if (defaultArgument != null) {
                return defaultArgument.makeAnnotationArgumentValue(exprGen, ai, org.eclipse.ceylon.langtools.tools.javac.util.List.<AnnotationFieldName>of(this));
            } else if (Strategy.hasEmptyDefaultArgument(p.getParameter())) {
                return exprGen.make().NewArray(null, null, org.eclipse.ceylon.langtools.tools.javac.util.List.<JCExpression>nil());
            }
        }
        return exprGen.makeErroneous(null, "compiler bug: not implemented yet");
    }

    @Override
    public org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeDpmAnnotations(
            ExpressionTransformer exprGen) {
        // TODO I suppose we can have a constructor like (X x, X y=x) so we do need to support this.
        // Or even (X x1, X x2, X[] x3=[x1, x2])
        return exprGen.makeAtParameterValue(makeLiteral(exprGen));
    }
    
    public JCExpression makeLiteral(ExpressionTransformer exprGen) {
        return exprGen.make().Literal(getSourceParameter().getName());
    }

    @Override
    public org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeExprs(
            ExpressionTransformer exprGen,
            org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeExprAnnotations(
            ExpressionTransformer exprGen, AnnotationInvocation toplevel,
            org.eclipse.ceylon.langtools.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        // TODO Auto-generated method stub
        return null;
    }

}