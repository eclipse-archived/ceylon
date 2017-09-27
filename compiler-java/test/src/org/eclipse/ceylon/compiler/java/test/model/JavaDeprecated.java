package org.eclipse.ceylon.compiler.java.test.model;

import ceylon.language.DeprecationAnnotation$annotation$;

@Deprecated
public class JavaDeprecated {

    @Deprecated
    public String s;
    
    @Deprecated
    public void m(@Deprecated String p) {}
    
    @org.eclipse.ceylon.compiler.java.metadata.Annotations({@org.eclipse.ceylon.compiler.java.metadata.Annotation(
            value = "deprecated",
            arguments = {"Foo"})})
    @DeprecationAnnotation$annotation$(description="Foo")
    public void ceylonDeprecation() {}
    
    @org.eclipse.ceylon.compiler.java.metadata.Annotations({@org.eclipse.ceylon.compiler.java.metadata.Annotation(
            value = "deprecated",
            arguments = {"Foo"})})
    @DeprecationAnnotation$annotation$(description="Foo")
    @Deprecated
    public void bothDeprecation() {}
}
