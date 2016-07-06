package com.redhat.ceylon.langtools.tools.javac.processing.wrappers;

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

    private javax.annotation.processing.Processor d;

    public ProcessorWrapper(javax.annotation.processing.Processor d) {
        this.d = d;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return d.getSupportedOptions();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return d.getSupportedAnnotationTypes();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
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

}
