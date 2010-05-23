public class Float(Float float)
        satisfies Numeric<Float,Float> { 
    
    doc "Implicit type promotion to |Decimal|"
    override public extension Decimal decimal { throw }
    
    public extension class StringToFloat(String string) {
    
        doc "Parse the string representation of a |Float| in the given radix"
        public Float parseFloat(small Natural radix=10) { throw }
        
    }

}