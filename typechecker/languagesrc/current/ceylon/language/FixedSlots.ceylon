shared interface FixedSlots<Other>
        satisfies Slots<Other> 
        given Other satisfies FixedSlots<Other> {

    doc "Slotwise complement operator |~x|.
         Implementations should respect the constraint
         that |(~x).slot(z)==!x.slot(z)|."
    shared formal Other complement;

}