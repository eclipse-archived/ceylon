object bar {
    @error print(bar);
    @error print(this);
    object baz {
        @error print(bar);
        @error print(baz);
        @error print(outer.baz);
        shared void printSelf() {
            @error print(bar);
            print(baz);
            print(outer.baz);
        }
    }
    shared void printSelf() {
        print(baz);
        print(bar);
        print(this);
    }
}

interface SelfReference {
    
    void use(Object o) {}
    class Super(Object o) {}
    
    class Good() extends Super("hello") {
        use("hello");
        function f() {
            use(this);
            return this;
        }
        value v {
            use(this);
            return this;
        }
        class Inner() 
                extends Super(outer) {
            function f() {
                use(outer);
                return outer;
            }
            value v {
                use(outer);
                return outer;
            }
        }
    }
    
    class Good2() extends Super("hello") {
        function f() {
            use(this);
            return this;
        }
        value v {
            use(this);
            return this;
        }
        class Inner() 
                extends Super(outer) {
            function f() {
                use(outer);
                return outer;
            }
            value v {
                use(outer);
                return outer;
            }
        }
    }
    
    @error class Bad() extends Super(this) {
        function f() {
            @error use(this);
            @error return this;
        }
        value v {
            @error use(this);
            @error return this;
        }
        @error class Inner() 
                extends Super(outer) {
            function f() {
                @error use(outer);
                @error return outer;
            }
            value v {
                @error use(outer);
                @error return outer;
            }
        }
        use("hello");
    }
    
    class Ok() {
        String hello = "hello";
        void method() {
            print(this.hello);
        }
        class Inner() {
            print(outer.hello);
        }
    }
    
    abstract class Abstract() {
        shared String hello = "hello";
        /*if (is Concrete conc = this) {}
        void method() {
            if (is Object sup = super) {}
        }*/
    }
    
    class Concrete() extends Abstract() {
        @error print(hello);
        @error print(super.hello);
        @error print(this.hello);
        void method() {
            print(hello);
            print(super.hello);
            print(this.hello);
        }
        class Class() {
            print(hello);
            print(outer.hello);
        }
    }
    
    class Outer() {
        class Inner() {
            Inner getInner() { @error return getInn(this); }
            Outer getOuter() { return getOut(outer); }
            Inner ii = getInner();
            Outer oo = getOuter();
        }
        Inner getInn(Inner i) { return i; }
        Outer getOut(Outer o) { return o; }
    }
}

