class ScalarImpl<Other>() of Other satisfies Scalar<Other>
        given Other satisfies Scalar<Other> {

    shared actual Other magnitude = bottom;
    
    shared actual Other fractionalPart = bottom;
    
    shared actual Other wholePart = bottom;

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
    
    shared actual Integer integer = bottom;
    
    shared actual Float float = bottom;
    
    shared actual Integer sign = bottom;
    
    shared actual Boolean positive = bottom;
    
    shared actual Boolean negative = bottom;
    
    shared actual Other negativeValue = bottom;
    
    shared actual Other positiveValue = bottom;
    
    shared actual Comparison compare(Other other) {
        return bottom;
    }
    

}