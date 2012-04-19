@nomodel
interface I1 {
    shared formal String m1(Integer i = 0, Boolean b = false);
}

@nomodel
interface I2 {
    shared formal String m2(Integer i = 0, Boolean b = false);
}

@nomodel
class C() satisfies I1&I2 {
    shared actual String m1(Integer i, Boolean b) {
        return "";
    }
    shared actual String m2(Integer i, Boolean b) {
        return "";
    }
}