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

package org.eclipse.ceylon.compiler.java.codegen;

import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isBooleanFalse;
import static org.eclipse.ceylon.model.typechecker.model.ModelUtil.isBooleanTrue;

import java.util.List;

import org.eclipse.ceylon.compiler.java.util.Util;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree;
import org.eclipse.ceylon.compiler.typechecker.tree.TreeUtil;
import org.eclipse.ceylon.compiler.typechecker.tree.Visitor;
import org.eclipse.ceylon.compiler.typechecker.tree.Tree.AttributeDeclaration;
import org.eclipse.ceylon.model.loader.JvmBackendUtil;
import org.eclipse.ceylon.model.loader.model.FieldValue;
import org.eclipse.ceylon.model.loader.model.LazyClass;
import org.eclipse.ceylon.model.loader.model.LazyInterface;
import org.eclipse.ceylon.model.typechecker.model.Annotation;
import org.eclipse.ceylon.model.typechecker.model.Class;
import org.eclipse.ceylon.model.typechecker.model.ClassOrInterface;
import org.eclipse.ceylon.model.typechecker.model.ConditionScope;
import org.eclipse.ceylon.model.typechecker.model.Constructor;
import org.eclipse.ceylon.model.typechecker.model.ControlBlock;
import org.eclipse.ceylon.model.typechecker.model.Declaration;
import org.eclipse.ceylon.model.typechecker.model.Function;
import org.eclipse.ceylon.model.typechecker.model.FunctionOrValue;
import org.eclipse.ceylon.model.typechecker.model.Functional;
import org.eclipse.ceylon.model.typechecker.model.Interface;
import org.eclipse.ceylon.model.typechecker.model.IntersectionType;
import org.eclipse.ceylon.model.typechecker.model.ModelUtil;
import org.eclipse.ceylon.model.typechecker.model.Module;
import org.eclipse.ceylon.model.typechecker.model.ModuleImportList;
import org.eclipse.ceylon.model.typechecker.model.NamedArgumentList;
import org.eclipse.ceylon.model.typechecker.model.Package;
import org.eclipse.ceylon.model.typechecker.model.Parameter;
import org.eclipse.ceylon.model.typechecker.model.ParameterList;
import org.eclipse.ceylon.model.typechecker.model.Scope;
import org.eclipse.ceylon.model.typechecker.model.Setter;
import org.eclipse.ceylon.model.typechecker.model.Type;
import org.eclipse.ceylon.model.typechecker.model.TypeDeclaration;
import org.eclipse.ceylon.model.typechecker.model.TypedDeclaration;
import org.eclipse.ceylon.model.typechecker.model.UnionType;
import org.eclipse.ceylon.model.typechecker.model.Unit;
import org.eclipse.ceylon.model.typechecker.model.Value;

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
     * Determines whether the declaration is a getter (a transient value 
     * that is not a parameter)
     * @param decl The declaration
     * @return true if the declaration is a getter
     */
    public static boolean isGetter(Declaration decl) {
        return decl instanceof Value
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
                && ModelUtil.isCaptured(decl);
    }
    
    public static boolean isMethodOrSharedOrCapturedParam(Declaration decl) {
        return decl instanceof Function 
            && !isConstructor(decl)
            && (!((Function)decl).isParameter() 
                    || ModelUtil.isCaptured(decl));
    }

    /**
     * Determines whether the declaration's is a method
     * @param decl The declaration
     * @return true if the declaration is a method
     */
    public static boolean isMethod(Declaration decl) {
        return JvmBackendUtil.isMethod(decl);
    }

    public static boolean isSmall(Declaration decl) {
        return decl instanceof FunctionOrValue
            && ((FunctionOrValue)decl).isSmall();
    }
    
    public static boolean isTransient(Declaration decl) {
        return decl instanceof FunctionOrValue
            && ((FunctionOrValue)decl).isTransient();
    }

    public static boolean isVariable(Value decl) {
        return decl.isVariable() 
            && !hasSetter(decl);
    }
    
    public static boolean hasSetter(Value decl) {
        return decl.getSetter() != null;
    }
    
    public static boolean isIndirect(Tree.AttributeDeclaration decl) {
        return decl.getDeclarationModel().isTransient() 
            && decl.getSpecifierOrInitializerExpression() == null;
    }

    public static boolean isDeferred(Declaration decl) {
        return decl instanceof Function 
            && ((Function)decl).isDeferred();
    }
    
    /**
     * Determines whether the declaration is local to a method,
     * getter, setter or Class initializer.
     * @param decl The declaration
     * @return true if the declaration is local
     */
    public static boolean isLocal(Declaration decl) {
        return ModelUtil.isLocalNotInitializer(decl) 
            || ModelUtil.isLocalToInitializer(decl);
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
        return decl.isClassOrInterfaceMember()
            && (Decl.isValue(decl) || decl instanceof Setter)
            && ModelUtil.isCaptured(decl);
    }

    public static boolean isClassParameter(Declaration decl) {
        return decl.isClassOrInterfaceMember()
            && decl instanceof Value
            && ((Value)decl).isParameter()
            && ModelUtil.isCaptured(decl);
    }

    public static boolean isOverloaded(Declaration decl) {
        return decl instanceof Functional 
            && decl.isOverloaded();
    }

    public static boolean isJavaField(Declaration decl) {
        return decl instanceof FieldValue;
    }

    public static boolean isStatic(TypeDeclaration declaration) {
        return (declaration instanceof LazyClass || 
                declaration instanceof LazyInterface)
            && declaration.isStatic();
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
    
    public static boolean isValueTypeDecl(Tree.Term decl) {
        return decl != null 
            && isValueTypeDecl(decl.getTypeModel());
    }
    
    public static boolean isValueTypeDecl(TypedDeclaration decl) {
        return decl != null 
            && isValueTypeDecl(decl.getType());
    }

    public static boolean isValueTypeDecl(Type type) {
        if (type == null) {
            return false;
        }
        type = type.resolveAliases();
        if (type != null) {
            TypeDeclaration decl = type.getDeclaration();
            if (decl instanceof LazyClass) {
                return ((LazyClass)decl).isValueType();
            }
        }
        return false;
    }

    static boolean isRefinableMemberClass(Declaration model) {
        return model instanceof Class 
            && model.isMember()
            && (model.isFormal() || model.isDefault())
            && !model.isAnonymous()
            && ModelUtil.isCeylonDeclaration((Class)model);
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
        return type != null 
            && type.getDeclaration().isAnonymous();
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

    /**
     * Dodgy method that only uses an untypechecked tree to see if a declaration
     * is an annotation class, by checking that it's a class with an "annotation"
     * annotation.
     */
    public static boolean isAnnotationClassNoModel(Tree.Declaration decl) {
        return decl instanceof Tree.AnyClass
            && TreeUtil.hasAnnotation(decl.getAnnotationList(), "annotation", decl.getUnit());
    }
    
    /**
     * Dodgy method that only uses an untypechecked tree to see if an annotation class
     * (as determined previously by {@link isAnnotationClassNoModel}), by checking if 
     * it directly satisfies a "SequencedAnnotation" interface.
     */
    public static boolean isSequencedAnnotationClassNoModel(Tree.AnyClass decl) {
        if(decl.getSatisfiedTypes() != null){
            for(Tree.StaticType sat : ((Tree.AnyClass)decl).getSatisfiedTypes().getTypes()){
                if(sat instanceof Tree.BaseType 
                    && ((Tree.BaseType) sat).getIdentifier().getText().equals("SequencedAnnotation")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAnnotationClass(Declaration declarationModel) {
        return declarationModel instanceof Class
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
        return decl instanceof Class
            && decl.getQualifiedNameString()
                .equals("java.lang::ObjectArray");
    }

    public static boolean isAnnotationClassOrConstructor(Declaration container) {
        return isAnnotationClass(container) 
            || isAnnotationConstructor(container);
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
        return isAnonCaseOfEnumeratedType(term.getDeclaration());
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
    
    public static boolean isJavaStaticOrInterfacePrimary(Tree.Term term) {
        if (!(term instanceof Tree.QualifiedMemberOrTypeExpression))
            return false;
        Declaration decl = ((Tree.QualifiedMemberOrTypeExpression)term).getDeclaration();
        return decl != null
            && (decl.isStatic() || decl instanceof Interface)
            && !(decl instanceof Constructor);
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
                && !Decl.hasScopeInType(decl.getContainer(), primary.getTypeModel());
        }
        return false;
    }
    
    private static boolean hasScopeInType(Scope scope, Type type) {
        TypeDeclaration declaration = type.getDeclaration();
        if(declaration instanceof UnionType){
            for(Type st : type.getCaseTypes()){
                if(hasScopeInType(scope, st))
                    return true;
            }
            return false;
        }
        if(declaration instanceof IntersectionType){
            for(Type st : type.getSatisfiedTypes()){
                if(hasScopeInType(scope, st))
                    return true;
            }
            return false;
        }
        if(declaration instanceof ClassOrInterface){
            return equalScopeDecl(scope, declaration);
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
                && !Decl.hasScopeInType(decl.getContainer(), primary.getTypeModel());
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
        while (scope instanceof ConditionScope) {
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

    public static boolean isDefaultConstructor(Constructor ctor) {
        String name = ctor.getName();
        return name == null || name.isEmpty();
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
    
    public static boolean hasOnlyValueConstructors(Class cls) {
        if (cls.hasEnumerated()) {
            for (Declaration d : cls.getMembers()) {
                if (d instanceof Constructor &&
                        !((Constructor) d).isValueConstructor()) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean hasAnyValueConstructors(Class cls) {
        if (cls.hasEnumerated()) {
            for (Declaration d : cls.getMembers()) {
                if (d instanceof Constructor &&
                        ((Constructor) d).isValueConstructor()) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
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
        if (ModelUtil.isLocalNotInitializer(decl))
            return true;
        Scope container = decl.getContainer();
        while (container != null) {
            if (ModelUtil.isLocalNotInitializerScope(container))
                return true;
            container = container.getContainer();
        }
        return false;
    }

    public static boolean isConstructor(Declaration decl) {
        return ModelUtil.isConstructor(decl);
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
        if(container instanceof Function 
                && ((Declaration) container).isAnonymous()){
            return true;
        }
        if(container instanceof Value
                && !((Value) container).isTransient()){
            return true;
        }
        return false;
    }

    public static boolean isValueConstructor(Declaration member) {
        return member instanceof Value
            && ((Value)member).getTypeDeclaration() instanceof Constructor;
    }
    
    public static boolean isDynamic(Declaration decl) {
        if (decl instanceof TypeDeclaration) {
            return ((TypeDeclaration)decl).isDynamic();
        } else if (decl instanceof TypedDeclaration) {
            return ((TypedDeclaration)decl).isDynamicallyTyped();
        }
        return false;
    }
    
    public static boolean isJavaVariadic(Parameter parameter) {
        return parameter.isSequenced()
            && parameter.getDeclaration() instanceof Function
            && isJavaMethod((Function) parameter.getDeclaration());
    }
    
    public static boolean isJavaVariadicIncludingInheritance(Parameter parameter){
        if(!parameter.isSequenced())
            return false;
        if(isJavaVariadic(parameter))
            return true;
        // perhaps it refines a Java method
        Scope container = parameter.getModel().getContainer();
        if(container instanceof Function){
            Declaration refinedDeclaration = CodegenUtil.getTopmostRefinedDeclaration((Declaration) container);
            if(refinedDeclaration instanceof Function
                    && Decl.isJavaMethod((Function) refinedDeclaration))
                return true;
        }
        return false;
    }

    public static boolean isJavaMethod(Function method) {
        ClassOrInterface container = ModelUtil.getClassOrInterfaceContainer(method);
        return container != null && !ModelUtil.isCeylonDeclaration(container);
    }

    public static Parameter getLastParameterFromFirstParameterList(Function model) {
        if(!model.getParameterLists().isEmpty()){
            ParameterList parameterList = model.getParameterLists().get(0);
            if(!parameterList.getParameters().isEmpty()){
                return parameterList.getParameters().get(parameterList.getParameters().size()-1);
            }
        }
        return null;
    }
    
    public static TypeDeclaration getOuterScopeOfMemberInvocation(Tree.StaticMemberOrTypeExpression expr, 
            Declaration decl){
        // First check whether the expression is captured from an enclosing scope
        TypeDeclaration outer = null;
        // get the ClassOrInterface container of the declaration
        Scope stop = ModelUtil.getClassOrInterfaceContainer(decl, false);
        if (stop instanceof TypeDeclaration) {// reified scope
            Scope scope = expr.getScope();
            while (!(scope instanceof Package)) {
                if (scope.equals(stop)) {
                    outer = (TypeDeclaration)stop;
                    break;
                }
                scope = scope.getContainer();
            }
        }
        // If not it might be inherited...
        if (outer == null) {
            outer = expr.getScope().getInheritingDeclaration(decl);
        }
        return outer;
    }

    public static Declaration getToplevelDeclarationContainer(Declaration decl) {
        while (decl!=null) {
            Scope container = decl.getContainer();
            if (container instanceof Package || 
                container instanceof ModuleImportList) {
                return decl;
            }
            decl = getDeclarationScope(container);
        }
        return decl;
    }
    
    public static boolean isJavaArrayWith(Constructor ctor) {
        if (ctor.isClassMember() && "with".equals(ctor.getName())) {
            Unit unit = ctor.getUnit();
            Scope cls = ctor.getContainer();
            return unit.getJavaObjectArrayDeclaration().equals(cls)
                || unit.getJavaIntArrayDeclaration().equals(cls)
                || unit.getJavaLongArrayDeclaration().equals(cls)
                || unit.getJavaShortArrayDeclaration().equals(cls)
                || unit.getJavaDoubleArrayDeclaration().equals(cls)
                || unit.getJavaFloatArrayDeclaration().equals(cls)
                || unit.getJavaCharArrayDeclaration().equals(cls)
                || unit.getJavaByteArrayDeclaration().equals(cls)
                || unit.getJavaBooleanArrayDeclaration().equals(cls);
        }
        return false;
    }
    
    public static boolean isJavaObjectArrayWith(Constructor ctor) {
        if (ctor.isClassMember() && "with".equals(ctor.getName())) {
            Unit unit = ctor.getUnit();
            Scope cls = ctor.getContainer();
            if (cls instanceof Class) {
                return cls.equals(unit.getJavaObjectArrayDeclaration());
            }
        }
        return false;
    }

    public static boolean isJavaArrayFrom(Declaration decl) {
        if (decl.isClassMember() && "from".equals(decl.getName())) {
            Unit unit = decl.getUnit();
            Scope cls = decl.getContainer();
            if (cls instanceof Class) {
                return cls.equals(unit.getJavaObjectArrayDeclaration())
                    || cls.equals(unit.getJavaIntArrayDeclaration())
                    || cls.equals(unit.getJavaLongArrayDeclaration())
                    || cls.equals(unit.getJavaShortArrayDeclaration())
                    || cls.equals(unit.getJavaDoubleArrayDeclaration())
                    || cls.equals(unit.getJavaFloatArrayDeclaration())
                    || cls.equals(unit.getJavaCharArrayDeclaration())
                    || cls.equals(unit.getJavaByteArrayDeclaration())
                    || cls.equals(unit.getJavaBooleanArrayDeclaration());
            }
        }
        return false;
    }

    /**
     * A value is memoized if it's {@code late} and has a specifier
     * <pre><code> 
     *   late value foo = ...
     * </code></pre>
     * See #3544
     */
    public static boolean isMemoized(AttributeDeclaration decl) {
        return decl.getSpecifierOrInitializerExpression() != null
            && decl.getDeclarationModel().isLate();
    }
}
