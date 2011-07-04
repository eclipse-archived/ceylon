package com.redhat.ceylon.compiler.typechecker.analyzer;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class TypeHierarchyVisitor extends Visitor {

    private Map<ClassOrInterface, Map<String, Declaration>> formalPerClassOrInterface = new HashMap<ClassOrInterface, Map<String, Declaration>>();

    public void visit(Tree.ClassOrInterface that) {
        super.visit(that);
        final ClassOrInterface classOrInterface = that.getDeclarationModel();
        boolean concrete = !classOrInterface.isAbstract() && !classOrInterface.isFormal();
        if (concrete) {
            //has to be a class since it's concrete
            final Class clazz = (Class) classOrInterface;
            final Map<String, Declaration> superFormals = getFormals(clazz.getExtendedTypeDeclaration());
            final Map<String, Declaration> superFormalsCopy = new HashMap<String, Declaration>(superFormals);
            boolean failure = false;
            if (superFormalsCopy.size()!=0) {
                StringBuilder sb = new StringBuilder("formal member(s) of superclasses not implemented: ");
                for (Declaration member : clazz.getMembers()) {
                    //subclass member override superclass member
                    if (!member.isFormal()&&superFormalsCopy.containsKey(member.getName())) {
                        superFormalsCopy.remove(member.getName());
                    }
                }
                if (superFormalsCopy.size() != 0) {
                    for(Declaration d : superFormalsCopy.values()) {
                        sb.append(d.toString()).append(", ");
                    }
                    sb.delete(sb.length()-2, sb.length());
                    that.addError(sb.toString());
                }
            };
        }


//        final ProducedType extendedType = classOrInterface.getExtendedType();
//        final Class extendedTypeDeclaration = classOrInterface.getExtendedTypeDeclaration();
//        extendedTypeDeclaration.getExtendedTypeDeclaration().getExtendedTypeDeclaration().getExtendedTypeDeclaration()
//        classOrInterface.getSatisfiedTypes();
    }

    private Map<String, Declaration> getFormals(Class clazz) {
        if (clazz==null) {
            return Collections.emptyMap();
        }
        Map<String, Declaration> formals = formalPerClassOrInterface.get(clazz);
        if (formals==null) {
            //conservative copy
            formals = new HashMap<String, Declaration>(getFormals(clazz.getExtendedTypeDeclaration()));
            for (Declaration member : clazz.getMembers()) {
                //subclass member override superclass member
                if (formals.containsKey(member.getName())) {
                    formals.remove(member.getName());
                }
                if (member.isFormal()) {
                    formals.put(member.getName(), member);
                }
            }
            formalPerClassOrInterface.put(clazz, formals);
        }
        return formals;
    }
}
