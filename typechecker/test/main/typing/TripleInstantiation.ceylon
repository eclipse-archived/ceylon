class TripleInstantiation() {

interface RIC_A {}

interface RIC_B {}

interface RIC_C {}

interface RIC_Top<out T> {
    shared formal T val;
    formal shared T get();
}

abstract class RIC_Middle() satisfies RIC_Top<RIC_A> {}

interface RIC_Left satisfies RIC_Top<RIC_B> {}

interface RIC_Right satisfies RIC_Top<RIC_C> {}

@error class RIC_Nothing_From_Class() 
        extends RIC_Middle() 
        satisfies RIC_Left & RIC_Right {}

void bugg() {
    RIC_Top<RIC_A&RIC_B&RIC_C> nfc = RIC_Nothing_From_Class();
    @type:"TripleInstantiation.RIC_A&TripleInstantiation.RIC_B&TripleInstantiation.RIC_C" 
    value get = RIC_Nothing_From_Class().get();
    @type:"TripleInstantiation.RIC_A&TripleInstantiation.RIC_B&TripleInstantiation.RIC_C" 
    value val = RIC_Nothing_From_Class().val;
}

}