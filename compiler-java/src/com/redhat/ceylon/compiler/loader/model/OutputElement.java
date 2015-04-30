package com.redhat.ceylon.compiler.loader.model;

import java.util.EnumMap;
import java.util.EnumSet;

import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ImportModule;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ModuleDescriptor;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.PackageDescriptor;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Enumerates the possible program elements capable of supporting 
 * Java annotations.
 */
public enum OutputElement {
    TYPE(AnnotationTarget.TYPE),
    FIELD(AnnotationTarget.FIELD),
    METHOD(AnnotationTarget.METHOD),
    GETTER(AnnotationTarget.METHOD),
    SETTER(AnnotationTarget.METHOD),
    PARAMETER(AnnotationTarget.PARAMETER),
    CONSTRUCTOR(AnnotationTarget.CONSTRUCTOR),
    LOCAL_VARIABLE(AnnotationTarget.LOCAL_VARIABLE),
    ANNOTATION_TYPE(AnnotationTarget.ANNOTATION_TYPE),
    PACKAGE(AnnotationTarget.PACKAGE);
    
    private final AnnotationTarget annotationTarget;

    private OutputElement(AnnotationTarget annotationTarget) {
        this.annotationTarget = annotationTarget;
    }
    
    public AnnotationTarget getAnnotationTarget() {
        return annotationTarget;
    }
    
    public static EnumSet<OutputElement> outputs(Tree.ObjectDefinition that) {
        return EnumSet.of(TYPE, CONSTRUCTOR, FIELD, GETTER);
    }
    
    public static EnumSet<OutputElement> outputs(Tree.AnyClass that) {
        EnumSet<OutputElement> result = EnumSet.of(TYPE);
        if (!that.getDeclarationModel().hasConstructors()) {
            result.add(CONSTRUCTOR);
        }
        if (that.getDeclarationModel().isAnnotation()) {
            result.add(ANNOTATION_TYPE);
        }
        return result;
    }

    public static EnumSet<OutputElement> outputs(PackageDescriptor annotated) {
        return EnumSet.of(TYPE);
    }

    public static EnumSet<OutputElement> outputs(ImportModule annotated) {
        return EnumSet.of(FIELD);
    }

    public static EnumSet<OutputElement> outputs(ModuleDescriptor annotated) {
        return EnumSet.of(TYPE);
    }
    
    public static EnumSet<OutputElement> outputs(Tree.TypeAliasDeclaration that) {
        return EnumSet.of(TYPE);
    }
    
    public static EnumSet<OutputElement> outputs(Tree.AnyInterface that) {
        return EnumSet.of(TYPE);
    }
    
    public static EnumSet<OutputElement> outputs(
            Tree.Constructor annotated) {
        return EnumSet.<OutputElement>of(CONSTRUCTOR);
    }
    
    public static EnumSet<OutputElement> outputs(Tree.AnyMethod that) {
        return EnumSet.of(METHOD);
    }
    
    public static EnumSet<OutputElement> outputs(Tree.AttributeGetterDefinition that) {
        return EnumSet.of(GETTER);
    }
    
    public static EnumSet<OutputElement> outputs(Tree.AttributeSetterDefinition that) {
        return EnumSet.of(SETTER);
    }
    
    public static EnumSet<OutputElement> outputs(Tree.AttributeDeclaration that) {
        EnumSet<OutputElement> result = EnumSet.noneOf(OutputElement.class);
        Value declarationModel = that.getDeclarationModel();
        if (declarationModel != null) {
            if (declarationModel.isClassMember()) {
                if (declarationModel.isParameter()) {
                    result.add(PARAMETER);
                }
                if (declarationModel.isShared() || declarationModel.isCaptured()) {
                    result.add(GETTER);
                    if (!(that.getSpecifierOrInitializerExpression() instanceof Tree.LazySpecifierExpression)) {
                        result.add(FIELD);
                    }
                } else if (!declarationModel.isParameter()) {
                    result.add(LOCAL_VARIABLE);
                }
            } else if (declarationModel.isInterfaceMember()) {
                result.add(GETTER);
            } else if (declarationModel.isToplevel()) {
                result.add(GETTER);
                result.add(FIELD);
            } else {
                if (declarationModel.isParameter()) {
                    result.add(PARAMETER);
                } else {
                    result.add(LOCAL_VARIABLE);
                }
            }
        }
        
        if (result.contains(GETTER) 
                && (declarationModel.isVariable() || declarationModel.isLate())) {
            result.add(SETTER);
        }
        return result;
    }
    
    public static EnumSet<OutputElement> possibleCeylonTargets(EnumSet<AnnotationTarget> targets) {
        if (targets == null) {
            targets = EnumSet.allOf(AnnotationTarget.class);
        }
        EnumSet<OutputElement> result = EnumSet.noneOf(OutputElement.class);
        for (AnnotationTarget t : targets) {
            for (OutputElement oe : OutputElement.values()) {
                if (oe.getAnnotationTarget() == t) {
                    result.add(oe);
                }
            }
        }
        return result;
    }
}