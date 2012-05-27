package com.redhat.ceylon.compiler.typechecker.analyzer;


import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkAssignable;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.checkIsExactly;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Generic;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Validates some simple rules relating to refinement.
 * 
 * @see TypeHierarchyVisitor for the fancy stuff!
 * 
 * @author Gavin King
 *
 */
public class RefinementVisitor extends Visitor {
    
    private boolean broken=false;
    
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

    @Override public void visit(Tree.TypeDeclaration that) {
        boolean ob = broken;
        broken = false;
        TypeDeclaration td = that.getDeclarationModel();
        if (td!=null) {
            List<ProducedType> supertypes = td.getType().getSupertypes();
            for (int i=0; i<supertypes.size(); i++) {
                ProducedType st1 = supertypes.get(i);
                for (int j=i+1; j<supertypes.size(); j++) {
                    ProducedType st2 = supertypes.get(j);
                    if (st1.getDeclaration().equals(st2.getDeclaration()) /*&& !st1.isExactly(st2)*/) {
                        if (!st1.isSubtypeOf(st2) && !st2.isSubtypeOf(st1)) {
                            that.addError("type " + td.getName() +
                                    " has the same supertype twice with incompatible type arguments: " +
                                    st1.getProducedTypeName() + " and " + st2.getProducedTypeName());
                            broken = true;
                        }
                    }
                }
                if (!isCompletelyVisible(td, st1)) {
                    that.addError("supertype of type is not visible everywhere type is visible: "
                            + st1.getProducedTypeName());
                }
            }
        }
        super.visit(that);
        broken = ob;
    }
    
    @Override public void visit(Tree.TypedDeclaration that) {
        TypedDeclaration td = that.getDeclarationModel();
        if ( td.getType()!=null && !isCompletelyVisible(td,td.getType()) ) {
            that.getType().addError("type of declaration is not visible everywhere declaration is visible: " 
                        + td.getName());
        }
        super.visit(that);
    }
    
    @Override public void visit(Tree.Declaration that) {
        super.visit(that);
        Declaration dec = that.getDeclarationModel();
        if (dec!=null) {
            boolean toplevel = dec.getContainer() instanceof Package;
            boolean member = dec.isClassOrInterfaceMember() && 
                    !(dec instanceof Parameter) &&
                    !(dec instanceof TypeParameter); //TODO: what about nested interfaces and abstract classes?!
            
            if (!toplevel && !member) {
                if (dec.isShared()) {
                    that.addError("shared declaration is not a member of a class, interface, or package", 1200);
                }
            }
            
            boolean mayBeShared = 
                    dec instanceof MethodOrValue || 
                    dec instanceof ClassOrInterface;
            if (!mayBeShared) {
                if (dec.isShared()) {
                    that.addError("shared member is not a method, attribute, class, or interface", 1200);
                }
            }
            
            boolean mayBeRefined = 
                    dec instanceof Getter || 
                    dec instanceof Value || 
                    dec instanceof Method ||
                    dec instanceof Class;
            if (!mayBeRefined) {
                checkNonrefinableDeclaration(that, dec);
            }

            if (!member) {
                checkNonMember(that, dec);
            }
            
            if ( !dec.isShared() ) {
                checkUnshared(that, dec);
            }
            
            if (member) {
                if (!(dec instanceof Setter)) {
                    checkMember(that, dec);
                }
                ClassOrInterface declaringType = (ClassOrInterface) dec.getContainer();
                Declaration refined = declaringType.getRefinedMember(dec.getName(), null);
                dec.setRefinedDeclaration(refined);
            }

        }

    }

    private void checkMember(Tree.Declaration that, Declaration dec) {
        ClassOrInterface ci = (ClassOrInterface) dec.getContainer();
        if (dec.isFormal() && ci instanceof Class) {
            Class c = (Class) ci;
            if (!c.isAbstract() && !c.isFormal()) {
                that.addError("formal member belongs to non-abstract, non-formal class", 1100);
            }
        }
        List<Declaration> others = ci.getInheritedMembers( dec.getName() );
        if (others.isEmpty()) {
            if (dec.isActual()) {
                that.addError("actual member does not refine any inherited member", 1300);
            }
        }
        else {
            for (Declaration refined: others) {
                if (dec instanceof Method) {
                    if (!(refined instanceof Method)) {
                        that.addError("refined declaration is not a method: " + 
                                message(refined));
                    }
                }
                else if (dec instanceof Class) {
                    if (!(refined instanceof Class)) {
                        that.addError("refined declaration is not a class: " + 
                                message(refined));
                    }
                }
                else if (dec instanceof TypedDeclaration) {
                    if (refined instanceof Class || refined instanceof Method) {
                        that.addError("refined declaration is not an attribute: " + 
                                message(refined));
                    }
                    else if (refined instanceof TypedDeclaration) {
                        if ( ((TypedDeclaration) refined).isVariable() && 
                                !((TypedDeclaration) dec).isVariable()) {
                            that.addError("non-variable attribute refines a variable attribute: " + 
                                    message(refined));
                        }
                    }
                }
                if (!dec.isActual()) {
                    that.addError("non-actual member refines an inherited member: " + 
                            message(refined), 600);
                }
                if (!refined.isDefault() && !refined.isFormal()) {
                    that.addError("member refines a non-default, non-formal member: " + 
                            message(refined), 500);
                }
                if (!broken) checkRefinedTypeAndParameterTypes(that, dec, ci, refined);
            }
        }
    }
    
    static String message(Declaration refined) {
        return refined.getName() + " in " + ((Declaration) refined.getContainer()).getName();
    }

    private void checkRefinedTypeAndParameterTypes(Tree.Declaration that,
            Declaration dec, ClassOrInterface ci, Declaration refined) {
        List<ProducedType> typeArgs = new ArrayList<ProducedType>();
        if (refined instanceof Generic && dec instanceof Generic) {
            List<TypeParameter> refinedTypeParams = ((Generic) refined).getTypeParameters();
            List<TypeParameter> refiningTypeParams = ((Generic) dec).getTypeParameters();
            int refiningSize = refiningTypeParams.size();
            int refinedSize = refinedTypeParams.size();
            if (refiningSize!=refinedSize) {
                that.addError("member does not have the same number of type parameters as refined member: " + 
                            message(refined));
            }
            for (int i=0; i<(refiningSize<=refinedSize ? refiningSize : refinedSize); i++) {
                TypeParameter refinedTypParam = refinedTypeParams.get(i);
                TypeParameter refiningTypeParam = refiningTypeParams.get(i);
                for (ProducedType t: refiningTypeParam.getSatisfiedTypes()) {
                    checkAssignable(refinedTypParam.getType(), t, that, 
                        "member type parameter " + refiningTypeParam.getName() +
                        " has constraint which refined member type parameter " + refinedTypParam.getName() +
                        " of " + message(refined) + " does not satisfy");
                }
                typeArgs.add(refinedTypParam.getType());
            }
        }
        ProducedReference refinedMember = ci.getType().getTypedReference(refined, typeArgs);
        ProducedReference refiningMember = ci.getType().getTypedReference(dec, typeArgs);
        if (refinedMember.getDeclaration() instanceof TypedDeclaration &&
                ((TypedDeclaration) refinedMember.getDeclaration()).isVariable()) {
            checkIsExactly(refiningMember.getType(), refinedMember.getType(), that,
                    "type of member must be exactly the same as type of variable refined member: " + 
                            message(refined));
        }
        else {
            //note: this version checks return type and parameter types in one shot, but the
            //resulting error messages aren't as friendly, so do it the hard way instead!
            //checkAssignable(refiningMember.getFullType(), refinedMember.getFullType(), that,
            checkAssignable(refiningMember.getType(), refinedMember.getType(), that,
                    "type of member must be assignable to type of refined member: " + 
                            message(refined));
        }
        if (dec instanceof Functional && refined instanceof Functional) {
           List<ParameterList> refiningParamLists = ((Functional) dec).getParameterLists();
           List<ParameterList> refinedParamLists = ((Functional) refined).getParameterLists();
           if (refinedParamLists.size()!=refiningParamLists.size()) {
               that.addError("member must have the same number of parameter lists as refined member: " + 
                            message(refined));
           }
           for (int i=0; i<refinedParamLists.size() && i<refiningParamLists.size(); i++) {
               checkParameterTypes(that, getParameterList(that, i), 
                       refiningMember, refinedMember, 
                       refiningParamLists.get(i), refinedParamLists.get(i));
           }
        }
    }

    private void checkUnshared(Tree.Declaration that, Declaration dec) {
        if (dec.isActual()) {
            that.addError("actual member is not shared", 701);
        }
        if (dec.isFormal()) {
            that.addError("formal member is not shared", 702);
        }
        if (dec.isDefault()) {
            that.addError("default member is not shared", 703);
        }
    }

    private void checkNonrefinableDeclaration(Tree.Declaration that,
            Declaration dec) {
        if (dec.isActual()) {
            that.addError("actual declaration is not a getter, simple attribute, or class", 1301);
        }
        if (dec.isFormal()) {
            that.addError("formal declaration is not a getter, simple attribute, or class", 1302);
        }
        if (dec.isDefault()) {
            that.addError("default declaration is not a getter, simple attribute, or class", 1303);
        }
    }

    private void checkNonMember(Tree.Declaration that, Declaration dec) {
        if (dec.isActual()) {
            that.addError("actual declaration is not a member of a class or interface", 1301);
        }
        if (dec.isFormal()) {
            that.addError("formal declaration is not a member of a class or interface", 1302);
        }
        if (dec.isDefault()) {
            that.addError("default declaration is not a member of a class or interface", 1303);
        }
    }

    private void checkParameterTypes(Tree.Declaration that, Tree.ParameterList pl,
            ProducedReference member, ProducedReference refinedMember,
            ParameterList params, ParameterList refinedParams) {
        if (params.getParameters().size()!=refinedParams.getParameters().size()) {
           that.addError("member does not have the same number of parameters as the member it refines");
        }
        else {
            for (int i=0; i<params.getParameters().size(); i++) {
                Parameter rparam = refinedParams.getParameters().get(i);
                ProducedType refinedParameterType = refinedMember.getTypedParameter(rparam).getFullType();
                Parameter param = params.getParameters().get(i);
                ProducedType parameterType = member.getTypedParameter(param).getFullType();
                Tree.Parameter p = pl.getParameters().get(i);
                if (p!=null) {
                    Tree.Type type = p.getType(); //some kind of syntax error
                    if (type!=null) {
                        if (refinedParameterType==null || parameterType==null) {
                            type.addError("could not determine if parameter type is the same as the corresponding parameter of refined member");
                        }
                        else {
                            //TODO: consider type parameter substitution!!!
                            checkIsExactly(parameterType, refinedParameterType, type, "type of parameter " + 
                                    param.getName() + " is different to type of corresponding parameter " +
                                    rparam.getName() + " of refined member");
                        }
                    }
                }
                param.setDefaulted(rparam.isDefaulted());
            }
        }
    }

    private static Tree.ParameterList getParameterList(Tree.Declaration that, int i) {
        Tree.ParameterList pl;
        if (that instanceof Tree.AnyMethod) {
            pl = ((Tree.AnyMethod) that).getParameterLists().get(i);
        }
        else {
            pl = ((Tree.ClassDefinition) that).getParameterList();
        }
        return pl;
    }
    
}
