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

import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCExpression;

public class ErroneousException extends Exception {

    private static final long serialVersionUID = -1315185846592489760L;
    
    private final Node node;

    public ErroneousException(Node node, String message, Throwable cause) {
        super(message, cause);
        this.node = node;
    }
    
    public ErroneousException(Node node, String message) {
        this(node, message, null);
    }
    
    public JCExpression makeErroneous(AbstractTransformer gen) {
        return gen.makeErroneous(node, getMessage());
    }
    
    public void logError(AbstractTransformer gen) {
        gen.log.error(gen.position(node), "ceylon", getMessage());
    }
}
