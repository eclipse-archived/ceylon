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

import static org.eclipse.ceylon.compiler.js.AttributeGenerator.defineAsProperty;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.getConstructor;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.isNativeJs;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.matchTypeParametersWithArguments;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.printTypeArguments;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getContainingClassOrInterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.compiler.js.GenerateJsVisitor.GenerateCallback;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.TypeArguments;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Generic;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.Scope;
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
        
        boolean directlyInvoked = bme.getDirectlyInvoked();
        
        Declaration decl = bme.getDeclaration();
        if (decl != null) {
            String name = decl.getName();
            Package pkg = decl.getUnit().getPackage();

            // map Ceylon true/false/null directly to JS true/false/null
            if (pkg.isLanguagePackage() 
                    && ("null".equals(name) 
                    || "false".equals(name) 
                    || "true".equals(name))) {
               gen.out(name);
               return;
            }
            
            if (ModelUtil.isConstructor(decl)) {
                Constructor cd = getConstructor(decl);
                Declaration cdc = (Declaration)cd.getContainer();
                if (!gen.qualify(bme, cd)) {
                    gen.out(gen.getNames().name(cdc), 
                            gen.getNames().constructorSeparator(decl));
                }
                if (decl instanceof Value) {
                    gen.out(gen.getNames().name(decl));
                } else {
                    gen.out(gen.getNames().name(cd));
                }
                if (!directlyInvoked && decl instanceof Value) {
                    gen.out("()");
                }
                return;
            }
        }
        
        String exp = gen.memberAccess(bme, null);
        if (decl==null && gen.isInDynamicBlock()) {
            if ("undefined".equals(exp)) {
                gen.out(exp);
            } else if (nonNull) {
                //throw error if native ref is undefined at runtime
                gen.out("(typeof ",exp,"==='undefined'?", 
                        gen.getClAlias(), "err$(", 
                        gen.getClAlias(), "Exception('undefined native reference: ", exp, "')):", 
                        exp, ")");
            } else {
                gen.out("(typeof ",exp,"==='undefined'?undefined:", exp, ")");
            }
        } else {
            boolean isCallable = 
                    !directlyInvoked 
                        && (decl instanceof Functional 
                            || bme.getTypeModel().isCallable());
            boolean hasTypeArgs = hasTypeArguments(bme);
            if (isCallable && (decl.isParameter() || decl.isToplevel() && !hasTypeArgs)) {
                //Callables passed as arguments are already wrapped in JsCallable
                gen.out(exp);
            }
            else {
                String who = 
                        isCallable && decl.isMember() ? 
                                gen.getMember(bme, null) : null;
                if (who == null || who.isEmpty()) {
                    //We may not need to wrap this in certain cases
                    ClassOrInterface cont = 
                            getContainingClassOrInterface(bme.getScope());
                    who = cont == null ? "0" : gen.getNames().self(cont);
                }
                if (isCallable && (who != null || hasTypeArgs)) {
                    if (hasTypeArgs) {
                        //Function refs with type arguments must be passed as a special function
                        printGenericMethodReference(gen, bme, who, exp);
                    } else {
                        //Member methods must be passed as JsCallables
                        gen.out(gen.getClAlias(), "f3$(", who, ",", exp, ")");
                    }
                } else {
                    gen.out(exp);
                }
            }
        }
    }

    static boolean hasTypeArguments(final Tree.StaticMemberOrTypeExpression expr) {
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
        gen.out(gen.getClAlias(), "f3$(", who, ",", member, ",");
        printTypeArguments(expr, gen, false,
                createTypeArguments(expr), 
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
        boolean dynamic = decl == null && gen.isInDynamicBlock();
        
        if (dynamic) {
            plainName = expr.getIdentifier().getText();
        } else if (isNativeJs(decl)) {
            // direct access to a native element
            plainName = decl.getName();
        }
        if (plainName != null) {
            if (lhs != null && !lhs.isEmpty()) {
                gen.out(lhs, ".");
            }
            gen.out(plainName, "=");
        }
        else {
            boolean protoCall = 
                    gen.opts.isOptimize() 
                    && gen.getSuperMemberScope(expr) != null;
            if (gen.accessDirectly(decl) 
                    && !(protoCall && defineAsProperty(decl))) {
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

    static void generateQte(final Tree.QualifiedTypeExpression that, 
            final GenerateJsVisitor gen) {
        Tree.Primary primary = that.getPrimary();
        Declaration d = that.getDeclaration();
        Tree.MemberOperator op = that.getMemberOperator();
        
        boolean isQte = 
                primary instanceof Tree.QualifiedTypeExpression;
        boolean isBte = 
                primary instanceof Tree.BaseTypeExpression;
        boolean directlyInvoked = that.getDirectlyInvoked();
        boolean dynamic = gen.isInDynamicBlock() && d == null;
        
        if (op instanceof Tree.SpreadOp) {
            SequenceGenerator.generateSpread(that, gen);
        } else if (dynamic 
                || (directlyInvoked
                    && !(op instanceof Tree.SafeMemberOp)
                    && !isBte)) {
            if (dynamic && directlyInvoked && !isQte) {
                gen.out("new ");
            }
            if (primary instanceof Tree.BaseMemberExpression) {
                generateBme((Tree.BaseMemberExpression)primary, gen);
            } else if (isQte) {
                generateQte((Tree.QualifiedTypeExpression)primary, gen);
            } else {
                primary.visit(gen);
            }
            if (dynamic) {
                gen.out(".", that.getIdentifier().getText());
            } else if (ModelUtil.isConstructor(d)) {
                gen.out(gen.getNames().constructorSeparator(d),
                        gen.getNames().name(d));
            } else {
                gen.out(".", gen.getNames().name(d));
            }
        } else {
            final boolean parens = directlyInvoked && isBte;
            if (parens) gen.out("(");
            FunctionHelper.generateCallable(that, gen.getNames().name(d), gen);
            if (parens) gen.out(")");
        }
    }

    static void generateBte(final Tree.BaseTypeExpression that, 
            final GenerateJsVisitor gen,
            final boolean forceReference) {
        Declaration d = that.getDeclaration();
        boolean directlyInvoked = that.getDirectlyInvoked();
        boolean dynamic = d == null && gen.isInDynamicBlock();
        
        if (dynamic) {
            //It's a native js type but will be wrapped in dyntype() call
            String id = that.getIdentifier().getText();
            gen.out("(typeof ", id, "==='undefined'?");
            gen.generateThrow(null, "Undefined type " + id, that);
            gen.out(":", id, ")");
        } else {
            boolean wrap = false;
            String pname = null;
            List<Parameter> params = null;
            if ((forceReference || !directlyInvoked) 
                    && d instanceof TypeDeclaration) {
                if (d.isParameterized()) {
                    wrap = true;
                    pname = gen.getNames().createTempVariable();
                    gen.out("function(");
                    params = ((Functional)d).getFirstParameterList().getParameters();
                    for (int i=0;i<params.size(); i++) {
                        if (i>0)gen.out(",");
                        gen.out(pname, "$", Integer.toString(i));
                    }
                    gen.out("){return ");
                }
            }
            if (d instanceof Constructor) {
                gen.qualify(that, d);
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
                Tree.TypeArguments typeArgs = that.getTypeArguments();
                printTypeArguments(that, 
                        gen, false, 
                        matchTypeParametersWithArguments(
                                d.getTypeParameters(), 
                                typeArgs == null ? null :
                                    typeArgs.getTypeModels()), 
                        null);
                gen.out(");}");
            }
        }
    }

    static void generateStaticReference(Node n, Declaration d, GenerateJsVisitor gen) {
        Declaration dec;
        if (d instanceof TypedDeclaration) {
            Declaration orig = ((TypedDeclaration)d).getOriginalDeclaration();
            dec = orig==null ? d : orig;
        }
        else {
            dec = d;
        }
        ClassOrInterface coi = (ClassOrInterface) dec.getContainer();
        gen.qualify(n, coi);
        gen.out(gen.getNames().name(coi), ".$st$.", gen.getNames().name(d));
        if (d instanceof Value 
                && ((Value)d).getType().getDeclaration().isAnonymous()) {
            gen.out("()");
        }
    }
    static void generateQme(final Tree.QualifiedMemberExpression that,
            final GenerateJsVisitor gen) {
        final Declaration d = that.getDeclaration();
        Tree.MemberOperator op = that.getMemberOperator();
        Tree.Primary primary = that.getPrimary();
    
        if (op instanceof Tree.SafeMemberOp) {
            Operators.generateSafeOp(that, gen);
        } else if (op instanceof Tree.SpreadOp) {
            SequenceGenerator.generateSpread(that, gen);
        } else if (d instanceof Function 
                && !that.getDirectlyInvoked()) {
            FunctionHelper.generateCallable(that, null, gen);
        } else if (that.getStaticMethodReference() && d!=null) {
            if (d instanceof Value 
                    && ModelUtil.isConstructor(d)) {
                Constructor cnst = (Constructor)
                        ((Value)d).getTypeDeclaration();
                String tse = cnst.getTypescriptEnum();
                if (tse != null && tse.matches("[0-9.-]+")) {
                    gen.out(tse);
                } else {
                    //TODO this won't work anymore I think
                    boolean wrap = false;
                    Scope container = d.getContainer();
                    if (primary instanceof Tree.QualifiedMemberOrTypeExpression) {
                        Tree.QualifiedMemberOrTypeExpression prim = 
                                (Tree.QualifiedMemberOrTypeExpression)
                                    primary;
                        if (prim.getStaticMethodReference()) {
                            wrap=true;
                            gen.out("function(_$){return _$");
                        } else {
                            prim.getPrimary().visit(gen);
                        }
                        gen.out(".");
                    } else {
                        if (container instanceof Declaration) {
                            gen.qualify(primary, (Declaration)container);
                        } else if (container instanceof Package) {
                            Module module = ((Package)container).getModule();
                            gen.out(gen.getNames().moduleAlias(module));
                        }
                    }
                    
                    TypeDeclaration type = (TypeDeclaration)container;
                    if (tse != null) {
                        gen.out(gen.getNames().name(type), ".", tse);
                    } else {
                        gen.out(gen.getNames().name(type), 
                                gen.getNames().constructorSeparator(d),
                                gen.getNames().name(d), "()");
                    }
                    if (wrap) {
                        gen.out(";}");
                    }
                }
            } else if (d instanceof Function) {
                Function fd = (Function)d;
                TypeDeclaration typeDec = fd.getTypeDeclaration();
                if (typeDec instanceof Constructor) {
                    primary.visit(gen);
                    gen.out(gen.getNames().constructorSeparator(fd), 
                            gen.getNames().name(typeDec));
                } else if (fd.isStatic()) {
                    generateStaticReference(that, fd, gen);
                } else {
                    gen.out("function($O$){return ");
                    if (hasTypeArguments(that)) {
                        printGenericMethodReference(gen, that, 
                                "$O$", "$O$."+gen.getNames().name(d));
                    } else {
                        gen.out(gen.getClAlias(), 
                                "f3$($O$,$O$.", 
                                gen.getNames().name(d), ")");
                    }
                    gen.out(";}");
                }
            } else {
                if (d.isStatic()) {
                    generateStaticReference(that, d, gen);
                } else {
                    gen.out("function($O$){return $O$.", 
                            gen.getNames().name(d), ";}");
                }
            }
        } else {
            final boolean isDynamic = primary instanceof Tree.Dynamic;
            String lhs = gen.generateToString(new GenerateJsVisitor.GenerateCallback() {
                @Override public void generateValue() {
                    if (isDynamic) {
                        gen.out("(");
                    }
                    gen.box(that.getPrimary(), true, true);
                    if (isDynamic) {
                        gen.out(")");
                    }
                }
            });
            if (d != null && d.isStatic()) {
                generateStaticReference(that, d, gen);
            } else {
                gen.out(gen.memberAccess(that, lhs));
            }
        }
    }

}
