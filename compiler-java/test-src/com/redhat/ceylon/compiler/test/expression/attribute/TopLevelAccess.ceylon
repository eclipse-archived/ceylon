@nomodel
Boolean b1 = true;
@nomodel
shared Boolean b2 = true;
@nomodel
Boolean b3 {
    return true;
}
@nomodel
shared Boolean b4 {
    return true;
}

@nomodel
class TopLevelAccess(){
    void m(){
        Boolean bb1 = b1;
        Boolean bb2 = b2;
        Boolean bb3 = b3;
        Boolean bb4 = b4;
    }
}