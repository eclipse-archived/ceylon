package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkAssignable;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkCallable;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkIsExactly;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkSupertype;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkTypeBelongsToContainingScope;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getBaseDeclaration;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.getTypeArguments;
import static com.redhat.ceylon.compiler.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.addToUnion;
import static com.redhat.ceylon.compiler.typechecker.model.Util.getContainingClassOrInterface;
import static com.redhat.ceylon.compiler.typechecker.model.Util.getOuterClassOrInterface;
import static com.redhat.ceylon.compiler.typechecker.model.Util.intersectionType;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isAbstraction;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isTypeUnknown;
import static com.redhat.ceylon.compiler.typechecker.model.Util.producedType;
import static com.redhat.ceylon.compiler.typechecker.model.Util.unionType;
import static com.redhat.ceylon.compiler.typechecker.tree.Util.name;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.BottomType;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.FunctionalParameter;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Expression;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

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
    private Tree.Expression switchExpression;
    private Declaration returnDeclaration;
    private boolean defaultArgument;

    private Unit unit;
    
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
            returnType.setTypeModel( unit.getBottomDeclaration().getType() );
        }
        return ort;
    }
    
    private void endReturnScope(Tree.Type t, TypedDeclaration td) {
        if (returnType instanceof Tree.FunctionModifier || 
                returnType instanceof Tree.ValueModifier) {
            td.setType( returnType.getTypeModel() );
        }
        returnType = t;
    }
    
    @Override public void visit(Tree.DefaultArgument that) {
        defaultArgument=true;
        super.visit(that);
        defaultArgument=false;
    }
    
    @Override public void visit(Tree.FunctionArgument that) {
        super.visit(that);
        ProducedType t = unit.denotableType(that.getExpression().getTypeModel());
        that.getDeclarationModel().setType(t);
        /*List<ProducedType> list = new ArrayList<ProducedType>();
        for (Parameter p: that.getDeclarationModel().getParameterLists().get(0)
                .getParameters()) {
            list.add(p.getType());
        }
        ProducedType ft = unit.getCallableDeclaration().getProducedType(null, list);*/
        that.getType().setTypeModel(t);
        that.setTypeModel(that.getDeclarationModel()
                .getProducedTypedReference(null, Collections.<ProducedType>emptyList())
                .getFullType());
    }
    
    @Override public void visit(Tree.ExpressionComprehensionClause that) {
        super.visit(that);
        that.setTypeModel(that.getExpression().getTypeModel());
    }
    
    @Override public void visit(Tree.ForComprehensionClause that) {
        super.visit(that);
        Tree.ComprehensionClause cc = that.getComprehensionClause();
        if (cc!=null) {
            that.setTypeModel(cc.getTypeModel());
        }
    }
    
    @Override public void visit(Tree.IfComprehensionClause that) {
        super.visit(that);
        Tree.ComprehensionClause cc = that.getComprehensionClause();
        if (cc!=null) {
            that.setTypeModel(cc.getTypeModel());
        }
    }
    
    @Override
    public void visit(Tree.AnyMethod that) {
        super.visit(that);
        TypedDeclaration td = that.getDeclarationModel();
        for (Tree.ParameterList list: that.getParameterLists()) {
            for (Tree.Parameter tp: list.getParameters()) {
                if (tp!=null) {
                    Parameter p = tp.getDeclarationModel();
                    if (p.getType()!=null && !isCompletelyVisible(td, p.getType())) {
                        tp.getType().addError("type of parameter is not visible everywhere declaration is visible: " 
                                + p.getName());
                    }
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.AnyClass that) {
        super.visit(that);
        Class td = that.getDeclarationModel();
        if (that.getParameterList()!=null) {
            for (Tree.Parameter tp: that.getParameterList().getParameters()) {
                if (tp!=null) {
                    Parameter p = tp.getDeclarationModel();
                    if (p.getType()!=null && !isCompletelyVisible(td, p.getType())) {
                        tp.getType().addError("type of parameter is not visible everywhere declaration is visible: " 
                                + p.getName());
                    }
                }
            }
        }
    }
    
    private boolean isCompletelyVisible(Declaration member, ProducedType pt) {
        if (pt.getDeclaration() instanceof UnionType) {
            for (ProducedType ct: pt.getDeclaration().getCaseTypes()) {
                if ( !isCompletelyVisible(member, ct.substitute(pt.getTypeArguments())) ) {
                    return false;
                }
            }
            return true;
        }
        else if (pt.getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: pt.getDeclaration().getSatisfiedTypes()) {
                if ( !isCompletelyVisible(member, ct.substitute(pt.getTypeArguments())) ) {
                    return false;
                }
            }
            return true;
        }
        else {
            if (!isVisible(member, pt.getDeclaration())) {
                return false;
            }
            for (ProducedType at: pt.getTypeArgumentList()) {
                if ( at!=null && !isCompletelyVisible(member, at) ) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean isVisible(Declaration member, TypeDeclaration type) {
        return type instanceof TypeParameter || 
                type.isVisible(member.getVisibleScope());
    }

    @Override public void visit(Tree.Variable that) {
        super.visit(that);
        if (that.getSpecifierExpression()!=null) {
            inferType(that, that.getSpecifierExpression());
            if (that.getType()!=null) {
                checkType(that.getType().getTypeModel(), that.getSpecifierExpression());
            }
        }
    }

    private void initOriginalDeclaration(Tree.Variable that) {
        if (that.getType() instanceof Tree.SyntheticVariable) {
            Tree.BaseMemberExpression bme = (Tree.BaseMemberExpression) that
                    .getSpecifierExpression().getExpression().getTerm();
            ((TypedDeclaration) that.getDeclarationModel())
                .setOriginalDeclaration((TypedDeclaration) bme.getDeclaration());
        }
    }
    
    @Override public void visit(Tree.IsCondition that) {
        //don't recurse to the Variable, since we don't
        //want to check that the specifier expression is
        //assignable to the declared variable type
        //(nor is it possible to infer the variable type)
        that.getType().visit(this);
        Tree.Variable v = that.getVariable();
        ProducedType type = that.getType().getTypeModel();
        if (v!=null) {
            //v.getType().visit(this);
            Tree.SpecifierExpression se = v.getSpecifierExpression();
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
                Expression e = se.getExpression();
                knownType = e==null ? null : e.getTypeModel();
                if (knownType!=null && knownType.isSubtypeOf(type)) {
                    that.addError("does not narrow type: " + knownType.getProducedTypeName() + 
                            " is a subtype of " + type.getProducedTypeName());
                }
            }
            defaultTypeToVoid(v);
            ProducedType it = knownType==null ? type : 
                    intersectionType(type, knownType, that.getUnit());
            if (it.getDeclaration() instanceof BottomType) {
                that.addError("tests assignability to Bottom type: intersection of " +
                        knownType.getProducedTypeName() + " and " + 
                        type.getProducedTypeName() +
                        " is empty");
            }
            if (v.getType() instanceof Tree.SyntheticVariable) {
                //when we're reusing the original name, we narrow to the
                //intersection of the outer type and the type specified
                //in the condition
                v.getType().setTypeModel(it);
                v.getDeclarationModel().setType(it);
            }
        }
        else if (that.getExpression()!=null) {
            that.getExpression().visit(this);
        }
        /*if (that.getExpression()!=null) {
            that.getExpression().visit(this);
        }*/
        checkReified(that, type);
    }

    private void checkReified(Node that, ProducedType type) {
        if (type!=null) {
            TypeDeclaration dec = type.getDeclaration();
            if (dec instanceof UnionType) {
                for (ProducedType pt: dec.getCaseTypes()) {
                    checkReified(that, pt);
                }
            }
            else if (dec instanceof IntersectionType) {
                for (ProducedType pt: dec.getSatisfiedTypes()) {
                    checkReified(that, pt);
                }
            }
            else if (dec instanceof TypeParameter) {
                that.addWarning("type parameter in assignability condition not yet supported (until we implement reified generics)");
            }
            else if (isGeneric(dec)) {
                List<TypeParameter> params = dec.getTypeParameters();
                List<ProducedType> args = type.getTypeArgumentList();
                if (params.size()==args.size()) {
                    for (int i=0; i<params.size(); i++) {
                        TypeParameter tp = params.get(i);
                        ProducedType ta = args.get(i);
                        if (ta!=null) {
                            if (tp.isCovariant()) {
                                List<ProducedType> list = new ArrayList<ProducedType>();
                                addToIntersection(list, unit.getVoidDeclaration().getType(), unit);
                                for (ProducedType st: tp.getSatisfiedTypes()) {
                                    addToIntersection(list, st, unit);
                                }
                                IntersectionType ut = new IntersectionType(unit);
                                ut.setSatisfiedTypes(list);
                                if (!ta.isExactly(ut.getType())) {
                                    that.addWarning("type argument to covariant type parameter in assignability condition must be " +
                                            ut.getType().getProducedTypeName() + " (until we implement reified generics)");
                                }
                            }
                            else if (tp.isContravariant()) {
                                if (!(ta.getDeclaration() instanceof BottomType)) {
                                    that.addWarning("type argument to contravariant type parameter in assignability condition must be Bottom (until we implement reified generics)");
                                }
                            }
                            else {
                                that.addWarning("type argument to invariant type parameter in assignability condition not yet supported (until we implement reified generics)");
                            }
                        }
                    }
                }
                //else there is another error, so don't bother
                //with this check at all
            }
        }
    }

    @Override public void visit(Tree.SatisfiesCondition that) {
        super.visit(that);
        that.addWarning("satisfies conditions not yet supported");
    }
    
    @Override public void visit(Tree.ExistsOrNonemptyCondition that) {
        //don't recurse to the Variable, since we don't
        //want to check that the specifier expression is
        //assignable to the declared variable type
        //(nor is it possible to infer the variable type)
        ProducedType t = null;
        Node n = that;
        Tree.Term term = null;
        Tree.Variable v = that.getVariable();
        if (v!=null) {
            //v.getType().visit(this);
            defaultTypeToVoid(v);
            Tree.SpecifierExpression se = v.getSpecifierExpression();
            if (se!=null) {
                se.visit(this);
                if (that instanceof Tree.ExistsCondition) {
                    inferDefiniteType(v, se);
                    checkOptionalType(v, se);
                }
                else if (that instanceof Tree.NonemptyCondition) {
                    inferNonemptyType(v, se);
                    checkEmptyOptionalType(v, se);
                }
                t = se.getExpression().getTypeModel();
                n = v;
                checkReferenceIsNonVariable(v, se);
                initOriginalDeclaration(v);
                term = se.getExpression().getTerm();
            }
        }
        else if (that.getExpression()!=null) {
            //note: this is only here to handle
            //      erroneous syntax elegantly
            that.getExpression().visit(this);
            t = that.getExpression().getTypeModel();
            term = that.getExpression().getTerm();
        }
        /*Tree.Expression e = that.getExpression();
        if (e!=null) {
            e.visit(this);
            t = e.getTypeModel();
            n = e;
        }*/
        if (that instanceof Tree.ExistsCondition) {
            checkOptional(t, term, n);
        }
        else if (that instanceof Tree.NonemptyCondition) {
            checkEmpty(t, term, n);
        }
    }

    private void defaultTypeToVoid(Tree.Variable v) {
        /*if (v.getType().getTypeModel()==null) {
            v.getType().setTypeModel( getVoidDeclaration().getType() );
        }*/
        v.getType().visit(this);
        if (v.getDeclarationModel().getType()==null) {
            v.getDeclarationModel().setType( defaultType() );
        }
    }

    private void checkReferenceIsNonVariable(Tree.Variable v,
            Tree.SpecifierExpression se) {
        if (v.getType() instanceof Tree.SyntheticVariable) {
            checkReferenceIsNonVariable((Tree.BaseMemberExpression) se.getExpression().getTerm());
        }
    }

    private void checkReferenceIsNonVariable(Tree.BaseMemberExpression ref) {
        Declaration d = ref.getDeclaration();
        if (d!=null) {
            if (d instanceof Getter) {
                ref.addError("referenced value is a getter: " + d.getName());
            }
            if ( ( (TypedDeclaration) d ).isVariable() ) {
                ref.addError("referenced value is variable: " + d.getName());
            }
            if ( d.isDefault() ) {
                ref.addError("referenced value is default and may be refined: " + d.getName());
            }
        }
    }
    
    private void checkEmpty(ProducedType t, Tree.Term term, Node n) {
        /*if (t==null) {
            n.addError("expression must be a type with fixed size: type not known");
        }
        else*/ if (t!=null && !unit.isEmptyType(t)) {
            term.addError("expression must be a possibly-empty type: " + 
                    t.getProducedTypeName() + " is not possibly-empty");
        }
    }
    
    private boolean hasUncheckedNulls(Tree.Term term) {
        if (term instanceof Tree.MemberOrTypeExpression) {
            Declaration d = ((Tree.MemberOrTypeExpression) term).getDeclaration();
            return d instanceof TypedDeclaration && 
                    ((TypedDeclaration) d).hasUncheckedNullType();
        }
        else if (term instanceof Tree.InvocationExpression) {
            return hasUncheckedNulls(((Tree.InvocationExpression) term).getPrimary());
        }
        else {
            return false;
        }
    }

    private void checkOptional(ProducedType t, Tree.Term term, Node n) {
        /*if (t==null) {
            n.addError("expression must be of optional type: type not known");
        }
        else*/ if (t!=null && !unit.isOptionalType(t) && 
                !hasUncheckedNulls(term)) {
            term.addError("expression must be of optional type: " +
                    t.getProducedTypeName() + " is not optional");
        }
    }

    @Override public void visit(Tree.BooleanCondition that) {
        super.visit(that);
        if (that.getExpression()!=null) {
            checkAssignable(that.getExpression().getTypeModel(), 
                    unit.getBooleanDeclaration().getType(), that, 
                    "expression must be of boolean type");
        }
    }

    @Override public void visit(Tree.Resource that) {
        super.visit(that);
        ProducedType t = null;
        Node typedNode = null;
        if (that.getExpression()!=null) {
            Tree.Expression e = that.getExpression();
            t = e.getTypeModel();
            typedNode = e;
            if (e.getTerm() instanceof Tree.InvocationExpression) {
                Tree.InvocationExpression ie = (Tree.InvocationExpression) e.getTerm();
                if (!(ie.getPrimary() instanceof Tree.BaseTypeExpression 
                        || ie.getPrimary() instanceof Tree.QualifiedTypeExpression)) {
                    e.addError("resource expression is not an unqualified value reference or instantiation");
                }
            }
            else if (!(e.getTerm() instanceof Tree.BaseMemberExpression)){
                e.addError("resource expression is not an unqualified value reference or instantiation");
            }
        }
        else if (that.getVariable()!=null) {
            t = that.getVariable().getType().getTypeModel();
            typedNode = that.getVariable().getType();
            Tree.SpecifierExpression se = that.getVariable().getSpecifierExpression();
            if (se==null) {
                that.getVariable().addError("missing resource specifier");
            }
            else if (typedNode instanceof Tree.ValueModifier){
                typedNode = se.getExpression();
            }
        }
        if (typedNode!=null) {
            checkAssignable(t, unit.getCloseableDeclaration().getType(), typedNode, 
                    "resource must be closeable");
        }
    }

    @Override public void visit(Tree.ValueIterator that) {
        super.visit(that);
        if (that.getVariable()!=null) {
            inferContainedType(that.getVariable(), that.getSpecifierExpression());
            checkContainedType(that.getVariable(), that.getSpecifierExpression());
        }
    }

    @Override public void visit(Tree.KeyValueIterator that) {
        super.visit(that);
        if (that.getKeyVariable()!=null && that.getValueVariable()!=null) {
            inferKeyType(that.getKeyVariable(), that.getSpecifierExpression());
            inferValueType(that.getValueVariable(), that.getSpecifierExpression());
            checkKeyValueType(that.getKeyVariable(), that.getValueVariable(), 
                    that.getSpecifierExpression());
        }
    }
    
    @Override public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);
        inferType(that, that.getSpecifierOrInitializerExpression());
        if (that.getType()!=null) {
            checkType(that.getType().getTypeModel(), 
                    that.getDeclarationModel().getName(),
                    that.getSpecifierOrInitializerExpression(), 
                    2100);
        }
    }
    
    @Override public void visit(Tree.SpecifierStatement that) {
        super.visit(that);
        Tree.Term me = that.getBaseMemberExpression();
        Tree.SpecifierExpression sie = that.getSpecifierExpression();
        if (me instanceof Tree.BaseMemberExpression) {
            Tree.BaseMemberExpression bme = (Tree.BaseMemberExpression) me;
            Declaration d = bme.getDeclaration();
            if (d!=null) { 
                if (that.getScope() instanceof Class && 
                        !d.isDefinedInScope(that.getScope())) {
                    //then it must be inherited ... TODO: is this totally correct? 
                    //so it's actually a refinement of a formal declaration!
                    if (d instanceof Value) {
                        refine((Value) d, bme, that);
                    }
                    else if (d instanceof Method) {
                        refine((Method) d, bme, that);
                    }
                    else {
                        //TODO!
                        bme.addError("not a reference to a formal attribute: " + d.getName());
                    }
                }
                else if (d instanceof MethodOrValue && 
                        ((MethodOrValue)d).isShortcutRefinement()) {
                    bme.addError("already specified: " + d.getName());
                }
                if (d instanceof Value && ((Value) d).isVariable()) {
                    sie.addError("variable values must be assigned using \":=\": " +
                                d.getName(), 802);
                }
                else if (d.isToplevel()) {
                    me.addError("toplevel declarations may not be specified");
                }
                checkType(me.getTypeModel(), d.getName(), sie, 2100);
            }
        }
        else {
            me.addError("illegal specification statement");
        }
    }

    private void refine(Value sv, Tree.BaseMemberExpression bme,
            Tree.SpecifierStatement that) {
        Class c = (Class) that.getScope();
        if (sv.isVariable()) {
            that.addError("attribute is variable: " + 
                    RefinementVisitor.message(sv));
        }
        if (!sv.isFormal()
                && !sv.isShortcutRefinement()) { //this condition is here to squash a dupe message
            bme.addError("attribute is not formal: " + 
                    RefinementVisitor.message(sv));
        }
        Value v = new Value();
        v.setName(sv.getName());
        /*if (sie!=null) {
            v.setType(sie.getExpression().getTypeModel());
        }*/
        v.setType(getRefinedMember(sv, c).getType());
        v.setShared(true);
        v.setActual(true);
        v.setRefinedDeclaration(v);
        v.setUnit(unit);
        v.setContainer(c);
        v.setShortcutRefinement(true);
        DeclarationVisitor.setVisibleScope(v);
        c.getMembers().add(v);
        bme.setDeclaration(v);
        //bme.setTypeModel(v.getType());
        that.setRefinement(true);
    }

    private ProducedReference getRefinedMember(MethodOrValue sv, Class c) {
        return sv.getProducedReference(c.getType().getSupertype((TypeDeclaration)sv.getContainer()), 
                Collections.<ProducedType>emptyList());
    }

    private void refine(Method sm, Tree.BaseMemberExpression bme,
            Tree.SpecifierStatement that) {
        Class c = (Class) that.getScope();
        if (!sm.isFormal()
                && !sm.isShortcutRefinement()) { //this condition is here to squash a dupe message
            bme.addError("method is not formal: " + 
                    RefinementVisitor.message(sm));
        }
        Method m = new Method();
        m.setName(sm.getName());
        /*if (sie!=null) {
            v.setType(sie.getExpression().getTypeModel());
        }*/
        ProducedReference rm = getRefinedMember(sm, c);
        m.setType(rm.getType());
        for (ParameterList pl: sm.getParameterLists()) {
            ParameterList l = new ParameterList();
            for (Parameter p: pl.getParameters()) {
                Parameter vp = new ValueParameter();
                vp.setSequenced(p.isSequenced());
                vp.setDefaulted(p.isDefaulted());
                vp.setName(p.getName());
                vp.setType(rm.getTypedParameter(p).getFullType());
                vp.setDeclaration(m);
                vp.setContainer(m);
                l.getParameters().add(vp);
            }
            m.getParameterLists().add(l);
        }
        if (!sm.getTypeParameters().isEmpty()) {
            bme.addError("method has type parameters: " +  
                    RefinementVisitor.message(sm));
        }
        m.setShared(true);
        m.setActual(true);
        m.setRefinedDeclaration(m);
        m.setUnit(unit);
        m.setContainer(c);
        m.setShortcutRefinement(true);
        m.setDeclaredVoid(sm.isDeclaredVoid());
        DeclarationVisitor.setVisibleScope(m);
        c.getMembers().add(m);
        bme.setDeclaration(m);
        //bme.setTypeModel(v.getType());
        that.setRefinement(true);
    }

    @Override public void visit(Tree.Parameter that) {
        super.visit(that);
        Tree.SpecifierExpression se = that.getDefaultArgument()==null ?
                null :
                that.getDefaultArgument().getSpecifierExpression();
        ProducedType t = that.getDeclarationModel().getProducedTypedReference(null, 
                Collections.<ProducedType>emptyList()).getFullType();
        checkType(t, that.getDeclarationModel().getName(), se, 2100);
    }

    @Override
    public void visit(Tree.ValueParameterDeclaration that) {
        if (that.getType() instanceof Tree.LocalModifier) {
            ValueParameter d = that.getDeclarationModel();
            if (d!=null) {
                that.getType().setTypeModel(d.getType());
            }
        }
        super.visit(that);
    }
    
    private void checkType(ProducedType declaredType, 
            Tree.SpecifierOrInitializerExpression sie) {
        if (sie!=null && sie.getExpression()!=null) {
            checkAssignable(sie.getExpression().getTypeModel(), declaredType, sie, 
                    "specified expression must be assignable to declared type");
        }
    }

    private void checkType(ProducedType declaredType, String name,
            Tree.SpecifierOrInitializerExpression sie, int code) {
        if (sie!=null && sie.getExpression()!=null) {
            checkAssignable(sie.getExpression().getTypeModel(), declaredType, sie, 
                    "specified expression must be assignable to declared type of " + name,
                    code);
        }
    }

    private void checkFunctionType(ProducedType et, Tree.Type that) {
        if (et!=null) {
            checkAssignable(et, that.getTypeModel(), that, 
                    "specified reference return type must be assignable to declared return type");
        }
    }

    private void checkOptionalType(Tree.Variable var, 
            Tree.SpecifierExpression se) {
        if (var.getType()!=null) {
            ProducedType vt = var.getType().getTypeModel();
            if (se!=null && se.getExpression()!=null) {
                ProducedType set = se.getExpression().getTypeModel();
                if (set!=null) {
                    checkAssignable(unit.getDefiniteType(set), vt, se, 
                            "specified expression must be assignable to declared type");
                }
            }
        }
    }

    private void checkEmptyOptionalType(Tree.Variable var, 
            Tree.SpecifierExpression se) {
        if (var.getType()!=null) {
            ProducedType vt = var.getType().getTypeModel();
            checkType(unit.getOptionalType(unit.getPossiblyNoneType(vt)), se);
        }
    }

    private void checkContainedType(Tree.Variable var, 
            Tree.SpecifierExpression se) {
        if (var.getType()!=null) {
            ProducedType vt = var.getType().getTypeModel();
            checkType(unit.getIterableType(vt), se);
        }
    }

    private void checkKeyValueType(Tree.Variable key, Tree.Variable value, 
            Tree.SpecifierExpression se) {
        if (key.getType()!=null && value.getType()!=null) {
            ProducedType kt = key.getType().getTypeModel();
            ProducedType vt = value.getType().getTypeModel();
            checkType(unit.getIterableType(unit.getEntryType(kt, vt)), se);
        }
    }

    @Override public void visit(Tree.AttributeGetterDefinition that) {
        Tree.Type rt = beginReturnScope(that.getType());
        Declaration od = beginReturnDeclaration(that.getDeclarationModel());
        super.visit(that);
        endReturnScope(rt, that.getDeclarationModel());
        endReturnDeclaration(od);
        Setter setter = that.getDeclarationModel().getSetter();
        if (setter!=null) {
            setter.getParameter().setType(that.getDeclarationModel().getType());
        }
    }

    @Override public void visit(Tree.AttributeArgument that) {
        Tree.Type rt = beginReturnScope(that.getType());
        Declaration od = beginReturnDeclaration(that.getDeclarationModel());
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, that.getDeclarationModel());
    }

    @Override public void visit(Tree.AttributeSetterDefinition that) {
        Tree.Type rt = beginReturnScope(that.getType());
        Declaration od = beginReturnDeclaration(that.getDeclarationModel());
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, that.getDeclarationModel());
    }

    @Override public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        Tree.SpecifierExpression se = that.getSpecifierExpression();
        if (se!=null) {
            Tree.Expression e = se.getExpression();
            if (e!=null) {
                ProducedType et = e.getTypeModel();
                for (Tree.ParameterList pl: that.getParameterLists()) {
                    if (checkCallable(et, se, "specified value must be a reference to a function or class")){
                        /*if (e.getTerm() instanceof MemberOrTypeExpression) {
                            Declaration d = ((MemberOrTypeExpression) e.getTerm()).getDeclaration();
                            if (d instanceof Class && ((Class) d).isAbstract()) {
                                se.addError("specified reference is to an abstract class: " + d.getName());
                            }
                        }*/
                        if (pl.getParameters().size()+1==et.getTypeArgumentList().size()) {
                            int i=0;
                            for (Tree.Parameter p: pl.getParameters()) {
                                if (p!=null) {
                                    i++;
                                    ProducedType rt = et.getTypeArgumentList().get(i);
                                    ProducedType pt = p.getDeclarationModel().getProducedTypedReference(null, 
                                            Collections.<ProducedType>emptyList()).getFullType();
                                    checkIsExactly(rt, pt, p.getType(), 
                                            "specified reference parameter type must be exactly the same as declared type of parameter " + 
                                            p.getDeclarationModel().getName());
                                }
                            }
                        }
                        else {
                            se.addError("specified reference must have the same number of parameters");
                        }
                        et = et.getTypeArgumentList().get(0);
                    }
                    else {
                        return; //Note: early exit!
                    }
                }
                inferFunctionType(that, et);
                if (that.getType()!=null) {
                    checkFunctionType(et, that.getType());
                }
            }
        }
    }

    @Override public void visit(Tree.MethodDefinition that) {
        Tree.Type rt = beginReturnScope(that.getType());           
        Declaration od = beginReturnDeclaration(that.getDeclarationModel());
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, that.getDeclarationModel());
    }

    @Override public void visit(Tree.MethodArgument that) {
        Tree.Type rt = beginReturnScope(that.getType());           
        Declaration od = beginReturnDeclaration(that.getDeclarationModel());
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, that.getDeclarationModel());
    }

    @Override public void visit(Tree.ClassDefinition that) {
        Tree.Type rt = beginReturnScope(new Tree.VoidModifier(that.getToken()));
        Declaration od = beginReturnDeclaration(that.getDeclarationModel());
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
        Class c = that.getDeclarationModel();
        if (!c.isAbstract()) {
            validateEnumeratedSupertypes(that, c);
        }
    }
    
    @Override public void visit(Tree.ClassOrInterface that) {
        super.visit(that);
        validateEnumeratedSupertypeArguments(that, that.getDeclarationModel());
    }

    @Override public void visit(Tree.InterfaceDefinition that) {
        Tree.Type rt = beginReturnScope(null);
        Declaration od = beginReturnDeclaration(that.getDeclarationModel());
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
    }

    @Override public void visit(Tree.ObjectDefinition that) {
        Tree.Type rt = beginReturnScope(new Tree.VoidModifier(that.getToken()));
        Declaration od = beginReturnDeclaration(that.getDeclarationModel());
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
        validateEnumeratedSupertypes(that, that.getAnonymousClass());
    }

    @Override public void visit(Tree.ObjectArgument that) {
        Tree.Type rt = beginReturnScope(new Tree.VoidModifier(that.getToken()));
        Declaration od = beginReturnDeclaration(that.getDeclarationModel());
        super.visit(that);
        endReturnDeclaration(od);
        endReturnScope(rt, null);
        validateEnumeratedSupertypes(that, that.getAnonymousClass());
    }

    @Override public void visit(Tree.ClassDeclaration that) {
        super.visit(that);
        Class alias = that.getDeclarationModel();
        Class c = alias.getExtendedTypeDeclaration();
        if (c!=null) {
            //that.getTypeSpecifier().getType().get
            ProducedType at = alias.getExtendedType();
            ParameterList pl = c.getParameterList();
            ParameterList apl = alias.getParameterList();
            if (pl!=null&&apl!=null) {
                int cps = pl.getParameters().size();
                int aps = apl.getParameters().size();
                if (cps!=aps) {
                    that.addError("wrong number of initializer parameters declared by class alias: " + 
                            alias.getName());
                }
                for (int i=0; i<(cps<=aps ? cps : aps); i++) {
                    Parameter ap = apl.getParameters().get(i);
                    Parameter cp = pl.getParameters().get(i);
                    ProducedType pt = at.getTypedParameter(cp).getType();
                    ap.setAliasedParameter(cp);
                    //TODO: properly check type of functional parameters!!
                    checkAssignable(ap.getType(), pt, that, "alias parameter " + 
                            ap.getName() + " must be assignable to corresponding class parameter " +
                            cp.getName());
                }
            }
        }
    }
    
    private void inferType(Tree.TypedDeclaration that, 
            Tree.SpecifierOrInitializerExpression spec) {
        if (that.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) that.getType();
            if (spec!=null) {
                setType(local, spec, that);
            }
            else {
//                local.addError("could not infer type of: " + 
//                        name(that.getIdentifier()));
            }
        }
    }

    private void inferFunctionType(Tree.TypedDeclaration that, ProducedType et) {
        if (that.getType() instanceof Tree.FunctionModifier) {
            Tree.FunctionModifier local = (Tree.FunctionModifier) that.getType();
            if (et!=null) {
                setFunctionType(local, et, that);
            }
        }
    }
    
    private void inferDefiniteType(Tree.Variable that, 
            Tree.SpecifierExpression se) {
        if (that.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) that.getType();
            if (se!=null) {
                setTypeFromOptionalType(local, se, that);
            }
            else {
//                local.addError("could not infer type of: " + 
//                        name(that.getIdentifier()));
            }
        }
    }

    private void inferNonemptyType(Tree.Variable that, 
            Tree.SpecifierExpression se) {
        if (that.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) that.getType();
            if (se!=null) {
                setTypeFromEmptyType(local, se, that);
            }
            else {
//                local.addError("could not infer type of: " + 
//                        name(that.getIdentifier()));
            }
        }
    }

    private void inferContainedType(Tree.Variable that, 
            Tree.SpecifierExpression se) {
        if (that.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) that.getType();
            if (se!=null) {
                setTypeFromIterableType(local, se, that);
            }
            else {
//                local.addError("could not infer type of: " + 
//                        name(that.getIdentifier()));
            }
        }
    }

    private void inferKeyType(Tree.Variable key, 
            Tree.SpecifierExpression se) {
        if (key.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) key.getType();
            if (se!=null) {
                setTypeFromKeyType(local, se, key);
            }
            else {
//                local.addError("could not infer type of key: " + 
//                        name(key.getIdentifier()));
            }
        }
    }

    private void inferValueType(Tree.Variable value, 
            Tree.SpecifierExpression se) {
        if (value.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) value.getType();
            if (se!=null) {
                setTypeFromValueType(local, se, value);
            }
            else {
//                local.addError("could not infer type of value: " + 
//                        name(value.getIdentifier()));
            }
        }
    }
    
    private void setTypeFromOptionalType(Tree.LocalModifier local, 
            Tree.SpecifierExpression se, Tree.Variable that) {
        ProducedType expressionType = se.getExpression().getTypeModel();
        if (expressionType!=null) {
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
//        local.addError("could not infer type of: " + 
//                name(that.getIdentifier()));
    }
    
    private void setTypeFromEmptyType(Tree.LocalModifier local, 
            Tree.SpecifierExpression se, Tree.Variable that) {
        ProducedType expressionType = se.getExpression().getTypeModel();
        if (expressionType!=null) {
            ProducedType t;
            if (unit.isEmptyType(expressionType)) {
                t = unit.getNonemptyDefiniteType(expressionType);
            }
            else {
                t = expressionType;
            }
            local.setTypeModel(t);
            that.getDeclarationModel().setType(t);
        }
//        local.addError("could not infer type of: " + 
//                name(that.getIdentifier()));
    }
    
    private void setTypeFromIterableType(Tree.LocalModifier local, 
            Tree.SpecifierExpression se, Tree.Variable that) {
        if (se.getExpression()!=null) {
            ProducedType expressionType = se.getExpression().getTypeModel();
            if (expressionType!=null) {
                if (unit.isIterableType(expressionType)) {
                    ProducedType t = unit.getIteratedType(expressionType);
                    local.setTypeModel(t);
                    that.getDeclarationModel().setType(t);
                    return;
                }
            }
        }
//        local.addError("could not infer type of: " + 
//                name(that.getIdentifier()));
    }
    
    private void setTypeFromKeyType(Tree.LocalModifier local,
            Tree.SpecifierExpression se, Tree.Variable that) {
        if (se.getExpression()!=null) {
            ProducedType expressionType = se.getExpression().getTypeModel();
            if (expressionType!=null) {
                if (unit.isIterableType(expressionType)) {
                    ProducedType entryType = unit.getIteratedType(expressionType);
                    if (entryType!=null) {
                        if (unit.isEntryType(entryType)) {
                            ProducedType et = unit.getKeyType(entryType);
                            local.setTypeModel(et);
                            that.getDeclarationModel().setType(et);
                            return;
                        }
                    }
                }
            }
        }
//        local.addError("could not infer type of: " + 
//                name(that.getIdentifier()));
    }
    
    private void setTypeFromValueType(Tree.LocalModifier local,
            Tree.SpecifierExpression se, Tree.Variable that) {
        if (se.getExpression()!=null) {
            ProducedType expressionType = se.getExpression().getTypeModel();
            if (expressionType!=null) {
                if (unit.isIterableType(expressionType)) {
                    ProducedType entryType = unit.getIteratedType(expressionType);
                    if (entryType!=null) {
                        if (unit.isEntryType(entryType)) {
                            ProducedType et = unit.getValueType(entryType);
                            local.setTypeModel(et);
                            that.getDeclarationModel().setType(et);
                            return;
                        }
                    }
                }
            }
        }
//        local.addError("could not infer type of: " + 
//                name(that.getIdentifier()));
    }
    
    private void setType(Tree.LocalModifier local, 
            Tree.SpecifierOrInitializerExpression s, 
            Tree.TypedDeclaration that) {
        if (s.getExpression()!=null) {
            ProducedType t = unit.denotableType(s.getExpression().getTypeModel());
            local.setTypeModel(t);
            that.getDeclarationModel().setType(t);
        }
    }
        
    private void setFunctionType(Tree.FunctionModifier local, 
            ProducedType et, Tree.TypedDeclaration that) {
        ProducedType t = unit.denotableType(et);
        local.setTypeModel(t);
        that.getDeclarationModel().setType(t);
    }
        
    @Override public void visit(Tree.Throw that) {
        super.visit(that);
        if (that.getExpression()!=null) {
            checkAssignable(that.getExpression().getTypeModel(),
                    unit.getExceptionDeclaration().getType(), 
                    that.getExpression(),
                    "thrown expression must be an exception");
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
            if (e==null) {
                if (!(returnType instanceof Tree.VoidModifier)) {
                    that.addError("a non-void method or getter must return a value: " +
                            returnDeclaration.getName());
                }
            }
            else {
                ProducedType et = returnType.getTypeModel();
                ProducedType at = e.getTypeModel();
                if (returnType instanceof Tree.VoidModifier) {
                    that.addError("a void method, setter, or class initializer may not return a value: " +
                            returnDeclaration.getName());
                }
                else if (returnType instanceof Tree.LocalModifier) {
                    inferReturnType(et, at);
                }
                else {
                    checkAssignable(at, et, that.getExpression(), 
                            "returned expression must be assignable to return type of " +
                            returnDeclaration.getName(), 2100);
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
                    List<ProducedType> list = new ArrayList<ProducedType>();
                    addToUnion(list, et);
                    addToUnion(list, at);
                    ut.setCaseTypes(list);
                    returnType.setTypeModel( ut.getType() );
                }
            }
        }
    }
    
    /*@Override public void visit(Tree.OuterExpression that) {
        that.getPrimary().visit(this);
        ProducedType pt = that.getPrimary().getTypeModel();
        if (pt!=null) {
            if (pt.getDeclaration() instanceof ClassOrInterface) {
                that.setTypeModel(getOuterType(that, (ClassOrInterface) pt.getDeclaration()));
                //TODO: some kind of MemberReference
            }
            else {
                that.addError("can't use outer on a type parameter");
            }
        }
    }*/

    ProducedType unwrap(ProducedType pt, Tree.QualifiedMemberOrTypeExpression mte) {
        ProducedType result;
        Tree.MemberOperator op = mte.getMemberOperator();
        if (op instanceof Tree.SafeMemberOp)  {
            if (unit.isOptionalType(pt)) {
                result = unit.getDefiniteType(pt);
            }
            else {
                mte.getPrimary().addError("receiver not of optional type");
                result = pt;
            }
        }
        else if (op instanceof Tree.SpreadOp) {
            ProducedType st = unit.getNonemptySequenceType(pt);
            if (st==null) {
                mte.getPrimary().addError("receiver not of type: Sequence");
                result = pt;
            }
            else {
                result = unit.getElementType(pt);
            }
        }
        else {
            result = pt;
        }
        if (result==null) {
            result = new UnknownType(mte.getUnit()).getType();
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
            ProducedType st = unit.getSequenceType(pt);
            //note: the following is nice, even though
            //      it is not actually blessed by the
            //      language spec!
            return unit.isEmptyType(receivingType) ?
                    unit.getEmptyType(st) : st;
        }
        else {
            return pt;
        }
    }
    
    @Override public void visit(Tree.InvocationExpression that) {
        
        boolean isDirectInvocation = that.getPrimary() instanceof Tree.MemberOrTypeExpression;
        if (isDirectInvocation) {
            ((Tree.MemberOrTypeExpression) that.getPrimary()).setDirectlyInvoked(true);
        }
        
        if (that.getPositionalArgumentList()!=null) {
            that.getPositionalArgumentList().visit(this);
            if (isDirectInvocation) {
                //set up the "signature" on the primary
                //so that we can resolve the the right 
                //overloaded declaration
                List<ProducedType> sig = new ArrayList<ProducedType>();
                for (Tree.PositionalArgument pa: that.getPositionalArgumentList()
                        .getPositionalArguments()) {
                    sig.add(pa.getExpression().getTypeModel());
                }
                ((Tree.MemberOrTypeExpression) that.getPrimary()).setSignature(sig);
            }
        }
        
        if (that.getNamedArgumentList()!=null) {
            that.getNamedArgumentList().visit(this);
        }
        
        that.getPrimary().visit(this);
        
        visitInvocation(that);
    }

    private void visitInvocation(Tree.InvocationExpression that) {
        Tree.Primary pr = that.getPrimary();
        if (pr==null) {
            that.addError("malformed invocation expression");
        }
        else if (pr instanceof Tree.StaticMemberOrTypeExpression) {
            Tree.StaticMemberOrTypeExpression mte = (Tree.StaticMemberOrTypeExpression) pr;
            Declaration dec = mte.getDeclaration();
            if ( mte.getTarget()==null && dec instanceof Functional && 
                    mte.getTypeArguments() instanceof Tree.InferredTypeArguments ) {
                List<ProducedType> typeArgs = getInferedTypeArguments(that, (Functional) dec);
                mte.getTypeArguments().setTypeModels(typeArgs);
                if (pr instanceof Tree.BaseTypeExpression) {
                    visitBaseTypeExpression((Tree.BaseTypeExpression) pr, 
                            (TypeDeclaration) dec, typeArgs, mte.getTypeArguments());
                }
                else if (pr instanceof Tree.QualifiedTypeExpression) {
                    visitQualifiedTypeExpression((Tree.QualifiedTypeExpression) pr, 
                            ((Tree.QualifiedTypeExpression) pr).getPrimary().getTypeModel(),
                            (TypeDeclaration) dec, typeArgs, mte.getTypeArguments());
                }
                else if (pr instanceof Tree.BaseMemberExpression) {
                    visitBaseMemberExpression((Tree.BaseMemberExpression) pr, 
                            (TypedDeclaration) dec, typeArgs, mte.getTypeArguments());
                }
                else if (pr instanceof Tree.QualifiedMemberExpression) {
                    visitQualifiedMemberExpression((Tree.QualifiedMemberExpression) pr, 
                            (TypedDeclaration) dec, typeArgs, mte.getTypeArguments());
                }
            }
            visitInvocation(that, mte.getTarget());
        }
        else if (pr instanceof Tree.ExtendedTypeExpression) {
            visitInvocation(that, ((Tree.ExtendedTypeExpression) pr).getTarget());
        }
        else {
            visitInvocation(that, null);
        }
    }

    private List<ProducedType> getInferedTypeArguments(Tree.InvocationExpression that, 
            Functional dec) {
        List<ProducedType> typeArgs = new ArrayList<ProducedType>();
        if (!dec.getParameterLists().isEmpty()) {
            ParameterList parameters = dec.getParameterLists().get(0);
            for (TypeParameter tp: dec.getTypeParameters()) {
                if (!tp.isSequenced()) {
                    typeArgs.add(constrainInferredType(dec, tp, 
                            inferTypeArgument(that, tp, parameters)));
                }
            }
        }
        return typeArgs;
    }

    private ProducedType constrainInferredType(Functional dec,
            TypeParameter tp, ProducedType ta) {
        List<ProducedType> list = new ArrayList<ProducedType>();
        addToIntersection(list, ta, unit);
        //Intersect the inferred type with any 
        //upper bound constraints on the type. This
        //helps with cases like passing an arg of
        //type String? to a parameter of type T?
        for (ProducedType st: tp.getSatisfiedTypes()) {
            //TODO: st.getProducedType(receiver, dec, typeArgs);
            if (//if the upper bound is a type parameter, ignore it
                !dec.getTypeParameters().contains(st.getDeclaration()) &&
                (st.getQualifyingType()==null ||
                !dec.getTypeParameters().contains(st.getQualifyingType().getDeclaration())) &&
                //TODO: remove this awful hack that 
                //tries to work around the possibility 
                //that a type parameter appears in the 
                //upper bound!
                !st.getDeclaration().isParameterized() &&
                (st.getQualifyingType()==null || 
                !st.getQualifyingType().getDeclaration().isParameterized())) {
                addToIntersection(list, st, unit);
            }
        }
        IntersectionType it = new IntersectionType(unit);
        it.setSatisfiedTypes(list);
        ProducedType type = it.canonicalize().getType();
        return type;
    }

    private ProducedType inferTypeArgument(Tree.InvocationExpression that,
            TypeParameter tp, ParameterList parameters) {
        List<ProducedType> inferredTypes = new ArrayList<ProducedType>();
        if (that.getPositionalArgumentList()!=null) {
            inferTypeArgument(tp, parameters, that.getPositionalArgumentList(), inferredTypes);
        }
        else if (that.getNamedArgumentList()!=null) {
            inferTypeArgument(tp, parameters, that.getNamedArgumentList(), inferredTypes);
        }
        UnionType ut = new UnionType(unit);
        ut.setCaseTypes(inferredTypes);
        return ut.getType();
    }

    private void inferTypeArgument(TypeParameter tp, ParameterList parameters,
            Tree.NamedArgumentList args, List<ProducedType> inferredTypes) {
        for (Tree.NamedArgument arg: args.getNamedArguments()) {
            ProducedType type = null;
            if (arg instanceof Tree.SpecifiedArgument) {
                Tree.Expression e = ((Tree.SpecifiedArgument) arg).getSpecifierExpression()
                                .getExpression();
                if (e!=null) {
                    type = e.getTypeModel();
                }
            }
            else if (arg instanceof Tree.TypedArgument) {
                //TODO: broken for method args
                type = ((Tree.TypedArgument) arg).getType().getTypeModel();
            }
            if (type!=null) {
                Parameter parameter = getMatchingParameter(parameters, arg);
                if (parameter!=null) {
                    addToUnion(inferredTypes, inferTypeArg(tp, parameter.getType(), 
                            type, new ArrayList<TypeParameter>()));
                }
            }
            //inferTypeArg(arg, tp, parameters, inferredTypes);
        }
        Tree.SequencedArgument sa = args.getSequencedArgument();
        if (sa!=null) {
            inferTypeArg(sa, tp, parameters, inferredTypes);
        }    
        Tree.Comprehension ch = args.getComprehension();
        if (ch!=null) {
            inferTypeArg(ch, tp, parameters, inferredTypes);
        }
    }

    private void inferTypeArg(Tree.Comprehension ch, TypeParameter tp,
            ParameterList parameters, List<ProducedType> inferredTypes) {
        Parameter sp = getSequencedParameter(parameters);
        if (sp!=null) {
            ProducedType ct = ch.getForComprehensionClause().getTypeModel();
            if (ct!=null) {
                ProducedType spt = unit.getIteratedType(sp.getType());
                addToUnion(inferredTypes, inferTypeArg(tp, spt, ct,
                        new ArrayList<TypeParameter>()));
            }
        }
    }

    private void inferTypeArg(Tree.SequencedArgument sa, TypeParameter tp,
            ParameterList parameters, List<ProducedType> inferredTypes) {
        Parameter sp = getSequencedParameter(parameters);
        if (sp!=null) {
            ProducedType spt = sa.getEllipsis()==null ? 
                    unit.getIteratedType(sp.getType()) :
                    sp.getType();
            for (Tree.Expression e: sa.getExpressionList().getExpressions()) {
                ProducedType sat = e.getTypeModel();
                if (sat!=null) {
                    addToUnion(inferredTypes, inferTypeArg(tp, spt, sat,
                            new ArrayList<TypeParameter>()));
                }
            }
        }
    }

    private void inferTypeArg(Tree.NamedArgument arg, TypeParameter tp,
            ParameterList parameters, List<ProducedType> inferredTypes) {
        ProducedType type = null;
        if (arg instanceof Tree.SpecifiedArgument) {
            type = ((Tree.SpecifiedArgument) arg).getSpecifierExpression()
                            .getExpression().getTypeModel();
        }
        else if (arg instanceof Tree.TypedArgument) {
            //TODO: broken for method args
            type = ((Tree.TypedArgument) arg).getType().getTypeModel();
        }
        if (type!=null) {
            Parameter parameter = getMatchingParameter(parameters, arg);
            if (parameter!=null) {
                addToUnion(inferredTypes, inferTypeArg(tp, parameter.getType(), 
                        type, new ArrayList<TypeParameter>()));
            }
        }
    }

    private void inferTypeArgument(TypeParameter tp, ParameterList parameters,
            Tree.PositionalArgumentList args, List<ProducedType> inferredTypes) {
        for (int i=0; i<parameters.getParameters().size(); i++) {
            Parameter parameter = parameters.getParameters().get(i);
            if (args.getPositionalArguments().size()>i) {
                if (parameter.isSequenced() && args.getEllipsis()==null) {
                    ProducedType spt = unit.getIteratedType(parameter.getType());
                    for (int k=i; k<args.getPositionalArguments().size(); k++) {
                        ProducedType sat = args.getPositionalArguments().get(k)
                                .getExpression().getTypeModel();
                        if (sat!=null) {
                            addToUnion(inferredTypes, inferTypeArg(tp, spt, sat,
                                    new ArrayList<TypeParameter>()));
                        }
                    }
                    break;
                }
                else {
                    Tree.PositionalArgument a = args.getPositionalArguments().get(i);
                    if (a.getExpression()!=null) {
                        ProducedType pt;
                        if (parameter instanceof FunctionalParameter) {
                            //TODO: this is very incomplete!
                            List<ProducedType> list = new ArrayList<ProducedType>();
                            list.add(parameter.getType());
                            for (Parameter p: ((FunctionalParameter) parameter).getParameterLists()
                                    .get(0).getParameters()) {
                                list.add(p.getType());
                            }
                            pt = unit.getCallableDeclaration().getProducedType(null, list);
                        }
                        else {
                            pt = parameter.getType();
                        }

                        addToUnion(inferredTypes, inferTypeArg(tp, pt, 
                                getPositionalArgumentType(a), 
                                new ArrayList<TypeParameter>()));
                    }
                }
            }
        }
        
        Tree.Comprehension ch = args.getComprehension();
        if (ch!=null) {
            inferTypeArg(ch, tp, parameters, inferredTypes);
        }

    }
    
    private ProducedType union(List<ProducedType> types) {
        if (types.isEmpty()) {
            return null;
        }
        UnionType ut = new UnionType(unit);
        ut.setCaseTypes(types);
        return ut.getType();
    }
    
    private ProducedType intersection(List<ProducedType> types) {
        if (types.isEmpty()) {
            return null;
        }
        IntersectionType it = new IntersectionType(unit);
        it.setSatisfiedTypes(types);
        return it.canonicalize().getType();
    }
    
    private ProducedType inferTypeArg(TypeParameter tp, ProducedType paramType,
            ProducedType argType, List<TypeParameter> visited) {
        if (paramType!=null) {
            if (paramType.getDeclaration() instanceof TypeParameter &&
                    paramType.getDeclaration().equals(tp)) {
                return unit.denotableType(argType);
            }
            else if (paramType.getDeclaration() instanceof UnionType) {
                List<ProducedType> list = new ArrayList<ProducedType>();
                for (ProducedType ct: paramType.getDeclaration().getCaseTypes()) {
                    addToIntersection(list, inferTypeArg(tp, 
                            ct.substitute(paramType.getTypeArguments()), 
                            argType, visited), unit);
                }
                return intersection(list);
            }
            else if (paramType.getDeclaration() instanceof IntersectionType) {
                List<ProducedType> list = new ArrayList<ProducedType>();
                for (ProducedType ct: paramType.getDeclaration().getSatisfiedTypes()) {
                    addToUnion(list, inferTypeArg(tp, 
                            ct.substitute(paramType.getTypeArguments()), 
                            argType, visited));
                }
                return union(list);
            }
            else if (argType.getDeclaration() instanceof UnionType) {
                List<ProducedType> list = new ArrayList<ProducedType>();
                for (ProducedType ct: argType.getDeclaration().getCaseTypes()) {
                    addToUnion(list, inferTypeArg(tp, paramType, 
                            ct.substitute(paramType.getTypeArguments()), 
                            visited));
                }
                return union(list);
            }
            else if (argType.getDeclaration() instanceof IntersectionType) {
                List<ProducedType> list = new ArrayList<ProducedType>();
                for (ProducedType ct: argType.getDeclaration().getSatisfiedTypes()) {
                    addToIntersection(list, inferTypeArg(tp, paramType, 
                            ct.substitute(paramType.getTypeArguments()), 
                            visited), unit);
                }
                return intersection(list);
            }
            else if (paramType.getDeclaration() instanceof TypeParameter) {
                TypeParameter tp2 = (TypeParameter) paramType.getDeclaration();
                if (!visited.contains(tp2)) {
                    visited.add(tp2);
                    List<ProducedType> list = new ArrayList<ProducedType>();
                    for (ProducedType pt: tp2.getSatisfiedTypes()) {
                        addToUnion(list, inferTypeArg(tp, pt, argType, visited) );
                        ProducedType st = argType.getSupertype(pt.getDeclaration());
                        if (st!=null) {
                            for (int j=0; j<pt.getTypeArgumentList().size(); j++) {
                                if (st.getTypeArgumentList().size()>j) {
                                    addToUnion(list, inferTypeArg(tp, 
                                            pt.getTypeArgumentList().get(j), 
                                            st.getTypeArgumentList().get(j), 
                                            visited));
                                }
                            }
                        }
                    }
                    return union(list);
                }
                else {
                    return null;
                }
            }
            else {
                ProducedType st = argType.getSupertype(paramType.getDeclaration());
                if (st!=null) {
                    List<ProducedType> list = new ArrayList<ProducedType>();
                    for (int j=0; j<paramType.getTypeArgumentList().size(); j++) {
                        if (st.getTypeArgumentList().size()>j) {
                            addToUnion(list, inferTypeArg(tp, 
                                    paramType.getTypeArgumentList().get(j), 
                                    st.getTypeArgumentList().get(j), 
                                    visited));
                        }
                    }
                    return union(list);
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

    /*@Override public void visit(Tree.ExtendedType that) {
        super.visit(that);
        Tree.Primary pr = that.getTypeExpression();
        Tree.PositionalArgumentList pal = that.getPositionalArgumentList();
        if (pr==null || pal==null) {
            that.addError("malformed expression");
        }
        else {
            visitInvocation(pal, null, that, pr);
        }
    }*/

    private void visitInvocation(Tree.InvocationExpression that, ProducedReference prf) {
        if (prf==null || !prf.isFunctional()) {
            ProducedType pt = that.getPrimary().getTypeModel();
            if (pt!=null) {
                if (checkCallable(pt, that.getPrimary(), 
                        "invoked expression must be callable")) {
                    List<ProducedType> typeArgs = pt.getTypeArgumentList();
                    if (!typeArgs.isEmpty()) {
                        that.setTypeModel(typeArgs.get(0));
                    }
                    //typecheck arguments using the type args of Callable
                    checkIndirectInvocationArguments(that, typeArgs);
                }
            }
        }
        else {
            Tree.MemberOrTypeExpression mte = (Tree.MemberOrTypeExpression) that.getPrimary();
            Functional dec = (Functional) mte.getDeclaration();
            if (!(that.getPrimary() instanceof Tree.ExtendedTypeExpression)) {
                if (dec instanceof Class && ((Class) dec).isAbstract()) {
                    that.addError("abstract classes may not be instantiated");
                }
            }
            if (that.getNamedArgumentList()!=null && 
                    dec.isAbstraction()) {
                //TODO: this is not really right - it's the fact 
                //      that we're calling Java and don't have
                //      meaningful parameter names that is the
                //      real problem, not the overload
                that.addError("overloaded declarations may not be called using named arguments: " +
                        dec.getName());
            }
            //that.setTypeModel(prf.getType());
            ProducedType ct = that.getPrimary().getTypeModel();
            if (ct!=null && !ct.getTypeArgumentList().isEmpty()) {
                //pull the return type out of the Callable
                that.setTypeModel(ct.getTypeArgumentList().get(0));
            }
            if (that.getNamedArgumentList() != null) {
                List<ParameterList> parameterLists = dec.getParameterLists();
                if(!parameterLists.isEmpty()
                        && !parameterLists.get(0).isNamedParametersSupported()) {
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

    private void checkIndirectInvocationArguments(
            Tree.InvocationExpression that, List<ProducedType> typeArgs) {
        if (that.getNamedArgumentList() != null) {
            that.addError("named arguments not supported for indirect invocations");
        }
        if (that.getPositionalArgumentList() != null) {
            if (that.getPositionalArgumentList().getEllipsis()!=null) {
                that.addError("sequenced arguments not supported for indirect invocations");
            }
            List<Tree.PositionalArgument> args = that.getPositionalArgumentList()
                    .getPositionalArguments();
            int argCount = args.size();
            int paramCount = typeArgs.size()-1;
            if (argCount<paramCount) {
                that.addError("not enough arguments: " + 
                         paramCount + " arguments required");
            }
            if (argCount>paramCount) {
                that.addError("too many arguments: " + 
                         paramCount + " arguments required");
            }
            for (int i=0; i<paramCount && i<argCount; i++) {
                Tree.PositionalArgument arg = args.get(i);
                checkAssignable(getPositionalArgumentType(arg), 
                        typeArgs.get(i+1), arg, 
                        "argument must be assignable to parameter type");
            }
        }
    }

    private void checkInvocationArguments(Tree.InvocationExpression that,
            ProducedReference prf, Functional dec) {
        List<ParameterList> pls = dec.getParameterLists();
        if (pls.isEmpty()) {
            if (dec instanceof TypeDeclaration) {
                that.addError("type cannot be instantiated: " + 
                        dec.getName() + " (or return statement is missing)");
            }
            else {
                that.addError("member cannot be invoked: " +
                        dec.getName());
            }
        }
        else /*if (!dec.isOverloaded())*/ {
            ParameterList pl = pls.get(0);            
            if ( that.getPositionalArgumentList()!=null ) {
                checkPositionalArguments(pl, prf, that.getPositionalArgumentList());
            }
            if ( that.getNamedArgumentList()!=null ) {
                if(pl.isNamedParametersSupported()) {
                    that.getNamedArgumentList().getNamedArgumentList().setParameterList(pl);
                    checkNamedArguments(pl, prf, that.getNamedArgumentList());
                }
            }
        }
    }

    private void checkNamedArguments(ParameterList pl, ProducedReference pr, 
            Tree.NamedArgumentList nal) {
        List<Tree.NamedArgument> na = nal.getNamedArguments();        
        Set<Parameter> foundParameters = new HashSet<Parameter>();
        
        for (Tree.NamedArgument a: na) {
            checkNamedArg(a, pl, pr, foundParameters);
        }
        
        Tree.SequencedArgument sa = nal.getSequencedArgument();
        if (sa!=null) {
            checkNamedArg(sa, pl, pr, foundParameters);
        }
        
        Tree.Comprehension ch = nal.getComprehension();
        if (ch!=null) {
            checkNamedArg(ch, pl, pr, foundParameters);
        }
            
        for (Parameter p: pl.getParameters()) {
            if (!foundParameters.contains(p) && 
                    !p.isDefaulted() && !p.isSequenced()) {
                nal.addError("missing named argument to parameter " + 
                        p.getName() + " of " + pr.getDeclaration().getName());
            }
        }
    }

    private void checkNamedArg(Tree.Comprehension ch, ParameterList pl,
            ProducedReference pr, Set<Parameter> foundParameters) {
        Parameter sp = getSequencedParameter(pl);
        if (sp==null) {
            ch.addError("no matching sequenced parameter declared by "
                     + pr.getDeclaration().getName());
        }
        else {
            if (!foundParameters.add(sp)) {
                ch.addError("duplicate argument for parameter: " +
                        sp.getName());
            }
            checkComprehensionArgument(ch, pr, sp);
        }
    }

    private void checkNamedArg(Tree.SequencedArgument sa, ParameterList pl,
            ProducedReference pr, Set<Parameter> foundParameters) {
        Parameter sp = getSequencedParameter(pl);
        if (sp==null) {
            sa.addError("no matching sequenced parameter declared by "
                     + pr.getDeclaration().getName());
        }
        else {
            if (!foundParameters.add(sp)) {
                sa.addError("duplicate argument for parameter: " +
                        sp.getName());
            }
            checkSequencedArgument(sa, pr, sp);
        }
    }

    private void checkNamedArg(Tree.NamedArgument a, ParameterList pl,
            ProducedReference pr, Set<Parameter> foundParameters) {
        Parameter p = getMatchingParameter(pl, a);
        if (p==null) {
            a.addError("no matching parameter for named argument " + 
                    name(a.getIdentifier()) + " declared by " + 
                    pr.getDeclaration().getName(), 101);
        }
        else {
            if (!foundParameters.add(p)) {
                a.addError("duplicate argument for parameter: " +
                        p.getName());
            }
            checkNamedArgument(a, pr, p);
        }
    }

    private void checkNamedArgument(Tree.NamedArgument a, ProducedReference pr, 
            Parameter p) {
        a.setParameter(p);
        ProducedType argType = null;
        if (a instanceof Tree.SpecifiedArgument) {
            Tree.SpecifiedArgument sa = (Tree.SpecifiedArgument) a;
            Tree.Expression e = sa.getSpecifierExpression().getExpression();
            if (e!=null) {
                argType = e.getTypeModel();
            }
        }
        else if (a instanceof Tree.TypedArgument) {
            Tree.TypedArgument ta = (Tree.TypedArgument) a;
            argType = ta.getDeclarationModel().getProducedTypedReference(null,
                     //assuming an argument can't have type params 
                    Collections.<ProducedType>emptyList()).getFullType();
            //argType = ta.getType().getTypeModel();
        }
        checkAssignable(argType, pr.getTypedParameter(p.getAliasedParameter()).getFullType(), 
                a, "named argument must be assignable to parameter " + 
                p.getName() + " of " + pr.getDeclaration().getName(), 
                2100);
    }
    
    private void checkSequencedArgument(Tree.SequencedArgument a, ProducedReference pr, 
            Parameter p) {
        a.setParameter(p);
        List<Tree.Expression> es = a.getExpressionList().getExpressions();
        Tree.Ellipsis ell = a.getEllipsis();
        ProducedType paramType = pr.getTypedParameter(p.getAliasedParameter())
                .getFullType();
        if (ell==null) {
            for (Tree.Expression e: es) {
                if (paramType==null) {
                    paramType = new UnknownType(a.getUnit()).getType();
                }
                checkAssignable(e.getTypeModel(), unit.getIteratedType(paramType), a, 
                        "sequenced argument must be assignable to sequenced parameter " + 
                        p.getName() + " of " + pr.getDeclaration().getName());
            }
        }
        else {
            if (es.size()>1) {
                ell.addError("more than one argument");
            }
            Tree.Expression e = es.get(0);
            checkAssignable(e.getTypeModel(), paramType, a, 
                    "sequenced argument must be assignable to sequenced parameter " + 
                    p.getName() + " of " + pr.getDeclaration().getName());
        }
    }
    
    private void checkComprehensionArgument(Tree.Comprehension ch, ProducedReference pr, 
            Parameter p) {
        //a.setParameter(p); //TODO!!!!!!
        ProducedType paramType = pr.getTypedParameter(p.getAliasedParameter())
                .getFullType();
        if (paramType==null) {
            paramType = new UnknownType(ch.getUnit()).getType();
        }
        ProducedType ct = ch.getForComprehensionClause().getTypeModel();
        checkAssignable(ct, unit.getIteratedType(paramType), ch, 
                "sequenced argument must be assignable to sequenced parameter " + 
                p.getName() + " of " + pr.getDeclaration().getName());
    }
    
    private Parameter getMatchingParameter(ParameterList pl, Tree.NamedArgument na) {
        String name = name(na.getIdentifier());
        for (Parameter p: pl.getParameters()) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    private Parameter getSequencedParameter(ParameterList pl) {
        int s = pl.getParameters().size();
        if (s==0) return null;
        Parameter p = pl.getParameters().get(s-1);
        if (p.isSequenced()) {
            return p;
        }
        else {
            return null;
        }
    }

    private void checkPositionalArguments(ParameterList pl, ProducedReference pr, 
            Tree.PositionalArgumentList pal) {
        List<Tree.PositionalArgument> args = pal.getPositionalArguments();
        List<Parameter> params = pl.getParameters();
        Tree.Ellipsis ell = pal.getEllipsis();
        for (int i=0; i<params.size(); i++) {
            Parameter p = params.get(i);
            if (i>=args.size()) {
                if (!p.isDefaulted() && !p.isSequenced()) {
                    pal.addError("missing argument to parameter " + 
                            p.getName() + " of " + pr.getDeclaration().getName());
                }
                if (p.isSequenced() && ell!=null) {
                    pal.addError("missing argument to sequenced parameter " + 
                            p.getName() + " of " + pr.getDeclaration().getName());
                }
            } 
            else {
                ProducedType paramType = pr.getTypedParameter(p.getAliasedParameter())
                        .getFullType();
                if (p.isSequenced() && ell==null) {
                    checkSequencedPositionalArgument(p, pr, pal, i, paramType);
                    if (pal.getComprehension()!=null) {
                        pal.getComprehension().addError("sequenced parameter already has arguments");
                    }
                    return; //Note: early return!
                }
                else {
                    checkPositionalArgument(p, pr, args.get(i), paramType);
                    if (p.isSequenced() && pal.getComprehension()!=null) {
                        pal.getComprehension().addError("sequenced parameter already has arguments");
                    }
                }
            }
        }
        for (int i=params.size(); i<args.size(); i++) {
            args.get(i).addError("no matching parameter for argument declared by " +
                     pr.getDeclaration().getName(), 2000);
        }

        Tree.Comprehension ch = pal.getComprehension();
        if (ch!=null) {
            Parameter p = getSequencedParameter(pl);
            if (p!=null) {
                ProducedType paramType = pr.getTypedParameter(p.getAliasedParameter())
                        .getFullType();
                checkComprehensionPositionalArgument(p, pr, ch, paramType);
            }
        }

        if ((ell!=null || ch!=null) && !pl.hasSequencedParameter()) {
            ell.addError("no matching sequenced parameter declared by " +
                     pr.getDeclaration().getName());
        }
        
    }

    private void checkComprehensionPositionalArgument(Parameter p, ProducedReference pr,
            Tree.Comprehension ch, ProducedType paramType) {
        ProducedType at = paramType==null ? null : 
                unit.getIteratedType(paramType);
        //a.setParameter(p); //TODO!!!!
        ProducedType ct = ch.getForComprehensionClause().getTypeModel();
        checkAssignable(ct, at, ch, 
                "argument must be assignable to sequenced parameter " + 
                p.getName()+ " of " + pr.getDeclaration().getName());
    }

    private void checkSequencedPositionalArgument(Parameter p, ProducedReference pr,
            Tree.PositionalArgumentList pal, int from, ProducedType paramType) {
        List<Tree.PositionalArgument> args = pal.getPositionalArguments();
        ProducedType at = paramType==null ? null : 
                unit.getIteratedType(paramType);
        for (int j=from; j<args.size(); j++) {
            Tree.PositionalArgument a = args.get(j);
            a.setParameter(p);
            Tree.Expression e = a.getExpression();
            if (e!=null) {
                checkAssignable(e.getTypeModel(), at, a, 
                        "argument must be assignable to sequenced parameter " + 
                        p.getName()+ " of " + pr.getDeclaration().getName());
            }
        }
    }

    private void checkPositionalArgument(Parameter p, ProducedReference pr,
            Tree.PositionalArgument a, ProducedType paramType) {
        a.setParameter(p);
        if (a.getExpression()!=null) {
            checkAssignable(getPositionalArgumentType(a), paramType, a, 
                    "argument must be assignable to parameter " + 
                    p.getName() + " of " + pr.getDeclaration().getName(), 
                    2100);
        }
    }

    private ProducedType getPositionalArgumentType(Tree.PositionalArgument a) {
        Tree.Expression e = a.getExpression();
        return e==null ? null : e.getTypeModel();
    }
        
    @Override public void visit(Tree.Annotation that) {
        super.visit(that);
        Declaration dec = ((Tree.MemberOrTypeExpression) that.getPrimary()).getDeclaration();
        if (dec!=null && !dec.isToplevel()) {
            that.getPrimary().addError("annotation must be a toplevel method reference");
        }
    }
    
    @Override public void visit(Tree.IndexExpression that) {
        super.visit(that);
        ProducedType pt = type(that);
        if (pt==null) {
            that.addError("could not determine type of receiver");
        }
        else {
            if (that.getIndexOperator() instanceof Tree.SafeIndexOp) {
                if (unit.isOptionalType(pt)) {
                    pt = unit.getDefiniteType(pt);
                }
                else {
                    that.getPrimary().addError("receiving type not of optional type: " +
                            pt.getDeclaration().getName() + " is not a subtype of Optional");
                }
            }
            if (that.getElementOrRange()==null) {
                that.addError("malformed index expression");
            }
            else {
                if (that.getElementOrRange() instanceof Tree.Element) {
                    ProducedType cst = pt.getSupertype(unit.getCorrespondenceDeclaration());
                    if (cst==null) {
                        that.getPrimary().addError("illegal receiving type for index expression: " +
                                pt.getDeclaration().getName() + " is not a subtype of Correspondence");
                    }
                    else {
                        List<ProducedType> args = cst.getTypeArgumentList();
                        ProducedType kt = args.get(0);
                        ProducedType vt = args.get(1);
                        Tree.Element e = (Tree.Element) that.getElementOrRange();
                        checkAssignable(e.getExpression().getTypeModel(), kt, 
                                e.getExpression(), 
                                "index must be assignable to key type");
                        ProducedType rt = unit.getOptionalType(vt);
                        that.setTypeModel(rt);
                    }
                }
                else {
                    ProducedType rst = pt.getSupertype(unit.getRangedDeclaration());
                    if (rst==null) {
                        that.getPrimary().addError("illegal receiving type for index range expression: " +
                                pt.getDeclaration().getName() + " is not a subtype of Ranged");
                    }
                    else {
                        List<ProducedType> args = rst.getTypeArgumentList();
                        ProducedType kt = args.get(0);
                        ProducedType rt = args.get(1);
                        Tree.ElementRange er = (Tree.ElementRange) that.getElementOrRange();
                        checkAssignable(er.getLowerBound().getTypeModel(), kt,
                                er.getLowerBound(), 
                                "lower bound must be assignable to index type");
                        if (er.getUpperBound()!=null) {
                            checkAssignable(er.getUpperBound().getTypeModel(), kt,
                                    er.getUpperBound(), 
                                    "upper bound must be assignable to index type");
                        }
                        if (er.getLength()!=null) {
                            checkAssignable(er.getLength().getTypeModel(), 
                                    unit.getIntegerDeclaration().getType(),
                                    er.getLength(), 
                                    "length must be an integer");
                        }
                        that.setTypeModel(rt);
                    }
                }
            }
        }
    }

    private ProducedType type(Tree.PostfixExpression that) {
        Tree.Primary p = that.getPrimary();
        return p==null ? null : p.getTypeModel();
    }
    
    @Override public void visit(Tree.PostfixOperatorExpression that) {
        super.visit(that);
        visitIncrementDecrement(that, type(that), that.getTerm());
        checkAssignability(that.getTerm(), that);
    }

    @Override public void visit(Tree.PrefixOperatorExpression that) {
        super.visit(that);
        visitIncrementDecrement(that, type(that), that.getTerm());
        checkAssignability(that.getTerm(), that);
    }
    
    private ProducedType checkOperandTypes(ProducedType lhst, ProducedType rhst, 
            TypeDeclaration td, Node node, String message) {
        ProducedType ut = unionType(lhst, rhst, unit);
        ProducedType st = producedType(td, ut);
        checkAssignable(lhst, st, node, message);
        checkAssignable(rhst, st, node, message);
        return ut.getSupertype(td);
    }

    private void visitIncrementDecrement(Tree.Term that,
            ProducedType pt, Tree.Term term) {
        if (pt!=null) {
            ProducedType ot = checkSupertype(pt, unit.getOrdinalDeclaration(), 
                    term, "operand expression must be of ordinal type");
            if (ot!=null) {
                ProducedType ta = ot.getTypeArgumentList().get(0);
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

    private void visitComparisonOperator(Tree.BinaryOperatorExpression that, 
            TypeDeclaration type) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            checkOperandTypes(lhst, rhst, type, that, 
                    "operand expressions must be comparable");
        }
        that.setTypeModel( unit.getBooleanDeclaration().getType() );            
    }
    
    private void visitCompareOperator(Tree.CompareOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            checkOperandTypes(lhst, rhst, unit.getComparableDeclaration(), that, 
                    "operand expressions must be comparable");
        }
        that.setTypeModel( unit.getComparisonDeclaration().getType() );            
    }
    
    private void visitRangeOperator(Tree.RangeOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            checkOperandTypes(lhst, rhst, unit.getOrdinalDeclaration(), that,
                    "operand expressions must be of compatible ordinal type");
            ProducedType ct = checkOperandTypes(lhst, rhst, 
                    unit.getComparableDeclaration(), that, 
                    "operand expressions must be comparable");
            if (ct!=null) {
                ProducedType pt = producedType(unit.getRangeDeclaration(), 
                        ct.getTypeArgumentList().get(0));
                that.setTypeModel(pt);
            }
        }
    }
    
    private void visitSegmentOperator(Tree.SegmentOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            ProducedType ot = checkSupertype(lhst, unit.getOrdinalDeclaration(), 
                    that.getLeftTerm(), "left operand must be of ordinal type");
            checkAssignable(rhst, unit.getIntegerDeclaration().getType(), 
                    that.getRightTerm(), "right operand must be an integer");
            if (ot!=null) {
                ProducedType ta = ot.getTypeArgumentList().get(0);
                ProducedType rt = unit.getEmptyType(unit.getSequenceType(ta));
                that.setTypeModel(rt);
            }
        }
    }
    
    private void visitEntryOperator(Tree.EntryOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            checkSupertype(lhst, unit.getObjectDeclaration(), that.getLeftTerm(), 
                    "operand expression must support equality");
            checkSupertype(rhst, unit.getObjectDeclaration(), that.getRightTerm(), 
                    "operand expression must support equality");
            ProducedType et = unit.getEntryType(lhst, rhst);
            that.setTypeModel(et);
        }
    }
    
    private void visitArithmeticOperator(Tree.BinaryOperatorExpression that, 
            TypeDeclaration type) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            ProducedType lhsst = checkSupertype(lhst, type, that.getLeftTerm(), 
                    "operand expression must be of numeric type");
            ProducedType rhsst = checkSupertype(rhst, type, that.getRightTerm(), 
                    "operand expression must be of numeric type");
            if (rhsst!=null && lhsst!=null) {
                rhst = rhsst.getTypeArgumentList().get(0);
                lhst = lhsst.getTypeArgumentList().get(0);
                //find the common type to which arguments
                //can be widened, according to Castable
                ProducedType rt;
                ProducedType st;
                if (lhst.isSubtypeOf(unit.getCastableType(lhst)) &&
                        rhst.isSubtypeOf(unit.getCastableType(lhst))) {
                    //the lhs has a wider type
                    rt = lhst;
                    st = lhsst;
                }
                else if (lhst.isSubtypeOf(unit.getCastableType(rhst)) && 
                        rhst.isSubtypeOf(unit.getCastableType(rhst))) {
                    //the rhs has a wider type
                    rt = rhst;
                    st = rhsst;
                }
                else if (lhst.isExactly(rhst)) { 
                    //in case the args don't implement Castable at all, but
                    //they are exactly the same type, so no promotion is 
                    //necessary - note the language spec does not actually 
                    //bless this at present
                    rt = lhst;
                    st = lhsst;
                }
                else {
                    that.addError("operand expressions must be promotable to common numeric type: " + 
                            lhst.getProducedTypeName() + " and " + 
                            rhst.getProducedTypeName());
                    return;
                }
                checkAssignable(rt, producedType(type, rt), that, 
                        "operands must be of compatible numeric type");
                that.setTypeModel(rt);
            }
        }
    }

    private void visitPowerOperator(Tree.BinaryOperatorExpression that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            ProducedType lhsst = checkSupertype(lhst, unit.getExponentiableDeclaration(), 
                    that.getLeftTerm(), "operand expression must be of exponentiable type");
            /*ProducedType rhsst = checkOperandType(rhst, unit.getNumericDeclaration(), 
                    that.getRightTerm(), "operand expression must be of numeric type");*/
            if (/*rhsst!=null &&*/lhsst!=null) {
                //rhst = rhsst.getTypeArgumentList().get(0);
                lhst = lhsst.getTypeArgumentList().get(0);
                that.setTypeModel(lhst);
                ProducedType powt = lhsst.getTypeArgumentList().get(1);
                if (!rhst.isSubtypeOf(unit.getCastableType(powt)) &&
                        !rhst.isSubtypeOf(powt)) { //note the language spec does not actually bless this
                    that.getRightTerm().addError("operand expression must be promotable to exponent type: " + 
                            rhst.getProducedTypeName() + " is not promotable to " +
                            powt.getProducedTypeName());
                }
            }
        }
    }

    private void visitArithmeticAssignOperator(Tree.BinaryOperatorExpression that, 
            TypeDeclaration type) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            ProducedType nt = checkSupertype(lhst, type, that.getLeftTerm(), 
                    "operand expression must be of numeric type");
            that.setTypeModel(lhst);
            if (nt!=null) {
                ProducedType t = nt.getTypeArgumentList().get(0);
                //that.setTypeModel(t); //stef requests lhst to make it easier on backend
                if (!rhst.isSubtypeOf(unit.getCastableType(t)) &&
                        !rhst.isSubtypeOf(lhst)) { //note the language spec does not actually bless this
                    that.getRightTerm().addError("operand expression must be promotable to declared type: " + 
                            rhst.getProducedTypeName() + " is not promotable to " +
                            nt.getProducedTypeName());
                }
                checkAssignable(t, lhst, that, 
                        "result type must be assignable to declared type");
            }
        }
    }

    private void visitSetOperator(Tree.BitwiseOp that) {
        //TypeDeclaration sd = unit.getSetDeclaration();
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            checkAssignable(lhst, unit.getSetType(unit.getObjectDeclaration().getType()), 
                    that.getLeftTerm(), "set operand expression must be a set");
            checkAssignable(lhst, unit.getSetType(unit.getObjectDeclaration().getType()), 
                    that.getRightTerm(), "set operand expression must be a set");
            ProducedType lhset = unit.getSetElementType(lhst);
            ProducedType rhset = unit.getSetElementType(rhst);
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
        if ( rhst!=null && lhst!=null ) {
            checkAssignable(lhst, unit.getSetType(unit.getObjectDeclaration().getType()), 
                    that.getLeftTerm(), "set operand expression must be a set");
            checkAssignable(lhst, unit.getSetType(unit.getObjectDeclaration().getType()), 
                    that.getRightTerm(), "set operand expression must be a set");
            ProducedType lhset = unit.getSetElementType(lhst);
            ProducedType rhset = unit.getSetElementType(rhst);
            if (that instanceof Tree.UnionAssignOp ||
                    that instanceof Tree.XorAssignOp) {
                checkAssignable(rhset, lhset, that.getRightTerm(), 
                        "resulting set element type must be assignable to to declared set element type");
            }            
            that.setTypeModel(unit.getSetType(lhst)); //in theory, we could make this narrower
        }
    }

    private void visitLogicalOperator(Tree.BinaryOperatorExpression that) {
        ProducedType bt = unit.getBooleanDeclaration().getType();
        checkAssignable(leftType(that), bt, that, 
                "logical operand expression must be a boolean value");
        checkAssignable(rightType(that), bt, that, 
                "logical operand expression must be a boolean value");
        that.setTypeModel(bt);
    }

    private void visitDefaultOperator(Tree.DefaultOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            checkOptional(lhst, that.getLeftTerm(), that.getLeftTerm());
            List<ProducedType> list = new ArrayList<ProducedType>();
            addToUnion(list, rhst);
            addToUnion(list, unit.getDefiniteType(lhst));
            if (list.size()==1) {
                that.setTypeModel(list.get(0));
            }
            else {
                UnionType ut = new UnionType(unit);
                ut.setCaseTypes(list);
                that.setTypeModel(ut.getType());
            }
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
                        ot.getProducedTypeName());
            }*/
        }
    }

    private void visitThenOperator(Tree.ThenOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( lhst!=null ) {
            checkAssignable(lhst, unit.getBooleanDeclaration().getType(), that.getLeftTerm(), 
                    "operand expression must be a boolean value");
        }
        if ( rhst!=null ) {
            checkAssignable(rhst, unit.getObjectDeclaration().getType(), that.getRightTerm(),
                    "operand expression may not be an optional type");
            that.setTypeModel(unit.getOptionalType(rhst));
        }
    }

    private void visitInOperator(Tree.InOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            checkAssignable(lhst, unit.getObjectDeclaration().getType(), that.getLeftTerm(), 
                    "operand expression must support equality");
            checkAssignable(rhst, unit.getCategoryDeclaration().getType(), that.getRightTerm(), 
                    "operand expression must be a category");
        }
        that.setTypeModel( unit.getBooleanDeclaration().getType() );
    }
    
    private void visitUnaryOperator(Tree.UnaryOperatorExpression that, 
            TypeDeclaration type) {
        ProducedType t = type(that);
        if (t!=null) {
            ProducedType nt = checkSupertype(t, type, that.getTerm(), 
                    "operand expression must be of correct type");
            if (nt!=null) {
                ProducedType at = nt.getTypeArguments().isEmpty() ? 
                        nt : nt.getTypeArgumentList().get(0);
                that.setTypeModel(at);
            }
        }
    }

    private void visitExistsOperator(Tree.Exists that) {
        checkOptional(type(that), that.getTerm(), that);
        that.setTypeModel(unit.getBooleanDeclaration().getType());
    }
    
    private void visitNonemptyOperator(Tree.Nonempty that) {
        checkEmpty(type(that), that.getTerm(), that);
        that.setTypeModel(unit.getBooleanDeclaration().getType());
    }
    
    private void visitIsOperator(Tree.IsOp that) {
        /*checkAssignable( type(that), 
                getOptionalType(getObjectDeclaration().getType()), 
                that.getTerm(), 
                "expression may not be of void type");*/
        Tree.Type rt = that.getType();
        if (rt!=null) {
            ProducedType t = rt.getTypeModel();
            if (t!=null) {
                checkReified(that, t);
                if (that.getTerm()!=null) {
                    ProducedType pt = that.getTerm().getTypeModel();
                    if (pt!=null && pt.isSubtypeOf(t)) {
                        that.addError("expression type is a subtype of the type: " +
                                pt.getProducedTypeName() + " is assignable to " +
                                t.getProducedTypeName());
                    }
                    else {
                        ProducedType it = intersectionType(t, pt, unit);
                        if (it.getDeclaration() instanceof BottomType) {
                            that.addError("tests assignability to Bottom type: intersection of " +
                                    pt.getProducedTypeName() + " and " + 
                                    t.getProducedTypeName() +
                                    " is empty");
                        }
                    }
                }
            }
        }
        that.setTypeModel(unit.getBooleanDeclaration().getType());
    }
    
    private void visitAssignOperator(Tree.AssignOp that) {
        ProducedType rhst = rightType(that);
        ProducedType lhst = leftType(that);
        if ( rhst!=null && lhst!=null ) {
            checkAssignable(rhst, lhst, that.getRightTerm(), 
                    "assigned expression must be assignable to declared type", 
                    2100);
        }
        //that.setTypeModel(rhst); //stef requests lhst to make it easier on backend
        that.setTypeModel(lhst);
    }

    private static void checkAssignability(Tree.Term that, Node node) {
        if (that instanceof Tree.BaseMemberExpression ||
                that instanceof Tree.QualifiedMemberExpression) {
            ProducedReference pr = ((Tree.MemberOrTypeExpression) that).getTarget();
            if (pr!=null) {
                Declaration dec = pr.getDeclaration();
                if (!(dec instanceof Value | dec instanceof Getter)) {
                    that.addError("member cannot be assigned: " 
                            + dec.getName());
                }
                else if ( !((TypedDeclaration) dec).isVariable() ) {
                    that.addError("value is not variable: " 
                            + dec.getName(), 800);
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
    
    @Override public void visit(Tree.ArithmeticOp that) {
        super.visit(that);
        if (that instanceof Tree.PowerOp) {
            visitPowerOperator(that);
        }
        else {
            visitArithmeticOperator(that, getArithmeticDeclaration(that));
        }
    }

    private Interface getArithmeticDeclaration(Tree.ArithmeticOp that) {
        if (that instanceof Tree.SumOp) {
            return unit.getSummableDeclaration();
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
        else if (that instanceof Tree.RemainderAssignOp) {
            return unit.getIntegralDeclaration();
        }
        else {
            return unit.getNumericDeclaration();
        }
    }

    @Override public void visit(Tree.BitwiseOp that) {
        super.visit(that);
        that.addWarning("Set operators not yet supported");
        visitSetOperator(that);
    }

    @Override public void visit(Tree.LogicalOp that) {
        super.visit(that);
        visitLogicalOperator(that);
    }

    @Override public void visit(Tree.EqualityOp that) {
        super.visit(that);
        visitComparisonOperator(that, unit.getObjectDeclaration());
    }

    @Override public void visit(Tree.ComparisonOp that) {
        super.visit(that);
        visitComparisonOperator(that, unit.getComparableDeclaration());
    }

    @Override public void visit(Tree.IdenticalOp that) {
        super.visit(that);
        visitComparisonOperator(that, unit.getIdentifiableDeclaration());
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
        visitUnaryOperator(that, unit.getInvertableDeclaration());
    }
        
    @Override public void visit(Tree.PositiveOp that) {
        super.visit(that);
        visitUnaryOperator(that, unit.getInvertableDeclaration());
    }
        
    @Override public void visit(Tree.NotOp that) {
        super.visit(that);
        visitUnaryOperator(that, unit.getBooleanDeclaration());
    }
        
    @Override public void visit(Tree.AssignOp that) {
        super.visit(that);
        visitAssignOperator(that);
        checkAssignability(that.getLeftTerm(), that);
    }
        
    @Override public void visit(Tree.ArithmeticAssignmentOp that) {
        super.visit(that);
        visitArithmeticAssignOperator(that, getArithmeticDeclaration(that));
        checkAssignability(that.getLeftTerm(), that);
    }
        
    @Override public void visit(Tree.LogicalAssignmentOp that) {
        super.visit(that);
        visitLogicalOperator(that);
        checkAssignability(that.getLeftTerm(), that);
    }
        
    @Override public void visit(Tree.BitwiseAssignmentOp that) {
        super.visit(that);
        that.addWarning("Set operators not yet supported");
        visitSetAssignmentOperator(that);
        checkAssignability(that.getLeftTerm(), that);
    }
        
    @Override public void visit(Tree.RangeOp that) {
        super.visit(that);
        visitRangeOperator(that);
    }
        
    @Override public void visit(Tree.SegmentOp that) {
        super.visit(that);
        visitSegmentOperator(that);
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
        
    @Override public void visit(Tree.Extends that) {
        super.visit(that);
        that.addWarning("extends operator not yet supported");
    }
        
    @Override public void visit(Tree.Satisfies that) {
        super.visit(that);
        that.addWarning("satisfies operator not yet supported");
    }
        
    @Override public void visit(Tree.InOp that) {
        super.visit(that);
        visitInOperator(that);
    }
        
    //Atoms:
    
    private void checkOverloadedReference(Tree.MemberOrTypeExpression that) {
        if (isAbstraction(that.getDeclaration()) &&
                that.getSignature() != null) {
            that.addError("ambiguous reference to overloaded method or class: " +
                    that.getDeclaration().getName());
        }
    }
    
    @Override public void visit(Tree.BaseMemberExpression that) {
        /*if (that.getTypeArgumentList()!=null)
            that.getTypeArgumentList().visit(this);*/
        super.visit(that);
        TypedDeclaration member = getBaseDeclaration(that, that.getSignature());
        if (member==null) {
            that.addError("method or attribute does not exist or is ambiguous: " +
                    name(that.getIdentifier()), 100);
            unit.getUnresolvedReferences().add(that.getIdentifier());
        }
        else {
            that.setDeclaration(member);
            if (!member.isVisible(that.getScope())) {
                that.addError("method or attribute is not visible: " +
                        name(that.getIdentifier()), 400);
            }
            Tree.TypeArguments tal = that.getTypeArguments();
            if (explicitTypeArguments(member, tal, that)) {
                List<ProducedType> ta = getTypeArguments(tal);
                tal.setTypeModels(ta);
                visitBaseMemberExpression(that, member, ta, tal);
                //otherwise infer type arguments later
            }
            else {
                //TODO: set the correct metatype
                that.setTypeModel(unit.getVoidDeclaration().getType());
            }
            /*if (defaultArgument) {
                if (member.isClassOrInterfaceMember()) {
                    that.addWarning("references to this from default argument expressions not yet supported");
                }
            }*/
            checkOverloadedReference(that);
        }
    }

    @Override public void visit(Tree.QualifiedMemberExpression that) {
        /*that.getPrimary().visit(this);
        if (that.getTypeArgumentList()!=null)
            that.getTypeArgumentList().visit(this);*/
        super.visit(that);
        ProducedType pt = that.getPrimary().getTypeModel();
        if (pt!=null && that.getIdentifier()!=null && 
                !that.getIdentifier().getText().equals("")) {
            TypeDeclaration d = unwrap(pt, that).getDeclaration();
            TypedDeclaration member = (TypedDeclaration) d.getMember(name(that.getIdentifier()), 
                    unit, that.getSignature());
            if (member==null) {
                that.addError("member method or attribute does not exist or is ambiguous: " +
                        name(that.getIdentifier()) + 
                        " in type " + d.getName(), 100);
                unit.getUnresolvedReferences().add(that.getIdentifier());
            }
            else {
                that.setDeclaration(member);
                if (!member.isVisible(that.getScope())) {
                    that.addError("member method or attribute is not visible: " +
                            name(that.getIdentifier()) + 
                            " of type " + d.getName(), 400);
                }
                if (member.isProtectedVisibility() &&
                        !(that.getPrimary() instanceof Tree.This) &&
                        !(that.getPrimary() instanceof Tree.Super)) {
                    that.addError("member method or attribute is not visible: " +
                            name(that.getIdentifier()) + 
                            " of type " + d.getName());
                }
                Tree.TypeArguments tal = that.getTypeArguments();
                if (explicitTypeArguments(member,tal, that)) {
                    List<ProducedType> ta = getTypeArguments(tal);
                    tal.setTypeModels(ta);
                    visitQualifiedMemberExpression(that, member, ta, tal);
                    //otherwise infer type arguments later
                }
                else {
                    //TODO: set the correct metatype
                    that.setTypeModel(unit.getVoidDeclaration().getType());
                }
                checkOverloadedReference(that);
            }
            if (that.getPrimary() instanceof Tree.Super) {
                if (member!=null && member.isFormal()) {
                    that.addError("superclass member is formal");
                }
            }
        }
    }

    private void visitQualifiedMemberExpression(Tree.QualifiedMemberExpression that,
            TypedDeclaration member, List<ProducedType> typeArgs, Tree.TypeArguments tal) {
        ProducedType receivingType = that.getPrimary().getTypeModel();
        ProducedType receiverType = unwrap(receivingType, that);
        if (acceptsTypeArguments(receiverType, member, typeArgs, tal, that)) {
            ProducedTypedReference ptr = receiverType.getTypedMember(member, typeArgs);
            /*if (ptr==null) {
                that.addError("member method or attribute does not exist: " + 
                        member.getName() + " of type " + 
                        receiverType.getDeclaration().getName());
            }
            else {*/
                ProducedType t = ptr.getFullType(wrap(ptr.getType(), receivingType, that));
                that.setTarget(ptr); //TODO: how do we wrap ptr???
                that.setTypeModel(t);
            //}
        }
    }
    
    private void visitBaseMemberExpression(Tree.BaseMemberExpression that, TypedDeclaration member, 
            List<ProducedType> typeArgs, Tree.TypeArguments tal) {
        if (acceptsTypeArguments(member, typeArgs, tal, that)) {
            ProducedType outerType = that.getScope().getDeclaringType(member);
            ProducedTypedReference pr = member.getProducedTypedReference(outerType, typeArgs);
            that.setTarget(pr);
            ProducedType t = pr.getFullType();
            if (isTypeUnknown(t)) {
                that.addError("could not determine type of method or attribute reference: " +
                        name(that.getIdentifier()));
            }
            else {
                that.setTypeModel(t);
            }
        }
    }

    @Override public void visit(Tree.BaseTypeExpression that) {
        super.visit(that);
        /*if (that.getTypeArgumentList()!=null)
            that.getTypeArgumentList().visit(this);*/
        TypeDeclaration type = getBaseDeclaration(that, that.getSignature());
        if (type==null) {
            that.addError("type does not exist or is ambiguous: " + 
                    name(that.getIdentifier()), 100);
            unit.getUnresolvedReferences().add(that.getIdentifier());
        }
        else {
            that.setDeclaration(type);
            if (!type.isVisible(that.getScope())) {
                that.addError("type is not visible: " +
                        name(that.getIdentifier()), 400);
            }
            Tree.TypeArguments tal = that.getTypeArguments();
            if (explicitTypeArguments(type, tal, that)) {
                List<ProducedType> ta = getTypeArguments(tal);
                tal.setTypeModels(ta);
                visitBaseTypeExpression(that, type, ta, tal);
                //otherwise infer type arguments later
            }
            else {
                //TODO: set the correct metatype
                that.setTypeModel(unit.getVoidDeclaration().getType());
            }
            checkOverloadedReference(that);
        }
    }
        
    @Override public void visit(Tree.ExtendedTypeExpression that) {
        super.visit(that);
        Declaration dec = that.getDeclaration();
        if (dec instanceof Class) {
            Class c = (Class) dec;
            if (c.isAbstraction()) { 
                //if the constructor is overloaded
                //resolve the right overloaded version
                Declaration result = dec.getContainer()
                        .getMemberOrParameter(that.getUnit(), dec.getName(), that.getSignature());
                if (result!=null && result!=dec) {
                    //patch the reference, which was already
                    //initialized to the abstraction
                    that.setDeclaration((TypeDeclaration) result);
                    checkOverloadedReference(that);
                }
                //else report to user that we could not
                //find a matching overloaded constructor
            }
        }
    }
        
    @Override public void visit(Tree.QualifiedTypeExpression that) {
        super.visit(that);
        /*that.getPrimary().visit(this);
        if (that.getTypeArgumentList()!=null)
            that.getTypeArgumentList().visit(this);*/
        ProducedType pt = that.getPrimary().getTypeModel();
        if (that.getPrimary() instanceof Tree.QualifiedTypeExpression || 
                that.getPrimary() instanceof Tree.BaseTypeExpression) {
            //this is a qualified type name, not member reference
            ProducedReference target = ((Tree.MemberOrTypeExpression) that.getPrimary()).getTarget();
            pt = target==null ? null : target.getType();
        }
        if (pt!=null) {
            TypeDeclaration d = unwrap(pt, that).getDeclaration();
            TypeDeclaration type = (TypeDeclaration) d.getMember(name(that.getIdentifier()), 
                    unit, that.getSignature());
            if (type==null) {
                that.addError("member type does not exist or is ambiguous: " +
                        name(that.getIdentifier()) +
                        " in type " + d.getName(), 100);
                unit.getUnresolvedReferences().add(that.getIdentifier());
            }
            else {
                that.setDeclaration(type);
                if (!type.isVisible(that.getScope())) {
                    that.addError("member type is not visible: " +
                            name(that.getIdentifier()) +
                            " of type " + d.getName(), 400);
                }
                if (type.isProtectedVisibility() &&
                        !(that.getPrimary() instanceof Tree.This) &&
                        !(that.getPrimary() instanceof Tree.Super)) {
                    that.addError("member type is not visible: " +
                            name(that.getIdentifier()) +
                            " of type " + d.getName());
                }
                Tree.TypeArguments tal = that.getTypeArguments();
                if (explicitTypeArguments(type, tal, that)) {
                    List<ProducedType> ta = getTypeArguments(tal);
                    tal.setTypeModels(ta);
                    visitQualifiedTypeExpression(that, pt, type, ta, tal);
                    //otherwise infer type arguments later
                }
                else {
                    //TODO: set the correct metatype
                    that.setTypeModel(unit.getVoidDeclaration().getType());
                }
                checkOverloadedReference(that);
            }
            //TODO: this is temporary until we get metamodel reference expressions!
            if (that.getPrimary() instanceof Tree.BaseTypeExpression ||
                    that.getPrimary() instanceof Tree.QualifiedTypeExpression) {
                ProducedReference target = that.getTarget();
                if (target!=null) {
                    checkTypeBelongsToContainingScope(target.getType(), 
                            that.getScope(), that);
                }
            }
            if (!inExtendsClause && that.getPrimary() instanceof Tree.Super) {
                if (type!=null && type.isFormal()) {
                    that.addError("superclass member class is formal");
                }
            }
        }
    }

    private boolean explicitTypeArguments(Declaration dec, Tree.TypeArguments tal, 
            Tree.MemberOrTypeExpression that) {
        return !dec.isParameterized() || 
                tal instanceof Tree.TypeArgumentList;
                //TODO: enable this line to enable
                //      typechecking of references
                //      without type arguments
                //|| !that.getDirectlyInvoked();
    }
    
    @Override public void visit(Tree.SimpleType that) {
        //this one is a declaration, not an expression!
        //we are only validating type arguments here
        super.visit(that);
        ProducedType pt = that.getTypeModel();
        if (pt!=null) {
            TypeDeclaration type = that.getDeclarationModel();//pt.getDeclaration()
            Tree.TypeArgumentList tal = that.getTypeArgumentList();
            //No type inference for declarations
            acceptsTypeArguments(type, getTypeArguments(tal), tal, that);
            //the type has already been set by TypeVisitor
        }
    }
        

    private void visitQualifiedTypeExpression(Tree.QualifiedTypeExpression that,
            ProducedType receivingType, TypeDeclaration type, 
            List<ProducedType> typeArgs, Tree.TypeArguments tal) {
        ProducedType receiverType = unwrap(receivingType, that);
        if (acceptsTypeArguments(receiverType, type, typeArgs, tal, that)) {
            ProducedType t = receiverType.getTypeMember(type, typeArgs);
            ProducedType ft = isAbstractType(t) ?
                    unit.getVoidDeclaration().getType() : //TODO: set the correct metatype
                    t.getFullType(wrap(t, receivingType, that));
            that.setTypeModel(ft);
            that.setTarget(t);
        }
    }

    private void visitBaseTypeExpression(Tree.BaseTypeExpression that, TypeDeclaration type, 
            List<ProducedType> typeArgs, Tree.TypeArguments tal) {
        ProducedType outerType = that.getScope().getDeclaringType(type);
        ProducedType t = type.getProducedType(outerType, typeArgs);
        if (!type.isAlias()) {
            //TODO: remove this awful hack which means
            //      we can't define aliases for types
            //      with sequenced type parameters
            type = t.getDeclaration();
        }
        if ( acceptsTypeArguments(type, typeArgs, tal, that) ) {
            ProducedType ft = isAbstractType(t) ?
                    unit.getVoidDeclaration().getType() : //TODO: set the correct metatype
                    t.getFullType();
            that.setTypeModel(ft);
            that.setTarget(t);
        }
    }

    private boolean isAbstractType(ProducedType t) {
        if (t.getDeclaration() instanceof Class) {
            return ((Class) t.getDeclaration()).isAbstract();
        }
        else if (t.getDeclaration() instanceof TypeParameter) {
            return ((TypeParameter) t.getDeclaration()).getParameterList()==null;
        }
        else {
            return true;
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
            if (t==null) {
                //that.addError("could not determine type of expression");
            }
            else {
                that.setTypeModel(t);
            }
        }
    }
    
    @Override public void visit(Tree.SpecifierOrInitializerExpression that) {
        //i.e. this is a parenthesized expression
        super.visit(that);
        Tree.Term term = that.getExpression()==null ? 
                null : that.getExpression().getTerm();
        if (term!=null) {
            //TODO: it seems a bit fragile to handle this here
            if (term instanceof Tree.StaticMemberOrTypeExpression) {
                Tree.StaticMemberOrTypeExpression smte = (Tree.StaticMemberOrTypeExpression) term;
                if (isGeneric(smte.getDeclaration())) {
                    if (smte.getTypeArguments() instanceof Tree.InferredTypeArguments) {
                        smte.addError("missing type arguments to: " + 
                                smte.getDeclaration().getName());
                    }
                }
            }
        }
    }
    
    @Override public void visit(Tree.Outer that) {
        ProducedType ci = getOuterClassOrInterface(that.getScope());
        if (ci==null) {
            that.addError("outer appears outside a nested class or interface definition");
        }
        else {
            that.setTypeModel(ci);
        }
        /*if (defaultArgument) {
            that.addError("reference to outer from default argument expression");
        }*/
    }

    private boolean inExtendsClause = false;

    @Override public void visit(Tree.Super that) {
        if (inExtendsClause) {
            ClassOrInterface ci = getContainingClassOrInterface(that.getScope());
            if (ci!=null) {
                if (ci.isClassOrInterfaceMember()) {
                    ClassOrInterface s = (ClassOrInterface) ci.getContainer();
                    ProducedType t = s.getExtendedType();
                    //TODO: type arguments??
                    that.setTypeModel(t);
                }
            }
        }
        else {
            ClassOrInterface ci = getContainingClassOrInterface(that.getScope());
            //TODO: for consistency, move these errors to SelfReferenceVisitor
            if (ci==null) {
                that.addError("super appears outside a class definition");
            }
            else if (!(ci instanceof Class)) {
                that.addError("super appears inside an interface definition");
            }
            else {
                ProducedType t = ci.getExtendedType();
                //TODO: type arguments
                that.setTypeModel(t);
            }
        }
        if (defaultArgument) {
            that.addError("reference to super from default argument expression");
        }
    }
    
    @Override public void visit(Tree.This that) {
        ClassOrInterface ci = getContainingClassOrInterface(that.getScope());
        if (ci==null) {
            that.addError("this appears outside a class or interface definition");
        }
        else {
            that.setTypeModel(ci.getType());
        }
        /*if (defaultArgument) {
            that.addWarning("references to this from default argument expressions not yet supported");
        }*/
    }
    
    @Override public void visit(Tree.SequenceEnumeration that) {
        super.visit(that);
        ProducedType st = null;
        if ( that.getComprehension()!=null) {
            ProducedType ct = that.getComprehension()
                    .getForComprehensionClause().getTypeModel();
            if (ct!=null) {
                st = unit.getEmptyType(unit.getSequenceType(ct));
            }
        }
        /*else {
            ProducedType et;
            List<ProducedType> list = new ArrayList<ProducedType>();
            for (Tree.Expression e: that.getExpressionList().getExpressions()) {
                if (e.getTypeModel()!=null) {
                    addToUnion(list, unit.denotableType(e.getTypeModel()));*/
        else if (that.getSequencedArgument()!=null) {
            Tree.Ellipsis ell = that.getSequencedArgument().getEllipsis();
            if (ell==null) {
                ProducedType et;
                List<ProducedType> list = new ArrayList<ProducedType>();
                Tree.ExpressionList el = that.getSequencedArgument().getExpressionList();
                if (el!=null) {
                    for (Tree.Expression e: el.getExpressions()) {
                        if (e.getTypeModel()!=null) {
                            addToUnion(list, unit.denotableType(e.getTypeModel()));
                        }
                    }
                }
                if (list.isEmpty()) {
    //                that.addError("could not infer type of sequence enumeration");
                    return;
                }
                else if (list.size()==1) {
                    et = list.get(0);
                }
                else {
                    UnionType ut = new UnionType(unit);
                    ut.setExtendedType( unit.getObjectDeclaration().getType() );
                    ut.setCaseTypes(list);
                    et = ut.getType(); 
                }
                st = unit.getSequenceType(et);
            }
            else {
                List<Tree.Expression> es = that.getSequencedArgument()
                        .getExpressionList().getExpressions();
                if (es.size()>1) {
                    ell.addError("more than one argument");
                }
                Tree.Expression e = es.get(0);
                ProducedType et = e.getTypeModel();
                if (et!=null) {
                    ProducedType it = unit.getIteratedType(et);
                    if (it==null) {
                        e.addError("argument must be of iterable type: " +
                                et.getProducedTypeName() + " is not Iterable");
                    }
                    else {
                        st = unit.getEmptyType(unit.getSequenceType(it));
                    }
                }
            }
        }
        else {
            st = unit.getEmptyDeclaration().getType();
        }
        
        if (st!=null) that.setTypeModel(st);
    }

    @Override public void visit(Tree.CatchVariable that) {
        super.visit(that);
        Tree.Variable var = that.getVariable();
        if (var!=null) {
            ProducedType et = unit.getExceptionDeclaration().getType();
            if (var.getType() instanceof Tree.LocalModifier) {
                var.getType().setTypeModel(et);
                var.getDeclarationModel().setType(et);
            }
            else {
                checkAssignable(var.getType().getTypeModel(), et, 
                        var.getType(), 
                        "catch type must be an exception type");
            }
        }
    }
    
    @Override public void visit(Tree.StringTemplate that) {
        super.visit(that);
        for (Tree.Expression e: that.getExpressions()) {
            checkAssignable(e.getTypeModel(), unit.getObjectDeclaration().getType(), e, 
                    "interpolated expression must be assignable to Object");
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
        setLiteralType(that, unit.getCharacterDeclaration());
    }
    
    @Override public void visit(Tree.QuotedLiteral that) {
        setLiteralType(that, unit.getQuotedDeclaration());
        String fn = that.getUnit().getFilename();
        if (!"package.ceylon".equals(fn) && !"module.ceylon".equals(fn)) {
            that.addWarning("single-quoted literals are not yet supported");
        }
    }
    
    private void setLiteralType(Tree.Atom that, TypeDeclaration languageType) {
        that.setTypeModel(languageType.getType());
    }
    
    @Override
    public void visit(Tree.CompilerAnnotation that) {
        //don't visit the argument       
    }
    
    @Override
    public void visit(Tree.MatchCase that) {
        super.visit(that);
        for (Tree.Expression e: that.getExpressionList().getExpressions()) {
            ProducedType t = e.getTypeModel();
            if (!isTypeUnknown(t)) {
                TypeDeclaration dec = t.getDeclaration();
                if (!dec.isToplevel() || !dec.isAnonymous()) {
                    e.addError("case must refer to a toplevel object declaration");
                }
                if (switchExpression!=null) {
                    if (!hasUncheckedNulls(switchExpression.getTerm()) || !isNullCase(t)) {
                        checkAssignable(t, switchExpression.getTypeModel(), e, 
                                "case must be assignable to switch expression type");
                    }
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.SatisfiesCase that) {
        super.visit(that);
        that.addWarning("satisfies cases are not yet supported");
    }
    
    @Override
    public void visit(Tree.IsCase that) {
        Tree.Type t = that.getType();
        if (t!=null) {
            t.visit(this);
        }
        Tree.Variable v = that.getVariable();
        if (v!=null) {
            v.visit(this);
            initOriginalDeclaration(v);
        }
        if (switchExpression!=null) {
            ProducedType st = switchExpression.getTypeModel();
            if (t!=null && st!=null) {
                ProducedType pt = t.getTypeModel();
                ProducedType it = intersectionType(pt, st, unit);
                if (!hasUncheckedNulls(switchExpression.getTerm()) || !isNullCase(pt)) {
                    if (it.isExactly(unit.getBottomDeclaration().getType())) {
                        that.addError("narrows to Bottom type: " + 
                                pt.getProducedTypeName() + " has empty intersection with " + 
                                st.getProducedTypeName());
                    }
                    /*checkAssignable(ct, switchType, cc.getCaseItem(), 
                        "case type must be a case of the switch type");*/
                }
                if (v!=null) {
                    v.getType().setTypeModel(it);
                    v.getDeclarationModel().setType(it);
                }
            }
        }
    }
    
    @Override
    public void visit(Tree.SwitchStatement that) {
        Tree.Expression ose = switchExpression;
        switchExpression = that.getSwitchClause().getExpression();
        super.visit(that);        
        switchExpression = ose;
        
    }
    
    @Override
    public void visit(Tree.SwitchCaseList that) {
        super.visit(that);

        if (switchExpression!=null) {
            boolean hasIsCase = false;
            for (Tree.CaseClause cc: that.getCaseClauses()) {
                if (cc.getCaseItem() instanceof Tree.IsCase) {
                    hasIsCase = true;
                }
                ProducedType ct = getType(cc);
                if (!isTypeUnknown(ct)) {
                    for (Tree.CaseClause occ: that.getCaseClauses()) {
                        if (occ==cc) break;
                        ProducedType oct = getType(occ);
                        if (!isTypeUnknown(oct)) {
                            //TODO: the following test doesn't work for cases of an interface!
                            if (!intersectionType(ct, oct, unit)
                                    .isExactly(unit.getBottomDeclaration().getType())) {
                                cc.getCaseItem().addError("cases are not disjoint: " + 
                                    ct.getProducedTypeName() + " and " + 
                                        oct.getProducedTypeName());
                            }
                        }
                    }
                }
            }
            if (hasIsCase) {
                Tree.Term st = switchExpression.getTerm();
                if (st instanceof Tree.BaseMemberExpression) {
                    checkReferenceIsNonVariable((Tree.BaseMemberExpression) st);
                }
                else {
                    switchExpression.addError("switch expression must be a value reference in switch with type cases");
                }
            }   
        }
        

        if (that.getElseClause()==null && switchExpression!=null) {
            ProducedType st = switchExpression.getTypeModel();
            if (st!=null) {
                //form the union of all the case types
                List<ProducedType> list = new ArrayList<ProducedType>();
                for (Tree.CaseClause cc: that.getCaseClauses()) {
                    ProducedType ct = getType(cc);
                    if (isTypeUnknown(ct)) {
                        return; //Note: early exit!
                    }
                    else {
                        addToUnion(list, ct);
                    }
                }
                UnionType ut = new UnionType(unit);
                ut.setCaseTypes(list);
                //if the union of the case types covers 
                //the switch expression type then the 
                //switch is exhaustive
                if (!ut.getType().covers(st)) {
                    that.addError("case types must cover all cases of the switch type or an else clause must appear: " +
                            ut.getType().getProducedTypeName() + " does not cover " + st.getProducedTypeName());
                }
            }
        }
        
    }
    
    private boolean isNullCase(ProducedType ct) {
        TypeDeclaration d = ct.getDeclaration();
        return d!=null &&
                d.equals(unit.getNothingDeclaration()) &&
                d.equals(unit.getNullDeclaration());
    }

    private ProducedType getType(Tree.CaseClause cc) {
        Tree.CaseItem ci = cc.getCaseItem();
        if (ci instanceof Tree.IsCase) {
            Tree.Type t = ((Tree.IsCase) ci).getType();
            if (t!=null) {
                return t.getTypeModel().getUnionOfCases(true);
            }
            else {
                return null;
            }
        }
        else if (ci instanceof Tree.MatchCase) {
            List<ProducedType> list = new ArrayList<ProducedType>();
            for (Tree.Expression e: ((Tree.MatchCase) ci).getExpressionList().getExpressions()) {
                if (e.getTypeModel()!=null) {
                    addToUnion(list, e.getTypeModel());
                }
            }
            UnionType ut = new UnionType(unit);
            ut.setCaseTypes(list);
            return ut.getType();
        }
        else {
            return null;
        }
    }
    
    @Override
    public void visit(Tree.TryCatchStatement that) {
        super.visit(that);
        for (Tree.CatchClause cc: that.getCatchClauses()) {
            if (cc.getCatchVariable()!=null && 
                    cc.getCatchVariable().getVariable()!=null) {
                ProducedType ct = cc.getCatchVariable()
                        .getVariable().getType().getTypeModel();
                if (ct!=null) {
                    for (Tree.CatchClause ecc: that.getCatchClauses()) {
                        if (ecc.getCatchVariable()!=null &&
                                ecc.getCatchVariable().getVariable()!=null) {
                            if (cc==ecc) break;
                            ProducedType ect = ecc.getCatchVariable()
                                    .getVariable().getType().getTypeModel();
                            if (ect!=null) {
                                if (ct.isSubtypeOf(ect)) {
                                    cc.getCatchVariable().getVariable().getType()
                                            .addError("exception type is already handled by earlier catch clause:" 
                                                    + ct.getProducedTypeName());
                                }
                                if (ct.getDeclaration() instanceof UnionType) {
                                    for (ProducedType ut: ct.getDeclaration().getCaseTypes()) {
                                        if ( ut.substitute(ct.getTypeArguments()).isSubtypeOf(ect) ) {
                                            cc.getCatchVariable().getVariable().getType()
                                                    .addError("exception type is already handled by earlier catch clause: "
                                                            + ut.getProducedTypeName());
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

    private static boolean acceptsTypeArguments(Declaration member, List<ProducedType> typeArguments, 
            Tree.TypeArguments tal, Node parent) {
        return acceptsTypeArguments(null, member, typeArguments, tal, parent);
    }
    
    private static boolean isGeneric(Declaration member) {
        return member instanceof Generic && 
            !((Generic) member).getTypeParameters().isEmpty();
    }
    
    private static boolean acceptsTypeArguments(ProducedType receiver, Declaration member, 
            List<ProducedType> typeArguments, Tree.TypeArguments tal, Node parent) {
        if (member==null) return false;
        if (isGeneric(member)) {
            List<TypeParameter> params = ((Generic) member).getTypeParameters();
            if ( params.size()==typeArguments.size() ) {
                for (int i=0; i<params.size(); i++) {
                    TypeParameter param = params.get(i);
                    ProducedType argType = typeArguments.get(i);
                    //Map<TypeParameter, ProducedType> self = Collections.singletonMap(param, arg);
                    for (ProducedType st: param.getSatisfiedTypes()) {
                        //sts = sts.substitute(self);
                        ProducedType sts = st.getProducedType(receiver, member, typeArguments);
                        if (argType!=null) {
                            if (!argType.isSubtypeOf(sts)) {
                                if (tal instanceof Tree.InferredTypeArguments) {
                                    parent.addError("inferred type argument " + argType.getProducedTypeName()
                                            + " to type parameter " + param.getName()
                                            + " of declaration " + member.getName()
                                            + " not assignable to " + sts.getProducedTypeName());
                                }
                                else {
                                    ( (Tree.TypeArgumentList) tal ).getTypes()
                                            .get(i).addError("type parameter " + param.getName() 
                                            + " of declaration " + member.getName()
                                            + " has argument " + argType.getProducedTypeName() 
                                            + " not assignable to " + sts.getProducedTypeName());
                                }
                                return false;
                            }
                        }
                    }
                    boolean asec = argumentSatisfiesEnumeratedConstraint(receiver, member, 
                            typeArguments, argType, param);
                    if (!asec) {
                        if (tal instanceof Tree.InferredTypeArguments) {
                            parent.addError("inferred type argument " + argType.getProducedTypeName()
                                    + " to type parameter " + param.getName()
                                    + " of declaration " + member.getName()
                                    + " not one of the enumerated cases");
                        }
                        else {
                            ( (Tree.TypeArgumentList) tal ).getTypes()
                            .get(i).addError("type parameter " + param.getName() 
                                    + " of declaration " + member.getName()
                                    + " has argument " + argType.getProducedTypeName() 
                                    + " not one of the enumerated cases");
                        }
                        return false;
                    }
                }
                return true;
            }
            else {
                if (tal==null || tal instanceof Tree.InferredTypeArguments) {
                    parent.addError("requires type arguments: " + member.getName());
                }
                else {
                    tal.addError("wrong number of type arguments to: " + member.getName());
                }
                return false;
            }
        }
        else {
            boolean empty = typeArguments.isEmpty();
            if (!empty) {
                tal.addError("does not accept type arguments: " + 
                        member.getName());
            }
            return empty;
        }
    }

    private static boolean argumentSatisfiesEnumeratedConstraint(ProducedType receiver, 
            Declaration member, List<ProducedType> typeArguments, ProducedType argType,
            TypeParameter param) {
        
        List<ProducedType> caseTypes = param.getCaseTypes();
        if (caseTypes==null) {
            //no enumerated constraint
            return true;
        }
        
        //if the type argument is a subtype of one of the cases
        //of the type parameter then the constraint is satisfied
        for (ProducedType ct: caseTypes) {
            ProducedType cts = ct.getProducedType(receiver, member, typeArguments);
            if (argType.isSubtypeOf(cts)) {
                return true;
            }
        }

        //if the type argument is itself a type parameter with
        //an enumerated constraint, and every enumerated case
        //is a subtype of one of the cases of the type parameter,
        //then the constraint is satisfied
        if (argType.getDeclaration() instanceof TypeParameter) {
            List<ProducedType> argCaseTypes = argType.getDeclaration().getCaseTypes();
            if (argCaseTypes!=null) {
                for (ProducedType act: argCaseTypes) {
                    boolean foundCase = false;
                    for (ProducedType ct: caseTypes) {
                        ProducedType cts = ct.getProducedType(receiver, member, typeArguments);
                        if (act.isSubtypeOf(cts)) {
                            foundCase = true;
                            break;
                        }
                    }
                    if (!foundCase) {
                        return false;
                    }
                }
                return true;
            }
        }

        return false;
    }

    @Override 
    public void visit(Tree.ExtendedType that) {
        inExtendsClause = true;
        super.visit(that);
        inExtendsClause = false;

        TypeDeclaration td = (TypeDeclaration) that.getScope();
        Tree.SimpleType et = that.getType();
        if (et!=null) {
            ProducedType type = et.getTypeModel();
            if (type!=null) {
                checkSelfTypes(et, td, type);
                checkExtensionOfMemberType(et, td, type);
                //checkCaseOfSupertype(et, td, type);
            }
        }
    }
    
    @Override 
    public void visit(Tree.SatisfiedTypes that) {
        super.visit(that);
        TypeDeclaration td = (TypeDeclaration) that.getScope();
        for (Tree.StaticType t: that.getTypes()) {
            ProducedType type = t.getTypeModel();
            if (type!=null) {
                checkSelfTypes(t, td, type);
                checkExtensionOfMemberType(t, td, type);
                /*if (!(td instanceof TypeParameter)) {
                    checkCaseOfSupertype(t, td, type);
                }*/
            }
        }
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
                    type.getDeclaration().getName());
        }
    }*/

    @Override 
    public void visit(Tree.CaseTypes that) {
        super.visit(that);
        //this forces every case to be a subtype of the
        //enumerated type, so that we can make use of the
        //enumerated type is equivalent to its cases
        TypeDeclaration td = (TypeDeclaration) that.getScope();
        
        //TODO: get rid of this awful hack:
        List<ProducedType> cases = td.getCaseTypes();
        td.setCaseTypes(null);
        
        if (!(td instanceof TypeParameter)) {
            for (Tree.StaticType t: that.getTypes()) {
                ProducedType type = t.getTypeModel();
                if (!(type.getDeclaration() instanceof TypeParameter)) {
                    //it's not a self type
                    if (type!=null) {
                        checkAssignable(type, td.getType(), t, 
                                "case type must be a subtype of enumerated type");
                        //note: this is a better, faster way to call 
                        //      validateEnumeratedSupertypeArguments()
                        //      but unfortunately it winds up displaying
                        //      the error on the wrong node, confusing
                        //      the user
                        /*ProducedType supertype = type.getDeclaration().getType().getSupertype(td);
                        validateEnumeratedSupertypeArguments(t, type.getDeclaration(), supertype);*/
                    }
                }
            }
            for (Tree.BaseMemberExpression bme: that.getBaseMemberExpressions()) {
                ProducedType type = bme.getTypeModel();
                if (type!=null) {
                    checkAssignable(type, td.getType(), bme, 
                            "case type must be a subtype of enumerated type");
                }
            }
        }
        
        //TODO: get rid of this awful hack:
        td.setCaseTypes(cases);
    }

    private void checkExtensionOfMemberType(Node that, TypeDeclaration td,
            ProducedType type) {
        ProducedType qt = type.getQualifyingType();
        if (qt!=null && td instanceof ClassOrInterface) {
            Scope s = td;
            while (s!=null) {
                s = s.getContainer();
                if (s instanceof TypeDeclaration) {
                    TypeDeclaration otd = (TypeDeclaration) s;
                    if ( otd.getType().isSubtypeOf(qt) ) {
                        return;
                    }
                }
            }
            that.addError("containing type " + qt.getDeclaration().getName() + 
                    " of supertype " + type.getDeclaration().getName() + 
                    " is not an outer type or supertype of any outer type of " +
                    td.getName());
        }
    }
    
    private void checkSelfTypes(Node that, TypeDeclaration td, ProducedType type) {
        if (!(td instanceof TypeParameter)) { //TODO: is this really ok?!
            List<TypeParameter> params = type.getDeclaration().getTypeParameters();
            List<ProducedType> args = type.getTypeArgumentList();
            for (int i=0; i<params.size(); i++) {
                TypeParameter param = params.get(i);
                if ( param.isSelfType() && args.size()>i ) {
                    ProducedType arg = args.get(i);
                    TypeDeclaration std = param.getSelfTypedDeclaration();
                    ProducedType at;
                    if (param.getContainer().equals(std)) {
                        at = td.getType();
                    }
                    else {
                        //TODO: lots wrong here?
                        TypeDeclaration mtd = (TypeDeclaration) td.getMember(std.getName(), null);
                        at = mtd==null ? null : mtd.getType();
                    }
                    if (at!=null) {
                        checkAssignable(at, arg, std, that,
                                "type argument does not satisfy self type constraint on type parameter " + 
                                    param.getName() + " of " + type.getDeclaration().getName());
                    }
                }
            }
        }
    }

    private void validateEnumeratedSupertypes(Node that, Class d) {
        ProducedType type = d.getType();
        for (ProducedType supertype: type.getSupertypes()) {
            if (!type.isExactly(supertype)) {
                TypeDeclaration std = supertype.getDeclaration();
                if (std.getCaseTypes()!=null) {
                    List<ProducedType> types=new ArrayList<ProducedType>();
                    for (ProducedType ct: std.getCaseTypes()) {
                        ProducedType cst = type.getSupertype(ct.getDeclaration());
                        if (cst!=null) {
                            types.add(cst);
                        }
                    }
                    if (types.isEmpty()) {
                        that.addError("concrete type is not a subtype of any case of enumerated supertype: " + 
                                d.getName() + " is a subtype of " + std.getName());
                    }
                    else if (types.size()>1) {
                        StringBuilder sb = new StringBuilder();
                        for (ProducedType pt: types) {
                            sb.append(pt.getProducedTypeName()).append(" and ");
                        }
                        sb.setLength(sb.length()-5);
                        that.addError("concrete type is a subtype of multiple cases of enumerated supertype: " + 
                                d.getName() + " is a subtype of " + sb);
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
                List<TypeDeclaration> ctds = supertype.getDeclaration().getCaseTypeDeclarations();
                if (ctds!=null) {
                    for (TypeDeclaration ct: ctds) {
                        if (ct.equals(d)) { //the declaration is a case of the current enumerated supertype
                            validateEnumeratedSupertypeArguments(that, d, supertype);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void validateEnumeratedSupertypeArguments(Node that, TypeDeclaration d, 
            ProducedType supertype) {
        for (TypeParameter p: supertype.getDeclaration().getTypeParameters()) {
            ProducedType arg = supertype.getTypeArguments().get(p); //the type argument that the declaration (indirectly) passes to the enumerated supertype
            if (arg!=null) {
                validateEnumeratedSupertypeArgument(that, d, supertype, p, arg);
            }
        }
    }

    private void validateEnumeratedSupertypeArgument(Node that, TypeDeclaration d, 
            ProducedType supertype, TypeParameter p, ProducedType arg) {
        TypeDeclaration td = arg.getDeclaration();
        if (td instanceof TypeParameter) {
            TypeParameter tp = (TypeParameter) td;
            if (tp.getDeclaration().equals(d)) { //the argument is a type parameter of the declaration
                //check that the variance of the argument type parameter is
                //the same as the type parameter of the enumerated supertype
                if (p.isCovariant() && !tp.isCovariant()) {
                    that.addError("argument to covariant type parameter of supertype must be covariant: " + 
                            p.getName() + " of "+ supertype.getDeclaration().getName());
                }
                if (p.isContravariant() && !tp.isContravariant()) {
                    that.addError("argument to contravariant type parameter of supertype must be contravariant: " + 
                            p.getName() + " of "+ supertype.getDeclaration().getName());
                }
            }
            else {
                that.addError("argument to type parameter of enumerated supertype must be a type parameter of " +
                        d.getName() + ": " + p.getName() + " of "+ supertype.getDeclaration().getName());
            }
        }
        else if (p.isCovariant()) {
            if (!(td instanceof BottomType)) {
                //TODO: let it be the union of the lower bounds on p
                that.addError("argument to covariant type parameter of enumerated supertype must be a type parameter or Bottom: " + 
                        p.getName() + " of "+ supertype.getDeclaration().getName());
            }
        }
        else if (p.isContravariant()) {
            if (!(td.equals(unit.getVoidDeclaration()))) {
                //TODO: let it be the intersection of the upper bounds on p
                that.addError("argument to contravariant type parameter of enumerated supertype must be a type parameter or Void" + 
                        p.getName() + " of "+ supertype.getProducedTypeName());
            }
        }
        else {
            that.addError("argument to type parameter of enumerated supertype must be a type parameter: " + 
                    p.getName() + " of "+ supertype.getDeclaration().getName());
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
        ut.setExtendedType(unit.getVoidDeclaration().getType());
        return ut.getType();
    }
    
}
