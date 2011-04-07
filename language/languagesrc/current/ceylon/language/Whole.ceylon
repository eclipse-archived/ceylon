shared class Whole(Boolean sign, small Natural... digits)
        extends Object()
        satisfies Integral<Whole> & Invertable<Whole> {

    shared small Natural precision;

    doc "Implicit type promotion to |Decimal|"
    shared actual extension Decimal decimal { throw; }

}
