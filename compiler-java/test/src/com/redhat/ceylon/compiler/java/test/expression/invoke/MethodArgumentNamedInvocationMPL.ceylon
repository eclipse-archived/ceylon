@nomodel
class MethodArgumentNamedInvocationMPL() {
    void m1(Callable<Callable<String, [Integer]>, []> f) {
        String s = f()(1);
    }
    void m2(Callable<Callable<Void, [Integer]>, []> f) {
        Void v = f()(1);
    }
    void m3(String(Integer)() f) {
        String s = f()(1);
    }
    void m4(Void(Integer)() f) {
        Void v = f()(1);
    }
    void m5(void f()(Integer x)) {
        Void v = f()(1);
    }
    void m6(Void f()(Integer x)) {
        Void v = f()(1);
    }
    void m7(String f()(Integer x)) {
        String s = f()(1);
    }
    
    void callsite() {
        
        m1{
            function f()(Integer x) {
                return x.string;
            }
        };
        
        m2{
            void f()(Integer x) {
                return;
            }
        };
        m2{
            function f()(Integer x) {
                return x.string;
            }
        };
        
        m3{
            function f()(Integer x) {
                return x.string;
            }
        };
        
        m4{
            void f()(Integer x) {
                return;
            }
        };
        m4{
            function f()(Integer x) {
                return x.string;
            }
        };
        
        m5{
            void f()(Integer x) {
                return;
            }
        };
        m5{
            function f()(Integer x) {
                return x.string;
            }
        };
        
        m6{
            void f()(Integer x) {
                return;
            }
        };
        m6{
            function f()(Integer x) {
                return x.string;
            }
        };
        
        m7{
            function f()(Integer x) {
                return x.string;
            }
        };
        
    }
}