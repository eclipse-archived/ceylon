shared class AllOperators() {
    variable Boolean b1 := false;
    variable Boolean b2 := false;
    variable Natural n1 := 0;
    variable Natural n2 := 0;
    variable Integer i1 := +0;
    variable Integer i2 := +0;
    variable Float f1 := 0.0;
    variable Float f2 := 0.0;
       
    void slotwise() {
        i1 := ~i2;
        i1 := +4 | i2;
        i1 := +4 & i2;
        i1 := +4 & i2;
        i1 := +4 ~ i2;
        i1 |= i2;
        i1 &= i2;
        i1 ~= i2;
        i1 ^= i2;
    }
}