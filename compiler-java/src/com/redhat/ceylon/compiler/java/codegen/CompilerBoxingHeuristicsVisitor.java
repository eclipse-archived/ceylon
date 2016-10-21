package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.java.codegen.Operators.OperatorTranslation;
import com.redhat.ceylon.compiler.java.codegen.Operators.OptimisationStrategy;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.InvocationExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;

public class CompilerBoxingHeuristicsVisitor extends BoxingHeuristicsVisitor {
    private AbstractTransformer transformer;
    
    public CompilerBoxingHeuristicsVisitor(AbstractTransformer transformer){
        this.transformer = transformer;
    }

    @Override
    protected boolean isInvocationExpressionOptimizable(InvocationExpression ce) {
        // FIXME: temporary hack for byte literals
        boolean ret = checkForByteLiterals(ce);
        if(ret)
            return true;
        // FIXME: temporary hack for bitwise operators literals
        ret = checkForBitwiseOperators(ce);
        if(ret)
            return true;
        return false;
    }

    private boolean checkForByteLiterals(Tree.InvocationExpression ce) {
        // same test as in BoxingVisitor.isByteLiteral()
        if(ce.getPrimary() instanceof Tree.BaseTypeExpression
                && ce.getPositionalArgumentList() != null){
            java.util.List<Tree.PositionalArgument> positionalArguments = ce.getPositionalArgumentList().getPositionalArguments();
            if(positionalArguments.size() == 1){
                PositionalArgument argument = positionalArguments.get(0);
                if(argument instanceof Tree.ListedArgument
                        && ((Tree.ListedArgument) argument).getExpression() != null){
                    Term term = ((Tree.ListedArgument)argument).getExpression().getTerm();
                    if(term instanceof Tree.NegativeOp){
                        term = ((Tree.NegativeOp) term).getTerm();
                    }
                    if(term instanceof Tree.NaturalLiteral){
                        Declaration decl = ((Tree.BaseTypeExpression)ce.getPrimary()).getDeclaration();
                        if(decl instanceof Class){
                            String name = decl.getQualifiedNameString();
                            if(name.equals("ceylon.language::Byte")){
                                // in the case of -128 to 127 we don't need to cast to byte by using an int literal, but only for
                                // assignment, not for method calls, so it's simpler to always cast
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean checkForBitwiseOperators(Tree.InvocationExpression ce) {
        if(!(ce.getPrimary() instanceof Tree.QualifiedMemberExpression))
            return false;
        Tree.QualifiedMemberExpression qme = (Tree.QualifiedMemberExpression) ce.getPrimary();
        // must be a positional arg (FIXME: why?)
        if(ce.getPositionalArgumentList() == null
                || ce.getPositionalArgumentList().getPositionalArguments() == null
                || ce.getPositionalArgumentList().getPositionalArguments().size() != 1)
            return false;
        Tree.PositionalArgument arg = ce.getPositionalArgumentList().getPositionalArguments().get(0);
        if(arg instanceof Tree.ListedArgument == false)
            return false;
        Tree.Expression right = ((Tree.ListedArgument)arg).getExpression();
        return checkForBitwiseOperators(ce, qme, right);
    }
    
    private boolean checkForBitwiseOperators(Tree.Term node, Tree.QualifiedMemberExpression qme, Tree.Term right) {
        // must be a call on Integer
        Tree.Term left = qme.getPrimary();
        if(left == null) {
            return false;
        }
        String signature;
        if (transformer.isCeylonInteger(left.getTypeModel())) {
            // must be a supported method/attribute
            String name = qme.getIdentifier().getText();
            signature = "ceylon.language.Integer."+name;
        } else if (transformer.isCeylonByte(left.getTypeModel())) {
            String name = qme.getIdentifier().getText();
            signature = "ceylon.language.Byte."+name;
        } else {
            return false;
        }
        // see if we have an operator for it
        OperatorTranslation operator = Operators.getOperator(signature);
        if(operator != null){
            if(operator.getArity() == 2){
                if(right == null)
                    return false;
                OptimisationStrategy optimisationStrategy = operator.getBinOpOptimisationStrategy(true, left, right, transformer);
                // check that we can optimise it
                if(!optimisationStrategy.useJavaOperator())
                    return false;
                
                return true;
            }else{
                // must be unary
                if(right != null)
                    return false;
                OptimisationStrategy optimisationStrategy = operator.getUnOpOptimisationStrategy(true, left, transformer);
                // check that we can optimise it
                if(!optimisationStrategy.useJavaOperator())
                    return false;
                
                return true;
            }
        }
        return false;
    }
    
}

