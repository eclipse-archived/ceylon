package com.redhat.ceylon.compiler.typechecker.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
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
            checkForFormalsNotImplemented(that, orderedTypes);
            checkForDoubleMemberInheritanceWoCommonAncestor(that, orderedTypes);
            checkForDoubleMemberInheritanceNotOverridden(that, orderedTypes);
        }
    }

    private void checkForDoubleMemberInheritanceWoCommonAncestor(Tree.ClassOrInterface that, List<Type> orderedTypes) {
        Type aggregateType = new Type();
        for(Type currentType : orderedTypes) {
            for (Type.Members currentTypeMembers:currentType.membersByName.values()) {
                String name = currentTypeMembers.name;
                Type.Members aggregateMembers = aggregateType.membersByName.get(name);
                if (aggregateMembers==null) {
                    //not accumulated yet, no need to check
                    aggregateMembers = new Type.Members();
                    aggregateMembers.name = name;
                    aggregateType.membersByName.put(name,aggregateMembers);
                }
                else {
                    boolean isMemberNameOnAncestor = isMemberNameOnAncestor(currentType, name);
                    if (!isMemberNameOnAncestor) {
                        StringBuilder sb = new StringBuilder("may not inherit two declarations with the same name that do not share a common supertype: ");
                        sb.append("[").append(currentType.declaration.getQualifiedNameString()).append("#").append(name).append("]");
                        String otherTypeName = getTypeDeclarationFor(aggregateMembers);
                        sb.append(" and [").append(otherTypeName).append("#").append(name).append("]");
                        that.addError(sb.toString());
                    }
                }
                aggregateMembers.nonFormalsNonDefaults.addAll(currentTypeMembers.nonFormalsNonDefaults);
                aggregateMembers.actuals.addAll(currentTypeMembers.actuals);
                aggregateMembers.formals.addAll(currentTypeMembers.formals);
                aggregateMembers.defaults.addAll(currentTypeMembers.defaults);
            }
        }
    }

    private boolean isMemberNameOnAncestor(Type currentType, String name) {
        List<Declaration> inheritedMembers = currentType.declaration.getInheritedMembers(name);
        boolean sameMemberInherited = false;
        for(Declaration d:inheritedMembers) {
            if (!(d instanceof Parameter)) {
                sameMemberInherited = true;
                break;
            }
        }
        return sameMemberInherited;
    }

    private void checkForDoubleMemberInheritanceNotOverridden(Tree.ClassOrInterface that, List<Type> orderedTypes) {
        Type aggregateType = new Type();
        int size = orderedTypes.size();
        for(int index = size -1;index>=0;index--) {
            Type currentType = orderedTypes.get(index);
            for (Type.Members currentTypeMembers:currentType.membersByName.values()) {
                String name = currentTypeMembers.name;
                Type.Members aggregateMembers = aggregateType.membersByName.get(name);
                if (aggregateMembers==null) {
                    //not accumulated yet, no need to check
                    aggregateMembers = new Type.Members();
                    aggregateMembers.name = name;
                    aggregateType.membersByName.put(name,aggregateMembers);
                }
                else {

                    boolean isMemberRefined = isMemberRefined(orderedTypes,index,name,currentTypeMembers);
                    if (!isMemberRefined) {
                        StringBuilder sb = new StringBuilder("may not inherit two declarations with the same name unless redefined in subclass: ");
                        sb.append("[").append(currentType.declaration.getQualifiedNameString()).append("#").append(name).append("]");
                        String otherTypeName = getTypeDeclarationFor(aggregateMembers);
                        sb.append(" and [").append(otherTypeName).append("#").append(name).append("]");
                        that.addError(sb.toString());
                    }
                }
                aggregateMembers.nonFormalsNonDefaults.addAll(currentTypeMembers.nonFormalsNonDefaults);
                aggregateMembers.actuals.addAll(currentTypeMembers.actuals);
                aggregateMembers.formals.addAll(currentTypeMembers.formals);
                aggregateMembers.defaults.addAll(currentTypeMembers.defaults);
            }
        }
    }

    private boolean isMemberRefined(List<Type> orderedTypes, int index, String name, Type.Members currentTypeMembers) {
        int size = orderedTypes.size();
        Declaration declarationOfSupertypeMember = getMemberDeclaration(currentTypeMembers);
        for (int subIndex = size-1 ; subIndex>index;subIndex--) {
            Type type = orderedTypes.get(subIndex);
            //has a direct member and su[ertype as inherited members
            Declaration directMember = type.declaration.getDirectMember(name);
            boolean isMemberRefined = directMember!=null && !(directMember instanceof Parameter);
            isMemberRefined = isMemberRefined && type.declaration.getInheritedMembers(name).contains(declarationOfSupertypeMember);
            if (isMemberRefined) {
                return true;
            };
        }
        return false;
    }

    private String getTypeDeclarationFor(Type.Members aggregateMembers) {
        Declaration memberDeclaration = getMemberDeclaration(aggregateMembers);
        return memberDeclaration == null ? null : memberDeclaration.getDeclaringType(memberDeclaration).getDeclaration().getQualifiedNameString();
    }

    private Declaration getMemberDeclaration(Type.Members aggregateMembers) {
        if (!aggregateMembers.formals.isEmpty()) {
            return aggregateMembers.formals.iterator().next();
        }
        if (!aggregateMembers.defaults.isEmpty()) {
            return aggregateMembers.defaults.iterator().next();
        }
        if (!aggregateMembers.actuals.isEmpty()) {
            return aggregateMembers.actuals.iterator().next();
        }
        if (!aggregateMembers.nonFormalsNonDefaults.isEmpty()) {
            return aggregateMembers.nonFormalsNonDefaults.iterator().next();
        }
        return null;
    }

    private void checkForFormalsNotImplemented(Tree.ClassOrInterface that, List<Type> orderedTypes) {
        Type aggregation = buildAggregatedType(orderedTypes);
        for (Type.Members members:aggregation.membersByName.values()) {
            if (members.formals.size()!=0&&members.actuals.size()==0) {
                that.addError("formal member " + members.name + " not implemented in class hierarchy");
            }
        }
    }

    //accumulate all members of a type hierarchy
    private Type buildAggregatedType(List<Type> orderedTypes) {
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
        return aggregation;
    }

    //sort type hierarchy from most abstract to most concrete
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
                if (!(member instanceof MethodOrValue)) {
                    continue;
                }
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
