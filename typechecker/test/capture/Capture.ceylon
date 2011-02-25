class Capture() {
    
    class X() {}
    
    void methodWithVariable() {
        variable X x := X();
        void innerMethod() {
            @error x;
        }
        X innerGetter {
            @error return x;
        }
        assign innerGetter {
            @error x:=innerGetter;
        }
        void containingMethod() {
            void nestedMethod() {
                @error x:=X();
            }
        }
    }
    
    void methodWithConstant() {
        X x = X();
        void innerMethod() {
            x;
        }
        X innerGetter {
            return x;
        }
        assign innerGetter {
            @error x:=innerGetter;
        }
        void containingMethod() {
            void nestedMethod() {
                x;
            }
        }
    }
    
    void methodWithParameter(X x) {
        void innerMethod() {
            x;
        }
        X innerGetter {
            return x;
        }
        assign innerGetter {
            @error x:=innerGetter;
        }
        void containingMethod() {
            void nestedMethod() {
                x;
            }
        }
    }
    
    X getterWithVariable {
        variable X x := X();
        void innerMethod() {
            @error x;
        }
        X innerGetter {
            @error return x;
        }
        assign innerGetter {
            @error x:=innerGetter;
        }
        void containingMethod() {
            void nestedMethod() {
                @error x:=X();
            }
        }
        return x;
    }
    
    class ClassWithVariable() {
        @captured variable X x := X();
        @uncaptured variable X y := X();
        @captured shared variable X z := X();
        void innerMethod() {
            x;
        }
        X innerGetter {
            return x;
        }
        assign innerGetter {
            x:=innerGetter;
        }
        void containingMethod() {
            void nestedMethod() {
                x:=X();
            }
        }
    }
    
    class ClassWithParameter(@captured X x) {
        void innerMethod() {
            x;
        }
        X innerGetter {
            return x;
        }
        assign innerGetter {
            @error x:=innerGetter;
        }
        void containingMethod() {
            void nestedMethod() {
                x;
            }
        }
    }
    
}