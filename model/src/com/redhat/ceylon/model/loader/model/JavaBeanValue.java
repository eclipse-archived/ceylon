package com.redhat.ceylon.model.loader.model;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.model.loader.mirror.MethodMirror;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Value;

/**
 * Normal value which allows us to remember if it's a "get" or "is" type of getter for interop.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class JavaBeanValue extends Value implements LocalDeclarationContainer {
    private String getterName;
    private String setterName;
    
    private Map<String,Declaration> localDeclarations;
    public final MethodMirror mirror;

    public JavaBeanValue(MethodMirror mirror) {
        this.mirror = mirror;
    }

    @Override
    protected Class<?> getModelClass() {
        return getClass().getSuperclass(); 
    }

    public void setGetterName(String getterName) {
        this.getterName = getterName;
    }

    public String getSetterName() {
        return setterName;
    }

    public void setSetterName(String setterName) {
        this.setterName = setterName;
    }

    public String getGetterName() {
        return getterName;
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
