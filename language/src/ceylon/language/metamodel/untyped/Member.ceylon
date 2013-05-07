shared interface Member<Kind> 
        satisfies Kind()
        given Kind satisfies Declaration {
    
    shared formal ClassOrInterface declaringClassOrInterface;
}