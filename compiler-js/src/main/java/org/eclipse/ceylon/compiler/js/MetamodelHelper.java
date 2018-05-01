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

import static org.eclipse.ceylon.compiler.js.util.TypeUtils.getConstructor;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.outputQualifiedTypename;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.printCollectedTypeArguments;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.printTypeArguments;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.typeNameOrList;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getContainingDeclaration;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.ceylon.compiler.js.util.JsIdentifierNames;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.ValueLiteral;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.IntersectionType;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.NothingType;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Reference;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeAlias;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.UnionType;
import org.eclipse.ceylon.model.typechecker.model.Value;

public class MetamodelHelper {

    static void generateOpenType(final Tree.MetaLiteral that, final Declaration d, 
            final GenerateJsVisitor gen, boolean compilingLanguageModule) {
        final Package pkg = d.getUnit().getPackage();
        final Module m = pkg.getModule();
        final boolean isConstructor = isConstructor(d)
                || that instanceof Tree.NewLiteral;
        if (!(d instanceof TypeParameter)) {
            gen.out(compilingLanguageModule ? 
                    "$i$" : gen.getClAlias());
        }
        JsIdentifierNames names = gen.getNames();
        if (d instanceof Interface) {
            gen.out("OpenInterface$jsint");
        } else if (isConstructor) {
            if (getConstructor(d).isValueConstructor()) {
                gen.out("OpenValueConstructor$jsint");
            } else {
                gen.out("OpenCallableConstructor$jsint");
            }
        } else if (d instanceof Class) {
            gen.out("openClass$jsint");
        } else if (d instanceof Function) {
            gen.out("OpenFunction$jsint");
        } else if (d instanceof Value) {
            gen.out("OpenValue$jsint");
        } else if (d instanceof IntersectionType) {
            gen.out("OpenIntersection");
        } else if (d instanceof UnionType) {
            gen.out("OpenUnion");
        } else if (d instanceof TypeParameter) {
            generateOpenType(that, 
                    ((TypeParameter) d).getDeclaration(), 
                    gen, compilingLanguageModule);
            gen.out(".getTypeParameterDeclaration('", d.getName(), "')");
            return;
        } else if (d instanceof NothingType) {
            gen.out("NothingType");
        } else if (d instanceof TypeAlias) {
            gen.out("OpenAlias$jsint(");
            if (compilingLanguageModule) {
                gen.out(")(");
            }
            if (d.isMember()) {
                //Make the chain to the top-level container
                ArrayList<Declaration> parents = 
                        new ArrayList<>(2);
                Declaration pd = 
                        (Declaration) 
                            d.getContainer();
                while (pd!=null) {
                    parents.add(0,pd);
                    pd = pd.isMember() ? 
                            (Declaration) 
                                pd.getContainer() : 
                            null;
                }
                for (Declaration p : parents) {
                    gen.out(names.name(p), ".$$.prototype.");
                }
            }
            gen.out(names.name(d), ")");
            return;
        }
        //TODO optimize for local declarations
        if (compilingLanguageModule) {
            gen.out("()");
        }
        gen.out("(", gen.getClAlias());
        Package currentPackage = that.getUnit().getPackage();
        if (Objects.equals(currentPackage.getModule(), pkg.getModule())) {
            gen.out("lmp$(x$,'");
        } else {
            //TODO use $ for language module as well
            gen.out("fmp$('", m.getNameAsString(), "','", m.getVersion(), "','");
        }
        gen.out(pkg.isLanguagePackage() ? "$" : pkg.getNameAsString(), "'),");
        if (d.isMember() || isConstructor) {
            if (isConstructor) {
                final Class actualClass;
                final String constrName;
                if (d instanceof Class) {
                    actualClass = (Class) d;
                    constrName = "$c$";
                } else {
                    actualClass = (Class) d.getContainer();
                    if (d instanceof Constructor 
                            && ((Constructor) d).isValueConstructor()) {
                        constrName = names.name(
                                actualClass.getDirectMember(d.getName(), 
                                        null, false));
                    } else {
                        constrName = names.name(d);
                    }
                }
                if (gen.isImported(currentPackage, actualClass)) {
                    Module acm = 
                            actualClass.getUnit()
                                .getPackage()
                                .getModule();
                    gen.out(names.moduleAlias(acm), ".");
                }
                if (actualClass.isMember()) {
                    outputPathToDeclaration(that, actualClass, gen);
                }
                gen.out(names.name(actualClass),
                        names.constructorSeparator(d), constrName, ")");
                return;
            } else {
                outputPathToDeclaration(that, d, gen);
            }
        }
        if (d instanceof Value || d.isParameter()) {
            if (!d.isMember()) gen.qualify(that, d);
            if (d.isStatic() 
                    && d instanceof Value 
                    && ((Value)d).getType()
                        .getDeclaration()
                        .isAnonymous()) {
                gen.out(names.name(d), ")");
            } else {
                gen.out(names.getter(d, true), ")");
            }
        } else {
            if (d.isAnonymous()) {
                final String oname = names.objectName(d);
                if (d.isToplevel()) {
                    gen.qualify(that, d);
                }
                gen.out("$i$", oname);
                if (!d.isToplevel()) {
                    gen.out("()");
                }
            } else {
                if (!d.isMember()) gen.qualify(that, d);
                gen.out(names.name(d));
            }
            gen.out(")");
        }
    }

    static void generateClosedTypeLiteral(final Tree.TypeLiteral that, 
            final GenerateJsVisitor gen) {
        final Type ltype = 
                that.getType().getTypeModel()
                    .resolveAliases();
        final Type type = that.getTypeModel();
        final TypeDeclaration td = ltype.getDeclaration();
        if (ltype.isClass()) {
            gen.out(gen.getClAlias(), 
                    td.isClassOrInterfaceMember() ? 
                            "$i$AppliedMemberClass$jsint()(" :
                            "$i$AppliedClass$jsint()(");
            //Tuple is a special case, otherwise gets gen'd as {t:'T'...
            if (that.getUnit().isTupleType(ltype)) {
                gen.qualify(that, td);
                gen.out(gen.getNames().name(td));
            } else if (ltype.isNullValue()) {
                gen.out(gen.getClAlias(), 
                        "$i$$_null()");
            } else {
                outputQualifiedTypename(null, 
                        gen.isImported(gen.getCurrentPackage(), td), 
                        ltype, gen, false);
            }
            gen.out(",");
            printTypeArguments(that, type, gen, false);
            if (!ltype.collectTypeArguments().isEmpty()) {
                gen.out(",undefined,");
                printCollectedTypeArguments(that, ltype, gen, false);
            }
            gen.out(")");
        } else if (ltype.isInterface()) {
            gen.out(gen.getClAlias(), 
                    td.isToplevel() ? 
                        "$i$AppliedInterface$jsint()(" :
                        "$i$AppliedMemberInterface$jsint()(");
            outputQualifiedTypename(null, 
                    gen.isImported(gen.getCurrentPackage(), td), 
                    ltype, gen, false);
            gen.out(",");
            printTypeArguments(that, type, gen, false);
            if (!ltype.collectTypeArguments().isEmpty()) {
                gen.out(",undefined,");
                printCollectedTypeArguments(that, ltype, gen, false);
            }
            gen.out(")");
        } else if (ltype.isNothing()) {
            gen.out(gen.getClAlias(),
                    "nothingType$meta$model()");
        } else if (that instanceof Tree.AliasLiteral) {
            gen.out("/*TODO: applied alias*/");
        } else if (that instanceof Tree.TypeParameterLiteral) {
            gen.out("/*TODO: applied type parameter*/");
        } else {
            gen.out(gen.getClAlias(), 
                    "typeLiteral$meta({Type$typeLiteral:");
            typeNameOrList(that, ltype, gen, false);
            gen.out("})");
        }
    }

    private static void constructorLiteral(final Type ltype, final Constructor cd,
            final Tree.MetaLiteral meta, final GenerateJsVisitor gen) {
        Class cla = (Class) cd.getContainer();
        gen.out(gen.getClAlias(), 
                cla.isClassOrInterfaceMember() ? 
                        "$i$AppliedMemberClass" : 
                        "$i$Applied",
                cd.isValueConstructor() ? 
                        "Value" : 
                        "Callable",
                "Constructor$jsint()(");
        outputQualifiedTypename(null, 
                gen.isImported(gen.getCurrentPackage(), cla), 
                cla.getType(), gen, false);
        JsIdentifierNames names = gen.getNames();
        gen.out(names.constructorSeparator(cd),
                cd.isValueConstructor() ? 
                    names.name(meta.getDeclaration()) : 
                    names.name(cd), 
                ",");
        final Type mtype = meta.getTypeModel().resolveAliases();
        printTypeArguments(meta, mtype, gen, false);
        if (ltype != null 
                && !ltype.collectTypeArguments().isEmpty()) {
            gen.out(",undefined,");
            printCollectedTypeArguments(meta, ltype, gen, false);
        }
        gen.out(")");
    }

    static void generateMemberLiteral(final Tree.MemberLiteral that, 
            final GenerateJsVisitor gen) {
        final Reference ref = that.getTarget();
        final Type type = that.getTypeModel();
        Tree.StaticType st = that.getType();
        Type ltype = st == null ? null : 
            st.getTypeModel().resolveAliases();
        final Declaration d = ref.getDeclaration();
        final Scope container = d.getContainer();
        final Class anonClass = d.isMember()
                && container instanceof Class 
                && ((Class) container).isAnonymous() ?
                        (Class) container : null;

        JsIdentifierNames names = gen.getNames();
        if (that instanceof Tree.NewLiteral
                || isConstructor(d)) {
            constructorLiteral(ref.getType(), 
                    getConstructor(d), that, gen);            
        } else if (that instanceof Tree.FunctionLiteral 
                || d instanceof Function) {
            gen.out(gen.getClAlias(), 
                    d.isMember() ?
                            "AppliedMethod$jsint(" :
                            "AppliedFunction$jsint(");
            if (anonClass != null) {
                gen.qualify(that, anonClass);
                gen.out(names.objectName(anonClass), ".");
            } else if (ltype == null) {
                gen.qualify(that, d);
            } else {
                if (ltype.isUnion() || ltype.isIntersection()) {
                    if (container instanceof TypeDeclaration) {
                        ltype = ((TypeDeclaration) container).getType();
                    } else if (container instanceof TypedDeclaration) {
                        ltype = ((TypedDeclaration) container).getType();
                    }
                }
                TypeDeclaration ltd = ltype.getDeclaration();
                if (ltd.isMember()) {
                    outputPathToDeclaration(that, ltd, gen);
                } else {
                    gen.qualify(that, ltd);
                }
                gen.out(names.name(ltd),
                        d.isStatic() ? ".$st$." : ".$$.prototype.");
            }
            gen.out(names.name(d), ",");
            if (d.isMember()) {
                Tree.TypeArgumentList tal = 
                        that.getTypeArgumentList();
                if (tal!=null) {
                    List<Type> typeModels = tal.getTypeModels();
                    if (typeModels!=null) {
                        gen.out("[");
                        boolean first=true;
                        for (Type targ : typeModels) {
                            if (first) {
                                first=false;
                            }
                            else {
                                gen.out(",");
                            }
                            gen.out(gen.getClAlias(),
                                    "typeLiteral$meta({Type$typeLiteral:");
                            typeNameOrList(that, targ, gen, false);
                            gen.out("})");
                        }
                        gen.out("]");
                        gen.out(",");
                    }
                    else {
                        gen.out("undefined,");
                    }
                } else {
                    gen.out("undefined,");
                }
                printTypeArguments(that, type, gen, false);
            } else {
                printTypeArguments(that, type, gen, false);
                if (!ref.getTypeArguments().isEmpty()) {
                    gen.out(",undefined,");
                    printTypeArguments(that, gen, false,
                            ref.getTypeArguments(), 
                            ref.getVarianceOverrides());
                }
            }
            gen.out(")");
        } else if (that instanceof ValueLiteral 
                || d instanceof Value) {
            Value vd = (Value) d;
            if (vd.isMember()) {
                gen.out(gen.getClAlias(), 
                        "$i$AppliedAttribute$meta$model()('");
                gen.out(d.getName(), "',");
            } else {
                gen.out(gen.getClAlias(), 
                        "$i$AppliedValue$jsint()(undefined,");
            }
            if (anonClass != null) {
                gen.qualify(that, anonClass);
                gen.out(names.objectName(anonClass), ".");
            } else if (ltype == null) {
                gen.qualify(that, d);
            } else {
                gen.qualify(that, ltype.getDeclaration());
                gen.out(names.name(ltype.getDeclaration()));
                gen.out(d.isStatic()?".$st$.":".$$.prototype.");
            }
            gen.out(names.getter(vd, true),",");
            printTypeArguments(that, type, gen, false);
            gen.out(")");
        } else {
            gen.out(gen.getClAlias(), 
                    "/*TODO:closed member literal*/typeLiteral$meta({Type$typeLiteral:");
            gen.out("{t:");
            if (ltype == null) {
                gen.qualify(that, d);
            } else {
                gen.qualify(that, ltype.getDeclaration());
                gen.out(names.name(ltype.getDeclaration()));
                gen.out(d.isStatic() ? ".$st$." : ".$$.prototype.");
            }
            gen.out(names.name(d));
            if (ltype != null 
                    && !ltype.getTypeArguments().isEmpty()) {
                gen.out(",a:");
                printTypeArguments(that, ltype, gen, false);
            }
            gen.out("}})");
        }
    }

    static void findModule(final Module m, final GenerateJsVisitor gen) {
        gen.out(gen.getClAlias(), 
                "findModuleOrThrow$('",
                m.getNameAsString(), "','", 
                m.getVersion(), "')");
    }

    static void outputPathToDeclaration(final Node that, final Declaration d, 
            final GenerateJsVisitor gen) {
        final Declaration parent = getContainingDeclaration(d);
        JsIdentifierNames names = gen.getNames();
        if (!gen.opts.isOptimize() 
                && parent instanceof TypeDeclaration 
                && ModelUtil.contains((Scope) parent, 
                        that.getScope())) {
            gen.out(names.self((TypeDeclaration) parent), 
                    ".");
        } else {
            Declaration member = d;
            final ArrayList<Declaration> parents = 
                    new ArrayList<>(3);
            while (member.isMember()) {
                member = getContainingDeclaration(member);
                parents.add(0, member);
            }
            boolean first = true;
            boolean imported = false;
            boolean parentIsObject = false;
            for (Declaration p : parents) {
                if (first) {
                    imported = gen.qualify(that, p);
                    first = false;
                } else if (parentIsObject) {
                    gen.out(".");
                    parentIsObject = false;
                } else {
                    gen.out(p.isStatic() ? ".$st$." : ".$$.prototype.");
                }
                if (p.isAnonymous()) {
                    String oname = names.objectName(p);
                    if (p.isToplevel()) {
                        gen.out(oname);
                        parentIsObject = true;
                    } else {
                        gen.out("$i$", oname, "()");
                    }
                } else {
                    if (!imported) gen.out("$i$");
                    gen.out(names.name(p), imported ? "" : "()");
                }
                imported = true;
            }
            if (!parents.isEmpty()) {
                if (parentIsObject) {
                    gen.out(".");
                } else {
                    gen.out(d.isStatic() ? ".$st$." : ".$$.prototype.");
                }
            }
        }
    }

}
