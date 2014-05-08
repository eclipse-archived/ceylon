"A type capable of representing the values [[true]] and 
 [[false]] of Boolean logic."
by ("Gavin")
shared abstract class Boolean()
        of true | false 
        satisfies Binary<Boolean> {
    
    shared actual Boolean not 
            => this then false else true;
    
    shared actual Boolean and(Boolean other) 
            => this && other;
    
    shared actual Boolean or(Boolean other) 
            => this||other;
    
    shared actual Boolean xor(Boolean other) 
            => this && !other || !this && other;
    
    shared actual Boolean clear(Integer index) 
            => index == 0 then false else this;
    
    shared actual Boolean flip(Integer index)
            => index == 0 then !this else this;
    
    shared actual Boolean get(Integer index)
            => index == 0 then this else false;
    
    shared actual Boolean set(Integer index, Boolean bit)
            => index == 0 then bit else this;
    
    shared actual Boolean leftLogicalShift(Integer shift) 
            => this;
    
    shared actual Boolean rightArithmeticShift(Integer shift) 
            => this;
    
    shared actual Boolean rightLogicalShift(Integer shift) 
            => this;
    
}

"A value representing truth in Boolean logic."
shared native object true extends Boolean() {
    string => "true";
}

"A value representing falsity in Boolean logic."
shared native object false extends Boolean() {
    string => "false";
}
