package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.typeDescription;
import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.typeNamesAsIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.areConsistentSupertypes;

import java.util.ArrayList;
import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.IntersectionType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

public class InheritanceVisitor extends Visitor {
    
    @Override public void visit(Tree.TypeDeclaration that) {
        validateSupertypes(that, that.getDeclarationModel());
        super.visit(that);
    }

    @Override public void visit(Tree.ObjectDefinition that) {
        validateSupertypes(that, 
                that.getDeclarationModel().getType().getDeclaration());
        super.visit(that);
    }

    @Override public void visit(Tree.ObjectArgument that) {
        validateSupertypes(that, 
                that.getAnonymousClass());
        super.visit(that);
    }

    @Override public void visit(Tree.ObjectExpression that) {
        validateSupertypes(that, 
                that.getAnonymousClass());
        super.visit(that);
    }

    @Override public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        validateUpperBounds(that, that.getDeclarationModel());
    }

    private void validateSupertypes(Node that, 
            TypeDeclaration td) {
        if (!(td instanceof TypeAlias)) {
            List<ProducedType> supertypes = td.getType().getSupertypes();
            for (int i=0; i<supertypes.size(); i++) {
                ProducedType st1 = supertypes.get(i);
                for (int j=i+1; j<supertypes.size(); j++) {
                    ProducedType st2 = supertypes.get(j);
                    checkSupertypeIntersection(that, td, st1, st2); //note: sets td.inconsistentType by side-effect
                }
            }
        }
    }
    private void checkSupertypeIntersection(Node that,
            TypeDeclaration td, ProducedType st1, ProducedType st2) {
        if (st1.getDeclaration().equals(st2.getDeclaration()) /*&& !st1.isExactly(st2)*/) {
            Unit unit = that.getUnit();
            if (!areConsistentSupertypes(st1, st2, unit)) {
                that.addError(typeDescription(td, unit) +
                        " has the same parameterized supertype twice with incompatible type arguments: '" +
                        st1.getProducedTypeName(unit) + " & " + 
                        st2.getProducedTypeName(unit) + "'");
               td.setInconsistentType(true);
            }
        }
    }

    private void validateUpperBounds(Tree.TypeConstraint that,
            TypeDeclaration td) {
        if (!td.isInconsistentType()) {
            Unit unit = that.getUnit();
            List<ProducedType> upperBounds = td.getSatisfiedTypes();
            List<ProducedType> list = 
                    new ArrayList<ProducedType>(upperBounds.size());
            for (ProducedType st: upperBounds) {
                addToIntersection(list, st, unit);
            }
            IntersectionType it = new IntersectionType(unit);
            it.setSatisfiedTypes(list);
            if (it.canonicalize().getType().isNothing()) {
                that.addError(typeDescription(td, unit) + 
                        " has unsatisfiable upper bound constraints: the constraints '" + 
                        typeNamesAsIntersection(upperBounds, unit) + 
                        "' cannot be satisfied by any type except 'Nothing'");
            }
        }
    }

}
