@nomodel
shared class NamedArgumentInvocationLocal() {
    shared void f() {
        Natural m(Natural n, String s) {
    	    return n;
        }
        void v(Natural n, String s) {
        }
        v{
            s="abc";
            n=123;
        };
        Natural x = m{
            s="abc";
            n=123;
        };
    }
    
}