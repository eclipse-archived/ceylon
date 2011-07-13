@nomodel
class LogicalOp(){
    shared Boolean m() {
        Boolean b1 = true;
        Boolean b2 = false;
        Boolean b3 = b1 && b2;
        variable Boolean b4 := b2 || b3;
        variable Boolean b5 := !b4;
        b4 &&= b3;
        b5 ||= b4;
        return true;
    }
}