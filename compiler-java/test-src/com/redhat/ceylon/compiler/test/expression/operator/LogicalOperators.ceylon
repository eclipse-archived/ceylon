@nomodel
shared class LogicalOperators() {
    variable Boolean b1 := false;
    variable Boolean b2 := false;
    
    void logical() {
        b1 := !b2;
        b1 := true || b2;
        b1 := false && b2;
        b1 ||= b2;
        b1 &&= b2;
    }
}