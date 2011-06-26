package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.model.BottomType;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
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
    private Context context;

    public ExpressionVisitor(Context context) {
        this.context = context;
    }
    
    private Tree.Type beginReturnScope(Tree.Type t) {
        Tree.Type ort = returnType;
        returnType = t;
        return ort;
    }
    
    private void endReturnScope(Tree.Type t) {
        returnType = t;
    }

    @Override public void visit(Tree.Variable that) {
        super.visit(that);
        if (that.getSpecifierExpression()!=null) {
            inferType(that, that.getSpecifierExpression());
            checkType(that.getType().getTypeModel(), that.getSpecifierExpression());
        }
    }
    
    @Override public void visit(Tree.IsCondition that) {
        if (that.getVariable()!=null) {
            that.getVariable().getSpecifierExpression().visit(this);
        }
        if (that.getExpression()!=null) {
            that.getExpression().visit(this);
        }
    }
    
    @Override public void visit(Tree.ExistsOrNonemptyCondition that) {
        ProducedType t = null;
        Node n = that;
        Tree.Variable v = that.getVariable();
        if (v!=null) {
            Tree.SpecifierExpression se = v.getSpecifierExpression();
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
        }
        Tree.Expression e = that.getExpression();
        if (e!=null) {
            e.visit(this);
            t = e.getTypeModel();
            n = e;
        }
        if (t==null) {
            n.addError("could not determine if expression is of optional type");
        }
        else {
            if (that instanceof Tree.ExistsCondition) {
                checkOptional(t, n);
            }
            else if (that instanceof Tree.NonemptyCondition) {
                checkEmpty(t, n);
            }
        }
    }

    private void checkEmpty(ProducedType t, Node n) {
        //ProducedType oct = getEmptyOptionalType(getContainerDeclaration().getType());
        if (!isEmptyType(t)) {
            n.addError("expression is not of correct type: " + 
                    t.getProducedTypeName() + " is not a supertype of: Empty");
        }
    }

    private void checkOptional(ProducedType t, Node n) {
        if (!isOptionalType(t)) {
            n.addError("expression is not of optional type: " +
                    t.getProducedTypeName() + " is not a supertype of: Nothing");
        }
    }

    @Override public void visit(Tree.BooleanCondition that) {
        super.visit(that);
        if (that.getExpression()!=null) {
            ProducedType t = that.getExpression().getTypeModel();
            if (t==null) {
                that.addError("could not determine if expression is of boolean type");
            }
            else {
                ProducedType bt = getBooleanDeclaration().getType();
                if (!bt.isSupertypeOf(t)) {
                    that.addError("expression is not of boolean type: " +
                            t.getProducedTypeName() + " is not Boolean");
                }
            }
        }
    }

    @Override public void visit(Tree.ValueIterator that) {
        super.visit(that);
        inferContainedType(that.getVariable(), that.getSpecifierExpression());
        checkContainedType(that.getVariable(), that.getSpecifierExpression());
    }

    @Override public void visit(Tree.KeyValueIterator that) {
        super.visit(that);
        inferKeyType(that.getKeyVariable(), that.getSpecifierExpression());
        inferValueType(that.getValueVariable(), that.getSpecifierExpression());
        checkKeyValueType(that.getKeyVariable(), that.getValueVariable(), that.getSpecifierExpression());
        
    }
    
    @Override public void visit(Tree.AttributeDeclaration that) {
        super.visit(that);
        inferType(that, that.getSpecifierOrInitializerExpression());
        checkType(that.getType().getTypeModel(), that.getSpecifierOrInitializerExpression());
    }

    @Override public void visit(Tree.SpecifierStatement that) {
        super.visit(that);
        checkType(that.getBaseMemberExpression().getTypeModel(), that.getSpecifierExpression());
    }

    @Override public void visit(Tree.Parameter that) {
        super.visit(that);
        checkType(that.getType().getTypeModel(), that.getSpecifierExpression());
    }

    private void checkType(ProducedType declaredType, Tree.SpecifierOrInitializerExpression sie) {
        if (sie!=null) {
            ProducedType expressionType = sie.getExpression().getTypeModel();
            if ( expressionType!=null && declaredType!=null) {
                if ( !declaredType.isSupertypeOf(expressionType) ) {
                    sie.addError("specifier expression not assignable to expected type: " + 
                            expressionType.getProducedTypeName() + " is not " + 
                            declaredType.getProducedTypeName());
                }
            }
            else {
                sie.addError("could not determine assignability of specified expression to expected type");
            }
        }
    }

    private void checkOptionalType(Tree.Variable var, Tree.SpecifierExpression se) {
        ProducedType vt = var.getType().getTypeModel();
        checkType(getOptionalType(vt), se);
    }

    private void checkEmptyOptionalType(Tree.Variable var, Tree.SpecifierExpression se) {
        ProducedType vt = var.getType().getTypeModel();
        checkType(getEmptyOptionalType(vt), se);
    }

    private void checkContainedType(Tree.Variable var, Tree.SpecifierExpression se) {
        ProducedType vt = var.getType().getTypeModel();
        checkType(getIterableType(vt), se);
    }

    private void checkKeyValueType(Tree.Variable key, Tree.Variable value, Tree.SpecifierExpression se) {
        ProducedType kt = key.getType().getTypeModel();
        ProducedType vt = value.getType().getTypeModel();
        checkType(getIterableType(getEntryType(kt, vt)), se);
    }

    private ProducedType getIterableType(ProducedType et) {
        return producedType(getIterableDeclaration(), et);
    }

    private ProducedType getCastableType(ProducedType et) {
        return producedType(getCastableDeclaration(), et);
    }

    private ProducedType getEntryType(ProducedType kt, ProducedType vt) {
        return producedType(getEntryDeclaration(), kt, vt);
    }

    @Override public void visit(Tree.AttributeGetterDefinition that) {
        Tree.Type rt = beginReturnScope(that.getType());
        super.visit(that);
        inferType(that, that.getBlock());
        endReturnScope(rt);
    }

    @Override public void visit(Tree.AttributeArgument that) {
        Tree.Type rt = beginReturnScope(that.getType());
        super.visit(that);
        //TODO: inferType(that, that.getBlock());
        endReturnScope(rt);
    }

    @Override public void visit(Tree.AttributeSetterDefinition that) {
        Tree.Type rt = beginReturnScope(that.getType());
        super.visit(that);
        inferType(that, that.getBlock());
        endReturnScope(rt);
    }

    @Override public void visit(Tree.MethodDeclaration that) {
        super.visit(that);
        inferType(that, that.getSpecifierExpression());
    }

    @Override public void visit(Tree.MethodDefinition that) {
        Tree.Type rt = beginReturnScope(that.getType());           
        super.visit(that);
        endReturnScope(rt);
        inferType(that, that.getBlock());
    }

    @Override public void visit(Tree.MethodArgument that) {
        Tree.Type rt = beginReturnScope(that.getType());           
        super.visit(that);
        endReturnScope(rt);
        inferType(that, that.getBlock());
    }

    @Override public void visit(Tree.ClassDefinition that) {
        Tree.Type rt = beginReturnScope(new Tree.VoidModifier(that.getAntlrTreeNode()));
        super.visit(that);
        endReturnScope(rt);
    }

    @Override public void visit(Tree.InterfaceDefinition that) {
        Tree.Type rt = beginReturnScope(null);
        super.visit(that);
        endReturnScope(rt);
    }

    @Override public void visit(Tree.ObjectDefinition that) {
        Tree.Type rt = beginReturnScope(new Tree.VoidModifier(that.getAntlrTreeNode()));
        super.visit(that);
        endReturnScope(rt);
    }

    @Override public void visit(Tree.ObjectArgument that) {
        Tree.Type rt = beginReturnScope(new Tree.VoidModifier(that.getAntlrTreeNode()));
        super.visit(that);
        endReturnScope(rt);
    }

    //Type inference for members declared "local":
    
    private void inferType(Tree.TypedDeclaration that, Tree.Block block) {
        if (that.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) that.getType();
            if (block!=null) {
                setType(local, block, that);
            }
            else {
                local.addError("could not infer type of: " + 
                        name(that.getIdentifier()));
            }
        }
    }

    //TODO: fix copy/paste code duplication
    private void inferType(Tree.MethodArgument that, Tree.Block block) {
        if (that.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) that.getType();
            if (block!=null) {
                setType(local, block, that);
            }
            else {
                local.addError("could not infer type of: " + 
                        name(that.getIdentifier()));
            }
        }
    }

    private void inferType(Tree.TypedDeclaration that, Tree.SpecifierOrInitializerExpression spec) {
        if (that.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) that.getType();
            if (spec!=null) {
                setType(local, spec, that);
            }
            else {
                local.addError("could not infer type of: " + 
                        name(that.getIdentifier()));
            }
        }
    }

    private void inferDefiniteType(Tree.Variable that, Tree.SpecifierExpression se) {
        if (that.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) that.getType();
            if (se!=null) {
                setTypeFromOptionalType(local, se, that);
            }
            else {
                local.addError("could not infer type of: " + 
                        name(that.getIdentifier()));
            }
        }
    }

    private void inferNonemptyType(Tree.Variable that, Tree.SpecifierExpression se) {
        if (that.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) that.getType();
            if (se!=null) {
                setTypeFromEmptyType(local, se, that);
            }
            else {
                local.addError("could not infer type of: " + 
                        name(that.getIdentifier()));
            }
        }
    }

    private void inferContainedType(Tree.Variable that, Tree.SpecifierExpression se) {
        if (that.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) that.getType();
            if (se!=null) {
                setTypeFromTypeArgument(local, se, that);
            }
            else {
                local.addError("could not infer type of: " + 
                        name(that.getIdentifier()));
            }
        }
    }

    private void inferKeyType(Tree.Variable key, Tree.SpecifierExpression se) {
        if (key.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) key.getType();
            if (se!=null) {
                setTypeFromTypeArgument(local, se, key, 0);
            }
            else {
                local.addError("could not infer type of key: " + 
                        name(key.getIdentifier()));
            }
        }
    }

    private void inferValueType(Tree.Variable value, Tree.SpecifierExpression se) {
        if (value.getType() instanceof Tree.LocalModifier) {
            Tree.LocalModifier local = (Tree.LocalModifier) value.getType();
            if (se!=null) {
                setTypeFromTypeArgument(local, se, value, 1);
            }
            else {
                local.addError("could not infer type of value: " + 
                        name(value.getIdentifier()));
            }
        }
    }

    private void setTypeFromTypeArgument(Tree.LocalModifier local, 
            Tree.SpecifierExpression se, 
            Tree.Variable that) {
        ProducedType expressionType = se.getExpression().getTypeModel();
        if (expressionType!=null) {
            ProducedType st = expressionType.getSupertype(getIterableDeclaration());
            if (st!=null && st.getTypeArguments().size()==1) {
                ProducedType t = st.getTypeArgumentList().get(0);
                local.setTypeModel(t);
                that.getDeclarationModel().setType(t);
                return;
            }
        }
        local.addError("could not infer type of: " + 
                name(that.getIdentifier()));
    }
    
    private void setTypeFromOptionalType(Tree.LocalModifier local, 
            Tree.SpecifierExpression se, 
            Tree.Variable that) {
        ProducedType expressionType = se.getExpression().getTypeModel();
        if (expressionType!=null) {
            if (isOptionalType(expressionType)) {
                ProducedType t = getDefiniteType(expressionType);
                local.setTypeModel(t);
                that.getDeclarationModel().setType(t);
                return;
            }
        }
        local.addError("could not infer type of: " + 
                name(that.getIdentifier()));
    }
    
    private void setTypeFromEmptyType(Tree.LocalModifier local, 
            Tree.SpecifierExpression se, 
            Tree.Variable that) {
        ProducedType expressionType = se.getExpression().getTypeModel();
        if (expressionType!=null) {
            if (isEmptyType(expressionType)) {
                ProducedType t = getNonemptyType(expressionType);
                local.setTypeModel(t);
                that.getDeclarationModel().setType(t);
                return;
            }
        }
        local.addError("could not infer type of: " + 
                name(that.getIdentifier()));
    }
    
    private void setTypeFromTypeArgument(Tree.LocalModifier local,
            Tree.SpecifierExpression se, 
            Tree.Variable that,
            int index) {
        ProducedType expressionType = se.getExpression().getTypeModel();
        if (expressionType!=null) {
            ProducedType it = expressionType.getSupertype(getIterableDeclaration());
            if (it!=null && it.getTypeArguments().size()==1) {
                ProducedType entryType = it.getTypeArgumentList().get(0);
                if (entryType!=null) {
                    ProducedType et = entryType.getSupertype(getEntryDeclaration());
                    if (et!=null && et.getTypeArguments().size()==2) {
                        ProducedType kt = et.getTypeArgumentList().get(index);
                        local.setTypeModel(kt);
                        that.getDeclarationModel().setType(kt);
                        return;
                    }
                }
            }
        }
        local.addError("could not infer type of: " + 
                name(that.getIdentifier()));
    }
    
    private void setType(Tree.LocalModifier local, 
            Tree.SpecifierOrInitializerExpression s, 
            Tree.TypedDeclaration that) {
        ProducedType t = s.getExpression().getTypeModel();
        local.setTypeModel(t);
        that.getDeclarationModel().setType(t);
    }
    
    private void setType(Tree.LocalModifier local, 
            Tree.Block block, 
            Tree.TypedDeclaration that) {
        int s = block.getStatements().size();
        Tree.Statement d = s==0 ? null : block.getStatements().get(s-1);
        if (d!=null && (d instanceof Tree.Return)) {
            ProducedType t = ((Tree.Return) d).getExpression().getTypeModel();
            local.setTypeModel(t);
            that.getDeclarationModel().setType(t);
        }
        else {
            local.addError("could not infer type of: " + 
                    name(that.getIdentifier()));
        }
    }
    
    //TODO: fix copy/paste code duplication
    private void setType(Tree.LocalModifier local, 
            Tree.Block block, 
            Tree.MethodArgument that) {
        int s = block.getStatements().size();
        Tree.Statement d = s==0 ? null : block.getStatements().get(s-1);
        if (d!=null && (d instanceof Tree.Return)) {
            ProducedType t = ((Tree.Return) d).getExpression().getTypeModel();
            local.setTypeModel(t);
            that.getDeclarationModel().setType(t);
        }
        else {
            local.addError("could not infer type of: " + 
                    name(that.getIdentifier()));
        }
    }
    
    @Override public void visit(Tree.Return that) {
        super.visit(that);
        if (returnType==null) {
            that.addError("could not determine expected return type");
        } 
        else {
            Tree.Expression e = that.getExpression();
            if ( returnType instanceof Tree.VoidModifier ) {
                if (e!=null) {
                    that.addError("a void method, setter, or class initializer may not return a value");
                }
            }
            else if ( !(returnType instanceof Tree.LocalModifier) ) {
                if (e==null) {
                    that.addError("a non-void method or getter must return a value");
                }
                else {
                    ProducedType et = returnType.getTypeModel();
                    ProducedType at = e.getTypeModel();
                    if (et!=null && at!=null) {
                        if ( !et.isSupertypeOf(at) ) {
                            that.addError("returned expression not assignable to expected return type: " +
                                    at.getProducedTypeName() + " is not " +
                                    et.getProducedTypeName());
                        }
                    }
                    else {
                        that.addError("could not determine assignability of returned expression to expected return type");
                    }
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
        Tree.MemberOperator op = mte.getMemberOperator();
        if (op instanceof Tree.SafeMemberOp)  {
            if (isOptionalType(pt)) {
                return getDefiniteType(pt);
            }
            else {
                mte.getPrimary().addError("receiver not of optional type");
                return pt;
            }
        }
        else if (op instanceof Tree.SpreadOp) {
            ProducedType st = getNonemptySequenceType(pt);
            if (st==null) {
                mte.getPrimary().addError("receiver not of type: Sequence");
                return pt;
            }
            else {
                return st.getTypeArgumentList().get(0);
            }
        }
        else {
            return pt;
        }
    }

    private ProducedType getNonemptySequenceType(ProducedType pt) {
        return pt.minus(getEmptyDeclaration()).getSupertype(getSequenceDeclaration());
    }
    
    ProducedType wrap(ProducedType pt, Tree.QualifiedMemberOrTypeExpression mte) {
        Tree.MemberOperator op = mte.getMemberOperator();
        if (op instanceof Tree.SafeMemberOp)  {
            return getOptionalType(pt);
        }
        else if (op instanceof Tree.SpreadOp) {
            return getSequenceType(pt);
        }
        else {
            return pt;
        }
    }

    private ProducedType getEmptyOptionalType(ProducedType pt) {
        if (pt==null) {
            return null;
        }
        else if (isEmptyType(pt) && isOptionalType(pt)) {
            //Nothing|Nothing|T == Nothing|T
            return pt;
        }
        else {
            UnionType ut = new UnionType();
            List<ProducedType> types = new ArrayList<ProducedType>();
            addToUnion(types,getNothingDeclaration().getType());
            addToUnion(types,getEmptyDeclaration().getType());
            addToUnion(types,pt);
            ut.setCaseTypes(types);
            return ut.getType();
        }
    }
    
    /*private ProducedType getEmptyType(ProducedType pt) {
        if (pt==null) {
            return null;
        }
        else if (isEmptyType(pt)) {
            //Nothing|Nothing|T == Nothing|T
            return pt;
        }
        else if (pt.getDeclaration() instanceof BottomType) {
            //Nothing|0 == Nothing
            return getEmptyDeclaration().getType();
        }
        else {
            UnionType ut = new UnionType();
            List<ProducedType> types = new ArrayList<ProducedType>();
            addToUnion(types,getEmptyDeclaration().getType());
            addToUnion(types,pt);
            ut.setCaseTypes(types);
            return ut.getType();
        }
    }*/
    
    private ProducedType getOptionalType(ProducedType pt) {
        if (pt==null) {
            return null;
        }
        else if (isOptionalType(pt)) {
            //Nothing|Nothing|T == Nothing|T
            return pt;
        }
        else if (pt.getDeclaration() instanceof BottomType) {
            //Nothing|0 == Nothing
            return getNothingDeclaration().getType();
        }
        else {
            UnionType ut = new UnionType();
            List<ProducedType> types = new ArrayList<ProducedType>();
            addToUnion(types,getNothingDeclaration().getType());
            addToUnion(types,pt);
            ut.setCaseTypes(types);
            return ut.getType();
        }
    }
    
    @Override public void visit(Tree.Annotation that) {
        //TODO: ignore annotations for now
    }
    
    @Override public void visit(Tree.InvocationExpression that) {
        super.visit(that);
        Tree.Primary pr = that.getPrimary();
        if (pr==null) {
            that.addError("malformed invocation expression");
        }
        else {
            Declaration dec = pr.getDeclaration();
            if ( pr.getTarget()==null && dec!=null ) {
                List<ProducedType> typeArgs = getInferedTypeArguments(that, dec);
                if (pr instanceof Tree.BaseTypeExpression) {
                    visitBaseTypeExpression((Tree.BaseTypeExpression) pr, (TypeDeclaration) dec, typeArgs, null);
                }
                else if (pr instanceof Tree.QualifiedTypeExpression) {
                    visitQualifiedTypeExpression((Tree.QualifiedTypeExpression) pr, (TypeDeclaration) dec, typeArgs, null);
                }
                else if (pr instanceof Tree.BaseMemberExpression) {
                    visitBaseMemberExpression((Tree.BaseMemberExpression) pr, (TypedDeclaration) dec, typeArgs, null);
                }
                else if (pr instanceof Tree.QualifiedMemberExpression) {
                    visitQualifiedMemberExpression((Tree.QualifiedMemberExpression) pr, (TypedDeclaration) dec, typeArgs, null);
                }
            }
            visitInvocation(that);
        }
    }

    private List<ProducedType> getInferedTypeArguments(
            Tree.InvocationExpression that, Declaration dec) {
        List<ProducedType> typeArgs = new ArrayList<ProducedType>();
        Functional functional = (Functional) dec;
        ParameterList parameters = functional.getParameterLists().get(0);
        for (TypeParameter tp: functional.getTypeParameters()) {
            List<ProducedType> inferredTypes = new ArrayList<ProducedType>();
            if (that.getPositionalArgumentList()!=null) {
                List<Tree.PositionalArgument> args = that.getPositionalArgumentList().getPositionalArguments();
                for (int i=0; i<parameters.getParameters().size(); i++) {
                    Parameter parameter = parameters.getParameters().get(i);
                    if (parameter.getType().getDeclaration()==tp) {
                        Tree.Expression value = args.get(i).getExpression();
                        addToUnion(inferredTypes, value.getTypeModel());
                    }
                }
            }
            else if (that.getNamedArgumentList()!=null) {
                List<Tree.NamedArgument> args = that.getNamedArgumentList().getNamedArguments();
                for (Tree.NamedArgument arg: args) {
                    ProducedType type = null;
                    if (arg instanceof Tree.SpecifiedArgument) {
                        Tree.Expression value = ((Tree.SpecifiedArgument)arg).getSpecifierExpression().getExpression();
                        type = value.getTypeModel();
                    }
                    else if (arg instanceof Tree.TypedArgument) {
                        //TODO: broken for method args
                        type = ((Tree.TypedArgument) arg).getType().getTypeModel();
                    }
                    if (type!=null) {
                        Parameter parameter = getMatchingParameter(parameters, arg);
                        if (parameter.getType().getDeclaration()==tp) {
                            addToUnion(inferredTypes, type);
                        }
                    }
                }
            }
            UnionType ut = new UnionType();
            ut.setCaseTypes(inferredTypes);
            typeArgs.add(ut.getType());
        }
        return typeArgs;
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

    private void visitInvocation(Tree.InvocationExpression that) {
        ProducedReference mr = that.getPrimary().getTarget();
        if (mr==null || !mr.isFunctional()) {
            that.addError("receiving expression cannot be invoked");
        }
        else {
            if (that instanceof Tree.InvocationExpression) {
                //that.setTypeModel(mr.getType()); //THIS IS THE CORRECT ONE!
                ( (Tree.InvocationExpression) that ).setTypeModel(that.getPrimary().getTypeModel()); //TODO: THIS IS A TEMPORARY HACK!
            }
            List<ParameterList> pls = ((Functional) mr.getDeclaration()).getParameterLists();
            if (pls.isEmpty()) {
                that.addError("receiver does not define a parameter list");
            }
            else {
                ParameterList pl = pls.get(0);            
                if ( that.getPositionalArgumentList()!=null ) {
                    checkPositionalArguments(pl, mr, that.getPositionalArgumentList());
                }
                if ( that.getNamedArgumentList()!=null ) {
                    checkNamedArguments(pl, mr, that.getNamedArgumentList());
                }
            }
        }
    }

    private void checkNamedArguments(ParameterList pl, ProducedReference pr, 
            Tree.NamedArgumentList nal) {
        List<Tree.NamedArgument> na = nal.getNamedArguments();        
        Set<Parameter> foundParameters = new HashSet<Parameter>();
        
        for (Tree.NamedArgument a: na) {
            Parameter p = getMatchingParameter(pl, a);
            if (p==null) {
                a.addError("no matching parameter for named argument: " + 
                        name(a.getIdentifier()));
            }
            else {
                foundParameters.add(p);
                checkNamedArgument(a, pr, p);
            }
        }
        
        Tree.SequencedArgument sa = nal.getSequencedArgument();
        if (sa!=null) {
            Parameter sp = getSequencedParameter(pl);
            if (sp==null) {
                sa.addError("no matching sequenced parameter");
            }
            else {
                foundParameters.add(sp);
                checkSequencedArgument(sa, pr, sp);
            }
        }
            
        for (Parameter p: pl.getParameters()) {
            if (!foundParameters.contains(p) && !p.isDefaulted() && !p.isSequenced()) {
                nal.addError("missing named argument to parameter: " + 
                        p.getName());
            }
        }
    }

    private void checkNamedArgument(Tree.NamedArgument a, ProducedReference pr, 
            Parameter p) {
        if (p.getType()==null) {
            a.addError("parameter type not known: " + name(a.getIdentifier()));
        }
        else {
            ProducedType argType = null;
            if (a instanceof Tree.SpecifiedArgument) {
                argType = ((Tree.SpecifiedArgument) a).getSpecifierExpression().getExpression().getTypeModel();
            }
            else if (a instanceof Tree.TypedArgument) {
                argType = ((Tree.TypedArgument) a).getType().getTypeModel();
            }
            if (argType==null) {
                a.addError("could not determine assignability of argument to parameter: " +
                        p.getName());
            }
            else {
                ProducedType paramType = pr.getTypedParameter(p).getType();
                if ( !paramType.getType().isSupertypeOf(argType) ) {
                    a.addError("named argument not assignable to parameter type: " + 
                            p.getName() + " since " +
                            argType.getProducedTypeName() + " is not " +
                            paramType.getProducedTypeName());
                }
            }
        }
    }
    
    private void checkSequencedArgument(Tree.SequencedArgument a, ProducedReference pr, 
            Parameter p) {
        if (p.getType()==null) {
            a.addError("sequenced parameter type not known");
        }
        else {
            for (Tree.Expression e: a.getExpressionList().getExpressions()) {
                ProducedType argType = e.getTypeModel();    
                if (argType==null) {
                    a.addError("could not determine assignability of argument to parameter: " +
                            p.getName());
                }
                else {
                    ProducedType paramType = pr.getTypedParameter(p).getType();
                    if (paramType!=null) {
                        ProducedType at = getIndividualSequencedParameterType(paramType);
                        if ( !at.getType().isSupertypeOf(argType) ) {
                            a.addError("sequenced argument not assignable to sequenced parameter type: " + 
                                    p.getName() + " since " +
                                    argType.getProducedTypeName() + " is not " +
                                    paramType.getProducedTypeName());
                        }
                    }
                }
            }
        }
    }
    
    private Parameter getMatchingParameter(ParameterList pl, Tree.NamedArgument na) {
        for (Parameter p: pl.getParameters()) {
            if (p.getName().equals(na.getIdentifier().getText())) {
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

    private void checkPositionalArguments(ParameterList pl, ProducedReference r, 
            Tree.PositionalArgumentList pal) {
        List<Tree.PositionalArgument> args = pal.getPositionalArguments();
        List<Parameter> params = pl.getParameters();
        for (int i=0; i<params.size(); i++) {
            Parameter p = params.get(i);
            if (i>=args.size()) {
                if (!p.isDefaulted() && !p.isSequenced()) {
                    pal.addError("no argument to parameter: " + p.getName());
                }
            }
            else if (p.isSequenced()) {
                ProducedType paramType = r.getTypedParameter(p).getType();
                if (paramType!=null) {
                    checkSequencedPositionalArgument(p, args, i, paramType);
                }
                return;
            }
            else {
                ProducedType paramType = r.getTypedParameter(p).getType();
                if (paramType!=null) {
                    checkPositionalArgument(p, args.get(i), paramType);
                }
            }
        }
        for (int i=params.size(); i<args.size(); i++) {
            args.get(i).addError("no matching parameter for argument");
        }
    }

    private void checkSequencedPositionalArgument(Parameter p,
            List<Tree.PositionalArgument> args, int i, ProducedType paramType) {
        ProducedType at = getIndividualSequencedParameterType(paramType);
        for (int j=i; j<args.size(); j++) {
            Tree.PositionalArgument a = args.get(i);
            Tree.Expression e = a.getExpression();
            if (e==null) {
                //TODO: this case is temporary until we get support for SPECIAL_ARGUMENTs
            }
            else {
                ProducedType argType = e.getTypeModel();
                if (argType!=null) {
                    if (paramType.isSupertypeOf(argType)) {
                        if (i<args.size()-1) {
                            a.addError("too many arguments to sequenced parameter: " + p.getName());
                        }
                    }
                    else if (!at.isSupertypeOf(argType)) {
                        a.addError("argument not assignable to parameter type: " + 
                                p.getName() + " since " +
                                argType.getProducedTypeName() + " is not " +
                                at.getProducedTypeName());
                    }
                }
                else {
                    a.addError("could not determine assignability of argument to parameter: " +
                            p.getName());
                }
            }
        }
    }

    private ProducedType getIndividualSequencedParameterType(
            ProducedType paramType) {
        ProducedType seqType = getNonemptySequenceType(paramType);
        return seqType.getTypeArgumentList().get(0);
    }

    private void checkPositionalArgument(Parameter p,
            Tree.PositionalArgument a, ProducedType paramType) {
        Tree.Expression e = a.getExpression();
        if (e==null) {
            //TODO: this case is temporary until we get support for SPECIAL_ARGUMENTs
        }
        else {
            ProducedType argType = e.getTypeModel();
            if (argType!=null) {
                if (!paramType.isSupertypeOf(argType)) {
                    a.addError("argument not assignable to parameter type: " + 
                            p.getName() + " since " +
                            argType.getProducedTypeName() + " is not " +
                            paramType.getProducedTypeName());
                }
            }
            else {
                a.addError("could not determine assignability of argument to parameter: " +
                        p.getName());
            }
        }
    }
    
    @Override public void visit(Tree.IndexExpression that) {
        super.visit(that);
        ProducedType pt = type(that);
        if (pt==null) {
            that.addError("could not determine type of receiver");
        }
        else {
            if (that instanceof Tree.SafeIndexOp) {
                if (isOptionalType(pt)) {
                    pt = getDefiniteType(pt);
                }
                else {
                    that.getPrimary().addError("receving type not of optional type: " +
                            pt.getProducedTypeName() + " is not Optional");
                }
            }
            ProducedType st = pt.minus(getEmptyDeclaration()).getSupertype(getCorrespondenceDeclaration());
            if (st==null) st = pt.getSupertype(getCorrespondenceDeclaration());
            if (st==null) {
                that.getPrimary().addError("illegal receiving type for index expression: " +
                        pt.getProducedTypeName() + " is not of type: Correspondence");
            }
            else {
                List<ProducedType> args = st.getTypeArgumentList();
                ProducedType kt = args.get(0);
                ProducedType vt = args.get(1);
                if (that.getElementOrRange()==null) {
                    that.addError("malformed index expression");
                }
                else {
                    ProducedType rt;
                    if (that.getElementOrRange() instanceof Tree.Element) {
                        Tree.Element e = (Tree.Element) that.getElementOrRange();
                        ProducedType et = e.getExpression().getTypeModel();
                        if (et!=null) {
                            if (!kt.isSupertypeOf(et)) {
                                e.addError("index must be of type: " +
                                        kt.getProducedTypeName());
                            }
                        }
                        rt = getOptionalType(vt);
                    }
                    else {
                        Tree.ElementRange er = (Tree.ElementRange) that.getElementOrRange();
                        ProducedType lbt = er.getLowerBound().getTypeModel();
                        if (lbt!=null) {
                            if (!kt.isSupertypeOf(lbt)) {
                                er.getLowerBound().addError("lower bound must be of type: " +
                                        kt.getProducedTypeName());
                            }
                        }
                        if (er.getUpperBound()!=null) {
                            ProducedType ubt = er.getUpperBound().getTypeModel();
                            if (ubt!=null) {
                                if (!kt.isSupertypeOf(ubt)) {
                                    er.getUpperBound().addError("upper bound must be of type: " +
                                            kt.getProducedTypeName());
                                }
                            }
                        }
                        rt = getSequenceType(vt);
                    }
                    that.setTypeModel(rt);
                }
            }
        }
    }

    private ProducedType getDefiniteType(ProducedType pt) {
        return pt.minus(getNothingDeclaration());
    }

    private ProducedType getNonemptyType(ProducedType pt) {
        return pt.minus(getNothingDeclaration()).minus(getEmptyDeclaration());
    }

    private ProducedType type(Tree.PostfixExpression that) {
        Tree.Primary p = that.getPrimary();
        return p==null ? null : p.getTypeModel();
    }
    
    @Override public void visit(Tree.PostfixOperatorExpression that) {
        super.visit(that);
        visitIncrementDecrement(that, type(that), that.getPrimary());
        checkAssignable(that.getPrimary());
    }

    @Override public void visit(Tree.PrefixOperatorExpression that) {
        super.visit(that);
        visitIncrementDecrement(that, type(that), that.getTerm());
        checkAssignable(that.getTerm());
    }

    private void visitIncrementDecrement(Tree.Term that,
            ProducedType pt, Tree.Term term) {
        if (pt!=null) {
            if (pt.getSupertype(getOrdinalDeclaration())==null) {
                term.addError("must be of type: Ordinal");
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

    private void visitComparisonOperator(Tree.BinaryOperatorExpression that, TypeDeclaration type) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            ProducedType nt = lhst.getSupertype(type);
            if (nt==null) {
                that.getLeftTerm().addError("must be of type: " + type.getName());
            }
            else {
                that.setTypeModel( getBooleanDeclaration().getType() );            
                if (!nt.isSupertypeOf(rhst)) {
                    that.getRightTerm().addError("must be of type: " + nt.getProducedTypeName());
                }
            }
        }
    }
    
    private void visitCompareOperator(Tree.CompareOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            ProducedType nt = lhst.getSupertype(getComparableDeclaration());
            if (nt==null) {
                that.getLeftTerm().addError("must be of type: Comparable");
            }
            else {
                that.setTypeModel( getComparisonDeclaration().getType() );            
                if (!nt.isSupertypeOf(rhst)) {
                    that.getRightTerm().addError("must be of type: " + nt.getProducedTypeName());
                }
            }
        }
    }
    
    private void visitRangeOperator(Tree.RangeOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            if ( lhst.getSupertype(getOrdinalDeclaration())==null) {
                that.getLeftTerm().addError("must be of type: Ordinal");
            }
            if ( rhst.getSupertype(getOrdinalDeclaration())==null) {
                that.getRightTerm().addError("must be of type: Ordinal");
            }
            ProducedType ct = lhst.getSupertype(getComparableDeclaration());
            if ( ct==null) {
                that.getLeftTerm().addError("must be of type: Comparable");
            }
            else {
                ProducedType t = ct.getTypeArgumentList().get(0);
                if ( !rhst.isSubtypeOf(t)) {
                    that.getRightTerm().addError("must be of type: " + 
                            t.getProducedTypeName());
                }
                else {
                    that.setTypeModel( producedType(getRangeDeclaration(), t) );
                }
            }
        }
    }
    
    private void visitEntryOperator(Tree.EntryOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            ProducedType let = lhst.getSupertype(getEqualityDeclaration());
            ProducedType ret = rhst.getSupertype(getEqualityDeclaration());
            if ( let==null) {
                that.getLeftTerm().addError("must be of type: Equality");
            }
            if ( ret==null) {
                that.getRightTerm().addError("must be of type: Equality");
            }
            ProducedType et = getEntryType(lhst, rhst);
            that.setTypeModel(et);
        }
    }
    
    private void visitArithmeticOperator(Tree.BinaryOperatorExpression that, TypeDeclaration type) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            ProducedType rhsst = rhst.getSupertype(type);
            ProducedType lhsst = lhst.getSupertype(type);
            if (rhsst==null) {
                that.getRightTerm().addError("must be of type: " + type.getName());
            }
            if (lhsst==null) {
                that.getLeftTerm().addError("must be of type: " + type.getName());
            }
            if (rhsst!=null && lhsst!=null) {
                rhst = rhsst.getTypeArgumentList().get(0);
                lhst = lhsst.getTypeArgumentList().get(0);
                ProducedType rt;
                Tree.Term node;
                if (lhst.isSubtypeOf(getCastableType(lhst)) && rhst.isSubtypeOf(getCastableType(lhst))) {
                    rt = lhst;
                    node = that.getLeftTerm();
                }
                else if (lhst.isSubtypeOf(getCastableType(rhst)) && rhst.isSubtypeOf(getCastableType(rhst))) {
                    rt = rhst;
                    node = that.getRightTerm();
                }
                else {
                    that.addError("could not promote operands to a common type: " + 
                            lhst.getProducedTypeName() + ", " + rhst.getProducedTypeName());
                    return;
                }
                if (!rt.isSubtypeOf(producedType(type,rt))) {
                    node.addError("must be of type: " + type.getName());
                }
                else {
                    that.setTypeModel(rt);
                }
            }
        }
    }

    private void visitArithmeticAssignOperator(Tree.BinaryOperatorExpression that, TypeDeclaration type) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            ProducedType nt = lhst.getSupertype(type);
            if (nt==null) {
                that.getLeftTerm().addError("must be of type: " + type.getName());
            }
            else {
                ProducedType t = nt.getTypeArguments().isEmpty() ? 
                        nt : nt.getTypeArgumentList().get(0);
                that.setTypeModel(t);
                if (!getCastableType(t).isSupertypeOf(rhst)) {
                    that.getRightTerm().addError("must be promotable to type: " + nt.getProducedTypeName());
                }
            }
        }
    }

    private void visitBinaryOperator(Tree.BinaryOperatorExpression that, TypeDeclaration type) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            ProducedType nt = lhst.getSupertype(type);
            if (nt==null) {
                that.getLeftTerm().addError("must be of type: " + type.getName());
            }
            else {
                ProducedType t = nt.getTypeArguments().isEmpty() ? 
                        nt : nt.getTypeArgumentList().get(0);
                that.setTypeModel(t);
                if (!nt.isSupertypeOf(rhst)) {
                    that.getRightTerm().addError("must be of type: " + nt.getProducedTypeName());
                }
            }
        }
    }

    private void visitDefaultOperator(Tree.DefaultOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            that.setTypeModel(rhst);
            if (!isOptionalType(lhst)) {
                that.getLeftTerm().addError("must be of optional type");
            }
            ProducedType ot;
            if (isOptionalType(rhst)) {
                ot = rhst;
            }
            else {
                ot = getOptionalType(rhst);
            }
            if (!lhst.isSubtypeOf(ot)) {
                that.getLeftTerm().addError("must be of type: " + ot.getProducedTypeName());
            }
        }
    }

    private boolean isOptionalType(ProducedType rhst) {
        return getNothingDeclaration().getType().isSubtypeOf(rhst);
    }
    
    private boolean isEmptyType(ProducedType rhst) {
        return getEmptyDeclaration().getType().isSubtypeOf(rhst);
    }
    
    private void visitInOperator(Tree.InOp that) {
        ProducedType lhst = leftType(that);
        ProducedType rhst = rightType(that);
        if ( rhst!=null && lhst!=null ) {
            if ( !lhst.isSubtypeOf(getObjectDeclaration().getType())) {
                that.getLeftTerm().addError("must be of type: Object");
            }
            if ( !rhst.isSubtypeOf(getCategoryDeclaration().getType()) ) {
                ProducedType it = rhst.getSupertype(getIterableDeclaration());
                if (it==null) {
                    that.getRightTerm().addError("must be of type: Category | Iterable<Equality>");
                }
                else if ( !it.getTypeArgumentList().get(0).isSubtypeOf(getEqualityDeclaration().getType()) ){
                    that.getRightTerm().addError("must be of type: Category | Iterable<Equality>");
                }
            }
        }
        that.setTypeModel( getBooleanDeclaration().getType() );
    }
    
    private void visitUnaryOperator(Tree.UnaryOperatorExpression that, TypeDeclaration type) {
        ProducedType t = type(that);
        if ( t!=null ) {
            ProducedType nt = t.getSupertype(type);
            if (nt==null) {
                that.getTerm().addError("must be of type: " + type.getName());
            }
            else {
                ProducedType at = nt.getTypeArguments().isEmpty() ? 
                        nt : nt.getTypeArgumentList().get(0);
                that.setTypeModel(at);
            }
        }
    }

    private void visitNegativeOperator(Tree.UnaryOperatorExpression that) {
        ProducedType t = type(that);
        if ( t!=null ) {
            ProducedType nt = t.getSupertype(getInvertableDeclaration());
            if (nt==null) {
                that.getTerm().addError("must be of type: Invertable");
            }
            else {
                ProducedType at = nt.getTypeArgumentList().get(0);
                that.setTypeModel(at);
            }
        }
    }

    private TypeDeclaration getLanguageDeclaration(String type) {
        return (TypeDeclaration) getLanguageModuleDeclaration(type, context);
    }

    private void visitFormatOperator(Tree.UnaryOperatorExpression that) {
        //TODO: reenable once we have extensions:
        /*ProducedType t = that.getTerm().getTypeModel();
        if ( t!=null ) {
            if ( !getLanguageType("Formattable").isSupertypeOf(t) ) {
                that.getTerm().addError("must be of type: Formattable");
            }
        }*/
        that.setTypeModel( getStringDeclaration().getType() );
    }
    
    private void visitExistsOperator(Tree.Exists that) {
        ProducedType t = type(that);
        if (t!=null) {
            checkOptional(t, that);
        }
        that.setTypeModel(getBooleanDeclaration().getType());
    }
    
    private void visitNonemptyOperator(Tree.Nonempty that) {
        ProducedType t = type(that);
        if (t!=null) {
            if (isOptionalType(t)) {
                if ( !getDefiniteType(t).isSubtypeOf(getContainerDeclaration().getType()) ) {
                    that.getTerm().addError("must be of type: Optional<Container>");
                }
            }
            else {
                that.getTerm().addError("must be of type: Optional<Container>");
            }
        }
        that.setTypeModel(getBooleanDeclaration().getType());
    }
    
    private void visitIsOperator(Tree.IsOp that) {
        ProducedType t = leftType(that);
        if (t!=null) {
            if (!t.isSubtypeOf(getObjectDeclaration().getType())) {
                that.getLeftTerm().addError("must be of type: Object");
            }
        }
        Tree.Term rt = that.getRightTerm();
        if (rt!=null) {
            if (!(rt instanceof Tree.BaseTypeExpression)) {
                rt.addError("must be a literal type");
            }
        }
        that.setTypeModel(getBooleanDeclaration().getType());
    }
    
    private void visitAssignOperator(Tree.AssignOp that) {
        ProducedType rhst = rightType(that);
        ProducedType lhst = leftType(that);
        if ( rhst!=null && lhst!=null ) {
            if ( !rhst.isSubtypeOf(lhst) ) {
                that.getRightTerm().addError("must be of type " +
                        lhst.getProducedTypeName());
            }
        }
        that.setTypeModel(rhst);

    }

    private void checkAssignable(Tree.Term that) {
        //TODO: what about QualifiedMemberExpression?!
        if (!(that instanceof Tree.BaseMemberExpression)) {
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
        visitArithmeticOperator(that, getArithmeticDeclaration(that));
    }

    private Interface getArithmeticDeclaration(Tree.ArithmeticOp that) {
        if (that instanceof Tree.SumOp) {
            return getSummableDeclaration();
        }
        else if (that instanceof Tree.RemainderOp) {
            return getIntegralDeclaration();
        }
        else {
            return getNumericDeclaration();
        }
    }

    private Interface getArithmeticDeclaration(Tree.ArithmeticAssignmentOp that) {
        if (that instanceof Tree.AddAssignOp) {
            return getSummableDeclaration();
        }
        else if (that instanceof Tree.RemainderAssignOp) {
            return getIntegralDeclaration();
        }
        else {
            return getNumericDeclaration();
        }
    }

    @Override public void visit(Tree.BitwiseOp that) {
        super.visit(that);
        visitBinaryOperator(that, getSlotsDeclaration());
    }

    @Override public void visit(Tree.LogicalOp that) {
        super.visit(that);
        visitBinaryOperator(that, getBooleanDeclaration());
    }

    @Override public void visit(Tree.EqualityOp that) {
        super.visit(that);
        visitComparisonOperator(that, getEqualityDeclaration());
    }

    @Override public void visit(Tree.ComparisonOp that) {
        super.visit(that);
        visitComparisonOperator(that, getComparableDeclaration());
    }

    @Override public void visit(Tree.IdenticalOp that) {
        super.visit(that);
        visitComparisonOperator(that, getIdentifiableObjectDeclaration());
    }

    @Override public void visit(Tree.CompareOp that) {
        super.visit(that);
        visitCompareOperator(that);
    }

    @Override public void visit(Tree.DefaultOp that) {
        super.visit(that);
        visitDefaultOperator(that);
    }
        
    @Override public void visit(Tree.NegativeOp that) {
        super.visit(that);
        visitNegativeOperator(that);
    }
        
    @Override public void visit(Tree.PositiveOp that) {
        super.visit(that);
        visitNegativeOperator(that);
    }
        
    @Override public void visit(Tree.FlipOp that) {
        super.visit(that);
        visitUnaryOperator(that, getSlotsDeclaration());
    }
        
    @Override public void visit(Tree.NotOp that) {
        super.visit(that);
        visitUnaryOperator(that, getBooleanDeclaration());
    }
        
    @Override public void visit(Tree.AssignOp that) {
        super.visit(that);
        visitAssignOperator(that);
        checkAssignable(that.getLeftTerm());
    }
        
    @Override public void visit(Tree.ArithmeticAssignmentOp that) {
        super.visit(that);
        visitArithmeticAssignOperator(that, getArithmeticDeclaration(that));
        checkAssignable(that.getLeftTerm());
    }
        
    @Override public void visit(Tree.LogicalAssignmentOp that) {
        super.visit(that);
        visitBinaryOperator(that, getBooleanDeclaration());
        checkAssignable(that.getLeftTerm());
    }
        
    @Override public void visit(Tree.BitwiseAssignmentOp that) {
        super.visit(that);
        visitBinaryOperator(that, getSlotsDeclaration());
        checkAssignable(that.getLeftTerm());
    }
        
    @Override public void visit(Tree.FormatOp that) {
        super.visit(that);
        visitFormatOperator(that);
    }
    
    @Override public void visit(Tree.RangeOp that) {
        super.visit(that);
        visitRangeOperator(that);
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
        
    @Override public void visit(Tree.InOp that) {
        super.visit(that);
        visitInOperator(that);
    }
        
    //Atoms:
    
    @Override public void visit(Tree.BaseMemberExpression that) {
        //TODO: this does not correctly handle methods
        //      and classes which are not subsequently 
        //      invoked (should return the callable type)
        TypedDeclaration member = (TypedDeclaration) getDeclaration(that.getScope(), that.getUnit(), that.getIdentifier(), context);
        if (member==null) {
            that.addError("could not determine target of member reference: " +
                    that.getIdentifier().getText());
        }
        else {
            that.setDeclaration(member);
            Tree.TypeArgumentList tal = that.getTypeArgumentList();
            if (explicitTypeArguments(member, tal)) {
                visitBaseMemberExpression(that, member, getTypeArguments(tal), tal);
            }
            //otherwise infer type arguments later
        }
    }

    @Override public void visit(Tree.QualifiedMemberExpression that) {
        that.getPrimary().visit(this);
        ProducedType pt = that.getPrimary().getTypeModel();
        if (pt!=null && that.getIdentifier()!=null) {
            TypedDeclaration member = (TypedDeclaration) getMemberDeclaration(unwrap(pt, that).getDeclaration(), that.getIdentifier(), context);
            if (member==null) {
                that.addError("could not determine target of member reference: " +
                        that.getIdentifier().getText());
            }
            else {
                that.setDeclaration(member);
                if (!member.isVisible(that.getScope())) {
                    that.addError("target of member reference is not shared: " +
                            that.getIdentifier().getText());
                }
                Tree.TypeArgumentList tal = that.getTypeArgumentList();
                if (explicitTypeArguments(member,tal)) {
                    visitQualifiedMemberExpression(that, member, getTypeArguments(tal), tal);
                }
                //otherwise infer type arguments later
            }
        }
    }

    private void visitQualifiedMemberExpression(Tree.QualifiedMemberExpression that,
            TypedDeclaration member, List<ProducedType> typeArgs, Tree.TypeArgumentList tal) {
        ProducedType receiverType = unwrap(that.getPrimary().getTypeModel(), that);
        if (acceptsTypeArguments(receiverType, member, typeArgs, tal, that)) {
            ProducedTypedReference ptr = receiverType.getTypedMember(member, typeArgs);
            if (ptr==null) {
                that.addError("member not found: " + 
                        member.getName() + " of type " + 
                        receiverType.getDeclaration().getName());
            }
            else {
                ProducedType t = ptr.getType();
                that.setTarget(ptr); //TODO: how do we wrap ptr???
                that.setTypeModel(wrap(t, that)); //TODO: this is not correct, should be Callable
            }
        }
    }
    
    private void visitBaseMemberExpression(Tree.BaseMemberExpression that, TypedDeclaration member, 
            List<ProducedType> typeArgs, Tree.TypeArgumentList tal) {
        ProducedType outerType;
        if (acceptsTypeArguments(member, typeArgs, tal, that)) {
            if ( member.isMember() ) {
                outerType = that.getScope().getDeclaringType(member);
            }
            else {
                //it must be a member of an outer scope
               outerType = null;
            }
            ProducedTypedReference pr = member.getProducedTypedReference(outerType, typeArgs);
            that.setTarget(pr);
            ProducedType t = pr.getType();
            if (t==null) {
                that.addError("could not determine type of member reference: " +
                        that.getIdentifier().getText());
            }
            else {
                that.setTypeModel(t);
            }
        }
    }

    @Override public void visit(Tree.BaseTypeExpression that) {
        TypeDeclaration type = (TypeDeclaration) getDeclaration(that.getScope(), that.getUnit(), that.getIdentifier(), context);
        if (type==null) {
            that.addError("could not determine target of type reference: " + 
                    that.getIdentifier().getText());
        }
        else {
            that.setDeclaration(type);
            Tree.TypeArgumentList tal = that.getTypeArgumentList();
            if (explicitTypeArguments(type, tal)) {
                visitBaseTypeExpression(that, type, getTypeArguments(tal), tal);
            }
            //otherwise infer type arguments later
        }
    }
        
    @Override public void visit(Tree.QualifiedTypeExpression that) {
        that.getPrimary().visit(this);
        ProducedType pt = that.getPrimary().getTypeModel();
        if (pt!=null) {
            TypeDeclaration type = (TypeDeclaration) getMemberDeclaration(unwrap(pt, that).getDeclaration(), that.getIdentifier(), context);
            if (type==null) {
                that.addError("could not determine target of member type reference: " +
                        that.getIdentifier().getText());
            }
            else {
                that.setDeclaration(type);
                if (!type.isVisible(that.getScope())) {
                    that.addError("target of member type reference is not shared: " +
                            that.getIdentifier().getText());
                }
                Tree.TypeArgumentList tal = that.getTypeArgumentList();
                if (explicitTypeArguments(type, tal)) {
                    visitQualifiedTypeExpression(that, type, getTypeArguments(tal), tal);
                    //otherwise infer type arguments later
                }
            }
        }
    }

    private boolean explicitTypeArguments(Declaration dec,
            Tree.TypeArgumentList tal) {
        return !dec.isParameterized() || tal!=null;
    }
    
    @Override public void visit(Tree.SimpleType that) {
        //this one is a declaration, not an expression!
        //we are only validating type arguments here
        ProducedType pt = that.getTypeModel();
        if (pt!=null) {
            TypeDeclaration type = pt.getDeclaration();
            Tree.TypeArgumentList tal = that.getTypeArgumentList();
            //No type inference for declarations
            List<ProducedType> typeArguments = getTypeArguments(tal);
            acceptsTypeArguments(type, typeArguments, tal, that);
            //the type has already been set by TypeVisitor
        }
    }
        

    private void visitQualifiedTypeExpression(Tree.QualifiedTypeExpression that,
            TypeDeclaration type, List<ProducedType> typeArgs, Tree.TypeArgumentList tal) {
        ProducedType receiverType = unwrap(that.getPrimary().getTypeModel(), that);
        if (acceptsTypeArguments(receiverType, type, typeArgs, tal, that)) {
            ProducedType t = receiverType.getTypeMember(type, typeArgs);
            that.setTypeModel(wrap(t, that)); //TODO: this is not correct, should be Callable
            that.setTarget(t);
        }
    }

    private void visitBaseTypeExpression(Tree.BaseTypeExpression that, TypeDeclaration type, 
            List<ProducedType> typeArgs, Tree.TypeArgumentList tal) {
        if ( acceptsTypeArguments(type, typeArgs, tal, that) ) {
            ProducedType outerType;
            if (type.isMemberType()) {
                outerType = that.getScope().getDeclaringType(type);
            }
            else {
                outerType = null;
            }
            ProducedType t = type.getProducedType(outerType, typeArgs);
            that.setTypeModel(t); //TODO: this is not correct, should be Callable
            that.setTarget(t);
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
                that.addError("could not determine type of expression");
            }
            else {
                that.setTypeModel(t);
            }
        }
    }
    
    @Override public void visit(Tree.Outer that) {
        that.setTypeModel(getOuterType(that, that.getScope()));
    }

    private ProducedType getOuterType(Node that, Scope scope) {
        Boolean foundInner = false;
        while (!(scope instanceof Package)) {
            if (scope instanceof ClassOrInterface) {
                if (foundInner) {
                    return ((ClassOrInterface) scope).getType();
                }
                else {
                    foundInner = true;
                }
            }
            scope = scope.getContainer();
        }
        return null;
    }
    
    private boolean inExtendsClause = false;

    @Override 
    public void visit(Tree.ExtendedType that) {
        inExtendsClause = true;
        super.visit(that);
        inExtendsClause = false;
    }
    
    @Override public void visit(Tree.Super that) {
        if (inExtendsClause) {
            ClassOrInterface ci = getContainingClassOrInterface(that);
            if (ci!=null) {
                if (ci.isClassOrInterfaceMember()) {
                    ClassOrInterface s = (ClassOrInterface) ci.getContainer();
                    ProducedType t = s.getExtendedType();
                    //TODO: type arguments
                    that.setTypeModel(t);
                }
            }
        }
        else {
            ClassOrInterface ci = getContainingClassOrInterface(that);
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
    }
    
    @Override public void visit(Tree.This that) {
        ClassOrInterface ci = getContainingClassOrInterface(that);
        if (ci!=null) {
            that.setTypeModel(ci.getType());
        }
    }
    
    @Override public void visit(Tree.Subtype that) {
        //TODO!
    }
    
    @Override public void visit(Tree.SequenceEnumeration that) {
        super.visit(that);
        ProducedType st;
        if ( that.getExpressionList()==null ) {
            st = getEmptyDeclaration().getType();
        }
        else {
            ProducedType et;
            List<ProducedType> list = new ArrayList<ProducedType>();
            for (Tree.Expression e: that.getExpressionList().getExpressions()) {
                if (e.getTypeModel()!=null) {
                    addToUnion(list, e.getTypeModel());
                }
            }
            if (list.isEmpty()) {
                that.addError("could not infer type of sequence enumeration");
                return;
            }
            else if (list.size()==1) {
                et = list.get(0);
            }
            else {
                UnionType ut = new UnionType();
                ut.setExtendedType( getObjectDeclaration().getType() );
                ut.setCaseTypes(list);
                et = ut.getType(); 
            }
            st = getSequenceType(et);
        }
        that.setTypeModel(st);
    }

    private ProducedType getSequenceType(ProducedType et) {
        return producedType(getSequenceDeclaration(), et);
    }
    
    @Override public void visit(Tree.StringTemplate that) {
        super.visit(that);
        //TODO: validate that the subexpression types are Formattable
        setLiteralType(that, getStringDeclaration());
    }
    
    @Override public void visit(Tree.StringLiteral that) {
        setLiteralType(that, getStringDeclaration());
    }
    
    @Override public void visit(Tree.NaturalLiteral that) {
        setLiteralType(that, getNaturalDeclaration());
    }
    
    @Override public void visit(Tree.FloatLiteral that) {
        setLiteralType(that, getFloatDeclaration());
    }
    
    @Override public void visit(Tree.CharLiteral that) {
        setLiteralType(that, getCharacterDeclaration());
    }
    
    @Override public void visit(Tree.QuotedLiteral that) {
        setLiteralType(that, getQuotedDeclaration());
    }
    
    private void setLiteralType(Tree.Atom that, TypeDeclaration languageType) {
        that.setTypeModel(languageType.getType());
    }
    
    @Override
    public void visit(Tree.CompilerAnnotation that) {
        //don't visit the argument       
    }

    private Interface getCorrespondenceDeclaration() {
        return (Interface) getLanguageDeclaration("Correspondence");
    }

    private Class getNothingDeclaration() {
        return (Class) getLanguageDeclaration("Nothing");
    }

    private Interface getEmptyDeclaration() {
        return (Interface) getLanguageDeclaration("Empty");
    }

    private Interface getSequenceDeclaration() {
        return (Interface) getLanguageDeclaration("Sequence");
    }

    private Interface getContainerDeclaration() {
        return (Interface) getLanguageDeclaration("Container");
    }
    
    private Class getObjectDeclaration() {
        return (Class) getLanguageDeclaration("Object");
    }
    
    private Interface getCategoryDeclaration() {
        return (Interface) getLanguageDeclaration("Category");
    }
    
    private Interface getIterableDeclaration() {
        return (Interface) getLanguageDeclaration("Iterable");
    }
    
    private Interface getCastableDeclaration() {
        return (Interface) getLanguageDeclaration("Castable");
    }
    
    private Interface getSummableDeclaration() {
        return (Interface) getLanguageDeclaration("Summable");
    }
        
    private Interface getNumericDeclaration() {
        return (Interface) getLanguageDeclaration("Numeric");
    }
        
    private Interface getIntegralDeclaration() {
        return (Interface) getLanguageDeclaration("Integral");
    }
        
    private Interface getInvertableDeclaration() {
        return (Interface) getLanguageDeclaration("Invertable");
    }
        
    private Interface getSlotsDeclaration() {
        return (Interface) getLanguageDeclaration("Slots");
    }
        
    private TypeDeclaration getComparisonDeclaration() {
        return getLanguageDeclaration("Comparison");
    }
        
    private TypeDeclaration getBooleanDeclaration() {
        return getLanguageDeclaration("Boolean");
    }
        
    private TypeDeclaration getStringDeclaration() {
        return getLanguageDeclaration("String");
    }
        
    private TypeDeclaration getFloatDeclaration() {
        return getLanguageDeclaration("Float");
    }
        
    private TypeDeclaration getNaturalDeclaration() {
        return getLanguageDeclaration("Natural");
    }
        
    private TypeDeclaration getCharacterDeclaration() {
        return getLanguageDeclaration("Character");
    }
        
    private TypeDeclaration getQuotedDeclaration() {
        return getLanguageDeclaration("Quoted");
    }
        
    private Interface getEqualityDeclaration() {
        return (Interface) getLanguageDeclaration("Equality");
    }
        
    private Interface getComparableDeclaration() {
        return (Interface) getLanguageDeclaration("Comparable");
    }
        
    private Class getIdentifiableObjectDeclaration() {
        return (Class) getLanguageDeclaration("IdentifiableObject");
    }
        
    private Interface getOrdinalDeclaration() {
        return (Interface) getLanguageDeclaration("Ordinal");
    }
        
    private Class getRangeDeclaration() {
        return (Class) getLanguageDeclaration("Range");
    }
        
    private Class getEntryDeclaration() {
        return (Class) getLanguageDeclaration("Entry");
    }
        
    private static boolean acceptsTypeArguments(Declaration member, List<ProducedType> typeArguments, 
            Tree.TypeArgumentList tal, Node parent) {
        return acceptsTypeArguments(null, member, typeArguments, tal, parent);
    }
    
    private static boolean acceptsTypeArguments(ProducedType receiver, Declaration member, List<ProducedType> typeArguments, 
            Tree.TypeArgumentList tal, Node parent) {
        if (member instanceof Generic) {
            List<TypeParameter> params = ((Generic) member).getTypeParameters();
            if ( params.size()==typeArguments.size() ) {
                for (int i=0; i<params.size(); i++) {
                    TypeParameter param = params.get(i);
                    ProducedType argType = typeArguments.get(i);
                    //Map<TypeParameter, ProducedType> self = Collections.singletonMap(param, arg);
                    for (ProducedType st: param.getSatisfiedTypes()) {
                        //sts = sts.substitute(self);
                        ProducedType sts = st.getProducedType(receiver, member, typeArguments);
                        if (argType!=null && !argType.isSubtypeOf(sts)) {
                            if (tal==null) {
                                argType.isSubtypeOf(sts);
                                parent.addError("inferred type argument " + argType.getProducedTypeName()
                                        + " to type parameter " + param.getName()
                                        + " of declaration " + member.getName()
                                        + " not assignable to " + sts.getProducedTypeName());
                            }
                            else {
                                tal.getTypes().get(i).addError("type parameter " + param.getName() 
                                        + " of declaration " + member.getName()
                                        + " has argument " + argType.getProducedTypeName() 
                                        + " not assignable to " + sts.getProducedTypeName());
                            }
                            return false;
                        }
                    }
                    if (param.getCaseTypes().size()>0) {
                        boolean found = false;
                        for (ProducedType ct: param.getCaseTypes()) {
                            ProducedType cts = ct.getProducedType(receiver, member, typeArguments);
                            if (argType.isSubtypeOf(cts)) found = true;
                        }
                        if (!found) {
                            if (tal==null) {
                                parent.addError("inferred type argument " + argType.getProducedTypeName()
                                		+ " to type parameter " + param.getName()
                                        + " of declaration " + member.getName()
                                        + " not one of the listed cases");
                            }
                            else {
                                tal.getTypes().get(i).addError("type parameter " + param.getName() 
                                        + " of declaration " + member.getName()
                                        + " has argument " + argType.getProducedTypeName() 
                                        + " not one of the listed cases");
                            }
                            return false;
                        }
                    }
                }
                return true;
            }
            else {
                if (tal==null) {
                    parent.addError("requires type arguments");
                }
                else {
                    tal.addError("wrong number of type arguments");
                }
                return false;
            }
        }
        else {
            boolean empty = typeArguments.isEmpty();
            if (!empty) {
                tal.addError("does not accept type arguments");
            }
            return empty;
        }
    }

}
