@nomodel
interface Bug667Interface<T> {
    default shared T? get { throw; }
}

class Bug667Class<Null>(Null n) satisfies Bug667Interface<String> 
        given Null satisfies Nothing {    
    shared actual Null get { return n; }
}

void bug667Method<Null>(Null n) given Null satisfies Nothing {
    object x satisfies Bug667Interface<String> {    
        shared actual Null get { return n; }
    }
}
