"""The abstract supertype of all types representing 
   definite values. Any two `Object`s may be compared
   for value equality using the `==` and `!=` operators:
   
       true==false
       1=="hello world"
       "hello"+" "+"world"=="hello world"
       Singleton("hello world")=={ "hello world" }
   
   However, since `Null` is not a subtype of `Object`, the
   value `null` cannot be compared to any other value
   using `==`. Thus, value equality is not defined for
   optional types. This neatly voids the problem of
   deciding the value of the expression `null==null`,
   which is simply illegal."""
see (`class Basic`, `class Null`)
by ("Gavin")
shared abstract class Object() 
        extends Anything() {
    
    "Determine if two values are equal. Implementations
     should respect the constraints that:
     
     - if `x===y` then `x==y` (reflexivity), 
     - if `x==y` then `y==x` (symmetry), 
     - if `x==y` and `y==z` then `x==z` (transitivity).
     
     Furthermore it is recommended that implementations
     ensure that if `x==y` then `x` and `y` have the
     same concrete class."
    shared formal Boolean equals(Object that);
    
    "The hash value of the value, which allows the value
     to be an element of a hash-based set or key of a
     hash-based map. Implementations must respect the
     constraint that if `x==y` then `x.hash==y.hash`."
    shared formal Integer hash;
    
    "A developer-friendly string representing the 
     instance. Concatenates the name of the concrete 
     class of the instance with the `hash` of the 
     instance. Subclasses are encouraged to refine this 
     implementation to produce a more meaningful 
     representation."
    shared default String string =>
            className(this) + "@" + hash.string;
    
}