package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.NO_TYPE_ARGS;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkAssignable;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkCallable;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkCasesDisjoint;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkIsExactlyForInterop;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.checkSupertype;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.declaredInPackage;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getMatchingParameter;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getPackageTypeDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getPackageTypedDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTupleType;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeArguments;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeMember;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypeUnknownError;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypedDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getTypedMember;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.getUnspecifiedParameter;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.inSameModule;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.involvesTypeParams;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isConstructor;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isGeneric;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.isIndirectInvocation;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.notAssignableMessage;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.spreadType;
import static com.redhat.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.unwrapAliasedTypeConstructor;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.eliminateParensAndWidening;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.hasError;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.hasUncheckedNulls;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.isEffectivelyBaseMemberExpression;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.isInstantiationExpression;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.name;
import static com.redhat.ceylon.compiler.typechecker.tree.TreeUtil.unwrapExpressionUntilTerm;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.addToIntersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.addToUnion;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.appliedType;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.argumentSatisfiesEnumeratedConstraint;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.canonicalIntersection;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.findMatchingOverloadedClass;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.genericFunctionType;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getContainingClassOrInterface;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getInterveningRefinements;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getNativeDeclaration;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getNativeHeader;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getOuterClassOrInterface;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getSignature;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getTypeArgumentMap;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.getTypeParameters;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.intersectionOfSupertypes;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.intersectionType;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isAbstraction;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isImplemented;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isOverloadedVersion;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isTypeUnknown;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.typeParametersAsArgList;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.union;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.unionOfCaseTypes;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.unionType;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.BackendSupport;
import com.redhat.ceylon.compiler.typechecker.context.TypecheckerUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Pattern;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PositionalArgument;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.VoidModifier;
import com.redhat.ceylon.compiler.typechecker.tree.TreeUtil;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Generic;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.NothingType;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.Reference;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedReference;
import com.redhat.ceylon.model.typechecker.model.UnknownType;
import com.redhat.ceylon.model.typechecker.model.Value;

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
    
    private Tree.Type returnType;
    private Declaration returnDeclaration;
//    private boolean isCondition;
    private boolean dynamic;
    private boolean inExtendsClause = false;
    private Backend inBackend = null;
    private TypeDeclaration constructorClass;

    private Node ifStatementOrExpression;
    private Node switchStatementOrExpression;
    
    private TypecheckerUnit unit;
    private final BackendSupport backendSupport;
    
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
    
    public ExpressionVisitor(BackendSupport backendSupport) {
        this.backendSupport = backendSupport;
    }
    
    public ExpressionVisitor(TypecheckerUnit unit, BackendSupport backendSupport) {
        this.unit = unit;
        this.backendSupport = backendSupport;
        String nat = unit.getPackage().getModule().getNativeBackend();
        inBackend = Backend.fromAnnotation(nat);
    }
    
    @Override public void visit(Tree.CompilationUnit that) {
        unit = that.getUnit();
        Backend ib = inBackend;
        String nat = unit.getPackage().getModule().getNativeBackend();
        inBackend = Backend.fromAnnotation(nat);
        super.visit(that);
        inBackend = ib;
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
            returnType.setTypeModel(unit.getNothingType());
        }
        return ort;
    }
    
    private void endReturnScope(Tree.Type t, TypedDeclaration td) {
        if (returnType instanceof Tree.FunctionModifier || 
                returnType instanceof Tree.ValueModifier) {
            td.setType(returnType.getTypeModel());
        }
        returnType = t;
    }
    
    @Override public void visit(Tree.FunctionArgument that) {
        Tree.Expression e = that.getExpression();
        Function fun = that.getDeclarationModel();
        Tree.Type type = that.getType();
        if (e==null) {
            Tree.Type rt = beginReturnScope(type);
            Declaration od = 
                    beginReturnDeclaration(fun);
            super.visit(that);
            endReturnDeclaration(od);
            endReturnScope(rt, fun);
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
            if (type instanceof Tree.VoidModifier &&
                    !isSatementExpression(e)) {
                e.addError("anonymous function is declared void so specified expression must be a statement");
            }
        }
        if (type instanceof Tree.VoidModifier) {
            fun.setType(unit.getAnythingType());            
        }
        TypedReference reference = fun.getTypedReference();
        Type fullType;
        //if the anonymous function has type parameters,
        //then the type of the expression is a type 
        //constructor, so create a fake type alias
        if (isGeneric(fun)) {
            Scope scope = that.getScope();
            fullType = 
                    genericFunctionType(fun, scope, fun, 
                            reference, unit);
        }
        else {
            fullType = reference.getFullType();
        }
        that.setTypeModel(fullType);
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
        checkCasesExhaustive(switchClause, switchCaseList);
        
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
                            Type et = 
                                    unit.getIteratedType(it);
                            boolean nonemptyIterable = 
                                    et!=null &&
                                    it.isSubtypeOf(unit.getNonemptyIterableType(et));
                            that.setPossiblyEmpty(
                                    !nonemptyIterable || 
                                    cc.getPossiblyEmpty());
                            Type firstType = 
                                    unionType(
                                            unit.getFirstType(it), 
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
                destructure(pattern, se, e.getTypeModel());
            }
        }
    }

    private void destructure(Tree.Pattern pattern,
            Tree.SpecifierExpression se, Type type) {
        if (type!=null) {
            if (pattern instanceof Tree.TuplePattern) {
                destructure(se, type, 
                        (Tree.TuplePattern) pattern);
            }
            else if (pattern instanceof Tree.KeyValuePattern) {
                destructure(se, type, 
                        (Tree.KeyValuePattern) pattern);
            }
            else {
                Tree.VariablePattern vp = 
                        (Tree.VariablePattern) pattern;
                Tree.Variable var = vp.getVariable();
                Tree.Type varType = var.getType();
                if (varType!=null) {
                    if (varType instanceof Tree.SequencedType) {
                        inferSequencedValueType(type, var);
                    }
                    else {
                        inferValueType(var, type);
                    }
                    Type declaredType = 
                            varType.getTypeModel();
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

    private void destructure(Tree.SpecifierExpression se, 
            Type entryType,
            Tree.KeyValuePattern keyValuePattern) {
        Tree.Pattern key = keyValuePattern.getKey();
        Tree.Pattern value = keyValuePattern.getValue();
        if (!unit.isEntryType(entryType)) {
            se.addError("assigned expression is not an entry type: '"
                    + entryType.asString(unit) + 
                    "' is not an entry type");
        }
        else {
            destructure(key, se, unit.getKeyType(entryType));
            destructure(value, se, unit.getValueType(entryType));
        }
    }

    private void destructure(Tree.SpecifierExpression se, 
            Type sequenceType,
            Tree.TuplePattern tuplePattern) {
        List<Tree.Pattern> patterns = 
                tuplePattern.getPatterns();
        int length = patterns.size();
        if (length==0) {
            tuplePattern.addError("tuple pattern must have at least one variable");
        }
        else {
            for (int i=0; i<length-1; i++) {
                Tree.Pattern p = patterns.get(i);
                if (p instanceof Tree.VariablePattern) {
                    Tree.VariablePattern vp = 
                            (Tree.VariablePattern) p;
                    Tree.Type t = 
                            vp.getVariable().getType();
                    if (t instanceof Tree.SequencedType) {
                        t.addError("variadic pattern element must occur as last element of tuple pattern");
                    }
                }
            }
            Tree.Pattern lastPattern = 
                    patterns.get(length-1);
            if (!unit.isSequentialType(sequenceType)) {
                se.addError("assigned expression is not a sequence type, so may not be destructured: '" + 
                        sequenceType.asString(unit) + 
                        "' is not a subtype of 'Sequential'");
            }
            else if (unit.isEmptyType(sequenceType)) {
                se.addError("assigned expression is an empty sequence type, so may not be destructured: '" + 
                        sequenceType.asString(unit) + 
                        "' is a subtype of `Empty`");
            }
            else if (unit.isTupleType(sequenceType)) {
                destructureTuple(se, sequenceType, patterns, 
                        length, lastPattern);
            }
            else {
                destructureSequence(se, sequenceType, patterns, 
                        length, lastPattern);
            }
        }
    }

    private boolean isVariadicPattern(Tree.Pattern lastPattern) {
        boolean variadic = false;
//      boolean nonempty = false;
        if (lastPattern instanceof Tree.VariablePattern) {
            Tree.VariablePattern variablePattern = 
                    (Tree.VariablePattern) lastPattern;
            Tree.Type type = 
                    variablePattern.getVariable().getType();
            if (type instanceof Tree.SequencedType) {
                variadic = true;
//              nonempty = ((Tree.SequencedType) type).getAtLeastOne();
            }
        }
        return variadic;
    }

    private void destructureTuple(Tree.SpecifierExpression se,
            Type sequenceType, List<Tree.Pattern> patterns, 
            int length, Tree.Pattern lastPattern) {
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
            se.addError("assigned tuple has too many elements");
        }
        if (!variadic && tupleLengthUnbounded) {
            se.addError("assigned tuple has unbounded length");
        }
        if (!variadic && minimumLength<types.size()) {
            se.addError("assigned tuple has variadic length");
        }
        int fixedLength = variadic ? length-1 : length;
        for (int i=0; i<types.size() && i<fixedLength; i++) {
            Type type = types.get(i);
            Tree.Pattern pattern = patterns.get(i);
            destructure(pattern, se, type);
        }
        if (variadic) {
            Type tail = 
                    unit.getTailType(sequenceType, 
                            fixedLength);
            destructure(lastPattern, se, tail);
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

    private void destructureSequence(Tree.SpecifierExpression se,
            Type sequenceType, List<Tree.Pattern> patterns, 
            int length, Tree.Pattern lastPattern) {
        boolean variadic = isVariadicPattern(lastPattern);
        
        if (!variadic) {
            se.addError("assigned expression is not a tuple type, so pattern must end in a variadic element: '" + 
                    sequenceType.asString(unit) + 
                    "' is not a tuple type");
        }
        else if (/*nonempty && length>1 ||*/ length>2) {
            se.addError("assigned expression is not a tuple type, so pattern must not have more than two elements: '" + 
                    sequenceType.asString(unit) + 
                    "' is not a tuple type");
        }
        else if ((/*nonempty ||*/ length>1) && 
                !unit.isSequenceType(sequenceType)) {
            se.addError("assigned expression is not a nonempty sequence type, so pattern must have exactly one element: '" + 
                    sequenceType.asString(unit) + 
                    "' is not a subtype of 'Sequence'");
        }
        
        if (length>1) {
            Type elementType = 
                    unit.getSequentialElementType(sequenceType);
            destructure(patterns.get(0), se, elementType);
            destructure(lastPattern, se, 
                    unit.getSequentialType(elementType));
        }
        else {
            destructure(lastPattern, se, sequenceType);
        }
    }

    @Override public void visit(Tree.Variable that) {
        super.visit(that);
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        if (se!=null) {
            inferType(that, se);
            if (that.getType()!=null) {
                Type t = 
                        that.getType().getTypeModel();
                if (!isTypeUnknown(t)) {
                    checkType(t, se);
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
        if (that.getType() instanceof Tree.SyntheticVariable) {
            TypedDeclaration td = 
                    (TypedDeclaration) 
                        that.getDeclarationModel();
            Tree.BaseMemberExpression bme = 
                    (Tree.BaseMemberExpression) that
                        .getSpecifierExpression()
                        .getExpression()
                        .getTerm();
            TypedDeclaration d = 
                    (TypedDeclaration) bme.getDeclaration();
            td.setOriginalDeclaration(d);
        }
    }
    
    @Override public void visit(Tree.IsCondition that) {
        //don't recurse to the Variable, since we don't
        //want to check that the specifier expression is
        //assignable to the declared variable type
        //(nor is it possible to infer the variable type)
//        isCondition=true;
        Tree.Type t = that.getType();
        if (t!=null) {
            t.visit(this);
        }
//        isCondition=false;
        Tree.Variable v = that.getVariable();
        Type type = 
                t==null ? null : 
                    t.getTypeModel();
        if (v!=null) {
//            if (type!=null && !that.getNot()) {
//                v.getType().setTypeModel(type);
//                v.getDeclarationModel().setType(type);
//            }
            //v.getType().visit(this);
            Tree.SpecifierExpression se = 
                    v.getSpecifierExpression();
            Type knownType;
            if (se==null) {
                knownType = null;
            }
            else {
                se.visit(this);
                checkReferenceIsNonVariable(v, se);
                /*checkAssignable( se.getExpression().getTypeModel(), 
                        getOptionalType(getObjectDeclaration().getType()), 
                        se.getExpression(), 
                        "expression may not be of void type");*/
                initOriginalDeclaration(v);
                //this is a bit ugly (the parser sends us a SyntheticVariable
                //instead of the real StaticType which it very well knows!)
                Tree.Expression e = se.getExpression();
                knownType = e==null ? null : e.getTypeModel();
                //TODO: what to do here in case of !is
                if (knownType!=null) {
                    if (hasUncheckedNulls(e)) {
                        knownType = unit.getOptionalType(knownType);
                    }
                    String help = " (expression is already of the specified type)";
                    if (that.getNot()) {
                        if (intersectionType(type, knownType, unit).isNothing()) {
                            that.addError("does not narrow type: intersection of '" + 
                                    type.asString(unit) + 
                                    "' and '" + 
                                    knownType.asString(unit) + "' is empty" + 
                                    help);
                        }
                        else if (knownType.isSubtypeOf(type)) {
                            that.addError("tests assignability to bottom type 'Nothing': '" + 
                                    knownType.asString(unit) + 
                                    "' is a subtype of '" + 
                                    type.asString(unit) + "'");
                        }
                    } 
                    else {
                        if (knownType.isSubtypeOf(type)) {
                            that.addError("does not narrow type: '" + 
                                    knownType.asString(unit) + 
                                    "' is a subtype of '" + 
                                    type.asString(unit) + "'" + 
                                    help);
                        }
                    }
                }
            }
            defaultTypeToAnything(v);
            if (knownType==null) {
                knownType = unit.getAnythingType(); //or should we use unknown?
            }
            
            Type it = 
                    narrow(type, knownType, that.getNot());
            //check for disjointness
            if (it.isNothing()) {
                if (that.getNot()) {
                    /*that.addError("tests assignability to Nothing type: " +
                            knownType.asString(unit) + " is a subtype of " + 
                            type.asString(unit));*/
                }
                else {
                    that.addError("tests assignability to bottom type 'Nothing': intersection of '" +
                            knownType.asString(unit) + "' and '" + 
                            type.asString(unit) + "' is empty");
                }
            }
            //do this *after* checking for disjointness!
            knownType = unit.denotableType(knownType);
            //now recompute the narrowed type!
            it = narrow(type, knownType, that.getNot());
            
            v.getType().setTypeModel(it);
            v.getDeclarationModel().setType(it);
        }
    }
    
	private Type narrow(Type type,
            Type knownType, boolean not) {
	    Type it;
	    if (not) {
	        //a !is condition, narrow to complement
	        it = /*unit.denotableType(*/knownType.minus(type);
	    }
	    else {
	        //narrow to the intersection of the outer type 
	        //and the type specified in the condition
	        it = intersectionType(type, knownType, unit);
	    }
	    return it;
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
                    checkReferenceIsNonVariable(v, se);
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
                            Pattern pattern = d.getPattern();
                            destructure(pattern, se, type);
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
            checkOptional(t, term);
        }
        else if (that instanceof Tree.NonemptyCondition) {
            checkEmpty(t, term);
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
            Tree.SpecifierExpression se) {
        if (v.getType() instanceof Tree.SyntheticVariable) {
            Tree.BaseMemberExpression term = 
                    (Tree.BaseMemberExpression) 
                        se.getExpression()
                            .getTerm();
			checkReferenceIsNonVariable(term, false);
        }
    }

    private void checkReferenceIsNonVariable(Tree.BaseMemberExpression ref,
    		boolean isSwitch) {
        Declaration d = ref.getDeclaration();
        if (d!=null) {
        	int code = isSwitch ? 3101:3100;
            String help=" (assign to a new local value to narrow type)";
            if (!(d instanceof Value)) {
                ref.addError("referenced declaration is not a value: '" + 
                        d.getName(unit) + "'", 
                        code);
            }
            else if (isNonConstant(d)) {
                ref.addError("referenced value is non-constant: '" + 
                        d.getName(unit) + "'" + help, 
                        code);
            }
            else if (d.isDefault() || d.isFormal()) {
                ref.addError("referenced value may be refined by a non-constant value: '" + 
                        d.getName(unit) + "'" + help, 
                        code);
            }
        }
    }

    private boolean isNonConstant(Declaration d) {
        if (d instanceof Value) {
            Value v = (Value) d;
            return v.isVariable() || v.isTransient();
        }
        else {
            return false;
        }
    }
    
    private void checkEmpty(Type t, Tree.Term term) {
        if (!isTypeUnknown(t)) {
            if (!unit.isSequentialType(unit.getDefiniteType(t))) {
                term.addError("expression must be a possibly-empty sequential type: '" + 
                        t.asString(unit) + 
                        "' is not a subtype of 'Anything[]?'");
            }
            else if (!unit.isPossiblyEmptyType(t)) {
                term.addError("expression must be a possibly-empty sequential type: '" + 
                        t.asString(unit) + 
                        "' is not possibly-empty");
            }
        }
    }
    
    private void checkOptional(Type type, Tree.Term term) {
        if (!isTypeUnknown(type) && 
                !unit.isOptionalType(type) && 
                !hasUncheckedNulls(term)) {
            term.addError("expression must be of optional type: '" +
                    type.asString(unit) + 
                    "' is not optional");
        }
    }

    @Override public void visit(Tree.BooleanCondition that) {
        super.visit(that);
        if (that.getExpression()!=null) {
            Type t = 
                    that.getExpression().getTypeModel();
            if (!isTypeUnknown(t)) {
                checkAssignable(t, unit.getBooleanType(), that, 
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
                    if (isInstantiationExpression(e)) {
                        if (!t.isSubtypeOf(dt) && !t.isSubtypeOf(ot)) {
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

    @Override public void visit(Tree.ForIterator that) {
        super.visit(that);
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        if (se!=null) {
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                Type et = e.getTypeModel();
                if (!isTypeUnknown(et)) {
                    if (!unit.isIterableType(et)) {
                        se.addError("expression is not iterable: '" + 
                                et.asString(unit) + 
                                "' is not a subtype of 'Iterable'");
                    }
                    else if (et!=null && unit.isEmptyType(et)) {
                        se.addError("iterated expression is definitely empty: '" +
                                et.asString(unit) + 
                                "' is a subtype of 'Empty'");
                    }
                }
            }
        }
    }

    @Override public void visit(Tree.ValueIterator that) {
        super.visit(that);
        Tree.Variable v = that.getVariable();
        if (v!=null) {
            inferContainedType(v, 
                    that.getSpecifierExpression());
            checkContainedType(v, 
                    that.getSpecifierExpression());
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
                    Type it = unit.getIteratedType(et);
                    if (it!=null && !isTypeUnknown(it)) {
                        destructure(that.getPattern(), se, it);
                    }
                }
            }
        }
    }
    
    @Override public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);
        Value dec = that.getDeclarationModel();
        Tree.SpecifierOrInitializerExpression sie = 
                that.getSpecifierOrInitializerExpression();
        inferType(that, sie);
        Tree.Type type = that.getType();
        if (type!=null) {
        	Type t = type.getTypeModel();
        	if (type instanceof Tree.LocalModifier) {
        		if (dec.isParameter()) {
        			type.addError("parameter may not have inferred type: '" + 
        					dec.getName() + 
        					"' must declare an explicit type");
        		}
        		else if (isTypeUnknown(t)) {
        		    if (sie==null) {
        		        type.addError("value must specify an explicit type or definition", 200);
        		    }
        		    else if (!hasError(sie)) {
        		        type.addError("value type could not be inferred" + 
        		                getTypeUnknownError(t));
        		    }
        		}
        	}
        	if (!isTypeUnknown(t)) {
        		checkType(t, dec, sie, 2100);
        	}
        }
    	Setter setter = dec.getSetter();
    	if (setter!=null) {
    		setter.getParameter()
    		    .getModel()
    		    .setType(dec.getType());
    	}
    }
    
    @Override public void visit(Tree.ParameterizedExpression that) {
        super.visit(that);
        Tree.Term primary = that.getPrimary();
        if (!hasError(that)) {
            if (primary instanceof Tree.QualifiedMemberExpression ||
                primary instanceof Tree.BaseMemberExpression) {
                Tree.MemberOrTypeExpression mte = 
                        (Tree.MemberOrTypeExpression) primary;
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
        super.visit(that);

        Tree.SpecifierExpression rhs = 
                that.getSpecifierExpression();
        Tree.Term lhs = 
                that.getBaseMemberExpression();

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
            if (that.getRefinement()) {
                // interpret this specification as a 
                // refinement of an inherited member
                if (d instanceof Value) {
                    refineValue(that);
                }
                else if (d instanceof Function) {
                    refineMethod(that);
                }
                Tree.StaticMemberOrTypeExpression smte = 
                        (Tree.StaticMemberOrTypeExpression) me;
                smte.setDeclaration(d);
            }
            else if (d instanceof FunctionOrValue) {
                FunctionOrValue mv = (FunctionOrValue) d;
                if (mv.isShortcutRefinement()) {
                    String desc;
                    if (d instanceof Value) {
                        desc = "value";
                    }
                    else {
                        desc = "function";
                    }
                    me.addError(desc + 
                            " already specified: '" + 
                                d.getName(unit) + "'");
                }
                else if (d instanceof Value && 
                        ((Value) d).isInferred()) {
                    me.addError("value is not a variable: '" + 
                            d.getName() + "'");
                }
                else if (!mv.isVariable() && !mv.isLate()) {
                    String desc;
                    if (d instanceof Value) {
                        desc = "value is neither variable nor late and";
                    }
                    else {
                        desc = "function";
                    }
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
            
            if (hasParams && d instanceof Function && 
                    ((Function) d).isDeclaredVoid() && 
                    !isSatementExpression(rhs.getExpression())) {
                rhs.addError("function is declared void so specified expression must be a statement: '" + 
                        d.getName(unit) + 
                        "' is declared 'void'");
            }
            if (d instanceof Value && 
                    rhs instanceof Tree.LazySpecifierExpression) {
                ((Value) d).setTransient(true);
            }
            
            Type t = lhs.getTypeModel();
            if (lhs==me && d instanceof Function &&
                    !t.isTypeConstructor()) {
                //if the declaration of the method has
                //defaulted parameters, we should ignore
                //that when determining if the RHS is
                //an acceptable implementation of the
                //method
                //TODO: this is a pretty nasty way to
                //      handle the problem
                t = eraseDefaultedParameters(t);
            }
            if (!isTypeUnknown(t)) {
                checkType(t, d, rhs, 2100);
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
    
    boolean isSatementExpression(Tree.Expression e) {
        if (e==null) {
            return false;
        }
        else {
            Tree.Term t = e.getTerm();
            return t instanceof Tree.InvocationExpression ||
                    t instanceof Tree.PostfixOperatorExpression ||
                    t instanceof Tree.AssignmentOp ||
                    t instanceof Tree.PrefixOperatorExpression;
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
    
    static Reference getRefinedMember(FunctionOrValue d, 
            ClassOrInterface classOrInterface) {
        TypeDeclaration td = 
                (TypeDeclaration) 
                    d.getContainer();
        Type supertype = 
                classOrInterface.getType()
                    .getSupertype(td);
        return d.appliedReference(supertype, NO_TYPE_ARGS);
    }
    
    private void refineValue(Tree.SpecifierStatement that) {
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
                getInterveningRefinements(value.getName(), 
                        null, root, ci, td);
        accountForIntermediateRefinements(that, 
                refinedValue, value, ci, 
                interveningRefinements);
    }

    private void refineMethod(Tree.SpecifierStatement that) {
        Function refinedMethod = (Function) that.getRefined();
        Function method = (Function) that.getDeclaration();
        ClassOrInterface ci = 
                (ClassOrInterface) 
                    method.getContainer();
        Declaration root = method.getRefinedDeclaration();
//        Declaration root = refinedMethod.getRefinedDeclaration();
//        method.setRefinedDeclaration(root);
        TypeDeclaration td = 
                (TypeDeclaration) 
                    root.getContainer();
        List<Declaration> interveningRefinements = 
                getInterveningRefinements(method.getName(), 
                        getSignature(method), 
                        root, ci, td);
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
                            .getFullType();
                    if (parameterList==null || 
                            parameterList.getParameters().size()<=j) {
                        Parameter p = 
                                parameters.getParameters()
                                    .get(j);
                        p.getModel().setType(refinedParameterType);
                    }
                    else {
                        Tree.Parameter parameter = 
                                parameterList.getParameters()
                                    .get(j);
                        Parameter p = 
                                parameter.getParameterModel();
                        Type parameterType = 
                                p.getModel()
                                    .getTypedReference()
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
                        checkIsExactlyForInterop(that.getUnit(),
                                refinedParameters.isNamedParametersSupported(),
                                parameterType, 
                                refinedParameterType, 
                                typeNode, 
                                "type of parameter '" + p.getName() + 
                                "' of '" + method.getName() + 
                                "' declared by '" + ci.getName() +
                                "' is different to type of corresponding parameter '" +
                                refinedParameter.getName() + "' of refined method '" + 
                                refinedMethod.getName() + "' of '" + 
                                ((Declaration) refinedMethod.getContainer()).getName() + 
                                "'");
                    }
                }
            }
        }
    }

    private Reference accountForIntermediateRefinements(
            Tree.SpecifierStatement that, 
            FunctionOrValue refinedMethodOrValue, 
            FunctionOrValue methodOrValue,
            ClassOrInterface ci, 
            List<Declaration> interveningRefinements) {
        Tree.SpecifierExpression rhs = 
                that.getSpecifierExpression();
        Reference refinedProducedReference = 
                getRefinedMember(refinedMethodOrValue, ci);
        List<Type> refinedTypes = 
                new ArrayList<Type>();
//        Type type = 
//                getRequiredSpecifiedType(that, 
//                        refinedProducedReference);
        addToIntersection(refinedTypes, 
                refinedProducedReference.getType(), 
                unit);
        for (Declaration refinement: interveningRefinements) {
            if (refinement instanceof FunctionOrValue && 
                    !refinement.equals(refinedMethodOrValue)) {
                FunctionOrValue rmv = 
                        (FunctionOrValue) refinement;
                Reference refinedMember = 
                        getRefinedMember(rmv, ci);
                addToIntersection(refinedTypes, 
                        refinedMember.getType(), 
                        unit);
                Type requiredType = 
                        getRequiredSpecifiedType(that, 
                                refinedMember);
                if (rhs!=null && 
                        !isTypeUnknown(requiredType)) {
                    checkType(requiredType, refinement, 
                            rhs, 2100);
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
        }
        Type it = canonicalIntersection(refinedTypes, unit);
        methodOrValue.setType(it);
        return refinedProducedReference;
    }

    private Type getRequiredSpecifiedType(
            Tree.SpecifierStatement that,
            Reference refinedMember) {
        Type t = refinedMember.getFullType();
        Tree.Term term = that.getBaseMemberExpression();
        if (term instanceof Tree.ParameterizedExpression) {
            Tree.ParameterizedExpression pe = 
                    (Tree.ParameterizedExpression) term;
            int pls = pe.getParameterLists().size();
            for (int i=0; !isTypeUnknown(t) && i<pls; i++) {
                t = unit.getCallableReturnType(t);
            }
        }
        return t;
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
        }
        else {
            Declaration a = 
                    that.getScope()
                        .getDirectMember(p.getName(), 
                                null, false);
            if (a==null) {
                that.addError("parameter declaration does not exist: '" + 
                        p.getName() + "'");
            }
        }
    }
    
    private void checkType(Type declaredType, 
            Tree.SpecifierOrInitializerExpression sie) {
        if (sie!=null) { 
            Tree.Expression e = sie.getExpression();
            if (e!=null) {
                Type set = e.getTypeModel();
                if (!isTypeUnknown(set)) {
                    checkAssignable(set, declaredType, sie, 
                            "specified expression must be assignable to declared type");
                }
            }
        }
    }

    private void checkType(Type declaredType, 
            Declaration dec, 
            Tree.SpecifierOrInitializerExpression sie, 
            int code) {
        if (sie!=null) {
            Tree.Expression e = sie.getExpression();
            if (e!=null) {
                Type t = e.getTypeModel();
                if (!isTypeUnknown(t)) {
                    String name = "'" + dec.getName(unit) + "'";
                    if (dec.isClassOrInterfaceMember()) {
                        Declaration c = 
                                (Declaration) 
                                    dec.getContainer();
                        name += " of '" + c.getName(unit) + "'";
                    }
                    checkAssignable(t, declaredType, sie, 
                            "specified expression must be assignable to declared type of " + 
                                    name,
                            code);
                }
            }
        }
    }

    private void checkFunctionType(Type et, 
            Tree.Type that, Tree.SpecifierExpression se) {
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
            if (!isTypeUnknown(vt)) {
                Type nt = not ?
                        unit.getNullType() :
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
                            Type net = not ?
                                    unit.getNullType() :
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
            Type vt = type.getTypeModel();
            if (!isTypeUnknown(vt)) {
                Type nt = not ?
                        unit.getEmptyType() :
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
                        Type net = not ?
                                unit.getEmptyType() :
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
                Type set = e.getTypeModel();
                if (!isTypeUnknown(vt) && 
                        !isTypeUnknown(set)) {
                    Type it = 
                            unit.getIteratedType(set);
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
        Tree.Type type = that.getType();
        Tree.Type rt = beginReturnScope(type);
        Value v = that.getDeclarationModel();
        Declaration od = beginReturnDeclaration(v);
        super.visit(that);
        endReturnScope(rt, v);
        endReturnDeclaration(od);
        Setter setter = v.getSetter();
        if (setter!=null) {
            setter.getParameter()
                .getModel()
                .setType(v.getType());
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
        Tree.Type type = that.getType();
        Value v = that.getDeclarationModel();
        if (se==null) {
            Tree.Type rt = beginReturnScope(type);
            Declaration od = beginReturnDeclaration(v);
            super.visit(that);
            endReturnDeclaration(od);
            endReturnScope(rt, v);
        }
        else {
            super.visit(that);
            inferType(that, se);
            if (type!=null) {
                Type t = type.getTypeModel();
                if (!isTypeUnknown(t)) {
                    checkType(t, v, se, 2100);
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
        Tree.Type rt = beginReturnScope(that.getType());
        Setter sd = that.getDeclarationModel();
        Declaration od = beginReturnDeclaration(sd);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, sd);
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        if (se!=null) {
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                if (!isSatementExpression(e)) {
                    se.addError("specified expression must be a statement: '" +
                            sd.getName() + "'");
                }
            }
        }
    }

    @Override public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        Tree.Type type = that.getType();
        Function m = that.getDeclarationModel();
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        if (se!=null) {
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                Type returnType = e.getTypeModel();
                inferFunctionType(that, returnType);
                if (type!=null && 
                        !(type instanceof Tree.DynamicModifier)) {
                    checkFunctionType(returnType, type, se);
                }
                if (type instanceof Tree.VoidModifier && 
                        !isSatementExpression(e)) {
                    se.addError("function is declared void so specified expression must be a statement: '" +
                            m.getName() + "' is declared 'void'");
                }
            }
        }
        if (type instanceof Tree.LocalModifier) {
            if (m.isParameter()) {
                type.addError("parameter may not have inferred type: '" + 
                        m.getName() + "' must declare an explicit type");
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
        Tree.Type type = that.getType();
        Tree.Type rt = beginReturnScope(type);
        Function m = that.getDeclarationModel();
        Declaration od = 
                beginReturnDeclaration(m);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, m);
        if (type instanceof Tree.LocalModifier) {
            if (isTypeUnknown(type.getTypeModel())) {
                type.addError("function type could not be inferred");
            }
        }
    }

    @Override public void visit(Tree.MethodArgument that) {
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        Function m = that.getDeclarationModel();
        Tree.Type type = that.getType();
        if (se==null) {
            Tree.Type rt = beginReturnScope(type);           
            Declaration od = beginReturnDeclaration(m);
            super.visit(that);
            endReturnDeclaration(od);
            endReturnScope(rt, m);
        }
        else {
            super.visit(that);
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                Type returnType = e.getTypeModel();
                inferFunctionType(that, returnType);
                if (type!=null && 
                        !(type instanceof Tree.DynamicModifier)) {
                    checkFunctionType(returnType, type, se);
                }
                if (m.isDeclaredVoid() && 
                        !isSatementExpression(e)) {
                    se.addError("functional argument is declared void so specified expression must be a statement: '" + 
                            m.getName() + "' is declared 'void'");
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
    
    @Override public void visit(Tree.Declaration that) {
        Backend ib = inBackend;
        String nat = that.getDeclarationModel().getNativeBackend();
        inBackend = Backend.fromAnnotation(nat);
        super.visit(that);
        inBackend = ib;
    }
    
    private static VoidModifier fakeVoid(Node that) {
        return new Tree.VoidModifier(that.getToken());
    }
    
    @Override public void visit(Tree.ClassDefinition that) {
        Tree.Type rt = beginReturnScope(fakeVoid(that));
        Class c = that.getDeclarationModel();
        Declaration od = 
                beginReturnDeclaration(c);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
    }

    @Override public void visit(Tree.InterfaceDefinition that) {
        Tree.Type rt = beginReturnScope(null);
        Interface i = that.getDeclarationModel();
        Declaration od = beginReturnDeclaration(i);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
    }

    @Override public void visit(Tree.ObjectDefinition that) {
        Tree.Type rt = beginReturnScope(fakeVoid(that));
        Class ac = that.getAnonymousClass();
        Declaration od = beginReturnDeclaration(ac);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
    }

    @Override public void visit(Tree.ObjectArgument that) {
        Tree.Type rt = beginReturnScope(fakeVoid(that));
        Class ac = that.getAnonymousClass();
        Declaration od = beginReturnDeclaration(ac);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
    }
    
    @Override public void visit(Tree.ObjectExpression that) {
        Tree.Type rt = beginReturnScope(fakeVoid(that));
        Class ac = that.getAnonymousClass();
        Declaration od = beginReturnDeclaration(ac);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
        that.setTypeModel(unit.denotableType(ac.getType()));
    }
    
    @Override public void visit(Tree.ClassDeclaration that) {
        super.visit(that);
        Class alias = that.getDeclarationModel();
        Type et = alias.getExtendedType();
        if (et!=null) {
            TypeDeclaration etd = et.getDeclaration();
            if (etd instanceof Constructor) {
                etd = etd.getExtendedType().getDeclaration();
            }
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
            else if (c instanceof Class && 
                    that.getClassSpecifier()!=null &&
                    that.getClassSpecifier()
                        .getInvocationExpression()!=null) {
                Tree.Primary primary = 
                        that.getClassSpecifier()
                            .getInvocationExpression()
                            .getPrimary();
                Tree.ExtendedTypeExpression smte= 
                        (Tree.ExtendedTypeExpression) 
                            primary;
                Functional classOrConstructor = 
                        (Functional) smte.getDeclaration();
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
                                .addUnsupportedError("wrong number of initializer parameters declared by class alias: '" + 
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
                pal.addUnsupportedError("wrong number of arguments for aliased class: '" + 
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
                        pa.addUnsupportedError("argument to variadic parameter of aliased class must be spread");
                    }
                    Tree.ListedArgument la = 
                            (Tree.ListedArgument) pa;
                    Tree.Expression e = 
                            la.getExpression();
                    checkAliasArg(aparam, e);
                }
                else if (pa instanceof Tree.SpreadArgument) {
                    if (!cparam.isSequenced()) {
                        pa.addUnsupportedError("argument to non-variadic parameter of aliased class may not be spread");
                    }
                    Tree.SpreadArgument sa = 
                            (Tree.SpreadArgument) pa;
                    Tree.Expression e = 
                            sa.getExpression();
                    checkAliasArg(aparam, e);
                }
                else if (pa!=null) {
                    pa.addUnsupportedError("argument to parameter or aliased class must be listed or spread");
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
            Type et) {
        Tree.Type type = that.getType();
        if (type instanceof Tree.FunctionModifier) {
            Tree.FunctionModifier local = 
                    (Tree.FunctionModifier) type;
            if (et!=null) {
                setFunctionType(local, et, that);
            }
        }
    }
    
    private void inferFunctionType(Tree.MethodArgument that, 
            Type et) {
        Tree.Type type = that.getType();
        if (type instanceof Tree.FunctionModifier) {
            Tree.FunctionModifier local = 
                    (Tree.FunctionModifier) type;
            if (et!=null) {
                setFunctionType(local, et, that);
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
                Type nullType = 
                        unit.getNullType();
                local.setTypeModel(nullType);
                that.getDeclarationModel().setType(nullType);
            }
            else if (se!=null) {
                setTypeFromOptionalType(local, se, that);
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
                Type emptyType= 
                        unit.getEmptyType();
                local.setTypeModel(emptyType);
                that.getDeclarationModel().setType(emptyType);
            }
            else if (se!=null) {
                setTypeFromEmptyType(local, se, that);
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
    
    private void setTypeFromOptionalType(Tree.LocalModifier local, 
            Tree.SpecifierExpression se, Tree.Variable that) {
        Tree.Expression e = se.getExpression();
        if (e!=null) {
            Type expressionType = e.getTypeModel();
            if (!isTypeUnknown(expressionType)) {
                Type t;
                if (unit.isOptionalType(expressionType)) {
                    t = unit.getDefiniteType(expressionType);
                }
                else {
                    t=expressionType;
                }
                local.setTypeModel(t);
                that.getDeclarationModel().setType(t);
            }
        }
    }
    
    private void setTypeFromEmptyType(Tree.LocalModifier local, 
            Tree.SpecifierExpression se, Tree.Variable that) {
        Tree.Expression e = se.getExpression();
        if (e!=null) {
            Type expressionType = e.getTypeModel();
            if (!isTypeUnknown(expressionType)) {
                Type t;
                if (unit.isPossiblyEmptyType(expressionType)) {
                    t = unit.getNonemptyDefiniteType(expressionType);
                }
                else {
                    t = expressionType;
                }
                local.setTypeModel(t);
                that.getDeclarationModel().setType(t);
            }
        }
    }
    
    private void setTypeFromIterableType(Tree.LocalModifier local, 
            Tree.SpecifierExpression se, Tree.Variable that) {
        if (se.getExpression()!=null) {
            Type expressionType = 
                    se.getExpression().getTypeModel();
            if (expressionType!=null) {
                Type t = 
                        unit.getIteratedType(expressionType);
                if (t!=null) {
                    local.setTypeModel(t);
                    that.getDeclarationModel().setType(t);
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
    
    private void setType(Tree.LocalModifier local, 
            Tree.SpecifierOrInitializerExpression s, 
            Tree.TypedDeclaration that) {
        Tree.Expression e = s.getExpression();
        if (e!=null) {
            Type type = e.getTypeModel();
            if (type!=null) {
                Type t = 
                        unit.denotableType(type)
                            .withoutUnderlyingType();
                local.setTypeModel(t);
                that.getDeclarationModel().setType(t);
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
                Type t = 
                        unit.denotableType(type)
                            .withoutUnderlyingType();
                local.setTypeModel(t);
                that.getDeclarationModel().setType(t);
            }
        }
    }
        
    private void setSequencedValueType(Tree.SequencedType spread, 
            Type et, Tree.TypedDeclaration that) {
        Type t = 
                unit.denotableType(et)
                    .withoutUnderlyingType();
        spread.setTypeModel(t);
        that.getDeclarationModel().setType(t);
    }
        
    private void setValueType(Tree.ValueModifier local, 
            Type et, Tree.TypedDeclaration that) {
        Type t = 
                unit.denotableType(et)
                    .withoutUnderlyingType();
        local.setTypeModel(t);
        that.getDeclarationModel().setType(t);
    }
        
    private void setFunctionType(Tree.FunctionModifier local, 
            Type et, Tree.TypedDeclaration that) {
        Type t = 
                unit.denotableType(et)
                    .withoutUnderlyingType();
        local.setTypeModel(t);
        that.getDeclarationModel().setType(t);
    }
        
    private void setFunctionType(Tree.FunctionModifier local, 
            Type et, Tree.MethodArgument that) {
        Type t = 
                unit.denotableType(et)
                    .withoutUnderlyingType();
        local.setTypeModel(t);
        that.getDeclarationModel().setType(t);
    }
        
    @Override public void visit(Tree.Throw that) {
        super.visit(that);
        if (returnDeclaration instanceof Constructor) {
            that.addUnsupportedError("throw in constructor is not yet supported");
        }
        Tree.Expression e = that.getExpression();
        if (e!=null) {
            Type et = e.getTypeModel();
            if (!isTypeUnknown(et)) {
                Type tt = unit.getThrowableType();
                checkAssignable(et, tt, e, 
                        "thrown expression must be a throwable");
//                if (et.getDeclaration().isParameterized()) {
//                    e.addUnsupportedError("parameterized types in throw not yet supported");
//                }
            }
        }
    }
    
    @Override public void visit(Tree.Return that) {
        super.visit(that);
        if (returnType==null) {
            //misplaced return statements are already handled by ControlFlowVisitor
            //missing return types declarations already handled by TypeVisitor
            //that.addError("could not determine expected return type");
        } 
        else {
            that.setDeclaration(returnDeclaration);
            Tree.Expression e = that.getExpression();
            String name = returnDeclaration.getName();
            if (name==null || 
                    returnDeclaration.isAnonymous()) {
                name = "anonymous function";
            }
            else {
                name = "'" + name + "'";
            }
            if (e==null) {
                if (!(returnType instanceof Tree.VoidModifier)) {
                    that.addError("non-void function or getter must return a value: " +
                            name + " is not a void function");
                }
            }
            else {
                Type et = returnType.getTypeModel();
                Type at = e.getTypeModel();
                if (returnType instanceof Tree.VoidModifier) {
                    that.addError("void function, setter, or class initializer may not return a value: " +
                            name + " is declared 'void'");
                }
                else if (returnType instanceof Tree.LocalModifier) {
                    inferReturnType(et, at);
                }
                else {
                    if (!isTypeUnknown(et) && 
                            !isTypeUnknown(at)) {
                        checkAssignable(at, et, e, 
                                "returned expression must be assignable to return type of " +
                                        name, 2100);
                    }
                }
            }
        }
    }

    private void inferReturnType(Type et, Type at) {
        if (at!=null) {
            at = unit.denotableType(at);
            if (et==null || et.isSubtypeOf(at)) {
                returnType.setTypeModel(at);
            }
            else {
                if (!at.isSubtypeOf(et)) {
                    Type rt = unionType(at, et, unit);
                    returnType.setTypeModel(rt);
                }
            }
        }
    }
    
    private void checkMemberOperator(Type pt, 
            Tree.QualifiedMemberOrTypeExpression mte) {
        Tree.MemberOperator op = mte.getMemberOperator();
        Tree.Primary p = mte.getPrimary();
        if (op instanceof Tree.SafeMemberOp)  {
            checkOptional(pt, p);
        }
        else if (op instanceof Tree.SpreadOp) {
            if (!unit.isIterableType(pt)) {
                p.addError("expression must be of iterable type: '" +
                        pt.asString(unit) + 
                        "' is not a subtype of 'Iterable'");
            }
        }
    }
    
    private Type unwrap(Type type, 
            Tree.QualifiedMemberOrTypeExpression mte) {
        if (type==null) {
            return null;
        }
        else {
            Tree.MemberOperator op = mte.getMemberOperator();
            if (op instanceof Tree.SafeMemberOp)  {
                return unit.getDefiniteType(type);
            }
            else if (op instanceof Tree.SpreadOp) {
                if (unit.isIterableType(type)) {
                    Type it = unit.getIteratedType(type);
                    return it==null ?
                            unit.getUnknownType() : it;
                }
                else {
                    return type;
                }
            }
            else {
                return type;
            }
        }
    }
    
    Type wrap(Type type, Type receivingType, 
            Tree.QualifiedMemberOrTypeExpression mte) {
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
    
    /**
     * Typecheck an invocation expression. Note that this
     * is a tricky process involving type argument inference,
     * anonymous function parameter type inference, function
     * reference type argument inference, Java overload
     * resolution, and argument type checking, and it all
     * has to happen in exactly the right order, which is
     * not at all the natural order for walking the tree.
     */
    @Override public void visit(Tree.InvocationExpression that) {
        
        Tree.Primary p = that.getPrimary();
        p.visit(this);
        
        Tree.PositionalArgumentList pal = 
                that.getPositionalArgumentList();
        if (pal!=null) {
            inferParameterTypes(p, pal);
            pal.visit(this);
        }
        
        Tree.NamedArgumentList nal = 
                that.getNamedArgumentList();
        if (nal!=null) {
            inferParameterTypes(p, nal);
            nal.visit(this);
        }
        
        if (p!=null) {
            visitInvocationPositionalArgs(that);
            visitInvocationPrimary(that);
            if (isIndirectInvocation(that)) {
                visitIndirectInvocation(that);
            }
            else {
                visitDirectInvocation(that);
            }
        }
        
    }
    
    /**
     * Infer parameter types for anonymous functions in a
     * positional argument list, and set up references from
     * arguments back to parameter models. 
     */
    private void inferParameterTypes(Tree.Primary p,
            Tree.PositionalArgumentList pal) {
        Tree.Term term = unwrapExpressionUntilTerm(p);
        if (term instanceof Tree.MemberOrTypeExpression) {
            Tree.MemberOrTypeExpression mte = 
                    (Tree.MemberOrTypeExpression) term;
            Declaration dec = mte.getDeclaration();
            if (dec instanceof Functional) {
                inferParameterTypesDirectly(dec, pal, mte);
            }
            else if (dec instanceof Value) {
                Value value = (Value) dec;
                inferParameterTypesIndirectly(pal, 
                        value.getType());
            }
        }
        else {
            inferParameterTypesIndirectly(pal, 
                    p.getTypeModel());
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
                inferParameterTypesDirectly(dec, nal, mte);
            }
        }
    }

    /**
     * Infer parameter types of anonymous function arguments
     * in an indirect invocation with positional arguments.
     */
    private void inferParameterTypesIndirectly(
            Tree.PositionalArgumentList pal,
            Type pt) {
        if (unit.isCallableType(pt)) {
            List<Type> paramTypes = 
                    unit.getCallableArgumentTypes(pt);
            List<Tree.PositionalArgument> args = 
                    pal.getPositionalArguments();
            int argCount = args.size();
            int paramsSize = paramTypes.size();
            for (int i=0; i<paramsSize && i<argCount; i++) {
                Type paramType = paramTypes.get(i);
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
                            inferParameterTypesFromCallableType(
                                    paramType, null, fa);
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
    }
    
    /**
     * Infer parameter types of anonymous function arguments
     * in a direct invocation with positional arguments.
     * 
     * Also sets references from arguments back to parameters
     * by side effect.
     */
    private void inferParameterTypesDirectly(Declaration dec,
            Tree.PositionalArgumentList pal,
            Tree.MemberOrTypeExpression mte) {
        Reference pr = 
                getInvokedProducedReference(dec, mte);
        Functional fun = (Functional) dec;
        List<ParameterList> pls = fun.getParameterLists();
        if (!pls.isEmpty()) {
            ParameterList pl = pls.get(0);
            List<Parameter> params = pl.getParameters();
            List<Tree.PositionalArgument> args = 
                    pal.getPositionalArguments();
            int j=0;
            int argCount = args.size();
            int paramsSize = params.size();
            for (int i=0; i<argCount && j<paramsSize; i++) {
                Parameter param = params.get(j);
                Tree.PositionalArgument arg = args.get(i);
                arg.setParameter(param);
                if (arg instanceof Tree.ListedArgument) {
                    Tree.ListedArgument la = 
                            (Tree.ListedArgument) arg;
                    inferParameterTypes(pr, param, 
                            la.getExpression(), 
                            param.isSequenced());
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
    private void inferParameterTypesDirectly(Declaration dec,
            Tree.NamedArgumentList nal,
            Tree.MemberOrTypeExpression mte) {
        Reference pr = 
                getInvokedProducedReference(dec, mte);
        Functional fun = (Functional) dec;
        List<ParameterList> pls = fun.getParameterLists();
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
                                (Tree.SpecifiedArgument) arg;
                        Tree.SpecifierExpression se = 
                                sa.getSpecifierExpression();
                        if (se!=null) {
                            inferParameterTypes(pr, param, 
                                    se.getExpression(), 
                                    false);
                        }
                    }
                }
            }
            Tree.SequencedArgument sa = 
                    nal.getSequencedArgument();
            if (sa!=null) {
                Parameter param = 
                        getUnspecifiedParameter(pr, pl, 
                                foundParameters);
                if (param!=null) {
                    sa.setParameter(param);
                    for (Tree.PositionalArgument pa: 
                            sa.getPositionalArguments()) {
                        if (pa instanceof Tree.ListedArgument) {
                            Tree.ListedArgument la = 
                                    (Tree.ListedArgument) pa;
                            la.setParameter(param);
                            inferParameterTypes(pr, param, 
                                    la.getExpression(), 
                                    true);
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
        Generic fun = (Generic) dec;
        List<TypeParameter> tps = fun.getTypeParameters();
        if (isPackageQualified(mte)) {
            Tree.QualifiedMemberOrTypeExpression qmte = 
                    (Tree.QualifiedMemberOrTypeExpression) 
                        mte;
            Type invokedType = 
                    qmte.getPrimary()
                        .getTypeModel()
                        .resolveAliases();
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

    private static boolean isPackageQualified(
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
     * Infer parameter types for an anonymous function, and
     * also set the "target parameter" for a reference, 
     * which will be used later for inferring type arguments
     * for a function ref or generic function ref.
     */
    private void inferParameterTypes(Reference pr, 
            Parameter param, Tree.Expression e, 
            boolean variadic) {
        FunctionOrValue model = param.getModel();
        if (e!=null && model!=null) {
            Tree.Term term = 
                    unwrapExpressionUntilTerm(e.getTerm());
            TypedReference tpr = 
                    pr.getTypedParameter(param);
            if (term instanceof Tree.FunctionArgument) {
                Tree.FunctionArgument anon = 
                        (Tree.FunctionArgument) term;
                if (model instanceof Functional) {
                    //NOTE: this branch is basically redundant
                    //      and could be removed
                    inferParameterTypesFromCallableParameter(
                            pr, param, anon);
                }
                else { 
                    Type paramType = 
                            tpr.getFullType();
                    if (variadic) {
                        paramType = 
                                unit.getIteratedType(paramType);
                    }
                    if (unit.isCallableType(paramType)) {
                        inferParameterTypesFromCallableType(
                                paramType, param, anon);
                    }
                }
            }
            else if (term instanceof Tree.StaticMemberOrTypeExpression) {
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
    
    private void inferParameterTypesFromCallableType(
            Type paramType, Parameter param, 
            Tree.FunctionArgument anon) {
        List<Tree.ParameterList> apls = 
                anon.getParameterLists();
        if (!apls.isEmpty()) {
            List<Type> types = 
                    unit.getCallableArgumentTypes(paramType);
            List<Tree.Parameter> aps = 
                    apls.get(0).getParameters();
            Declaration declaration = param==null ? 
                    null : param.getDeclaration();
            for (int j=0; 
                    j<types.size() && 
                    j<aps.size(); 
                    j++) {
                Tree.Parameter ap = aps.get(j);
                if (ap instanceof Tree.InitializerParameter) {
                    Parameter parameter = 
                            ap.getParameterModel();
                    if (parameter.getModel()==null) {
                        createInferredParameter(anon,
                                declaration, ap,
                                parameter,
                                types.get(j));
                    }
                }
            }
        }
    }

    private void inferParameterTypesFromCallableParameter(
            Reference pr, Parameter param, 
            Tree.FunctionArgument anon) {
        Declaration declaration = param.getDeclaration();
        Functional fun = (Functional) param.getModel();
        List<ParameterList> fpls = fun.getParameterLists();
        List<Tree.ParameterList> apls = 
                anon.getParameterLists();
        if (!fpls.isEmpty() && !apls.isEmpty()) {
            List<Parameter> fps = 
                    fpls.get(0).getParameters();
            List<Tree.Parameter> aps = 
                    apls.get(0).getParameters();
            for (int j=0; 
                    j<fps.size() && j<aps.size(); 
                    j++) {
                Parameter fp = fps.get(j);
                Tree.Parameter ap = aps.get(j);
                if (ap instanceof Tree.InitializerParameter) {
                    Parameter parameter = 
                            ap.getParameterModel();
                    if (parameter.getModel()==null) {
                        createInferredParameter(anon,
                                declaration, ap,
                                parameter,
                                pr.getTypedParameter(fp)
                                    .getType());
                    }
                }
            }
        }
    }
    
    /**
     * Create a model for an inferred parameter of an
     * anonymous function.
     */
    private void createInferredParameter(Tree.FunctionArgument anon,
            Declaration declaration, Tree.Parameter ap,
            Parameter parameter, Type type) {
        if (isTypeUnknown(type)) {
            type = unit.getUnknownType();
            if (!dynamic) {
                ap.addError("could not infer parameter type: '" +
                        parameter.getName() + 
                        "' would have unknown type");
            }
        }
        else if (involvesTypeParams(declaration, type)) {
            ap.addError("could not infer parameter type: '" +
                    parameter.getName() + 
                    "' would have type '" + 
                    type.asString(unit) + 
                    "' involving type parameters");
            type = unit.getUnknownType();
        }
        Value model = new Value();
        model.setUnit(unit);
        model.setType(type);
        model.setName(parameter.getName());
        model.setInferred(true);
        parameter.setModel(model);
        model.setInitializerParameter(parameter);
        Function m = anon.getDeclarationModel();
        model.setContainer(m);
        m.addMember(model);
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
            List<Type> sig = 
                    new ArrayList<Type>
                        (args.size());
            for (Tree.PositionalArgument pa: args) {
                Type pat = pa.getTypeModel();
                sig.add(unit.denotableType(pat));
            }
            mte.setSignature(sig);
            mte.setEllipsis(hasSpreadArgument(args));
        }
    }
    
    private void checkSuperInvocation(
            Tree.MemberOrTypeExpression qmte) {
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
                    Type et = ci.getExtendedType();
                    if (et!=null) {
                        //TODO: might be better to pass the 
                        //      signature here in order to 
                        //      avoid an error when a 
                        //      different overloaded version 
                        //      has been refined
                        Declaration etm = 
                                et.getDeclaration()
                                    .getMember(name, null, false);
                        if (etm!=null && 
                                !etm.getContainer()
                                    .equals(type) && 
                                etm.refines(member)) {
                            TypeDeclaration container = 
                                    (TypeDeclaration) 
                                        etm.getContainer();
                            qmte.addError("inherited member is refined by intervening superclass: '" + 
                                    container.getName() + 
                                    "' refines '" + name + 
                                    "' declared by '" + 
                                    type.getName() + "'");
                        }
                    }
                    for (Type st: ci.getSatisfiedTypes()) {
                        //TODO: might be better to pass the 
                        //      signature here in order to 
                        //      avoid an error when a 
                        //      different overloaded version 
                        //      has been refined
                        Declaration stm = 
                                st.getDeclaration()
                                    .getMember(name, null, false);
                        if (stm!=null && 
                                !stm.getContainer()
                                    .equals(type) && 
                                stm.refines(member)) {
                            TypeDeclaration container = 
                                    (TypeDeclaration) 
                                        stm.getContainer();
                            qmte.addError("inherited member is refined by intervening superinterface: '" + 
                                    container.getName() + 
                                    "' refines '" + name + 
                                    "' declared by '" + 
                                    type.getName() + "'");
                        }
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
                    checkArgumentsAgainstTypeConstructor(
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
                tas.setTypeModels(typeArgs);
                visitBaseTypeExpression(bte, type, typeArgs, 
                        tas, receivingType);
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
                tas.setTypeModels(typeArgs);
                Tree.Primary primary = qte.getPrimary();
                if (primary instanceof Tree.Package) {
                    visitBaseTypeExpression(qte, type, 
                            typeArgs, tas, null);
                }
                else {
                    visitQualifiedTypeExpression(qte, 
                            receivingType, type, typeArgs, 
                            tas);
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
                tas.setTypeModels(typeArgs);
                visitBaseMemberExpression(bme, base, 
                        typeArgs, tas, receivingType);
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
                tas.setTypeModels(typeArgs);
                Tree.Primary primary = qme.getPrimary();
                if (primary instanceof Tree.Package) {
                    visitBaseMemberExpression(qme, 
                            member, typeArgs, tas, null);
                }
                else {
                    visitQualifiedMemberExpression(qme, 
                            receivingType, member, typeArgs, 
                            tas);
                }
            }
        }
    }

    protected Type getBaseReceivingType(
            Tree.InvocationExpression that,
            Declaration dec) {
        Scope scope = that.getScope();
        if (dec.isClassOrInterfaceMember() &&
                !dec.isStaticallyImportable() &&
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
                    List<PositionalArgument> args = 
                            pal.getPositionalArguments();
                    List<Parameter> params = 
                            pl.getParameters();
                    for (int i=0, j=0; 
                            i<args.size() && 
                            j<params.size();
                            i++) {
                        PositionalArgument arg = 
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
        if (isGeneric(dec)) {
            //a generic declaration
            if (explicit) {
                return getTypeArguments(tas, receiverType, 
                        getTypeParameters(dec));
            }
            else {
                return getInferredTypeArguments(that, 
                        (Generic) dec, receiverType);
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
            Generic generic, Type receiverType) {
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
                TypeDeclaration type = 
                        (TypeDeclaration) generic;
                return new TypeArgumentInference(unit)
                        .getInferredTypeArgsForStaticReference(
                                that, type, receiverType);
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
                return !declaration.isStaticallyImportable();
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
                List<Type> typeArgs = 
                        pt.getSupertype(cd)
                            .getTypeArgumentList();
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
            if (dec instanceof TypeDeclaration) {
                that.addError("type has no parameter list: '" + 
                        dec.getName(unit) + "'");
            }
            else {
                that.addError("function has no parameter list: '" +
                        dec.getName(unit) + "'");
            }
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
    
    /*private void checkSpreadArgumentSequential(Tree.SpreadArgument arg,
            Type argTuple) {
        if (!unit.isSequentialType(argTuple)) {
            arg.addError("spread argument expression is not sequential: " +
                    argTuple.asString(unit) + " is not a sequence type");
        }
    }*/
    
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
            Parameter sp = 
                    getUnspecifiedParameter(pr, pl, 
                            foundParameters);
            if (sp!=null && 
                    !unit.isNonemptyIterableType(
                            sp.getType())) {
                foundParameters.add(sp);
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
        Parameter sp = 
                getUnspecifiedParameter(pr, pl, 
                        foundParameters);
        if (sp==null) {
            sa.addError("all iterable parameters specified by named argument list: '" + 
                    pr.getDeclaration().getName(unit) +
                    "' does not declare any additional parameters of type 'Iterable'");
        }
        else {
            if (!foundParameters.add(sp)) {
                sa.addError("duplicate argument for parameter: '" +
                        sp.getName() + "' of '" + 
                        pr.getDeclaration().getName(unit) + "'");
            }
            else if (!dynamic &&
                    isTypeUnknown(sp.getType())) {
                sa.addError("parameter type could not be determined: " + 
                        paramdesc(sp) +
                        getTypeUnknownError(sp.getType()));
            }
            checkSequencedArgument(sa, pr, sp);
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
            TypedReference argRef = 
                    argDec.getTypedReference();
            if (isGeneric(argDec)) {
                argType = 
                        genericFunctionType(
                                (Generic) argDec, 
                                arg.getScope(), //TODO!!! 
                                argDec, argRef, unit);
            }
            else {
                argType = argRef.getFullType();
            }
            checkArgumentToVoidParameter(param, typedArg);
            if (!dynamic && 
                    isTypeUnknown(argType)) {
                arg.addError("could not determine type of named argument: '" + 
                        param.getName() + "'");
            }
        }
        TypedReference paramRef = 
                reference.getTypedParameter(param);
        FunctionOrValue paramModel = param.getModel();
        Type paramType;
        if (isGeneric(paramModel)) {
            paramType = 
                    ModelUtil.genericFunctionType(
                            (Generic) paramModel, 
                            arg.getScope(), //TODO!!! 
                            paramModel, paramRef, unit);
        }
        else {
            paramType = paramRef.getFullType();
        }

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
                            !isSatementExpression(e)) {
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
                            that instanceof Tree.Annotation && 
                            args.isEmpty() ? 
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
                Type pt = param.getType();
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
                                (Tree.ListedArgument) arg);
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
        a.setParameter(p);
        Type rat = arg.getTypeModel();
        if (!isTypeUnknown(rat)) {
            if (!unit.isIterableType(rat)) {
                //note: check already done by visit(SpreadArgument)
                /*arg.addError("spread argument is not iterable: " + 
                        rat.asString(unit) + 
                        " is not a subtype of Iterable");*/
            }
            else {
                Type at = spreadType(rat, unit, true);
                //checkSpreadArgumentSequential(arg, at);
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
        //checkSpreadArgumentSequential(sa, at);
        if (!isTypeUnknown(at)) {
            if (unit.isIterableType(at)) {
                Type sat = spreadType(at, unit, true);
                if (!isTypeUnknown(sat) && 
                        !isTypeUnknown(tailType)) {
                    checkAssignable(sat, tailType, sa, 
                            "spread argument not assignable to parameter types");
                }
            }
            else {
              //note: check already done by visit(SpreadArgument)
              /*sa.addError("spread argument is not iterable: " + 
                      at.asString(unit) + 
                      " is not a subtype of Iterable");*/
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
    
    private void checkComprehensionPositionalArgument(
            Parameter p, Reference pr, Tree.Comprehension c, 
            boolean atLeastOne) {
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
    
    private boolean hasSpreadArgument(
            List<Tree.PositionalArgument> args) {
        int size = args.size();
        if (size>0) {
            return args.get(size-1) 
                    instanceof Tree.SpreadArgument;
        }
        else {
            return false;
        }
    }

    private void checkPositionalArgument(Parameter p, 
            Reference pr, Tree.ListedArgument a) {
        FunctionOrValue model = p.getModel();
        if (model!=null) {
            TypedReference paramRef = 
                    pr.getTypedParameter(p);
            a.setParameter(p);
            Type paramType;
            if (isGeneric(model)) {
                paramType = 
                        genericFunctionType(
                                (Generic) model, 
                                a.getScope(), //TODO!!! 
                                model, paramRef, unit);
            }
            else {
                paramType = paramRef.getFullType();
            }
            Type at = a.getTypeModel();
            if (!isTypeUnknown(at) && 
                    !isTypeUnknown(paramType)) {
                checkAssignable(at, paramType, a, 
                        "argument must be assignable to parameter " + 
                                argdesc(p, pr), 2100);
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
            if (!isTypeUnknown(t)) {
                if (!unit.isIterableType(t)) {
                    e.addError("spread argument is not iterable: '" + 
                            t.asString(unit) + 
                            "' is not a subtype of 'Iterable'");
                }
            }
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
                        isTypeUnknown(lb.getTypeModel()) ||
                    ub!=null && 
                        isTypeUnknown(ub.getTypeModel());
        }
    }
    
    @Override public void visit(Tree.IndexExpression that) {
        super.visit(that);
        Type pt = type(that);
        if (pt==null) {
            that.addError("could not determine type of receiver");
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
            else if (!isTypeUnknown(pt) && 
                     !involvesUnknownTypes(eor)) {
                if (eor instanceof Tree.Element) {
                    Interface cd = 
                            unit.getCorrespondenceDeclaration();
                    Type cst = pt.getSupertype(cd);
                    if (cst==null) {
                        that.getPrimary()
                            .addError("illegal receiving type for index expression: '" +
                                    pt.getDeclaration().getName(unit) + 
                                    "' is not a subtype of 'Correspondence'");
                    }
                    else {
                        List<Type> args = 
                                cst.getTypeArgumentList();
                        Type kt = args.get(0);
                        Type vt = args.get(1);
                        Tree.Element e = (Tree.Element) eor;
                        Tree.Expression ee = 
                                e.getExpression();
                        checkAssignable(ee.getTypeModel(), 
                                kt, ee, 
                                "index must be assignable to key type");
                        Type rt = unit.getOptionalType(vt);
                        that.setTypeModel(rt);
                        Tree.Term t = ee.getTerm();
                        //TODO: in theory we could do a whole lot
                        //      more static-execution of the 
                        //      expression, but this seems
                        //      perfectly sufficient
                        refineTypeForTupleElement(that, pt, t);
                    }
                }
                else {
                    Interface rd = unit.getRangedDeclaration();
                    Type rst = pt.getSupertype(rd);
                    if (rst==null) {
                        that.getPrimary()
                            .addError("illegal receiving type for index range expression: '" +
                                    pt.getDeclaration().getName(unit) + 
                                    "' is not a subtype of 'Ranged'");
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
                        Expression l = er.getLength();
                        if (lb!=null) {
                            checkAssignable(lb.getTypeModel(), kt, lb, 
                                    "lower bound must be assignable to index type");
                        }
                        if (ub!=null) {
                            checkAssignable(ub.getTypeModel(), kt, ub, 
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

    private void refineTypeForTupleElement(
            Tree.IndexExpression that, Type pt, Tree.Term t) {
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
                lbt==null ? null : lbt.getTypeModel();
        Type rhst = 
                ubt==null ? null : ubt.getTypeModel();
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
            Type ta = 
                    ot.getTypeArgumentList().get(0);
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
                that.addError("values of disjoint types are never identical: '" +
                        lhst.asString(unit) + 
                        "' has empty intersection with '" +
                        rhst.asString(unit) + "'");
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
                    "operand expression must be of type Object");
            checkAssignable(rhst, obt, 
                    that.getRightTerm(), 
                    "operand expression must be of type Object");
        }
        that.setTypeModel(unit.getBooleanType());
    }
    
    private void visitAssignOperator(Tree.AssignOp that) {
        Type rhst = rightType(that);
        Type lhst = leftType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            Type leftHandType = lhst;
            // allow assigning null to java properties that could after all be null
            if (hasUncheckedNulls(that.getLeftTerm())) {
                leftHandType = 
                        unit.getOptionalType(leftHandType);
            }
            checkAssignable(rhst, leftHandType, 
                    that.getRightTerm(), 
                    "assigned expression must be assignable to declared type", 
                    2100);
        }
        that.setTypeModel(rhst);
//      that.setTypeModel(lhst); //this version is easier on backend
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
    
    private void visitArithmeticAssignOperator(Tree.BinaryOperatorExpression that, 
            TypeDeclaration type) {
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
            Type ot = 
                    unit.getObjectType();
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
        //TypeDeclaration sd = unit.getSetDeclaration();
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
            checkOptional(lhst, that.getLeftTerm());
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
            Type ct = 
                    checkSupertype(rhst,
                            unit.getCategoryDeclaration(),
                            that.getRightTerm(), 
                            "operand expression must be a category");
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
                    checkSupertype(t, type, 
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
        checkOptional(type(that), that.getTerm());
        that.setTypeModel(unit.getBooleanType());
    }
    
    private void visitNonemptyOperator(Tree.Nonempty that) {
        checkEmpty(type(that), that.getTerm());
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
                    if (!isTypeUnknown(pt)) {
                        if (!t.covers(pt)) {
                            that.addError("specified type does not cover the cases of the operand expression: '" +
                                    t.asString(unit) + "' does not cover '" + 
                                    pt.asString(unit) + "'");
                        }
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
                        if (knownType.isSubtypeOf(type)) {
                            that.addError("expression type is a subtype of the type: '" +
                                    knownType.asString(unit) + 
                                    "' is assignable to '" +
                                    type.asString(unit) + "'");
                        }
                        else {
                            if (intersectionType(type, knownType, unit)
                                    .isNothing()) {
                                that.addError("tests assignability to bottom type 'Nothing': intersection of '" +
                                        knownType.asString(unit) + "' and '" + 
                                        type.asString(unit) + "' is empty");
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
        visitSetOperator(that);
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
    
    @Override public void visit(Tree.AssignOp that) {
        assign(that.getLeftTerm());
        super.visit(that);
        visitAssignOperator(that);
        checkAssignability(that.getLeftTerm(), that);
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
        assign(that.getLeftTerm());
        super.visit(that);
        visitSetAssignmentOperator(that);
        checkAssignability(that.getLeftTerm(), that);
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
            if (!type.isVisible(that.getScope())) {
                that.addError("type is not visible: " + 
                        baseDescription(that), 400);
            }
            else if (type.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("package private type is not visible: " + 
                        baseDescription(that));
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
            if (!type.isVisible(that.getScope())) {
                if (type instanceof Constructor) {
                    that.addError("constructor is not visible: " + 
                            qualifiedDescription(that), 400);
                }
                else {
                    that.addError("member type is not visible: " + 
                            qualifiedDescription(that), 400);
                }
            }
            else if (type.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("package private member type is not visible: " + 
                        qualifiedDescription(that));
            }
            //this is actually slightly too restrictive
            //since a qualified type may in fact be an
            //inherited member type, but in that case
            //you can just get rid of the qualifier, so
            //in fact this restriction is OK
            else if (type.isProtectedVisibility() &&
                    !declaredInPackage(type, unit)) {
                that.addError("protected member type is not visible: " + 
                        qualifiedDescription(that));
            }
            //Note: we should remove this check if we ever 
            //      make qualified member types like T.Member
            //      into a sort of virtual type
            Tree.StaticType outerType = that.getOuterType();
			if (outerType instanceof Tree.SimpleType) {
				Tree.SimpleType st = 
				        (Tree.SimpleType) outerType;
                if (st.getDeclarationModel() 
                        instanceof TypeParameter) {
					outerType.addError("type parameter should not occur as qualifying type: " +
							qualifiedDescription(that));
				}
			}
        }
    }

    private void checkBaseVisibility(Node that, 
            TypedDeclaration member, String name) {
        if (!member.isVisible(that.getScope())) {
            that.addError("function or value is not visible: '" +
                    name + "'", 400);
        }
        else if (member.isPackageVisibility() && 
                !declaredInPackage(member, unit)) {
            that.addError("package private function or value is not visible: '" +
                    name + "'");
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
            that.addError("method or attribute is not visible: '" +
                    name + "' of " + container, 400);
        }
        else if (member.isPackageVisibility() && 
                !declaredInPackage(member, unit)) {
            that.addError("package private method or attribute is not visible: '" +
                    name + "' of " + container);
        }
        //this is actually too restrictive since
        //it doesn't take into account "other 
        //instance" access (access from a different
        //instance of the same type)
        else if (member.isProtectedVisibility() && 
                !selfReference && 
                !declaredInPackage(member, unit)) {
            that.addError("protected method or attribute is not visible: '" +
                    name + "' of " + container);
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
                that.addError("type is not visible: '" + name + "'");
            }
            else if (at.isPackageVisibility() &&
                    !declaredInPackage(type, unit)) {
                that.addError("package private type is not visible: '" + name + "'");
            }
            else if (at.isProtectedVisibility() &&
                    !declaredInPackage(type, unit)) {
                that.addError("protected type is not visible: '" + name + "'");
            }
            else if (!type.isVisible(that.getScope())) {
                that.addError("type constructor is not visible: '" + name + "'");
            }
            else if (type.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("package private constructor is not visible: '" + name + "'");
            }
            else if (type.isProtectedVisibility() &&
                    !declaredInPackage(type, unit)) {
                that.addError("protected constructor is not visible: '" + name + "'");
            }
        }
        else {
            if (!type.isVisible(that.getScope())) {
                that.addError("type is not visible: '" + name + "'", 400);
            }
            else if (type.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("package private type is not visible: '" + name + "'");
            }
            else if (type.isProtectedVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("protected type is not visible: '" + name + "'");
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
                that.addError("member type is not visible: '" +
                        name + "' of '" + container);
            }
            else if (at.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("package private member type is not visible: '" +
                        name + "' of type " + container);
            }
            else if (at.isProtectedVisibility() &&
                    !declaredInPackage(type, unit)) {
                that.addError("protected member type is not visible: '" +
                        name + "' of type " + container);
            }
            else if (!type.isVisible(that.getScope())) {
                that.addError("member type constructor is not visible: '" +
                        name + "' of " + container);
            }
            else if (type.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("package private member type constructor is not visible: '" +
                        name + "' of " + container);
            }
            else if (type.isProtectedVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("protected member type constructor is not visible: '" +
                        name + "' of " + container);
            }
        }
        else {
            if (!type.isVisible(that.getScope())) {
                if (type instanceof Constructor) {
                    that.addError("constructor is not visible: '" +
                            name + "' of " + container, 400);
                }
                else {
                    that.addError("member type is not visible: '" +
                            name + "' of " + container, 400);
                }
            }
            else if (type.isPackageVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("package private member type is not visible: '" +
                        name + "' of " + container);
            }
            else if (type.isProtectedVisibility() && 
                    !declaredInPackage(type, unit)) {
                that.addError("protected member type is not visible: '" +
                        name + "' of " + container);
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
        boolean notDirectlyInvoked = 
                !that.getDirectlyInvoked();
        TypedDeclaration member = 
                resolveBaseMemberExpression(that, 
                        notDirectlyInvoked);
        if (member!=null && notDirectlyInvoked) {
            if (inExtendsClause &&
                    constructorClass!=null &&
                    member.getContainer()
                        .equals(constructorClass)) {
                that.addError("reference to class member from constructor extends clause");
            }
            Tree.TypeArguments tal = that.getTypeArguments();
            List<Type> typeArgs;
            if (typeConstructorArgumentsInferrable(member, that)) {
                typeArgs = 
                        new TypeArgumentInference(unit)
                            .getInferredTypeArgsForFunctionRef(that);
            }
            else if (explicitTypeArguments(member, tal)) {
                typeArgs = getTypeArguments(tal, null, 
                        getTypeParameters(member));
            }
            else {
                typeArgs = 
                        new TypeArgumentInference(unit)
                            .getInferredTypeArgsForFunctionRef(that);
            }
            if (typeArgs!=null) {
                tal.setTypeModels(typeArgs);
                visitBaseMemberExpression(that, member, 
                        typeArgs, tal, null);
                //otherwise infer type arguments later
            }
            else {
                visitGenericBaseMemberReference(that, member);
//                typeArgumentsImplicit(that);
            }
        }
    }

    private void visitGenericBaseMemberReference(
            Tree.StaticMemberOrTypeExpression that,
            TypedDeclaration member) {
        if (isGeneric(member) && member instanceof Function) {
            Generic generic = (Generic) member;
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
        }
    }

    private TypedDeclaration resolveBaseMemberExpression(
            Tree.BaseMemberExpression that,
            boolean error) {
        Tree.Identifier id = that.getIdentifier();
        String name = name(id);
        TypedDeclaration member = 
                getTypedDeclaration(that.getScope(), name, 
                        that.getSignature(), 
                        that.getEllipsis(),
                        that.getUnit());
        if (member==null) {
            if (!dynamic &&
                    !isNativeForWrongBackend() &&
                    error) {
                that.addError("function or value does not exist: '" +
                        name + "'", 100);
                unit.getUnresolvedReferences()
                    .add(id);
            }
        }
        else {
            member = (TypedDeclaration) 
                    handleAbstractionOrHeader(member, that);
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
        boolean notDirectlyInvoked = 
                !that.getDirectlyInvoked();
        TypedDeclaration member = 
                resolveQualifiedMemberExpression(that, 
                        notDirectlyInvoked);
        if (member!=null && notDirectlyInvoked) {
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
                            .getInferredTypeArgsForFunctionRef(that);
            }
            else if (explicitTypeArguments(member, tal)) {
                typeArgs = getTypeArguments(tal, 
                        receiverType, 
                        getTypeParameters(member));
            }
            else {
                typeArgs = 
                        new TypeArgumentInference(unit)
                            .getInferredTypeArgsForFunctionRef(that);
            }
            if (typeArgs!=null) {
                tal.setTypeModels(typeArgs);
                if (primary instanceof Tree.Package) {
                    visitBaseMemberExpression(that, member, 
                            typeArgs, tal, null);
                }
                else {
                    visitQualifiedMemberExpression(that, 
                            receiverType, member, typeArgs, 
                            tal);
                }
                if (that.getStaticMethodReference()) {
                    handleStaticPrimaryImplicitTypeArguments(
                            that);
                }
                //otherwise infer type arguments later
            }
            else {
                if (that.getStaticMethodReference()) {
                    handleStaticPrimaryImplicitTypeArguments(
                            that);
                }
                else {
                    if (primary instanceof Tree.Package) {
                        visitGenericBaseMemberReference(that, 
                                member);
                    }
                    else {
                        visitGenericQualifiedMemberReference(
                                that, receiverType, member);
                    }
                }
            }
        }
    }

    private void visitGenericQualifiedMemberReference(
            Tree.QualifiedMemberExpression that,
            Type receiverType,
            TypedDeclaration member) {
        if (isGeneric(member) && member instanceof Function) {
            Generic generic = (Generic) member;
            Scope scope = that.getScope();
            TypedReference target = 
                    receiverType.getTypedMember(member, 
                            NO_TYPE_ARGS);
            that.setTarget(target);
            Type functionType = 
                    genericFunctionType(generic, scope, 
                            member, target, unit);
            that.setTypeModel(functionType);
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
    private void handleStaticPrimaryImplicitTypeArguments(
            Tree.QualifiedMemberOrTypeExpression that) {
        //we do this check later than usual, in order
        //to allow qualified refs to Java static members
        //without type arguments to the qualifying type
        if (isStaticReference(that)) {
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
            if (type!=null && 
                    !explicitTypeArguments(type, typeArgs) &&
                    typeArgs.getTypeModels()==null) { //nothing inferred
                Declaration declaration = 
                        smte.getDeclaration();
                Generic dec = (Generic) declaration;
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
                smte.addError("missing type arguments to generic type qualifying static reference: '" + 
                        declaration.getName(unit) + 
                        "' declares type parameters " + 
                        paramList);
            }
        }
    }

    private TypedDeclaration resolveQualifiedMemberExpression(
            Tree.QualifiedMemberExpression that, 
            boolean error) {
        Tree.Identifier id = that.getIdentifier();
        boolean nameNonempty = 
                id!=null && 
                !id.getText().equals("");
        if (nameNonempty && checkMember(that)) {
            Tree.Primary p = that.getPrimary();
            String name = name(id);
            List<Type> signature = 
                    that.getSignature();
            boolean ellipsis = that.getEllipsis();
            String container;
            boolean ambiguous;
            TypedDeclaration member;
            if (p instanceof Tree.Package) {
                Package pack = unit.getPackage();
                container = "package '" + 
                        pack.getNameAsString() + "'";
                member = getPackageTypedDeclaration(name, 
                        signature, ellipsis, unit);
                ambiguous = false;
            }
            else {
                Type pt = 
                        p.getTypeModel()
                         .resolveAliases(); //needed for aliases like "alias Id<T> => T"
                TypeDeclaration d = getDeclaration(that, pt);
                container = "type '" + d.getName(unit) + "'";
                Scope scope = that.getScope();
                ClassOrInterface ci = 
                        getContainingClassOrInterface(scope);
                if (ci!=null && 
                        d.inherits(ci) && 
                        !(d instanceof NothingType)) {
                    Declaration direct = 
                            ci.getDirectMember(name, 
                                    signature, ellipsis);
                    if (direct instanceof TypedDeclaration) {
                        member = (TypedDeclaration) direct;
                    }
                    else {
                        member = getTypedMember(d, name, 
                                signature, ellipsis, unit);
                    }
                }
                else {
                    member = getTypedMember(d, name, 
                            signature, ellipsis, unit);
                }
                ambiguous = member==null && 
                        d.isMemberAmbiguous(name, unit, 
                                signature, ellipsis);
            }
            if (member==null) {
                if (error) {
                    if (ambiguous) {
                        that.addError("method or attribute is ambiguous: '" +
                                name + "' for " + container);
                    }
                    else {
                        that.addError("method or attribute does not exist: '" +
                                name + "' in " + container, 
                                100);
                        unit.getUnresolvedReferences()
                            .add(id);
                    }
                }
            }
            else {
                member = (TypedDeclaration) 
                        handleAbstractionOrHeader(member, that);
                that.setDeclaration(member);
                resetSuperReference(that);
                boolean selfReference = 
                        isSelfReference(p);
                if (!selfReference && 
                        !member.isShared()) {
                    member.setOtherInstanceAccess(true);
                }
                if (error) {
                    if (checkConcreteConstructor(member, that)) {
                        checkQualifiedVisibility(that, 
                                member, name, container, 
                                selfReference);
                    }
                    checkSuperMember(that);
                }
            }
            return member;
        }
        else {
            return null;
        }
    }

    private boolean isSelfReference(Tree.Primary p) {
        return p instanceof Tree.This ||
                p instanceof Tree.Outer ||
                p instanceof Tree.Super;
    }

    private void checkSuperMember(
            Tree.QualifiedMemberOrTypeExpression that) {
        Tree.Term term = 
                eliminateParensAndWidening(that.getPrimary());
        if (term instanceof Tree.Super) {
            checkSuperInvocation(that);
        }
    }

    private void visitQualifiedMemberExpression(
            Tree.QualifiedMemberExpression that,
            Type receivingType, 
            TypedDeclaration member, 
            List<Type> typeArgs, 
            Tree.TypeArguments tal) {
        checkMemberOperator(receivingType, that);
        Type receiverType =
                accountForStaticReferenceReceiverType(that, 
                        unwrap(receivingType, that));
        if (acceptsTypeArguments(member, receiverType, 
                typeArgs, tal, that)) {
            TypedReference ptr = 
                    receiverType.getTypedMember(member, 
                            typeArgs, that.getAssigned());
            /*if (ptr==null) {
                that.addError("member method or attribute does not exist: " + 
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
            if (!dynamic && 
                    !isAbstraction(member) && 
                    isTypeUnknown(fullType)) {
                //this occurs with an ambiguous reference
                //to a member of an intersection type
                String rtname = 
                        receiverType.getDeclaration()
                            .getName(unit);
                that.addError("could not determine type of method or attribute reference: '" +
                        member.getName(unit) + 
                        "' of '" + rtname + "'" + 
                        getTypeUnknownError(fullType));
            }
            that.setTypeModel(accountForStaticReferenceType(
                    that, member, fullType));
            //}
        }
        if (that.getStaticMethodReference()) {
            handleStaticPrimaryImplicitTypeArguments(
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
                    //TODO: it's ugly to do this here, better to 
                    //      suck it into TypedReference
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
                if (primary.getStaticMethodReference()) {
                    Tree.QualifiedMemberOrTypeExpression qmte = 
                            (Tree.QualifiedMemberOrTypeExpression) 
                                primary;
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
                if (member.isStaticallyImportable()) {
                    //static member of Java type
                    if (primary.getStaticMethodReference()) {
                        Tree.QualifiedMemberOrTypeExpression qmte = 
                                (Tree.QualifiedMemberOrTypeExpression) 
                                    primary;
                        if (qmte.getDeclaration()
                                .isStaticallyImportable()) {
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
            Type receivingType) {
        if (acceptsTypeArguments(member, null, typeArgs, 
                tal, that)) {
            Type outerType = 
                    that.getScope()
                        .getDeclaringType(member);
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
            if (!dynamic && 
                    !isAbstraction(member) && 
                    isTypeUnknown(fullType)) {
                that.addError("could not determine type of function or value reference: '" +
                        member.getName(unit) + "'" + 
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
    }
    
    @Override public void visit(Tree.BaseTypeExpression that) {
        super.visit(that);
        boolean notDirectlyInvoked = 
                !that.getDirectlyInvoked();
        TypeDeclaration type = 
                resolveBaseTypeExpression(that, 
                        notDirectlyInvoked);
        if (type!=null && notDirectlyInvoked) {
            Tree.TypeArguments tal = that.getTypeArguments();
            List<Type> typeArgs;
            if (explicitTypeArguments(type, tal)) {
                List<TypeParameter> tps = 
                        type.getTypeParameters();
                typeArgs = getTypeArguments(tal, null, tps);
            }
            else {
                typeArgs = 
                        new TypeArgumentInference(unit)
                            .getInferredTypeArgsForFunctionRef(that);
            }
            if (typeArgs!=null) {
                tal.setTypeModels(typeArgs);
                visitBaseTypeExpression(that, type, typeArgs, 
                        tal, null);
                //otherwise infer type arguments later
            }
            else if (!that.getStaticMethodReferencePrimary()) {
                visitGenericBaseTypeReference(that, type);
            }
        }
    }

    private void visitGenericBaseTypeReference(
            Tree.StaticMemberOrTypeExpression that,
            TypeDeclaration type) {
        if (isGeneric(type) && type instanceof Class) {
            Generic generic = (Generic) type;
            Scope scope = that.getScope();
            Type outerType = 
                    scope.getDeclaringType(type);
            Type target = 
                    type.appliedType(outerType, 
                            typeParametersAsArgList(generic));
            that.setTarget(target);
            Type functionType = 
                    genericFunctionType(generic, scope, 
                            type, target, unit);
            that.setTypeModel(functionType);
        }
    }

    private TypeDeclaration resolveBaseTypeExpression(
            Tree.BaseTypeExpression that,
            boolean error) {
        Tree.Identifier id = that.getIdentifier();
        String name = name(id);
        TypeDeclaration type = 
                getTypeDeclaration(that.getScope(), name, 
                        that.getSignature(), 
                        that.getEllipsis(), 
                        that.getUnit());
        if (type==null) {
            if (!dynamic && !isNativeForWrongBackend() && error) {
                that.addError("type does not exist: '" + 
                        name + "'", 
                        102);
                unit.getUnresolvedReferences()
                    .add(id);
            }
        }
        else {
            type = (TypeDeclaration) 
                    handleAbstractionOrHeader(type, that);
            that.setDeclaration(type);
            if (error) {
                if (checkConcreteClass(type, that)) {
                    if (checkSealedReference(type, that)) {
                        checkBaseTypeAndConstructorVisibility(that, 
                                name, type);
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
                that.addError("partial constructor cannot be invoked: '" +
                        member.getName(unit) + "' is abstract");
                return false;
            }
            else if (container instanceof Class) {
                Class c = (Class) container;
                if (c.isAbstract()) {
                    that.addError("class cannot be instantiated: '" +
                            member.getName(unit) + 
                            "' is a constructor for the abstract class '" +
                            c.getName(unit));
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
                    that.addError("class cannot be instantiated: '" +
                            type.getName(unit) + "' is abstract");
                    return false;
                }
                else if (c.getParameterList()==null) {
                    that.addError("class cannot be instantiated: '" +
                            type.getName(unit) + 
                            "' does not have a default constructor");
                    return false;
                }
                else {
                    return true;
                }
            }
            else if (type instanceof Constructor) {
                Scope container = type.getContainer();
                if (type.isAbstract()) {
                    that.addError("partial constructor cannot be invoked: '" +
                            type.getName(unit) + "' is abstract");
                    return false;
                }
                else if (container instanceof Class) {
                    Class c = (Class) container;
                    if (c.isAbstract()) {
                        that.addError("class cannot be instantiated: '" +
                                type.getName(unit) + 
                                "' is a constructor for the abstract class '" +
                                c.getName(unit));
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
                that.addError("type cannot be instantiated: '" +
                        type.getName(unit) + "' is not a class");
                return false;
            }
        }
    }

	private boolean checkSealedReference(TypeDeclaration type,
            Tree.MemberOrTypeExpression that) {
	    if (type.isSealed() && 
	            !inSameModule(type, unit) &&
	    		!that.getStaticMethodReferencePrimary()) {
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
                that.addError("invokes or references a sealed constructor in a different module: '" +
	                    type.getName(unit) + "' of '" + cname + 
	                    "' in '" + moduleName + "'");
	        }
	        else {
	            that.addError("instantiates or references a sealed class in a different module: '" +
	                    type.getName(unit) + 
	                    "' in '" + moduleName + "'");
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
                    if (isOverloadedVersion(result)) {  
                        //it is a Java constructor
                        if (result.isPackageVisibility() && 
                                !declaredInPackage(result, unit)) {
                            that.addError("package private constructor is not visible: '" + 
                                    result.getName() + "'");
                        }
                    }
                }
                //else report to user that we could not
                //find a matching overloaded constructor
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
                if (pd instanceof Functional) {
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
        boolean notDirectlyInvoked = 
                !that.getDirectlyInvoked();
        TypeDeclaration type = 
                resolveQualifiedTypeExpression(that, 
                        notDirectlyInvoked);
        if (type!=null && notDirectlyInvoked) {
            Tree.Primary primary = that.getPrimary();
            Tree.TypeArguments tal = that.getTypeArguments();
            Type receiverType = 
                    primary.getTypeModel()
                        .resolveAliases();
            List<Type> typeArgs;
            if (explicitTypeArguments(type, tal)) {
                List<TypeParameter> tps = 
                        type.getTypeParameters();
                typeArgs = getTypeArguments(tal, receiverType, 
                        tps);
            }
            else {
                typeArgs = 
                        new TypeArgumentInference(unit)
                            .getInferredTypeArgsForFunctionRef(that);
            }
            if (typeArgs!=null) {
                tal.setTypeModels(typeArgs);
                if (primary instanceof Tree.Package) {
                    visitBaseTypeExpression(that, type, 
                            typeArgs, tal, null);
                }
                else {
                    visitQualifiedTypeExpression(that, 
                            receiverType, type, typeArgs, 
                            tal);
                }
                if (that.getStaticMethodReference()) {
                    handleStaticPrimaryImplicitTypeArguments(
                            that);
                }
                //otherwise infer type arguments later
            }
            else {
                if (that.getStaticMethodReference()) {
                    handleStaticPrimaryImplicitTypeArguments(
                            that);
                }
                else if (!that.getStaticMethodReferencePrimary()) {
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
        }
    }

    private void visitGenericQualifiedTypeReference(
            Tree.QualifiedTypeExpression that,
            Type outerType,
            TypeDeclaration type) {
        if (isGeneric(type) && type instanceof Class) {
            Generic generic = (Generic) type;
            Scope scope = that.getScope();
            Type target =
                    outerType.getTypeMember(type, 
                            typeParametersAsArgList(generic));
            that.setTarget(target);
            Type functionType = 
                    genericFunctionType(generic, scope, 
                            type, target, unit);
            that.setTypeModel(functionType);
        }
    }

    private TypeDeclaration resolveQualifiedTypeExpression(
            Tree.QualifiedTypeExpression that,
            boolean error) {
        if (checkMember(that)) {
            Tree.Primary p = that.getPrimary();
            Tree.Identifier id = that.getIdentifier();
            List<Type> signature = 
                    that.getSignature();
            boolean ellipsis = that.getEllipsis();
            String name = name(id);
            String container;
            boolean ambiguous;
            TypeDeclaration type;
            if (p instanceof Tree.Package) {
                Package pack = unit.getPackage();
                container = "package '" + 
                        pack.getNameAsString() + "'";
                type = getPackageTypeDeclaration(name, 
                        signature, ellipsis, unit);
                ambiguous = false;
            }
            else {
                Type pt = 
                        p.getTypeModel()
                         .resolveAliases(); //needed for aliases like "alias Id<T> => T"
                TypeDeclaration d = getDeclaration(that, pt);
                container = "type '" + d.getName(unit) + "'";
                Scope scope = that.getScope();
                ClassOrInterface ci = 
                        getContainingClassOrInterface(scope);
                if (ci!=null && 
                        !(d instanceof NothingType) &&
                        d.inherits(ci)) {
                    Declaration direct = 
                            ci.getDirectMember(name, 
                                    signature, ellipsis);
                    if (direct instanceof TypeDeclaration) {
                        type = (TypeDeclaration) direct;
                    }
                    else {
                        type = getTypeMember(d, name, 
                                signature, ellipsis, unit);
                    }
                }
                else {
                    type = getTypeMember(d, name, 
                            signature, ellipsis, unit);
                }
                ambiguous = type==null && 
                        d.isMemberAmbiguous(name, unit, 
                                signature, ellipsis);
            }
            if (type==null) {
                if (error) {
                    if (ambiguous) {
                        that.addError("member type is ambiguous: '" +
                                name + "' for " + container);
                    }
                    else {
                        that.addError("member type does not exist: '" +
                                name + "' in " + container, 
                                100);
                        unit.getUnresolvedReferences()
                            .add(id);
                    }
                }
            }
            else {
                type = (TypeDeclaration) 
                        handleAbstractionOrHeader(type, that);
                that.setDeclaration(type);
                resetSuperReference(that);
                if (!isSelfReference(p) && 
                        !type.isShared()) {
                    type.setOtherInstanceAccess(true);
                }
                if (error) {
                    if (checkConcreteClass(type, that)) {
                        if (checkSealedReference(type, that)) {
                            checkQualifiedTypeAndConstructorVisibility(that, 
                                    type, name, container);
                        }
                    }
                    if (!inExtendsClause) {
                        checkSuperMember(that);
                    }
                }
            }
            return type;
        }
        else {
            return null;
        }
    }
    
    private static boolean checkMember(
            Tree.QualifiedMemberOrTypeExpression qmte) {
        Tree.Primary p = qmte.getPrimary();
        Type pt = p.getTypeModel();
        boolean packageQualified = p instanceof Tree.Package;
        return packageQualified ||
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
        if (that.getStaticMethodReference()) {
            Tree.MemberOrTypeExpression primary = 
                    (Tree.MemberOrTypeExpression) 
                        that.getPrimary();
            TypeDeclaration td = (TypeDeclaration) 
                    primary.getDeclaration();
            return td==null ? new UnknownType(unit) : td;
        }
        else {
            return unwrap(pt, that).getDeclaration();
        }
    }

    private boolean explicitTypeArguments
            (Declaration dec, Tree.TypeArguments tal) {
        return tal instanceof Tree.TypeArgumentList ||
                !dec.isParameterized();
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
                acceptsTypeArguments(type, null, typeArgs, 
                        tal, that);
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
            Tree.TypeArguments tal) {
        checkMemberOperator(receivingType, that);
        Type receiverType =
                accountForStaticReferenceReceiverType(that, 
                        unwrap(receivingType, that));
        if (acceptsTypeArguments(memberType, receiverType, 
                typeArgs, tal, that)) {
            Type type = 
                    receiverType.getTypeMember(memberType, 
                            typeArgs);
            that.setTarget(type);
            Type fullType =
                    type.getFullType(wrap(type, 
                            receivingType, that));
            if (!dynamic && 
                    !that.getStaticMethodReference() &&
                    memberType instanceof Class &&
                    !isAbstraction(memberType) &&
                    isTypeUnknown(fullType)) {
                //this occurs with an ambiguous reference
                //to a member of an intersection type
                String rtname = 
                        receiverType.getDeclaration()
                            .getName(unit);
                that.addError("could not determine type of member class reference: '" +
                        memberType.getName(unit)  + "' of '" + 
                        rtname + "'");
            }
            that.setTypeModel(accountForStaticReferenceType(
                    that, memberType, fullType));
        }
        if (that.getStaticMethodReference()) {
            handleStaticPrimaryImplicitTypeArguments(
                    that);
        }
    }
    
    private void visitBaseTypeExpression(
            Tree.StaticMemberOrTypeExpression that, 
            TypeDeclaration baseType, 
            List<Type> typeArgs, 
            Tree.TypeArguments tal, 
            Type receivingType) {
        if (acceptsTypeArguments(baseType, null, typeArgs, 
                tal, that)) {
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
            that.addError("outer appears outside a nested class or interface definition");
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
                that.addError("super occurs outside any type definition");
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
                that.addError("this appears outside a class or interface definition");
            }
            else {
                that.setDeclarationModel(ci);
                that.setTypeModel(ci.getType());
            }
        }
    }
    
    @Override public void visit(Tree.Package that) {
        if (!that.getQualifier()) {
            that.addError("package must qualify a reference to a toplevel declaration");
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
            if (vt instanceof Tree.LocalModifier) {
                Type et = unit.getExceptionType();
                vt.setTypeModel(et);
                var.getDeclarationModel().setType(et);
            }
            else {
                checkAssignable(vt.getTypeModel(), 
                        unit.getThrowableType(), vt, 
                        "catch type must be a throwable type");
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
        for (Tree.Expression e: 
                that.getExpressionList()
                    .getExpressions()) {
            if (e!=null) {
                Type t = e.getTypeModel();
                if (!isTypeUnknown(t)) {
                    if (switchStatementOrExpression!=null) {
                        Tree.Switched switched = 
                                switchClause().getSwitched();
                        if (switched!=null) {
                            Tree.Expression switchExpression = 
                                    switched.getExpression();
                            Tree.Variable switchVariable = 
                                    switched.getVariable();
                            Type st = 
                                    getSwitchType(switchExpression,
                                            switchVariable);
                            if (st!=null) {
                                Type it = 
                                        intersectionType(t, st, unit);
                                if (it.isNothing() &&
                                        mayNotBeNothing(switchExpression, 
                                                switchVariable, t)) {
                                    e.addError("value is not a case of the switched type: '" +
                                                t.asString(unit) +
                                                "' is not a case of the type '" +
                                                st.asString(unit) + "'");
                                }
                            }
                        }
                    }
                    checkValueCase(e);
                }
            }
        }
    }

    private void checkValueCase(Tree.Expression e) {
        Tree.Term term = e.getTerm();
        Type type = e.getTypeModel();
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
            TypeDeclaration dec = type.getDeclaration();
            boolean isToplevelInstance = 
                    dec.isObjectClass() && dec.isToplevel();
            boolean isToplevelClassInstance = 
                    dec.isValueConstructor() &&
                    (dec.getContainer().isToplevel() || dec.isStaticallyImportable());
            if (!isToplevelInstance && 
                !isToplevelClassInstance) {
                e.addError("case must refer to a toplevel object declaration, value constructor for a toplevel class, or literal value");
            }
            else {
                Type ut = unionType(
                        unit.getNullType(), 
                        unit.getIdentifiableType(), 
                        unit);
                checkAssignable(type, ut, e, 
                        "case must be identifiable or null");
            }
        }
        else if (term!=null) {
            e.addError("case must be a literal value or refer to a toplevel object declaration or value constructor for a toplevel class");
        }
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
                Tree.Variable v = that.getVariable();
                Type st = 
                        getSwitchType(switchExpression,
                                switchVariable);
                if (st!=null) {
                    if (v!=null) {
                        if (dynamic || 
                                !isTypeUnknown(st)) { //eliminate dupe errors
                            v.visit(this);
                        }
                        initOriginalDeclaration(v);
                    }
                    if (t!=null) {
                        Type pt = t.getTypeModel();
//                        if (!isTypeUnknown(pt)) {
                        Type it = 
                                intersectionType(pt, st, unit);
                        if (it.isNothing() &&
                                mayNotBeNothing(switchExpression, 
                                        switchVariable, pt)) {
                            that.addError("narrows to bottom type 'Nothing': '" + 
                                    pt.asString(unit) + 
                                    "' has empty intersection with '" + 
                                    st.asString(unit) + "'");
                        }
                        if (v!=null) {
                            v.getType().setTypeModel(it);
                            v.getDeclarationModel().setType(it);
                        }
//                        }
                    }
                }
            }
        }
    }

    private static boolean mayNotBeNothing(Tree.Expression switchExpression,
            Tree.Variable switchVariable, Type pt) {
        return switchVariable!=null || 
                switchExpression!=null &&
                (!hasUncheckedNulls(switchExpression.getTerm()) || 
                        !pt.isNull());
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
        
        checkCasesExhaustive(that.getSwitchClause(), 
                that.getSwitchCaseList());
        
        switchStatementOrExpression = oss;
        ifStatementOrExpression = ois;
    }

    private void checkCasesExhaustive(Tree.SwitchClause switchClause,
            Tree.SwitchCaseList switchCaseList) {
        Tree.Switched switched = 
                switchClause.getSwitched();
        if (switched!=null) {
            Type switchExpressionType = 
                    getSwitchedExpressionType(switched);
            if (switchCaseList!=null && 
                    switchExpressionType!=null) {
                checkCases(switchCaseList);
                Tree.ElseClause elseClause = 
                        switchCaseList.getElseClause();
                if (!isTypeUnknown(switchExpressionType) 
                        && elseClause==null) {
                    Type caseUnionType = 
                            caseUnionType(switchCaseList);
                    if (caseUnionType!=null) {
                        //if the union of the case types covers 
                        //the switch expression type then the 
                        //switch is exhaustive
                        if (!caseUnionType.covers(switchExpressionType)) {
                            switchClause.addError("case types must cover all cases of the switch type or an else clause must appear: '" +
                                    caseUnionType.asString(unit) + "' does not cover '" + 
                                    switchExpressionType.asString(unit) + "'", 
                                    10000);
                        }
                    }
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
                                        /*unit.denotableType(*/
                                        switchExpressionType
                                            .minus(caseUnionType);
                                var.getType()
                                    .setTypeModel(complementType);
                                var.getDeclarationModel()
                                    .setType(complementType);
                            }
                        }
                    }
                }
            }
            if (ifStatementOrExpression!=null) {
                Tree.ConditionList conditionList = 
                        ifClause().getConditionList();
                if (conditionList!=null) {
                    Tree.Condition c = 
                            conditionList.getConditions()
                                .get(0);
                    Tree.SpecifierExpression se = 
                            var.getSpecifierExpression();
                    if (c instanceof Tree.ExistsCondition) {
                        Tree.ExistsCondition ec = 
                                (Tree.ExistsCondition) c;
                        inferDefiniteType(var, se, !ec.getNot());
                    }
                    else if (c instanceof Tree.NonemptyCondition) {
                        Tree.NonemptyCondition ec = 
                                (Tree.NonemptyCondition) c;
                        inferNonemptyType(var, se, !ec.getNot());
                    }
                    else if (c instanceof Tree.IsCondition) {
                        Tree.IsCondition ic = 
                                (Tree.IsCondition) c;
                        Tree.Expression e = se.getExpression();
                        Type t = 
                                narrow(ic.getType().getTypeModel(), 
                                        e.getTypeModel(),
                                        !ic.getNot());
                        var.getType().setTypeModel(t);
                        var.getDeclarationModel().setType(t);
                    }
                }
            }
        }
        Tree.Block block = that.getBlock();
        if (block!=null) block.visit(this);
        Tree.Expression expression = that.getExpression();
        if (expression!=null) expression.visit(this);
    }
    
    private void checkCases(Tree.SwitchCaseList switchCaseList) {
        List<Tree.CaseClause> cases = 
                switchCaseList.getCaseClauses();
        boolean hasIsCase = false;
        for (Tree.CaseClause cc: cases) {
            if (cc.getCaseItem() instanceof Tree.IsCase) {
                hasIsCase = true;
            }
            for (Tree.CaseClause occ: cases) {
                if (occ==cc) break;
                checkCasesClausesDisjoint(cc, occ);
            }
        }
        if (hasIsCase) {
            if (switchStatementOrExpression!=null) {
                Tree.Switched switched = 
                        switchClause().getSwitched();
                if (switched!=null) {
                    Tree.Expression switchExpression =
                            switched.getExpression();
                    if (switchExpression!=null) {
                        Tree.Term st = 
                                switchExpression.getTerm();
                        if (st instanceof Tree.BaseMemberExpression) {
                            Tree.BaseMemberExpression bme = 
                                    (Tree.BaseMemberExpression) st;
                            checkReferenceIsNonVariable(bme, true);
                        }
                        else if (st!=null) {
                            st.addError("switch expression must be a value reference in switch with type cases", 3102);
                        }
                    }
                }
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
            Type ct = getTypeIgnoringLiterals(cc);
            if (isTypeUnknown(ct)) {
                return null; //Note: early exit!
            }
            else {
                addToUnion(list, ct);
            }
        }
        return ModelUtil.union(list, unit);
    }

    private void checkCasesClausesDisjoint(
            Tree.CaseClause cc, 
            Tree.CaseClause occ) {
        Tree.CaseItem cci = cc.getCaseItem();
        Tree.CaseItem occi = occ.getCaseItem();
        if (cci instanceof Tree.IsCase || 
            occi instanceof Tree.IsCase) {
            checkCasesDisjoint(
                    getType(cc), 
                    getType(occ), 
                    cci);
        }
        else {
            checkCasesDisjoint(
                    getTypeIgnoringLiterals(cc),
                    getTypeIgnoringLiterals(occ), 
                    cci);
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
                cci.getExpressionList().getExpressions()) {
            for (Tree.Expression f: 
                occi.getExpressionList().getExpressions()) {
                Tree.Term et = e.getTerm();
                Tree.Term ft = f.getTerm();
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
                        cci.addUnsupportedError("literal cases with mixed bases not yet supported");
                    }
                    else if (etv.equals(ftv) && eneg==fneg) {
                        cci.addError("literal cases must be disjoint: " +
                                (eneg?"-":"") +
                                etv.replaceAll("\\p{Cntrl}","?") + 
                                " occurs in multiple cases");
                    }
                }
            }
        }
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
            return t.getTypeModel().getUnionOfCases();
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
            List<Tree.Expression> es = 
                    mc.getExpressionList().getExpressions();
            List<Type> list = 
                    new ArrayList<Type>(es.size());
            for (Tree.Expression e: es) {
                if (e.getTypeModel()!=null) {
                    addToUnion(list, e.getTypeModel());
                }
            }
            return union(list, unit);
        }
        else {
            return null;
        }
    }

    private Type getTypeIgnoringLiterals(Tree.CaseClause cc) {
        Tree.CaseItem ci = cc.getCaseItem();
        if (ci instanceof Tree.IsCase) {
            return getType(ci);
        }
        else if (ci instanceof Tree.MatchCase) {
            Tree.MatchCase mc = (Tree.MatchCase) ci;
            List<Tree.Expression> es = 
                    mc.getExpressionList().getExpressions();
            List<Type> list = 
                    new ArrayList<Type>(es.size());
            for (Tree.Expression e: es) {
                if (e.getTypeModel()!=null && 
                        !(e.getTerm() instanceof Tree.Literal) && 
                        !(e.getTerm() instanceof Tree.NegativeOp)) {
                    addToUnion(list, e.getTypeModel());
                }
            }
            return union(list, unit);
        }
        else {
            return null;
        }
    }
    
    @Override
    public void visit(Tree.TryCatchStatement that) {
        super.visit(that);
        for (Tree.CatchClause cc: that.getCatchClauses()) {
            Tree.CatchVariable ccv = cc.getCatchVariable();
            if (ccv!=null && 
                    ccv.getVariable()!=null) {
                Type ct = 
                        ccv.getVariable().getType()
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
     *        
     * @return false if there were errors, or if there
     *         were no arguments for a generic declaration
     */
    private boolean acceptsTypeArguments(Declaration dec, 
            Type receiverType, 
            List<Type> typeArguments, 
            Tree.TypeArguments tas, 
            Node parent) {
        if (dec==null) {
            return false;
        }
        else if (isGeneric(dec)) {
            if (typeArguments==null) {
                return false;
            }
            else {
                return checkTypeArgumentAgainstDeclaration(
                        receiverType, dec, typeArguments, 
                        tas, parent);
            }
        }
        else {
            boolean empty = 
                    typeArguments==null ||
                    typeArguments.isEmpty();
            if (dec instanceof TypeAlias && !empty) {
                TypeAlias alias = (TypeAlias) dec;
                dec = unwrapAliasedTypeConstructor(alias);
                return checkTypeArgumentAgainstDeclaration(
                        receiverType, dec, typeArguments, 
                        tas, parent);
            }
            if (dec instanceof Value && !empty) {
                Value td = (Value) dec;
                Type type = td.getType();
                if (type!=null) {
                    type = type.resolveAliases();
                    if (type.isTypeConstructor()) {
                        checkArgumentsAgainstTypeConstructor(
                                typeArguments, tas, type, 
                                parent);
                        return true;
                    }
                }
            }
            boolean explicit = 
                    tas instanceof Tree.TypeArgumentList;
            if (!empty || explicit) {
                tas.addError("does not accept type arguments: '" + 
                        dec.getName(unit) + 
                        "' is not a generic declaration");
            }
            return empty;
        }
    }

    private boolean checkTypeArgumentAgainstDeclaration(
            Type receiver, Declaration dec, 
            List<Type> typeArguments,
            Tree.TypeArguments tas, Node parent) {
        Generic g = (Generic) dec;
        List<TypeParameter> params = g.getTypeParameters();
        boolean explicit = 
                tas instanceof Tree.TypeArgumentList;
        int min = 0;
        for (TypeParameter tp: params) { 
            if (!tp.isDefaulted()) min++;
        }
        if (receiver==null && 
                dec.isClassOrInterfaceMember()) {
            receiver = parent.getScope()
                    .getDeclaringType(dec);
        }
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
                    /*if (argType.isTypeConstructor() && 
                            !param.isTypeConstructor()) {
                        typeArgNode(tas, i, parent)
                            .addError("type argument must be a regular type: parameter '" + 
                                    param.getName() + 
                                    "' is a regular type parameter but '" +
                                    argType.asString(unit) + 
                                    "' is a type constructor");
                    }
                    else*/
                    if (!argType.isTypeConstructor() && 
                            param.isTypeConstructor()) {
                        typeArgNode(tas, i, parent)
                            .addError("type argument must be a type constructor: parameter '" + 
                                    param.getName() + 
                                    "' is a type constructor parameter but '" +
                                    argType.asString(unit) + 
                                    "' is a regular type");
                    }
                    else if (param.isTypeConstructor()) {
                        Node argNode;
                        if (explicit) {
                            Tree.TypeArgumentList tl = 
                                    (Tree.TypeArgumentList) tas;
                            argNode = tl.getTypes().get(i);
                        }
                        else {
                            argNode = parent;
                        }
                        checkTypeConstructorParam(param, 
                                argType, argNode);
                    }
                }
                List<Type> sts = param.getSatisfiedTypes();
                boolean hasConstraints = 
                        !sts.isEmpty() || 
                        param.getCaseTypes()!=null;
                boolean enforceConstraints = 
                        modelLiteral ||
                        !(parent instanceof Tree.SimpleType) ||
                        ((Tree.SimpleType) parent).getInherited();
                if (//!isCondition && 
                        hasConstraints &&
                        enforceConstraints) {
                    Type assignedType = 
                            argumentTypeForBoundsCheck(param, 
                                    argType);
                    for (Type st: sts) {
                        Type bound =
                                st.appliedType(receiver, 
                                        dec, typeArguments, 
                                        null);
                        if (!assignedType.isSubtypeOf(bound)) {
                            if (argTypeMeaningful) {
                                if (explicit) {
                                    typeArgNode(tas, i, parent)
                                        .addError("type parameter '" + param.getName() 
                                                + "' of declaration '" + dec.getName(unit)
                                                + "' has argument '" 
                                                + assignedType.asString(unit) 
                                                + "' which is not assignable to upper bound '" 
                                                + bound.asString(unit)
                                                + "' of '" + param.getName() + "'", 2102);
                                }
                                else {
                                    parent.addError("inferred type argument '" 
                                            + assignedType.asString(unit)
                                            + "' to type parameter '" + param.getName()
                                            + "' of declaration '" + dec.getName(unit)
                                            + "' is not assignable to upper bound '" 
                                            + bound.asString(unit)
                                            + "' of '" + param.getName() + "'");
                                }
                            }
                            return false;
                        }
                    }
                    if (!argumentSatisfiesEnumeratedConstraint(receiver, 
                            dec, typeArguments, assignedType, param)) {
                        if (argTypeMeaningful) {
                            if (explicit) {
                                typeArgNode(tas, i, parent)
                                    .addError("type parameter '" + param.getName() 
                                            + "' of declaration '" + dec.getName(unit)
                                            + "' has argument '" 
                                            + assignedType.asString(unit) 
                                            + "' which is not one of the enumerated cases of '" 
                                            + param.getName() + "'");
                            }
                            else {
                                parent.addError("inferred type argument '" 
                                        + assignedType.asString(unit)
                                        + "' to type parameter '" + param.getName()
                                        + "' of declaration '" + dec.getName(unit)
                                        + "' is not one of the enumerated cases of '" 
                                        + param.getName() + "'");
                            }
                        }
                        return false;
                    }
                }
            }
            return true;
        }
        else {
            if (explicit) {
                StringBuilder paramList = 
                        new StringBuilder();
                for (TypeParameter tp: 
                        g.getTypeParameters()) {
                    if (paramList.length()>0) {
                        paramList.append(", ");
                    }
                    paramList.append("'")
                        .append(tp.getName())
                        .append("'");
                }
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
                tas.addError("wrong number of type arguments: '" + 
                        dec.getName(unit) + "'" + help);
            }
            else {
                //Now handled in TypeArgumentVisitor
//                    if (!metamodel) {
//                        parent.addError("missing type arguments to generic type: '" + 
//                                dec.getName(unit) + 
//                                "' declares type parameters");
//                    }
            }
            return false;
        }
    }

    private static Type argumentTypeForBoundsCheck(
            TypeParameter param, Type argType) {
        if (argType==null) {
            return null;
        }
        else if (argType.isTypeConstructor()) {
            return argType.getDeclaration()
                .appliedType(null, 
                    param.getType()
                        .getTypeArgumentList());
        }
        else {
            return argType;
        }
    }

    private static Node typeArgNode(Tree.TypeArguments tas, 
            int i, Node parent) {
        if (tas instanceof Tree.TypeArgumentList) {
            Tree.TypeArgumentList tal = 
                    (Tree.TypeArgumentList) tas;
            return tal.getTypes().get(i);
        }
        else {
            return parent;
        }
    }

    private void checkArgumentsAgainstTypeConstructor(
            List<Type> typeArguments,
            Tree.TypeArguments tas, Type type,
            Node parent) {
        boolean explicit = 
                tas instanceof Tree.TypeArgumentList;
        TypeDeclaration tcd = type.getDeclaration();
        List<TypeParameter> typeParameters = 
                tcd.getTypeParameters();
        int size = typeArguments.size();
        if (size != typeParameters.size()) {
            tas.addError("wrong number of type arguments: type constructor requires " + 
                    size + " type arguments");
        }
        else {
            Map<TypeParameter, Type> args = 
                    getTypeArgumentMap(tcd, null, 
                            typeArguments);
            Map<TypeParameter, SiteVariance> variances =
                    Collections.emptyMap(); //TODO!!!!!
            for (int i=0; i<size; i++) {
                TypeParameter param = typeParameters.get(i);
                Type arg = typeArguments.get(i);
                if (!isTypeUnknown(arg)) {
                    List<Type> sts = 
                            param.getSatisfiedTypes();
                    for (Type st: sts) {
                        Type bound = 
                                st.substitute(args, 
                                        variances);
                        if (!isTypeUnknown(bound) &&
                                !arg.isSubtypeOf(bound)) {
                            String message = 
                                    "type argument '" + 
                                    arg.asString(unit) +
                                    "' is not assignable to upper bound '" +
                                    bound.asString(unit) +
                                    "' of type parameter '" +
                                    param.getName() + "' of '" +
                                    param.getDeclaration()
                                        .getName(unit) + 
                                    "'";
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
                if (!satisfiesEnumeratedConstraint(param, 
                        arg, args, variances)) {
                    String message = 
                            "type argument '" + 
                            arg.asString(unit) +
                            "' is not one of the enumerated cases of the type parameter '" +
                            param.getName() + "' of '" +
                            param.getDeclaration()
                                .getName(unit) + 
                            "'";
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
            Map<TypeParameter, Type> args, 
            Map<TypeParameter, SiteVariance> variances) {
        List<Type> cts = param.getCaseTypes();
        if (cts!=null) {
            for (Type ct: cts) {
                Type bound = 
                        ct.substitute(args, 
                                variances);
                if (arg.isSubtypeOf(bound)) {
                    return true;
                }
            }
            if (arg.isTypeParameter()) {
                for (Type act: arg.getCaseTypes()) {
                    boolean foundCase = false;
                    for (Type ct: cts) {
                        Type bound = 
                                ct.substitute(args, 
                                        variances);
                        if (act.isSubtypeOf(bound)) {
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
        
        if (!argType.isTypeConstructor()) {
            argNode.addError("not a type constructor: '" +
                    argType.asString(unit) + "'");
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
                    argNode.addError("argument type constructor has wrong number of type parameters: argument '" +
                            argTypeDec.getName(unit) + "' has " + 
                            allowed + " type parameters " +
                            "but parameter '" + 
                            param.getName(unit) + "' has " + 
                            size + " type parameters");
                }
                for (int j=0; j<size && j<allowed; j++) {
                    TypeParameter paramParam = 
                            paramTypeParams.get(j);
                    TypeParameter argParam = 
                            argTypeParams.get(j);
                    if (paramParam.isCovariant() &&
                            !argParam.isCovariant()) {
                        argNode.addError("argument type constructor is not covariant: '" +
                                argParam.getName() + "' of '" + 
                                argTypeDec.getName(unit) + 
                                "' must have the same variance as '" +
                                paramParam.getName() + "' of '" + 
                                param.getName(unit) + 
                                "'");
                    }
                    else if (paramParam.isContravariant() &&
                            !argParam.isContravariant()) {
                        argNode.addError("argument type constructor is not contravariant: '" +
                                argParam.getName() + "' of '" + 
                                argTypeDec.getName(unit) + 
                                "' must have the same variance as '" +
                                paramParam.getName() + "' of '" + 
                                param.getName(unit) + 
                                "'");
                    }
                    if (!intersectionOfSupertypes(paramParam)
                            .isSubtypeOf(intersectionOfSupertypes(argParam))) {
                        argNode.addError("upper bound on type parameter of argument type constructor is not a supertype of upper bound on corresponding type parameter of parameter: '" + 
                                argParam.getName() + "' of '" + 
                                argTypeDec.getName(unit) + 
                                "' does accept all type arguments accepted by '" + 
                                paramParam.getName() + "' of '" + 
                                param.getName(unit) + 
                                "'");
                    }
                    if (!unionOfCaseTypes(paramParam)
                            .isSubtypeOf(unionOfCaseTypes(argParam))) {
                        argNode.addError("enumerated bound on type parameter of argument type constructor is not a supertype of enumerated bound on corresponding type parameter of parameter: '" + 
                                argParam.getName() + "' of '" + 
                                argTypeDec.getName(unit) + 
                                "' does accept all type arguments accepted by '" + 
                                paramParam.getName() + "' of '" + 
                                param.getName(unit) + 
                                "'");
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
        checkDelegatedConstructor(that.getDelegatedConstructor(), 
                e, that);
        TypeDeclaration occ = enterConstructorDelegation(e);
        Tree.Type rt = beginReturnScope(fakeVoid(that));
        Declaration od = beginReturnDeclaration(e);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
        endConstructorDelegation(occ);
    }
    
    @Override 
    public void visit(Tree.Constructor that) {
        Constructor c = that.getConstructor();
        checkDelegatedConstructor(that.getDelegatedConstructor(), 
                c, that);
        TypeDeclaration occ = enterConstructorDelegation(c);
        Tree.Type rt = beginReturnScope(fakeVoid(that));
        Declaration od = beginReturnDeclaration(c);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
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

    protected void checkDelegatedConstructor(Tree.DelegatedConstructor dc,
            Constructor c, Node node) {
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
        Package p;
        if (that.getImportPath()==null) {
            that.setImportPath(new Tree.ImportPath(null));
            p = unit.getPackage();
        }
        else {
            p = TypeVisitor.getPackage(that.getImportPath(), backendSupport);
        }
        that.getImportPath().setModel(p);
        that.setTypeModel(unit.getPackageDeclarationType());
    }
    
    @Override
    public void visit(Tree.ModuleLiteral that) {
        super.visit(that);
        Module m;
        if (that.getImportPath()==null) {
            that.setImportPath(new Tree.ImportPath(null));
            m = unit.getPackage().getModule();
        }
        else {
            m = TypeVisitor.getModule(that.getImportPath());
        }
        that.getImportPath().setModel(m);
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
        if (that instanceof Tree.InterfaceLiteral||
            that instanceof Tree.ClassLiteral||
            that instanceof Tree.NewLiteral||
            that instanceof Tree.AliasLiteral||
            that instanceof Tree.TypeParameterLiteral) {
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
		if (type != null) {
        	t = type.getTypeModel();
        	d = t.getDeclaration();
        	errorNode = type;
        }
		else {
		    errorNode = that;
            ClassOrInterface classOrInterface = 
                    getContainingClassOrInterface(that.getScope());
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
                    Constructor defaultConstructor = 
                            ((Class) d).getDefaultConstructor();
                    if (defaultConstructor!=null) {
                        d = defaultConstructor;
                    }
                }
                if (!(d instanceof Constructor)) {
                    if (d != null) {
                        errorNode.addError("referenced declaration is not a constructor" +
                                getDeclarationReferenceSuggestion(d));
                    }
                }
                that.setTypeModel(unit.getConstructorDeclarationType());
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
                    if (((Class) d).isAbstraction()) {
                        errorNode.addError("class constructor is overloaded");
                    }
                    else {
                        that.setTypeModel(unit.getClassMetatype(t));
                    }
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
                Scope scope = 
                        that.getPackageQualified() ?
                                unit.getPackage() :
                                that.getScope();
                TypedDeclaration result = 
                        getTypedDeclaration(scope, name, 
                                null, false, unit);
                if (result!=null) {
                    checkBaseVisibility(that, result, name);
                    setMemberMetatype(that, result);
                }
                else {
                    that.addError("function or value does not exist: '" +
                            name(id) + "'", 100);
                    unit.getUnresolvedReferences().add(id);
                }
            } else {
                TypeDeclaration qtd = 
                        type.getTypeModel().getDeclaration();
            	//checkNonlocalType(that.getType(), qtd);
            	String container = "type '" + qtd.getName(unit) + "'";
            	TypedDeclaration member = 
            	        getTypedMember(qtd, name, 
            	                null, false, unit);
            	if (member==null) {
            		if (qtd.isMemberAmbiguous(name, unit, null, false)) {
            			that.addError("method or attribute is ambiguous: '" +
            					name + "' for " + container);
            		}
            		else {
            			that.addError("method or attribute does not exist: '" +
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
            that.setTypeModel(unit.getValueDeclarationType(result));
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
                            getTypeArguments(tal, 
                                    outerType, 
                                    getTypeParameters(method));
                    if (tal != null) {
                        tal.setTypeModels(typeArgs);
                    }
                    if (acceptsTypeArguments(method,
                            outerType, typeArgs, tal, that)) {
                        TypedReference pr;
                        if (outerType==null) {
                            pr = method.appliedTypedReference(null, typeArgs);
                        }
                        else {
                            pr = outerType.getTypedMember(method, typeArgs);
                        }
                        that.setTarget(pr);
                        Type metatype = constructor ?
                                unit.getConstructorMetatype(pr) :
                                unit.getFunctionMetatype(pr);
                        that.setTypeModel(metatype);
                    }
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
                that.setTypeModel(unit.getValueMetatype(reference));
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
    
    private Declaration handleAbstractionOrHeader(Declaration dec, 
            Tree.MemberOrTypeExpression that) {
        if (dec.isNative()) {
            if (!backendSupport.supportsBackend(Backend.None)) {
                BackendSupport backend = 
                        inBackend == null ?
                                backendSupport : 
                                inBackend.backendSupport;
                Declaration impl;
                Declaration hdr;
                if (dec.isNativeHeader()) {
                    hdr = dec;
                    impl = getNativeDeclaration(hdr, backend);
                } else {
                    hdr = getNativeHeader(dec.getContainer(), dec.getName());
                    if (hdr == null || backend.supportsBackend(Backend.fromAnnotation(dec.getNativeBackend()))) {
                        impl = dec;
                    } else {
                        impl = getNativeDeclaration(hdr, backend);
                    }
                }
                if (impl==null && hdr != null) {
                    if (!isImplemented(hdr)) {
                        that.addError("no native implementation for backend: native '"
                                + dec.getName(unit) +
                                "' is not implemented for one or more backends");
                    }
                }
                return inBackend == null || impl==null ? 
                        dec : impl;
            }
        }
        else {
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
            	if (overloads.size()==1) {
            		return overloads.get(0);
            	}
            }
        }
        return dec;
    }
    
    // We use this to check for similar situations as "dynamic"
    // where in this case the backend compiler can't check the
    // validity of the code for the other backend 
    private boolean isNativeForWrongBackend() {
        return inBackend != null && 
                !backendSupport.supportsBackend(inBackend);
    }    
}
