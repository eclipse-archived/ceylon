service(`interface Greeter`)
shared class Bonjour() 
        satisfies Greeter {
    shared actual String greeting => "bonjour"; 
}