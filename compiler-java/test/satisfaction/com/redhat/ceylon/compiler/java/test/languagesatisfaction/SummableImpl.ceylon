class SummableImpl<Other>() of Other satisfies Summable<Other> 
    given Other satisfies SummableImpl<Other> {

    shared actual Other plus(Other other) {
        return bottom;
    }
}