"The abstract supertype of all types. A value of type 
 `Anything` may be a definite value of type [[Object]], or 
 it may be the [[null]] value. A method declared `void` is 
 considered to have the return type `Anything`.
 
 Note that the bottom type `Nothing`, representing the 
 intersection of all types, is a subtype of all types."
by ("Gavin")
tagged("Basic types")
shared abstract class Anything() 
        of Object | Null {}