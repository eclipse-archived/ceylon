doc "The Float representing positive infinity."
shared Float infinity = 1.0/0.0;

doc "A 64-bit floating point number."
shared abstract class Float()
        extends Object()
        satisfies Castable<Float> &
                  Numeric<Float> {

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

doc "Convert a string representation of a float into a Float value."
shared Float? parseFloat(String string) { throw; }

