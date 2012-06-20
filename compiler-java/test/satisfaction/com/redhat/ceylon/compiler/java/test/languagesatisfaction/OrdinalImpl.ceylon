class OrdinalImpl<out Other>() of Other
        satisfies Ordinal<Other>
        given Other satisfies OrdinalImpl<Other> {
        
    shared actual Other successor {
        return bottom;
    }
    
    shared actual Other predecessor {
        return bottom;
    }
}