class IntegralImpl<Other>() of Other satisfies Integral<Other>
        given Other satisfies IntegralImpl<Other> {

    shared actual Other plus(Other other) {
        return bottom;
    }
    
    shared actual Other minus(Other other) {
        return bottom;
    }
    
    shared actual Other times(Other other) {
        return bottom;
    }
    
    shared actual Other divided(Other other) {
        return bottom;
    }
    
    shared actual Other remainder(Other other) {
        return bottom;
    }
    
    shared actual Other successor {
        return bottom;
    }
    
    shared actual Other predecessor {
        return bottom;
    }
    
    shared actual Other negativeValue {
        return bottom;
    }
    
    shared actual Other positiveValue {
        return bottom;
    }
    
    shared actual Boolean zero {
        return bottom;
    }
    
    shared actual Boolean unit {
        return bottom;
    }
    
    shared actual Integer distanceFrom(Other other) {
        return 0;
    }

}