class Classes() {
    
    Natural count = 0;
    
    count++;
    
    String name;
    name = "Gavin";
    
    formal Boolean shouldRetry;
    
    formal void print(String s);
    
    void callsForward() {
        @error printLine("Hello world");
    }
    
    void printLine(String s) {
        @error print(s + "\n");
    }
    
    String upper(String x) {
        return x.uppercase;
    }
    
    printLine("Hi!");
    
    @error x = 0;
    @error y := 0.0;
    
    interface NestedInterface {
        formal String hello;
    }
    
    abstract class NestedAbstractClass() {
        String hello = upper("hello");
        formal Natural times;
    }
    
    class MemberClass() {
        shared String goodbye = upper("goodbye");
        Natural times = 1;
    }
    
    MemberClass();
        
    @type["String"] MemberClass().goodbye;
    @error MemberClass().goodbye = "Foo";
    @error MemberClass().goodbye := "Foo";
    
    MemberClass create() {
        return MemberClass();
    }
    
    String warning {
        return "Watch out!";
    }
    
    void usesSuperAndThis() {
        super;
        this;
    }
    
    @error return Classes();
    
}