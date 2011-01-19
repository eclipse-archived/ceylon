class Objects() {
    
    object simple {}
    
    object withMethods {
        void doNothing() {}
        Natural returnZero() { return 0; }
        String returnArgument(String arg) { return arg; }
    }
    
    object withAttributes {
        Natural count = 0;
        variable String description := "";
        String countAsString { return $count; }
        assign countAsString { count := countAsString.parseNatural; }
    }
    
    class Superclass() {}
    interface Superinterface<T> {}
    
    object withSupertypes extends Superclass() satisfies Superinterface<Natural> {}
    
}
