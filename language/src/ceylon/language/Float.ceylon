doc "A 64-bit floating point number."
shared abstract class Float()
        extends Object()
        satisfies Castable<Float> &
                  Numeric<Float> {

}

shared Float? parseFloat(String string) { throw; }