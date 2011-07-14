class Hiding() {
    
    class Hidden() {
        shared String hello = "hello";
        void helloWorld() {}
    }
    
    class Hider() extends Hidden() {
        @error String hello = "hi"; //language spec says this is illegal!
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
    
}