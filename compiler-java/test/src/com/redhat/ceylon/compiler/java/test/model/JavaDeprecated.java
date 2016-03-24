package com.redhat.ceylon.compiler.java.test.model;

import ceylon.language.DeprecationAnnotation$annotation$;

@Deprecated
public class JavaDeprecated {

    @Deprecated
    public String s;
    
    @Deprecated
    public void m(@Deprecated String p) {}
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({@com.redhat.ceylon.compiler.java.metadata.Annotation(
            value = "deprecated",
            arguments = {"Foo"})})
    @DeprecationAnnotation$annotation$(description="Foo")
    public void ceylonDeprecation() {}
    
    @com.redhat.ceylon.compiler.java.metadata.Annotations({@com.redhat.ceylon.compiler.java.metadata.Annotation(
            value = "deprecated",
            arguments = {"Foo"})})
    @DeprecationAnnotation$annotation$(description="Foo")
    @Deprecated
    public void bothDeprecation() {}
}
