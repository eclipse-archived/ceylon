package com.redhat.ceylon.model.loader.model;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.model.loader.mirror.MethodMirror;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;

/**
 * Instance method that allows us to remember the exact method name
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class JavaMethod extends Function implements LocalDeclarationContainer {

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
