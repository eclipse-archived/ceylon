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
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.NO_SUBSTITUTIONS;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.NO_TYPE_ARGS;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkAssignable;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkAssignableIgnoringNull;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkCallable;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkCasesDisjoint;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkIsExactlyIgnoringNull;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkSupertype;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.correctionMessage;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.declaredInPackage;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getMatchingParameter;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getPackageTypeDeclaration;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getPackageTypedDeclaration;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTupleType;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeArguments;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeDeclaration;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeMember;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeUnknownError;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypedDeclaration;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypedMember;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getUnspecifiedParameter;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.importedModule;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.importedPackage;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.involvesTypeParams;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isIndirectInvocation;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isStatementExpression;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.memberCorrectionMessage;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.notAssignableMessage;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.spreadType;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.typeParametersString;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.unwrapAliasedTypeConstructor;
import static org.eclipse.ceylon.compiler.typechecker.parser.CeylonLexer.LIDENTIFIER;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.eliminateParensAndWidening;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.hasError;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.hasUncheckedNulls;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.isEffectivelyBaseMemberExpression;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.isInstantiationExpression;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.name;
import static org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil.unwrapExpressionUntilTerm;
import static org.eclipse.ceylon.compiler.typechecker.util.NativeUtil.checkNotJvm;
import static org.eclipse.ceylon.compiler.typechecker.util.NativeUtil.declarationScope;
import static org.eclipse.ceylon.compiler.typechecker.util.NativeUtil.getBackends;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.addToIntersection;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.addToUnion;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.appliedType;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.argumentSatisfiesEnumeratedConstraint;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.canonicalIntersection;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.contains;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.findMatchingOverloadedClass;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.genericFunctionType;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getContainingClassOrInterface;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getInterveningRefinements;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getNativeDeclaration;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getNativeHeader;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getOuterClassOrInterface;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.intersectionOfSupertypes;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.intersectionType;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isAbstraction;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isCompletelyVisible;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isConstructor;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isForBackend;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isImplemented;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isNativeForWrongBackend;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isOverloadedVersion;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isTypeUnknown;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.typeArgumentsAsMap;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.typeParametersAsArgList;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.union;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.unionOfCaseTypes;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.unionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.eclipse.ceylon.common.Backend;
import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.compiler.typechecker.tree.CustomTree;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Cancellable;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.ModuleImport;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.Reference;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeAlias;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypeParameter;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypedReference;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.model.UnknownType;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 * Third and final phase of type analysis.
 * Finally visit all expressions and determine their types.
 * Use type inference to assign types to declarations with
 * the local modifier. Finally, assigns types to the 
 * associated model objects of declarations declared using
 * the local modifier.
 * 
 * @author Gavin King
 *
 */
public class ExpressionVisitor extends Visitor {
    
    Cancellable cancellable;
    
    private Tree.Type returnType;
    private Declaration returnDeclaration;
//    private boolean isCondition;
    private boolean dynamic;
    private boolean inExtendsClause = false;
    private TypeDeclaration constructorClass;

    private Node ifStatementOrExpression;
    private Node switchStatementOrExpression;
    
    private Unit unit;
    
    private Tree.IfClause ifClause() {
        if (ifStatementOrExpression 
                instanceof Tree.IfStatement) {
            Tree.IfStatement is = 
                    (Tree.IfStatement) 
                        ifStatementOrExpression;
            return is.getIfClause();
        }
        if (ifStatementOrExpression 
                instanceof Tree.IfExpression) {
            Tree.IfExpression ie = 
                    (Tree.IfExpression) 
                        ifStatementOrExpression;
            return ie.getIfClause();
        }
        return null;
    }
    
    private Tree.SwitchClause switchClause() {
        if (switchStatementOrExpression 
                instanceof Tree.SwitchStatement) {
            Tree.SwitchStatement ss = 
                    (Tree.SwitchStatement) 
                        switchStatementOrExpression;
            return ss.getSwitchClause();
        }
        if (switchStatementOrExpression 
                instanceof Tree.SwitchExpression) {
            Tree.SwitchExpression se = 
                    (Tree.SwitchExpression) 
                        switchStatementOrExpression;
            return se.getSwitchClause();
        }
        return null;
    }
    
    private Tree.SwitchCaseList switchCaseList() {
        if (switchStatementOrExpression 
                instanceof Tree.SwitchStatement) {
            Tree.SwitchStatement ss = 
                    (Tree.SwitchStatement) 
                        switchStatementOrExpression;
            return ss.getSwitchCaseList();
        }
        if (switchStatementOrExpression 
                instanceof Tree.SwitchExpression) {
            Tree.SwitchExpression se = 
                    (Tree.SwitchExpression) 
                        switchStatementOrExpression;
            return se.getSwitchCaseList();
        }
        return null;
    }
    
    public ExpressionVisitor(Cancellable cancellable) {
        this.cancellable = cancellable;
    }
    
    public ExpressionVisitor(Unit unit, Cancellable cancellable) {
        this.unit = unit;
        this.cancellable = cancellable;
    }
    
    @Override public void visit(Tree.CompilationUnit that) {
        unit = that.getUnit();
        super.visit(that);
    }
        
    private Declaration beginReturnDeclaration(Declaration d) {
        Declaration od = returnDeclaration;
        returnDeclaration = d;
        return od;
    }
    
    private void endReturnDeclaration(Declaration od) {
        returnDeclaration = od;
    }
    
    private Tree.Type beginReturnScope(Tree.Type t) {
        Tree.Type ort = returnType;
        returnType = t;
        if (returnType instanceof Tree.FunctionModifier || 
                returnType instanceof Tree.ValueModifier) {
            if (!returnDeclaration.isActual())
                returnType.setTypeModel(unit.getNothingType());
        }
        return ort;
    }
    
    private void endReturnScope(Tree.Type t, TypedDeclaration td) {
        if (returnType instanceof Tree.FunctionModifier || 
                returnType instanceof Tree.ValueModifier) {
            if (!returnDeclaration.isActual())
                td.setType(returnType.getTypeModel());
        }
        returnType = t;
    }
    
    @Override public void visit(Tree.FunctionArgument that) {
        Tree.Expression e = that.getExpression();
        Function fun = that.getDeclarationModel();
        Tree.Type type = that.getType();
        if (e==null) {
            Tree.Type ret = 
                    type instanceof Tree.VoidModifier 
                        || !fun.isDeclaredVoid() ? 
                            type : 
                            new Tree.VoidModifier(null);
            Declaration od = beginReturnDeclaration(fun);
            Tree.Type rt = beginReturnScope(ret);
            super.visit(that);
            endReturnScope(rt, fun);
            endReturnDeclaration(od);
        }
        else {
            super.visit(that);
            Type returnType = 
                    unit.denotableType(e.getTypeModel());
            fun.setType(returnType);
            //if (that.getType() instanceof Tree.FunctionModifier) {
            type.setTypeModel(returnType);
            /*}
            else {
                checkAssignable(t, that.getType().getTypeModel(), e, 
                        "expression type must be assignable to specified return type");
            }*/
            if (type instanceof Tree.VoidModifier 
                    && !isStatementExpression(e)) {
                e.addError("anonymous function is declared void so specified expression must be a statement");
            }
        }
        if (fun.isDeclaredVoid()) {
            fun.setType(unit.getAnythingType());            
        }
        that.setTypeModel(funType(that, fun));
        Tree.TypeParameterList tpl = 
                that.getTypeParameterList();
        if (tpl!=null) {
            checkNotJvm(tpl, 
                    "type functions are not supported on the JVM: anonymous function is generic (remove type parameters)");
        }
    }

    private Type funType(Tree.FunctionArgument that, Function fun) {
        //if the anonymous function has type parameters,
        //then the type of the expression is a type 
        //constructor, so create a fake type alias
        TypedReference reference = fun.getTypedReference();
        if (fun.isParameterized()) {
            Scope scope = that.getScope();
            return genericFunctionType(fun, 
                    scope, fun, reference, unit);
        }
        else {
            return reference.getFullType();
        }
    }
    
    @Override public void visit(Tree.IfExpression that) {
        Node ose = switchStatementOrExpression;
        Node oie = ifStatementOrExpression;
        switchStatementOrExpression = null;
        ifStatementOrExpression = that;
        super.visit(that);
        
        List<Type> list = new ArrayList<Type>();
        Tree.IfClause ifClause = that.getIfClause();
        if (ifClause!=null && 
                ifClause.getExpression()!=null) {
            Type t = 
                    ifClause.getExpression()
                        .getTypeModel();
            if (t!=null) {
                addToUnion(list, t);
            }
        }
        else {
            that.addError("missing then expression");
        }
        
        Tree.ElseClause elseClause = that.getElseClause();
        if (elseClause!=null && 
                elseClause.getExpression()!=null) {
            Type t = 
                    elseClause.getExpression()
                        .getTypeModel();
            if (t!=null) {
                addToUnion(list, t);
            }
        }
        else {
            that.addError("missing else expression");
        }
        
        that.setTypeModel(union(list, unit));
        
        switchStatementOrExpression = ose;        
        ifStatementOrExpression = oie;
    }
    
    @Override
    public void visit(Tree.Switched that) {
        Tree.Variable variable = that.getVariable();
        if (variable!=null && 
                variable.getSpecifierExpression()==null) {
            that.addError("missing specified expression");
        }
        super.visit(that);
    }
    
    @Override public void visit(Tree.SwitchExpression that) {
        Node ose = switchStatementOrExpression;
        Node oie = ifStatementOrExpression;
        switchStatementOrExpression = that;
        ifStatementOrExpression = null;
        
        super.visit(that);

        Tree.SwitchCaseList switchCaseList = 
                that.getSwitchCaseList();
        Tree.SwitchClause switchClause = 
                that.getSwitchClause();
        checkSwitch(switchClause, switchCaseList);
        
        if (switchCaseList!=null) {
            List<Type> list = new ArrayList<Type>();
            for (Tree.CaseClause cc: 
                    that.getSwitchCaseList()
                        .getCaseClauses()) {
                Tree.Expression e = cc.getExpression();
                if (e!=null) {
                    Type t = e.getTypeModel();
                    if (t!=null) {
                        addToUnion(list, t);
                    }
                }
            }
            Tree.ElseClause elseClause = 
                    that.getSwitchCaseList()
                        .getElseClause();
            if (elseClause!=null) {
                Tree.Expression e = 
                        elseClause.getExpression();
                if (e!=null) {
                    Type t = e.getTypeModel();
                    if (t!=null) {
                        addToUnion(list, t);
                    }
                }
            }
            that.setTypeModel(union(list, unit));
        }
        switchStatementOrExpression = ose;        
        ifStatementOrExpression = oie;
    }
    
    @Override public void visit(Tree.ExpressionComprehensionClause that) {
        super.visit(that);
        that.setTypeModel(that.getExpression().getTypeModel());
        that.setFirstTypeModel(unit.getNothingType());
    }
    
    @Override public void visit(Tree.ForComprehensionClause that) {
        super.visit(that);
        that.setPossiblyEmpty(true);
        Tree.ComprehensionClause cc = 
                that.getComprehensionClause();
        if (cc!=null) {
            that.setTypeModel(cc.getTypeModel());
            Tree.ForIterator fi = that.getForIterator();
            if (fi!=null) {
                Tree.SpecifierExpression se = 
                        fi.getSpecifierExpression();
                if (se!=null) {
                    Tree.Expression e = se.getExpression();
                    if (e!=null) {
                        Type it = e.getTypeModel();
                        if (it!=null) {
                            checkIterable(it, e);
                            that.setPossiblyEmpty(
                                    cc.getPossiblyEmpty() || 
                                    !unit.isNonemptyIterableType(it));
                            Type absentType = unit.getAbsentType(it);
                            if (absentType==null) {
                                absentType = unit.getNullType();
                            }
                            Type firstType = 
                                    unionType(absentType, 
                                        cc.getFirstTypeModel(), 
                                        unit);
                            that.setFirstTypeModel(firstType);
                        }
                    }
                }
            }
        }
    }
    
    @Override public void visit(Tree.IfComprehensionClause that) {
        super.visit(that);
        that.setPossiblyEmpty(true);
        that.setFirstTypeModel(unit.getNullType());
        Tree.ComprehensionClause cc = 
                that.getComprehensionClause();
        if (cc!=null) {
            that.setTypeModel(cc.getTypeModel());
        }
    }
    
    @Override public void visit(Tree.Destructure that) {
        super.visit(that);
        Tree.Pattern pattern = that.getPattern();
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        if (se != null) {
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                inPatternCase = that.getPatternCase();
                destructure(pattern, e.getTypeModel());
                inPatternCase = false;
            }
        }
    }
    
    private void destructure(Tree.Pattern pattern, Type type) {
        if (type!=null) {
            type = type.resolveAliases();
            if (pattern instanceof Tree.TuplePattern) {
                destructure(type, 
                        (Tree.TuplePattern) 
                            pattern);
            }
            else if (pattern instanceof Tree.KeyValuePattern) {
                destructure(type, 
                        (Tree.KeyValuePattern) 
                            pattern);
            }
            else {
                Tree.VariablePattern vp = 
                        (Tree.VariablePattern) 
                            pattern;
                Tree.Variable var = vp.getVariable();
                Tree.Type varType = var.getType();
                if (varType!=null) {
                    if (varType instanceof Tree.SequencedType) {
                        inferSequencedValueType(type, var);
                    }
                    else {
                        inferValueType(var, type);
                    }
                    Type declaredType = varType.getTypeModel();
                    checkAssignable(type, declaredType, var, 
                            "type of element of assigned value must be a subtype of declared type of pattern variable");
                }
            }
        }
    }

    private void inferSequencedValueType(Type type, Tree.Variable var) {
        Tree.SequencedType st = 
                (Tree.SequencedType) 
                    var.getType();
        if (st.getType() instanceof Tree.ValueModifier) {
            if (type!=null) {
                Type set = unit.getSequentialElementType(type);
                st.getType().setTypeModel(set);
                setSequencedValueType(st, type, var);
            }
        }
    }
    
    private boolean inPatternCase;
    
    private void destructure(Type entryType,
            Tree.KeyValuePattern keyValuePattern) {
        Tree.Pattern key = keyValuePattern.getKey();
        Tree.Pattern value = keyValuePattern.getValue();
        if (entryType.isExactlyNothing()) {
            if (!inPatternCase) {
                keyValuePattern
                    .addError("assigned expression has bottom type 'Nothing', so may not be destructured");
            }
        }
        else if (!unit.isEntryType(entryType)) {
            if (!inPatternCase) {
                keyValuePattern
                    .addError(
                        "assigned expression is not an entry type, so may not be destructured: '" + 
                        entryType.asString(unit) + 
                        "' is not an entry type");
            }
        }
        else {
            destructure(key, unit.getKeyType(entryType));
            destructure(value, unit.getValueType(entryType));
        }
    }

    private void destructure(Type sequenceType, 
            Tree.TuplePattern tuplePattern) {
        List<Tree.Pattern> patterns = 
                tuplePattern.getPatterns();
        int length = patterns.size();
        if (length==0) {
            tuplePattern
                .addError("tuple pattern must have at least one variable");
        }
        else if (sequenceType.isExactlyNothing()) {
            if (!inPatternCase) {
                tuplePattern
                    .addError("assigned expression has bottom type 'Nothing', so may not be destructured");
            }
        }
        else {
            for (int i=0; i<length-1; i++) {
                Tree.Pattern pattern = patterns.get(i);
                if (pattern instanceof Tree.VariablePattern) {
                    Tree.VariablePattern vp = 
                            (Tree.VariablePattern) 
                                pattern;
                    Tree.Type t = 
                            vp.getVariable()
                                .getType();
                    if (t instanceof Tree.SequencedType) {
                        t.addError("variadic pattern element must occur as last element of tuple pattern");
                    }
                }
            }
            if (!unit.isSequentialType(sequenceType)) {
                if (!inPatternCase) {
                    tuplePattern
                        .addError(
                            "assigned expression is not a sequence type, so may not be destructured: '" + 
                            sequenceType.asString(unit) + 
                            "' is not a subtype of 'Sequential'");
                }
            }
            else if (unit.isEmptyType(sequenceType)) {
                tuplePattern
                    .addError(
                        "assigned expression is an empty sequence type, so may not be destructured: '" + 
                        sequenceType.asString(unit) + 
                        "' is a subtype of 'Empty'");
            }
            else if (unit.isTupleType(sequenceType)) {
                destructureTuple(sequenceType, tuplePattern);
            }
            else {
                destructureSequence(sequenceType, tuplePattern);
            }
        }
    }

    private boolean isVariadicPattern(Tree.Pattern lastPattern) {
        if (lastPattern instanceof Tree.VariablePattern) {
            Tree.VariablePattern variablePattern = 
                    (Tree.VariablePattern) 
                        lastPattern;
            Tree.Type type = 
                    variablePattern.getVariable()
                        .getType();
            if (type instanceof Tree.SequencedType) {
                return true;
            }
        }
        return false;
    }

    private void destructureTuple(Type sequenceType, 
            Tree.TuplePattern tuplePattern) {
        List<Tree.Pattern> patterns = 
                tuplePattern.getPatterns();
        int length = patterns.size();
        Tree.Pattern lastPattern = patterns.get(length-1);
        boolean variadic = isVariadicPattern(lastPattern);
        List<Type> types = 
                unit.getTupleElementTypes(sequenceType);
        boolean tupleLengthUnbounded = 
                unit.isTupleLengthUnbounded(sequenceType);
//                boolean tupleVariantAtLeastOne = 
//                        unit.isTupleVariantAtLeastOne(sequenceType);
        int minimumLength = 
                unit.getTupleMinimumLength(sequenceType);
        if (!variadic && types.size()>length) {
            tuplePattern
                .addError("assigned tuple has too many elements");
        }
        if (!variadic && tupleLengthUnbounded) {
            tuplePattern
                .addError("assigned tuple has unbounded length");
        }
        if (!variadic && minimumLength<types.size()) {
            tuplePattern
                .addError("assigned tuple has variadic length");
        }
        int fixedLength = variadic ? length-1 : length;
        for (int i=0; i<types.size() && i<fixedLength; i++) {
            Type type = types.get(i);
            Tree.Pattern pattern = patterns.get(i);
            destructure(pattern, type);
        }
        if (variadic) {
            Type tail = 
                    unit.getTailType(sequenceType, 
                            fixedLength);
            destructure(lastPattern, tail);
        }
        for (int i=types.size(); i<length; i++) {
            Tree.Pattern pattern = patterns.get(i);
            Node errNode;
            if (pattern instanceof Tree.VariablePattern) {
                Tree.VariablePattern vp = 
                        (Tree.VariablePattern) pattern;
                errNode = vp.getVariable();
            }
            else {
                errNode = pattern;
            }
            errNode.addError("assigned tuple has too few elements");
        }
    }

    private void destructureSequence(Type sequenceType, 
            Tree.TuplePattern tuplePattern) {
        List<Tree.Pattern> patterns = 
                tuplePattern.getPatterns();
        int length = patterns.size();
        Tree.Pattern lastPattern = patterns.get(length-1);
        if (!isVariadicPattern(lastPattern)) {
            Tree.Pattern pattern = lastPattern;
            if (pattern==null) pattern = tuplePattern;
            pattern.addError(
                    "assigned expression is not a tuple type, so pattern must end in a variadic element: '" + 
                    sequenceType.asString(unit) + 
                    "' is not a tuple type");
        }
        else if (/*nonempty && length>1 ||*/ length>2) {
            Tree.Pattern pattern = patterns.get(2);
            if (pattern==null) pattern = tuplePattern;
            pattern.addError(
                    "assigned expression is not a tuple type, so pattern must not have more than two elements: '" + 
                    sequenceType.asString(unit) + 
                    "' is not a tuple type");
        }
        else if ((/*nonempty ||*/ length>1) && 
                !unit.isSequenceType(sequenceType)) {
            Tree.Pattern pattern = patterns.get(1);
            if (pattern==null) pattern = tuplePattern;
            pattern.addError(
                    "assigned expression is not a nonempty sequence type, so pattern must have exactly one element: '" + 
                    sequenceType.asString(unit) + 
                    "' is not a subtype of 'Sequence'");
        }
        
        if (length>1) {
            Type elementType = 
                    unit.getSequentialElementType(sequenceType);
            destructure(patterns.get(0), elementType);
            destructure(lastPattern, 
                    unit.getSequentialType(elementType));
        }
        else {
            destructure(lastPattern, sequenceType);
        }
    }
    
    @Override public void visit(Tree.Variable that) {
        super.visit(that);
        if (that instanceof CustomTree.GuardedVariable) {
            CustomTree.GuardedVariable gv = 
                    (CustomTree.GuardedVariable) 
                        that;
            setTypeForGuardedVariable(that, 
                    gv.getConditionList(), 
                    gv.isReversed());
            Tree.BaseMemberExpression bme =
                    (Tree.BaseMemberExpression)
                        that.getSpecifierExpression()
                            .getExpression()
                            .getTerm();
            Declaration original = bme.getDeclaration();
            if (original instanceof TypedDeclaration) {
                that.getDeclarationModel()
                    .setOriginalDeclaration(
                            (TypedDeclaration) original);
            }
        }
        else {
            Tree.SpecifierExpression se = 
                    that.getSpecifierExpression();
            if (se!=null) {
                inferType(that, se);
                Tree.Type type = that.getType();
                if (type!=null) {
                    Type t = type.getTypeModel();
                    if (!isTypeUnknown(t)) {
                        checkType(t, se);
                    }
                }
            }
        }
    }
    
    @Override public void visit(Tree.ConditionList that) {
        if (that.getConditions().isEmpty()) {
            that.addError("empty condition list");
        }
        super.visit(that);
    }

    @Override public void visit(Tree.ResourceList that) {
        if (that.getResources().isEmpty()) {
            that.addError("empty resource list");
        }
        super.visit(that);
    }

    private void initOriginalDeclaration(Tree.Variable that) {
        if (that.getType() 
                instanceof Tree.SyntheticVariable) {
            TypedDeclaration td = 
                    (TypedDeclaration) 
                        that.getDeclarationModel();
            Tree.BaseMemberExpression bme = 
                    (Tree.BaseMemberExpression) that
                        .getSpecifierExpression()
                        .getExpression()
                        .getTerm();
            TypedDeclaration d = 
                    (TypedDeclaration) 
                        bme.getDeclaration();
            td.setOriginalDeclaration(d);
        }
    }
    
    @Override public void visit(Tree.IsCondition that) {
        //don't recurse to the Variable, since we don't
        //want to check that the specifier expression is
        //assignable to the declared variable type
        //(nor is it possible to infer the variable type)
        Tree.Type t = that.getType();
        if (t!=null) {
            t.visit(this);
        }
        Tree.Variable v = that.getVariable();
        Type type = 
                t==null ? null : 
                    t.getTypeModel();
        if (v!=null) {
            Tree.SpecifierExpression se = 
                    v.getSpecifierExpression();
            Type knownType;
            if (se==null) {
                knownType = null;
            }
            else {
                se.visit(this);
                checkReferenceIsNonVariable(v, 
                        se.getExpression());
                initOriginalDeclaration(v);
                //this is a bit ugly (the parser sends us a SyntheticVariable
                //instead of the real StaticType which it very well knows!)
                Tree.Expression e = se.getExpression();
                knownType = 
                        e==null ? null : 
                            e.getTypeModel();
                //TODO: what to do here in case of !is
                if (knownType!=null 
                        && !isTypeUnknown(knownType)) { //TODO: remove this if we make unknown a subtype of Anything
                    if (!isTypeUnknown(type)) {
                        checkReified(t, type, knownType, 
                                that.getAssertion());
                    }
                    Type checkType;
                    if (hasUncheckedNulls(e)) {
                        checkType = unit.getOptionalType(knownType);
                        // if the expression has unchecked nulls,
                        // widen the known type to an optional
                        // type, which allows idioms like an 
                        // assert that simultaneously narrows
                        // and widens to optional
                        if (unit.getNullType()
                                .isSubtypeOf(type)) {
                            knownType = checkType;
                        }
                    }
                    else {
                        checkType = knownType;
                    }
                    if (that.getNot()) {
                        if (intersectionType(type, checkType, unit)
                                .isNothing()) {
                            that.addUsageWarning(Warning.redundantNarrowing,
                                    "condition does not narrow type: intersection of '" + 
                                    type.asString(unit) + 
                                    "' and '" + 
                                    knownType.asString(unit) + 
                                    "' is empty" + 
                                    " (expression is already of the specified type)");
                        }
                        else if (checkType.isSubtypeOf(type)) {
                            that.addUsageWarning(Warning.redundantNarrowing,
                                    "condition tests assignability to bottom type 'Nothing': '" + 
                                    knownType.asString(unit) + 
                                    "' is a subtype of '" + 
                                    type.asString(unit) + "'");
                        }
                    } 
                    else {
                        if (checkType.isSubtypeOf(type)) {
                            that.addUsageWarning(Warning.redundantNarrowing,
                                    "condition does not narrow type: '" + 
                                    knownType.asString(unit) + 
                                    "' is a subtype of '" + 
                                    type.asString(unit) + "'" + 
                                    " (expression is already of the specified type)");
                        }
                    }
                }
            }
            defaultTypeToAnything(v);
            if (knownType==null) {
                knownType = unit.getAnythingType(); //or should we use unknown?
            }
            
            Type it = narrow(type, knownType, that.getNot());
            //check for disjointness
            if (it.isNothing()) {
                if (that.getNot()) {
                    /*that.addError("tests assignability to Nothing type: " +
                            knownType.asString(unit) + " is a subtype of " + 
                            type.asString(unit));*/
                }
                else {
                    that.addUsageWarning(Warning.redundantNarrowing,
                            "condition tests assignability to bottom type 'Nothing': intersection of '" +
                            knownType.asString(unit) + 
                            "' and '" + 
                            type.asString(unit) + 
                            "' is empty");
                }
            }
            //do this *after* checking for disjointness!
            knownType = unit.denotableType(knownType);
            //now recompute the narrowed type!
            it = narrow(type, knownType, that.getNot());
            
            v.getType()
             .setTypeModel(it);
            v.getDeclarationModel()
             .setType(it);
            if (!canHaveUncheckedNulls(it)) {
                v.getDeclarationModel()
                 .setUncheckedNullType(false);
            }
        }
    }

    private void checkReified(Tree.Type t, Type type, Type knownType, boolean assertion) {
        type = type.resolveAliases();
        if (!type.isReified()) {
            t.addError("type is not reified: '" + 
                    type.asString(unit) + 
                    "' involves a type parameter declared in Java");
        }
        else if (getBackends(t).supports(Backend.Java) && 
                type.hasUnreifiedInstances(knownType)) {
            if (assertion) {
                t.addUsageWarning(Warning.uncheckedTypeArguments,
                        "type condition might not be fully checked at runtime: '" + 
                        type.asString(unit) + 
                        "' involves a type argument that is unchecked for Java class instances",
                        Backend.Java);
            }
            else {
                t.addUnsupportedError("type condition cannot be fully checked at runtime: '" + 
                        type.asString(unit) + 
                        "' involves a type argument that is unchecked for Java class instances",
                        Backend.Java);
            }
        }
    }

    private Type narrow(Type type,
            Type knownType, boolean not) {
        if (not) {
            //a !is condition, narrow to complement
            return /*unit.denotableType(*/knownType.minus(type);
        }
        else {
            //narrow to the intersection of the outer type 
            //and the type specified in the condition
            return intersectionType(type, knownType, unit);
        }
    }
    
    @Override public void visit(Tree.SatisfiesCondition that) {
        super.visit(that);
        that.addUnsupportedError("satisfies conditions not yet supported");
    }
    
    @Override public void visit(Tree.ExistsOrNonemptyCondition that) {
        //don't recurse to the Variable, since we don't
        //want to check that the specifier expression is
        //assignable to the declared variable type
        //(nor is it possible to infer the variable type)
        Type t = null;
        Tree.Term term = null;
        Tree.Statement s = that.getVariable();
        if (s instanceof Tree.Variable) {
            Tree.Variable v = (Tree.Variable) s;
            //v.getType().visit(this);
            defaultTypeToAnything(v);
            Tree.SpecifierExpression se = 
                    v.getSpecifierExpression();
            if (se==null) {
                v.addError("missing specifier");
            }
            else {
                Tree.Expression e = se.getExpression();
                if (e!=null) {
                    se.visit(this);
                    boolean not = that.getNot();
                    if (that instanceof Tree.ExistsCondition) {
                        inferDefiniteType(v, se, not);
                        checkOptionalType(v, se, not);
                    }
                    else if (that instanceof Tree.NonemptyCondition) {
                        inferNonemptyType(v, se, not);
                        checkEmptyOptionalType(v, se, not);
                    }
                    t = e.getTypeModel();
                    checkReferenceIsNonVariable(v, 
                            se.getExpression());
                    initOriginalDeclaration(v);
                    term = e.getTerm();
                }
            }
        }
        else if (s instanceof Tree.Destructure) {
            Tree.Destructure d = (Tree.Destructure) s;
            if (that.getNot()) {
                that.addError("negated conditions do not support destructuring");
            }
            Tree.SpecifierExpression se = 
                    d.getSpecifierExpression();
            if (se!=null) {
                Tree.Expression e = se.getExpression();
                if (e!=null) {
                    se.visit(this);
                    t = e.getTypeModel();
                    if (!isTypeUnknown(t)) {
                        Type type = null;
                        if (that instanceof Tree.ExistsCondition) {
                            type = unit.getDefiniteType(t);
                        }
                        else if (that instanceof Tree.NonemptyCondition) {
                            type = unit.getNonemptyDefiniteType(t);
                        }
                        if (!isTypeUnknown(type) && 
                                !type.isNothing()) {
                            destructure(d.getPattern(), type);
                        }
                        /*else {
                            d.getPattern().addError("cannot be destructured: '" + 
                                    type.asString(unit) + "'");
                        }*/
                    }
                    term = e.getTerm();
                }
            }
        }
        if (that instanceof Tree.ExistsCondition) {
            checkOptional(t, term, that);
        }
        else if (that instanceof Tree.NonemptyCondition) {
            checkPossiblyEmpty(t, term, that);
        }
    }

    private void defaultTypeToAnything(Tree.Variable v) {
        /*if (v.getType().getTypeModel()==null) {
            v.getType().setTypeModel( getAnythingDeclaration().getType() );
        }*/
        v.getType().visit(this);
        Value dec = v.getDeclarationModel();
        if (dec.getType()==null) {
            dec.setType(defaultType());
        }
    }

    private void checkReferenceIsNonVariable(Tree.Variable v,
            Tree.Expression se) {
        if (v.getType() instanceof Tree.SyntheticVariable) {
            Tree.BaseMemberExpression term = 
                    (Tree.BaseMemberExpression) 
                        se.getTerm();
            checkReferenceIsNonVariable(term, false, true);
        }
    }
    
    private void checkReferenceIsNonVariable(Tree.BaseMemberExpression ref,
            boolean isSwitch, boolean narrowing) {
        Declaration d = ref.getDeclaration();
        if (d!=null) {
            int code = isSwitch ? 3101:3100;
            String help = " (assign to a new local value to narrow type)";
            String extra = narrowing ? "" : " will not be narrowed";
            
            String message;
            if (!(d instanceof Value)) {
                message = "referenced declaration is not a value: '" + 
                        d.getName(unit) + "'";
            }
            else if (isNonConstant(d)) {
                message = "referenced value is non-constant: '" + 
                        d.getName(unit) + "'" + extra + help;
            }
            else if (d.isDefault() || d.isFormal()) {
                message = "referenced value may be refined by a non-constant value: '" + 
                        d.getName(unit) + "'" + extra + help;
            }
            else {
                return;
            }
            
            if (narrowing) {
                ref.addError(message, code);
            }
            else {
                ref.addUsageWarning(Warning.syntaxDeprecation, 
                        message);
            }
        }
    }

    private boolean isNonConstant(Declaration d) {
        if (d instanceof Value) {
            Value v = (Value) d;
            return v.isVariable() 
                || v.isTransient();
        }
        else {
            return false;
        }
    }
    
    private void checkPossiblyEmpty(Type type, Tree.Term term, Node that) {
        if (!isTypeUnknown(type) 
                && !unit.isPossiblyEmptyType(type)) {
            String message = 
                    "expression type is not a possibly-empty sequential type: '" 
                            + type.asString(unit) + "' ";
            if (!unit.isSequentialType(unit.getDefiniteType(type))) {
                that.addError(message + "' is not a subtype of 'Anything[]?'");
            }
            else {
                if (type.isSubtypeOf(unit.getOptionalType(
                        unit.getSequenceType(unit.getAnythingType())))) {
                    message += " cannot be empty";
                }
                else if (type.isSubtypeOf(unit.getEmptyType())) {
                    message += " is always empty";
                }
                else if (type.isSubtypeOf(unit.getNullType())) {
                    message += " is always null";
                }
                else if (type.isSubtypeOf(unit.getOptionalType(unit.getEmptyType()))) {
                    message += " is always empty or null";
                }
                that.addUsageWarning(Warning.redundantNarrowing, message);
            }
        }
    }
    
    private void checkOptional(Type type, Tree.Term term, Node that) {
        if (!isTypeUnknown(type) && 
                !unit.isOptionalType(type) && 
                !hasUncheckedNulls(term)) {
            String message = 
                    "expression type is not optional: '" 
                            + type.asString(unit) + "'";
            if (type.isSubtypeOf(unit.getObjectType())) {
                message += " cannot be null";
            }
            else if (type.isSubtypeOf(unit.getNullType())) {
                message += " is always null";
            }
            that.addUsageWarning(Warning.redundantNarrowing, message);
        }
    }

    private void checkIterable(Type pt, Tree.Primary p) {
        if (!unit.isContainerType(pt)) {
            p.addError("expression is not iterable: '" +
                    pt.asString(unit) + 
                    "' is not a subtype of 'Iterable'");
        }
    }
    
    @Override public void visit(Tree.BooleanCondition that) {
        super.visit(that);
        if (that.getExpression()!=null) {
            Type type = 
                    that.getExpression()
                        .getTypeModel();
            if (!isTypeUnknown(type)) {
                checkAssignable(type, 
                        unit.getBooleanType(), 
                        that, 
                        "expression must be of boolean type");
            }
        }
    }

    @Override public void visit(Tree.Resource that) {
        super.visit(that);
        Type t = null;
        Node typedNode = null;
        Tree.Expression e = that.getExpression();
        Tree.Variable v = that.getVariable();
        if (e!=null) {
            t = e.getTypeModel();
            typedNode = e;
            
        } 
        else if (v!=null) {
            t = v.getType().getTypeModel();
            typedNode = v.getType();
            Tree.SpecifierExpression se = 
                    v.getSpecifierExpression();
            if (se==null) {
                v.addError("missing resource specifier");
            }
            else {
                e = se.getExpression();
                if (typedNode instanceof Tree.ValueModifier) {
                    typedNode = se.getExpression();
                }
            }
        }
        else {
            that.addError("missing resource expression");
        }
        if (typedNode!=null) {
            if (!isTypeUnknown(t)) {
                if (e != null) {
                    Type ot = unit.getObtainableType();
                    Type dt = unit.getDestroyableType();
                    Type act = unit.getJavaAutoCloseableType();
                    if (!t.isSubtypeOf(act)) {
                        if (isInstantiationExpression(e)) {
                            if (!t.isSubtypeOf(dt) 
                             && !t.isSubtypeOf(ot)) {
                                typedNode.addError("resource must be either obtainable or destroyable: '" +
                                        t.asString(unit) + 
                                        "' is neither 'Obtainable' nor 'Destroyable'");
                            }
                        }
                        else {
                            checkAssignable(t, ot, typedNode, 
                                    "resource must be obtainable");
                        }
                    }
                }
            }
        }
    }

    @Override public void visit(Tree.ForIterator that) {
        super.visit(that);
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        if (se!=null) {
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                Type et = e.getTypeModel();
                checkContainer(se, et, 
                        "iterated expression");
            }
        }
    }

    private void checkContainer(Node e, Type et, String desc) {
        if (!isTypeUnknown(et)) {
            if (!unit.isContainerType(et)) {
                e.addError(desc + 
                        " is not iterable: '" + 
                        et.asString(unit) + 
                        "' is not a subtype of 'Iterable'");
            }
            else if (unit.isEmptyType(et)) {
                e.addUsageWarning(Warning.redundantIteration,
                        desc + 
                        " is definitely empty: '" +
                        et.asString(unit) + 
                        "' is a subtype of 'Empty'");
            }
        }
    }

    @Override public void visit(Tree.ValueIterator that) {
        super.visit(that);
        Tree.Variable v = that.getVariable();
        if (v!=null) {
            Tree.SpecifierExpression se = 
                    that.getSpecifierExpression();
            inferContainedType(v, se);
            checkContainedType(v, se);
        }
    }

    @Override public void visit(Tree.PatternIterator that) {
        super.visit(that);
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        if (se!=null) {
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                Type et = e.getTypeModel();
                if (!isTypeUnknown(et)) {
                    Type it = unit.getElementType(et);
                    if (it!=null && !isTypeUnknown(it)) {
                        destructure(that.getPattern(), it);
                    }
                }
            }
        }
    }
    
    @Override public void visit(Tree.AttributeDeclaration that) {
        Value val = that.getDeclarationModel();
        Tree.SpecifierOrInitializerExpression sie = 
                that.getSpecifierOrInitializerExpression();
        
        if (sie!=null) {
            inferParameterTypesFromAssignment(
                    val.getType(), 
                    sie.getExpression());
        }
        
        super.visit(that);
        
        if (!val.isActual() 
                // Note: actual members have a completely 
                //       different type inference approach, 
                //       implemented in 
                //       accountForIntermediateRefinements()
                || isTypeUnknown(val.getType())) {
            inferType(that, sie);
        }
        Tree.Type type = that.getType();
        if (type!=null) {
            Type t = type.getTypeModel();
            if (type instanceof Tree.LocalModifier 
                    && !isNativeForWrongBackend(val, unit)) {
                if (val.isParameter() && !val.isInferred()) {
                    type.addError(
                            "parameter may not have inferred type: '" + 
                            val.getName() + 
                            "' must declare an explicit type");
                }
                else if (isTypeUnknown(t)) {
                    if (sie==null) {
                        type.addError("value must specify an explicit type or definition", 200);
                    }
                    else if (!hasError(sie)) {
                        type.addError("value type could not be inferred: '" + 
                                val.getName() + "'" +
                                getTypeUnknownError(t));
                    }
                }
            }
            if (!isTypeUnknown(t)) {
                checkType(t, val, sie, 2100);
            }
        }
        Setter setter = val.getSetter();
        if (setter!=null) {
            setter.getParameter()
                .getModel()
                .setType(val.getType());
        }
    }

    private void inferParameterTypesFromAssignment(Type type, 
            Tree.Term ex) {
        Tree.Term term = unwrapExpressionUntilTerm(ex);
        if (term instanceof Tree.FunctionArgument) {
            Tree.FunctionArgument fun =
                    (Tree.FunctionArgument) term;
            List<Tree.ParameterList> lists = 
                    fun.getParameterLists();
            for (int i=0; 
                    i<lists.size() && 
                    type!=null; 
                    i++) {
                Tree.ParameterList list =
                        lists.get(i);
                List<Type> paramTypes = 
                        unit.getCallableArgumentTypes(type);
                type = unit.getCallableReturnType(type);
                if (paramTypes!=null) {
                    List<Tree.Parameter> params = 
                            list.getParameters();
                    for (int j=0; 
                            j<paramTypes.size() &&
                            j<params.size(); 
                            j++) {
                        Tree.Parameter param = 
                                params.get(j);
                        Parameter p = 
                                param.getParameterModel();
                        FunctionOrValue model = 
                                p.getModel();
                        if (model==null) {
                            Type t = paramTypes.get(j);
                            createInferredParameter(fun, null, 
                                    param, p, t, null, false);
                        }
                    }
                }
            }
        }
    }
    
    @Override public void visit(Tree.ParameterizedExpression that) {
        super.visit(that);
        Tree.Term primary = that.getPrimary();
        if (!hasError(that)) {
            if (primary instanceof Tree.QualifiedMemberExpression ||
                primary instanceof Tree.BaseMemberExpression) {
                Tree.MemberOrTypeExpression mte = 
                        (Tree.MemberOrTypeExpression) 
                            primary;
                if (primary.getTypeModel()!=null && 
                        mte.getDeclaration()!=null) {
                    Type pt = primary.getTypeModel();
                    if (pt!=null) {
                        List<Tree.ParameterList> pls = 
                                that.getParameterLists();
                        for (int j=0; j<pls.size(); j++) {
                            pt = handleExpressionParameterList(
                                    that, mte, pt, pls.get(j));
                        }
                    }
                }
            }
        }
    }

    private Type handleExpressionParameterList(
            Tree.ParameterizedExpression that,
            Tree.MemberOrTypeExpression mte, Type pt,
            Tree.ParameterList pl) {
        Interface cd = unit.getCallableDeclaration();
        Type ct = pt.getSupertype(cd);
        String refName = mte.getDeclaration().getName();
        if (ct==null) { 
            pl.addError("no matching parameter list in referenced declaration: '" + 
                    refName + "'");
        }
        else if (ct.getTypeArgumentList().size()>=2) {
            Type tupleType = 
                    ct.getTypeArgumentList().get(1);
            List<Type> argTypes = 
                    unit.getTupleElementTypes(tupleType);
            boolean variadic = 
                    unit.isTupleLengthUnbounded(tupleType);
            boolean atLeastOne = 
                    unit.isTupleVariantAtLeastOne(tupleType);
            List<Tree.Parameter> params = pl.getParameters();
            if (argTypes.size()!=params.size()) {
                pl.addError("wrong number of declared parameters: '" + 
                        refName  + "' has " + 
                        argTypes.size() + " parameters");
            }
            for (int i=0; 
                    i<argTypes.size() && i<params.size(); 
                    i++) {
                Type at = argTypes.get(i);
                Tree.Parameter param = params.get(i);
                Parameter model = param.getParameterModel();
                //TODO: for #1329 this is not working yet
                //because the parameters of a specification
                //all wind up getting shoved into the 
                //namespace of the containing Declaration
                //back in DeclarationVisitor, when by rights
                //they should belong to the Specification,
                //which means they all get muddled up with
                //each other (this is probably causing other
                //bugs too!)
                /*Reference ref = mte.getTarget();
                Declaration fakeDec = model.getDeclaration();
                if (pt.isTypeConstructor()) {
                    List<Type> typeArgs = 
                            typeParametersAsArgList(
                                    pt.getDeclaration());
                    ref = fakeDec.appliedReference(null, typeArgs);
                                
                }
                else {
                    ref = fakeDec.getReference();
                }
                Type paramType = 
                        ref.getTypedParameter(model)
                            .getFullType();*/
                Type paramType = 
                        model.getModel()
                            .getTypedReference()
                            .getFullType();
                if (!isTypeUnknown(paramType) && 
                    !isTypeUnknown(at) &&
                        !at.isSubtypeOf(paramType)) {
                    param.addError("type of parameter '" + 
                            model.getName() + 
                            "' must be a supertype of corresponding parameter type in declaration of '" + 
                            refName + "'");
                }
            }
            if (!params.isEmpty()) {
                Tree.Parameter lastParam = 
                        params.get(params.size()-1);
                Parameter model = 
                        lastParam.getParameterModel();
                boolean refSequenced = model.isSequenced();
                boolean refAtLeastOne = model.isAtLeastOne();
                if (refSequenced && !variadic) {
                    lastParam.addError("parameter list in declaration of '" + 
                            refName + 
                            "' does not have a variadic parameter");
                }
                else if (!refSequenced && variadic) {
                    lastParam.addError("parameter list in declaration of '" + 
                            refName + 
                            "' has a variadic parameter");
                }
                else if (refAtLeastOne && !atLeastOne) {
                    lastParam.addError("variadic parameter in declaration of '" + 
                            refName + 
                            "' is optional");
                }
                else if (!refAtLeastOne && atLeastOne) {
                    lastParam.addError("variadic parameter in declaration of '" + 
                            refName + 
                            "' is not optional");
                }
            }
            pt = ct.getTypeArgumentList().get(0);
            that.setTypeModel(pt);
        }
        return pt;
    }
    
    @Override public void visit(Tree.SpecifierStatement that) {
        
        Tree.SpecifierExpression rhs = 
                that.getSpecifierExpression();
        Tree.Term lhs = 
                that.getBaseMemberExpression();
        
        if (lhs!=null) {
            lhs.visit(this);
        }
        
        if (rhs!=null) {
            inferParameterTypesFromAssignment(
                    lhs.getTypeModel(), 
                    rhs.getExpression());
            rhs.visit(this);
        }
        
        boolean hasParams = false;
        Tree.Term me = lhs;
        while (me instanceof Tree.ParameterizedExpression) {
            hasParams = true;
            Tree.ParameterizedExpression pe = 
                    (Tree.ParameterizedExpression) me;
            me = pe.getPrimary();
        }
        if (!(me instanceof Tree.StaticMemberOrTypeExpression)) {
            me.addError("illegal specification statement: only a function or value may be specified");
            return;
        }
        
        assign(me);
        
        Declaration d = that.getDeclaration();
        if (d==null &&
                me instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) me;
            d = mte.getDeclaration();
        }
        if (d instanceof TypedDeclaration) {
            TypedDeclaration td = (TypedDeclaration) d;
            
            if (that.getRefinement()) {
                // interpret this specification as a 
                // refinement of an inherited member
                if (d instanceof Value) {
                    refineAttribute(that);
                }
                else if (d instanceof Function) {
                    refineMethod(that);
                }
                Tree.StaticMemberOrTypeExpression smte = 
                        (Tree.StaticMemberOrTypeExpression) me;
                smte.setDeclaration(d);
            } 
            else if (d instanceof FunctionOrValue 
                    && !lhs.hasErrors()) {
                FunctionOrValue mv = (FunctionOrValue) d;
                if (mv.isShortcutRefinement()) {
                    String desc = 
                            d instanceof Value ? 
                                "value" : 
                                "function";
                    me.addError(desc + 
                            " already specified by shortcut refinement: '" + 
                            d.getName(unit) + "'");
                }
                else if (d instanceof Value 
                        && ((Value) d).isInferred()) {
                    me.addError("value is not a variable: '" + 
                            d.getName() + "'");
                }
                else if (!mv.isVariable() && !mv.isLate()) {
                    String desc;
                    desc = d instanceof Value ? 
                            "value is neither variable nor late and" : 
                            "function";
                    if (mv.isToplevel()) {
                        me.addError("toplevel " + desc + 
                                " may not be specified: '" + 
                                d.getName(unit) + "'", 
                                803);
                    }
                    else if (!mv.isDefinedInScope(that.getScope())) {
                        me.addError(desc + 
                                " may not be specified here: '" + 
                                d.getName(unit) + "'", 
                                803);
                    }
                }
            }
            
            if (hasParams 
                    && d instanceof Function) {
                Function f = (Function) d;
                Tree.Expression se = rhs.getExpression();
                if (f.isDeclaredVoid() 
                        && !isStatementExpression(se)) {
                    rhs.addError(
                            "function is declared void so specified expression must be a statement: '" + 
                            d.getName(unit) + 
                            "' is declared 'void'");
                }
            }
            if (rhs instanceof Tree.LazySpecifierExpression 
                    && d instanceof Value) {
                Value v = (Value) d;
                v.setTransient(true);
            }
            
            Type lhst = lhs.getTypeModel();
            if (!isTypeUnknown(lhst)) {
                if (lhs==me 
                        && d instanceof Function 
                        && !lhst.isTypeConstructor()) {
                    //if the declaration of the method has
                    //defaulted parameters, we should ignore
                    //that when determining if the RHS is
                    //an acceptable implementation of the
                    //method
                    //TODO: this is a pretty nasty way to
                    //      handle the problem
                    lhst = eraseDefaultedParameters(lhst);
                }
                TypedDeclaration member = 
                        that.getRefinement() ? 
                                that.getRefined() : td;
                checkType(lhst, member, rhs, 2100);
            }
        }
        
        if (lhs instanceof Tree.ParameterizedExpression) {
            if (!(rhs instanceof Tree.LazySpecifierExpression)) {
                rhs.addError("functions with parameters must be specified using =>");
            }
        }
        else {
            if (rhs instanceof Tree.LazySpecifierExpression && d instanceof Function) {
                rhs.addError("functions without parameters must be specified using =");
            }
        }
    }
    
    private Type eraseDefaultedParameters(Type type) {
        Interface cd = unit.getCallableDeclaration();
        Type callableType = type.getSupertype(cd);
        if (callableType!=null) {
            List<Type> typeArgs = 
                    callableType.getTypeArgumentList();
            if (typeArgs.size()>=2) {
                Type rt = typeArgs.get(0);
                Type pts = typeArgs.get(1);
                List<Type> argTypes = 
                        unit.getTupleElementTypes(pts);
                boolean variadic = 
                        unit.isTupleLengthUnbounded(pts);
                boolean atLeastOne = 
                        unit.isTupleVariantAtLeastOne(pts);
                if (variadic) {
                    argTypes = new ArrayList<Type>(argTypes);
                    Type spt = 
                            argTypes.get(argTypes.size()-1);
                    argTypes.set(argTypes.size()-1, 
                            unit.getIteratedType(spt));
                }
                Type tt = 
                        unit.getTupleType(argTypes, 
                                variadic, atLeastOne, -1);
                return appliedType(cd, rt, tt);
            }
        }
        return type;
    }
    
    static Reference getRefinedMemberReference(
            FunctionOrValue refinement, 
            ClassOrInterface classOrInterface) {
        TypeDeclaration td = 
                (TypeDeclaration) 
                    refinement.getContainer();
        Type supertype = 
                classOrInterface.getType()
                    .getSupertype(td);
        return refinement.appliedReference(supertype, 
                NO_TYPE_ARGS);
    }
    
    private void refineAttribute(Tree.SpecifierStatement that) {
        Value refinedValue = (Value) that.getRefined();
        Value value = (Value) that.getDeclaration();
        ClassOrInterface ci = 
                (ClassOrInterface) 
                    value.getContainer();
        Declaration root = 
                refinedValue.getRefinedDeclaration();
        TypeDeclaration td = 
                (TypeDeclaration) 
                    root.getContainer();
        List<Declaration> interveningRefinements = 
                getInterveningRefinements(value, root, ci, td);
        accountForIntermediateRefinements(that, 
                refinedValue, value, ci, 
                interveningRefinements);
    }
    
    private static Map<TypeParameter,Type>
    substitutions(FunctionOrValue refined, 
                  FunctionOrValue member) {
        if (refined instanceof Function) {
            Function refinedMethod = (Function) refined;
            Function method = (Function) member;
            List<TypeParameter> refinedParams = 
                    refinedMethod.getTypeParameters();
            List<TypeParameter> params = 
                    method.getTypeParameters();
            Map<TypeParameter,Type> result = 
                    new HashMap<TypeParameter,Type>
                    (params.size());
            for (int i=0; 
                    i<refinedParams.size() 
                        && i<params.size(); 
                    i++) {
                result.put(refinedParams.get(i),
                        params.get(i).getType());
            }
            return result;
        }
        else {
            return NO_SUBSTITUTIONS;
        }
    }

    private void refineMethod(Tree.SpecifierStatement that) {
        Function refinedMethod = 
                (Function) 
                    that.getRefined();
        Function method = 
                (Function) 
                    that.getDeclaration();

        ClassOrInterface ci = 
                (ClassOrInterface) 
                    method.getContainer();
        Declaration root = method.getRefinedDeclaration();
        TypeDeclaration td = 
                (TypeDeclaration) 
                    root.getContainer();
        List<Declaration> interveningRefinements = 
                getInterveningRefinements(method, root, ci, td);
        if (interveningRefinements.isEmpty()) {
            that.getBaseMemberExpression()
                .addError("shortcut refinement does not exactly refine any overloaded inherited member");
        }
        else {
            Reference refinedProducedReference = 
                    accountForIntermediateRefinements(that, 
                            refinedMethod, method, ci, 
                            interveningRefinements);
            List<Tree.ParameterList> parameterLists;
            Tree.Term me = that.getBaseMemberExpression();
            if (me instanceof Tree.ParameterizedExpression) {
                Tree.ParameterizedExpression pe = 
                        (Tree.ParameterizedExpression) me;
                parameterLists = pe.getParameterLists();
            }
            else {
                parameterLists = emptyList();
            }
            List<ParameterList> refinedParamLists = 
                    refinedMethod.getParameterLists();
            List<ParameterList> methodParamLists = 
                    method.getParameterLists();
            Map<TypeParameter,Type> subs = 
                    substitutions(refinedMethod, method);
            for (int i=0; 
                    i<refinedParamLists.size() &&
                    i<methodParamLists.size(); 
                    i++) {
                ParameterList refinedParameters = 
                        refinedParamLists.get(i);
                ParameterList parameters = 
                        methodParamLists.get(i);
                Tree.ParameterList parameterList = 
                        parameterLists.size()<=i ? null : 
                            parameterLists.get(i);
                List<Parameter> rps = 
                        refinedParameters.getParameters();
                for (int j=0; j<rps.size(); j++) {
                    Parameter refinedParameter = rps.get(j);
                    Type refinedParameterType = 
                            refinedProducedReference
                            .getTypedParameter(refinedParameter)
                            .getFullType()
                            .substitute(subs, null);
                    Parameter parameter;
                    if (parameterList==null || 
                            parameterList.getParameters().size()<=j) {
                        parameter = 
                                parameters.getParameters()
                                    .get(j);
                        parameter.getModel()
                            .setType(refinedParameterType);
                        parameter.setSequenced(refinedParameter.isSequenced());
                    }
                    else {
                        Tree.Parameter param = 
                                parameterList.getParameters()
                                    .get(j);
                        parameter = 
                                param.getParameterModel();
                        Type parameterType = 
                                parameter.getModel()
                                    .getTypedReference()
                                    .getFullType();
                        Node typeNode = param;
                        if (param instanceof Tree.ParameterDeclaration) {
                            Tree.ParameterDeclaration pd = 
                                    (Tree.ParameterDeclaration) 
                                        param;
                            Tree.Type type = 
                                    pd.getTypedDeclaration()
                                        .getType();
                            if (type!=null) {
                                typeNode = type;
                            }
                        }
                        checkIsExactlyIgnoringNull(
                                refinedParameters.isNamedParametersSupported(),
                                parameterType, 
                                refinedParameterType, 
                                typeNode, 
                                "type of parameter '" + parameter.getName() 
                                + "' of '" + method.getName() 
                                + "' declared by '" + ci.getName() 
                                + "' is different to type of corresponding parameter " 
                                + message(refinedMethod, refinedParameter),
                                9200);
                        if (refinedParameter.isSequenced() 
                                && !parameter.isSequenced()) {
                            param.addError("parameter must be variadic: parameter " 
                                    + message(refinedMethod, refinedParameter)
                                    + " is variadic");
                        }
                        if (!refinedParameter.isSequenced() 
                                && parameter.isSequenced()) {
                            param.addError("parameter may not be variadic: parameter " 
                                    + message(refinedMethod, refinedParameter)
                                    + " is not variadic");
                        }
                    }
                }
            }
        }
    }

    private String message(Function fun, Parameter param) {
        return "'" 
                + param.getName() 
                + "' of refined method '" 
                + fun.getName() 
                + "' of '" 
                + ((Declaration) fun.getContainer()).getName() 
                + "'";
    }

    private Reference accountForIntermediateRefinements(
            Tree.SpecifierStatement that, 
            FunctionOrValue refinedMethodOrValue, 
            FunctionOrValue methodOrValue,
            ClassOrInterface refiningType, 
            List<Declaration> interveningRefinements) {
        //accumulate an intersection of the types of
        //everything it refines
        List<Type> refinedTypes = new ArrayList<Type>();
        //don't check this one here because it is
        //separately checked in visit(SpecifierStatement)
        Reference refinedProducedReference = 
                getRefinedMemberReference(
                        refinedMethodOrValue, 
                        refiningType);
        Map<TypeParameter,Type> substs = 
                substitutions(refinedMethodOrValue, 
                        methodOrValue);
        Type refinedType = 
                refinedProducedReference.getType()
                    .substitute(substs, null);
        boolean allHaveNulls = 
                hasNullReturnValues(refinedType, 
                        refinedMethodOrValue);
        intersectReturnType(refinedTypes, refinedType);
        for (Declaration intervening: interveningRefinements) {
            if (intervening instanceof FunctionOrValue
                    //this one is directly checked from visit(SpecifierStatement)
                    //which correctly handles defaulted parameters 
                    //and assignments of generic function references
                    //TODO: do we need to handle those complicating
                    //      factors here as well?
                    && !refinedMethodOrValue.equals(intervening)) {
                FunctionOrValue refinement = 
                        (FunctionOrValue) 
                            intervening;
                Reference refinedMember = 
                        getRefinedMemberReference(refinement, 
                                refiningType);
                Map<TypeParameter,Type> subs = 
                        substitutions(refinement, 
                                methodOrValue);
                Type type = 
                        refinedMember.getType()
                            .substitute(subs, null);
                allHaveNulls = allHaveNulls 
                        && hasNullReturnValues(type, 
                                refinement);
                intersectReturnType(refinedTypes, type);
                checkIntermediateRefinement(that,
                        refinement, refinedMember);
            }
        }
        Type it = canonicalIntersection(refinedTypes, unit);
        if (allHaveNulls && !unit.isOptionalType(it)) {
            methodOrValue.setUncheckedNullType(true);
            Tree.Term lhs = that.getBaseMemberExpression();
            //TODO: this is pretty ugly, think of something better!
            lhs.setTypeModel(unit.getOptionalType(lhs.getTypeModel()));
        }
        methodOrValue.setType(it);
        return refinedProducedReference;
    }

    private void checkIntermediateRefinement(
            Tree.SpecifierStatement that,
            FunctionOrValue refinement,
            Reference refinedMember) {
        Type requiredType = 
                getRequiredSpecifiedType(that, 
                        refinedMember);
        Tree.SpecifierExpression rhs = 
                that.getSpecifierExpression();
        if (rhs!=null && 
                !isTypeUnknown(requiredType)) {
            checkType(requiredType, refinement, rhs, 2100);
        }
        
        if (!refinement.isDefault() && 
            !refinement.isFormal()) {
            Declaration container = 
                    (Declaration) 
                        refinement.getContainer();
            that.getBaseMemberExpression()
                .addError("shortcut refinement refines non-formal, non-default member: '" +
                        refinement.getName() + "' of '" +
                        container.getName(unit));
        }
    }

    private boolean hasNullReturnValues(
            Type refinedType, 
            FunctionOrValue declaration) {
        return declaration.hasUncheckedNullType() 
            || unit.isOptionalType(refinedType);
    }

    private void intersectReturnType(
            List<Type> refinedTypes, 
            Type refinedType) {
        addToIntersection(refinedTypes, refinedType, unit);
    }

    private Type getRequiredSpecifiedType(
            Tree.SpecifierStatement that,
            Reference refinedMember) {
        Type type = refinedMember.getFullType();
        Tree.Term term = that.getBaseMemberExpression();
        if (term instanceof Tree.ParameterizedExpression) {
            Tree.ParameterizedExpression pe = 
                    (Tree.ParameterizedExpression) term;
            int pls = pe.getParameterLists().size();
            for (int i=0; !isTypeUnknown(type) && i<pls; i++) {
                type = unit.getCallableReturnType(type);
            }
        }
        return type;
    }
    
    @Override public void visit(Tree.TypeParameterDeclaration that) {
        super.visit(that);
        Tree.TypeSpecifier ts = that.getTypeSpecifier();
        if (ts!=null) {
            TypeParameter tpd = that.getDeclarationModel();
            Type dta = tpd.getDefaultTypeArgument();
            if (dta!=null) {
                for (Type st: tpd.getSatisfiedTypes()) {
                    checkAssignable(dta, st, ts.getType(),
                            "default type argument does not satisfy type constraint");
                }
            }
        }
    }
    
    @Override public void visit(Tree.InitializerParameter that) {
        super.visit(that);
        Parameter p = that.getParameterModel();
        FunctionOrValue model = p.getModel();
        if (model!=null) {
            Type type = 
                    model.getTypedReference()
                        .getFullType();
            if (type!=null && !isTypeUnknown(type)) {
                checkType(type, 
                        that.getSpecifierExpression());
            }
            if (model.isParameterized()) {
                checkNotJvm(that,
                        "type functions are not supported on the JVM: '" + 
                        model.getName() + 
                        "' is generic (remove type parameters)");
            }
        }
        else {
            Declaration a = 
                    that.getScope()
                        .getDirectMember(p.getName(), 
                                null, false);
            if (a==null) {
                Declaration fun = p.getDeclaration();
                that.addError(
                        (fun!=null && fun.isAnonymous() ?
                            "parameter is not declared explicitly, and its type cannot be inferred" :
                            "parameter is not declared")
                        +": '" + p.getName() + 
                        "' is not declared anywhere (specify the parameter type explicitly)");
            }
        }
    }
    
    private void checkType(Type declaredType, 
            Tree.SpecifierOrInitializerExpression sie) {
        if (sie!=null) { 
            Tree.Expression ex = sie.getExpression();
            if (ex!=null) {
                Type type = ex.getTypeModel();
                if (!isTypeUnknown(type)) {
                    checkAssignable(type, declaredType, sie, 
                            "specified expression must be assignable to declared type");
                }
            }
        }
    }

    private void checkType(Type declaredType, 
            TypedDeclaration dec, 
            Tree.SpecifierOrInitializerExpression sie, 
            int code) {
        if (sie!=null) {
            Tree.Expression ex = sie.getExpression();
            if (ex!=null) {
                Type type = ex.getTypeModel();
                if (!isTypeUnknown(type)) {
                    checkAssignableIgnoringNull(type, 
                           declaredType, sie, dec,
                           "specified expression must be assignable to declared type of " 
                           + decdesc(dec), 
                           code);
                }
            }
        }
    }

    private String decdesc(Declaration dec) {
        String name = "'" + dec.getName(unit) + "'";
        if (dec.isClassOrInterfaceMember()) {
            Declaration c = 
                    (Declaration) 
                        dec.getContainer();
            return name + " of '" + c.getName(unit) + "'";
        }
        else {
            return name;
        }
    }

    private void checkFunctionType(Tree.Expression e, 
            Tree.Type that, Tree.SpecifierExpression se) {
        Type et = e.getTypeModel();
        if (!isTypeUnknown(et)) {
            checkAssignable(et, that.getTypeModel(), se, 
                    "specified expression type must be assignable to declared return type",
                    2100);
        }
    }

    private void checkOptionalType(Tree.Variable var, 
            Tree.SpecifierExpression se, boolean not) {
        Tree.Type type = var.getType();
        if (type!=null && 
                !(type instanceof Tree.LocalModifier)) {
            Type vt = type.getTypeModel();
            Type nullType = unit.getNullType();
            if (!isTypeUnknown(vt)) {
                Type nt = not ? nullType :
                        unit.getObjectType();
                String message = not ?
                        "specified type must be the null type" :
                        "specified type may not be optional";
                checkAssignable(vt, nt, type, message);
            }
            if (se!=null) {
                Tree.Expression e = se.getExpression();
                if (e!=null) {
                    Type set = e.getTypeModel();
                    if (set!=null) {
                        if (!isTypeUnknown(vt) && 
                                !isTypeUnknown(set)) {
                            Type net = not ? nullType :
                                    unit.getDefiniteType(set);
                            checkAssignable(net, vt, se, 
                                    "specified expression must be assignable to declared type after narrowing");
                        }
                    }
                }
            }
        }
    }

    private void checkEmptyOptionalType(Tree.Variable var, 
            Tree.SpecifierExpression se, boolean not) {
        Tree.Type type = var.getType();
        if (type!=null && 
                !(type instanceof Tree.LocalModifier)) {
            Type emptyType = unit.getEmptyType();
            if (se!=null) {
                Tree.Expression e = se.getExpression();
                if (e!=null) {
                    Type set = e.getTypeModel();
                    Type nullType = unit.getNullType();
                    if (!isTypeUnknown(set) 
                            && !intersectionType(set, nullType, unit)
                                .isNothing()) {
                        emptyType = unit.getOptionalType(emptyType);
                    }
                }
            }
            Type vt = type.getTypeModel();
            if (!isTypeUnknown(vt)) {
                Type nt = not ? emptyType :
                        unit.getSequenceType(unit.getAnythingType());
                String message = not ?
                        "specified type must be the empty sequence type" :
                        "specified type must be a nonempty sequence type";
                checkAssignable(vt, nt, type, message);
            }
            if (se!=null) {
                Tree.Expression e = se.getExpression();
                if (e!=null) {
                    Type set = e.getTypeModel();
                    if (!isTypeUnknown(vt) && 
                            !isTypeUnknown(set)) {
                        Type net = not ? emptyType :
                                unit.getNonemptyDefiniteType(set);
                        checkAssignable(net, vt, se, 
                                "specified expression must be assignable to declared type after narrowing");
                    }
                }
            }
        }
    }

    private void checkContainedType(Tree.Variable var, 
            Tree.SpecifierExpression se) {
        Tree.Type type = var.getType();
        if (type!=null && se!=null) {
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                Type vt = type.getTypeModel();
                Type expressionType = e.getTypeModel();
                if (!isTypeUnknown(vt) && 
                        !isTypeUnknown(expressionType)) {
                    Type it = unit.getElementType(expressionType);
                    checkAssignable(it, vt, var, 
                            "iterable element type must be assignable to iterator variable type");
                }
            }
        }
    }

    /*private void checkKeyValueType(Tree.Variable key, Tree.Variable value, 
            Tree.SpecifierExpression se) {
        if (key.getType()!=null && value.getType()!=null) {
            Type kt = key.getType().getTypeModel();
            Type vt = value.getType().getTypeModel();
            if (!isTypeUnknown(kt) && !isTypeUnknown(vt)) {
                checkType(unit.getIterableType(unit.getEntryType(kt, vt)), se);
            }
        }
    }*/
    
    @Override public void visit(Tree.AttributeGetterDefinition that) {
        Value val = that.getDeclarationModel();
        Declaration od = beginReturnDeclaration(val);
        Tree.Type type = that.getType();
        Tree.Type rt = beginReturnScope(type);
        super.visit(that);
        endReturnScope(rt, val);
        endReturnDeclaration(od);
        Setter setter = val.getSetter();
        if (setter!=null) {
            setter.getParameter()
                .getModel()
                .setType(val.getType());
        }
        if (type instanceof Tree.LocalModifier) {
            if (isTypeUnknown(type.getTypeModel())) {
                type.addError("getter type could not be inferred");
            }
        }
    }

    @Override public void visit(Tree.AttributeArgument that) {
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        Value val = that.getDeclarationModel();
        Tree.Type type = that.getType();
        
        if (se!=null) {
            Type t = null;
            if (type instanceof Tree.LocalModifier) {
                Parameter p = that.getParameter();
                if (p!=null) {
                    FunctionOrValue model = p.getModel();
                    if (model instanceof Value) {
                        t = model.getType();
                    }
                    //TODO: else for a Function use the full type?
                }
            }
            else {
                t = val.getType();
            }
            
            inferParameterTypesFromAssignment(t, 
                    se.getExpression());
        }
        
        if (se==null) {
            Declaration od = beginReturnDeclaration(val);
            Tree.Type rt = beginReturnScope(type);
            super.visit(that);
            endReturnScope(rt, val);
            endReturnDeclaration(od);
        }
        else {
            super.visit(that);
            inferType(that, se);
            if (type!=null) {
                Type t = type.getTypeModel();
                if (!isTypeUnknown(t)) {
                    checkType(t, val, se, 2100);
                }
            }
        }
        if (type instanceof Tree.LocalModifier) {
            if (isTypeUnknown(type.getTypeModel())) {
                if (se==null || !hasError(se)) {
                    Node node = 
                            type.getToken()==null ? 
                                    that : type;
                    node.addError("argument type could not be inferred");
                }
            }
        }
    }

    @Override public void visit(Tree.AttributeSetterDefinition that) {
        Setter set = that.getDeclarationModel();
        Declaration od = beginReturnDeclaration(set);
        Tree.Type rt = beginReturnScope(that.getType());
        super.visit(that);
        endReturnScope(rt, set);
        endReturnDeclaration(od);
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        if (se!=null) {
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                if (!isStatementExpression(e)) {
                    se.addError("specified expression must be a statement: '" +
                            set.getName() + "'");
                }
            }
        }
    }

    @Override public void visit(Tree.MethodDeclaration that) {
        
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        Function fun = that.getDeclarationModel();
        
        if (se!=null) {
            inferParameterTypesFromAssignment(
                    fun.getType(), 
                    se.getExpression());
        }
        
        super.visit(that);
        
        Tree.Type type = that.getType();
        if (se!=null) {
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                if (!fun.isActual() 
                        // Note: actual members have a completely 
                        //       different type inference approach, 
                        //       implemented in 
                        //       accountForIntermediateRefinements()
                        //       & RV.checkRefinedMemberTypeAssignable()
                        || isTypeUnknown(fun.getType())) {
                    inferFunctionType(that, e);
                }
                if (type!=null && 
                        !(type instanceof Tree.DynamicModifier)) {
                    checkFunctionType(e, type, se);
                }
                if (type instanceof Tree.VoidModifier && 
                        !isStatementExpression(e)) {
                    se.addError("function is declared void so specified expression must be a statement: '" +
                            fun.getName() + 
                            "' is declared 'void'",
                            210);
                }
            }
        }
        if (type instanceof Tree.LocalModifier) {
            if (fun.isParameter()) {
                type.addError("parameter may not have inferred type: '" + 
                        fun.getName() + "' must declare an explicit type");
            }
            else if (isTypeUnknown(type.getTypeModel())) {
                if (se==null) {
                    type.addError("function must specify an explicit return type or definition", 200);
                }
                else if (!hasError(se)) {
                    type.addError("function type could not be inferred");
                }
            }
        }
    }

    @Override public void visit(Tree.MethodDefinition that) {
        Function fun = that.getDeclarationModel();
        Declaration od = beginReturnDeclaration(fun);
        Tree.Type type = that.getType();
        Tree.Type rt = beginReturnScope(type);
        super.visit(that);
        endReturnScope(rt, fun);
        endReturnDeclaration(od);
        if (type instanceof Tree.LocalModifier) {
            if (isTypeUnknown(type.getTypeModel())) {
                type.addError("function type could not be inferred");
            }
        }
    }

    @Override public void visit(Tree.MethodArgument that) {
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        Function fun = that.getDeclarationModel();
        Tree.Type type = that.getType();
        if (se==null) {
            Declaration od = beginReturnDeclaration(fun);
            Tree.Type rt = beginReturnScope(type);           
            super.visit(that);
            endReturnScope(rt, fun);
            endReturnDeclaration(od);
        }
        else {
            super.visit(that);
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                inferFunctionType(that, e);
                if (type!=null && 
                        !(type instanceof Tree.DynamicModifier)) {
                    checkFunctionType(e, type, se);
                }
                if (fun.isDeclaredVoid() && 
                        !isStatementExpression(e)) {
                    se.addError("functional argument is declared void so specified expression must be a statement: '" + 
                            fun.getName() + "' is declared 'void'");
                }
            }
        }
        if (type instanceof Tree.LocalModifier) {
            if (isTypeUnknown(type.getTypeModel())) {
                if (se==null || hasError(type)) {
                    Node node = 
                            type.getToken()==null ? 
                                    that : type;
                    node.addError("argument type could not be inferred");
                }
            }
        }
    }
    
    @Override public void visit(Tree.CaseTypes that) {
        super.visit(that);
        if (that.getTypes().size()==1) {
            Tree.Type type = that.getTypes().get(0);
            Type ct = type.getTypeModel();
            if (!isTypeUnknown(ct)) {
                TypeDeclaration ctd = ct.getDeclaration();
                if (ctd.isSelfType()) {
                    TypeDeclaration td = 
                            (TypeDeclaration) 
                            that.getScope();
                    Type t = td.getType();
                    for (Type bound: ct.getSatisfiedTypes()) {
                        if (!t.isSubtypeOf(bound)) {
                            type.addError("type does not satisfy upper bound of self type: '" + 
                                    td.getName() + 
                                    "' is not a subtype of upper bound '" + 
                                    bound.asString(unit) + 
                                    "' of its self type '" + 
                                    ctd.getName() + "'");
                        }
                    }
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.TypedDeclaration that) {
        super.visit(that);
        TypedDeclaration d = 
                that.getDeclarationModel();
        Type type = d.getType();
        FunctionOrValue fov = (FunctionOrValue) d;
        if (fov.isSmall()
                && type != null
                && !type.isInteger()
                && !type.isFloat()
                && !type.isCharacter()
                && !that.getType().hasErrors()) {
            that.addError("type may not be annotated 'small': '" 
                + d.getName() + "' has type '"
                + type.asString(that.getUnit()) 
                + "' (only an 'Integer', 'Float', or 'Character' may be small)");
        }
    }
    
    private static Tree.VoidModifier fakeVoid(Node that) {
        return new Tree.VoidModifier(that.getToken());
    }
    
    @Override public void visit(Tree.ClassDefinition that) {
        Class c = that.getDeclarationModel();
        Declaration od = beginReturnDeclaration(c);
        Tree.Type rt = beginReturnScope(fakeVoid(that));
        super.visit(that);
        endReturnScope(rt, null);
        endReturnDeclaration(od);
    }

    @Override public void visit(Tree.InterfaceDefinition that) {
        Interface i = that.getDeclarationModel();
        Declaration od = beginReturnDeclaration(i);
        Tree.Type rt = beginReturnScope(null);
        super.visit(that);
        endReturnScope(rt, null);
        endReturnDeclaration(od);
    }

    @Override public void visit(Tree.ObjectDefinition that) {
        Class ac = that.getAnonymousClass();
        Declaration od = beginReturnDeclaration(ac);
        Tree.Type rt = beginReturnScope(fakeVoid(that));
        super.visit(that);
        endReturnScope(rt, null);
        endReturnDeclaration(od);
    }

    @Override public void visit(Tree.ObjectArgument that) {
        Class ac = that.getAnonymousClass();
        Declaration od = beginReturnDeclaration(ac);
        Tree.Type rt = beginReturnScope(fakeVoid(that));
        super.visit(that);
        endReturnScope(rt, null);
        endReturnDeclaration(od);
    }
    
    @Override public void visit(Tree.ObjectExpression that) {
        Class ac = that.getAnonymousClass();
        Declaration od = beginReturnDeclaration(ac);
        Tree.Type rt = beginReturnScope(fakeVoid(that));
        super.visit(that);
        endReturnScope(rt, null);
        endReturnDeclaration(od);
        that.setTypeModel(unit.denotableType(ac.getType()));
    }
    
    @Override public void visit(Tree.ClassDeclaration that) {
        super.visit(that);
        Class alias = that.getDeclarationModel();
        Type et = alias.getExtendedType();
        Tree.ClassSpecifier cs = that.getClassSpecifier();
        if (cs!=null && et!=null) {
            TypeDeclaration etd = et.getDeclaration();
            if (etd instanceof Constructor) {
                etd = etd.getExtendedType().getDeclaration();
            }
            if (etd instanceof Class) {
                //TODO: some of this belongs in InheritanceVisitor! 
                Class c = (Class) etd;
                if (c.isAbstract()) {
                    if (!alias.isFormal() && 
                        !alias.isAbstract()) {
                        that.addError("alias of abstract class must be annotated abstract", 
                                310);
                    }
                }
                if (c.isAbstraction()) {
                    that.addError("class alias may not alias overloaded class");
                }
                else {
                    Tree.InvocationExpression ie = 
                            cs.getInvocationExpression();
                    if (ie!=null) {
                        checkClassAliasParameters(alias, that, ie);
                    }
                }
            }
        }
    }
    
    private void checkClassAliasParameters(Class alias, 
            Tree.ClassDeclaration that, 
            Tree.InvocationExpression ie) {
        Tree.Primary primary = ie.getPrimary();
        Tree.ExtendedTypeExpression smte= 
                (Tree.ExtendedTypeExpression) 
                    primary;
        Functional classOrConstructor = 
                (Functional) 
                    smte.getDeclaration();
        ParameterList cpl = 
                classOrConstructor.getFirstParameterList();
        ParameterList apl = alias.getParameterList();
        if (cpl!=null && apl!=null) {
            List<Parameter> cplps = 
                    cpl.getParameters();
            List<Parameter> aplps = 
                    apl.getParameters();
            int cps = cplps.size();
            int aps = aplps.size();
            if (cps!=aps) {
                that.getParameterList()
                    .addUnsupportedError(
                            "wrong number of initializer parameters declared by class alias: '" + 
                                    alias.getName() + "'");
            }
            
            for (int i=0; i<cps && i<aps; i++) {
                Parameter ap = aplps.get(i);
                Parameter cp = cplps.get(i);
                Reference target = smte.getTarget();
                FunctionOrValue apm = ap.getModel();
                if (apm!=null && target!=null) {
                    Type pt = 
                            target.getTypedParameter(cp)
                                .getFullType();
                    Type apt = 
                            apm.getReference()
                                .getFullType();
                    if (!isTypeUnknown(pt) && 
                        !isTypeUnknown(apt) &&
                            !apt.isSubtypeOf(pt)) {
                        that.addUnsupportedError(
                                "alias parameter '" + 
                                ap.getName() + 
                                "' must be assignable to corresponding class parameter '" +
                                cp.getName() + 
                                "'" + 
                                notAssignableMessage(
                                        apt, pt, that));
                    }
                }
            }
            //temporary restrictions
            checkAliasedClass(that, cpl, apl);
        }
    }

    private void checkAliasedClass(Tree.ClassDeclaration that, 
            ParameterList cpl, ParameterList apl) {
        //temporary restrictions
        //TODO: all this can be removed once the backend
        //      implements full support for the new class 
        //      alias stuff
        Tree.InvocationExpression ie = 
                that.getClassSpecifier()
                    .getInvocationExpression();
        Tree.PositionalArgumentList pal = 
                ie.getPositionalArgumentList();
        if (pal!=null) {
            List<Tree.PositionalArgument> pas = 
                    pal.getPositionalArguments();
            int cps = cpl.getParameters().size();
            int aps = apl.getParameters().size();
            int size = pas.size();
            
            if (cps != size) {
                pal.addUnsupportedError(
                        "wrong number of arguments for aliased class: '" + 
                        that.getDeclarationModel().getName() + 
                        "' has " + cps + " parameters");
            }
            for (int i=0; 
                    i<size && i<cps && i<aps; 
                    i++) {
                Tree.PositionalArgument pa = 
                        pas.get(i);
                Parameter aparam = 
                        apl.getParameters().get(i);
                Parameter cparam = 
                        cpl.getParameters().get(i);
                if (pa instanceof Tree.ListedArgument) {
                    if (cparam.isSequenced()) {
                        pa.addUnsupportedError(
                                "argument to variadic parameter of aliased class must be spread");
                    }
                    Tree.ListedArgument la = 
                            (Tree.ListedArgument) pa;
                    Tree.Expression e = 
                            la.getExpression();
                    checkAliasArg(aparam, e);
                }
                else if (pa instanceof Tree.SpreadArgument) {
                    if (!cparam.isSequenced()) {
                        pa.addUnsupportedError(
                                "argument to non-variadic parameter of aliased class may not be spread");
                    }
                    Tree.SpreadArgument sa = 
                            (Tree.SpreadArgument) pa;
                    Tree.Expression e = 
                            sa.getExpression();
                    checkAliasArg(aparam, e);
                }
                else if (pa!=null) {
                    pa.addUnsupportedError(
                            "argument to parameter or aliased class must be listed or spread");
                }
            }
        }
    }

    private void checkAliasArg(Parameter param, 
            Tree.Expression e) {
        if (e!=null && param!=null) {
            FunctionOrValue p = param.getModel();
            if (p!=null) {
                Tree.Term term = e.getTerm();
                if (term instanceof Tree.BaseMemberExpression) {
                    Tree.BaseMemberExpression bme = 
                            (Tree.BaseMemberExpression) 
                                term;
                    Declaration d = bme.getDeclaration();
                    if (d!=null && !d.equals(p)) {
                        e.addUnsupportedError("argument must be a parameter reference to " +
                                paramdesc(param));
                    }
                }
                else {
                    e.addUnsupportedError("argument must be a parameter reference to " +
                            paramdesc(param));
                }
            }
        }
    }
    
    private void inferType(Tree.TypedDeclaration that, 
            Tree.SpecifierOrInitializerExpression spec) {
        Tree.Type type = that.getType();
        if (type instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = 
                    (Tree.LocalModifier) type;
            if (spec!=null) {
                setType(local, spec, that);
            }
        }
    }

    private void inferType(Tree.AttributeArgument that, 
            Tree.SpecifierOrInitializerExpression spec) {
        Tree.Type type = that.getType();
        if (type instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = 
                    (Tree.LocalModifier) type;
            if (spec!=null) {
                setType(local, spec, that);
            }
        }
    }

    private void inferFunctionType(Tree.TypedDeclaration that, 
            Tree.Expression e) {
        Tree.Type type = that.getType();
        if (type instanceof Tree.FunctionModifier) {
            Tree.FunctionModifier local = 
                    (Tree.FunctionModifier) type;
            Type et = e.getTypeModel();
            if (et!=null) {
                setFunctionType(local, et, that, e);
            }
        }
    }
    
    private void inferFunctionType(Tree.MethodArgument that, 
            Tree.Expression e) {
        Tree.Type type = that.getType();
        if (type instanceof Tree.FunctionModifier) {
            Tree.FunctionModifier local = 
                    (Tree.FunctionModifier) type;
            Type et = e.getTypeModel();
            if (et!=null) {
                setFunctionType(local, et, that, e);
            }
        }
    }
    
    private void inferDefiniteType(Tree.Variable that, 
            Tree.SpecifierExpression se, boolean not) {
        Tree.Type type = that.getType();
        if (type instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = 
                    (Tree.LocalModifier) type;
            if (not) {
                setNullTypeFromOptionalType(that, local, se);
            }
            else if (se!=null) {
                setDefiniteTypeFromOptionalType(that, local, se);
            }
        }
    }

    private void inferNonemptyType(Tree.Variable that, 
            Tree.SpecifierExpression se, boolean not) {
        Tree.Type type = that.getType();
        if (type instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = 
                    (Tree.LocalModifier) type;
            if (not) {
                setEmptyTypeFromSequenceType(that, local, se);
            }
            else if (se!=null) {
                setNonemptyTypeFromSequenceType(that, local, se);
            }
        }
    }

    private void inferContainedType(Tree.Variable that, 
            Tree.SpecifierExpression se) {
        Tree.Type type = that.getType();
        if (type instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = 
                    (Tree.LocalModifier) type;
            if (se!=null) {
                setTypeFromIterableType(local, se, that);
            }
        }
    }

    /*private void inferKeyType(Tree.Variable key, 
            Tree.SpecifierExpression se) {
        if (key.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = 
                    (Tree.LocalModifier) key.getType();
            if (se!=null) {
                setTypeFromKeyType(local, se, key);
            }
        }
    }

    private void inferValueType(Tree.Variable value, 
            Tree.SpecifierExpression se) {
        if (value.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = 
                    (Tree.LocalModifier) value.getType();
            if (se!=null) {
                setTypeFromValueType(local, se, value);
            }
        }
    }*/
    
    private void inferValueType(Tree.Variable value, 
            Type t) {
        Tree.Type type = value.getType();
        if (type instanceof Tree.LocalModifier) {
            Tree.ValueModifier local = 
                    (Tree.ValueModifier) type;
            if (t!=null) {
                setValueType(local, t, value);
            }
        }
    }
    
    private void setDefiniteTypeFromOptionalType(
            Tree.Variable that, Tree.LocalModifier local, 
            Tree.SpecifierExpression se) {
        Tree.Expression ex = se.getExpression();
        if (ex!=null) {
            Type expressionType = ex.getTypeModel();
            if (!isTypeUnknown(expressionType)) {
                expressionType = 
                        unit.getDefiniteType(
                                expressionType);
                local.setTypeModel(expressionType);
                that.getDeclarationModel()
                    .setType(expressionType);
            }
        }
    }
    
    private void setNullTypeFromOptionalType(
            Tree.Variable that, Tree.LocalModifier local,
            Tree.SpecifierExpression se) {
        Type nullType = unit.getNullType();
        Value dec = that.getDeclarationModel();
        Tree.Expression ex = se.getExpression();
        if (ex!=null) {
            Type expressionType = ex.getTypeModel();
            if (!isTypeUnknown(expressionType)) {
                nullType = 
                        intersectionType(expressionType, 
                                nullType, unit);
            }
            handleUncheckedNulls(local, ex, dec);
        }
        local.setTypeModel(nullType);
        dec.setType(nullType);
    }

    private void setNonemptyTypeFromSequenceType(
            Tree.Variable that, Tree.LocalModifier local, 
            Tree.SpecifierExpression se) {
        Tree.Expression ex = se.getExpression();
        if (ex!=null) {
            Type expressionType = ex.getTypeModel();
            if (!isTypeUnknown(expressionType)) {
                if (unit.isSequentialType(
                        unit.getDefiniteType(
                                expressionType))) {
                    expressionType = 
                            unit.getNonemptyDefiniteType(
                                    expressionType);
                }
                local.setTypeModel(expressionType);
                that.getDeclarationModel()
                    .setType(expressionType);
            }
        }
    }
    
    private void setEmptyTypeFromSequenceType(
            Tree.Variable that, Tree.LocalModifier local,
            Tree.SpecifierExpression se) {
        Type emptyType = unit.getEmptyType();
        Value dec = that.getDeclarationModel();
        Tree.Expression ex = se.getExpression();
        if (ex!=null) {
            Type expressionType = ex.getTypeModel();
            if (!isTypeUnknown(expressionType)) {
                Type nullType = unit.getNullType();
                emptyType = 
                        intersectionType(
                            expressionType, 
                            unionType(
                                emptyType, 
                                nullType, 
                                unit),
                            unit);
            }
            handleUncheckedNulls(local, ex, dec);
        }
        local.setTypeModel(emptyType);
        dec.setType(emptyType);
    }

    private void setTypeFromIterableType(
            Tree.LocalModifier local, 
            Tree.SpecifierExpression se, 
            Tree.Variable that) {
        Tree.Expression ex = se.getExpression();
        if (ex!=null) {
            Type ext = ex.getTypeModel();
            if (ext!=null) {
                Value dec = 
                        that.getDeclarationModel();
                Type elementType = 
                        unit.getElementType(ext);
                if (elementType!=null) {
                    if (unit.isJavaIterableType(ext) || 
                        unit.isJavaObjectArrayType(ext)) {
                        handleUncheckedNulls(local,
                                elementType, ex, dec);
                    }
                    local.setTypeModel(elementType);
                    dec.setType(elementType);
                }
            }
        }
    }
    
    /*private void setTypeFromKeyType(Tree.LocalModifier local,
            Tree.SpecifierExpression se, Tree.Variable that) {
        Tree.Expression e = se.getExpression();
        if (e!=null) {
            Type expressionType = e.getTypeModel();
            if (expressionType!=null) {
                Type entryType = 
                        unit.getIteratedType(expressionType);
                if (entryType!=null) {
                    Type kt = 
                            unit.getKeyType(entryType);
                    if (kt!=null) {
                        local.setTypeModel(kt);
                        that.getDeclarationModel().setType(kt);
                    }
                }
            }
        }
    }
    
    private void setTypeFromValueType(Tree.LocalModifier local,
            Tree.SpecifierExpression se, Tree.Variable that) {
        Tree.Expression e = se.getExpression();
        if (e!=null) {
            Type expressionType = 
                    e.getTypeModel();
            if (expressionType!=null) {
                Type entryType = 
                        unit.getIteratedType(expressionType);
                if (entryType!=null) {
                    Type vt = 
                            unit.getValueType(entryType);
                    if (vt!=null) {
                        local.setTypeModel(vt);
                        that.getDeclarationModel().setType(vt);
                    }
                }
            }
        }
    }*/
    
    private void handleUncheckedNulls(
            Tree.LocalModifier local, 
            Tree.Expression ex,
            Declaration declaration) {
        if (hasUncheckedNulls(ex)) {
            handleUncheckedNulls(local, 
                    ex.getTypeModel(),
                    ex.getTerm(),
                    declaration);
        }
    }
    
    private void handleUncheckedNulls(
            Tree.LocalModifier local, 
            Type type, Tree.Term term,
            Declaration declaration) {
        if (type!=null
                && !type.isUnknown()
                && type.isSubtypeOf(unit.getObjectType())) {
            if (declaration 
                    instanceof TypedDeclaration
                && canHaveUncheckedNulls(type)) {
                TypedDeclaration td =
                        (TypedDeclaration) 
                            declaration;
                td.setUncheckedNullType(true);
            }
            else {
                //this case happens when a method of
                //a Ceylon-primitive instantiation of
                //a Java generic type is assigned to
                //an inferred value or function
                warnUncheckedNulls(local, type, term);
            }
        }
    }
    
    private static boolean canHaveUncheckedNulls(Type type) {
        return !type.isInteger() 
            && !type.isFloat()
            && !type.isBoolean() 
            && !type.isByte()
            && !type.isCharacter();
    }
    
    private static void warnUncheckedNulls(
            Tree.LocalModifier local, 
            Type type, Tree.Term term) {
        Node node = term==null ? local : term; //often the LocalModifier doesn't have location info
        Unit unit = local.getUnit();
        if (term instanceof Tree.InvocationExpression) {
            Tree.InvocationExpression ie = 
                    (Tree.InvocationExpression) 
                        term;
            Tree.Term p = ie.getPrimary();
            if (p instanceof Tree.StaticMemberOrTypeExpression) {
                Tree.StaticMemberOrTypeExpression bme = 
                        (Tree.StaticMemberOrTypeExpression) p;
                Declaration dec = bme.getDeclaration();
                if (dec!=null) {
                    String str = type.asSourceCodeString(unit);
                    node.addUsageWarning(Warning.inferredNotNull, 
                            "not null type inferred from invocation of function with unchecked nulls: '" 
                            + dec.getName(unit) 
                            + "' is not known to be null-safe (explicitly specify the type '" 
                            + str + "' or '" + str + "?')");
                    return;
                }
            }
        }
        else if (term instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression bme = 
                    (Tree.StaticMemberOrTypeExpression) 
                        term;
            Declaration dec = bme.getDeclaration();
            if (dec!=null) {
                String str = type.asSourceCodeString(unit);
                node.addUsageWarning(Warning.inferredNotNull, 
                        "not null type inferred from reference to value with unchecked nulls: '" 
                        + dec.getName(unit) 
                        + "' is not known to be null-safe (explicitly specify the type '" 
                        + str + "' or '" + str + "?')");
                return;
            }
        }
        String str = type.asSourceCodeString(unit);
        node.addUsageWarning(Warning.inferredNotNull, 
                "not null type inferred from reference to function or value with unchecked nulls (explicitly specify the type '" 
                + str + "' or '" + str + "?')");
    }
    
    private void setType(Tree.LocalModifier local, 
            Tree.SpecifierOrInitializerExpression s, 
            Tree.TypedDeclaration that) {
        Tree.Expression e = s.getExpression();
        if (e!=null) {
            Type type = e.getTypeModel();
            if (type!=null) {
                TypedDeclaration dec = 
                        that.getDeclarationModel();
                handleUncheckedNulls(local, e, dec);
                Type t = inferrableType(type);
                local.setTypeModel(t);
                dec.setType(t);
            }
        }
    }

    private void setType(Tree.LocalModifier local, 
            Tree.SpecifierOrInitializerExpression s, 
            Tree.AttributeArgument that) {
        Tree.Expression e = s.getExpression();
        if (e!=null) {
            Type type = e.getTypeModel();
            if (type!=null) {
                Value dec = that.getDeclarationModel();
                handleUncheckedNulls(local, e, dec);
                Type t = inferrableType(type);
                local.setTypeModel(t);
                dec.setType(t);
            }
        }
    }
        
    private void setSequencedValueType(
            Tree.SequencedType spread, 
            Type et, Tree.TypedDeclaration that) {
        Type t = inferrableType(et);
        spread.setTypeModel(t);
        that.getDeclarationModel().setType(t);
    }
        
    private void setValueType(Tree.ValueModifier local, 
            Type et, Tree.TypedDeclaration that) {
        Type t = inferrableType(et);
        local.setTypeModel(t);
        that.getDeclarationModel().setType(t);
    }
        
    private void setFunctionType(Tree.FunctionModifier local, 
            Type et, Tree.TypedDeclaration that, 
            Tree.Expression e) {
        TypedDeclaration dec = 
                that.getDeclarationModel();
        handleUncheckedNulls(local, e, dec);
        Type t = inferrableType(et);
        local.setTypeModel(t);
        dec.setType(t);
    }
        
    private void setFunctionType(Tree.FunctionModifier local, 
            Type et, Tree.MethodArgument that, 
            Tree.Expression e) {
        Function dec = that.getDeclarationModel();
        handleUncheckedNulls(local, e, dec);
        Type t = inferrableType(et);
        local.setTypeModel(t);
        dec.setType(t);
    }

    private Type inferrableType(Type et) {
        return unit.denotableType(et)
            .withoutUnderlyingType();
    }
        
    @Override public void visit(Tree.Throw that) {
        super.visit(that);
        Tree.Expression e = that.getExpression();
        if (e!=null) {
            Type et = e.getTypeModel();
            if (!isTypeUnknown(et)) {
                Type tt = unit.getThrowableType();
                checkAssignable(et, tt, e, 
                        "thrown expression must be a throwable");
            }
        }
    }
    
    @Override public void visit(Tree.Return that) {
        Tree.Expression e = that.getExpression();
        
        if (returnDeclaration instanceof FunctionOrValue) {
            FunctionOrValue model = 
                    (FunctionOrValue)
                        returnDeclaration;
            inferParameterTypesFromAssignment(model.getType(), e);
        }
        
        super.visit(that);
        
        if (returnType==null) {
            //misplaced return statements are already handled by ControlFlowVisitor
            //missing return types declarations already handled by TypeVisitor
            //that.addError("could not determine expected return type");
        } 
        else {
            that.setDeclaration(returnDeclaration);
            if (e==null) {
                if (!(returnType instanceof Tree.VoidModifier)) {
                    if (returnDeclaration instanceof Function) {
                        that.addError("function must return a value: " +
                                returndesc() + " is not a 'void' function", 
                                12000);
                    }
                    else {
                        that.addError("getter must return a value: " +
                                returndesc() + " is a getter");
                    }
                }
            }
            else {
                if (returnType instanceof Tree.VoidModifier) {
                    if (returnDeclaration instanceof Function) {
                        that.addError("function may not return a value: " +
                                returndesc() + " is a 'void' function", 
                                13000);
                    }
                    else if (returnDeclaration instanceof TypedDeclaration) {
                        that.addError("setter may not return a value: " +
                                returndesc() + " is a setter");
                    }
                    else {
                        that.addError("class initializer may not return a value");
                    }
                }
                else {
                    Type at = e.getTypeModel();
                    if (at!=null) {
                        Type et = returnType.getTypeModel();
                        if (returnType instanceof Tree.LocalModifier) {
                            if (returnDeclaration.isActual()) {
                                // Note: actual members have a completely 
                                //       different type inference approach, 
                                //       implemented in 
                                //       accountForIntermediateRefinements()
                                //       & RV.checkRefinedMemberTypeAssignable()
                                if (!isTypeUnknown(et) 
                                        && !isTypeUnknown(at)) {
                                    checkAssignable(at, et, e, 
                                            "returned expression must be assignable to inferred return type of " +
                                            returndesc(), 
                                            2100);
                                }
                            }
                            else {
                                inferReturnType(et, at, e);
                            }
                        }
                        else {
                            if (!isTypeUnknown(et) 
                                    && !isTypeUnknown(at)) {
                                checkAssignable(at, et, e, 
                                        "returned expression must be assignable to return type of " +
                                        returndesc(), 
                                        2100);
                            }
                        }
                    }
                }
            }
        }
    }

    private String returndesc() {
        String name = returnDeclaration.getName();
        if (name==null || 
                returnDeclaration.isAnonymous()) {
            name = "anonymous function";
        }
        else {
            name = "'" + name + "'";
        }
        return name;
    }

    private void inferReturnType(Type et, Type at, 
            Tree.Expression e) {
        if (at!=null) {
            Tree.LocalModifier local = 
                    (Tree.LocalModifier) 
                        returnType;
            handleUncheckedNulls(local, e, 
                    returnDeclaration);
            at = unit.denotableType(at);
            if (et==null || et.isSubtypeOf(at)) {
                returnType.setTypeModel(at);
            }
            else if (!at.isSubtypeOf(et)) {
                Type rt = unionType(at, et, unit);
                returnType.setTypeModel(rt);
            }
        }
    }
    
    private void checkMemberOperator(Type pt, 
            Tree.QualifiedMemberOrTypeExpression qmte) {
        Tree.MemberOperator op = qmte.getMemberOperator();
        Tree.Primary p = qmte.getPrimary();
        if (op instanceof Tree.SafeMemberOp)  {
            if (qmte.getStaticMethodReference()) {
                op.addError("static reference may not involve safe member operator");
            }
            else {
                checkOptional(pt, p, p);
            }
        }
        else if (op instanceof Tree.SpreadOp) {
            if (qmte.getStaticMethodReference()) {
                op.addError("static reference may not involve spread member operator");
            }
            else {
                checkIterable(pt, p);
            }
        }
        if (!qmte.getStaticMethodReference() 
                && pt.isCallable()) {
            qmte.addUsageWarning(Warning.expressionTypeCallable, 
                    "operation is not meaningful for function references: receiver is of type 'Callable'");
        }
    }

    private Type unwrap(Type type, 
            Tree.QualifiedMemberOrTypeExpression qmte) {
        if (qmte.getStaticMethodReference()) {
            return type;
        }
        else if (type==null) {
            return null;
        }
        else {
            Tree.MemberOperator op = qmte.getMemberOperator();
            if (op instanceof Tree.SafeMemberOp)  {
                return unit.getDefiniteType(type);
            }
            else if (op instanceof Tree.SpreadOp) {
                Type it = unit.getElementType(type);
                return it==null ? unit.getUnknownType() : it;
            }
            else {
                return type;
            }
        }
    }
    
    Type wrap(Type type, Type receivingType, 
            Tree.QualifiedMemberOrTypeExpression mte) {
        if (mte.getStaticMethodReference()) {
            return type;
        }
        else {
            Tree.MemberOperator op = mte.getMemberOperator();
            if (op instanceof Tree.SafeMemberOp)  {
                return unit.getOptionalType(type);
            }
            else if (op instanceof Tree.SpreadOp) {
                return unit.isNonemptyIterableType(receivingType) ?
                        unit.getSequenceType(type) :
                            unit.getSequentialType(type);
            }
            else {
                return type;
            }
        }
    }
    
    /**
     * Typecheck an invocation expression. Note that this
     * is a tricky process involving type argument inference,
     * anonymous function parameter type inference, function
     * reference type argument inference, Java overload
     * resolution, and argument type checking, and it all
     * has to happen in exactly the right order, which is
     * not at all the natural order for walking the tree.
     */
    @Override 
    public void visit(Tree.InvocationExpression that) {
        
        Tree.Primary p = that.getPrimary();
        Tree.PositionalArgumentList pal = 
                that.getPositionalArgumentList();
        Tree.NamedArgumentList nal = 
                that.getNamedArgumentList();
        
        if (unwrapExpressionUntilTerm(p)
                instanceof Tree.FunctionArgument) {
            //if the primary is an anonymous 
            //function, we must infer its parameter
            //types, but we don't need to worry
            //about overload resolution or named
            //arguments
            if (pal!=null) {
                pal.visit(this);
                inferPrimaryParameterTypes(p, pal);
            }
            p.visit(this);
            visitInvocationPrimary(that);
            if (pal!=null) {
                inferParameterTypes(p, pal, true);
            }
            visitIndirectInvocation(that);
            return;
        }
        
        //assign some provisional argument types 
        //that will help with overload resolution
        visitInvocationPositionalArgs(that);
        
        //make a first attempt at resolving the 
        //invoked reference (but we don't have 
        //all the needed types yet)
        p.visit(this);
        
        //set up the parameter lists of all 
        //argument anonymous functions with 
        //inferred (missing) parameter lists
        createAnonymousFunctionParameters(that);
        
        //named argument lists are the easy
        //case because they don't support
        //anonymous functions with inferred 
        //parameter lists
        if (nal!=null) {
            inferParameterTypes(p, nal);
            nal.visit(this);
        }
        
        int argCount = 0; 
        boolean[] delayed = null;
        if (pal!=null) {
            List<PositionalArgument> args = 
                    pal.getPositionalArguments();
            argCount = args.size();
            //infer parameter types as far as we 
            //can without having the inferred type 
            //parameters of the primary
            delayed = inferParameterTypes(p, pal, false);
            //assign types to all the positional 
            //arguments up until the first one for 
            //which parameter type inference failed
            for (int j=0; j<argCount; j++) {
                if (!delayed[j]) {
                    Tree.PositionalArgument pa = 
                            args.get(j);
                    if (pa!=null) {
                        pa.visit(this);
                    }
                }
            }
        }
        
        if (p instanceof Tree.Compose) {
            inferComposedParameterType(pal, delayed);
        }
        
        //assign some additional types that
        //we will use for overload resolution
        visitInvocationPositionalArgs(that);
        
        //now here's where overloading is 
        //finally resolved and then type 
        //arguments are inferred 
        visitInvocationPrimary(that);
        
        if (pal!=null) {
            List<PositionalArgument> args = 
                    pal.getPositionalArguments();
            //now infer the remaining parameter 
            //types
            inferParameterTypes(p, pal, true);
            //assign types to the remaining 
            //positional arguments which we
            //missed the first time round
            for (int j=0; j<argCount; j++) {
                if (delayed[j]) {
                    Tree.PositionalArgument pa = 
                            args.get(j);
                    if (pa!=null) {
                        pa.visit(this);
                    }
                }
            }
        }
        
        //now check that the invocation is
        //well-typed (that arguments are
        //assignable to parameters)
        if (isIndirectInvocation(that)) {
            visitIndirectInvocation(that);
        }
        else {
            visitDirectInvocation(that);
        }
        
    }
    
    /**
     * Special-case hack to allow the parameter
     * type of an anonymous function used as
     * the RHS of >|> to be inferred from the
     * known return type of the LHS. (The
     * problem is that the parameter type is
     * set too late to use it to infer the
     * type argument to the compose() 
     * function.)
     */
    private void inferComposedParameterType(
            Tree.PositionalArgumentList pal, 
            boolean[] delayed) {
        List<Tree.PositionalArgument> args = 
                pal.getPositionalArguments();
        Tree.ListedArgument x = 
                (Tree.ListedArgument)
                    args.get(0);
        Tree.ListedArgument y = 
                (Tree.ListedArgument)
                    args.get(1);
        Tree.Term xt = 
                unwrapExpressionUntilTerm(
                    x.getExpression());
        if (xt instanceof Tree.FunctionArgument) {
            Tree.FunctionArgument fun =
                    (Tree.FunctionArgument) xt;
            List<Tree.ParameterList> pls = 
                    fun.getParameterLists();
            if (!pls.isEmpty()) {
                Tree.ParameterList pl = 
                        pls.get(0);
                List<Tree.Parameter> params = 
                        pl.getParameters();
                if (!params.isEmpty()) {
                    Tree.Parameter param = 
                            params.get(0);
                    Type t = unit.getCallableReturnType(
                            y.getTypeModel());
                    Parameter model = 
                            param.getParameterModel();
                    createInferredParameter(fun, null, 
                            param, model, t, null, 
                            false);
                    x.visit(this);
                    delayed[0] = false;
                }
            }
        }
    }
    
    private void inferPrimaryParameterTypes(
            Tree.Primary p, 
            Tree.PositionalArgumentList pal) {
        Tree.Term term = unwrapExpressionUntilTerm(p);
        if (term instanceof Tree.FunctionArgument) {
            Tree.FunctionArgument fun = 
                    (Tree.FunctionArgument) term;
            List<Tree.ParameterList> pls = 
                    fun.getParameterLists();
            if (!pls.isEmpty()) {
                Tree.ParameterList pl =
                        pls.get(0);
                List<Tree.Parameter> params = 
                        pl.getParameters();
                List<Tree.PositionalArgument> args = 
                        pal.getPositionalArguments();
                for (int i=0; 
                        i<params.size() &&
                        i<args.size(); 
                        i++) {
                    Tree.PositionalArgument arg = 
                            args.get(i);
                    Tree.Parameter param = 
                            params.get(i);
                    Parameter pmodel = 
                            param.getParameterModel();
                    FunctionOrValue model = 
                            pmodel.getModel();
                    Type type = arg.getTypeModel();
                    if (model==null) {
                        createInferredParameter(fun, null, 
                                param, pmodel, type, null, 
                                true);
                    }
//                    if (!isTypeUnknown(type)
//                            && model instanceof Value 
//                            && ((Value) model).isInferred()) {
//                        model.setType(type);
//                    }
                }
            }
        }
    }

    /**
     * Iterate over the arguments of an invocation
     * looking for anonymous functions with missing
     * parameter lists, and create the parameter
     * lists from the type of the parameters to 
     * which the arguments are assigned.
     */
    private void createAnonymousFunctionParameters(
            Tree.InvocationExpression that) {
        Tree.Primary primary = that.getPrimary();
        Tree.PositionalArgumentList argList =
                that.getPositionalArgumentList();
        if (argList!=null && 
                primary instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) 
                        primary;
            List<Tree.PositionalArgument> args = 
                    argList.getPositionalArguments();
            Declaration dec = mte.getDeclaration();
            if (dec instanceof Functional) {
                Functional fun = (Functional) dec;
                ParameterList paramList = 
                        fun.getFirstParameterList();
                if (paramList!=null) {
                    List<Parameter> params = 
                            paramList.getParameters();
                    for (int i=0, 
                            asz=args.size(), 
                            psz=params.size(); 
                            i<asz && i<psz; i++) {
                        Tree.PositionalArgument arg = 
                                args.get(i);
                        Parameter param = params.get(i);
                        if (arg instanceof Tree.ListedArgument
                                && param!=null) {
                            Tree.ListedArgument larg = 
                                    (Tree.ListedArgument) arg;
                            List<Parameter> fpl = 
                                    inferrableParameters(param);
                            Tree.Expression ex = 
                                    larg.getExpression();
                            if (ex!=null && fpl!=null) {
                                Tree.Term term = ex.getTerm();
                                if (term instanceof Tree.FunctionArgument) {
                                    Tree.FunctionArgument anon =
                                            (Tree.FunctionArgument) 
                                                term;
                                    createAnonymousFunctionParameters(
                                            fpl, term, anon);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Create the parameter list of the given
     * anonymous function with a missing argument
     * list, from the given parameter list of a
     * functional parameter to which it is assigned.
     */
    private void createAnonymousFunctionParameters(
            List<Parameter> parameters, Tree.Term term, 
            Tree.FunctionArgument anon) {
        Function model = anon.getDeclarationModel();
        if (anon.getParameterLists().isEmpty()) {
            Tree.ParameterList pl =
                    new Tree.ParameterList(null);
            ParameterList mpl = new ParameterList();
            for (Parameter parameter: parameters) {
                Tree.InitializerParameter ip = 
                        new Tree.InitializerParameter(null);
                CommonToken token = 
                        new CommonToken(LIDENTIFIER, 
                                parameter.getName());
                token.setStartIndex(term.getStartIndex());
                Token tok = term.getToken();
                token.setLine(tok.getLine());
                token.setCharPositionInLine(
                        tok.getCharPositionInLine());
                Tree.Identifier id = 
                        new Tree.Identifier(token);
                ip.setIdentifier(id);
                pl.addParameter(ip);
                ip.setUnit(unit);
                ip.setScope(model);
                id.setUnit(unit);
                id.setScope(model);
                Parameter mp = new Parameter();
                mp.setDeclaration(model);
                mp.setName(parameter.getName());
                ip.setParameterModel(mp);
                mpl.getParameters().add(mp);
                pl.setModel(mpl);
            }
            pl.setUnit(unit);
            pl.setScope(model);
            model.addParameterList(mpl);
            anon.addParameterList(pl);
        }
    }

    /** 
     * Get the parameters of a callable parameter, or, if
     * the given parameter is a value parameter of type
     * X(Y), a single faked parameter with the name 'it'.
     */
    private List<Parameter> inferrableParameters(Parameter param) {
        FunctionOrValue model = param.getModel();
        if (model instanceof Function) {
            Function fun = (Function) model;
            ParameterList fpl = fun.getFirstParameterList();
            return fpl==null ? null : fpl.getParameters();
        }
        else if (model instanceof Value) {
            Type type = param.getType();
            if (type!=null) {
                Type callable = 
                        intersectionType(type.resolveAliases(), 
                                appliedType(
                                    unit.getCallableDeclaration(), 
                                    unit.getAnythingType(), 
                                    unit.getNothingType()),
                                unit);
                if (callable.isCallable()) { 
                    Type tup = unit.getCallableTuple(callable);
                    int min = unit.getTupleMinimumLength(tup);
                    int max = unit.getTupleMaximumLength(tup);
                    if (min==1 || max==1) {
                        Parameter p = new Parameter();
                        p.setName("it");
                        return Collections.<Parameter>singletonList(p);
                    }
                    else if (min==0) {
                        return Collections.<Parameter>emptyList();
                    }
                }
            }
            return null;
        }
        else {
            return null;
        }
    }

    /**
     * Infer parameter types for anonymous functions in a
     * positional argument list, and set up references from
     * arguments back to parameter models. 
     */
    private boolean[] inferParameterTypes(Tree.Primary p,
            Tree.PositionalArgumentList pal, boolean error) {
        Type type = p.getTypeModel();
        Tree.Term term = unwrapExpressionUntilTerm(p);
        if (term instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) 
                        term;
            Declaration dec = mte.getDeclaration();
            if (dec==null || dec.isDynamic()) {
                if (type==null || type.isUnknown()) {
                    inferDynamicParameters(pal);
                }
            }
            else if (dec instanceof Functional) {
                setupTargetParametersDirectly(pal, 
                        mte, dec, error);
                return inferParameterTypesDirectly(pal, 
                        mte, dec, error);
            }
            else if (dec instanceof Value) {
                Value value = (Value) dec;
                Type valueType = value.getType();
                if (valueType!=null) {
                    if (valueType.isTypeConstructor()) {
                        return inferParameterTypesIndirectly(pal,
                                getInvokedProducedReference(
                                        valueType.getDeclaration(), 
                                        mte)
                                    .getType(), error);
                    }
                    else {
                        return inferParameterTypesIndirectly(pal,
                                valueType, error);
                    }
                }
            }
        }
        else {
            if (type==null || type.isUnknown()) {
                inferDynamicParameters(pal);
            }
            else {
                return inferParameterTypesIndirectly(pal, 
                        type, error);
            }
        }
        int size = pal.getPositionalArguments().size();
        return new boolean[size];
    }

    private void inferDynamicParameters(
            Tree.PositionalArgumentList pal) {
        if (dynamic) {
            for (Tree.PositionalArgument pa: 
                    pal.getPositionalArguments()) {
                if (pa instanceof Tree.ListedArgument) {
                    Tree.ListedArgument arg = 
                            (Tree.ListedArgument) pa;
                    Tree.Expression e = arg.getExpression();
                    if (e!=null) {
                        Tree.Term et = e.getTerm();
                        if (et instanceof Tree.FunctionArgument) {
                            Tree.FunctionArgument anon =
                                    (Tree.FunctionArgument) 
                                        et;
                            for (Tree.ParameterList pl: 
                                    anon.getParameterLists()) {
                                for (Tree.Parameter pm: 
                                        pl.getParameters()) {
                                    if (isInferrableParameter(pm)) {
                                        createInferredDynamicParameter(
                                                anon.getDeclarationModel(), 
                                                pm.getParameterModel());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void createInferredDynamicParameter(Function m, Parameter pm) {
        FunctionOrValue model = pm.getModel();
        if (model==null) {
            Value value = (Value) model; 
            value = new Value();
            value.setUnit(unit);
            value.setName(pm.getName());
            pm.setModel(value);
            value.setContainer(m);
            value.setScope(m);
            m.addMember(value);
            value.setType(unit.getUnknownType());
//            value.setDynamic(true);
            value.setDynamicallyTyped(true);
            value.setInferred(true);
            value.setInitializerParameter(pm);
        }
    }

    /**
     * Infer parameter types for anonymous functions in a
     * named argument list, and set up references from
     * arguments back to parameter models. 
     */
    private void inferParameterTypes(Tree.Primary p,
            Tree.NamedArgumentList nal) {
        Tree.Term term = unwrapExpressionUntilTerm(p);
        if (term instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) term;
            Declaration dec = mte.getDeclaration();
            if (dec instanceof Functional) {
                inferParameterTypesDirectly(nal, mte, dec);
            }
        }
    }

    /**
     * Infer parameter types of anonymous function arguments
     * in an indirect invocation with positional arguments.
     */
    private boolean[] inferParameterTypesIndirectly(
            Tree.PositionalArgumentList pal,
            Type type, boolean error) {
        List<Tree.PositionalArgument> args = 
                pal.getPositionalArguments();
        int argCount = args.size();
        boolean[] delayed = new boolean[argCount];
        if (type!=null 
                && !type.isTypeConstructor()
                && unit.isCallableType(type)) {
            List<Type> paramTypes = 
                    unit.getCallableArgumentTypes(type);
            int paramsSize = paramTypes.size();
            for (int i=0; i<paramsSize && i<argCount; i++) {
                Type paramType = paramTypes.get(i);
                paramType = callableFromUnion(paramType);
                Tree.PositionalArgument arg = args.get(i);
                if (arg instanceof Tree.ListedArgument &&
                        unit.isCallableType(paramType)) {
                    Tree.ListedArgument la = 
                            (Tree.ListedArgument) arg;
                    Tree.Expression e = la.getExpression();
                    if (e!=null) {
                        Tree.Term term = 
                                unwrapExpressionUntilTerm(
                                        e.getTerm());
                        if (term instanceof Tree.FunctionArgument) {
                            Tree.FunctionArgument fa = 
                                    (Tree.FunctionArgument) term;
                            delayed[i] =
                                !inferParameterTypesFromCallableType(
                                        paramType, null, fa, error);
                        }
                        else if (term instanceof Tree.StaticMemberOrTypeExpression) {
                            Tree.StaticMemberOrTypeExpression smte = 
                                    (Tree.StaticMemberOrTypeExpression) 
                                        term;
                            smte.setParameterType(paramType);
                        }
                    }
                }
            }
        }
        return delayed;
    }
    
    /**
     * Infer parameter types of anonymous function arguments
     * in a direct invocation with positional arguments.
     */
    private boolean[] inferParameterTypesDirectly(
            Tree.PositionalArgumentList pal,
            Tree.MemberOrTypeExpression mte,
            Declaration dec, boolean error) {
        
        List<Tree.PositionalArgument> args = 
                pal.getPositionalArguments();
        int argCount = args.size();
        boolean[] delayed = new boolean[argCount];
        
        Functional fun = (Functional) dec;
        List<ParameterList> pls = 
                fun.getParameterLists();
        if (!pls.isEmpty()) {
            List<Parameter> params = 
                    pls.get(0).getParameters();
            Reference reference = 
                    getInvokedProducedReference(dec, mte);
            int paramsSize = params.size();
            for (int i=0, j=0; 
                    i<argCount && j<paramsSize; 
                    i++) {
                Parameter param = params.get(j);
                Tree.PositionalArgument arg = args.get(i);
                arg.setParameter(param);
                if (arg instanceof Tree.ListedArgument) {
                    Tree.ListedArgument la = 
                            (Tree.ListedArgument) 
                                arg;
                    delayed[i] =
                        !inferParameterTypes(
                                reference, 
                                param, 
                                la.getExpression(), 
                                param.isSequenced(),
                                error);
                }
                if (!param.isSequenced()) {
                    j++;
                }
            }
        }
        
        return delayed;
    }
    
    /**
     * Sets references from arguments back to parameters
     * in a direct invocation with positional arguments,
     * special casing "coercion points" generated by the
     * model loader.
     */
    private void setupTargetParametersDirectly(
            Tree.PositionalArgumentList pal,
            Tree.MemberOrTypeExpression mte,
            Declaration dec, boolean error) {
        
        //because the original declaration takes
        //precedence over a coercion point in the 
        //member lookup algorithm, but isn't very
        //useful for function reference type arg 
        //inference, look up the associated 
        //coercion point and use that instead
        if (!error
                && isOverloadedVersion(dec)
                && !dec.isCoercionPoint()) {
            Declaration abstraction = 
                    dec.getContainer()
                        .getDirectMember(dec.getName(), 
                                null, false);
            if (abstraction.isAbstraction()) {
                List<Declaration> overloads = 
                        abstraction.getOverloads();
                if (overloads.size()==2) {
                    for (Declaration overload: overloads) {
                        if (overload.isCoercionPoint()) {
                            dec = overload;
                            break;
                        }
                    }
                }
            }
        }
        
        List<Tree.PositionalArgument> args = 
                pal.getPositionalArguments();
        int argCount = args.size();
        
        Functional fun = (Functional) dec;
        List<ParameterList> pls = 
                fun.getParameterLists();
        if (!pls.isEmpty()) {
            List<Parameter> params = 
                    pls.get(0).getParameters();
            Reference reference = 
                    getInvokedProducedReference(dec, mte);
            int paramsSize = params.size();
            for (int i=0, j=0; 
                    i<argCount && j<paramsSize; 
                    i++) {
                Parameter param = params.get(j);
                Tree.PositionalArgument arg = args.get(i);
                arg.setParameter(param);
                if (arg instanceof Tree.ListedArgument) {
                    Tree.ListedArgument la = 
                            (Tree.ListedArgument) 
                                arg;
                    setupTargetParameters(
                            reference, 
                            param, 
                            la.getExpression());
                }
                if (!param.isSequenced()) {
                    j++;
                }
            }
        }
        
    }

    /**
     * Infer parameter types of anonymous function arguments
     * in a direct invocation with named arguments.
     * 
     * Also sets references from arguments back to parameters
     * by side effect.
     */
    private void inferParameterTypesDirectly(
            Tree.NamedArgumentList nal,
            Tree.MemberOrTypeExpression mte,
            Declaration dec) {
        Reference reference = 
                getInvokedProducedReference(dec, mte);
        Functional fun = (Functional) dec;
        List<ParameterList> pls = 
                fun.getParameterLists();
        if (!pls.isEmpty()) {
            Set<Parameter> foundParameters = 
                    new HashSet<Parameter>();
            ParameterList pl = pls.get(0);
            List<Tree.NamedArgument> args = 
                    nal.getNamedArguments();
            for (int i=0; i<args.size(); i++) {
                Tree.NamedArgument arg = args.get(i);
                Parameter param = 
                        getMatchingParameter(pl, arg, 
                                foundParameters);
                if (param!=null) {
                    foundParameters.add(param);
                    arg.setParameter(param);
                    if (arg instanceof Tree.SpecifiedArgument) {
                        Tree.SpecifiedArgument sa = 
                                (Tree.SpecifiedArgument) 
                                    arg;
                        Tree.SpecifierExpression se = 
                                sa.getSpecifierExpression();
                        if (se!=null) {
                            setupTargetParameters(reference, 
                                    param, se.getExpression());
                            inferParameterTypes(reference, 
                                    param, se.getExpression(), 
                                    false, true);
                        }
                    }
                }
            }
            Tree.SequencedArgument sa = 
                    nal.getSequencedArgument();
            if (sa!=null) {
                Parameter param = 
                        getUnspecifiedParameter(reference, 
                                pl, foundParameters);
                if (param!=null) {
                    sa.setParameter(param);
                    for (Tree.PositionalArgument pa: 
                            sa.getPositionalArguments()) {
                        if (pa instanceof Tree.ListedArgument) {
                            Tree.ListedArgument la = 
                                    (Tree.ListedArgument) pa;
                            la.setParameter(param);
                            setupTargetParameters(reference, param, 
                                    la.getExpression());
                            inferParameterTypes(reference, param, 
                                    la.getExpression(), 
                                    true, true);
                        }
                    }
                }
            }
        }
    }

    private Reference getInvokedProducedReference(
            Declaration dec, 
            Tree.MemberOrTypeExpression mte) {
        Tree.TypeArguments tas;
        if (mte instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression smte = 
                    (Tree.StaticMemberOrTypeExpression) 
                        mte;
            tas = smte.getTypeArguments();
        }
        else {
            tas = null;
        }
        List<TypeParameter> tps = dec.getTypeParameters();
        if (isReallyQualified(mte)) {
            Tree.QualifiedMemberOrTypeExpression qmte = 
                    (Tree.QualifiedMemberOrTypeExpression) 
                        mte;
            Type invokedType = 
                    qmte.getPrimary()
                        .getTypeModel();
            if (invokedType!=null) {
                invokedType = invokedType.resolveAliases();
            }
            Type receiverType = unwrap(invokedType, qmte);
            return receiverType.getTypedReference(dec,
                    getTypeArguments(tas, receiverType, tps));
        }
        else {
            Type receiverType = 
                    mte.getScope()
                        .getDeclaringType(dec);
            return dec.appliedReference(receiverType,
                    getTypeArguments(tas, receiverType, tps));
        }
    }

    private static boolean isReallyQualified(
            Tree.MemberOrTypeExpression mte) {
        if (mte instanceof Tree.QualifiedMemberOrTypeExpression) {
            Tree.QualifiedMemberOrTypeExpression qmte = 
                    (Tree.QualifiedMemberOrTypeExpression) 
                        mte;
            Tree.Primary primary = qmte.getPrimary();
            return !(primary instanceof Tree.Package);
        }
        else {
            return false;
        }
    }

    /**
     * Infer parameter types for an anonymous function.
     */
    private boolean inferParameterTypes(Reference pr, 
            Parameter param, Tree.Expression e, 
            boolean variadic, boolean error) {
        FunctionOrValue model = param.getModel();
        if (e!=null && model!=null) {
            Tree.Term term = 
                    unwrapExpressionUntilTerm(e.getTerm());
            TypedReference tpr = 
                    pr.getTypedParameter(param);
            if (term instanceof Tree.InvocationExpression) {
                Tree.InvocationExpression ie = 
                        (Tree.InvocationExpression) term;
                Tree.PositionalArgumentList pal = 
                        ie.getPositionalArgumentList();
                Tree.NamedArgumentList nal = 
                        ie.getNamedArgumentList();
                if (pal!=null && pal.getPositionalArguments().isEmpty() 
                 || nal!=null && nal.getNamedArguments().isEmpty()) {
                    term = ie.getPrimary();
                }
            }
            if (term instanceof Tree.FunctionArgument) {
                Tree.FunctionArgument anon = 
                        (Tree.FunctionArgument) term;
                if (model instanceof Functional) {
                    //NOTE: this branch is basically redundant
                    //      and could be removed
                    return inferParameterTypesFromCallableParameter(
                            pr, param, anon, error);
                }
                else { 
                    Type paramType = tpr.getFullType();
                    if (variadic) {
                        paramType = 
                                unit.getIteratedType(paramType);
                    }
                    paramType = callableFromUnion(paramType);
                    if (unit.isCallableType(paramType)) {
                        return inferParameterTypesFromCallableType(
                                paramType, param, anon, error);
                    }
                }
            }
        }
        return true;
    }

    /**
     * Set the "target parameter" for a reference, which 
     * will be used later for inferring type arguments for 
     * a function ref or generic function ref.
     */
    private void setupTargetParameters(Reference pr, 
            Parameter param, Tree.Expression e) {
        if (e!=null) {
            Tree.Term term = 
                    unwrapExpressionUntilTerm(e.getTerm());
            TypedReference tpr = 
                    pr.getTypedParameter(param);
            if (term instanceof Tree.InvocationExpression) {
                Tree.InvocationExpression ie = 
                        (Tree.InvocationExpression) term;
                Tree.PositionalArgumentList pal = 
                        ie.getPositionalArgumentList();
                Tree.NamedArgumentList nal = 
                        ie.getNamedArgumentList();
                if (pal!=null && pal.getPositionalArguments().isEmpty() 
                 || nal!=null && nal.getNamedArguments().isEmpty()) {
                    term = ie.getPrimary();
                }
            }
            if (term instanceof Tree.StaticMemberOrTypeExpression) {
                //the "target parameter" is used later to
                //infer type arguments for function refs
                //and generic function refs
                Tree.StaticMemberOrTypeExpression stme = 
                        (Tree.StaticMemberOrTypeExpression) 
                            term;
                if (stme instanceof Tree.QualifiedMemberOrTypeExpression &&
                        stme.getStaticMethodReference()) {
                    Tree.QualifiedMemberOrTypeExpression qmte = 
                            (Tree.QualifiedMemberOrTypeExpression) 
                                stme;
                    Tree.StaticMemberOrTypeExpression ote = 
                            (Tree.StaticMemberOrTypeExpression) 
                                qmte.getPrimary();
                    ote.setTargetParameter(tpr);
                    stme.setParameterType(tpr.getFullType());
                }
                else {
                    stme.setTargetParameter(tpr);
                    stme.setParameterType(tpr.getFullType());
                }
            }
        }
    }
    
    private Type callableFromUnion(Type paramType) {
        if (paramType==null) {
            return null;
        }
        if (paramType.getDeclaration()
                    .isAlias()) {
            //TODO: this is a bit too aggressive, 
            //      we don't really need to resolve
            //      aliases in type arguments
            paramType = paramType.resolveAliases();
        }
        if (paramType.isUnion()) {
            List<Type> cts = paramType.getCaseTypes();
            List<Type> list = 
                    new ArrayList<Type>
                        (cts.size());
            for (Type ct: cts) {
                if (unit.isCallableType(ct)) {
                    list.add(ct);
                }
            }
            return union(list, unit);
        }
        else {
            return paramType;
        }
    }
    
    private boolean inferParameterTypesFromCallableType(
            Type paramType, Parameter param, 
            Tree.FunctionArgument anon, boolean error) {
        List<Tree.ParameterList> apls = 
                anon.getParameterLists();
        boolean result = true;
        if (!apls.isEmpty()) {
            List<Type> types = 
                    unit.getCallableArgumentTypes(paramType);
            List<Tree.Parameter> aps = 
                    apls.get(0)
                        .getParameters();
            Declaration declaration = param==null ? 
                    null : param.getDeclaration();
            for (int j=0; 
                    j<types.size() && 
                    j<aps.size(); 
                    j++) {
                Tree.Parameter ap = aps.get(j);
                if (isInferrableParameter(ap)) {
                    result = result & 
                        createInferredParameter(anon,
                            declaration, ap,
                            ap.getParameterModel(),
                            types.get(j),
                            param==null ? null : 
                                param.getModel(),
                            error);
                }
            }
        }
        return result;
    }

    private boolean inferParameterTypesFromCallableParameter(
            Reference pr, Parameter param, 
            Tree.FunctionArgument anon, boolean error) {
        boolean result = true;
        Declaration declaration = param.getDeclaration();
        Functional fun = (Functional) param.getModel();
        List<ParameterList> fpls = fun.getParameterLists();
        List<Tree.ParameterList> apls = 
                anon.getParameterLists();
        if (!fpls.isEmpty() && !apls.isEmpty()) {
            List<Parameter> fps = 
                    fpls.get(0)
                        .getParameters();
            List<Tree.Parameter> aps = 
                    apls.get(0)
                        .getParameters();
            for (int j=0; 
                    j<fps.size() && j<aps.size(); 
                    j++) {
                Parameter fp = fps.get(j);
                Tree.Parameter ap = aps.get(j);
                if (isInferrableParameter(ap)) {
                    result = result &
                        createInferredParameter(anon,
                                declaration, ap,
                                ap.getParameterModel(),
                                pr.getTypedParameter(fp)
                                    .getType(),
                                fp.getModel(),
                                error);
                }
            }
        }
        return result;
    }

    private static boolean isInferrableParameter(Tree.Parameter p) {
        if (p instanceof Tree.ValueParameterDeclaration) {
            Tree.ValueParameterDeclaration vpd =
                    (Tree.ValueParameterDeclaration) p;
            return vpd.getTypedDeclaration().getType()
                    instanceof Tree.ValueModifier;
        }
        else if (p instanceof Tree.InitializerParameter) {
            //TODO: is this change OK??
//            return p.getParameterModel().getModel() == null;
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Create a model for an inferred parameter of an
     * anonymous function.
     */
    private boolean createInferredParameter(
            Tree.FunctionArgument anon,
            Declaration declaration, Tree.Parameter ap,
            Parameter parameter, Type type,
            FunctionOrValue original, boolean error) {
        if (isTypeUnknown(type)) {
            type = unit.getUnknownType();
            if (!dynamic) {
                if (error) {
                    ap.addError("could not infer parameter type: '" 
                            + parameter.getName() 
                            + "' would have unknown type");
                }
                else {
                    return false;
                }
            }
        }
        else if (involvesTypeParams(declaration, type)) {
            if (error) {
                type = unit.getUnknownType();
                ap.addError("could not infer parameter type: '" 
                        + parameter.getName() 
                        + "' would have type '" 
                        + type.asString(unit) 
                        + "' involving type parameters");
            }
            else {
                return false;
            }
        }
        Value model = (Value) parameter.getModel(); 
        if (model==null) {
            model = new Value();
            model.setUnit(unit);
            model.setName(parameter.getName());
            model.setOriginalParameterDeclaration(original);
            parameter.setModel(model);
            Function m = anon.getDeclarationModel();
            model.setContainer(m);
            model.setScope(m);
            m.addMember(model);
        }
        model.setType(type);
        model.setInferred(true);
        if (declaration!=null && type!=null
                && declaration.isJava()
                && !type.isUnknown()
                && type.isSubtypeOf(unit.getObjectType())
                && canHaveUncheckedNulls(type)) {
            model.setUncheckedNullType(true);
        }
        model.setInitializerParameter(parameter);
        if (ap instanceof Tree.ValueParameterDeclaration) {
            Tree.ValueParameterDeclaration vpd =
                (Tree.ValueParameterDeclaration) ap;
            vpd.getTypedDeclaration()
                .getType()
                .setTypeModel(type);
        }
        return true;
    }
    
    private void visitInvocationPositionalArgs(
            Tree.InvocationExpression that) {
        Tree.Primary p = that.getPrimary();
        Tree.PositionalArgumentList pal = 
                that.getPositionalArgumentList();
        if (pal!=null && 
                p instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) p;
            //set up the "signature" on the primary
            //so that we can resolve the correct 
            //overloaded declaration
            List<Tree.PositionalArgument> args = 
                    pal.getPositionalArguments();
            List<Type> signature = 
                    new ArrayList<Type>
                        (args.size());
            boolean spread = false;
            for (int i=0, s=args.size(); i<s; i++) {
                Tree.PositionalArgument pa = args.get(i);
                Type t = pa.getTypeModel();
                Type pat = unit.denotableType(t);
                if (pa instanceof Tree.ListedArgument) {
                    if (isTypeUnknown(pat)) {
                        Tree.ListedArgument la = 
                                (Tree.ListedArgument) pa;
                        Tree.Expression ex = 
                                la.getExpression();
                        if (ex!=null) {
                            Tree.Term arg = ex.getTerm();
                            if (arg instanceof 
                                    Tree.ObjectExpression) {
                                Tree.ObjectExpression obj =
                                        (Tree.ObjectExpression)
                                            arg;
                                pat = unit.denotableType(
                                        obj.getAnonymousClass()
                                            .getType());
                            }
                            else if (arg instanceof 
                                    Tree.FunctionArgument) {
                                Tree.FunctionArgument fun = 
                                        (Tree.FunctionArgument) 
                                            arg;
                                pat = getCallableBottomType(
                                        countParameters(fun));
                            }
                            else if (arg instanceof 
                                    Tree.BaseTypeExpression) {
                                //must be a class instantiation
                                pat = getCallableBottomType();
                            }
                            else if (arg instanceof
                                    Tree.NaturalLiteral) {
                                pat = unit.getIntegerType();
                            }
                            else if (arg instanceof
                                    Tree.FloatLiteral) {
                                pat = unit.getFloatType();
                            }
                            else if (arg instanceof
                                    Tree.StringLiteral) {
                                pat = unit.getStringType();
                            }
                            else if (arg instanceof
                                    Tree.CharLiteral) {
                                pat = unit.getCharacterType();
                            }
                        }
                    }
                    signature.add(pat);
                }
                else if (pat!=null) {
                    if (pa instanceof Tree.SpreadArgument) {
                        if (unit.isTupleType(pat)) {
                            signature.addAll(unit.getTupleElementTypes(pat));
                            spread = unit.isTupleLengthUnbounded(pat);
                        }
                        else {
                            signature.add(pat);
                            spread = true;
                        }
                    }
                    else if (pa instanceof Tree.Comprehension) {
                        signature.add(unit.getSequentialType(pat));
                        spread = true;
                    }
                }
            }
            mte.setSignature(signature);
            mte.setEllipsis(spread);
        }
    }

    private int countParameters(Tree.FunctionArgument fun) {
        List<Tree.ParameterList> parameterLists = 
                fun.getParameterLists();
        if (parameterLists.isEmpty()) {
            return -1;
        }
        else {
            return parameterLists.get(0)
                    .getParameters()
                    .size();
        }
    }

    private Type getCallableBottomType() {
        return appliedType(
                unit.getCallableDeclaration(), 
                unit.getNothingType(),
                unit.getSequentialType(unit.getAnythingType()));
    }

    private Type getCallableBottomType(int size) {
        Type paramListType;
        Type nothingType = unit.getNothingType();
        Type anythingType = unit.getAnythingType();
        if (size<0) {
            paramListType = anythingType;
        }
        else {
            paramListType = unit.getEmptyType();
            Class tuple = 
                    unit.getTupleDeclaration();
            for (int i=0; i<size; i++) {
                paramListType = 
                        appliedType(tuple,
                                anythingType, 
                                anythingType,
                                paramListType);
            }
        }
        Interface callable = 
                unit.getCallableDeclaration();
        return appliedType(callable, 
                nothingType, paramListType);
    }
    
    private void checkSuperInvocation(
            Tree.MemberOrTypeExpression qmte,
            List<Type> signature, boolean spread) {
        Declaration member = qmte.getDeclaration();
        if (member!=null) {
            String name = member.getName();
            TypeDeclaration type = 
                    (TypeDeclaration) 
                        member.getContainer();
            if (member.isFormal() && !inExtendsClause) {
                qmte.addError("supertype member is declared formal: '" + 
                        name + "' of '" + 
                        type.getName() + "'");
            }
            else {
                Scope scope = qmte.getScope();
                ClassOrInterface ci = 
                        getContainingClassOrInterface(scope);
                if (ci!=null) {
                    List<TypeDeclaration> refiners = 
                            new ArrayList<TypeDeclaration>(1);
                    Type et = ci.getExtendedType();
                    if (et!=null) {
                        Declaration etm = 
                                et.getDeclaration()
                                    .getMember(name, 
                                            signature, 
                                            spread);
                        if (etm!=null && 
                                !etm.getContainer()
                                    .equals(type) && 
                                etm.refines(member)) {
                            TypeDeclaration container = 
                                    (TypeDeclaration) 
                                        etm.getContainer();
                            refiners.add(container);
                        }
                    }
                    for (Type st: ci.getSatisfiedTypes()) {
                        Declaration stm = 
                                st.getDeclaration()
                                    .getMember(name, 
                                            signature, 
                                            spread);
                        if (stm!=null && 
                                !stm.getContainer()
                                    .equals(type) && 
                                stm.refines(member)) {
                            TypeDeclaration container = 
                                    (TypeDeclaration) 
                                        stm.getContainer();
                            refiners.add(container);
                        }
                    }
                    if (refiners.size()==1) {
                        TypeDeclaration r = refiners.get(0);
                        if (r instanceof Class) {
                            qmte.addError("inherited member is refined by intervening superclass: '" + 
                                    r.getName() + "' refines '" +
                                    name + "' declared by '" + 
                                    type.getName() + "'");
                        }
                        else {
                            qmte.addError("inherited member is refined by intervening superinterface: '" + 
                                    r.getName() + "' refines '" +
                                    name + "' declared by '" + 
                                    type.getName() + "'");
                        }
                    }
                    else if (!refiners.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (TypeDeclaration r: refiners) {
                            if (sb.length()>0) {
                                sb.append(", ");
                            }
                            sb.append('\'')
                              .append(r.getName())
                              .append('\'');
                        }
                        qmte.addError("inherited member is refined by intervening supertypes: '" + 
                                name + "' declared by '" + 
                                type.getName() + 
                                "' is refined by intervening types " + sb);
                    }
                }
            }
        }
    }
    
    private void visitInvocationPrimary(
            Tree.InvocationExpression that) {
        Tree.Term primary = 
                unwrapExpressionUntilTerm(that.getPrimary());
        if (primary instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression pmte = 
                    (Tree.StaticMemberOrTypeExpression) 
                        primary;
            visitInvocationPrimary(that, pmte);
            
        }
        else if (primary instanceof Tree.ExtendedTypeExpression) {
            Tree.ExtendedTypeExpression ete = 
                    (Tree.ExtendedTypeExpression) 
                        primary;
            visitExtendedTypePrimary(ete);
        }
        else {
            visitInvocationPrimary(that, primary);
        }
    }

    private void visitInvocationPrimary(
            Tree.InvocationExpression that, 
            Tree.Term term) {
        if (term!=null) {
            Type type = term.getTypeModel();
            if (type!=null) {
                type = type.resolveAliases();
                if (type.isTypeConstructor()) {
                    List<Type> typeArgs = 
                            getOrInferTypeArgumentsForTypeConstructor(
                                    that, null, type, null);
                    checkTypeArgumentsOfTypeConstructor(
                            typeArgs, null, type, term);
                    Type fullType =
                            accountForGenericFunctionRef(true, 
                                    null, null, typeArgs, type);
                    term.setTypeModel(fullType);
                }
            }
        }
    }

    private void visitInvocationPrimary(
            Tree.InvocationExpression that,
            Tree.StaticMemberOrTypeExpression reference) {
        
        if (reference instanceof Tree.QualifiedMemberOrTypeExpression) {
            Tree.QualifiedMemberOrTypeExpression qmte = 
                    (Tree.QualifiedMemberOrTypeExpression) 
                        reference;
            Tree.Term term = 
                    unwrapExpressionUntilTerm(qmte.getPrimary());
            if (term instanceof Tree.StaticMemberOrTypeExpression) {
                Tree.StaticMemberOrTypeExpression pmte = 
                        (Tree.StaticMemberOrTypeExpression) 
                            term;
                visitInvocationPrimary(that, pmte);
            }
        }
        
        Tree.TypeArguments tas = 
                reference.getTypeArguments();
        if (reference instanceof Tree.BaseTypeExpression) {
            Tree.BaseTypeExpression bte = 
                    (Tree.BaseTypeExpression) 
                        reference;
            TypeDeclaration type = 
                    resolveBaseTypeExpression(bte, true);
            if (type!=null) {
                setArgumentParameters(that, type);
                Type receivingType = 
                        getBaseReceivingType(that, type);
                List<Type> typeArgs = 
                        getOrInferTypeArguments(that, type, 
                                reference, receivingType);
                if (typeArgs!=null) {
                    tas.setTypeModels(typeArgs);
                }
                else {
                    typeArgs = tas.getTypeModels();
                }
                visitBaseTypeExpression(bte, type, typeArgs, 
                        tas, receivingType, true);
            }
        }
        
        else if (reference instanceof Tree.QualifiedTypeExpression) {
            Tree.QualifiedTypeExpression qte = 
                    (Tree.QualifiedTypeExpression) 
                        reference;
            TypeDeclaration type = 
                    resolveQualifiedTypeExpression(qte, true);
            if (type!=null) {
                setArgumentParameters(that, type);
                Type receivingType = getReceivingType(qte);
                List<Type> typeArgs = 
                        getOrInferTypeArguments(that, type, 
                                reference, 
                                unwrap(receivingType, qte));
                if (typeArgs!=null) {
                    tas.setTypeModels(typeArgs);
                }
                else {
                    typeArgs = tas.getTypeModels();
                }
                Tree.Primary primary = qte.getPrimary();
                if (primary instanceof Tree.Package) {
                    visitBaseTypeExpression(qte, type, 
                            typeArgs, tas, null, true);
                }
                else {
                    visitQualifiedTypeExpression(qte, 
                            receivingType, type, typeArgs, 
                            tas, true);
                }
            }
        }
        
        else if (reference instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression bme = 
                    (Tree.BaseMemberExpression) 
                        reference;
            TypedDeclaration base = 
                    resolveBaseMemberExpression(bme, true);
            if (base!=null) {
                setArgumentParameters(that, base);
                Type receivingType = 
                        getBaseReceivingType(that, base);
                List<Type> typeArgs = 
                        getOrInferTypeArguments(that, base, 
                                reference, receivingType);
                if (typeArgs!=null) {
                    tas.setTypeModels(typeArgs);
                }
                else {
                    typeArgs = tas.getTypeModels();
                }
                visitBaseMemberExpression(bme, base, 
                        typeArgs, tas, receivingType,
                        true);
            }
        }
        
        else if (reference instanceof Tree.QualifiedMemberExpression) {
            Tree.QualifiedMemberExpression qme = 
                    (Tree.QualifiedMemberExpression) 
                        reference;
            TypedDeclaration member = 
                    resolveQualifiedMemberExpression(qme, 
                            true);
            if (member!=null) {
                setArgumentParameters(that, member);
                Type receivingType = getReceivingType(qme);
                List<Type> typeArgs = 
                        getOrInferTypeArguments(that, 
                                member, reference, 
                                unwrap(receivingType, qme));
                if (typeArgs!=null) {
                    tas.setTypeModels(typeArgs);
                }
                else {
                    typeArgs = tas.getTypeModels();
                }
                Tree.Primary primary = qme.getPrimary();
                if (primary instanceof Tree.Package) {
                    visitBaseMemberExpression(qme, 
                            member, typeArgs, tas, null,
                            true);
                }
                else {
                    visitQualifiedMemberExpression(qme, 
                            receivingType, member, typeArgs, 
                            tas, true);
                }
            }
        }
    }

    protected Type getBaseReceivingType(
            Tree.InvocationExpression that,
            Declaration dec) {
        Scope scope = that.getScope();
        if (dec.isClassOrInterfaceMember() &&
                !dec.isStatic() &&
                !dec.isDefinedInScope(scope)) {
            ClassOrInterface ci = 
                    (ClassOrInterface) 
                        dec.getContainer();
            Type qualifyingType = 
                    scope.getDeclaringType(dec);
            List<Type> inferredArgs = 
                    new TypeArgumentInference(unit)
                        .getInferredTypeArgsForReference(
                                that, dec, ci,
                                qualifyingType);
            return ci.appliedType(null, inferredArgs);
        }
        else {
            return null;
        }
    }

    private Type getReceivingType(
            Tree.QualifiedMemberOrTypeExpression reference) {
        Tree.Primary primary = reference.getPrimary();
        if (primary instanceof Tree.Package) {
            return null;
        }
        else {
            return primary.getTypeModel()
                    .resolveAliases();
        }
    }
    
    /**
     * Set up references from positional arguments to
     * parameters for later use during type inference. This 
     * is only necessary here because in the case of an 
     * overloaded Java function or constructors, the 
     * overload has not been resolved the first time we 
     * visit the arguments. So we need to revisit them here
     * with the fully-resolved overloaded version. 
     * 
     * @param that an invocation
     * @param invoked the thing being invoked
     */
    private void setArgumentParameters(
            Tree.InvocationExpression that,
            Declaration invoked) {
        if (invoked instanceof Functional) {
            Functional fun = (Functional) invoked;
            List<ParameterList> pls = 
                    fun.getParameterLists();
            if (!pls.isEmpty()) {
                ParameterList pl = pls.get(0);
                //no need to do named arg lists because
                //they can't be used with overloaded decs
                Tree.PositionalArgumentList pal = 
                        that.getPositionalArgumentList();
                if (pal!=null) {
                    List<Tree.PositionalArgument> args = 
                            pal.getPositionalArguments();
                    List<Parameter> params = 
                            pl.getParameters();
                    for (int i=0, j=0; 
                            i<args.size() && 
                            j<params.size();
                            i++) {
                        Tree.PositionalArgument arg = 
                                args.get(i);
                        Parameter param = 
                                params.get(j);
                        if (arg!=null && param!=null) {
                            arg.setParameter(param);
                        }
                        if (param==null ||
                                !param.isSequenced()) {
                            j++;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Get the explicitly specified or inferred type 
     * arguments of a reference that occurs within the
     * primary of an invocation expression.
     * 
     * @param that the invocation
     * @param dec the thing with type parameters
     * @param reference the reference to the thing with type
     *        parameters
     * @param receiverType the qualifying type
     * 
     * @return the type arguments
     */
    private List<Type> getOrInferTypeArguments(
            Tree.InvocationExpression that, 
            Declaration dec,
            Tree.StaticMemberOrTypeExpression reference, 
            Type receiverType) {
        Tree.TypeArguments tas = 
                reference.getTypeArguments();
        boolean explicit = 
                tas instanceof Tree.TypeArgumentList;
        if (tas.getTypeModels()!=null) {
            return null;
        }
        
        if (dec!=null && dec.isParameterized()) {
            //a generic declaration
            if (explicit) {
                return getTypeArguments(tas, receiverType, 
                        dec.getTypeParameters());
            }
            else {
                List<Type> typeArgs = 
                        getInferredTypeArguments(that, 
                            reference, dec, 
                            receiverType);
                /*if (typeArgs==null) {
                    reference.addError("type arguments could not be inferred: '" +
                            dec.getName(unit) + "' is generic");
                }*/
                return typeArgs;
            }
        }
        else if (dec instanceof Value) {
            //a generic function reference
            Value value = (Value) dec;
            Type type = value.getType();
            if (type!=null) {
                type = type.resolveAliases();
                if (type.isTypeConstructor()) {
                    return getOrInferTypeArgumentsForTypeConstructor(
                            that, receiverType, type, tas);
                }
            }
            return NO_TYPE_ARGS;
        }
        else {
            return NO_TYPE_ARGS;
        }
    }

    /**
     * Get the explicitly specified or inferred type 
     * arguments of a generic function reference that occurs 
     * within the primary of an invocation expression.
     * 
     * @param that the invocation
     * @param receiverType the qualifying type
     * @param type the type constructor
     * @param tas the type argument list
     * @return the type arguments
     */
    private List<Type> getOrInferTypeArgumentsForTypeConstructor(
            Tree.InvocationExpression that, 
            Type receiverType,
            Type type, 
            Tree.TypeArguments tas) {
        TypeDeclaration td = type.getDeclaration();
        List<TypeParameter> typeParameters = 
                td.getTypeParameters();
        boolean explicit = 
                tas instanceof Tree.TypeArgumentList;
        if (explicit) {
            return getTypeArguments(tas, receiverType, 
                    typeParameters);
        }
        else {
            return new TypeArgumentInference(unit)
                    .getInferredTypeArgsForTypeConstructor(
                            that, receiverType, type, 
                            typeParameters);
        }
    }

    /**
     * Infer type arguments for a reference that is invoked
     * or qualifies a reference being invoked.
     * 
     * @param that the invocation
     * @param reference 
     * @param reference the reference to a thing with type
     *        parameters
     * @param generic the model of the thing with type 
     *        parameters
     * @param receiverType 
     *        
     * @return a list of inferred type arguments
     */
    private List<Type> getInferredTypeArguments(
            Tree.InvocationExpression that,
            //this is the reference to the thing we're
            //inferring type arguments for
            Tree.StaticMemberOrTypeExpression reference, 
            Declaration generic, Type receiverType) {
        
        Tree.Term primary =
                unwrapExpressionUntilTerm(that.getPrimary());
        if (primary instanceof Tree.StaticMemberOrTypeExpression) {
            //note: this is the immediate primary of the
            //invocation, not the reference to the thing
            //we're inferring type arguments for
            Tree.StaticMemberOrTypeExpression pmte = 
                    (Tree.StaticMemberOrTypeExpression) 
                        primary;
            if (isStaticReference(pmte)) {
                if (reference==pmte) {
                    //can't infer type arguments for member
                    //ref in static member reference
                    return null;
                }
                else {
                    TypeDeclaration type = 
                            (TypeDeclaration) generic;
                    return new TypeArgumentInference(unit)
                            .getInferredTypeArgsForStaticReference(
                                    that, type, receiverType, pmte);
                }
            }
            else {
                Declaration declaration = 
                        pmte.getDeclaration();
                return new TypeArgumentInference(unit)
                        .getInferredTypeArgsForReference(
                                that, declaration, generic,
                                receiverType);
            }
        }
        else {
            return null;
        }
    }
    
    /**
     * Determine if a reference is really a static reference, 
     * taking into account that it might be something that 
     * looks like a static reference, but really isn't, a 
     * constructor reference, or a reference to a static
     * method in Java. 
     */
    private static boolean isStaticReference(
            Tree.StaticMemberOrTypeExpression reference) {
        if (reference.getStaticMethodReference()) {
            //Note: for a real static method reference
            //      the declaration has not yet been 
            //      resolved at this point (for a 
            //      Constructor it always has been)
            Declaration declaration = 
                    reference.getDeclaration();
            if (declaration==null) {
                return true;
            }
            else if (isConstructor(declaration)) {
                return false;
            }
            else {
                return !declaration.isStatic();
            }
        }
        else {
            return false;
        }
    }
    
    /**
     * Typecheck a direct invocation.
     */
    private void visitDirectInvocation(
            Tree.InvocationExpression that) {
        Tree.Term primary = 
                unwrapExpressionUntilTerm(that.getPrimary());
        if (primary==null) {
            return;
        }
        Tree.MemberOrTypeExpression mte = 
                (Tree.MemberOrTypeExpression) primary;
        Reference prf = mte.getTarget();
        Declaration dec = mte.getDeclaration();
        Functional fun = (Functional) dec;
        if (dec!=null) {
            if (!(primary instanceof Tree.ExtendedTypeExpression)) {
                if (dec instanceof Class) {
                    Class c = (Class) dec;
                    if (c.isAbstract()) {
                        that.addError("abstract class may not be instantiated: '" + 
                                dec.getName(unit) + "'");
                    }
                }
            }
            Tree.NamedArgumentList nal = 
                    that.getNamedArgumentList();
            if (nal!=null && 
                    dec.isAbstraction()) {
                //TODO: this is not really right - it's the fact 
                //      that we're calling Java and don't have
                //      meaningful parameter names that is the
                //      real problem, not the overload
                that.addError("overloaded declarations may not be called using named arguments: '" +
                        dec.getName(unit) + "'");
            }
            //that.setTypeModel(prf.getType());
            Type ct = primary.getTypeModel();
            if (ct!=null) {
                List<Type> tal = ct.getTypeArgumentList();
                if (!tal.isEmpty()) {
                    //pull the return type out of the Callable
                    that.setTypeModel(tal.get(0));
                }
            }
            if (nal!=null) {
                List<ParameterList> parameterLists = 
                        fun.getParameterLists();
                if (!parameterLists.isEmpty()
                        && !parameterLists.get(0)
                            .isNamedParametersSupported()) {
                    that.addError("named invocations of Java methods not supported");
                }
            }
            if (dec.isAbstraction()) {
                //nothing to check the argument types against
                //that.addError("no matching overloaded declaration");
            }
            else {
                //typecheck arguments using the parameter list
                //of the target declaration
                checkInvocationArguments(that, prf, fun);
            }
        }
    }

    /**
     * Typecheck an indirect invocation.
     */
    private void visitIndirectInvocation(
            Tree.InvocationExpression that) {
        Tree.Term primary = 
                unwrapExpressionUntilTerm(that.getPrimary());
        if (primary==null) {
            return;
        }
        Type pt = primary.getTypeModel();
        if (!isTypeUnknown(pt)) {
            
            if (that.getNamedArgumentList()!=null) {
                that.addError("named arguments not supported for indirect invocations");
                return;
            }
            
            Tree.PositionalArgumentList pal = 
                    that.getPositionalArgumentList();
            if (pal==null) {
                return;
            }
            
            if (pt.isNothing()) {
                that.setTypeModel(unit.getNothingType());
            }
            else if (checkCallable(pt, primary, 
                    "invoked expression must be callable")) {
                Interface cd = unit.getCallableDeclaration();
                Type ct = pt.getSupertype(cd);
                if (ct==null) {
                    primary.addError(
                            "invoked expression must be callable: type '" 
                            + pt.asString(unit) + 
                            "' involves a type function");
                    return;
                }
                List<Type> typeArgs = 
                        ct.getTypeArgumentList();
                if (!typeArgs.isEmpty()) {
                    that.setTypeModel(typeArgs.get(0));
                }
                //typecheck arguments using the type args of Callable
                if (typeArgs.size()>=2) {
                    Type paramTypesAsTuple = typeArgs.get(1);
                    if (paramTypesAsTuple!=null) {
                        TypeDeclaration pttd = 
                                paramTypesAsTuple.getDeclaration();
                        if (pttd instanceof ClassOrInterface &&
                                (pttd.isEmpty() || 
                                 pttd.isTuple() || 
                                 pttd.isSequence() || 
                                 pttd.isSequential())) {
                            //we have a plain tuple type so we can check the
                            //arguments individually
                            checkIndirectInvocationArguments(
                                    that, paramTypesAsTuple,
                                    unit.getTupleElementTypes(
                                            paramTypesAsTuple),
                                    unit.isTupleLengthUnbounded(
                                            paramTypesAsTuple),
                                    unit.isTupleVariantAtLeastOne(
                                            paramTypesAsTuple),
                                    unit.getTupleMinimumLength(
                                            paramTypesAsTuple));
                        }
                        else {
                            //we have something exotic, a union of tuple types
                            //or whatever, so just check the whole argument tuple
                            Type tt = 
                                    getTupleType(pal.getPositionalArguments(), 
                                            unit, false);
                            checkAssignable(tt, paramTypesAsTuple, pal,
                                    "argument list type must be assignable to parameter list type");
                        }
                    }                
                }
            }
        }
    }
    
    /**
     * Typecheck the arguments of a direct invocation.
     */
    private void checkInvocationArguments(
            Tree.InvocationExpression that,
            Reference prf, Functional dec) {
        List<ParameterList> pls = dec.getParameterLists();
        if (pls.isEmpty()) {
            /*if (dec instanceof TypeDeclaration) {
                that.addError("type has no parameter list: '" + 
                        dec.getName(unit) + "'");
            }
            else {
                that.addError("function has no parameter list: '" +
                        dec.getName(unit) + "'");
            }*/
        }
        else /*if (!dec.isOverloaded())*/ {
            ParameterList pl = pls.get(0);            
            Tree.PositionalArgumentList args = 
                    that.getPositionalArgumentList();
            if (args!=null) {
                checkPositionalArguments(pl, prf, args, that);
            }
            Tree.NamedArgumentList namedArgs = 
                    that.getNamedArgumentList();
            if (namedArgs!=null) {
                if (pl.isNamedParametersSupported()) {
                    namedArgs.getNamedArgumentList()
                            .setParameterList(pl);
                    checkNamedArguments(pl, prf, namedArgs);
                }
            }
        }
    }
    
    private void checkNamedArguments(ParameterList pl, 
            Reference pr, 
            Tree.NamedArgumentList nal) {
        Set<Parameter> foundParameters = 
                new HashSet<Parameter>();
        
        List<Tree.NamedArgument> na = 
                nal.getNamedArguments();
        for (Tree.NamedArgument a: na) {
            checkNamedArg(a, pl, pr, 
                    foundParameters);
        }
        
        Tree.SequencedArgument sa = 
                nal.getSequencedArgument();
        if (sa!=null) {
            checkSequencedArg(sa, pl, pr, 
                    foundParameters);
        }
        else {
            Parameter param = 
                    getUnspecifiedParameter(pr, 
                            pl, foundParameters);
            if (param!=null && 
                    !unit.isNonemptyIterableType(
                            param.getType())) {
                foundParameters.add(param);
            }
        }
        
        checkForMissingNamedParameters(pl, pr, nal, 
                foundParameters);
    }

    private void checkForMissingNamedParameters(
            ParameterList pl, Reference pr,
            Tree.NamedArgumentList nal, 
            Set<Parameter> foundParameters) {
        StringBuilder missing = null;
        int count = 0;
        for (Parameter p: pl.getParameters()) {
            if (!foundParameters.contains(p) && 
                    !p.isDefaulted() && 
                    (!p.isSequenced() || p.isAtLeastOne())) {
                count++;
                Type type = 
                        pr.getTypedParameter(p)
                            .getFullType();
                if (missing==null) {
                    missing = new StringBuilder();
                }
                else {
                    missing.append(", ");
                }
                if (type!=null) {
                    missing.append(type.asString(unit))
                        .append(" ");
                }
                missing.append(p.getName());
            }
        }
        if (count==1) {
            nal.addError("missing named argument to required parameter '" + 
                    missing + "' of '" + 
                    pr.getDeclaration().getName(unit) + "'",
                    11000);
        }
        else if (count>1) {
            nal.addError("missing named arguments to " + 
                    count + " required parameters '" + 
                    missing + "' of '" + 
                    pr.getDeclaration().getName(unit) + "'",
                    11000);
        }
    }
    
    private void checkSequencedArg(Tree.SequencedArgument sa, 
            ParameterList pl, Reference pr, 
            Set<Parameter> foundParameters) {
        Parameter param = 
                getUnspecifiedParameter(pr, 
                        pl, foundParameters);
        if (param==null) {
            sa.addError("all iterable parameters specified by named argument list: '" + 
                    pr.getDeclaration().getName(unit) +
                    "' does not declare any additional parameters of type 'Iterable'");
        }
        else {
            if (!foundParameters.add(param)) {
                sa.addError("duplicate argument for parameter: '" +
                        param.getName() + "' of '" + 
                        pr.getDeclaration().getName(unit) + "'");
            }
            else if (!dynamic &&
                    isTypeUnknown(param.getType())) {
                sa.addError("parameter type could not be determined: " + 
                        paramdesc(param) +
                        getTypeUnknownError(param.getType()));
            }
            checkSequencedArgument(sa, pr, param);
        }
    }

    private void checkNamedArg(Tree.NamedArgument a, 
            ParameterList pl, Reference pr, 
            Set<Parameter> foundParameters) {
        Parameter param = 
                getMatchingParameter(pl, a, 
                        foundParameters);
        if (param==null) {
            if (a.getIdentifier()==null) {
                a.addError("all parameters specified by named argument list: '" + 
                        pr.getDeclaration().getName(unit) +
                        "' does not declare any additional parameters");
            }
            else {
                a.addError("no matching parameter for named argument '" + 
                        name(a.getIdentifier()) + "' declared by '" + 
                        pr.getDeclaration().getName(unit) + "'", 
                        101);
            }
        }
        else {
            if (!foundParameters.add(param)) {
                a.addError("duplicate argument for parameter: '" +
                        param.getName() + "' of '" + 
                        pr.getDeclaration().getName(unit) + "'");
            }
            else if (!dynamic && 
                    isTypeUnknown(param.getType())) {
                a.addError("parameter type could not be determined: " + 
                        paramdesc(param) + 
                        getTypeUnknownError(param.getType()));
            }
            checkNamedArgument(a, pr, param);
            //hack in an identifier node just for the backend:
            //TODO: get rid of this nasty thing
            if (a.getIdentifier()==null) {
                Tree.Identifier node = 
                        new Tree.Identifier(null);
                node.setScope(a.getScope());
                node.setUnit(a.getUnit());
                node.setText(param.getName());
                a.setIdentifier(node);
            }
        }
    }

    private String paramdesc(Parameter p) {
        Declaration dec = p.getDeclaration();
        String result = 
                "'" + p.getName() + "' of '" + 
                dec.getName(unit) + "'";
        if (dec.isClassOrInterfaceMember()) {
            ClassOrInterface ci = 
                    (ClassOrInterface) 
                        dec.getContainer();
            result += " in '" + ci.getName(unit) + "'";
        }
        return result;
    }

    private String argdesc(Parameter p, Reference pr) {
        Declaration dec = pr.getDeclaration();
        String result = 
                "'" + p.getName() + "' of '" + 
                dec.getName(unit) + "'";
        Type qt = pr.getQualifyingType();
        if (qt!=null) { 
            result += " in '" + qt.asString(unit) + "'";
        }
        return result;
    }
    
    private void checkNamedArgument(Tree.NamedArgument arg, 
            Reference reference, Parameter param) {
        arg.setParameter(param);
        Type argType = null;
        if (arg instanceof Tree.SpecifiedArgument) {
            Tree.SpecifiedArgument sa = 
                    (Tree.SpecifiedArgument) arg;
            Tree.Expression e = 
                    sa.getSpecifierExpression()
                        .getExpression();
            if (e!=null) {
                argType = e.getTypeModel();
            }
        }
        else if (arg instanceof Tree.TypedArgument) {
            Tree.TypedArgument typedArg = 
                    (Tree.TypedArgument) arg;
            TypedDeclaration argDec = 
                    typedArg.getDeclarationModel();
            argType = 
                    argumentType(arg.getScope(), argDec);
            checkArgumentToVoidParameter(param, typedArg);
            if (!dynamic 
                    && isTypeUnknown(argType)
                    && !hasError(arg)) {
                arg.addError("could not determine type of named argument: the type of '" + 
                        param.getName() + "' is not known");
            }
        }
        
        FunctionOrValue paramModel = param.getModel();
        if (paramModel!=null) {
            TypedReference paramRef = 
                    reference.getTypedParameter(param);
            Type paramType = 
                    paramType(arg.getScope(), 
                            paramRef, paramModel);
            if (!isTypeUnknown(argType) && 
                    !isTypeUnknown(paramType)) {
                Node node;
                if (arg instanceof Tree.SpecifiedArgument) {
                    Tree.SpecifiedArgument specifiedArg = 
                            (Tree.SpecifiedArgument) arg;
                    node = specifiedArg.getSpecifierExpression();
                }
                else {
                    node = arg;
                }
                checkAssignable(argType, paramType, node,
                        "named argument must be assignable to parameter " + 
                                argdesc(param, reference), 
                        2100);
            }
        }
    }

    private Type paramType(Scope argScope, 
            TypedReference paramRef, 
            FunctionOrValue paramModel) {
        if (paramModel.isParameterized()) {
            return genericFunctionType(
                    paramModel, 
                    argScope, //TODO!!! 
                    paramModel, paramRef, unit);
        }
        else {
            return paramRef.getFullType();
        }
    }

    private Type argumentType(Scope argScope, 
            TypedDeclaration argDec) {
        TypedReference argRef = 
                argDec.getTypedReference();
        if (argDec.isParameterized()) {
            return genericFunctionType(
                    argDec, 
                    argScope, //TODO!!! 
                    argDec, argRef, unit);
        }
        else {
            return argRef.getFullType();
        }
    }

    private void checkArgumentToVoidParameter(Parameter p, 
            Tree.TypedArgument ta) {
        if (ta instanceof Tree.MethodArgument) {
            Tree.MethodArgument ma = 
                    (Tree.MethodArgument) ta;
            Tree.SpecifierExpression se = 
                    ma.getSpecifierExpression();
            if (se!=null) {
                Tree.Expression e = se.getExpression();
                if (e!=null) {
                    Tree.Type t = ta.getType();
                    // if the argument is explicitly declared 
                    // using the function modifier, it should 
                    // be allowed, even if the parameter is 
                    // declared void
                    //TODO: what is a better way to check that 
                    //      this is really the "shortcut" form
                    if (t instanceof Tree.FunctionModifier && 
                            t.getToken()==null &&
                            p.isDeclaredVoid() &&
                            !isStatementExpression(e)) {
                        ta.addError("functional parameter is declared void so argument may not evaluate to a value: '" +
                                p.getName() + "' is declared 'void'");
                    }
                }
            }
        }
    }
    
    private void checkSequencedArgument(Tree.SequencedArgument sa, 
            Reference pr, Parameter p) {
        sa.setParameter(p);
        List<Tree.PositionalArgument> args = 
                sa.getPositionalArguments();
        Type paramType = 
                pr.getTypedParameter(p)
                    .getFullType();
        Interface id = unit.getIterableDeclaration();
        Type att = 
                getTupleType(args, unit, false)
                    .getSupertype(id);
        if (!isTypeUnknown(att) && 
                !isTypeUnknown(paramType)) {
            checkAssignable(att, paramType, sa, 
                    "iterable arguments must be assignable to iterable parameter " + 
                            argdesc(p, pr));
        }
    }
    
    private void checkPositionalArguments(ParameterList pl, 
            Reference pr, Tree.PositionalArgumentList pal, 
            Tree.InvocationExpression that) {
        List<Tree.PositionalArgument> args = 
                pal.getPositionalArguments();
        List<Parameter> params = pl.getParameters();
        Declaration target = pr.getDeclaration();
        int argCount = args.size();
        int paramsSize = params.size();
        
        for (int i=0; i<paramsSize; i++) {
            Parameter param = params.get(i);
            if (i>=argCount) {
                if (isRequired(param)) {
                    Node errorNode = 
                            that instanceof Tree.Annotation 
                                && args.isEmpty() ? 
                                    that : pal;
                    StringBuilder message = 
                            new StringBuilder();
                    if (i+1<paramsSize &&
                            isRequired(params.get(i+1))) {
                        message.append("missing arguments to required parameters '");
                        appendParam(pr, param, message);
                        int count = 1;
                        for (int j=i+1; j<paramsSize; j++) {
                            Parameter p = params.get(j);
                            if (p.isDefaulted() ||
                                    p.isSequenced() &&
                                    !p.isAtLeastOne()) {
                                break;
                            }
                            message.append(", ");
                            appendParam(pr, p, message);
                            count++;
                        }
                        message.append("'")
                            .insert(20, " " + count);
                    }
                    else {
                        message.append("missing argument to required parameter '");
                        appendParam(pr, param, message);
                        message.append("'");
                    }
                    message.append(" of '")
                        .append(target.getName(unit))
                        .append("'");
                    errorNode.addError(message.toString());
                    break;
                }
            } 
            else {
                Tree.PositionalArgument arg = args.get(i);
                Type pt = param.getType(); //this is only the return type
                //TODO: check parameter types for callable parameter
                if (!dynamic && 
                        isTypeUnknown(pt)) {
                    arg.addError("parameter type could not be determined: " + 
                            paramdesc(param) +
                            getTypeUnknownError(pt));
                }
                if (arg instanceof Tree.SpreadArgument) {
                    checkSpreadArgument(pr, param, arg, 
                            (Tree.SpreadArgument) arg, 
                            params.subList(i, paramsSize));
                    break;
                }
                else if (arg instanceof Tree.Comprehension) {
                    if (param.isSequenced()) {
                        checkComprehensionPositionalArgument(
                                param, pr, 
                                (Tree.Comprehension) arg, 
                                param.isAtLeastOne());
                    }
                    else {
                        arg.addError("not a variadic parameter: parameter '" + 
                                param.getName() + "' of '" + 
                                target.getName() + "'");
                    }
                    break;
                }
                else {
                    if (param.isSequenced()) {
                        checkSequencedPositionalArgument(
                                param, pr, 
                                args.subList(i, argCount));
                        return; //Note: early return!
                    }
                    else {
                        checkPositionalArgument(param, pr, 
                                (Tree.ListedArgument) arg,
                                that);
                    }
                }
            }
        }
        
        for (int i=paramsSize; i<argCount; i++) {
            Tree.PositionalArgument arg = args.get(i);
            if (arg instanceof Tree.SpreadArgument) {
                if (unit.isEmptyType(arg.getTypeModel())) {
                    continue;
                }
            }
            arg.addError("no matching parameter declared by '" +
                    target.getName(unit) + "': '" + 
                    target.getName(unit) + "' has " + 
                    paramsSize + " parameters", 
                    2000);
        }
        
        checkJavaAnnotationElements(args, params, target);
    
    }

    private static boolean isRequired(Parameter param) {
        return !param.isDefaulted() && 
                (!param.isSequenced() || 
                  param.isAtLeastOne());
    }

    private void appendParam(Reference pr, 
            Parameter param, StringBuilder missing) {
        Type pt = 
                pr.getTypedParameter(param)
                    .getFullType();
        if (!isTypeUnknown(pt)) {
            if (param.isSequenced()) {
                pt = unit.getSequentialElementType(pt);
            }
            missing.append(pt.asString(unit));
            if (param.isSequenced()) {
                missing.append(param.isAtLeastOne()?"+":"*");
            }
            missing.append(" ");
        }
        missing.append(param.getName());
    }

    private void checkJavaAnnotationElements(
            List<Tree.PositionalArgument> args, 
            List<Parameter> params,
            Declaration target) {
        if (target instanceof Function && 
                target.isAnnotation()) {
            Function method = (Function) target;
            ParameterList parameterList = 
                    method.getFirstParameterList();
            if (!parameterList.isPositionalParametersSupported()) {
                for (int i=0; 
                        i<params.size() && i<args.size(); 
                        i++) {
                    Parameter parameter = 
                            parameterList.getParameters()
                                .get(i);
                    Tree.PositionalArgument arg = args.get(i);
                    boolean isJavaAnnotationValueParameter = 
                            "value".equals(parameter.getName());
                    if (arg!=null && 
                            !isJavaAnnotationValueParameter) {
                        arg.addUsageWarning(Warning.javaAnnotationElement, 
                                "positional argument to Java annotation element: '" + 
                                        parameter.getName() + 
                                        "' (use named arguments)");
                    }
                }
            }
        }
    }

    private void checkSpreadArgument(Reference pr, 
            Parameter p, Tree.PositionalArgument a, 
            Tree.SpreadArgument arg, 
            List<Parameter> params) {
        if (pr.getDeclaration().isCoercionPoint()
                && isCallableVariadic(p)) {
            arg.addUnsupportedError("lambda conversions with spread arguments not supported");
        }
        a.setParameter(p);
        Type rat = arg.getTypeModel();
        if (!isTypeUnknown(rat)
                //note: we already checked that it's an
                //      iterable in visit(SpreadArgument)
                && unit.isContainerType(rat)) {
            Type at = spreadType(rat, unit, true);
            Type ptt = 
                    unit.getParameterTypesAsTupleType(
                            params, pr);
            if (!isTypeUnknown(at) && 
                    !isTypeUnknown(ptt)) {
                checkAssignable(at, ptt, arg, 
                        "spread argument not assignable to parameter types");
            }
        }
    }
    
    private void appendParamType(Type pt,
            boolean sequenced, StringBuilder missing) {
        if (!isTypeUnknown(pt)) {
            boolean atLeastOne;
            if (sequenced) {
                atLeastOne = unit.isSequenceType(pt);
                pt = unit.getSequentialElementType(pt);
            }
            else {
                atLeastOne = false;
            }
            missing.append(pt.asString(unit));
            if (sequenced) {
                missing.append(atLeastOne?"+":"*");
            }
        }
    }

    private void checkIndirectInvocationArguments(
            Tree.InvocationExpression that, 
            Type paramTypesAsTuple, 
            List<Type> paramTypes, 
            boolean sequenced, boolean atLeastOne, 
            int firstDefaulted) {
        
        Tree.PositionalArgumentList pal = 
                that.getPositionalArgumentList();
        List<Tree.PositionalArgument> args = 
                pal.getPositionalArguments();
        
        int paramsSize = paramTypes.size();
        int argCount = args.size();
        
        for (int i=0; i<paramsSize; i++) {
            if (isTypeUnknown(paramTypes.get(i))) {
                that.addError("parameter types cannot be determined from function reference");
                return;
            }
        }
        
        int lastRequired = 
                lastRequired(paramsSize, firstDefaulted, 
                        sequenced, atLeastOne);
        
        for (int i=0; i<paramsSize; i++) {
            Type paramType = paramTypes.get(i);
            if (i>=argCount) {
                StringBuilder missingTypes = 
                        new StringBuilder();
                appendParamType(paramType, 
                        sequenced && i==paramsSize-1, 
                        missingTypes);
                if (i==lastRequired) {
                    pal.addError("missing argument to required parameter of type '" + 
                            missingTypes + 
                            "' in indirect invocation");
                    
                }
                else if (i<lastRequired) {
                    for (int j=i+1; j<=lastRequired; j++) {
                        missingTypes.append(", ");
                        appendParamType(paramTypes.get(j), 
                                sequenced && j==paramsSize-1,
                                missingTypes);
                    }
                    pal.addError("missing arguments to " + 
                            (lastRequired-i+1) + 
                            " required parameters of types '" + 
                            missingTypes + 
                            "' in indirect invocation");
                }
                break;
            }
            else {
                Tree.PositionalArgument arg = args.get(i);
                Type at = arg.getTypeModel();
                if (arg instanceof Tree.SpreadArgument) {
                    Tree.SpreadArgument sa = 
                            (Tree.SpreadArgument) arg;
                    Type tt = 
                            unit.getTailType(
                                    paramTypesAsTuple, i);
                    checkSpreadIndirectArgument(sa, tt, at);
                    break;
                }
                else if (arg instanceof Tree.Comprehension) {
                    if (sequenced && 
                            i==paramsSize-1) {
                        Tree.Comprehension c = 
                                (Tree.Comprehension) arg;
                        checkComprehensionIndirectArgument(
                                c, paramType, atLeastOne);
                    }
                    else {
                        arg.addError("not a variadic parameter: parameter " + i);
                    }
                    break;
                }
                else {
                    if (sequenced && i==paramsSize-1) {
                        List<Tree.PositionalArgument> sublist = 
                                args.subList(i, argCount);
                        checkSequencedIndirectArgument(
                                sublist, paramType);
                        return; //Note: early return!
                    }
                    else if (at!=null && paramType!=null && 
                            !isTypeUnknown(at) && 
                            !isTypeUnknown(paramType)) {
                        checkAssignable(at, paramType, arg, 
                                "argument must be assignable to parameter type");
                    }
                }
            }
        }
        
        for (int i=paramsSize; i<argCount; i++) {
            Tree.PositionalArgument arg = args.get(i);
            if (arg instanceof Tree.SpreadArgument) {
                if (unit.isEmptyType(arg.getTypeModel())) {
                    continue;
                }
            }
            arg.addError("no matching parameter: function reference has " + 
                    paramsSize + " parameters", 
                    2000);
        }
        
    }

    private static int lastRequired(int paramsSize,
            int firstDefaulted, boolean sequenced, 
            boolean atLeastOne) {
        if (firstDefaulted<paramsSize) {
            return firstDefaulted-1;
        }
        else  {
            return sequenced && !atLeastOne ?
                    paramsSize-2 : paramsSize-1;
        }
    }

    private void checkSpreadIndirectArgument(Tree.SpreadArgument sa,
            Type tailType, Type at) {
        if (!isTypeUnknown(at) 
                //note: we already checked that it's an
                //      iterable in visit(SpreadArgument)
                && unit.isContainerType(at)) {
            Type sat = spreadType(at, unit, true);
            if (!isTypeUnknown(sat) && 
                    !isTypeUnknown(tailType)) {
                checkAssignable(sat, tailType, sa, 
                        "spread argument not assignable to parameter types");
            }
        }
    }

    private void checkSequencedIndirectArgument(
            List<Tree.PositionalArgument> args,
            Type paramType) {
        Type set = paramType==null ? 
                null : unit.getIteratedType(paramType);
        for (int j=0; j<args.size(); j++) {
            Tree.PositionalArgument a = args.get(j);
            Type at = a.getTypeModel();
            if (!isTypeUnknown(at) && 
                    !isTypeUnknown(paramType)) {
                if (a instanceof Tree.SpreadArgument) {
                    at = spreadType(at, unit, true);
                    checkAssignable(at, paramType, a, 
                            "spread argument must be assignable to variadic parameter",
                            2101);
                }
                else {
                    checkAssignable(at, set, a, 
                            "argument must be assignable to variadic parameter", 
                            2101);
                    //if we already have an arg to a nonempty variadic parameter,
                    //we can treat it like a possibly-empty variadic now
                    Interface sd = 
                            unit.getSequentialDeclaration();
                    paramType = paramType.getSupertype(sd);
                }
            }
        }
    }
    
    private void checkComprehensionIndirectArgument(
            Tree.Comprehension c, Type paramType, 
            boolean atLeastOne) {
        Tree.InitialComprehensionClause icc = 
                c.getInitialComprehensionClause();
        if (icc.getPossiblyEmpty() && atLeastOne) {
            c.addError("variadic parameter is required but comprehension is possibly empty");
        }
        Type at = c.getTypeModel();
        Type set = paramType==null ? null : 
                unit.getIteratedType(paramType);
        if (!isTypeUnknown(at) && 
                !isTypeUnknown(set)) {
            checkAssignable(at, set, c, 
                    "argument must be assignable to variadic parameter");
        }
    }
    
    private void checkSequencedPositionalArgument(
            Parameter p, 
            Reference pr, 
            List<Tree.PositionalArgument> args) {
        Type paramType = 
                pr.getTypedParameter(p)
                    .getFullType();
        Type set = paramType==null ? null : 
                unit.getIteratedType(paramType);
        for (int j=0; j<args.size(); j++) {
            Tree.PositionalArgument a = args.get(j);
            a.setParameter(p);
            Type at = a.getTypeModel();
            if (!isTypeUnknown(at) && 
                    !isTypeUnknown(paramType)) {
                if (a instanceof Tree.SpreadArgument) {
                    at = spreadType(at, unit, true);
                    checkAssignable(at, paramType, a, 
                            "spread argument must be assignable to variadic parameter " + 
                                    argdesc(p, pr), 
                            2101);
                }
                else {
                    checkAssignable(at, set, a, 
                            "argument must be assignable to variadic parameter " + 
                                    argdesc(p, pr), 
                            2101);
                    //if we already have an arg to a nonempty variadic parameter,
                    //we can treat it like a possibly-empty variadic now
                    Interface sd = 
                            unit.getSequentialDeclaration();
                    paramType = paramType.getSupertype(sd);
                }
            }
        }
    }
    
    private boolean isCallableVariadic(Parameter p) {
        Type type = p.getType();
        return p.isSequenced()
            && type!=null
            && (type.isSequence() || type.isSequential())
            && unit.getDefiniteType(unit.getSequentialElementType(type))
                   .isCallable();
    }
    
    private void checkComprehensionPositionalArgument(
            Parameter p, Reference pr, Tree.Comprehension c, 
            boolean atLeastOne) {
        if (pr.getDeclaration().isCoercionPoint()
                && isCallableVariadic(p)) {
            c.addUnsupportedError("lambda conversions with conprehensions not supported");
        }
        c.setParameter(p);
        Tree.InitialComprehensionClause icc = 
                c.getInitialComprehensionClause();
        if (icc.getPossiblyEmpty() && atLeastOne) {
            c.addError("variadic parameter is required but comprehension is possibly empty");
        }
        Type paramType = 
                pr.getTypedParameter(p)
                    .getFullType();
        c.setParameter(p);
        Type at = c.getTypeModel();
        if (!isTypeUnknown(at) && 
                !isTypeUnknown(paramType)) {
            Type set = paramType==null ? 
                    null : unit.getIteratedType(paramType);
            checkAssignable(at, set, c, 
                    "argument must be assignable to variadic parameter " + 
                            argdesc(p, pr), 
                    2101);
        }
    }
    
    private void checkPositionalArgument(Parameter p, 
            Reference pr, Tree.ListedArgument a,
            Tree.InvocationExpression ie) {
        FunctionOrValue paramModel = p.getModel();
        if (paramModel!=null) {
            a.setParameter(p);
            TypedReference paramRef = 
                    pr.getTypedParameterWithWildcardCapture(p);
            Type paramType = 
                    paramType(a.getScope(), 
                            paramRef, paramModel);
            Type at = a.getTypeModel();
            if (!isTypeUnknown(at) && 
                !isTypeUnknown(paramType)) {
                if (ie.getPrimary() 
                        instanceof Tree.Compose) {
                    Type ot = 
                            ie.getPositionalArgumentList()
                              .getPositionalArguments()
                              .get(1)
                              .getTypeModel();
                    checkAssignable(at, paramType, a, 
                            "functions of type '" 
                            + ot.asString(unit)
                            + "' and '"
                            + at.asString(unit)
                            + "' do not compose");

                }
                else {
                    checkAssignable(at, paramType, a, 
                            "argument must be assignable to parameter " 
                                    + argdesc(p, pr), 2100);
                }
            }
        }
    }
    
    @Override public void visit(Tree.Comprehension that) {
        super.visit(that);
        Tree.InitialComprehensionClause icc = 
                that.getInitialComprehensionClause();
        that.setTypeModel(icc.getTypeModel());
    }
    
    @Override public void visit(Tree.SpreadType that) {
        super.visit(that);
        Tree.Type type = that.getType();
        if (type!=null) {
            Type at = unit.getAnythingType();
            checkAssignable(that.getTypeModel(), 
                    unit.getSequentialType(at), type, 
                    "spread type must be a sequence type");
        }
    }

    @Override public void visit(Tree.SpreadArgument that) {
        super.visit(that);
        Tree.Expression e = that.getExpression();
        if (e!=null) {
            Type t = e.getTypeModel();
            checkContainer(e, t, "spread argument");
            that.setTypeModel(t);
        }
    }

    @Override public void visit(Tree.ListedArgument that) {
        super.visit(that);
        Tree.Expression e = that.getExpression();
        if (e!=null) {
            that.setTypeModel(e.getTypeModel());
        }
    }
    
    private boolean involvesUnknownTypes(Tree.ElementOrRange eor) {
        if (eor instanceof Tree.Element) {
            Tree.Element el = (Tree.Element) eor;
            Tree.Expression expression = el.getExpression();
            return isTypeUnknown(expression.getTypeModel());
        }
        else {
            Tree.ElementRange er = (Tree.ElementRange) eor;
            Tree.Expression lb = er.getLowerBound();
            Tree.Expression ub = er.getUpperBound();
            return lb!=null && 
                        isTypeUnknown(lb.getTypeModel())
                || ub!=null && 
                        isTypeUnknown(ub.getTypeModel());
        }
    }
    
    @Override public void visit(Tree.IndexExpression that) {
        super.visit(that);
        Type pt = type(that);
        Tree.Primary primary = that.getPrimary();
        if (pt==null) {
            if (primary==null || !hasError(primary)) {
                that.addError("could not determine type of receiver");
            }
        }
        else {
            /*if (that.getIndexOperator() instanceof Tree.SafeIndexOp) {
                if (unit.isOptionalType(pt)) {
                    pt = unit.getDefiniteType(pt);
                }
                else {
                    that.getPrimary().addError("receiving type not of optional type: " +
                            pt.getDeclaration().getName(unit) + " is not a subtype of Optional");
                }
            }*/
            Tree.ElementOrRange eor = 
                    that.getElementOrRange();
            if (eor==null) {
                that.addError("malformed index expression");
            }
            else if (!that.getAssigned() &&
                     !isTypeUnknown(pt) && 
                     !involvesUnknownTypes(eor)) {
                if (eor instanceof Tree.Element) {
                    Interface cd = 
                            unit.getCorrespondenceDeclaration();
                    checkIndexElement(that, pt, cd, true, 
                            "Correspondence", false);
                }
                else {
                    Interface rd = unit.getRangedDeclaration();
                    Type rst = pt.getSupertype(rd);
                    if (rst==null) {
                        primary.addError("illegal receiving type for index range expression: '" 
                                + pt.getDeclaration().getName(unit) 
                                + "' is not a subtype of 'Ranged'");
                    }
                    else {
                        List<Type> args = 
                                rst.getTypeArgumentList();
                        Type kt = args.get(0);
                        Type rt = args.get(2);
                        Tree.ElementRange er = 
                                (Tree.ElementRange) eor;
                        Tree.Expression lb = er.getLowerBound();
                        Tree.Expression ub = er.getUpperBound();
                        Tree.Expression l = er.getLength();
                        if (lb!=null) {
                            checkAssignable(lb.getTypeModel(), 
                                    kt, lb, 
                                    "lower bound must be assignable to index type");
                        }
                        if (ub!=null) {
                            checkAssignable(ub.getTypeModel(), 
                                    kt, ub, 
                                    "upper bound must be assignable to index type");
                        }
                        if (l!=null) {
                            checkAssignable(l.getTypeModel(), 
                                    unit.getIntegerType(), l, 
                                    "length must be an integer");
                        }
                        that.setTypeModel(rt);
//                        if (er.getLowerBound()!=null && er.getUpperBound()!=null) {
//                            refineTypeForTupleRange(that, pt, 
//                                    er.getLowerBound().getTerm(), 
//                                    er.getUpperBound().getTerm());
//                        }
//                        else if (er.getLowerBound()!=null) {
                        if (lb!=null && ub==null && l==null) {
                            refineTypeForTupleOpenRange(that, 
                                    pt, lb.getTerm());
                        }
                        /*if (that.getIndexOperator() instanceof Tree.SafeIndexOp) {
                            that.setTypeModel(unit.getOptionalType(that.getTypeModel()));
                        }*/
                    }
                }
            }
        }
    }

    private Type checkIndexElement(Tree.IndexExpression that,
            Type pt, Interface cd, boolean nullable,
            String superTypeName, 
            boolean allowIndexedCorrespondenceMutator) {
        if (dynamic && 
                isTypeUnknown(pt)) {
            // In dynamic blocks we allow index assignments
            // on any dynamic types
            return null;
        }
        Type kt = null;
        Type vt = null;
        Tree.ElementOrRange eor = 
                that.getElementOrRange();
        Type cst = pt.getSupertype(cd);
        if (cst != null) {
            List<Type> args = 
                    cst.getTypeArgumentList();
            kt = args.get(0);
            vt = args.get(1);
            if (nullable) {
                vt = unit.getOptionalType(vt);
            }
        }
        if (cst==null && 
                allowIndexedCorrespondenceMutator) {
            Interface ld = 
                    unit.getIndexedCorrespondenceMutatorDeclaration();
            cst = pt.getSupertype(ld);
            if (cst != null) {
                List<Type> args = 
                        cst.getTypeArgumentList();
                kt = unit.getIntegerType();
                vt = args.get(0);
                if (nullable) {
                    vt = unit.getOptionalType(vt);
                }
            }
        }
        if (cst==null) {
            Interface ld = 
                    unit.getJavaListDeclaration();
            cst = pt.getSupertype(ld);
            if (cst != null) {
                List<Type> args = 
                        cst.getTypeArgumentList();
                kt = unit.getIntegerType();
                vt = unit.getOptionalType(args.get(0));
            }
        }
        if (cst==null) {
            Interface md = 
                    unit.getJavaMapDeclaration();
            cst = pt.getSupertype(md);
            if (cst != null) {
                List<Type> args =
                        cst.getTypeArgumentList();
                kt = args.get(0);
                vt = unit.getOptionalType(args.get(1));
            }
        }
        if (cst==null) {
            boolean objectArray = 
                    unit.isJavaObjectArrayType(pt);
            boolean primitiveArray = 
                    unit.isJavaPrimitiveArrayType(pt);
            if (objectArray || primitiveArray) {
                cst = pt;
                kt = unit.getIntegerType();
                Type et = unit.getJavaArrayElementType(pt);
                vt = primitiveArray ? et : unit.getOptionalType(et);
            }
        }
        
        if (cst==null) {
            that.getPrimary()
                .addError("illegal receiving type for index expression: '" 
                        + pt.getDeclaration().getName(unit) 
                        + "' is not a subtype of '" + superTypeName + "'" 
                        + (allowIndexedCorrespondenceMutator ? 
                                " or 'IndexedCorrespondenceMutator'" : ""));
        }
        else {
            Tree.Element e = (Tree.Element) eor;
            Tree.Expression ee = 
                    e.getExpression();
            checkAssignable(ee.getTypeModel(), 
                    kt, ee, 
                    "index must be assignable to key type");
            that.setTypeModel(vt);
            Tree.Term t = ee.getTerm();
            refineTypeForTupleElement(that, pt, t);
        }
        
        return vt;
    }

    public boolean isJavaNullableMutator(Type pt, Interface cd) {
        boolean nullable = false;
        Type cst = pt.getSupertype(cd);
        if (cst==null) {
            Interface ld = unit.getJavaListDeclaration();
            cst = pt.getSupertype(ld);
            if (cst != null) {
                nullable = true;
            }
        }
        if (cst==null) {
            Interface md = unit.getJavaMapDeclaration();
            cst = pt.getSupertype(md);
            if (cst != null) {
                nullable = true;
            }
        }
        if (cst==null) {
            boolean objectArray = unit.isJavaObjectArrayType(pt);
            nullable = objectArray;
        }
        return nullable;
    }

    private void refineTypeForTupleElement(
            Tree.IndexExpression that, Type pt, Tree.Term t) {
        //TODO: in theory we could do a whole lot
        //      more static-execution of the 
        //      expression, but this seems
        //      perfectly sufficient
        boolean negated = false;
        if (t instanceof Tree.NegativeOp) {
            t = ((Tree.NegativeOp) t).getTerm();
            negated = true;
        }
        else if (t instanceof Tree.PositiveOp) {
            t = ((Tree.PositiveOp) t).getTerm();
        }
        Type tt = pt;
        if (unit.isSequentialType(tt)) {
            if (t instanceof Tree.NaturalLiteral) {
                int index = Integer.parseInt(t.getText());
                if (negated) index = -index;
                final List<Type> elementTypes = 
                        unit.getTupleElementTypes(tt);
                boolean variadic = 
                        unit.isTupleLengthUnbounded(tt);
                int minimumLength = 
                        unit.getTupleMinimumLength(tt);
                boolean atLeastOne = 
                        unit.isTupleVariantAtLeastOne(tt);
                if (elementTypes!=null) {
                    int size = elementTypes.size();
                    Type nt = unit.getNullType();
                    if (size==0) {
                        that.setTypeModel(nt);
                    }
                    else if (index<0) {
                        that.setTypeModel(nt);
                    }
                    else if (index < size-(variadic?1:0)) {
                        Type iet = 
                                elementTypes.get(index);
                        if (iet==null) return;
                        if (index >= minimumLength) {
                            iet = unionType(iet, nt, unit);
                        }
                        that.setTypeModel(iet);
                    }
                    else if (variadic) {
                        Type iet = elementTypes.get(size-1);
                        if (iet==null) return;
                        Type it = unit.getIteratedType(iet);
                        if (it==null) return;
                        if (!atLeastOne || index >= size) {
                            it = unionType(it, nt, unit);
                        }
                        that.setTypeModel(it);
                    }
                    else {
                        that.setTypeModel(nt);
                    }
                }
            }
        }
    }
    
    private void refineTypeForTupleOpenRange(
            Tree.IndexExpression that, Type pt, Tree.Term l) {
        boolean lnegated = false;
        if (l instanceof Tree.NegativeOp) {
            l = ((Tree.NegativeOp) l).getTerm();
            lnegated = true;
        }
        else if (l instanceof Tree.PositiveOp) {
            l = ((Tree.PositiveOp) l).getTerm();
        }
        Type tt = pt;
        if (unit.isSequentialType(tt)) {
            if (l instanceof Tree.NaturalLiteral) {
                int lindex = Integer.parseInt(l.getText());
                if (lnegated) lindex = -lindex;
                final List<Type> elementTypes = 
                        unit.getTupleElementTypes(tt);
                boolean variadic = 
                        unit.isTupleLengthUnbounded(tt);
                boolean atLeastOne = 
                        unit.isTupleVariantAtLeastOne(tt);
                int minimumLength = 
                        unit.getTupleMinimumLength(tt);
                List<Type> list = 
                        new ArrayList<Type>();
                if (elementTypes!=null) {
                    int size = elementTypes.size();
                    if (lindex<0) {
                        lindex=0;
                    }
                    for (int index=lindex; 
                            index < size-(variadic?1:0); 
                            index++) {
                        Type et = elementTypes.get(index);
                        if (et==null) return;
                        list.add(et);
                    }
                    if (variadic) {
                        Type it = elementTypes.get(size-1);
                        if (it==null) return;
                        Type rt = unit.getIteratedType(it);
                        if (rt==null) return;
                        list.add(rt);
                    }
                    Type rt = 
                            unit.getTupleType(list, 
                                    variadic, 
                                    atLeastOne && lindex<size, 
                                    minimumLength-lindex);
                    //intersect with the type determined using
                    //Ranged, which may be narrower, for example,
                    //for String
                    that.setTypeModel(intersectionType(rt, 
                            that.getTypeModel(), unit));
                }
            }
        }
    }

    private Type type(Tree.PostfixExpression that) {
        Tree.Primary p = that.getPrimary();
        return p==null ? null : p.getTypeModel();
    }
    
    private void assign(Tree.Term term) {
        if (term instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression m = 
                    (Tree.MemberOrTypeExpression) term;
            m.setAssigned(true);
        }
        else if (term instanceof Tree.IndexExpression) {
            Tree.IndexExpression m = 
                    (Tree.IndexExpression) term;
            m.setAssigned(true);
        }
    }
    
    @Override public void visit(
            Tree.PostfixOperatorExpression that) {
        assign(that.getTerm());
        super.visit(that);
        Type type = type(that);
        visitIncrementDecrement(that, type, 
                that.getTerm());
        checkAssignability(that.getTerm(), that);
    }

    @Override public void visit(
            Tree.PrefixOperatorExpression that) {
        assign(that.getTerm());
        super.visit(that);
        Type type = type(that);
        if (that.getTerm()!=null) {
            visitIncrementDecrement(that, type, 
                    that.getTerm());
            checkAssignability(that.getTerm(), that);
        }
    }
    
    private void visitIncrementDecrement(Tree.Term that,
            Type pt, Tree.Term term) {
        if (!isTypeUnknown(pt)) {
            Type ot = 
                    checkSupertype(pt, 
                            unit.getOrdinalDeclaration(), 
                            term, 
                            "operand expression must be of enumerable type");
            if (ot!=null) {
                Type ta = ot.getTypeArgumentList().get(0);
                checkAssignable(ta, pt, that, 
                        "result type must be assignable to declared type");
            }
            that.setTypeModel(pt);
        }
    }
    
    /*@Override public void visit(Tree.SumOp that) {
        super.visit( (Tree.BinaryOperatorExpression) that );
        Type lhst = leftType(that);
        if (lhst!=null) {
            //take into account overloading of + operator
            if (lhst.isSubtypeOf(getStringDeclaration().getType())) {
                visitBinaryOperator(that, getStringDeclaration());
            }
            else {
                visitBinaryOperator(that, getNumericDeclaration());
            }
        }
    }*/

    private void visitScaleOperator(Tree.ScaleOp that) {
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            Interface sd = unit.getScalableDeclaration();
            Type st = checkSupertype(rhst, sd, that, 
                    "right operand must be of scalable type");
            if (st!=null) {
                Type ta = st.getTypeArgumentList().get(0);
                Type rt = st.getTypeArgumentList().get(1);
                //hardcoded implicit type conversion Integer->Float 
                TypeDeclaration fd = 
                        unit.getFloatDeclaration();
                TypeDeclaration id = 
                        unit.getIntegerDeclaration();
                if (lhst.getDeclaration().inherits(id) &&
                        ta.getDeclaration().inherits(fd)) {
                    lhst = fd.getType();
                }
                checkAssignable(lhst, ta, that, 
                        "scale factor must be assignable to scale type");
                that.setTypeModel(rt);
            }
        }
    }
    
    private void checkComparable(Tree.BinaryOperatorExpression that) {
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            checkOperandTypes(lhst, rhst, 
                    unit.getComparableDeclaration(), that, 
                    "operand expressions must be comparable");
        }
    }
    
    private void visitComparisonOperator(Tree.BinaryOperatorExpression that) {
        checkComparable(that);
        that.setTypeModel(unit.getBooleanType());            
    }

    private void visitCompareOperator(Tree.CompareOp that) {
        checkComparable(that);
        that.setTypeModel(unit.getComparisonType());            
    }

    private void visitWithinOperator(Tree.WithinOp that) {
        Tree.Term lbt = that.getLowerBound().getTerm();
        Tree.Term ubt = that.getUpperBound().getTerm();
        Type lhst = 
                lbt==null ? null : 
                    lbt.getTypeModel();
        Type rhst = 
                ubt==null ? null : 
                    ubt.getTypeModel();
        Type t = that.getTerm().getTypeModel();
        if (!isTypeUnknown(t) &&
            !isTypeUnknown(lhst) && !isTypeUnknown(rhst)) {
            checkOperandTypes(t, lhst, rhst, 
                    unit.getComparableDeclaration(), 
                    that, "operand expressions must be comparable");
        }
        that.setTypeModel(unit.getBooleanType());            
    }

    private void visitSpanOperator(Tree.RangeOp that) {
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        Interface ed = unit.getEnumerableDeclaration();
        Type ot = checkOperandTypes(lhst, rhst, ed, that,
                "operand expressions must be of compatible enumerable type");
        if (ot!=null) {
            that.setTypeModel(unit.getSpanType(ot));
        }
    }
    
    private void visitMeasureOperator(Tree.SegmentOp that) {
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        Interface ed = unit.getEnumerableDeclaration();
        Type ot = checkSupertype(lhst, ed, 
                that.getLeftTerm(), 
                "left operand must be of enumerable type");
        if (!isTypeUnknown(rhst)) {
            checkAssignable(rhst, 
                    unit.getIntegerType(), 
                    that.getRightTerm(), 
                    "right operand must be an integer");
        }
        if (ot!=null) {
            Type ta = ot.getTypeArgumentList().get(0);
            that.setTypeModel(unit.getMeasureType(ta));
        }
    }

    private void visitEntryOperator(Tree.EntryOp that) {
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        checkAssignable(lhst, unit.getObjectType(), 
                that.getLeftTerm(), 
                "operand expression must not be an optional type");
//        checkAssignable(rhst, ot, that.getRightTerm(), 
//                "operand expression must not be an optional type");
        that.setTypeModel(unit.getEntryType(lhst, rhst));
    }

    private void visitIdentityOperator(Tree.BinaryOperatorExpression that) {
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            Type idt = unit.getIdentifiableType();
            checkAssignable(lhst, idt, 
                    that.getLeftTerm(), 
                    "operand expression must be of type 'Identifiable'");
            checkAssignable(rhst, idt, 
                    that.getRightTerm(), 
                    "operand expression must be of type 'Identifiable'");
            if (intersectionType(lhst, rhst, unit).isNothing()) {
                that.addError("values of disjoint types are never identical: '" 
                        + lhst.asString(unit) 
                        + "' has empty intersection with '" 
                        + rhst.asString(unit) 
                        + "'");
            }
        }
        that.setTypeModel(unit.getBooleanType());
    }

    private void visitEqualityOperator(Tree.BinaryOperatorExpression that) {
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            Type obt = unit.getObjectType();
            checkAssignable(lhst, obt, 
                    that.getLeftTerm(), 
                    "operand expression must be of type 'Object'");
            checkAssignable(rhst, obt, 
                    that.getRightTerm(), 
                    "operand expression must be of type 'Object'");
            if (intersectionType(lhst, rhst, unit).isNothing()) {
                Interface ld = unit.getListDeclaration();
                Interface sd = unit.getSetDeclaration();
                Interface md = unit.getMapDeclaration();
                if (!(lhst.getDeclaration().inherits(ld) &&
                      rhst.getDeclaration().inherits(ld)) &&
                    !(lhst.getDeclaration().inherits(sd) &&
                      rhst.getDeclaration().inherits(sd)) &&
                    !(lhst.getDeclaration().inherits(md) &&
                      rhst.getDeclaration().inherits(md)) &&
                    !(lhst.isInteger() && rhst.isFloat()) &&
                    !(lhst.isFloat() && rhst.isInteger())) {
                    that.addUsageWarning(Warning.disjointEquals, 
                            "tests equality for operands with disjoint types: '" 
                            + lhst.asString(unit) 
                            + "' and '" 
                            + rhst.asString(unit) 
                            + "' are disjoint");
                }
            }
            if (lhst.isCallable()) {
                that.getLeftTerm()
                    .addUsageWarning(Warning.expressionTypeCallable,
                            "equality test not meaningful for function references: expression is of type 'Callable'");
            }
            if (rhst.isCallable()) {
                that.getRightTerm()
                    .addUsageWarning(Warning.expressionTypeCallable,
                            "equality test not meaningful for function references: expression is of type 'Callable'");
            }
            if (lhst.isIterable()) {
                that.getLeftTerm()
                    .addUsageWarning(Warning.expressionTypeIterable,
                            "equality test not meaningful for abstract streams: expression is of type 'Iterable'");
            }
            if (rhst.isIterable()) {
                that.getRightTerm()
                    .addUsageWarning(Warning.expressionTypeIterable,
                            "equality test not meaningful for abstract streams: expression is of type 'Iterable'");
            }
        }
        that.setTypeModel(unit.getBooleanType());
    }
    
    private void visitAssignOperator(Tree.AssignOp that) {
        Type rhst = rightType(that);
        if (!isTypeUnknown(rhst)) {
            Tree.Term leftTerm = that.getLeftTerm();
            if (leftTerm 
                    instanceof Tree.IndexExpression) {
                checkIndexedAssignment(that, rhst);
            }
            else {
                Type lhst = leftType(that);
                if (!isTypeUnknown(lhst)) {
                    Type leftHandType = 
                            // allow assigning null to java properties 
                            // that could after all be null
                            hasUncheckedNulls(leftTerm) ?
                                    unit.getOptionalType(lhst) : 
                                    lhst;
                    checkAssignable(rhst, leftHandType, 
                            that.getRightTerm(), 
                            "assigned expression must be assignable to declared type", 
                            2100);
                }
            }
        }
        that.setTypeModel(rhst);
//      that.setTypeModel(lhst); //this version is easier on backend
    }

    private void checkIndexedAssignment(
            Tree.AssignmentOp that, Type rhst) {
        Tree.IndexExpression idx = 
                (Tree.IndexExpression)
                    that.getLeftTerm();
        if (idx.getElementOrRange() 
                instanceof Tree.Element) {
            Type pt = type(idx);
            if (that.getTypeModel()!=null 
                    && pt!=null) {
                Interface cmd = 
                        unit.getKeyedCorrespondenceMutatorDeclaration();
                Type vt = 
                        checkIndexElement(idx, pt, cmd, false, 
                                "KeyedCorrespondenceMutator", true);
                if (vt!=null) {
                    checkAssignable(rhst, vt,
                            that.getRightTerm(), 
                            "assigned expression must be assignable to item type '" +
                            vt.asString() +
                            "' of 'CorrespondenceMutator'");
//                    that.setTypeModel(vt);
                }
            }
        }
        else {
            idx.getPrimary()
                .addError("ranged index assignment is not supported");
        }
    }

    private Type checkOperandTypes(
            Type lhst, Type rhst, 
            TypeDeclaration td, Node node, String message) {
        Type lhsst = 
                checkSupertype(lhst, td, node, message);
        if (lhsst!=null) {
            Type at = lhsst.getTypeArgumentList().get(0);
            checkAssignable(rhst, at, node, message);
            return at;
        }
        else {
            return null;
        }
    }
    
    private Type checkOperandTypes(Type t, 
            Type lhst, Type rhst, 
            TypeDeclaration td, Node node, String message) {
        Type st = checkSupertype(t, td, node, message);
        if (st!=null) {
            Type at = st.getTypeArgumentList().get(0);
            checkAssignable(lhst, at, node, message);
            checkAssignable(rhst, at, node, message);
            return at;
        }
        else {
            return null;
        }
    }
    
    private void visitArithmeticOperator(
            Tree.BinaryOperatorExpression that, 
            TypeDeclaration type) {
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            //hard coded implicit type conversion Integer->Float
            TypeDeclaration fd = unit.getFloatDeclaration();
            TypeDeclaration id = unit.getIntegerDeclaration();
            if (!rhst.isNothing() && !lhst.isNothing()) {
                if (rhst.getDeclaration()
                            .inherits(fd) &&
                        lhst.getDeclaration()
                            .inherits(id)) {
                    lhst = fd.getType();
                }
                else if (rhst.getDeclaration()
                            .inherits(id) &&
                        lhst.getDeclaration()
                            .inherits(fd)) {
                    rhst = fd.getType();
                }
            }
            Type nt = 
                    checkSupertype(lhst, type, 
                        that.getLeftTerm(), 
                        that instanceof Tree.SumOp ?
                                "left operand must be of summable type" :
                                "left operand must be of numeric type");
            if (nt!=null) {
                List<Type> tal = nt.getTypeArgumentList();
                if (tal.isEmpty()) return;
                Type tt = tal.get(0);
                that.setTypeModel(tt);
                Type ot;
                if (that instanceof Tree.PowerOp) {
                    if (tal.size()<2) return;
                    ot = tal.get(1);
                }
                else {
                    ot = tt;
                }
                checkAssignable(rhst, ot, that, 
                        that instanceof Tree.SumOp ?
                            "right operand must be of compatible summable type" :
                            "right operand must be of compatible numeric type");
            }
        }
    }
    
    private void visitArithmeticAssignOperator(
            Tree.AssignmentOp that, 
            TypeDeclaration type) {
        if (that.getLeftTerm()
                instanceof Tree.IndexExpression) {
            that.addError("compound assignment not supported for indexed expression");
            return;
        }
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            //hard coded implicit type conversion Integer->Float
            TypeDeclaration fd = unit.getFloatDeclaration();
            TypeDeclaration id = unit.getIntegerDeclaration();
            if (!rhst.isNothing() && 
                    rhst.getDeclaration()
                        .inherits(id) &&
                !lhst.isNothing() && 
                    lhst.getDeclaration()
                        .inherits(fd)) {
                rhst = fd.getType();
            }
            Type nt = 
                    checkSupertype(lhst, type, 
                        that.getLeftTerm(),
                        that instanceof Tree.AddAssignOp ?
                                "operand expression must be of summable type" :
                                "operand expression must be of numeric type");
            that.setTypeModel(lhst);
            if (nt!=null) {
                Type t = nt.getTypeArgumentList().get(0);
                //that.setTypeModel(t); //stef requests lhst to make it easier on backend
                checkAssignable(rhst, t, that, 
                        that instanceof Tree.AddAssignOp ?
                                "right operand must be of compatible summable type" :
                                "right operand must be of compatible numeric type");
                checkAssignable(t, lhst, that, 
                        "result type must be assignable to declared type");
            }
        }
    }
    
    private void visitSetOperator(Tree.BitwiseOp that) {
        //TypeDeclaration sd = unit.getSetDeclaration();
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            Type ot = unit.getObjectType();
            checkAssignable(lhst, 
                    unit.getSetType(ot), 
                    that.getLeftTerm(), 
                    "set operand expression must be a set");
            checkAssignable(rhst, 
                    unit.getSetType(ot), 
                    that.getRightTerm(), 
                    "set operand expression must be a set");
            Type lhset = 
                    unit.getSetElementType(lhst);
            Type rhset = 
                    unit.getSetElementType(rhst);
            Type et;
            if (that instanceof Tree.IntersectionOp) {
                et = intersectionType(rhset, lhset, unit);
            }
            else if (that instanceof Tree.ComplementOp) {
                et = lhset;
            }
            else {
                et = unionType(rhset, lhset, unit);
            }            
            that.setTypeModel(unit.getSetType(et));
        }
    }

    private void visitSetAssignmentOperator(
            Tree.BitwiseAssignmentOp that) {
        if (that.getLeftTerm()
                instanceof Tree.IndexExpression) {
            that.addError("compound assignment not supported for indexed expression");
            return;
        }
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            Type sot = unit.getSetType(unit.getObjectType());
            Type snt = unit.getSetType(unit.getNothingType());
            checkAssignable(lhst, sot, 
                    that.getLeftTerm(), 
                    "set operand expression must be a set");
            checkAssignable(rhst, sot, 
                    that.getRightTerm(), 
                    "set operand expression must be a set");
            checkAssignable(snt, lhst, 
                    that.getLeftTerm(),
                    "assigned expression type must be an instantiation of 'Set'");
            Type lhset = unit.getSetElementType(lhst);
            Type rhset = unit.getSetElementType(rhst);
            if (that instanceof Tree.UnionAssignOp) {
                checkAssignable(rhset, lhset, 
                        that.getRightTerm(), 
                        "resulting set element type must be assignable to to declared set element type");
            }            
            that.setTypeModel(unit.getSetType(lhset)); //in theory, we could make this narrower
        }
    }

    private void visitLogicalOperator(
            Tree.BinaryOperatorExpression that) {
        Type bt = unit.getBooleanType();
        Type lt = leftType(that);
        Type rt = rightType(that);
        if (!isTypeUnknown(rt) && !isTypeUnknown(lt)) {
            checkAssignable(lt, bt, that, 
                    "logical operand expression must be a boolean value");
            checkAssignable(rt, bt, that, 
                    "logical operand expression must be a boolean value");
        }
        that.setTypeModel(bt);
    }

    private void visitDefaultOperator(Tree.DefaultOp that) {
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            Tree.Term lt = that.getLeftTerm();
            checkOptional(lhst, lt, lt);
            Type dt = unit.getDefiniteType(lhst);
            Type rt = unionType(dt, rhst, unit);
            that.setTypeModel(rt);
            /*that.setTypeModel(rhst);
            Type ot;
            if (isOptionalType(rhst)) {
                ot = rhst;
            }
            else {
                ot = getOptionalType(rhst);
            }
            if (!lhst.isSubtypeOf(ot)) {
                that.getLeftTerm().addError("must be of type: " + 
                        ot.asString(unit));
            }*/
        }
    }

    private void visitThenOperator(Tree.ThenOp that) {
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        if (!isTypeUnknown(lhst)) {
            checkAssignable(lhst, unit.getBooleanType(), 
                    that.getLeftTerm(), 
                    "operand expression must be a boolean value");
        }
        if ( rhst!=null && !isTypeUnknown(rhst)) {
            checkAssignable(rhst, unit.getObjectType(), 
                    that.getRightTerm(),
                    "operand expression may not be an optional type");
            that.setTypeModel(unit.getOptionalType(rhst));
        }
    }

    private void visitInOperator(Tree.InOp that) {
        Type lhst = leftType(that);
        Type rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            Type ct = rhst.getSupertype(
                    unit.getJavaCollectionDeclaration());
            if (ct==null) {
                ct = checkSupertype(rhst,
                        unit.getCategoryDeclaration(),
                        that.getRightTerm(), 
                        "operand expression must be a category");
                Type et = unit.getIteratedType(rhst);
                if (et!=null 
                        && intersectionType(lhst, et, unit).isNothing() 
                        && !(lhst.isString() && rhst.isString())) {
                  that.addUsageWarning(Warning.disjointContainment,
                          "tests containment with disjoint element types: '"
                          + lhst.asString(unit) 
                          + "' and '"
                          + et.asString(unit) 
                          + "' are disjoint");
               }
            }
            if (ct!=null) {
                Type at = 
                        ct.getTypeArguments().isEmpty() ? null : 
                            ct.getTypeArgumentList()
                                .get(0);
                checkAssignable(lhst, at, 
                        that.getLeftTerm(), 
                        "operand expression must be assignable to category type");
            }
        }
        that.setTypeModel(unit.getBooleanType());
    }
    
    private void visitUnaryOperator(
            Tree.UnaryOperatorExpression that, 
            TypeDeclaration type) {
        Type t = type(that);
        if (!isTypeUnknown(t)) {
            Type nt = 
                    checkSupertype(t, true, type, 
                            that.getTerm(), 
                            "operand expression must be of correct type");
            if (nt!=null) {
                Type at = 
                        nt.getTypeArguments().isEmpty() ? nt : 
                            nt.getTypeArgumentList()
                                .get(0);
                that.setTypeModel(at);
            }
        }
    }

    private void visitExistsOperator(Tree.Exists that) {
        checkOptional(type(that), that.getTerm(), that);
        that.setTypeModel(unit.getBooleanType());
    }
    
    private void visitNonemptyOperator(Tree.Nonempty that) {
        checkPossiblyEmpty(type(that), that.getTerm(), that);
        that.setTypeModel(unit.getBooleanType());
    }
    
    private void visitOfOperator(Tree.OfOp that) {
        Tree.Type rt = that.getType();
        if (rt!=null) {
            Type t = rt.getTypeModel();
            if (!isTypeUnknown(t)) {
                that.setTypeModel(t);
                Tree.Term tt = that.getTerm();
                if (tt!=null) {
                    Type pt = tt.getTypeModel();
                    if (isTypeUnknown(pt)) {
                        tt.addError("of operand has unknown type");
                    }
                    else if (!t.covers(pt)) {
                        that.addError(
                                "specified type does not cover the cases of the operand expression: '" 
                                + t.asString(unit) 
                                + "' does not cover '"
                                + pt.asString(unit) + "'");
                    }
                    //Just especially for the IDE!
                    if (tt instanceof Tree.Super) {
                        Tree.Super s = (Tree.Super) tt;
                        s.setDeclarationModel(t.getDeclaration());
                    }
                }
            }
            /*else if (dynamic) {
                that.addError("static type not known");
            }*/
        }
    }
    
    private void visitIsOperator(Tree.IsOp that) {
        Tree.Type rt = that.getType();
        if (rt!=null) {
            Type type = rt.getTypeModel();
            if (!isTypeUnknown(type)) {
                Tree.Term term = that.getTerm();
                if (term!=null) {
                    Type knownType = term.getTypeModel();
                    if (!isTypeUnknown(knownType)) {
                        checkReified(rt, type, knownType, false);
                        if (knownType.isSubtypeOf(type)) {
                            that.addUsageWarning(Warning.redundantNarrowing,
                                    "expression type is a subtype of the type: '" 
                                    + knownType.asString(unit) 
                                    + "' is assignable to '"
                                    + type.asString(unit) + "'");
                        }
                        else {
                            if (intersectionType(type, knownType, unit)
                                    .isNothing()) {
                                that.addError("tests assignability to bottom type 'Nothing': intersection of '" 
                                        + knownType.asString(unit) + "' and '"
                                        + type.asString(unit) + "' is empty");
                            }
                        }
                    }
                }
            }
        }
        that.setTypeModel(unit.getBooleanType());
    }

    private void checkAssignability(Tree.Term that, Node node) {
        if (that instanceof Tree.QualifiedMemberOrTypeExpression ||
            that instanceof Tree.BaseMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression smte =
                    (Tree.StaticMemberOrTypeExpression) 
                        that;
            Declaration dec = smte.getDeclaration();
            if (dec!=null && 
                    (!isEffectivelyBaseMemberExpression(smte) ||
                     (dec instanceof Value && 
                             ((Value) dec).isInferred()) ||
                     !unit.equals(dec.getUnit()))) { //Note: other cases handled in SpecificationVisitor
                if (dec instanceof Value) {
                    Value value = (Value) dec;
                    if (!value.isVariable() && 
                            !value.isLate()) {
                        that.addError("value is not a variable: '" + 
                                dec.getName(unit) + "'", 
                                800);
                    }
                }
                else {
                    that.addError("not a variable value: '" + 
                            dec.getName(unit) + "'");
                }
            }
            if (that instanceof Tree.QualifiedMemberOrTypeExpression) {
                Tree.QualifiedMemberOrTypeExpression qmte = 
                        (Tree.QualifiedMemberOrTypeExpression) 
                            that;
                Tree.MemberOperator mo = 
                        qmte.getMemberOperator();
                if (!(mo instanceof Tree.MemberOp)) {
                    that.addUnsupportedError("assignment to expression involving ?. or *. not supported");
                }
            }
        } else if (that instanceof Tree.IndexExpression) {
        }
        else {
            that.addError("expression cannot be assigned");
        }
    }
    
    private Type rightType(Tree.BinaryOperatorExpression that) {
        Tree.Term rt = that.getRightTerm();
        return rt==null? null : rt.getTypeModel();
    }

    private Type leftType(Tree.BinaryOperatorExpression that) {
        Tree.Term lt = that.getLeftTerm();
        return lt==null ? null : lt.getTypeModel();
    }
    
    private Type type(Tree.UnaryOperatorExpression that) {
        Tree.Term t = that.getTerm();
        return t==null ? null : t.getTypeModel();
    }
    
    private Interface getArithmeticDeclaration(
            Tree.ArithmeticOp that) {
        if (that instanceof Tree.PowerOp) {
            return unit.getExponentiableDeclaration();
        }
        else if (that instanceof Tree.SumOp) {
            return unit.getSummableDeclaration();
        }
        else if (that instanceof Tree.ProductOp) {
            return unit.getMultiplicableDeclaration();
        }
        else if (that instanceof Tree.DifferenceOp) {
            return unit.getInvertableDeclaration();
        }
        else if (that instanceof Tree.RemainderOp) {
            return unit.getIntegralDeclaration();
        }
        else {
            return unit.getNumericDeclaration();
        }
    }

    private Interface getArithmeticDeclaration(
            Tree.ArithmeticAssignmentOp that) {
        if (that instanceof Tree.AddAssignOp) {
            return unit.getSummableDeclaration();
        }
        if (that instanceof Tree.MultiplyAssignOp) {
            return unit.getMultiplicableDeclaration();
        }
        else if (that instanceof Tree.SubtractAssignOp) {
            return unit.getInvertableDeclaration();
        }
        else if (that instanceof Tree.RemainderAssignOp) {
            return unit.getIntegralDeclaration();
        }
        else {
            return unit.getNumericDeclaration();
        }
    }

    @Override public void visit(Tree.ArithmeticOp that) {
        super.visit(that);
        visitArithmeticOperator(that, 
                getArithmeticDeclaration(that));
    }

    @Override public void visit(Tree.BitwiseOp that) {
        super.visit(that);
        if (interpretAsSetOperator(that.getLeftTerm())) {
            visitSetOperator(that);
        }
        else {
            that.setBinary(true);
            visitArithmeticOperator(that, 
                    unit.getBinaryDeclaration());
        }
    }

    private boolean interpretAsSetOperator(Tree.Term term) {
        Type lhst = term.getTypeModel();
        return lhst==null 
            || lhst.resolveAliases().getDeclaration()
                .inherits(unit.getSetDeclaration());
    }

    @Override public void visit(Tree.ScaleOp that) {
        super.visit(that);
        visitScaleOperator(that);
    }

    @Override public void visit(Tree.LogicalOp that) {
        super.visit(that);
        visitLogicalOperator(that);
    }

    @Override public void visit(Tree.EqualityOp that) {
        super.visit(that);
        visitEqualityOperator(that);
    }

    @Override public void visit(Tree.ComparisonOp that) {
        super.visit(that);
        visitComparisonOperator(that);
    }

    @Override public void visit(Tree.WithinOp that) {
        super.visit(that);
        visitWithinOperator(that);
    }

    @Override public void visit(Tree.IdenticalOp that) {
        super.visit(that);
        visitIdentityOperator(that);
    }

    @Override public void visit(Tree.CompareOp that) {
        super.visit(that);
        visitCompareOperator(that);
    }

    @Override public void visit(Tree.DefaultOp that) {
        super.visit(that);
        visitDefaultOperator(that);
    }
        
    @Override public void visit(Tree.ThenOp that) {
        super.visit(that);
        visitThenOperator(that);
    }
        
    @Override public void visit(Tree.NegativeOp that) {
        super.visit(that);
        visitUnaryOperator(that, 
                unit.getInvertableDeclaration());
    }
        
    @Override public void visit(Tree.PositiveOp that) {
        super.visit(that);
        visitUnaryOperator(that, 
                unit.getInvertableDeclaration());
    }
    
    @Override public void visit(Tree.NotOp that) {
        super.visit(that);
        visitUnaryOperator(that, 
                unit.getBooleanDeclaration());
    }
    
    @Override public void visit(Tree.FlipOp that) {
        super.visit(that);
        visitUnaryOperator(that, 
                unit.getBinaryDeclaration());
    }
    
    @Override public void visit(Tree.AssignOp that) {
        Tree.Term leftTerm = that.getLeftTerm();
        Tree.Term rightTerm = that.getRightTerm();
        
        assign(leftTerm);
        
        if (that.getTypeModel()==null) {
            that.setTypeModel(defaultType());
        }

        if (leftTerm!=null) {
            leftTerm.visit(this);
        }
        
        if (rightTerm!=null && 
            leftTerm!=null) {
            inferParameterTypesFromAssignment(
                    leftTerm.getTypeModel(), 
                    rightTerm);
        }
        
        if (rightTerm!=null) {
            rightTerm.visit(this);
        }
        
        visitAssignOperator(that);
        checkAssignability(leftTerm, that);        
    }
    
    @Override public void visit(Tree.ArithmeticAssignmentOp that) {
        assign(that.getLeftTerm());
        super.visit(that);
        visitArithmeticAssignOperator(that, 
                getArithmeticDeclaration(that));
        checkAssignability(that.getLeftTerm(), that);
    }
    
    @Override public void visit(Tree.LogicalAssignmentOp that) {
        assign(that.getLeftTerm());
        super.visit(that);
        visitLogicalOperator(that);
        checkAssignability(that.getLeftTerm(), that);
    }
    
    @Override public void visit(Tree.BitwiseAssignmentOp that) {
        Tree.Term leftTerm = that.getLeftTerm();
        assign(leftTerm);
        super.visit(that);
        if (interpretAsSetOperator(leftTerm)) {
            visitSetAssignmentOperator(that);
        }
        else {
            that.setBinary(true);
            visitArithmeticAssignOperator(that, 
                    unit.getBinaryDeclaration());
        }
        checkAssignability(leftTerm, that);
    }
    
    @Override public void visit(Tree.RangeOp that) {
        super.visit(that);
        visitSpanOperator(that);
    }
    
    @Override public void visit(Tree.SegmentOp that) {
        super.visit(that);
        visitMeasureOperator(that);
    }
        
    @Override public void visit(Tree.EntryOp that) {
        super.visit(that);
        visitEntryOperator(that);
    }
    
    @Override public void visit(Tree.Exists that) {
        super.visit(that);
        visitExistsOperator(that);
    }
    
    @Override public void visit(Tree.Nonempty that) {
        super.visit(that);
        visitNonemptyOperator(that);
    }
    
    @Override public void visit(Tree.IsOp that) {
        super.visit(that);
        visitIsOperator(that);
    }
    
    @Override public void visit(Tree.OfOp that) {
        super.visit(that);
        visitOfOperator(that);
    }
    
    @Override public void visit(Tree.Extends that) {
        super.visit(that);
        that.addUnsupportedError("extends operator not yet supported");
    }
    
    @Override public void visit(Tree.Satisfies that) {
        super.visit(that);
        that.addUnsupportedError("satisfies operator not yet supported");
    }
    
    @Override public void visit(Tree.InOp that) {
        super.visit(that);
        visitInOperator(that);
    }
    
    @Override public void visit(Tree.LetExpression that) {
        super.visit(that);
        Tree.LetClause lc = that.getLetClause();
        if (lc.getVariables().isEmpty()) {
            lc.addError("let clause must declare at least one variable or pattern");
        }
        Tree.Expression e = lc.getExpression();
        if (e!=null) {
            that.setTypeModel(e.getTypeModel());
        }
    }
    
    @Override
    public void visit(Tree.BaseType that) {
        super.visit(that);
        TypeDeclaration type = that.getDeclarationModel();
        if (type!=null) {
            type = 
                    (TypeDeclaration)
                        handleNativeHeader(type, that, true);
            if (!type.isVisible(that.getScope())) {
                that.addError("type is not visible: " 
                        + baseDescription(that), 
                        400);
            }
            else if (type.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("type is not visible: " 
                        + baseDescription(that) 
                        + "is package private");
            }
            //don't need to consider "protected" because
            //toplevel types can't be declared protected
            //and inherited protected member types are
            //visible to subclasses
        }
    }

    @Override
    public void visit(Tree.QualifiedType that) {
        super.visit(that);
        TypeDeclaration type = that.getDeclarationModel();
        if (type!=null) {
            type = 
                    (TypeDeclaration)
                        handleNativeHeader(type, that, true);
            if (!type.isVisible(that.getScope())) {
                if (type instanceof Constructor) {
                    that.addError("constructor is not visible: " 
                            + qualifiedDescription(that), 
                            400);
                }
                else {
                    that.addError("member type is not visible: " 
                            + qualifiedDescription(that), 
                            400);
                }
            }
            else if (type.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("member type is not visible: " 
                        + qualifiedDescription(that) 
                        + " is package private");
            }
            //this is actually slightly too restrictive
            //since a qualified type may in fact be an
            //inherited member type, but in that case
            //you can just get rid of the qualifier, so
            //in fact this restriction is OK
            else if (type.isProtectedVisibility() &&
                    !declaredInPackage(type, unit)) {
                that.addError("member type is not visible: " 
                        + qualifiedDescription(that) 
                        + " is protected");
            }
            //Note: we should remove this check if we ever 
            //      make qualified member types like T.Member
            //      into a sort of virtual type
            Tree.StaticType outerType = that.getOuterType();
            if (outerType instanceof Tree.SimpleType) {
                Tree.SimpleType st = 
                        (Tree.SimpleType) outerType;
                TypeDeclaration std = st.getDeclarationModel();
                if (std.isAlias()) {
                    Type et = std.getExtendedType();
                    if (et!=null) {
                        std = et.resolveAliases()
                                .getDeclaration();
                    }
                }
                if (std instanceof TypeParameter) {
                    outerType.addError(
                            "type parameter should not occur as qualifying type: '" 
                            + std.getName(unit) 
                            + "' is a type parameter");
                }
            }
        }
    }

    private void checkBaseVisibility(Node that, 
            TypedDeclaration member, String name) {
        if (!member.isVisible(that.getScope())) {
            that.addError("function or value is not visible: '" 
                    + name + "'", 
                    400);
        }
        else if (member.isPackageVisibility() && 
                !declaredInPackage(member, unit)) {
            that.addError("function or value is not visible: '" 
                    + name 
                    + "' is package private");
        }
        //don't need to consider "protected" because
        //there are no toplevel members in Java and
        //inherited protected members are visible to 
        //subclasses
    }
    
    private void checkQualifiedVisibility(Node that, 
            TypedDeclaration member, String name, 
            String container, boolean selfReference) {
        if (!member.isVisible(that.getScope())) {
            that.addError("method or attribute is not visible: '" 
                    + name + "' of " + container, 
                    400);
        }
        else if (member.isPackageVisibility() && 
                !declaredInPackage(member, unit)) {
            that.addError("method or attribute is not visible: '" 
                    + name + "' of " + container 
                    + " is package private");
        }
        //this is actually too restrictive since
        //it doesn't take into account "other 
        //instance" access (access from a different
        //instance of the same type)
        else if (member.isProtectedVisibility() && 
                !selfReference && 
                !declaredInPackage(member, unit)) {
            that.addError("method or attribute is not visible: '" 
                    + name + "' of " + container
                    + " is protected");
        }
    }

    private void checkBaseTypeAndConstructorVisibility(
            Tree.BaseTypeExpression that, String name, 
            TypeDeclaration type) {
        //Note: the handling of "protected" here looks
        //      wrong because Java has a crazy rule 
        //      that you can't instantiate protected
        //      member classes from a subclass
        if (isOverloadedVersion(type)) {  
            //it is a Java constructor
            //get the actual type that
            //owns the constructor
            //Declaration at = type.getContainer().getDirectMember(type.getName(), null, false);
            Declaration at = 
                    type.getExtendedType()
                        .getDeclaration();
            if (!at.isVisible(that.getScope())) {
                that.addError("type is not visible: '" 
                        + name + "'");
            }
            else if (at.isPackageVisibility() &&
                    !declaredInPackage(type, unit)) {
                that.addError("type is not visible: '" 
                        + name + "' package private");
            }
            else if (at.isProtectedVisibility() &&
                    !declaredInPackage(type, unit)) {
                that.addError("type is not visible: '" 
                        + name + "' is protected");
            }
            else if (!type.isVisible(that.getScope())) {
                that.addError("type constructor is not visible: '" 
                        + name + "'");
            }
            else if (type.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("constructor is not visible: '" 
                        + name + "' is package private");
            }
            else if (type.isProtectedVisibility() &&
                    !declaredInPackage(type, unit)) {
                that.addError("constructor is not visible: '" 
                        + name + "' is protected");
            }
        }
        else {
            if (!type.isVisible(that.getScope())) {
                that.addError("type is not visible: '" 
                        + name + "'", 
                        400);
            }
            else if (type.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("type is not visible: '" 
                        + name + "' is package private");
            }
            else if (type.isProtectedVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("type is not visible: '" 
                        + name + "' is protected");
            }
        }
    }
    
    private void checkQualifiedTypeAndConstructorVisibility(
            Tree.QualifiedTypeExpression that, 
            TypeDeclaration type, String name, 
            String container) {
        //Note: the handling of "protected" here looks
        //      wrong because Java has a crazy rule 
        //      that you can't instantiate protected
        //      member classes from a subclass
        if (isOverloadedVersion(type)) {
            //it is a Java constructor
            //get the actual type that
            //owns the constructor
            //Declaration at = type.getContainer().getDirectMember(type.getName(), null, false);
            Declaration at = 
                    type.getExtendedType()
                        .getDeclaration();
            if (!at.isVisible(that.getScope())) {
                that.addError("member type is not visible: '" 
                        + name + "' of '" + container);
            }
            else if (at.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("member type is not visible: '" 
                        + name + "' of type " + container 
                        + " is package private");
            }
            else if (at.isProtectedVisibility() &&
                    !declaredInPackage(type, unit)) {
                that.addError("member type is not visible: '" 
                        + name + "' of type " + container
                        + " is protected");
            }
            else if (!type.isVisible(that.getScope())) {
                that.addError("member type constructor is not visible: '" 
                        + name + "' of " + container);
            }
            else if (type.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("member type constructor is not visible: '" 
                        + name + "' of " + container
                        + " is package private");
            }
            else if (type.isProtectedVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("member type constructor is not visible: '" 
                        + name + "' of " + container
                        + " is protected");
            }
        }
        else {
            if (!type.isVisible(that.getScope())) {
                if (type instanceof Constructor) {
                    that.addError("constructor is not visible: '" 
                            + name + "' of " + container, 
                            400);
                }
                else {
                    that.addError("member type is not visible: '" 
                            + name + "' of " + container, 
                            400);
                }
            }
            else if (type.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("member type is not visible: '" 
                        + name + "' of " + container
                        + " is package private");
            }
            else if (type.isProtectedVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("member type is not visible: '" 
                        + name + "' of " + container
                        + " is protected");
            }
        }
    }

    private static String baseDescription(Tree.BaseType that) {
        return "'" + name(that.getIdentifier()) +"'";
    }
    
    private static String qualifiedDescription(Tree.QualifiedType that) {
        String name = name(that.getIdentifier());
        Type ot = that.getOuterType().getTypeModel();
        return "'" + name + "' of type '" + 
                ot.getDeclaration().getName() + "'";
    }
    
    @Override public void visit(Tree.BaseMemberExpression that) {
        super.visit(that);
        boolean notIndirectlyInvoked = 
                !that.getIndirectlyInvoked();
        boolean notDirectlyInvoked = 
                !that.getDirectlyInvoked();
        TypedDeclaration member = 
                resolveBaseMemberExpression(that, 
                        notIndirectlyInvoked);
        boolean inferrableAnyway =
                !notDirectlyInvoked
                && inferrableAnyway(member);
        checkExtendsClauseReference(that, member);
        if (member!=null && notDirectlyInvoked 
                || inferrableAnyway) {
            Tree.TypeArguments tal = that.getTypeArguments();
            List<Type> typeArgs;
            if (typeConstructorArgumentsInferrable(member, that)) {
                typeArgs = 
                        new TypeArgumentInference(unit)
                            .getInferredTypeArgsForFunctionRef(
                                    that, null, inferrableAnyway);
            }
            else if (explicitTypeArguments(member, tal)) {
                typeArgs = 
                        getTypeArguments(tal, null, 
                                member.getTypeParameters());
            }
            else {
                typeArgs = 
                        new TypeArgumentInference(unit)
                            .getInferredTypeArgsForFunctionRef(
                                    that, null, inferrableAnyway);
            }
            if (typeArgs!=null) {
                tal.setTypeModels(typeArgs);
                visitBaseMemberExpression(that, member, 
                        typeArgs, tal, null, notDirectlyInvoked);
                //otherwise infer type arguments later
            }
            else if (notDirectlyInvoked) {
                visitGenericBaseMemberReference(that, member);
//                typeArgumentsImplicit(that);
            }
        }
    }

    private void checkExtendsClauseReference(
            Tree.BaseMemberOrTypeExpression that,
            Declaration member) {
        if (inExtendsClause 
                && constructorClass!=null 
                && member!=null 
                && member.getContainer()
                    .equals(constructorClass) 
                && !member.isStatic() 
                && !isConstructor(member)) {
            that.addError("reference to class member from constructor extends clause");
        }
    }

    private void visitGenericBaseMemberReference(
            Tree.StaticMemberOrTypeExpression that,
            TypedDeclaration member) {
        if (member instanceof Function 
                && member.isParameterized()) {
            Function generic = (Function) member;
            Scope scope = that.getScope();
            Type outerType = 
                    scope.getDeclaringType(member);
            TypedReference target = 
                    member.appliedTypedReference(outerType, 
                            NO_TYPE_ARGS);
            that.setTarget(target);
            Type functionType = 
                    genericFunctionType(generic, scope, 
                            member, target, unit);
            that.setTypeModel(functionType);
            checkNotJvm(that, 
                    "type functions are not supported on the JVM: '" + 
                    member.getName(unit) + 
                    "' is generic (specify explicit type arguments)");
        }
    }

    private TypedDeclaration resolveBaseMemberExpression(
            Tree.BaseMemberExpression that,
            boolean error) {
        
        if (that instanceof Tree.Compose) {
            Function compose = (Function) 
                    unit.getLanguageModuleDeclaration("compose");
            that.setDeclaration(compose);
            return compose;
        }
        
        Tree.Identifier id = that.getIdentifier();
        String name = name(id);
        Scope scope = that.getScope();
        TypedDeclaration member = 
                getTypedDeclaration(scope, name, 
                        that.getSignature(), 
                        that.getEllipsis(),
                        that.getUnit());
        if (member==null) {
            if (!dynamic 
                    && !isNativeForWrongBackend(scope, unit) 
                    && error) {
                that.addError(
                        "function or value is not defined: '" 
                            + name + "'" 
                            + correctionMessage(name, scope, 
                                    unit, cancellable), 
                        100);
                unit.setUnresolvedReferences();
            }
        }
        else {
            member = 
                    (TypedDeclaration) 
                        handleAbstractionOrHeader(member, 
                                that, error);
            that.setDeclaration(member);
            if (error) {
                if (checkConcreteConstructor(member, that)) {
                    checkBaseVisibility(that, member, name);
                }
            }
        }
        return member;
    }
    
    @Override public void visit(
            Tree.QualifiedMemberExpression that) {
        super.visit(that);
        boolean notIndirectlyInvoked = 
                !that.getIndirectlyInvoked();
        boolean notDirectlyInvoked = 
                !that.getDirectlyInvoked();
        TypedDeclaration member = 
                resolveQualifiedMemberExpression(that, 
                        notIndirectlyInvoked);
        boolean inferrableAnyway =
                !notDirectlyInvoked
                && inferrableAnyway(member);
        if (member!=null && notDirectlyInvoked
                || inferrableAnyway) {
            Tree.Primary primary = that.getPrimary();
            Tree.TypeArguments tal = that.getTypeArguments();
            Type receiverType = 
                    primary.getTypeModel()
                        .resolveAliases();
            if (receiverType.isTypeConstructor()) {
                that.addError("missing type arguments in reference to member of generic type: the type '" +
                        receiverType.asString(unit) + 
                        "' is a type constructor (specify explicit type arguments)");
            }
            List<Type> typeArgs;
            if (typeConstructorArgumentsInferrable(member, that)) {
                typeArgs = 
                        new TypeArgumentInference(unit)
                            .getInferredTypeArgsForFunctionRef(
                                    that, receiverType, 
                                    inferrableAnyway);
            }
            else if (explicitTypeArguments(member, tal)) {
                typeArgs = 
                        getTypeArguments(tal, receiverType, 
                                member.getTypeParameters());
            }
            else {
                typeArgs = 
                        new TypeArgumentInference(unit)
                            .getInferredTypeArgsForFunctionRef(
                                    that, receiverType,
                                    inferrableAnyway);
            }
            if (typeArgs!=null) {
                tal.setTypeModels(typeArgs);
                if (primary instanceof Tree.Package) {
                    visitBaseMemberExpression(that, member, 
                            typeArgs, tal, null, 
                            notDirectlyInvoked);
                }
                else {
                    visitQualifiedMemberExpression(that, 
                            receiverType, member, typeArgs, 
                            tal, notDirectlyInvoked);
                }
            }
            else if (notDirectlyInvoked) {
                if (primary instanceof Tree.Package) {
                    visitGenericBaseMemberReference(that, 
                            member);
                }
                else {
                    visitGenericQualifiedMemberReference(
                            that, receiverType, member);
                }
            }
            if (that.getStaticMethodReference()) {
                handleStaticReferenceImplicitTypeArguments(
                        that);
            }
            //otherwise infer type arguments later
        }
    }

    private void visitGenericQualifiedMemberReference(
            Tree.QualifiedMemberExpression that,
            Type receiverType,
            TypedDeclaration member) {
        if (member instanceof Function
                && member.isParameterized()) {
            Function generic = (Function) member;
            Scope scope = that.getScope();
            TypedReference target = 
                    receiverType.getTypedMember(member, 
                            NO_TYPE_ARGS);
            that.setTarget(target);
            Type functionType = 
                    genericFunctionType(generic, scope, 
                            member, target, unit);
            that.setTypeModel(functionType);
            checkNotJvm(that, 
                    "type functions are not supported on the JVM: '" + 
                    member.getName(unit) + 
                    "' is generic (specify explicit type arguments)");
        }
    }
    
    /**
     * Validate the type arguments to the qualifying type
     * in a static reference when no type arguments are
     * given explicitly. 
     * 
     * This is called later than usual because the type args 
     * might be inferrable from an invocation of the whole 
     * static reference.
     * 
     * @param that the static reference
     */
    private void handleStaticReferenceImplicitTypeArguments(
            Tree.QualifiedMemberOrTypeExpression that) {
        Declaration member = that.getDeclaration();
        Tree.TypeArguments tas = that.getTypeArguments();
        
        //we do this check later than usual, in order
        //to allow qualified refs to Java static members
        //without type arguments to the qualifying type
        if (isStaticReference(that)) {
            if (member!=null 
                    && !explicitTypeArguments(member, tas)) {
                that.addError("type arguments could not be inferred: '" +
                        member.getName(unit) + "' is generic");
            }
            //the reference to the qualifying type
            Tree.StaticMemberOrTypeExpression smte =
                    (Tree.StaticMemberOrTypeExpression) 
                        that.getPrimary();
            //we have to get the type args from the tree
            //here because the calling code doesn't know
            //them (it is walking the qualifying reference)
            Tree.TypeArguments typeArgs = 
                    smte.getTypeArguments();
            TypeDeclaration type = 
                    (TypeDeclaration) 
                        smte.getDeclaration();
            if (type!=null 
                    && !explicitTypeArguments(type, typeArgs) 
                    && typeArgs.getTypeModels()==null) { //nothing inferred
                Declaration declaration = 
                        smte.getDeclaration();
                smte.addError("missing type arguments to generic type qualifying static reference: '" + 
                        declaration.getName(unit) + 
                        "' declares type parameters " + 
                        typeParameterList(declaration));
            }
        }
        
        Tree.Primary primary = that.getPrimary();
        if (!that.getDirectlyInvoked() 
                && (member.isStatic() || isConstructor(member))
                && primary instanceof 
                    Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression smte =
                    (Tree.StaticMemberOrTypeExpression) 
                        primary;
            Declaration qualifyingType = 
                    smte.getDeclaration();
            Tree.TypeArguments qtas = 
                    smte.getTypeArguments();
            if (qualifyingType!=null
                    && qualifyingType.isParameterized()
                    && !qualifyingType.isJava()
                    && !explicitTypeArguments(qualifyingType, qtas)) {
                if (explicitTypeArguments(member, tas)) {
                    Type functionType = 
                            genericFunctionType(
                                    qualifyingType,
                                    that.getScope(),
                                    member,
                                    that.getTarget(),
                                    unit);
                    that.setTypeModel(functionType);
                    checkNotJvm(that, 
                            "type functions are not supported on the JVM: '" + 
                            qualifyingType.getName(unit) + 
                            "' is generic (specify explicit type arguments)");
                }
                else {
                    that.addError("missing explicit type arguments to generic qualifying type: '" + 
                            qualifyingType.getName(unit) +
                            "' declares type parameters " + 
                            typeParameterList(qualifyingType));
                }
            }
        }
    }

    private static StringBuilder typeParameterList(Declaration dec) {
        StringBuilder paramList = 
                new StringBuilder();
        for (TypeParameter tp: 
            dec.getTypeParameters()) {
            if (paramList.length()>0) {
                paramList.append(", ");
            }
            paramList.append("'")
                .append(tp.getName())
                .append("'");
        }
        return paramList;
    }

    private TypedDeclaration resolveQualifiedMemberExpression(
            Tree.QualifiedMemberExpression that, 
            boolean error) {
        Tree.Identifier id = that.getIdentifier();
        boolean nameNonempty = 
                id!=null && 
                !id.getText().equals("");
        if (nameNonempty && checkMember(that)) {
            Tree.Primary primary = that.getPrimary();
            String name = name(id);
            List<Type> signature = that.getSignature();
            boolean spread = that.getEllipsis();
            String container;
            boolean ambiguous;
            TypedDeclaration member;
            Type pt; 
            if (primary instanceof Tree.Package) {
                Package pack = unit.getPackage();
                container = "package '" + 
                        pack.getNameAsString() + "'";
                member = 
                        getPackageTypedDeclaration(name, 
                                signature, spread, unit);
                ambiguous = false;
                pt = null;
            }
            else {
                pt = primary.getTypeModel()
                        .resolveAliases(); //needed for aliases like "alias Id<T> => T"
                TypeDeclaration d = getDeclaration(that, pt);
                if (d instanceof Constructor) {
                    d = d.getExtendedType().getDeclaration();
                }
                container = "type '" + d.getName(unit) + "'";
                Scope scope = that.getScope();
                member = 
                        getTypedMember(d, name, 
                                signature, spread, unit, scope);
                ambiguous = member==null && 
                        d.isMemberAmbiguous(name, unit, 
                                signature, spread);
                if (member==null) {
                    container += memberCorrectionMessage(name, 
                            d, scope, unit, cancellable);
                }
            }
            if (member==null) {
                if (error) {
                    if (ambiguous) {
                        that.addError("method or attribute is ambiguous: '" +
                                name + "' for " + container);
                    }
                    else {
                        that.addError("method or attribute is not defined: '" +
                                name + "' in " + container, 
                                100);
                        unit.setUnresolvedReferences();
                    }
                }
            }
            else {
                member = 
                        (TypedDeclaration) 
                            handleAbstractionOrHeader(member, 
                                    that, error);
                if (error) {
                    checkStaticPrimary(that, primary, member, pt);
                }
                that.setDeclaration(member);
                resetSuperReference(that);
                boolean selfReference = 
                        isSelfOrSuperReference(primary);
                if (!selfReference && 
                        !member.isShared()) {
                    if (that.getAssigned()) {
                        member.setOtherInstanceWriteAccess(true);
                    }
                    else {
                        member.setOtherInstanceReadAccess(true);
                    }
                }
                if (error) {
                    if (checkConcreteConstructor(member, that)) {
                        checkQualifiedVisibility(that, 
                                member, name, container, 
                                selfReference);
                    }
                    checkSuperMember(that, signature, spread);
                }
            }
            return member;
        }
        else {
            return null;
        }
    }

    private boolean isSelfOrSuperReference(Tree.Primary p) {
        return p instanceof Tree.This 
            || p instanceof Tree.Outer 
            || p instanceof Tree.Super;
    }

    private void checkSuperMember(
            Tree.QualifiedMemberOrTypeExpression that,
            List<Type> signature, boolean spread) {
        Tree.Primary primary = that.getPrimary();
        Tree.Term term = 
                eliminateParensAndWidening(primary);
        if (term instanceof Tree.Super) {
            Tree.MemberOperator op = 
                    that.getMemberOperator();
            if (op instanceof Tree.SpreadOp) {
                primary.addError("spread member operator may not be applied to 'super' reference");
            }
            checkSuperInvocation(that, signature, spread);
        }
    }

    private void visitQualifiedMemberExpression(
            Tree.QualifiedMemberExpression that,
            Type receivingType, 
            TypedDeclaration member, 
            List<Type> typeArgs, 
            Tree.TypeArguments tal,
            boolean error) {
        
        Type receiverType =
                accountForStaticReferenceReceiverType(that, 
                        unwrap(receivingType, that));
        
        if (error) {
            checkMemberOperator(receivingType, that);
            Tree.Primary primary = that.getPrimary();
            if (isConstructor(member) &&
                    !(primary instanceof Tree.BaseTypeExpression ||
                      primary instanceof Tree.QualifiedTypeExpression)) {
                primary.addError("constructor reference must be qualified by a type expression");
            }
            checkTypeArguments(member, receiverType, 
                    typeArgs, tal, that);
        }
        
        TypedReference ptr = 
                receiverType.getTypedMember(member, 
                        typeArgs, that.getAssigned());
        /*if (ptr==null) {
            that.addError("method or attribute is not defined: " + 
                    member.getName(unit) + " of type " + 
                    receiverType.getDeclaration().getName(unit));
        }
        else {*/
        that.setTarget(ptr);
        checkSpread(member, that);
        boolean direct = that.getDirectlyInvoked();
        Type fullType = 
                accountForGenericFunctionRef(direct, 
                        tal, receivingType, typeArgs, 
                        ptr.getFullType(wrap(ptr.getType(), 
                                receivingType, that)));
        
        if (error
                && !dynamic 
                && !isNativeForWrongBackend(that.getScope(), unit) 
                && !isAbstraction(member) 
                && isTypeUnknown(fullType) 
                && !hasError(that)) {
            //this occurs with an ambiguous reference
            //to a member of an intersection type
            String rtname = 
                    receiverType.getDeclaration()
                        .getName(unit);
            that.addError(
                    "could not determine type of method or attribute reference: '" +
                    member.getName(unit) + 
                    "' of '" + rtname + "' is ambiguous" + 
                    getTypeUnknownError(fullType));
        }
        
        that.setTypeModel(accountForStaticReferenceType(
                that, member, fullType));
        //}
        if (that.getStaticMethodReference()) {
            handleStaticReferenceImplicitTypeArguments(
                    that);
        }
    }

    private static Type accountForGenericFunctionRef(
            boolean directlyInvoked, 
            Tree.TypeArguments tas, 
            Type receivingType,
            List<Type> typeArgs, 
            Type fullType) {
        if (fullType == null) {
            return null;
        }
        else {
            boolean explicit = 
                    tas instanceof Tree.TypeArgumentList;
            if (explicit || directlyInvoked || 
                    typeArgs!=null && !typeArgs.isEmpty()) {
                Type realType = fullType.resolveAliases();
                if (realType.isTypeConstructor()) {
                    //apply the type arguments to the generic
                    //function reference
                    //TODO: it's ugly to do this here, better
                    //      to suck it into TypedReference
                    return realType.getDeclaration()
                            .appliedType(receivingType, typeArgs);
                }
                else {
                    return fullType;
                }
            }
            else {
                return fullType;
            }
        }
    }

    private void checkSpread(TypedDeclaration member, 
            Tree.QualifiedMemberExpression that) {
        if (!(that.getMemberOperator() 
                instanceof Tree.MemberOp)) {
            if (member instanceof Functional) {
                Functional f = (Functional) member;
                if (f.getParameterLists().size()!=1) {
                    that.addError("spread method must have exactly one parameter list");
                }
            }
        }
    }

    private Type accountForStaticReferenceReceiverType(
            Tree.QualifiedMemberOrTypeExpression that, 
            Type receivingType) {
        if (that.getStaticMethodReference()) {
            Tree.MemberOrTypeExpression primary = 
                    (Tree.MemberOrTypeExpression) 
                        that.getPrimary();
            Reference target = primary.getTarget();
            return target==null ? 
                    unit.getUnknownType() : 
                    target.getType();
        }
        else {
            return receivingType;
        }
    }
    
    private Type accountForStaticReferenceType(
            Tree.QualifiedMemberOrTypeExpression that, 
            Declaration member, Type type) {
        if (that.getStaticMethodReference()) {
            Tree.MemberOrTypeExpression primary = 
                    (Tree.MemberOrTypeExpression) 
                        that.getPrimary();
            if (isConstructor(member)) {
                //Ceylon named constructor
                if (primary instanceof Tree.QualifiedMemberOrTypeExpression) {
                    Tree.QualifiedMemberOrTypeExpression qmte = 
                            (Tree.QualifiedMemberOrTypeExpression) primary;
                    Tree.MemberOperator mo = qmte.getMemberOperator();
                    if (!(mo instanceof Tree.MemberOp)) {
                        mo.addError("illegal operator qualifying constructor reference");
                        return null;
                    }
                }
                if (primary.getStaticMethodReference()) {
                    Tree.QualifiedMemberOrTypeExpression qmte = 
                            (Tree.QualifiedMemberOrTypeExpression) 
                                primary;
                    if (qmte.getDeclaration()
                            .isStatic()) {
                        return type;
                    }
                    Tree.MemberOrTypeExpression pp = 
                            (Tree.MemberOrTypeExpression) 
                                qmte.getPrimary();
                    return accountForStaticReferenceType(qmte, 
                            pp.getDeclaration(), type);
                }
                else {
                    return type;
                }
            }
            else {
                //something other than a constructor 
                if (primary instanceof Tree.QualifiedMemberOrTypeExpression) {
                    Tree.QualifiedMemberOrTypeExpression qmte =
                            (Tree.QualifiedMemberOrTypeExpression) 
                                primary;
                    Tree.Primary pp = qmte.getPrimary();
                    if (!(pp instanceof Tree.BaseTypeExpression) &&
                        !(pp instanceof Tree.QualifiedTypeExpression) &&
                        !(pp instanceof Tree.Package)) {
                        pp.addError("non-static type expression qualifies static member reference");   
                    }
                }
                if (member.isStatic()) {
                    //static member of Java type
                    if (primary.getStaticMethodReference()) {
                        Tree.QualifiedMemberOrTypeExpression qmte = 
                                (Tree.QualifiedMemberOrTypeExpression) 
                                    primary;
                        if (qmte.getDeclaration()
                                .isStatic()) {
                            return type;
                        }
                        else {
                            Tree.MemberOrTypeExpression pp = 
                                    (Tree.MemberOrTypeExpression) 
                                        qmte.getPrimary();
                            return accountForStaticReferenceType(
                                    qmte, pp.getDeclaration(), 
                                    type);
                        }
                    }
                    else {
                        return type;
                    }
                }
                else {
                    //ordinary non-static, non-constructor member
                    Reference target = primary.getTarget();
                    if (target==null) {
                        return unit.getUnknownType();
                    }
                    else {
                        return getStaticReferenceType(type, 
                                target.getType());
                    }
                }
            }
        }
        else {
            return type;
        }
    }
    
    private Type getStaticReferenceType(Type type, Type rt) {
        return appliedType(unit.getCallableDeclaration(), type,
                appliedType(unit.getTupleDeclaration(), rt, rt, 
                        unit.getEmptyType()));
    }
    
    private void visitBaseMemberExpression(
            Tree.StaticMemberOrTypeExpression that, 
            TypedDeclaration member, 
            List<Type> typeArgs, 
            Tree.TypeArguments tal, 
            Type receivingType,
            boolean error) {
        
        if (error) {
            checkTypeArguments(member, null, 
                    typeArgs, tal, that);
        }
        
        Scope scope = that.getScope();
        Type outerType = scope.getDeclaringType(member);
        if (outerType==null) {
            outerType = receivingType;
        }
        TypedReference pr = 
                member.appliedTypedReference(outerType, 
                        typeArgs, that.getAssigned());
        that.setTarget(pr);
        boolean direct = that.getDirectlyInvoked();
        Type fullType =
                accountForGenericFunctionRef(direct, 
                        tal, outerType, typeArgs, 
                        pr.getFullType());
        if (error 
                && !dynamic 
                && !isNativeForWrongBackend(scope, unit) 
                && !isAbstraction(member) 
                && isTypeUnknown(fullType) 
                && !hasError(that)) {
            that.addError(
                    "could not determine type of function or value reference: the type of '" +
                    member.getName(unit) + "' is not known" + 
                    getTypeUnknownError(fullType));
        }
        
        if (dynamic && 
                isTypeUnknown(fullType)) {
            //deliberately throw away the partial
            //type information we have
            return;
        }
        that.setTypeModel(fullType);
    }
    
    private static boolean inferrableAnyway(Declaration dec) {
        if (dec!=null
                && dec.isParameterized()
                && dec instanceof Functional) {
            Functional fd = (Functional) dec;
            ParameterList list = fd.getFirstParameterList();
            return list!=null && list.getParameters().isEmpty();
        }
        else {
            return false;
        }
    }
    
    @Override public void visit(Tree.BaseTypeExpression that) {
        super.visit(that);
        boolean notIndirectlyInvoked = 
                !that.getIndirectlyInvoked();
        boolean notDirectlyInvoked = 
                !that.getDirectlyInvoked();
        TypeDeclaration type = 
                resolveBaseTypeExpression(that, 
                        notIndirectlyInvoked);
        boolean inferrableAnyway =
                !notDirectlyInvoked
                && inferrableAnyway(type);
        checkExtendsClauseReference(that, type);
        if (type!=null && notDirectlyInvoked
                || inferrableAnyway) {
            Tree.TypeArguments tal = that.getTypeArguments();
            List<Type> typeArgs;
            if (explicitTypeArguments(type, tal)) {
                typeArgs = 
                        getTypeArguments(tal, null, 
                                type.getTypeParameters());
            }
            else {
                typeArgs = 
                        new TypeArgumentInference(unit)
                            .getInferredTypeArgsForFunctionRef(
                                    that, null, inferrableAnyway);
            }
            if (typeArgs!=null) {
                tal.setTypeModels(typeArgs);
                visitBaseTypeExpression(that, type, typeArgs, 
                        tal, null, notDirectlyInvoked);
                //otherwise infer type arguments later
            }
            else if (notDirectlyInvoked
                    && !that.getStaticMethodReferencePrimary()) {
                visitGenericBaseTypeReference(that, type);
            }
        }
    }

    private void visitGenericBaseTypeReference(
            Tree.StaticMemberOrTypeExpression that,
            TypeDeclaration type) {
        if (type instanceof Class
                && type.isParameterized()) {
            Class generic = (Class) type;
            Scope scope = that.getScope();
            Type outerType = scope.getDeclaringType(type);
            Type target = 
                    type.appliedType(outerType, 
                            typeParametersAsArgList(generic));
            that.setTarget(target);
            Type functionType = 
                    genericFunctionType(generic, scope, 
                            type, target, unit);
            that.setTypeModel(functionType);
            checkNotJvm(that, 
                    "type functions are not supported on the JVM: '" + 
                    type.getName(unit) + 
                    "' is generic (specify explicit type arguments)");
        }
    }

    private TypeDeclaration resolveBaseTypeExpression(
            Tree.BaseTypeExpression that,
            boolean error) {
        Tree.Identifier id = that.getIdentifier();
        String name = name(id);
        Scope scope = that.getScope();
        TypeDeclaration type = 
                getTypeDeclaration(scope, name, 
                        that.getSignature(), 
                        that.getEllipsis(), 
                        that.getUnit());
        if (type==null) {
            if (error 
                    && !dynamic 
                    && !isNativeForWrongBackend(scope, unit)) {
                that.addError(
                        "type is not defined: '" + name + "'"
                            + correctionMessage(name, scope, 
                                    unit, cancellable), 
                        102);
                unit.setUnresolvedReferences();
            }
        }
        else {
            type = 
                    (TypeDeclaration) 
                        handleAbstractionOrHeader(type, 
                                that, error);
            that.setDeclaration(type);
            if (error) {
                if (checkConcreteClass(type, that)) {
                    if (checkVisibleConstructor(that, type)) {
                        checkBaseTypeAndConstructorVisibility(
                                that, name, type);
                    }
                }
            }
        }
        return type;
    }

    private boolean checkConcreteConstructor(TypedDeclaration member,
            Tree.StaticMemberOrTypeExpression that) {
        if (isConstructor(member)) {
            Scope container = member.getContainer();
            Constructor cons = 
                    (Constructor) 
                        member.getTypeDeclaration();
            if (cons.isAbstract()) {
                that.addError("partial constructor cannot be invoked: '" 
                        + member.getName(unit) 
                        + "' is abstract");
                return false;
            }
            else if (container instanceof Class) {
                Class c = (Class) container;
                if (c.isAbstract()) {
                    that.addError("class cannot be instantiated: '" 
                            + member.getName(unit) 
                            + "' is a constructor for the abstract class '" 
                            + c.getName(unit));
                    return false;
                }
                else {
                    return true;
                }
            }
            else {
                return false;
            }
        }
        else {
            return true;
        }
    }

    private boolean checkConcreteClass(TypeDeclaration type,
            Tree.MemberOrTypeExpression that) {
        if (that.getStaticMethodReferencePrimary()) {
                return true;
        }
        else {
            if (type instanceof Class) {
                Class c = (Class) type;
                if (c.isAbstract()) {
                    that.addError("class cannot be instantiated: '" 
                            + type.getName(unit) 
                            + "' is abstract");
                    return false;
                }
                else if (c.getParameterList()==null) {
                    if (!c.isAbstraction()) {
                        that.addError("class cannot be instantiated: '" 
                                + type.getName(unit) 
                                + "' does not have a default constructor");
                    }
                    //else the parameter list is null because an
                    //overloaded declaration could not be resolved
                    return false;
                }
                else {
                    return true;
                }
            }
            else if (type instanceof Constructor) {
                Scope container = type.getContainer();
                if (type.isAbstract()) {
                    that.addError("partial constructor cannot be invoked: '" 
                            + type.getName(unit) 
                            + "' is abstract");
                    return false;
                }
                else if (container instanceof Class) {
                    Class c = (Class) container;
                    if (c.isAbstract()) {
                        that.addError("class cannot be instantiated: '" 
                                + type.getName(unit) 
                                + "' is a constructor for the abstract class '" 
                                + c.getName(unit));
                        return false;
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return false;
                }
            }
            else {
                that.addError("type cannot be instantiated: '" 
                        + type.getName(unit) 
                        + "' is not a class");
                return false;
            }
        }
    }

    private boolean checkVisibleConstructor(
            Tree.MemberOrTypeExpression that,
            TypeDeclaration type) {
        return checkDefaultConstructorVisibility(that, type) 
            && checkSealedReference(that, type);
    }

    private boolean checkDefaultConstructorVisibility(
            Tree.MemberOrTypeExpression that, 
            TypeDeclaration type) {
        if (type instanceof Class 
                && !contains(type, that.getScope())
                && !that.getStaticMethodReferencePrimary()) {
            Class c = (Class) type;
            Constructor dc = c.getDefaultConstructor();
            if (dc!=null && !dc.isShared()) {
                that.addError("default constructor for class '"
                        + c.getName(unit)
                        + "' is not 'shared'");
                return false;
            }
        }
        return true;
    }

    private boolean checkSealedReference(
            Tree.MemberOrTypeExpression that, 
            TypeDeclaration type) {
        if (type.isSealed() 
                && !unit.inSameModule(type) 
                && !that.getStaticMethodReferencePrimary()) {
            String moduleName = 
                    type.getUnit()
                        .getPackage()
                        .getModule()
                        .getNameAsString();
            if (type instanceof Constructor) {
                String cname = 
                        type.getExtendedType()
                            .getDeclaration()
                            .getName(unit);
                that.addError("invokes or references a sealed constructor in a different module: '" 
                        + type.getName(unit) 
                        + "' of '" + cname 
                        + "' in '" + moduleName 
                        + "'");
            }
            else {
                that.addError("instantiates or references a sealed class in a different module: '" 
                        + type.getName(unit) 
                        + "' in '" 
                        + moduleName 
                        + "'");
            }
            return false;
        }
        else {
            return true;
        }
    }
    
    void visitExtendedTypePrimary(Tree.ExtendedTypeExpression that) {
        Declaration dec = that.getDeclaration();
        if (dec instanceof Class) {
            Class c = (Class) dec;
            if (c.isAbstraction()) {
                //if the constructor is overloaded
                //resolve the right overloaded version
                Declaration result = 
                        findMatchingOverloadedClass(c, 
                                that.getSignature(), 
                                that.getEllipsis());
                if (result!=null && result!=dec) {
                    //patch the reference, which was already
                    //initialized to the abstraction
                    TypeDeclaration td = 
                            (TypeDeclaration) result;
                    that.setDeclaration(td);
                    c = (Class) td;
                    if (isOverloadedVersion(result)) {  
                        //it is a Java constructor
                        if (result.isPackageVisibility() && 
                                !declaredInPackage(result, unit)) {
                            that.addError("constructor is not visible: '" 
                                    + result.getName() 
                                    + "' is package private");
                        }
                    }
                }
                //else report to user that we could not
                //find a matching overloaded constructor
            }
            if (!c.isAbstraction() && c.getParameterList()==null) {
                that.addError("class cannot be instantiated: '" 
                        + c.getName(unit) 
                        + "' does not have a parameter list or default constructor");
            }
        }
    }

    @Override public void visit(
            Tree.QualifiedMemberOrTypeExpression that) {
        super.visit(that);
        Tree.Term p = that.getPrimary();
        while (p instanceof Tree.Expression &&
                p.getMainToken()==null) { //this hack allows actual parenthesized expressions through
            Tree.Expression e = (Tree.Expression) p;
            p = e.getTerm();
        }
        if (p instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) p;
            Declaration pd = mte.getDeclaration();
            if (!that.getStaticMethodReference()) {
                if (pd instanceof Functional && !pd.isDynamic()) {
                    //this is a direct function ref
                    //it's not a type, it can't have members
                    that.addError("direct function references do not have members");
                }
            }
        }
    }

    void resetSuperReference(
            Tree.QualifiedMemberOrTypeExpression that) {
        //Just for the IDE!
        Tree.Term p = that.getPrimary();
        if (p instanceof Tree.Super) {
            Declaration dec = that.getDeclaration();
            if (dec!=null) {
                Tree.Super s = (Tree.Super) p;
                TypeDeclaration td = 
                        (TypeDeclaration) 
                            dec.getContainer();
                s.setDeclarationModel(td);
            }
        }
    }

    @Override public void visit(
            Tree.QualifiedTypeExpression that) {
        super.visit(that);
        boolean notIndirectlyInvoked = 
                !that.getIndirectlyInvoked();
        boolean notDirectlyInvoked = 
                !that.getDirectlyInvoked();
        TypeDeclaration type = 
                resolveQualifiedTypeExpression(that, 
                        notIndirectlyInvoked);
        boolean inferrableAnyway =
                !notDirectlyInvoked
                && inferrableAnyway(type);
        if (type!=null && notDirectlyInvoked
                || inferrableAnyway) {
            Tree.Primary primary = that.getPrimary();
            Tree.TypeArguments tal = that.getTypeArguments();
            Type receiverType = 
                    primary.getTypeModel()
                        .resolveAliases();
            List<Type> typeArgs;
            if (explicitTypeArguments(type, tal)) {
                typeArgs = 
                        getTypeArguments(tal, receiverType, 
                                type.getTypeParameters());
            }
            else {
                typeArgs = 
                        new TypeArgumentInference(unit)
                            .getInferredTypeArgsForFunctionRef(
                                    that, receiverType, 
                                    inferrableAnyway);
            }
            if (typeArgs!=null) {
                tal.setTypeModels(typeArgs);
                if (primary instanceof Tree.Package) {
                    visitBaseTypeExpression(that, type, 
                            typeArgs, tal, null, 
                            notDirectlyInvoked);
                }
                else {
                    visitQualifiedTypeExpression(that, 
                            receiverType, type, typeArgs, 
                            tal, notDirectlyInvoked);
                }
            }
            else if (notDirectlyInvoked) {
                if (!that.getStaticMethodReferencePrimary()) {
                    if (primary instanceof Tree.Package) {
                        visitGenericBaseTypeReference(that, 
                                type);
                    }
                    else {
                        visitGenericQualifiedTypeReference(
                                that, receiverType, type);
                    }
                }
            }
            if (that.getStaticMethodReference()) {
                handleStaticReferenceImplicitTypeArguments(
                        that);
            }
            //otherwise infer type arguments later

        }
    }

    private void visitGenericQualifiedTypeReference(
            Tree.QualifiedTypeExpression that,
            Type outerType,
            TypeDeclaration type) {
        if (type instanceof Class
                && type.isParameterized()) {
            Class generic = (Class) type;
            Scope scope = that.getScope();
            Type target =
                    outerType.getTypeMember(type, 
                            typeParametersAsArgList(generic));
            that.setTarget(target);
            Type functionType = 
                    genericFunctionType(generic, scope, 
                            type, target, unit);
            that.setTypeModel(functionType);
            checkNotJvm(that, 
                    "type functions are not supported on the JVM: '" + 
                    type.getName(unit) + 
                    "' is generic (specify explicit type arguments)");
        }
    }

    private TypeDeclaration resolveQualifiedTypeExpression(
            Tree.QualifiedTypeExpression that,
            boolean error) {
        if (checkMember(that)) {
            Tree.Primary primary = that.getPrimary();
            Tree.Identifier id = that.getIdentifier();
            List<Type> signature = that.getSignature();
            boolean spread = that.getEllipsis();
            String name = name(id);
            String container;
            boolean ambiguous;
            TypeDeclaration type;
            Type pt;
            if (primary instanceof Tree.Package) {
                Package pack = unit.getPackage();
                container = "package '" + 
                        pack.getNameAsString() + "'";
                type = 
                        getPackageTypeDeclaration(name, 
                                signature, spread, unit);
                ambiguous = false;
                pt = null;
            }
            else {
                pt = primary.getTypeModel()
                        .resolveAliases(); //needed for aliases like "alias Id<T> => T"
                TypeDeclaration d = getDeclaration(that, pt);
                if (d instanceof Constructor) {
                    d = d.getExtendedType().getDeclaration();
                }
                container = "type '" + d.getName(unit) + "'";
                Scope scope = that.getScope();
                type = 
                        getTypeMember(d, name, 
                                signature, spread, unit, scope);
                ambiguous = type==null && 
                        d.isMemberAmbiguous(name, unit, 
                                signature, spread);
                if (type==null) {
                    container += 
                            memberCorrectionMessage(name, 
                                    d, scope, unit, cancellable);
                }
            }
            if (type==null) {
                if (error) {
                    if (ambiguous) {
                        that.addError("member type is ambiguous: '" +
                                name + "' for " + container);
                    }
                    else {
                        that.addError("member type is not defined: '" +
                                name + "' in " + container, 
                                100);
                        unit.setUnresolvedReferences();
                    }
                }
            }
            else {
                type = 
                        (TypeDeclaration) 
                            handleAbstractionOrHeader(type, 
                                    that, error);
                if (error) {
                    checkStaticPrimary(that, primary, type, pt);
                }
                that.setDeclaration(type);
                resetSuperReference(that);
                if (!isSelfOrSuperReference(primary) && 
                        !type.isShared()) {
                    if (that.getAssigned()) {
                        type.setOtherInstanceWriteAccess(true);
                    }
                    else {
                        type.setOtherInstanceReadAccess(true);
                    }
                }
                if (error) {
                    if (checkConcreteClass(type, that)) {
                        if (checkVisibleConstructor(that, type)) {
                            checkQualifiedTypeAndConstructorVisibility(
                                    that, type, name, container);
                        }
                    }
                    if (!inExtendsClause) {
                        checkSuperMember(that, signature, spread);
                    }
                }
            }
            return type;
        }
        else {
            return null;
        }
    }

    private void checkStaticPrimary(
            Tree.QualifiedMemberOrTypeExpression that, 
            Tree.Primary primary, 
            Declaration member, Type pt) {
        if (member.isStatic() 
                && !that.getStaticMethodReference()) {
            Tree.MemberOperator mo = 
                    that.getMemberOperator();
            TypeDeclaration outer =
                    (TypeDeclaration)
                        member.getContainer();
            if (member.isJava()) {
                primary.addUsageWarning(Warning.syntaxDeprecation, 
                        "reference to static member should be qualified by type: '"
                        + member.getName(unit) 
                        + "' is a static member of '"
                        + outer.getName(unit) 
                        + "'");
            }
            else if (!(mo instanceof Tree.MemberOp)) {
                mo.addError("operator '" + mo.getText() + 
                        "' may not be followed by reference to static member: '" +
                        member.getName(unit) + 
                        "' is a static member of '"
                        + outer.getName(unit) 
                        + "'");
            }
            else {
                primary.addError( 
                        "reference to static member must be qualified by type: '"
                        + member.getName(unit) 
                        + "' is a static member of '"
                        + pt.getSupertype(outer).asString(unit)
                        + "'", 14000);
            }
        }
    }
    
    private static boolean checkMember(
            Tree.QualifiedMemberOrTypeExpression qmte) {
        Tree.Primary p = qmte.getPrimary();
        Type pt = p.getTypeModel();
        return p instanceof Tree.Package ||
                isResolvedStaticMethodRef(qmte) ||
                pt!=null && 
                //account for dynamic blocks
                (!pt.isUnknown() || 
                        qmte.getMemberOperator() 
                            instanceof Tree.SpreadOp);
    }

    private static boolean isResolvedStaticMethodRef(
            Tree.QualifiedMemberOrTypeExpression qmte) {
        Tree.Primary p = qmte.getPrimary();
        if (qmte.getStaticMethodReference() &&
                p instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression smte = 
                    (Tree.StaticMemberOrTypeExpression) p;
            return smte.getDeclaration()!=null;
        }
        else {
            return false;
        }
    }
    
    private TypeDeclaration getDeclaration(
            Tree.QualifiedMemberOrTypeExpression that,
            Type pt) {
        TypeDeclaration td;
        if (that.getStaticMethodReference()) {
            Tree.MemberOrTypeExpression primary = 
                    (Tree.MemberOrTypeExpression) 
                        that.getPrimary();
            td = (TypeDeclaration)
                    primary.getDeclaration();
            td = td==null ? new UnknownType(unit) : td;
        }
        else {
            td = unwrap(pt, that).getDeclaration();
        }
        if (td != null && td.isNativeImplementation()) {
            TypeDeclaration header = 
                    (TypeDeclaration)
                        getNativeHeader(td);
            if (header!=null) {
                td = header;
            }
        }
        return td;
    }

    private boolean explicitTypeArguments
            (Declaration dec, Tree.TypeArguments tal) {
        return tal instanceof Tree.TypeArgumentList 
            || !dec.isParameterized();
    }
    
    private boolean typeConstructorArgumentsInferrable(
            Declaration dec, 
            Tree.StaticMemberOrTypeExpression smte) {
        if (smte.getTypeArguments() 
                instanceof Tree.TypeArgumentList) {
            return false;
        }
        else if (dec instanceof Value) {
            Value value = (Value) dec;
            TypedReference param = 
                    smte.getTargetParameter();
            Type paramType = 
                    smte.getParameterType();
            if (param!=null && paramType==null) {
                paramType = param.getType();
            }
            Type type = value.getType();
            return type!=null &&
                    type.resolveAliases()
                        .isTypeConstructor() &&
                    paramType!=null &&
                    !paramType.resolveAliases()
                        .isTypeConstructor();
        }
        else {
            return false;
        }
    }

    @Override public void visit(Tree.SimpleType that) {
        //this one is a declaration, not an expression!
        //we are only validating type arguments here
        super.visit(that);
        Type pt = that.getTypeModel();
        if (pt!=null) {
            //the type has already been set by TypeVisitor
            TypeDeclaration type = 
                    that.getDeclarationModel();
            Tree.TypeArgumentList tal = 
                    that.getTypeArgumentList();
            //No type inference for declarations
            if (type!=null) {
                List<TypeParameter> params = 
                        type.getTypeParameters();
                List<Type> typeArgs = 
                        getTypeArguments(tal, 
                                pt.getQualifyingType(), 
                                params);
                checkTypeArguments(type, null, 
                        typeArgs, tal, that);
            }
            if (pt.isTypeConstructor()) {
                if (modelLiteral) {
                    that.addError("missing type arguments of generic type: '" + 
                            type.getName(unit) +
                            "' has type parameters " +
                            typeParametersString(type) +
                            " (add missing type argument list)");
                }
                else if (declarationLiteral) {
                    //this is fine
                }
                else {
                    if (!that.getStaticTypePrimary() 
                            //qualifying types for
                            //Java static types don't
                            //require type arguments
                            || !type.isJava()) {
                        checkNotJvm(that, 
                                "type functions are not supported on the JVM: '" + 
                                type.getName(unit) + 
                                "' is generic (specify explicit type arguments)");
                    }
                }
            }
        }
    }
    
    @Override public void visit(Tree.EntryType that) {
        super.visit(that);
        Tree.StaticType keyType = that.getKeyType();
        checkAssignable(keyType.getTypeModel(), 
                unit.getObjectType(), keyType,
                "entry key type must not be an optional type");
    }

    private void visitQualifiedTypeExpression(
            Tree.QualifiedTypeExpression that,
            Type receivingType, 
            TypeDeclaration memberType, 
            List<Type> typeArgs, 
            Tree.TypeArguments tal, 
            boolean error) {
        
        Type receiverType =
                accountForStaticReferenceReceiverType(that, 
                        unwrap(receivingType, that));
        
        if (error) {
            checkMemberOperator(receivingType, that);
            if (memberType instanceof Constructor) {
                that.addError("constructor is not a type: '" + 
                        memberType.getName(unit) + "' is a constructor");
            }
            checkTypeArguments(memberType, receiverType, 
                    typeArgs, tal, that);
        }
        
        Type type = 
                receiverType.getTypeMember(memberType, 
                        typeArgs);
        that.setTarget(type);
        Type fullType =
                type.getFullType(wrap(type, 
                        receivingType, that));
        if (error
                && !dynamic 
                && !that.getStaticMethodReference() 
                && memberType instanceof Class 
                && !isAbstraction(memberType) 
                && isTypeUnknown(fullType) 
                && !hasError(that)) {
            //this occurs with an ambiguous reference
            //to a member of an intersection type
            String rtname = 
                    receiverType.getDeclaration()
                        .getName(unit);
            that.addError("could not determine type of member class reference: '" +
                    memberType.getName(unit) + "' of '" + 
                    rtname + "' is ambiguous");
        }
        
        that.setTypeModel(accountForStaticReferenceType(
                that, memberType, fullType));
        if (that.getStaticMethodReference()) {
            handleStaticReferenceImplicitTypeArguments(
                    that);
        }
    }
    
    private void visitBaseTypeExpression(
            Tree.StaticMemberOrTypeExpression that, 
            TypeDeclaration baseType, 
            List<Type> typeArgs, 
            Tree.TypeArguments tal, 
            Type receivingType, 
            boolean error) {
        
        if (error) {
            checkTypeArguments(baseType, null, 
                    typeArgs, tal, that);
        }
        
        Type outerType = 
                that.getScope()
                    .getDeclaringType(baseType);
        if (outerType==null) {
            outerType = receivingType;
        }
        Type type = 
                baseType.appliedType(outerType, 
                        typeArgs);
        Type fullType = type.getFullType();
        that.setTypeModel(fullType);
        that.setTarget(type);
    }

    @Override public void visit(Tree.Expression that) {
        //i.e. this is a parenthesized expression
        super.visit(that);
        Tree.Term term = that.getTerm();
        if (term==null) {
            that.addError("expression not well formed");
        }
        else {
            Type t = term.getTypeModel();
            if (t!=null) {
                that.setTypeModel(t);
            }
        }
    }
    
    @Override public void visit(Tree.Outer that) {
        Type oci = 
                getOuterClassOrInterface(that.getScope());
        if (oci==null) {
            that.addError("'outer' occurs outside a nested class or interface definition");
        }
        else {
            that.setTypeModel(oci);
            that.setDeclarationModel(oci.getDeclaration());
        }
        /*if (defaultArgument) {
            that.addError("reference to outer from default argument expression");
        }*/
    }

    @Override public void visit(Tree.Super that) {
        ClassOrInterface ci = 
                getContainingClassOrInterface(that.getScope());
        if (inExtendsClause) {
            //Note: super has a special meaning in any 
            //      extends clause where it refers to the 
            //      supertype of the outer class
            if (ci!=null) {
                if (ci.isClassOrInterfaceMember()) {
                    ClassOrInterface cci = 
                            (ClassOrInterface) 
                                ci.getContainer();
                    that.setDeclarationModel(cci);
                    that.setTypeModel(intersectionOfSupertypes(cci));
                }
            }
        }
        else {
            //TODO: for consistency, move these errors to SelfReferenceVisitor
            if (ci==null) {
                that.addError("'super' occurs outside any type definition");
            }
            else {
                that.setDeclarationModel(ci);
                that.setTypeModel(intersectionOfSupertypes(ci));
            }
        }
    }

    @Override public void visit(Tree.This that) {
        ClassOrInterface ci = 
                getContainingClassOrInterface(that.getScope());
        if (inExtendsClause) {
            if (ci!=null) {
                if (ci.isClassOrInterfaceMember()) {
                    ClassOrInterface cci = 
                            (ClassOrInterface) 
                                ci.getContainer();
                    that.setDeclarationModel(cci);
                    that.setTypeModel(cci.getType());
                }
            }
        }
        else {
            if (ci==null) {
                that.addError("'this' occurs outside a class or interface definition");
            }
            else {
                that.setDeclarationModel(ci);
                that.setTypeModel(ci.getType());
            }
        }
    }
    
    @Override public void visit(Tree.Package that) {
        if (!that.getQualifier()) {
            that.addError("'package' must qualify a reference to a toplevel declaration");
        }
        super.visit(that);
    }
    
    
    @Override public void visit(Tree.Dynamic that) {
        super.visit(that);
        if (dynamic) {
            Tree.NamedArgumentList nal = 
                    that.getNamedArgumentList();
            if (nal!=null) {
                for (Tree.NamedArgument na: 
                        nal.getNamedArguments()) {
                    if (na instanceof Tree.SpecifiedArgument) {
                        if (na.getIdentifier()==null) {
                            na.addError("missing argument name in dynamic instantiation expression");
                        }
                    }
                }
            }
        }
        else {
            that.addError("dynamic instantiation expression occurs outside dynamic block");
        }
    }
    
    @Override public void visit(Tree.Tuple that) {
        super.visit(that);
        Type tt = null;
        Tree.SequencedArgument sa = 
                that.getSequencedArgument();
        if (sa!=null) {
            List<Tree.PositionalArgument> pas = 
                    sa.getPositionalArguments();
            tt = getTupleType(pas, unit, true);
        }
        else {
            tt = unit.getEmptyType();
        }
        if (tt!=null) {
            that.setTypeModel(tt);
            if (tt.containsUnknowns()) {
                that.addError("tuple element type could not be inferred");
            }
        }
    }

    @Override public void visit(Tree.SequenceEnumeration that) {
        super.visit(that);
        for (Tree.Statement st: that.getStatements()) {
            st.addError("enumeration expression may not contain statements");
        }
        Type st = null;
        Tree.SequencedArgument sa = 
                that.getSequencedArgument();
        if (sa!=null) {
            List<Tree.PositionalArgument> pas = 
                    sa.getPositionalArguments();
            Type tt = getTupleType(pas, unit, false);
            if (tt!=null) {
                Interface id = unit.getIterableDeclaration();
                st = tt.getSupertype(id);
                if (st==null) {
                    st = unit.getIterableType(unit.getUnknownType());
                }
            }
        }
        else {
            st = unit.getIterableType(unit.getNothingType());
        }
        if (st!=null) {
            that.setTypeModel(st);
            if (st.containsUnknowns()) {
                that.addError("iterable element type could not be inferred");
            }
        }
    }

    @Override public void visit(Tree.CatchVariable that) {
        super.visit(that);
        Tree.Variable var = that.getVariable();
        if (var!=null) {
            Tree.Type vt = var.getType();
            Type et = unit.getExceptionType();
            if (vt instanceof Tree.LocalModifier) {
                vt.setTypeModel(et);
                var.getDeclarationModel().setType(et);
            }
            else {
                Type t = vt.getTypeModel();
                if (!isTypeUnknown(t)) {
                    Type tt = unit.getThrowableType();
                    checkAssignable(t, tt, vt, 
                            "catch type must be a throwable type");
                    Type ret = unit.getJavaRuntimeExceptionType();
                    if (t.isExactly(ret)) {
                        vt.addError("illegal catch type: 'RuntimeException' may not be caught");
                    }
                    if (!vt.hasErrors() 
                            && !t.isSubtypeOf(et)) {
                        vt.addUsageWarning(Warning.catchType, 
                                "discouraged 'catch' type: '" 
                                + t.asString(unit) 
                                + "' is not a subtype of 'Exception'");
                    }
                }
            }
        }
    }

    @Override public void visit(Tree.StringTemplate that) {
        super.visit(that);
        for (Tree.Expression e: that.getExpressions()) {
            Type et = e.getTypeModel();
            if (!isTypeUnknown(et)) {
                checkAssignable(et, 
                        unit.getObjectType(), e, 
                        "interpolated expression must not be an optional type");
                if (et.isCallable()) {
                    e.addUsageWarning(Warning.expressionTypeCallable, 
                            "interpolated function reference does not have a meaningful representation: expression is of type 'Callable'");
                }
            }
        }
        that.setTypeModel(unit.getStringType());
    }
    
    @Override public void visit(Tree.StringLiteral that) {
        that.setTypeModel(unit.getStringType());
    }
    
    @Override public void visit(Tree.NaturalLiteral that) {
        that.setTypeModel(unit.getIntegerType());
    }
    
    @Override public void visit(Tree.FloatLiteral that) {
        that.setTypeModel(unit.getFloatType());
    }
    
    @Override public void visit(Tree.CharLiteral that) {
        String result = that.getText();
        if (result.codePointCount(1, result.length()-1)!=1) {
            that.addError("character literal must contain exactly one character");
        }
        that.setTypeModel(unit.getCharacterType());
    }
    
    @Override public void visit(Tree.QuotedLiteral that) {
        that.setTypeModel(unit.getStringType());
    }
    
    @Override
    public void visit(Tree.CompilerAnnotation that) {
        //don't visit the argument       
    }
    
    @Override
    public void visit(Tree.MatchCase that) {
        super.visit(that);
        if (switchStatementOrExpression!=null) {
            Tree.Switched switched = 
                    switchClause().getSwitched();
            if (switched!=null) {
                Tree.Expression switchExpression = 
                        switched.getExpression();
                Tree.Variable switchVariable = 
                        switched.getVariable();
                Type switchType = 
                        getSwitchType(switchExpression,
                                switchVariable);
                if (switchType!=null) {
                    Tree.Variable var = that.getVariable();
                    if (var!=null) {
                        if (dynamic || 
                                !isTypeUnknown(switchType)) { //eliminate dupe errors
                            var.visit(this);
                        }
                        initOriginalDeclaration(var);
                        //disable narrowing if the switch
                        //expression is a ref to a variable
                        if (switchExpression!=null) {
                            Tree.Term switchTerm = 
                                    switchExpression.getTerm();
                            if (switchTerm instanceof 
                                    Tree.BaseMemberExpression) {
                                Tree.BaseMemberExpression bme = 
                                        (Tree.BaseMemberExpression) 
                                            switchTerm;
                                Declaration dec = 
                                        bme.getDeclaration();
                                if (dec instanceof Value 
                                        && ((Value) dec).isVariable()) {
                                    var.getDeclarationModel()
                                        .setDropped(true);
                                }
                            }
                        }
                    }
                    Tree.MatchList matchList = 
                            that.getExpressionList();
                    for (Tree.Expression e: 
                            matchList.getExpressions()) {
                        if (e!=null) {
                            Type caseType = e.getTypeModel();
                            if (!isTypeUnknown(caseType)) {
                                if (var!=null && 
                                        switchType.isExactly(caseType)) {
                                    var.getDeclarationModel()
                                        .setDropped(true);
                                }
                                Type narrowedType = 
                                        intersectionType(
                                                caseType, switchType, 
                                                unit);
                                if (isDefinitelyNothing(switched,
                                        caseType, narrowedType)) {
                                    e.addError("value is not a case of the switched type: '" +
                                            caseType.asString(unit) +
                                            "' is not a case of the type '" +
                                            switchType.asString(unit) + "'");
                                }
                            }
                        }
                        checkValueCase(e);
                    }
                    for (Tree.Type t: 
                            matchList.getTypes()) {
                        if (t!=null) {
                            Type caseType = t.getTypeModel();
                            checkReified(t, 
                                    caseType, switchType, 
                                    false);
                            Type narrowedType = 
                                    intersectionType(
                                            caseType, switchType, 
                                            unit);
                            if (isDefinitelyNothing(switched, 
                                    caseType, narrowedType)) {
                                t.addError("narrows to bottom type 'Nothing': '" + 
                                        caseType.asString(unit) + 
                                        "' has empty intersection with '" + 
                                        switchType.asString(unit) + "'");
                            }
                        }
                    }
                    if (var!=null) {
                        Type caseType = getType(that);
                        Type narrowedType = 
                                intersectionType(
                                        caseType, switchType, 
                                        unit);
                        var.getType()
                            .setTypeModel(narrowedType);
                        var.getDeclarationModel()
                            .setType(narrowedType);
                        if (!canHaveUncheckedNulls(narrowedType)) {
                            var.getDeclarationModel()
                                .setUncheckedNullType(false);
                        }
                    }                        
                }
            }
        }
    }

    private void checkValueCase(Tree.Expression e) {
        if (e==null) {
            return;
        }
        Tree.Term term = e.getTerm();
        Type type = e.getTypeModel();
        if (term instanceof Tree.Tuple) {
            Tree.Tuple tuple = (Tree.Tuple) term;
            Tree.SequencedArgument sa = 
                    tuple.getSequencedArgument();
            if (sa!=null) {
                for (Tree.PositionalArgument pa: 
                        sa.getPositionalArguments()) {
                    if (pa instanceof Tree.ListedArgument) {
                        Tree.ListedArgument la = 
                                (Tree.ListedArgument) pa;
                        checkValueCase(la.getExpression());
                    }
                    else {
                        pa.addError("case must be a simple tuple");
                    }
                }
            }
            return;
        }
        if (term instanceof Tree.NegativeOp) {
            Tree.NegativeOp no = (Tree.NegativeOp) term;
            term = no.getTerm();
        }
        if (term instanceof Tree.Literal) {
            if (term instanceof Tree.FloatLiteral) {
                e.addError("literal case may not be a 'Float' literal");
            }
        }
        else if (term instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) term;
            Declaration ref = mte.getDeclaration();
            TypeDeclaration dec = type.getDeclaration();
            if (isToplevelObjectCase(dec)) {
                //no need to check for Identifiable
                //because the anonymous type check
                //itself is sufficient
                warnIfCustomEquals(e, dec);
            }
            else if (isToplevelValueConstructorCase(dec)
                    || isConstantCase(ref)) {
                //we already know that String, Integer, 
                //Character have reasonable definitions 
                //of value equality that the backend 
                //uses to compare them instead of using
                //identity equality
                if (!isPrimitiveCase(dec)) {
                    //TODO: actually we don't need to do this check
                    //      if the toplevel value constructor is
                    //      the only constructor of its type
                    Interface id = unit.getIdentifiableDeclaration();
                    if (dec.inherits(id)) {
                        warnIfCustomEquals(e, dec);
                    }
                    else {
                        //we don't have a guaranteed well-defined disjoint
                        //equality unless it is a String, Integer, Character, 
                        //a unit type (toplevel object), or an Identifiable
                        //TODO: change this to a warning?
                        e.addError("value case must be identifiable, a toplevel or static object, or a constant 'String', 'Integer', or 'Character': '"
                                + dec.getName(unit) 
                                + "' does not inherit 'Identifiable'");
                    }
                }
            }
            else {
                e.addError("value case must be a toplevel or static object, a value constructor of a toplevel or static class, or a literal 'String', 'Integer', or 'Character'");
            }
        }
        else if (term!=null) {
            e.addError("value case must be a toplevel or static object, a value constructor of a toplevel or static class, or a literal 'String', 'Integer', or 'Character'");
        }
    }

    private static boolean isPrimitiveCase(TypeDeclaration dec) {
        return dec.isString() 
            || dec.isInteger()
            || dec.isCharacter();
    }

    private void warnIfCustomEquals(Node node, 
            TypeDeclaration dec) {
        //assume that Java enums all have
        //reasonable definitions of equality
        if (!dec.isJavaEnum()) {
            Declaration eq =
                    dec.getMember("equals", 
                            Arrays.asList(unit.getObjectType()), 
                            false);
            if (eq!=null) {
                Scope container = eq.getContainer();
                if (container instanceof TypeDeclaration) {
                    TypeDeclaration td = 
                            (TypeDeclaration) 
                                container;
                    Interface id = unit.getIdentifiableDeclaration();
                    if (!container.equals(id)) {
                        node.addUsageWarning(Warning.valueEqualityIgnored, 
                                "value equality defined by type '" 
                                + td.getName(unit) 
                                + "' ignored (identity equality is used to match value case)");
                    }
                }
            }
        }
    }

    private static boolean isEnumCase(Tree.Term term) {
        if (term instanceof Tree.MemberOrTypeExpression) {
            Type type = term.getTypeModel();
            if (type==null) {
                return false;
            }
            else {
                TypeDeclaration dec = type.getDeclaration();
                return isToplevelObjectCase(dec)
                    || isToplevelValueConstructorCase(dec);
            }
        }
        else if (term instanceof Tree.Tuple) {
            Tree.Tuple tup = (Tree.Tuple) term;
            return tup.getSequencedArgument()==null;
        }
        else {
            return false;
        }
    }

    private static boolean isConstantCase(Declaration ref) {
        if (ref instanceof Value) {
            Value val = (Value) ref;
            return !ModelUtil.isObject(val)
                && !ModelUtil.isConstructor(val)
                && (ref.isToplevel() || ref.isStatic()) 
                && !val.isVariable()
                && !val.isTransient();
        }
        else {
            return false;
        }
    }

    private static boolean isToplevelValueConstructorCase(TypeDeclaration dec) {
        if (dec.isValueConstructor()) {
            Scope container = dec.getContainer();
            if (container instanceof Class) {
                Class cl = (Class) container;
                return cl.isStatic() 
                    || cl.isToplevel();
            }
        }
        return false;
    }

    private static boolean isToplevelObjectCase(TypeDeclaration dec) {
        return dec.isObjectClass() 
            && (dec.isToplevel() || dec.isStatic());
    }

    @Override
    public void visit(Tree.IsCase that) {
        Tree.Type t = that.getType();
        if (t!=null) {
            t.visit(this);
        }
        if (switchStatementOrExpression!=null) {
            Tree.Switched switched = 
                    switchClause().getSwitched();
            if (switched!=null) {
                Tree.Expression switchExpression = 
                        switched.getExpression();
                Tree.Variable switchVariable = 
                        switched.getVariable();
                Type switchType = 
                        getSwitchType(switchExpression,
                                switchVariable);
                if (switchType!=null) {
                    Tree.Variable v = that.getVariable();
                    if (v!=null) {
                        if (dynamic || 
                                !isTypeUnknown(switchType)) { //eliminate dupe errors
                            v.visit(this);
                        }
                        initOriginalDeclaration(v);
                    }
                    if (t!=null) {
                        Type caseType = t.getTypeModel();
                        checkReified(t, 
                                caseType, switchType, 
                                false);
                        Type narrowedType = 
                                intersectionType(
                                        caseType, switchType, 
                                        unit);
                        if (isDefinitelyNothing(switched, 
                                caseType, narrowedType)) {
                            that.addError("narrows to bottom type 'Nothing': '" + 
                                    caseType.asString(unit) + 
                                    "' has empty intersection with '" + 
                                    switchType.asString(unit) + "'");
                        }
                        if (v!=null) {
                            v.getType()
                             .setTypeModel(narrowedType);
                            v.getDeclarationModel()
                             .setType(narrowedType);
                            if (!canHaveUncheckedNulls(narrowedType)) {
                                v.getDeclarationModel()
                                 .setUncheckedNullType(false);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isDefinitelyNothing(
            Tree.Switched switched, 
            Type caseType, Type narrowedType) {
        return narrowedType.isNothing() 
                && !(switchHasUncheckedNulls(switched) 
                    && unit.getNullValueType()
                           .isSubtypeOf(caseType));
    }
    
    @Override
    public void visit(Tree.SatisfiesCase that) {
        super.visit(that);
        that.addUnsupportedError("satisfies cases are not yet supported");
    }
    
    private static Type getSwitchType(
            Tree.Expression switchExpression,
            Tree.Variable switchVariable) {
        if (switchVariable!=null) {
            return switchVariable.getType().getTypeModel();
        }
        else if (switchExpression!=null) {
            return switchExpression.getTypeModel();
        }
        else {
            return null;
        }
    }
    
    @Override
    public void visit(Tree.SwitchStatement that) {
        Node oss = switchStatementOrExpression;
        Node ois = ifStatementOrExpression;
        ifStatementOrExpression = null;
        switchStatementOrExpression = that;
        
        super.visit(that);
        
        checkSwitch(that.getSwitchClause(), 
                that.getSwitchCaseList());
        
        switchStatementOrExpression = oss;
        ifStatementOrExpression = ois;
    }

    private void checkSwitch(Tree.SwitchClause switchClause,
            Tree.SwitchCaseList switchCaseList) {
        Tree.Switched switched = 
                switchClause.getSwitched();
        if (switched!=null) {
            Type switchExpressionType = 
                    getSwitchedExpressionType(switched);
            if (switchCaseList!=null && 
                switchExpressionType!=null) {
                checkSwitchCases(switchCaseList);
                if (switchCaseList.getElseClause()==null) {
                    checkSwitchExhaustive(
                            switchClause, switchCaseList, 
                            switchExpressionType);
                }
            }
        }
    }

    private void checkSwitchExhaustive(
            Tree.SwitchClause switchClause, 
            Tree.SwitchCaseList switchCaseList,
            Type switchExpressionType) {
        if (!isTypeUnknown(switchExpressionType)) {
            Type caseUnionType = 
                    caseUnionType(switchCaseList);
            if (caseUnionType!=null) {
                //if the union of the case types covers 
                //the switch expression type then the 
                //switch is exhaustive
                if (!caseUnionType.covers(switchExpressionType)) {
                    switchClause.addError(
                            "case types must cover all cases of the switch type or an else clause must appear: '" +
                            caseUnionType.asString(unit) + 
                            "' does not cover '" + 
                            switchExpressionType.asString(unit) + "'", 
                            10000);
                }
            }
        }
    }

    private static Type getSwitchedExpressionType(
            Tree.Switched switched) {
        if (switched!=null) {
            Tree.Expression e = switched.getExpression();
            Tree.Variable v = switched.getVariable();
            if (e!=null) {
                return e.getTypeModel();
            }
            else if (v!=null) {
                Tree.Type t = v.getType();
                if (t!=null) {
                    return t.getTypeModel();
                }
            }
        }
        return null;
    }
    
    private static boolean switchHasUncheckedNulls(
            Tree.Switched switched) {
        if (switched!=null) {
            Tree.Expression e = switched.getExpression();
            Tree.Variable v = switched.getVariable();
            if (e!=null) {
                return hasUncheckedNulls(e);
            }
            else if (v!=null) {
                Tree.SpecifierExpression se = 
                        v.getSpecifierExpression();
                if (se!=null) {
                    e = se.getExpression();
                    return hasUncheckedNulls(e);
                }
            }
        }
        return false;
    }
    
    @Override
    public void visit(Tree.IfStatement that) {
        Node ois = ifStatementOrExpression;
        Node oss = switchStatementOrExpression;
        ifStatementOrExpression = that;
        switchStatementOrExpression = null;
        super.visit(that);
        ifStatementOrExpression = ois;
        switchStatementOrExpression = oss;
    }
    
    @Override
    public void visit(Tree.ElseClause that) {
        Tree.Variable var = that.getVariable();
        if (var!=null) {
            var.visit(this);
            initOriginalDeclaration(var);
            if (switchStatementOrExpression!=null) {
                Tree.Switched switched = 
                        switchClause().getSwitched();
                if (switched!=null) {
                    setTypeForElseVariable(var, switched);
                }
            }
            if (ifStatementOrExpression!=null) {
                Tree.ConditionList conditionList = 
                        ifClause().getConditionList();
                if (conditionList!=null) {
                    setTypeForGuardedVariable(var, 
                            conditionList, true);
                }
            }
        }
        Tree.Block block = that.getBlock();
        if (block!=null) block.visit(this);
        Tree.Expression expression = that.getExpression();
        if (expression!=null) expression.visit(this);
    }

    private void setTypeForElseVariable(Tree.Variable var, 
            Tree.Switched switched) {
        Type switchExpressionType = 
                getSwitchedExpressionType(switched);
        Tree.SwitchCaseList switchCaseList = 
                switchCaseList();
        if (switchExpressionType!=null && 
                switchCaseList!=null) {
            if (!isTypeUnknown(switchExpressionType)) {
                Type caseUnionType = 
                        caseUnionType(switchCaseList);
                if (caseUnionType!=null) {
                    Type complementType = 
                            switchExpressionType
                                .minus(caseUnionType);
                    complementType =
                            unit.denotableType(
                                    complementType);
                    Value dec = var.getDeclarationModel();
                    if (!isCompletelyVisible(dec, complementType)) {
                        complementType = switchExpressionType;
                    }
                    Tree.Type local = var.getType();
                    local.setTypeModel(complementType);
                    dec.setType(complementType);
                    if (local instanceof Tree.LocalModifier
                        && switchHasUncheckedNulls(switched)
                            && caseUnionType.isSubtypeOf(
                                    unit.getObjectType())) {
                        handleUncheckedNulls(
                                (Tree.LocalModifier)
                                    local, 
                                complementType, 
                                switched.getExpression(), //TODO!!! 
                                dec);
                    }
                    else {
                        dec.setUncheckedNullType(false);
                    }
                }
            }
        }
    }

    private void setTypeForGuardedVariable(Tree.Variable var, 
            Tree.ConditionList conditionList,
            boolean reversed) {
        Tree.Condition c = 
                conditionList.getConditions()
                    .get(0);
        Tree.SpecifierExpression se = 
                var.getSpecifierExpression();
        if (c instanceof Tree.ExistsCondition) {
            Tree.ExistsCondition ec = 
                    (Tree.ExistsCondition) c;
            inferDefiniteType(var, se, 
                    ec.getNot() ^ reversed);
        }
        else if (c instanceof Tree.NonemptyCondition) {
            Tree.NonemptyCondition nec = 
                    (Tree.NonemptyCondition) c;
            inferNonemptyType(var, se, 
                    nec.getNot() ^ reversed);
        }
        else if (c instanceof Tree.IsCondition) {
            Tree.IsCondition ic = 
                    (Tree.IsCondition) c;
            Tree.Expression ex = se.getExpression();
            Type expressionType = ex.getTypeModel();
            Type type = ic.getType().getTypeModel();
            Type narrowedType = 
                    narrow(type, expressionType,
                            ic.getNot() ^ reversed);
            Value dec = var.getDeclarationModel();
            if (!isCompletelyVisible(dec, narrowedType)) {
                narrowedType = expressionType;
            }
            Tree.Type local = var.getType();
            local.setTypeModel(narrowedType);
            dec.setType(narrowedType);
            if (local instanceof Tree.LocalModifier 
                    && handlesNull(ic, type)
                    && hasUncheckedNulls(ex)) {
               handleUncheckedNulls(
                    (Tree.LocalModifier)
                        local, 
                    narrowedType, ex, dec);
            }
            else {
                dec.setUncheckedNullType(false);
            }
        }
    }

    private boolean handlesNull(Tree.IsCondition ic, Type type) {
        return ic.getNot() ?
                unit.getNullValueType()
                    .isSubtypeOf(type) :
                type.isSubtypeOf(
                    unit.getObjectType());
    }
    
    private void checkSwitchCases(Tree.SwitchCaseList switchCaseList) {
        List<Tree.CaseClause> cases = 
                switchCaseList.getCaseClauses();
        boolean hasIsCase = false;
        for (Tree.CaseClause cc: cases) {
            Tree.CaseItem item = cc.getCaseItem();
            hasIsCase = hasIsCase 
                    || item instanceof Tree.IsCase
                    && item.getToken().getText()
                        .equals("is");
            checkCaseClausesDisjoint(cases, cc);
        }
        if (switchStatementOrExpression!=null) {
            Tree.Switched switched = 
                    switchClause().getSwitched();
            if (switched!=null) {
                Tree.Expression switchExpression =
                        switched.getExpression();
                if (switchExpression!=null) {
                    Tree.Term st = 
                            switchExpression.getTerm();
                    if (st instanceof 
                            Tree.BaseMemberExpression) {
                        Tree.BaseMemberExpression bme = 
                                (Tree.BaseMemberExpression) st;
                        checkReferenceIsNonVariable(bme, true, hasIsCase);
                    }
                    else if (st!=null && hasIsCase) {
                        st.addError("switch expression must be a value reference in switch with type cases", 
                                3102);
                    }
                }
            }
        }   
    }

    private void checkCaseClausesDisjoint(
            List<Tree.CaseClause> cases, 
            Tree.CaseClause cc) {
        if (!cc.getOverlapping()) {
            for (Tree.CaseClause occ: cases) {
                if (occ==cc) break;
                checkCaseClausesDisjoint(cc, occ);
            }
        }
    }

    private Type caseUnionType(Tree.SwitchCaseList switchCaseList) {
        //form the union of all the case types
        List<Tree.CaseClause> caseClauses = 
                switchCaseList.getCaseClauses();
        List<Type> list = 
                new ArrayList<Type>
                    (caseClauses.size());
        for (Tree.CaseClause cc: caseClauses) {
            Type ct = getTypeIgnoringLiteralsAndConstants(cc);
            if (isTypeUnknown(ct)) {
                return null; //Note: early exit!
            }
            else {
                addToUnion(list, ct);
            }
        }
        return ModelUtil.union(list, unit);
    }

    private void checkCaseClausesDisjoint(
            Tree.CaseClause cc, 
            Tree.CaseClause occ) {
        Tree.CaseItem cci = cc.getCaseItem();
        Tree.CaseItem occi = occ.getCaseItem();
        if (cci instanceof Tree.IsCase || 
            occi instanceof Tree.IsCase) {
            checkCasesDisjoint(
                    getType(cc), 
                    getType(occ), 
                    cci, " (use 'else case')");
        }
        else {
            if (checkCasesDisjoint(
                    getType(cc),
                    getTypeIgnoringLiterals(occ), 
                    cci, " (use 'else case')")) {
                checkCasesDisjoint(
                        getTypeIgnoringLiterals(cc),
                        getType(occ), 
                        cci, " (use 'else case')");
            }
        }
        if (cci instanceof Tree.MatchCase && 
            occi instanceof Tree.MatchCase) {
            checkLiteralsDisjoint(
                    (Tree.MatchCase) cci, 
                    (Tree.MatchCase) occi);
        }
    }
    
    private void checkLiteralsDisjoint(Tree.MatchCase cci, Tree.MatchCase occi) {
        for (Tree.Expression e: 
                cci.getExpressionList()
                    .getExpressions()) {
            for (Tree.Expression f: 
                    occi.getExpressionList()
                        .getExpressions()) {
                String msg = disjointLiterals(e, f);
                if (msg!=null) {
                    cci.addError("literal cases must be disjoint: " +
                             msg + " occurs in multiple cases");
                }
            }
        }
    }

    private String disjointLiterals(Tree.Expression e, Tree.Expression f) {
        if (e==null || f==null) {
            return null;
        }
        Tree.Term et = e.getTerm();
        Tree.Term ft = f.getTerm();
        if (et instanceof Tree.Tuple && 
            ft instanceof Tree.Tuple) {
            Tree.Tuple ett = (Tree.Tuple) et;
            Tree.Tuple ftt = (Tree.Tuple) ft;
            Tree.SequencedArgument esa = 
                    ett.getSequencedArgument();
            Tree.SequencedArgument fsa = 
                    ftt.getSequencedArgument();
            List<Tree.PositionalArgument> eargs = 
                    esa == null ?
                    Collections.<Tree.PositionalArgument>emptyList() :
                    esa.getPositionalArguments();
            List<Tree.PositionalArgument> fargs = 
                    fsa == null ?
                    Collections.<Tree.PositionalArgument>emptyList() :
                    fsa.getPositionalArguments();
            if (eargs.size()!=fargs.size()) {
                return null;
            }
            for (int i=0, size=eargs.size(); i<size; i++) {
                Tree.PositionalArgument ee = eargs.get(i);
                Tree.PositionalArgument ff = fargs.get(i);        
                if (ff instanceof Tree.ListedArgument &&
                    ee instanceof Tree.ListedArgument) {
                    Tree.ListedArgument el =
                            (Tree.ListedArgument) ee;
                    Tree.ListedArgument fl =
                            (Tree.ListedArgument) ff;
                    String msg =
                            disjointLiterals(
                                el.getExpression(),
                                fl.getExpression());
                    if (msg==null) {
                        return null;
                    }
                }
                else {
                    return null;
                }
            }
            return "tuple"; //TODO!!
        }
        boolean eneg = et instanceof Tree.NegativeOp;
        boolean fneg = ft instanceof Tree.NegativeOp;
        if (eneg) {
            et = ((Tree.NegativeOp) et).getTerm();
        }
        if (fneg) {
            ft = ((Tree.NegativeOp) ft).getTerm();
        }
        if (et instanceof Tree.Literal && 
            ft instanceof Tree.Literal) {
            String ftv = getLiteralText(ft);
            String etv = getLiteralText(et);
            if (et instanceof Tree.NaturalLiteral && 
                ft instanceof Tree.NaturalLiteral &&
                ((ftv.startsWith("#") && 
                        !etv.startsWith("#")) ||
                (!ftv.startsWith("#") && 
                        etv.startsWith("#")) ||
                (ftv.startsWith("$") && 
                        !etv.startsWith("$")) ||
                (!ftv.startsWith("$") && 
                        etv.startsWith("$")))) {
                f.addUnsupportedError("literal cases with mixed bases not yet supported");
            }
            else if (etv.equals(ftv) && eneg==fneg) {
                return (eneg?"-":"") 
                        + etv.replaceAll("\\p{Cntrl}","?");
            }
        }
        if (et instanceof Tree.MemberOrTypeExpression &&
            ft instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression er = 
                    (Tree.MemberOrTypeExpression) et;
            Tree.MemberOrTypeExpression fr = 
                    (Tree.MemberOrTypeExpression) ft;
            Declaration ed = er.getDeclaration();
            Declaration fd = fr.getDeclaration();
            if (ed!=null && fd!=null && ed.equals(fd)) {
                return "'" + ed.getName(unit) + "'";
            }
        }
        return null;
    }

    private static String getLiteralText(Tree.Term et) {
        String etv = et.getText();
        if (et instanceof Tree.CharLiteral) {
            return "'" + etv + "'"; 
        }
        else if (et instanceof Tree.StringLiteral) {
            return "\"" + etv + "\"";
        }
        else {
            return etv;
        }
    }
    
    private Type getType(Tree.CaseItem ci) {
        Tree.IsCase ic = (Tree.IsCase) ci;
        Tree.Type t = ic.getType();
        if (t!=null) {
            Type type = t.getTypeModel();
            return type/*==null ? null :
                type.getUnionOfCases()*/;
        }
        else {
            return null;
        }
    }
    
    private Type getType(Tree.CaseClause cc) {
        Tree.CaseItem ci = cc.getCaseItem();
        if (ci instanceof Tree.IsCase) {
            return getType(ci);
        }
        else if (ci instanceof Tree.MatchCase) {
            Tree.MatchCase mc = (Tree.MatchCase) ci;
            return getType(mc);
        }
        else {
            return null;
        }
    }

    private Type getType(Tree.MatchCase mc) {
        Tree.MatchList ml = 
                mc.getExpressionList();
        List<Tree.Expression> es = 
                ml.getExpressions();
        List<Tree.Type> ts = 
                ml.getTypes();
        List<Type> list = 
                new ArrayList<Type>
                (es.size() + ts.size());
        for (Tree.Expression e: es) {
            if (e.getTypeModel()!=null) {
                addToUnion(list, e.getTypeModel());
            }
        }
        for (Tree.Type t: ts) {
            Type tm = t.getTypeModel();
            if (tm!=null) {
                addToUnion(list, tm);
            }
        }
        return union(list, unit);
    }

    private Type getTypeIgnoringLiterals(
            Tree.CaseClause cc) {
        Tree.CaseItem ci = cc.getCaseItem();
        if (ci instanceof Tree.IsCase) {
            return getType(ci);
        }
        else if (ci instanceof Tree.MatchCase) {
            Tree.MatchCase mc = 
                    (Tree.MatchCase) ci;
            return getTypeIgnoringLiterals(mc);
        }
        else {
            return null;
        }
    }

    private Type getTypeIgnoringLiterals(Tree.MatchCase mc) {
        Tree.MatchList ml = 
                mc.getExpressionList();
        List<Tree.Expression> es = 
                ml.getExpressions();
        List<Tree.Type> ts = 
                ml.getTypes();
        List<Type> list = 
                new ArrayList<Type>
                (es.size() + ts.size());
        for (Tree.Expression e: es) {
            if (e.getTypeModel()!=null) {
                Tree.Term term = e.getTerm();
                if (!isLiteralCase(term)) {
                    addToUnion(list, 
                            e.getTypeModel());
                }
            }
        }
        for (Tree.Type t: ts) {
            Type tm = t.getTypeModel();
            if (tm!=null) {
                addToUnion(list, tm);
            }
        }
        return union(list, unit);
    }

    private Type getTypeIgnoringLiteralsAndConstants(
            Tree.CaseClause cc) {
        Tree.CaseItem ci = cc.getCaseItem();
        if (ci instanceof Tree.IsCase) {
            return getType(ci);
        }
        else if (ci instanceof Tree.MatchCase) {
            Tree.MatchCase mc = 
                    (Tree.MatchCase) ci;
            return getTypeIgnoringLiteralsAndConstants(mc);
        }
        else {
            return null;
        }
    }

    private Type getTypeIgnoringLiteralsAndConstants(Tree.MatchCase mc) {
        Tree.MatchList ml = 
                mc.getExpressionList();
        List<Tree.Expression> es = 
                ml.getExpressions();
        List<Tree.Type> ts = 
                ml.getTypes();
        List<Type> list = 
                new ArrayList<Type>
                (es.size() + ts.size());
        for (Tree.Expression e: es) {
            Type tm = e.getTypeModel();
            if (tm!=null) {
                Tree.Term term = e.getTerm();
                if (isEnumCase(term)) {
                    addToUnion(list, tm);
                }
            }
        }
        for (Tree.Type t: ts) {
            Type tm = t.getTypeModel();
            if (tm!=null) {
                addToUnion(list, tm);
            }
        }
        return union(list, unit);
    }

    private boolean isLiteralCase(Tree.Term term) {
        return term instanceof Tree.Literal 
            || term instanceof Tree.Tuple 
            || term instanceof Tree.NegativeOp;
    }
    
    @Override
    public void visit(Tree.TryCatchStatement that) {
        super.visit(that);
        for (Tree.CatchClause cc: that.getCatchClauses()) {
            Tree.CatchVariable ccv = cc.getCatchVariable();
            if (ccv!=null && 
                    ccv.getVariable()!=null) {
                Type ct = 
                        ccv.getVariable()
                            .getType()
                            .getTypeModel();
                if (ct!=null) {
                    for (Tree.CatchClause ecc: 
                            that.getCatchClauses()) {
                        Tree.CatchVariable eccv = 
                                ecc.getCatchVariable();
                        if (eccv!=null &&
                                eccv.getVariable()!=null) {
                            if (cc==ecc) break;
                            Type ect = 
                                    eccv.getVariable()
                                        .getType()
                                        .getTypeModel();
                            if (ect!=null) {
                                if (ct.isSubtypeOf(ect)) {
                                    ccv.getVariable()
                                        .getType()
                                        .addError("exception type is already handled by earlier catch clause: '" 
                                                + ct.asString(unit) + "'");
                                }
                                if (ct.isUnion()) {
                                    for (Type ut: 
                                            ct.getCaseTypes()) {
                                        if (ut.isSubtypeOf(ect)) {
                                            ccv.getVariable()
                                                .getType()
                                                .addError("exception type is already handled by earlier catch clause: '"
                                                        + ut.asString(unit) + "'");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.DynamicStatement that) {
        boolean od = dynamic;
        dynamic = true;
        super.visit(that);
        dynamic = od;
    }
    
    /**
     * Checks the types of the given type arguments against
     * the type parameters of the given declaration, and
     * adds errors to the arguments that don't satisfy the
     * constraints. Also adds an error if there were 
     * explicit type arguments and the declaration is not 
     * generic, or if there are the wrong number of type
     * arguments for a generic declaration.
     * 
     * @param dec the declaration
     * @param receiverType the qualifying type
     * @param typeArguments the type arguments to the 
     *        given declaration
     * @param tas the type argument list
     * @param parent a node to add errors to in the case
     *        that the type args are inferred
     */
    private void checkTypeArguments(Declaration dec, 
            Type receiverType, 
            List<Type> typeArguments, 
            Tree.TypeArguments tas, 
            Node parent) {
        
        if (typeArguments!=null && dec!=null) {
            checkVarianceAnnotations(tas, parent);
            
            if (dec.isParameterized()) {
                checkTypeArgumentsOfDeclaration(
                        receiverType, dec, typeArguments, 
                        tas, parent);
                return;
            }
            else if (dec instanceof Value
                    && !typeArguments.isEmpty()) {
                //special case for generic function ref
                Value val = (Value) dec;
                Type type = val.getType();
                if (type!=null) {
                    type = type.resolveAliases();
                    if (type.isTypeConstructor()) {
                        checkTypeArgumentsOfTypeConstructor(
                                typeArguments, tas, type, 
                                parent);
                        return;
                    }
                }
            }
            
            if (tas instanceof Tree.TypeArgumentList) {
                //explicit type argument list to something
                //that cannot possibly accept type arguments
                tas.addError("declaration does not accept type arguments: '" + 
                        dec.getName(unit) + 
                        "' is not a generic declaration");
            }
        }
    }

    private void checkVarianceAnnotations(Tree.TypeArguments tas, Node parent) {
        boolean explicit = 
                tas instanceof Tree.TypeArgumentList;
        if (explicit && enforceTypeConstraints(parent)) {
            Tree.TypeArgumentList tal = 
                    (Tree.TypeArgumentList) tas;
            for (Tree.Type t: tal.getTypes()) {
                if (t instanceof Tree.StaticType) {
                    Tree.StaticType st = 
                            (Tree.StaticType) t;
                    Tree.TypeVariance var = 
                            st.getTypeVariance();
                    if (var!=null) {
                        var.addError("use-site variance annotation may not occur in value or supertype expression");
                    }
                }
            }
        }
    }

    private void checkTypeArgumentsOfDeclaration(
            Type receiver, Declaration dec, 
            List<Type> typeArguments,
            Tree.TypeArguments tas, Node parent) {
        
        boolean explicit = 
                tas instanceof Tree.TypeArgumentList;
        
        List<TypeParameter> params = dec.getTypeParameters();
        int min = 0;
        for (TypeParameter tp: params) { 
            if (!tp.isDefaulted()) min++;
        }
        if (receiver==null 
                && dec.isClassOrInterfaceMember()) {
            receiver = 
                    parent.getScope()
                        .getDeclaringType(dec);
        }
        
        boolean enforceConstraints = 
                enforceTypeConstraints(parent);

        int max = params.size();
        int args = typeArguments.size();
        if (args<=max && args>=min) {
            for (int i=0; i<args; i++) {
                TypeParameter param = params.get(i);
                Type argType = typeArguments.get(i);
                boolean argTypeMeaningful = 
                        argType!=null && 
                        !argType.isUnknown();
                if (argTypeMeaningful) {
                    if (enforceConstraints 
                            && argType.isTypeParameter()) {
                        TypeParameter tp = 
                                (TypeParameter) 
                                    argType.getDeclaration();
                        if (!tp.isReified() && param.isReified()) {
                            if (explicit) {
                                typeArgNode(tas, i, parent)
                                    .addError("type parameter '" 
                                            + param.getName() 
                                            + "' of declaration '" 
                                            + dec.getName(unit)
                                            + "' has argument '" 
                                            + tp.getName()
                                            + "' which is not a reified type");
                            }
                            else {
                                parent.addError("inferred type argument '" 
                                        + tp.getName()
                                        + "' to type parameter '" 
                                        + param.getName()
                                        + "' of declaration '" 
                                        + dec.getName(unit)
                                        + "' is not a reified type");
                            }
                        }
                    }
                    if (param.isTypeConstructor()) {
                        if (!argType.resolveAliases()
                                    .isTypeConstructor()) {
                            typeArgNode(tas, i, parent)
                                .addError("type argument must be a type constructor: parameter '" 
                                        +  param.getName() 
                                        + "' of declaration '" 
                                        + dec.getName(unit)
                                        + "' is a type constructor parameter but '" 
                                        + argType.asString(unit) 
                                        + "' is a regular type");
                        }
                        else {
                            Node argNode;
                            if (explicit) {
                                Tree.TypeArgumentList tl = 
                                        (Tree.TypeArgumentList) tas;
                                argNode = i<tl.getTypes().size() ?
                                        tl.getTypes().get(i) : parent;
                            }
                            else {
                                argNode = parent;
                            }
                            checkTypeConstructorParam(param, 
                                    argType, argNode);
                        }
                    }
                }
                List<Type> sts = param.getSatisfiedTypes();
                boolean hasConstraints = 
                        !sts.isEmpty() || 
                        param.getCaseTypes()!=null;
                if (//!isCondition && 
                        hasConstraints &&
                        enforceConstraints) {
                    Type assignedType = 
                            argumentTypeForBoundsCheck(param, 
                                    argType);
                    for (Type st: sts) {
                        Type bound =
                                st.appliedType(receiver, 
                                        dec, typeArguments);
                        if (!assignedType.isSubtypeOf(bound)) {
                            if (argTypeMeaningful) {
                                if (explicit) {
                                    typeArgNode(tas, i, parent)
                                        .addError("type parameter '" 
                                                + param.getName() 
                                                + "' of declaration '" 
                                                + dec.getName(unit)
                                                + "' has argument '" 
                                                + assignedType.asString(unit) 
                                                + "' which is not assignable to upper bound '" 
                                                + bound.asString(unit)
                                                + "' of '" 
                                                + param.getName() 
                                                + "'", 
                                                2102);
                                }
                                else {
                                    parent.addError("inferred type argument '" 
                                            + assignedType.asString(unit)
                                            + "' to type parameter '" 
                                            + param.getName()
                                            + "' of declaration '" 
                                            + dec.getName(unit)
                                            + "' is not assignable to upper bound '" 
                                            + bound.asString(unit)
                                            + "' of '" 
                                            + param.getName() 
                                            + "'");
                                }
                            }
                            return;
                        }
                    }
                    if (!argumentSatisfiesEnumeratedConstraint(receiver, 
                            dec, typeArguments, assignedType, param)) {
                        if (argTypeMeaningful) {
                            if (explicit) {
                                typeArgNode(tas, i, parent)
                                    .addError("type parameter '" 
                                            + param.getName() 
                                            + "' of declaration '" 
                                            + dec.getName(unit)
                                            + "' has argument '" 
                                            + assignedType.asString(unit) 
                                            + "' which is not one of the enumerated cases of '" 
                                            + param.getName() 
                                            + "'");
                            }
                            else {
                                parent.addError("inferred type argument '" 
                                        + assignedType.asString(unit)
                                        + "' to type parameter '" 
                                        + param.getName()
                                        + "' of declaration '" 
                                        + dec.getName(unit)
                                        + "' is not one of the enumerated cases of '" 
                                        + param.getName() 
                                        + "'");
                            }
                        }
                    }
                }
            }
        }
        else {
            if (explicit) {
                StringBuilder paramList = typeParameterList(dec);
                String help;
                if (args<min) {
                    help = " requires at least " + min + 
                            " type arguments to " +
                            paramList;
                }
                else if (args>max) {
                    help = " allows at most " + max + 
                            " type arguments to " + 
                            paramList;
                }
                else {
                    help = "";
                }
                tas.addError("wrong number of type arguments: '" 
                        + dec.getName(unit) 
                        + "'" 
                        + help);
            }
            else {
                //Now handled in TypeArgumentVisitor
//                    if (!metamodel) {
//                        parent.addError("missing type arguments to generic type: '" + 
//                                dec.getName(unit) + 
//                                "' declares type parameters");
//                    }
            }
        }
    }

    private boolean enforceTypeConstraints(Node parent) {
        //Note: class metamodel expressions return 
        //      instantiable references, so enforce type
        //      constraints and disallow use-site variance
        //TODO: we could stop enforcing type constraints
        //      in metamodel expressions, and have them
        //      produce types, not classes
        return modelLiteral 
            || !(parent instanceof Tree.SimpleType) 
            || ((Tree.SimpleType) parent).getInherited();
    }

    private static Type argumentTypeForBoundsCheck(
            TypeParameter param, Type argType) {
        if (argType==null) {
            return null;
        }
        else {
            TypeDeclaration dec = argType.getDeclaration();
            if (argType.isTypeConstructor()) {
                return dec.appliedType(null, 
                        param.getType()
                             .getTypeArgumentList());
            }
            else if (argType.isClass() && !dec.isJava()) {
                //ceylon classes are implicitly Serializable
                Unit unit = param.getUnit();
                Interface sd = 
                        unit.getJavaSerializableDeclaration();
                if (sd!=null
                        && param.inherits(sd)
                        && !dec.inherits(sd)) {
                    return intersectionType(argType, 
                            sd.getType(), unit);
                }
            }
            return argType;
        }
    }

    private static Node typeArgNode(Tree.TypeArguments tas, 
            int i, Node parent) {
        if (tas instanceof Tree.TypeArgumentList) {
            Tree.TypeArgumentList tal = 
                    (Tree.TypeArgumentList) tas;
            List<Tree.Type> types = tal.getTypes();
            return types.isEmpty() ? parent : types.get(i);
        }
        else {
            return parent;
        }
    }

    private void checkTypeArgumentsOfTypeConstructor(
            List<Type> typeArguments,
            Tree.TypeArguments tas, Type type,
            Node parent) {
        
        boolean explicit = 
                tas instanceof Tree.TypeArgumentList;
        
        TypeDeclaration tcd = type.getDeclaration();
        List<TypeParameter> typeParameters = 
                tcd.getTypeParameters();
        int size = typeParameters.size();
        if (size != typeArguments.size()) {
            String msg = 
                    size==1 ? 
                        "1 type argument" : 
                        size + " type arguments";
            if (explicit) {
                tas.addError("wrong number of type arguments: type constructor '" 
                        + type.asString(unit) 
                        + "' requires " 
                        + msg);
            }
            else {
                parent.addError("missing type arguments: type constructor '"
                        + type.asString(unit) 
                        + "' requires " 
                        + msg);
            }
        }
        else {
            Map<TypeParameter, Type> args = 
                    typeArgumentsAsMap(tcd, typeArguments);
            for (int i=0; i<size; i++) {
                TypeParameter param = typeParameters.get(i);
                Type arg = typeArguments.get(i);
                if (!isTypeUnknown(arg)) {
                    List<Type> sts = 
                            param.getSatisfiedTypes();
                    for (Type st: sts) {
                        Type ub = st.substitute(args, null);
                        if (!isTypeUnknown(ub) 
                                && !arg.isSubtypeOf(ub)) {
                            String message = 
                                    "type argument '" 
                                    + arg.asString(unit) 
                                    + "' is not assignable to upper bound '" 
                                    + ub.asString(unit) 
                                    + "' of type parameter '" 
                                    + param.getName() 
                                    + "' of '" 
                                    + param.getDeclaration()
                                        .getName(unit) 
                                    + "'";
                            if (explicit) {
                                typeArgNode(tas, i, parent)
                                    .addError(message);
                            }
                            else {
                                parent.addError("inferred " + 
                                        message);
                            }
                        }
                    }
                }
                if (!satisfiesEnumeratedConstraint(
                        param, arg, args)) {
                    String message = 
                            "type argument '" 
                            + arg.asString(unit) 
                            + "' is not one of the enumerated cases of the type parameter '" 
                            + param.getName() 
                            + "' of '" 
                            + param.getDeclaration()
                                .getName(unit) 
                            + "'";
                    if (explicit) {
                        typeArgNode(tas, i, parent)
                            .addError(message);
                    }
                    else {
                        parent.addError("inferred " + 
                                message);
                    }
                }
            }
        }
    }

    boolean satisfiesEnumeratedConstraint(
            TypeParameter param,
            Type arg,
            Map<TypeParameter, Type> args) {
        List<Type> cts = param.getCaseTypes();
        if (cts!=null) {
            for (Type ct: cts) {
                Type eb = ct.substitute(args, null);
                if (arg.isSubtypeOf(eb)) {
                    return true;
                }
            }
            if (arg.isTypeParameter()) {
                for (Type act: arg.getCaseTypes()) {
                    boolean foundCase = false;
                    for (Type ct: cts) {
                        Type eb = ct.substitute(args, null);
                        if (act.isSubtypeOf(eb)) {
                            foundCase = true;
                        }
                    }
                    if(!foundCase) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return true;
    }

    private void checkTypeConstructorParam(TypeParameter param, 
            Type argType, Node argNode) {
        argType = argType.resolveAliases();
        
        if (!argType.isTypeConstructor()) {
            argNode.addError("not a type constructor: '" 
                    + argType.asString(unit) 
                    + "' is a regular type but '" 
                    + param.getName(unit) 
                    + "' expects a type constructor");
        }
        else {
            argType = unwrapAliasedTypeConstructor(argType);
            if (argType.isUnion()) {
                for (Type ct: argType.getCaseTypes()) {
                    checkTypeConstructorParam(param, 
                            ct, argNode);
                }
            }
            else if (argType.isIntersection()) {
                for (Type st: argType.getSatisfiedTypes()) {
                    checkTypeConstructorParam(param, 
                            st, argNode);
                }
            }
            else if (argType.isNothing()) {
                //just let it through?!
            }
            else {
                TypeDeclaration argTypeDec = 
                        argType.getDeclaration();
                List<TypeParameter> argTypeParams = 
                        argTypeDec.getTypeParameters();
                int allowed = argTypeParams.size();
                int required = 0;
                for (TypeParameter tp: argTypeParams) {
                    if (tp.isDefaulted()) {
                        break;
                    }
                    required++;
                }
                List<TypeParameter> paramTypeParams = 
                        param.getTypeParameters();
                int size = paramTypeParams.size();
                if (allowed<size || required>size) {
                    argNode.addError(
                            "argument type constructor has wrong number of type parameters: argument '" 
                            + argTypeDec.getName(unit) 
                            + "' has " 
                            + allowed 
                            + " type parameters " 
                            + "but parameter '" 
                            + param.getName(unit) 
                            + "' has " 
                            + size 
                            + " type parameters");
                }
                for (int j=0; j<size && j<allowed; j++) {
                    TypeParameter paramParam = 
                            paramTypeParams.get(j);
                    TypeParameter argParam = 
                            argTypeParams.get(j);
                    if (paramParam.isCovariant() &&
                            !argParam.isCovariant()) {
                        argNode.addError(
                                "argument type constructor is not covariant: '" 
                                + argParam.getName() 
                                + "' of '" 
                                + argTypeDec.getName(unit) 
                                + "' must have the same variance as '" 
                                + paramParam.getName() 
                                + "' of '" 
                                + param.getName(unit) 
                                + "'");
                    }
                    else if (paramParam.isContravariant() &&
                            !argParam.isContravariant()) {
                        argNode.addError(
                                "argument type constructor is not contravariant: '" 
                                + argParam.getName() 
                                + "' of '" 
                                + argTypeDec.getName(unit) 
                                + "' must have the same variance as '" 
                                + paramParam.getName() 
                                + "' of '" 
                                + param.getName(unit) 
                                + "'");
                    }
                    if (!intersectionOfSupertypes(paramParam)
                            .isSubtypeOf(intersectionOfSupertypes(argParam))) {
                        argNode.addError(
                                "upper bound on type parameter of argument type constructor is not a supertype of upper bound on corresponding type parameter of parameter: '" 
                                + argParam.getName() 
                                + "' of '" 
                                + argTypeDec.getName(unit) 
                                + "' does accept all type arguments accepted by '" 
                                + paramParam.getName() 
                                + "' of '" 
                                + param.getName(unit) 
                                + "'");
                    }
                    if (!unionOfCaseTypes(paramParam)
                            .isSubtypeOf(unionOfCaseTypes(argParam))) {
                        argNode.addError(
                                "enumerated bound on type parameter of argument type constructor is not a supertype of enumerated bound on corresponding type parameter of parameter: '" 
                                + argParam.getName() 
                                + "' of '" 
                                + argTypeDec.getName(unit) 
                                + "' does accept all type arguments accepted by '" 
                                + paramParam.getName() 
                                + "' of '" 
                                + param.getName(unit) 
                                + "'");
                    }
                }
            }
        }
    }

    private void visitExtendedOrAliasedType(Tree.SimpleType et,
            Tree.InvocationExpression ie) {
        if (et!=null && ie!=null) {
            Type type = et.getTypeModel();
            if (type!=null) {
                Tree.Primary primary = ie.getPrimary();
                if (primary instanceof Tree.InvocationExpression) {
                    Tree.InvocationExpression iie = 
                            (Tree.InvocationExpression) 
                                primary;
                    primary = iie.getPrimary();
                }
                if (primary instanceof Tree.ExtendedTypeExpression) {
                    Tree.ExtendedTypeExpression ete = 
                            (Tree.ExtendedTypeExpression) 
                                primary;
                    ete.setDeclaration(et.getDeclarationModel());
                    ete.setTarget(type);
                    Type qt = type.getQualifyingType();
                    Type ft = type.getFullType();
                    if (ete.getStaticMethodReference()) {
                        ft = getStaticReferenceType(ft, qt);
                    }
                    primary.setTypeModel(ft);
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.ClassSpecifier that) {
        visitExtendedOrAliasedType(that.getType(), 
                that.getInvocationExpression());
        
        inExtendsClause = true;
        super.visit(that);
        inExtendsClause = false;
        
    }
    
    @Override 
    public void visit(Tree.Enumerated that) {
        Constructor e = that.getEnumerated();
        checkDelegatedConstructor(
                that.getDelegatedConstructor(), 
                e, that);
        TypeDeclaration occ = enterConstructorDelegation(e);
        Declaration od = beginReturnDeclaration(e);
        Tree.Type rt = beginReturnScope(fakeVoid(that));
        super.visit(that);
        endReturnScope(rt, null);
        endReturnDeclaration(od);
        endConstructorDelegation(occ);
    }
    
    @Override 
    public void visit(Tree.Constructor that) {
        Constructor c = that.getConstructor();
        checkDelegatedConstructor(
                that.getDelegatedConstructor(), 
                c, that);
        TypeDeclaration occ = enterConstructorDelegation(c);
        Declaration od = beginReturnDeclaration(c);
        Tree.Type rt = beginReturnScope(fakeVoid(that));
        super.visit(that);
        endReturnScope(rt, null);
        endReturnDeclaration(od);
        endConstructorDelegation(occ);
    }

    private void endConstructorDelegation(TypeDeclaration occ) {
        constructorClass = occ;
    }

    private TypeDeclaration enterConstructorDelegation(Constructor c) {
        TypeDeclaration occ = constructorClass;
        Type et = c.getExtendedType();
        constructorClass = 
                et==null ? null :
                    et.getDeclaration();
        return occ;
    }

    protected void checkDelegatedConstructor(
            Tree.DelegatedConstructor dc, Constructor c, 
            Node node) {
        if (dc==null) {
            if (c.isClassMember()) {
                Class clazz = (Class) c.getContainer();
                Type et = clazz.getExtendedType();
                if (et!=null && !et.isBasic()) {
                    TypeDeclaration superclass = 
                            et.getDeclaration();
                    if (superclass!=null) {
                        node.addError("constructor must explicitly delegate to some superclass constructor: '" +
                                clazz.getName() + "' extends '" + 
                                superclass.getName() + "'");
                    }
                }
            }
        }
    }
    
    @Override 
    public void visit(Tree.DelegatedConstructor that) {
        visitExtendedOrAliasedType(that.getType(), 
                that.getInvocationExpression());
        
        inExtendsClause = true;
        super.visit(that);
        inExtendsClause = false;
    }

    @Override 
    public void visit(Tree.ExtendedType that) {
        visitExtendedOrAliasedType(that.getType(), 
                that.getInvocationExpression());
        
        inExtendsClause = true;
        super.visit(that);
        inExtendsClause = false;
    }

    @Override public void visit(Tree.Term that) {
        super.visit(that);
        if (that.getTypeModel()==null) {
            that.setTypeModel(defaultType());
        }
    }

    @Override public void visit(Tree.Type that) {
        super.visit(that);
        if (that.getTypeModel()==null) {
            that.setTypeModel(defaultType());
        }
    }
    
    @Override public void visit(Tree.TypeConstructor that) {
        super.visit(that);
        checkNotJvm(that, 
                "type functions are not supported on the JVM");
    }
    
    @Override public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        Tree.TypeParameterList typeParams = 
                that.getTypeParameterList();
        if (typeParams!=null) {
            checkNotJvm(typeParams, 
                    "type functions are not supported on the JVM: type parameter is generic (remove type parameters)");
        }
    }
    
    @Override
    public void visit(Tree.FunctionalParameterDeclaration that) {
        super.visit(that);
        Tree.MethodDeclaration md = 
                (Tree.MethodDeclaration) 
                    that.getTypedDeclaration();
        Tree.TypeParameterList tpl = 
                md.getTypeParameterList();
        if (tpl!=null) {
            checkNotJvm(tpl, 
                    "type functions are not supported on the JVM: '" + 
                    md.getDeclarationModel().getName() + 
                    "' is generic (remove type parameters)");
        }
    }
    
    private Type defaultType() {
        TypeDeclaration ut = new UnknownType(unit);
        Class ad = unit.getAnythingDeclaration();
        if (ad!=null) {
            ut.setExtendedType(ad.getType());
        }
        return ut.getType();
    }
    
    @Override
    public void visit(Tree.PackageLiteral that) {
        super.visit(that);
        
        Tree.ImportPath path = that.getImportPath();
        if (path==null) {
            path = new Tree.ImportPath(null);
            that.setImportPath(path);
        }
        
        Package pack = 
                path.getIdentifiers().isEmpty() ?
                        unit.getPackage() :
                        importedPackage(path, unit);
        path.setModel(pack);
        that.setTypeModel(unit.getPackageDeclarationType());
    }
    
    @Override
    public void visit(Tree.ModuleLiteral that) {
        super.visit(that);
        
        Tree.ImportPath path = that.getImportPath();
        if (path==null) {
            path = new Tree.ImportPath(null);
            that.setImportPath(path);
        }
        
        Module mod = 
                path.getIdentifiers().isEmpty() ?
                        unit.getPackage().getModule() :
                        importedModule(path, 
                                that.getRestriction());
        path.setModel(mod);
        that.setTypeModel(unit.getModuleDeclarationType());
    }
    
    private boolean declarationLiteral = false;
    private boolean modelLiteral = false;
    
    @Override
    public void visit(Tree.TypeArgumentList that) {
        if (declarationLiteral) {
            that.addError("declaration reference may not specify type arguments");
        }
        super.visit(that);
    }
    
    @Override
    public void visit(Tree.TypeLiteral that) {
        boolean isDeclaration = 
                that instanceof Tree.InterfaceLiteral
                || that instanceof Tree.ClassLiteral
                || that instanceof Tree.NewLiteral
                || that instanceof Tree.AliasLiteral
                || that instanceof Tree.TypeParameterLiteral;
        if (isDeclaration) {
            declarationLiteral = true;
        }
        else {
            modelLiteral = true;
        }
        try {
            super.visit(that);
        }
        finally {
            declarationLiteral = false;
            modelLiteral = false;
        }
        Type t;
        TypeDeclaration d;
        Tree.StaticType type = that.getType();
        Node errorNode;
        if (type!=null) {
            t = type.getTypeModel();
            d = t.getDeclaration();
            errorNode = type;
        }
        else {
            errorNode = that;
            ClassOrInterface classOrInterface = 
                    getContainingClassOrInterface(
                            that.getScope());
            if (that instanceof Tree.ClassLiteral ||
                that instanceof Tree.InterfaceLiteral) {
                d = classOrInterface;
                if (d==null) {
                    errorNode.addError("no containing type");
                    return; //EARLY EXIT!!
                }
                else {
                    t = classOrInterface.getType();
                }
            }
            else {
                errorNode.addError("missing type reference");
                return; //EARLY EXIT!!
            }
        }
        if (t!=null) {
            that.setDeclaration(d);
            that.setWantsDeclaration(true);
            if (that instanceof Tree.ClassLiteral) {
                if (!(d instanceof Class)) {
                    if (d != null) {
                        errorNode.addError("referenced declaration is not a class" +
                                getDeclarationReferenceSuggestion(d));
                    }
                    that.setTypeModel(unit.getClassDeclarationType());
                }
                else {
                    that.setTypeModel(unit.getClassDeclarationType((Class) d));

                }
            }
            else if (that instanceof Tree.NewLiteral) {
                if (d instanceof Class) {
                    Class c = (Class) d;
                    Constructor defaultConstructor = 
                            c.getDefaultConstructor();
                    if (defaultConstructor!=null) {
                        d = defaultConstructor;
                    }
                }
                if (d instanceof Constructor) {
                    Constructor c = (Constructor) d;
                    if (c.getParameterList()==null) {
                        that.setTypeModel(unit.getValueConstructorDeclarationType());
                    }
                    else {
                        that.setTypeModel(unit.getCallableConstructorDeclarationType());
                    }
                }
                else if (d!=null) {
                    errorNode.addError("referenced declaration is not a constructor" +
                            getDeclarationReferenceSuggestion(d));
                }
            }
            else if (that instanceof Tree.InterfaceLiteral) {
                if (!(d instanceof Interface)) {
                    if (d!=null) {
                        errorNode.addError("referenced declaration is not an interface" +
                                getDeclarationReferenceSuggestion(d));
                    }
                }
                that.setTypeModel(unit.getInterfaceDeclarationType());
            }
            else if (that instanceof Tree.AliasLiteral) {
                if (!(d instanceof TypeAlias)) {
                    errorNode.addError("referenced declaration is not a type alias" +
                            getDeclarationReferenceSuggestion(d));
                }
                that.setTypeModel(unit.getAliasDeclarationType());
            }
            else if (that instanceof Tree.TypeParameterLiteral) {
                if (!(d instanceof TypeParameter)) {
                    errorNode.addError("referenced declaration is not a type parameter" +
                            getDeclarationReferenceSuggestion(d));
                }
                that.setTypeModel(unit.getTypeParameterDeclarationType());
            }
            else if (d != null) {
                that.setWantsDeclaration(false);
                t = t.resolveAliases();
                if (t==null || t.isUnknown()) {
                    return;
                }
                //checkNonlocalType(that.getType(), t.getDeclaration());
                if (d instanceof Constructor) {
                    if (((Constructor) d).isAbstraction()) {
                        errorNode.addError("constructor is overloaded");
                    }
                    else {
                        that.setTypeModel(unit.getConstructorMetatype(t));
                    }
                }
                else if (d instanceof Class) {
//                    checkNonlocal(that, t.getDeclaration());
                    that.setTypeModel(unit.getClassMetatype(t));
                }
                else if (d instanceof Interface) {
                    that.setTypeModel(unit.getInterfaceMetatype(t));
                }
                else {
                    that.setTypeModel(unit.getTypeMetaType(t));
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.MemberLiteral that) {
        if (that instanceof Tree.FunctionLiteral ||
            that instanceof Tree.ValueLiteral) {
            declarationLiteral = true;
        }
        else {
            modelLiteral = true;
        }
        try {
            super.visit(that);
        }
        finally {
            declarationLiteral = false;
            modelLiteral = false;
        }
        Tree.Identifier id = that.getIdentifier();
        if (id!=null) {
            String name = name(id);
            Tree.StaticType type = that.getType();
            if (type == null) {
                TypedDeclaration result;
                if (that.getPackageQualified()) {
                    result = 
                            getPackageTypedDeclaration(
                                    name, 
                                    null, false, unit);
                }
                else {
                    result = 
                            getTypedDeclaration(
                                    that.getScope(), name, 
                                    null, false, unit);
                }
                if (result!=null) {
                    checkBaseVisibility(that, result, name);
                    setMemberMetatype(that, result);
                }
                else {
                    that.addError("function or value is not defined: '" +
                            name(id) + "'", 100);
                    unit.setUnresolvedReferences();
                }
            } else {
                TypeDeclaration qtd = 
                        type.getTypeModel()
                            .getDeclaration();
                //checkNonlocalType(that.getType(), qtd);
                String container = "type '" + qtd.getName(unit) + "'";
                TypedDeclaration member = 
                        getTypedMember(qtd, name, 
                                null, false, unit, 
                                that.getScope());
                if (member==null) {
                    if (qtd.isMemberAmbiguous(name, unit, null, false)) {
                        that.addError("method or attribute is ambiguous: '" +
                                name + "' for " + container);
                    }
                    else {
                        that.addError("method or attribute is not defined: '" +
                                name + "' in " + container);
                    }
                }
                else {
                    checkQualifiedVisibility(that, member, 
                            name, container, false);
                    setMemberMetatype(that, member);
                }
            }
        }
    }

    private void setMemberMetatype(Tree.MemberLiteral that, 
            TypedDeclaration result) {
        that.setDeclaration(result);
        if (that instanceof Tree.ValueLiteral) {
            if (result instanceof Value) {
                checkNonlocal(that, result);
            }
            else {
                that.getIdentifier()
                    .addError("referenced declaration is not a value" + 
                            getDeclarationReferenceSuggestion(result));
            }
            if (that.getBroken()) {
                that.addError("keyword object may not appear here: " +
                              "use the value keyword to refer to anonymous class declarations");
            }
            that.setWantsDeclaration(true);
            that.setTypeModel(unit.getValueDeclarationType());
        }
        else if (that instanceof Tree.FunctionLiteral) {
            if (result instanceof Function) {
                checkNonlocal(that, result);
            }
            else {
                that.getIdentifier()
                    .addError("referenced declaration is not a function" + 
                            getDeclarationReferenceSuggestion(result));
            }
            that.setWantsDeclaration(true);
            that.setTypeModel(unit.getFunctionDeclarationType());
        }
        else {
            // constructors look like functions but they're always members, they can't
            // ever be local
            if(!ModelUtil.isConstructor(result))
                checkNonlocal(that, result);
            setMetamodelType(that, result);
        }
    }

    private String getDeclarationReferenceSuggestion(Declaration result) {
        String name = ": " + result.getName(unit);
        if (result instanceof Function) {
            return name + " is a function";
        }
        else if (result instanceof Value) {
            return name + " is a value";
        }
        else if (result instanceof Constructor) {
            return name + " is a constructor";
        }
        else if (result instanceof Class) {
            return name + " is a class";
        }
        else if (result instanceof Interface) {
            return name + " is an interface";
        }
        else if (result instanceof TypeAlias) {
            return name + " is a type alias";
        }
        else if (result instanceof TypeParameter) {
            return name + " is a type parameter";
        }
        return "";
    }

    private void setMetamodelType(Tree.MemberLiteral that, 
            Declaration result) {
        Type outerType;
        if (result.isClassOrInterfaceMember()) {
            Tree.StaticType type = that.getType();
            outerType = type==null ? 
                    that.getScope()
                        .getDeclaringType(result) : 
                    type.getTypeModel();            
        }
        else {
            outerType = null;
        }
        boolean constructor = isConstructor(result);
        if (result instanceof Function) {
            Function method = (Function) result;
            if (method.isAbstraction()) {
                that.addError("method is overloaded");
            }
            else {
                Tree.TypeArgumentList tal = 
                        that.getTypeArgumentList();
                if (explicitTypeArguments(method, tal)) {
                    List<Type> typeArgs = 
                            getTypeArguments(tal, outerType, 
                                    method.getTypeParameters());
                    if (tal!=null) {
                        tal.setTypeModels(typeArgs);
                    }
                    checkTypeArguments(method, outerType, 
                            typeArgs, tal, that);
                    TypedReference pr = outerType==null ? 
                            method.appliedTypedReference(null, typeArgs) : 
                            outerType.getTypedMember(method, typeArgs);
                    that.setTarget(pr);
                    Type metatype = constructor ?
                            unit.getConstructorMetatype(pr) :
                            unit.getFunctionMetatype(pr);
                    that.setTypeModel(metatype);
                }
                else {
                    that.addError("missing type arguments to generic declaration: '" + 
                            method.getName(unit) + "'");
                }
            }
        }
        else if (result instanceof Value) {
            Value value = (Value) result;
            if (that.getTypeArgumentList() != null) {
                that.addError("does not accept type arguments: '" + 
                        result.getName(unit) + "' is a value");
            }
            else {
                TypedReference reference = 
                        value.appliedTypedReference(outerType, 
                                NO_TYPE_ARGS);
                that.setTarget(reference);
                Type metatype = constructor ?
                        unit.getValueConstructorMetatype(reference):
                        unit.getValueMetatype(reference);
                that.setTypeModel(metatype);
            }
        }
    }

    private void checkNonlocal(Node that, Declaration declaration) {
        if ((!declaration.isClassOrInterfaceMember() || !declaration.isShared())
                    && !declaration.isToplevel()) {
            that.addError("metamodel reference to local declaration");
        }
    }
    
    /*private void checkNonlocalType(Node that, TypeDeclaration declaration) {
        if (declaration instanceof UnionType) {
            for (TypeDeclaration ctd: declaration.getCaseTypeDeclarations()) {
                checkNonlocalType(that, ctd);
            }
        }
        if (declaration instanceof IntersectionType) {
            for (TypeDeclaration std: declaration.getSatisfiedTypeDeclarations()) {
                checkNonlocalType(that, std);
            }
        }
        else if (declaration instanceof ClassOrInterface &&
                (!declaration.isClassOrInterfaceMember()||!declaration.isShared())
                        && !declaration.isToplevel()) {
            that.addWarning("metamodel reference to local type not yet supported");
        }
        else if (declaration.getContainer() instanceof TypeDeclaration) {
            checkNonlocalType(that, (TypeDeclaration) declaration.getContainer());
        }
    }*/
    
    private Declaration handleAbstractionOrHeader(
            Declaration dec, 
            Tree.MemberOrTypeExpression that, 
            boolean error) {
        return handleAbstraction(
                handleNativeHeader(dec, that, error), 
                that);
    }
    
    private Declaration handleNativeHeader(
            Declaration dec, 
            Node that, 
            boolean error) {
        //really nasty workaround to get the "real" scope
        //in which an annotation occurs! (bug #7143)
        Scope scope = 
                (that instanceof Tree.BaseMemberExpression ? 
                ((Tree.BaseMemberExpression) that).getIdentifier() :
                that).getScope();
        
        Declaration impl = dec;
        Declaration hdr = null;
        Module ctxModule = 
                unit.getPackage()
                    .getModule();
        Module decModule = 
                dec.getUnit()
                    .getPackage()
                    .getModule();
        Backends decModuleBackends =
                getModuleBackends(decModule, unit);
        Backends inBackends = 
                scope.getScopedBackends();
        if (dec.isNative()) {
            Backends backends = 
                    inBackends.none() ?
                            unit.getSupportedBackends() :
                            inBackends;
            if (dec.isNativeHeader()) {
                hdr = dec;
                impl = getNativeDeclaration(hdr, backends);
            }
            else {
                Declaration tmp = getNativeHeader(dec);
                if (tmp != dec) {
                    hdr = tmp;
                    if (hdr != null) {
                        if (backends.none()
                                || !backends.supports(
                                        dec.getNativeBackends())) {
                            impl = getNativeDeclaration(hdr, backends);
                        }
                    }
                }
            }
        }
        if (error
                && impl != null
                && (dec.isToplevel() || dec.isMember())
                && declarationScope(scope) != null
                && (hdr == null || !isImplemented(hdr))
                && (ctxModule != decModule
                        && !decModuleBackends.none()
                    || ctxModule == decModule
                        && dec.isNative()
                        && hdr == null)
                && (inBackends.none()
                    || impl.isNative()
                        && !isForBackend(
                                impl.getNativeBackends(), 
                                inBackends)
                    || !decModuleBackends.none()
                        && !isForBackend(decModuleBackends, 
                                inBackends))) {
            Declaration d = declarationScope(scope);
            if (!inBackends.none()) {
                that.addError("illegal reference to native declaration '" + 
                        dec.getName(unit) + "': native declaration '" +
                        d.getName(unit) + "' has a different backend");
            } else {
                that.addError("illegal reference to native declaration '" +
                        dec.getName(unit) + "': declaration '" +
                        d.getName(unit) + "' is not native (mark it or the module native)",
                        20010);
            }
        }
        if (dec.isNative()) {
            return inBackends.none() || impl==null ? 
                    dec : impl;
        }
        return dec;
    }

    private Backends getModuleBackends(Module decModule, Unit unit) {
        Backends bs = decModule.getNativeBackends();
        List<ModuleImport> imports = 
                unit.getPackage()
                    .getModule()
                    .getImports();
        for (ModuleImport imp: imports) {
            if (imp.getModule().equals(decModule)) {
                if (!imp.getNativeBackends().none()) {
                    bs = bs.none() ? 
                            imp.getNativeBackends() : 
                            bs.supported(imp.getNativeBackends());
                }
                break;
            }
        }
        return bs;
    }

    private Declaration handleAbstraction(
            Declaration dec, 
            Tree.MemberOrTypeExpression that) {
        if (!dec.isNative()) {
            //NOTE: if this is the qualifying type of a static 
            //      method reference, don't do anything special 
            //      here, since we're not actually calling the 
            //      constructor
            if (!that.getStaticMethodReferencePrimary() &&
                    isAbstraction(dec)) {
                //handle the case where it's not _really_ 
                //overloaded, it's just a constructor with a 
                //different visibility to the class itself
                //(this is a hack the Java model loader uses
                //because we didn't used to have constructors)
                List<Declaration> overloads = 
                        dec.getOverloads();
                Declaration found = null;
                for (Declaration overload: overloads) {
                    if (!overload.isCoercionPoint()) {
                        if (found == null) {
                            found = overload;
                        }
                        else {
                            // give up if we find two
                            return dec;
                        }
                    }
                }
                if (found != null) {
                    return found;
                }
            }
        }
        return dec;
    }
    
}