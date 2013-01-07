class Hiding() {
    
    class Hidden() {
        shared String hello = "hello";
        void helloWorld() {}
    }
    
    class Hider() extends Hidden() {
        String hello = "hi";
        shared void helloWorld() {}
    }
    
    String h = Hider().hello;
    Hider().helloWorld();
    
    class Outer() {
        String? hello = "hello";
        void helloWorld(String name) {}
        
        class Inner() {
            String hello = "hello";
            void helloWorld() {
                outer.helloWorld("gavin");
            }
           
            String ih = hello;
            String? oh = outer.hello;
            helloWorld();
        }
        
    }
    
    class Super() {
        shared String hello = "hello";
    }
    
    class OuterWithParameter() {
        
        Object hello = "hello";
        
        class InnerWithAttribute() {
            String hello = "hello";
            void method() {
                @type:"String" value s = hello;
            }
        }
        
        class InnerWithSuper() extends Super() {
            void method() {
                @type:"String" value s = hello;
            }
        }
        
        class InnerWithParameter(String hello) {
            void method() {
                @type:"String" value s = hello;
            }
        }
    }
    
    void method() {
        String? hello = "hello";
        void helloWorld(String name) {}
        
        void nested() {
            String hello = "hello";
            void helloWorld() {
                @error outer.helloWorld("gavin");
            }
           
            String ih = hello;
            helloWorld();
        }
    }
    
    class OuterSuperclass() {
        shared interface InnerInterface {}
        shared abstract class InnerAbstractClass() {}
    }
    
    class OuterClass() extends OuterSuperclass() {
        class Inner() 
            extends InnerAbstractClass()
            satisfies InnerInterface {}
    }
    
}