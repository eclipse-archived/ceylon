shared void bug2316() {
    interface A {
        String ident() => "A";
        shared default String identS() => "As";
        shared interface B satisfies A {
            String ident() => "B";
            shared actual String identS() => "Bs";
            
            shared String outerIdent() => outer.ident();        // ok
            shared String identA1() => super.ident();           // error
            shared String identA2() => (super of A).ident();    // error
            shared String identB() => ident();                  // ok
            
            shared String outerIdentS() => outer.identS();      // ok
            shared String identA1S() => super.identS();         // error
            shared String identA2S() => (super of A).identS();  // error
            shared String identBS() => identS();                // ok
            
        }
        shared B b => object satisfies B {};
    }
    object a satisfies A {}
    
    print(a.b.outerIdent());
    print(a.b.identA1());
    print(a.b.identA2());
    print(a.b.identB());
    
    print(a.b.outerIdentS());
    print(a.b.identA1S());
    print(a.b.identA2S());
    print(a.b.identBS());
}