doc "The abstract supertype of all types. A value of type 
     `Void` may be a definite value of type `Object`, or it 
     may be the `null` value. A method declared `void` is 
     considered to have the return type `Void`.
     
     Note that the type `Bottom`, representing the 
     intersection of all types, is a subtype of all types."
by "Gavin"
shared abstract class Void() 
        of Object | Nothing /*extends null*/ {}