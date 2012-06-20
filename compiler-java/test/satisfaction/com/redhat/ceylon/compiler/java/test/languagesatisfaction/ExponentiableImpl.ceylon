class ExponentiableImpl<This,Other>() of This satisfies Exponentiable<This,Other>  
        given This satisfies ExponentiableImpl<This,Other> 
        given Other satisfies Numeric<Other> {

    shared actual This power(Other exponent) {
        return bottom;
    }
        
    shared actual This plus(This other) {
        return bottom;
    }
    
    shared actual This minus(This other) {
        return bottom;
    }
    
    shared actual This times(This other) {
        return bottom;
    }
    
    shared actual This divided(This other) {
        return bottom;
    }
    
    shared actual This negativeValue {
        return bottom;
    }
    
    shared actual This positiveValue {
        return bottom;
    }
}