shared interface FixedSlots<B>
        satisfies Slots<B> {

    doc "Slotwise complement operator |~x|.
         Implementations should respect the constraint
         that |(~x).slot(z)==!x.slot(z)|."
    shared formal Bits<B> complement;

}