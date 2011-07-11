@nomodel
class LogicalOp(){
    shared Boolean m() {
        Boolean b1 = true;
        Boolean b2 = false;
        Boolean b3 = b1 && b2;
        Boolean b4 = b2 || b3;
        return true;
    }
}