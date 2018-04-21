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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.compiler.js.util.JsIdentifierNames;
import org.eclipse.ceylon.compiler.js.util.RetainedVars;
import org.eclipse.ceylon.compiler.js.util.TypeUtils;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.TypeArguments;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Generic;
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
        } else {
            positionalInvocation(that);
        }
    }

    private Map<TypeParameter,Type> getTypeArguments(Tree.Primary p) {
        if (p instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression smote = (Tree.StaticMemberOrTypeExpression)p;
            final Declaration d = smote.getDeclaration();
            final boolean hasTargs = d != null 
                    && d.getContainer() instanceof Generic
                    && ((Generic)d.getContainer()).isParameterized();
            final boolean hasParentTargs = TypeUtils.isStaticWithGenericContainer(d);
            Reference target = smote.getTarget();
			TypeArguments typeArgs = smote.getTypeArguments();
			if (hasTargs && ModelUtil.isConstructor(d)) {
                return target.getTypeArguments();
            } else if (hasParentTargs) {
			    if (typeArgs != null 
			            && !typeArgs.getTypeModels().isEmpty()) {
			        //If the type is static AND has type arguments of its own, we need to merge them
			        Map<TypeParameter, Type> targs = new HashMap<>();
			        targs.putAll(target.getTypeArguments());
			        targs.putAll(target.getQualifyingType().getTypeArguments());
			        return targs;
			    }
			    return target.getQualifyingType().getTypeArguments();
			} else if (d instanceof Functional) {
			    Map<TypeParameter,Type> targs = 
			            TypeUtils.matchTypeParametersWithArguments(
			                d.getTypeParameters(),
			                typeArgs == null ? null :
			                typeArgs.getTypeModels());
			    if (targs == null) {
			        gen.out("/*TARGS != TPARAMS!!!!*/");
			    }
			    return targs;
			}
        } else if (p instanceof Tree.ExtendedTypeExpression) {
            Tree.ExtendedTypeExpression ete = (Tree.ExtendedTypeExpression)p;
            return ete.getTarget().getTypeArguments();
        }
        return null;
    }

    private void namedInvocation(final Tree.InvocationExpression that) {
        final Tree.Primary typeArgSource = that.getPrimary();
        Tree.NamedArgumentList argList = that.getNamedArgumentList();
        if (gen.isInDynamicBlock() 
        		&& typeArgSource instanceof Tree.MemberOrTypeExpression
                && ((Tree.MemberOrTypeExpression)typeArgSource).getDeclaration() == null) {
            final String fname = names.createTempVariable();
            gen.out("(", fname, "=");
            //Call a native js constructor passing a native js object as parameter
            if (typeArgSource instanceof Tree.QualifiedTypeExpression) {
                BmeGenerator.generateQte((Tree.QualifiedTypeExpression)typeArgSource, gen);
            } else {
                typeArgSource.visit(gen);
            }
            gen.out(",", fname, ".$$===undefined?new ", fname, "(");
            nativeObject(argList);
            gen.out("):", fname, "(");
            nativeObject(argList);
            gen.out("))");
        } else {
            gen.out("(");
            Map<String,String> argVarNames = defineNamedArguments(typeArgSource, argList);
            if (typeArgSource instanceof Tree.BaseMemberExpression) {
                BmeGenerator.generateBme((Tree.BaseMemberExpression)typeArgSource, gen);
            } else if (typeArgSource instanceof Tree.QualifiedTypeExpression) {
                BmeGenerator.generateQte((Tree.QualifiedTypeExpression)typeArgSource, gen);
            } else {
                typeArgSource.visit(gen);
            }
            if (typeArgSource instanceof Tree.MemberOrTypeExpression) {
                Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression) typeArgSource;
                if (mte.getDeclaration() instanceof Functional) {
                    Functional f = (Functional) mte.getDeclaration();
                    Map<TypeParameter, Type> targs = getTypeArguments(typeArgSource);
                    applyNamedArguments(argList, f, argVarNames, gen.getSuperMemberScope(mte)!=null, targs);
                }
            }
            gen.out(")");
        }
    }

    private void positionalInvocation(final Tree.InvocationExpression that) {
        final Tree.Primary primary = that.getPrimary();
        Type primaryType = primary.getTypeModel();
        final Tree.PositionalArgumentList argList = that.getPositionalArgumentList();
        final Map<TypeParameter, Type> targs = getTypeArguments(primary);
        List<Tree.PositionalArgument> positionalArgs = argList.getPositionalArguments();
		if (gen.isInDynamicBlock() 
        		&& primary instanceof Tree.BaseTypeExpression
                && ((Tree.BaseTypeExpression)primary).getDeclaration() == null) {
            gen.out("(");
            //Could be a dynamic object, or a Ceylon one
            //We might need to call "new" so we need to get all the args to pass directly later
            final List<String> argnames = generatePositionalArguments(primary,
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
            gen.out(",", fname, ".$$===undefined?new ", fname, "(", theargs, "):", fname, "(", theargs, "))");
            //TODO we lose type args for now
            return;
        } else {
            final Tree.PositionalArgument lastArg = positionalArgs.isEmpty() ?
            		null : positionalArgs.get(positionalArgs.size()-1);
			boolean hasSpread = lastArg instanceof Tree.SpreadArgument 
                    && that.getUnit().isUnknownArgumentsCallable(primaryType)
                    && !primaryType.isUnknown();
            if (hasSpread) {
                gen.out(gen.getClAlias(), "spread$2(");
            }
            if (primary instanceof Tree.BaseMemberExpression) {
                final Tree.BaseMemberExpression bme = (Tree.BaseMemberExpression)primary;
                if (gen.isInDynamicBlock()) {
                    Declaration dec = bme.getDeclaration();
					if (dec == null 
                    		|| dec.isDynamic()
                            || dec instanceof TypedDeclaration 
                            		&& ((TypedDeclaration)dec).isDynamicallyTyped()) {
                        if (lastArg instanceof Tree.SpreadArgument &&
                                (lastArg.getTypeModel() == null 
                                	|| lastArg.getTypeModel().isUnknown())) {
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
                    } else if ("ceylon.language::print".equals(dec.getQualifiedNameString())) {
                        Tree.PositionalArgument printArg = positionalArgs.get(0);
                        if (ModelUtil.isTypeUnknown(printArg.getTypeModel())) {
                            gen.out(gen.getClAlias(), "pndo$("); //#397
                            printArg.visit(gen);
                            gen.out(")");
                            return;
                        }
                    }
                }
                BmeGenerator.generateBme(bme, gen);
            } else if (primary instanceof Tree.QualifiedTypeExpression) {
                BmeGenerator.generateQte((Tree.QualifiedTypeExpression)primary, gen);
            } else {
                primary.visit(gen);
            }

            if (gen.opts.isOptimize() && (gen.getSuperMemberScope(primary) != null)) {
                gen.out(".call(", names.self(ModelUtil.getContainingClassOrInterface(primary.getScope())));
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
            if (fillInParams) {
                //Get the callable and try to assign params from there
                Interface cd = that.getUnit().getCallableDeclaration();
                final Type ed = that.getUnit().getEmptyType();
                Class td = that.getUnit().getTupleDeclaration();
                Type callable = primaryType == null ? null :
                    	primaryType.getSupertype(cd);
                if (callable != null) {
                    //This is a tuple with the arguments to the callable
                    //(can be union with empty if first param is defaulted)
                    Type callableArgs = callable.getTypeArgumentList().get(1);
                    boolean isUnion=false;
                    if (callableArgs.isUnion()) {
                        if (callableArgs.getCaseTypes().size() == 2) {
                            callableArgs = callableArgs.minus(ed);
                        }
                        isUnion=callableArgs.isUnion();
                    }
                    //This is the type of the first argument
                    boolean isSequenced = !isUnion && !td.equals(callableArgs.getDeclaration());
                    Type argtype = isUnion ? callableArgs :
                        callableArgs.isTypeParameter() || callableArgs.isEmpty() ? callableArgs :
                        callableArgs.isSequence() || callableArgs.isSequential() ? callableArgs.getTypeArgumentList().get(0) :
                        callableArgs.getTypeArgumentList().get(isSequenced ? 0 : 1);
                    Parameter p = null;
                    int c = 0;
                    for (Tree.PositionalArgument arg : positionalArgs) {
                        if (p == null) {
                            p = new Parameter();
                            p.setName("arg"+c);
                            p.setDeclaration(primaryType.getDeclaration());
                            Value v = new Value();
                            Scope scope = argList.getScope();
                            v.setContainer(scope);
                            v.setScope(scope);
                            v.setType(argtype);
                            p.setModel(v);
                            if (callableArgs == null || isSequenced) {
                                p.setSequenced(true);
                            } else if (!isSequenced) {
                                Type next = isUnion ? null : callableArgs.getTypeArgumentList().get(2);
                                if (next != null && next.getSupertype(td) == null) {
                                    //It's not a tuple, so no more regular parms. It can be:
                                    //empty|tuple if defaulted params
                                    //empty if no more params
                                    //sequential if sequenced param
                                    if (next.isUnion()) {
                                        //empty|tuple
                                        callableArgs = next.minus(ed);
                                        isSequenced = !td.equals(callableArgs.getDeclaration());
                                        argtype = callableArgs.getTypeArgumentList().get(isSequenced ? 0 : 1);
                                    } else {
                                        //we'll bet on sequential (if it's empty we don't care anyway)
                                        argtype = next;
                                        callableArgs = null;
                                    }
                                } else {
                                    //If it's a tuple then there are more params
                                    callableArgs = next;
                                    argtype = callableArgs == null ? null : 
                                    	callableArgs.getTypeArgumentList().get(1);
                                }
                            }
                        }
                        arg.setParameter(p);
                        c++;
                        if (!p.isSequenced()) {
                            p = null;
                        }
                    }
                }
            }
            generatePositionalArguments(primary, argList, positionalArgs, false, false);
        }
        if (targs != null && !targs.isEmpty()
                && primary instanceof Tree.MemberOrTypeExpression
                && ((Tree.MemberOrTypeExpression)primary).getDeclaration() 
                		instanceof Functional) {
            if (positionalArgs.size() > 0) {
                gen.out(",");
            }
            Functional bmed = (Functional)((Tree.MemberOrTypeExpression)primary).getDeclaration();
            //If there are fewer arguments than there are parameters...
            final int argsSize = positionalArgs.size();
            int paramArgDiff = bmed.getFirstParameterList().getParameters().size() - argsSize;
            if (paramArgDiff > 0) {
                final Tree.PositionalArgument parg = argsSize > 0 ? positionalArgs.get(argsSize-1) : null;
                if (parg instanceof Tree.Comprehension || parg instanceof Tree.SpreadArgument) {
                    paramArgDiff--;
                }
                for (int i=0; i < paramArgDiff; i++) {
                    gen.out("undefined,");
                }
            }
            if (targs != null && !targs.isEmpty()) {
                TypeUtils.printTypeArguments(primary, targs, gen, false, null);
            }
        }
        gen.out(")");
    }

    /** Generates the code to evaluate the expressions in the named argument list and assign them
     * to variables, then returns a map with the parameter names and the corresponding variable names. */
    Map<String, String> defineNamedArguments(Tree.Primary primary, Tree.NamedArgumentList argList) {
        Map<String, String> argVarNames = new HashMap<>();
        for (Tree.NamedArgument arg: argList.getNamedArguments()) {
            Parameter p = arg.getParameter();
            final String paramName;
            if (p == null && gen.isInDynamicBlock()) {
                paramName = arg.getIdentifier().getText();
            } else {
                paramName = arg.getParameter().getName();
            }
            String varName = names.createTempVariable();
            argVarNames.put(paramName, varName);
            retainedVars.add(varName);
            gen.out(varName, "=");
            if (arg instanceof Tree.MethodArgument) {
                Tree.MethodArgument marg = (Tree.MethodArgument)arg;
                gen.out(gen.getClAlias(), "f2$(");
                FunctionHelper.methodArgument(marg, gen);
                gen.out(",");
                //Add parameters
                TypeUtils.encodeParameterListForRuntime(true, arg, marg.getParameterLists().get(0).getModel(), gen);
                gen.out(",");
                Type margType = marg.getDeclarationModel().getType().getFullType();
                TypeUtils.printTypeArguments(arg, margType.getTypeArguments(), gen, false,
                        margType.getVarianceOverrides());
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
            generatePositionalArguments(primary, argList, sarg.getPositionalArguments(), true, false);
            gen.out(",");
        }
        return argVarNames;
    }

    void applyNamedArguments(Tree.NamedArgumentList argList, Functional func,
                Map<String, String> argVarNames, boolean superAccess, 
                Map<TypeParameter, Type> targs) {
        final ParameterList plist = func.getFirstParameterList();
        boolean first=true;
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
            TypeUtils.printTypeArguments(argList, targs, gen, false, null);
        }
        gen.out(")");
    }

    /** Generate a list of PositionalArguments, optionally assigning a variable name to each one
     * and returning the variable names. */
    List<String> generatePositionalArguments(final Tree.Primary primary, 
    		final Tree.ArgumentList that,
            final List<Tree.PositionalArgument> args,
            final boolean forceSequenced, final boolean generateVars) {
        if (args.isEmpty()) {
            return Collections.emptyList();
        }
        final List<String> argvars = new ArrayList<>(args.size());
        boolean first=true;
        boolean opened=false;
        Type sequencedType=null;
        for (Tree.PositionalArgument arg : args) {
            Tree.Expression expr;
            final Parameter pd = arg.getParameter();
            Type argType = arg.getTypeModel();
			if (arg instanceof Tree.ListedArgument) {
                if (!first) gen.out(",");
                expr = ((Tree.ListedArgument) arg).getExpression();
                Type exprType = expr.getTypeModel();
                boolean dyncheck = gen.isInDynamicBlock() 
                		&& pd != null 
                		&& !ModelUtil.isTypeUnknown(pd.getType())
                        && exprType.containsUnknowns();
                if (forceSequenced || pd != null && pd.isSequenced()) {
                    if (dyncheck) {
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
                        sequencedType = ModelUtil.unionType(elemtype, sequencedType, that.getUnit());
                    }
                    if (!opened) {
                        if (generateVars) {
                            final String argvar = names.createTempVariable();
                            argvars.add(argvar);
                            gen.out(argvar, "=");
                        }
                        if (exprType.isSequential() || pd != null && pd.isSequenced()) {
                            gen.out(gen.getClAlias(), "$arr$sa$([");
                        } else {
                            int argpos = args.indexOf(arg);
                            ArrayList<Tree.PositionalArgument> seqargs = new ArrayList<>(args.size()-argpos+1);
                            for (;argpos<args.size();argpos++) {
                                seqargs.add(args.get(argpos));
                            }
                            final Tree.PositionalArgument lastArg = seqargs.get(seqargs.size()-1);
                            if (sequencedType.isTypeParameter()) {
                                sequencedType = that.getUnit().getIterableType(sequencedType);
                            }
                            SequenceGenerator.lazyEnumeration(seqargs, primary, sequencedType,
                                    lastArg instanceof Tree.SpreadArgument ||
                                            lastArg instanceof Tree.Comprehension, gen);
                            return argvars;
                        }
                    }
                    opened=true;
                } else if (generateVars) {
                    final String argvar = names.createTempVariable();
                    argvars.add(argvar);
                    gen.out(argvar, "=");
                }
                final int boxType = pd==null ? 0 :
                	gen.boxUnboxStart(expr.getTerm(), pd.getModel());
                Map<TypeParameter,Type> targs = null;
                if (dyncheck) {
                    if (primary instanceof Tree.MemberOrTypeExpression) {
                        targs = ((Tree.MemberOrTypeExpression)primary).getTarget().getTypeArguments();
                    }
                    TypeUtils.generateDynamicCheck(expr, pd.getType(), gen, false, targs);
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
                        Tree.Term argTerm = ((Tree.ListedArgument)arg).getExpression().getTerm();
                        if (argTerm instanceof Tree.MemberOrTypeExpression) {
                            targs = ((Tree.MemberOrTypeExpression)argTerm).getTarget().getTypeArguments();
                        }
                    }
                    TypeUtils.printTypeArguments(arg, targs, gen, false,
                            argType.getVarianceOverrides());
                }
                gen.boxUnboxEnd(boxType);
            } else if (arg instanceof Tree.SpreadArgument || arg instanceof Tree.Comprehension) {
                final boolean isSpreadArg = arg instanceof Tree.SpreadArgument;
                if (isSpreadArg) {
                    expr = ((Tree.SpreadArgument) arg).getExpression();
                } else {
                    expr = null;
                }
                boolean chained=false;
                if (opened) {
                    SequenceGenerator.closeSequenceWithReifiedType(that,
                            TypeUtils.wrapAsIterableArguments(sequencedType), gen, false);
                    gen.out(".chain(");
                    sequencedType=null;
                    chained=true;
                } else if (!first) {
                    gen.out(",");
                }
                if (isSpreadArg) {
                    generateSpreadArgument(primary, (Tree.SpreadArgument)arg, expr, pd);
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
                    Map<TypeParameter,Type> _targs;
                    Map<TypeParameter, SiteVariance> _vo;
                    Interface iterableDec = that.getUnit().getIterableDeclaration();
					if (expr == null) {
                        //it's a comprehension
                        _targs = TypeUtils.wrapAsIterableArguments(argType);
                        _vo = null;
                    } else {
                        Type spreadType = TypeUtils.findSupertype(
                                that.getUnit().getSequentialDeclaration(),
                                expr.getTypeModel());
                        if (spreadType == null) {
                            //Go directly to Iterable
                            spreadType = TypeUtils.findSupertype(iterableDec,
                                    expr.getTypeModel());
                        }
                        _targs = spreadType.getTypeArguments();
                        _vo = spreadType.getVarianceOverrides();
                    }
                    if (chained) {
                        Type[] _tlist = new Type[2];
                        for (TypeParameter tp : _targs.keySet()) {
                            if ("Element".equals(tp.getName())) {
                                _tlist[0] = _targs.get(tp);
                            } else if ("Absent".equals(tp.getName())) {
                                _tlist[1] = _targs.get(tp);
                            }
                        }
                        if (_tlist[1] == null) {
                            _tlist[1] = that.getUnit().getNothingType();
                        }
                        Function cdec = (Function)iterableDec.getMember("chain", null, false);
                        _targs = TypeUtils.matchTypeParametersWithArguments(cdec.getTypeParameters(), Arrays.asList(_tlist));
                    }
                    TypeUtils.printTypeArguments(that, _targs, gen, false, _vo);
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
            seqtargs = forceSequenced && args.size() > 0 ? 
            		that.getUnit().getNonemptyIterableType(sequencedType).getTypeArguments() : 
        			TypeUtils.wrapAsIterableArguments(sequencedType);
            SequenceGenerator.closeSequenceWithReifiedType(primary,
                    seqtargs, gen, false);
        }
        return argvars;
    }

    private void generateSpreadArgument(final Tree.Primary primary,
            final Tree.SpreadArgument arg, Tree.Expression expr,
            final Parameter pd) {
        TypedDeclaration td = pd == null ? null : pd.getModel();
        int boxType = gen.boxUnboxStart(expr.getTerm(), td);
        Type argType = arg.getTypeModel();
		if (boxType == 4) {
            arg.visit(gen);
            gen.out(",");
            describeMethodParameters(expr.getTerm());
            gen.out(",");
            TypeUtils.printTypeArguments(arg, 
            		argType.getTypeArguments(), 
            		gen, false,
                    argType.getVarianceOverrides());
        } else if (pd == null) {
            final Declaration primDec = 
            		primary instanceof Tree.MemberOrTypeExpression ? 
            				((Tree.MemberOrTypeExpression)primary).getDeclaration() : 
        					null;
            if (gen.isInDynamicBlock() 
            		&& primary instanceof Tree.MemberOrTypeExpression
                    && (primDec == null || primDec.isDynamic() ||
                    		(primDec instanceof TypedDeclaration 
                    			&& ((TypedDeclaration)primDec).isDynamicallyTyped()))
                    && argType != null 
                    && argType.getDeclaration()
                    		.inherits(arg.getUnit().getTupleDeclaration())) {
                //Spread dynamic parameter
                Type tupleType = argType;
                Type targ = tupleType.getTypeArgumentList().get(2);
                arg.visit(gen);
                gen.out(".$_get(0)");
                int i = 1;
                while (!targ.isSubtypeOf(arg.getUnit().getEmptyType())) {
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
            if (!arg.getUnit().isSequentialType(argType)) {
                gen.out(".sequence()");
            }
        } else if (!argType.isEmpty()) {
            final String specialSpreadVar = gen.getNames().createTempVariable();
            gen.out("(", specialSpreadVar, "=");
            arg.visit(gen);
            final boolean unknownSpread = argType.isUnknown();
            final String get0 = unknownSpread ?"[":".$_get(";
            final String get1 = unknownSpread ?"]":")";
            if (!unknownSpread && !arg.getUnit().isSequentialType(argType)) {
                gen.out(".sequence()");
            }
            gen.out(",");
            if (pd.isDefaulted()) {
                gen.out(gen.getClAlias(), "nn$(",
                        specialSpreadVar, get0, "0", get1, ")?", specialSpreadVar,
                        get0, "0", get1, ":undefined)");
            } else {
                gen.out(specialSpreadVar, get0, "0", get1, ")");
            }
            //Find out if there are more params
            final List<Parameter> moreParams;
            final Declaration pdd = pd.getDeclaration();
            boolean found = false;
            if (pdd instanceof Function) {
                moreParams = ((Function)pdd).getFirstParameterList().getParameters();
            } else if (pdd instanceof Class) {
                moreParams = ((Class)pdd).getParameterList().getParameters();
            } else {
                //Check the parameters of the primary (obviously a callable, so this is a Tuple)
                List<Parameter> cparms = TypeUtils.convertTupleToParameters(
                        primary.getTypeModel().getTypeArgumentList().get(1));
                cparms.remove(0);
                moreParams = cparms;
                found = true;
            }
            if (moreParams != null) {
                int c = 1;
                for (Parameter restp : moreParams) {
                    if (found) {
                        final String cs=Integer.toString(c++);
                        if (restp.isDefaulted()) {
                            gen.out(",", gen.getClAlias(), "nn$(", specialSpreadVar,
                                    get0, cs, get1, ")?", specialSpreadVar, get0, cs, get1, ":undefined");
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
                    if (JsIdentifierNames.isTrueReservedWord(argName)) {
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
                        String argName = arg.getIdentifier().getText();
                        if (JsIdentifierNames.isTrueReservedWord(argName)) {
                            gen.out("\"", argName, "\"");
                        }
                        else {
                            gen.out(argName);
                        }
                        gen.out("',{get:function(){ return ");
                        gen.visitSingleExpression(((Tree.AttributeArgument) arg).getSpecifierExpression().getExpression());
                        gen.out("},configurable:true,enumerable:true})");
                    }
                }
                gen.out(",", tmpobjvar, ")");
            }
        } else {
            String arr = null;
            boolean isComp = false;
            boolean isSpread = seqArg != null &&
                    !seqArg.getPositionalArguments().isEmpty() &&
                    seqArg.getPositionalArguments().get(
                            seqArg.getPositionalArguments().size() -1) 
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
            plist = ((Function)(((Tree.FunctionArgument)term).getDeclarationModel())).getFirstParameterList();
        } else if (term instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mote = (Tree.MemberOrTypeExpression)term;
            if (mote.getStaticMethodReference()) {
                plist = new ParameterList();
                Parameter param = new Parameter();
                plist.getParameters().add(param);
                final Value pm = new Value();
                pm.setType(mote.getTarget().getQualifyingType());
                param.setModel(pm);
                param.setName("_0");
            } else if (mote.getDeclaration() instanceof Function) {
                plist = ((Function)((Tree.MemberOrTypeExpression)term).getDeclaration()).getFirstParameterList();
            }
        } else if (term instanceof Tree.InvocationExpression) {
            TypeUtils.encodeCallableArgumentsAsParameterListForRuntime(term, term.getTypeModel(), gen);
            return;
        } else {
            gen.out("/*WARNING4 Callable EXPR of type ", term.getClass().getName(), "*/");
        }
        if (plist == null) {
            gen.out("[]");
        } else {
            TypeUtils.encodeParameterListForRuntime(true, term, plist, gen);
        }
    }

}
