public final class Natural(Natural natural)
        satisfies Integral<Natural,Integer>, Case<Integral>, Bits<Natural> { 
    
    doc "Implicit type promotion to |Integer|"
    override public extension Integer integer { throw }
    
    doc "Implicit type promotion to |Whole|"
    override public extension Whole whole { throw }
    
    doc "Implicit type promotion to |Float|"
    override public extension Float float { throw }
    
    doc "Implicit type promotion to |Decimal|"
    override public extension Decimal decimal { throw }

    doc "Shift bits left by the given number of places"
    public Natural leftShift(Natural digits) { throw }
    
    doc "Shift bits right by the given number of places"
    public Natural rightShift(Natural digits) { throw }
    
    public extension class StringToNatural(String string) {
    
        doc "Parse the string representation of a |Natural| in the given radix"
        public Natural parseNatural(small Natural radix=10) { throw }
        
    }

}