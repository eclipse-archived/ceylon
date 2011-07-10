package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.analyzer.Util.name;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Check that all former members of superclasses and interfaces are implemented in concrete classes
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class TypeHierarchyVisitor extends Visitor {

    private final Map<TypeDeclaration,Type> types = new HashMap<TypeDeclaration, Type>();

    private static final class Type {
        public Map<String,Members> membersByName = new HashMap<String, Members>();
        public TypeDeclaration declaration;
        public static final class Members {
            public String name;
            public Set<Declaration> formals = new HashSet<Declaration>();
            public Set<Declaration> actuals = new HashSet<Declaration>();
            public Set<Declaration> defaults = new HashSet<Declaration>();
            public Set<Declaration> nonFormalsNonDefaults = new HashSet<Declaration>();
        }

        @Override public String toString() {
            return declaration.getName();
        }
    }

    public void visit(Tree.ClassOrInterface that) {
        super.visit(that);
        final ClassOrInterface classOrInterface = that.getDeclarationModel();
        boolean concrete = !classOrInterface.isAbstract() && !classOrInterface.isFormal();
        if (concrete) {
            List<Type> orderedTypes = sortDAGAndBuildMetadata(classOrInterface, that);
            int size = orderedTypes.size();
            Type aggregation = new Type();
            for (int index = size-1;index>=0;index--) {
                Type current = orderedTypes.get(index);
                for (Type.Members currentMembers:current.membersByName.values()) {
                    Type.Members aggregateMembers = aggregation.membersByName.get(currentMembers.name);
                    if (aggregateMembers==null) {
                        aggregateMembers = new Type.Members();
                        aggregateMembers.name = currentMembers.name;
                        aggregation.membersByName.put(currentMembers.name,aggregateMembers);
                    }
                    aggregateMembers.nonFormalsNonDefaults.addAll(currentMembers.nonFormalsNonDefaults);
                    aggregateMembers.actuals.addAll(currentMembers.actuals);
                    aggregateMembers.formals.addAll(currentMembers.formals);
                    aggregateMembers.defaults.addAll(currentMembers.defaults);
                }
            }
            //verify hierarchy sanity
            for (Type.Members members:aggregation.membersByName.values()) {
                if (members.formals.size()!=0&&members.actuals.size()==0) {
                    that.addError("formal member " + members.name + " not implemented in class hierarchy");
                }
            }
        }
    }

    private List<Type> sortDAGAndBuildMetadata(TypeDeclaration declaration, Node errorReporter) {
        //Apply a partial sort on the class hierarchy which is a Directed Acyclic Graph (DAG)
        // with subclasses pointing to superclasses or interfaces
        //use depth-first plus a stack fo processed nodes to detect non DAG
        //http://en.wikipedia.org/wiki/Topological_sorting
        List<Type> sortedDag = new ArrayList<Type>();
        List<TypeDeclaration> visitedDeclarationPerBranch = new ArrayList<TypeDeclaration>();
        Set<TypeDeclaration> visited = new HashSet<TypeDeclaration>();
        visitDAGNode(declaration, sortedDag, visited, visitedDeclarationPerBranch, errorReporter);
        return sortedDag;
    }

    private void visitDAGNode(TypeDeclaration declaration, List<Type> sortedDag, Set<TypeDeclaration> visited,
            List<TypeDeclaration> stackOfProcessedType, Node errorReporter) {
        if (declaration == null) {
            return;
        }
        if (checkCyclicInheritance(declaration, stackOfProcessedType, errorReporter)) {
            return; //stop the cycle here but try and process the rest
        }

        if ( visited.contains(declaration) ) {
            return;
        }
        visited.add(declaration);
        Type type = getOrBuildType(declaration);

        stackOfProcessedType.add(declaration);
        visitDAGNode(declaration.getExtendedTypeDeclaration(), sortedDag, visited, stackOfProcessedType, errorReporter);
        for (TypeDeclaration superSatisfiedType : declaration.getSatisfiedTypeDeclarations()) {
            visitDAGNode(superSatisfiedType, sortedDag, visited, stackOfProcessedType, errorReporter);
        }
        stackOfProcessedType.remove(stackOfProcessedType.size()-1);
        sortedDag.add(type);
    }

    private boolean checkCyclicInheritance(TypeDeclaration declaration, List<TypeDeclaration> stackOfProcessedType, Node errorReporter) {
        final int matchingIndex = stackOfProcessedType.indexOf(declaration);
        if (matchingIndex!=-1) {
            StringBuilder sb = new StringBuilder("Cyclical dependencies in ");
            sb.append(declaration.getQualifiedNameString());
            sb.append(" (involving ");
            for (int index = stackOfProcessedType.size()-1;index>matchingIndex;index--) {
                sb.append(stackOfProcessedType.get(index).getName()).append(", ");
            }
            removeTrailing(", ", sb);
            sb.append(")");
            errorReporter.addError(sb.toString());
            return true;
        }
        return false;
    }

    private void removeTrailing(String trailingString, StringBuilder sb) {
        final int length = sb.length();
        sb.delete(length-trailingString.length(), length);
    }

    private Type getOrBuildType(TypeDeclaration declaration) {
        Type type = types.get(declaration);
        if (type == null) {
            type = new Type();
            type.declaration = declaration;
            for (Declaration member : declaration.getMembers()) {
                final String name = member.getName();
                Type.Members members = type.membersByName.get(name);
                if (members==null) {
                    members = new Type.Members();
                    members.name = name;
                    type.membersByName.put(name,members);
                }
                if (member.isActual()) {
                    members.actuals.add(member);
                }
                if (member.isFormal()) {
                    members.formals.add(member);
                }
                if (member.isDefault()) {
                    members.defaults.add(member);
                }
                if (!member.isFormal()&&!member.isDefault()) {
                    members.nonFormalsNonDefaults.add(member);
                }
            }
            types.put(declaration,type);
        }
        return type;
    }
}
