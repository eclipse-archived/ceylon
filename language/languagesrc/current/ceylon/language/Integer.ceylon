shared class Integer(Boolean sign, Natural natural)
        extends Object()
        satisfies Integral<Integer> & Invertable<Integer> & Matcher<Integer> {

    doc "Implicit type promotion to |Float|"
    shared actual extension Float float { throw; }

}