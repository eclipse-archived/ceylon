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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.compiler.js.GenerateJsVisitor.GenerateCallback;
import org.eclipse.ceylon.compiler.js.util.TypeUtils;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.TypeArguments;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Generic;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Value;

public class BmeGenerator {

    static void generateBme(final Tree.BaseMemberExpression bme, final GenerateJsVisitor gen) {
        generateBme(bme, gen, true);
    }
    static void generateBme(final Tree.BaseMemberExpression bme, final GenerateJsVisitor gen,
                            boolean nonNull) {
        final boolean forInvoke = bme.getDirectlyInvoked();
        Declaration decl = bme.getDeclaration();
        if (decl != null) {
            String name = decl.getName();
            Package pkg = decl.getUnit().getPackage();

            // map Ceylon true/false/null directly to JS true/false/null
            if (pkg.isLanguagePackage()) {
                if ("true".equals(name) || "false".equals(name) || "null".equals(name)) {
                    gen.out(name);
                    return;
                }
            }
            if (ModelUtil.isConstructor(decl)) {
                Constructor cd = TypeUtils.getConstructor(decl);
                Declaration cdc = (Declaration)cd.getContainer();
                if (!gen.qualify(bme, cd)) {
                    gen.out(gen.getNames().name(cdc), gen.getNames().constructorSeparator(decl));
                }
                if (decl instanceof Value) {
                    gen.out(gen.getNames().name(decl));
                } else {
                    gen.out(gen.getNames().name(cd));
                }
                if (!forInvoke && decl instanceof Value) {
                    gen.out("()");
                }
                return;
            }
        }
        String exp = gen.memberAccess(bme, null);
        if (decl == null && gen.isInDynamicBlock()) {
            if ("undefined".equals(exp)) {
                gen.out(exp);
            } else if (nonNull) {
                gen.out("(typeof ", exp, "==='undefined'||", exp, "===null?");
                gen.generateThrow(null, "Undefined or null reference: " + exp, bme);
                gen.out(":", exp, ")");
            } else {
                gen.out(exp);
            }
        } else {
            final boolean isCallable = !forInvoke && (decl instanceof Functional
                    || bme.getUnit().getCallableDeclaration().equals(bme.getTypeModel().getDeclaration()));
            final boolean hasTparms = hasTypeParameters(bme);
            if (isCallable && (decl.isParameter() || (decl.isToplevel() && !hasTparms))) {
                //Callables passed as arguments are already wrapped in JsCallable
                gen.out(exp);
                return;
            }
            String who = isCallable && decl.isMember() ? gen.getMember(bme, null) : null;
            if (who == null || who.isEmpty()) {
                //We may not need to wrap this in certain cases
                ClassOrInterface cont = ModelUtil.getContainingClassOrInterface(bme.getScope());
                who = cont == null ? "0" : gen.getNames().self(cont);
            }
            if (isCallable && (who != null || hasTparms)) {
                if (hasTparms) {
                    //Function refs with type arguments must be passed as a special function
                    printGenericMethodReference(gen, bme, who, exp);
                } else {
                    //Member methods must be passed as JsCallables
                    gen.out(gen.getClAlias(), "jsc$3(", who, ",", exp, ")");
                }
            } else {
                gen.out(exp);
            }
        }
    }

    static boolean hasTypeParameters(final Tree.StaticMemberOrTypeExpression expr) {
        Tree.TypeArguments typeArguments = expr.getTypeArguments();
		return typeArguments != null 
    		&& typeArguments.getTypeModels() != null
            && !typeArguments.getTypeModels().isEmpty();
    }

    /** Create a map with type arguments from the type parameter list in the expression's declaration and the
     *  type argument list in the expression itself. */
    static Map<TypeParameter, Type> createTypeArguments(final Tree.StaticMemberOrTypeExpression expr) {
        List<TypeParameter> tparams = null;
        Declaration declaration = expr.getDeclaration();
        if (declaration instanceof Generic) {
            tparams = declaration.getTypeParameters();
        }
        else if (declaration instanceof TypedDeclaration 
                && ((TypedDeclaration)declaration).getType()!=null 
                && ((TypedDeclaration)declaration).getType().isTypeConstructor()) {
            tparams = ((TypedDeclaration)declaration).getType().getDeclaration().getTypeParameters();
        }
        else {
            expr.addUnexpectedError("Getting type parameters from unidentified declaration type "
                    + declaration, Backend.JavaScript);
            return null;
        }
        final HashMap<TypeParameter, Type> targs = new HashMap<>();
        TypeArguments typeArguments = expr.getTypeArguments();
        if (typeArguments!=null) {
            List<Type> typeModels = typeArguments.getTypeModels();
            if (typeModels!=null) {
                final Iterator<Type> iter = typeModels.iterator();
                for (TypeParameter tp : tparams) {
                    Type pt = iter.hasNext() ? iter.next() : tp.getDefaultTypeArgument();
                    targs.put(tp, pt);
                }
            }
        }
        return targs;
    }

    static void printGenericMethodReference(final GenerateJsVisitor gen,
            final Tree.StaticMemberOrTypeExpression expr, final String who, final String member) {
        //Function refs with type arguments must be passed as a special function
        gen.out(gen.getClAlias(), "jsc$3(", who, ",", member, ",");
        TypeUtils.printTypeArguments(expr, createTypeArguments(expr), gen, false,
                expr.getTypeModel().getVarianceOverrides());
        gen.out(")");
    }

    /**
     * Generates a write access to a member, as represented by the given expression.
     * The given callback is responsible for generating the assigned value.
     * If lhs==null and the expression is a BaseMemberExpression
     * then the qualified path is prepended.
     */
    static void generateMemberAccess(Tree.StaticMemberOrTypeExpression expr,
                GenerateCallback callback, String lhs, final GenerateJsVisitor gen) {
        Declaration decl = expr.getDeclaration();
        boolean paren = false;
        String plainName = null;
        if (decl == null && gen.isInDynamicBlock()) {
            plainName = expr.getIdentifier().getText();
        } else if (TypeUtils.isNativeJs(decl)) {
            // direct access to a native element
            plainName = decl.getName();
        }
        if (plainName != null) {
            if ((lhs != null) && (lhs.length() > 0)) {
                gen.out(lhs, ".");
            }
            gen.out(plainName, "=");
        }
        else {
            boolean protoCall = gen.opts.isOptimize() && (gen.getSuperMemberScope(expr) != null);
            if (gen.accessDirectly(decl) && !(protoCall && AttributeGenerator.defineAsProperty(decl))) {
                // direct access, without setter
                gen.out(gen.memberAccessBase(expr, decl, true, lhs), "=");
            }
            else {
                // access through setter
                gen.out(gen.memberAccessBase(expr, decl, true, lhs),
                        protoCall ? ".call(this," : "(");
                paren = true;
            }
        }
        
        callback.generateValue();
        if (paren) { gen.out(")"); }
    }

    static void generateMemberAccess(final Tree.StaticMemberOrTypeExpression expr,
            final String strValue, final String lhs, final GenerateJsVisitor gen) {
        generateMemberAccess(expr, new GenerateCallback() {
            @Override public void generateValue() { gen.out(strValue); }
        }, lhs, gen);
    }

    static void generateQte(final Tree.QualifiedTypeExpression that, final GenerateJsVisitor gen) {
        Tree.Primary prim = that.getPrimary();
        final Declaration d = that.getDeclaration();
        final boolean dyncall = gen.isInDynamicBlock() && d == null;
        if (that.getMemberOperator() instanceof Tree.SpreadOp) {
            SequenceGenerator.generateSpread(that, gen);
        } else if ((that.getDirectlyInvoked() && that.getMemberOperator() instanceof Tree.SafeMemberOp==false
                && prim instanceof Tree.BaseTypeExpression == false) || dyncall) {
            final boolean isQte = prim instanceof Tree.QualifiedTypeExpression;
            if (dyncall && that.getDirectlyInvoked() && !isQte) {
                gen.out("new ");
            }
            if (prim instanceof Tree.BaseMemberExpression) {
                generateBme((Tree.BaseMemberExpression)prim, gen);
            } else if (isQte) {
                generateQte((Tree.QualifiedTypeExpression)prim, gen);
            } else {
                prim.visit(gen);
            }
            if (dyncall) {
                gen.out(".", that.getIdentifier().getText());
            } else if (ModelUtil.isConstructor(d)) {
                gen.out(gen.getNames().constructorSeparator(d),
                        gen.getNames().name(d));
            } else {
                gen.out(".", gen.getNames().name(d));
            }
        } else {
            final boolean parens = that.getDirectlyInvoked() &&
                    prim instanceof Tree.BaseTypeExpression;
            if (parens)gen.out("(");
            FunctionHelper.generateCallable(that, gen.getNames().name(d), gen);
            if (parens)gen.out(")");
        }
    }

    static void generateBte(final Tree.BaseTypeExpression that, final GenerateJsVisitor gen,
            final boolean forceReference) {
        Declaration d = that.getDeclaration();
        if (d == null && gen.isInDynamicBlock()) {
            //It's a native js type but will be wrapped in dyntype() call
            String id = that.getIdentifier().getText();
            gen.out("(typeof ", id, "==='undefined'?");
            gen.generateThrow(null, "Undefined type " + id, that);
            gen.out(":", id, ")");
        } else {
            boolean wrap = false;
            String pname = null;
            List<Parameter> params = null;
            if ((forceReference || !that.getDirectlyInvoked()) 
                    && d instanceof TypeDeclaration) {
                if (d.isParameterized()) {
                    wrap = true;
                    pname = gen.getNames().createTempVariable();
                    gen.out("function(");
                    if (d instanceof Class) {
                        params = ((Class)d).getParameterList().getParameters();
                    } else if (d instanceof Constructor) {
                        params = ((Constructor)d).getFirstParameterList().getParameters();
                    }
                    for (int i=0;i<params.size(); i++) {
                        if (i>0)gen.out(",");
                        gen.out(pname, "$", Integer.toString(i));
                    }
                    gen.out("){return ");
                }
            }
            if (d instanceof Constructor) {
                //This is an ugly-ass hack for when the typechecker incorrectly reports
                //the declaration as the constructor instead of the class;
                //this happens with classes that have a default constructor with the same name as the type
                if (gen.getNames().name(d).equals(gen.getNames().name((TypeDeclaration)d.getContainer()))) {
                    gen.qualify(that, (TypeDeclaration)d.getContainer());
                } else {
                    gen.qualify(that, d);
                }
            } else {
                if (d instanceof Class && d.isDynamic()) {
                    gen.out("new ");
                }
                gen.qualify(that, d);
            }
            gen.out(gen.getNames().name(d));
            if (wrap) {
                gen.out("(");
                for (int i=0;i<params.size(); i++) {
                    gen.out(pname, "$", Integer.toString(i), ",");
                }
                List<Type> targs = that.getTypeArguments() == null ? null :
                    that.getTypeArguments().getTypeModels();
                TypeUtils.printTypeArguments(that, 
                        TypeUtils.matchTypeParametersWithArguments(
                                d.getTypeParameters(), targs), 
                        gen, false, null);
                gen.out(");}");
            }
        }
    }

    static void generateStaticReference(Node n, Declaration d, GenerateJsVisitor gen) {
        Declaration orig = d instanceof TypedDeclaration ? ((TypedDeclaration)d).getOriginalDeclaration() : d;
        ClassOrInterface coi = (ClassOrInterface)(orig == null ? d : orig).getContainer();
        gen.qualify(n, coi);
        gen.out(gen.getNames().name(coi), ".$st$.", gen.getNames().name(d));
        if (d instanceof Value && ((Value)d).getType().getDeclaration().isAnonymous()) {
            gen.out("()");
        }
    }

}
