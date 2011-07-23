@nomodel
class NamedArgumentGetterInvocation(){
    Boolean m() {
        return f {
            String s { return "foo"; }
            Natural n { return 2; }
        };
    }
    
    Boolean f(Natural n, String s) {
        return true;
    }
}