shared interface Declaration of Value<Anything> |
                                Function |
                                ClassOrInterface |
                                TypeParameter {
    
    shared formal String name;
    
    shared formal Annotation[] annotations<Annotation>()
            given Annotation satisfies Object;
    
    shared formal Package packageContainer;
    
    shared formal Boolean toplevel;
}