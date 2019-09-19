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

import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkAssignable;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isExecutableStatement;
import static org.eclipse.ceylon.model.loader.model.AnnotationTarget.ANNOTATION_TYPE;
import static org.eclipse.ceylon.model.loader.model.AnnotationTarget.CONSTRUCTOR;
import static org.eclipse.ceylon.model.loader.model.AnnotationTarget.FIELD;
import static org.eclipse.ceylon.model.loader.model.AnnotationTarget.LOCAL_VARIABLE;
import static org.eclipse.ceylon.model.loader.model.AnnotationTarget.METHOD;
import static org.eclipse.ceylon.model.loader.model.AnnotationTarget.PACKAGE;
import static org.eclipse.ceylon.model.loader.model.AnnotationTarget.PARAMETER;
import static org.eclipse.ceylon.model.loader.model.AnnotationTarget.TYPE;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isAbstraction;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isTypeUnknown;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.loader.model.AnnotationTarget;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeAlias;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.model.Value;

public class AnnotationVisitor extends Visitor {
    
    private static final String DOC_LINK_MODULE = "module ";
    private static final String DOC_LINK_PACKAGE = "package ";
    private static final String DOC_LINK_CLASS = "class ";
    private static final String DOC_LINK_INTERFACE = "interface ";
    private static final String DOC_LINK_FUNCTION = "function ";
    private static final String DOC_LINK_VALUE = "value ";
    private static final String DOC_LINK_ALIAS = "alias ";

    private static boolean isIllegalAnnotationParameterType(Type pt) {
        if (pt!=null) {
            if (pt.isIntersection() || pt.isUnion()) {
                return true;
            }
            TypeDeclaration ptd = pt.getDeclaration();
            Unit unit = ptd.getUnit();
            if (!ptd.isAnnotation() && !isEnum(ptd) 
                    && !pt.isBoolean() 
                    && !pt.isString() 
                    && !pt.isInteger() 
                    && !pt.isFloat() 
                    && !pt.isCharacter() 
                    && !pt.isIterable() 
                    && !pt.isSequential() 
                    && !pt.isSequence() 
                    && !pt.isSubtypeOf(unit.getType(unit.getDeclarationDeclaration()))) {
                return true;
            }
            if (pt.isIterable() || pt.isSequential() || pt.isSequence()) {
                Type elementType = unit.getIteratedType(pt);
                if (isIllegalAnnotationParameterType(elementType)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean isEnum(TypeDeclaration ptd) {
        List<Type> cts = ptd.getCaseTypes();
        if (cts==null) {
            return false;
        }
        else {
            for (Type ct: cts) {
                if (!ct.getDeclaration()
                        .isObjectClass()) {
                    return false;
                }
            }
            return true;
        }
    }
    
    private void checkAnnotationParameter(Functional a, 
            Tree.Parameter pn) {
        Parameter p = pn.getParameterModel();
        if (!(p.getModel() instanceof Value)) {
            pn.addError("annotations may not have callable parameters");
        }
        else {
            Type pt = p.getType();
            if (pt!=null && 
                    isIllegalAnnotationParameterType(pt)) {
                Node errorNode;
                if (pn instanceof Tree.ValueParameterDeclaration) {
                    Tree.ValueParameterDeclaration vpd = 
                            (Tree.ValueParameterDeclaration) pn;
                    errorNode = 
                            vpd.getTypedDeclaration()
                               .getType();
                }
                else {
                    errorNode = pn;
                }
                errorNode.addError("illegal annotation parameter type: '" + 
                        pt.asString() + "'");
            }
            Tree.SpecifierOrInitializerExpression se = null;
            if (pn instanceof Tree.InitializerParameter) {
                Tree.InitializerParameter ip = 
                        (Tree.InitializerParameter) pn;
                se = ip.getSpecifierExpression();
            }
            else if (pn instanceof Tree.ParameterDeclaration) {
                Tree.ParameterDeclaration pd = 
                        (Tree.ParameterDeclaration) pn;
                Tree.TypedDeclaration td = 
                        pd.getTypedDeclaration();
                if (td instanceof Tree.MethodDeclaration) {
                    Tree.MethodDeclaration md = 
                            (Tree.MethodDeclaration) td;
                    se = md.getSpecifierExpression();
                }
                else if (td instanceof Tree.AttributeDeclaration) {
                    Tree.AttributeDeclaration ad = 
                            (Tree.AttributeDeclaration) td;
                    se = ad.getSpecifierOrInitializerExpression();
                }
            }
            if (se!=null) {
                checkAnnotationArgument(a, 
                        se.getExpression());
            }
        }
    }
    
    /** 
     * Checks that the given expression is a valid 
     * annotation argument 
     * (literal, tuple, annotation invocation etc)
     */
    private void checkAnnotationArgument(Functional a, 
            Tree.Expression e) {
        if (e!=null) {
            Tree.Term term = e.getTerm();
            if (term instanceof Tree.Literal) {
                //ok
            }
            else if (term instanceof Tree.NegativeOp && 
                    ((Tree.NegativeOp) term).getTerm() 
                        instanceof Tree.Literal) {
                //ok
            }
            else if (term instanceof Tree.MetaLiteral) {
                //ok
            }
            else if (term instanceof Tree.Tuple) {
                Tree.Tuple tuple = (Tree.Tuple) term;
                Tree.SequencedArgument sa = 
                        tuple.getSequencedArgument();
                if (sa!=null) {
                    for (Tree.PositionalArgument arg: 
                            sa.getPositionalArguments()) {
                        if (arg instanceof Tree.ListedArgument) {
                            Tree.ListedArgument la = 
                                    (Tree.ListedArgument) arg;
                            Tree.Expression expression = 
                                    la.getExpression();
                            if (expression!=null) {
                                checkAnnotationArgument(a, 
                                        expression);
                            }
                        }
                        else {
                            e.addError("illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference");
                        }
                    }
                }
            }
            else if (term instanceof Tree.SequenceEnumeration) {
                Tree.SequenceEnumeration se = 
                        (Tree.SequenceEnumeration) term;
                Tree.SequencedArgument sa = 
                        se.getSequencedArgument();
                if (sa!=null) {
                    for (Tree.PositionalArgument arg: 
                            sa.getPositionalArguments()){
                        if (arg instanceof Tree.ListedArgument) {
                            Tree.ListedArgument la = 
                                    (Tree.ListedArgument) arg;
                            Tree.Expression expression = 
                                    la.getExpression();
                            if (expression!=null) {
                                checkAnnotationArgument(a, 
                                        expression);
                            }
                        }
                        else {
                            e.addError("illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference");
                        }
                    }
                }
            }
            else if (term instanceof Tree.InvocationExpression) {
                checkAnnotationInstantiation(a, e, "illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference");
            }
            else if (term instanceof Tree.BaseMemberExpression) {
                Tree.BaseMemberExpression bme = 
                        (Tree.BaseMemberExpression) term;
                Declaration d = bme.getDeclaration();
                if (a!=null && d!=null && d.isParameter()) {
                    FunctionOrValue mv = (FunctionOrValue) d;
                    Parameter p = 
                            mv.getInitializerParameter();
                    if (!p.getDeclaration().equals(a)) {
                        e.addError("illegal annotation argument: must be a reference to a parameter of the annotation");
                    }
                }
                else if (d instanceof Value &&
                        (((Value) d).isEnumValue() || 
                         ((Value) d).getTypeDeclaration()
                             .isObjectClass())) {
                    //ok
                }
                else if (d != null && d.isStatic()) {
                    // ok
                }
                else {
                    e.addError("illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference");
                }
            }
            else if (term instanceof Tree.QualifiedMemberExpression) {
                Tree.QualifiedMemberExpression qme = 
                        (Tree.QualifiedMemberExpression) term;
                Declaration d = qme.getDeclaration();
                if (d!=null && !d.isStatic()) {
                    e.addError("illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference");
                    
                }
                else {
                    Tree.Primary p = qme.getPrimary();
                    while (!(p instanceof Tree.BaseTypeExpression)) {
                        if (p instanceof Tree.QualifiedTypeExpression) {
                            Tree.QualifiedTypeExpression qte = 
                                    (Tree.QualifiedTypeExpression) p;
                            p = qte.getPrimary();
                        }
                        else {
                            e.addError("illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference");
                            break;
                        }
                    }
                }
            }
            else {
                e.addError("illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference");
            }
        }
    }

    @Override 
    public void visit(Tree.PackageDescriptor that) {
        super.visit(that);
        Unit unit = that.getUnit();
        checkAnnotations(that.getAnnotationList(), 
                unit.getPackageDeclarationType(), null,
                that);
    }
    
    @Override 
    public void visit(Tree.ModuleDescriptor that) {
        super.visit(that);
        Unit unit = that.getUnit();
        checkAnnotations(that.getAnnotationList(), 
                unit.getModuleDeclarationType(), null,
                that);
    }
    
    @Override 
    public void visit(Tree.ImportModule that) {
        super.visit(that);
        Unit unit = that.getUnit();
        checkAnnotations(that.getAnnotationList(), 
                unit.getImportDeclarationType(), null,
                that);
    }
    
    @Override
    public void visit(Tree.AnyClass that) {
        super.visit(that);
        Class c = that.getDeclarationModel();
        if (c.isAnnotation()) {
            checkAnnotationType(that, c);
        }
        
        Unit unit = that.getUnit();
        checkAnnotations(that.getAnnotationList(),
                unit.getClassDeclarationType(c),
                unit.getClassMetatype(c.getType()),
                that);
        checkServiceAnnotations(c, 
                that.getAnnotationList());
    }
    
    @Override
    public void visit(Tree.TypedDeclaration that) {
        super.visit(that);
        if (that.getAnnotationList() != null
                && that.getAnnotationList().getAnnotations() != null) { 
            for (Tree.Annotation a : that.getAnnotationList().getAnnotations()) {
                Tree.Primary p = a.getPrimary();
                if (p instanceof Tree.BaseMemberOrTypeExpression) {
                    Declaration d = ((Tree.BaseMemberOrTypeExpression)p).getDeclaration();
                    if (d != null && "java.lang::native".equals(d.getQualifiedNameString())) {
                        ((FunctionOrValue)that.getDeclarationModel()).setJavaNative(true);
                    }
                }
            }
        }
    }

    @Override 
    public void visit(Tree.AnyInterface that) {
        super.visit(that);
        TypeDeclaration i = that.getDeclarationModel();
        Unit unit = that.getUnit();
        checkAnnotations(that.getAnnotationList(),
                unit.getInterfaceDeclarationType(),
                unit.getInterfaceMetatype(i.getType()),
                that);
    }
    
    @Override 
    public void visit(Tree.Constructor that) {
        super.visit(that);
        Function f = that.getDeclarationModel();
        Unit unit = that.getUnit();
        checkAnnotations(that.getAnnotationList(),
                unit.getCallableConstructorDeclarationType(),
                unit.getConstructorMetatype(f.getTypedReference()),
                that);
    }
    
    @Override 
    public void visit(Tree.Enumerated that) {
        super.visit(that);
        Value v = that.getDeclarationModel();
        Unit unit = that.getUnit();
        //TODO: metamodel types for Enumerated!!
        checkAnnotations(that.getAnnotationList(),
                unit.getValueConstructorDeclarationType(),
                unit.getValueMetatype(v.getTypedReference()),
                that);
    }
    
    @Override
    public void visit(Tree.AnyAttribute that) {
        super.visit(that);
        TypedDeclaration a = that.getDeclarationModel();
        Unit unit = that.getUnit();
        checkAnnotations(that.getAnnotationList(), 
                unit.getValueDeclarationType(),
                unit.getValueMetatype(a.getTypedReference()),
                that);
    }

    @Override
    public void visit(Tree.ObjectDefinition that) {
        super.visit(that);
        TypedDeclaration o = that.getDeclarationModel();
        Unit unit = that.getUnit();
        checkAnnotations(that.getAnnotationList(), 
                unit.getValueDeclarationType(),
                unit.getValueMetatype(o.getTypedReference()),
                that);
    }

    @Override
    public void visit(Tree.AnyMethod that) {
        super.visit(that);
        Function a = that.getDeclarationModel();
        if (a.isAnnotation()) {
            checkAnnotationConstructor(that, a);
        }
        
        TypedDeclaration m = that.getDeclarationModel();
        Unit unit = that.getUnit();
        checkAnnotations(that.getAnnotationList(),
                unit.getFunctionDeclarationType(),
                unit.getFunctionMetatype(m.getTypedReference()),
                that);
    }

    private void checkAnnotationType(Tree.AnyClass that, Class c) {
        if (c.isParameterized()) {
            that.addError("annotation class may not be a parameterized type");
        }
        /*if (c.isAbstract()) {
            that.addError("annotation class may not be abstract");
        }*/
        if (!c.isFinal()) {
            that.addError("annotation class must be final");
        }
        Type et = c.getExtendedType();
        if (et!=null) {
            if (!et.isBasic()) {
                that.addError("annotation class must directly extend 'Basic'");
            }
        }
        if(that.getParameterList()!=null)
        for (Tree.Parameter pn: 
                that.getParameterList().getParameters()) {
            checkAnnotationParameter(c, pn);
        }
        if (that instanceof Tree.ClassDefinition) {
            Tree.ClassDefinition cd = 
                    (Tree.ClassDefinition) that;
            Tree.ClassBody body = cd.getClassBody();
            if (body!=null && 
                    !getExecutableStatements(body)
                        .isEmpty()) {
                that.addError("annotation class body may not contain executable statements");
            }
        }
    }

    private void checkAnnotationConstructor(Tree.AnyMethod that, Function a) {
        Tree.Type type = that.getType();
        if (type!=null) {
            Type t = type.getTypeModel();
            if (t!=null) {
                TypeDeclaration td = t.getDeclaration();
                if (td!=null) {
                    if (td.isAnnotation()) {
                        Unit unit = that.getUnit();
                        TypeDeclaration annotationDec = 
                                unit.getAnnotationDeclaration();
                        if (t.isNothing()) {
                            that.addError("annotation constructor may not return 'Nothing'");
                        }
                        if (!td.inherits(annotationDec)) {
                            that.addError("annotation constructor must return a subtype of 'Annotation'");
                        }
                        if (!unit.getPackage().isLanguagePackage()) {
                            boolean langPackage = 
                                    td.getUnit()
                                      .getPackage()
                                      .isLanguagePackage();
                            String typeName = td.getName();
                            if (langPackage && 
                                    (typeName.equals("Shared") ||
                                    typeName.equals("Abstract") || 
                                    typeName.equals("Default") ||
                                    typeName.equals("Formal") ||
                                    typeName.equals("Actual") ||
                                    typeName.equals("Final") ||
                                    typeName.equals("Variable") ||
                                    typeName.equals("Late") ||
                                    typeName.equals("Native") ||
                                    typeName.equals("Deprecated") ||
                                    typeName.equals("Annotation"))) {
                                type.addError("annotation constructor may not return modifier annotation type");
                            }
                        }
                    } 
                    else {
                        type.addError("annotation constructor must return an annotation type");
                    }
                }
            }
        }
        List<Tree.ParameterList> pls = 
                that.getParameterLists();
        if (pls.size() == 1) {
            for (Tree.Parameter pn: 
                    pls.get(0).getParameters()) {
                checkAnnotationParameter(a, pn);
            }
        }
        else {
            that.addError("annotation constructor must have exactly one parameter list");
        }
        if (that instanceof Tree.MethodDefinition) {
            Tree.MethodDefinition md = 
                    (Tree.MethodDefinition) that;
            Tree.Block block = md.getBlock();
            if (block!=null) {
                List<Tree.Statement> list = 
                        getExecutableStatements(block);
                if (list.size()==1) {
                    Tree.Statement s = list.get(0);
                    if (s instanceof Tree.Return) {
                        Tree.Return r = (Tree.Return) s;
                        Tree.Expression e = 
                                r.getExpression();
                        checkAnnotationInstantiation(a, e, 
                                "annotation constructor must return a newly-instantiated annotation");
                    }
                    else {
                        s.addError("annotation constructor body must return an annotation instance");
                    }
                }
                else {
                    block.addError("annotation constructor body must have exactly one statement");
                }
            }
        }
        else {
            Tree.MethodDeclaration md = 
                    (Tree.MethodDeclaration) that;
            Tree.SpecifierExpression se = 
                    md.getSpecifierExpression();
            if (se!=null) {
                checkAnnotationInstantiation(a, 
                        se.getExpression(), 
                        "annotation constructor must return a newly-instantiated annotation");
            }
        }
    }
    
    private static List<Tree.Statement> 
    getExecutableStatements(Tree.Body block) {
        List<Tree.Statement> list = 
                new ArrayList<Tree.Statement>();
        Unit unit = block.getUnit();
        for (Tree.Statement s: block.getStatements()) {
            if (isExecutableStatement(unit, s)) {
                list.add(s);
            }
        }
        return list;
    }
    
    private void checkAnnotationInstantiation(Functional a, 
            Tree.Expression e, String errorMessage) {
        if (e!=null) {
            Tree.Term term = e.getTerm();
            if (term instanceof Tree.InvocationExpression) {
                Tree.InvocationExpression ie = 
                        (Tree.InvocationExpression) term;
                /*if (!ie.getTypeModel().isExactly(pt)) {
                    ie.addError("annotation constructor must return exactly the annotation type");
                }*/
                Tree.Primary primary = ie.getPrimary();
                if (!(primary instanceof Tree.BaseTypeExpression) && 
                    (!(primary instanceof Tree.BaseMemberExpression)
                            || !((Tree.BaseMemberExpression) primary).getDeclaration().isAnnotation())) {
                    term.addError(errorMessage);
                }
                checkAnnotationArguments(a, ie);
            }
            else {
                term.addError(errorMessage);
            }
        }
    }

    private void checkAnnotationArguments(Functional a, 
            Tree.InvocationExpression ie) {
        Tree.PositionalArgumentList pal = 
                ie.getPositionalArgumentList();
        Tree.NamedArgumentList nal = 
                ie.getNamedArgumentList();
        if (pal!=null) {
            checkPositionalArguments(a, 
                    pal.getPositionalArguments());
        }
        if (nal!=null) {
            checkNamedArguments(a, nal);
            Tree.SequencedArgument sa = 
                    nal.getSequencedArgument();
            if (sa!=null) {
                Parameter p = sa.getParameter();
                if (p!=null) {
                    for (Tree.PositionalArgument pa: sa.getPositionalArguments()) {
                        if (pa!=null) {
                            if (pa instanceof Tree.ListedArgument) {
                                Tree.ListedArgument la = 
                                        (Tree.ListedArgument) pa;
                                checkAnnotationArgument(a, 
                                        la.getExpression());
                            }
                            else {
                                pa.addError("illegal annotation argument");
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkNamedArguments(Functional a, 
            Tree.NamedArgumentList nal) {
        for (Tree.NamedArgument na: nal.getNamedArguments()) {
            if (na!=null) {
                if (na instanceof Tree.SpecifiedArgument) {
                    Tree.SpecifiedArgument sa = 
                            (Tree.SpecifiedArgument) na;
                    Tree.SpecifierExpression se = 
                            sa.getSpecifierExpression();
                    Parameter p = na.getParameter();
                    if (se!=null && p!=null) {
                        checkAnnotationArgument(a, 
                                se.getExpression());
                    }
                }
                else {
                    na.addError("illegal annotation argument");
                }
            }
        }
    }

    private void checkPositionalArguments(Functional a, 
            List<Tree.PositionalArgument> pal) {
        for (Tree.PositionalArgument pa: pal) {
            if (pa!=null) {
                Parameter p = pa.getParameter();
                if (p!=null) {
                    if (pa instanceof Tree.ListedArgument) {
                        Tree.ListedArgument la = 
                                (Tree.ListedArgument) pa;
                        checkAnnotationArgument(a, 
                                la.getExpression());
                    }
                    else if (pa instanceof Tree.SpreadArgument) {
                        Tree.SpreadArgument sa = 
                                (Tree.SpreadArgument) pa;
                        checkAnnotationArgument(a, 
                                sa.getExpression());
                    }
                    else {
                        pa.addError("illegal annotation argument");
                    }
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.DocLink that) {
        super.visit(that);
        String text = that.getText();
        
        int pipeIndex = text.indexOf("|");
        if (pipeIndex != -1) {
            text = text.substring(pipeIndex + 1);
        }
        
        String kind = null;
        if (text.startsWith(DOC_LINK_MODULE)) {
            kind = DOC_LINK_MODULE;
            text = text.substring(DOC_LINK_MODULE.length());
        } else if (text.startsWith(DOC_LINK_PACKAGE)) {
            kind = DOC_LINK_PACKAGE;
            text = text.substring(DOC_LINK_PACKAGE.length());
        } else if (text.startsWith(DOC_LINK_CLASS)) {
            kind = DOC_LINK_CLASS;
            text = text.substring(DOC_LINK_CLASS.length());
        } else if (text.startsWith(DOC_LINK_INTERFACE)) {
            kind = DOC_LINK_INTERFACE;
            text = text.substring(DOC_LINK_INTERFACE.length());
        } else if (text.startsWith(DOC_LINK_FUNCTION)) {
            kind = DOC_LINK_FUNCTION;
            text = text.substring(DOC_LINK_FUNCTION.length());
        } else if (text.startsWith(DOC_LINK_VALUE)) {
            kind = DOC_LINK_VALUE;
            text = text.substring(DOC_LINK_VALUE.length());
        } else if (text.startsWith(DOC_LINK_ALIAS)) {
            kind = DOC_LINK_ALIAS;
            text = text.substring(DOC_LINK_ALIAS.length());
        }
        
        boolean parentheses = false;
        if( text.endsWith("()") ) {
            parentheses = true;
            text = text.substring(0, text.length()-2);
        }
        
        int scopeIndex = text.indexOf("::");
        
        String packageName;
        if (DOC_LINK_MODULE.equals(kind) || 
            DOC_LINK_PACKAGE.equals(kind)) {
            packageName = text;
        } else {
            packageName = scopeIndex < 0 ? 
                    null : text.substring(0, scopeIndex);
        }
                
        String path = scopeIndex < 0 ? 
                text : text.substring(scopeIndex + 2);
        String[] names = path.isEmpty() ? 
                new String[0] : path.split("\\.");
        Declaration base = null;
        if (packageName==null) {
            if (names.length > 0) {
                base = that.getScope()
                        .getMemberOrParameter(that.getUnit(), 
                                names[0], null, false);
            }
        }
        else {
            Package pack = 
                    that.getUnit()
                        .getPackage()
                        .getModule()
                        .getPackage(packageName);
            if (pack == null) {
                if (DOC_LINK_MODULE.equals(kind)) {
                    that.addUsageWarning(Warning.doclink, 
                                "module is missing: '" + 
                                        packageName + "'");
                } else {
                    that.addUsageWarning(Warning.doclink, 
                                "package is missing: '" + 
                                        packageName + "'");
                }
            }
            else {
                that.setPkg(pack);
                if (DOC_LINK_MODULE.equals(kind)) {
                    Package rootPack = 
                            pack.getModule()
                                .getRootPackage();
                    if (pack.equals(rootPack)) {
                        that.setModule(pack.getModule());
                    } else {
                        that.addUsageWarning(Warning.doclink,
                                    "module is missing: '" + 
                                            packageName + "'");
                    }
                }
                if (names.length > 0) {
                    base = pack.getDirectMember(names[0], 
                            null, false);
                }
            }
            
            if (DOC_LINK_MODULE.equals(kind) || 
                DOC_LINK_PACKAGE.equals(kind)) {
                return;
            }
        }
        if (base==null) {
            that.addUsageWarning(Warning.doclink,
                    "declaration is missing: '" + 
                    (names.length > 0 ? names[0] : text) + "'");
        }
        else {
            that.setBase(base);
            if (names.length>1) {
                that.setQualified(new ArrayList<Declaration>(names.length-1));
            }
            for (int i=1; i<names.length; i++) {
                if (base instanceof Value) {
                    Value value = (Value) base;
                    if (!value.isParameter()
                            && !value.isTransient()
                            && value.getTypeDeclaration() != null
                            && value.getTypeDeclaration()
                                    .isAnonymous()) {
                        base = value.getTypeDeclaration();
                    }
                }
                if (base instanceof TypeDeclaration || 
                    base instanceof Functional) {
                    Declaration qualified = 
                            base.getMember(names[i], null, false);
                    if (qualified==null) {
                        that.addUsageWarning(Warning.doclink,
                                    "member declaration or parameter is missing: '" + 
                                            names[i] + "'");
                        break;
                    }
                    else {
                        that.getQualified().add(qualified);
                        base = qualified;
                    }
                }
                else {
                    that.addUsageWarning(Warning.doclink,
                                "not a type or functional declaration: '" + 
                                        base.getName() + "'");
                    break;
                }
            }
        }
        
        if (base != null) {
            if (kind != null && 
                    (names.length == 1 || 
                     names.length == that.getQualified().size() + 1)) {
                if (DOC_LINK_CLASS.equals(kind) && 
                        !(base instanceof Class)) {
                    that.addUsageWarning(Warning.doclink, 
                            "linked declaration is not a class: '" + 
                                    base.getName() + "'");
                }
                else if (DOC_LINK_INTERFACE.equals(kind) && 
                        !(base instanceof Interface)) {
                    that.addUsageWarning(Warning.doclink, 
                            "linked declaration is not an interface: '" + 
                                    base.getName() + "'");
                }
                else if (DOC_LINK_ALIAS.equals(kind) && 
                        !(base instanceof TypeAlias)) {
                    that.addUsageWarning(Warning.doclink, 
                            "linked declaration is not a type alias: '" + 
                                    base.getName() + "'");
                } else if (DOC_LINK_FUNCTION.equals(kind) && 
                        !(base instanceof Function)) {
                    that.addUsageWarning(Warning.doclink, 
                            "linked declaration is not a function: '" + 
                                    base.getName() + "'");
                } else if (DOC_LINK_VALUE.equals(kind) && 
                        !(base instanceof Value)) {
                    that.addUsageWarning(Warning.doclink, 
                            "linked declaration is not a value: '" + 
                                    base.getName() + "'");
                }
            }
            if (parentheses && 
                    !(base instanceof Functional)) {
                that.addUsageWarning(Warning.doclink, 
                        "linked declaration is not a function: '" + 
                                base.getName() + "'");
            }
        }

    }

    @Override 
    public void visit(Tree.Annotation that) {
        super.visit(that);
        Tree.MemberOrTypeExpression primary = 
                (Tree.MemberOrTypeExpression) 
                    that.getPrimary();
        Declaration dec = primary.getDeclaration();
        /*if (dec!=null && !dec.isToplevel()) {
            that.getPrimary().addError("annotation must be a toplevel function reference");
        }*/
        if (dec!=null) {
            if (!dec.isAnnotation()) {
                primary.addError("not an annotation constructor: '" 
                        + dec.getName(that.getUnit()) + "'");
            }
            else {
                String pname = 
                        dec.getUnit()
                            .getPackage()
                            .getNameAsString();
                String name = dec.getName();
                switch (pname) {
                case "java.lang":
                    switch (name) {
                    case "deprecated":
                        that.addError("illegal Java annotation (use 'deprecated' in 'ceylon.language')");
                        break;
                    case "override":
                        that.addError("illegal Java annotation (use 'actual' in 'ceylon.language')");
                        break;
                    } 
                    break;
                case "java.lang.annotation":
                    switch (name) {
                    case "target":
                    case "retention":
                        that.addError("illegal Java annotation");
                    }
                }
                checkAnnotationArguments(null, 
                        (Tree.InvocationExpression) that);
            }
        }
    }

    
    private void checkServiceAnnotations(Class clazz, 
            Tree.AnnotationList annotationList) {
        for (Tree.Annotation ann: 
                annotationList.getAnnotations()) {
            Tree.BaseMemberExpression p = 
                    (Tree.BaseMemberExpression) 
                        ann.getPrimary();
            Declaration pd = p.getDeclaration();
            if (pd != null) {
                Declaration sd = 
                        ann.getUnit()
                            .getLanguageModuleDeclaration("service");
                if (pd.equals(sd)) {
                    checkServiceImplementation(clazz, 
                            getService(ann), ann);
                }
            }
        }
    }

    private static Declaration getService(Tree.Annotation that) {
        
        Tree.PositionalArgumentList pal = 
                that.getPositionalArgumentList();
        if (pal!=null) {
            List<Tree.PositionalArgument> args = 
                    pal.getPositionalArguments();
            if (!args.isEmpty()) {
                Tree.PositionalArgument argument = 
                        args.get(0);
                if (argument instanceof Tree.ListedArgument) {
                    Tree.ListedArgument la = 
                            (Tree.ListedArgument)
                            argument;
                    return getDeclaration(la.getExpression());
                }
            }
        }
        
        Tree.NamedArgumentList nal = 
                that.getNamedArgumentList();
        if (nal!=null) {
            List<Tree.NamedArgument> args = 
                    nal.getNamedArguments();
            if (!args.isEmpty()) {
                Tree.NamedArgument namedArgument = 
                        args.get(0);
                if (namedArgument instanceof Tree.SpecifiedArgument) {
                    Tree.SpecifiedArgument sa = 
                            (Tree.SpecifiedArgument)
                            namedArgument;
                    Tree.SpecifierExpression se = 
                            sa.getSpecifierExpression();
                    if (se!=null) {
                        return getDeclaration(se.getExpression());
                    }
                }
            }
        }
        
        return null;
    }

    private void checkServiceImplementation(Class clazz, 
            Declaration d, Node that) {
        if (d instanceof ClassOrInterface) {
            ClassOrInterface service = (ClassOrInterface) d;
            ParameterList pl = clazz.getParameterList();
            if (pl==null) {
                that.addError("service class must have a parameter list or default constructor");
            }
            else {
                List<Parameter> params = pl.getParameters();
                if (!params.isEmpty() 
                        && !params.get(0).isDefaulted()) {
                    that.addError("service class must be instantiable with an empty argument list");
                }
            }
            if (clazz.inherits(service)) {
                ModelUtil.getModule(clazz)
                    .addService(service, clazz);
            }
            else {
                that.addError("service class does not implement service '" + service + "'");
            }
        }
        else {
            that.addError("service must be an interface or class");
        }
    }

    private static Declaration getDeclaration(Tree.Expression ex) {
        if (ex!=null) {
            Tree.Term term = ex.getTerm();
            if (term instanceof Tree.MetaLiteral) {
                Tree.MetaLiteral lit = 
                        (Tree.MetaLiteral) term;
                return lit.getDeclaration();
            }
        }
        return null;
    }

    private void checkAnnotations(
            Tree.AnnotationList annotationList,
            Type declarationType, 
            Type modelType, Node that) {
        Unit unit = annotationList.getUnit();
        List<Tree.Annotation> annotations = 
                annotationList.getAnnotations();
        for (Tree.Annotation annotation: annotations) {
            Type t = annotation.getTypeModel();
            if (t!=null) {
                TypeDeclaration cad = 
                        unit.getConstrainedAnnotationDeclaration();
                Type cat = t.getSupertype(cad);
                if (cat!=null) {
                    //check *Ceylon* annotation constraints
                    List<Type> args = 
                            cat.getTypeArgumentList();
                    if (args.size()>2) {
                        Type constraint = args.get(2);
                        checkAssignable(declarationType, 
                                constraint, annotation, 
                                "annotated program element does not satisfy annotation constraint");
                    }
                    if (args.size()>3) {
                        Type constraint = args.get(3);
                        if (!constraint.isAnything()) {
                            checkAssignable(modelType, 
                                    constraint, annotation, 
                                    "annotated program element does not satisfy annotation constraint");
                        }
                    }
                }
                EnumSet<AnnotationTarget> target = null;
                Tree.Primary primary = annotation.getPrimary();
                if (primary instanceof Tree.MemberOrTypeExpression) {
                    Declaration ac = ((Tree.MemberOrTypeExpression)primary).getDeclaration();
                    if (ac instanceof TypedDeclaration) {
                        target = ((TypedDeclaration)ac).getAnnotationTargets();
                    }
                }
                if (target!=null) {
                    //check the *Java* annotation constraints
                    boolean ok = false;
                    if (that instanceof Tree.PackageDescriptor) {
                        if (target.contains(PACKAGE)) {
                            ok = true;
                        }
                    }
                    if (that instanceof Tree.InterfaceDefinition) {
                        if (target.contains(TYPE)) {
                            ok = true;
                        }
                    }
                    if (that instanceof Tree.ClassDefinition) {
                        Tree.ClassDefinition c = 
                                (Tree.ClassDefinition) that;
                        boolean initializer = 
                                c.getParameterList()!=null;
                        if (target.contains(TYPE)) {
                            //it always goes on the class,
                            //not on the constructor
                            ok = true;
                        }
                        if (target.contains(CONSTRUCTOR) &&
                                initializer) {
                            //it goes on the constructor
                            ok = true;
                        }
                        if (target.contains(ANNOTATION_TYPE) &&
                                c.getDeclarationModel()
                                 .isAnnotation()) {
                            //it goes on the annotation type
                            ok = true;
                        }
                    }
                    if (that instanceof Tree.ObjectDefinition) {
                        if (target.contains(FIELD)) {
                            ok = true;
                        }
                    }
                    if (that instanceof Tree.Constructor ||
                        that instanceof Tree.Enumerated) {
                        if (target.contains(CONSTRUCTOR)) {
                            ok = true;
                        }
                    }
                    if (that instanceof Tree.MethodDefinition ||
                        that instanceof Tree.MethodDeclaration ||
                        that instanceof Tree.AttributeGetterDefinition ||
                        that instanceof Tree.AttributeSetterDefinition) {
                        if (target.contains(METHOD)) {
                            //it goes on the method, getter,
                            //or setter, unambiguously
                            ok = true;
                        }
                    }
                    if (that instanceof Tree.AttributeDeclaration) {
                        Tree.AttributeDeclaration ad = 
                                (Tree.AttributeDeclaration) that;
                        Value model = ad.getDeclarationModel();
                        boolean parameter = 
                                model.isParameter();
                        boolean classMember = 
                                model.isClassMember();
                        boolean toplevel = 
                                model.isToplevel();
                        boolean local =
                                !toplevel &&
                                !model.isClassOrInterfaceMember();
                        if (target.contains(PARAMETER) &&
                                parameter) {
                            //in this case there is a parameter,
                            //so the annotation *never* goes on
                            //the field, getter, nor setter
                            ok = true;
                        }
                        Tree.SpecifierOrInitializerExpression se = 
                                ad.getSpecifierOrInitializerExpression();
                        if (se instanceof Tree.LazySpecifierExpression 
                                || model.isFormal()) {
                            if (target.contains(METHOD)) {
                                //there is no field, so it
                                //goes on the getter
                                ok = true;
                            }
                        }
                        else {
                            //there is some sort of field or local
                            //in this case only goes on the getter or setter
                            //if it's cannot go on the field
                            if (classMember||toplevel) {
                                if (target.contains(FIELD)) {
                                    ok = true;
                                } else if (target.contains(METHOD)) {
                                    ok = true;
                                }
                            }
                            if (target.contains(LOCAL_VARIABLE) &&
                                    !parameter && local) {
                                ok = true;
                            }
                        }
                    }
                    if (!ok) {
                        StringBuilder message = new StringBuilder();
                        for (AnnotationTarget at: target) {
                            if (message.length()>0) {
                                message.append(", ");
                            }
                            message.append(at);
                        }
                        annotation.addError(
                                "annotated program element does not satisfy annotation constraint: the annotation is declared 'target {"
                                + message + "}'");
                    }
                }
            }
        }
        TypeDeclaration od = 
                unit.getOptionalAnnotationDeclaration();
        for (int i=0; i<annotations.size(); i++) {
            Tree.Annotation ann = annotations.get(i);
            Type t = ann.getTypeModel();
            if (t!=null) {
                TypeDeclaration td = t.getDeclaration();
                // this implicitly excludes Java annotations but they are checked in the backend for duplicates
                if (td.inherits(od)) {
                    for (int j=0; j<i; j++) {
                        Tree.Annotation other = 
                                annotations.get(j);
                        Type ot = 
                                other.getTypeModel();
                        if (ot!=null) {
                            TypeDeclaration otd = 
                                    ot.getDeclaration();
                            if (otd.equals(td)) {
                                ann.addError("duplicate annotation: there are multiple annotations of type '" + 
                                        td.getName() + "'");
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    //Note: this simply doesn't belong here at all, since it has
    //      nothing at all to do with annotations, but it has
    //      to happen after ExpressionVisitor because overloaded
    //      references are only resolved when we get to the 
    //      containing InvocationExpression, and I did not want
    //      to add a whole new Visitor just for overloading errors
    @Override 
    public void visit(Tree.MemberOrTypeExpression that) {
        super.visit(that);
        Declaration dec = that.getDeclaration();
        if (!that.getStaticMethodReferencePrimary() 
                && isAbstraction(dec)) {
            Unit unit = that.getUnit();
            if (that.getStaticMethodReference() 
                    && !dec.isStatic()) {
                that.addError("ambiguous static reference to overloaded method or class: '" +
                        dec.getName(unit) + "' is overloaded");
            }
            else {
                List<Type> sig = that.getSignature();
                if (sig==null) {
                    that.addError("ambiguous callable reference to overloaded method or class: '" +
                            dec.getName(unit) + "' is overloaded");
                }
                else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(" '");
                    for (Type pt: sig) {
                        if (pt!=null) {
                            sb.append(pt.asString(unit));
                        }
                        sb.append(", ");
                    }
                    if (!sig.isEmpty()) {
                        sb.setLength(sb.length()-2);
                    }
                    sb.append("'");
                    that.addError("illegal argument types in invocation of overloaded method or class: " +
                            "there must be exactly one overloaded declaration of '" + 
                            dec.getName(unit) + 
                            "' which accepts the given argument types" + sb);
                }
            }
        }
    }
    
    //Note: this simply doesn't belong here at all, since it has
    //      nothing at all to do with annotations, but it has to
    //      happen after ExpressionVisitor because parameter 
    //      lists are only inferred when we get to the containing 
    //      InvocationExpression, and I did not want to add a  
    //      whole new Visitor just for this
    @Override 
    public void visit(Tree.FunctionArgument that) {
        if (that.getParameterLists().isEmpty()) {
            that.addError("missing parameter list: the parameter list of the anonymous function could not be inferred");
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.TypeConstraint that) {
        Tree.SatisfiedTypes sts = that.getSatisfiedTypes();
        if (sts != null) {
            Unit unit = that.getUnit();
            for (Tree.StaticType t: sts.getTypes()) {
                Type type = t.getTypeModel();
                if (type!=null && unit.isJavaArrayType(type)) {
                    t.addError("type parameter upper bound is a Java array type");
                }
            }
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.MemberLiteral tl) {
        super.visit(tl);
        Tree.Type type = tl.getType();
        if (type!=null) {
            checkJavaArrayElementType(
                    errorNode(type), 
                    tl.getUnit(), 
                    (Type) type.getTypeModel());
        }
    }
    
    @Override
    public void visit(Tree.TypeLiteral tl) {
        super.visit(tl);
        Tree.Type type = tl.getType();
        if (type!=null) {
            checkJavaArrayElementType(
                    errorNode(type), 
                    tl.getUnit(), 
                    (Type) type.getTypeModel());
        }
    }

    private static Node errorNode(Tree.Type type) {
        if (type instanceof Tree.SimpleType) {
            Tree.SimpleType st = 
                    (Tree.SimpleType) type;
            return st.getTypeArgumentList();
        }
        else {
            return type;
        }
    }
    
    @Override
    public void visit(Tree.BaseTypeExpression that) {
        super.visit(that);
        Type type = (Type) that.getTarget();
        if (type!=null) {
            checkJavaArrayElementType(
                    errorNode(that), 
                    that.getUnit(), 
                    type);
        }
    }

    private static Node errorNode(Tree.BaseTypeExpression that) {
        Tree.TypeArguments tas = 
                that.getTypeArguments();
        return tas instanceof Tree.TypeArgumentList ?
                tas : that;
    }

    private void checkJavaArrayElementType(
            Node errNode, Unit unit, Type type) {
        if (unit.isJavaObjectArrayType(type)) {
            Type ta = unit.getJavaArrayElementType(type);
            if (!isTypeUnknown(ta) && 
                    (ta.isNothing() || 
                     ta.isIntersection() || 
                     unit.getDefiniteType(ta)
                         .isUnion() ||
                     ta.isTypeParameter())) {
                errNode.addError(
                        "illegal Java array element type: arrays with element type '" 
                                + ta.asString(unit) + "' may not be instantiated");
            }
        }
    }
    
}
