package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.name;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Check that all former members of superclasses and interfaces are implemented in concrete classes
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class TypeHierarchyVisitor extends Visitor {

    private Map<TypeDeclaration, Map<String, Declaration>> unansweredFormalPerClassOrInterface = new HashMap<TypeDeclaration, Map<String, Declaration>>();
    private Map<TypeDeclaration, Map<String, Declaration>> actualPerClassOrInterface = new HashMap<TypeDeclaration, Map<String, Declaration>>();

    public void visit(Tree.ClassOrInterface that) {
        super.visit(that);
        final ClassOrInterface classOrInterface = that.getDeclarationModel();
        boolean concrete = !classOrInterface.isAbstract() && !classOrInterface.isFormal();
        if (concrete) {
            computeFormalActual(classOrInterface);
            final Map<String, Declaration> allFormals = new HashMap<String, Declaration>();
            final Map<String, Declaration> allActuals = new HashMap<String, Declaration>();
            //populate formals and actuals from super types
            for (TypeDeclaration superSatisfiedType : classOrInterface.getSatisfiedTypeDeclarations()) {
                allFormals.putAll(unansweredFormalPerClassOrInterface.get(superSatisfiedType));
                allActuals.putAll(actualPerClassOrInterface.get(superSatisfiedType));
            }
            final Class extendedTypeDeclaration = classOrInterface.getExtendedTypeDeclaration();
            allFormals.putAll(unansweredFormalPerClassOrInterface.get(extendedTypeDeclaration));
            allActuals.putAll(actualPerClassOrInterface.get(extendedTypeDeclaration));
            //remove unnecessary formals
            for (String name : allActuals.keySet()) {
                allFormals.remove(name);
            }
            if (allFormals.size()!=0) {
                StringBuilder sb = new StringBuilder("formal member(s) of superclasses not implemented in [");
                sb.append(name(that.getIdentifier())).append("]: ");
                for (Declaration member : classOrInterface.getMembers()) {
                    //subclass member override superclass member
                    if (!member.isFormal() && allFormals.containsKey(member.getName())) {
                        allFormals.remove(member.getName());
                    }
                }
                if (allFormals.size()!=0) {
                    for (Declaration d : allFormals.values()) {
                        sb.append(d.toString()).append(", ");
                    }
                    sb.delete(sb.length() - 2, sb.length());
                    that.addError(sb.toString());
                }
            }
        }
    }

    private void computeFormalActual(TypeDeclaration typeDeclaration) {
        if (typeDeclaration==null) {
            return;
        }
        Map<String, Declaration> formals = unansweredFormalPerClassOrInterface.get(typeDeclaration);
        if (formals==null) {
            //conservative copy
            formals = new HashMap<String, Declaration>();
            Map<String, Declaration> actuals = new HashMap<String, Declaration>();
            //interface before superclass seems better;
            for (TypeDeclaration superSatisfiedType : typeDeclaration.getSatisfiedTypeDeclarations()) {
                populateFromType(formals, actuals, superSatisfiedType);
            }
            final Class extendedTypeDeclaration = typeDeclaration.getExtendedTypeDeclaration();
            populateFromType(formals, actuals, extendedTypeDeclaration);

            for (Declaration member : typeDeclaration.getMembers()) {
                //subclass member override superclass member
                if (formals.containsKey(member.getName())) {
                    formals.remove(member.getName());
                }
                if (member.isFormal()) {
                    formals.put(member.getName(), member);
                }
                else {
                    actuals.put(member.getName(), member);
                }
            }
            unansweredFormalPerClassOrInterface.put(typeDeclaration, formals);
            actualPerClassOrInterface.put(typeDeclaration, actuals);
        }
    }

    private void populateFromType(Map<String, Declaration> formals, Map<String, Declaration> actuals, TypeDeclaration superSatisfiedType) {
        if (superSatisfiedType!=null) {
            computeFormalActual(superSatisfiedType);
            formals.putAll(unansweredFormalPerClassOrInterface.get(superSatisfiedType));
            actuals.putAll(actualPerClassOrInterface.get(superSatisfiedType));
        }
    }
}
