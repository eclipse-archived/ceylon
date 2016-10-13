package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.redhat.ceylon.javax.annotation.processing.Completion;
import com.redhat.ceylon.javax.annotation.processing.ProcessingEnvironment;
import com.redhat.ceylon.javax.annotation.processing.Processor;
import com.redhat.ceylon.javax.annotation.processing.RoundEnvironment;
import com.redhat.ceylon.javax.lang.model.SourceVersion;
import com.redhat.ceylon.javax.lang.model.element.AnnotationMirror;
import com.redhat.ceylon.javax.lang.model.element.Element;
import com.redhat.ceylon.javax.lang.model.element.ExecutableElement;
import com.redhat.ceylon.javax.lang.model.element.TypeElement;

public class ProcessorWrapper implements Processor {

    javax.annotation.processing.Processor d;

    public ProcessorWrapper(javax.annotation.processing.Processor d) {
        this.d = d;
    }

    @Override
    public Set<String> getSupportedOptions() {
        // FIXME: probably check that the method is not overridden from AbstractProcessor
        javax.annotation.processing.SupportedOptions so2 = 
                d.getClass().getAnnotation(javax.annotation.processing.SupportedOptions.class);
        if(so2 != null)
            return arrayToSet(so2.value());
        return d.getSupportedOptions();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        // FIXME: probably check that the method is not overridden from AbstractProcessor
        javax.annotation.processing.SupportedAnnotationTypes sat2 = 
                d.getClass().getAnnotation(javax.annotation.processing.SupportedAnnotationTypes.class);
        if(sat2 != null)
            return arrayToSet(sat2.value());
        return d.getSupportedAnnotationTypes();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        // FIXME: probably check that the method is not overridden from AbstractProcessor
        javax.annotation.processing.SupportedSourceVersion ssv2 = 
                d.getClass().getAnnotation(javax.annotation.processing.SupportedSourceVersion.class);
        if(ssv2 != null)
            return Wrappers.wrap(ssv2.value());
        return Wrappers.wrap(d.getSupportedSourceVersion());
    }

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        d.init(Facades.facade(processingEnv));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return d.process(Facades.facadeTypeElements(annotations), Facades.facade(roundEnv));
    }


    @Override
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
        // TODO Auto-generated method stub
        return null;
    }

    private static Set<String> arrayToSet(String[] array) {
        assert array != null;
        Set<String> set = new HashSet<String>(array.length);
        for (String s : array)
            set.add(s);
        return Collections.unmodifiableSet(set);
    }
}
