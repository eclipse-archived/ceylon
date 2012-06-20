class NumericImpl<Other>() of Other satisfies Numeric<Other>
        given Other satisfies Numeric<Other>  {
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
    
    shared actual Other negativeValue {
        return bottom;
    }
    
    shared actual Other positiveValue {
        return bottom;
    }
}