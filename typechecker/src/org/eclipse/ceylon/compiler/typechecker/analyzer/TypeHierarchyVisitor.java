/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.typechecker.analyzer;

import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.NO_TYPE_ARGS;
import static org.eclipse.ceylon.compiler.typechecker.analyzer.AnalyzerUtil.message;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.getNativeDeclaration;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.hasMatchingSignature;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isAbstraction;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isOverloadedVersion;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isResolvable;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isTypeUnknown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.ceylon.common.Backends;
import org.eclipse.ceylon.compiler.typechecker.tree.Node;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.IntersectionType;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.Reference;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.UnionType;
import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 * Checks associated to the traversal of the hierarchy:
 * 
 * - detect circularity in inheritance
 * - lll formal must be implemented as actual by concrete 
 *   classes
 * - may not inherit two declarations with the same name 
 *   that do not share a common supertype
 * - may not inherit two declarations with the same name 
 *   unless redefined in subclass
 *
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class TypeHierarchyVisitor extends Visitor {

    private final Map<TypeDeclKey,Type> types = new HashMap<TypeDeclKey, Type>();

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
    
    // Special wrapper class for TypeDeclarations that takes into
    // account the native backend property when determining equality
    private static final class TypeDeclKey {
        public final TypeDeclaration decl;
        public TypeDeclKey(TypeDeclaration decl) {
            this.decl = decl;
        }
        @Override
        public int hashCode() {
            return decl.hashCode();
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TypeDeclKey other = (TypeDeclKey) obj;
            return decl.equals(other.decl) && 
                    (!decl.isNative() ||
                            decl.getNativeBackends()
                                .supports(other.decl.getNativeBackends()));
        }
    }
    
    @Override
    public void visit(Tree.ObjectDefinition that) {
        super.visit(that);
        Value value = that.getDeclarationModel();
        Class anonymousClass = that.getAnonymousClass();
        //an object definition is always concrete
        List<Type> orderedTypes = 
                sortDAGAndBuildMetadata(value.getTypeDeclaration(), that);
        checkForFormalsNotImplemented(that, 
                orderedTypes, anonymousClass);
        checkForDoubleMemberInheritanceNotOverridden(that, 
                orderedTypes, anonymousClass);
        checkForDoubleMemberInheritanceWoCommonAncestor(that, 
                orderedTypes, anonymousClass);
        validateMemberRefinement(that, anonymousClass);
    }

    @Override
    public void visit(Tree.ObjectArgument that) {
        super.visit(that);
        Class anonymousClass = that.getAnonymousClass();
        //an object definition is always concrete
        List<Type> orderedTypes = 
                sortDAGAndBuildMetadata(anonymousClass, that);
        checkForFormalsNotImplemented(that, 
                orderedTypes, anonymousClass);
        checkForDoubleMemberInheritanceNotOverridden(that, 
                orderedTypes, anonymousClass);
        checkForDoubleMemberInheritanceWoCommonAncestor(that, 
                orderedTypes, anonymousClass);
        validateMemberRefinement(that, anonymousClass);
    }

    @Override
    public void visit(Tree.ObjectExpression that) {
        super.visit(that);
        Class anonymousClass = that.getAnonymousClass();
        //an object definition is always concrete
        List<Type> orderedTypes = 
                sortDAGAndBuildMetadata(anonymousClass, that);
        checkForFormalsNotImplemented(that, 
                orderedTypes, anonymousClass);
        checkForDoubleMemberInheritanceNotOverridden(that, 
                orderedTypes, anonymousClass);
        checkForDoubleMemberInheritanceWoCommonAncestor(that, 
                orderedTypes, anonymousClass);
        validateMemberRefinement(that, anonymousClass);
    }

    @Override
    public void visit(Tree.ClassOrInterface that) {
        super.visit(that);
        ClassOrInterface classOrInterface = 
                that.getDeclarationModel();
        if (!classOrInterface.isAlias()) {
            boolean concrete = 
                    !classOrInterface.isAbstract() && 
                    !classOrInterface.isFormal();
            List<Type> orderedTypes = 
                    sortDAGAndBuildMetadata(classOrInterface, that);
            if (concrete) {
                checkForFormalsNotImplemented(that, 
                        orderedTypes, (Class) classOrInterface);
            }
            checkForDoubleMemberInheritanceNotOverridden(that, 
                    orderedTypes, classOrInterface);
            checkForDoubleMemberInheritanceWoCommonAncestor(that, 
                    orderedTypes, classOrInterface);
        }
        validateMemberRefinement(that, classOrInterface);
    }

    @Override
    public void visit(Tree.TypeConstraint that) {
        super.visit(that);
        sortDAGAndBuildMetadata(that.getDeclarationModel(), that);
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
        if (dec instanceof FunctionOrValue && 
                ((FunctionOrValue) dec).isShortcutRefinement()) {
            checkForShortcutRefinement(that, dec);
        }
    }

    private static void checkForShortcutRefinement(Node that, Declaration dec) {
        ClassOrInterface classOrInterface = 
                (ClassOrInterface) dec.getContainer();
        for (Declaration im: 
                classOrInterface.getInheritedMembers(dec.getName())) {
            if (im instanceof FunctionOrValue && 
                    ((FunctionOrValue) im).isShortcutRefinement()) {
                that.addError("refines a non-formal, non-default member: " + 
                        message(im));
            }
        }
    }

    private void checkForDoubleMemberInheritanceWoCommonAncestor(Node that, 
            List<Type> orderedTypes, ClassOrInterface classOrInterface) {
        Type aggregateType = new Type();
        for(Type currentType: orderedTypes) {
            for (Type.Members currentTypeMembers: 
                    currentType.membersByName.values()) {
                String name = currentTypeMembers.name;
                Type.Members aggregateMembers = 
                        aggregateType.membersByName.get(name);
                if (aggregateMembers==null) {
                    //not accumulated yet, no need to check
                    aggregateMembers = new Type.Members();
                    aggregateMembers.name = name;
                    aggregateType.membersByName.put(name,aggregateMembers);
                }
                else {
                    boolean superMemberIsShared = 
                            !aggregateMembers.shared.isEmpty();
                    boolean currentMemberIsShared = 
                            !currentTypeMembers.shared.isEmpty();
                    if (superMemberIsShared && currentMemberIsShared) {
                        boolean isMemberNameOnAncestor = 
                                isMemberNameOnAncestor(currentType, name);
                        if (!isMemberNameOnAncestor) {
                            TypeDeclaration otherType = 
                                    getTypeDeclarationFor(aggregateMembers);
                            if (!mixedInBySupertype(currentType.declaration, 
                                    otherType, classOrInterface)) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("type '")
                                  .append(classOrInterface.getName())
                                  .append("' inherits multiple definitions of '")
                                  .append(name)
                                  .append("': inherited member '")
                                  .append(name)
                                  .append("' is defined by unrelated supertypes '")
                                  .append(currentType.declaration.getName())
                                  .append("' and '")
                                  .append(otherType.getName())
                                  .append("'");
                                that.addError(sb.toString());
                            }
                        }
                    }
                }
                pourCurrentTypeInfoIntoAggregatedType(currentTypeMembers, 
                        aggregateMembers);
            }
        }
    }

    private void checkForDoubleMemberInheritanceNotOverridden(Node that, 
            List<Type> orderedTypes, ClassOrInterface classOrInterface) {
        Type aggregateType = new Type();
        int size = orderedTypes.size();
        for(int index = size-1; index>=0; index--) {
            Type currentType = orderedTypes.get(index);
            for (Type.Members currentTypeMembers: 
                    currentType.membersByName.values()) {
                String name = currentTypeMembers.name;
                Type.Members aggregateMembers = 
                        aggregateType.membersByName.get(name);
                if (aggregateMembers==null) {
                    //not accumulated yet, no need to check
                    aggregateMembers = new Type.Members();
                    aggregateMembers.name = name;
                    aggregateType.membersByName.put(name,aggregateMembers);
                }
                else {
                    boolean subtypeMemberIsShared = 
                            !aggregateMembers.shared.isEmpty();
                    boolean currentMemberIsShared = 
                            !currentTypeMembers.shared.isEmpty();
                    if (subtypeMemberIsShared && currentMemberIsShared) {
                        boolean isMemberRefined = 
                                isMemberRefined(orderedTypes,index,name,currentTypeMembers);
                        boolean isMemberNameOnAncestor = 
                                isMemberNameOnAncestor(currentType, name);
                        if (!isMemberRefined && isMemberNameOnAncestor) {
                            TypeDeclaration otherType = 
                                    getTypeDeclarationFor(aggregateMembers);
                            if (!mixedInBySupertype(currentType.declaration, 
                                    otherType, classOrInterface)) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("type '")
                                  .append(classOrInterface.getName())
                                  .append("' inherits but does not refine multiple definitions of '")
                                  .append(name)
                                  .append("': inherited member '")
                                  .append(name)
                                  .append("' is defined by supertypes '")
                                  .append(currentType.declaration.getName())
                                  .append("' and '")
                                  .append(otherType.getName())
                                  .append("' (refine '")
                                  .append(name)
                                  .append("' in '")
                                  .append(classOrInterface.getName())
                                  .append("')");
                                that.addError(sb.toString(), 350);
                            }
                        }
                    }
                }
                pourCurrentTypeInfoIntoAggregatedType(currentTypeMembers, 
                        aggregateMembers);
            }
        }
    }

    private void pourCurrentTypeInfoIntoAggregatedType(Type.Members currentTypeMembers, 
            Type.Members aggregateMembers) {
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

    private boolean mixedInBySupertype(TypeDeclaration currentType, 
            TypeDeclaration otherType, ClassOrInterface classOrInterface) {
        org.eclipse.ceylon.model.typechecker.model.Type et = 
                classOrInterface.getExtendedType();
        if (et!=null) {
            TypeDeclaration etd = et.getDeclaration();
            if (etd.inherits(currentType) && 
                etd.inherits(otherType)) {
                return true;
            }
        }
        for (org.eclipse.ceylon.model.typechecker.model.Type st: 
                classOrInterface.getSatisfiedTypes()) {
            if (st!=null) {
                TypeDeclaration std = st.getDeclaration();
                if (std.inherits(currentType) && 
                    std.inherits(otherType)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isMemberRefined(List<Type> orderedTypes, int index, 
            String name, Type.Members currentTypeMembers) {
        Declaration declarationOfSupertypeMember = 
                getMemberDeclaration(currentTypeMembers);
        for (int subIndex = orderedTypes.size()-1; 
                subIndex>index; subIndex--) {
            Type type = orderedTypes.get(subIndex);
            //has a direct member and supertype as inherited members
            Declaration directMember = 
                    type.declaration.getDirectMember(name, null, false);
            boolean isMemberRefined = 
                    directMember!=null && 
                    directMember.isShared() && 
                    type.declaration
                        .getInheritedMembers(name)
                        .contains(declarationOfSupertypeMember);
            if (isMemberRefined) {
                return true;
            }
        }
        return false;
    }

    private TypeDeclaration getTypeDeclarationFor(Type.Members aggregateMembers) {
        Declaration memberDeclaration = 
                getMemberDeclaration(aggregateMembers);
        return memberDeclaration == null ? null : 
            memberDeclaration.getDeclaringType(memberDeclaration).getDeclaration();
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

    private boolean isJavaSetter(Declaration dec) {
        return dec instanceof Function
            && dec.isJava()
            && dec.getName().matches("set[A-Z0-9].*")
            && ((Function)dec).isDeclaredVoid()
            && ((Function)dec).getFirstParameterList().getParameters().size() == 1;
    }

    private void checkForFormalsNotImplemented(Node that, 
            List<Type> orderedTypes, Class clazz) {
        Type aggregation = buildAggregatedType(orderedTypes);
        for (Type.Members members: aggregation.membersByName.values()) {
            if (!members.formals.isEmpty()) {
                if (members.actualsNonFormals.isEmpty()) {
                    Declaration example =
                            members.formals.iterator().next();
                    Declaration declaringType =
                            (Declaration)
                                example.getContainer();
                    if (!clazz.equals(declaringType)) {
                        String name = example.getName();
                        if (isJavaSetter(example) 
                                && aggregation.membersByName
                                    .containsKey(setterToProperty(name))) {
                            //we need to ignore setters that are
                            //refined by get/set pairs lower down
                            //the Java hierarchy
                            //TODO: add the concept of write-only
                            //      properties to the typechecker
                            //      (i.e. a setter with no getter)
                            continue;
                         }
                        addUnimplementedFormal(clazz, example);
                        that.addError("formal member '"
                                + name + "' of '"
                                + declaringType.getName()
                                + "' not implemented for concrete class '"
                                + clazz.getName()
                                + "': '"
                                + clazz.getName()
                                + "' neither directly implements nor inherits a concrete implementation of '"
                                + name + "'",
                                300);
                        continue;
                    }
                }
                for (Declaration f: members.formals) {
                    if (isOverloadedVersion(f) && !f.isCoercionPoint()) {
                        boolean found = false;
//                        List<org.eclipse.ceylon.model.typechecker.model.Type>
//                                signature = ModelUtil.getSignature(f);
//                        boolean variadic = f.isVariadic();
                        for (Declaration a: members.actualsNonFormals) {
                            if (f.getRefinedDeclaration()
                                    .equals(a.getRefinedDeclaration())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            StringBuilder paramTypes = new StringBuilder();
                            List<ParameterList> parameterLists = 
                                    ((Functional) f).getParameterLists();
                            if (!parameterLists.isEmpty()) {
                                for (Parameter p: parameterLists.get(0).getParameters()) {
                                    if (paramTypes.length()>0) {
                                        paramTypes.append(", ");
                                    }
                                    if (!isTypeUnknown(p.getType())) {
                                        paramTypes.append(p.getType().asString());
                                    }
                                }
                            }
                            Declaration declaringType = 
                                    (Declaration) f.getContainer();
                            addUnimplementedFormal(clazz, f);
                            that.addError("overloaded formal member '" + f.getName() + 
                                    "(" + paramTypes + ")' of '" + declaringType.getName() +
                                    "' not implemented in class hierarchy"/*, 300*/);
                        }
                    }
                }
            }
            /*if (!members.concretesOnInterfaces.isEmpty() && members.actualsNonFormals.isEmpty()) {
                Declaration declaringType = (Declaration) members.concretesOnInterfaces.iterator().next().getContainer();
                that.addWarning("interface member " + members.name + 
                        " of " + declaringType.getName() +
                        " not implemented in class hierarchy (concrete interface members not yet supported)");
            }*/
        }
    }

    private static String setterToProperty(String name) {
        return name.substring(3, 4).toLowerCase() 
            + name.substring(4);
    }

    private static boolean isJavaInterfaceMember(Declaration formal) {
        return formal.isInterfaceMember() 
            && formal.isJava();
    }

    private void addUnimplementedFormal(Class clazz, Declaration member) {
        Reference unimplemented = 
                member.appliedReference(clazz.getType(), 
                        NO_TYPE_ARGS);
        List<Reference> list = 
                clazz.getUnimplementedFormals();
        if (list.isEmpty()) {
            list = new ArrayList<Reference>();
            clazz.setUnimplementedFormals(list);
        }
        list.add(unimplemented);
    }

    //accumulate all members of a type hierarchy
    private Type buildAggregatedType(List<Type> orderedTypes) {
        Type aggregation = new Type();
        for (int index = orderedTypes.size()-1; 
                index>=0; index--) {
            Type current = orderedTypes.get(index);
            for (Type.Members currentMembers:
                    current.membersByName.values()) {
                Type.Members aggregateMembers = 
                        aggregation.membersByName.get(currentMembers.name);
                if (aggregateMembers==null) {
                    aggregateMembers = new Type.Members();
                    aggregateMembers.name = currentMembers.name;
                    aggregation.membersByName.put(currentMembers.name,aggregateMembers);
                }
                pourCurrentTypeInfoIntoAggregatedType(currentMembers, aggregateMembers);
                //if an actual implementation high in the hierarchy is overridden as formal => do not add
                //remember we go from most specific to most generic type
                for (Declaration actualNonFormal: currentMembers.actualsNonFormals) {
                    for (Declaration formal: aggregateMembers.formals) {
                        if (formal.getName().equals(actualNonFormal.getName())) {
                            if (!isJavaInterfaceMember(formal)) {
                                aggregateMembers.actualsNonFormals.remove(actualNonFormal);
                                break;
                            }
                        }
                    }
                }
            }
        }
        //a Java interface method can be implemented by stuff that doesn't
        //explicitly/directly refine it
        for (Type.Members aggregateMembers: aggregation.membersByName.values()) {
            for (Declaration formal: 
                    new ArrayList<Declaration>
                        (aggregateMembers.formals)) {
                if (isJavaInterfaceMember(formal)) {
                    boolean overloaded = isOverloadedVersion(formal);
                    List<org.eclipse.ceylon.model.typechecker.model.Type> 
                        signature = overloaded ? ModelUtil.getSignature(formal) : null;
                    boolean variadic = formal.isVariadic();
                    for (Declaration concrete: aggregateMembers.defaults) {
                        if (isJavaRefinement(formal, overloaded, signature, variadic, concrete)) {
                            aggregateMembers.formals.remove(formal);
                            break;
                        }
                    }
                    for (Declaration concrete: aggregateMembers.nonFormalsNonDefaults) {
                        if (isJavaRefinement(formal, overloaded, signature, variadic, concrete)) {
                            aggregateMembers.formals.remove(formal);
                            break;
                        }
                    }
                }
            }
        }
        return aggregation;
    }

    private static boolean isJavaRefinement(Declaration formal, boolean overloaded,
            List<org.eclipse.ceylon.model.typechecker.model.Type> signature, 
            boolean variadic, Declaration concrete) {
        return formal.getName().equals(concrete.getName())
            && isDefinedInJava(concrete)
            && (!overloaded || hasMatchingSignature(concrete, signature, variadic));
    }

    //sort type hierarchy from most abstract to most concrete
    private List<Type> sortDAGAndBuildMetadata(TypeDeclaration declaration, 
            Node errorReporter) {
        //Apply a partial sort on the class hierarchy which is a 
        //Directed Acyclic Graph (DAG) with subclasses pointing to 
        //superclasses or interfaces use depth-first plus a stack to 
        //processed nodes to detect non DAG
        //http://en.wikipedia.org/wiki/Topological_sorting
        List<Type> sortedDag = new ArrayList<Type>();
        List<TypeDeclaration> visitedDeclarationPerBranch = 
                new ArrayList<TypeDeclaration>();
        Set<TypeDeclaration> visited = new HashSet<TypeDeclaration>();
        visitDAGNode(declaration, sortedDag, visited, 
                visitedDeclarationPerBranch, errorReporter);
        return sortedDag;
    }

    private void visitDAGNode(TypeDeclaration declaration, 
            List<Type> sortedDag, Set<TypeDeclaration> visited, 
            List<TypeDeclaration> stackOfProcessedType, 
            Node errorReporter) {
        if (declaration == null) {
            return;
        }
        if (checkCyclicInheritance(declaration, 
                stackOfProcessedType, errorReporter)) {
            return; //stop the cycle here but try and process the rest
        }

        if (visited.contains(declaration)) {
            return;
        }
        visited.add(declaration);
        Type type = getOrBuildType(declaration);

        stackOfProcessedType.add(declaration);
        org.eclipse.ceylon.model.typechecker.model.Type 
        extendedType = declaration.getExtendedType();
        if (extendedType!=null) {
            visitDAGNode(extendedType.getDeclaration(), 
                    sortedDag, visited, stackOfProcessedType, 
                    errorReporter);
        }
        for (org.eclipse.ceylon.model.typechecker.model.Type 
                superSatisfiedType: declaration.getSatisfiedTypes()) {
            if (superSatisfiedType!=null) {
                visitDAGNode(superSatisfiedType.getDeclaration(), 
                        sortedDag, visited, 
                        stackOfProcessedType, errorReporter);
            }
        }
        for (org.eclipse.ceylon.model.typechecker.model.Type 
                superSatisfiedType: declaration.getBrokenSupertypes()) {
            TypeDeclaration typeDec = superSatisfiedType.getDeclaration();
            if (!(typeDec instanceof UnionType) && 
                !(typeDec instanceof IntersectionType)) {
                visitDAGNode(typeDec,
                        sortedDag, visited, stackOfProcessedType, 
                        errorReporter);
            }
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
        Type type = types.get(new TypeDeclKey(declaration));
        if (type == null) {
            type = new Type();
            type.declaration = declaration;
            for (Declaration member: declaration.getMembers()) {
                if (!(member instanceof FunctionOrValue ||
                      member instanceof Class) || 
                    member.isConstructor() ||
                    member.isStatic() ||
                    isAbstraction(member)) {
                    continue;
                }
                if (declaration.isNative() && member.isNative()) {
                    // Make sure we get the right member declaration (the one for the same backend as its container)
                    Backends backends = declaration.getNativeBackends();
                    member = getNativeDeclaration(member, backends);
                    if (member == null) {
                        continue;
                    }
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
                if (!member.isFormal() && !member.isDefault() && member.isShared()) {
                    members.nonFormalsNonDefaults.add(member);
                }
                if (member.isShared()) {
                    members.shared.add(member);
                }
            }
            types.put(new TypeDeclKey(declaration),type);
        }
        return type;
    }
    
    private void validateMemberRefinement(Node that, 
            TypeDeclaration td) {
        if (!td.isInconsistentType() 
                && td instanceof ClassOrInterface 
                && !td.isAbstract() 
                && !td.isAlias()
                //The work here dupes some checks 
                //that are already done above in 
                //checkForDoubleMemberInheritance, 
                //resulting in multiple errors
                && !that.hasErrors()) {
            Set<String> errors = new HashSet<String>();
            for (TypeDeclaration std: 
                    td.getSupertypeDeclarations()) {
                for (Declaration d: std.getMembers()) {
                    if (d.isShared() 
                            && !d.isStatic() 
                            && !d.isConstructor() 
                            && !isOverloadedVersion(d) 
                            && isResolvable(d) 
                            && !errors.contains(d.getName())) {
                        Declaration r = td.getMember(d.getName(), null, false);
                        //TODO: figure out which other declaration 
                        //      causes the problem and display it 
                        //      to the user!
                        if (r!=null && !r.refines(d) && 
                                //squash bogus error when there 
                                //is a dupe declaration
                                !r.getContainer().equals(td) &&
                                !((std instanceof Interface 
                                    || r.isInterfaceMember()) && 
                                  isDefinedInJava(std) &&
                                  isDefinedInJava(r))) {
                            TypeDeclaration ctd = 
                                    (TypeDeclaration) 
                                        r.getContainer();
                            that.addError("member '" 
                                    + d.getName() 
                                    + "' is inherited ambiguously by '" 
                                    + td.getName() 
                                    + "' from '" 
                                    + std.getName()
                                    + "' and a different generic instantiation of '"
                                    + ctd.getName() 
                                    + "' and is not refined by '" 
                                    + td.getName() 
                                    + "' (refine '"
                                    + d.getName() 
                                    + "' to satisfy both instantiations of '"
                                    + ctd.getName() 
                                    + "')", 
                                    350);
                            errors.add(d.getName());
                        }
                    }
                }
            }
        }
    }

    private static boolean isDefinedInJava(Declaration d) {
        if (d.isJava()) {
            return true;
        }
        else {
            Declaration rd = d.getRefinedDeclaration();
            return rd!=null 
                && !rd.equals(d) 
                && isDefinedInJava(rd);
        }
    }

}
