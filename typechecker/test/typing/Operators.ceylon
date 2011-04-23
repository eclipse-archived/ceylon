class Operators() {
    
    class X() {}
    class Y() {}
    
    class Z() extends Object() {}
    
    @type["String"] local x1 = "Hello" + " " + "World";
    
    @error local x1 = "Hello" + 1;
    
    @type["Float"] local x2 = 1.3 + 2.3;
    
    @type["Float"] local x3 = -2.5;
    
    @type["Float"] local x4 = 2.4 ** 0.5;
    
    @type["Natural"]  local x5 = 1 + 2 - -3;
    
    @type["Float"]  local x6 = 1.0 * 2.5 ** (-0.5);
    
    @type["Natural"] local x7 = 10 % 3;
    @type["Natural"] local x8 = 10 / 3;
    
    @error local x9 = 1 + 1.0;
    
    @error local x10 = 1.0 * 2.5 ** -0.5;
    
    @type["Boolean"] local x11 = !( true || false ) && true;
    
    @type["Boolean"] local x12 = 1 < 100;
    
    @error local x13 = "foo" < 100;
    
    @type["Boolean"] local x14 = "foo" == "bar";
    @type["Boolean"] local x15 = "foo" != "bar";
    
    @error local x16 = "foo" == Z();
    
    @type["Boolean"] local x17 = Y() === X();
    
    @error local x18 = "foo" === 12;
    
    @type["String"] local x19 = $12.34;
    
    X? nothing = null;
    
    @type["Optional<Operators.X>"] local x20 = nothing ? nothing;
    
    @type["Operators.X"] local x21 = nothing ? X();
    
    @type["Operators.X"] local x22 = nothing ? nothing ? X();
    
    @error local x23 = X() ? X();
    
    @error local x24 = X() ? nothing;
    
    @type["Range<Natural>"] local x25 = 1..4;
    
    @error local x26 = 1.2..3.4;
    
    @type["Entry<Natural,Operators.X>"] local x27 = 0->X();
    
    @error local x28 = 0->nothing;
    
    @type["Boolean"] local x29 = nothing exists;
    
    @error local x30 = 1 exists;
    
    @type["Boolean"] local x31 = 1 is Natural;
    
    @error local x32 = nothing is Natural;
    
    @type["Boolean"] local x33 = 1 in {1, 2};
    
    @type["Comparison"] local x34 = 1<=>3;
    @type["Comparison"] local x35 = "foo"<=>"bar";
    @error local x36 = X()<=>X();
    @error local x37 = 1<=>"hello";
    
    X[] sequence = {X(), X()};
    String[]? noSequence = null;
    
    @type["Optional<Operators.X>"] local x38 = sequence[0];
    @type["Sequence<Operators.X>"] local x39 = sequence[0..1];
    @type["Sequence<Operators.X>"] local x40 = sequence[1+1...];
    @type["Optional<Optional<Operators.X>>"] local x41 = {nothing}[0];
    @type["Optional<String>"] local x42 = noSequence?[0];
    
    @error local x43 = sequence["hello"];
    @error local x44 = sequence["hello"...];
    @error local x45 = sequence[1.."hello"];
    
    @type["Sequence<Sequence<Character>>"] local x46 = {"hello", "world"}[].characters;
    @type["Sequence<String>"] local x47 = {"hello", "world"}[].uppercase;
    @type["Optional<Sequence<Character>>"] local x48 = {"hello", "world"}[0]?.characters;
    @type["Sequence<Sequence<Character>>"] local x49 = {"hello", "world"}[].characters;
    @type["Sequence<Iterable<String>>"] local x50 = {"hello", "world"}[].lines();
    @type["Optional<String>"] local x51 = {"hello", "world"}[0]?.normalize(" #");
    @type["Sequence<String>"] local x52 = {"hello", "world"}[].normalize(" #");
    @type["Optional<String>"] local x53 = noSequence?[0]?.normalize(" #");
    @type["Sequence<Operators.X>"] local x54 = {Operators()}[].X();
    
    variable Natural n := 0;
    @type["Natural"] n++;
    @type["Natural"] ++n;
    @type["Natural"] n+=1;
    @type["Natural"] n/=3;
    
    variable Boolean bb := false;
    @type["Boolean"] bb||=false;
    @type["Boolean"] bb&&=true;
    
    @error (n+1) ++;
    @error ++ (n+1);
    
    Natural const = 0;
    @error const++;
    @error --const;
    @error const+=10;
    
    variable Natural nn;
    @error nn+=1;
    @error nn++;
    
    @error X()[].doIt();
    @error X()?.doIt();

}