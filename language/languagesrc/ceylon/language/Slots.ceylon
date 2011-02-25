shared interface Slots<B>
        given B satisfies Slots<B> {
        
    doc "Slotwise or operator |x | y|. Implementations
         should respect the constraint that 
         |(x|y).slot(z)==(x.slot(z)||y.slot(z))|."
    shared formal B or(B bits);
    
    doc "Slotwise and operator |x & y|. Implementations
         should respect the constraint that 
         |(x&y).slot(z)==(x.slot(z)&&y.slot(z))|."
    shared formal B and(B bits);
    
    doc "Slotwise xor operator |x ^ y|. Implementations
         should respect the constraint that 
         |(x^y).slot(z)==((x|y).slot(z)&&!(x&y).slot(z))|."
    shared formal B xor(B bits);
    
    doc "Slotwise complement in operator |x ~ y|. 
         Implementations should respect the constraint 
         that |(x~y).slot(z)==(x.slot(z)&&!y.slot(z))|."
    shared formal B complement(B bits);
    
}
