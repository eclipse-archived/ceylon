/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.js;

import static org.eclipse.ceylon.compiler.js.FunctionHelper.methodArgument;
import static org.eclipse.ceylon.compiler.js.SequenceGenerator.closeSequenceWithReifiedType;
import static org.eclipse.ceylon.compiler.js.SequenceGenerator.lazyEnumeration;
import static org.eclipse.ceylon.compiler.js.util.JsIdentifierNames.isTrueReservedWord;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.encodeCallableArgumentsAsParameterListForRuntime;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.encodeParameterListForRuntime;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.findSupertype;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.generateDynamicCheck;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.isNativeJs;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.matchTypeParametersWithArguments;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.printTypeArguments;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.wrapAsIterableArguments;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.eliminateParensAndWidening;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getContainingClassOrInterface;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isTypeUnknown;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.unionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.compiler.js.util.JsIdentifierNames;
import org.eclipse.ceylon.compiler.js.util.RetainedVars;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.TypeArguments;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.Reference;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.SiteVariance;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.model.Value;

/** Generates js code for invocation expression (named and positional). */
public class InvocationGenerator {

    private final GenerateJsVisitor gen;
    private final JsIdentifierNames names;
    private final RetainedVars retainedVars;

    InvocationGenerator(GenerateJsVisitor owner, JsIdentifierNames names, RetainedVars rv) {
        gen = owner;
        this.names = names;
        retainedVars = rv;
    }

    public void generateInvocation(Tree.InvocationExpression that) {
        if (that.getNamedArgumentList()!=null) {
            namedInvocation(that);
        } else if (that.getPositionalArgumentList()!=null) {
            positionalInvocation(that);
        }
    }

    private Map<TypeParameter,Type> getTypeArguments(Tree.Primary prim) {
        Tree.Term term = eliminateParensAndWidening(prim);
        if (term instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression smote = 
                    (Tree.StaticMemberOrTypeExpression) term;
            final Declaration dec = smote.getDeclaration();
            if (dec!=null) {
                Reference target = smote.getTarget();
                TypeArguments typeArgs = smote.getTypeArguments();
                if (ModelUtil.isConstructor(dec) || dec.isStatic()) {
                    //constructor or static method
                    Type qtype = target.getQualifyingType();
                    if (qtype.getDeclaration().isParameterized()) {
                        //if the member is static AND has type arguments 
                        //of its own, we need to merge them
                        Map<TypeParameter, Type> targs = new HashMap<>();
                        targs.putAll(target.getTypeArguments());
                        targs.putAll(qtype.getTypeArguments());
                        return targs;
                    }
                    return target.getTypeArguments();
                } else if (dec instanceof Functional) {
                    //function or class
                    Map<TypeParameter,Type> targs = 
                            matchTypeParametersWithArguments(
                                dec.getTypeParameters(),
                                typeArgs == null ? null :
                                    typeArgs.getTypeModels());
                    if (targs == null) {
                        gen.out("/*TARGS != TPARAMS!!!!*/");
                    }
                    return targs;
                } else if (dec instanceof Value) {
                    Type type = ((Value) dec).getType();
                    if (type!=null && type.isTypeConstructor()) {
                        //a generic function ref
                        return matchTypeParametersWithArguments(
                                type.getDeclaration()
                                    .getTypeParameters(),
                                typeArgs == null ? null :
                                    typeArgs.getTypeModels());
                    }
                }
            }
            return null;
        } else if (term instanceof Tree.ExtendedTypeExpression) {
            Tree.ExtendedTypeExpression ete = 
                    (Tree.ExtendedTypeExpression) term;
            return ete.getTarget().getTypeArguments();
        } else if (term instanceof Tree.InvocationExpression) {
            //a generic function ref returned by a function
            Type type = term.getTypeModel();
            if (type.getDeclaration().isAnonymous()) {
                //TODO: this is wrong if the return
                //      type of the function is an
                //      alias of the type constructor
                return type.getTypeArguments();
            }   
            else {
                return null;
            }
        } else {
            return null;
        }
    }

    private void namedInvocation(final Tree.InvocationExpression that) {
        final Tree.Primary primary = that.getPrimary();
        Tree.NamedArgumentList argList = that.getNamedArgumentList();
        
        boolean dynamic = gen.isInDynamicBlock() 
                && primary instanceof Tree.MemberOrTypeExpression
                && ((Tree.MemberOrTypeExpression)primary).getDeclaration() == null;
        
        if (dynamic) {
            final String fname = names.createTempVariable();
            gen.out("(", fname, "=");
            //Call a native js constructor passing a native js object as parameter
            if (primary instanceof Tree.QualifiedTypeExpression) {
                BmeGenerator.generateQte((Tree.QualifiedTypeExpression)primary, gen);
            } else {
                primary.visit(gen);
            }
            gen.out(",", fname, ".$$===undefined?new ", fname, "(");
            nativeObject(argList);
            gen.out("):", fname, "(");
            nativeObject(argList);
            gen.out("))");
        } else {
            gen.out("(");
            Map<String,String> argVarNames = defineNamedArguments(primary, argList);
            if (primary instanceof Tree.BaseMemberExpression) {
                BmeGenerator.generateBme((Tree.BaseMemberExpression)primary, gen);
            } else if (primary instanceof Tree.QualifiedTypeExpression) {
                BmeGenerator.generateQte((Tree.QualifiedTypeExpression)primary, gen);
            } else {
                primary.visit(gen);
            }
            if (primary instanceof Tree.MemberOrTypeExpression) {
                Tree.MemberOrTypeExpression mte = 
                        (Tree.MemberOrTypeExpression) primary;
                Declaration dec = mte.getDeclaration();
                if (dec instanceof Functional) {
                    Functional f = (Functional) dec;
                    applyNamedArguments(argList, f, argVarNames, 
                            gen.getSuperMemberScope(mte)!=null, 
                            getTypeArguments(primary));
                }
            }
            gen.out(")");
        }
    }

    private void positionalInvocation(final Tree.InvocationExpression that) {
        Tree.Primary primary = that.getPrimary();
        Type primaryType = primary.getTypeModel();
        Tree.PositionalArgumentList argList = 
                that.getPositionalArgumentList();
        List<Tree.PositionalArgument> positionalArgs = 
                argList.getPositionalArguments();
        Tree.PositionalArgument lastArg = 
                positionalArgs.isEmpty() ? null : 
                    positionalArgs.get(positionalArgs.size()-1);
        Unit unit = that.getUnit();
        
        //special cases relating to dynamic 
        if (gen.isInDynamicBlock()) {
            
            boolean dynamicInstantiation =  
                    primary instanceof Tree.BaseTypeExpression
                    && ((Tree.BaseTypeExpression)primary).getDeclaration() == null;
            if (dynamicInstantiation) {
                gen.out("(");
                //Could be a dynamic object, or a Ceylon one
                //We might need to call "new" so we need to get 
                //all the args to pass directly later
                final List<String> argnames = 
                        generatePositionalArguments(primary,
                                argList, positionalArgs, false, true);
                if (!argnames.isEmpty()) {
                    gen.out(",");
                }
                final String fname = names.createTempVariable();
                gen.out(fname,"=");
                primary.visit(gen);
                String theargs = "";
                if (!argnames.isEmpty()) {
                    theargs = argnames.toString().substring(1);
                    theargs = theargs.substring(0, theargs.length()-1);
                }
                gen.out(",", 
                        fname, ".$$===undefined?new ", 
                        fname, "(", theargs, "):", 
                        fname, "(", theargs, "))");
                //TODO we lose type args for now
                return;
            }
            
            if (primary instanceof Tree.BaseMemberExpression) {
                //handle a couple more dynamic-related special cases
                
                final Tree.BaseMemberExpression bme = 
                        (Tree.BaseMemberExpression) primary;
                Declaration dec = bme.getDeclaration();
                
                if (isPrintMethod(dec) && !positionalArgs.isEmpty()) {
                    //printing a value of unknown type
                    Tree.PositionalArgument printArg = positionalArgs.get(0);
                    if (isTypeUnknown(printArg.getTypeModel())) {
                        gen.out(gen.getClAlias(), "pndo$("); //#397
                        printArg.visit(gen);
                        gen.out(")");
                        return;
                    }
                }
                
                boolean dynamicallyTyped = 
                           dec == null 
                        || dec.isDynamic()
                        || dec instanceof TypedDeclaration 
                        && ((TypedDeclaration) dec).isDynamicallyTyped();
                if (dynamicallyTyped) {
                    if (lastArg instanceof Tree.SpreadArgument) {
                        Type lastArgType = lastArg.getTypeModel();
                        if (lastArgType==null || lastArgType.isUnknown()) {
                            //spreading something whose type we don't know
                            BmeGenerator.generateBme(bme, gen);
                            gen.out(".apply(0,");
                            if (positionalArgs.size()==1) {
                                generatePositionalArguments(primary,
                                        argList, positionalArgs, false, true);
                            } else {
                                gen.out("[");
                                ArrayList<Tree.PositionalArgument> subargs = 
                                        new ArrayList<>(positionalArgs.size());
                                subargs.addAll(positionalArgs);
                                subargs.remove(subargs.size()-1);
                                generatePositionalArguments(primary,
                                        argList, subargs, false, true);
                                gen.out("].concat(");
                                lastArg.visit(gen);
                                gen.out(")");
                            }
                            gen.out(")");
                            return;
                        }
                    }
                }
            }
        }
        
        boolean hasSpread = 
                lastArg instanceof Tree.SpreadArgument 
                && unit.isUnknownArgumentsCallable(primaryType)
                && !primaryType.isUnknown();
        if (hasSpread) {
            gen.out(gen.getClAlias(), "spread$2(");
        }
        
        if (primary instanceof Tree.BaseMemberExpression) {
            BmeGenerator.generateBme((Tree.BaseMemberExpression) primary, gen);
        } else if (primary instanceof Tree.QualifiedTypeExpression) {
            BmeGenerator.generateQte((Tree.QualifiedTypeExpression) primary, gen);
        } else {
            primary.visit(gen);
        }
        
        if (gen.opts.isOptimize() 
                && gen.getSuperMemberScope(primary) != null) {
            gen.out(".call(", 
                    names.self(getContainingClassOrInterface(
                            primary.getScope())));
            if (!positionalArgs.isEmpty()) {
                gen.out(",");
            }
        } else if (hasSpread) {
            gen.out(",");
        } else {
            gen.out("(");
        }
        
        //Check if args have params
        boolean fillInParams = !positionalArgs.isEmpty();
        for (Tree.PositionalArgument arg : positionalArgs) {
            fillInParams &= arg.getParameter() == null;
        }
        if (fillInParams 
                && unit.isCallableType(primaryType)) {
            //Get the callable and try to assign params from there
            List<Type> argTypes = 
                    unit.getCallableArgumentTypes(primaryType);
            boolean hasVariadic =
                    unit.isCallableVariadic(primaryType);
            
            Parameter p = null;
            int j=0;
            for (Tree.PositionalArgument arg : positionalArgs) {
                if (j<argTypes.size()) {
                    p = new Parameter();
                    p.setName("arg"+j);
                    p.setDeclaration(primaryType.getDeclaration());
                    Value v = new Value();
                    Scope scope = argList.getScope();
                    v.setContainer(scope);
                    v.setScope(scope);
                    v.setType(argTypes.get(j++));
                    p.setModel(v);
                    p.setSequenced(hasVariadic && j==argTypes.size());
                }
                arg.setParameter(p);
            }
        }
        
        generatePositionalArguments(primary, 
                argList, positionalArgs, false, false);
        
        final Map<TypeParameter, Type> targs = 
                getTypeArguments(primary);
        if (targs!=null && !targs.isEmpty()) {
            if (!positionalArgs.isEmpty()) {
                gen.out(",");
            }
            if (primary instanceof Tree.MemberOrTypeExpression) {
                Tree.MemberOrTypeExpression ref = 
                        (Tree.MemberOrTypeExpression)
                            primary;
                Declaration dec = ref.getDeclaration();
                if (dec instanceof Functional) {
                    Functional bmed = (Functional)dec;
                    //If there are fewer arguments than there are parameters...
                    final int argsSize = positionalArgs.size();
                    int paramSize = 
                            bmed.getFirstParameterList()
                                .getParameters().size();
                    int paramArgDiff = paramSize - argsSize;
                    if (paramArgDiff > 0) {
                        if (lastArg instanceof Tree.Comprehension 
                         || lastArg instanceof Tree.SpreadArgument) {
                            paramArgDiff--;
                        }
                        for (int i=0; i<paramArgDiff; i++) {
                            //pad missing args with 'undefined'
                            gen.out("undefined,");
                        }
                    }
                }
            }
            printTypeArguments(primary, gen, false, targs, null);
        }
        
        gen.out(")");
    }

    private static boolean isPrintMethod(Declaration dec) {
        return dec!=null 
            && dec.getUnit().getPackage().isLanguagePackage()
            && "print".equals(dec.getName());
    }

    /** Generates the code to evaluate the expressions in the named 
     * argument list and assign them to variables, then returns a map 
     * with the parameter names and the corresponding variable names. */
    Map<String, String> defineNamedArguments(Tree.Primary primary, 
            Tree.NamedArgumentList argList) {
        Map<String, String> argVarNames = new HashMap<>();
        for (Tree.NamedArgument arg: argList.getNamedArguments()) {
            Parameter p = arg.getParameter();
            final String paramName = 
                    p==null && gen.isInDynamicBlock() ? 
                        arg.getIdentifier().getText() : 
                        arg.getParameter().getName();
            String varName = names.createTempVariable();
            argVarNames.put(paramName, varName);
            retainedVars.add(varName);
            gen.out(varName, "=");
            
            if (arg instanceof Tree.MethodArgument) {
                Tree.MethodArgument marg = 
                        (Tree.MethodArgument) arg;
                gen.out(gen.getClAlias(), "f2$(");
                methodArgument(marg, gen);
                gen.out(",");
                //Add parameters
                encodeParameterListForRuntime(true, arg, 
                        marg.getParameterLists()
                            .get(0)
                            .getModel(), 
                        gen);
                gen.out(",");
                printTypeArguments(arg, 
                        marg.getDeclarationModel()
                            .getType()
                            .getFullType(), 
                        gen, false);
                gen.boxUnboxEnd(4);
            } else {
                arg.visit(gen);
            }
            gen.out(",");
        }
        
        Tree.SequencedArgument sarg = argList.getSequencedArgument();
        if (sarg!=null) {
            String paramName = sarg.getParameter().getName();
            String varName = names.createTempVariable();
            argVarNames.put(paramName, varName);
            retainedVars.add(varName);
            gen.out(varName, "=");
            generatePositionalArguments(primary, argList, 
                    sarg.getPositionalArguments(), true, false);
            gen.out(",");
        }
        
        return argVarNames;
    }

    void applyNamedArguments(Tree.NamedArgumentList argList, Functional func,
                Map<String, String> argVarNames, boolean superAccess, 
                Map<TypeParameter, Type> targs) {
        final ParameterList plist = func.getFirstParameterList();
        boolean first = true;
        if (superAccess) {
            gen.out(".call(this");
            if (!plist.getParameters().isEmpty()) { gen.out(","); }
        }
        else {
            gen.out("(");
        }
        for (Parameter p : plist.getParameters()) {
            if (!first) gen.out(",");
            final String vname = argVarNames.get(p.getName());
            if (vname == null) {
                if (p.isDefaulted()) {
                    gen.out("undefined");
                } else {
                    gen.out(gen.getClAlias(), "empty()");
                }
            } else {
                gen.out(vname);
            }
            first = false;
        }
        if (targs != null && !targs.isEmpty()) {
            if (!first){
                gen.out(",");
            }
            printTypeArguments(argList, gen, false, targs, null);
        }
        gen.out(")");
    }

    /** Generate a list of PositionalArguments, optionally assigning a variable name to each one
     * and returning the variable names. */
    List<String> generatePositionalArguments(
            final Tree.Primary primary, 
            final Tree.ArgumentList that,
            final List<Tree.PositionalArgument> args,
            final boolean forceSequenced, 
            final boolean generateVars) {
        if (args.isEmpty()) {
            return Collections.emptyList();
        }
        Unit unit = that.getUnit();
        final List<String> argvars = 
                new ArrayList<>(args.size());
        boolean first = true;
        boolean opened = false;
        Type sequencedType=null;
        for (Tree.PositionalArgument arg : args) {
            Tree.Expression expr;
            final Parameter pd = arg.getParameter();
            Type argType = arg.getTypeModel();
            if (arg instanceof Tree.ListedArgument) {
                if (!first) gen.out(",");
                expr = ((Tree.ListedArgument) arg).getExpression();
                Type exprType = expr.getTypeModel();
                boolean dynamic = 
                        gen.isInDynamicBlock() 
                            && pd != null 
                            && !isTypeUnknown(pd.getType())
                            && exprType.containsUnknowns();
                if (forceSequenced || pd!=null && pd.isSequenced()) {
                    if (dynamic) {
                        //We don't have a real type so get the one declared in the parameter
                        exprType = pd.getType();
                    }
                    Type elemtype;
                    if (pd == null 
                            || pd.getType() != null 
                            && pd.getType().getTypeArgumentList().isEmpty()) {
                        elemtype = exprType;
                    } else if (pd != null 
                            && pd.getType() == null 
                            && pd.getModel() != null 
                            && pd.getModel().getContainer() instanceof Value) {
                        elemtype = ((Value)pd.getModel().getContainer()).getType();
                    } else {
                        elemtype = pd.getType().getTypeArgumentList().get(0);
                        if (!elemtype.isTypeParameter()) {
                            elemtype = exprType;
                        }
                    }
                    
                    if (sequencedType == null) {
                        sequencedType = elemtype;
                    } else {
                        sequencedType = unionType(elemtype, sequencedType, unit);
                    }
                    
                    if (!opened) {
                        if (generateVars) {
                            final String argvar = names.createTempVariable();
                            argvars.add(argvar);
                            gen.out(argvar, "=");
                        }
                        if (exprType.isSequential() 
                                || pd!=null && pd.isSequenced()) {
                            gen.out(gen.getClAlias(), "$arr$sa$([");
                        } else {
                            int argpos = args.indexOf(arg);
                            ArrayList<Tree.PositionalArgument> seqargs = 
                                    new ArrayList<>(args.size()-argpos+1);
                            for (;argpos<args.size();argpos++) {
                                seqargs.add(args.get(argpos));
                            }
                            final Tree.PositionalArgument lastArg = 
                                    seqargs.get(seqargs.size()-1);
                            if (sequencedType.isTypeParameter()) {
                                sequencedType = unit.getIterableType(sequencedType);
                            }
                            lazyEnumeration(seqargs, primary, sequencedType,
                                       lastArg instanceof Tree.SpreadArgument 
                                    || lastArg instanceof Tree.Comprehension, 
                                    gen);
                            return argvars;
                        }
                    }
                    opened=true;
                } else if (generateVars) {
                    final String argvar = names.createTempVariable();
                    argvars.add(argvar);
                    gen.out(argvar, "=");
                }
                final int boxType = 
                        pd==null ? 0 :
                            gen.boxUnboxStart(expr.getTerm(), 
                                    isNativeJs(pd.getModel()), 
                                    true, false);
                Map<TypeParameter,Type> targs = null;
                if (dynamic) {
                    if (primary instanceof Tree.MemberOrTypeExpression) {
                        targs = ((Tree.MemberOrTypeExpression)primary)
                                    .getTarget()
                                    .getTypeArguments();
                    }
                    generateDynamicCheck(expr, pd.getType(), gen, false, targs);
                } else {
                    arg.visit(gen);
                }
                if (boxType == 4) {
                    gen.out(",");
                    //Add parameters
                    describeMethodParameters(expr.getTerm());
                    gen.out(",");
                    targs = argType.getTypeArguments();
                    if (arg instanceof Tree.ListedArgument) {
                        Tree.Term argTerm = ((Tree.ListedArgument)arg)
                                .getExpression().getTerm();
                        if (argTerm instanceof Tree.MemberOrTypeExpression) {
                            targs = ((Tree.MemberOrTypeExpression)argTerm)
                                        .getTarget()
                                        .getTypeArguments();
                        }
                    }
                    printTypeArguments(arg, gen, false,
                            targs, argType.getVarianceOverrides());
                }
                gen.boxUnboxEnd(boxType);
            } else if (arg instanceof Tree.SpreadArgument 
                    || arg instanceof Tree.Comprehension) {
                final boolean isSpreadArg = 
                        arg instanceof Tree.SpreadArgument;
                if (isSpreadArg) {
                    expr = ((Tree.SpreadArgument) arg).getExpression();
                } else {
                    expr = null;
                }
                boolean chained=false;
                if (opened) {
                    closeSequenceWithReifiedType(that,
                            wrapAsIterableArguments(sequencedType), gen, false);
                    gen.out(".chain(");
                    sequencedType=null;
                    chained=true;
                } else if (!first) {
                    gen.out(",");
                }
                if (isSpreadArg) {
                    generateSpreadArgument(primary, 
                            (Tree.SpreadArgument) arg, expr, pd);
                } else {
                    arg.visit(gen);
                    if (pd != null 
                            && pd.getType().isSequential() 
                            && !argType.isSequential() 
                            && !argType.isUnknown()) {
                        gen.out(".sequence()");
                    }
                }
                if (opened) {
                    gen.out(",");
                    Map<TypeParameter,Type> targs;
                    Map<TypeParameter, SiteVariance> variances;
                    Interface iterableDec = unit.getIterableDeclaration();
                    Interface sequentialDec = unit.getSequentialDeclaration();
                    if (expr == null) {
                        //it's a comprehension
                        targs = wrapAsIterableArguments(argType);
                        variances = null;
                    } else {
                        Type exprType = expr.getTypeModel();
                        Type spreadType = findSupertype(sequentialDec, exprType);
                        if (spreadType == null) {
                            //Go directly to Iterable
                            spreadType = findSupertype(iterableDec, exprType);
                        }
                        targs = spreadType.getTypeArguments();
                        variances = spreadType.getVarianceOverrides();
                    }
                    if (chained) {
                        Type[] tlist = new Type[2];
                        for (TypeParameter tp : targs.keySet()) {
                            String tpname = tp.getName();
                            switch (tpname) {
                                case "Element": tlist[0] = targs.get(tp); break;
                                case "Absent": tlist[1] = targs.get(tp); break;
                            }
                        }
                        if (tlist[1] == null) {
                            tlist[1] = unit.getNothingType();
                        }
                        Function cdec = (Function)
                                iterableDec.getMember("chain", null, false);
                        targs = matchTypeParametersWithArguments(
                                    cdec.getTypeParameters(), 
                                    Arrays.asList(tlist));
                    }
                    printTypeArguments(that, gen, false, targs, variances);
                    gen.out(")");
                    if (chained) {
                        gen.out(".sequence()");
                    }
                }
                if (arg instanceof Tree.Comprehension) {
                    break;
                }
            }
            first = false;
        }
        if (sequencedType != null) {
            final Map<TypeParameter,Type> seqtargs;
            seqtargs = forceSequenced && !args.isEmpty() ? 
                    unit.getNonemptyIterableType(sequencedType)
                        .getTypeArguments() : 
                    wrapAsIterableArguments(sequencedType);
            closeSequenceWithReifiedType(primary, seqtargs, gen, false);
        }
        return argvars;
    }

    private void generateSpreadArgument(final Tree.Primary primary,
            final Tree.SpreadArgument arg, Tree.Expression expr,
            final Parameter pd) {
        Unit unit = arg.getUnit();
        TypedDeclaration td = pd == null ? null : pd.getModel();
        int boxType = 
                gen.boxUnboxStart(expr.getTerm(), 
                        isNativeJs(td), 
                        true, true);
        Type argType = arg.getTypeModel();
        if (boxType == 4) {
            arg.visit(gen);
            gen.out(",");
            describeMethodParameters(expr.getTerm());
            gen.out(",");
            printTypeArguments(arg, argType, gen, false);
        } else if (pd == null) {
            final Declaration primDec = 
                    primary instanceof Tree.MemberOrTypeExpression ? 
                        ((Tree.MemberOrTypeExpression)primary).getDeclaration() : 
                        null;
            if (gen.isInDynamicBlock() 
                    && primary instanceof Tree.MemberOrTypeExpression
                    && (primDec == null 
                        || primDec.isDynamic() 
                        || primDec instanceof TypedDeclaration 
                            && ((TypedDeclaration) primDec).isDynamicallyTyped())
                    && argType != null 
                    && argType.getDeclaration()
                            .inherits(unit.getTupleDeclaration())) {
                //Spread dynamic parameter
                Type targ = argType.getTypeArgumentList().get(2);
                arg.visit(gen);
                gen.out(".$_get(0)");
                int i = 1;
                while (!targ.isSubtypeOf(unit.getEmptyType())) {
                    gen.out(",");
                    arg.visit(gen);
                    gen.out(".$_get(" + (i++) + ")");
                    targ = targ.getTypeArgumentList().get(2);
                }
            } else {
                arg.visit(gen);
            }
        } else if (pd.isSequenced()) {
            arg.visit(gen);
            if (!unit.isSequentialType(argType)) {
                gen.out(".sequence()");
            }
        } else if (!argType.isEmpty()) {
            final String specialSpreadVar = 
                    gen.getNames().createTempVariable();
            gen.out("(", specialSpreadVar, "=");
            arg.visit(gen);
            final boolean unknownSpread = argType.isUnknown();
            final String get0 = unknownSpread ?"[":".$_get(";
            final String get1 = unknownSpread ?"]":")";
            if (!unknownSpread 
                    && !unit.isSequentialType(argType)) {
                gen.out(".sequence()");
            }
            gen.out(",");
            if (pd.isDefaulted()) {
                gen.out(gen.getClAlias(), "nn$(",
                        specialSpreadVar, get0, "0", get1, ")?", 
                        specialSpreadVar, get0, "0", get1, ":undefined)");
            } else {
                gen.out(specialSpreadVar, get0, "0", get1, ")");
            }
            
            //Find out if there are more params
            final List<Parameter> moreParams;
            final Declaration pdd = pd.getDeclaration();
            boolean found = false;
            if (pdd instanceof Functional) {
                moreParams = ((Functional) pdd).getFirstParameterList()
                                .getParameters();
            } else {
                //Check the parameters of the primary 
                //(obviously a callable, so this is a Tuple)
                Type callableType = primary.getTypeModel();
                List<Type> paramTypes = 
                        unit.getCallableArgumentTypes(
                                callableType);
                boolean variadic = unit.isCallableVariadic(callableType);
                int required = unit.getCallableRequiredParamCount(callableType);
                int size = paramTypes.size();
                moreParams = new ArrayList<>(size);
                for (int i=1; i<size; i++) {
                    Type paramType = paramTypes.get(i);
                    Parameter p = new Parameter();
                    p.setName("arg"+i);
                    Value v = new Value();
                    v.setType(paramType);
                    p.setModel(v);
                    p.setSequenced(variadic && i==size-1);
                    p.setDefaulted(i>=required);
                    moreParams.add(p);
                }
                found = true;
            }
            
            if (moreParams != null) {
                int c = 1;
                for (Parameter restp : moreParams) {
                    if (found) {
                        final String cs = Integer.toString(c++);
                        if (restp.isDefaulted()) {
                            gen.out(",", gen.getClAlias(), "nn$(", 
                                    specialSpreadVar, get0, cs, get1, ")?", 
                                    specialSpreadVar, get0, cs, get1, ":undefined");
                        } else if (restp.isSequenced()) {
                            if (c == 2) {
                                gen.out(",", specialSpreadVar, ".rest");
                            } else {
                                gen.out(",", specialSpreadVar, ".sublistFrom(", cs, ")");
                            }
                        } else {
                            gen.out(",", specialSpreadVar, get0, cs, get1);
                        }
                    } else {
                        found = restp.equals(pd);
                    }
                }
            }
        }
        gen.boxUnboxEnd(boxType);
    }

    /** Generate the code to create a native js object. */
    void nativeObject(Tree.NamedArgumentList argList) {
        final List<Tree.NamedArgument> nargs = argList.getNamedArguments();
        Tree.SequencedArgument seqArg = argList.getSequencedArgument();
        if (seqArg == null) {
            ArrayList<Tree.NamedArgument> getters = null;
            for (Tree.NamedArgument arg : nargs) {
                if (arg instanceof Tree.AttributeArgument) {
                    if (getters == null) {
                        getters = new ArrayList<>(nargs.size());
                    }
                    getters.add(arg);
                }
            }
            final String tmpobjvar = getters == null ? null : 
                    gen.createRetainedTempVar();
            if (getters != null) {
                gen.out("(", tmpobjvar, "=");
            }
            gen.out("{");
            boolean first = true;
            for (Tree.NamedArgument arg : nargs) {
                if (!(arg instanceof Tree.AttributeArgument)) {
                    if (first) { first = false; } else { gen.out(","); }
                    String argName = arg.getIdentifier().getText();
                    if (isTrueReservedWord(argName)) {
                        gen.out("\"", argName, "\"");
                    }
                    else {
                        gen.out(argName);
                    }
                    gen.out(":");
                    arg.visit(gen);
                }
            }
            gen.out("}");
            if (getters != null) {
                for (Tree.NamedArgument arg : getters) {
                    if (first) { first = false; } else { gen.out(","); }
                    gen.out("Object.defineProperty(", tmpobjvar, ",'");
                    if (arg instanceof Tree.AttributeArgument) {
                        Tree.AttributeArgument att = 
                                (Tree.AttributeArgument) arg;
                        String argName = arg.getIdentifier().getText();
                        if (isTrueReservedWord(argName)) {
                            gen.out("\"", argName, "\"");
                        }
                        else {
                            gen.out(argName);
                        }
                        gen.out("',{get:function(){ return ");
                        gen.visitSingleExpression(att.getSpecifierExpression()
                                .getExpression());
                        gen.out("},configurable:true,enumerable:true})");
                    }
                }
                gen.out(",", tmpobjvar, ")");
            }
        } else {
            String arr = null;
            boolean isComp = false;
            boolean isSpread = seqArg != null 
                    && !seqArg.getPositionalArguments().isEmpty() 
                    && seqArg.getPositionalArguments().get(
                            seqArg.getPositionalArguments().size()-1) 
                        instanceof Tree.SpreadArgument;
            if (nargs.size() > 0) {
                gen.out("function()");
                gen.beginBlock();
                arr = names.createTempVariable();
                gen.out("var ", arr, "=");
            } else {
                isComp = seqArg.getPositionalArguments().size() == 1
                        && seqArg.getPositionalArguments().get(0) 
                            instanceof Tree.Comprehension;
            }
            if (isComp) {
                gen.out(gen.getClAlias(), "nfor$(");
            } else if (isSpread) {
                gen.out(gen.getClAlias(), "tpl$([");
            } else {
                gen.out("[");
            }
            boolean first = true;
            for (Tree.PositionalArgument arg : seqArg.getPositionalArguments()) {
                if (arg instanceof Tree.SpreadArgument) {
                    gen.out("],");
                    arg.visit(gen);
                    gen.out(").nativeArray()");
                } else {
                    if (first) { first = false; } else { gen.out(","); }
                    arg.visit(gen);
                }
            }
            if (isComp) {
                gen.out(")");
            } else if (!isSpread) {
                gen.out("]");
            }
            if (nargs.size() > 0) {
                gen.endLine(true);
                for (Tree.NamedArgument arg : nargs) {
                    gen.out(arr, ".", arg.getIdentifier().getText(), "=");
                    arg.visit(gen);
                    gen.endLine(true);
                }
                gen.out("return ", arr,";");
                gen.endBlock();
                gen.out("()");
            }
        }
    }

    void describeMethodParameters(Tree.Term term) {
        ParameterList plist = null;
        if (term instanceof Tree.FunctionArgument) {
            plist = ((Function)(((Tree.FunctionArgument)term).getDeclarationModel()))
                        .getFirstParameterList();
        } else if (term instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mote = 
                    (Tree.MemberOrTypeExpression) term;
            if (mote.getStaticMethodReference()) {
                plist = new ParameterList();
                Parameter param = new Parameter();
                plist.getParameters().add(param);
                final Value pm = new Value();
                pm.setType(mote.getTarget().getQualifyingType());
                param.setModel(pm);
                param.setName("_0");
            } else if (mote.getDeclaration() instanceof Function) {
                plist = ((Function) mote.getDeclaration())
                        .getFirstParameterList();
            }
        } else if (term instanceof Tree.InvocationExpression) {
            encodeCallableArgumentsAsParameterListForRuntime(term, term.getTypeModel(), gen);
            return;
        } else {
            gen.out("/*WARNING4 Callable EXPR of type ", term.getClass().getName(), "*/");
        }
        if (plist == null) {
            gen.out("[]");
        } else {
            encodeParameterListForRuntime(true, term, plist, gen);
        }
    }

}
