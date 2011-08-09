@nomodel
class NamedArgumentInvocation(){
    Boolean f(Natural n, String s) {
        return true;
    }
    Boolean m1() {
        return f {
            s = "foo";
            n = 1;
        };
    }
    Boolean m2() {
        return NamedArgumentInvocation().f {
            s = "bar";
            n = 2;
        };
    }
}