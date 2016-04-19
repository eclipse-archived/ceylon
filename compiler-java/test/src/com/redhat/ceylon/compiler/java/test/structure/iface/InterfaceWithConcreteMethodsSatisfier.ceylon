class InterfaceWithConcreteMethodsSatisfier() satisfies InterfaceWithConcreteMethods<Integer> {
    //shared actual variable Integer formalAttribute = 1;
    
    void nonShared<U>(Integer i, Integer j) {}
    shared actual void default(Integer i, Integer j) {
        // TODO Don't need to implement overloads for default parameters
        // XXX OR do I -- what about $canonical$???
        nonShared<String>(0,0);
        shared();
        super.shared();
        super.default();
        (super of InterfaceWithConcreteMethods<Integer>).shared();
        (super of InterfaceWithConcreteMethods<Integer>).default();
    }
    shared actual void formal(Integer i, Integer j) {
        
    }
}