package com.redhat.ceylon.compiler.loader.model;

import java.util.EnumSet;

import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportModule;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ModuleDescriptor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PackageDescriptor;


/**
 * Mirrors the elements of {@link java.lang.annotation.ElementType} for 
 * the purpose of targeting java annotations.
 */
public enum AnnotationTarget {

    TYPE,
    FIELD,
    METHOD,
    PARAMETER,
    CONSTRUCTOR,
    LOCAL_VARIABLE,
    ANNOTATION_TYPE,
    PACKAGE;
    
    /**
     * Returns the possible targets of the given annotation proxy class, 
     * or null if the given class is not an annotation proxy.
     */
    public static EnumSet<AnnotationTarget> annotationTargets(Class annotationClass) {
        if (annotationClass instanceof AnnotationProxyClass) {
            return ((AnnotationProxyClass)annotationClass).getAnnotationTarget();
        } else {
            return null;
        }
    }
    
    public static EnumSet<AnnotationTarget> outputs(Tree.ObjectDefinition that) {
        return EnumSet.of(TYPE, CONSTRUCTOR, FIELD, METHOD);
    }
    
    public static EnumSet<AnnotationTarget> outputs(Tree.AnyClass that) {
        EnumSet<AnnotationTarget> result = EnumSet.of(TYPE);
        if (!that.getDeclarationModel().hasConstructors()) {
            result.add(CONSTRUCTOR);
        }
        if (that.getDeclarationModel().isAnnotation()) {
            result.add(ANNOTATION_TYPE);
        }
        return result;
    }

    public static EnumSet<AnnotationTarget> outputs(PackageDescriptor annotated) {
        return EnumSet.of(TYPE);
    }

    public static EnumSet<AnnotationTarget> outputs(ImportModule annotated) {
        return EnumSet.of(FIELD);
    }

    public static EnumSet<AnnotationTarget> outputs(ModuleDescriptor annotated) {
        return EnumSet.of(TYPE);
    }
    
    public static EnumSet<AnnotationTarget> outputs(Tree.TypeAliasDeclaration that) {
        return EnumSet.of(TYPE);
    }
    
    public static EnumSet<AnnotationTarget> outputs(Tree.AnyInterface that) {
        return EnumSet.of(TYPE);
    }
    
    public static EnumSet<AnnotationTarget> outputs(
            Tree.Constructor annotated) {
        return EnumSet.<AnnotationTarget>of(CONSTRUCTOR);
    }
    
    public static EnumSet<AnnotationTarget> outputs(Tree.AnyMethod that) {
        return EnumSet.of(METHOD);
    }
    
    public static EnumSet<AnnotationTarget> outputs(Tree.AttributeGetterDefinition that) {
        return EnumSet.of(METHOD);
    }
    
    public static EnumSet<AnnotationTarget> outputs(Tree.AttributeSetterDefinition that) {
        return EnumSet.of(METHOD);
    }
    
    public static EnumSet<AnnotationTarget> outputs(Tree.AttributeDeclaration that) {
        EnumSet<AnnotationTarget> result = EnumSet.noneOf(AnnotationTarget.class);
        Value declarationModel = that.getDeclarationModel();
        if (declarationModel != null) {
            if (declarationModel.isClassMember()) {
                if (declarationModel.isParameter()) {
                    result.add(PARAMETER);
                }
                result.add(METHOD);
                if (!(that.getSpecifierOrInitializerExpression() instanceof Tree.LazySpecifierExpression)) {
                    result.add(FIELD);
                }
            } else if (declarationModel.isInterfaceMember()) {
                result.add(METHOD);
            } else if (declarationModel.isToplevel()) {
                result.add(METHOD);
                result.add(FIELD);
            } else {
                if (declarationModel.isParameter()) {
                    result.add(PARAMETER);
                } else {
                    result.add(LOCAL_VARIABLE);
                }
            }
        }
        return result;
    }
    

    public static EnumSet<AnnotationTarget> interopAnnotationTargeting(EnumSet<AnnotationTarget> outputs,
            Tree.Annotation annotation, boolean errors) {
        Declaration annoCtor = ((Tree.BaseMemberExpression)annotation.getPrimary()).getDeclaration();
        if (annoCtor instanceof AnnotationProxyMethod) {
            EnumSet<AnnotationTarget> possibleTargets;
            AnnotationProxyMethod proxyCtor = (AnnotationProxyMethod)annoCtor;
            AnnotationProxyClass annoClass = proxyCtor.getProxyClass();
            if (proxyCtor.getAnnotationTarget() != null) {
                possibleTargets = EnumSet.of(proxyCtor.getAnnotationTarget());
            } else {
                possibleTargets = AnnotationTarget.annotationTargets(annoClass);
            }
            if (possibleTargets == null) {
                return null;
            }
            EnumSet<AnnotationTarget> actualTargets = possibleTargets.clone();
            actualTargets.retainAll(outputs);
            if (errors) {
                if (actualTargets.size() > 1) {
                    annotation.addError("ambiguous annotation target: " + annoCtor.getName() + 
                            " could be applied to any of " + actualTargets);
                } else if (actualTargets.size() == 0) {
                    annotation.addError("no target for " + annoCtor.getName() + 
                            " annotation: @Target of @interface " + 
                            ((AnnotationProxyClass)annoClass).iface.getName() + 
                            " lists " + possibleTargets + 
                            " but annotated element tranforms to " + outputs);
                }
            }
            return actualTargets;
        } else {
            return null;
        }
    }
}
