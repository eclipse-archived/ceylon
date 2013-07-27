[String, Integer, Float] triple(String s, Integer i, Float f) {
    return [s, i, f];
}

Float add([Float,Float] floats=[1.0, 2.0]) {
    return floats.first+floats.rest.first;
}

void test() {
    @type:"Tuple<String|Integer|Float,String,Tuple<Integer|Float,Integer,Tuple<Float,Float,Empty>>>" 
    value tup = triple("hello", 0, 0.0);
    
    @type:"String" value first = tup.first;
    @type:"Integer" value second = tup.rest.first;
    @type:"Float" value third = tup.rest.rest.first;
    @type:"Empty" value nuthin = tup.rest.rest.rest;
    @type:"Null" value fourth = tup.rest.rest.rest.first;
    
    @type:"Null" value before1 = tup[-1];
    @type:"String" value first1 = tup[0];
    @type:"Integer" value second1 = tup[1];
    @type:"Float" value third1 = tup[2];
    @type:"Null" value fourth1 = tup[3];
    @type:"Null" value fifth1 = tup[4];
    
    [String, String] hibye = ["hello", "goodbye"];
    [String, String] fun() {
        return hibye;
    }
    @type:"String" value hi = hibye.first;
    @type:"String" value bye = hibye.rest.first;
    add([1.0, 2.0]);
    Sequence<String> strings = hibye;
    @type:"Tuple<String,String,Sequential<String>>"
    [String, String*] hibye1 = hibye;
    @type:"Sequential<String>"
    [String*] hibye2 = hibye;
    [String, Integer, Object*] trip = triple("", 0, 0.0);
    value ints = [1,2,3];
    [String,Integer,Integer*] vartup = ["hello", 4, *ints];
    @type:"Null" value v0 = vartup[-1];
    @type:"String" value v1 = vartup[0];
    @type:"Integer" value v2 = vartup[1];
    @type:"Null|Integer" value v3 = vartup[2];
    @type:"Null|Integer" value v4 = vartup[3];
    @type:"Null|Integer" value v5 = vartup[4];
    [] emp1 = {};
    [] emp2 = [];
    Tuple<Integer|Float,Integer,Tuple<Float,Float,[]>> unsugared1 = Tuple(0,Tuple(1.0,[]));
    Tuple<String,String,Tuple<String,String,[]>> unsugared2 = Tuple("hello",Tuple("goodbye",{}));
    [Integer,Float] sugared1 = unsugared1;
    [String,String] sugared2 = unsugared2;
    
    //<String, Integer> t0 = 
    //        (1, 2, "", 4, 5)[2..3];
    [String, Integer, Integer] t1 = 
            [1, 2, "", 4, 5][2...];
    //<String, Integer> t2 = 
    //        (1, 2, "", 4, 5, *"hello")[2..3];
    //<String, Integer, Integer, Character> t3 = 
    //        (1, 2, "", 4, 5, *"hello")[2..5];
    //<String, Integer, Integer, Character, Character, Character, Character> t4 = 
    //        (1, 2, "", 4, 5, *"hello")[2..8];
    [String, Integer, Integer, Character*] t5 = 
            [1, 2, "", 4, 5, *"hello"][2...];
    //<Integer, Integer, String, Integer, Integer> t6 = 
    //        (1, 2, "", 4, 5)[-2..6];
    [Integer, Integer, String, Integer, Integer] t6 = 
            [1, 2, "", 4, 5][-2...];
    
    @error interface R<Element> satisfies Ranged<Integer,Element[]> {
        shared actual Element[] spanTo(Integer to) {
            value end = to;
            return this[0:end+1];
        }
    }
    
    void ft<Element, First, Rest>(Tuple<Element, First, Rest> t) 
            given First satisfies Element
            given Rest satisfies Sequential<Element>{
        Integer end = 0;
        Element[] s1 = t[0:end+1];
        Element[] s2 = t[3:5];
        First f = t[0];
        Element? e = t[3];
    }
    
    [String,Float=,Integer=] tupWithOptionals1 = ["hello"];
    [String,Float=,Integer=] tupWithOptionals2 = ["hello", 1.0];
    [String,Float=,Integer=] tupWithOptionals3 = ["hello", 1.0, 1];
    @error [String,Float=,Integer=] tupWithOptionals4 = [];
    @error [String,Float,Integer=] tupWithOptionals4 = ["hello"];

    [Integer=] noint = [];
    [Integer=] int = [2];

    function func(String s, Integer i=0, Float f=0.0) => 1+i;
    Integer(String, Integer=, Float=) ref1 = func;
    Integer(String, Integer, Float) ref2 = func;
    Integer(String, Integer) ref3 = func;
    Integer(String) ref4 = func;
    
    function var(Float* xs) => 1;
    Integer(Float*) ref5 = var;
    Integer() ref6 = var;
    Integer(Float) ref7 = var;
    Integer(Float, Float) ref8 = var;
    Integer(Float, Float*) ref9 = var;
    
    value tail = [1, "goodbye", 2];
    value headTail = [0, "hello", *tail];
    [Integer,String,Integer,String,Integer] result = headTail;
    @type:"Tuple<Integer|Character,Integer,Sequential<Character>>" 
    value strange = [0, *"hello"];
    
    [String,Integer=,Float*] varlen = ["hello"];
    @type:"String" value elem0 = varlen[0];
    @type:"Null|Integer" value elem1 = varlen[1];
    @type:"Null|Float" value elem2 = varlen[2];
    @type:"Null|Float" value elem3 = varlen[3];
    [String,Integer=,Float*] range0 = varlen[0...];
    [Integer=,Float*] range1 = varlen[1...];
    [Float*] range2 = varlen[2...];
    [Float*] range3 = varlen[3...];
    @error [String,Integer,Float*] rangeError0 = varlen[0...];

    [String,Integer=,Float=] fixedlen = ["hello"];
    @type:"String" value elem0x = fixedlen[0];
    @type:"Null|Integer" value elem1x = fixedlen[1];
    @type:"Null|Float" value elem2x = fixedlen[2];
    @type:"Null" value elem3x = fixedlen[3];
    [String,Integer=,Float=] range0x = fixedlen[0...];
    [Integer=,Float=] range1x = fixedlen[1...];
    [Float=] range2x = fixedlen[2...];
    [] range3x = fixedlen[3...];
    @error [String,Integer,Float=] rangeError0x = fixedlen[0...];
    
    @type:"Iterable<String,Null>" {String*} i1 = {""};
    @type:"Iterable<String,Nothing>" {String+} i2 = {""};
    @type:"Sequence<String>" [String+] s1 = [""];
    @type:"Sequential<String>" [String*] s2 = [""];
    @type:"Tuple<String,String,Sequence<String>>" [String,String+] p1 = ["", *s1];
    @type:"Tuple<String,String,Sequential<String>>" [String,String*] p2 = ["", *s2];
    
    alias Point => [Float,Float];
    Point pt = [0.0,0.0];
    @type:"Float" value ptx = pt[0];
    @type:"Float" value pty = pt[1];
    @type:"Null" value ptz = pt[2];
}

void testRanges() {
    @type:"Sequential<Integer>" value s1 = [1, 2, 3, 4, 5][2..3];
    @type:"Sequential<Integer>" value s2 = [1, 2, 3, 4, 5][2:3];
    @type:"Integer" value e1 = [1, 2, 3, 4, 5][0];
    @type:"Integer" value e2 = [1, 2, 3, 4, 5][4];
    @type:"Null" value e3 = [1, 2, 3, 4, 5][-1];
    @type:"Null" value e4 = [1, 2, 3, 4, 5][5];
    [Integer*] possiblyEmpty = [];
    [Integer+] notEmpty = [1];
    @type:"Integer" value v1 = notEmpty[0];
    @type:"Null|Integer" value v2 = notEmpty[1];
    @type:"Null" value v3 = notEmpty[-1];
    @type:"Null|Integer" value v4 = possiblyEmpty[0];
    @type:"Null|Integer" value v5 = possiblyEmpty[1];
    @type:"Null" value v6 = possiblyEmpty[-1];
    @type:"Integer" value v7 = [1,*possiblyEmpty][0];
    @type:"Null|Integer" value v8 = [1,*possiblyEmpty][1];
    @type:"Null" value v9 = [1,*possiblyEmpty][-1];
    @type:"Integer" value v10 = [1,*notEmpty][0];
    @type:"Integer" value v11 = [1,*notEmpty][1];
    @type:"Null|Integer" value v13 = [1,*notEmpty][2];
    @type:"Null" value v12 = [1,*notEmpty][-1];
    @type:"Sequential<Integer>" value t4 = [*possiblyEmpty][0...];
    @type:"Sequential<Integer>" value t5 = [*possiblyEmpty][1...];
    @type:"Tuple<Integer,Integer,Sequential<Integer>>" value t6 = [1,*possiblyEmpty][0...];
    @type:"Sequential<Integer>" value t7 = [1,*possiblyEmpty][1...];
    @type:"Sequence<Integer>" value t8 = [*notEmpty][0...];
    @type:"Sequential<Integer>" value t9 = [*notEmpty][1...];
    @type:"Sequential<Integer>" value t10 = [*notEmpty][2...];
    @type:"Tuple<Integer,Integer,Sequence<Integer>>" value t11 = [1,*notEmpty][0...];
    @type:"Sequence<Integer>" value t12 = [1,*notEmpty][1...];
    @type:"Sequential<Integer>" value t13 = [2,*notEmpty][2...];
    @type:"Sequence<Integer>" value t14 = notEmpty[0...];
    @type:"Sequential<Integer>" value t15 = notEmpty[1...];
    @type:"Sequential<Integer>" value t16 = possiblyEmpty[0...];
    @type:"Sequential<Integer>" value t17 = possiblyEmpty[1...];
}