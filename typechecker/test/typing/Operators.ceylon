class Operators() {
    
    class X() {}
    class Y() {}
    
    class Z() extends Object() {}
    
    @type["String"] local x0 = "Hello" + " " + "World";
    
    @error local x1 = "Hello" + 1;
    
    @type["Float"] local x2 = 1.3 + 2.3;
    
    @type["Float"] local x3 = -2.5;
    
    @type["Float"] local w2 = +2.5;
    
    @type["Integer"] local w0 = -1;
    
    @type["Integer"] local w3 = +1;
    
    @type["Float"] local x4 = 2.4 ** 0.5;
    
    @type["Integer"] local x5 = (1 + 2) - -3;
    
    @type["Float"] local w4 = -3 * 2.5;
    
    @type["Float"]  local x6 = 1.0 * 2.5 ** (-0.5);
    
    @type["Natural"] local x7 = 10 % 3;
    @type["Natural"] local x8 = 10 / 3;
    
    @error local w1 = 4.0 % 2.0;
    
    @type["Float"] local x9 = 1 + 1.0;
    
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
    
    @type["Nothing|Operators.X"] local x20 = nothing ? nothing;
    
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
    
    object cat satisfies Category {
        shared actual Boolean contains(Object obj) {
            return true;
        }
    }
    
    @type["Boolean"] local x55 = 1 in cat;
    
    Sequence<Natural?> seqopt2 = { null, 1 };    
    @error local x56 = "foo" in seqopt2;
    
    @type["Comparison"] local x34 = 1<=>3;
    @type["Comparison"] local x35 = "foo"<=>"bar";
    @error local x36 = X()<=>X();
    @error local x37 = 1<=>"hello";
    
    X[] sequence = {X(), X()};
    String[]? noSequence = null;
    
    @type["Nothing|Operators.X"] local x38 = sequence[0];
    @type["Sequence<Operators.X>"] local x39 = sequence[0..1];
    @type["Sequence<Operators.X>"] local x40 = sequence[1+1...];
    @type["Nothing|Operators.X"] local x41 = {nothing}[0];
    @type["Nothing|String"] local x42 = noSequence?[0];
    
    @error local x43 = sequence["hello"];
    @error local x44 = sequence["hello"...];
    @error local x45 = sequence[1.."hello"];
    
    @type["Sequence<Empty|Sequence<Character>>"] local x46 = {"hello", "world"}[].characters;
    @type["Sequence<String>"] local x47 = {"hello", "world"}[].uppercase;
    @type["Nothing|Empty|Sequence<Character>"] local x48 = {"hello", "world"}[0]?.characters;
    @type["Sequence<Empty|Sequence<Character>>"] local x49 = {"hello", "world"}[].characters;
    @type["Sequence<Iterable<String>>"] local x50 = {"hello", "world"}[].lines();
    @type["Nothing|String"] local x51 = {"hello", "world"}[0]?.normalize(" #");
    @type["Sequence<String>"] local x52 = {"hello", "world"}[].normalize(" #");
    @type["Nothing|String"] local x53 = noSequence?[0]?.normalize(" #");
    @type["Sequence<Operators.X>"] local x54 = {Operators()}[].X();
    
    variable Natural n := 0;
    @type["Natural"] n++;
    @type["Natural"] ++n;
    @type["Natural"] n+=1;
    @type["Natural"] n/=3;
    @error n+=1.0;
    @error n+=-1;
    
    variable Float f:=0.0;
    @type["Float"] f+=1.0;
    @type["Float"] f+=1;
    @type["Float"] f-=4;
    @type["Float"] f+=-1;
    
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
    
    @type["Empty"] local es = {};
    
    @type["Nothing"] local nnnn = es[0];
    Nothing nnnn2 = nnnn;
    
    @type["Nothing|String"] local ns = { null, "hello", "world" }[1];
    String? ns2 = ns;
    
    @type["Sequence<Integer|Natural>"] local ins = { -10, -1, 1, 3 };
    @type["Nothing|Integer|Natural"] local ion = ins[2];
    @error String ions = ion.string;
    Sequence<Integer|Natural> ins2 = ins;
    Nothing|Integer|Natural ion2 = ion;
    Integer?|Natural? ion3 = ion;
    Nothing|Integer|Natural ion4 { return ion2; }
    Nothing|Integer|Natural ion5 { return ion3; }
    Integer|Natural defaultIon(Nothing|Integer|Natural x) {
        if (exists x) {
            return x;
        }
        else {
            return 0;
        }
    }
    //we could make this work by making x?y have type X|Y where x is type X? and y is type Y
    /*Integer|Natural defaultIon2(Nothing|Integer|Natural x) {
        return x?0;
    }*/

}