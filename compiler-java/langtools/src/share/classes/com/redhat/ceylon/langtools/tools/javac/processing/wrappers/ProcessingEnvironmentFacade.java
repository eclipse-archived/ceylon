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

    public ProcessingEnvironmentFacade(com.redhat.ceylon.javax.annotation.processing.ProcessingEnvironment f) {
        this.f = f;
    }

    @Override
    public Elements getElementUtils() {
        return Facades.facade(f.getElementUtils());
    }

    @Override
    public Filer getFiler() {
        return Facades.facade(f.getFiler());
    }

    @Override
    public Locale getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Messager getMessager() {
        return Facades.facade(f.getMessager());
    }

    @Override
    public Map<String, String> getOptions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SourceVersion getSourceVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Types getTypeUtils() {
        // TODO Auto-generated method stub
        return null;
    }

}
