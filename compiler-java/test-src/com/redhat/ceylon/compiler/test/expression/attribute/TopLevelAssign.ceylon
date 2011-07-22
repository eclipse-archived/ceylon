@nomodel
variable Boolean b1 := true;
@nomodel
variable shared Boolean b2 := true;
@nomodel
Boolean b3 {
    return true;
}
assign b3 {
    throw;
}
@nomodel
shared Boolean b4 {
    return true;
}
assign b4 {
    throw;
}

@nomodel
class TopLevelAssign(){
    void m(){
        b1 := false;
        b2 := false;
        b3 := false;
        b4 := false;
    }
}