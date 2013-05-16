shared interface Module satisfies Identifiable {
    shared formal String name;
    shared formal String version;
    
    shared formal Package[] members;
}

shared interface Package satisfies Identifiable {
    
    shared formal String name;
    
    shared formal Module container;
    
    shared formal Kind[] members<Kind>() 
            given Kind satisfies Declaration;
    
    shared formal Kind[] annotatedMembers<Kind, Annotation>() 
            given Kind satisfies Declaration;

    shared formal Value? getAttribute(String name);
    shared formal Function? getFunction(String name);
}