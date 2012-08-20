class OrdinalImpl<Other>() of Other
        satisfies Ordinal<Other>
        given Other satisfies OrdinalImpl<Other> {
        
    shared actual Other successor {
        return bottom;
    }
    
    shared actual Other predecessor {
        return bottom;
    }
    
    shared actual Integer distanceFrom(Other other) {
        return 0;
    }
}