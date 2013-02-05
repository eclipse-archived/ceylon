@error;
class Operators() {
    
    class X() {}
    class Y() {}
    
    class Z() extends Object() {
        //fake impl
        shared actual String string = "Z()";
        shared actual Boolean equals(Object that) => true;
        shared actual Integer hash => 0;
    }
    
    @type:"String" value x0 = "Hello" + " " + "World";
    
    @error value x1 = "Hello" + 1;
    
    @type:"Float" value x2 = 1.3 + 2.3;
    
    @type:"Integer" value w9 = 1 - 2;
    
    @type:"Integer" value w5 = 1 * 1;
    
    @type:"Float" value x3 = -2.5;
    
    @type:"Float" value w2 = +2.5;
    
    @type:"Integer" value w0 = -1;
    
    @type:"Integer" value w3 = +1;
    
    @type:"Float" value x4 = 2.4 ^ 0.5;
    
    @type:"Integer" value x5 = (1 + 2) - -3;
    
    @type:"Float" value w4 = -3 * 2.5;
    
    @type:"Float"  value x6 = 1.0 * 2.5 ^ (-0.5);
    
    @type:"Integer" value x7 = 10 % 3;
    @type:"Integer" value x8 = 10 / 3;
    
    @error value w1 = 4.0 % 2.0;
    
    @type:"Float" value x9 = 1 + 1.0;
    
    @error value x10 = 1.0 * 2.5 ^ -0.5;
    
    @type:"Boolean" value x11 = !( true || false ) && true;
    
    @type:"Boolean" value x12 = 1 < 100;
    
    @error value x13 = "foo" < 100;
    
    @type:"Boolean" value x14 = "foo" == "bar";
    @type:"Boolean" value x15 = "foo" != "bar";
    
    @error value x16 = "foo" == null;
    
    @type:"Boolean" value x17 = Y() === X();
    
    @error value x18 = "foo" === 12;
    
    @type:"String" value x19 = 12.34.string;
    
    X? none = null;
    
    //@type:"Null|Operators.X" value x20 = none ? none;
    //
    //@type:"Operators.X" value x21 = none ? X();
    //
    //@type:"Operators.X" value x22 = none ? none ? X();
    //
    //@type:"Operators.Y|Operators.X" value x60 = none ? Y();
    //
    //@error value x23 = X() ? X();
    //
    //@error value x24 = X() ? none;
    
    @type:"Range<Integer>" value x25 = 1..4;
    
    @type:"Range<Integer>" value x252 = -10..+10;
    
    value x253 = -1..5;
    
    @error value x26 = 1.2..3.4;
    
    @type:"Entry<Integer,Operators.X>" value x27 = 0->X();
    @type:"Operators.X" value x27item = x27.item;
    @type:"Integer" value x27key = x27.key;
    
    @error value x28 = 0->none;
    
    @type:"Boolean" value x29 = none exists;
    //@type:"Boolean" value x29n = exists none;
    
    @error @type:"Boolean" value x30 = 1 exists;
    //@error @type:"Boolean" value x30n = exists 1;
    
    @error @type:"Boolean" value x73 = null exists;
    //@error @type:"Boolean" value x73n = exists null;
    
    @error @type:"Boolean" value x70 = {} nonempty;
    //@error @type:"Boolean" value x70n = nonempty {};
    @error @type:"Boolean" value x71 = {"hello"} nonempty;
    //@error @type:"Boolean" value x71n = nonempty {"hello"};
    String[] strs = {};
    @type:"Boolean" value x72 = strs nonempty;
    //@type:"Boolean" value x72n = nonempty strs;
    
    Object one = 1;
    @type:"Boolean" value x31 = one is Integer;
    //@type:"Boolean" value x31n = is Integer one;
    @error value x31e = 1 is Integer;
    
    @type:"Boolean" value x32 = none is X;
    //@type:"Boolean" value x32n = is X none;    
    @error value x32e = none is Integer;
    
    @type:"Boolean" value x33 = "hello" in "hello world";
    
    object cat satisfies Category {
        shared actual Boolean contains(Object element) {
            return true;
        }
    }
    
    @type:"Boolean" value x55 = 1 in cat;
    
    Sequence<Integer?> seqopt2 = [ null, 1 ];    
    value x56 = "foo" in seqopt2;
    
    @type:"Comparison" value x34 = 1<=>3;
    @type:"Comparison" value x35 = "foo"<=>"bar";
    @error value x36 = X()<=>X();
    @error value x37 = 1<=>"hello";
    
    X[] sequence = [X(), X()];
    String[]? noSequence = null;
    String[] emp = {};
    
    @type:"Null|Operators.X" value x38 = sequence[0];
    @type:"Sequential<Operators.X>" value x39 = sequence[0..1];
    @type:"Sequential<Operators.X>" value x40 = sequence[1+1...];
    @type:"Null|Operators.X" value x41 = [none][0];
    //@type:"Null|String" value x42 = noSequence?[0];
    @type:"Sequential<Operators.X>" value x39u = sequence[...1];
    
    @error value x43 = sequence["hello"];
    @error value x44 = sequence["hello"*];
    @error value x44u = sequence[*"hello"];
    @error value x45 = sequence[1.."hello"];
    
    String? maybeString = null;
    Callable<Null|Iterable<String>,[Iterable<Character>|Callable<Boolean,[Character]>,Boolean,Boolean]> mss = maybeString?.split;
    Callable<Sequence<Iterable<String>>,[Iterable<Character>|Callable<Boolean,[Character]>,Boolean,Boolean]> hws = ["hello", "world"]*.split;
    
    Sequence<String> helloworld = ["hello", "world"];
    @type:"Sequential<String>" value e45 = emp*.uppercased;
    @type:"Sequence<Sequential<Character>>" value x46 = helloworld*.characters;
    @type:"Sequence<String>" value x47 = helloworld*.uppercased;
    @type:"Null|Sequential<Character>" value x48 = helloworld[1]?.characters;
    @type:"Sequence<Sequential<Character>>" value x49 = helloworld*.characters;
    @type:"Sequence<Iterable<String,Null>>" value x50 = helloworld*.lines;
    @type:"Null|String" value x51 = helloworld[1]?.normalized;
    @type:"Null|Iterable<String,Null>" value x512 = helloworld[1]?.split((Character c) => c==' ');
    @type:"Sequence<String>" value x52 = helloworld*.normalized;
    @type:"Sequence<Iterable<String,Null>>" value x522 = helloworld*.split((Character c) => c==' ');
    //@type:"Null|String" value x53 = noSequence?[0]?.normalized;
    //@type:"Null|Iterable<String,Null>" value x532 = noSequence?[0]?.split((Character c) => c==' ');
    @type:"Sequence<Operators.X>" value x54 = [Operators()]*.X();

    {String*} onetwo = {"one", "two"};
    @type:"Sequential<String>" value x61 = onetwo*.uppercased;
    @type:"Sequential<Sequential<Character>>" value x62 = onetwo*.characters;
    @type:"Sequential<Iterable<String,Null>>" value x63 = onetwo*.split((Character c) => c==' ');
    
    @type:"Sequential<Operators.X>" value s1 = sequence[1...];
    @type:"Sequential<Operators.X>" value s2 = sequence[...2];
    @type:"Sequential<Operators.X>" value s3 = sequence[1..2];
    @type:"Sequential<Operators.X>" value s4 = sequence[1:2];
    
    variable Integer n = 0;
    @type:"Integer" n++;
    @type:"Integer" ++n;
    @type:"Integer" n+=1;
    @type:"Integer" n/=3;
    @type:"Integer" n*=2;
    n*=-2;
    @error n+=1.0;
    n+=-1;
    n-=1;
    
    variable Float f=0.0;
    @type:"Float" f+=1.0;
    @type:"Float" f+=1;
    @type:"Float" f-=4;
    @type:"Float" f/=4.0;
    @type:"Float" f*=2;
    @type:"Float" f/=-2;
    @type:"Float" f+=-1;
    
    variable Boolean bb = false;
    @type:"Boolean" bb||=false;
    @type:"Boolean" bb&&=true;
    
    @error (n+1) ++;
    @error ++ (n+1);
    
    Integer const = 0;
    @error const++;
    @error --const;
    @error const+=10;
    
    variable Integer nn;
    @error nn+=1;
    @error nn++;
    
    String-><Boolean->String> ent = "hello"->(true->"hello");
    
    @error X()*.doIt();
    @error X()?.doIt();
    
    @type:"Empty" value es = {};
    
    @type:"Null" value nnnn = es[0];
    Null nnnn2 = nnnn;
    
    String?[] nullhelloworld = [ null, "hello", "world" ];
    @type:"Null|String" value ns = nullhelloworld[1];
    String? ns2 = ns;
    
    <Null|String|Float>[] nullhelloone = [ null, "hello", 1.0 ];
    @type:"Null|String|Float" value nsf = nullhelloone[1];
    String|Float? nsf2 = ns;
    
    @type:"Tuple<Integer|Float,Integer,Tuple<Integer|Float,Integer,Tuple<Float|Integer,Float,Tuple<Integer,Integer,Empty>>>>" 
    value ins = [ -10, -1, 1.0, 3 ];
    @type:"Float" value ion = ins[2];
    String ions = ion.string;
    Sequence<Integer|Float> ins2 = ins;
    Null|Integer|Float ion2 = ion;
    Integer?|Float? ion3 = ion;
    Null|Integer|Float ion4 { return ion2; }
    Null|Integer|Float ion5 { return ion3; }
    Integer|Float defaultIon(Null|Integer|Float x) {
        if (exists x) {
            return x;
        }
        else {
            return 0;
        }
    }
    Integer|Float defaultIon2(Null|Integer|Float x) {
        return x else 0;
    }

    Boolean b1 = true;
    Boolean b2 = false;
    Boolean b3 = b1 && b2;
    variable Boolean b4 = b2 || b3;
    variable Boolean b5 = !b4;
    b4 &&= b3;
    b5 ||= b4;
    
    @type:"Entry<String,Float>" String->Float esf = "hello"->1.0;
    @type:"Sequence<Entry<String,Float>>" Sequence<String->Float> esfs = [esf];
    
    String->Object okEntry;   
    @error String->Anything brokenEntry1;
    @error Null->String brokenEntry2;
    
    @error value brokenEntry3 = "hello"->null;
    @error value brokenEntry4 = null->"hello";
    
    @type:"Entry<String,Float|Integer>" 
    String->Float|Integer okEntry2 = "hello"->(true then 1.0 else 1);

    Float x=1.0;
    Float result = x>0.0 then x else 0.0;
    
    //@error String str1 = null ? null ? "hello";
    @error String str2 = null else null else "hello";

    String? nostring = null;
    //@type:"String" value str3 = nostring ? nostring ? "hello";
    @type:"String" value str4 = nostring else nostring else "hello";
    
    Anything vd = null;
    //@type:"Object" value vd1 = vd ? 1;
    //@type:"Object" value vd2 = vd ? "hello";
    @type:"Object" value vd1 = vd else 1;
    @type:"Object" value vd2 = vd else "hello";
    
    Float? nof = null;
    //Float ff = nof?1.0 + nof?2.0;
    Float ff = (nof else 1.0) + (nof else 2.0);
    
    variable Ordinal<Integer> oi=0;
    oi++;
    --oi;
    variable Numeric<Integer> ai=0;
    ai+=1;
    ai=ai+1;
    ai=ai-1;
    
    void testStef<T>(T t) given T satisfies Integer {

        variable T oi=t;
        @error oi++;
        @error --oi;
        variable T ai=t;
        @error ai+=1;
        @error ai=ai+1;
        @error ai=ai-1;

    }
    
    Set<String> ss = nothing;
    Set<Integer> si = nothing;
    Set<Object> so = nothing;
    @type:"Set<String|Integer>" value sr0 = ss|si;
    @type:"Set<Nothing>" value sr2 = ss&si;
    @type:"Set<String>" value sr3 = ss~si;
    @type:"Set<String>" value sr4 = ss&so;
    @type:"Set<Object>" value sr5 = ss|so;
    
    variable Set<String> vss = ss;
    @type:"Set<String>" vss|=ss;
    @type:"Set<String>" vss&=ss;
    
    variable Object anythingAtAll = 0;
    @type:"Integer" value somethingSpecial = anythingAtAll = 1;
    @type:"Singleton<Float>" Singleton(anythingAtAll = 1.0);
    
    {String(Float)*} funcs = { (Float x) => x.string.lowercased, 
                               (Float y) => y.string.uppercased };
    @error {String*} results1 = funcs(1.0);
    
    {Float*} floats = { 1.0, 2.0 };
    [Float*] results2 = floats*.plus(1.0);
    @type:"Callable<Sequential<Float>,Tuple<Float,Float,Empty>>" 
    value ref1 = floats*.plus;
    Float[](Float) ref2 = floats*.times;
    
    Float? floatOrNull = null;
    Float? result1 = floatOrNull?.plus(1.0);
    @type:"Callable<Null|Float,Tuple<Float,Float,Empty>>" 
    value ref3 = floatOrNull?.plus;
    Float?(Float) ref4 = floatOrNull?.times;
    
    {Operators*} opers = { Operators(), Operators() };
    X[] exes = opers*.X();
    X[]() refs = opers*.X; 
    
    String string = "";
    Character? c = string[0];
    Null nl = string[-1];
    variable String sync;
    sync = string[0..1];
    sync = string[0:1];
    sync = string[0...];
    sync = string[1...];
    sync = string[...1];
    @type:"String" value syncit = string[0...];
    
    Integer bin1 = $1010_0101;
    Integer bin2 = $1111_0000;
    Integer bin3 = bin1 and (bin1 not);
        
}
