package org.eclipse.ceylon.langtools.tools.javac.processing.wrappers;

import javax.lang.model.element.Name;

public class NameFacade implements Name {

    private org.eclipse.ceylon.javax.lang.model.element.Name f;

    public NameFacade(org.eclipse.ceylon.javax.lang.model.element.Name f) {
        this.f = f;
    }

    @Override
    public char charAt(int arg0) {
        return f.charAt(arg0);
    }

    @Override
    public int length() {
        return f.length();
    }

    @Override
    public CharSequence subSequence(int arg0, int arg1) {
        return f.subSequence(arg0, arg1);
    }

    @Override
    public boolean contentEquals(CharSequence arg0) {
        return f.contentEquals(arg0);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NameFacade == false)
            return false;
        return f.equals(((NameFacade)obj).f);
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
