shared interface Slots<Other>
        given Other satisfies Slots<Other> {
        
    doc "Slotwise or operator |x | y|. Implementations
         should respect the constraint that 
         |(x|y).slot(z)==(x.slot(z)||y.slot(z))|."
    shared formal Other or(Other bits);
    
    doc "Slotwise and operator |x & y|. Implementations
         should respect the constraint that 
         |(x&y).slot(z)==(x.slot(z)&&y.slot(z))|."
    shared formal Other and(Other bits);
    
    doc "Slotwise xor operator |x ^ y|. Implementations
         should respect the constraint that 
         |(x^y).slot(z)==((x|y).slot(z)&&!(x&y).slot(z))|."
    shared formal Other xor(Other bits);
    
    doc "Slotwise complement in operator |x ~ y|. 
         Implementations should respect the constraint 
         that |(x~y).slot(z)==(x.slot(z)&&!y.slot(z))|."
    shared formal Other complementIn(Other bits);
    
}
