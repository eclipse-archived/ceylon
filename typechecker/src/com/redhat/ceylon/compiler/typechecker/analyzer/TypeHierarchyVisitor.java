package com.redhat.ceylon.compiler.typechecker.analyzer;

import static com.redhat.ceylon.compiler.typechecker.model.Util.isResolvable;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isOverloadedVersion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Node;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Checks associated to the traversal of the hierarchy:
 * - detect circularity in inheritance
 * - All formal must be implemented as actual by concrete classes
 * - may not inherit two declarations with the same name that do not share a common supertype
 * - may not inherit two declarations with the same name unless redefined in subclass
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
            public Set<Declaration> formals = new LinkedHashSet<Declaration>();
            //public Set<Declaration> concretesOnInterfaces = new LinkedHashSet<Declaration>();
            public Set<Declaration> actuals = new HashSet<Declaration>();
            public Set<Declaration> actualsNonFormals = new HashSet<Declaration>();
            public Set<Declaration> defaults = new HashSet<Declaration>();
            public Set<Declaration> nonFormalsNonDefaults = new HashSet<Declaration>();
			public Set<Declaration> shared = new HashSet<Declaration>();
        }

        @Override
        public String toString() {
            return declaration.getName();
        }
    }
    
    @Override
    public void visit(Tree.ObjectDefinition that) {
        Value value = that.getDeclarationModel();
        Class anonymousClass = (Class) value.getType().getDeclaration();
        validateMemberRefinement(that, anonymousClass);
        super.visit(that);
        //an object definition is always concrete
        List<Type> orderedTypes = sortDAGAndBuildMetadata(value.getTypeDeclaration(), that);
        checkForFormalsNotImplemented(that, orderedTypes);
        checkForDoubleMemberInheritanceNotOverridden(that, orderedTypes, anonymousClass);
        checkForDoubleMemberInheritanceWoCommonAncestor(that, orderedTypes, anonymousClass);
    }

    @Override
    public void visit(Tree.ObjectArgument that) {
        Value value = that.getDeclarationModel();
        Class anonymousClass = (Class) value.getType().getDeclaration();
        validateMemberRefinement(that, anonymousClass);
        super.visit(that);
        //an object definition is always concrete
        List<Type> orderedTypes = sortDAGAndBuildMetadata(value.getTypeDeclaration(), that);
        checkForFormalsNotImplemented(that, orderedTypes);
        checkForDoubleMemberInheritanceNotOverridden(that, orderedTypes, anonymousClass);
        checkForDoubleMemberInheritanceWoCommonAncestor(that, orderedTypes, anonymousClass);
    }

    @Override
    public void visit(Tree.ClassOrInterface that) {
        ClassOrInterface classOrInterface = that.getDeclarationModel();
        validateMemberRefinement(that, classOrInterface);
        super.visit(that);
        if (!classOrInterface.isAlias()) {
            boolean concrete = !classOrInterface.isAbstract() && !classOrInterface.isFormal();
            List<Type> orderedTypes = sortDAGAndBuildMetadata(classOrInterface, that);
            if (concrete) {
                checkForFormalsNotImplemented(that, orderedTypes);
            }
            checkForDoubleMemberInheritanceNotOverridden(that, orderedTypes, classOrInterface);
            checkForDoubleMemberInheritanceWoCommonAncestor(that, orderedTypes, classOrInterface);
        }
    }

    @Override
    public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        final TypeParameter typeParameter = that.getDeclarationModel();
        sortDAGAndBuildMetadata(typeParameter, that);
    }
    
    @Override
    public void visit(Tree.Declaration that) {
        super.visit(that);
        Declaration dec = that.getDeclarationModel();
        if (dec.isClassOrInterfaceMember() && dec.isActual()) {
            checkForShortcutRefinement(that, dec);
        }
    }

    @Override
    public void visit(Tree.BaseMemberExpression that) {
        super.visit(that);
        Declaration dec = that.getDeclaration();
        if (dec instanceof MethodOrValue && 
                ((MethodOrValue) dec).isShortcutRefinement()) {
            checkForShortcutRefinement(that, dec);
        }
    }

    private static void checkForShortcutRefinement(Node that, Declaration dec) {
        for (Declaration im: ((ClassOrInterface) dec.getContainer()).getInheritedMembers(dec.getName())) {
            if (im instanceof MethodOrValue && 
                    ((MethodOrValue) im).isShortcutRefinement()) {
                that.addError("refines a non-formal, non-default member: " + 
                        RefinementVisitor.message(im));
            }
        }
    }

    private void checkForDoubleMemberInheritanceWoCommonAncestor(Tree.StatementOrArgument that, 
            List<Type> orderedTypes, ClassOrInterface classOrInterface) {
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
                    boolean superMemberIsShared = !aggregateMembers.shared.isEmpty();
                    boolean currentMemberIsShared = !currentTypeMembers.shared.isEmpty();
                    if (superMemberIsShared && currentMemberIsShared) {
                        boolean isMemberNameOnAncestor = isMemberNameOnAncestor(currentType, name);
                        if (!isMemberNameOnAncestor) {
                            TypeDeclaration otherType = getTypeDeclarationFor(aggregateMembers);
                        	if (!mixedInBySupertype(currentType.declaration, otherType, classOrInterface)) {
                        		StringBuilder sb = new StringBuilder("may not inherit two declarations with the same name that do not share a common supertype: ");
                                sb.append(name)
                                  .append(" is defined by supertypes ")
                        		  .append(currentType.declaration.getName())
                        		  .append(" and ")
                        		  .append(otherType.getName());
                        		that.addError(sb.toString());
                        	}
                        }
                    }
                }
                pourCurrentTypeInfoIntoAggregatedType(currentTypeMembers, aggregateMembers);
            }
        }
    }

    private void checkForDoubleMemberInheritanceNotOverridden(Tree.StatementOrArgument that, 
            List<Type> orderedTypes, ClassOrInterface classOrInterface) {
        Type aggregateType = new Type();
        int size = orderedTypes.size();
        for(int index = size-1;index>=0;index--) {
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
                    boolean subtypeMemberIsShared = !aggregateMembers.shared.isEmpty();
                    boolean currentMemberIsShared = !currentTypeMembers.shared.isEmpty();
                    if (subtypeMemberIsShared && currentMemberIsShared) {
                        boolean isMemberRefined = isMemberRefined(orderedTypes,index,name,currentTypeMembers);
                        boolean isMemberNameOnAncestor = isMemberNameOnAncestor(currentType, name);
                        if (!isMemberRefined && isMemberNameOnAncestor) {
                            TypeDeclaration otherType = getTypeDeclarationFor(aggregateMembers);
                        	if (!mixedInBySupertype(currentType.declaration, otherType, classOrInterface)) {
                        		StringBuilder sb = new StringBuilder("may not inherit two declarations with the same name unless redefined in subclass: ");
                                sb.append(name)
                                  .append(" is defined by supertypes ")
                        		  .append(currentType.declaration.getName())
                        		  .append(" and ")
                        		  .append(otherType.getName());
                        		that.addError(sb.toString());
                        	}
                        }
                    }
                }
                pourCurrentTypeInfoIntoAggregatedType(currentTypeMembers, aggregateMembers);
            }
        }
    }

    private void pourCurrentTypeInfoIntoAggregatedType(Type.Members currentTypeMembers, Type.Members aggregateMembers) {
        aggregateMembers.nonFormalsNonDefaults.addAll(currentTypeMembers.nonFormalsNonDefaults);
        aggregateMembers.actuals.addAll(currentTypeMembers.actuals);
        aggregateMembers.formals.addAll(currentTypeMembers.formals);
        //aggregateMembers.concretesOnInterfaces.addAll(currentTypeMembers.concretesOnInterfaces);
        aggregateMembers.actualsNonFormals.addAll(currentTypeMembers.actualsNonFormals);
        aggregateMembers.defaults.addAll(currentTypeMembers.defaults);
        aggregateMembers.shared.addAll(currentTypeMembers.shared);
    }

    private boolean isMemberNameOnAncestor(Type currentType, String name) {
        //retrieve inherited members (shared)
        return !currentType.declaration.getInheritedMembers(name).isEmpty();
    }

    private boolean mixedInBySupertype(TypeDeclaration currentType, TypeDeclaration otherType, 
            ClassOrInterface classOrInterface) {
        TypeDeclaration et = classOrInterface.getExtendedTypeDeclaration();
        if (et!=null && 
                et.inherits(currentType) && 
                et.inherits(otherType)) {
            return true;
        }
        for (TypeDeclaration st: classOrInterface.getSatisfiedTypeDeclarations()) {
            if (st!=null && 
                    st.inherits(currentType) && 
                    st.inherits(otherType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMemberRefined(List<Type> orderedTypes, int index, String name, Type.Members currentTypeMembers) {
        int size = orderedTypes.size();
        Declaration declarationOfSupertypeMember = getMemberDeclaration(currentTypeMembers);
        for (int subIndex = size-1 ; subIndex>index;subIndex--) {
            Type type = orderedTypes.get(subIndex);
            //has a direct member and supertype as inherited members
            Declaration directMember = type.declaration.getDirectMember(name, null, false);
            boolean isMemberRefined = directMember!=null && directMember.isShared(); //&& !(directMember instanceof Parameter);
            isMemberRefined = isMemberRefined && type.declaration.getInheritedMembers(name).contains(declarationOfSupertypeMember);
            if (isMemberRefined) {
                return true;
            }
        }
        return false;
    }

    private TypeDeclaration getTypeDeclarationFor(Type.Members aggregateMembers) {
        Declaration memberDeclaration = getMemberDeclaration(aggregateMembers);
        return memberDeclaration == null ? null : memberDeclaration.getDeclaringType(memberDeclaration).getDeclaration();
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

    private void checkForFormalsNotImplemented(Tree.StatementOrArgument that, List<Type> orderedTypes) {
        Type aggregation = buildAggregatedType(orderedTypes);
        for (Type.Members members:aggregation.membersByName.values()) {
            if (!members.formals.isEmpty() && members.actualsNonFormals.isEmpty()) {
                Declaration declaringType = (Declaration) members.formals.iterator().next().getContainer();
				that.addError("formal member " + members.name + 
                		" of " + declaringType.getName() +
                        " not implemented in class hierarchy", 300);
            }
            /*if (!members.concretesOnInterfaces.isEmpty() && members.actualsNonFormals.isEmpty()) {
                Declaration declaringType = (Declaration) members.concretesOnInterfaces.iterator().next().getContainer();
                that.addWarning("interface member " + members.name + 
                		" of " + declaringType.getName() +
                        " not implemented in class hierarchy (concrete interface members not yet supported)");
            }*/
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
                pourCurrentTypeInfoIntoAggregatedType(currentMembers, aggregateMembers);
                //if an actual implementation high in the hierarchy is overridden as formal => do not add
                //remember we go from most specific to most generic type
                for (Declaration actualNonFormal : currentMembers.actualsNonFormals) {
                    for (Declaration formal : aggregateMembers.formals) {
                        if (formal.getName().equals(actualNonFormal.getName())) {
                            if (!formal.getUnit().getPackage().getModule().isJava() ||
                                    !formal.isInterfaceMember()) {
                                aggregateMembers.actualsNonFormals.remove(actualNonFormal);
                                break;
                            }
                        }
                    }
                }
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

    private void visitDAGNode(TypeDeclaration declaration, List<Type> sortedDag, 
            Set<TypeDeclaration> visited, List<TypeDeclaration> stackOfProcessedType, 
            Node errorReporter) {
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
        for (ProducedType superSatisfiedType : declaration.getBrokenSupertypes()) {
            visitDAGNode(superSatisfiedType.getDeclaration(), sortedDag, visited, stackOfProcessedType, errorReporter);
        }
        stackOfProcessedType.remove(stackOfProcessedType.size()-1);
        sortedDag.add(type);
    }

    private boolean checkCyclicInheritance(TypeDeclaration declaration, 
            List<TypeDeclaration> stackOfProcessedType, Node errorReporter) {
        final int matchingIndex = stackOfProcessedType.indexOf(declaration);
        if (matchingIndex!=-1) {
            /*StringBuilder sb = new StringBuilder("cyclical inheritance in ");
            sb.append(declaration.getName());
            sb.append(" (involving ");
            for (int index = stackOfProcessedType.size()-1;index>matchingIndex;index--) {
                sb.append(stackOfProcessedType.get(index).getName()).append(", ");
            }
            removeTrailing(", ", sb);
            sb.append(")");
            errorReporter.addError(sb.toString());
            return true;*/
        }
        return false;
    }

    /*private void removeTrailing(String trailingString, StringBuilder sb) {
        final int length = sb.length();
        sb.delete(length-trailingString.length(), length);
    }*/

    private Type getOrBuildType(TypeDeclaration declaration) {
        Type type = types.get(declaration);
        if (type == null) {
            type = new Type();
            type.declaration = declaration;
            for (Declaration member: declaration.getMembers()) {
                if (!(member instanceof MethodOrValue || 
                      member instanceof Class) || 
                        member.isStaticallyImportable()) {
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
                    if (!member.isFormal()) {
                        members.actualsNonFormals.add(member);
                    }
                }
                if (member.isFormal()) {
                    members.formals.add(member);
                }
                /*if (!member.isFormal() && member.isInterfaceMember()) {
                	members.concretesOnInterfaces.add(member);
                }*/
                if (member.isDefault()) {
                    members.defaults.add(member);
                }
                if (!member.isFormal()&&!member.isDefault()) {
                    members.nonFormalsNonDefaults.add(member);
                }
                if (member.isShared()) {
                    members.shared.add(member);
                }
            }
            types.put(declaration,type);
        }
        return type;
    }
    
    private void validateMemberRefinement(Tree.StatementOrArgument that, 
            TypeDeclaration td) {
        if (!td.isInconsistentType()) {
            List<ProducedType> supertypes = td.getType().getSupertypes();
            Set<String> errors = new HashSet<String>();
            for (ProducedType st: supertypes) {
                if (td instanceof ClassOrInterface && 
                        !((ClassOrInterface) td).isAbstract() &&
                        !((ClassOrInterface) td).isAlias()) {
                    for (Declaration d: st.getDeclaration().getMembers()) {
                        if (d.isShared() && !isOverloadedVersion(d) && isResolvable(d) && 
                                !errors.contains(d.getName())) {
                            Declaration r = td.getMember(d.getName(), null, false);
                            if (r==null || !r.refines(d) && 
                                    //squash bogus error when there is a dupe declaration
                                    !r.getContainer().equals(td)) {
                                //TODO: This seems to dupe some checks that are already 
                                //      done in TypeHierarchyVisitor, resulting in
                                //      multiple errors
                                //TODO: figure out which other declaration causes the
                                //      problem and display it to the user!
                                if (r==null) {
                                    that.addError("member " + d.getName() +
                                            " is inherited ambiguously by " + td.getName() +
                                            " from " + st.getDeclaration().getName() +  
                                            " and another unrelated supertype");
                                }
                                else {
                                    //TODO: I'm not really certain that the following
                                    //      condition is correct, we really should 
                                    //      check that the other declaration is a Java
                                    //      interface member (see the TODO above)
                                    if (!(d.getUnit().getPackage().getModule().isJava() &&
                                            r.getUnit().getPackage().getModule().isJava() &&
                                            r.isInterfaceMember() &&
                                            d.isClassMember())) {
                                        that.addError("member " + d.getName() + 
                                                " is inherited ambiguously by " + td.getName() +
                                                " from " + st.getDeclaration().getName() +  
                                                " and another subtype of " + ((TypeDeclaration) r.getContainer()).getName() + 
                                                " and so must be refined by " + td.getName(), 350);
                                    }
                                }
                                errors.add(d.getName());
                            }
                            /*else if (!r.getContainer().equals(td)) { //the case where the member is actually declared by the current type is handled by checkRefinedTypeAndParameterTypes()
                                //TODO: I think this case never occurs, because getMember() always
                                //      returns null in the case of an ambiguity
                                List<ProducedType> typeArgs = new ArrayList<ProducedType>();
                                if (d instanceof Generic) {
                                    for (TypeParameter refinedTypeParam: ((Generic) d).getTypeParameters()) {
                                        typeArgs.add(refinedTypeParam.getType());
                                    }
                                }
                                ProducedType t = td.getType().getTypedReference(r, typeArgs).getType();
                                ProducedType it = st.getTypedReference(d, typeArgs).getType();
                                checkAssignable(t, it, that, "type of member " + d.getName() + 
                                        " must be assignable to all types inherited from instantiations of " +
                                        st.getDeclaration().getName());
                            }*/
                        }
                    }
                }
            }
        }
    }

}
