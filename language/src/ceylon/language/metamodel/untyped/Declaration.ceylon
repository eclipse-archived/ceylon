shared interface Declaration of Value |
                                Function |
                                ClassOrInterface {
    
    shared formal String name;
    
    shared formal Annotation[] annotations<Annotation>()
            given Annotation satisfies Object;
    
    // FIXME: that name sucks
    shared formal Package packageContainer;
    
    shared formal Boolean toplevel;
}