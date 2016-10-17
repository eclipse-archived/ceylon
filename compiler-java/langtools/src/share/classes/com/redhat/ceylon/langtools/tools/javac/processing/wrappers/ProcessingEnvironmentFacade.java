package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.Locale;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class ProcessingEnvironmentFacade implements ProcessingEnvironment {

    private com.redhat.ceylon.javax.annotation.processing.ProcessingEnvironment f;
    private Filer filer;

    public ProcessingEnvironmentFacade(com.redhat.ceylon.javax.annotation.processing.ProcessingEnvironment f) {
        this.f = f;
        this.filer = Facades.facade(f.getFiler());
    }

    @Override
    public Elements getElementUtils() {
        return Facades.facade(f.getElementUtils());
    }

    @Override
    public Filer getFiler() {
        return filer;
    }

    @Override
    public Locale getLocale() {
        return f.getLocale();
    }

    @Override
    public Messager getMessager() {
        return Facades.facade(f.getMessager());
    }

    @Override
    public Map<String, String> getOptions() {
        return f.getOptions();
    }

    @Override
    public SourceVersion getSourceVersion() {
        return Facades.facade(f.getSourceVersion());
    }

    @Override
    public Types getTypeUtils() {
        return Facades.facade(f.getTypeUtils());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ProcessingEnvironmentFacade == false)
            return false;
        return f.equals(((ProcessingEnvironmentFacade)obj).f);
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
