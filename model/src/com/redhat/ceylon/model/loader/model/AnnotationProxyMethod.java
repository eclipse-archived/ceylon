package com.redhat.ceylon.model.loader.model;

import java.util.List;

import com.redhat.ceylon.model.loader.ModelCompleter;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.ParameterList;

/**
 * Used for annotation interop.
 * 
 * The completer only sets members, parameterLists and annotationConstructor, so load for them.
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public class AnnotationProxyMethod extends Function {
    
    public final AnnotationProxyClass proxyClass;
    
    private OutputElement annotationTarget;

    private ModelCompleter completer;

    private boolean isLoaded2;

    private boolean isLoaded;
    
    public AnnotationProxyMethod(ModelCompleter completer, AnnotationProxyClass proxyClass) {
        this.completer = completer;
        this.proxyClass = proxyClass;
    }

    public AnnotationProxyClass getProxyClass() {
        return proxyClass;
    }
    
    public void setAnnotationTarget(OutputElement annotationTarget) {
        this.annotationTarget = annotationTarget;
    }
    
    /**
     * If this is a disambiguating proxy annotation method, then this is the 
     * Java program element that the constructor targets. Otherwise null
     */
    public OutputElement getAnnotationTarget() {
        return this.annotationTarget;
    }

    @Override
    public Object getAnnotationConstructor() {
        load();
        return super.getAnnotationConstructor();
    }
    
    @Override
    public List<ParameterList> getParameterLists() {
        load();
        return super.getParameterLists();
    }

    @Override
    public List<Declaration> getMembers() {
        load();
        return super.getMembers();
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
