/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.analyzer;


import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.NO_SUBSTITUTIONS;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.NO_TYPE_ARGS;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkAssignableIgnoringNull;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkIsExactly;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkIsExactlyIgnoringNull;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.declaredInPackage;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeErrorNode;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypedDeclaration;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.message;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.ExpressionVisitor.getRefinedMemberReference;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.name;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.addToIntersection;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.erasedType;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getInheritedDeclarations;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getInterveningRefinements;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getNativeHeader;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getRealScope;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getSignature;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.intersectionOfSupertypes;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.intersectionType;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isAbstraction;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isConstructor;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isImplemented;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isNativeForWrongBackend;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isObject;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isOverloadedVersion;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isTypeUnknown;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isVariadic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Annotation;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.IntersectionType;
import org.eclipse.ceylon.model.typechecker.model.LazyType;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.Reference;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.SiteVariance;
import org.eclipse.ceylon.model.typechecker.model.Specification;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 * Validates some simple rules relating to refinement and
 * native overloading. Also responsible for creating models
 * for methods and attributes written using "shortcut"-style
 * refinement.
 * This work happens during an intermediate phase in 
 * between the second and third phases of type analysis.
 * 
 * @see TypeHierarchyVisitor for the fancy stuff!
 * 
 * @author Gavin King
 *
 */
public class RefinementVisitor extends Visitor {
    
    @Override
    public void visit(Tree.AnyMethod that) {
        super.visit(that);
        inheritDefaultedArguments(
                that.getDeclarationModel());
    }

    @Override
    public void visit(Tree.AnyClass that) {
        super.visit(that);
        inheritDefaultedArguments(
                that.getDeclarationModel());
    }

    private void inheritDefaultedArguments(Declaration d) {
        Declaration rd = d.getRefinedDeclaration();
        if (rd!=d && 
                rd instanceof Functional && 
                d instanceof Functional) {
            Functional fd = (Functional) d;
            Functional frd = (Functional) rd;
            List<ParameterList> tdpls = 
                    fd.getParameterLists();
            List<ParameterList> rdpls = 
                    frd.getParameterLists();
            if (!tdpls.isEmpty() && !rdpls.isEmpty()) {
                List<Parameter> tdps = 
                        tdpls.get(0)
                            .getParameters();
                List<Parameter> rdps = 
                        rdpls.get(0)
                            .getParameters();
                for (int i=0; 
                        i<tdps.size() && i<rdps.size(); 
                        i++) {
                    Parameter tdp = tdps.get(i);
                    Parameter rdp = rdps.get(i);
                    if (tdp!=null && rdp!=null) {
                        tdp.setDefaulted(rdp.isDefaulted());
                    }
                }
            }
        }
    }

    @Override public void visit(Tree.Declaration that) {
        super.visit(that);
        
        Declaration dec = that.getDeclarationModel();
        if (dec!=null) {
            
            boolean mayBeShared = 
                    dec.isToplevel() || 
                    dec.isClassOrInterfaceMember();
            if (dec.isShared() && !mayBeShared) {
                that.addError(
                        "shared declaration is not a member of a class, interface, or package: " 
                                + message(dec) 
                                + " may not be annotated 'shared'", 
                        1200);
            }
            
            boolean mayBeRefined =
                    dec instanceof Value || 
                    dec instanceof Function ||
                    dec instanceof Class;
            if (!mayBeRefined) {
                checkNonrefinableDeclaration(that, dec);
            }
            
            boolean member = 
                    dec.isClassOrInterfaceMember() &&
                    dec.isShared() &&
                    !isConstructor(dec) &&
                    !(dec instanceof TypeParameter); //TODO: what about nested interfaces and abstract classes?!            
            if (member) {
                checkMember(that, dec);
            }
            else {
                checkNonMember(that, dec);
            }
            
            if (dec.isNativeImplementation()
                    || isNativeMember(dec)) {
                checkNative(that, dec);
            }
        }
        
    }

    private static boolean isNativeMember(Declaration dec) {
        if (dec.isMember()) {
            Declaration container = 
                    (Declaration)
                        dec.getContainer();
            return container.isNativeImplementation();
        }
        else {
            return false;
        }
    }

    private void checkNative(Tree.Declaration that, Declaration dec) {
        if (dec instanceof Setter) {
            // We ignore setter assuming the 
            // check done for their getters 
            // will have been enough
            return;
        }
        
        Declaration header = getNativeHeader(dec);
        if (header==null || 
                // getNativeHeader() will sometimes return the
                // declaration itself, or, worse, a different
                // overload of the declaration
                // TODO: fix getNativeHeader()
                !(dec==header 
                    || dec.isClassOrInterfaceMember() 
                    && dec.getContainer()==header.getContainer())) {
            checkNativeDeclaration(that, dec, header);
        }
        
    }
    
    private void checkNativeDeclaration(Tree.Declaration that, 
            Declaration dec, Declaration header) {
        if (header == null && dec.isMember()) {
            Declaration container = 
                    (Declaration)
                        dec.getContainer();
            if (container.isNative() &&
                dec.isNative() && 
                dec.isShared() && 
                !dec.isFormal() && 
                !dec.isActual() && 
                !dec.isDefault()) {
                that.addError(
                        "native member does not implement any header member: " +
                                message(dec));
            }
            if (!dec.isNative() && dec.isShared()) {
                that.addError(
                        "non-native shared members not allowed in native implementations: " +
                                message(dec));
            }
            return;
        }
        if (header != null
                && dec.isMember()
                && !dec.isParameter()
                && !dec.isNative()) {
            that.addError(
                    "member implementing a native header member must be marked native: " +
                            message(dec));
            return;
        }
        if (dec instanceof Function && 
                (header == null ||
                header instanceof Function)) {
            checkNativeMethod(that, 
                    (Function) dec, 
                    (Function) header);
        }
        else if (dec instanceof Value &&
                (header == null ||
                header instanceof Value)) {
            checkNativeValue(that, 
                    (Value) dec, 
                    (Value) header);
        }
        else if (dec instanceof Class &&
                (header == null ||
                header instanceof Class)) {
            checkNativeClass(that, 
                    (Class) dec, 
                    (Class) header);
        }
        else if (dec instanceof Interface &&
                (header == null ||
                header instanceof Interface)) {
            checkNativeInterface(that, 
                    (Interface) dec, 
                    (Interface) header);
        }
        else if (header != null) {
            that.addError("native declarations not of same type: " + 
                    message(dec));
        }
    }
    
    private void checkNativeClass(Tree.Declaration that, 
            Class dec, Class header) {
        if (header == null) {
            return;
        }
        if (dec.isAlias() || header.isAlias()) {
            return;
        }
        if (dec.isShared() && !header.isShared()) {
            that.addError("native header is not shared: " +
                    message(dec));
        }
        if (!dec.isShared() && header.isShared()) {
            that.addError("native header is shared: " +
                    message(dec));
        }
        if (dec.isAbstract() && !header.isAbstract()) {
            that.addError("native header is not abstract: " +
                    message(dec));
        }
        if (!dec.isAbstract() && header.isAbstract()) {
            that.addError("native header is abstract: " +
                    message(dec));
        }
        if (dec.isFinal() && !header.isFinal()) {
            that.addError("native header is not final: " +
                    message(dec));
        }
        if (!dec.isFinal() && header.isFinal()) {
            that.addError("native header is final: " +
                    message(dec));
        }
        if (dec.isSealed() && !header.isSealed()) {
            that.addError("native header is not sealed: " +
                    message(dec));
        }
        if (!dec.isSealed() && header.isSealed()) {
            that.addError("native header is sealed: " +
                    message(dec));
        }
        if (dec.isAnnotation() && !header.isAnnotation()) {
            that.addError("native header is not an annotation type: " +
                    message(dec));
        }
        if (!dec.isAnnotation() && header.isAnnotation()) {
            that.addError("native header is an annotation type: " +
                    message(dec));
        }
        Type dext = dec.getExtendedType();
        Type aext = header.getExtendedType();
        if ((dext != null && aext == null)
                || (dext == null && aext != null)
                || !dext.isExactly(aext)) {
            that.addError("native classes do not extend the same type: " +
                    message(dec));
        }
        List<Type> dst = 
                dec.getSatisfiedTypes();
        List<Type> ast = 
                header.getSatisfiedTypes();
        if (dst.size() != ast.size() || 
                !dst.containsAll(ast)) {
            that.addError("native classes do not satisfy the same interfaces: " +
                    message(dec));
        }
        // FIXME probably not the right tests
        checkNativeClassParameters(that,
                dec, header,
                dec.getReference(),
                header.getReference());
        checkNativeTypeParameters(that,
                dec, header,
                dec.getTypeParameters(),
                header.getTypeParameters());
        
        checkMissingMemberImpl(that, dec, header);
    }

    private void checkMissingMemberImpl(
            Tree.Declaration that, 
            Class dec, Class header) {
        List<Declaration> hdrMembers = 
                getNativeClassMembers(header);
        List<Declaration> implMembers = 
                getNativeClassMembers(dec);
        Iterator<Declaration> hdrIter = 
                hdrMembers.iterator();
        Iterator<Declaration> implIter = 
                implMembers.iterator();
        boolean hdrNext = true;
        boolean implNext = true;
        Declaration hdr = null;
        Declaration impl = null;
        while ((hdrIter.hasNext() || !hdrNext)
                && (implIter.hasNext() || !implNext)) {
            if (hdrNext) {
                hdr = hdrIter.next();
            }
            if (implNext) {
                impl = implIter.next();
            }
            int cmp = declarationCmp.compare(hdr, impl);
            if (cmp < 0) {
                if (!isImplemented(hdr)) {
                    that.addError("native header '" 
                            + hdr.getName() 
                            + "' of '" 
                            + containerName(hdr) 
                            + "' has no native implementation");
                }
                hdrNext = true;
                implNext = false;
            }
            else if (cmp > 0) {
                hdrNext = false;
                implNext = true;
            }
            else {
                hdrNext = true;
                implNext = true;
            }
        }
        while (hdrIter.hasNext()) {
            hdr = hdrIter.next();
            if (!isImplemented(hdr)) {
                that.addError("native header '" 
                        + hdr.getName() 
                        + "' of '" 
                        + containerName(hdr) 
                        + "' has no native implementation");
            }
        }
    }
    
    private static final Comparator<Declaration> declarationCmp =
            new Comparator<Declaration>() {
        @Override
        public int compare(Declaration o1, Declaration o2) {
            if (o1.getName() == null && o2.getName() == null) {
                return 0;
            }
            if (o1.getName() == null) {
                return -1;
            }
            if (o2.getName() == null) {
                return 1;
            }
            return o1.getName().compareTo(o2.getName());
        }
    };
    
    private List<Declaration> getNativeClassMembers(Class dec) {
        List<Declaration> members = dec.getMembers();
        ArrayList<Declaration> nats = 
                new ArrayList<Declaration>
                    (members.size());
        for (Declaration m : members) {
            if (m.isNative() && 
                !m.isFormal() && 
                !m.isActual() && 
                !m.isDefault()) {
                nats.add(m);
            }
        }
        Collections.sort(nats, declarationCmp);
        return nats;
    }
    
    private void checkNativeInterface(Tree.Declaration that, 
            Interface dec, Interface header) {
        if (header == null) {
            return;
        }
        if (dec.isAlias() || header.isAlias()) {
            return;
        }
        if (dec.isShared() && !header.isShared()) {
            that.addError("native header is not shared: " +
                    message(dec));
        }
        if (!dec.isShared() && header.isShared()) {
            that.addError("native header is shared: " +
                    message(dec));
        }
        if (dec.isFinal() && !header.isFinal()) {
            that.addError("native header is not final: " +
                    message(dec));
        }
        if (!dec.isFinal() && header.isFinal()) {
            that.addError("native header is final: " +
                    message(dec));
        }
        if (dec.isSealed() && !header.isSealed()) {
            that.addError("native header is not sealed: " +
                    message(dec));
        }
        if (!dec.isSealed() && header.isSealed()) {
            that.addError("native header is sealed: " +
                    message(dec));
        }
        if (dec.isAnnotation() && !header.isAnnotation()) {
            that.addError("native header is not an annotation type: " +
                    message(dec));
        }
        if (!dec.isAnnotation() && header.isAnnotation()) {
            that.addError("native header is an annotation type: " +
                    message(dec));
        }
        Type dext = dec.getExtendedType();
        Type aext = header.getExtendedType();
        if ((dext != null && aext == null)
                || (dext == null && aext != null)
                || !dext.isExactly(aext)) {
            that.addError("native classes do not extend the same type: " +
                    message(dec));
        }
        List<Type> dst = 
                dec.getSatisfiedTypes();
        List<Type> ast = 
                header.getSatisfiedTypes();
        if (dst.size() != ast.size() 
                || !dst.containsAll(ast)) {
            that.addError("native classes do not satisfy the same interfaces: " +
                    message(dec));
        }
        // FIXME probably not the right tests
        checkNativeTypeParameters(that,
                dec, header,
                dec.getTypeParameters(),
                header.getTypeParameters());
    }

    private void checkNativeMethod(Tree.Declaration that, 
            Function dec, Function header) {
        if (header == null) {
            return;
        }
        Type at = header.getType();
        Type dt = dec.getType();
        if (dt != null && !dt.isExactly(at)) {
            that.addError(
                    "native implementation must have the same return type as native header: " +
                    message(dec) + " must have the type '" +
                    at.asString(that.getUnit()) + "'");
        }
        if (dec.isShared() && !header.isShared()) {
            that.addError("native header is not shared: " +
                    message(dec));
        }
        if (!dec.isShared() && header.isShared()) {
            that.addError("native header is shared: " +
                    message(dec));
        }
        if (dec.isAnnotation() && !header.isAnnotation()) {
            that.addError("native header is not an annotation constructor: " +
                    message(dec));
        }
        if (!dec.isAnnotation() && header.isAnnotation()) {
            that.addError("native header is an annotation constructor: " +
                    message(dec));
        }
        // FIXME probably not the right tests
        checkRefiningMemberParameters(that,
                dec, header,
                dec.getReference(),
                header.getReference(),
                true);
        checkNativeTypeParameters(that,
                dec, header,
                dec.getTypeParameters(),
                header.getTypeParameters());
    }
    
    private void checkNativeValue(Tree.Declaration that, 
            Value dec, Value header) {
        if (header == null) {
            return;
        }
        Type at = header.getType();
        Type dt = dec.getType();
        if (dt!=null 
                && !dt.isExactly(at)
                && !sameObjects(dec, header)) {
            that.addError("native implementation must have the same type as native header: " +
                    message(dec) + " must have the type '" +
                    at.asString(that.getUnit()) + "'");
        }
        if (dec.isShared() && !header.isShared()) {
            that.addError("native header is not shared: " +
                    message(dec));
        }
        if (!dec.isShared() && header.isShared()) {
            that.addError("native header is shared: " +
                    message(dec));
        }
        if (dec.isVariable() && !header.isVariable()) {
            that.addError("native header is not variable: " +
                    message(dec));
        }
        if (!dec.isVariable() && header.isVariable()) {
            that.addError("native header is variable: " +
                    message(dec));
        }
        if (that instanceof Tree.ObjectDefinition) {
            checkMissingMemberImpl(that, 
                    (Class) dec.getTypeDeclaration(), 
                    (Class) header.getTypeDeclaration());
        }
    }
    
    private boolean sameObjects(Value dec, Value header) {
        return isObject(dec) 
            && isObject(header) 
            && dec.getQualifiedNameString()
                    .equals(header.getQualifiedNameString());
    }

    private void checkNativeClassParameters(Tree.Declaration that,
            Class dec, Class header,
            Reference decRef, 
            Reference hdrRef) {
        // First check if the header has constructors and the dec has none and no parameters either
        // (which means it will inherit the constructors from the header)
        boolean checkok = 
                (header.hasConstructors() || header.hasEnumerated())
                    && !dec.hasConstructors()
                    && !dec.hasEnumerated()
                    && dec.getParameterLists().isEmpty();
        if (!checkok) {
            if (dec.hasConstructors() != header.hasConstructors()
                    || dec.hasEnumerated() != header.hasEnumerated()) {
                that.addError("native classes must all have parameters or all have constructors: " + 
                        message(dec));
            }
            else if (!dec.hasConstructors() && 
                     !dec.hasEnumerated()) {
                List<ParameterList> refiningParamLists = 
                        dec.getParameterLists();
                List<ParameterList> refinedParamLists = 
                        header.getParameterLists();
                if (refinedParamLists.size()!=refiningParamLists.size()) {
                    that.addError("native classes must have the same number of parameter lists: " + 
                            message(dec));
                }
                for (int i=0; 
                        i<refinedParamLists.size() && 
                        i<refiningParamLists.size(); 
                        i++) {
                    checkParameterTypes(that, 
                            getParameterList(that, i), 
                            hdrRef, decRef, 
                            refiningParamLists.get(i), 
                            refinedParamLists.get(i),
                            true);
                }
            }
        }
    }

    private void checkNativeTypeParameters(
            Tree.Declaration that,
            Declaration impl, Declaration header, 
            List<TypeParameter> implTypeParams,
            List<TypeParameter> headerTypeParams) {
        int headerSize = headerTypeParams.size();
        int implSize = implTypeParams.size();
        if (headerSize != implSize) {
            that.addError("native header does not have the same number of type parameters as native implementation: " +
                    message(impl));
        } else {
            for (int i = 0; i < headerSize; i++) {
                TypeParameter headerTP = 
                        headerTypeParams.get(i);
                TypeParameter implTP = 
                        implTypeParams.get(i);
                if (!headerTP.getName().equals(implTP.getName())) {
                    that.addError("type parameter does not have the same name as its header: '" 
                            + implTP.getName() 
                            + "' is not '" 
                            + headerTP.getName() 
                            + "' for " 
                            + message(impl));
                }
                Type headerIntersect = 
                        intersectionOfSupertypes(headerTP);
                Type implIntersect = 
                        intersectionOfSupertypes(implTP);
                if (!headerIntersect.isExactly(implIntersect)) {
                    that.addError("type parameter does not have the same bounds as its header: '" 
                            + implTP.getName() 
                            + "' for " 
                            + message(impl));
                }
            }
        }
    }

    private void checkMember(Tree.Declaration that, 
            Declaration member) {
        String name = member.getName();
        if (name==null) {
            return;
        }
        
        if (member instanceof Setter) {
            Setter setter = (Setter) member;
            Value getter = setter.getGetter();
            Declaration rd = getter.getRefinedDeclaration();
            member.setRefinedDeclaration(rd);
            return;
        }
        
        ClassOrInterface type = 
                (ClassOrInterface) 
                    member.getContainer();
        if (member.isFormal() && 
                type instanceof Class) {
            Class c = (Class) type;
            if (!c.isAbstract() && !c.isFormal()) {
                if (c.isClassOrInterfaceMember()) {
                    that.addError("formal member belongs to concrete nested class: '" 
                            + member.getName() 
                            + "' is a member of class '" 
                            + c.getName()
                            + "' which is neither 'abstract' nor 'formal'", 
                            1100);
                }
                else {
                    that.addError("formal member belongs to concrete class: '" 
                            + member.getName() 
                            + "' is a member of class '" 
                            + c.getName()
                            + "' which is not annotated 'abstract'", 
                            1100);
                }
            }
        }
        if (member.isStatic() 
                && !type.isToplevel()) {
            that.addError("static member belongs to a nested class: '" 
                    + member.getName() 
                    + "' is a member of nested type '" 
                    + type.getName() 
                    + "'");
        }
        if (type.isDynamic()) {
            if (member instanceof Class) {
                that.addError("member class belongs to dynamic interface");
            }
            else if (!member.isFormal()) {
                that.addError("non-formal member belongs to dynamic interface");
            }
        }
        
        if (member instanceof Functional
                && !that.hasErrors()
                && isOverloadedVersion(member)) {
            checkOverloadedAnnotation(that, member);
            checkOverloadedParameters(that, member);
        }
        
        checkRefinement(that, member, type);
    }

    private void checkRefinement(Tree.Declaration that, 
            Declaration member, ClassOrInterface type) {
        Unit unit = that.getUnit();
        String name = member.getName();
        
        if (that instanceof Tree.ObjectDefinition) {
        	((Tree.ObjectDefinition) that).getDeclarationModel().getType();
        }
        
        List<Type> signature = getSignature(member);
        boolean variadic = isVariadic(member);
        Declaration root = 
                type.getRefinedMember(name, 
                        signature, variadic);
        boolean legallyOverloaded = 
                member.isNative()
                || !isOverloadedVersion(member)
                || isOverloadedVersion(root);
        if (root == null 
                || root.equals(member) 
                || root.isNative() && member.isNative()) {
            member.setRefinedDeclaration(member);
            if (member.isActual() 
                    && !isNativeForWrongBackend(member)) {
                that.addError(
                        "actual member does not refine any inherited member: "
                                + message(member)
                                + " is annotated 'actual' but '"
                                + type.getName()
                                + "' does not inherit any member named '"
                                + name +
                                "'",
                        1300);
            }
            else if (!legallyOverloaded) {
                that.addError(
                        "duplicate or overloaded member name: " 
                                + message(member));
            }
            else {
                List<Declaration> inheritedDeclarations = 
                        getInheritedDeclarations(name, type);
                if (!inheritedDeclarations.isEmpty()) {
                    that.addError(
                            "duplicate or overloaded member name in type hierarchy: " 
                                    + message(member)
                                    + " collides with "
                                    + message(inheritedDeclarations.get(0)));
                }
            }
        }
        else {
            member.setRefinedDeclaration(root);
            if (!root.withinRestrictions(unit)) {
                that.addError(
                        "refined declaration is not visible: " 
                        + message(member) 
                        + " refines " 
                        + message(root) 
                        + " which is restricted");
            }
            else if (root.isPackageVisibility() 
                    && !declaredInPackage(root, unit)) {
                that.addError(
                        "refined declaration is not visible: " 
                        + message(member) 
                        + " refines " 
                        + message(root) 
                        + " which is package private");
            }
            if (root.isCoercionPoint()) {
                // FIXME: add message pointing to the real method?
                that.addError(
                        "refined declaration is not a real method: " 
                        + message(member) 
                        + " refines " 
                        + message(root));
            }
            boolean found = false;
            TypeDeclaration rootType = 
                    (TypeDeclaration) 
                        root.getContainer();
            List<Declaration> interveningRefinements = 
                    getInterveningRefinements(member, 
                            root, type, rootType);
            for (Declaration refined: interveningRefinements) {
                TypeDeclaration interveningType = 
                        (TypeDeclaration) 
                            refined.getContainer();
                if (interveningType.isJava() &&
                        atLeastOneJava(getInterveningRefinements(
                            member, root, type, interveningType))) {
                    //If there is at least one 
                    //intervening Java refinement, 
                    //check that refinement instead
                    //of this member (Java types only 
                    //support single-instantiation 
                    //inheritance, but they do support 
                    //inheritance of raw types)
                    continue;
                }
                if (isOverloadedVersion(refined)) {
                    //if this member is overloaded, the
                    //inherited member it refines must
                    //also be overloaded
                    legallyOverloaded = true;
                }
                found = true;
                checkRefiningMember(that, refined, member, type);
            }
            if (!found) {
                if (member instanceof Function && 
                      root instanceof Function) { //see the condition in DeclarationVisitor.checkForDuplicateDeclaration()
                    that.addError(
                            "overloaded member does not exactly refine an inherited overloaded member: " 
                                    + message(member, signature, variadic, unit)
                                    + " does not match any overloaded version of "
                                    + message(root));
                }
            }
            else if (!legallyOverloaded) {
                that.addError(
                        "overloaded member does not exactly refine an inherited overloaded member: " 
                                + message(member, signature, variadic, unit)
                                + " does not match any overloaded version of "
                                + message(root));
            }
        }
    }

    private void checkOverloadedAnnotation(
            Tree.Declaration that, 
            Declaration member) {
        //non-actual overloaded methods
        //must be annotated 'overloaded'
        boolean marked = false;
        Unit unit = that.getUnit();
        for (Tree.Annotation a: 
                that.getAnnotationList()
                    .getAnnotations()) {
            Tree.Primary p = a.getPrimary();
            if (p instanceof Tree.BaseMemberExpression) {
                Tree.BaseMemberExpression bme = 
                        (Tree.BaseMemberExpression) p;
                String aname = bme.getIdentifier().getText();
                Declaration ad = 
                        p.getScope()
                         .getMemberOrParameter(unit, aname, 
                                 null, false);
                if (ad!=null && isOverloadedAnnotation(ad)) {
                    marked = true;
                }
            }
        }
        if (!marked) {
            if (member.isActual()) {
                that.addUsageWarning(
                        Warning.unknownWarning,
                        "overloaded function should be declared with the 'overloaded' annotation in 'java.lang'");
            }
            else {
                if (member instanceof Constructor) {
                    //default constructors are the only 
                    //thing that can legally have no name
                    that.addError("duplicate default constructor (overloaded default constructor must be declared with the 'overloaded' annotation in 'java.lang')");
                }
                else if (member instanceof Function) {
                    //functions are the only thing  
                    //that can legally have a name
                    //and be overloaded
                    that.addError(
                            "duplicate declaration: the name '" 
                            + member.getName()
                            + "' is not unique in this scope " +
                            "(overloaded function must be declared with the 'overloaded' annotation in 'java.lang')");
                }
            }
        }
    }

    private static boolean isOverloadedAnnotation(Declaration ad) {
        return "java.lang::overloaded"
                .equals(ad.getQualifiedNameString());
    }

    private void checkOverloadedParameters(
            Tree.Declaration that, 
            Declaration member) {
        String name = member.getName();
        Declaration abstraction = 
                member.getScope()
                      .getDirectMember(name, 
                              null, false);
        if (abstraction!=null) {
            Functional fun = (Functional) member;
            List<Parameter> parameters = 
                    fun.getFirstParameterList()
                       .getParameters();
            for (Parameter param: parameters) {
                if (param.isDefaulted()) {
                    that.addError("overloaded function parameter must be required: parameter '" 
                            + param.getName() 
                            + "' is defaulted");
                }
            }
            Unit unit = that.getUnit();
            for (Declaration dec: 
                    abstraction.getOverloads()) {
                if (dec==member) break;
                Functional other = (Functional) dec;
                List<Parameter> otherParams = 
                        other.getFirstParameterList()
                             .getParameters();
                if (otherParams.size() == parameters.size()) {
                    boolean allSame = true;
                    for (int i=0; i<parameters.size(); i++) {
                        TypeDeclaration paramType = 
                                erasedType(parameters.get(i), 
                                         unit);
                        TypeDeclaration otherType = 
                                erasedType(otherParams.get(i), 
                                         unit);
                        if (paramType!=null && otherType!=null
                                && !paramType.equals(otherType)) {
                            allSame = false;
                            break;
                        }
                    }
                    if (allSame) {
                        that.addError("non-unique parameter list erasure for overloaded function: each overloaded declaration of '"
                                + name + "' must have a distinct parameter list erasure");
                    }
                }
            }
        }
    }

    private void checkRefiningMember(
            Tree.Declaration that, 
            Declaration refined, Declaration member,
            ClassOrInterface type) {
        boolean checkTypes = true;
        if (member instanceof Function) {
            if (!(refined instanceof Function)) {
                that.addError(
                        "refined declaration is not a method: " 
                        + message(member) 
                        + " refines " 
                        + message(refined));
                checkTypes = false;
            }
        }
        else if (member instanceof Class) {
            if (!(refined instanceof Class)) {
                that.addError(
                        "refined declaration is not a class: " 
                        + message(member) 
                        + " refines " 
                        + message(refined));
                checkTypes = false;
            }
        }
        else if (member instanceof TypedDeclaration) {
            if (refined instanceof Class || 
                refined instanceof Function) {
                that.addError(
                        "refined declaration is not an attribute: " 
                        + message(member) 
                        + " refines " 
                        + message(refined));
                checkTypes = false;
            }
            else if (refined instanceof TypedDeclaration) {
                TypedDeclaration rtd = (TypedDeclaration) refined;
                TypedDeclaration mtd = (TypedDeclaration) member;
                if (rtd.isVariable() && !mtd.isVariable()) {
                    if (member instanceof Value) {
                        that.addError(
                                "non-variable attribute refines a variable attribute: " 
                                + message(member) 
                                + " refines variable " 
                                + message(refined) 
                                + " and so must be 'variable' or have a setter", 
                                804);
                    }
                    else {
                        //TODO: this message seems like it's not quite right
                        that.addError(
                                "non-variable attribute refines a variable attribute: " 
                                + message(member) 
                                + " refines variable " 
                                + message(refined));
                    }
                }
            }
        }
        if (!member.isActual()) {
            that.addError(
                    "non-actual member collides with an inherited member: " 
                    + message(member) 
                    + " refines " 
                    + message(refined) 
                    + " but is not annotated 'actual'", 
                    600);
        }
        else if (!refined.isDefault() && !refined.isFormal()) {
            that.addError(
                    "member refines a non-default, non-formal member: " 
                    + message(member) 
                    + " refines " 
                    + message(refined) 
                    + " which is not annotated 'formal' or 'default'", 
                    500);
        }
        if (checkTypes && !type.isInconsistentType()) {
            checkRefinedTypeAndParameterTypes(that, 
                    member, type, refined);
        }
    }

    private static boolean atLeastOneJava(
            List<Declaration> members) {
        for (Declaration d: members) {
            if (d.isJava()) {
                return members.size()>1;
            }
        }
        return false;
    }

    /*private boolean refinesOverloaded(Declaration dec, 
            Declaration refined, Type st) {
        Functional fun1 = (Functional) dec;
        Functional fun2 = (Functional) refined;
        if (fun1.getParameterLists().size()!=1 ||
            fun2.getParameterLists().size()!=1) {
            return false;
        }
        List<Parameter> pl1 = fun1.getParameterLists()
                .get(0).getParameters();
        List<Parameter> pl2 = fun2.getParameterLists()
                .get(0).getParameters();
        if (pl1.size()!=pl2.size()) {
            return false;
        }
        for (int i=0; i<pl1.size(); i++) {
            Parameter p1 = pl1.get(i);
            Parameter p2 = pl2.get(i);
            if (p1==null || p2==null ||
                    p1.getType()==null || 
                    p2.getType()==null) {
                return false;
            }
            else {
                Type p2st = p2.getType()
                        .substitute(st.getTypeArguments());
                if (!matches(p1.getType(), p2st, dec.getUnit())) {
                    return false;
                }
            }
        }
        return true;
    }*/
    
    private void checkRefinedTypeAndParameterTypes(
            Tree.Declaration that, Declaration refining, 
            ClassOrInterface ci, Declaration refined) {
        
        List<TypeParameter> refinedTypeParams = 
                refined.getTypeParameters();
        List<TypeParameter> refiningTypeParams = 
                refining.getTypeParameters();
        checkRefiningMemberTypeParameters(that, 
                refining, refined, refinedTypeParams, 
                refiningTypeParams);
        List<Type> typeArgs = 
                checkRefiningMemberUpperBounds(that, 
                    ci, refined, 
                    refinedTypeParams, 
                    refiningTypeParams);
        
        Type cit = ci.getType();
        Reference refinedMember = 
                cit.getTypedReference(refined, 
                        typeArgs);
        Reference refiningMember = 
                cit.getTypedReference(refining, 
                        typeArgs);
        Declaration refinedMemberDec = 
                refinedMember.getDeclaration();
        Declaration refiningMemberDec = 
                refiningMember.getDeclaration();
        Node typeNode = getTypeErrorNode(that);
        if (refinedMemberIsDynamicallyTyped(
                refinedMemberDec, refiningMemberDec)) {
            checkRefiningMemberDynamicallyTyped(refined, 
                    refiningMemberDec, typeNode);
        }
        else if (refiningMemberIsDynamicallyTyped(
                refinedMemberDec, refiningMemberDec)) {
            checkRefinedMemberDynamicallyTyped(refined, 
                    refinedMemberDec, typeNode);
        }
        else if (refinedMemberIsVariable(refinedMemberDec)) {
            checkRefinedMemberTypeExactly(refiningMember, 
                    refinedMember, typeNode, refined, refining);
        }
        else {
            //note: this version checks return type and parameter types in one shot, but the
            //resulting error messages aren't as friendly, so do it the hard way instead!
            //checkAssignable(refiningMember.getFullType(), refinedMember.getFullType(), that,
            checkRefinedMemberTypeAssignable(refiningMember, 
                    refinedMember, typeNode, refined, refining,
                    that instanceof Tree.ObjectDefinition);
        }
        if (refining instanceof Functional && 
             refined instanceof Functional) {
           checkRefiningMemberParameters(that, refining, refined, 
                   refinedMember, refiningMember, false);
        }
    }

    private void checkRefiningMemberParameters(
            Tree.Declaration that,
            Declaration refining, Declaration refined,
            Reference refinedMember, 
            Reference refiningMember,
            boolean forNative) {
        Functional refiningFun = (Functional) refining;
        Functional refinedFun = (Functional) refined;
        List<ParameterList> refiningParamLists = 
                refiningFun.getParameterLists();
        List<ParameterList> refinedParamLists = 
                refinedFun.getParameterLists();
        if (refinedParamLists.size()!=refiningParamLists.size()) {
            String subject = 
                    forNative ? 
                        "native header" :
                        "refined member";
            String current = 
                    forNative ? 
                        "native implementation" : 
                        "refining member";
            StringBuilder message = new StringBuilder();
            message.append(current)
                    .append(" must have the same number of parameter lists as ")
                    .append(subject)
                    .append(": ")
                    .append(message(refining));
            if (!forNative) {
                message.append(" refines ")
                        .append(message(refined));
            }
            that.addError(message.toString());
        }
        for (int i=0; 
                i<refinedParamLists.size() && 
                i<refiningParamLists.size(); 
                i++) {
            checkParameterTypes(that, 
                    getParameterList(that, i), 
                    refiningMember, refinedMember, 
                    refiningParamLists.get(i), 
                    refinedParamLists.get(i),
                    forNative);
        }
    }

    private boolean refinedMemberIsVariable(
            Declaration refinedMemberDec) {
        if (refinedMemberDec instanceof TypedDeclaration) {
            TypedDeclaration typedDec = 
                    (TypedDeclaration) 
                        refinedMemberDec;
            return typedDec.isVariable();
        }
        else {
            return false;
        }
    }

    private void checkRefinedMemberDynamicallyTyped(
            Declaration refined,
            Declaration refinedMemberDec, 
            Node typeNode) {
        TypedDeclaration td = 
                (TypedDeclaration) 
                    refinedMemberDec;
        if (!td.isDynamicallyTyped()) {
            typeNode.addError(
                    "member which refines statically typed refined member must also be statically typed: " + 
                    message(refined));
        }
    }

    private void checkRefiningMemberDynamicallyTyped(
            Declaration refined,
            Declaration refiningMemberDec, 
            Node typeNode) {
        TypedDeclaration td = 
                (TypedDeclaration) 
                    refiningMemberDec;
        if (!td.isDynamicallyTyped()) {
            typeNode.addError(
                    "member which refines dynamically typed refined member must also be dynamically typed: " + 
                    message(refined));
        }
    }

    private boolean refiningMemberIsDynamicallyTyped(
            Declaration refinedMemberDec, Declaration refiningMemberDec) {
        return refinedMemberDec instanceof TypedDeclaration 
            && refiningMemberDec instanceof TypedDeclaration 
            && ((TypedDeclaration) refiningMemberDec)
                    .isDynamicallyTyped();
    }

    private boolean refinedMemberIsDynamicallyTyped(
            Declaration refinedMemberDec, 
            Declaration refiningMemberDec) {
        return refinedMemberDec instanceof TypedDeclaration 
            && refiningMemberDec instanceof TypedDeclaration 
            && ((TypedDeclaration) refinedMemberDec)
                    .isDynamicallyTyped();
    }

    private void checkRefiningMemberTypeParameters(
            Tree.Declaration that,
            Declaration dec, Declaration refined, 
            List<TypeParameter> refinedTypeParams,
            List<TypeParameter> refiningTypeParams) {
        int refiningSize = refiningTypeParams.size();
        int refinedSize = refinedTypeParams.size();
        if (refiningSize!=refinedSize) {
            that.addError("refining member does not have the same number of type parameters as refined member: "
                    + message(dec)
                    + " refines "
                    + message(refined));
        }
    }

    private List<Type> checkRefiningMemberUpperBounds(
            Tree.Declaration that,
            ClassOrInterface ci, Declaration refined,
            List<TypeParameter> refinedTypeParams, 
            List<TypeParameter> refiningTypeParams) {
        int refiningSize = refiningTypeParams.size();
        int refinedSize = refinedTypeParams.size();
        int max = refiningSize <= refinedSize ? 
                refiningSize : refinedSize;
        if (max==0) {
            return NO_TYPE_ARGS;
        }
        //we substitute the type parameters of the refined
        //declaration into the bounds of the refining 
        //declaration
        Map<TypeParameter, Type> substitution =
                new HashMap<TypeParameter, Type>();
        for (int i=0; i<max; i++) {
            TypeParameter refinedTypeParam = 
                    refinedTypeParams.get(i);
            TypeParameter refiningTypeParam = 
                    refiningTypeParams.get(i);
            substitution.put(refiningTypeParam, 
                    refinedTypeParam.getType());
        }
        Map<TypeParameter, SiteVariance> noVariances = 
                emptyMap();
        TypeDeclaration rc = 
                (TypeDeclaration) 
                    refined.getContainer();
        //we substitute the type arguments of the subtype's
        //instantiation of the supertype into the bounds of 
        //the refined declaration
        Type supertype = ci.getType().getSupertype(rc);
        Map<TypeParameter, Type> args = 
                supertype.getTypeArguments();
        Map<TypeParameter, SiteVariance> variances = 
                supertype.getVarianceOverrides();
        List<Type> typeArgs = 
                new ArrayList<Type>(max); 
        for (int i=0; i<max; i++) {
            TypeParameter refinedTypeParam = 
                    refinedTypeParams.get(i);
            TypeParameter refiningTypeParam = 
                    refiningTypeParams.get(i);
            
            refiningTypeParam.setReified(
                    refinedTypeParam.isReified());
            
            Type refinedProducedType = 
                    refinedTypeParam.getType();
            List<Type> refinedBounds = 
                    refinedTypeParam.getSatisfiedTypes();
            List<Type> refiningBounds = 
                    refiningTypeParam.getSatisfiedTypes();
            Unit unit = that.getUnit();
            for (Type bound: refiningBounds) {
                Type refiningBound = 
                        bound.substitute(substitution, 
                                noVariances);
                //for every type constraint of the refining member, there must
                //be at least one type constraint of the refined member which
                //is assignable to it, guaranteeing that the intersection of
                //the refined member bounds is assignable to the intersection
                //of the refining member bounds
                //TODO: would it be better to just form the intersections and
                //      test assignability directly (the error messages might
                //      not be as helpful, but it might be less restrictive)
                boolean ok = false;
                for (Type refinedBound: refinedBounds) {
                    refinedBound = 
                            refinedBound.substitute(args, variances);
                    if (refinedBound.isSubtypeOf(refiningBound)) {
                        ok = true;
                    }
                }
                if (!ok) {
                    that.addError(
                            "refining member type parameter '" 
                            + refiningTypeParam.getName() 
                            + "' has upper bound which refined member type parameter '" 
                            + refinedTypeParam.getName() 
                            + "' of " 
                            + message(refined) 
                            + " does not satisfy: '" 
                            + bound.asString(unit) 
                            + "' ('" 
                            + refiningTypeParam.getName() 
                            + "' should be upper bounded by '" 
                            + intersectionOfSupertypes(refinedTypeParam)
                                .substitute(args, variances)
                                .asString(unit) 
                            + "')");
                }
            }
            for (Type bound: refinedBounds) {
                Type refinedBound =
                        bound.substitute(args, variances);
                boolean ok = false;
                for (Type refiningBound: refiningBounds) {
                    refiningBound = 
                            refiningBound.substitute(
                                    substitution, 
                                    noVariances);
                    if (refinedBound.isSubtypeOf(refiningBound)) {
                        ok = true;
                    }
                }
                if (!ok) {
                    that.addUnsupportedError(
                            "refined member type parameter '" 
                            + refinedTypeParam.getName() 
                            + "' of " 
                            + message(refined) 
                            + " has upper bound which refining member type parameter '" 
                            + refiningTypeParam.getName() 
                            + "' does not satisfy: '" 
                            + bound.asString(unit) 
                            + "' ('" 
                            + refiningTypeParam.getName() 
                            + "' should be upper bounded by '" 
                            + intersectionOfSupertypes(refinedTypeParam)
                                .substitute(args, variances)
                                .asString(unit) 
                            + "')");
                }
            }
            typeArgs.add(refinedProducedType);
        }
        return typeArgs;
    }

    private void checkRefinedMemberTypeAssignable(
            Reference refiningMember, 
            Reference refinedMember,
            Node that, 
            Declaration refined, 
            Declaration refining,
            boolean objectDeclaration) {
        Unit unit = that.getUnit();
        Type refiningType = refiningMember.getType();
        Type refinedType = refinedMember.getType();
        if (!isTypeUnknown(refinedType)) {
            if (that instanceof Tree.LocalModifier
            		&& !objectDeclaration) {
                //infer the type of an actual member 
                //by taking the intersection of all 
                //members it refines
                //NOTE: feature not blessed by the spec!
                TypedDeclaration td = 
                        (TypedDeclaration) 
                            refining;
                Tree.LocalModifier mod = 
                        (Tree.LocalModifier) 
                            that;
                Type t;
                t = isTypeUnknown(refiningType) ? 
                        refinedType : 
                        intersectionType(
                            refinedType, 
                            refiningType, 
                            unit);
                td.setType(t);
                mod.setTypeModel(t);
                return;
            }
            checkAssignableIgnoringNull(refiningType, 
                    refinedType,  that, refined,
                    "type of member must be assignable to type of refined member " + 
                    message(refined), 
                    9000);
            checkSmallRefinement(that, 
                    refiningMember.getDeclaration(), 
                    refinedMember.getDeclaration());
        }
    }

    private void checkRefinedMemberTypeExactly(
            Reference refiningMember, 
            Reference refinedMember, 
            Node that, 
            Declaration refined,
            Declaration refining) {
        Unit unit = that.getUnit();
        Type refiningType = refiningMember.getType();
        Type refinedType = refinedMember.getType();
        if (!isTypeUnknown(refinedType)) {
            if (that instanceof Tree.LocalModifier) {
                TypedDeclaration td = 
                        (TypedDeclaration) 
                            refining;
                Tree.LocalModifier mod = 
                        (Tree.LocalModifier) 
                            that;
                Type t;
                if (isTypeUnknown(refiningType)) {
                    t = refinedType;
                    td.setType(t);
                    mod.setTypeModel(t);
                }
                else {
                    checkIsExactly(refiningType, 
                            refinedType, that,
                            "inferred type of member must be exactly the same as type of variable refined member: " + 
                            message(refined), 
                            9000);
                }
                return;
            }
        
            checkIsExactlyIgnoringNull(refined,
                    refiningType, 
                    refinedType, that,
                    "type of member must be exactly the same as type of variable refined member: " + 
                    message(refined), 
                    9000);
        }
    }

    private void checkSmallRefinement(Node that, 
            Declaration refiningDeclaration, 
            Declaration refinedDeclaration) {
        if (refiningDeclaration instanceof FunctionOrValue &&
            refinedDeclaration instanceof FunctionOrValue) {
            FunctionOrValue refiningFunctionOrValue = 
                    (FunctionOrValue)
                        refiningDeclaration;
            FunctionOrValue refinedFunctionOrValue = 
                    (FunctionOrValue)
                        refinedDeclaration;
            boolean refiningSmall = 
                    refiningFunctionOrValue.isSmall();
            boolean refinedSmall = 
                    refinedFunctionOrValue.isSmall();
            if (refiningSmall && !refinedSmall) {
                that.addUsageWarning(Warning.smallIgnored, 
                        "small annotation on actual member " 
                        + message(refiningDeclaration) 
                        + " will be ignored: " 
                        + message(refinedDeclaration) 
                        + " is not small");
            }
            refiningFunctionOrValue.setSmall(refinedSmall);
        }
    }

    /*private void checkUnshared(Tree.Declaration that, Declaration dec) {
        if (dec.isActual()) {
            that.addError("actual member is not shared", 701);
        }
        if (dec.isFormal()) {
            that.addError("formal member is not shared", 702);
        }
        if (dec.isDefault()) {
            that.addError("default member is not shared", 703);
        }
    }*/

    private void checkNonrefinableDeclaration(Tree.Declaration that,
            Declaration dec) {
        if (dec.isActual()) {
            that.addError("actual declaration is not a method, getter, reference attribute, or class", 
                    1301);
        }
        if (dec.isFormal()) {
            that.addError("formal declaration is not a method, getter, reference attribute, or class", 
                    1302);
        }
        if (dec.isDefault()) {
            that.addError("default declaration is not a method, getter, reference attribute, or class", 
                    1303);
        }
    }

    private void checkNonMember(Tree.Declaration that, Declaration dec) {
        boolean mayBeShared = !(dec instanceof TypeParameter);
        boolean nonTypeMember = !dec.isClassOrInterfaceMember();
        
        String name = dec.getName();
        
        if (dec.isStatic()) {
            if (nonTypeMember) {
                that.addError("static declaration is not a member of a class or interface: '" 
                        + name 
                        + "' is not defined directly in the body of a class or interface");
            }
            else {
                ClassOrInterface type = 
                        (ClassOrInterface) 
                            dec.getContainer();
                if (!type.isToplevel()) {
                    that.addError("static declaration belongs to a nested a class or interface: '" 
                            + name 
                            + "' is a member of nested type '" 
                            + type.getName() 
                            + "'");
                }
            }
        }
        
        if (nonTypeMember && mayBeShared) {
            if (dec.isActual()) {
                that.addError("actual declaration is not a member of a class or interface: '" + 
                        name + "'", 
                        1301);
            }
            if (dec.isFormal()) {
                that.addError("formal declaration is not a member of a class or interface: '" + 
                        name + "'", 
                        1302);
            }
            if (dec.isDefault()) {
                that.addError("default declaration is not a member of a class or interface: '" + 
                        name + "'", 
                        1303);
            }
        }
        else if (!dec.isShared() && mayBeShared) {
            if (dec.isActual()) {
                that.addError("actual declaration must be shared: '" + 
                        name + "'", 
                        701);
            }
            if (dec.isFormal()) {
                that.addError("formal declaration must be shared: '" + 
                        name + "'", 
                        702);
            }
            if (dec.isDefault()) {
                that.addError("default declaration must be shared: '" + 
                        name + "'", 
                        703);
            }
        }
        else {
            if (dec.isActual()) {
                that.addError("declaration may not be actual: '" + 
                        name + "'", 
                        1301);
            }
            if (dec.isFormal()) {
                that.addError("declaration may not be formal: '" + 
                        name + "'", 
                        1302);
            }
            if (dec.isDefault()) {
                that.addError("declaration may not be default: '" + 
                        name + "'", 
                        1303);
            }
        }
        
        if (isOverloadedVersion(dec)) {
            if (isConstructor(dec)) {
                checkOverloadedAnnotation(that, dec);
                checkOverloadedParameters(that, dec);
            }
            else {
                that.addError("duplicate declaration: the name '" 
                        + name + "' is not unique in this scope");
            }
        }
        else if (isAbstraction(dec)) {
            //validation of default constructor overloading
            //is a real mess because it's the Class itself
            //that is considered overloaded in the model
            if (that instanceof Tree.ClassDefinition
                    && !that.hasErrors()) {
                Tree.ClassDefinition def = 
                        (Tree.ClassDefinition) that;
                Class abs = (Class) dec; //this is an abstraction
                //iterate over all the default constructors
                //declarations of the class (need to find
                //the tree Nodes so that errors can be
                //correctly located)
                for (Tree.Statement st: 
                        def.getClassBody()
                           .getStatements()) {
                    if (st instanceof Tree.Constructor) {
                        Tree.Constructor node =
                                (Tree.Constructor) st;
                        if (node.getIdentifier()==null) {
                            Constructor con = node.getConstructor();
                            //get the corresponding overloaded version
                            Class cla = classOverloadForConstructor(abs, con);
                            checkOverloadedAnnotation(node, con);
                            checkOverloadedParameters(node, cla);
                        }
                    }
                }
            }
        }
        
        if (isConstructor(dec) && dec.isShared()) {
            Scope container = dec.getContainer();
            if (container instanceof Class) {
                Class clazz = (Class) container;
                Declaration member = 
                        intersectionOfSupertypes(clazz)
                            .getDeclaration()
                            .getMember(name, null, false);
                if (member!=null && 
                        member.isShared() &&
                        !isConstructor(member)) {
                    Declaration supertype = 
                            (Declaration) 
                                member.getContainer();
                    that.addError("constructor has same name as an inherited member '" 
                            + clazz.getName() 
                            + "' inherits '" 
                            + member.getName() 
                            + "' from '" 
                            + supertype.getName(that.getUnit()) 
                            + "'");
                }
            }
        }
        
    }
    
    //A class with an overloaded default constructor
    //is represented in the model as an overloaded 
    //class. This method gives me the Class object
    //corresponding to a Constructor that represents
    //an overloaded default constructor
    private static Class classOverloadForConstructor(
            Class abs, Constructor con) {
        //we are given the abstraction, and we're
        //looking for the right overloaded version
        for (Declaration d: abs.getOverloads()) {
            if (d instanceof Class) {
                Class c = (Class) d;
                //wow, this is amazingly fragile
                //TODO: store a ref back to the
                //Class in the COnstructor model
                if (c.getParameterList() 
                        == con.getParameterList()) {
                    return c;
                }
            }
        }
        return null;
    }
    
    private static String containerName(
            Reference member) {
        return containerName(member.getDeclaration());
    }

    private static String containerName(
            Declaration member) {
        Scope container = 
                member.getContainer();
        if (container instanceof Declaration) {
            Declaration dec = (Declaration) container;
            return dec.getName();
        }
        else if (container instanceof Package) {
            Package pack = (Package) container;
            return pack.getQualifiedNameString();
        }
        else {
            return "Unknown";
        }
    }

    private void checkParameterTypes(
            Tree.Declaration that, 
            Tree.ParameterList pl,
            Reference member, 
            Reference refinedMember,
            ParameterList params, 
            ParameterList refinedParams, 
            boolean forNative) {
        List<Parameter> paramsList = params.getParameters();
        List<Parameter> refinedParamsList = 
                refinedParams.getParameters();
        if (paramsList.size()!=refinedParamsList.size()) {
           handleWrongParameterListLength(that, 
                   member, refinedMember, forNative,
                   pl);
        }
        else {
            for (int i=0; i<paramsList.size(); i++) {
                Parameter rparam = refinedParamsList.get(i);
                Parameter param = paramsList.get(i);
                Tree.Parameter parameter = 
                        pl.getParameters().get(i);
                if (forNative &&
                        !param.getName().equals(rparam.getName())) {
                    parameter.addError(
                            "parameter does not have the same name as its header: '"
                            + param.getName() 
                            + "' is not '" 
                            + rparam.getName() 
                            + "' for "
                            + message(refinedMember.getDeclaration()));
                }
                if (rparam.isSequenced() && !param.isSequenced()) {
                    parameter.addError(
                            "parameter must be variadic: parameter '" 
                            + rparam.getName()
                            + "' of "
                            + (forNative ? "native header " : "refined member ")
                            + message(refinedMember.getDeclaration()) 
                            + " is variadic");
                }
                if (!rparam.isSequenced() && param.isSequenced()) {
                    parameter.addError("parameter may not be variadic: parameter '" 
                            + rparam.getName()
                            + "' of "
                            + (forNative ? "native header " : "refined member ")
                            + message(refinedMember.getDeclaration()) 
                            + " is not variadic");
                }
                Type refinedParameterType = 
                        refinedMember.getTypedParameter(rparam)
                                .getFullType();
                Type parameterType = 
                        member.getTypedParameter(param)
                                .getFullType();
                Node typeNode = parameter;
                if (parameter instanceof Tree.ParameterDeclaration) {
                    Tree.ParameterDeclaration pd = 
                            (Tree.ParameterDeclaration) 
                                parameter;
                    Tree.Type type = 
                            pd.getTypedDeclaration()
                                .getType();
                    if (type!=null) {
                        typeNode = type;
                    }
                }
                if (parameter!=null) {
                    if (rparam.getModel().isDynamicallyTyped()) {
                        checkRefiningParameterDynamicallyTyped(
                                member, refinedMember, 
                                param, typeNode);
                    }
                    else if (param.getModel() != null && 
                             param.getModel().isDynamicallyTyped()) {
                        checkRefinedParameterDynamicallyTyped(
                                member, refinedMember, 
                                rparam, param, typeNode);
                    }
                    else if (refinedParameterType==null || 
                             parameterType==null) {
                        handleUnknownParameterType(member, 
                                refinedMember, param,
                                typeNode, forNative);
                    }
                    else {
                        checkRefiningParameterType(member, 
                                refinedMember, refinedParams, 
                                rparam, refinedParameterType,
                                param, parameterType,
                                typeNode, forNative);
                        checkRefiningParameterSmall(typeNode, 
                                member.getDeclaration(), param, 
                                refinedMember.getDeclaration(), rparam);
                    }
                }
            }
        }
    }

    private void checkRefiningParameterSmall(Node that, 
                Declaration member, Parameter param, 
                Declaration refinedMember, 
                Parameter refinedParam) {
        if (param.getModel().isSmall()
                && !refinedParam.getModel().isSmall()) {
            that.addUsageWarning(Warning.smallIgnored, 
                    "small annotation on parameter '" 
                    + param.getName() 
                    + "' of '" 
                    + member.getName()
                    + "' will be ignored: corresponding parameter '" 
                    + refinedParam.getName() 
                    + "' of '" 
                    + refinedMember.getName() 
                    + "' is not small");
        }
        param.getModel().setSmall(refinedParam.getModel().isSmall());
    }

    private void handleWrongParameterListLength(
            Tree.Declaration that,
            Reference member, 
            Reference refinedMember,
            boolean forNative, 
            Tree.ParameterList paramList) {
        StringBuilder message = new StringBuilder();
        String subject = 
                forNative ? 
                    "native header" : 
                    "refined member";
        message.append("member does not have the same number of parameters as ") 
                .append(subject)
                .append(": '") 
                .append(member.getDeclaration().getName())
                .append("'");
        if (!forNative) {
            message.append(" declared by '") 
                    .append(containerName(member)) 
                    .append("' refining '") 
                    .append(refinedMember.getDeclaration().getName())
                    .append("' declared by '") 
                    .append(containerName(refinedMember))
                    .append("'");
        }
        paramList.addError(message.toString(), 9100);
    }

    private static void checkRefiningParameterType(
            Reference member,
            Reference refinedMember, 
            ParameterList refinedParams,
            Parameter rparam, 
            Type refinedParameterType,
            Parameter param, 
            Type parameterType,
            Node typeNode, 
            boolean forNative) {
        //TODO: consider type parameter substitution!!!
        StringBuilder message = new StringBuilder();
        String subject = 
                forNative ? 
                    "native header" : 
                    "refined member";
        message.append("type of parameter '")
                .append(param.getName())
                .append("' of '")
                .append(member.getDeclaration().getName())
                .append("'");
        if (!forNative) {
            message.append(" declared by '")
                    .append(containerName(member)) 
                    .append("'");
        }
        message.append(" is different to type of corresponding parameter '")
                .append(rparam.getName())
                .append("' of ") 
                .append(subject)
                .append(" '")
                .append(refinedMember.getDeclaration().getName())
                .append("'");
        if (!forNative) {
            message.append(" of '")
                    .append(containerName(refinedMember)) 
                    .append("'");
        }
        checkIsExactlyIgnoringNull( 
                refinedParams.isNamedParametersSupported(), 
                parameterType, refinedParameterType, 
                typeNode, message.toString(),
                9200);
    }

    private void handleUnknownParameterType(
            Reference member,
            Reference refinedMember, 
            Parameter param, 
            Node typeNode, 
            boolean forNative) {
        StringBuilder message = new StringBuilder();
        String subject = 
                forNative ? 
                    "native header" : 
                    "refined member";
        message.append("could not determine if parameter type is the same as the corresponding parameter of ") 
                .append(subject).append(": '")
                .append(param.getName())
                .append("' of '") 
                .append(member.getDeclaration().getName());
        if (!forNative) {
                message.append("' declared by '") 
                        .append(containerName(member))
                        .append("' refining '") 
                        .append(refinedMember.getDeclaration().getName())
                        .append("' declared by '") 
                        .append(containerName(refinedMember))
                        .append("'");
        }
        typeNode.addError(message.toString());
    }

    private void checkRefinedParameterDynamicallyTyped(
            Reference member, 
            Reference refinedMember,
            Parameter rparam, Parameter param, 
            Node typeNode) {
        if (!rparam.getModel().isDynamicallyTyped()) {
            typeNode.addError(
                    "parameter which refines statically typed parameter must also be statically typed: '" 
                    + param.getName() 
                    + "' of '" 
                    + member.getDeclaration().getName() 
                    + "' declared by '" 
                    + containerName(member) 
                    + "' refining '" 
                    + refinedMember.getDeclaration().getName() 
                    + "' declared by '" 
                    + containerName(refinedMember) 
                    + "'");
        }
    }

    private void checkRefiningParameterDynamicallyTyped(
            Reference member, Reference refinedMember,
            Parameter param, Node typeNode) {
        if (!param.getModel().isDynamicallyTyped()) {
            typeNode.addError(
                    "parameter which refines dynamically typed parameter must also be dynamically typed: '" 
                    + param.getName() 
                    + "' of '" 
                    + member.getDeclaration().getName() 
                    + "' declared by '" 
                    + containerName(member) 
                    + "' refining '" 
                    + refinedMember.getDeclaration().getName() 
                    + "' declared by '" 
                    + containerName(refinedMember) 
                    + "'");
        }
    }

    private static Tree.ParameterList getParameterList(
            Tree.Declaration that, int i) {
        if (that instanceof Tree.AnyMethod) {
            Tree.AnyMethod am = (Tree.AnyMethod) that;
            return am.getParameterLists().get(i);
        }
        else if (that instanceof Tree.AnyClass) {
            Tree.AnyClass ac = (Tree.AnyClass) that;
            return ac.getParameterList();
        }
        else if (that instanceof Tree.Constructor) {
            Tree.Constructor con = (Tree.Constructor) that;
            return con.getParameterList();
        }
        else {
            return null;
        }
    }
    
    @Override
    public void visit(Tree.ParameterList that) {
        super.visit(that);
        boolean foundSequenced = false;
        boolean foundDefault = false;
        ParameterList pl = that.getModel();
        for (Tree.Parameter p: that.getParameters()) {
            if (p!=null) {
                Parameter pm = p.getParameterModel();
                if (pm!=null) {
                    if (pm.isDefaulted()) {
                        if (foundSequenced) {
                            p.addError("defaulted parameter must occur before variadic parameter");
                        }
                        foundDefault = true;
                        if (!pl.isFirst()) {
                            p.addError("only the first parameter list may have defaulted parameters");
                        }
                    }
                    else if (pm.isSequenced()) {
                        if (foundSequenced) {
                            p.addError("parameter list may have at most one variadic parameter");
                        }
                        foundSequenced = true;
                        if (!pl.isFirst()) {
                            p.addError("only the first parameter list may have a variadic parameter");
                        }
                        if (foundDefault && 
                                pm.isAtLeastOne()) {
                            p.addError("parameter list with defaulted parameters may not have a nonempty variadic parameter");
                        }
                    }
                    else {
                        if (foundDefault) {
                            p.addError("required parameter must occur before defaulted parameters");
                        }
                        if (foundSequenced) {
                            p.addError("required parameter must occur before variadic parameter");
                        }
                    }
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.SpecifierStatement that) {
        super.visit(that);
        
        List<Type> sig = new ArrayList<Type>();
        Tree.Term term = that.getBaseMemberExpression();
        while (term instanceof Tree.ParameterizedExpression) {
            sig.clear();
            Tree.ParameterizedExpression pe = 
                    (Tree.ParameterizedExpression) 
                        term;
            Tree.TypeParameterList typeParameterList = 
                    pe.getTypeParameterList();
            if (typeParameterList!=null) {
                //TODO: remove this for #1329
                typeParameterList.addError("specification statements may not have type parameters");
            }
            Tree.ParameterList pl = 
                    pe.getParameterLists()
                        .get(0);
            for (Tree.Parameter p: pl.getParameters()) {
                if (p == null) {
                    sig.add(null);
                }
                else {
                    Parameter model = p.getParameterModel();
                    if (model!=null) {
                        sig.add(model.getType());
                    }
                    else {
                        sig.add(null);
                    }
                }
            }
            term = pe.getPrimary();
        }
        if (term instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression bme = 
                    (Tree.BaseMemberExpression) 
                        term;
            Unit unit = that.getUnit();
            TypedDeclaration td = 
                    getTypedDeclaration(bme.getScope(), 
                            name(bme.getIdentifier()), 
                            sig, false, unit);
            if (td!=null) {
                that.setDeclaration(td);
                Scope scope = that.getScope();
                Scope container = scope.getContainer();
                Scope realScope = getRealScope(container);
                if (realScope instanceof ClassOrInterface) {
                    ClassOrInterface ci = 
                            (ClassOrInterface) 
                                realScope;
                    Scope tdcontainer = td.getContainer();
                    if (td.isClassOrInterfaceMember()) {
                        ClassOrInterface tdci = 
                                (ClassOrInterface) 
                                    tdcontainer;
                        if (!tdcontainer.equals(realScope) && 
                                ci.inherits(tdci)) {
                            boolean lazy = 
                                    that.getSpecifierExpression() 
                                        instanceof Tree.LazySpecifierExpression;
                            if (!lazy && td.isVariable() 
                                    && td.isJava()) {
                                //allow assignment to variable 
                                //member of Java supertype
                            }
                            // interpret this specification as a 
                            // refinement of an inherited member
                            else if (tdcontainer==scope) {
                                that.addError("parameter declaration hides refining member: '" 
                                        + td.getName(unit) 
                                        + "' (rename parameter)");
                            }
                            else if (td instanceof Value) {
                                refineAttribute((Value) td, 
                                        bme, that, ci);
                            }
                            else if (td instanceof Function) {
                                refineMethod((Function) td, 
                                        bme, that, ci);
                            }
                            else {
                                //TODO!
                                bme.addError("not a reference to a formal attribute: '" + 
                                        td.getName(unit) + "'");
                            }
                        }
                    }
                }
            }
        }
    }

    private void refineAttribute(
            Value assignedAttribute, 
            Tree.BaseMemberExpression bme,
            Tree.SpecifierStatement that, 
            ClassOrInterface c) {
        if (!assignedAttribute.isFormal() && !assignedAttribute.isDefault()
                && !assignedAttribute.isShortcutRefinement()) { //this condition is here to squash a dupe message
            that.addError("inherited attribute may not be assigned in initializer and may not be refined: " + 
                    message(assignedAttribute) + " is declared neither 'formal' nor 'default'", 
                    510);
//            return;
        }
        else if (assignedAttribute.isVariable()) {
            that.addError("inherited attribute may not be assigned in initializer and may not be refined by non-variable: " + 
                    message(assignedAttribute) + " is declared 'variable'");
//            return;
        }
        ClassOrInterface ci = 
                (ClassOrInterface) 
                    assignedAttribute.getContainer();
        String name = assignedAttribute.getName();
        Declaration refined = 
                ci.getRefinedMember(name, null, false);
        Value root = 
                refined instanceof Value ? 
                        (Value) refined : 
                        assignedAttribute;
        Reference rv = getRefinedMemberReference(assignedAttribute, c);
        boolean lazy = 
                that.getSpecifierExpression() 
                    instanceof Tree.LazySpecifierExpression;
        Value attribute = new Value();
        attribute.setName(name);
        attribute.setShared(true);
        attribute.setActual(true);
        attribute.getAnnotations().add(new Annotation("shared"));
        attribute.getAnnotations().add(new Annotation("actual"));
        attribute.setRefinedDeclaration(root);
        Unit unit = that.getUnit();
        attribute.setUnit(unit);
        attribute.setContainer(c);
        attribute.setScope(c);
        attribute.setShortcutRefinement(true);
        attribute.setTransient(lazy);
        Declaration rvd = rv.getDeclaration();
        if (rvd instanceof TypedDeclaration) {
            TypedDeclaration rvtd = 
                    (TypedDeclaration) rvd;
            attribute.setUncheckedNullType(
                    rvtd.hasUncheckedNullType());
        }
        ModelUtil.setVisibleScope(attribute);
        c.addMember(attribute);
        that.setRefinement(true);
        that.setDeclaration(attribute);
        that.setRefined(assignedAttribute);
        unit.addDeclaration(attribute);
        setRefiningType(c, ci, name, null, false, root, 
                attribute, unit, NO_SUBSTITUTIONS);
    }

    private void refineMethod(
            Function assignedMethod, 
            Tree.BaseMemberExpression bme,
            Tree.SpecifierStatement that, 
            ClassOrInterface c) {
        if (!assignedMethod.isFormal() && !assignedMethod.isDefault()
                && !assignedMethod.isShortcutRefinement()) { //this condition is here to squash a dupe message
            bme.addError("inherited method may not be refined: " 
                    + message(assignedMethod) + " is declared neither 'formal' nor 'default'", 
                    510);
//            return;
        }
        ClassOrInterface ci = 
                (ClassOrInterface) 
                    assignedMethod.getContainer();
        String name = assignedMethod.getName();
        List<Type> signature = getSignature(assignedMethod);
        boolean variadic = isVariadic(assignedMethod);
        Declaration refined = 
                ci.getRefinedMember(name,
                        signature, variadic);
        Function root = 
                refined instanceof Function ? 
                        (Function) refined : 
                        assignedMethod;
        Reference rm = getRefinedMemberReference(assignedMethod,c);
        Function method = new Function();
        method.setName(name);
        List<Tree.ParameterList> paramLists;
        List<TypeParameter> typeParams;
        Tree.Term me = that.getBaseMemberExpression();
        if (me instanceof Tree.ParameterizedExpression) {
            Tree.ParameterizedExpression pe = 
                    (Tree.ParameterizedExpression) me;
            paramLists = pe.getParameterLists();
            Tree.TypeParameterList typeParameterList = 
                    pe.getTypeParameterList();
            if (typeParameterList!=null) {
                typeParams = new ArrayList<TypeParameter>();
                for (Tree.TypeParameterDeclaration tpd: 
                    typeParameterList.getTypeParameterDeclarations()) {
                    typeParams.add(tpd.getDeclarationModel());
                }
            }
            else {
                typeParams = null;
            }
        }
        else {
            paramLists = emptyList();
            typeParams = null;
        }
        
        Unit unit = that.getUnit();
        
        final Map<TypeParameter,Type> subs;
        if (typeParams!=null) {
            //the type parameters are written
            //down in the shortcut refinement
            method.setTypeParameters(typeParams);
            //TODO: check 'em!!
            //no need to check them because 
            //this case is actually disallowed
            //elsewhere (specification statements
            //may not have type parameters)
            subs = NO_SUBSTITUTIONS;
        }
        else if (assignedMethod.isParameterized()) {
            if (me instanceof Tree.ParameterizedExpression) {
                //we have parameters, but no type parameters
                bme.addError("refined method is generic: '" 
                        + assignedMethod.getName(unit) 
                        + "' declares type parameters");
                subs = NO_SUBSTITUTIONS;
            }
            else {
                //we're assigning a method reference
                //so we need to magic up some "fake"
                //type parameters
                subs = copyTypeParametersFromRefined(
                        assignedMethod, method, unit);
            }
        }
        else {
            subs = NO_SUBSTITUTIONS;
        }
        
        int i=0;
        for (ParameterList pl: 
                assignedMethod.getParameterLists()) {
            Tree.ParameterList params = 
                    paramLists.size()<=i ? null : 
                        paramLists.get(i++);
            createRefiningParameterList(rm, method, 
                    params, unit, subs, pl);
        }

        method.setShared(true);
        method.setActual(true);
        method.getAnnotations().add(new Annotation("shared"));
        method.getAnnotations().add(new Annotation("actual"));
        method.setRefinedDeclaration(root);
        method.setUnit(unit);
        method.setContainer(c);
        method.setScope(c);
        method.setShortcutRefinement(true);
        method.setDeclaredVoid(assignedMethod.isDeclaredVoid());
        Declaration rmd = rm.getDeclaration();
        if (rmd instanceof TypedDeclaration) {
            TypedDeclaration rmtd = (TypedDeclaration) rmd;
            method.setUncheckedNullType(rmtd.hasUncheckedNullType());
        }
        ModelUtil.setVisibleScope(method);
        c.addMember(method);
        that.setRefinement(true);
        that.setDeclaration(method);
        that.setRefined(root);
        unit.addDeclaration(method);
        Scope scope = that.getScope();
        if (scope instanceof Specification) {
            Specification spec = (Specification) scope;
            spec.setDeclaration(method);
        }
        setRefiningType(c, ci, name, signature, variadic, 
                root, method, unit, subs);
        inheritDefaultedArguments(method);
    }

    private void createRefiningParameterList(
            Reference rm, Function method, 
            Tree.ParameterList params, 
            Unit unit, 
            Map<TypeParameter, Type> subs, 
            ParameterList pl) {
        ParameterList list = new ParameterList();
        int j=0;
        for (Parameter p: pl.getParameters()) {
            //TODO: meaningful errors when parameters don't line up
            //      currently this is handled elsewhere, but we can
            //      probably do it better right here
            createRefiningParameter(rm, method, p, 
                    list, params, j++, subs, unit);
        }
        method.getParameterLists().add(list);
    }

    private void setRefiningType(final ClassOrInterface c, 
            final ClassOrInterface ci, final String name, 
            final List<Type> signature, final boolean variadic, 
            final FunctionOrValue root, FunctionOrValue member, 
            Unit unit, final Map<TypeParameter, Type> subs) {
        member.setType(new LazyType(unit) {
            Type intersection() {
                List<Type> list = new ArrayList<Type>();
//                list.add(rm.getType());
                for (Declaration d: 
                    getInterveningRefinements(name, 
                        signature, variadic, root, c, ci)) {
                    addToIntersection(list,
                            c.getType()
                             .getTypedReference(d, NO_TYPE_ARGS)
                             .getType(),
                            getUnit());
                }
                IntersectionType it = 
                        new IntersectionType(getUnit()); 
                it.setSatisfiedTypes(list);
                return it.canonicalize()
                        .getType()
                        .substitute(subs, null);
            }
            @Override
            public Type initQualifyingType() {
                Type type = intersection();
                return type==null ? null : 
                    type.getQualifyingType();
            }
            @Override
            public Map<TypeParameter, Type> 
            initTypeArguments() {
                Type type = intersection();
                return type==null ? null : 
                    type.getTypeArguments();
            }
            @Override
            public TypeDeclaration initDeclaration() {
                Type type = intersection();
                return type==null ? null : 
                    type.getDeclaration();
            }
            @Override
            public Map<TypeParameter, SiteVariance> getVarianceOverrides() {
                Type type = intersection();
                return type==null ? null : 
                    type.getVarianceOverrides();
            }
        });
    }

    private void createRefiningParameter(
            final Reference rm, Function method, 
            final Parameter p, ParameterList l,
            Tree.ParameterList tpl, int j, 
            final Map<TypeParameter, Type> subs, 
            Unit unit) {
        if (tpl==null || tpl.getParameters().size()<=j) {
            Parameter vp = new Parameter();
            Value v = new Value();
            vp.setModel(v);
            v.setInitializerParameter(vp);
            vp.setSequenced(p.isSequenced());
            vp.setAtLeastOne(p.isAtLeastOne());
//                    vp.setDefaulted(p.isDefaulted());
            vp.setName(p.getName());
            v.setName(p.getName());
            vp.setDeclaration(method);
            v.setContainer(method);
            v.setScope(method);
            l.getParameters().add(vp);
            v.setType(new LazyType(unit) {
                private Type type() {
                    return rm.getTypedParameter(p)
                            .getFullType()
                            .substitute(subs, null);
                }
                @Override
                public Type initQualifyingType() {
                    Type type = type();
                    return type==null ? null :
                        type.getQualifyingType();
                }
                @Override
                public Map<TypeParameter,Type> 
                initTypeArguments() {
                    Type type = type();
                    return type==null ? null :
                        type.getTypeArguments();
                }
                @Override
                public TypeDeclaration initDeclaration() {
                    Type type = type();
                    return type==null ? null :
                        type.getDeclaration();
                }
                @Override
                public Map<TypeParameter, SiteVariance> 
                getVarianceOverrides() {
                    Type type = type();
                    return type==null ? null :
                        type.getVarianceOverrides();
                }
            });
        }
        else {
            Tree.Parameter tp =
                    tpl.getParameters()
                        .get(j);
            Parameter rp = tp.getParameterModel();
            rp.setDefaulted(p.isDefaulted());
            rp.setDeclaration(method);
            l.getParameters().add(rp);
        }
    }

    private static Map<TypeParameter,Type> 
    copyTypeParametersFromRefined(Function refinedMethod, 
            Function method, Unit unit) {
        //we're refining it by assigning a function
        //reference using the = specifier, not =>
        //copy the type parameters of the refined
        //declaration
        List<TypeParameter> typeParameters = 
                refinedMethod.getTypeParameters();
        List<TypeParameter> tps = 
                new ArrayList<TypeParameter>
                    (typeParameters.size());
        Map<TypeParameter, Type> subs = 
                new HashMap<TypeParameter,Type>();
        for (int j=0; j<typeParameters.size(); j++) {
            TypeParameter param = typeParameters.get(j);
            TypeParameter tp = new TypeParameter();
            tp.setName(param.getName());
            tp.setUnit(unit);
            tp.setScope(method);
            tp.setContainer(method);
            tp.setDeclaration(method);
            tp.setCovariant(param.isCovariant());
            tp.setContravariant(param.isContravariant());
            tps.add(tp);
            subs.put(param, tp.getType());
        }
        //we need to substitute these type parameters 
        //into the upper bounds of the type parameters
        //of the refined declaration
        for (int j=0; j<typeParameters.size(); j++) {
            TypeParameter param = typeParameters.get(j);
            TypeParameter tp = tps.get(j);
            List<Type> sts = param.getSatisfiedTypes();
            ArrayList<Type> ssts = 
                    new ArrayList<Type>(sts.size());
            for (Type st: sts) {
                ssts.add(st.substitute(subs, null));
            }
            tp.setSatisfiedTypes(ssts);
            List<Type> cts = param.getCaseTypes();
            if (cts!=null) {
                ArrayList<Type> scts = 
                        new ArrayList<Type>(cts.size());
                for (Type ct: cts) {
                    scts.add(ct.substitute(subs, null));
                }
                tp.setCaseTypes(scts);
            }
        }
        method.setTypeParameters(tps);
        return subs;
    }
    
}
