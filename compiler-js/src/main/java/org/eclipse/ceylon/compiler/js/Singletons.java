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

import static org.eclipse.ceylon.compiler.js.ClassGenerator.addFunctionTypeArguments;
import static org.eclipse.ceylon.compiler.js.ClassGenerator.callSupertypes;
import static org.eclipse.ceylon.compiler.js.Constructors.classStatementsAfterConstructor;
import static org.eclipse.ceylon.compiler.js.Constructors.classStatementsBetweenConstructors;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.encodeForRuntime;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.getTypes;
import static org.eclipse.ceylon.compiler.js.util.TypeUtils.printTypeArguments;
import static org.eclipse.ceylon.compiler.typechecker.util.NativeUtil.hasNativeMembers;
import static org.eclipse.ceylon.compiler.typechecker.util.NativeUtil.isForBackend;
import static org.eclipse.ceylon.compiler.typechecker.util.NativeUtil.isHeaderWithoutBackend;
import static org.eclipse.ceylon.compiler.typechecker.util.NativeUtil.isNativeHeader;
import static org.eclipse.ceylon.compiler.typechecker.util.NativeUtil.mergeStatements;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getContainingClassOrInterface;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getNativeDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.compiler.js.GenerateJsVisitor.InitDeferrer;
import org.eclipse.ceylon.compiler.js.GenerateJsVisitor.SuperVisitor;
import org.eclipse.ceylon.compiler.js.util.JsIdentifierNames;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.Value;

public class Singletons {
    /** Generate an object definition, that is, define an anonymous class and then a function
     * to return a single instance of it.
     * @param that The node with the definition (can be ObjectDefinition, ObjectExpression, ObjectArgument)
     * @param d The Value declaration for the object
     * @param sats The list of satisfied types of the anonymous class
     * @param superType The supertype of the anonymous class
     * @param superCall The invocation of the supertype (object bla extends Foo(x))
     * @param body The object definition's body
     * @param annots The annotations (in case of ObjectDefinition)
     * @param gen The main visitor/generator.
     */
    static void defineObject(final Node that, final Value d, final List<Type> sats,
            final Tree.SimpleType superType, final Tree.InvocationExpression superCall,
            final Tree.Body body, final Tree.AnnotationList annots, 
            final GenerateJsVisitor gen, InitDeferrer initDeferrer) {
        final boolean addToPrototype = 
                gen.opts.isOptimize() 
                && d != null 
                && d.isClassOrInterfaceMember();
        final boolean isObjExpr = that instanceof Tree.ObjectExpression;
        final TypeDeclaration td = isObjExpr ? 
                ((Tree.ObjectExpression) that).getAnonymousClass() : 
                d.getTypeDeclaration();
        final Class c = (Class)
                (td instanceof Constructor ? td.getContainer() : td);
        
        JsIdentifierNames names = gen.getNames();
        final String className = names.name(c);
        final String objectName = names.name(d);
        final String selfName = names.self(c);

        final Value natd = d == null ? null : (Value) 
                getNativeDeclaration(d, Backend.JavaScript);
        if (that instanceof Tree.Declaration) {
            if (isNativeHeader((Tree.Declaration)that) 
                    && natd != null) {
                // It's a native header, remember it for later 
                // when we deal with its implementation
                gen.saveNativeHeader((Tree.Declaration)that);
                return;
            }
            if (!(isForBackend((Tree.Declaration) that, 
                        Backend.JavaScript)
                    || isHeaderWithoutBackend((Tree.Declaration) that, 
                            Backend.JavaScript))) {
                return;
            }
        }
        final List<Tree.Statement> stmts;
        if (d!=null && isForBackend(d, Backend.JavaScript)) {
            Tree.Declaration nh = gen.getNativeHeader(d);
            if (nh == null 
                    && hasNativeMembers(c) 
                    && that instanceof Tree.Declaration) {
                nh = (Tree.Declaration) that;
            }
            stmts = mergeStatements(body, nh, Backend.JavaScript);
        } else {
            stmts = body.getStatements();
        }

        Map<TypeParameter, Type> targs=new HashMap<>();
        if (sats != null) {
            for (Type st : sats) {
                Map<TypeParameter, Type> stargs = st.getTypeArguments();
                if (stargs != null && !stargs.isEmpty()) {
                    targs.putAll(stargs);
                }
            }
        }
        gen.out(GenerateJsVisitor.function, className, 
                targs.isEmpty() ? "()" : "($a$)");
        gen.beginBlock();
        if (isObjExpr) {
            gen.out("var ", selfName, "=new ", className, ".$$;");
            final ClassOrInterface coi = 
                    getContainingClassOrInterface(c.getContainer());
            if (coi != null) {
                gen.out(selfName, ".outer$=", names.self(coi));
                gen.endLine(true);
            }
        } else {
            if (c.isMember() && !d.isStatic()) {
                gen.initSelf(that);
            }
            gen.instantiateSelf(c);
            gen.referenceOuter(c);
        }

        //TODO should we generate all this code for native headers?
        //Really we should merge the body of the header with that of the impl
        //It's the only way to make this shit work in lexical scope mode
        final List<Declaration> superDecs = new ArrayList<>();
        if (!gen.opts.isOptimize()) {
            final SuperVisitor superv = new SuperVisitor(superDecs);
            for (Tree.Statement st : stmts) {
                st.visit(superv);
            }
        }
        if (!targs.isEmpty()) {
            gen.out(selfName, ".$a$=$a$");
            gen.endLine(true);
        }
        TypeGenerator.callSupertypes(sats, superType, c, that, superDecs, superCall,
                superType == null ? null : 
                    ((Class) c.getExtendedType().getDeclaration())
                            .getParameterList(), 
                gen);
        
        gen.visitStatements(stmts);
        gen.out("return ", selfName, ";");
        gen.endBlock();
        gen.out(";", className, ".$m$=");
        encodeForRuntime(that, c, gen);
        gen.endLine(true);
        TypeGenerator.initializeType(that, gen, initDeferrer);
        final String objvar = 
                (addToPrototype ? "this.":"")
                + names.createTempVariable();

        if (d != null && !addToPrototype) {
            gen.out("var ", objvar);
            //If it's a property, create the object here
            if (AttributeGenerator.defineAsProperty(d)) {
                gen.out("=", className, "(");
                if (!targs.isEmpty()) {
                    printTypeArguments(that, gen, false, targs, null);
                }
                gen.out(")");
            }
            gen.endLine(true);
        }

        if (d!=null && AttributeGenerator.defineAsProperty(d)) {
            gen.out(gen.getClAlias(), "atr$(");
            gen.outerSelf(d);
            gen.out(",'", objectName, "',function(){return ");
            if (addToPrototype) {
                gen.out("this.", names.privateName(d));
            } else {
                gen.out(objvar);
            }
            gen.out(";},undefined,");
            encodeForRuntime(that, d, annots, gen);
            gen.out(")");
            gen.endLine(true);
        } else if (d!=null) {
            final String objectGetterName = names.getter(d, false);
            gen.out(GenerateJsVisitor.function, objectGetterName, "()");
            gen.beginBlock();
            //Create the object lazily
            final String oname = names.objectName(c);
            gen.out("if(", objvar, "===", gen.getClAlias(), "INIT$)");
            gen.generateThrow(gen.getClAlias()+"InitializationError",
                    "Cyclic initialization trying to read the value of '" +
                    d.getName() + "' before it was set", that);
            gen.endLine(true);
            gen.out("if(", objvar, "===undefined){", 
                    objvar, "=", gen.getClAlias(), "INIT$;",
                    objvar, "=$i$", oname);
            if (!oname.endsWith("()")) {
                gen.out("()");
            }
            gen.out("(");
            if (!targs.isEmpty()) {
                printTypeArguments(that, gen, false, targs, null);
            }
            gen.out(");", objvar, ".$m$=", objectGetterName, ".$m$;}");
            gen.endLine();
            gen.out("return ", objvar, ";");
            gen.endBlockNewLine();            
            
            if (addToPrototype || d.isShared()) {
                gen.outerSelf(d);
                gen.out(".", objectGetterName, "=", objectGetterName);
                gen.endLine(true);
            }
            if (!d.isToplevel()) {
                if(gen.outerSelf(d))gen.out(".");
            }
            gen.out(objectGetterName, ".$m$=");
            encodeForRuntime(that, d, annots, gen);
            gen.endLine(true);
            gen.out(names.getter(c, true), "=", objectGetterName);
            gen.endLine(true);
            if (d.isToplevel()) {
                final String objectGetterNameMM = names.getter(d, true);
                gen.out("x$.", objectGetterNameMM, "=", objectGetterNameMM);
                gen.endLine(true);
            }
        } else if (isObjExpr) {
            gen.out("return ", className, "();");
        }
    }

    static void objectDefinition(final Tree.ObjectDefinition that, 
            final GenerateJsVisitor gen, InitDeferrer initDeferrer) {
        final Tree.SatisfiedTypes sts = that.getSatisfiedTypes();
        final Tree.ExtendedType et = that.getExtendedType();
        Value dec = that.getDeclarationModel();
        defineObject(that, dec,
                sts == null ? null : getTypes(sts.getTypes()),
                et == null ? null : et.getType(), 
                et == null ? null : et.getInvocationExpression(),
                that.getClassBody(), that.getAnnotationList(), 
                gen, initDeferrer);
        //Objects defined inside methods need their init sections are exec'd
        if (!dec.isToplevel() 
                && !dec.isClassOrInterfaceMember()) {
            gen.out(gen.getNames().objectName(dec), "();");
        }
    }

    static void valueConstructor(final Tree.ClassDefinition cdef,
            final Tree.Enumerated that, final GenerateJsVisitor gen) {
        final Value d = that.getDeclarationModel();
        final Constructor c = that.getEnumerated();
        final Tree.DelegatedConstructor dc = 
                that.getDelegatedConstructor();
        final TypeDeclaration td = 
                (TypeDeclaration) c.getContainer();
        Class cdec = cdef.getDeclarationModel();
        final boolean nested = cdec.isClassOrInterfaceMember();
        
        JsIdentifierNames names = gen.getNames();
        final String objvar = names.createTempVariable();
        final String selfvar = names.self(td);
        final String typevar = names.name(td);
        final String singvar = names.name(d);
        final String constructorName = 
                typevar + names.constructorSeparator(c) + singvar;
        
        gen.out(nested?"this.":"var ", objvar, 
                "=undefined;function ", constructorName, 
                "(){if(", nested?"this.":"", objvar, "===undefined){");
        if (dc==null) {
            gen.out("$i$", typevar, "();");
        }
        if (nested) {
            gen.out("var ", selfvar, "=");
        } else {
            gen.out(objvar, "=");
        }
        gen.out("new ", typevar, ".$$;");
        if (td.isClassOrInterfaceMember()) {
            gen.out(nested?selfvar:objvar, ".outer$=this;");
        }
        
        if (dc != null) {
            Tree.InvocationExpression invoke = 
                    dc.getInvocationExpression();
            //For now, only positional invocations are 
            //allowed here
            Tree.PositionalArgumentList pal = 
                    invoke.getPositionalArgumentList();
            Tree.Primary primary = invoke.getPrimary();
            primary.visit(gen);
            gen.out("(");
            gen.getInvoker()
                .generatePositionalArguments(primary, pal, 
                        false, false);
            if (!pal.getPositionalArguments().isEmpty()) {
                gen.out(",");
            }
            Type dct = dc.getType().getTypeModel();
            if (!dct.getTypeArguments().isEmpty()) {
                printTypeArguments(dc, dct, gen, false);
                gen.out(",");
            }
            gen.out(nested?selfvar:objvar, ")");
            gen.endLine(true);
        }
        
        if (!nested) {
            gen.out("var ", selfvar, "=", objvar, ";");
        }
        addFunctionTypeArguments(cdec, objvar, gen);
        callSupertypes(cdef, cdec, typevar, gen);
        List<? extends Tree.Statement> stmts = 
                classStatementsBetweenConstructors(
                        cdef, null, that, gen);
        if (!stmts.isEmpty()) {
            gen.generateConstructorStatements(that, stmts);
        }
        stmts = classStatementsAfterConstructor(cdef, that);
        if (!stmts.isEmpty()) {
            gen.visitStatements(stmts);
        }
        if (nested) {
            gen.out("this.", objvar, "=", selfvar, ";");
        }
        gen.out("}return ", nested?"this.":"", objvar, ";};", constructorName, ".$m$=");
        encodeForRuntime(that, d, that.getAnnotationList(), gen);
        gen.out(";");
        if (td.isClassOrInterfaceMember()) {
            gen.outerSelf(td);
            gen.out(".", constructorName, "=", constructorName, ";");
        } else if (td.isShared()) {
            gen.out("x$.", constructorName, "=", constructorName, ";");
        }
        gen.out(names.name(td), ".", constructorName, "=", constructorName);
        gen.endLine(true);
    }

}
