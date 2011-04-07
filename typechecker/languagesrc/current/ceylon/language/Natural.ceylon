shared class Natural(Natural natural)
        extends Object()
        satisfies Integral<Natural> & Invertable<Integer> & Matcher<Integer> {

    doc "Implicit type promotion to |Integer|"
    shared actual extension Integer integer { throw; }

    doc "Implicit type promotion to |Whole|"
    shared actual extension Whole whole { throw; }

    doc "Implicit type promotion to |Float|"
    shared actual extension Float float { throw; }

    doc "Implicit type promotion to |Decimal|"
    shared actual extension Decimal decimal { throw; }

    doc "Shift bits left by the given number of places"
    shared Natural leftShift(Natural digits) { throw; }

    doc "Shift bits right by the given number of places"
    shared Natural rightShift(Natural digits) { throw; }

}