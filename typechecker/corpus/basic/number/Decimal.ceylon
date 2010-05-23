public class Decimal(Whole value, small Integer scale)
        satisfies Numeric<Decimal,Decimal> {

    public small Natural precision { throw }
    public small Integer scale { throw }
    
    public extension class StringToDecimal(String string) {
    
        doc "Parse the string representation of a |Decimal| in the given radix"
        public Decimal parseDecimal(small Natural radix=10) { return ... }
        
    }

}