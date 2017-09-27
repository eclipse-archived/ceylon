package org.eclipse.ceylon.model.loader.model;

import org.eclipse.ceylon.model.typechecker.model.Value;

/**
 * Marker class to be able to mark class attributes that are not JavaBean properties
 * but simple fields. Used for Java interoperability only.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class FieldValue extends Value {
    
    private String fieldName;

    public FieldValue(String fieldName){
        this.fieldName = fieldName;
    }

    public String getRealName(){
        return fieldName;
    }
    
    @Override
    protected Class<?> getModelClass() {
        return getClass().getSuperclass(); 
    }

    @Override
    public boolean isJava() {
        return true;
    }
}
