@nomodel
class BooleanBoxing(){
    void m() {
        Boolean b1 = true;
        Boolean b2 = !b1;
        Boolean b3 = negate(b2);
        Boolean? b4 = negate2(b3);
        Boolean b5 = negate3(b4);
    }
    Boolean negate(Boolean b) {
        return !b;
    }
    Boolean? negate2(Boolean? b) {
        if (exists b) {
            return !b;
        }
        return b;
    }
    Boolean negate3(Boolean? b) {
        if (exists b) {
            return !b;
        } else {
            return false;
        }
    }
}