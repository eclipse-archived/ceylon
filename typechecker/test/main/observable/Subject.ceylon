interface Subject<S,O> of S
        given O satisfies Observer<S,O> 
        given S satisfies Subject<S,O> {
    
    shared formal List<O> observers;
    
    shared void register(O o) { 
        observers.add(o);
    }

    shared void notify() { 
        for (o in observers) { 
            o.update(this); 
        }
    }
    
}
