@nomodel
class NamedArgumentInvocation(){
    Boolean m() {
        return f {
            s = "foo";
            n = 2;
        };
    }
    
    Boolean f(Natural n, String s) {
        return true;
    }
}