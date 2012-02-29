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

import java.util.ArrayList;

import com.redhat.ceylon.compiler.java.codegen.AbstractTransformer.BoxingStrategy;
import com.redhat.ceylon.compiler.java.codegen.ExpressionTransformer.TermTransformer;
import com.redhat.ceylon.compiler.java.util.Decl;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedMemberExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.QualifiedTypeExpression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Term;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeCast;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;


public class InvocationBuilder {
    
    /** Abstracts over things with arguments, in particular 
     * {@link PositionalArguments positional argument invocations} 
     * and {@link CallableArguments callable invocations}
     */
    interface Arguments {
        /** Gets the number of arguments */
        public int getNumArguments();
        /** 
         * Gets the expression supplying the argument value for the 
         * given argument index 
         */
        public Expression getExpression(int argIndex);
        /**
         * Gets the transformed expression supplying the argument value for the 
         * given argument index
         */
        public JCExpression getTransformedExpression(int argIndex, boolean isRaw, java.util.List<ProducedType> typeArgumentModels);
        /**
         * Gets the parameter for the given argument index
         */
        public Parameter getParameter(int argIndex);
        public boolean hasEllipsis();
    }
    
    class PositionalArguments implements Arguments {
        private Tree.PositionalArgumentList positional;
        
        public PositionalArguments(Tree.PositionalArgumentList positional) {
            this.positional = positional;
        }
        @Override
        public Expression getExpression(int argIndex) {
            return positional.getPositionalArguments().get(argIndex).getExpression();
        }
        @Override
        public JCExpression getTransformedExpression(int argIndex, boolean isRaw, java.util.List<ProducedType> typeArgumentModels) {
            return gen.expressionGen().transformArg(
                    getExpression(argIndex), 
                    getParameter(argIndex), isRaw, typeArgumentModels);
        }
        @Override
        public Parameter getParameter(int argIndex) {
            return positional.getPositionalArguments().get(argIndex).getParameter();
        }
        @Override
        public int getNumArguments() {
            return positional.getPositionalArguments().size();
        }
        @Override
        public boolean hasEllipsis() {
            return positional.getEllipsis() != null;
        }
    }
    
    class CallableArguments implements Arguments {
        private java.util.List<Parameter> functionalParameters;
        private java.util.List<Parameter> declaredParameters;
        
        CallableArguments(Functional functionalParameter,
                java.util.List<Parameter> declaredParameters) {
            this.functionalParameters = functionalParameter.getParameterLists().get(0).getParameters();
            this.declaredParameters = declaredParameters;
        }
        
        @Override
        public Expression getExpression(int argIndex) {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public Parameter getParameter(int argIndex) {
            return functionalParameters.get(argIndex);
        }
        @Override
        public int getNumArguments() {
            return functionalParameters.size();
        }
        @Override
        public boolean hasEllipsis() {
            return false;
        }
        @Override
        public JCExpression getTransformedExpression(int argIndex,
                boolean isRaw,
                java.util.List<ProducedType> typeArgumentModels) {
            Parameter param = functionalParameters.get(argIndex);
            JCExpression argExpr;
            if (functionalParameters.size() <= 3) {
                // The Callable has overridden one of the non-varargs call() 
                // methods
                argExpr = gen.make().Ident(
                        gen.names().fromString("arg"+argIndex));
            } else {
                // The Callable has overridden the varargs call() method
                // so we need to index into the varargs array
                argExpr = gen.make().Indexed(
                        gen.make().Ident(gen.names().fromString("arg0")), 
                        gen.make().Literal(argIndex));
            }
            JCTypeCast cast = gen.make().TypeCast(gen.makeJavaType(param.getType(), AbstractTransformer.NO_PRIMITIVES), argExpr);
            
            JCExpression boxed = gen.boxUnboxIfNecessary(cast, true, 
                    param.getType(), declaredParameters.get(argIndex).getUnboxed() ? BoxingStrategy.UNBOXED : BoxingStrategy.BOXED);
            return boxed;
        }
    }
    
    private final AbstractTransformer gen;
    private Tree.InvocationExpression invocation;
    private final ProducedType returnType;
    private Tree.Primary primary;
    
    private Node node;
    private ListBuffer<JCVariableDecl> vars;
    private ListBuffer<JCExpression> args;
    private List<JCExpression> typeArgs;
    private String callVarName;
    
    private InvocationBuilder(AbstractTransformer gen, ProducedType returnType) {
        this.gen = gen;
        this.returnType = returnType;
    }
    
    public static InvocationBuilder invocation(AbstractTransformer gen, Tree.InvocationExpression invocation) {
        InvocationBuilder builder = new InvocationBuilder(gen, invocation.getTypeModel());
        builder.invocation = invocation;
        builder.primary = invocation.getPrimary();
        builder.node = invocation;
        if (invocation.getPositionalArgumentList() != null) {
            builder.transformPositionalInvocation();
        } else if (invocation.getNamedArgumentList() != null) {
            builder.transformNamedInvocation();
        } else {
            throw new RuntimeException("Illegal State");
        }
        return builder;
    }
    
    public static ProducedType getCallableReturnType(Tree.Term expr) {
        ProducedType typeModel = expr.getTypeModel();
        assert "ceylon.language.Callable".equals(typeModel.getDeclaration().getQualifiedNameString());
        return typeModel.getTypeArgumentList().get(0);
    }
    
    public static InvocationBuilder invocationForCallable(AbstractTransformer gen, Term expr, Functional parameter) {
        InvocationBuilder builder = new InvocationBuilder(gen, getCallableReturnType(expr));
        builder.node = expr;
        builder.transformForCallable(expr, parameter);
        return builder;
    }
    
    private void transformForCallable(Term expr, Functional functionalParameter) {
        java.util.List<Parameter> declaredParameters;
        if (expr instanceof Tree.Expression) {
            Term term = ((Tree.Expression)expr).getTerm();
            this.primary = (Tree.Primary)term;
            if (term instanceof Tree.MemberOrTypeExpression) {
                Tree.MemberOrTypeExpression bme = (Tree.MemberOrTypeExpression)term;
                Functional decl = (Functional)bme.getDeclaration();
                declaredParameters = decl.getParameterLists().get(0).getParameters();
            } else {
                throw new RuntimeException(term+"");
            }
        } else {
            throw new RuntimeException(expr+"");
        }
        
        java.util.List<ParameterList> parameterLists = functionalParameter.getParameterLists();
        java.util.List<TypeParameter> typeParameters = functionalParameter.getTypeParameters();
        ArrayList<ProducedType> typeArguments = new ArrayList<ProducedType>(typeParameters.size());
        for (TypeParameter typeParameter : typeParameters) {
            typeArguments.add(typeParameter.getType());
        }        
        computeCallInfo(expr.getTypeModel().getDeclaration(), 
                new CallableArguments(functionalParameter, declaredParameters), 
                typeArguments, parameterLists);
        
    }
    
    // Named invocation
    
    private void transformNamedInvocation() {
        ListBuffer<JCVariableDecl> vars = ListBuffer.lb();
        ListBuffer<JCExpression> args = ListBuffer.lb();

        java.util.List<ProducedType> typeArgumentModels = getTypeArguments(invocation);
        List<JCExpression> typeArgs = transformTypeArguments(typeArgumentModels);
        boolean isRaw = typeArgs.isEmpty();
        String callVarName = null;
        
        Declaration primaryDecl = ((Tree.MemberOrTypeExpression)invocation.getPrimary()).getDeclaration();
        if (primaryDecl != null) {
            java.util.List<ParameterList> paramLists = ((Functional)primaryDecl).getParameterLists();
            java.util.List<Tree.NamedArgument> namedArguments = invocation.getNamedArgumentList().getNamedArguments();
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
                        argExpr = gen.at(node).Apply(null, gen.makeQuotedQualIdent(gen.makeQuotedFQIdent(container.getQualifiedNameString()), className, methodName), arglist);
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
            
            Tree.SequencedArgument sequencedArgument = invocation.getNamedArgumentList().getSequencedArgument();
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

        this.vars = vars;
        this.args = args;
        this.typeArgs = typeArgs;
        this.callVarName = callVarName;
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
    
    private void transformPositionalInvocation() {
        Declaration primaryDecl = ((Tree.MemberOrTypeExpression)invocation.getPrimary()).getDeclaration();
        Tree.PositionalArgumentList positional = invocation.getPositionalArgumentList();
        java.util.List<ProducedType> typeArgumentModels = getTypeArguments(invocation);
        java.util.List<ParameterList> paramLists = ((Functional)primaryDecl).getParameterLists();
        computeCallInfo(primaryDecl, new PositionalArguments(positional), typeArgumentModels, paramLists);
    }

    
    private void computeCallInfo(
            Declaration primaryDecl,
            Arguments positionalArguments,
            java.util.List<ProducedType> typeArgumentModels,
            java.util.List<ParameterList> paramLists) {
        ListBuffer<JCVariableDecl> vars = null;
        ListBuffer<JCExpression> args = new ListBuffer<JCExpression>();
        String callVarName = null;
        List<JCExpression> typeArgs = transformTypeArguments(typeArgumentModels);
        boolean isRaw = typeArgs.isEmpty();
        java.util.List<Parameter> declaredParams = paramLists.get(0).getParameters();
        int numParameters = declaredParams.size();
        int numArguments = positionalArguments.getNumArguments();
        Parameter lastDeclaredParam = declaredParams.isEmpty() ? null : declaredParams.get(declaredParams.size() - 1); 
        if (lastDeclaredParam != null 
                && lastDeclaredParam.isSequenced()
                && !positionalArguments.hasEllipsis() // foo(sequence...) syntax => no need to box
                && numArguments >= (numParameters -1)) {
            // => call to a varargs method
            // first, append the normal args
            for (int ii = 0; ii < numParameters - 1; ii++) {
                args.append(positionalArguments.getTransformedExpression(ii, isRaw, typeArgumentModels));
            }
            JCExpression boxed;
            // then, box the remaining passed arguments
            if (numParameters -1 == numArguments) {
                // box as Empty
                boxed = gen.makeEmpty();
            } else {
                // box with an ArraySequence<T>
                List<Tree.Expression> x = List.<Tree.Expression>nil();
                for (int ii = numParameters - 1; ii < numArguments; ii++) {
                    x = x.append(positionalArguments.getExpression(ii));
                }
                boxed = gen.makeSequenceRaw(x);
            }
            args.append(boxed);
        } else if (numArguments < numParameters) {
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
            for (int ii = 0; ii < positionalArguments.getNumArguments(); ii++) {
                gen.at(positionalArguments.getExpression(ii));
                Tree.Expression expr = positionalArguments.getExpression(ii);
                Parameter declaredParam = positionalArguments.getParameter(ii);
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
                for (int ii = numArguments; ii < numParameters; ii++) {
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
                        argExpr = gen.at(node).Apply(null, gen.makeQuotedQualIdent(gen.makeQuotedFQIdent(container.getQualifiedNameString()), className, methodName), arglist);
                    } else {
                        argExpr = gen.makeEmpty();
                    }
                    BoxingStrategy boxType = Util.getBoxingStrategy(param);
                    ProducedType type = gen.getTypeForParameter(param, isRaw, typeArgumentModels);
                    JCExpression typeExpr = gen.makeJavaType(type, (boxType == BoxingStrategy.BOXED) ? AbstractTransformer.TYPE_ARGUMENT : 0);
                    JCVariableDecl varDecl = gen.makeVar(varName, typeExpr, argExpr);
                    vars.append(varDecl);
                }
                args.appendList(makeVarRefArgumentList(varBaseName, numParameters));
            } else {
                // For overloaded methods (and therefore Java interop) we just pass the arguments we have
                args.appendList(makeVarRefArgumentList(varBaseName, numArguments));
            }
        } else {
            // append the normal args
            for (int ii = 0; ii < positionalArguments.getNumArguments(); ii++) {
                args.append(positionalArguments.getTransformedExpression(ii, 
                        isRaw, typeArgumentModels));
            }
        }

        this.vars = vars;
        this.args = args;
        this.typeArgs = typeArgs;
        this.callVarName = callVarName;
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
    
    private JCExpression makeInvocation() {
        gen.at(node);
        JCExpression result = gen.expressionGen().transformPrimary(primary, new TermTransformer() {

            @Override
            public JCExpression transform(JCExpression primaryExpr, String selector) {
                JCExpression actualPrimExpr = null;
                if (vars != null && !vars.isEmpty() 
                        && primaryExpr != null 
                        && selector != null) {
                    // Prepare the first argument holding the primary for the call
                    JCExpression callVarExpr = gen.makeUnquotedIdent(callVarName);
                    ProducedType type = ((Tree.QualifiedMemberExpression)primary).getTarget().getQualifyingType();
                    JCVariableDecl callVar = gen.makeVar(callVarName, gen.makeJavaType(type, AbstractTransformer.NO_PRIMITIVES), primaryExpr);
                    vars.prepend(callVar);
                    actualPrimExpr = callVarExpr;
                } else {
                    actualPrimExpr = primaryExpr;
                }
                
                JCExpression resultExpr;
                if (primary instanceof Tree.BaseTypeExpression) {
                    ProducedType classType = (ProducedType)((Tree.BaseTypeExpression)primary).getTarget();
                    resultExpr = gen.make().NewClass(null, null, gen.makeJavaType(classType, AbstractTransformer.CLASS_NEW), args.toList(), null);
                } else if (primary instanceof Tree.QualifiedTypeExpression) {
                    resultExpr = gen.make().NewClass(actualPrimExpr, null, gen.makeQuotedIdent(selector), args.toList(), null);
                } else {
                    resultExpr = gen.make().Apply(typeArgs, gen.makeQuotedQualIdent(actualPrimExpr, selector), args.toList());
                }

                if (vars != null && !vars.isEmpty()) {
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

    public JCExpression build() {
        return makeInvocation();
    }
    
}
