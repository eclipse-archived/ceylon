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
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class AnnotationUtil {

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
                possibleTargets = possibleCeylonTargets(AnnotationTarget.annotationTargets(annoClass));
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
                    annotation.addUsageWarning(Warning.ambiguousAnnotation, sb.toString(), Backend.Java);
                }
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
        
            return actualTargets;
        } else {
            return null;
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
        return EnumSet.of(TYPE);
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

    public static void duplicateInteropAnnotation(EnumSet<OutputElement> outputs, List<Annotation> annotations) {
        for (int i=0; i<annotations.size(); i++) {
            Tree.Annotation ann = annotations.get(i);
            Type t = ann.getTypeModel();
            EnumSet<OutputElement> mainTargets = interopAnnotationTargeting(outputs, ann, false);
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
                                EnumSet<OutputElement> dupeTargets = interopAnnotationTargeting(outputs, other, false);
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
}
