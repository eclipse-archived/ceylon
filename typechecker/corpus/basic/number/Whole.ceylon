public class Whole(Boolean sign, small Natural... digits)
        satisfies Integral<Whole,Whole> {
    
    public small Natural precision { throw }
    
    doc "Implicit type promotion to |Decimal|"
    override public extension Decimal decimal { throw }
    
    public extension class StringToWhole(String string) {
    
        doc "Parse the string representation of a |Whole| in the given radix"
        public Whole parseWhole(small Natural radix=10) { throw }
        
    }

}