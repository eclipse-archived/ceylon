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
import org.eclipse.ceylon.langtools.tools.javac.util.List;
import org.eclipse.ceylon.langtools.tools.javac.util.ListBuffer;


public class InvocationAnnotationTerm extends AnnotationTerm {

    AnnotationInvocation instantiation;

    public AnnotationInvocation getInstantiation() {
        return instantiation;
    }

    public void setInstantiation(AnnotationInvocation instantiation) {
        this.instantiation = instantiation;
    }
    
    @Override
    public String toString() {
        return String.valueOf(instantiation);
    }

    @Override
    public int encode(AbstractTransformer gen, ListBuffer<JCExpression> instantiations) {
        instantiations.add(instantiation.encode(gen, instantiations));
        return -instantiations.size();
    }

    @Override
    public JCExpression makeAnnotationArgumentValue(
            ExpressionTransformer exprGen, AnnotationInvocation ai,
            List<AnnotationFieldName> fieldPath) {
        return instantiation.makeAnnotation(exprGen, ai, fieldPath);
    }

    @Override
    public List<JCAnnotation> makeDpmAnnotations(ExpressionTransformer exprGen) {
        List<JCAnnotation> statics = getInstantiation().makeExprAnnotations(exprGen, null, List.<AnnotationFieldName>nil());
        if (statics == null) {
            statics = List.<JCAnnotation>nil();
        }
        return statics.prepend(exprGen.classGen().makeAtAnnotationInstantiation(getInstantiation()));
    }

    @Override
    public List<JCAnnotation> makeExprs(ExpressionTransformer exprGen,
            List<JCAnnotation> value) {
        // TODO Auto-generated method stub
        return value;
    }

    @Override
    public org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeExprAnnotations(
            ExpressionTransformer exprGen, AnnotationInvocation toplevel,
            org.eclipse.ceylon.langtools.tools.javac.util.List<AnnotationFieldName> fieldPath) {
        // Recurse to our instantiation, since it may have constants
        return getInstantiation().makeExprAnnotations(exprGen, toplevel, fieldPath);
    }

}
