package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class RefinementVisitor extends Visitor {
    
    @Override public void visit(Tree.Declaration that) {
        super.visit(that);
        Declaration dec = that.getDeclarationModel();
        if (dec!=null) {
            boolean toplevel = dec.getContainer() instanceof Package;
            boolean member = (dec.getContainer() instanceof ClassOrInterface) && 
                    !(dec instanceof Parameter) &&
                    !(dec instanceof TypeParameter); //TODO: what about nested interfaces and abstract classes?!
            
            if (!toplevel && !member) {
                if (dec.isShared()) {
                    that.addError("shared declaration is not a member of a class, interface, or package");
                }
            }
            
            boolean mayBeShared = 
                    dec instanceof MethodOrValue || 
                    dec instanceof ClassOrInterface;
            if (!mayBeShared) {
                if (dec.isShared()) {
                    that.addError("shared member is not a method, attribute, class, or interface");
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
                checkMember(that, dec);
            }

        }

    }

    private void checkMember(Tree.Declaration that, Declaration dec) {
        ClassOrInterface ci = (ClassOrInterface) dec.getContainer();
        if (dec.isFormal() && (ci instanceof Class)) {
            Class c = (Class) ci;
            if (!c.isAbstract() && !c.isFormal()) {
                that.addError("formal member belongs to non-abstract, non-formal class");
            }
        }
        List<Declaration> others = ci.getInheritedMembers( dec.getName() );
        if (others.isEmpty()) {
            if (dec.isActual()) {
                that.addError("actual member does not refine any inherited member");
            }
        }
        else {
            for (Declaration refined: others) {
                if (!dec.isActual()) {
                    that.addError("non-actual member refines an inherited member");
                }
                if (!refined.isDefault() && !refined.isFormal()) {
                    that.addError("member refines a non-default, non-formal member");
                }
                if (dec instanceof TypedDeclaration) {
                    TypedDeclaration tdec = (TypedDeclaration) dec;
                    ProducedType type = tdec.getType();
                    TypedDeclaration trefined = (TypedDeclaration) refined;
                    ProducedType refinedType = trefined.getType();
                    if (type!=null) {
                        if (refinedType==null) {
                            that.addError("could not determine type of refined member");
                        }
                        else if (!type.isSubtypeOf(refinedType)) {
                            ((Tree.TypedDeclaration) that).getType().addError(
                                    "member type is not a subtype of refined member type: " +
                                    type.getProducedTypeName() + " is not " + 
                                    refinedType.getProducedTypeName());
                        }
                    }
                    if (dec instanceof Method) {
                       if (!(refined instanceof Method)) {
                           that.addError("method refines an attribute");
                       }
                       else {
                           ParameterList params = ((Method) dec).getParameterLists().get(0);
                           ParameterList refinedParams = ((Method) refined).getParameterLists().get(0);
                           checkParameterTypes(that, params, refinedParams);
                       }
                    }
                    else {
                        if (refined instanceof Method) {
                            that.addError("attribute refines a method");
                        }
                        else {
                            if (trefined.isVariable() && !tdec.isVariable()) {
                                that.addError("non-variable attribute refines a variable attribute");
                            }
                        }
                    }
                }
                else if (dec instanceof Class) {
                    if (!(refined instanceof Class)) {
                        that.addError("refined declaration is not a class");
                    }
                    else {
                        ProducedType type = ((Class) dec).getType();
                        ProducedType refinedType = ((Class) refined).getType();
                        if (!type.isSubtypeOf(refinedType)) {
                            that.addError("member class is not a subclass of refined class: " +
                                    dec.getName() + " is not " + 
                                    refined.getName());
                        }
                        ParameterList params = ((Class) dec).getParameterList();
                        ParameterList refinedParams = ((Class) refined).getParameterList();
                        checkParameterTypes(that, params, refinedParams);
                    }
                }
            }
            if (others.size()>1) {
                //TODO: this is broken for recursive refinement
                that.addError("member refines multiple inherited members");
            }
        }
    }

    private void checkUnshared(Tree.Declaration that, Declaration dec) {
        if (dec.isActual()) {
            that.addError("actual member is not shared");
        }
        if (dec.isFormal()) {
            that.addError("formal member is not shared");
        }
        if (dec.isDefault()) {
            that.addError("default member is not shared");
        }
    }

    private void checkNonrefinableDeclaration(Tree.Declaration that,
            Declaration dec) {
        if (dec.isActual()) {
            that.addError("actual declaration is not a getter, simple attribute, or class");
        }
        if (dec.isFormal()) {
            that.addError("formal declaration is not a getter, simple attribute, or class");
        }
        if (dec.isDefault()) {
            that.addError("default declaration is not a getter, simple attribute, or class");
        }
    }

    private void checkNonMember(Tree.Declaration that, Declaration dec) {
        if (dec.isActual()) {
            that.addError("actual declaration is not a member of a class, interface, or package");
        }
        if (dec.isFormal()) {
            that.addError("formal declaration is not a member of a class, interface, or package");
        }
        if (dec.isDefault()) {
            that.addError("default declaration is not a member of a class, interface, or package");
        }
    }

    private void checkParameterTypes(Tree.Declaration that,
            ParameterList params, ParameterList refinedParams) {
        if (params.getParameters().size()!=refinedParams.getParameters().size()) {
           that.addError("member does not have the same number of parameters as the member it refines");
        }
        else {
            for (int i=0; i<params.getParameters().size(); i++) {
                ProducedType refinedParameterType = refinedParams.getParameters().get(i).getType();
                ProducedType parameterType = params.getParameters().get(i).getType();
                Tree.Type type = getParameterList(that).getParameters().get(i).getType(); //some kind of syntax error
                if (type!=null) {
                    if (refinedParameterType==null || parameterType==null) {
                        type.addError("could not determine if parameter type is the same as the corresponding parameter of refined member");
                    }
                    else if (!parameterType.isExactly(refinedParameterType)) {
                        //TODO: consider type parameter substitution!!!
                        type.addError("type of parameter " + 
                                        params.getParameters().get(i).getName() + " is different to type of corresponding parameter " +
                                        refinedParams.getParameters().get(i).getName() + " of refined member: " +
                                        parameterType.getProducedTypeName() + " is not exactly " +
                                        refinedParameterType.getProducedTypeName());
                    }
                }
            }
        }
    }

    private static Tree.ParameterList getParameterList(Tree.Declaration that) {
        Tree.ParameterList pl;
        if (that instanceof Tree.AnyMethod) {
            pl = ((Tree.AnyMethod) that).getParameterLists().get(0);
        }
        else {
            pl = ((Tree.ClassDefinition) that).getParameterList();
        }
        return pl;
    }
    
}
