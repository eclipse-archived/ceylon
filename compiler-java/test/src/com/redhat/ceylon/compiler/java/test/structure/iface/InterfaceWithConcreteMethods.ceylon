interface InterfaceWithConcreteMethods<T> {
    //void nonShared<U>(Integer i, Integer j) {}
    
    void nonShared<U>(Integer i = 0, Integer j = i) {}
    shared void shared(Integer i = 0, Integer j = i) {}
    shared default void default(Integer i = 0, Integer j = i) {}
    shared formal void formal(Integer i = 0, Integer j = i);
}
