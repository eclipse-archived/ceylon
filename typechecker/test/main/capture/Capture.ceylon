variable Integer n=0;

class Capture() {
    
    n=12;
    
    void use(Object o) {}
    
    class X() {}
    
    void methodWithVariable() {
        @captured variable X x = X();
        void innerMethod() {
            use(x);
        }
        X innerGetter {
            return x;
        }
        assign innerGetter {
            x=innerGetter;
        }
        void containingMethod() {
            void nestedMethod() {
                x=X();
            }
        }
    }
    
    void methodWithConstant() {
        X x = X();
        void innerMethod() {
            use(x);
        }
        X innerGetter {
            return x;
        }
        assign innerGetter {
            @error x=innerGetter;
        }
        void containingMethod() {
            void nestedMethod() {
                use(x);
            }
        }
    }
    
    void methodWithParameter(@captured X x) {
        void innerMethod() {
            use(x);
        }
        X innerGetter {
            return x;
        }
        assign innerGetter {
            @error x=innerGetter;
        }
        void containingMethod() {
            void nestedMethod() {
                use(x);
            }
        }
    }
    
    X getterWithVariable {
        @captured variable X x = X();
        void innerMethod() {
            use(x);
        }
        X innerGetter {
            return x;
        }
        assign innerGetter {
            x=innerGetter;
        }
        void containingMethod() {
            void nestedMethod() {
                x=X();
            }
        }
        return x;
    }
    
    class ClassWithVariable() {
        @captured variable X x = X();
        @uncaptured variable X y = X();
        @captured shared variable X z = X();
        void innerMethod() {
            use(x);
        }
        X innerGetter {
            return x;
        }
        assign innerGetter {
            x=innerGetter;
        }
        void containingMethod() {
            void nestedMethod() {
                x=X();
            }
        }
    }
    
    class ClassWithParameter(@captured X x) {
        void innerMethod() {
            use(x);
        }
        X innerGetter {
            return x;
        }
        assign innerGetter {
            @error x=innerGetter;
        }
        void containingMethod() {
            void nestedMethod() {
                use(x);
            }
        }
    }
    
    class WithMultipleParameters(@captured String w,
                                 @captured String x, 
                                 @uncaptured String y, 
                                 @uncaptured String z) {
        shared String yy = y;
        use(z);
        shared void xx() {
            if (true) {
                use(x);
            }
        }
        shared class Inner() {
            use(w);
        }
    }
    
    class UnsharedDuped(@uncaptured s) {
        @uncaptured String s;
    }
    
    class SharedDuped(@uncaptured s) {
        @captured shared String s;
    }
    
    class UnsharedDuped2(@error s) {
        String s { return s; }
    }
    
    class SharedDuped2(@error s) {
        shared String s { return s; }
    }
    
    class WithMultipleAttributes() {
        @uncaptured String x = "X";
        @captured String y = "Y";
        @captured String w = "W";
        @captured String z = "Z";
        use(x);
        void inner() {
            use(y);
            use(this.w);
        }
        class Inner() {
            use(outer.z);
        }
    }
    
    class WithNamedArgument() {
        @uncaptured String hello = "hello";
        use { o=hello; };
    }
    
    class WithNamedArgumentInMethod() {
        @captured String hello = "hello";
        void method() {
            use { o=hello; };
        }
    }
    
    class WithNamedArgumentInClass() {
        @captured String hello = "hello";
        class Class() {
            use { o=hello; };
        }
    }
    
    class WithAttributeArgument() {
        @captured String hello = "hello";
        use {
            value o {
                return hello;
            }
        };
    }
    
    class WithValueArgument() {
        @captured String hello = "hello";
        use {
            object o {
                print(hello);
            }
        };
    }
    
    class WithQualifiedRef() {
        @captured String hello = "hello";
        @captured value q = WithQualifiedRef();
        function f() { 
            return q.hello;
        }
    }
    
    class WithQualifiedRefToThis() {
        @captured String hello = "hello";
        function t() { 
            return this; 
        }
        function f() { 
            return t().hello;
        }
    }
    
    class QualifiedAttributeAccess(){
        @captured Boolean b = true;
        @captured variable Boolean b2 = true;
        @captured QualifiedAttributeAccess q = QualifiedAttributeAccess();
        
        Boolean m(){
            return this.b;
        }

        Boolean m2(){
            return this.b2;
        }

        Boolean qm(){
            return QualifiedAttributeAccess().b;
        }

        Boolean qm2(){
            return QualifiedAttributeAccess().b2;
        }

        Boolean qm3(){
            return q.b;
        }

        Boolean qm4(){
            return q.b2;
        }
     }
     
     class Something1(@uncaptured n) {
         shared Integer n;
     }
     
     class Something2(@uncaptured n) { //was @captured before...
         shared Integer n;
         shared void p() {
             print(n);
         }
     }

     class Something3(@captured Integer n) {
         shared void p() {
             print(n);
         }
     }

    class C(@captured void f(), @captured void g()) {
        void m(C c) {
            c.f();
            this.g();
        }
    }
    
}

class MethodDefaultedParamCaptureInitParam1(@captured String s) {
    String m(String t = s);
    m = (String x) => x;
}

class MethodDefaultedParamCaptureInitParam2(@captured String s) {
    String m(String t = s) { return t; }
}

class MethodDefaultedParamCaptureInitParam3(@captured String s) {
    String m(String t = s);
}

class MethodDefaultedParamCaptureInitParam4(@captured String s) {
    class C(String t = s) {}
}

void methodDefaultedParamCaptureInitParam1(@captured String s) {
    String m(String t = s);
    m = (String x) => x;
}

void methodDefaultedParamCaptureInitParam2(@captured String s) {
    String m(String t = s) { return t; }
}

void methodDefaultedParamCaptureInitParam3(@captured String s) {
    String m(String t = s);
}

void methodDefaultedParamCaptureInitParam4(@captured String s) {
    class C(String t = s) {}
}

String() cap1(@captured String s) {
    return () => s;
}

String() cap2() {
    @captured String s="hello";
    return () => s;
}

class MethodSpecifyingInitParam(@uncaptured Callable<Anything,[]> x) {
    Anything foo() => x();
}

class MethodSpecifyingInitParam2(@uncaptured void x()) {
    Anything foo() => x();
}

void methodSpecifyingInitParam(@uncaptured Callable<Anything,[]> x) {
    Anything foo() => x();
}

void methodSpecifyingInitParam2(@uncaptured void x()) {
    Anything foo() => x();
}

class MethodCapturingInitParam(@captured Callable<Anything,[]> x) {
    void foo() { x(); }
}

class MethodCapturingInitParam2(@captured void x()) {
    void foo() { x(); }
}

void methodCapturingInitParam(@captured Callable<Anything,[]> x) {
    void foo() { x(); }
}

void methodCapturingInitParam2(@captured void x()) {
    void foo() { x(); }
}

class DefaultedParameterClassInstantiation(@uncaptured Integer m = 1, Integer n = m + 1) {}