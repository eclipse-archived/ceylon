abstract class FormalMembers() { 
    shared formal class Bar(){
        shared formal String baz;
    }
    shared formal String bar();
    @error String brokenHello = Bar().baz;
    @error String brokenHello2 = bar();
    String hello = "Hello World";       
}