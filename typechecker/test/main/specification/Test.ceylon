class Test() {
    void print(String s) {}
    @error Inner();
    @error method();
    @error print(attribute);
    String attribute { 
        Inner();
        method();
        @error print(attribute);
        return "hello"; 
    }
    class Inner() {
        Inner();
        method();
        print(attribute);
    }
    void method() {
        Inner();
        method();
        print(attribute);
    }
}

abstract class WithFormals() {
    shared formal String name;
    @error name = "Ceylon";
    shared formal Integer count;
    @error count => 0;
    shared formal variable Float price;
    @error print(price=0.0);
}

abstract class CcCc() {
    shared formal variable String name;
    @error this.name = "";
    @error print(this.name = "");
}

abstract class DdDd() {
    shared String name = "";
    @error this.name = "";
    @error print(this.name = "");
}

interface InterfaceAttributeWithConflictingMethods {
    shared formal variable String attr;
    shared void m(){
        attr = attr;
        print(attr = attr);
        this.attr = this.attr;
    } 
}

class ClassAttributeWithConflictingMethods() {
    shared String attr="";
    shared void m(){
        @error attr = attr;
        @error print(attr = attr);
        @error this.attr = this.attr;
    } 
}
