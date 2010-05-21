public final class Integer(Boolean sign, Natural natural)
        satisfies Integral<Integer,Integer>, Case<Integral> {
    
    doc "Implicit type promotion to |Whole|"
    override public extension Whole whole { throw }
    
    doc "Implicit type promotion to |Float|"
    override public extension Float float { throw }

    doc "Implicit type promotion to |Decimal|"
    override public extension Decimal decimal { throw }

    public extension class StringToInteger(String string) {
    
        doc "Parse the string representation of an |Integer| in the given radix"
        public Integer parseInteger(small Natural radix=10) { throw }
        
    }

}