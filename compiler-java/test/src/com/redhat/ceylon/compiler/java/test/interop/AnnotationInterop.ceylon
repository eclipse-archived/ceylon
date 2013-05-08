import javax.annotation{generated, 
    postConstruct, 
    preDestroy, 
    resource,
    Resource {
        AuthenticationType{
            application=\iAPPLICATION
        }
    }
}

@nomodel
generated{
    \ivalue={"Some", "code generator name"};
}
class AnnotationInterop() {
    
    resource{
        name="tom";
        authenticationType=application;
        type=AnnotationInterop;
    }
    shared variable String tom = "";
    
    postConstruct
    shared void afterConstrution() {}
    
    preDestroy
    shared void beforeDestruction() {}
}