abstract class Classes() {
    
    variable Integer count = 0;
    
    count++;
    
    String name;
    name = "Gavin";
    
    shared formal Boolean shouldRetry;
    
    shared formal void print(String s);
    
    void callsForward() {
        @error printLine("Hello world");
    }
    
    void printLine(String s) {
        @error print(s + "\n");
    }
    
    String upper(String x) {
        return x.uppercased;
    }
    
    printLine("Hi!");
    
    @error x = 0;
    @error y = 0.0;
    
    interface NestedInterface {
        shared formal String hello;
    }
    
    abstract class NestedAbstractClass() {
        shared String hello = upper("hello");
        shared formal Integer times;
    }
    
    class MemberClass() {
        shared String goodbye = upper("goodbye");
        shared variable Integer times = 1;
    }
    
    MemberClass();
        
    printLine(MemberClass().goodbye);
    @error MemberClass().goodbye = "Foo";
    @error MemberClass().times = "Foo";
    MemberClass().times = 5;
    
    MemberClass create() {
        return MemberClass();
    }
    
    String warning {
        return "Watch out!";
    }
    
    void usesSuperAndThis() {
        void use(Object o) {}
        @error use(super);
        use(this);
    }
    
    String str {
        return super.string;
    }
    
    //@error return Classes();
    
}