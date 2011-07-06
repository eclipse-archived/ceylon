interface Observer<S,O> 
        given O satisfies Observer<S,O> 
                abstracts Observer<S,O>
        given S satisfies Subject<S,O> {
    
    shared formal void update(S s);
    
}
