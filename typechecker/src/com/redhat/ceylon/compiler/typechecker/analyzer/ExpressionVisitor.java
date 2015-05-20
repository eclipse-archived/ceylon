package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkAssignable;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkAssignableWithWarning;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkCallable;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkIsExactly;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkIsExactlyForInterop;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkSupertype;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.declaredInPackage;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.eliminateParensAndWidening;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getPackageTypeDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getPackageTypedDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTupleType;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypeArguments;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypeDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypeMember;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypeUnknownError;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypedDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypedMember;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.hasError;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.inLanguageModule;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.isEffectivelyBaseMemberExpression;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.isGeneric;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.isIndirectInvocation;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.isInstantiationExpression;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.spreadType;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.typeDescription;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.typeNamesAsIntersection;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.unwrapExpressionUntilTerm;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.getNativeBackend;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.hasUncheckedNulls;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.name;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.argumentSatisfiesEnumeratedConstraint;
import static com.redhat.ceylon.model.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.model.typechecker.model.Util.addToUnion;
import static com.redhat.ceylon.model.typechecker.model.Util.findMatchingOverloadedClass;
import static com.redhat.ceylon.model.typechecker.model.Util.getContainingClassOrInterface;
import static com.redhat.ceylon.model.typechecker.model.Util.getInterveningRefinements;
import static com.redhat.ceylon.model.typechecker.model.Util.getNativeDeclaration;
import static com.redhat.ceylon.model.typechecker.model.Util.getOuterClassOrInterface;
import static com.redhat.ceylon.model.typechecker.model.Util.getSignature;
import static com.redhat.ceylon.model.typechecker.model.Util.getTypeArgumentMap;
import static com.redhat.ceylon.model.typechecker.model.Util.intersectionOfSupertypes;
import static com.redhat.ceylon.model.typechecker.model.Util.intersectionType;
import static com.redhat.ceylon.model.typechecker.model.Util.isAbstraction;
import static com.redhat.ceylon.model.typechecker.model.Util.isOverloadedVersion;
import static com.redhat.ceylon.model.typechecker.model.Util.isTypeUnknown;
import static com.redhat.ceylon.model.typechecker.model.Util.producedType;
import static com.redhat.ceylon.model.typechecker.model.Util.toTypeArgs;
import static com.redhat.ceylon.model.typechecker.model.Util.unionOfCaseTypes;
import static com.redhat.ceylon.model.typechecker.model.Util.unionType;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
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
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Primary;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Generic;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.IntersectionType;
import com.redhat.ceylon.model.typechecker.model.Method;
import com.redhat.ceylon.model.typechecker.model.MethodOrValue;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.NothingType;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.ProducedReference;
import com.redhat.ceylon.model.typechecker.model.ProducedType;
import com.redhat.ceylon.model.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.UnionType;
import com.redhat.ceylon.model.typechecker.model.Unit;
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
    
    private static final List<ProducedType> NO_TYPE_ARGS = 
            emptyList();
    
    private Tree.Type returnType;
    private Declaration returnDeclaration;
    private boolean isCondition;
    private boolean dynamic;
    private boolean inExtendsClause = false;
    private Backend inBackend = null;

    private Node ifStatementOrExpression;
    private Node switchStatementOrExpression;
    
    
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
    
    private TypecheckerUnit unit;
    private final BackendSupport backendSupport;
    
    public ExpressionVisitor(BackendSupport backendSupport) {
        this.backendSupport = backendSupport;
    }
    
    public ExpressionVisitor(TypecheckerUnit unit, BackendSupport backendSupport) {
        this.unit = unit;
        this.backendSupport = backendSupport;
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
            ProducedType nt = 
                    nothingType();
            returnType.setTypeModel(nt);
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
        Method fun = that.getDeclarationModel();
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
            ProducedType returnType = 
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
            fun.setType(anythingType());            
        }
        ProducedType fullType = 
                fun.getTypedReference()
                    .getFullType();
        //if the anonymous function has type parameters,
        //then the type of the expression is a type 
        //constructor, so create a fake type alias
        if (!fun.getTypeParameters().isEmpty()) {
            TypeAlias ta = new TypeAlias();
            ta.setName(fun.getName() + "#");
            ta.setAnonymous(true);
            ta.setTypeParameters(fun.getTypeParameters());
            Scope scope = that.getScope();
            ta.setContainer(scope);
            ta.setScope(scope);
            ta.setUnit(unit);
            ta.setExtendedType(fullType);
            fullType = ta.getType();
            fullType.setTypeConstructor(true);
        }
        that.setTypeModel(fullType);
    }
    
    @Override public void visit(Tree.IfExpression that) {
        Node ose = switchStatementOrExpression;
        Node oie = ifStatementOrExpression;
        switchStatementOrExpression = null;
        ifStatementOrExpression = that;
        super.visit(that);
        
        List<ProducedType> list = 
                new ArrayList<ProducedType>();
        Tree.IfClause ifClause = that.getIfClause();
        if (ifClause!=null && 
                ifClause.getExpression()!=null) {
            ProducedType t = 
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
            ProducedType t = 
                    elseClause.getExpression()
                        .getTypeModel();
            if (t!=null) {
                addToUnion(list, t);
            }
        }
        else {
            that.addError("missing else expression");
        }
        UnionType ut = new UnionType(unit);
        ut.setCaseTypes(list);
        that.setTypeModel(ut.getType());
        
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
            List<ProducedType> list = 
                    new ArrayList<ProducedType>();
            for (Tree.CaseClause cc: 
                    that.getSwitchCaseList()
                        .getCaseClauses()) {
                Tree.Expression e = cc.getExpression();
                if (e!=null) {
                    ProducedType t = e.getTypeModel();
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
                    ProducedType t = e.getTypeModel();
                    if (t!=null) {
                        addToUnion(list, t);
                    }
                }
            }
            UnionType ut = new UnionType(unit);
            ut.setCaseTypes(list);
            that.setTypeModel(ut.getType());
        }
        switchStatementOrExpression = ose;        
        ifStatementOrExpression = oie;
    }
    
    @Override public void visit(Tree.ExpressionComprehensionClause that) {
        super.visit(that);
        that.setTypeModel(that.getExpression().getTypeModel());
        that.setFirstTypeModel(nothingType());
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
                        ProducedType it = e.getTypeModel();
                        if (it!=null) {
                            ProducedType et = 
                                    unit.getIteratedType(it);
                            boolean nonemptyIterable = et!=null &&
                                    it.isSubtypeOf(unit.getNonemptyIterableType(et));
                            that.setPossiblyEmpty(!nonemptyIterable || 
                                    cc.getPossiblyEmpty());
                            ProducedType firstType = 
                                    unionType(unit.getFirstType(it), 
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
        ProducedType nt = 
                nullType();
        that.setFirstTypeModel(nt);
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
            Tree.SpecifierExpression se, ProducedType type) {
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
                    ProducedType declaredType = 
                            varType.getTypeModel();
                    checkAssignable(type, declaredType, var, 
                            "type of element of assigned value must be a subtype of declared type of pattern variable");
                }
            }
        }
    }

    private void inferSequencedValueType(ProducedType type, Tree.Variable var) {
        Tree.SequencedType st = (Tree.SequencedType) var.getType();
        if (st.getType() instanceof Tree.ValueModifier) {
            if (type!=null) {
                st.getType().setTypeModel(unit.getSequentialElementType(type));
                setSequencedValueType(st, type, var);
            }
        }
    }

    private void destructure(Tree.SpecifierExpression se, 
            ProducedType entryType,
            Tree.KeyValuePattern keyValuePattern) {
        Tree.Pattern key = keyValuePattern.getKey();
        Tree.Pattern value = keyValuePattern.getValue();
        if (!unit.isEntryType(entryType)) {
            se.addError("assigned expression is not an entry type: '"
                    + entryType.getProducedTypeName(unit) + 
                    "' is not an entry type");
        }
        else {
            destructure(key, se, unit.getKeyType(entryType));
            destructure(value, se, unit.getValueType(entryType));
        }
    }

    private void destructure(Tree.SpecifierExpression se, 
            ProducedType sequenceType,
            Tree.TuplePattern tuplePattern) {
        List<Tree.Pattern> patterns = tuplePattern.getPatterns();
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
                        sequenceType.getProducedTypeName(unit) + 
                        "' is not a subtype of 'Sequential'");
            }
            else if (unit.isEmptyType(sequenceType)) {
                se.addError("assigned expression is an empty sequence type, so may not be destructured: '" + 
                        sequenceType.getProducedTypeName(unit) + 
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
            ProducedType sequenceType, List<Tree.Pattern> patterns, 
            int length, Tree.Pattern lastPattern) {
        boolean variadic = isVariadicPattern(lastPattern);
        List<ProducedType> types = 
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
            ProducedType type = types.get(i);
            Tree.Pattern pattern = patterns.get(i);
            destructure(pattern, se, type);
        }
        if (variadic) {
            ProducedType tail = 
                    getTailType(sequenceType, fixedLength);
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
            ProducedType sequenceType, List<Tree.Pattern> patterns, 
            int length, Tree.Pattern lastPattern) {
        boolean variadic = isVariadicPattern(lastPattern);
        
        if (!variadic) {
            se.addError("assigned expression is not a tuple type, so pattern must end in a variadic element: '" + 
                    sequenceType.getProducedTypeName(unit) + 
                    "' is not a tuple type");
        }
        else if (/*nonempty && length>1 ||*/ length>2) {
            se.addError("assigned expression is not a tuple type, so pattern must not have more than two elements: '" + 
                    sequenceType.getProducedTypeName(unit) + 
                    "' is not a tuple type");
        }
        else if ((/*nonempty ||*/ length>1) && 
                !unit.isSequenceType(sequenceType)) {
            se.addError("assigned expression is not a nonempty sequence type, so pattern must have exactly one element: '" + 
                    sequenceType.getProducedTypeName(unit) + 
                    "' is not a subtype of 'Sequence'");
        }
        
        if (length>1) {
            ProducedType elementType = 
                    unit.getSequentialElementType(sequenceType);
            destructure(patterns.get(0), se, elementType);
            destructure(lastPattern, se, 
                    unit.getSequentialType(elementType));
        }
        else {
            destructure(lastPattern, se, sequenceType);
        }
    }

    public ProducedType getTailType(ProducedType sequenceType, 
            int fixedLength) {
        int i=0;
        ProducedType tail = sequenceType;
        while (i++<fixedLength && tail!=null) {
            if (unit.isTupleType(tail)) {
                List<ProducedType> list = 
                        tail.getTypeArgumentList();
                if (list.size()>=3) {
                    tail = list.get(2);
                }
                else {
                    tail = null;
                }
            }
            else {
                tail = null;
            }
        }
        return tail;
    }
    
    @Override public void visit(Tree.Variable that) {
        super.visit(that);
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        if (se!=null) {
            inferType(that, se);
            if (that.getType()!=null) {
                ProducedType t = 
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
        isCondition=true;
        Tree.Type t = that.getType();
        if (t!=null) {
            t.visit(this);
        }
        isCondition=false;
        Tree.Variable v = that.getVariable();
        ProducedType type = 
                t==null ? null : t.getTypeModel();
        if (v!=null) {
//            if (type!=null && !that.getNot()) {
//                v.getType().setTypeModel(type);
//                v.getDeclarationModel().setType(type);
//            }
            //v.getType().visit(this);
            Tree.SpecifierExpression se = 
                    v.getSpecifierExpression();
            ProducedType knownType;
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
                                    type.getProducedTypeName(unit) + 
                                    "' and '" + 
                                    knownType.getProducedTypeName(unit) + "' is empty" + 
                                    help);
                        }
                        else if (knownType.isSubtypeOf(type)) {
                            that.addError("tests assignability to bottom type 'Nothing': '" + 
                                    knownType.getProducedTypeName(unit) + 
                                    "' is a subtype of '" + 
                                    type.getProducedTypeName(unit) + "'");
                        }
                    } 
                    else {
                        if (knownType.isSubtypeOf(type)) {
                            that.addError("does not narrow type: '" + 
                                    knownType.getProducedTypeName(unit) + 
                                    "' is a subtype of '" + 
                                    type.getProducedTypeName(unit) + "'" + 
                                    help);
                        }
                    }
                }
            }
            defaultTypeToAnything(v);
            if (knownType==null) {
                knownType = anythingType(); //or should we use unknown?
            }
            
            ProducedType it = 
                    narrow(type, knownType, that.getNot());
            //check for disjointness
            if (it.isNothing()) {
                if (that.getNot()) {
                    /*that.addError("tests assignability to Nothing type: " +
                            knownType.getProducedTypeName(unit) + " is a subtype of " + 
                            type.getProducedTypeName(unit));*/
                }
                else {
                    that.addError("tests assignability to bottom type 'Nothing': intersection of '" +
                            knownType.getProducedTypeName(unit) + "' and '" + 
                            type.getProducedTypeName(unit) + "' is empty");
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
    
	private ProducedType narrow(ProducedType type,
            ProducedType knownType, boolean not) {
	    ProducedType it;
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
        ProducedType t = null;
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
                        ProducedType type = null;
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
                                    type.getProducedTypeName(unit) + "'");
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
    
    private void checkEmpty(ProducedType t, Tree.Term term) {
        if (!isTypeUnknown(t)) {
            if (!unit.isSequentialType(unit.getDefiniteType(t))) {
                term.addError("expression must be a possibly-empty sequential type: '" + 
                        t.getProducedTypeName(unit) + 
                        "' is not a subtype of 'Anything[]?'");
            }
            else if (!unit.isPossiblyEmptyType(t)) {
                term.addError("expression must be a possibly-empty sequential type: '" + 
                        t.getProducedTypeName(unit) + 
                        "' is not possibly-empty");
            }
        }
    }
    
    private void checkOptional(ProducedType t, Tree.Term term) {
        if (!isTypeUnknown(t) && 
                !unit.isOptionalType(t) && 
                !hasUncheckedNulls(term)) {
            term.addError("expression must be of optional type: '" +
                    t.getProducedTypeName(unit) + 
                    "' is not optional");
        }
    }

    @Override public void visit(Tree.BooleanCondition that) {
        super.visit(that);
        if (that.getExpression()!=null) {
            ProducedType t = 
                    that.getExpression().getTypeModel();
            if (!isTypeUnknown(t)) {
                checkAssignable(t, booleanType(), that, 
                        "expression must be of boolean type");
            }
        }
    }

    @Override public void visit(Tree.Resource that) {
        super.visit(that);
        ProducedType t = null;
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
                    ProducedType ot = obtainableType();
                    ProducedType dt = destroyableType();
                    if (isInstantiationExpression(e)) {
                        if (!t.isSubtypeOf(dt) && !t.isSubtypeOf(ot)) {
                            typedNode.addError("resource must be either obtainable or destroyable: '" +
                                    t.getProducedTypeName(unit) + 
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

    private ProducedType destroyableType() {
        return unit.getType(unit.getDestroyableDeclaration());
    }

    private ProducedType obtainableType() {
        return unit.getType(unit.getObtainableDeclaration());
    }
    
    private ProducedType emptyType() {
        return unit.getType(unit.getEmptyDeclaration());
    }

    private ProducedType anythingType() {
        return unit.getType(unit.getAnythingDeclaration());
    }

    @Override public void visit(Tree.ForIterator that) {
        super.visit(that);
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        if (se!=null) {
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                ProducedType et = e.getTypeModel();
                if (!isTypeUnknown(et)) {
                    if (!unit.isIterableType(et)) {
                        se.addError("expression is not iterable: '" + 
                                et.getProducedTypeName(unit) + 
                                "' is not a subtype of 'Iterable'");
                    }
                    else if (et!=null && unit.isEmptyType(et)) {
                        se.addError("iterated expression is definitely empty: '" +
                                et.getProducedTypeName(unit) + 
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
                ProducedType et = e.getTypeModel();
                if (!isTypeUnknown(et)) {
                    ProducedType it = unit.getIteratedType(et);
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
        	ProducedType t = type.getTypeModel();
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
                    ProducedType pt = primary.getTypeModel();
                    if (pt!=null) {
                        List<Tree.ParameterList> pls = 
                                that.getParameterLists();
                        for (int j=0; j<pls.size(); j++) {
                            pt = handleExpressionParameterList(that, 
                                    mte, pt, pls.get(j));
                        }
                    }
                }
            }
        }
    }

    private ProducedType handleExpressionParameterList(
            Tree.ParameterizedExpression that,
            Tree.MemberOrTypeExpression mte, ProducedType pt,
            Tree.ParameterList pl) {
        ProducedType ct = 
                pt.getSupertype(unit.getCallableDeclaration());
        String refName = 
                mte.getDeclaration().getName();
        if (ct==null) { 
            pl.addError("no matching parameter list in referenced declaration: '" + 
                    refName + "'");
        }
        else if (ct.getTypeArgumentList().size()>=2) {
            ProducedType tupleType = 
                    ct.getTypeArgumentList().get(1);
            List<ProducedType> argTypes = 
                    unit.getTupleElementTypes(tupleType);
            boolean variadic = 
                    unit.isTupleLengthUnbounded(tupleType);
            boolean atLeastOne = 
                    unit.isTupleVariantAtLeastOne(tupleType);
            List<Tree.Parameter> params = 
                    pl.getParameters();
            if (argTypes.size()!=params.size()) {
                pl.addError("wrong number of declared parameters: '" + 
                        refName  + "' has " + 
                        argTypes.size() + " parameters");
            }
            for (int i=0; 
                    i<argTypes.size() && i<params.size(); 
                    i++) {
                ProducedType at = argTypes.get(i);
                Tree.Parameter param = params.get(i);
                Parameter model = param.getParameterModel();
                ProducedType t = model.getModel()
                        .getTypedReference()
                        .getFullType();
                checkAssignable(at, t, param, 
                        "type of parameter '" + 
                                model.getName() + 
                        "' must be a supertype of parameter type in declaration of '" + 
                        refName + "'");
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
                else if (d instanceof Method) {
                    refineMethod(that);
                }
                Tree.StaticMemberOrTypeExpression smte = 
                        (Tree.StaticMemberOrTypeExpression) me;
                smte.setDeclaration(d);
            }
            else if (d instanceof MethodOrValue) {
                MethodOrValue mv = (MethodOrValue) d;
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
            
            if (hasParams && d instanceof Method && 
                    ((Method) d).isDeclaredVoid() && 
                    !isSatementExpression(rhs.getExpression())) {
                rhs.addError("function is declared void so specified expression must be a statement: '" + 
                        d.getName(unit) + 
                        "' is declared 'void'");
            }
            if (d instanceof Value && 
                    rhs instanceof Tree.LazySpecifierExpression) {
                ((Value) d).setTransient(true);
            }
            
            ProducedType t = lhs.getTypeModel();
            if (lhs==me && d instanceof Method) {
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
            if (rhs instanceof Tree.LazySpecifierExpression && d instanceof Method) {
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
    
    private ProducedType eraseDefaultedParameters(ProducedType t) {
        Interface cd = unit.getCallableDeclaration();
        ProducedType ct = t.getSupertype(cd);
        if (ct!=null) {
            List<ProducedType> typeArgs = 
                    ct.getTypeArgumentList();
            if (typeArgs.size()>=2) {
                ProducedType rt = typeArgs.get(0);
                ProducedType pts = typeArgs.get(1);
                List<ProducedType> argTypes = 
                        unit.getTupleElementTypes(pts);
                boolean variadic = 
                        unit.isTupleLengthUnbounded(pts);
                boolean atLeastOne = 
                        unit.isTupleVariantAtLeastOne(pts);
                if (variadic) {
                    ProducedType spt = 
                            argTypes.get(argTypes.size()-1);
                    argTypes.set(argTypes.size()-1, 
                            unit.getIteratedType(spt));
                }
                ProducedType tt = 
                        unit.getTupleType(argTypes, 
                                variadic, atLeastOne, -1);
                return producedType(cd, rt, tt);
            }
        }
        return t;
    }
    
    static ProducedReference getRefinedMember(MethodOrValue d, 
            ClassOrInterface classOrInterface) {
        TypeDeclaration td = 
                (TypeDeclaration) 
                    d.getContainer();
        ProducedType supertype = 
                classOrInterface.getType()
                    .getSupertype(td);
        return d.getProducedReference(supertype, 
                NO_TYPE_ARGS);
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
        Method refinedMethod = (Method) that.getRefined();
        Method method = (Method) that.getDeclaration();
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
            ProducedReference refinedProducedReference = 
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
            for (int i=0; 
                    i<refinedMethod.getParameterLists().size(); 
                    i++) {
                ParameterList refinedParameters = 
                        refinedMethod.getParameterLists()
                            .get(i);
                ParameterList parameters = 
                        method.getParameterLists()
                            .get(i);
                Tree.ParameterList parameterList = 
                        parameterLists.size()<=i ? 
                                null : parameterLists.get(i);
                List<Parameter> rps = 
                        refinedParameters.getParameters();
                for (int j=0; j<rps.size(); j++) {
                    Parameter refinedParameter = rps.get(j);
                    ProducedType refinedParameterType = 
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
                        ProducedType parameterType = 
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

    private ProducedReference accountForIntermediateRefinements(
            Tree.SpecifierStatement that, 
            MethodOrValue refinedMethodOrValue, 
            MethodOrValue methodOrValue,
            ClassOrInterface ci, 
            List<Declaration> interveningRefinements) {
        Tree.SpecifierExpression rhs = 
                that.getSpecifierExpression();
        ProducedReference refinedProducedReference = 
                getRefinedMember(refinedMethodOrValue, ci);
        List<ProducedType> refinedTypes = 
                new ArrayList<ProducedType>();
//        ProducedType type = 
//                getRequiredSpecifiedType(that, 
//                        refinedProducedReference);
        addToIntersection(refinedTypes, 
                refinedProducedReference.getType(), 
                unit);
        for (Declaration refinement: interveningRefinements) {
            if (refinement instanceof MethodOrValue && 
                    !refinement.equals(refinedMethodOrValue)) {
                MethodOrValue rmv = 
                        (MethodOrValue) refinement;
                ProducedReference refinedMember = 
                        getRefinedMember(rmv, ci);
                addToIntersection(refinedTypes, 
                        refinedMember.getType(), 
                        unit);
                ProducedType requiredType = 
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
        IntersectionType it = new IntersectionType(unit);
        it.setSatisfiedTypes(refinedTypes);
        methodOrValue.setType(it.canonicalize().getType());
        return refinedProducedReference;
    }

    private ProducedType getRequiredSpecifiedType(
            Tree.SpecifierStatement that,
            ProducedReference refinedMember) {
        ProducedType t = refinedMember.getFullType();
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
            ProducedType dta = tpd.getDefaultTypeArgument();
            if (dta!=null) {
                for (ProducedType st: tpd.getSatisfiedTypes()) {
                    checkAssignable(dta, st, ts.getType(),
                            "default type argument does not satisfy type constraint");
                }
            }
        }
    }
    
    @Override public void visit(Tree.InitializerParameter that) {
        super.visit(that);
        Parameter p = that.getParameterModel();
        MethodOrValue model = p.getModel();
        if (model!=null) {
        	ProducedType type = 
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
    
    private void checkType(ProducedType declaredType, 
            Tree.SpecifierOrInitializerExpression sie) {
        if (sie!=null) { 
            Tree.Expression e = sie.getExpression();
            if (e!=null) {
                ProducedType set = e.getTypeModel();
                if (!isTypeUnknown(set)) {
                    checkAssignable(set, declaredType, sie, 
                            "specified expression must be assignable to declared type");
                }
            }
        }
    }

    private void checkType(ProducedType declaredType, 
            Declaration dec, 
            Tree.SpecifierOrInitializerExpression sie, 
            int code) {
        if (sie!=null) {
            Tree.Expression e = sie.getExpression();
            if (e!=null) {
                ProducedType t = e.getTypeModel();
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

    private void checkFunctionType(ProducedType et, 
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
            ProducedType vt = type.getTypeModel();
            if (!isTypeUnknown(vt)) {
                ProducedType nt = not ?
                        nullType() :
                        objectType();
                String message = not ?
                        "specified type must be the null type" :
                        "specified type may not be optional";
                checkAssignable(vt, nt, type, 
                        message);
            }
            if (se!=null) {
                Tree.Expression e = se.getExpression();
                if (e!=null) {
                    ProducedType set = e.getTypeModel();
                    if (set!=null) {
                        if (!isTypeUnknown(vt) && 
                                !isTypeUnknown(set)) {
                            ProducedType net = not ?
                                    nullType() :
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
            ProducedType vt = type.getTypeModel();
            if (!isTypeUnknown(vt)) {
                ProducedType nt = not ?
                        emptyType() :
                        unit.getSequenceType(anythingType());
                String message = not ?
                        "specified type must be the empty sequence type" :
                        "specified type must be a nonempty sequence type";
                checkAssignable(vt, nt, type, message);
            }
            if (se!=null) {
                Tree.Expression e = se.getExpression();
                if (e!=null) {
                    ProducedType set = e.getTypeModel();
                    if (!isTypeUnknown(vt) && 
                            !isTypeUnknown(set)) {
                        ProducedType net = not ?
                                emptyType() :
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
                ProducedType vt = type.getTypeModel();
                ProducedType set = e.getTypeModel();
                if (!isTypeUnknown(vt) && 
                        !isTypeUnknown(set)) {
                    ProducedType it = 
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
            ProducedType kt = key.getType().getTypeModel();
            ProducedType vt = value.getType().getTypeModel();
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
                ProducedType t = type.getTypeModel();
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
        Method m = that.getDeclarationModel();
        Tree.SpecifierExpression se = 
                that.getSpecifierExpression();
        if (se!=null) {
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                ProducedType returnType = e.getTypeModel();
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
        Method m = that.getDeclarationModel();
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
        Method m = that.getDeclarationModel();
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
                ProducedType returnType = e.getTypeModel();
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
    
    @Override public void visit(Tree.AnyMethod that) {
        Backend ib = inBackend;
        inBackend = Backend.fromAnnotation(
                getNativeBackend(that.getAnnotationList(), unit));
        super.visit(that);
        inBackend = ib;
    }
    
    @Override public void visit(Tree.AnyAttribute that) {
        Backend ib = inBackend;
        inBackend = Backend.fromAnnotation(
                getNativeBackend(that.getAnnotationList(), unit));
        super.visit(that);
        inBackend = ib;
    }
    
    @Override public void visit(Tree.ClassOrInterface that) {
        Backend ib = inBackend;
        inBackend = Backend.fromAnnotation(
                getNativeBackend(that.getAnnotationList(), unit));
        super.visit(that);
        inBackend = ib;
        validateEnumeratedSupertypeArguments(that, 
                that.getDeclarationModel());
    }

    @Override public void visit(Tree.ClassDefinition that) {
        Tree.Type rt = 
                beginReturnScope(new Tree.VoidModifier(that.getToken()));
        Class c = that.getDeclarationModel();
        Declaration od = 
                beginReturnDeclaration(c);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
        validateEnumeratedSupertypes(that, c);
    }
    
    @Override public void visit(Tree.InterfaceDefinition that) {
        Tree.Type rt = beginReturnScope(null);
        Interface i = that.getDeclarationModel();
        Declaration od = beginReturnDeclaration(i);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
        validateEnumeratedSupertypes(that, i);
    }

    @Override public void visit(Tree.ObjectDefinition that) {
        Tree.Type rt = 
                beginReturnScope(new Tree.VoidModifier(that.getToken()));
        Class ac = that.getAnonymousClass();
        Declaration od = beginReturnDeclaration(ac);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
        validateEnumeratedSupertypes(that, ac);
    }

    @Override public void visit(Tree.ObjectArgument that) {
        Tree.Type rt = 
                beginReturnScope(new Tree.VoidModifier(that.getToken()));
        Class ac = that.getAnonymousClass();
        Declaration od = beginReturnDeclaration(ac);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
        validateEnumeratedSupertypes(that, ac);
    }
    
    @Override public void visit(Tree.ObjectExpression that) {
        Tree.Type rt = 
                beginReturnScope(new Tree.VoidModifier(that.getToken()));
        Class ac = that.getAnonymousClass();
        Declaration od = beginReturnDeclaration(ac);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
        validateEnumeratedSupertypes(that, ac);
        that.setTypeModel(unit.denotableType(ac.getType()));
    }
    
    @Override public void visit(Tree.ClassDeclaration that) {
        super.visit(that);
        Class alias = that.getDeclarationModel();
        Class c = alias.getExtendedTypeDeclaration();
        if (c!=null) {
            if (c.isAbstract()) {
                if (!alias.isFormal() && 
                        !alias.isAbstract()) {
                    that.addError("alias of abstract class must be annotated abstract", 310);
                }
            }
            if (c.isAbstraction()) {
                that.addError("class alias may not alias overloaded class");
            }
            else {
                //TODO: all this can be removed once the backend
                //      implements full support for the new class 
                //      alias stuff
                ProducedType at = alias.getExtendedType();
                ParameterList cpl = c.getParameterList();
                ParameterList apl = alias.getParameterList();
                if (cpl!=null && apl!=null) {
                    int cps = cpl.getParameters().size();
                    int aps = apl.getParameters().size();
                    if (cps!=aps) {
                        that.getParameterList()
                                .addUnsupportedError("wrong number of initializer parameters declared by class alias: '" + 
                                        alias.getName() + "'");
                    }
                    
                    for (int i=0; i<cps && i<aps; i++) {
                        Parameter ap = 
                                apl.getParameters().get(i);
                        Parameter cp = 
                                cpl.getParameters().get(i);
                        ProducedType pt = 
                                at.getTypedParameter(cp)
                                    .getType();
                        //TODO: properly check type of functional parameters!!
                        checkAssignableWithWarning(ap.getType(), 
                                pt, that, 
                                "alias parameter '" + 
                                ap.getName() + "' must be assignable to corresponding class parameter '" +
                                cp.getName() + "'");
                    }
                    //temporary restrictions
                    checkAliasedClass(that, cpl, apl);
                }
            }
        }
    }

    private static void checkAliasedClass(Tree.ClassDeclaration that, 
            ParameterList cpl, ParameterList apl) {
        //temporary restrictions
        if (that.getClassSpecifier()!=null) {
            Tree.InvocationExpression ie = 
                    that.getClassSpecifier()
                        .getInvocationExpression();
            if (ie!=null) {
                Tree.PositionalArgumentList pal = 
                        ie.getPositionalArgumentList();
                if (pal!=null) {
                    List<Tree.PositionalArgument> pas = 
                            pal.getPositionalArguments();
                    int cps = cpl.getParameters().size();
                    int aps = apl.getParameters().size();
                    if (cps!=pas.size()) {
                        pal.addUnsupportedError("wrong number of arguments for aliased class: '" + 
                                that.getDeclarationModel().getName() + 
                                "' has " + cps + " parameters");
                    }
                    for (int i=0; 
                            i<pas.size() && i<cps && i<aps; 
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
        }
    }

    private static void checkAliasArg(Parameter param, 
            Tree.Expression e) {
        if (e!=null && param!=null) {
            MethodOrValue p = param.getModel();
            if (p!=null) {
                Tree.Term term = e.getTerm();
                if (term instanceof Tree.BaseMemberExpression) {
                    Tree.BaseMemberExpression bme = 
                            (Tree.BaseMemberExpression) term;
                    Declaration d = 
                            bme.getDeclaration();
                    if (d!=null && !d.equals(p)) {
                        e.addUnsupportedError("argument must be a parameter reference to " +
                                p.getName());
                    }
                }
                else {
                    e.addUnsupportedError("argument must be a parameter reference to " +
                            p.getName());
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
            ProducedType et) {
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
            ProducedType et) {
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
                ProducedType nullType = 
                        nullType();
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
                ProducedType emptyType= 
                        emptyType();
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
            ProducedType t) {
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
            ProducedType expressionType = e.getTypeModel();
            if (!isTypeUnknown(expressionType)) {
                ProducedType t;
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
            ProducedType expressionType = e.getTypeModel();
            if (!isTypeUnknown(expressionType)) {
//                if (expressionType.getDeclaration() instanceof Interface && 
//                        expressionType.getDeclaration().equals(unit.getSequentialDeclaration())) {
//                    expressionType = unit.getEmptyType(unit.getSequenceType(expressionType.getTypeArgumentList().get(0)));
//                }
                ProducedType t;
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
            ProducedType expressionType = 
                    se.getExpression().getTypeModel();
            if (expressionType!=null) {
                ProducedType t = 
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
            ProducedType expressionType = e.getTypeModel();
            if (expressionType!=null) {
                ProducedType entryType = 
                        unit.getIteratedType(expressionType);
                if (entryType!=null) {
                    ProducedType kt = 
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
            ProducedType expressionType = 
                    e.getTypeModel();
            if (expressionType!=null) {
                ProducedType entryType = 
                        unit.getIteratedType(expressionType);
                if (entryType!=null) {
                    ProducedType vt = 
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
            ProducedType type = e.getTypeModel();
            if (type!=null) {
                ProducedType t = 
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
            ProducedType type = e.getTypeModel();
            if (type!=null) {
                ProducedType t = 
                        unit.denotableType(type)
                            .withoutUnderlyingType();
                local.setTypeModel(t);
                that.getDeclarationModel().setType(t);
            }
        }
    }
        
    private void setSequencedValueType(Tree.SequencedType spread, 
            ProducedType et, Tree.TypedDeclaration that) {
        ProducedType t = 
                unit.denotableType(et)
                    .withoutUnderlyingType();
        spread.setTypeModel(t);
        that.getDeclarationModel().setType(t);
    }
        
    private void setValueType(Tree.ValueModifier local, 
            ProducedType et, Tree.TypedDeclaration that) {
        ProducedType t = 
                unit.denotableType(et)
                    .withoutUnderlyingType();
        local.setTypeModel(t);
        that.getDeclarationModel().setType(t);
    }
        
    private void setFunctionType(Tree.FunctionModifier local, 
            ProducedType et, Tree.TypedDeclaration that) {
        ProducedType t = 
                unit.denotableType(et)
                    .withoutUnderlyingType();
        local.setTypeModel(t);
        that.getDeclarationModel().setType(t);
    }
        
    private void setFunctionType(Tree.FunctionModifier local, 
            ProducedType et, Tree.MethodArgument that) {
        ProducedType t = 
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
            ProducedType et = e.getTypeModel();
            if (!isTypeUnknown(et)) {
                ProducedType tt = throwableType();
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
        if (returnDeclaration instanceof Constructor) {
            that.addUnsupportedError("return in constructor is not yet supported");
        }
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
                ProducedType et = returnType.getTypeModel();
                ProducedType at = e.getTypeModel();
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

    private void inferReturnType(ProducedType et, ProducedType at) {
        if (at!=null) {
            at = unit.denotableType(at);
            if (et==null || et.isSubtypeOf(at)) {
                returnType.setTypeModel(at);
            }
            else {
                if (!at.isSubtypeOf(et)) {
                    UnionType ut = new UnionType(unit);
                    List<ProducedType> list = 
                            new ArrayList<ProducedType>(2);
                    addToUnion(list, et);
                    addToUnion(list, at);
                    ut.setCaseTypes(list);
                    returnType.setTypeModel( ut.getType() );
                }
            }
        }
    }
    
    ProducedType unwrap(ProducedType pt, 
            Tree.QualifiedMemberOrTypeExpression mte) {
        ProducedType result;
        Tree.MemberOperator op = 
                mte.getMemberOperator();
        Tree.Primary p = mte.getPrimary();
        if (op instanceof Tree.SafeMemberOp)  {
            checkOptional(pt, p);
            result = unit.getDefiniteType(pt);
        }
        else if (op instanceof Tree.SpreadOp) {
            if (unit.isIterableType(pt)) {
                result = unit.getIteratedType(pt);
            }
            else {
                p.addError("expression must be of iterable type: '" +
                        pt.getProducedTypeName(unit) + 
                        "' is not a subtype of 'Iterable'");
                result = pt;
            }
        }
        else {
            result = pt;
        }
        if (result==null) {
            result = unknownType();
        }
        return result;
    }
    
    ProducedType wrap(ProducedType pt, ProducedType receivingType, 
            Tree.QualifiedMemberOrTypeExpression mte) {
        Tree.MemberOperator op = mte.getMemberOperator();
        if (op instanceof Tree.SafeMemberOp)  {
            return unit.getOptionalType(pt);
        }
        else if (op instanceof Tree.SpreadOp) {
            return unit.isNonemptyIterableType(receivingType) ?
                    unit.getSequenceType(pt) :
                    unit.getSequentialType(pt);
        }
        else {
            return pt;
        }
    }
    
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

    private void inferParameterTypesIndirectly(
            Tree.PositionalArgumentList pal,
            ProducedType pt) {
        if (unit.isCallableType(pt)) {
            List<ProducedType> paramTypes = 
                    unit.getCallableArgumentTypes(pt);
            List<Tree.PositionalArgument> args = 
                    pal.getPositionalArguments();
            for (int i=0; 
                    i<paramTypes.size() && 
                    i<args.size(); 
                    i++) {
                ProducedType paramType = paramTypes.get(i);
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
                                    paramType, fa, null);
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

    private void inferParameterTypesDirectly(Declaration dec,
            Tree.PositionalArgumentList pal,
            Tree.MemberOrTypeExpression mte) {
        ProducedReference pr = 
                getInvokedProducedReference(dec, mte);
        Functional fun = (Functional) dec;
        List<ParameterList> pls = fun.getParameterLists();
        if (!pls.isEmpty()) {
            ParameterList pl = pls.get(0);
            List<Parameter> params = pl.getParameters();
            List<Tree.PositionalArgument> args = 
                    pal.getPositionalArguments();
            int j=0;
            for (int i=0; 
                    i<args.size() && 
                    j<params.size(); 
                    i++) {
                Parameter param = params.get(j);
                Tree.PositionalArgument arg = args.get(i);
                if (arg instanceof Tree.ListedArgument) {
                    Tree.ListedArgument la = 
                            (Tree.ListedArgument) arg;
                    la.setParameter(param);
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

    private void inferParameterTypesDirectly(Declaration dec,
            Tree.NamedArgumentList nal,
            Tree.MemberOrTypeExpression mte) {
        ProducedReference pr = 
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
                if (arg instanceof Tree.SpecifiedArgument) {
                    Tree.SpecifiedArgument sa = 
                            (Tree.SpecifiedArgument) arg;
                    Parameter param = 
                            getMatchingParameter(pl, arg, 
                                    foundParameters);
                    if (param!=null) {
                        foundParameters.add(param);
                        sa.setParameter(param);
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

    private ProducedReference getInvokedProducedReference(
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
        Functional fun = (Functional) dec;
        List<TypeParameter> tps = fun.getTypeParameters();
        ProducedReference pr;
        if (isPackageQualified(mte)) {
            Tree.QualifiedMemberOrTypeExpression qmte = 
                    (Tree.QualifiedMemberOrTypeExpression) 
                        mte;
            ProducedType pt = 
                    qmte.getPrimary().getTypeModel()
                        .resolveAliases();
            ProducedType qt = unwrap(pt, qmte);
            pr = qt.getTypedReference(dec,
                    getTypeArguments(tas, qt, tps));
        }
        else {
            ProducedType qt = 
                    mte.getScope()
                        .getDeclaringType(dec);
            pr = dec.getProducedReference(qt,
                    getTypeArguments(tas, qt, tps));
        }
        return pr;
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

    private void inferParameterTypes(ProducedReference pr, 
            Parameter param, Tree.Expression e, 
            boolean variadic) {
        MethodOrValue model = param.getModel();
        if (e!=null && model!=null) {
            Tree.Term term = 
                    unwrapExpressionUntilTerm(e.getTerm());
            ProducedTypedReference tpr = 
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
                    ProducedType paramType = 
                            tpr.getFullType();
                    if (variadic) {
                        paramType = 
                                unit.getIteratedType(paramType);
                    }
                    if (unit.isCallableType(paramType)) {
                        inferParameterTypesFromCallableType(
                                paramType, anon, param);
                    }
                }
            }
            else if (term instanceof Tree.StaticMemberOrTypeExpression) {
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
                }
                else {
                    stme.setTargetParameter(tpr);
                }
            }
        }
    }
    
    private List<ProducedType> inferFunctionRefTypeArgs(
            Tree.StaticMemberOrTypeExpression smte) {
        Tree.TypeArguments typeArguments = 
                smte.getTypeArguments();
        if (typeArguments instanceof Tree.InferredTypeArguments) {
            Declaration dec = smte.getDeclaration();
            List<TypeParameter> typeParameters = 
                    getTypeParametersAccountingForTypeConstructor(dec);
            if (typeParameters!=null &&
                    !typeParameters.isEmpty()) {
                ProducedTypedReference param = 
                        smte.getTargetParameter();
                ProducedType paramType = 
                        smte.getParameterType();
                Declaration parameterizedDec = 
                        param==null ? null :
                            (Declaration)
                                param.getDeclaration()
                                    .getContainer();
                if (paramType==null && param!=null) {
                    paramType = param.getFullType();
                }
                if (paramType==null ||
                        paramType.isTypeConstructor()) {
                    return null;
                }
                ProducedReference arg = 
                        getProducedReference(smte);
                if (!smte.getStaticMethodReferencePrimary()) {
                    if (dec instanceof Functional && 
                            param!=null) {
                        Functional fun = (Functional) dec;
                        List<ParameterList> apls = 
                                fun.getParameterLists();
                        Declaration pdec = param.getDeclaration();
                        if (pdec instanceof Functional) {
                            Functional pfun = (Functional) pdec;
                            List<ParameterList> ppls = 
                                    pfun.getParameterLists();
                            if (apls.isEmpty() || ppls.isEmpty()) {
                                return null; //TODO: to give a nicer error
                            }
                            else {
                                List<ProducedType> inferredTypes = 
                                        new ArrayList<ProducedType>
                                            (typeParameters.size());
                                List<Parameter> apl = 
                                        apls.get(0)
                                            .getParameters();
                                List<Parameter> ppl = 
                                        ppls.get(0)
                                            .getParameters();
                                for (TypeParameter tp: typeParameters) {
                                    ProducedType it = 
                                            inferFunctionRefTypeArg(
                                                    smte, 
                                                    typeParameters,
                                                    param, parameterizedDec, arg, 
                                                    apl, ppl, tp,
                                                    isEffectivelyContravariant(tp));
                                    inferredTypes.add(it);
                                }
                                return inferredTypes;
                            }
                        }
                    }
                    else if (dec instanceof Value) {
                        List<ProducedType> inferredTypes = 
                                new ArrayList<ProducedType>
                                    (typeParameters.size());
                        Value value = (Value) dec;
                        ProducedType argType = 
                                value.getType()
                                    .getDeclaration()
                                    .getType();
                        for (TypeParameter tp: typeParameters) {
                            ProducedType it = 
                                    inferTypeArg(tp, 
                                            argType, paramType,  
                                            !isEffectivelyContravariant(tp),
                                            smte);
                            inferredTypes.add(it);
                        }
                        return inferredTypes;
                    }
                }
                if (unit.isSequentialType(paramType)) {
                    paramType = unit.getSequentialElementType(paramType);
                }
                if (unit.isCallableType(paramType)) {
                    ProducedType template;
                    if (smte.getStaticMethodReferencePrimary()) {
                        ProducedType type = arg.getType();
                        template = producedType(
                                unit.getTupleDeclaration(), 
                                type, type, emptyType());
                    }
                    else {
                        template = unit.getCallableTuple(
                                arg.getFullType());
                    }
                    ProducedType type = 
                            unit.getCallableTuple(paramType);
                    List<ProducedType> inferredTypes = 
                            new ArrayList<ProducedType>
                                (typeParameters.size());
                    for (TypeParameter tp: typeParameters) {
                        ProducedType it = 
                                inferFunctionRefTypeArg(
                                        smte, 
                                        typeParameters, parameterizedDec, 
                                        template, type, tp, 
                                        isEffectivelyContravariant(tp));
                        inferredTypes.add(it);
                    }
                    return inferredTypes;
                }
            }
        }
        return null;
    }

    private List<TypeParameter> 
    getTypeParametersAccountingForTypeConstructor(
            Declaration dec) {
        if (isGeneric(dec)) {
            Generic generic = (Generic) dec;
            return generic.getTypeParameters();
        }
        else if (dec instanceof Value) {
            Value value = (Value) dec;
            ProducedType type = value.getType();
            if (type.isTypeConstructor()) {
                return type.getDeclaration()
                        .getTypeParameters();
            }
        }
        return null;
    }

    private ProducedType inferFunctionRefTypeArg(
            Tree.StaticMemberOrTypeExpression smte, 
            List<TypeParameter> typeParams, Declaration pd, 
            ProducedType template, ProducedType type,
            TypeParameter tp, 
            boolean findingUpperBounds) {
        ProducedType it = 
                inferTypeArg(tp,
                        template, type,
                        findingUpperBounds, smte);
        if (isTypeUnknown(it) ||
                it.involvesTypeParameters(typeParams) ||
                involvesTypeParams(pd, it)) {
            return nothingType();
        }
        else {
            return it;
        }
    }

    private ProducedType inferFunctionRefTypeArg(
            Tree.StaticMemberOrTypeExpression smte,
            List<TypeParameter> typeParams, 
            ProducedTypedReference param, 
            Declaration pd, ProducedReference arg, 
            List<Parameter> apl, List<Parameter> ppl,
            TypeParameter tp, 
            boolean findingUpperBounds) {
        List<ProducedType> list = 
                new ArrayList<ProducedType>();
        for (int i=0; 
                i<apl.size() && 
                i<ppl.size(); 
                i++) {
            Parameter ap = apl.get(i);
            Parameter pp = ppl.get(i);
            ProducedType type = 
                    param.getTypedParameter(pp)
                        .getFullType();
            ProducedType template = 
                    arg.getTypedParameter(ap)
                        .getFullType();
            ProducedType it = 
                    inferTypeArg(tp, 
                            template, type, 
                            findingUpperBounds, 
                            smte);
            if (!(isTypeUnknown(it) ||
                    it.involvesTypeParameters(typeParams) ||
                    involvesTypeParams(pd, it))) {
                addToUnionOrIntersection(findingUpperBounds, 
                        list, it);
            }
        }
        return formUnionOrIntersection(findingUpperBounds, 
                list);
    }
    
    private ProducedType inferTypeArg(TypeParameter tp,
            ProducedType template, ProducedType type, 
            boolean findingUpperBounds,
            Node node) {
        return inferTypeArg(tp, 
                template, type, 
                true, false,
                findingUpperBounds, 
                new ArrayList<TypeParameter>(),
                node);
    }
    
    private ProducedReference getProducedReference(
            Tree.StaticMemberOrTypeExpression smte) {
        //TODO: this might not be right for static refs
        ProducedType qt;
        if (smte instanceof Tree.QualifiedMemberOrTypeExpression) {
            Tree.QualifiedMemberOrTypeExpression qte = 
                    (Tree.QualifiedMemberOrTypeExpression) 
                        smte;
            qt = qte.getPrimary().getTypeModel();
        }
        else {
            qt = null;
        }
        Declaration dec = smte.getDeclaration();
        if (smte.getStaticMethodReferencePrimary()) {
            //TODO: why this special case, exactly?
            TypeDeclaration td = (TypeDeclaration) dec;
            return td.getType();
        }
        else {
            List<ProducedType> list;
            if (isGeneric(dec)) {
                Generic generic = (Generic) dec;
                List<TypeParameter> tps = 
                        generic.getTypeParameters();
                list = new ArrayList<ProducedType>
                        (tps.size());
                for (TypeParameter tp: tps) {
                    list.add(tp.getType());
                }
            }
            else {
                list = NO_TYPE_ARGS;
            }
            return dec.getProducedReference(qt,list);
        }
    }

    private static boolean involvesTypeParams
            (Declaration dec, ProducedType type) {
        if (isGeneric(dec)) {
            Generic g = (Generic) dec;
            return type.involvesTypeParameters(g);
        }
        else {
            return false;
        }
    }

    private void inferParameterTypesFromCallableType(
            ProducedType paramType, 
            Tree.FunctionArgument anon, Parameter param) {
        List<Tree.ParameterList> apls = 
                anon.getParameterLists();
        if (!apls.isEmpty()) {
            List<ProducedType> types = 
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
            ProducedReference pr, Parameter param, 
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

    private void createInferredParameter(Tree.FunctionArgument anon,
            Declaration declaration, Tree.Parameter ap,
            Parameter parameter, ProducedType type) {
        if (isTypeUnknown(type)) {
            type = unknownType();
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
                    type.getProducedTypeName(unit) + 
                    "' involving type parameters");
            type = unknownType();
        }
        Value model = new Value();
        model.setUnit(unit);
        model.setType(type);
        model.setName(parameter.getName());
        model.setInferred(true);
        parameter.setModel(model);
        model.setInitializerParameter(parameter);
        Method m = anon.getDeclarationModel();
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
            List<ProducedType> sig = 
                    new ArrayList<ProducedType>
                        (args.size());
            for (Tree.PositionalArgument pa: args) {
                ProducedType pat = pa.getTypeModel();
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
                    ClassOrInterface etd = 
                            ci.getExtendedTypeDeclaration();
                    if (etd!=null) {
                        //TODO: might be better to pass the signature here
                        //      in order to avoid an error when a different
                        //      overloaded version has been refined
                        Declaration etm = 
                                etd.getMember(name, null, false);
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
                    for (TypeDeclaration std: 
                            ci.getSatisfiedTypeDeclarations()) {
                        //TODO: might be better to pass the signature here
                        //      in order to avoid an error when a different
                        //      overloaded version has been refined
                        Declaration stm = 
                                std.getMember(name, null, false);
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
    
    private void visitInvocationPrimary(Tree.InvocationExpression that) {
        Tree.Primary primary = that.getPrimary();
        Tree.Term term = unwrapExpressionUntilTerm(primary);
        if (term instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression mte = 
                    (Tree.StaticMemberOrTypeExpression) 
                        term;
            visitInvocationPrimary(that, mte);
            
        }
        else if (primary instanceof Tree.ExtendedTypeExpression) {
            Tree.ExtendedTypeExpression ete = 
                    (Tree.ExtendedTypeExpression) 
                        primary;
            visitExtendedTypePrimary(ete);
        }
        else {
            visitInvocationPrimary(that, term);
        }
    }

    private void visitInvocationPrimary(
            Tree.InvocationExpression that, 
            Tree.Term term) {
        ProducedType type = term.getTypeModel();
        if (type!=null && 
                type.isTypeConstructor()) {
            List<ProducedType> typeArgs = 
                    getOrInferTypeArgumentsForTypeConstructor(
                            that, null, null, type);
            checkArgumentsAgainstTypeConstructor(
                    typeArgs, null, type, term);
            ProducedType fullType =
                    accountForGenericFunctionRef(true, null, 
                            null, typeArgs, type);
            term.setTypeModel(fullType);
        }
    }

    private void visitInvocationPrimary(Tree.InvocationExpression that,
            Tree.StaticMemberOrTypeExpression mte) {
        
        if (mte instanceof Tree.QualifiedMemberOrTypeExpression) {
            Tree.QualifiedMemberOrTypeExpression qmte = 
                    (Tree.QualifiedMemberOrTypeExpression) mte;
            Primary primary = qmte.getPrimary();
            Tree.Term term = 
                    unwrapExpressionUntilTerm(primary);
            if (term instanceof Tree.StaticMemberOrTypeExpression) {
                Tree.StaticMemberOrTypeExpression pmte = 
                        (Tree.StaticMemberOrTypeExpression) 
                            term;
                visitInvocationPrimary(that, pmte);
            }
        }
        
        Tree.TypeArguments tas = mte.getTypeArguments();
        if (mte instanceof Tree.BaseTypeExpression) {
            Tree.BaseTypeExpression bte = 
                    (Tree.BaseTypeExpression) mte;
            TypeDeclaration type = 
                    resolveBaseTypeExpression(bte, true);
            if (type!=null) {
                ProducedType qt;
                Scope scope = that.getScope();
                if (type.isClassOrInterfaceMember() &&
                        !type.isStaticallyImportable() &&
//                        type instanceof Constructor && 
                        !type.isDefinedInScope(scope)) {
                    ClassOrInterface c = 
                            (ClassOrInterface) 
                                type.getContainer();
                    List<ProducedType> inferredArgs = 
                            getInferredTypeArgsForReference(
                                    that, c, type);
                    qt = c.getProducedType(null, 
                            inferredArgs);
                }
                else {
                    qt = null;
                }
                List<ProducedType> typeArgs = 
                        getOrInferTypeArguments(that, type, 
                                mte, qt);
                tas.setTypeModels(typeArgs);
                visitBaseTypeExpression(bte, type, 
                        typeArgs, tas, qt);
            }
        }
        
        else if (mte instanceof Tree.QualifiedTypeExpression) {
            Tree.QualifiedTypeExpression qte = 
                    (Tree.QualifiedTypeExpression) mte;
            TypeDeclaration type = 
                    resolveQualifiedTypeExpression(qte, true);
            if (type!=null) {
                Tree.Primary primary = qte.getPrimary();
                ProducedType qt = 
                        primary.getTypeModel()
                            .resolveAliases();
                List<ProducedType> typeArgs = 
                        getOrInferTypeArguments(that, type, 
                                mte, qt);
                tas.setTypeModels(typeArgs);
                if (primary instanceof Tree.Package) {
                    visitBaseTypeExpression(qte, type, 
                            typeArgs, tas);
                }
                else {
                    visitQualifiedTypeExpression(qte, qt, 
                            type, typeArgs, tas);
                }
            }
        }
        
        else if (mte instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression bme = 
                    (Tree.BaseMemberExpression) mte;
            TypedDeclaration base = 
                    resolveBaseMemberExpression(bme, true);
            if (base!=null) {
                List<ProducedType> typeArgs = 
                        getOrInferTypeArguments(that, base, 
                                mte, null);
                tas.setTypeModels(typeArgs);
                visitBaseMemberExpression(bme, base, 
                        typeArgs, tas);
            }
        }
        
        else if (mte instanceof Tree.QualifiedMemberExpression) {
            Tree.QualifiedMemberExpression qme = 
                    (Tree.QualifiedMemberExpression) 
                        mte;
            TypedDeclaration member = 
                    resolveQualifiedMemberExpression(qme, 
                            true);
            if (member!=null) {
                Tree.Primary primary = qme.getPrimary();
                ProducedType qt = 
                        primary.getTypeModel()
                            .resolveAliases();
                List<ProducedType> typeArgs = 
                        getOrInferTypeArguments(that, 
                                member, mte, qt);
                tas.setTypeModels(typeArgs);
                if (primary instanceof Tree.Package) {
                    visitBaseMemberExpression(qme, member, 
                            typeArgs, tas);
                }
                else {
                    visitQualifiedMemberExpression(qme, qt, 
                            member, typeArgs, tas);
                }
            }
        }
    }

    private List<ProducedType> getOrInferTypeArguments(
            Tree.InvocationExpression that, Declaration dec,
            Tree.StaticMemberOrTypeExpression term, 
            ProducedType receiverType) {
        Tree.TypeArguments tas = term.getTypeArguments();
        boolean explicit = 
                tas instanceof Tree.TypeArgumentList;
        if (isGeneric(dec)) {
            if (explicit) {
                return getTypeArguments(tas, receiverType, 
                        getTypeParameters(dec));
            }
            else {
                return getInferredTypeArguments(that, 
                        (Generic) dec, term);
            }
        }
        else if (dec instanceof Value) {
            Value value = (Value) dec;
            ProducedType type = value.getType();
            if (type!=null && 
                    type.isTypeConstructor()) {
                return getOrInferTypeArgumentsForTypeConstructor(
                        that, receiverType, tas, type);
            }
            else {
                return NO_TYPE_ARGS;
            }
        }
        else {
            return NO_TYPE_ARGS;
        }
    }

    private List<ProducedType> getOrInferTypeArgumentsForTypeConstructor(
            Tree.InvocationExpression that, 
            ProducedType receiverType,
            Tree.TypeArguments tas, 
            ProducedType type) {
        List<ProducedType> typeArguments;
        TypeDeclaration td = 
                type.getDeclaration();
        List<TypeParameter> typeParameters = 
                td.getTypeParameters();
        boolean explicit = 
                tas instanceof Tree.TypeArgumentList;
        if (explicit) {
            typeArguments = 
                    getTypeArguments(tas, 
                            receiverType, 
                            typeParameters);
        }
        else {
            typeArguments = 
                    getInferredTypeArguments(that,
                            receiverType, type, 
                            typeParameters);
        }
        return typeArguments;
    }

    List<ProducedType> getInferredTypeArguments(
            Tree.InvocationExpression that,
            ProducedType receiverType, 
            ProducedType type,
            List<TypeParameter> typeParameters) {
        Tree.PositionalArgumentList pal = 
                that.getPositionalArgumentList();
        if (pal==null) {
            return NO_TYPE_ARGS;
        }
        else {
            List<ProducedType> typeArguments = 
                    new ArrayList<ProducedType>();
            for (TypeParameter tp: typeParameters) {
                List<ProducedType> paramTypes = 
                        unit.getCallableArgumentTypes(type);
                boolean contra =
                        isEffectivelyContravariant(tp);
                List<ProducedType> inferredTypes = 
                        new ArrayList<ProducedType>();
                inferTypeArgumentFromPositionalArgs(tp, 
                        paramTypes, receiverType, 
                        pal, contra, inferredTypes);
                ProducedType it = formUnionOrIntersection(
                        contra, inferredTypes);
                typeArguments.add(it);
            }
            return typeArguments;
        }
    }
    
    private List<ProducedType> getInferredTypeArguments(
            Tree.InvocationExpression that, 
            Generic generic, 
            Tree.StaticMemberOrTypeExpression mte) {
        Primary primary = that.getPrimary();
        Tree.Term term = unwrapExpressionUntilTerm(primary);
        if (term instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression pmte = 
                    (Tree.StaticMemberOrTypeExpression) 
                        term;
            Declaration declaration = pmte.getDeclaration();
            if (mte.getStaticMethodReferencePrimary() &&
                //Note: for a real static method reference
                //      the declaration has not yet been 
                //      resolved at this point (for a 
                //      Constructor it always has been)
                !(declaration instanceof Constructor ||
                  declaration!=null && 
                      declaration.isStaticallyImportable())) {
                return getInferredTypeArgsForStaticReference(
                        that, generic, mte.getDeclaration());
            }
            else {
                return getInferredTypeArgsForReference(that, 
                        generic, declaration);
            }
        }
        return NO_TYPE_ARGS;
    }

    private List<ProducedType> getInferredTypeArgsForReference(
            Tree.InvocationExpression that, Generic generic,
            Declaration declaration) {
        if (declaration instanceof Functional) {
            Functional functional = 
                    (Functional) declaration;
            List<ParameterList> parameters = 
                    functional.getParameterLists();
            if (!parameters.isEmpty()) {
                List<ProducedType> typeArgs = 
                        new ArrayList<ProducedType>();
                for (TypeParameter tp: 
                        generic.getTypeParameters()) {
                    Tree.Primary primary = that.getPrimary();
                    ProducedType pt;
                    if (primary instanceof Tree.QualifiedMemberOrTypeExpression) {
                        Tree.QualifiedMemberOrTypeExpression qmte = 
                                (Tree.QualifiedMemberOrTypeExpression) 
                                    primary;
                        pt = qmte.getPrimary()
                                .getTypeModel();
                    }
                    else {
                        //TODO: wouldn't null be more correct?!
                        pt = unknownType();
                    }
                    ParameterList pl = parameters.get(0);
                    ProducedType it = 
                            inferTypeArgument(that, pt, tp, pl, 
                                    isEffectivelyContravariant(tp));
                    if (it==null || it.containsUnknowns()) {
                        that.addError("could not infer type argument from given arguments: type parameter '" + 
                                tp.getName() + "' could not be inferred");
                    }
                    else {
                        it = constrainInferredType(tp, it);
                    }
                    typeArgs.add(it);
                }
                return typeArgs;
            }
        }
        return NO_TYPE_ARGS;
    }

    private List<ProducedType> getInferredTypeArgsForStaticReference(
            Tree.InvocationExpression that, Generic generic,
            Declaration type) {
        if (type instanceof TypeDeclaration) {
            Tree.PositionalArgumentList pal = 
                    that.getPositionalArgumentList();
            if (pal!=null && 
                    !pal.getPositionalArguments()
                        .isEmpty()) {
                Tree.PositionalArgument arg = 
                        pal.getPositionalArguments()
                            .get(0);
                if (arg!=null) {
                    ProducedType at = arg.getTypeModel();
                    TypeDeclaration td = 
                            (TypeDeclaration) type;
                    ProducedType tt = td.getType();
                    List<ProducedType> typeArgs = 
                            new ArrayList<ProducedType>();
                    for (TypeParameter tp: 
                            generic.getTypeParameters()) {
                        ProducedType it = 
                                inferTypeArg(tp, tt, at,
                                        isEffectivelyContravariant(tp), 
                                        arg);
                        if (it==null || it.containsUnknowns()) {
                            that.addError("could not infer type argument from given arguments: type parameter '" + 
                                    tp.getName() + "' could not be inferred");
                        }
                        else {
                            it = constrainInferredType(tp, it);
                        }
                        typeArgs.add(it);
                    }
                    return typeArgs;
                }
            }
        }
        return NO_TYPE_ARGS;
    }

    private boolean isEffectivelyContravariant(TypeParameter tp) {
        if (tp.isCovariant()) {
            return false;
        }
        else if (tp.isContravariant()) {
            return true;
        }
        else {
            Declaration declaration = tp.getDeclaration();
            ProducedType fullType;
            if (declaration instanceof TypedDeclaration) {
                TypedDeclaration typed =
                        (TypedDeclaration) declaration;
                fullType = typed.getTypedReference()
                            .getFullType();
                ProducedType returnType =
                        unit.getCallableReturnType(fullType);

                if (returnType!=null) {
                    boolean occursInvariantly =
                            returnType.occursInvariantly(tp);
                    boolean occursCovariantly =
                            returnType.occursCovariantly(tp);
                    boolean occursContravariantly =
                            returnType.occursContravariantly(tp);
                    if (occursCovariantly
                			&& !occursContravariantly
            				&& !occursInvariantly) {
                        //if the parameter occurs only
                        //covariantly in the return type,
                        //then treat it as 'out'
                        return false;
                    }
                    else if (!occursCovariantly
                			&& occursContravariantly
            				&& !occursInvariantly) {
                        //if the parameter occurs only
                        //contravariantly in the return type,
                        //then treat it as 'in'
                        return true;
                    }
                }
            }
            else {
            	TypeDeclaration type =
                        (TypeDeclaration) declaration;
                fullType = type.getType().getFullType();
            }

        	// Return type wasn't definitive,
        	// optimize variance for arguments
            List<ProducedType> argumentTypes =
        			unit.getCallableArgumentTypes(fullType);
            boolean occursContravariantly = false;
            boolean occursCovariantly = false;
            boolean occursInvariantly = false;
            for (ProducedType argumentType : argumentTypes) {
            	occursContravariantly = occursContravariantly
        				|| argumentType.occursContravariantly(tp);
            	occursCovariantly = occursCovariantly
            			|| argumentType.occursCovariantly(tp);
            	occursInvariantly = occursInvariantly
            			|| argumentType.occursInvariantly(tp);
            }

            // FIXME we should be ignoring unspecified optional arguments!!!

            //if the parameter occurs only
            //contravariantly in the argument
            //list, then treat it as 'in'
    		return occursContravariantly
					&& !occursCovariantly
					&& !occursInvariantly;
        }
    }
    private ProducedType constrainInferredType(TypeParameter tp, 
            ProducedType ta) {
        //Note: according to the language spec we should only 
        //      do this for contravariant  parameters, but in
        //      fact it also helps for special cases like
        //      emptyOrSingleton(null)
        List<ProducedType> sts = tp.getSatisfiedTypes();
        List<ProducedType> list = 
                new ArrayList<ProducedType>
                    (sts.size()+1);
        addToIntersection(list, ta, unit);
        //Intersect the inferred type with any 
        //upper bound constraints on the type.
        for (ProducedType st: sts) {
            //TODO: substitute in the other inferred type args
            //      of the invocation
            //TODO: st.getProducedType(receiver, dec, typeArgs);
            if (!st.involvesTypeParameters()) {
                addToIntersection(list, st, unit);
            }
        }
        IntersectionType it = new IntersectionType(unit);
        it.setSatisfiedTypes(list);
        return it.canonicalize().getType();
    }

    private ProducedType inferTypeArgument(
            Tree.InvocationExpression that,
            ProducedType qt, TypeParameter tp, 
            ParameterList parameters,
            boolean findingUpperBounds) {
        List<ProducedType> inferredTypes = 
                new ArrayList<ProducedType>();
        Tree.PositionalArgumentList pal = 
                that.getPositionalArgumentList();
        Tree.NamedArgumentList nal = 
                that.getNamedArgumentList();
        if (pal!=null) {
            inferTypeArgumentFromPositionalArgs(tp, 
                    parameters, qt, pal, 
                    findingUpperBounds,
                    inferredTypes);
        }
        else if (nal!=null) {
            inferTypeArgumentFromNamedArgs(tp, parameters, 
                    qt, nal, findingUpperBounds, 
                    inferredTypes);
        }
        return formUnionOrIntersection(findingUpperBounds, 
                inferredTypes);
    }

    private void inferTypeArgumentFromNamedArgs(
            TypeParameter tp, ParameterList parameters, 
            ProducedType qt, Tree.NamedArgumentList args, 
            boolean findingUpperBounds,
            List<ProducedType> inferredTypes) {
        Set<Parameter> foundParameters = 
                new HashSet<Parameter>();
        for (Tree.NamedArgument arg: 
                args.getNamedArguments()) {
            inferTypeArgFromNamedArg(arg, tp, qt, 
                    parameters,
                    findingUpperBounds,
                    inferredTypes, 
                    foundParameters);
        }
        Parameter sp = 
                getUnspecifiedParameter(null, parameters, 
                        foundParameters);
        if (sp!=null) {
        	Tree.SequencedArgument sa = 
        	        args.getSequencedArgument();
        	inferTypeArgFromSequencedArg(sa, tp, sp, 
        	        findingUpperBounds,
        	        inferredTypes, sa);
        }    
    }

    private void inferTypeArgFromSequencedArg(
            Tree.SequencedArgument sa, 
            TypeParameter tp, Parameter sp, 
            boolean findingUpperBounds,
            List<ProducedType> inferredTypes, 
            Node argNode) {
    	ProducedType att;
    	if (sa==null) {
    		att = emptyType();
    	}
    	else {
    		List<Tree.PositionalArgument> args = 
    		        sa.getPositionalArguments();
    		att = getTupleType(args, unit, false);
    	}
        ProducedType spt = sp.getType();
        addToUnionOrIntersection(
                findingUpperBounds, 
                inferredTypes,
                inferTypeArg(tp, spt, att, 
                        findingUpperBounds, 
                        argNode));
    }

    private void inferTypeArgFromNamedArg(
            Tree.NamedArgument arg, 
            TypeParameter tp, ProducedType qt, 
            ParameterList parameters, 
            boolean findingUpperBounds,
            List<ProducedType> inferredTypes, 
            Set<Parameter> foundParameters) {
        ProducedType type = null;
        if (arg instanceof Tree.SpecifiedArgument) {
            Tree.SpecifiedArgument sa = 
                    (Tree.SpecifiedArgument) arg;
            Tree.SpecifierExpression se = 
                    sa.getSpecifierExpression();
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                type = e.getTypeModel();
            }
        }
        else if (arg instanceof Tree.TypedArgument) {
            //copy/pasted from checkNamedArgument()
            Tree.TypedArgument ta = 
                    (Tree.TypedArgument) arg;
            type = ta.getDeclarationModel()
            		.getTypedReference() //argument can't have type parameters
            		.getFullType();
        }
        if (type!=null) {
            Parameter parameter = 
                    getMatchingParameter(parameters, arg, 
                            foundParameters);
            if (parameter!=null) {
                foundParameters.add(parameter);
                ProducedType pt = 
                        qt.getTypedParameter(parameter)
                            .getFullType();
                addToUnionOrIntersection(
                        findingUpperBounds,
                        inferredTypes,
                        inferTypeArg(tp, pt, type,
                                findingUpperBounds, 
                                arg));
            }
        }
    }

    private void inferTypeArgumentFromPositionalArgs(
            TypeParameter tp, 
            ParameterList parameters, ProducedType qt, 
            Tree.PositionalArgumentList pal, 
            boolean findingUpperBounds,
            List<ProducedType> inferredTypes) {
        List<Parameter> params = parameters.getParameters();
        for (int i=0; i<params.size(); i++) {
            Parameter parameter = params.get(i);
            List<Tree.PositionalArgument> args = 
                    pal.getPositionalArguments();
            if (args.size()>i) {
                Tree.PositionalArgument a = args.get(i);
                ProducedType at = a.getTypeModel();
                if (a instanceof Tree.SpreadArgument) {
                    at = spreadType(at, unit, true);
                    List<Parameter> subList = 
                            params.subList(i, 
                                    params.size());
                    ProducedType ptt = 
                            unit.getParameterTypesAsTupleType(subList, qt);
                    addToUnionOrIntersection(
                            findingUpperBounds, 
                            inferredTypes, 
                            inferTypeArg(tp, ptt, at, 
                                    findingUpperBounds, 
                                    pal));
                }
                else if (a instanceof Tree.Comprehension) {
                    if (parameter.isSequenced()) {
                        Tree.Comprehension c = 
                                (Tree.Comprehension) a;
                        inferTypeArgFromComprehension(tp, 
                                parameter, c, 
                                findingUpperBounds,
                                inferredTypes);
                    }
                }
                else {
                    if (parameter.isSequenced()) {
                        inferTypeArgFromPositionalArgs(tp, 
                                parameter,
                                args.subList(i, args.size()),
                                findingUpperBounds,
                                inferredTypes);
                        break;
                    }
                    else {
                        //this is sorta rubbish: we use the
                        //type that declares the member with
                        //the parameter to substitute type
                        //args into the parameter type, which
                        //is I guess an abuse of the API
                        Scope container = 
                                parameter.getDeclaration()
                                    .getContainer();
                        ProducedType dt = qt;
                        if (container instanceof TypeDeclaration) {
                            TypeDeclaration td = 
                                    (TypeDeclaration) container;
                            ProducedType supertype = 
                                    qt.getSupertype(td);
                            dt = supertype==null ? 
                                    qt : supertype;
                        }
                        else {
                            dt = qt;
                        }
                        ProducedType pt = 
                                dt.getTypedParameter(parameter)
                                  .getFullType();
                        addToUnionOrIntersection(
                                findingUpperBounds, 
                                inferredTypes,
                                inferTypeArg(tp, pt, at,
                                        findingUpperBounds, 
                                        pal));
                    }
                }
            }
        }
    }

    private void inferTypeArgumentFromPositionalArgs(
            TypeParameter tp, 
            List<ProducedType> parameterTypes, 
            ProducedType qt, 
            Tree.PositionalArgumentList pal, 
            boolean findingUpperBounds,
            List<ProducedType> inferredTypes) {
        for (int i=0; i<parameterTypes.size(); i++) {
            ProducedType pt = parameterTypes.get(i);
            List<Tree.PositionalArgument> args = 
                    pal.getPositionalArguments();
            if (args.size()>i) {
                Tree.PositionalArgument a = args.get(i);
                ProducedType at = a.getTypeModel();
                addToUnionOrIntersection(
                        findingUpperBounds, 
                        inferredTypes,
                        inferTypeArg(tp, pt, at, 
                                findingUpperBounds, 
                                pal));
            }
            //TODO: comprehensions, spreads, sequenced args!
        }
    }

    private void inferTypeArgFromPositionalArgs(
            TypeParameter tp, 
            Parameter parameter, 
            List<Tree.PositionalArgument> args,
            boolean findingUpperBounds,
            List<ProducedType> inferredTypes) {
        for (int k=0; k<args.size(); k++) {
            Tree.PositionalArgument pa = args.get(k);
            ProducedType sat = pa.getTypeModel();
            if (sat!=null) {
                ProducedType pt = parameter.getType();
                if (pa instanceof Tree.SpreadArgument) {
                    sat = spreadType(sat, unit, true);
                    addToUnionOrIntersection(
                            findingUpperBounds, 
                            inferredTypes,
                            inferTypeArg(tp, pt, sat, 
                                    findingUpperBounds,
                                    pa));
                }
                else {
                    ProducedType spt = 
                            unit.getIteratedType(pt);
                    addToUnionOrIntersection(
                            findingUpperBounds, 
                            inferredTypes,
                            inferTypeArg(tp, spt, sat, 
                                    findingUpperBounds,
                                    pa));
                }
            }
        }
    }
    
    private void inferTypeArgFromComprehension(
            TypeParameter tp, 
            Parameter parameter, 
            Tree.Comprehension c, 
            boolean findingUpperBounds,
            List<ProducedType> inferredTypes) {
            ProducedType sat = c.getTypeModel();
            if (sat!=null) {
                ProducedType pt = parameter.getType();
                ProducedType spt = 
                        unit.getIteratedType(pt);
                addToUnionOrIntersection(
                        findingUpperBounds, 
                        inferredTypes,
                        inferTypeArg(tp, spt, sat, 
                                findingUpperBounds, 
                                c));
            }
    }
    
    private ProducedType formUnionOrIntersection(
            boolean findingUpperBounds,
            List<ProducedType> inferredTypes) {
        if (findingUpperBounds) {
            return formIntersection(inferredTypes);
        }
        else {
            return formUnion(inferredTypes);
        }
    }
    
    private ProducedType unionOrIntersection(
            boolean findingUpperBounds,
            List<ProducedType> inferredTypes) {
        if (inferredTypes.isEmpty()) {
            return null;
        }
        else {
            return formUnionOrIntersection(
                    findingUpperBounds, 
                    inferredTypes);
        }
    }
    
    private void addToUnionOrIntersection(
            boolean findingUpperBounds, 
            List<ProducedType> list, 
            ProducedType pt) {
        if (findingUpperBounds) {
            addToIntersection(list, pt, unit);
        }
        else {
            addToUnion(list, pt);
        }
    }

    private ProducedType union(List<ProducedType> types) {
        if (types.isEmpty()) {
            return null;
        }
        return formUnion(types);
    }

    private ProducedType intersection(List<ProducedType> types) {
        if (types.isEmpty()) {
            return null;
        }
        return formIntersection(types);
    }

    private ProducedType formUnion(List<ProducedType> types) {
        UnionType ut = new UnionType(unit);
        ut.setCaseTypes(types);
        return ut.getType();
    }
    
    private ProducedType formIntersection(List<ProducedType> types) {
        IntersectionType it = new IntersectionType(unit);
        it.setSatisfiedTypes(types);
        return it.canonicalize().getType();
    }
    
    private ProducedType inferTypeArg(TypeParameter tp,
            ProducedType paramType, ProducedType argType, 
            boolean covariant, boolean contravariant,
            boolean findingUpperBounds,
            List<TypeParameter> visited, 
            Node argNode) {
        if (paramType!=null && argType!=null) {
            paramType = paramType.resolveAliases();
            argType = argType.resolveAliases();
            TypeDeclaration paramTypeDec = 
                    paramType.getDeclaration();
            Map<TypeParameter, ProducedType> paramTypeArgs = 
                    paramType.getTypeArguments();
            TypeDeclaration atd = argType.getDeclaration();
            if (paramTypeDec instanceof TypeParameter &&
                    paramTypeDec.equals(tp)) {
                if (tp.isTypeConstructor()) {
                    if (argType.isTypeConstructor()) {
                        return argType;
                    }
                    else {
                        return null;
                    }
                }
                else if (findingUpperBounds && covariant ||
                        !findingUpperBounds && contravariant) {
                    //ignore occurrences of covariant type 
                    //parameters in contravariant locations 
                    //in the parameter list, and occurrences 
                    //of contravariant type parameters in 
                    //covariant locations in the list
                    return null;
                }
                else if (argType.isUnknown()) {
                    //TODO: is this error really necessary now!?
                    if (argNode.getErrors().isEmpty()) {
                        argNode.addError("argument of unknown type assigned to inferred type parameter: '" + 
                                tp.getName() + "' of '" + 
                                tp.getDeclaration()
                                    .getName(unit) + "'");
                    }
                    //TODO: would it be better to return UnknownType here?
                    return null;
                }
                else {
                    return unit.denotableType(argType);
                }
            }
            else if (paramTypeDec instanceof TypeParameter &&
                    !paramTypeDec.isParameterized()) {
                TypeParameter tp2 = 
                        (TypeParameter) paramTypeDec;
                if (!findingUpperBounds &&
                        //protect ourselves from revisiting
                        //this upper bound due to circularities
                        //in the upper bounds
                        !visited.contains(tp2)) {
                    visited.add(tp2);
                    List<ProducedType> sts = 
                            tp2.getSatisfiedTypes();
                    List<ProducedType> list = 
                            new ArrayList<ProducedType>
                                (sts.size());
                    for (ProducedType upperBound: sts) {
                        //recurse to the upper bounds
                        addToUnionOrIntersection(
                                findingUpperBounds, list,
                                inferTypeArg(tp, 
                                        upperBound, argType,
                                        covariant, contravariant,
                                        findingUpperBounds, 
                                        visited, argNode));
                    }
                    visited.remove(tp2);
                    return unionOrIntersection(
                            findingUpperBounds, list);
                }
                else {
                    return null;
                }
            }
            else if (paramTypeDec instanceof UnionType) {
                //If there is more than one type parameter in
                //the union, ignore this union when inferring 
                //types. 
                //TODO: This is all a bit adhoc. The problem 
                //      is that when a parameter type involves 
                //      a union of type parameters, it in theory 
                //      imposes a compound constraint upon the 
                //      type parameters, but our algorithm 
                //      doesn't know how to deal with compound
                //      constraints
                /*ProducedType typeParamType = null;
                boolean found = false;
                for (ProducedType ct: 
                        paramType.getDeclaration()
                            .getCaseTypes()) {
                    TypeDeclaration ctd = 
                            ct.getDeclaration();
                    if (ctd instanceof TypeParameter) {
                        typeParamType = ct;
                    }
                    if (ct.containsTypeParameters()) { //TODO: check that they are "free" type params                        
                        if (found) {
                            //the parameter type involves two type
                            //parameters which are being inferred
                            return null;
                        }
                        else {
                            found = true;
                        }
                    }
                }*/
                ProducedType pt = paramType;
                ProducedType apt = argType;
                if (atd instanceof UnionType) {
                    Map<TypeParameter, ProducedType> args = 
                            argType.getTypeArguments();
                    for (ProducedType act: 
                            atd.getCaseTypes()) {
                        //some element of the argument union 
                        //is already a subtype of the 
                        //parameter union, so throw it away 
                        //from both unions
                        if (!act.involvesDeclaration(tp) && //in a recursive generic function, T can get assigned to T
                                act.substitute(args)
                                    .isSubtypeOf(paramType)) {
                            pt = pt.shallowMinus(act);
                            apt = apt.shallowMinus(act);
                        }
                    }
                }
                TypeDeclaration ptd = pt.getDeclaration();
                if (ptd instanceof UnionType)  {
                    boolean found = false;
                	for (TypeDeclaration td: 
                	        ptd.getCaseTypeDeclarations()) {
                		if (td instanceof TypeParameter) {
                			if (found) {
                			    return null;
                			}
                			found = true;
                		}
                	}
                	//just one type parameter left in the union
                    Map<TypeParameter, ProducedType> args = 
                            pt.getTypeArguments();
                    List<ProducedType> cts = 
                            ptd.getCaseTypes();
                    List<ProducedType> list = 
                            new ArrayList<ProducedType>
                                (cts.size());
                    for (ProducedType ct: cts) {
                        addToUnionOrIntersection(
                                findingUpperBounds, list, 
                                inferTypeArg(tp, 
                                        ct.substitute(args), apt, 
                                        covariant, contravariant,
                                        findingUpperBounds,
                                        visited, argNode));
                    }
                    return unionOrIntersection(
                            findingUpperBounds, list);
                }
                else {
                    return inferTypeArg(tp, 
                            pt, apt, 
                            covariant, contravariant,
                            findingUpperBounds,
                            visited, argNode);
                }
                /*else {
                    //if the param type is of form T|A1 and the arg type is
                    //of form A2|B then constrain T by B and A1 by A2
                    ProducedType pt = paramType.minus(typeParamType);
                    addToUnionOrIntersection(tp, list, inferTypeArg(tp, 
                            paramType.minus(pt), argType.minus(pt), visited));
                    addToUnionOrIntersection(tp, list, inferTypeArg(tp, 
                            paramType.minus(typeParamType), pt, visited));
                    //return null;
                }*/
            } 
            else if (paramTypeDec instanceof IntersectionType) {
                List<ProducedType> sts = 
                        paramTypeDec.getSatisfiedTypes();
                List<ProducedType> list = 
                        new ArrayList<ProducedType>
                            (sts.size());
                for (ProducedType ct: sts) {
                    //recurse to intersected types
                    addToUnionOrIntersection(
                            findingUpperBounds, list, 
                            inferTypeArg(tp, 
                                    ct.substitute(paramTypeArgs), 
                                    argType, 
                                    covariant, contravariant,
                                    findingUpperBounds,
                                    visited, argNode));
                }
                return unionOrIntersection(
                        findingUpperBounds, list);
            }
            else if (atd instanceof UnionType) {
                List<ProducedType> cts = 
                        atd.getCaseTypes();
                List<ProducedType> list = 
                        new ArrayList<ProducedType>
                            (cts.size());
                for (ProducedType ct: cts) {
                    //recurse to union types
                    addToUnion(list, 
                            inferTypeArg(tp, 
                            paramType, 
                            ct.substitute(paramTypeArgs),
                            covariant, contravariant,
                            findingUpperBounds,
                            visited, argNode));
                }
                return union(list);
            }
            else if (atd instanceof IntersectionType) {
                List<ProducedType> sts = 
                        atd.getSatisfiedTypes();
                List<ProducedType> list = 
                        new ArrayList<ProducedType>
                            (sts.size());
                for (ProducedType st: sts) {
                    //recurse to intersected types
                    addToIntersection(list, 
                            inferTypeArg(tp,
                                    paramType, 
                                    st.substitute(paramTypeArgs), 
                                    covariant, contravariant,
                                    findingUpperBounds,
                                    visited, argNode), 
                            unit);
                }
                return intersection(list);
            }
            else {
                ProducedType supertype = 
                        argType.getSupertype(paramTypeDec);
                if (supertype!=null) {
                    List<ProducedType> list = 
                            new ArrayList<ProducedType>(2);
                    ProducedType pqt = 
                            paramType.getQualifyingType();
                    ProducedType sqt = 
                            supertype.getQualifyingType();
                    if (pqt!=null && sqt!=null) {
                        //recurse to qualifying types
                        addToUnionOrIntersection(
                                findingUpperBounds, list, 
                                inferTypeArg(tp, 
                                        pqt, sqt, 
                                        covariant, contravariant,
                                        findingUpperBounds,
                                        visited, argNode));
                    }
                    inferTypeArg(tp, 
                            paramType, supertype, 
                            covariant, contravariant,
                            findingUpperBounds,
                            list, visited, 
                            argNode);
                    return unionOrIntersection(
                            findingUpperBounds, list);
                }
                else {
                    return null;
                }
            }
        }
        else {
            return null;
        }
    }
    
    private void inferTypeArg(TypeParameter tp,
            ProducedType paramType, ProducedType supertype, 
            boolean covariant, boolean contravariant,
            boolean findingUpperBounds,
            List<ProducedType> list, 
            List<TypeParameter> visited,
            Node argNode) {
        List<TypeParameter> typeParameters = 
                paramType.getDeclaration()
                    .getTypeParameters();
        List<ProducedType> paramTypeArgs = 
                paramType.getTypeArgumentList();
        List<ProducedType> superTypeArgs = 
                supertype.getTypeArgumentList();
        for (int j=0; 
                j<paramTypeArgs.size() && 
                j<superTypeArgs.size() && 
                j<typeParameters.size(); 
                j++) {
            ProducedType paramTypeArg = 
                    paramTypeArgs.get(j);
            ProducedType argTypeArg = 
                    superTypeArgs.get(j);
            TypeParameter typeParameter = 
                    typeParameters.get(j);
            boolean co;
            boolean contra;
            if (paramType.isCovariant(typeParameter)) {
                //leave them alone
                co = covariant;
                contra = contravariant;
            }
            else if (paramType.isContravariant(typeParameter)) {
                if (covariant|contravariant) {
                    //flip them
                    co = !covariant;
                    contra = !contravariant;
                }
                else {
                    //leave them invariant
                    co = false;
                    contra = false;
                }
            }
            else { //invariant
                co = false;
                contra = false;
            }
            addToUnionOrIntersection(
                    findingUpperBounds, list, 
                    inferTypeArg(tp,
                            paramTypeArg, argTypeArg, 
                            co, contra,
                            findingUpperBounds,
                            visited, argNode));
        }
    }
    
    private void visitDirectInvocation(
            Tree.InvocationExpression that) {
        Tree.Term primary = 
                unwrapExpressionUntilTerm(that.getPrimary());
        Tree.MemberOrTypeExpression mte = 
                (Tree.MemberOrTypeExpression) primary;
        ProducedReference prf = mte.getTarget();
        Functional dec = (Functional) mte.getDeclaration();
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
            ProducedType ct = primary.getTypeModel();
            if (ct!=null) {
                List<ProducedType> tal = 
                        ct.getTypeArgumentList();
                if (!tal.isEmpty()) {
                    //pull the return type out of the Callable
                    that.setTypeModel(tal.get(0));
                }
            }
            if (nal!=null) {
                List<ParameterList> parameterLists = 
                        dec.getParameterLists();
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
                checkInvocationArguments(that, prf, dec);
            }
        }
    }

    private void visitIndirectInvocation(
            Tree.InvocationExpression that) {
        Tree.Term primary = 
                unwrapExpressionUntilTerm(that.getPrimary());
        ProducedType pt = primary.getTypeModel();
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
                that.setTypeModel(nothingType());
            }
            else if (checkCallable(pt, primary, 
                    "invoked expression must be callable")) {
                List<ProducedType> typeArgs = 
                        pt.getSupertype(unit.getCallableDeclaration())
                            .getTypeArgumentList();
                if (!typeArgs.isEmpty()) {
                    that.setTypeModel(typeArgs.get(0));
                }
                //typecheck arguments using the type args of Callable
                if (typeArgs.size()>=2) {
                    ProducedType paramTypesAsTuple = 
                            typeArgs.get(1);
                    if (paramTypesAsTuple!=null) {
                        TypeDeclaration pttd = 
                                paramTypesAsTuple.getDeclaration();
                        if (pttd instanceof ClassOrInterface &&
                                (pttd.equals(unit.getTupleDeclaration()) ||
                                pttd.equals(unit.getSequenceDeclaration()) ||
                                pttd.equals(unit.getSequentialDeclaration()) ||
                                pttd.equals(unit.getEmptyDeclaration()))) {
                            //we have a plain tuple type so we can check the
                            //arguments individually
                            checkIndirectInvocationArguments(that, paramTypesAsTuple,
                                    unit.getTupleElementTypes(paramTypesAsTuple),
                                    unit.isTupleLengthUnbounded(paramTypesAsTuple),
                                    unit.isTupleVariantAtLeastOne(paramTypesAsTuple),
                                    unit.getTupleMinimumLength(paramTypesAsTuple));
                        }
                        else {
                            //we have something exotic, a union of tuple types
                            //or whatever, so just check the whole argument tuple
                            ProducedType tt = 
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
    
    private void checkInvocationArguments(Tree.InvocationExpression that,
            ProducedReference prf, Functional dec) {
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
            ProducedType argTuple) {
        if (!unit.isSequentialType(argTuple)) {
            arg.addError("spread argument expression is not sequential: " +
                    argTuple.getProducedTypeName(unit) + " is not a sequence type");
        }
    }*/
    
    private void checkNamedArguments(ParameterList pl, 
            ProducedReference pr, 
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
                    !unit.isNonemptyIterableType(sp.getType())) {
                foundParameters.add(sp);
            }
        }
            
        for (Parameter p: pl.getParameters()) {
            if (!foundParameters.contains(p) && 
                    !p.isDefaulted() && 
                    (!p.isSequenced() || p.isAtLeastOne())) {
                nal.addError("missing named argument to parameter '" + 
                        p.getName() + "' of '" + 
                        pr.getDeclaration().getName(unit) + "'",
                        11000);
            }
        }
    }
    
    private void checkSequencedArg(Tree.SequencedArgument sa, 
            ParameterList pl, ProducedReference pr, 
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
                    !isNativeForWrongBackend() &&
                    isTypeUnknown(sp.getType())) {
                sa.addError("parameter type could not be determined: " + 
                        paramdesc(sp) +
                        getTypeUnknownError(sp.getType()));
            }
            checkSequencedArgument(sa, pr, sp);
        }
    }

    private void checkNamedArg(Tree.NamedArgument a, 
            ParameterList pl, ProducedReference pr, 
            Set<Parameter> foundParameters) {
        Parameter p = 
                getMatchingParameter(pl, a, 
                        foundParameters);
        if (p==null) {
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
            if (!foundParameters.add(p)) {
                a.addError("duplicate argument for parameter: '" +
                        p.getName() + "' of '" + 
                        pr.getDeclaration().getName(unit) + "'");
            }
            else if (!dynamic && 
                    !isNativeForWrongBackend() &&
                    isTypeUnknown(p.getType())) {
                a.addError("parameter type could not be determined: " + 
                        paramdesc(p) + 
                        getTypeUnknownError(p.getType()));
            }
            checkNamedArgument(a, pr, p);
            //hack in an identifier node just for the backend:
            //TODO: get rid of this nasty thing
            if (a.getIdentifier()==null) {
                Tree.Identifier node = 
                        new Tree.Identifier(null);
                node.setScope(a.getScope());
                node.setUnit(a.getUnit());
                node.setText(p.getName());
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

    private String argdesc(Parameter p, ProducedReference pr) {
        Declaration dec = pr.getDeclaration();
        String result = 
                "'" + p.getName() + "' of '" + 
                dec.getName(unit) + "'";
        ProducedType qt = pr.getQualifyingType();
        if (qt!=null) { 
            result += " in '" + qt.getProducedTypeName(unit) + "'";
        }
        return result;
    }
    
    private void checkNamedArgument(Tree.NamedArgument a, 
            ProducedReference pr, Parameter p) {
        a.setParameter(p);
        ProducedType argType = null;
        if (a instanceof Tree.SpecifiedArgument) {
            Tree.SpecifiedArgument sa = 
                    (Tree.SpecifiedArgument) a;
            Tree.Expression e = 
                    sa.getSpecifierExpression()
                        .getExpression();
            if (e!=null) {
                argType = e.getTypeModel();
            }
        }
        else if (a instanceof Tree.TypedArgument) {
            Tree.TypedArgument ta = 
                    (Tree.TypedArgument) a;
            argType = ta.getDeclarationModel()
            		.getTypedReference() //argument can't have type parameters
            		.getFullType();
            checkArgumentToVoidParameter(p, ta);
            if (!dynamic && 
                    !isNativeForWrongBackend() &&
                    isTypeUnknown(argType)) {
                a.addError("could not determine type of named argument: '" + 
                        p.getName() + "'");
            }
        }
        ProducedType pt = 
                pr.getTypedParameter(p)
                    .getFullType();
//      if (p.isSequenced()) pt = unit.getIteratedType(pt);
        if (!isTypeUnknown(argType) && 
                !isTypeUnknown(pt)) {
            Node node;
            if (a instanceof Tree.SpecifiedArgument) {
                Tree.SpecifiedArgument sa = 
                        (Tree.SpecifiedArgument) a;
                node = sa.getSpecifierExpression();
            }
            else {
                node = a;
            }
            checkAssignable(argType, pt, node,
                    "named argument must be assignable to parameter " + 
                            argdesc(p, pr), 
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
            ProducedReference pr, Parameter p) {
        sa.setParameter(p);
        List<Tree.PositionalArgument> args = 
                sa.getPositionalArguments();
        ProducedType paramType = 
                pr.getTypedParameter(p)
                    .getFullType();
        ProducedType att = 
                getTupleType(args, unit, false)
                    .getSupertype(unit.getIterableDeclaration());
        if (!isTypeUnknown(att) && 
                !isTypeUnknown(paramType)) {
            checkAssignable(att, paramType, sa, 
                    "iterable arguments must be assignable to iterable parameter " + 
                            argdesc(p, pr));
        }
    }
    
    private Parameter getMatchingParameter(ParameterList pl, 
            Tree.NamedArgument na, 
            Set<Parameter> foundParameters) {
        Tree.Identifier id = na.getIdentifier();
        if (id==null) {
            for (Parameter p: pl.getParameters()) {
                if (!foundParameters.contains(p)) {
                    return p;
                }
            }
        }
        else {
            String name = name(id);
            for (Parameter p: pl.getParameters()) {
                if (p.getName()!=null &&
                        p.getName().equals(name)) {
                    return p;
                }
            }
        }
        return null;
    }

    private Parameter getUnspecifiedParameter(ProducedReference pr,
            ParameterList pl, Set<Parameter> foundParameters) {
        for (Parameter p: pl.getParameters()) {
            ProducedType t = pr==null ? 
                    p.getType() : 
                    pr.getTypedParameter(p)
                        .getFullType();
            if (t!=null) {
                t = t.resolveAliases();
                if (!foundParameters.contains(p) &&
                		unit.isIterableParameterType(t)) {
                    return p;
                }
            }
        }
        return null;
    }
    
    private void checkPositionalArguments(ParameterList pl, 
            ProducedReference pr, Tree.PositionalArgumentList pal, 
            Tree.InvocationExpression that) {
        List<Tree.PositionalArgument> args = 
                pal.getPositionalArguments();
        List<Parameter> params = pl.getParameters();
        Declaration target = pr.getDeclaration();
        for (int i=0; i<params.size(); i++) {
            Parameter p = params.get(i);
            if (i>=args.size()) {
                if (!p.isDefaulted() && 
                        (!p.isSequenced() || 
                         p.isAtLeastOne())) {
                    Node errorNode = 
                            that instanceof Tree.Annotation && 
                            args.isEmpty() ? that : pal;
                    errorNode.addError("missing argument to required parameter '" + 
                            p.getName() + "' of '" + 
                            target.getName(unit) + "'");
                }
            } 
            else {
                Tree.PositionalArgument a = args.get(i);
                if (!dynamic && 
                        !isNativeForWrongBackend() &&
                        isTypeUnknown(p.getType())) {
                    a.addError("parameter type could not be determined: " + 
                            paramdesc(p) +
                            getTypeUnknownError(p.getType()));
                }
                if (a instanceof Tree.SpreadArgument) {
                    checkSpreadArgument(pr, p, a, 
                            (Tree.SpreadArgument) a, 
                            params.subList(i, params.size()));
                    break;
                }
                else if (a instanceof Tree.Comprehension) {
                    if (p.isSequenced()) {
                        checkComprehensionPositionalArgument(p, pr, 
                                (Tree.Comprehension) a, 
                                p.isAtLeastOne());
                    }
                    else {
                        a.addError("not a variadic parameter: parameter '" + 
                                p.getName() + "' of '" + 
                                target.getName() + "'");
                    }
                    break;
                }
                else {
                    if (p.isSequenced()) {
                        checkSequencedPositionalArgument(p, pr, 
                                args.subList(i, 
                                        args.size()));
                        return; //Note: early return!
                    }
                    else {
                        checkPositionalArgument(p, pr, 
                                (Tree.ListedArgument) a);
                    }
                }
            }
        }
        
        for (int i=params.size(); i<args.size(); i++) {
            Tree.PositionalArgument arg = args.get(i);
            if (arg instanceof Tree.SpreadArgument) {
                if (unit.isEmptyType(arg.getTypeModel())) {
                    continue;
                }
            }
            arg.addError("no matching parameter declared by '" +
                    target.getName(unit) + "': '" + 
                    target.getName(unit) + "' has " + 
                    params.size() + " parameters", 
                    2000);
        }
        
        checkJavaAnnotationElements(args, params, target);
    
    }

    private void checkJavaAnnotationElements(
            List<Tree.PositionalArgument> args, 
            List<Parameter> params,
            Declaration target) {
        if (target instanceof Method && target.isAnnotation()) {
            Method method = (Method) target;
            ParameterList parameterList = 
                    method.getParameterLists()
                        .get(0);
            if (!parameterList.isPositionalParametersSupported()) {
                for (int i=0; 
                        i<params.size() && i<args.size(); 
                        i++) {
                    Parameter parameter = 
                            parameterList.getParameters()
                                .get(i);
                    Tree.PositionalArgument arg = args.get(i);
                    if (arg!=null && 
                            !"value".equals(parameter.getName())) {
                        arg.addUsageWarning(Warning.javaAnnotationElement, 
                                "positional argument to Java annotation element: '" + 
                                        parameter.getName() + 
                                        "' (use named arguments)");
                    }
                }
            }
        }
    }

    private void checkSpreadArgument(ProducedReference pr, 
            Parameter p, Tree.PositionalArgument a, 
            Tree.SpreadArgument arg, List<Parameter> psl) {
        a.setParameter(p);
        ProducedType rat = arg.getTypeModel();
        if (!isTypeUnknown(rat)) {
            if (!unit.isIterableType(rat)) {
                //note: check already done by visit(SpreadArgument)
                /*arg.addError("spread argument is not iterable: " + 
                        rat.getProducedTypeName(unit) + 
                        " is not a subtype of Iterable");*/
            }
            else {
                ProducedType at = 
                        spreadType(rat, unit, true);
                //checkSpreadArgumentSequential(arg, at);
                ProducedType ptt = 
                        unit.getParameterTypesAsTupleType(psl, pr);
                if (!isTypeUnknown(at) && 
                        !isTypeUnknown(ptt)) {
                    checkAssignable(at, ptt, arg, 
                            "spread argument not assignable to parameter types");
                }
            }
        }
    }
    
    private void checkIndirectInvocationArguments(
            Tree.InvocationExpression that, 
            ProducedType paramTypesAsTuple, 
            List<ProducedType> paramTypes, 
            boolean sequenced, boolean atLeastOne, 
            int firstDefaulted) {
        
        Tree.PositionalArgumentList pal = 
                that.getPositionalArgumentList();
        List<Tree.PositionalArgument> args = 
                pal.getPositionalArguments();
        
        for (int i=0; i<paramTypes.size(); i++) {
            if (isTypeUnknown(paramTypes.get(i))) {
                that.addError("parameter types cannot be determined from function reference");
                return;
            }
        }
        
        for (int i=0; i<paramTypes.size(); i++) {
            if (i>=args.size()) {
                if (i<firstDefaulted && 
                        (!sequenced || atLeastOne || 
                                i!=paramTypes.size()-1)) {
                    pal.addError("missing argument for required parameter " + i);
                }
            }
            else {
                Tree.PositionalArgument arg = args.get(i);
                ProducedType at = arg.getTypeModel();
                if (arg instanceof Tree.SpreadArgument) {
                    Tree.SpreadArgument sa = 
                            (Tree.SpreadArgument) arg;
                    ProducedType tt = 
                            getTailType(paramTypesAsTuple, i);
                    checkSpreadIndirectArgument(sa, tt, at);
                    break;
                }
                else if (arg instanceof Tree.Comprehension) {
                    ProducedType paramType = 
                            paramTypes.get(i);
                    if (sequenced && 
                            i==paramTypes.size()-1) {
                        Tree.Comprehension c = 
                                (Tree.Comprehension) arg;
                        checkComprehensionIndirectArgument(c, 
                                paramType, atLeastOne);
                    }
                    else {
                        arg.addError("not a variadic parameter: parameter " + i);
                    }
                    break;
                }
                else {
                    ProducedType paramType = paramTypes.get(i);
                    if (sequenced && i==paramTypes.size()-1) {
                        List<Tree.PositionalArgument> sublist = 
                                args.subList(i, args.size());
                        checkSequencedIndirectArgument(sublist, 
                                paramType);
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
        
        for (int i=paramTypes.size(); i<args.size(); i++) {
            Tree.PositionalArgument arg = args.get(i);
            if (arg instanceof Tree.SpreadArgument) {
                if (unit.isEmptyType(arg.getTypeModel())) {
                    continue;
                }
            }
            arg.addError("no matching parameter: function reference has " + 
                    paramTypes.size() + " parameters", 
                    2000);
        }
        
    }

    private void checkSpreadIndirectArgument(Tree.SpreadArgument sa,
            ProducedType tailType, ProducedType at) {
        //checkSpreadArgumentSequential(sa, at);
        if (!isTypeUnknown(at)) {
            if (unit.isIterableType(at)) {
                ProducedType sat = 
                        spreadType(at, unit, true);
                if (!isTypeUnknown(sat) && 
                        !isTypeUnknown(tailType)) {
                    checkAssignable(sat, tailType, sa, 
                            "spread argument not assignable to parameter types");
                }
            }
            else {
              //note: check already done by visit(SpreadArgument)
              /*sa.addError("spread argument is not iterable: " + 
                      at.getProducedTypeName(unit) + 
                      " is not a subtype of Iterable");*/
            }
        }
    }

    private void checkSequencedIndirectArgument(
            List<Tree.PositionalArgument> args,
            ProducedType paramType) {
        ProducedType set = paramType==null ? 
                null : unit.getIteratedType(paramType);
        for (int j=0; j<args.size(); j++) {
            Tree.PositionalArgument a = args.get(j);
            ProducedType at = a.getTypeModel();
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
    
    private void checkComprehensionIndirectArgument(Tree.Comprehension c, 
            ProducedType paramType, boolean atLeastOne) {
        Tree.InitialComprehensionClause icc = 
                c.getInitialComprehensionClause();
        if (icc.getPossiblyEmpty() && atLeastOne) {
            c.addError("variadic parameter is required but comprehension is possibly empty");
        }
        ProducedType at = c.getTypeModel();
        ProducedType set = paramType==null ? null : 
                unit.getIteratedType(paramType);
        if (!isTypeUnknown(at) && 
                !isTypeUnknown(set)) {
            checkAssignable(at, set, c, 
                    "argument must be assignable to variadic parameter");
        }
    }
    
    private void checkSequencedPositionalArgument(Parameter p, 
            ProducedReference pr, 
            List<Tree.PositionalArgument> args) {
        ProducedType paramType = 
                pr.getTypedParameter(p)
                    .getFullType();
        ProducedType set = paramType==null ? null : 
                unit.getIteratedType(paramType);
        for (int j=0; j<args.size(); j++) {
            Tree.PositionalArgument a = args.get(j);
            a.setParameter(p);
            ProducedType at = a.getTypeModel();
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
    
    private void checkComprehensionPositionalArgument(Parameter p, 
            ProducedReference pr, Tree.Comprehension c, 
            boolean atLeastOne) {
        Tree.InitialComprehensionClause icc = 
                c.getInitialComprehensionClause();
        if (icc.getPossiblyEmpty() && atLeastOne) {
            c.addError("variadic parameter is required but comprehension is possibly empty");
        }
        ProducedType paramType = 
                pr.getTypedParameter(p)
                    .getFullType();
        c.setParameter(p);
        ProducedType at = c.getTypeModel();
        if (!isTypeUnknown(at) && 
                !isTypeUnknown(paramType)) {
            ProducedType set = paramType==null ? 
                    null : unit.getIteratedType(paramType);
            checkAssignable(at, set, c, 
                    "argument must be assignable to variadic parameter " + 
                            argdesc(p, pr), 
                    2101);
        }
    }
    
    private boolean hasSpreadArgument(List<Tree.PositionalArgument> args) {
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
            ProducedReference pr, Tree.ListedArgument a) {
        if (p.getModel()!=null) {
            ProducedType paramType = 
                    pr.getTypedParameter(p)
                    .getFullType();
            a.setParameter(p);
            ProducedType at = a.getTypeModel();
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
        Tree.Type t = that.getType();
        if (t!=null) {
            ProducedType at = 
                    anythingType();
            checkAssignable(that.getTypeModel(), 
                    unit.getSequentialType(at), 
                    t, "spread type must be a sequence type");
        }
    }

    @Override public void visit(Tree.SpreadArgument that) {
        super.visit(that);
        Tree.Expression e = that.getExpression();
        if (e!=null) {
            ProducedType t = e.getTypeModel();
            if (t!=null) {
                if (!isTypeUnknown(t)) {
                    if (!unit.isIterableType(t)) {
                        e.addError("spread argument is not iterable: '" + 
                                t.getProducedTypeName(unit) + 
                                "' is not a subtype of 'Iterable'");
                    }
                }
                that.setTypeModel(t);
            }
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
            return er.getLowerBound()!=null && 
                        isTypeUnknown(er.getLowerBound()
                                .getTypeModel()) ||
                    er.getUpperBound()!=null && 
                        isTypeUnknown(er.getUpperBound()
                                .getTypeModel());
        }
    }
    
    @Override public void visit(Tree.IndexExpression that) {
        super.visit(that);
        ProducedType pt = type(that);
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
                    ProducedType cst = 
                            pt.getSupertype(unit.getCorrespondenceDeclaration());
                    if (cst==null) {
                        that.getPrimary()
                            .addError("illegal receiving type for index expression: '" +
                                    pt.getDeclaration().getName(unit) + 
                                    "' is not a subtype of 'Correspondence'");
                    }
                    else {
                        List<ProducedType> args = 
                                cst.getTypeArgumentList();
                        ProducedType kt = args.get(0);
                        ProducedType vt = args.get(1);
                        Tree.Element e = (Tree.Element) eor;
                        Tree.Expression ee = e.getExpression();
                        checkAssignable(ee.getTypeModel(), kt, ee, 
                                "index must be assignable to key type");
                        ProducedType rt = unit.getOptionalType(vt);
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
                    ProducedType rst = 
                            pt.getSupertype(unit.getRangedDeclaration());
                    if (rst==null) {
                        that.getPrimary()
                            .addError("illegal receiving type for index range expression: '" +
                                    pt.getDeclaration().getName(unit) + 
                                    "' is not a subtype of 'Ranged'");
                    }
                    else {
                        List<ProducedType> args = 
                                rst.getTypeArgumentList();
                        ProducedType kt = args.get(0);
                        ProducedType rt = args.get(2);
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
                                    integerType(),
                                    l, "length must be an integer");
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

    private void refineTypeForTupleElement(Tree.IndexExpression that,
            ProducedType pt, Tree.Term t) {
        boolean negated = false;
        if (t instanceof Tree.NegativeOp) {
            t = ((Tree.NegativeOp) t).getTerm();
            negated = true;
        }
        else if (t instanceof Tree.PositiveOp) {
            t = ((Tree.PositiveOp) t).getTerm();
        }
        ProducedType tt = pt;
        if (unit.isSequentialType(tt)) {
            if (t instanceof Tree.NaturalLiteral) {
                int index = Integer.parseInt(t.getText());
                if (negated) index = -index;
                final List<ProducedType> elementTypes = 
                        unit.getTupleElementTypes(tt);
                boolean variadic = 
                        unit.isTupleLengthUnbounded(tt);
                int minimumLength = 
                        unit.getTupleMinimumLength(tt);
                boolean atLeastOne = 
                        unit.isTupleVariantAtLeastOne(tt);
                if (elementTypes!=null) {
                    int size = elementTypes.size();
                    ProducedType nt = 
                            nullType();
                    if (size==0) {
                        that.setTypeModel(nt);
                    }
                    else if (index<0) {
                        that.setTypeModel(nt);
                    }
                    else if (index < size-(variadic?1:0)) {
                        ProducedType iet = 
                                elementTypes.get(index);
                        if (iet==null) return;
                        if (index >= minimumLength) {
                            iet = unionType(iet, 
                                    nt, 
                                    unit);
                        }
                        that.setTypeModel(iet);
                    }
                    else if (variadic) {
                        ProducedType iet = 
                                elementTypes.get(size-1);
                        if (iet==null) return;
                        ProducedType it = 
                                unit.getIteratedType(iet);
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
    
    private void refineTypeForTupleOpenRange(Tree.IndexExpression that,
            ProducedType pt, Tree.Term l) {
        boolean lnegated = false;
        if (l instanceof Tree.NegativeOp) {
            l = ((Tree.NegativeOp) l).getTerm();
            lnegated = true;
        }
        else if (l instanceof Tree.PositiveOp) {
            l = ((Tree.PositiveOp) l).getTerm();
        }
        ProducedType tt = pt;
        if (unit.isSequentialType(tt)) {
            if (l instanceof Tree.NaturalLiteral) {
                int lindex = Integer.parseInt(l.getText());
                if (lnegated) lindex = -lindex;
                final List<ProducedType> elementTypes = 
                        unit.getTupleElementTypes(tt);
                boolean variadic = 
                        unit.isTupleLengthUnbounded(tt);
                boolean atLeastOne = 
                        unit.isTupleVariantAtLeastOne(tt);
                int minimumLength = 
                        unit.getTupleMinimumLength(tt);
                List<ProducedType> list = 
                        new ArrayList<ProducedType>();
                if (elementTypes!=null) {
                    int size = elementTypes.size();
                    if (lindex<0) {
                        lindex=0;
                    }
                    for (int index=lindex; 
                            index < size-(variadic?1:0); 
                            index++) {
                        ProducedType et = 
                                elementTypes.get(index);
                        if (et==null) return;
                        list.add(et);
                    }
                    if (variadic) {
                        ProducedType it = 
                                elementTypes.get(size-1);
                        if (it==null) return;
                        ProducedType rt = 
                                unit.getIteratedType(it);
                        if (rt==null) return;
                        list.add(rt);
                    }
                    ProducedType rt = 
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

    private ProducedType type(Tree.PostfixExpression that) {
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
    
    @Override public void visit(Tree.PostfixOperatorExpression that) {
        assign(that.getTerm());
        super.visit(that);
        ProducedType type = type(that);
        visitIncrementDecrement(that, type, 
                that.getTerm());
        checkAssignability(that.getTerm(), that);
    }

    @Override public void visit(Tree.PrefixOperatorExpression that) {
        assign(that.getTerm());
        super.visit(that);
        ProducedType type = type(that);
        if (that.getTerm()!=null) {
            visitIncrementDecrement(that, type, 
                    that.getTerm());
            checkAssignability(that.getTerm(), that);
        }
    }
    
    private void visitIncrementDecrement(Tree.Term that,
            ProducedType pt, Tree.Term term) {
        if (!isTypeUnknown(pt)) {
            ProducedType ot = 
                    checkSupertype(pt, 
                            unit.getOrdinalDeclaration(), 
                            term, 
                            "operand expression must be of enumerable type");
            if (ot!=null) {
                ProducedType ta = 
                        ot.getTypeArgumentList()
                            .get(0);
                checkAssignable(ta, pt, that, 
                        "result type must be assignable to declared type");
            }
            that.setTypeModel(pt);
        }
    }
    
    /*@Override public void visit(Tree.SumOp that) {
        super.visit( (Tree.BinaryOperatorExpression) that );
        ProducedType lhst = leftType(that);
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
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            ProducedType st = 
                    checkSupertype(rhst, 
                            unit.getScalableDeclaration(), that, 
                            "right operand must be of scalable type");
            if (st!=null) {
                ProducedType ta = 
                        st.getTypeArgumentList()
                            .get(0);
                ProducedType rt = 
                        st.getTypeArgumentList()
                            .get(1);
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
    
    private ProducedType comparisonType() {
        return unit.getType(unit.getComparisonDeclaration());
    }
    
    private ProducedType booleanType() {
        return unit.getType(unit.getBooleanDeclaration());
    }

    private ProducedType integerType() {
        return unit.getType(unit.getIntegerDeclaration());
    }
    
    private ProducedType objectType() {
        return unit.getType(unit.getObjectDeclaration());
    }
    
    private ProducedType identifiableType() {
        return unit.getType(unit.getIdentifiableDeclaration());
    }
    
    private ProducedType nullType() {
        return unit.getType(unit.getNullDeclaration());
    }
    
    private void checkComparable(Tree.BinaryOperatorExpression that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            checkOperandTypes(lhst, rhst, 
                    unit.getComparableDeclaration(), that, 
                    "operand expressions must be comparable");
        }
    }
    
    private void visitComparisonOperator(Tree.BinaryOperatorExpression that) {
        checkComparable(that);
        that.setTypeModel(booleanType());            
    }

    private void visitCompareOperator(Tree.CompareOp that) {
        checkComparable(that);
        that.setTypeModel(comparisonType());            
    }

    private void visitWithinOperator(Tree.WithinOp that) {
        Tree.Term lbt = that.getLowerBound().getTerm();
        Tree.Term ubt = that.getUpperBound().getTerm();
        ProducedType lhst = lbt==null ? null : lbt.getTypeModel();
        ProducedType rhst = ubt==null ? null : ubt.getTypeModel();
        ProducedType t = that.getTerm().getTypeModel();
        if (!isTypeUnknown(t) &&
            !isTypeUnknown(lhst) && !isTypeUnknown(rhst)) {
            checkOperandTypes(t, lhst, rhst, 
                    unit.getComparableDeclaration(), 
                    that, "operand expressions must be comparable");
        }
        that.setTypeModel(booleanType());            
    }

    private void visitSpanOperator(Tree.RangeOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        ProducedType ot = checkOperandTypes(lhst, rhst, 
                unit.getEnumerableDeclaration(), that,
                "operand expressions must be of compatible enumerable type");
        if (ot!=null) {
            that.setTypeModel(unit.getSpanType(ot));
        }
    }
    
    private void visitMeasureOperator(Tree.SegmentOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        ProducedType ot = checkSupertype(lhst, 
                unit.getEnumerableDeclaration(), 
                that.getLeftTerm(), 
                "left operand must be of enumerable type");
        if (!isTypeUnknown(rhst)) {
            checkAssignable(rhst, 
                    integerType(), 
                    that.getRightTerm(), 
                    "right operand must be an integer");
        }
        if (ot!=null) {
            ProducedType ta = 
                    ot.getTypeArgumentList().get(0);
            that.setTypeModel(unit.getMeasureType(ta));
        }
    }

    private void visitEntryOperator(Tree.EntryOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        checkAssignable(lhst, objectType(), 
                that.getLeftTerm(), 
                "operand expression must not be an optional type");
//        checkAssignable(rhst, ot, that.getRightTerm(), 
//                "operand expression must not be an optional type");
        that.setTypeModel(unit.getEntryType(lhst, rhst));
    }

    private void visitIdentityOperator(Tree.BinaryOperatorExpression that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            ProducedType idt = identifiableType();
            checkAssignable(lhst, idt, 
                    that.getLeftTerm(), 
                    "operand expression must be of type 'Identifiable'");
            checkAssignable(rhst, idt, 
                    that.getRightTerm(), 
                    "operand expression must be of type 'Identifiable'");
            if (intersectionType(lhst, rhst, unit).isNothing()) {
                that.addError("values of disjoint types are never identical: '" +
                        lhst.getProducedTypeName(unit) + 
                        "' has empty intersection with '" +
                        rhst.getProducedTypeName(unit) + "'");
            }
        }
        that.setTypeModel(booleanType());
    }

    private void visitEqualityOperator(Tree.BinaryOperatorExpression that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            ProducedType obt = objectType();
            checkAssignable(lhst, obt, 
                    that.getLeftTerm(), 
                    "operand expression must be of type Object");
            checkAssignable(rhst, obt, 
                    that.getRightTerm(), 
                    "operand expression must be of type Object");
        }
        that.setTypeModel(booleanType());
    }
    
    private void visitAssignOperator(Tree.AssignOp that) {
        ProducedType rhst = rightType(that);
        ProducedType lhst = leftType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            ProducedType leftHandType = lhst;
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

    private ProducedType checkOperandTypes(
            ProducedType lhst, ProducedType rhst, 
            TypeDeclaration td, Node node, String message) {
        ProducedType lhsst = 
                checkSupertype(lhst, td, node, message);
        if (lhsst!=null) {
            ProducedType at = 
                    lhsst.getTypeArgumentList()
                        .get(0);
            checkAssignable(rhst, at, node, message);
            return at;
        }
        else {
            return null;
        }
    }
    
    private ProducedType checkOperandTypes(ProducedType t, 
            ProducedType lhst, ProducedType rhst, 
            TypeDeclaration td, Node node, String message) {
        ProducedType st = 
                checkSupertype(t, td, node, message);
        if (st!=null) {
            ProducedType at = 
                    st.getTypeArgumentList()
                        .get(0);
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
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
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
            ProducedType nt = 
                    checkSupertype(lhst, type, 
                        that.getLeftTerm(), 
                        that instanceof Tree.SumOp ?
                                "left operand must be of summable type" :
                                "left operand must be of numeric type");
            if (nt!=null) {
                List<ProducedType> tal = 
                        nt.getTypeArgumentList();
                if (tal.isEmpty()) return;
                ProducedType tt = tal.get(0);
                that.setTypeModel(tt);
                ProducedType ot;
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
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
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
            ProducedType nt = 
                    checkSupertype(lhst, type, 
                        that.getLeftTerm(),
                        that instanceof Tree.AddAssignOp ?
                                "operand expression must be of summable type" :
                                "operand expression must be of numeric type");
            that.setTypeModel(lhst);
            if (nt!=null) {
                ProducedType t = 
                        nt.getTypeArgumentList()
                            .get(0);
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
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            ProducedType ot = 
                    objectType();
            checkAssignable(lhst, 
                    unit.getSetType(ot), 
                    that.getLeftTerm(), 
                    "set operand expression must be a set");
            checkAssignable(rhst, 
                    unit.getSetType(ot), 
                    that.getRightTerm(), 
                    "set operand expression must be a set");
            ProducedType lhset = 
                    unit.getSetElementType(lhst);
            ProducedType rhset = 
                    unit.getSetElementType(rhst);
            ProducedType et;
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

    private void visitSetAssignmentOperator(Tree.BitwiseAssignmentOp that) {
        //TypeDeclaration sd = unit.getSetDeclaration();
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            ProducedType ot = 
                    objectType();
            checkAssignable(lhst, 
                    unit.getSetType(ot), 
                    that.getLeftTerm(), 
                    "set operand expression must be a set");
            checkAssignable(rhst, 
                    unit.getSetType(ot), 
                    that.getRightTerm(), 
                    "set operand expression must be a set");
            checkAssignable(unit.getSetType(nothingType()), 
                    lhst, 
                    that.getLeftTerm(),
                    "assigned expression type must be an instantiation of 'Set'");
            ProducedType lhset = 
                    unit.getSetElementType(lhst);
            ProducedType rhset = 
                    unit.getSetElementType(rhst);
            if (that instanceof Tree.UnionAssignOp) {
                checkAssignable(rhset, lhset, 
                        that.getRightTerm(), 
                        "resulting set element type must be assignable to to declared set element type");
            }            
            that.setTypeModel(unit.getSetType(lhset)); //in theory, we could make this narrower
        }
    }

    private void visitLogicalOperator(Tree.BinaryOperatorExpression that) {
        ProducedType bt = 
                booleanType();
        ProducedType lt = leftType(that);
        ProducedType rt = rightType(that);
        if (!isTypeUnknown(rt) && !isTypeUnknown(lt)) {
            checkAssignable(lt, bt, that, 
                    "logical operand expression must be a boolean value");
            checkAssignable(rt, bt, that, 
                    "logical operand expression must be a boolean value");
        }
        that.setTypeModel(bt);
    }

    private void visitDefaultOperator(Tree.DefaultOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            checkOptional(lhst, that.getLeftTerm());
            List<ProducedType> list = 
                    new ArrayList<ProducedType>(2);
            addToUnion(list, rhst);
            addToUnion(list, unit.getDefiniteType(lhst));
            UnionType ut = new UnionType(unit);
            ut.setCaseTypes(list);
            ProducedType rt = ut.getType();
            that.setTypeModel(rt);
            /*that.setTypeModel(rhst);
            ProducedType ot;
            if (isOptionalType(rhst)) {
                ot = rhst;
            }
            else {
                ot = getOptionalType(rhst);
            }
            if (!lhst.isSubtypeOf(ot)) {
                that.getLeftTerm().addError("must be of type: " + 
                        ot.getProducedTypeName(unit));
            }*/
        }
    }

    private void visitThenOperator(Tree.ThenOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if (!isTypeUnknown(lhst)) {
            checkAssignable(lhst, booleanType(), 
                    that.getLeftTerm(), 
                    "operand expression must be a boolean value");
        }
        if ( rhst!=null && !isTypeUnknown(rhst)) {
            checkAssignable(rhst, objectType(), 
                    that.getRightTerm(),
                    "operand expression may not be an optional type");
            that.setTypeModel(unit.getOptionalType(rhst));
        }
    }

    private void visitInOperator(Tree.InOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if (!isTypeUnknown(rhst) && !isTypeUnknown(lhst)) {
            ProducedType ct = 
                    checkSupertype(rhst,
                            unit.getCategoryDeclaration(),
                            that.getRightTerm(), 
                            "operand expression must be a category");
            if (ct!=null) {
                ProducedType at = 
                        ct.getTypeArguments().isEmpty() ? null : 
                            ct.getTypeArgumentList()
                                .get(0);
            	checkAssignable(lhst, at, 
            	        that.getLeftTerm(), 
            			"operand expression must be assignable to category type");
            }
        }
        that.setTypeModel(booleanType());
    }
    
    private void visitUnaryOperator(Tree.UnaryOperatorExpression that, 
            TypeDeclaration type) {
        ProducedType t = type(that);
        if (!isTypeUnknown(t)) {
            ProducedType nt = 
                    checkSupertype(t, type, 
                            that.getTerm(), 
                            "operand expression must be of correct type");
            if (nt!=null) {
                ProducedType at = 
                        nt.getTypeArguments().isEmpty() ? nt : 
                            nt.getTypeArgumentList()
                                .get(0);
                that.setTypeModel(at);
            }
        }
    }

    private void visitExistsOperator(Tree.Exists that) {
        checkOptional(type(that), that.getTerm());
        that.setTypeModel(booleanType());
    }
    
    private void visitNonemptyOperator(Tree.Nonempty that) {
        checkEmpty(type(that), that.getTerm());
        that.setTypeModel(booleanType());
    }
    
    private void visitOfOperator(Tree.OfOp that) {
        Tree.Type rt = that.getType();
        if (rt!=null) {
            ProducedType t = rt.getTypeModel();
            if (!isTypeUnknown(t)) {
                that.setTypeModel(t);
                Tree.Term tt = that.getTerm();
                if (tt!=null) {
                    ProducedType pt = tt.getTypeModel();
                    if (!isTypeUnknown(pt)) {
                        if (!t.covers(pt)) {
                            that.addError("specified type does not cover the cases of the operand expression: '" +
                                    t.getProducedTypeName(unit) + "' does not cover '" + 
                                    pt.getProducedTypeName(unit) + "'");
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
            ProducedType type = rt.getTypeModel();
            if (type!=null) {
                if (that.getTerm()!=null) {
                    ProducedType knownType = 
                            that.getTerm().getTypeModel();
                    if (knownType!=null && 
                            knownType.isSubtypeOf(type)) {
                        that.addError("expression type is a subtype of the type: '" +
                                knownType.getProducedTypeName(unit) + 
                                "' is assignable to '" +
                                type.getProducedTypeName(unit) + "'");
                    }
                    else {
                        if (intersectionType(type, knownType, unit).isNothing()) {
                            that.addError("tests assignability to bottom type 'Nothing': intersection of '" +
                                    knownType.getProducedTypeName(unit) + "' and '" + 
                                    type.getProducedTypeName(unit) + "' is empty");
                        }
                    }
                }
            }
        }
        that.setTypeModel(booleanType());
    }

    private void checkAssignability(Tree.Term that, Node node) {
        if (that instanceof Tree.QualifiedMemberOrTypeExpression ||
            that instanceof Tree.BaseMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression smte =
                    (Tree.StaticMemberOrTypeExpression) that;
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
                        (Tree.QualifiedMemberOrTypeExpression) that;
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
    
    private ProducedType rightType(Tree.BinaryOperatorExpression that) {
        Tree.Term rt = that.getRightTerm();
        return rt==null? null : rt.getTypeModel();
    }

    private ProducedType leftType(Tree.BinaryOperatorExpression that) {
        Tree.Term lt = that.getLeftTerm();
        return lt==null ? null : lt.getTypeModel();
    }
    
    private ProducedType type(Tree.UnaryOperatorExpression that) {
        Tree.Term t = that.getTerm();
        return t==null ? null : t.getTypeModel();
    }
    
    private Interface getArithmeticDeclaration(Tree.ArithmeticOp that) {
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

    private Interface getArithmeticDeclaration(Tree.ArithmeticAssignmentOp that) {
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
        Tree.Expression e = that.getLetClause().getExpression();
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

    private void checkBaseVisibility(Node that, TypedDeclaration member, 
            String name) {
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
                    type.getExtendedTypeDeclaration();
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
            Tree.QualifiedTypeExpression that, TypeDeclaration type,
            String name, String container) {
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
                    type.getExtendedTypeDeclaration();
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
        ProducedType ot = 
                that.getOuterType().getTypeModel();
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
            List<ProducedType> typeArgs;
            if (typeConstructorArgumentsInferrable(member, that)) {
                typeArgs = inferFunctionRefTypeArgs(that);
            }
            else if (explicitTypeArguments(member, tal)) {
                typeArgs = getTypeArguments(tal, null, 
                        getTypeParameters(member));
            }
            else {
                typeArgs = inferFunctionRefTypeArgs(that);
            }
            if (typeArgs!=null) {
                tal.setTypeModels(typeArgs);
                visitBaseMemberExpression(that, member, 
                        typeArgs, tal);
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
        if (isGeneric(member)) {
            Generic g = (Generic) member;
            List<TypeParameter> typeParameters = 
                    g.getTypeParameters();
            Scope scope = that.getScope();
            ProducedType outerType = 
                    scope.getDeclaringType(member);
            ProducedTypedReference pr = 
                    member.getProducedTypedReference(outerType, 
                            NO_TYPE_ARGS);
            that.setTarget(pr);
            TypeAlias ta = new TypeAlias();
            ta.setContainer(scope);
            ta.setName("Anonymous#" + member.getName());
            ta.setAnonymous(true);
            ta.setScope(scope);
            ta.setUnit(unit);
            ta.setExtendedType(pr.getFullType());
            ta.setTypeParameters(typeParameters);
            ProducedType t = ta.getType();
            t.setTypeConstructor(true);
            that.setTypeModel(t);
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
            if (member.isNative()) {
                member = (TypedDeclaration)
                        nativeDeclaration(member);
            }
            if (member == null) {
                that.addError("function or value does not have a proper native backend implementation: '" +
                        name + "'" /* , 100 */);
                unit.getUnresolvedReferences()
                    .add(id);
            }
            else {
                member = (TypedDeclaration) 
                        handleAbstraction(member, that);
                that.setDeclaration(member);
                if (error) {
                    checkBaseVisibility(that, member, name);
                }
            }
        }
        return member;
    }

    private List<TypeParameter> getTypeParameters
            (Declaration member) {
        if (member instanceof Generic) { 
            Generic d = (Generic) member;
            return d.getTypeParameters();
        }
        else {
            return emptyList();
        }
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
            ProducedType receiverType = 
                    primary.getTypeModel()
                        .resolveAliases(); //TODO: probably not necessary
            if (receiverType.isTypeConstructor()) {
                that.addError("missing type arguments in reference to member of generic type: the type '" +
                        receiverType.getProducedTypeName(unit) + 
                        "' is a type constructor (specify explicit type arguments)");
            }
            List<ProducedType> typeArgs;
            if (typeConstructorArgumentsInferrable(member, that)) {
                typeArgs = inferFunctionRefTypeArgs(that);
            }
            else if (explicitTypeArguments(member, tal)) {
                typeArgs = getTypeArguments(tal, 
                        receiverType, 
                        getTypeParameters(member));
            }
            else {
                typeArgs = inferFunctionRefTypeArgs(that);
            }
            if (typeArgs!=null) {
                tal.setTypeModels(typeArgs);
                if (primary instanceof Tree.Package) {
                    visitBaseMemberExpression(that, 
                            member, typeArgs, tal);
                }
                else {
                    visitQualifiedMemberExpression(that, 
                            receiverType, member, typeArgs, 
                            tal);
                }
                //otherwise infer type arguments later
            }
            else if (!that.getStaticMethodReference()) {
                if (primary instanceof Tree.Package) {
                    visitGenericBaseMemberReference(that, 
                            member);
                }
                else {
                    visitGenericQualifiedMemberReference(
                            that, receiverType, member);
                }
            }
            else {
                typeArgumentsImplicit(that);
            }
            handleStaticPrimaryImplicitTypeArguments(that, 
                    primary);
        }
    }

    private void visitGenericQualifiedMemberReference(
            Tree.QualifiedMemberExpression that,
            ProducedType receiverType,
            TypedDeclaration member) {
        if (isGeneric(member)) {
            Generic g = (Generic) member;
            List<TypeParameter> typeParameters = 
                    g.getTypeParameters();
            Scope scope = that.getScope();
            ProducedTypedReference pr = 
                    receiverType.getTypedMember(member, 
                            NO_TYPE_ARGS);
            that.setTarget(pr);
            TypeAlias ta = new TypeAlias();
            ta.setContainer(scope);
            ta.setName("Anonymous#" + member.getName());
            ta.setAnonymous(true);
            ta.setScope(scope);
            ta.setUnit(unit);
            ta.setExtendedType(pr.getFullType());
            ta.setTypeParameters(typeParameters);
            ProducedType t = ta.getType();
            t.setTypeConstructor(true);
            that.setTypeModel(t);
        }
    }

    private void handleStaticPrimaryImplicitTypeArguments(
            Tree.StaticMemberOrTypeExpression that,
            Tree.Primary primary) {
        if (that.getStaticMethodReference()) {
            //we do this check later than usual, in order
            //to allow qualified refs to Java static members
            //without type arguments to the qualifying type
            Declaration dec = that.getDeclaration();
            if (!(dec instanceof Constructor || 
                    dec!=null && 
                    dec.isStaticallyImportable())) {
                Tree.StaticMemberOrTypeExpression smte =
                        (Tree.StaticMemberOrTypeExpression) 
                            primary;
                Tree.TypeArguments typeArgs = 
                        smte.getTypeArguments();
                TypeDeclaration type = 
                        (TypeDeclaration) smte.getDeclaration();
                if (type!=null && 
                        !explicitTypeArguments(type, typeArgs) &&
                        typeArgs.getTypeModels()==null) {
                    typeArgumentsImplicit(smte);
                }
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
            List<ProducedType> signature = 
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
                ProducedType pt = 
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
                if (member.isNative()) {
                    member = (TypedDeclaration)
                            nativeDeclaration(member);
                }
                if (member == null) {
                    that.addError("method or attribute does not have a native implementation for this backend: '" +
                            name + "' in " + container);
                    unit.getUnresolvedReferences()
                        .add(id);
                }
                else {
                    member = (TypedDeclaration) 
                            handleAbstraction(member, that);
                    that.setDeclaration(member);
                    resetSuperReference(that);
                    boolean selfReference = 
                            isSelfReference(p);
                    if (!selfReference && 
                            !member.isShared()) {
                        member.setOtherInstanceAccess(true);
                    }
                    if (error) {
                        checkQualifiedVisibility(that, member, 
                                name, container, selfReference);
                        checkSuperMember(that);
                    }
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
        Tree.Term t = eliminateParensAndWidening(that.getPrimary());
        if (t instanceof Tree.Super) {
            checkSuperInvocation(that);
        }
    }

    private void typeArgumentsImplicit(
            Tree.StaticMemberOrTypeExpression that) {
        Declaration declaration = that.getDeclaration();
        Generic dec = (Generic) declaration;
        StringBuilder params = new StringBuilder();
        for (TypeParameter tp: dec.getTypeParameters()) {
            if (params.length()>0) params.append(", ");
            params.append("'").append(tp.getName()).append("'");
        }
        that.addError("missing type arguments to generic declaration: '" + 
                declaration.getName(unit) + 
                "' declares type parameters " + params);
    }
    
    private void visitQualifiedMemberExpression(
            Tree.QualifiedMemberExpression that,
            ProducedType receivingType, 
            TypedDeclaration member, 
            List<ProducedType> typeArgs, 
            Tree.TypeArguments tal) {
        ProducedType receiverType =
                accountForStaticReferenceReceiverType(that, 
                        unwrap(receivingType, that));
        if (acceptsTypeArguments(receiverType, member, 
                typeArgs, tal, that)) {
            ProducedTypedReference ptr = 
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
            ProducedType fullType = 
                    accountForGenericFunctionRef(direct, 
                            tal, receivingType, typeArgs, 
                            ptr.getFullType(wrap(ptr.getType(), 
                                    receivingType, that)));
            if (!dynamic && 
                    !isNativeForWrongBackend() && 
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
            that.setTypeModel(accountForStaticReferenceType(that, 
                    member, fullType));
            //}
        }
    }

    private static ProducedType accountForGenericFunctionRef(
            boolean directlyInvoked, 
            Tree.TypeArguments tas, 
            ProducedType receivingType,
            List<ProducedType> typeArgs, 
            ProducedType fullType) {
        if (fullType == null) {
            return null;
        }
        else {
            boolean explicit = 
                    tas instanceof Tree.TypeArgumentList;
            if (explicit || directlyInvoked || 
                    typeArgs!=null && !typeArgs.isEmpty()) {
                TypeDeclaration ftd = 
                        fullType.getDeclaration();
                if (fullType.isTypeConstructor()) {
                    //apply the type arguments to the generic
                    //function reference
                    //TODO: it's ugly to do this here, better to 
                    //      suck it into ProducedTypedReference
                    return ftd.getProducedType(receivingType, typeArgs)
                            .resolveAliases();
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

    private ProducedType accountForStaticReferenceReceiverType(
            Tree.QualifiedMemberOrTypeExpression that, 
            ProducedType receivingType) {
        if (that.getStaticMethodReference()) {
            Tree.MemberOrTypeExpression primary = 
                    (Tree.MemberOrTypeExpression) 
                        that.getPrimary();
            ProducedReference target = primary.getTarget();
            return target==null ? 
                    unknownType() : 
                    target.getType();
        }
        else {
            return receivingType;
        }
    }
    
    private ProducedType accountForStaticReferenceType(
            Tree.QualifiedMemberOrTypeExpression that, 
            Declaration member, ProducedType type) {
        if (that.getStaticMethodReference()) {
            Tree.MemberOrTypeExpression p = 
                    (Tree.MemberOrTypeExpression) 
                        that.getPrimary();
            if (member instanceof Constructor) {
                //Ceylon named constructor
                if (p.getStaticMethodReference()) {
                    Tree.QualifiedMemberOrTypeExpression qmte = 
                            (Tree.QualifiedMemberOrTypeExpression) p;
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
                if (p instanceof Tree.QualifiedMemberOrTypeExpression) {
                    Tree.QualifiedMemberOrTypeExpression qmte =
                            (Tree.QualifiedMemberOrTypeExpression) p;
                    Tree.Primary pp = qmte.getPrimary();
                    if (!(pp instanceof Tree.BaseTypeExpression) &&
                            !(pp instanceof Tree.QualifiedTypeExpression) &&
                            !(pp instanceof Tree.Package)) {
                        pp.addError("non-static type expression qualifies static member reference");   
                    }
                }
                if (member.isStaticallyImportable()) {
                    //static member of Java type
                    if (p.getStaticMethodReference()) {
                        Tree.QualifiedMemberOrTypeExpression qmte = 
                                (Tree.QualifiedMemberOrTypeExpression) p;
                        if (qmte.getDeclaration().isStaticallyImportable()) {
                            return type;
                        }
                        else {
                            Tree.MemberOrTypeExpression pp = 
                                    (Tree.MemberOrTypeExpression) 
                                        qmte.getPrimary();
                            return accountForStaticReferenceType(qmte, 
                                    pp.getDeclaration(), type);
                        }
                    }
                    else {
                        return type;
                    }
                }
                else {
                    //ordinary non-static, non-constructor member
                    ProducedReference target = p.getTarget();
                    if (target==null) {
                        return unknownType();
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
    
    private ProducedType getStaticReferenceType(ProducedType type, ProducedType rt) {
        return producedType(unit.getCallableDeclaration(), type,
                producedType(unit.getTupleDeclaration(), rt, rt, 
                        emptyType()));
    }
    
    private void visitBaseMemberExpression(
            Tree.StaticMemberOrTypeExpression that, 
            TypedDeclaration member, 
            List<ProducedType> typeArgs, 
            Tree.TypeArguments tal) {
        if (acceptsTypeArguments(member, typeArgs, tal, that)) {
            ProducedType outerType = 
                    that.getScope()
                        .getDeclaringType(member);
            ProducedTypedReference pr = 
                    member.getProducedTypedReference(outerType, 
                            typeArgs, that.getAssigned());
            that.setTarget(pr);
            boolean direct = that.getDirectlyInvoked();
            ProducedType fullType =
                    accountForGenericFunctionRef(direct, 
                            tal, outerType, typeArgs, 
                            pr.getFullType());
            if (!dynamic && 
                    !isNativeForWrongBackend() && 
                    !isAbstraction(member) && 
                    isTypeUnknown(fullType)) {
                that.addError("could not determine type of function or value reference: '" +
                        member.getName(unit) + "'" + 
                        getTypeUnknownError(fullType));
            }
            if (dynamic && 
                    !isNativeForWrongBackend() && 
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
            List<ProducedType> typeArgs;
            if (explicitTypeArguments(type, tal)) {
                List<TypeParameter> tps = 
                        type.getTypeParameters();
                typeArgs = getTypeArguments(tal, null, tps);
            }
            else {
                typeArgs = inferFunctionRefTypeArgs(that);
            }
            if (typeArgs!=null) {
                tal.setTypeModels(typeArgs);
                visitBaseTypeExpression(that, type, 
                        typeArgs, tal);
                //otherwise infer type arguments later
            }
            else if (!that.getStaticMethodReferencePrimary()) {
                if (!(type instanceof Class)) {
                    typeArgumentsImplicit(that);
                }
                else {
                    visitGenericBaseTypeReference(that, type);
                }
            }
        }
    }

    private void visitGenericBaseTypeReference(
            Tree.StaticMemberOrTypeExpression that,
            TypeDeclaration type) {
        if (isGeneric(type)) {
            Generic g = (Generic) type;
            List<TypeParameter> typeParameters = 
                    g.getTypeParameters();
            Scope scope = that.getScope();
            ProducedType outerType = 
                    scope.getDeclaringType(type);
            ProducedType pt = 
                    type.getProducedType(outerType, 
                            toTypeArgs(g));
            that.setTarget(pt);
            TypeAlias ta = new TypeAlias();
            ta.setContainer(scope);
            ta.setName("Anonymous#" + type.getName());
            ta.setAnonymous(true);
            ta.setScope(scope);
            ta.setUnit(unit);
            ta.setExtendedType(pt.getFullType());
            ta.setTypeParameters(typeParameters);
            ProducedType t = ta.getType();
            t.setTypeConstructor(true);
            that.setTypeModel(t);
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
            if (!dynamic && 
                    !isNativeForWrongBackend() && 
                    error) {
                that.addError("type does not exist: '" + 
                        name + "'", 
                        102);
                unit.getUnresolvedReferences()
                    .add(id);
            }
        }
        else {
            if (type.isNative()) {
                type = (TypeDeclaration)
                        nativeDeclaration(type);
            }
            if (type == null) {
                that.addError("type does not have a native implementation for this backend: '" +
                        name + "'");
                unit.getUnresolvedReferences()
                    .add(id);
            } else {
                type = (TypeDeclaration) 
                        handleAbstraction(type, that);
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
        }
        return type;
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
                    that.addError("abstract constructor cannot be invoked: '" +
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
            else if (type instanceof TypeParameter) {
                TypeParameter tp = (TypeParameter) type;
                if (tp.getParameterList()==null) {
                    that.addError("type parameter cannot be instantiated: '" +
                            type.getName(unit) + "'");
                    return false;
                }
                else {
                    return true;
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
	            !inSameModule(type) &&
	    		!that.getStaticMethodReferencePrimary()) {
	        String moduleName = 
	                type.getUnit()
	                    .getPackage()
	                    .getModule()
	                    .getNameAsString();
            if (type instanceof Constructor) {
	            String cname = 
	                    type.getExtendedTypeDeclaration()
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
            ProducedType receiverType = 
                    primary.getTypeModel()
                        .resolveAliases(); //TODO: probably not necessary
            List<ProducedType> typeArgs;
            if (explicitTypeArguments(type, tal)) {
                List<TypeParameter> tps = 
                        type.getTypeParameters();
                typeArgs = getTypeArguments(tal, receiverType, 
                        tps);
            }
            else {
                typeArgs = inferFunctionRefTypeArgs(that);
            }
            if (typeArgs!=null) {
                tal.setTypeModels(typeArgs);
                if (primary instanceof Tree.Package) {
                    visitBaseTypeExpression(that, type, 
                            typeArgs, tal);
                }
                else {
                    visitQualifiedTypeExpression(that, 
                            receiverType, type, typeArgs, 
                            tal);
                }
                //otherwise infer type arguments later
            }
            else if (!that.getStaticMethodReferencePrimary() &&
                    !that.getStaticMethodReference()) {
                if (!(type instanceof Class)) {
                    typeArgumentsImplicit(that);
                }
                else if (primary instanceof Tree.Package) {
                    visitGenericBaseTypeReference(that, type);
                }
                else {
                    visitGenericQualifiedTypeReference(that, 
                            receiverType, type);
                }
            }
            else {
                typeArgumentsImplicit(that);
            }
            handleStaticPrimaryImplicitTypeArguments(that, 
                    primary);
        }
    }

    private void visitGenericQualifiedTypeReference(
            Tree.QualifiedTypeExpression that,
            ProducedType outerType,
            TypeDeclaration type) {
        if (isGeneric(type)) {
            Generic g = (Generic) type;
            List<TypeParameter> typeParameters = 
                    g.getTypeParameters();
            Scope scope = that.getScope();
            ProducedType pt =
                    outerType.getTypeMember(type, 
                            toTypeArgs(g));
            that.setTarget(pt);
            TypeAlias ta = new TypeAlias();
            ta.setContainer(scope);
            ta.setName("Anonymous#" + type.getName());
            ta.setAnonymous(true);
            ta.setScope(scope);
            ta.setUnit(unit);
            ta.setExtendedType(pt.getFullType());
            ta.setTypeParameters(typeParameters);
            ProducedType t = ta.getType();
            t.setTypeConstructor(true);
            that.setTypeModel(t);
        }
    }

    private TypeDeclaration resolveQualifiedTypeExpression(
            Tree.QualifiedTypeExpression that,
            boolean error) {
        if (checkMember(that)) {
            Tree.Primary p = that.getPrimary();
            Tree.Identifier id = that.getIdentifier();
            List<ProducedType> signature = 
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
                ProducedType pt = 
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
                if (type.isNative()) {
                    type = (TypeDeclaration)
                            nativeDeclaration(type);
                }
                if (type == null) {
                    that.addError("member type does not have a native implementation for this backend: '" +
                            name + "' in " + container);
                    unit.getUnresolvedReferences()
                        .add(id);
                }
                else {
                    type = (TypeDeclaration) 
                            handleAbstraction(type, that);
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
        ProducedType pt = p.getTypeModel();
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
            ProducedType pt) {
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
        if (dec instanceof Value) {
            Value value = (Value) dec;
            ProducedTypedReference param = 
                    smte.getTargetParameter();
            ProducedType paramType = 
                    smte.getParameterType();
            if (param!=null && paramType==null) {
                paramType = param.getType();
            }
            ProducedType type = value.getType();
            return type!=null &&
                    type.isTypeConstructor() &&
                    paramType!=null &&
                    !paramType.isTypeConstructor();
        }
        else {
            return false;
        }
    }

    @Override public void visit(Tree.SimpleType that) {
        //this one is a declaration, not an expression!
        //we are only validating type arguments here
        super.visit(that);
        ProducedType pt = that.getTypeModel();
        if (pt!=null) {
            TypeDeclaration type = 
                    that.getDeclarationModel();//pt.getDeclaration()
            Tree.TypeArgumentList tal = 
                    that.getTypeArgumentList();
            //No type inference for declarations
            if (type!=null) {
                List<TypeParameter> params = 
                        type.getTypeParameters();
                List<ProducedType> ta = 
                        getTypeArguments(tal, pt.getQualifyingType(), 
                                params);
                acceptsTypeArguments(type, ta, tal, that);
                //the type has already been set by TypeVisitor
                if (tal!=null) {
                    //TODO: dupe of logic in TypeVisitor!!!!
                    List<Tree.Type> args = tal.getTypes();
                    for (int i = 0; 
                            i<args.size() && 
                            i<params.size(); 
                            i++) {
                        Tree.Type t = args.get(i);
                        if (t instanceof Tree.StaticType) {
                            TypeParameter p = params.get(i);
                            Tree.StaticType st = 
                                    (Tree.StaticType) t;
                            Tree.TypeVariance variance = 
                                    st.getTypeVariance();
                            if (variance!=null) {
                                if (!p.isInvariant()) {
                                    variance.addError("type parameter is not declared invariant: '" + 
                                            p.getName() + "' of '" + 
                                            type.getName(unit) + "'");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override public void visit(Tree.EntryType that) {
        super.visit(that);
        Tree.StaticType keyType = that.getKeyType();
        checkAssignable(keyType.getTypeModel(), 
                objectType(), keyType,
                "entry key type must not be an optional type");
    }

    private void visitQualifiedTypeExpression(
            Tree.QualifiedTypeExpression that,
            ProducedType receivingType, 
            TypeDeclaration memberType, 
            List<ProducedType> typeArgs, 
            Tree.TypeArguments tal) {
        ProducedType receiverType =
                accountForStaticReferenceReceiverType(that, 
                        unwrap(receivingType, that));
        if (acceptsTypeArguments(receiverType, memberType, 
                typeArgs, tal, that)) {
            ProducedType type = 
                    receiverType.getTypeMember(memberType, 
                            typeArgs);
            that.setTarget(type);
            ProducedType fullType =
                    type.getFullType(wrap(type, 
                            receivingType, that));
            if (!dynamic && 
                    !isNativeForWrongBackend() &&
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
            that.setTypeModel(accountForStaticReferenceType(that, 
                    memberType, fullType));
        }
    }
    
    private void visitBaseTypeExpression(
            Tree.StaticMemberOrTypeExpression that, 
            TypeDeclaration baseType, 
            List<ProducedType> typeArgs, 
            Tree.TypeArguments tal) {
        visitBaseTypeExpression(that, baseType, typeArgs, 
                tal, null);
    }
    
    private void visitBaseTypeExpression(
            Tree.StaticMemberOrTypeExpression that, 
            TypeDeclaration baseType, 
            List<ProducedType> typeArgs, 
            Tree.TypeArguments tal, 
            ProducedType qt) {
        if (acceptsTypeArguments(baseType, typeArgs, tal, that)) {
            ProducedType outerType = 
                    that.getScope()
                        .getDeclaringType(baseType);
            if (outerType==null) {
                outerType = qt;
            }
            ProducedType type = 
                    baseType.getProducedType(outerType, 
                            typeArgs);
            ProducedType fullType = type.getFullType();
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
            ProducedType t = term.getTypeModel();
            if (t!=null) {
                that.setTypeModel(t);
            }
        }
    }
    
    @Override public void visit(Tree.Outer that) {
        ProducedType oci = 
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
        ProducedType tt = null;
        Tree.SequencedArgument sa = 
                that.getSequencedArgument();
        if (sa!=null) {
            List<Tree.PositionalArgument> pas = 
                    sa.getPositionalArguments();
            tt = getTupleType(pas, unit, true);
        }
        else {
            tt = emptyType();
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
        ProducedType st = null;
        Tree.SequencedArgument sa = 
                that.getSequencedArgument();
        if (sa!=null) {
            List<Tree.PositionalArgument> pas = 
                    sa.getPositionalArguments();
            ProducedType tt = getTupleType(pas, unit, false);
            if (tt!=null) {
                st = tt.getSupertype(unit.getIterableDeclaration());
                if (st==null) {
                    st = unit.getIterableType(unknownType());
                }
            }
        }
        else {
            st = unit.getIterableType(nothingType());
        }
        if (st!=null) {
            that.setTypeModel(st);
            if (st.containsUnknowns()) {
                that.addError("iterable element type could not be inferred");
            }
        }
    }

    private ProducedType nothingType() {
        return unit.getNothingDeclaration().getType();
    }

    private ProducedType unknownType() {
        return new UnknownType(unit).getType();
    }

    private ProducedType throwableType() {
        return unit.getType(unit.getThrowableDeclaration());
    }

    private ProducedType exceptionType() {
        return unit.getType(unit.getExceptionDeclaration());
    }

    @Override public void visit(Tree.CatchVariable that) {
        super.visit(that);
        Tree.Variable var = that.getVariable();
        if (var!=null) {
            Tree.Type vt = var.getType();
            if (vt instanceof Tree.LocalModifier) {
                ProducedType et = exceptionType();
                vt.setTypeModel(et);
                var.getDeclarationModel().setType(et);
            }
            else {
                checkAssignable(vt.getTypeModel(), 
                        throwableType(), vt, 
                        "catch type must be a throwable type");
            }
        }
    }

    @Override public void visit(Tree.StringTemplate that) {
        super.visit(that);
        for (Tree.Expression e: that.getExpressions()) {
            ProducedType et = e.getTypeModel();
            if (!isTypeUnknown(et)) {
                checkAssignable(et, 
                        objectType(), e, 
                        "interpolated expression must not be an optional type");
            }
        }
        setLiteralType(that, unit.getStringDeclaration());
    }
    
    @Override public void visit(Tree.StringLiteral that) {
        setLiteralType(that, unit.getStringDeclaration());
    }
    
    @Override public void visit(Tree.NaturalLiteral that) {
        setLiteralType(that, unit.getIntegerDeclaration());
    }
    
    @Override public void visit(Tree.FloatLiteral that) {
        setLiteralType(that, unit.getFloatDeclaration());
    }
    
    @Override public void visit(Tree.CharLiteral that) {
        String result = that.getText();
        if (result.codePointCount(1, result.length()-1)!=1) {
            that.addError("character literal must contain exactly one character");
        }
        setLiteralType(that, 
                unit.getCharacterDeclaration());
    }
    
    @Override public void visit(Tree.QuotedLiteral that) {
        setLiteralType(that, 
                unit.getStringDeclaration());
    }
    
    private void setLiteralType(Tree.Atom that, 
            TypeDeclaration languageType) {
        that.setTypeModel(unit.getType(languageType));
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
                ProducedType t = e.getTypeModel();
                if (!isTypeUnknown(t)) {
                    if (switchStatementOrExpression!=null) {
                        Tree.Switched switched = 
                                switchClause().getSwitched();
                        if (switched!=null) {
                            Tree.Expression switchExpression = 
                                    switched.getExpression();
                            Tree.Variable switchVariable = 
                                    switched.getVariable();
                            ProducedType st = 
                                    getSwitchType(switchExpression,
                                            switchVariable);
                            if (st!=null) {
                                ProducedType it = 
                                        intersectionType(t, st, unit);
                                if (it.isNothing() &&
                                        mayNotBeNothing(switchExpression, 
                                                switchVariable, t)) {
                                    e.addError("value is not a case of the switched type: '" +
                                                t.getProducedTypeName(unit) +
                                                "' is not a case of the type '" +
                                                st.getProducedTypeName(unit) + "'");
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
        ProducedType t = e.getTypeModel();
        if (term instanceof Tree.NegativeOp) {
            Tree.NegativeOp no = 
                    (Tree.NegativeOp) term;
            term = no.getTerm();
        }
        if (term instanceof Tree.Literal) {
            if (term instanceof Tree.FloatLiteral) {
                e.addError("literal case may not be a 'Float' literal");
            }
        }
        else if (term instanceof Tree.MemberOrTypeExpression) {
            ProducedType ut = 
                    unionType(nullType(), identifiableType(), 
                            unit);
            TypeDeclaration dec = t.getDeclaration();
            if (!dec.isAnonymous() || 
                    (!dec.isToplevel() && 
                     !dec.isStaticallyImportable())) {
                e.addError("case must refer to a toplevel object declaration or literal value");
            }
            else {
                checkAssignable(t, ut, e, 
                        "case must be identifiable or null");
            }
        }
        else if (term!=null) {
            e.addError("case must be a literal value or refer to a toplevel object declaration");
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
                ProducedType st = 
                        getSwitchType(switchExpression,
                                switchVariable);
                if (st!=null) {
                    if (v!=null) {
                        if (dynamic || 
                                isNativeForWrongBackend() || 
                                !isTypeUnknown(st)) { //eliminate dupe errors
                            v.visit(this);
                        }
                        initOriginalDeclaration(v);
                    }
                    if (t!=null) {
                        ProducedType pt = t.getTypeModel();
//                        if (!isTypeUnknown(pt)) {
                        ProducedType it = 
                                intersectionType(pt, st, unit);
                        if (it.isNothing() &&
                                mayNotBeNothing(switchExpression, 
                                        switchVariable, pt)) {
                            that.addError("narrows to bottom type 'Nothing': '" + 
                                    pt.getProducedTypeName(unit) + 
                                    "' has empty intersection with '" + 
                                    st.getProducedTypeName(unit) + "'");
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
            Tree.Variable switchVariable, ProducedType pt) {
        return switchVariable!=null || 
                switchExpression!=null &&
                (!hasUncheckedNulls(switchExpression.getTerm()) || 
                        !isNullCase(pt));
    }

    @Override
    public void visit(Tree.SatisfiesCase that) {
        super.visit(that);
        that.addUnsupportedError("satisfies cases are not yet supported");
    }
    
    private static ProducedType getSwitchType(
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
            ProducedType switchExpressionType = 
                    getSwitchedExpressionType(switched);
            if (switchCaseList!=null && 
                    switchExpressionType!=null) {
                checkCases(switchCaseList);
                Tree.ElseClause elseClause = 
                        switchCaseList.getElseClause();
                if (!isTypeUnknown(switchExpressionType) 
                        && elseClause==null) {
                    ProducedType caseUnionType = 
                            caseUnionType(switchCaseList);
                    if (caseUnionType!=null) {
                        //if the union of the case types covers 
                        //the switch expression type then the 
                        //switch is exhaustive
                        if (!caseUnionType.covers(switchExpressionType)) {
                            switchClause.addError("case types must cover all cases of the switch type or an else clause must appear: '" +
                                    caseUnionType.getProducedTypeName(unit) + "' does not cover '" + 
                                    switchExpressionType.getProducedTypeName(unit) + "'", 
                                    10000);
                        }
                    }
                }
            }
        }
    }

    private static ProducedType getSwitchedExpressionType(
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
                    ProducedType switchExpressionType = 
                            getSwitchedExpressionType(switched);
                    Tree.SwitchCaseList switchCaseList = 
                            switchCaseList();
                    if (switchExpressionType!=null && 
                            switchCaseList!=null) {
                        if (!isTypeUnknown(switchExpressionType)) {
                            ProducedType caseUnionType = 
                                    caseUnionType(switchCaseList);
                            if (caseUnionType!=null) {
                                ProducedType complementType = 
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
                        ProducedType t = 
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
                checkCasesDisjoint(cc, occ);
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

    private ProducedType caseUnionType(Tree.SwitchCaseList switchCaseList) {
        //form the union of all the case types
        List<Tree.CaseClause> caseClauses = 
                switchCaseList.getCaseClauses();
        List<ProducedType> list = 
                new ArrayList<ProducedType>
                    (caseClauses.size());
        for (Tree.CaseClause cc: caseClauses) {
            ProducedType ct = getTypeIgnoringLiterals(cc);
            if (isTypeUnknown(ct)) {
                return null; //Note: early exit!
            }
            else {
                addToUnion(list, ct);
            }
        }
        UnionType ut = new UnionType(unit);
        ut.setCaseTypes(list);
        return ut.getType();
    }

    private void checkCasesDisjoint(Tree.CaseClause cc, 
            Tree.CaseClause occ) {
        Tree.CaseItem cci = cc.getCaseItem();
        Tree.CaseItem occi = occ.getCaseItem();
        if (cci instanceof Tree.IsCase || 
            occi instanceof Tree.IsCase) {
            checkCasesDisjoint(getType(cc), getType(occ), cci);
        }
        else {
            checkCasesDisjoint(getTypeIgnoringLiterals(cc),
                    getTypeIgnoringLiterals(occ), cci);
        }
        if (cci instanceof Tree.MatchCase && 
            occi instanceof Tree.MatchCase) {
            checkLiteralsDisjoint((Tree.MatchCase) cci, 
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

    private static boolean isNullCase(ProducedType ct) {
        TypeDeclaration d = ct.getDeclaration();
        Unit unit = d.getUnit();
        return d!=null && d instanceof Class &&
                d.equals(unit.getNullDeclaration());
    }

    private ProducedType getType(Tree.CaseItem ci) {
        Tree.IsCase ic = (Tree.IsCase) ci;
        Tree.Type t = ic.getType();
        if (t!=null) {
            return t.getTypeModel().getUnionOfCases();
        }
        else {
            return null;
        }
    }
    
    private ProducedType getType(Tree.CaseClause cc) {
        Tree.CaseItem ci = cc.getCaseItem();
        if (ci instanceof Tree.IsCase) {
            return getType(ci);
        }
        else if (ci instanceof Tree.MatchCase) {
            Tree.MatchCase mc = (Tree.MatchCase) ci;
            List<Tree.Expression> es = 
                    mc.getExpressionList().getExpressions();
            List<ProducedType> list = 
                    new ArrayList<ProducedType>(es.size());
            for (Tree.Expression e: es) {
                if (e.getTypeModel()!=null) {
                    addToUnion(list, e.getTypeModel());
                }
            }
            return formUnion(list);
        }
        else {
            return null;
        }
    }

    private ProducedType getTypeIgnoringLiterals(Tree.CaseClause cc) {
        Tree.CaseItem ci = cc.getCaseItem();
        if (ci instanceof Tree.IsCase) {
            return getType(ci);
        }
        else if (ci instanceof Tree.MatchCase) {
            Tree.MatchCase mc = (Tree.MatchCase) ci;
            List<Tree.Expression> es = 
                    mc.getExpressionList().getExpressions();
            List<ProducedType> list = 
                    new ArrayList<ProducedType>(es.size());
            for (Tree.Expression e: es) {
                if (e.getTypeModel()!=null && 
                        !(e.getTerm() instanceof Tree.Literal) && 
                        !(e.getTerm() instanceof Tree.NegativeOp)) {
                    addToUnion(list, e.getTypeModel());
                }
            }
            return formUnion(list);
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
                ProducedType ct = 
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
                            ProducedType ect = 
                                    eccv.getVariable()
                                        .getType()
                                        .getTypeModel();
                            if (ect!=null) {
                                if (ct.isSubtypeOf(ect)) {
                                    ccv.getVariable()
                                        .getType()
                                        .addError("exception type is already handled by earlier catch clause: '" 
                                                + ct.getProducedTypeName(unit) + "'");
                                }
                                if (ct.getDeclaration() instanceof UnionType) {
                                    for (ProducedType ut: 
                                            ct.getDeclaration()
                                                .getCaseTypes()) {
                                        if (ut.substitute(ct.getTypeArguments())
                                                .isSubtypeOf(ect)) {
                                            ccv.getVariable()
                                                .getType()
                                                .addError("exception type is already handled by earlier catch clause: '"
                                                        + ut.getProducedTypeName(unit) + "'");
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
    
    private boolean acceptsTypeArguments(Declaration member, 
            List<ProducedType> typeArguments, 
            Tree.TypeArguments tal, Node parent) {
        return acceptsTypeArguments(null, member, 
                typeArguments, tal, parent);
    }
    
    private boolean acceptsTypeArguments(ProducedType receiver, 
            Declaration dec, List<ProducedType> typeArguments, 
            Tree.TypeArguments tas, Node parent) {
        if (dec==null) {
            return false;
        }
        else if (isGeneric(dec)) {
            return checkTypeArgumentAgainstDeclaration(
                    receiver, dec, typeArguments, tas, 
                    parent);
        }
        else {
            boolean empty = typeArguments.isEmpty();
            if (dec instanceof Value && !empty) {
                Value td = (Value) dec;
                ProducedType type = td.getType();
                if (type!=null && 
                        type.isTypeConstructor()) {
                    checkArgumentsAgainstTypeConstructor(
                            typeArguments, tas, type, 
                            parent);
                    return true;
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
            ProducedType receiver, Declaration dec, 
            List<ProducedType> typeArguments,
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
                ProducedType argType = 
                        typeArguments.get(i);
                boolean argTypeMeaningful = 
                        argType!=null && 
                        !(argType.getDeclaration() 
                                instanceof UnknownType);
                if (argTypeMeaningful) {
                    if (argType.isTypeConstructor() && 
                            !param.isTypeConstructor()) {
                        typeArgNode(tas, i, parent)
                            .addError("type argument must be a regular type: parameter '" + 
                                    param.getName() + 
                                    "' is a regular type parameter but '" +
                                    argType.getProducedTypeName(unit) + 
                                    "' is a type constructor");
                    }
                    else if (!argType.isTypeConstructor() && 
                            param.isTypeConstructor()) {
                        typeArgNode(tas, i, parent)
                            .addError("type argument must be a type constructor: parameter '" + 
                                    param.getName() + 
                                    "' is a type constructor parameter but '" +
                                    argType.getProducedTypeName(unit) + 
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
                if (!isCondition && 
                        !(param.getSatisfiedTypes().isEmpty() && 
                                param.getCaseTypes()==null)) {
                    ProducedType assignedType = 
                            argumentTypeForBoundsCheck(param, 
                                    argType);
                    for (ProducedType st: param.getSatisfiedTypes()) {
                        ProducedType bound =
                                st.getProducedType(receiver, 
                                        dec, typeArguments);
                        if (!assignedType.isSubtypeOf(bound)) {
                            if (argTypeMeaningful) {
                                if (explicit) {
                                    typeArgNode(tas, i, parent)
                                        .addError("type parameter '" + param.getName() 
                                                + "' of declaration '" + dec.getName(unit)
                                                + "' has argument '" 
                                                + assignedType.getProducedTypeName(unit) 
                                                + "' which is not assignable to upper bound '" 
                                                + bound.getProducedTypeName(unit)
                                                + "' of '" + param.getName() + "'", 2102);
                                }
                                else {
                                    parent.addError("inferred type argument '" 
                                            + assignedType.getProducedTypeName(unit)
                                            + "' to type parameter '" + param.getName()
                                            + "' of declaration '" + dec.getName(unit)
                                            + "' is not assignable to upper bound '" 
                                            + bound.getProducedTypeName(unit)
                                            + "' of effectively " + 
                                            (isEffectivelyContravariant(param)?"contra":"co") + 
                                            "variant '" + param.getName() + "'");
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
                                            + assignedType.getProducedTypeName(unit) 
                                            + "' which is not one of the enumerated cases of '" 
                                            + param.getName() + "'");
                            }
                            else {
                                parent.addError("inferred type argument '" 
                                        + assignedType.getProducedTypeName(unit)
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
                String help;
                if (args<min) {
                    help = " requires at least " + min + 
                            " type arguments";
                }
                else if (args>max) {
                    help = " allows at most " + max + 
                            " type arguments";
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

    private static ProducedType argumentTypeForBoundsCheck(TypeParameter param,
            ProducedType argType) {
        if (argType==null) {
            return null;
        }
        else if (argType.isTypeConstructor()) {
            return argType.getDeclaration()
                .getProducedType(null, 
                    param.getType()
                        .getTypeArgumentList());
        }
        else {
            return argType;
        }
    }

    private static Node typeArgNode
            (Tree.TypeArguments tas, int i, Node parent) {
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
            List<ProducedType> typeArguments,
            Tree.TypeArguments tas, ProducedType type,
            Node parent) {
        boolean explicit = 
                tas instanceof Tree.TypeArgumentList;
        TypeDeclaration tcd = 
                type.getDeclaration();
        List<TypeParameter> typeParameters = 
                tcd.getTypeParameters();
        int size = typeArguments.size();
        if (size != typeParameters.size()) {
            tas.addError("wrong number of type arguments: type constructor requires " + 
                    size + " type arguments");
        }
        else {
            Map<TypeParameter, ProducedType> args = 
                    getTypeArgumentMap(tcd, 
                            null, typeArguments);
            for (int i=0; i<size; i++) {
                ProducedType arg = 
                        typeArguments.get(i);
                TypeParameter param = 
                        typeParameters.get(i);
                List<ProducedType> sts = 
                        param.getSatisfiedTypes();
                for (ProducedType st: sts) {
                    ProducedType bound = 
                            st.substitute(args);
                    if (explicit) {
                        checkAssignable(arg, bound, 
                                typeArgNode(tas, i, parent), 
                                "type argument '" + 
                                arg.getProducedTypeName(unit) +
                                "' is not assignable to upper bound '" +
                                bound.getProducedTypeName(unit) +
                                "' of type parameter '" +
                                param.getName() + "' of '" +
                                param.getDeclaration().getName(unit) + 
                                "'");
                    }
                    else {
                        checkAssignable(arg, bound, parent,
                                "inferred type argument '" + 
                                arg.getProducedTypeName(unit) +
                                "' is not assignable to upper bound '" +
                                bound.getProducedTypeName(unit) +
                                "' of type parameter '" +
                                param.getName() + "' of '" +
                                param.getDeclaration().getName(unit) + 
                                "'");
                    }
                }
                if (!satisfiesEnumeratedConstraint(args, arg, param)) {
                    if (explicit) {
                        typeArgNode(tas, i, parent)
                            .addError("type argument '" + 
                                    arg.getProducedTypeName(unit) +
                                    "' is not one of the enumerated cases of the type parameter '" +
                                    param.getName() + "' of '" +
                                    param.getDeclaration().getName(unit) + 
                                    "'");
                    }
                    else {
                        parent.addError("inferred type argument '" + 
                                arg.getProducedTypeName(unit) +
                                "' is not one of the enumerated cases of the type parameter '" +
                                param.getName() + "' of '" +
                                param.getDeclaration().getName(unit) + 
                                "'");

                    }
                }
            }
        }
    }

    boolean satisfiesEnumeratedConstraint(
            Map<TypeParameter, ProducedType> args,
            ProducedType arg, TypeParameter param) {
        List<ProducedType> cts = 
                param.getCaseTypes();
        if (cts!=null) {
            for (ProducedType ct: cts) {
                ProducedType bound = 
                        ct.substitute(args);
                if (arg.isSubtypeOf(bound)) {
                    return true;
                }
            }
            if (arg.getDeclaration() instanceof TypeParameter) {
                for (ProducedType act: arg.getCaseTypes()) {
                    boolean foundCase = false;
                    for (ProducedType ct: cts) {
                        ProducedType bound = 
                                ct.substitute(args);
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
            ProducedType argType, Node argNode) {
        if (!argType.isTypeConstructor()) {
            argNode.addError("not a type constructor: '" +
                    argType.getProducedTypeName(unit) + "'");
        }
        else {
            TypeDeclaration atd = 
                    argType.getDeclaration();
            if (atd instanceof UnionType) {
                for (ProducedType ct: 
                        atd.getCaseTypes()) {
                    checkTypeConstructorParam(param, 
                            ct, argNode);
                }
            }
            else if (atd instanceof IntersectionType) {
                for (ProducedType st: 
                        atd.getSatisfiedTypes()) {
                    checkTypeConstructorParam(param, 
                            st, argNode);
                }
            }
            else if (atd instanceof NothingType) {
                //just let it through?!
            }
            else {
                List<TypeParameter> argTypeParams = 
                        atd.getTypeParameters();
                int allowed = argTypeParams.size();
                int required = 0;
                for (TypeParameter tp: argTypeParams) {
                    if (tp.isDefaulted()) break;
                    required++;
                }
                List<TypeParameter> paramTypeParams = 
                        param.getTypeParameters();
                int size = paramTypeParams.size();
                if (allowed<size || required>size) {
                    argNode.addError("argument type constructor has wrong number of type parameters: argument '" +
                            atd.getName(unit) + "' has " + 
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
                                atd.getName(unit) + 
                                "' must have the same variance as '" +
                                paramParam.getName() + "' of '" + 
                                param.getName(unit) + 
                                "'");
                    }
                    else if (paramParam.isContravariant() &&
                            !argParam.isContravariant()) {
                        argNode.addError("argument type constructor is not contravariant: '" +
                                argParam.getName() + "' of '" + 
                                atd.getName(unit) + 
                                "' must have the same variance as '" +
                                paramParam.getName() + "' of '" + 
                                param.getName(unit) + 
                                "'");
                    }
                    if (!intersectionOfSupertypes(paramParam)
                            .isSubtypeOf(intersectionOfSupertypes(argParam))) {
                        argNode.addError("upper bound on type parameter of argument type constructor is not a supertype of upper bound on corresponding type parameter of parameter: '" + 
                                argParam.getName() + "' of '" + 
                                atd.getName(unit) + 
                                "' does accept all type arguments accepted by '" + 
                                paramParam.getName() + "' of '" + 
                                param.getName(unit) + 
                                "'");
                    }
                    if (!unionOfCaseTypes(paramParam)
                            .isSubtypeOf(unionOfCaseTypes(argParam))) {
                        argNode.addError("enumerated bound on type parameter of argument type constructor is not a supertype of enumerated bound on corresponding type parameter of parameter: '" + 
                                argParam.getName() + "' of '" + 
                                atd.getName(unit) + 
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
            ProducedType type = et.getTypeModel();
            if (type!=null) {
                Tree.Primary pr = ie.getPrimary();
                if (pr instanceof Tree.InvocationExpression) {
                    Tree.InvocationExpression iie = 
                            (Tree.InvocationExpression) pr;
                    pr = iie.getPrimary();
                }
                if (pr instanceof Tree.ExtendedTypeExpression) {
                    Tree.ExtendedTypeExpression ete = 
                            (Tree.ExtendedTypeExpression) pr;
                    ete.setDeclaration(et.getDeclarationModel());
                    ete.setTarget(type);
                    ProducedType qt = type.getQualifyingType();
                    ProducedType ft = type.getFullType();
                    if (ete.getStaticMethodReference()) {
                        ft = getStaticReferenceType(ft, qt);
                    }
                    pr.setTypeModel(ft);
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
        
        //Dupe check:
        /*if (ie!=null && 
                ie.getPrimary() instanceof Tree.MemberOrTypeExpression) {
            checkOverloadedReference((Tree.MemberOrTypeExpression) ie.getPrimary());
        }*/
    }
    
    private ClassOrInterface constructorClass;
    
    @Override 
    public void visit(Tree.Constructor that) {
        Constructor c = that.getDeclarationModel();
        if (that.getDelegatedConstructor()==null) {
            if (c.isClassMember()) {
                Class clazz = (Class) c.getContainer();
                Class superclass = 
                        clazz.getExtendedTypeDeclaration();
                if (superclass!=null &&
                        !unit.getBasicDeclaration()
                            .equals(superclass)) {
                    that.addError("constructor must explicitly delegate to some superclass constructor: '" +
                            clazz.getName() + "' extends '" + 
                            superclass.getName() + "'");
                }
            }
        }
        ClassOrInterface occ = constructorClass;
        constructorClass = 
                that.getDeclarationModel()
                        .getExtendedTypeDeclaration();
        Tree.Type rt = 
                beginReturnScope(new Tree.VoidModifier(that.getToken()));
        Declaration od = 
                beginReturnDeclaration(c);
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
        constructorClass = occ;
    }
    
    @Override 
    public void visit(Tree.DelegatedConstructor that) {
        Tree.SimpleType type = that.getType();
        visitExtendedOrAliasedType(type, 
                that.getInvocationExpression());
        
        inExtendsClause = true;
        super.visit(that);
        inExtendsClause = false;
        
        TypeDeclaration constructor = 
                (TypeDeclaration) that.getScope();
        Scope container = constructor.getContainer();
        if (type!=null &&
                constructor instanceof Constructor &&
                container instanceof Class) {
            Class containingClass = (Class) container;
            Class superclass = 
                    containingClass.getExtendedTypeDeclaration();
            if (superclass!=null) {
                ProducedType extendedType = 
                        containingClass.getExtendedType();
                ProducedType constructedType = 
                        type.getTypeModel();
                Declaration delegate = 
                        type.getDeclarationModel();
                if (delegate instanceof Constructor) {
                    Constructor c = (Constructor) delegate;
                    if (c.equals(constructor)) {
                        type.addError("constructor delegates to itself: '" +
                                c.getName() + "'");
                    }
                    ClassOrInterface delegatedType = 
                            c.getExtendedTypeDeclaration();
                    if (superclass.equals(delegatedType)) {
                        checkIsExactly(
                                constructedType.getExtendedType(), 
                                extendedType, type, 
                                "type arguments must match type arguments in extended class expression");
                    }
                    else if (containingClass.equals(delegatedType)) {
                        if (type instanceof Tree.QualifiedType) {
                            Tree.QualifiedType qt = 
                                    (Tree.QualifiedType) type;
                            checkIsExactly(
                                    constructedType.getQualifyingType(), 
                                    containingClass.getType(), 
                                    qt.getOuterType(), 
                                    "type arguments must be the type parameters of this class");
                        }
                    }
                    else {
                        type.addError("not a constructor of the immediate superclass: '" +
                                delegate.getName(unit) + 
                                "' is not a constructor of '" + 
                                superclass.getName(unit) + "'");
                    }
                }
                else if (delegate instanceof Class) {
                    if (superclass.equals(delegate)) {
                        checkIsExactly(constructedType, 
                                extendedType, type, 
                                "type arguments must match type arguments in extended class expression");
                    }
                    else if (containingClass.equals(delegate)) {
                        checkIsExactly(constructedType, 
                                containingClass.getType(), type, 
                                "type arguments must be the type parameters of this class");
                    }
                    else {
                        type.addError("does not instantiate the immediate superclass: '" +
                                delegate.getName(unit) + "' is not '" + 
                                superclass.getName(unit) + "'");
                    }
                }
            }
        }
    }

    @Override 
    public void visit(Tree.ExtendedType that) {
        visitExtendedOrAliasedType(that.getType(), 
                that.getInvocationExpression());
        
        inExtendsClause = true;
        super.visit(that);
        inExtendsClause = false;
                
        TypeDeclaration td = 
                (TypeDeclaration) that.getScope();
        if (!td.isAlias()) {
            Tree.SimpleType et = that.getType();
            if (et!=null) {
                Tree.InvocationExpression ie = 
                        that.getInvocationExpression();
                Class clazz = (Class) td;
                boolean hasConstructors = 
                        clazz.hasConstructors();
                boolean anonymous = clazz.isAnonymous();
                if (ie==null) { 
                    if (!hasConstructors || anonymous) {
                        et.addError("missing instantiation arguments");
                    }
                }
                else {
                    if (hasConstructors && !anonymous) {
                        et.addError("unnecessary instantiation arguments");
                    }
                }

                ProducedType type = et.getTypeModel();
                if (type!=null) {
                    checkSelfTypes(et, td, type);
                    checkExtensionOfMemberType(et, td, type);
                    //checkCaseOfSupertype(et, td, type);
                    TypeDeclaration etd = 
                            td.getExtendedTypeDeclaration();
                    while (etd!=null && etd.isAlias()) {
                        etd = etd.getExtendedTypeDeclaration();
                    }
                    if (etd!=null) {
                        if (etd.isFinal()) {
                            et.addError("extends a final class: '" + 
                                    etd.getName(unit) + "'");
                        }
                    	if (etd.isSealed() && 
                    	        !inSameModule(etd)) {
                    		String moduleName = 
                    		        etd.getUnit()
                    				    .getPackage()
                    				    .getModule()
                    				    .getNameAsString();
                            et.addError("extends a sealed class in a different module: '" +
                    				etd.getName(unit) + "' in '" + moduleName + "'");
                    	}
                    }
//                if (td.isParameterized() &&
//                        type.getDeclaration().inherits(unit.getExceptionDeclaration())) {
//                    et.addUnsupportedError("generic exception types not yet supported");
//                }
                }
                checkSupertypeVarianceAnnotations(et);
            }
        }
    }

    private void checkSupertypeVarianceAnnotations(Tree.SimpleType et) {
        Tree.TypeArgumentList tal = 
                et.getTypeArgumentList();
        if (tal!=null) {
            for (Tree.Type t: tal.getTypes()) {
                if (t instanceof Tree.StaticType) {
                    Tree.StaticType st = (Tree.StaticType) t;
                    Tree.TypeVariance variance = 
                            st.getTypeVariance();
                    if (variance!=null) {
                        variance.addError("supertype expression may not specify variance");
                    }
                }
            }
        }
    }

	private boolean inSameModule(TypeDeclaration etd) {
	    return etd.getUnit().getPackage().getModule()
	    		.equals(unit.getPackage().getModule());
    }

    @Override 
    public void visit(Tree.SatisfiedTypes that) {
        super.visit(that);
        TypeDeclaration td = 
                (TypeDeclaration) that.getScope();
        if (td.isAlias()) {
            return;
        }
        Set<TypeDeclaration> set = 
                new HashSet<TypeDeclaration>();
        if (td.getSatisfiedTypes().isEmpty()) {
            return; //handle undecidability case
        }
        for (Tree.StaticType t: that.getTypes()) {
            ProducedType type = t.getTypeModel();
            if (type!=null && type.getDeclaration()!=null) {
                type = type.resolveAliases();
                TypeDeclaration std = type.getDeclaration();
                if (td instanceof ClassOrInterface &&
                        !inLanguageModule(that.getUnit())) {
                    if (unit.isCallableType(type)) {
                        t.addError("satisfies 'Callable'");
                    }
                    TypeDeclaration cad = 
                            unit.getConstrainedAnnotationDeclaration();
                    if (type.getDeclaration().equals(cad)) {
                        t.addError("directly satisfies 'ConstrainedAnnotation'");
                    }
                }
                if (!set.add(type.getDeclaration())) {
                    //this error is not really truly necessary
                    //but the spec says it is an error, and
                    //the backend doesn't like it
                    t.addError("duplicate satisfied type: '" + 
                            type.getDeclaration().getName(unit) +
                            "' of '" + td.getName() + "'");
                }
            	if (td instanceof ClassOrInterface && 
            			std.isSealed() && !inSameModule(std)) {
        			String moduleName = 
        			        std.getUnit()
        			            .getPackage()
        			            .getModule()
        			            .getNameAsString();
                    t.addError("satisfies a sealed interface in a different module: '" +
        					std.getName(unit) + "' in '" + moduleName + "'");
            	}
                checkSelfTypes(t, td, type);
                checkExtensionOfMemberType(t, td, type);
                /*if (!(td instanceof TypeParameter)) {
                    checkCaseOfSupertype(t, td, type);
                }*/
            }
            if (t instanceof Tree.SimpleType) {
                Tree.SimpleType st = (Tree.SimpleType) t;
                checkSupertypeVarianceAnnotations(st);
            }
        }
        //Moved to RefinementVisitor, which 
        //handles this kind of stuff:
        /*if (td instanceof TypeParameter) {
            List<ProducedType> list = new ArrayList<ProducedType>();
            for (ProducedType st: td.getSatisfiedTypes()) {
                addToIntersection(list, st, unit);
            }
            IntersectionType it = new IntersectionType(unit);
            it.setSatisfiedTypes(list);
            if (it.getType().getDeclaration() instanceof NothingType) {
                that.addError("upper bound constraints cannot be satisfied by any type except Nothing");
            }
        }*/
    }

    /*void checkCaseOfSupertype(Tree.StaticType t, TypeDeclaration td,
            ProducedType type) {
        //TODO: I think this check is a bit too restrictive, since 
        //      it doesn't allow intermediate types between the 
        //      enumerated type and the case type, but since the
        //      similar check below doesn't work, we need it
        if (type.getDeclaration().getCaseTypes()!=null) {
            for (ProducedType ct: type.getDeclaration().getCaseTypes()) {
                if (ct.substitute(type.getTypeArguments())
                        .isExactly(td.getType())) {
                    return;
                }
            }
            t.addError("not a case of supertype: " + 
                    type.getDeclaration().getName(unit));
        }
    }*/

    @Override 
    public void visit(Tree.CaseTypes that) {
        super.visit(that);
        //this forces every case to be a subtype of the
        //enumerated type, so that we can make use of the
        //enumerated type is equivalent to its cases
        TypeDeclaration td = 
                (TypeDeclaration) that.getScope();
        
        //TODO: get rid of this awful hack:
        List<ProducedType> cases = td.getCaseTypes();
        td.setCaseTypes(null);
        
        if (td instanceof TypeParameter) {
            for (Tree.StaticType t: that.getTypes()) {
                for (Tree.StaticType ot: that.getTypes()) {
                    if (t==ot) break;
                    checkCasesDisjoint(t.getTypeModel(), 
                            ot.getTypeModel(), ot);
                }
            }
        }
        else {
            Set<TypeDeclaration> typeSet = 
                    new HashSet<TypeDeclaration>();
            for (Tree.StaticType ct: that.getTypes()) {
                ProducedType type = ct.getTypeModel();
                if (type!=null && type.getDeclaration()!=null) {
                    type = type.resolveAliases();
                    TypeDeclaration ctd = type.getDeclaration();
                    if (!typeSet.add(ctd)) {
                        //this error is not really truly necessary
                        ct.addError("duplicate case type: '" + 
                                ctd.getName(unit) + "' of '" + 
                                td.getName() + "'");
                    }
                    if (!(ctd instanceof TypeParameter)) {
                        //it's not a self type
                        if (checkDirectSubtype(td, ct, type)) {
                            checkAssignable(type, td.getType(), ct,
                                    getCaseTypeExplanation(td, type));
                        }
                        //note: this is a better, faster way to call 
                        //      validateEnumeratedSupertypeArguments()
                        //      but unfortunately it winds up displaying
                        //      the error on the wrong node, confusing
                        //      the user
                        /*ProducedType supertype = type.getDeclaration().getType().getSupertype(td);
                        validateEnumeratedSupertypeArguments(t, type.getDeclaration(), supertype);*/
                    }
                    if (ctd instanceof ClassOrInterface && 
                            ct instanceof Tree.SimpleType) {
                        Tree.SimpleType s = 
                                (Tree.SimpleType) ct;
                        Tree.TypeArgumentList tal = 
                                s.getTypeArgumentList();
                        if (tal!=null) {
                            List<Tree.Type> args = 
                                    tal.getTypes();
                            List<TypeParameter> typeParameters = 
                                    ctd.getTypeParameters();
                            for (int i=0; 
                                    i<args.size() && 
                                    i<typeParameters.size(); 
                                    i++) {
                                Tree.Type arg = args.get(i);
                                TypeParameter typeParameter = 
                                        ctd.getTypeParameters()
                                            .get(i);
                                ProducedType argType = 
                                        arg.getTypeModel();
                                if (argType!=null) {
                                    TypeDeclaration argTypeDec = 
                                            argType.getDeclaration();
                                    if (argTypeDec instanceof TypeParameter) {
                                        TypeParameter tp = 
                                                (TypeParameter) 
                                                    argTypeDec;
                                        if (!tp.getDeclaration().equals(td)) {
                                            arg.addError("type argument is not a type parameter of the enumerated type: '" +
                                                    argTypeDec.getName() + "' is not a type parameter of '" + td.getName());
                                        }
                                    }
                                    else if (typeParameter.isCovariant()) {
                                        checkAssignable(typeParameter.getType(), 
                                                argType, arg, 
                                                "type argument not an upper bound of the type parameter");
                                    }
                                    else if (typeParameter.isContravariant()) {
                                        checkAssignable(argType, 
                                                typeParameter.getType(), arg, 
                                                "type argument not a lower bound of the type parameter");
                                    }
                                    else {
                                        arg.addError("type argument is not a type parameter of the enumerated type: '" +
                                                argTypeDec.getName() + "'");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Set<Declaration> valueSet = new HashSet<Declaration>();
            for (Tree.BaseMemberExpression bme: 
                    that.getBaseMemberExpressions()) {
                ProducedType type = bme.getTypeModel();
                Declaration d = bme.getDeclaration();
                if (d!=null && !valueSet.add(d)) {
                    //this error is not really truly necessary
                    bme.addError("duplicate case: '" + 
                            d.getName(unit) + 
                            "' of '" + td.getName() + "'");
                }
                if (d!=null && type!=null && 
                        !type.getDeclaration().isAnonymous()) {
                    bme.addError("case must be a toplevel anonymous class: '" + 
                            d.getName(unit) + "' is not an anonymous class");
                }
                else if (d!=null && !d.isToplevel()) {
                    bme.addError("case must be a toplevel anonymous class: '" + 
                            d.getName(unit) + "' is not toplevel");
                }
                if (type!=null) {
                    if (checkDirectSubtype(td, bme, type)) {
                        checkAssignable(type, td.getType(), bme, 
                                getCaseTypeExplanation(td, type));
                    }
                }
            }
        }
        
        //TODO: get rid of this awful hack:
        td.setCaseTypes(cases);
    }

    private static boolean checkDirectSubtype(TypeDeclaration td, 
            Node node, ProducedType type) {
        boolean found = false;
        TypeDeclaration ctd = type.getDeclaration();
        if (td instanceof Interface) {
            for (ProducedType st: ctd.getSatisfiedTypes()) {
                if (st!=null && 
                        st.resolveAliases().getDeclaration()
                            .equals(td)) {
                    found = true;
                }
            }
        }
        else if (td instanceof Class) {
            ProducedType et = ctd.getExtendedType();
            if (et!=null && 
                    et.resolveAliases().getDeclaration()
                        .equals(td)) {
                found = true;
            }
        }
        if (!found) {
            node.addError("case type is not a direct subtype of enumerated type: " + 
                    ctd.getName(node.getUnit()));
        }
        return found;
    }

    private String getCaseTypeExplanation(TypeDeclaration td, 
            ProducedType type) {
        String message = "case type must be a subtype of enumerated type";
        if (!td.getTypeParameters().isEmpty() &&
                type.getDeclaration().inherits(td)) {
            message += " for every type argument of the generic enumerated type";
        }
        return message;
    }

    private void checkCasesDisjoint(ProducedType type, ProducedType other,
            Node ot) {
        if (!isTypeUnknown(type) && !isTypeUnknown(other)) {
            ProducedType it = 
                    intersectionType(type.resolveAliases(), 
                            other.resolveAliases(), unit);
            if (!it.isNothing()) {
                ot.addError("cases are not disjoint: '" + 
                        type.getProducedTypeName(unit) + "' and '" + 
                        other.getProducedTypeName(unit) + "'");
            }
        }
    }

    private void checkExtensionOfMemberType(Node that, TypeDeclaration td,
            ProducedType type) {
        ProducedType qt = type.getQualifyingType();
        if (qt!=null && td instanceof ClassOrInterface) {
            TypeDeclaration d = type.getDeclaration();
            if (d.isStaticallyImportable() ||
                    d instanceof Constructor) {
                checkExtensionOfMemberType(that, td, qt);
            }
            else {
                Scope s = td;
                while (s!=null) {
                    s = s.getContainer();
                    if (s instanceof TypeDeclaration) {
                        TypeDeclaration otd = 
                                (TypeDeclaration) s;
                        if (otd.getType().isSubtypeOf(qt)) {
                            return;
                        }
                    }
                }
                that.addError("qualifying type '" + qt.getProducedTypeName(unit) + 
                        "' of supertype '" + type.getProducedTypeName(unit) + 
                        "' is not an outer type or supertype of any outer type of '" +
                        td.getName(unit) + "'");
            }
        }
    }
    
    private void checkSelfTypes(Tree.StaticType that, TypeDeclaration td, ProducedType type) {
        if (!(td instanceof TypeParameter)) { //TODO: is this really ok?!
            List<TypeParameter> params = 
                    type.getDeclaration().getTypeParameters();
            List<ProducedType> args = 
                    type.getTypeArgumentList();
            for (int i=0; i<params.size(); i++) {
                TypeParameter param = params.get(i);
                if ( param.isSelfType() && args.size()>i ) {
                    ProducedType arg = args.get(i);
                    if (arg==null) arg = 
                            unknownType();
                    TypeDeclaration std = 
                            param.getSelfTypedDeclaration();
                    ProducedType at;
                    TypeDeclaration mtd;
                    if (param.getContainer().equals(std)) {
                        at = td.getType();
                        mtd = td;
                    }
                    else {
                        //TODO: lots wrong here?
                        mtd = (TypeDeclaration) 
                                td.getMember(std.getName(), 
                                        null, false);
                        at = mtd==null ? null : mtd.getType();
                    }
                    if (at!=null && !at.isSubtypeOf(arg) && 
                            !(mtd.getSelfType()!=null && 
                                mtd.getSelfType().isExactly(arg))) {
                        String help;
                        TypeDeclaration ad = arg.getDeclaration();
                        if (ad instanceof TypeParameter &&
                                ((TypeParameter) ad).getDeclaration().equals(td)) {
                            help = " (try making " + ad.getName() + 
                                    " a self type of " + td.getName() + ")";
                        }
                        else if (ad instanceof Interface) {
                            help = " (try making " + td.getName() + 
                                    " satisfy " + ad.getName() + ")";
                        }
                        else if (ad instanceof Class && td instanceof Class) {
                            help = " (try making " + td.getName() + 
                                    " extend " + ad.getName() + ")";
                        }
                        else {
                            help = "";
                        }
                        that.addError("type argument does not satisfy self type constraint on type parameter '" +
                                param.getName() + "' of '" + 
                                type.getDeclaration().getName(unit) + "': '" +
                                arg.getProducedTypeName(unit) + 
                                "' is not a supertype or self type of '" + 
                                td.getName(unit) + "'" + help);
                    }
                }
            }
        }
    }

    private void validateEnumeratedSupertypes(Node that, ClassOrInterface d) {
        ProducedType type = d.getType();
        for (ProducedType supertype: type.getSupertypes()) {
            if (!type.isExactly(supertype)) {
                TypeDeclaration std = supertype.getDeclaration();
                List<ProducedType> cts = std.getCaseTypes();
                if (cts!=null && 
                        !cts.isEmpty()) {
                    if (cts.size()==1 && 
                            cts.get(0).getDeclaration()
                                .isSelfType()) {
                        continue;
                    }
                    List<ProducedType> types =
                            new ArrayList<ProducedType>
                                (cts.size());
                    for (ProducedType ct: cts) {
                        TypeDeclaration ctd = 
                                ct.resolveAliases().getDeclaration();
                        ProducedType cst = 
                                type.getSupertype(ctd);
                        if (cst!=null) {
                            types.add(cst);
                        }
                    }
                    if (types.isEmpty()) {
                        that.addError("not a subtype of any case of enumerated supertype: '" + 
                                d.getName(unit) + "' is a subtype of '" + 
                                std.getName(unit) + "'");
                    }
                    else if (types.size()>1) {
                        StringBuilder sb = new StringBuilder();
                        for (ProducedType pt: types) {
                            sb.append("'")
                              .append(pt.getProducedTypeName(unit))
                              .append("' and ");
                        }
                        sb.setLength(sb.length()-5);
                        that.addError("concrete type is a subtype of multiple cases of enumerated supertype '" +
                                std.getName(unit) + "': '" + 
                                d.getName(unit) + "' is a subtype of " + sb);
                    }
                }
            }
        }
    }

    private void validateEnumeratedSupertypeArguments(Node that, ClassOrInterface d) {
        //note: I hate doing the whole traversal here, but it is the
        //      only way to get the error in the right place (see
        //      the note in visit(CaseTypes) for more)
        ProducedType type = d.getType();
        for (ProducedType supertype: type.getSupertypes()) { //traverse the entire supertype hierarchy of the declaration
            if (!type.isExactly(supertype)) {
                List<TypeDeclaration> ctds = 
                        supertype.getDeclaration()
                            .getCaseTypeDeclarations();
                if (ctds!=null) {
                    for (TypeDeclaration ct: ctds) {
                        if (ct.equals(d)) { //the declaration is a case of the current enumerated supertype
                            validateEnumeratedSupertypeArguments(that, 
                                    d, supertype);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void validateEnumeratedSupertypeArguments(Node that, TypeDeclaration d, 
            ProducedType supertype) {
        for (TypeParameter p: 
                supertype.getDeclaration().getTypeParameters()) {
            ProducedType arg = 
                    supertype.getTypeArguments().get(p); //the type argument that the declaration (indirectly) passes to the enumerated supertype
            if (arg!=null) {
                validateEnumeratedSupertypeArgument(that, 
                        d, supertype, p, arg);
            }
        }
    }

    private void validateEnumeratedSupertypeArgument(Node that, 
            TypeDeclaration d, ProducedType supertype, 
            TypeParameter p, ProducedType arg) {
        TypeDeclaration td = arg.getDeclaration();
        if (td instanceof TypeParameter) {
            TypeParameter tp = (TypeParameter) td;
            if (tp.getDeclaration().equals(d)) { //the argument is a type parameter of the declaration
                //check that the variance of the argument type parameter is
                //the same as the type parameter of the enumerated supertype
                if (p.isCovariant() && !tp.isCovariant()) {
                    that.addError("argument to covariant type parameter of enumerated supertype must be covariant: " + 
                            typeDescription(p, unit));
                }
                if (p.isContravariant() && 
                        !tp.isContravariant()) {
                    that.addError("argument to contravariant type parameter of enumerated supertype must be contravariant: " + 
                            typeDescription(p, unit));
                }
            }
            else {
                that.addError("argument to type parameter of enumerated supertype must be a type parameter of '" +
                        d.getName() + "': " + 
                        typeDescription(p, unit));
            }
        }
        else if (p.isCovariant()) {
            if (!(td instanceof NothingType)) {
                //TODO: let it be the union of the lower bounds on p
                that.addError("argument to covariant type parameter of enumerated supertype must be a type parameter or 'Nothing': " + 
                        typeDescription(p, unit));
            }
        }
        else if (p.isContravariant()) {
            List<ProducedType> sts = p.getSatisfiedTypes();
            //TODO: do I need to do type arg substitution here??
            ProducedType ub = formIntersection(sts);
            if (!(arg.isExactly(ub))) {
                that.addError("argument to contravariant type parameter of enumerated supertype must be a type parameter or '" + 
                        typeNamesAsIntersection(sts, unit) + "': " + 
                        typeDescription(p, unit));
            }
        }
        else {
            that.addError("argument to type parameter of enumerated supertype must be a type parameter: " + 
                    typeDescription(p, unit));
        }
    }
    
    @Override public void visit(Tree.Term that) {
        super.visit(that);
        if (that.getTypeModel()==null) {
            that.setTypeModel( defaultType() );
        }
    }

    @Override public void visit(Tree.Type that) {
        super.visit(that);
        if (that.getTypeModel()==null) {
            that.setTypeModel( defaultType() );
        }
    }

    private ProducedType defaultType() {
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
        try {
            super.visit(that);
        }
        finally {
            declarationLiteral = false;
        }
        ProducedType t;
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
        // FIXME: should we disallow type parameters in there?
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
                if (t==null || t.isUnknown()) return;
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
        try {
            super.visit(that);
        }
        finally {
            declarationLiteral = false;
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
            if (result instanceof Method) {
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
        if (result instanceof Method) {
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
        ProducedType outerType;
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
        if (result instanceof Method) {
            Method method = (Method) result;
            if (method.isAbstraction()) {
                that.addError("method is overloaded");
            }
            else {
                Tree.TypeArgumentList tal = 
                        that.getTypeArgumentList();
                if (explicitTypeArguments(method, tal)) {
                    List<ProducedType> ta = 
                            getTypeArguments(tal, 
                                    outerType, 
                                    getTypeParameters(method));
                    if (tal != null) {
                        tal.setTypeModels(ta);
                    }
                    if (acceptsTypeArguments(outerType, method, 
                            ta, tal, that)) {
                        ProducedTypedReference pr = 
                                outerType==null ? 
                                        method.getProducedTypedReference(null, ta) : 
                                        outerType.getTypedMember(method, ta);
                                that.setTarget(pr);
                                that.setTypeModel(unit.getFunctionMetatype(pr));
                    }
                }
                else {
                    that.addError("missing type arguments to: '" + 
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
                ProducedTypedReference pr = 
                        value.getProducedTypedReference(outerType, 
                                NO_TYPE_ARGS);
                that.setTarget(pr);
                that.setTypeModel(unit.getValueMetatype(pr));
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
    
    private Declaration handleAbstraction(Declaration dec, 
            Tree.MemberOrTypeExpression that) {
        //NOTE: if this is the qualifying type of a static method
        //      reference, don't do anything special here, since
        //      we're not actually calling the constructor
        if (!that.getStaticMethodReferencePrimary() &&
                isAbstraction(dec)) {
            //handle the case where it's not _really_ overloaded,
            //it's just a constructor with a different visibility
            //to the class itself
        	List<Declaration> overloads = 
        	        ((Functional) dec).getOverloads();
        	if (overloads.size()==1) {
        		return overloads.get(0);
        	}
        }
        return dec;
    }
    
    private boolean isNativeForWrongBackend() {
        return inBackend != null && 
                !backendSupport.supportsBackend(inBackend);
    }
    
    private Declaration nativeDeclaration(Declaration decl) {
        Declaration d = 
                getNativeDeclaration(decl, 
                        inBackend != null ? 
                                inBackend.backendSupport : 
                                backendSupport);
        if (d == null) {
            d = getNativeDeclaration(decl, Backend.None);
        }
        return d;
    }
}
