shared class Whole(Boolean sign, small Natural... digits)
        extends Object()
        satisfies Integral<Whole> & Invertable<Whole> {
    //TODO finish

    shared small Natural precision;

    doc "Implicit type promotion to |Decimal|"
    shared actual extension Decimal decimal { throw; }

    shared extension class StringToWhole(String this) {

        doc "Parse the string representation of a |Whole| in the given radix"
        shared Whole parseWhole(small Natural radix=10) { throw; }

    }

}
