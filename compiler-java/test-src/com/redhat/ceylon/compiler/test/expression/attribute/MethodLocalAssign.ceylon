@nomodel
class MethodLocalAssign(){
    void m(){
        variable Boolean b1 := true;
        b1 := false;
        Boolean b2 {
            return b1;
        } assign b2 {
            b1 = b2;
        }
    }
}