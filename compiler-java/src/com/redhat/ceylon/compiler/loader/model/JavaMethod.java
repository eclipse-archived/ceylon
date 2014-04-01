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
package com.redhat.ceylon.compiler.loader.model;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.loader.mirror.MethodMirror;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;

/**
 * Instance method that allows us to remember the exact method name
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class JavaMethod extends Method implements LocalDeclarationContainer {

    private String realName;
    private boolean defaultedAnnotation;
    public final MethodMirror mirror;
    private Map<String,Declaration> localDeclarations;
    
    @Override
    protected Class<?> getModelClass() {
        return getClass().getSuperclass(); 
    }
    
    public JavaMethod(MethodMirror mirror){
        this.mirror = mirror;
    }
    
    public void setRealName(String name) {
        this.realName = name;
    }

    public String getRealName(){
        return realName;
    }
    
    /**
     * If this is a method on an annotation type, whether the method has a 
     * {@code default} expression;
     */
    public boolean isDefaultedAnnotation() {
        return defaultedAnnotation;
    }
    
    public void setDefaultedAnnotation(boolean defaultedAnnotation) {
        this.defaultedAnnotation = defaultedAnnotation;
    }

    @Override
    public Declaration getLocalDeclaration(String name) {
        if(localDeclarations == null)
            return null;
        return localDeclarations.get(name);
    }

    @Override
    public void addLocalDeclaration(Declaration declaration) {
        if(localDeclarations == null)
            localDeclarations = new HashMap<String, Declaration>();
        localDeclarations.put(declaration.getPrefixedName(), declaration);
    }
}
