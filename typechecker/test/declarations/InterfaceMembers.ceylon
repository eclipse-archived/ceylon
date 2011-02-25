interface Interfaces {
    
    @error Natural count = 0;
    
    @error count++;
    
    shared formal String name;
    
    @error name = "Gavin";
    
    shared formal void print(String s);
    
    void callsForward() {
        printLine("Hello world");
    }
    
    void printLine(String s) {
        print(s + "\n");
    }
    
    String upper(String x) {
        return x.uppercase;
    }
    
    @error printLine("Hi!");
    
    interface NestedInterface {
        shared formal String hello;
    }
    
    abstract class NestedAbstractClass() {
        shared String hello = upper("hello");
        shared formal Natural times;
    }
    
    class MemberClass() {
        shared String goodbye = upper("goodbye");
        shared Natural times = 1;
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