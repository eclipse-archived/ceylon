package com.redhat.ceylon.compiler.java.codegen;

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.java.codegen.ExpressionTransformer.TermTransformer;
import com.redhat.ceylon.compiler.java.util.Decl;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;


public class InvocationBuilder {
    private AbstractTransformer gen;
    private Tree.InvocationExpression invocation;
    
    public static InvocationBuilder invocation(AbstractTransformer gen, Tree.InvocationExpression invocation) {
        return new InvocationBuilder(gen, invocation);
    }
    
    private InvocationBuilder(AbstractTransformer gen, Tree.InvocationExpression invocation) {
        this.gen = gen;
        this.invocation = invocation;
    }
    
    public JCExpression build() {
        if (invocation.getPositionalArgumentList() != null) {
            return transformPositionalInvocation(invocation);
        } else if (invocation.getNamedArgumentList() != null) {
            return transformNamedInvocation(invocation);
        } else {
            throw new RuntimeException("Illegal State");
        }
    }
    
    // Named invocation
    
    private JCExpression transformNamedInvocation(final Tree.InvocationExpression ce) {
        ListBuffer<JCVariableDecl> vars = ListBuffer.lb();
        ListBuffer<JCExpression> args = ListBuffer.lb();

        java.util.List<ProducedType> typeArgumentModels = getTypeArguments(ce);
        List<JCExpression> typeArgs = transformTypeArguments(typeArgumentModels);
        boolean isRaw = typeArgs.isEmpty();
        String callVarName = null;
        
        Declaration primaryDecl = ce.getPrimary().getDeclaration();
        if (primaryDecl != null) {
            java.util.List<ParameterList> paramLists = ((Functional)primaryDecl).getParameterLists();
            java.util.List<Tree.NamedArgument> namedArguments = ce.getNamedArgumentList().getNamedArguments();
            java.util.List<Parameter> declaredParams = paramLists.get(0).getParameters();
            Parameter lastDeclared = declaredParams.size() > 0 ? declaredParams.get(declaredParams.size() - 1) : null;
            boolean boundSequenced = false;
            String varBaseName = gen.aliasName("arg");
            callVarName = varBaseName + "$callable$";
            
            int numDeclared = declaredParams.size();
            int numDeclaredFixed = (lastDeclared != null && lastDeclared.isSequenced()) ? numDeclared - 1 : numDeclared;
            int numPassed = namedArguments.size();
            int idx = 0;
            for (Tree.NamedArgument namedArg : namedArguments) {
                gen.at(namedArg);
                Tree.Expression expr = ((Tree.SpecifiedArgument)namedArg).getSpecifierExpression().getExpression();
                Parameter declaredParam = namedArg.getParameter();
                int index;
                BoxingStrategy boxType;
                ProducedType type;
                if (declaredParam != null) {
                    if (declaredParam.isSequenced()) {
                        boundSequenced = true;
                    }
                    index = declaredParams.indexOf(declaredParam);
                    boxType = Util.getBoxingStrategy(declaredParam);
                    type = gen.getTypeForParameter(declaredParam, isRaw, typeArgumentModels);
                } else {
                    // Arguments of overloaded methods don't have a reference to parameter
                    index = idx++;
                    boxType = BoxingStrategy.UNBOXED;
                    type = expr.getTypeModel();
                }
                String varName = varBaseName + "$" + index;
                // if we can't pick up on the type from the declaration, revert to the type of the expression
                if(gen.isTypeParameter(type))
                    type = expr.getTypeModel();
                JCExpression typeExpr = gen.makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? AbstractTransformer.TYPE_ARGUMENT : 0);
                JCExpression argExpr = gen.expressionGen().transformExpression(expr, boxType, type);
                JCVariableDecl varDecl = gen.makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            }
            
            if (!Decl.isOverloaded(primaryDecl) && numPassed < numDeclaredFixed) {
                boolean needsThis = false;
                if (Decl.withinClassOrInterface(primaryDecl)) {
                    // first append $this
                    ProducedType thisType = gen.getThisType(primaryDecl);
                    vars.append(gen.makeVar(varBaseName + "$this$", gen.makeJavaType(thisType, AbstractTransformer.NO_PRIMITIVES), gen.makeUnquotedIdent(callVarName)));
                    needsThis = true;
                }
                // append any arguments for defaulted parameters
                for (int ii = 0; ii < numDeclaredFixed; ii++) {
                    Parameter param = declaredParams.get(ii);
                    if (containsParameter(namedArguments, param)) {
                        continue;
                    }
                    String varName = varBaseName + "$" + ii;
                    String methodName = Util.getDefaultedParamMethodName(primaryDecl, param);
                    List<JCExpression> arglist = makeThisVarRefArgumentList(varBaseName, ii, needsThis);
                    JCExpression argExpr;
                    if (!param.isSequenced()) {
                        Declaration container = param.getDeclaration().getRefinedDeclaration();
                        if (!container.isToplevel()) {
                            container = (Declaration)container.getContainer();
                        }
                        String className = Util.getCompanionClassName(container.getName());
                        argExpr = gen.at(ce).Apply(null, gen.makeQuotedQualIdent(gen.makeQuotedQualIdentFromString(container.getQualifiedNameString()), className, methodName), arglist);
                    } else {
                        argExpr = gen.makeEmpty();
                    }
                    BoxingStrategy boxType = Util.getBoxingStrategy(param);
                    ProducedType type = gen.getTypeForParameter(param, isRaw, typeArgumentModels);
                    JCExpression typeExpr = gen.makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? AbstractTransformer.TYPE_ARGUMENT : 0);
                    JCVariableDecl varDecl = gen.makeVar(varName, typeExpr, argExpr);
                    vars.append(varDecl);
                }
            }
            
            Tree.SequencedArgument sequencedArgument = ce.getNamedArgumentList().getSequencedArgument();
            if (sequencedArgument != null) {
                gen.at(sequencedArgument);
                String varName = varBaseName + "$" + numDeclaredFixed;
                JCExpression typeExpr = gen.makeJavaType(lastDeclared.getType(), AbstractTransformer.WANT_RAW_TYPE);
                JCExpression argExpr = gen.makeSequenceRaw(sequencedArgument.getExpressionList().getExpressions());
                JCVariableDecl varDecl = gen.makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            } else if (lastDeclared != null 
                    && lastDeclared.isSequenced() 
                    && !boundSequenced) {
                String varName = varBaseName + "$" + numDeclaredFixed;
                JCExpression typeExpr = gen.makeJavaType(lastDeclared.getType(), AbstractTransformer.WANT_RAW_TYPE);
                JCVariableDecl varDecl = gen.makeVar(varName, typeExpr, gen.makeEmpty());
                vars.append(varDecl);
            }
            
            if (!Decl.isOverloaded(primaryDecl)) {
                args.appendList(makeVarRefArgumentList(varBaseName, numDeclared));
            } else {
                // For overloaded methods (and therefore Java interop) we just pass the arguments we have
                args.appendList(makeVarRefArgumentList(varBaseName, numPassed));
            }
        }

        return makeInvocation(ce, vars, args, typeArgs, callVarName);
    }
    
    private boolean containsParameter(java.util.List<Tree.NamedArgument> namedArguments, Parameter param) {
        for (Tree.NamedArgument namedArg : namedArguments) {
            Parameter declaredParam = namedArg.getParameter();
            if (param == declaredParam) {
                return true;
            }
        }
        return false;
    }

    // Positional invocation
    
    private JCExpression transformPositionalInvocation(Tree.InvocationExpression ce) {
        ListBuffer<JCVariableDecl> vars = null;
        ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();

        Declaration primaryDecl = ce.getPrimary().getDeclaration();
        Tree.PositionalArgumentList positional = ce.getPositionalArgumentList();

        java.util.List<ProducedType> typeArgumentModels = getTypeArguments(ce);
        List<JCExpression> typeArgs = transformTypeArguments(typeArgumentModels);
        boolean isRaw = typeArgs.isEmpty();
        String callVarName = null;
        
        java.util.List<ParameterList> paramLists = ((Functional)primaryDecl).getParameterLists();
        java.util.List<Parameter> declaredParams = paramLists.get(0).getParameters();
        int numDeclared = declaredParams.size();
        int numPassed = positional.getPositionalArguments().size();
        Parameter lastDeclaredParam = declaredParams.isEmpty() ? null : declaredParams.get(declaredParams.size() - 1); 
        if (lastDeclaredParam != null 
                && lastDeclaredParam.isSequenced()
                && positional.getEllipsis() == null // foo(sequence...) syntax => no need to box
                && numPassed >= (numDeclared -1)) {
            // => call to a varargs method
            // first, append the normal args
            for (int ii = 0; ii < numDeclared - 1; ii++) {
                Tree.PositionalArgument arg = positional.getPositionalArguments().get(ii);
                args.append(gen.expressionGen().transformArg(arg.getExpression(), arg.getParameter(), isRaw, typeArgumentModels));
            }
            JCExpression boxed;
            // then, box the remaining passed arguments
            if (numDeclared -1 == numPassed) {
                // box as Empty
                boxed = gen.makeEmpty();
            } else {
                // box with an ArraySequence<T>
                List<Tree.Expression> x = List.<Tree.Expression>nil();
                for (int ii = numDeclared - 1; ii < numPassed; ii++) {
                    Tree.PositionalArgument arg = positional.getPositionalArguments().get(ii);
                    x = x.append(arg.getExpression());
                }
                boxed = gen.makeSequenceRaw(x);
            }
            args.append(boxed);
        } else if (numPassed < numDeclared) {
            vars = ListBuffer.lb();
            String varBaseName = gen.aliasName("arg");
            callVarName = varBaseName + "$callable$";
            boolean needsThis = false;
            if (Decl.withinClassOrInterface(primaryDecl)) {
                // first append $this
                ProducedType thisType = gen.getThisType(primaryDecl);
                vars.append(gen.makeVar(varBaseName + "$this$", gen.makeJavaType(thisType, AbstractTransformer.NO_PRIMITIVES), gen.makeUnquotedIdent(callVarName)));
                needsThis = true;
            }
            // append the normal args
            int idx = 0;
            for (Tree.PositionalArgument arg : positional.getPositionalArguments()) {
                gen.at(arg);
                Tree.Expression expr = arg.getExpression();
                Parameter declaredParam = arg.getParameter();
                int index;
                BoxingStrategy boxType;
                ProducedType type;
                if (declaredParam != null) {
                    index = declaredParams.indexOf(declaredParam);
                    boxType = Util.getBoxingStrategy(declaredParam);
                    type = gen.getTypeForParameter(declaredParam, isRaw, typeArgumentModels);
                } else {
                    // Arguments of overloaded methods don't have a reference to parameter
                    index = idx++;
                    boxType = BoxingStrategy.UNBOXED;
                    type = expr.getTypeModel();
                }
                String varName = varBaseName + "$" + index;
                // if we can't pick up on the type from the declaration, revert to the type of the expression
                if(gen.isTypeParameter(type))
                    type = expr.getTypeModel();
                JCExpression typeExpr = gen.makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? AbstractTransformer.TYPE_ARGUMENT : 0);
                JCExpression argExpr = gen.expressionGen().transformExpression(expr, boxType, type);
                JCVariableDecl varDecl = gen.makeVar(varName, typeExpr, argExpr);
                vars.append(varDecl);
            }
            if (!Decl.isOverloaded(primaryDecl)) {
                // append any arguments for defaulted parameters
                for (int ii = numPassed; ii < numDeclared; ii++) {
                    Parameter param = declaredParams.get(ii);
                    String varName = varBaseName + "$" + ii;
                    String methodName = Util.getDefaultedParamMethodName(primaryDecl, param);
                    List<JCExpression> arglist = makeThisVarRefArgumentList(varBaseName, ii, needsThis);
                    JCExpression argExpr;
                    if (!param.isSequenced()) {
                        Declaration container = param.getDeclaration().getRefinedDeclaration();
                        if (!container.isToplevel()) {
                            container = (Declaration)container.getContainer();
                        }
                        String className = Util.getCompanionClassName(container.getName());
                        argExpr = gen.at(ce).Apply(null, gen.makeQuotedQualIdent(gen.makeQuotedQualIdentFromString(container.getQualifiedNameString()), className, methodName), arglist);
                    } else {
                        argExpr = gen.makeEmpty();
                    }
                    BoxingStrategy boxType = Util.getBoxingStrategy(param);
                    ProducedType type = gen.getTypeForParameter(param, isRaw, typeArgumentModels);
                    JCExpression typeExpr = gen.makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? AbstractTransformer.TYPE_ARGUMENT : 0);
                    JCVariableDecl varDecl = gen.makeVar(varName, typeExpr, argExpr);
                    vars.append(varDecl);
                }
                args.appendList(makeVarRefArgumentList(varBaseName, numDeclared));
            } else {
                // For overloaded methods (and therefore Java interop) we just pass the arguments we have
                args.appendList(makeVarRefArgumentList(varBaseName, numPassed));
            }
        } else {
            // append the normal args
            for (Tree.PositionalArgument arg : positional.getPositionalArguments()) {
                args.append(gen.expressionGen().transformArg(arg.getExpression(), arg.getParameter(), isRaw, typeArgumentModels));
            }
        }

        return makeInvocation(ce, vars, args, typeArgs, callVarName);
    }

    // Make a list of $arg0, $arg1, ... , $argN
    private List<JCExpression> makeVarRefArgumentList(String varBaseName, int argCount) {
        List<JCExpression> names = List.<JCExpression> nil();
        for (int i = 0; i < argCount; i++) {
            names = names.append(gen.makeUnquotedIdent(varBaseName + "$" + i));
        }
        return names;
    }

    // Make a list of $arg$this$, $arg0, $arg1, ... , $argN
    private List<JCExpression> makeThisVarRefArgumentList(String varBaseName, int argCount, boolean needsThis) {
        List<JCExpression> names = List.<JCExpression> nil();
        if (needsThis) {
            names = names.append(gen.makeUnquotedIdent(varBaseName + "$this$"));
        }
        names = names.appendList(makeVarRefArgumentList(varBaseName, argCount));
        return names;
    }

    private JCExpression makeInvocation(
            final Tree.InvocationExpression ce,
            final ListBuffer<JCVariableDecl> vars,
            final ListBuffer<JCExpression> args,
            final List<JCExpression> typeArgs,
            final String callVarName) {
        gen.at(ce);
        JCExpression result = gen.expressionGen().transformPrimary(ce.getPrimary(), new TermTransformer() {

            @Override
            public JCExpression transform(JCExpression primaryExpr, String selector) {
                JCExpression actualPrimExpr = null;
                if (vars != null && !vars.isEmpty() && primaryExpr != null && selector != null) {
                    // Prepare the first argument holding the primary for the call
                    JCExpression callVarExpr = gen.makeUnquotedIdent(callVarName);
                    ProducedType type = ((Tree.QualifiedMemberExpression)ce.getPrimary()).getTarget().getQualifyingType();
                    JCVariableDecl callVar = gen.makeVar(callVarName, gen.makeJavaType(type, AbstractTransformer.NO_PRIMITIVES), primaryExpr);
                    vars.prepend(callVar);
                    actualPrimExpr = callVarExpr;
                } else {
                    actualPrimExpr = primaryExpr;
                }
                
                JCExpression resultExpr;
                if (ce.getPrimary() instanceof Tree.BaseTypeExpression) {
                    ProducedType classType = (ProducedType)((Tree.BaseTypeExpression)ce.getPrimary()).getTarget();
                    resultExpr = gen.make().NewClass(null, null, gen.makeJavaType(classType, AbstractTransformer.CLASS_NEW), args.toList(), null);
                } else if (ce.getPrimary() instanceof Tree.QualifiedTypeExpression) {
                    resultExpr = gen.make().NewClass(actualPrimExpr, null, gen.makeQuotedIdent(selector), args.toList(), null);
                } else {
                    resultExpr = gen.make().Apply(typeArgs, gen.makeQuotedQualIdent(actualPrimExpr, selector), args.toList());
                }

                if (vars != null && !vars.isEmpty()) {
                    ProducedType returnType = ce.getTypeModel();
                    if (gen.isVoid(returnType)) {
                        // void methods get wrapped like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1); null)
                        return gen.make().LetExpr(vars.toList(), List.<JCStatement>of(gen.make().Exec(resultExpr)), gen.makeNull());
                    } else {
                        // all other methods like (let $arg$1=expr, $arg$0=expr in call($arg$0, $arg$1))
                        return gen.make().LetExpr(vars.toList(), resultExpr);
                    }
                } else {
                    return resultExpr;
                }
            }
            
        });
        return result;
    }

    // Invocation helper functions
    
    private java.util.List<ProducedType> getTypeArguments(Tree.InvocationExpression ce) {
        if(ce.getPrimary() instanceof Tree.StaticMemberOrTypeExpression){
            return ((Tree.StaticMemberOrTypeExpression)ce.getPrimary()).getTypeArguments().getTypeModels();
        }
        return null;
    }

    private List<JCExpression> transformTypeArguments(java.util.List<ProducedType> typeArguments) {
        List<JCExpression> result = List.<JCExpression> nil();
        if(typeArguments != null){
            for (ProducedType arg : typeArguments) {
                // cancel type parameters and go raw if we can't specify them
                if(gen.willEraseToObject(arg)
                        || gen.isTypeParameter(arg))
                    return List.nil();
                result = result.append(gen.makeJavaType(arg, AbstractTransformer.TYPE_ARGUMENT));
            }
        }
        return result;
    }
    
}
