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

import java.util.List;

import com.redhat.ceylon.compiler.java.tools.CeylonLog;
import com.redhat.ceylon.compiler.loader.model.LazyClass;
import com.redhat.ceylon.compiler.typechecker.analyzer.UsageWarning;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.tree.Message;
import com.redhat.ceylon.compiler.typechecker.tree.NaturalVisitor;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;

public class Errors {

    private final Log log;
    private AbstractTransformer gen;
    private final DeclarationErrorVisitor declarationVisitor;
    private final StatementErrorVisitor statementVisitor;
    private final ErrorVisitor expressionVisitor;

    Errors(Context context) {
        log = CeylonLog.instance(context);
        gen = ClassTransformer.getInstance(context);
        declarationVisitor = new DeclarationErrorVisitor();
        statementVisitor = new StatementErrorVisitor();
        expressionVisitor = new ErrorVisitor();
    }
    
    static Errors instance(Context context) {
        Errors instance = context.get(Errors.class);
        if (instance == null) {
            instance = new Errors(context);
            context.put(Errors.class, instance);
        }
        return instance;
    }
    
    private void error(Node node, String message) {
        log.error(gen.getPosition(node), "ceylon.codegen.error", message);
    }
    
    class ErrorVisitor extends Visitor implements NaturalVisitor {
        
        public boolean hasErrors(Node target) {
            try{
                target.visit(this);
                return false;
            }catch(HasErrorException x){
                return true;
            }
        }
        
        public HasErrorException getFirstErrorMessage(Node target) {
            try{
                target.visit(this);
                return null;
            }catch(HasErrorException x){
                return x;
            }
        }
        
        @Override
        public void handleException(Exception e, Node that) {
            // rethrow
            throw (RuntimeException)e;
        }
        
        @Override
        public void visitAny(Node that) {
            // fast termination
            throwIfError(that);
            super.visitAny(that);
        }
        
        protected void throwIfError(Node that) {
            Message m = hasError(that);
            if (m != null) {
                throw new HasErrorException(that, m);
            }
        }
        
        private Message hasError(Node that) {
            // skip only usage warnings
            List<Message> errors = that.getErrors();
            for(int i=0,l=errors.size();i<l;i++){
                Message message = errors.get(i);
                if(!(message instanceof UsageWarning))
                    return message;
            }
            return null;
        }
    }
    
    class DeclarationErrorVisitor extends ErrorVisitor {
        
        @Override
        public void visit(Tree.Annotation that) {
            // We do care about errors in expressions in annotations: Those are
            // considerd part of the declaration because there's no way we 
            // can throw when the bad declaration is used.
            expressionVisitor.visitAny(that);
        }
        
        @Override
        public void visit(Tree.Body that) {
            // don't go there
        }
        
        @Override
        public void visit(Tree.SpecifierOrInitializerExpression that) {
            // don't go there
        }
        
        @Override
        public void visit(Tree.InitializerParameter that) {
            throwIfError(that);
            // don't visit children
        }
        
        public void visit(Tree.ExtendedType that) {
            that.getType().visit(this);
            // Don't visit the invocation expression unless our superclass is a 
            // Java class with a private ctor -- that could cause a javac 
            // error if we tried to generate a ctor for this class.
            if (that.getType().getDeclarationModel() instanceof LazyClass
                    && !((LazyClass)that.getType().getDeclarationModel()).isCeylon()) {
                boolean hasPrivateCtor = false;
                List<Declaration> overloads = ((LazyClass)that.getType().getDeclarationModel()).getOverloads();
                if (overloads != null) {
                    for (Declaration ctor : overloads) {
                        if (!ctor.isShared()) {
                            hasPrivateCtor = true;
                            break;
                        }
                    }
                }
                if (hasPrivateCtor) {
                    that.getInvocationExpression().visit(this);
                }
            }
        }
    }
    
    class StatementErrorVisitor extends ErrorVisitor {
        @Override
        public void visit(Tree.Block that) {
            // don't go there
        }
        
        @Override
        public void visit(Tree.Declaration that) {
            // don't go there
        }
        
        @Override
        public void visit(Tree.Variable that) {
            // unlike other declarations, Variables are part of the 
            // statement, and if they have a pb we're screwed, so we
            // *do* want to visit them.
            visitAny(that);
        }
    }
    
    /** Visit the given declaration (but not it's body, specifier or 
     * initializer or defaulted parameter expressions) and return true if 
     * there are any errors
     */
    public boolean hasDeclarationError(Tree.Declaration node) {
        return declarationVisitor.hasErrors(node);
    }
    
    /**
     * Visit the given tree of expressions returning the first error found, 
     * or null if the tree is free of errors.
     */
    public HasErrorException getFirstExpressionError(Tree.Term node) {
        return expressionVisitor.getFirstErrorMessage(node);
    }
    
    public HasErrorException getFirstExpressionError(Tree.ExtendedType node) {
        return expressionVisitor.getFirstErrorMessage(node);
    }
    
    /**
     * Visit the given statement returning the first error found, 
     * or null if the tree is free of errors. Does not visit the 
     * statement block(s) or directives.
     */
    public HasErrorException getFirstErrorBlock(Tree.Statement blockStatement) {
        if (blockStatement instanceof Tree.Declaration) {
            HasErrorException r =  declarationVisitor.getFirstErrorMessage(blockStatement);
            if (r == null && Decl.isLocal((Tree.Declaration)blockStatement)) {
                r = expressionVisitor.getFirstErrorMessage(blockStatement);
            }
            return r;
        } else if (blockStatement instanceof Tree.ExecutableStatement) {
            // An executable statement
            return statementVisitor.getFirstErrorMessage(blockStatement);
        }
        return null;
    }
    
    public HasErrorException getFirstErrorInitializer(Tree.Statement classBodyStatement) {
        if (classBodyStatement instanceof Tree.Declaration
                && Decl.isLocalToInitializer((Tree.Declaration)classBodyStatement)) {
            return declarationVisitor.getFirstErrorMessage(classBodyStatement);
        } else if (classBodyStatement instanceof Tree.ExecutableStatement) {
            // An executable statement
            return statementVisitor.getFirstErrorMessage(classBodyStatement);
        }
        return null;
    }
}