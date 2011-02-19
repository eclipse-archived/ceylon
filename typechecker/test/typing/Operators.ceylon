class Operators() {
    
    class X() {}
    class Y() {}
    
    @type["String"] "Hello" + " " + "World";
    
    @type["Natural"]  1 + 2 - -3;
    
    @type["Float"]  1.0 * 2.5 ** (-0.5);
    
    @error 1.0 * 2.5 ** -0.5;
    
    @type["Boolean"]  !( true || false ) && true;
    
    @type["Boolean"]  1 < 100;
    
    @type["Boolean"]  "foo" == "bar";
    
    @error "foo" == 12;
    
    @type["Boolean"] Y() === X();
    
    @error "foo" === 12;
    
    @type["String"] $12.34;

}