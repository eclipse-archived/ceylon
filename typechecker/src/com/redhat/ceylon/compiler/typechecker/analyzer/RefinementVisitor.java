package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.typechecker.model.Class;

public class RefinementVisitor extends Visitor {
    
    @Override public void visit(Tree.Declaration that) {
        super.visit(that);
        Declaration dec = that.getDeclarationModel();
        if (dec!=null && !(dec instanceof Setter)) {
            if ( dec.isShared() ) {
                if ( dec.getContainer() instanceof ClassOrInterface ) {
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
                    else if (others.size()==1) {
                        Declaration refined = others.get(0);
                        if (!dec.isActual()) {
                            that.addError("non-actual member refines an inherited member");
                        }
                        if (!refined.isDefault() && !refined.isFormal()) {
                            that.addError("member refines a non-default, non-formal member");
                        }
                    }
                    else {
                        that.addError("member refines multiple inherited members");
                    }
                }
                else {
                    that.addError("shared declaration does not belong to a class or interface");
                }
            }
            else {
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
        }
    }
    
}
