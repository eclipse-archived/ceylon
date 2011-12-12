class Formal() {
    
    interface Interface {
        shared formal String name;
        shared formal void hello();
        @error shared formal String badName = "gavin";
        @error shared formal void badHello() {}
        @error String reallyBadName = "gavin";
    }
    
    abstract class AbstractClass() {
        shared formal String name;
        shared formal void hello();
        @error shared formal String badName = "gavin";
        @error shared formal void badHello() {}
        String goodName = "gavin";
    }

    //formal member not implemented in concrete class
    @error
    class Class() {
        @error shared formal String name;
        @error shared formal void hello();
        @error shared formal String badName = "gavin";
        @error shared formal void badHello() {}
        String goodName = "gavin";
    }
    
    void method() {
        @error shared formal String name;
        @error shared formal void hello();
        @error shared formal String badName = "gavin";
        @error shared formal void badHello() {}
        String goodName = "gavin";
    }
    
    String getter {
        @error shared formal String name;
        @error shared formal void hello();
        @error shared formal String badName = "gavin";
        @error shared formal void badHello() {}
        String goodName = "gavin";
        return goodName;
    }
    
}