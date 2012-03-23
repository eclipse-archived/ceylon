@nomodel
class DefaultedAndSequenced() {
    void m(String s, Integer i = 1, Boolean... b = {true, false}) {
    }
    DefaultedAndSequenced self() {
        return this;
    }
    void positional() {
        m("");
        m("", 2);
        m("", 2, false);
        Boolean[] x = {false, false};
        m("", 2, x...);
    }
    void qmePositional() {
        self().m("");
        self().m("", 2);
        self().m("", 2, false);
        Boolean[] x = {false, false};
        self().m("", 2, x...);
    }
    void qtePositional() {
        DefaultedAndSequenced().m("");
        DefaultedAndSequenced().m("", 2);
        DefaultedAndSequenced().m("", 2, false);
        Boolean[] x = {false, false};
        DefaultedAndSequenced().m("", 2, x...);
    }
    void namedArgs() {
        m{
            s="";
        };
        m{
            s="";
            i=2;
        };
        m{
            s="";
            i=2;
            b={false};
        };
        m{
            s=""; 
            i=2; 
            true, true
        };
    }
    void qmeNamedArgs() {
        self().m{
            s="";
        };
        self().m{
            s="";
            i=2;
        };
        self().m{
            s="";
            i=2;
            b={false};
        };
        self().m{
            s=""; 
            i=2; 
            true, true
        };
    }
    void qteNamedArgs() {
        DefaultedAndSequenced().m{
            s="";
        };
        DefaultedAndSequenced().m{
            s="";
            i=2;
        };
        DefaultedAndSequenced().m{
            s="";
            i=2;
            b={false};
        };
        DefaultedAndSequenced().m{
            s=""; 
            i=2; 
            true, true
        };
    }
}