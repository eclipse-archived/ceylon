// simplest test case

@nomodel
class Bug667TopClass() {
    default shared String? get { throw; }
}

@nomodel
class Bug667BottomClass(Nothing n) extends Bug667TopClass() {    
    shared actual Nothing get { return n; }
    shared String? perhaps(){
        return n;
    }
}

// original test case

@nomodel
interface Bug667Interface<T> {
    default shared T? get { throw; }
}
 
@nomodel
class Bug667Class<Null>(Null n) satisfies Bug667Interface<String> 
        given Null satisfies Nothing {    
    shared actual Null get { return n; }
}

@nomodel
void bug667Method<Null>(Null n) given Null satisfies Nothing {
    object x satisfies Bug667Interface<String> {    
        shared actual Null get { return n; }
    }
}
