@nomodel
class NamedArgumentInvocation(){
    Boolean f(Natural n, String s) {
        return true;
    }
    
    Boolean simple1() {
        return f {
            s = "foo";
            n = 1;
        };
    }
    Boolean simple2() {
        return this.f {
            s = "bar";
            n = 2;
        };
    }
    Boolean qualified() {
        return NamedArgumentInvocation().f {
            s = "bar";
            n = 2;
        };
    }
    // init
    f {
        s = "foo";
        n = 0;
    };
}
