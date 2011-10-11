@nomodel
shared class BasicOperators() {
    variable Boolean b1 := false;
    variable Boolean b2 := false;
    variable Integer i1 := +0;
    variable Integer i2 := +0;
    
    void basic() {
        b1 := b2;
        i1 := i2.positiveValue;
        basic();
        basic{};
        i1 .= positiveValue;
        i1 .= plus(+3);
        i1 .= plus{
            that = +3;
        };
    }
}