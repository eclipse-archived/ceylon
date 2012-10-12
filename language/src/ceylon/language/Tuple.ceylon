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
        extends Object()
        satisfies AbstractTuple
        given Y satisfies AbstractTuple {
    shared X first;
    shared Y rest;
    shared actual String string { 
        return "(" first?.string else "null" ", " rest.string[1...] "";
    }
    shared actual Boolean equals(Object that) {
        if (is Tuple<Void,AbstractTuple> that) {
            return first==that.first && rest==that.rest;
        }
        else {
            return false;
        }
    }
    shared actual Integer hash = (31 + first.hash) * 31 + rest.hash;
}