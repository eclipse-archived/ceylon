interface Interfaces {
    
    @error Natural count = 0;
    
    @error count++;
    
    formal String name;
    
    @error name = "Gavin";
    
    formal void print(String s);
    
    void printLine(String s) {
        @error print(s + "\n");
    }
    
    String upper(String x) {
        return x.uppercase;
    }
    
    @error print("Hi!");
    
    interface NestedInterface {
        formal String hello;
    }
    
    abstract class NestedAbstractClass() {
        String hello = upper("hello");
        formal Natural times;
    }
    
    class MemberClass() {
        String goodbye = upper("goodbye");
        Natural times = 1;
    }
    
    @error MemberClass();
    
    @error throw;
    
    @error return MemberClass();
    
    MemberClass create() {
        return MemberClass();
    }
    
    String warning {
        return "Watch out!";
    }
    
    void usesSuperAndThis() {
        @error super;
        this;
    }
    
}