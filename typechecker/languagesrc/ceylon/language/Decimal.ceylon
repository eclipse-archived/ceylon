shared class Decimal(Whole value, small Integer scale)
        extends Object()
        satisfies Numeric<Decimal> & Invertable<Decimal> {
    //TODO finish

    shared small Natural precision;
    shared small Integer scale;

    shared extension class StringToDecimal(String this) {

        doc "Parse the string representation of a |Decimal| in the given radix"
        shared Decimal parseDecimal(small Natural radix=10) { throw; }

    }

}