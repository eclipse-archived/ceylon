package com.redhat.ceylon.compiler.loader.model;

import java.util.EnumSet;
import java.util.Iterator;

import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;


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
    PACKAGE,
    TYPE_USE,
    TYPE_PARAMETER;
    
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
    

    public static EnumSet<OutputElement> interopAnnotationTargeting(EnumSet<OutputElement> outputs,
            Tree.Annotation annotation, boolean errors) {
        Declaration annoCtor = ((Tree.BaseMemberExpression)annotation.getPrimary()).getDeclaration();
        if (annoCtor instanceof AnnotationProxyMethod) {
            EnumSet<OutputElement> possibleTargets;
            AnnotationProxyMethod proxyCtor = (AnnotationProxyMethod)annoCtor;
            AnnotationProxyClass annoClass = proxyCtor.getProxyClass();
            if (proxyCtor.getAnnotationTarget() != null) {
                possibleTargets = EnumSet.of(proxyCtor.getAnnotationTarget());
            } else {
                possibleTargets = OutputElement.possibleCeylonTargets(AnnotationTarget.annotationTargets(annoClass));
            }
            if (possibleTargets == null) {
                return null;
            }
            EnumSet<OutputElement> actualTargets = possibleTargets.clone();
            actualTargets.retainAll(outputs);
            
            if (actualTargets.size() > 1) {
                if (errors) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("ambiguous annotation target: ").append(annoCtor.getName());
                    sb.append(" could be applied to several targets, use one of ");
                    for (Iterator<OutputElement> iterator = actualTargets.iterator(); iterator.hasNext();) {
                        OutputElement x = iterator.next();
                        sb.append(Naming.getDisambigAnnoCtorName((Interface)((AnnotationProxyMethod) annoCtor).getProxyClass().iface, x));
                        if (iterator.hasNext()) {
                            sb.append(", ");
                        }
                    }
                    sb.append(" to disambiguate");
                    annotation.addUsageWarning(Warning.ambiguousAnnotation, sb.toString());
                }
                return null;
            } else if (actualTargets.size() == 0) {
                if (errors) {
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
