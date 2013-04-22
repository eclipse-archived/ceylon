shared interface ClassOrInterface 
        of Class | Interface 
        satisfies Declaration & Parameterised {
    
    shared formal Boolean typeOf(Anything instance);
    
    shared formal Boolean supertypeOf(ClassOrInterface type);
    
    shared formal Boolean subtypeOf(ClassOrInterface type);
    
    shared formal ClassType? superclass;
    
    shared formal InterfaceType[] interfaces;
    
    shared formal Member<Subtype,Kind>[] members<Subtype,Kind>() 
            given Kind satisfies Declaration;
    
    shared formal Member<Subtype,Kind>[] annotatedMembers<Subtype,Kind,Annotation>() 
            given Kind satisfies Declaration;
    
    shared formal AppliedClassOrInterfaceType<Anything> apply(AppliedProducedType* types);
}