package com.redhat.ceylon.compiler.typechecker.model;

import java.util.List;

import com.redhat.ceylon.compiler.typechecker.model.Declaration;

/**
 * <p>Represents the instantiation of an annotation class in an annotation 
 * constructor. This is used so that annotation constructors can be 
 * 'inlined'.</p>
 * 
 * For example:<p>
 * <pre>
 *     shared annotation Doc doc(String description) => Doc(description);
 * </pre>
 */
public class AnnotationInstantiation {

    private Declaration primary;
    private List<AnnotationArgument> arguments;
    
    /** The primary of the instantiation */
    public Declaration getPrimary() {
        return primary;
    }

    public void setPrimary(Declaration primary) {
        this.primary = primary;
    }

    /** The arguments of the instantiation */
    public List<AnnotationArgument> getArguments() {
        return arguments;
    }

    public void setArguments(List<AnnotationArgument> arguments) {
        this.arguments = arguments;
    }
    
}
