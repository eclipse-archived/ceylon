shared interface Package {
    
    shared formal String name;
    
    shared formal Module container;
    
    shared formal Declaration[] members<Kind>() 
            given Kind satisfies Declaration;
    shared formal Declaration[] annotatedMembers<Kind,Annotation>() 
            given Kind satisfies Declaration;
}