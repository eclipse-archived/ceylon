shared class Natural(Natural n)
        extends Object()
        satisfies Castable<Natural|Integer|Float> &
                  Integral<Natural> &
                  Invertable<Integer> {}