interface InterfaceWithConcreteMethods<T> {
    //void nonShared<U>(Integer i, Integer j) {}
    
    void nonShared<U>(Integer i = 0, Integer j = i) {}
    shared void shared(Integer i = 0, Integer j = i) {}
    shared default void default(Integer i = 0, Integer j = i) {}
    shared formal void formal(Integer i = 0, Integer j = i);
    
    
    Integer nonSharedAttribute {
        return 1;
    }
    
    shared Integer sharedAttribute => 1;
    /*shared Integer defaultAttribute => 1;
    assign defaultAttribute {
        this.formalAttribute = defaultAttribute;
    }*/
    shared formal variable Integer formalAttribute;
}
