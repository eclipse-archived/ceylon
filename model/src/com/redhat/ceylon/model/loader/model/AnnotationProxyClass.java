package com.redhat.ceylon.model.loader.model;

import java.util.EnumSet;
import java.util.List;

import com.redhat.ceylon.model.loader.ModelCompleter;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.ParameterList;

/**
 * Used for annotation proxies for interop.
 * 
 * The completer only sets members and parameterlist, so we only load on access of those
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class AnnotationProxyClass extends Class {

    public final LazyInterface iface;
    private ModelCompleter completer;
    private boolean isLoaded2;
    private boolean isLoaded;

    public AnnotationProxyClass(ModelCompleter completer, LazyInterface iface) {
        this.iface = iface;
        this.completer = completer;
    }
    
    /**
     * The elements in the {@code @Target} annotation, or null if 
     * the annotation type lacks the {@code @Target} annotation.
     */
    @Override
    public EnumSet<AnnotationTarget> getAnnotationTarget() {
        return AnnotationTarget.getAnnotationTarget(iface);
    }

    @Override
    public List<Declaration> getMembers() {
        load();
        return super.getMembers();
    }
    
    @Override
    public ParameterList getParameterList() {
        // getParameterLists() depends on this, so we cover both in one override
        load();
        return super.getParameterList();
    }
    
    @Override
    public boolean isSealed() {
        // super.isSealed depends on the parameter list
        load();
        return super.isSealed();
    }

    private void load() {
        if(!isLoaded2){
            synchronized(completer.getLock()){
                if(!isLoaded){
                    isLoaded = true;
                    completer.complete(this);
                    isLoaded2 = true;
                }
            }
        }
    }
}
