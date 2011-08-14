@nomodel
class NamedArgumentInvocation(){
    Boolean f(Natural n, String s) {
        return true;
    }
    void v(Natural n, String s) {
    }
    
    Boolean simple1() {
        v{
            s = "foo";
            n = 1;
        };
        return f {
            s = "foo";
            n = 1;
        };
    }
    Boolean simple2() {
        this.v{
            s = "bar";
            n = 2;
        };
        return this.f {
            s = "bar";
            n = 2;
        };
    }
    Boolean qualified() {
        NamedArgumentInvocation().v{
            s = "bar";
            n = 2;
        };
        return NamedArgumentInvocation().f {
            s = "bar";
            n = 2;
        };
    }
    // init
    v{
        s = "foo";
        n = 0;
    };
    f {
        s = "foo";
        n = 0;
    };
}
