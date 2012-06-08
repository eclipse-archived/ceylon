doc "An instance of `Float` representing 
     positive infinity \{221E}."
shared Float infinity = 1.0/0.0;

doc "A 64-bit floating point number."
shared abstract class Float()
        extends Object()
        satisfies Scalar<Float> & Exponentiable<Float,Float> & 
                  Castable<Float> {

    doc "Determines whether this value is undefined (Not a Number)."
    shared Boolean undefined {
        return this != this;
    }

    doc "Determines whether this value is infinite in magnitude 
         (that is, `infinity` or `-infinity`)."
    see(infinity, finite)
    shared Boolean infinite {
        return this == infinity || this == -infinity;
    }
    
    doc "Determines whether this value is finite (that is, neither `infinity`,
         `-infinity` nor `undefined`)."
    see(infinite, infinity)
    shared Boolean finite {
        return this != infinity && this != -infinity && !this.undefined;
    }

}

doc "The `Float` value of the given string representation of an decimal number
     or `null` if the string does not represent an decimal number.
     
     The syntax accepted by this method is the same as the syntax for a 
     `Float` literal in the Ceylon language except that it may optionally 
     begin with a sign character (`+` or `-`)."
shared Float? parseFloat(String string) { throw; }

