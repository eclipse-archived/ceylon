class Operators() {
    
    class X() {}
    class Y() {}
    
    @type["String"] "Hello" + " " + "World";
    
    @error "Hello" + 1;
    
    @type["Float"] 1.3 + 2.3;
    
    @type["Float"] -2.5;
    
    @type["Float"] 2.4 ** 0.5;
    
    @type["Natural"]  1 + 2 - -3;
    
    @type["Float"]  1.0 * 2.5 ** (-0.5);
    
    @type["Natural"] 10 % 3;
    @type["Natural"] 10 / 3;
    
    @error 1 + 1.0;
    
    @error 1.0 * 2.5 ** -0.5;
    
    @type["Boolean"]  !( true || false ) && true;
    
    @type["Boolean"]  1 < 100;
    
    @error "foo" < 100;
    
    @type["Boolean"]  "foo" == "bar";
    @type["Boolean"]  "foo" != "bar";
    
    @error "foo" == 12;
    
    @type["Boolean"] Y() === X();
    
    @error "foo" === 12;
    
    @type["String"] $12.34;
    
    X? nothing = null;
    
    @type["Optional<X>"] nothing ? nothing;
    
    @type["X"] nothing ? X();
    
    @type["X"] nothing ? nothing ? X();
    
    @error X() ? X();
    
    @error X() ? nothing;
    
    @type["Range<Natural>"] 1..4;
    
    @error 1.2..3.4;
    
    @type["Entry<Natural,X>"] 0->X();
    
    @error 0->nothing;
    
    @type["Boolean"] nothing exists;
    
    @error 1 exists;
    
    @type["Boolean"] 1 is Natural;
    
    @error nothing is Natural;
    
    @type["Boolean"] 1 in {1, 2};
    
    @type["Comparison"] 1<=>3;
    @type["Comparison"] "foo"<=>"bar";
    @error X()<=>X();
    @error 1<=>"hello";
    
    X[] sequence = {X(), X()};
    String[]? noSequence = null;
    
    @type["Optional<X>"] sequence[0];
    @type["Sequence<X>"] sequence[0..1];
    @type["Sequence<X>"] sequence[1+1...];
    @type["Optional<Optional<X>>"]{nothing}[0];
    @type["Optional<String>"] noSequence?[0];
    
    @error sequence["hello"];
    @error sequence["hello"...];
    @error sequence[1.."hello"];
    
    @type["Sequence<Sequence<Character>>"] {"hello", "world"}[].chars;
    @type["Sequence<String>"] {"hello", "world"}[].uppercase;
    @type["Optional<Sequence<Character>>"] {"hello", "world"}[0]?.chars;
    @type["Sequence<Sequence<Character>>"] {"hello", "world"}[].chars;
    @type["Sequence<Iterable<String>>"] {"hello", "world"}[].lines();
    @type["Optional<String>"] {"hello", "world"}[0]?.normalize(" #");
    @type["Sequence<String>"] {"hello", "world"}[].normalize(" #");
    @type["Optional<String>"] noSequence?[0]?.normalize(" #");
    @type["Sequence<X>"] {Operators()}[].X();
    
    variable Natural n := 0;
    @type["Natural"] n++;
    @type["Natural"] ++n;
    @type["Natural"] n+=1;
    @type["Natural"] n/=3;
    
    variable Boolean bb := false;
    @type["Boolean"] bb||=false;
    @type["Boolean"] bb&&=true;

}