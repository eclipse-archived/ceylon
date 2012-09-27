@nomodel
class MethodIfConditionList() {
    Boolean m1(Integer x) {
        if (x > 0, x < 10) {
            return x == 1;
        } else {
            return false;
        }
    }
    Boolean m2(Void x, Integer z) {
        if (is Integer y = x, y > 0, z < 10) {
            return z == 1;
        } else {
            return false;
        }
    }
    
    Boolean m3(Void x, Integer z) {
        if (z < 10, is Integer y = x, y > 0) {
            return y == 1;
        } else {
            return false;
        }
    }
    
    Boolean m4(Void x, Integer z) {
        if (z < 10, z > 0, is Integer y = x) {
            return y == 1;
        } else {
            return false;
        }
    }
    
    Boolean m5(Void[] x) {
        if (nonempty x, is Integer y = x[0], y > 0) {
            return y == 1;
        } else {
            return false;
        }
    }
    
    Boolean m6(Void[] x) {
        if (exists x[0], is Integer y = x, y > 0) {
            return y == 1;
        } else {
            return false;
        }
    }
}