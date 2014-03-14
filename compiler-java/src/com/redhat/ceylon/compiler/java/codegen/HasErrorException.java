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

import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.util.List;

@SuppressWarnings("serial")
public class HasErrorException extends RuntimeException {

    private final Message message;
    private final Node node;
    
    public HasErrorException(Node that, Message message) {
        this.node = that;
        this.message = message;
    }
    
    public Message getErrorMessage() {
        return message;
    }
    
    public Node getErrorNode() {
        return node;
    }
    
    public JCThrow makeThrow(AbstractTransformer gen) {
        String errorMessage = getErrorMessage().getMessage();
        return gen.at(getErrorNode()).Throw(gen.make().NewClass(null, List.<JCExpression>nil(), 
                gen.make().QualIdent(gen.syms().ceylonUnresolvedCompilationErrorType.tsym), 
                List.<JCExpression>of(gen.make().Literal(errorMessage != null ? errorMessage : "compiler bug: error with unknown message")),
                null));
    }

}
