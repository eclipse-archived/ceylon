"""Abstraction of types which support a binary addition
   operator `x + y`. For [[numeric types|Numeric]], this is
   just familiar numeric addition. For [[String]], it is
   string concatenation.
       
       Integer next = current + 1;
       String helloWorld = "hello" + " " + "world";
   
   A concrete class that implements this interface should be 
   a mathematical _semigroup_. That is, the addition 
   operation should be associative, satisfying:
   
   - `(x+y)+z == x+(y+z)`"""
see (`class String`, `interface Numeric`)
by ("Gavin")
shared interface Summable<Other> of Other
        given Other satisfies Summable<Other> {

    "The result of adding the given value to this value. 
     This operation should never perform any kind of 
     mutation upon either the receiving value or the 
     argument value."
    shared formal Other plus(Other other);
    
}