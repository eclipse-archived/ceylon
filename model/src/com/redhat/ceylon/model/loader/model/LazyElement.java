package com.redhat.ceylon.model.loader.model;


/**
 * Represents a lazy declaration.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface LazyElement {

    public boolean isLoaded();
    
    public boolean isLocal();
    
    public void setLocal(boolean local);
}
