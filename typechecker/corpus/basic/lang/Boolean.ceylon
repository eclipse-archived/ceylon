public final class Boolean
        satisfies Case<Boolean>, Bits<Boolean> {
    case true, case false;

    doc "The binary or operator x | y"
    override public Boolean or(Boolean boolean) {
        if (this) {
            return true
        }
        else {
            return boolean
        }
    }
    
    doc "The binary and operator x & y"
    override public Boolean and(Boolean boolean) {
        if (this) {
            return boolean
        }
        else {
            return false
        }
    }
    
    doc "The binary xor operator x ^ y"
    override public Boolean xor(Boolean boolean) {
        if (this) {
            return boolean.complement
        }
        else {
            return boolean
        }
    }
    
    doc "The unary not operator !x"
    override public Boolean complement { 
        if (this) {
            return false
        }
        else {
            return true
        }
    }
    
    override public List<Boolean> bits {
        return SingletonList(this)
    }
    
}