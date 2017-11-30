interface Observer<S,O> of O 
        given O satisfies Observer<S,O>
        given S satisfies Subject<S,O> {
    
    shared formal void update(S s);
    
}
