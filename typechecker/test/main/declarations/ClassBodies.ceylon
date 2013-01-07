class ClassBodies() {
    
    class GoodWithCircular() {
        String name = "gavin";
        void x() { y(); }
        void y() { x(); }
    }
    
    object goodWithCircular {
        String name = "gavin";
        void x() { y(); }
        void y() { x(); }
    }
    
    class Good2WithCircular() {
        void x() { y(); }
        void y() { x(); }
    }
    
    object good2WithCircular {
        void x() { y(); }
        void y() { x(); }
    }
    
    class Good3WithCircular() {
        String name = "gavin";
        void x() { this.y(); }
        void y() { this.x(); }
    }
    
    object good3WithCircular {
        String name = "gavin";
        void x() { this.y(); }
        void y() { this.x(); }
    }
    
    class Good4WithCircular() {
        void x() { this.y(); }
        void y() { this.x(); }
    }
    
    object good4WithCircular {
        void x() { this.y(); }
        void y() { this.x(); }
    }
    
    class BadWithCircular() {
        void x() { @error y(); }
        void y() { x(); }
        String name = "gavin";
    }
    
    object badWithCircular {
        void x() { @error y(); }
        void y() { x(); }
        String name = "gavin";
    }
    
    class Bad2WithCircular() {
        void x() { @error this.y(); }
        void y() { this.x(); }
        String name = "gavin";
    }
    
    object bad2WithCircular {
        void x() { @error this.y(); }
        void y() { this.x(); }
        String name = "gavin";
    }
    
    class GoodWithInner() {
        String name = "gavin";
        class Inner() {
            x();
        }
        String x() { return "Hell"; }
    }
    
    class BadWithInner() {
        class Inner() {
            @error x();
        }
        String x() { return "Hell"; }
        String name = "gavin";
    }
    
    class Good2WithInner() {
        String name = "gavin";
        class Inner() {
            void y() {
                x();
            }
        }
        String x() { return "Hell"; }
    }
    
    class Bad2WithInner() {
        class Inner() {
            void y() {
                @error x();
            }
        }
        String x() { return "Hell"; }
        String name = "gavin";
    }
    
    object goodWithInner {
        String name = "gavin";
        class Inner() {
            x();
        }
        String x() { return "Hell"; }
    }
    
    object badWithInner {
        class Inner() {
            @error x();
        }
        String x() { return "Hell"; }
        String name = "gavin";
    }
    
    object good2WithInner {
        String name = "gavin";
        class Inner() {
            void y() {
                x();
            }
        }
        String x() { return "Hell"; }
    }
    
    object bad2WithInner {
        class Inner() {
            void y() {
                @error x();
            }
        }
        String x() { return "Hell"; }
        String name = "gavin";
    }
    
    class Good3WithInner() {
        String name = "gavin";
        class Inner() {
            @type:"String" outer.x();
            void x() {}
        }
        String x() { return "Hell"; }
    }
    
    class Bad3WithInner() {
        class Inner() {
            void x() {}
            @error outer.x();
        }
        String x() { return "Hell"; }
        String name = "gavin";
    }
    
    class Good4WithInner() {
        String name = "gavin";
        class Inner() {
            void x() {}
            void y() {
                @type:"String" outer.x();
            }
        }
        String x() { return "Hell"; }
    }
    
    class Bad4WithInner() {
        class Inner() {
            void y() {
                void x() {}
                @error outer.x();
            }
        }
        String x() { return "Hell"; }
        String name = "gavin";
    }
    
    object good3WithInner {
        String name = "gavin";
        object inner {
            void x() {
                @type:"String" outer.x();
            }
        }
        String x() { return "Hell"; }
    }
    
    object bad8WithInner {
        String name = "gavin";
        object inner {
            @error outer.x();
            void x() {}
        }
        String x() { return "Hell"; }
    }
    
    object bad3WithInner {
        object inner {
            void x() {}
            @error outer.x();
        }
        String x() { return "Hell"; }
        String name = "gavin";
    }
    
    object good4WithInner {
        String name = "gavin";
        object inner {
            void y() {
                @type:"String" outer.x();
            }
            void x() {}
        }
        String x() { return "Hell"; }
    }
    
    object bad4WithInner {
        object inner {
            void x() {}
            void y() {
                @error outer.x();
            }
        }
        String x() { return "Hell"; }
        String name = "gavin";
    }
    
    class GoodWithThis() {
        String name = "gavin";
        value get { return this; }
    }
    
    object goodWithThis {
        String name = "gavin";
        value get { return this; }
    }
    
    class BadWithThis() {
        value get { @error return this; }
        String name = "gavin";
    }
    
    object badWithThis {
        value get { @error return this; }
        String name = "gavin";
    }
    
    void print(Value o) {}
    
    class Good2WithThis() {
        String name = "gavin";
        void print() { outer.print(this); }
    }
    
    object good2WithThis {
        String name = "gavin";
        void print() { outer.print(this); }
    }
    
    class Bad2WithThis() {
        void print() { @error outer.print(this); }
        String name = "gavin";
    }
    
    object bad2WithThis {
        void print() { @error outer.print(this); }
        String name = "gavin";
    }
    
    class Good3WithThis() {
        String name = "gavin";
        void member() {
            value t = this;
        }
    }
    
    object good3WithThis {
        String name = "gavin";
        void member() {
            value t = this;
        }
    }
    
    class Bad3WithThis() {
        String name = "gavin";
        @error value t = this;
    }
    
    object bad3WithThis {
        String name = "gavin";
        @error value t = this;
    }
    
    class Good4WithThis() {
        String name = "gavin";
        void member() {
            variable value t = this;
        }
    }
    
    object good4WithThis {
        String name = "gavin";
        void member() {
            variable value t = this;
        }
    }
    
    class Bad4WithThis() {
        String name = "gavin";
        @error variable value t = this;
    }
    
    object bad4WithThis {
        String name = "gavin";
        @error variable value t = this;
    }
    
    class Good5WithThis() {
        String name = "gavin";
        variable Good5WithThis? t = null;
        void member() {
            t = this;
        }
    }
    
    object good5WithThis {
        String name = "gavin";
        variable Value? t = null;
        void member() {
            t = this;
        }
    }
    
    class Bad5WithThis() {
        String name = "gavin";
        variable Bad5WithThis? t = null;
        @error t = this;
    }
    
    object bad5WithThis {
        String name = "gavin";
        variable Value? t = null;
        @error t = this;
    }
    
    class GoodWithOuter() {
        String name = "gavin";
        class Inner() {
            GoodWithOuter o { 
                return outer;
            }
        }
    }
    
    class BadWithOuter() {
        class Inner() {
            BadWithOuter o { 
                @error return outer;
            }
        }
        String name = "gavin";
    }
    
    object goodWithOuter {
        String name = "gavin";
        object inner {
            value o { 
                return outer;
            }
        }
    }
    
    object badWithOuter {
        object inner {
            value o { 
                @error return outer;
            }
        }
        String name = "gavin";
    }
    
    class Good2WithOuter() {
        String name = "gavin";
        class Inner() {
            print(outer);
        }
    }
    
    class Bad2WithOuter() {
        class Inner() {
            @error print(outer);
        }
        String name = "gavin";
    }
    
    object good2WithOuter {
        String name = "gavin";
        object inner {
            void method() {
                print(outer);
            }
        }
    }
    
    object bad5WithOuter {
        String name = "gavin";
        object inner {
            @error print(outer);
        }
    }
    
    object bad2WithOuter {
        object inner {
            @error print(outer);
        }
        String name = "gavin";
    }
    
    class Good3WithOuter() {
        String name = "gavin";
        class Inner() {
            value o = outer;
        }
    }
    
    class Bad3WithOuter() {
        class Inner() {
            @error value o = outer;
        }
        String name = "gavin";
    }
    
    object good3WithOuter {
        String name = "gavin";
        object inner {
            void method() {
                value o = outer;
            }
        }
    }
    
    object bad6WithOuter {
        String name = "gavin";
        object inner {
            @error value o = outer;
        }
    }
    
    object bad3WithOuter {
        object inner {
            @error value o = outer;
        }
        String name = "gavin";
    }
    
    class Good4WithOuter() {
        String name = "gavin";
        variable Value? o = null;
        class Inner() {
            o = outer;
        }
    }
    
    class Bad4WithOuter() {
        variable Value? o = null;
        class Inner() {
            @error o = outer;
        }
        String name = "gavin";
    }
    
    object good4WithOuter {
        String name = "gavin";
        variable Value? o = null;
        object inner {
            void method() {
                o = outer;
            }
        }
    }
    
    object bad7WithOuter {
        String name = "gavin";
        variable Value? o = null;
        object inner {
            @error o = outer;
        }
    }
    
    object bad4WithOuter {
        variable Value? o = null;
        object inner {
            @error o = outer;
        }
        String name = "gavin";
    }
    
    class Super() {
        shared String name="gavin";
    }
    
    class BadWithSuper() extends Super() {
        void inner() {
            String n = super.name;
            @error Value o = super;
            @error print(super);
            @error return super;
        }
    }
    
    class GoodWithParameter(String name) {
        String method() {
            return name;
        }
    }
    
    class GoodWithInitialized() {
        shared String name = "gavin";
        String method() {
            return name;
        }
    }
    
    class GoodWithUninitialized() {
        String name;
    }
    
    class Good2WithUninitialized() {
        String name;
        if (false) {
            name = "gavin";
        }
    }
    
    class Good3WithUninitialized() {
        String name;
        if (false) {
            return;
        }
        name = "gavin";
    }
    
    abstract class Good4WithUninitialized() {
        shared formal String name;
    }
    
    class Good5WithUninitialized() {
        String name;
        void method() {
            return;
        }
    }
    
    class Good6WithUninitialized() {
        String name;
        String method() {
            //Note: error not really necessary, 
            //      because captured by non-shared
            //      declaration
            @error return name; 
        }
    }
    
    class Bad7WithUninitialized() {
        String name;
        String method() {
            @error return name;
        }
        method();
    }
    
    class Bad8WithUninitialized() {
        String name;
        String method() {
            @error return name;
        }
        if (false) {
            method();
        }
    }
    
    class Bad9WithUninitialized() {
        String name;
        String method() {
            @error return name;
        }
        shared String attribute {
            return method();
        }
    }
    
    class Bad10WithUninitialized() {
        String name;
        String method() {
            @error return name;
        }
        void method2() {
            method();
        }
        method2();
    }
    
    class Bad5WithUninitialized() {
        String name;
        shared String method() {
            @error return name;
        }
        method();
    }
    
    class BadWithUninitialized() {
        @error shared String name;
    }
    
    class Bad2WithUninitialized() {
        @error shared String name;
        if (false) {
            name = "gavin";
        }
    }
    
    class Bad3WithUninitialized() {
        shared String name;
        if (false) {
            @error return;
        }
        name = "gavin";
    }
    
    class Outer() { 
        String s;  
        object inner {
            //TODO: in theory, error not really needed
            @error s = "hello"; 
        } 
    }
    
    class Class() {
        interface Interface {
            @error return;
        }
    }
    
    void method() {
        interface Interface {
            @error return;
        }
    }
    
    class GoodClassWithValue() {
        object foo {
            void method() {
                bar();
            }
        }
        void bar() {}
    }
    
    class BadClassWithValue() {
        object foo {
            @error bar();
        }
        void bar() {}
    }
    
    class BadClassWithValue2() {
        object foo {
            void method() {
                @error bar();
            }
        }
        print("hello");
        void bar() {}
    }
    
    class BadClassWithValue3() {
        object foo {
            print("hello");
            void method() {
                @error bar();
            }
        }
        void bar() {}
    }
    
    abstract class BadClassWithFormal() {
    	shared formal void noop();
    	shared formal String greeting;
    	@error noop();
    	@error String hello = greeting;
    	void method() {
        	noop();
        	String hello = greeting;
    	}
    }
    
    class BadClassWithCallToSuper() {
    	@error String s1 = string;
    	@error String s2 = super.string;
        void method() {
            String s1 = string;
            String s2 = super.string;
        }
    }
    
}