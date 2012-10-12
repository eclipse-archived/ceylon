shared interface AbstractTuple of Unit|Tuple<Void,AbstractTuple> {}

shared interface Unit of unit satisfies AbstractTuple {}

shared object unit extends Object() satisfies Unit {
    shared actual String string { 
        return "()"; 
    }
    shared actual Boolean equals(Object that) {
        return that is Unit;
    }
    shared actual Integer hash { 
        return 0; 
    }
}

shared class Tuple<out X, out Y> (first, rest)
        satisfies AbstractTuple
        given Y satisfies AbstractTuple {
    shared X first;
    shared Y rest;
    shared actual String string { 
        return "(" first?.string else "null" ", " rest.string[1...] "";
    }
}