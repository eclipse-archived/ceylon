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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCAnnotation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCNewArray;
import org.eclipse.ceylon.langtools.tools.javac.util.ListBuffer;

public class CollectionLiteralAnnotationTerm extends LiteralAnnotationTerm {
    private final List<AnnotationTerm> elements;
    private final LiteralAnnotationTerm factory;
    public CollectionLiteralAnnotationTerm(LiteralAnnotationTerm factory) {
        super();
        this.factory = factory;
        this.elements = new ArrayList<AnnotationTerm>(); 
    }
    public boolean isTuple() {
        return factory == null;
    }
    public void addElement(AnnotationTerm element) {
        if (factory != null && !factory.getClass().isInstance(element)) {
            throw new RuntimeException("Different types in sequence " + factory.getClass() + " vs " + element.getClass());
        }
        elements.add(element);
    }
    @Override
    public org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeDpmAnnotations(
            ExpressionTransformer exprGen) {
        if (factory == null) {// A tuple
            // TODO @TupleValue({elements...})
        } else {// A sequence
            ListBuffer<JCExpression> lb = new ListBuffer<JCExpression>();
            for (AnnotationTerm term : elements) {
                lb.add(((LiteralAnnotationTerm)term).makeLiteral(exprGen));
            }
            JCNewArray array = exprGen.make().NewArray(null,  null,  lb.toList());
            return factory.makeAtValue(exprGen, null, array);
            
        }
        return org.eclipse.ceylon.langtools.tools.javac.util.List.<JCAnnotation>nil();
    }
    @Override
    public String toString() {
        return elements.toString();
    }
    @Override
    protected JCExpression makeLiteral(ExpressionTransformer exprGen) {
        ListBuffer<JCExpression> lb = new ListBuffer<JCExpression>();
        for (AnnotationTerm term : elements) {
            lb.add(((LiteralAnnotationTerm)term).makeLiteral(exprGen));
        }
        JCNewArray array = exprGen.make().NewArray(null,  null,  lb.toList());
        return array;
    }
    @Override
    protected org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeAtValue(
            ExpressionTransformer exprGen, String name, JCExpression value) {
        return factory.makeAtValue(exprGen, name, value);
    }
    @Override
    public org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> makeExprs(ExpressionTransformer exprGen, org.eclipse.ceylon.langtools.tools.javac.util.List<JCAnnotation> value) {
        return factory.makeExprs(exprGen, value);
    }
}