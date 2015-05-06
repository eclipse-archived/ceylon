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
package com.redhat.ceylon.compiler.java.codegen.recovery;

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer;
import com.redhat.ceylon.compiler.java.codegen.ClassTransformer;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;


public class Errors {
    /** The instance of {@link Generate} */
    public static final Generate GENERATE = new Generate();
    
    private final Log log;
    private AbstractTransformer gen;
    private final DeclarationErrorVisitor declarationVisitor;
    private final StatementErrorVisitor statementVisitor;
    private final ExpressionErrorVisitor expressionVisitor;

    Errors(Context context) {
        log = CeylonLog.instance(context);
        gen = ClassTransformer.getInstance(context);
        statementVisitor = new StatementErrorVisitor();
        expressionVisitor = new ExpressionErrorVisitor();
        declarationVisitor = new DeclarationErrorVisitor(expressionVisitor);
    }
    
    public static Errors instance(Context context) {
        Errors instance = context.get(Errors.class);
        if (instance == null) {
            instance = new Errors(context);
            context.put(Errors.class, instance);
        }
        return instance;
    }
    
    private TransformationPlan annotateBrokenness(TransformationPlan result) {
        if (result!= GENERATE && gen.current() != null) {
            gen.current().broken();
        }
        return result;
    }
    
    private HasErrorException annotateBrokenness(HasErrorException result) {
        if (result!= null && gen.current() != null) {
            gen.current().broken();
        }
        return result;
    }

    
    public boolean hasAnyError(Tree.Declaration node) {
        return expressionVisitor.getFirstErrorMessage(node) != null;
    }
    
    /** Visit the given declaration (but not it's body, specifier or 
     * initializer or defaulted parameter expressions) and return true if 
     * there are any errors
     * WARNING: annotated the current decl as broken as side-effect
     */
    public TransformationPlan hasDeclarationAndMarkBrokenness(Tree.Declaration node) {
        return annotateBrokenness(declarationVisitor.getRecoveryPlan(node));
    }

    /** Visit the given declaration (but not it's body, specifier or 
     * initializer or defaulted parameter expressions) and return true if 
     * there are any errors
     */
    public TransformationPlan hasDeclarationError(Tree.Declaration node) {
        return declarationVisitor.getRecoveryPlan(node);
    }

    /**
     * Visit the given tree of expressions returning the first error found, 
     * or null if the tree is free of errors.
     * WARNING: annotated the current decl as broken as side-effect
     */
    public HasErrorException getFirstExpressionErrorAndMarkBrokenness(Tree.Term node) {
        return annotateBrokenness(expressionVisitor.getFirstErrorMessage(node));
    }

    /**
     * WARNING: annotated the current decl as broken as side-effect
     */
    public HasErrorException getFirstExpressionErrorAndMarkBrokenness(Tree.ExtendedType node) {
        return annotateBrokenness(expressionVisitor.getFirstErrorMessage(node));
    }
    
    public HasErrorException getFirstErrorBlock(Tree.Block block) {
        for (Tree.Statement stmt : block.getStatements()) {
            HasErrorException ex = getFirstErrorBlock(stmt);
            if (ex != null) {
                return ex;
            }
        }
        return null;
    }
    
    /**
     * Visit the given statement returning the first error found, 
     * or null if the tree is free of errors. Does not visit the 
     * statement block(s) or directives.
     */
    public HasErrorException getFirstErrorBlock(Tree.Statement blockStatement) {
        if (blockStatement instanceof Tree.Declaration) {
            HasErrorException r =  declarationVisitor.getFirstErrorMessage((Tree.Declaration)blockStatement);
            if (r == null && Decl.isLocal((Tree.Declaration)blockStatement)) {
                r = expressionVisitor.getFirstErrorMessage(blockStatement);
            }
            return annotateBrokenness(r);
        } else if (blockStatement instanceof Tree.ExecutableStatement) {
            // An executable statement
            return annotateBrokenness(statementVisitor.getFirstErrorMessage(blockStatement));
        }
        return null;
    }
    
    public HasErrorException getFirstErrorInitializer(Tree.Statement classBodyStatement) {
        if (classBodyStatement instanceof Tree.Declaration
                && Decl.isLocalToInitializer((Tree.Declaration)classBodyStatement)) {
            return annotateBrokenness(declarationVisitor.getFirstErrorMessage((Tree.Declaration)classBodyStatement));
        } else if (classBodyStatement instanceof Tree.ExecutableStatement) {
            // An executable statement
            return annotateBrokenness(statementVisitor.getFirstErrorMessage(classBodyStatement));
        }
        return null;
    }
}