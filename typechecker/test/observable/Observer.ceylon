interface Observer<S,O> 
        given this is O satisfies Observer<S,O>
        given S satisfies Subject<S,O> {
    
    shared formal void update(S s);
    
}
