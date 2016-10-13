package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import javax.lang.model.element.Name;

import com.redhat.ceylon.javax.lang.model.element.Element;
import com.redhat.ceylon.javax.lang.model.element.PackageElement;

public class PackageElementFacade extends ElementFacade implements javax.lang.model.element.PackageElement {

    public PackageElementFacade(Element f) {
        super(f);
    }

    @Override
    public Name getQualifiedName() {
        return Facades.facade(((PackageElement)f).getQualifiedName());
    }

    @Override
    public boolean isUnnamed() {
        return ((PackageElement)f).isUnnamed();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PackageElementFacade == false)
            return false;
        return f.equals(((PackageElementFacade)obj).f);
    }
    
    @Override
    public int hashCode() {
        return f.hashCode();
    }
    
    @Override
    public String toString() {
        return f.toString();
    }

}
