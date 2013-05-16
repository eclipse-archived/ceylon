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

import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

public class ParameterDefinitionBuilder {

    private final AbstractTransformer gen;
    
    private long modifiers;
    
    private JCExpression type;
    
    private List<JCAnnotation> typeAnnos;

    private boolean sequenced;

    private boolean defaulted;
    
    private final String name;
    
    private String aliasedName;
    
    private int annotationFlags = Annotations.MODEL_AND_USER;
    
    private boolean built = false;

    private ListBuffer<JCAnnotation> annotations;
    private ListBuffer<JCAnnotation> modelAnnotations;

    private ParameterDefinitionBuilder(AbstractTransformer gen, String name) {
        this.gen = gen;
        this.name = name;
    }
    
    static ParameterDefinitionBuilder instance(AbstractTransformer gen, String name) {
        return new ParameterDefinitionBuilder(gen, name);
    }

    public ParameterDefinitionBuilder modifiers(long mods) {
        this.modifiers |= mods;
        return this;
    }
    
    public ParameterDefinitionBuilder annotations(List<JCAnnotation> annos) {
        if (annos != null) {
            if (this.annotations == null) {
                this.annotations = ListBuffer.lb();
            }
            this.annotations.appendList(annos);
        }
        return this;
    }
    
    public ParameterDefinitionBuilder modelAnnotations(java.util.List<Annotation> annos) {
        if (annos != null) {
            if (this.modelAnnotations == null) {
                this.modelAnnotations = ListBuffer.lb();
            }
            this.modelAnnotations.appendList(gen.makeAtAnnotations(annos));
        }
        return this;
    }
    
    public ParameterDefinitionBuilder type(JCExpression type, List<JCAnnotation> typeAnnos) {
        this.type = type;
        this.typeAnnos = typeAnnos;
        return this;
    }
    
    public ParameterDefinitionBuilder sequenced(boolean sequenced) {
        this.sequenced = sequenced;
        return this;
    }
    
    public ParameterDefinitionBuilder aliasName(String aliasedName) {
        this.aliasedName = aliasedName;
        return this;
    }
    
    /** 
     * Adds the {@code @Ignore} model annotation instead of adding the 
     * actual model annotations.
     */
    public ParameterDefinitionBuilder ignored() {
        this.annotationFlags = Annotations.ignore(this.annotationFlags);
        return this;
    }
    
    public ParameterDefinitionBuilder defaulted(boolean defaulted) {
        this.defaulted = defaulted;
        return this;
    }
    
    /**
     * Prevents adding the user or model annotations. Does not affect 
     * whether {@code @Ignore} is added.
     */
    public ParameterDefinitionBuilder noUserOrModelAnnotations() {
        this.annotationFlags = Annotations.noUserOrModel(annotationFlags);
        return this;
    }
    
    public JCVariableDecl build() {
        if (built) {
            throw new IllegalStateException();
        }
        built = true;
        ListBuffer<JCAnnotation> annots = ListBuffer.lb();
        if (Annotations.includeModel(annotationFlags)) {
            annots.appendList(gen.makeAtName(name));
            if (sequenced) {
                annots.appendList(gen.makeAtSequenced());
            }
            if (defaulted) {
                annots.appendList(gen.makeAtDefaulted());
            }
            if (typeAnnos != null) {
                annots.appendList(typeAnnos);
            }
        }
        if (Annotations.includeUser(annotationFlags)
                && annotations != null) {
            annots.appendList(annotations.toList());
        }
        if (Annotations.includeModel(annotationFlags)) {
            if (modelAnnotations != null) {
                annots.appendList(modelAnnotations.toList());
            }
        }
        
        if(Annotations.includeIgnore(annotationFlags)){
            annots = annots.appendList(gen.makeAtIgnore());
        }
        Name name = gen.names().fromString(aliasedName != null ? aliasedName : this.name);
        return gen.make().VarDef(gen.make().Modifiers(modifiers | Flags.PARAMETER, annots.toList()), 
                name, type, null);   
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Flags.toString(modifiers)).append(' ');
        sb.append(type).append(' ');
        sb.append(aliasedName != null ? aliasedName : name);
        return sb.toString();
    }
    
}
