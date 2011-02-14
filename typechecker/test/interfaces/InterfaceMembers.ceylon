interface Interfaces {
    
    class String() {
        shared String uppercase {
            return this;
        }
    }
    class Natural() {}
    
    @error Natural count = 0;
    
    @error count++;
    
    formal String name;
    
    @error name = "Gavin";
    
    formal void print(String s);
    
    void printLine(String s) {
        @error print(s + "\n"); //not really an error, just a todo
    }
    
    String upper(String x) {
        return x.uppercase;
    }
    
    @error print("Hi!");
    
    interface NestedInterface {
        formal String hello;
    }
    
    abstract class NestedAbstractClass() {
        String hello = upper("Hello");
        formal Natural times;
    }
    
    class MemberClass() {
        String hello = upper("Hello");
        Natural times = 1;
    }
    
    @error MemberClass();
    
    @error throw;
    
    @error return MemberClass();
    
    MemberClass create() {
        return MemberClass();
    }
    
}