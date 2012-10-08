@nomodel
class MethodIfConditionList() {
    Boolean boolBool(Integer x) {
        if (x > 0, x < 10) {
            return x == 1;
        } else {
            return false;
        }
    }
    Boolean isBoolBool(Void x, Integer z) {
        if (is Integer y = x, y > 0, z < 10) {
            return z == 1;
        } else {
            return false;
        }
    }
    Boolean isBoolBoolSynthetic(Void x, Integer z) {
        if (is Integer x, x > 0, z < 10) {
            return z == 1;
        } else {
            return false;
        }
    }
    
    Boolean boolIsBool(Void x, Integer z) {
        if (z < 10, is Integer y = x, y > 0) {
            return y == 1;
        } else {
            return false;
        }
    }
    
    Boolean boolIsBoolSynthetic(Void x, Integer z) {
        if (z < 10, is Integer x, x > 0) {
            return x == 1;
        } else {
            return false;
        }
    }
    
    Boolean boolBoolIs(Void x, Integer z) {
        if (z < 10, z > 0, is Integer y = x) {
            return y == 1;
        } else {
            return false;
        }
    }
    
    Boolean boolBoolIsSynthetic(Void x, Integer z) {
        if (z < 10, z > 0, is Integer x) {
            return x == 1;
        } else {
            return false;
        }
    }
    
    Boolean nonemptyIsBool(Void[] x) {
        if (nonempty x, is Integer y = x[0], y > 0) {
            return y == 1;
        } else {
            return false;
        }
    }
    
    Boolean existsIsBool(Void[] x) {
        if (exists x[0], is Integer y = x[0], y > 0) {
            return y == 1;
        } else {
            return false;
        }
    }
    
    void bug791(String|Integer x) {
        if (is String x, x.uppercased=="S") {
            print (x.uppercased);
        }
        if (is String z=x, z.uppercased=="S") {
        }
    }
}