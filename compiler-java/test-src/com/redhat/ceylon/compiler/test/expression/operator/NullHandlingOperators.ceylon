@nomodel
shared class NullHandlingOperators() {
    variable Boolean b1 := false;
    variable Natural n1 := 0;
    variable Natural n2 := 0;
    variable Natural? nat := 0;
    variable Integer i1 := +0;
    variable Integer i2 := +0;
    variable Integer? int := +0;
    
    void nullHandling(Natural[] seq) {
        b1 := nat exists;
        b1 := seq nonempty;
        n1 := nat ? n2;
        nat ?= n1;
        variable Integer? nullSafeMember := int?.negativeValue;
        variable Integer? nullSafeInvoke := int?.plus(+1);
        nullSafeInvoke := int?.plus{
            that = +1;
        };
    }
}