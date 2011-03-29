shared class Integer(Boolean sign, Natural natural)
        extends Object()
        satisfies Integral<Integer> & Invertable<Integer> & Matcher<Integer> {
    //TODO finish

    doc "Implicit type promotion to |Whole|"
    shared actual extension Whole whole { throw; }

    doc "Implicit type promotion to |Float|"
    shared actual extension Float float { throw; }

    doc "Implicit type promotion to |Decimal|"
    shared actual extension Decimal decimal { throw; }

    shared extension class StringToInteger(String this) {

        doc "Parse the string representation of an |Integer| in the given radix"
        shared Integer parseInteger(small Natural radix=10) { throw; }

    }

}