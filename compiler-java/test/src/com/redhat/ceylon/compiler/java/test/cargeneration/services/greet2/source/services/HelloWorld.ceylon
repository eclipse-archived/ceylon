service(`interface Greeter`)
shared class HelloWorld() 
        satisfies Greeter {
    shared actual String greeting => "hello world"; 
}