@nomodel
class BooleanBoxing(){
    void m() {
        Boolean b1 = true;
        Boolean b2 = !b1;
        Boolean b3 = negate(b2);
    }
    Boolean negate(Boolean b) {
        return !b;
    }
}