@noanno
class SmallConversion(Integer big) {
    shared default small Integer ar {
        return big;
    }
    shared default small Integer ar2 
            => big;
    
    
    shared default small Integer mr() {
        return big;
    }
    
    shared default small Integer mr2() 
            => big;
    
    void mp(small Integer p) {
        
    }
    shared void mpc() {
        mp(big);
        mp{p=big;};
    }
    
    void locals() {
        small value sm = big;
        small value sm2 => big;
    }
}

@noanno
class SmallConversion2(Integer big, Integer bigm()) 
        extends SmallConversion(big) {
    ar = big;
    ar2 => big;
    mr = bigm;
    mr2() => big;
}

Integer smallConversionBig => 1;
variable small Integer smallConversion1 = smallConversionBig;
small Integer smallConversion2 => smallConversionBig;

small Integer smallConversion3 => 1;
assign smallConversion3 {
    
}
void smallConversionToplvels() {
    smallConversion1 = smallConversionBig;
    smallConversion3 = smallConversionBig;
}