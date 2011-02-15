shared class Natural(Natural natural)
        extends Object()
        satisfies Integral<Natural> & Invertable<Integer> & Matcher<Integer> {
    //TODO finish

    doc "Implicit type promotion to |Integer|"
    shared actual extension Integer integer { return ... }

    doc "Implicit type promotion to |Whole|"
    shared actual extension Whole whole { return ... }

    doc "Implicit type promotion to |Float|"
    shared actual extension Float float { return ... }

    doc "Implicit type promotion to |Decimal|"
    shared actual extension Decimal decimal { return ... }

    doc "Shift bits left by the given number of places"
    shared Natural leftShift(Natural digits) { return ... }

    doc "Shift bits right by the given number of places"
    shared Natural rightShift(Natural digits) { return ... }

    shared extension class StringToNatural(String this) {

        doc "Parse the string representation of a |Natural| in the given radix"
        shared Natural parseNatural(small Natural radix=10) { return ... }

    }

}