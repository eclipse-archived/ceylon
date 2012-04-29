doc "A 64-bit integer."
shared abstract class Integer()
        extends Object()
        satisfies Castable<Integer|Float> &
                  Integral<Integer> &
                  Exponentiable<Integer,Integer> {
    shared formal Character character;
}

doc "The `Integer` value of the given string representation of an 
     integer, or `null` if the string does not represent an integer 
     or if the mathematical integer it represents is too large in magnitude 
     to be represented by an `Integer`.
     
     The syntax accepted by this method is the same as the syntax for an 
     `Integer` literal in the Ceylon language except that it may optionally 
     begin with a sign character (`+` or `-`)."
shared Integer? parseInteger(String string) { throw; }