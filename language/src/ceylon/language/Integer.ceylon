doc "A 64-bit integer."
shared abstract class Integer()
        extends Object()
        satisfies Castable<Integer|Float> &
                  Integral<Integer> &
                  Numeric<Integer> {

}