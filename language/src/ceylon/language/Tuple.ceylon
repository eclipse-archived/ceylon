shared interface TupleOrUnit of Unit|Tuple<Void,TupleOrUnit> {}

shared interface Unit of unit satisfies TupleOrUnit {}

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

shared class Tuple<out First, out Rest> (first, rest)
        extends Object()
        satisfies TupleOrUnit
        given Rest satisfies TupleOrUnit {
        
    shared First first;
    shared Rest rest;
    
    shared actual String string { 
        return "(" first?.string else "null" ", " rest.string[1...] "";
    }
    
    shared actual Boolean equals(Object that) {
        if (is Tuple<Void,TupleOrUnit> that) {
            return first==that.first && rest==that.rest;
        }
        else {
            return false;
        }
    }
    
    shared actual Integer hash = (31 + first.hash) * 31 + rest.hash;
    
}