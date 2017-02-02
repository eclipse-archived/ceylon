package com.redhat.ceylon.model.loader.model;

import static com.redhat.ceylon.model.typechecker.model.DeclarationFlags.ACTUAL;
import static com.redhat.ceylon.model.typechecker.model.DeclarationFlags.ValueFlags.VARIABLE;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.model.loader.mirror.MethodMirror;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Scope;
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

    @Override
    public boolean isJava() {
        Scope container = getContainer();
        while(container != null && container instanceof Declaration == false)
            container = container.getContainer();
        return container != null ? ((Declaration) container).isJava() : false;
    }
    
    @Override
    public boolean isVariable() {
        if (actualCompleter != null) {
            completeActual();
        }
        return (flags&VARIABLE)!=0;
    }
}
