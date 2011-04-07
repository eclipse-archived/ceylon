shared class Float(Float float)
        extends Object()
        satisfies Numeric<Float> & Invertable<Float> {

    doc "Implicit type promotion to |Decimal|"
    shared actual extension Decimal decimal { throw; }

}