shared abstract class Natural()
        extends Object()
        satisfies Castable<Natural|Integer|Float> &
                  Integral<Natural> &
                  Invertable<Integer> {}