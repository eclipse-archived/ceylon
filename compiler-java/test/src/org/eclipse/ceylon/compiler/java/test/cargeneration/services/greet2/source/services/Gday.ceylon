service(`interface Greeter`)
shared class Gday() 
        satisfies Greeter {
    shared actual String greeting => "g'day"; 
}