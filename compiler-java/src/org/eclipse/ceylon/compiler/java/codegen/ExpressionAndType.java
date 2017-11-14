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

import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;
import org.eclipse.ceylon.langtools.tools.javac.util.List;
import org.eclipse.ceylon.langtools.tools.javac.util.ListBuffer;

/**
 * A transformed expression and it's transformed type
 * @author tom
 */
class ExpressionAndType {

    final JCExpression expression;
    final JCExpression type;
    
    public ExpressionAndType(JCExpression expression, JCExpression type) {
        this.expression = expression;
        this.type = type;
    }
    
    /**
     * Returns a list of the types in the given list
     */
    public static List<JCExpression> toTypeList(Iterable<ExpressionAndType> exprAndTypes) {
        ListBuffer<JCExpression> lb = new ListBuffer<JCExpression>();
        for (ExpressionAndType arg : exprAndTypes) {
            lb.append(arg.type);
        }
        return lb.toList();
    }

    /**
     * Returns a list of the expressions in the given list
     */
    public static List<JCExpression> toExpressionList(Iterable<ExpressionAndType> exprAndTypes) {
        ListBuffer<JCExpression> lb = new ListBuffer<JCExpression>();
        for (ExpressionAndType arg : exprAndTypes) {
            lb.append(arg.expression);
        }
        return lb.toList();
    }
    
    public String toString() {
        return type + " " + expression;
    }
    
}
