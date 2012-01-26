doc "A 64-bit floating point number."
shared abstract class Float()
        extends Object()
        satisfies Castable<Float> &
                  Numeric<Float> {

}

doc "Convert a string representation of a float into a Float value."
shared Float? parseFloat(String string) { throw; }

doc "The Float representing positive infinity."
shared Float positiveInfinity = 1.0/0.0;

doc "The Float representing negative infinity."
shared Float negativeInfinity = -1.0/0.0;

doc "Determines whether the given float is undefined (Not a Number)."
shared Boolean undefined(Float value) {
    return value != value;
}