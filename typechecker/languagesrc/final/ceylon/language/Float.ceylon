shared class Float(Float float)
        extends Object()
        satisfies Numeric<Float> & Invertable<Float> {
    //TODO finish

    doc "The natural logarithm of the number"
    shared Float ln { throw; }

    doc "Implicit type promotion to |Decimal|"
    shared actual extension Decimal decimal { throw; }

    shared extension class StringToFloat(String this) {

        doc "Parse the string representation of a |Float| in the given radix"
        shared Float parseFloat(small Natural radix=10) { throw; }

    }

}