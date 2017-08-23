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

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import static com.redhat.ceylon.model.loader.model.OutputElement.*;

import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.compiler.typechecker.analyzer.Warning;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.Annotation;
import com.redhat.ceylon.model.loader.model.AnnotationProxyClass;
import com.redhat.ceylon.model.loader.model.AnnotationProxyMethod;
import com.redhat.ceylon.model.loader.model.AnnotationTarget;
import com.redhat.ceylon.model.loader.model.OutputElement;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeAlias;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class AnnotationUtil {

    /**
     * Returns the set of output program elements that the given annotation 
     * could be applied to. If the {@code errors} flag is true then add 
     * warnings/errors to the tree about ambiguous/impossible targets.
     */
    public static EnumSet<OutputElement> interopAnnotationTargeting(boolean isEe, EnumSet<OutputElement> outputs,
            Tree.Annotation annotation, boolean errors, boolean warnings,
            Declaration d) {
        Declaration annoCtor = ((Tree.BaseMemberExpression)annotation.getPrimary()).getDeclaration();
        if (annoCtor instanceof AnnotationProxyMethod) {
            AnnotationProxyMethod proxyCtor = (AnnotationProxyMethod)annoCtor;
            AnnotationProxyClass annoClass = proxyCtor.getProxyClass();
            EnumSet<OutputElement> possibleTargets;
            if (proxyCtor.getAnnotationTarget() != null) {
                possibleTargets = EnumSet.of(proxyCtor.getAnnotationTarget());
            } else {
                possibleTargets = AnnotationTarget.outputTargets(annoClass);
            }
            EnumSet<OutputElement> actualTargets = possibleTargets.clone();
            actualTargets.retainAll(outputs);
            
            if (actualTargets.size() > 1) {
                if (warnings) {
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
                    annotation.addUsageWarning(Warning.ambiguousAnnotation, sb.toString(), Backend.Java);
                }
                checkForLateFieldAnnotation(isEe, annotation, d, annoCtor, possibleTargets, actualTargets);
                return null;
            } else if (actualTargets.size() == 0) {
                if (errors) {
                    annotation.addError("no target for " + annoCtor.getName() + 
                            " annotation: @Target of @interface " + 
                            ((AnnotationProxyClass)annoClass).iface.getName() + 
                            " lists " + possibleTargets + 
                            " but annotated element tranforms to " + outputs, Backend.Java);
                }
            }
            
            checkForLateFieldAnnotation(isEe, annotation, d, annoCtor, possibleTargets, actualTargets);
        
            return actualTargets;
        } else {
            return null;
        }
    }

    protected static void checkForLateFieldAnnotation(
            boolean isEe,
            Tree.Annotation annotation, Declaration d, Declaration annoCtor,
            EnumSet<OutputElement> possibleTargets, EnumSet<OutputElement> actualTargets) {
        if (actualTargets.contains(OutputElement.FIELD)
                && d instanceof Value
                && !isEe
                && ((Value)d).isLate()
                && (annotation.getUnit().isOptionalType(((Value)d).getType())
                        || ((Value)d).getType().isInteger()
                        || ((Value)d).getType().isFloat()
                        || ((Value)d).getType().isBoolean()
                        || ((Value)d).getType().isByte()
                        || ((Value)d).getType().isCharacter())) {
            StringBuilder sb = new StringBuilder();
            sb.append("the 'late' attribute '").append(d.getName())
              .append("' cannot be properly initialized just by setting the field value ")
              .append("because ").append(annotation.getUnit().isOptionalType(((Value)d).getType()) ? "it has an optional type: " : "it is erased to a primitive type: ");
            sb.append("depending on the semantics of '"+annoCtor.getName()+"' consider "); 
            if (possibleTargets.contains(OutputElement.GETTER)) {
                sb.append("annotating the JavaBean Property getter with "+annoCtor.getName()+"__GETTER");
                if (possibleTargets.contains(OutputElement.SETTER)) {
                    sb.append(" or its setter with "+annoCtor.getName()+"__SETTER ");
                }
                sb.append("or ");
            }
            sb.append("making it non-'late'");
            annotation.addUsageWarning(Warning.ambiguousAnnotation, sb.toString(), Backend.Java);
        }
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

    public static EnumSet<OutputElement> outputs(Tree.PackageDescriptor annotated) {
        return EnumSet.of(TYPE, PACKAGE);
    }

    public static EnumSet<OutputElement> outputs(Tree.ImportModule annotated) {
        return EnumSet.of(FIELD);
    }

    public static EnumSet<OutputElement> outputs(Tree.ModuleDescriptor annotated) {
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
    
    public static EnumSet<OutputElement> outputs(
            Tree.Enumerated annotated) {
        return EnumSet.<OutputElement>of(CONSTRUCTOR, FIELD, GETTER);
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
                if (ModelUtil.isCaptured(declarationModel)) {
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
                // only non-lazy get fields
                if(!declarationModel.isTransient())
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
                // variables with lazy value MUST have a distinct setter
                && ((declarationModel.isVariable() && !declarationModel.isTransient()) 
                        || declarationModel.isLate())) {
            result.add(SETTER);
        }
        return result;
    }

    public static void duplicateInteropAnnotation(boolean isEe, EnumSet<OutputElement> outputs, List<Annotation> annotations, Declaration d) {
        for (int i=0; i<annotations.size(); i++) {
            Tree.Annotation ann = annotations.get(i);
            Type t = ann.getTypeModel();
            EnumSet<OutputElement> mainTargets = interopAnnotationTargeting(isEe, outputs, ann, false, false, d);
            if (t!=null && mainTargets != null) {
                TypeDeclaration td = t.getDeclaration();
                if (!ModelUtil.isCeylonDeclaration(td)) {
                    for (int j=0; j<i; j++) {
                        Tree.Annotation other = annotations.get(j);
                        Type ot =  other.getTypeModel();
                        if (ot!=null) {
                            TypeDeclaration otd = ot.getDeclaration();
                            if (otd.equals(td)) {
                                // check if they have the same targets (if not that's fine)
                                EnumSet<OutputElement> dupeTargets = interopAnnotationTargeting(isEe, outputs, other, false, false, d);
                                if(dupeTargets != null){
                                    EnumSet<OutputElement> sameTargets = intersection(mainTargets, dupeTargets);
                                    if(!sameTargets.isEmpty()){
                                        ann.addError("duplicate annotation: there are multiple annotations of type '" + 
                                                td.getName() + "' for targets: '"+sameTargets+"'");
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static EnumSet<OutputElement> intersection(EnumSet<OutputElement> mainTargets, EnumSet<OutputElement> dupeTargets) {
        EnumSet<OutputElement> intersect = EnumSet.copyOf(mainTargets);
        intersect.retainAll(dupeTargets);
        return intersect;
    }

    /**
     * Whether an annotation (with the given {@code annotationCtorDecl} 
     * annotation constructor) used on the given declaration ({@code useSite})
     * should be added to the Java annotations of the given generated program 
     * elements ({@code target}) 
     * @param annotationCtorDecl
     * @param useSite
     * @param target
     * @return
     */
    public static boolean isNaturalTarget(
            Function annotationCtorDecl, // use site is either a Declaration, or a Package, or a Module, 
            // or a Tree.ImportModule. Yes that's ugly as hell, but
            // there's no supertype for annotated things, nor a "model" for 
            // module imports
            Object useSite, OutputElement target) {
        EnumSet<AnnotationTarget> interopTargets;
        if (annotationCtorDecl instanceof AnnotationProxyMethod) {
            AnnotationProxyMethod annotationProxyMethod = (AnnotationProxyMethod)annotationCtorDecl;
            if (annotationProxyMethod.getAnnotationTarget() == target) {
                // Foo__WHATEVER, so honour the WHATEVER
                return true;
            }
            interopTargets = annotationProxyMethod.getAnnotationTargets(); 
        } else {
            interopTargets = null;
        }
        if (useSite instanceof Declaration) {
            if (ModelUtil.isConstructor((Declaration)useSite)) {
                if (useSite instanceof Functional) {
                    return target == OutputElement.CONSTRUCTOR;
                } else if (useSite instanceof Value) {
                    // If the constructor has a getter we can't annotate, let's
                    // put the annotations on the constructor
                    Class constructedClass = ModelUtil.getConstructedClass((Declaration) useSite);
                    // See CeylonVisitor.transformSingletonConstructor for those tests
                    if(constructedClass.isToplevel() || constructedClass.isClassMember())
                        return target == OutputElement.GETTER;
                    return target == OutputElement.CONSTRUCTOR;
                }
            } else if (useSite instanceof Class) {
                if (((Class)useSite).getParameterList() != null
                        && interopTargets != null
                        && interopTargets.contains(AnnotationTarget.CONSTRUCTOR)
                        && !interopTargets.contains(AnnotationTarget.TYPE)) {
                    return target == OutputElement.CONSTRUCTOR;
                }
                return target == OutputElement.TYPE;
            } else  if (useSite instanceof Interface) {
                return target == OutputElement.TYPE;
            } else if (useSite instanceof Value) {
                Value value = (Value)useSite;
                boolean p = value.isParameter()
                        && target == OutputElement.PARAMETER;
                if (annotationCtorDecl instanceof AnnotationProxyMethod) {
                    if (!value.isTransient() &&
                            (interopTargets == null ||
                            interopTargets.contains(AnnotationTarget.FIELD))) {
                        return target == OutputElement.FIELD;
                    } else {
                        return target == OutputElement.GETTER;
                    }
                } else {
                    return p || target == OutputElement.GETTER;
                }
            } else if (useSite instanceof Setter) {
                return target == OutputElement.SETTER;
            } else if (useSite instanceof Function) {
                return target == OutputElement.METHOD;
            } else if (useSite instanceof Constructor) {
                return target == OutputElement.CONSTRUCTOR;
            } else if (useSite instanceof TypeAlias) {
                return target == OutputElement.TYPE;
            } 
        } else if (useSite instanceof Package) {
            return (annotationCtorDecl instanceof AnnotationProxyMethod) ?  target == OutputElement.PACKAGE : target == OutputElement.TYPE;
        } else if (useSite instanceof Module) {
            return target == OutputElement.TYPE;
        } else if (useSite instanceof Tree.ImportModule) {
            return target == OutputElement.FIELD;
        }
        throw new RuntimeException(""+useSite);
    }
}
