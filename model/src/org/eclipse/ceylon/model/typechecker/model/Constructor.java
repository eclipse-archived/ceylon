/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.model.typechecker.model;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.eclipse.ceylon.model.typechecker.model.DeclarationFlags.ClassFlags.JAVA_ENUM;
import static org.eclipse.ceylon.model.typechecker.model.DeclarationFlags.ConstructorFlags.ABSTRACT;
import static org.eclipse.ceylon.model.typechecker.model.DeclarationFlags.ConstructorFlags.ABSTRACTION;
import static org.eclipse.ceylon.model.typechecker.model.DeclarationFlags.ConstructorFlags.OVERLOADED;
import static org.eclipse.ceylon.model.typechecker.model.DeclarationKind.CONSTRUCTOR;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A constructor.
 *
 * @author Gavin King
 */
public class Constructor extends TypeDeclaration implements Functional {

    private ParameterList parameterList;
    private List<Declaration> overloads;
    private List<Declaration> members = new ArrayList<Declaration>(3);
    private List<Annotation> annotations = new ArrayList<Annotation>(4);
    
    private String typescriptEnum = null;
    
    @Override
    public boolean isConstructor() {
        return true;
    }
    
    public boolean isJavaEnum() {
        return (flags&JAVA_ENUM)!=0;
    }

    public void setJavaEnum(boolean javaEnum) {
        if (javaEnum) {
            flags|=JAVA_ENUM;
        }
        else {
            flags&=(~JAVA_ENUM);
        }
    }
    
    public boolean isValueConstructor() {
        return parameterList==null;
    }

    @Override
    public boolean isAbstract() {
        return (flags&ABSTRACT)!=0;
    }
    
    public void setAbstract(boolean abstr) {
        if (abstr) {
            flags|=ABSTRACT;
        }
        else {
            flags&=(~ABSTRACT);
        }
    }
    
    public ParameterList getParameterList() {
        return parameterList;
    }
    
    @Override
    public ParameterList getFirstParameterList() {
        return getParameterList();
    }

    @Override
    public List<ParameterList> getParameterLists() {
        ParameterList parameterList = getParameterList();
        if (parameterList==null) {
            return emptyList();
        }
        else {
            return singletonList(parameterList);
        }
    }

    @Override
    public void addParameterList(ParameterList pl) {
        parameterList = pl;
    }
    
    @Override
    public boolean isOverloaded() {
        return (flags&OVERLOADED)!=0;
    }
    
    public void setOverloaded(boolean overloaded) {
        if (overloaded) {
            flags|=OVERLOADED;
        }
        else {
            flags&=(~OVERLOADED);
        }
    }
    
    public void setAbstraction(boolean abstraction) {
        if (abstraction) {
            flags|=ABSTRACTION;
        }
        else {
            flags&=(~ABSTRACTION);
        }
    }
    
    @Override
    public boolean isAbstraction() {
        return (flags&ABSTRACTION)!=0;
    }
    
    @Override
    public List<Declaration> getOverloads() {
        return overloads;
    }
    
    public void setOverloads(List<Declaration> overloads) {
        this.overloads = overloads;
    }
    
    public void initOverloads(Constructor... initial) {
        overloads = 
                new ArrayList<Declaration>
                    (initial.length+1);
        for (Declaration d: initial) {
            overloads.add(d);
        }
    }
    
    public Parameter getParameter(String name) {
        for (Declaration d : getMembers()) {
            if (d.isParameter() && ModelUtil.isNamed(name, d)) {
                return ((FunctionOrValue) d).getInitializerParameter();
            }
        }
        return null;
    }
    
    /**
     * Whether this constructor corresponds to a TypeScript enum.
     *
     * @return
     * <ul>
     *   <li>{@code null} if this constructor does not correspond to a TypeScript enum.</li>
     *   <li>A numerical string if this constructor corresponds to a {@code const} TypeScript enum.</li>
     *   <li>An identifier string if this constructor corresponds to a non-{@code const} TypeScript enum.</li>
     * </ul>
     */
    public String getTypescriptEnum() {
        return typescriptEnum;
    }
    
    /**
     * @see #getTypescriptEnum
     * @throws IllegalStateException if this is not a value constructor.
     * @throws IllegalArgumentException if the argument is neither numeric nor a legal JavaScript identifier.
     */
    public void setTypescriptEnum(String val) {
        if (val != null) {
            if (!isValueConstructor())
                throw new IllegalStateException("Only value constructors can be TypeScript enums");
            if (!val.matches("[0-9.-]+") && !val.matches("[\\p{L}\\{Nl}$_][\\p{L}\\p{Nl}$_\u200C\u200D\\p{Mn}\\p{Mc}\\p{Nd}\\p{Pc}]*")) {
                StringBuilder message = new StringBuilder("Illegal TypeScript enum name or value '");
                message.append(val);
                message.append("'");
                if (isMember()) {
                    message.append(" for member of '");
                    message.append(((ClassOrInterface)getContainer()).getName());
                    message.append("'");
                }
                throw new IllegalArgumentException(message.toString());
            }
        }
        typescriptEnum = val;
    }
    
    @Override
    public boolean isFunctional() {
        return true;
    }

    @Override
    public boolean isDeclaredVoid() {
        return false;
    }

    @Override
    public List<Annotation> getAnnotations() {
        return annotations;
    }
    
    @Override
    public List<Declaration> getMembers() {
        return members;
    }
    
    @Override
    public void addMember(Declaration declaration) {
        members.add(declaration);
    }
    
    @Override
    public boolean isMember() {
        return getContainer() instanceof ClassOrInterface;
    }

    @Override
    public DeclarationKind getDeclarationKind() {
        return CONSTRUCTOR;
    }
    
    protected Declaration getMemberOrParameter(String name, 
            List<Type> signature, boolean ellipsis) {
        return getDirectMember(name, signature, ellipsis);
    }
    
    @Override
    public TypeDeclaration getInheritingDeclaration(
            Declaration member) {
        if (member.getContainer().equals(this)) {
            return null;
        }
        else if (getContainer()!=null) {
            return getContainer()
                    .getInheritingDeclaration(member);
        }
        else {
            return null;
        }
    }
    
    @Override
    void collectSupertypeDeclarations(
            List<TypeDeclaration> results) {
        Type et = getExtendedType();
        if (et!=null) { 
            et.getDeclaration()
                .collectSupertypeDeclarations(results);
        }
    }
    
    @Override
    public boolean inherits(TypeDeclaration dec) {
        Type et = getExtendedType();
        if (et!=null) { 
            return et.getDeclaration().inherits(dec);
        }
        else {
            return false;
        }
    }

    @Override
    protected int hashCodeForCache() {
        int ret = 17;
        Scope container = getContainer();
        if (container instanceof Declaration) {
            ret = (37 * ret) + ((Declaration) container).hashCodeForCache();
        }
        else {
            ret = (37 * ret) + container.hashCode();
        }
        String qualifier = getQualifier();
        ret = (37 * ret) + (qualifier == null ? 0 : qualifier.hashCode());
        ret = (37 * ret) + Objects.hashCode(getName());
        return ret;
    }

    @Override
    protected boolean equalsForCache(Object o) {
        if (o == null || o instanceof Constructor == false) {
            return false;
        }
        Constructor b = (Constructor) o;
        Scope container = getContainer();
        if (container instanceof Declaration) {
            if (!((Declaration) container).equalsForCache(b.getContainer())) {
                return false;
            }
        }
        else {
            if (!container.equals(b.getContainer())) {
                return false;
            }
        }
        if (!Objects.equals(getQualifier(), b.getQualifier())) {
            return false;
        }
        return Objects.equals(getName(), b.getName());
    }
    
    @Override
    public String toString() {
        StringBuilder params = new StringBuilder();
        for (ParameterList pl: getParameterLists()) {
            params.append("(");
            boolean first = true;
            for (Parameter p: pl.getParameters()) {
                if (first) {
                    first = false;
                }
                else {
                    params.append(", ");
                }
                if (p.getType()!=null) {
                    Type type;
                    FunctionOrValue model = p.getModel();
                    if (model.isFunctional()) {
                        type = model.getTypedReference()
                                .getFullType();
                    }
                    else {
                        type = model.getType();
                    }
                    params.append(type.asString())
                          .append(" ");
                }
                params.append(p.getName());
            }
            params.append(")");
        }
        return "new " + toStringName() + params;
    }
    
    @Override
    public boolean isAnonymous() {
        return true;
    }
    
    @Override
    public boolean isFinal() {
        return true;
    }
    
}
