// import java.util.*;

class Generics () {
    Integer? x() { return null; }
    Float? xx() { return null; }
}

   class TypeWithParameter<X>(X init) {
        mutable X x := init;
        X process(X input) { return input }
    }
    
