/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package com.redhat.ceylon.compiler.java.codegen;

import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isBooleanFalse;
import static com.redhat.ceylon.model.typechecker.model.ModelUtil.isBooleanTrue;

import java.util.List;

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.loader.model.FieldValue;
import com.redhat.ceylon.model.loader.model.LazyClass;
import com.redhat.ceylon.model.loader.model.LazyInterface;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.ConditionScope;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.ControlBlock;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Element;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.NamedArgumentList;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Utility functions telling you about Ceylon declarations
 * @see Strategy
 */
public class Decl {
    private Decl() {
    }

    public static boolean equal(Declaration decl, Declaration other) {
        return ModelUtil.equal(decl, other);
    }

    public static boolean equal(Parameter decl, Parameter other) {
        return ModelUtil.eq(decl, other);
    }
    
    public static boolean equalScopes(Scope scope, Scope other) {
        return ModelUtil.eq(scope, other);
    }
    
    public static boolean equalScopeDecl(Scope scope, Declaration other) {
        return ModelUtil.eq(scope, other);
    }
    
    public static boolean equalModules(Module scope, Module other) {
        return ModelUtil.equalModules(scope, other);
    }
    
    /**
     * Returns the declaration's container
     * @param decl The declaration
     * @return the declaration's container
     */
    public static Scope container(Tree.Declaration decl) {
        return container(decl.getDeclarationModel());
    }

    /**
     * Returns the declaration's container
     * @param decl The declaration
     * @return the declaration's container
     */
    public static Scope container(Declaration decl) {
        return decl.getContainer();
    }
    
    /**
     * Determines whether the declaration is a getter (a transient value 
     * that is not a parameter)
     * @param decl The declaration
     * @return true if the declaration is a getter
     */
    public static boolean isGetter(Declaration decl) {
        return (decl instanceof Value) 
                && !((Value)decl).isParameter() 
                && ((Value)decl).isTransient();
    }

    public static boolean isNonTransientValue(Declaration decl) {
        return ModelUtil.isNonTransientValue(decl);
    }
    
    public static boolean isSharedParameter(Declaration decl) {
        return decl instanceof Value 
                && ((Value)decl).isParameter() 
                && decl.isShared();
    }
    
    
    /**
     * Determines whether the declaration is a non-transient non-parameter value 
     * (not a getter)
     * @param decl The declaration
     * @return true if the declaration is a value
     */
    public static boolean isValue(Declaration decl) {
        return JvmBackendUtil.isValue(decl);
    }

    /**
     * Determines whether the declaration's is a value or a shared value parameter
     * @param decl The declaration
     * @return true if the declaration is a value or a shared value parameter
     */
    public static boolean isValueOrSharedParam(Declaration decl) {
        return isValue(decl) 
                || decl instanceof Value 
                    && ((Value)decl).isParameter() 
                    && decl.isShared();
    }
    
    public static boolean isValueOrSharedOrCapturedParam(Declaration decl) {
        return isValue(decl) 
                || decl instanceof Value 
                    && ((Value)decl).isParameter() 
                    && (decl.isShared() || decl.isCaptured());
    }
    
    public static boolean isMethodOrSharedOrCapturedParam(Declaration decl) {
        return decl instanceof Function 
                && !isConstructor(decl)
                    && (!((Function)decl).isParameter() || decl.isShared() || decl.isCaptured());
    }

    /**
     * Determines whether the declaration's is a method
     * @param decl The declaration
     * @return true if the declaration is a method
     */
    public static boolean isMethod(Declaration decl) {
        return JvmBackendUtil.isMethod(decl);
    }

    /**
     * Determines whether the declaration's containing scope is a method
     * @param decl The declaration
     * @return true if the declaration is within a method
     */
    public static boolean withinMethod(Tree.Declaration decl) {
        return withinMethod(decl.getDeclarationModel());
    }
    
    /**
     * Determines whether the declaration's containing scope is a getter
     * @param decl The declaration
     * @return true if the declaration is within a getter
     */
    public static boolean withinGetter(Tree.Declaration decl) {
        return withinGetter(decl.getDeclarationModel());
    }
    
    /**
     * Determines whether the declaration's containing scope is a setter
     * @param decl The declaration
     * @return true if the declaration is within a setter
     */
    public static boolean withinSetter(Tree.Declaration decl) {
        return withinSetter(decl.getDeclarationModel());
    }

    /**
     * Determines whether the declaration's containing scope is a method
     * @param decl The declaration
     * @return true if the declaration is within a method
     */
    public static boolean withinMethod(Declaration decl) {
        return container(decl) instanceof Function;
    }
    
    /**
     * Determines whether the declaration's containing scope is a getter
     * @param decl The declaration
     * @return true if the declaration is within a getter
     */
    public static boolean withinGetter(Declaration decl) {
        Scope s = container(decl);
        return isGetter((Declaration)s);
    }
    
    /**
     * Determines whether the declaration's containing scope is a setter
     * @param decl The declaration
     * @return true if the declaration is within a setter
     */
    public static boolean withinSetter(Declaration decl) {
        return container(decl) instanceof Setter;
    }
    
    /**
     * Determines whether the declaration's containing scope is a package
     * @param decl The declaration
     * @return true if the declaration is within a package
     */
    public static boolean withinPackage(Tree.Declaration decl) {
        return container(decl) instanceof com.redhat.ceylon.model.typechecker.model.Package;
    }
    
    /**
     * Determines whether the declaration's containing scope is a class
     * @param decl The declaration
     * @return true if the declaration is within a class
     */
    public static boolean withinClass(Tree.Declaration decl) {
        return container(decl) instanceof com.redhat.ceylon.model.typechecker.model.Class;
    }
    
    public static boolean withinClass(Declaration decl) {
        return ModelUtil.withinClass(decl);
    }
    
    /**
     * Determines whether the declaration's containing scope is an interface
     * @param decl The declaration
     * @return true if the declaration is within an interface
     */
    public static boolean withinInterface(Tree.Declaration decl) {
        return container(decl) instanceof com.redhat.ceylon.model.typechecker.model.Interface;
    }
    
    public static boolean withinInterface(Declaration decl) {
        return container(decl) instanceof com.redhat.ceylon.model.typechecker.model.Interface;
    }
    
    /**
     * Determines whether the declaration's containing scope is a class or interface
     * @param decl The declaration
     * @return true if the declaration is within a class or interface
     */
    public static boolean withinClassOrInterface(Tree.Declaration decl) {
        return withinClassOrInterface(decl.getDeclarationModel());
    }
    
    /**
     * Determines whether the declaration's containing scope is a class or interface
     * @param decl The declaration
     * @return true if the declaration is within a class or interface
     */
    public static boolean withinClassOrInterface(Declaration decl) {
        return ModelUtil.withinClassOrInterface(decl);
    }
    
    public static boolean isShared(Tree.Declaration decl) {
        return isShared(decl.getDeclarationModel());
    }
    
    public static boolean isShared(Declaration decl) {
        return decl.isShared();
    }
    
    public static boolean isCaptured(Tree.Declaration decl) {
        return isCaptured(decl.getDeclarationModel());
    }
    
    public static boolean isCaptured(Declaration decl) {
        return ModelUtil.isCaptured(decl);
    }

    public static boolean isAbstract(Tree.ClassOrInterface decl) {
        return decl.getDeclarationModel().isAbstract();
    }

    public static boolean isDefault(Tree.Declaration decl) {
        return decl.getDeclarationModel().isDefault();
    }

    public static boolean isFormal(Tree.Declaration decl) {
        return decl.getDeclarationModel().isFormal();
    }

    public static boolean isActual(Tree.Declaration decl) {
        return isActual(decl.getDeclarationModel());
    }
    
    public static boolean isActual(Declaration decl) {
        return decl.isActual();
    }

    public static boolean isTransient(Tree.AttributeDeclaration decl) {
        return decl.getDeclarationModel().isTransient();
    }

    public static boolean isTransient(Declaration decl) {
        if (decl instanceof FunctionOrValue) {
            return ((FunctionOrValue)decl).isTransient();
        } else {
            return false;
        }
    }

    public static boolean isVariable(Tree.AttributeDeclaration decl) {
        return decl.getDeclarationModel().isVariable() && !hasSetter(decl);
    }
    
    public static boolean hasSetter(Tree.AttributeDeclaration decl) {
        return decl.getDeclarationModel().getSetter() != null;
    }
    
    public static boolean isLate(Tree.AttributeDeclaration decl) {
        return isLate(decl.getDeclarationModel());
    }

    public static boolean isLate(Value model) {
        return model.isLate();
    }
    
    public static boolean isIndirect(Tree.AttributeDeclaration decl) {
        return decl.getDeclarationModel().isTransient() && decl.getSpecifierOrInitializerExpression() == null;
    }

    public static boolean isToplevel(Tree.Declaration decl) {
        return isToplevel(decl.getDeclarationModel());
    }
    
    public static boolean isToplevel(Declaration decl) {
        return decl.isToplevel();
    }
    
    public static boolean isDeferred(Tree.Declaration decl) {
        return isDeferred(decl.getDeclarationModel());
    }
    
    public static boolean isDeferred(Declaration decl) {
        return (decl instanceof Function) && ((Function)decl).isDeferred();
    }
    
    /**
     * Determines whether the declaration is local to a method,
     * getter or setter, but <strong>returns {@code false} for a declaration 
     * local to a Class initializer.</strong>
     * @param decl The declaration
     * @return true if the declaration is local
     */
    public static boolean isLocalNotInitializer(Tree.Declaration decl) {
        return isLocalNotInitializer(decl.getDeclarationModel());
    }

    /**
     * Determines whether the declaration is local to a method,
     * getter or setter, but <strong>returns {@code false} for a declaration 
     * local to a Class initializer.</strong>
     * @param decl The declaration
     * @return true if the declaration is local
     */
    public static boolean isLocalNotInitializer(Declaration decl) {
        return ModelUtil.isLocalNotInitializer(decl);
    }
    
    /**
     * Determines whether the declaration is local to a method,
     * getter, setter or Class initializer.
     * @param decl The declaration
     * @return true if the declaration is local
     */
    public static boolean isLocal(Tree.Declaration decl) {
        return isLocal(decl.getDeclarationModel());
    }

    /**
     * Determines whether the declaration is local to a method,
     * getter, setter or Class initializer.
     * @param decl The declaration
     * @return true if the declaration is local
     */
    public static boolean isLocal(Declaration decl) {
        return isLocalNotInitializer(decl) || isLocalToInitializer(decl);
    }
    
    /**
     * Is the given scope a local scope but not an initializer scope?
     */
    public static boolean isLocalNotInitializerScope(Scope scope) {
        return ModelUtil.isLocalNotInitializerScope(scope);
    }
    
    /**
     * Determines whether the declaration is local or a descendant of a 
     * local. 
     * @param decl The declaration
     * @return true if the decl is local or descendant from a local
     */
    public static boolean isAncestorLocal(Tree.Declaration decl) {
        return isAncestorLocal(decl.getDeclarationModel());
    }
    
    /**
     * Determines whether the declaration is local or a descendant of a 
     * local. 
     * @param decl The declaration
     * @return true if the decl is local or descendant from a local
     */
    public static boolean isAncestorLocal(Declaration decl) {
        Scope container = decl.getContainer();
        while (container != null) {
            if (container instanceof FunctionOrValue
                    || container instanceof ControlBlock
                    || container instanceof NamedArgumentList) {
                return true;
            }
            container = container.getContainer();
        }
        return false;
    }
        
    public static boolean isClassAttribute(Declaration decl) {
        return (withinClassOrInterface(decl))
                && (Decl.isValue(decl) || decl instanceof Setter)
                && (decl.isCaptured() || decl.isShared());
    }

    public static boolean isClassParameter(Declaration decl) {
        return (withinClassOrInterface(decl))
                && (decl instanceof Value) 
                && ((Value)decl).isParameter()
                && (decl.isCaptured() || decl.isShared());
    }

    public static boolean isLocalToInitializer(Tree.Declaration decl) {
        return isLocalToInitializer(decl.getDeclarationModel());
    }
    
    public static boolean isLocalToInitializer(Declaration decl) {
        return ModelUtil.isLocalToInitializer(decl);
    }
    
    public static boolean isOverloaded(Declaration decl) {
        if (decl instanceof Functional) {
            return decl.isOverloaded();
        }
        return false;
    }

    public static boolean isJavaField(Declaration decl) {
        return decl instanceof FieldValue;
    }

    public static boolean isStatic(TypeDeclaration declaration) {
        if(declaration instanceof LazyClass){
            return ((LazyClass)declaration).isStatic();
        }
        if(declaration instanceof LazyInterface){
            return ((LazyInterface)declaration).isStatic();
        }
        return false;
    }

    public static boolean isCeylon(TypeDeclaration declaration) {
        return JvmBackendUtil.isCeylon(declaration);
    }

    /**
     * Is the declaration a method declared to return {@code void} 
     * (as opposed to a {@code Anything})
     */
    public static boolean isUnboxedVoid(Declaration decl) {
        return Util.isUnboxedVoid(decl);
    }
    
    public static boolean isMpl(Functional decl) {
        return decl.getParameterLists().size() > 1;
    }
    
    public static ClassOrInterface getClassOrInterfaceContainer(Element decl){
        return ModelUtil.getClassOrInterfaceContainer(decl);
    }
    
    public static ClassOrInterface getClassOrInterfaceContainer(Element decl, boolean includingDecl){
        return ModelUtil.getClassOrInterfaceContainer(decl, includingDecl);
    }

    public static Package getPackage(Declaration decl){
        return ModelUtil.getPackage(decl);
    }

    public static Package getPackageContainer(Scope scope){
        return ModelUtil.getPackageContainer(scope);
    }

    public static Module getModule(Declaration decl){
        return ModelUtil.getModule(decl);
    }

    public static Module getModuleContainer(Scope scope) {
        return ModelUtil.getModuleContainer(scope);
    }

    public static boolean isValueTypeDecl(Tree.Term decl) {
        if (decl != null){
            return isValueTypeDecl(decl.getTypeModel());
        }
        return false;
    }
    
    public static boolean isValueTypeDecl(TypedDeclaration decl) {
        if (decl != null){
            return isValueTypeDecl(decl.getType());
        }
        return false;
    }

    public static boolean isValueTypeDecl(Type type) {
        if(type == null)
            return false;
        type = type.resolveAliases();
        if ((type != null) && type.getDeclaration() instanceof LazyClass) {
            return ((LazyClass)type.getDeclaration()).isValueType();
        }
        return false;
    }

    static boolean isRefinableMemberClass(Declaration model) {
        return model instanceof Class 
                && model.isMember()
                && (model.isFormal() || model.isDefault())
                && !model.isAnonymous()
                && isCeylon((Class)model);
    }
    
    /**
     * WARNING: this only works for Ceylon declarations!!!
     */
    public static String className(Declaration decl) {
        String name = decl.getQualifiedNameString().replace("::", ".");
        if (Naming.isLowerCase(decl.getName())) {
            name += "_";
        }
        return name;
    }

    public static Tree.Term unwrapExpressionsUntilTerm(Tree.Term term) {
        while (term instanceof Tree.Expression) {
            term = ((Tree.Expression)term).getTerm();
        }
        return term;
    }
    
    /**
     * Determines whether the given attribute should be accessed and assigned 
     * via a {@code VariableBox}
     */
    public static boolean isBoxedVariable(Tree.AttributeDeclaration attr) {
        return isBoxedVariable(attr.getDeclarationModel());
    }
    
    /**
     * Determines whether the given attribute should be accessed and assigned 
     * via a {@code VariableBox}
     */
    public static boolean isBoxedVariable(TypedDeclaration attr) {
        return JvmBackendUtil.isBoxedVariable(attr);
    }

    public static boolean isObjectValue(TypedDeclaration attr) {
        Type type = attr.getType();
        // Check type because in case of compile errors it can be null
        if (type != null) {
            TypeDeclaration typeDecl = type.getDeclaration();
            return typeDecl.isAnonymous();
        }
        return false;
    }

    public static boolean isAnnotationConstructor(Tree.AnyMethod def) {
        return isAnnotationConstructor(def.getDeclarationModel());
    }
    
    public static boolean isAnnotationConstructor(Declaration def) {
        return def.isToplevel()
                && def instanceof Function
                && containsAnnotationAnnotation(def);
    }

    private static boolean containsAnnotationAnnotation(
            Declaration decl) {
        List<Annotation> annotations = decl.getAnnotations();
        if (annotations != null) {
            for (Annotation ann : annotations) {
                if ("annotation".equals(ann.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAnnotationClass(Tree.ClassOrInterface def) {
        return isAnnotationClass(def.getDeclarationModel());
    }

    public static boolean isAnnotationClass(Declaration declarationModel) {
        return (declarationModel instanceof Class)
                && containsAnnotationAnnotation(declarationModel);
    }

    /** 
     * Determines whether the given annotation class is a synthetic "proxy" 
     * created by LazyPackage
     */
    public static boolean isInteropAnnotationClass(
            ClassOrInterface annotationClass) {   
        return annotationClass.getName().endsWith("$Proxy");
    }
    
    public static boolean isJavaArray(TypeDeclaration decl) {
        return JvmBackendUtil.isJavaArray(decl);
    }

    public static boolean isJavaObjectArray(TypeDeclaration decl) {
        if(decl instanceof Class == false)
            return false;
        Class c = (Class) decl;
        String name = c.getQualifiedNameString();
        return name.equals("java.lang::ObjectArray");
    }

    public static boolean isAnnotationClassOrConstructor(Declaration container) {
        return isAnnotationClass(container) || isAnnotationConstructor(container);
    }
    
    public static Tree.SpecifierOrInitializerExpression getDefaultArgument(Tree.Parameter parameter) {
        if (parameter instanceof Tree.InitializerParameter) {
            return ((Tree.InitializerParameter)parameter).getSpecifierExpression();
        } else if (parameter instanceof Tree.ValueParameterDeclaration) {
            Tree.TypedDeclaration typedDeclaration = ((Tree.ValueParameterDeclaration)parameter).getTypedDeclaration();
            if (typedDeclaration != null) {
                return ((Tree.AttributeDeclaration)typedDeclaration).getSpecifierOrInitializerExpression();
            }
        } else if (parameter instanceof Tree.FunctionalParameterDeclaration) {
            Tree.TypedDeclaration typedDeclaration = ((Tree.FunctionalParameterDeclaration)parameter).getTypedDeclaration();
            if (typedDeclaration != null) {
                return ((Tree.MethodDeclaration)typedDeclaration).getSpecifierExpression();
            }
        }
        return null;
    }
    
    /** 
     * Returns the tree for the member of the given class corresponding 
     * to the given parameter. For initializer parameters this involves 
     * walking the class.  
     */
    public static Tree.TypedDeclaration getMemberDeclaration(Tree.Declaration def, final Tree.Parameter parameter) {
        if (parameter instanceof Tree.ParameterDeclaration) {
            return ((Tree.ParameterDeclaration)parameter).getTypedDeclaration();
        } else if (parameter instanceof Tree.InitializerParameter) {
            final Tree.TypedDeclaration[] annotationList = new Tree.TypedDeclaration[]{null};
            def.visit(new Visitor() {
                public void visit(Tree.MethodDeclaration that) {
                    if (that.getDeclarationModel().equals(parameter.getParameterModel().getModel())) {
                        annotationList[0] = that;
                    }
                }
                public void visit(Tree.AttributeDeclaration that) {
                    if (that.getDeclarationModel().equals(parameter.getParameterModel().getModel())) {
                        annotationList[0] = that;
                    }
                }
            });
            return annotationList[0];
        }
        return null;
    }
    
    public static Tree.AnnotationList getAnnotations(Tree.AnyClass def, final Tree.Parameter parameter) {
        return getMemberDeclaration(def, parameter).getAnnotationList();
    }
    
    public static boolean isParameter(Declaration decl) {
        return decl instanceof FunctionOrValue
                && ((FunctionOrValue)decl).isParameter();
    }
    
    public static boolean isFunctionalParameter(Declaration decl) {
        return decl instanceof Function
                && ((Function)decl).isParameter();
    }
    
    public static boolean isValueParameter(Declaration decl) {
        return decl instanceof Value
                && ((Value)decl).isParameter();
    }
    

    
    
    public static boolean isEnumeratedTypeWithAnonCases(Type parameterType) {
        if (parameterType.isBoolean()) {
            return false;
        }
        if (parameterType.getCaseTypes() == null) {
            return false;
        }
        for (Type type : parameterType.getCaseTypes()) {
            if (!type.isClass() || !type.getDeclaration().isAnonymous()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isAnonCaseOfEnumeratedType(Tree.BaseMemberExpression term) {
        Declaration decl = term.getDeclaration();
        return isAnonCaseOfEnumeratedType(decl);
    }
    public static boolean isAnonCaseOfEnumeratedType(Declaration decl) {
    
        if (isBooleanTrue(decl) || isBooleanFalse(decl)) {
            return false;
        }
        if (decl instanceof Value) {
            Type type = ((Value) decl).getType();
            if (type.isClass() && type.getDeclaration().isAnonymous()) {
                if (isEnumeratedTypeWithAnonCases(type.getExtendedType())) {
                    return true;
                }
                for (Type s : type.getSatisfiedTypes()) {
                    if (isEnumeratedTypeWithAnonCases(s)) {
                        return true;
                    }
                }
            }
        }
        return false;
        
    }
    
    public static boolean isJavaStaticPrimary(Tree.Term term) {
        return term instanceof Tree.QualifiedMemberOrTypeExpression
                && ((Tree.QualifiedMemberOrTypeExpression)term).getDeclaration() != null
                && ((Tree.QualifiedMemberOrTypeExpression)term).getDeclaration().isStaticallyImportable()
                && !(((Tree.QualifiedMemberOrTypeExpression)term).getDeclaration() instanceof Constructor);
    }
    
    public static boolean isConstructorPrimary(Tree.Term term) {
        if (term instanceof Tree.MemberOrTypeExpression) {
            Declaration decl = ((Tree.MemberOrTypeExpression)term).getDeclaration();
            return decl != null
                    && (isConstructor(decl)
                    || (decl instanceof Class
                            && ((Class)decl).hasConstructors()));
        } else {
            return false;
        }
        
    }
    
    
    /**
     * Is the member private and not visible from the primary (i.e. is an 
     * upcast required to be able to see that member) 
     */
    public static boolean isPrivateAccessRequiringUpcast(Tree.StaticMemberOrTypeExpression qual) {
        if (qual instanceof Tree.QualifiedMemberOrTypeExpression) {
            Tree.Primary primary = ((Tree.QualifiedMemberOrTypeExpression)qual).getPrimary();
            Declaration decl = qual.getDeclaration();
            return decl.isMember()
                    && !decl.isShared()
                    && !(decl instanceof Constructor) 
                    && decl.getContainer() instanceof Class
                    && !Decl.equalScopeDecl(decl.getContainer(), primary.getTypeModel().getDeclaration());
        }
        return false;
    }
    
    public static boolean isPrivateAccessRequiringCompanion(Tree.StaticMemberOrTypeExpression qual) {
        if (qual instanceof Tree.QualifiedMemberOrTypeExpression) {
            Tree.Primary primary = ((Tree.QualifiedMemberOrTypeExpression)qual).getPrimary();
            Declaration decl = qual.getDeclaration();
            return decl.isMember()
                    && !decl.isShared()
                    && decl.getContainer() instanceof Interface
                    && !Decl.equalScopeDecl(decl.getContainer(), primary.getTypeModel().getDeclaration());
        }
        return false;
    }
    
    public static Type getPrivateAccessType(Tree.StaticMemberOrTypeExpression qmte) {
        return ((TypeDeclaration)qmte.getDeclaration().getRefinedDeclaration().getContainer()).getType();
    }

    /**
     * Finds the first containing scope of the given scope which is not a ConditionScope.
     * @param scope
     * @return
     */
    public static Scope getNonConditionScope(Scope scope) {
        while (scope != null &&
                scope instanceof ConditionScope) {
            scope = scope.getContainer();
        }
        return scope;
    }
    

    /**
     * Returns true if the given model is a toplevel object expression type.
     */
    public static boolean isTopLevelObjectExpressionType(Declaration model) {
        return model instanceof Class
                && model.isAnonymous()
                && getNonSkippedContainer(model) instanceof Package
                && !model.isNamed();
    }

    /**
     * Returns true if the given model is an object expression type.
     */
    public static boolean isObjectExpressionType(Declaration model) {
        return model instanceof Class
                && model.isAnonymous()
                && !model.isNamed();
    }

    public static Constructor getDefaultConstructor(Class c) {
        for (Declaration d : c.getMembers()) {
            if (d instanceof Constructor
                    && d.getName() == null) {
                return (Constructor)d;
            }
        }
        return null;
    }
    
    public static boolean isDefaultConstructor(Constructor ctor) {
        if (ctor.getName() == null
                || ctor.getName().isEmpty()) {
            return true;
        }
        return false;
    }
    
    public static Class getConstructedClass(Declaration classOrCtor) {
        if (classOrCtor instanceof FunctionOrValue &&
                ((FunctionOrValue)classOrCtor).getTypeDeclaration() instanceof Constructor) {
            classOrCtor = ((FunctionOrValue)classOrCtor).getTypeDeclaration();
        }
        if (classOrCtor instanceof Constructor) {
            return (Class)classOrCtor.getContainer();
        } else if (classOrCtor instanceof Class) {
            return (Class)classOrCtor;
        } else {
            return null;
        }
    }
    
    /**
     * true iff the given class has any abstract constructors.
     */
    public static boolean hasAbstractConstructor(Class cls) {
        if (cls.hasConstructors()) {
            for (Declaration d : cls.getMembers()) {
                if (d instanceof Constructor &&
                        ((Constructor) d).isAbstract()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /** Is the given constructor an enumerated ("singleton") constructor */
    public static boolean isEnumeratedConstructor(Constructor ctor) {
        return ctor != null && ctor.getContainer().getDirectMember(ctor.getName(), null, false) instanceof Value;
    }
    
    /** Is the given value the result of an enumerated ("singleton") constructor */
    public static boolean isEnumeratedConstructor(Value value) {
        return value.getType().getDeclaration() instanceof Constructor;
    }

    public static Declaration getDeclarationScope(Scope scope) {
        while (true) {
            if (scope instanceof Declaration) {
                return (Declaration)scope;
            } else if (scope instanceof Package) {
                return null;
            }
            scope = scope.getContainer();
        }
    }
    
    public static boolean hasLocalNotInitializerAncestor(Declaration decl){
        if(isLocalNotInitializer(decl))
            return true;
        Scope container = decl.getContainer();
        while(container != null){
            if(ModelUtil.isLocalNotInitializerScope(container))
                return true;
            container = container.getContainer();
        }
        return false;
    }

    public static boolean isConstructor(Declaration decl) {
        return getConstructor(decl) != null;
    }
    
    public static Constructor getConstructor(Declaration decl) {
        if (decl instanceof Constructor) {
            return (Constructor)decl;
        } else if (decl instanceof FunctionOrValue
                && ((FunctionOrValue)decl).getTypeDeclaration() instanceof Constructor) {
            return (Constructor)((FunctionOrValue)decl).getTypeDeclaration();
        } else {
            return null;
        }
    }

    public static Scope getNonSkippedContainer(Declaration decl){
        if(decl instanceof Scope)
            return getNonSkippedContainer((Scope)decl);
        return getNonSkippedContainer(decl.getContainer());
    }

    public static Scope getNonSkippedContainer(Scope object){
        Scope scope = object.getContainer();
        while(skipContainer(scope))
            scope = scope.getContainer();
        return scope;
    }

    public static Scope getFirstDeclarationContainer(Scope object){
        Scope scope = object.getContainer();
        while(scope instanceof Package == false
                && scope instanceof Declaration == false)
            scope = scope.getContainer();
        return scope;
    }

    public static boolean skipContainer(Scope container) {
        // skip anonymous methods
        if(container instanceof Function && ((Declaration) container).isAnonymous()){
            return true;
        }
        if(container instanceof Value
                && !((Value) container).isTransient()){
            return true;
        }
        return false;
    }
}
