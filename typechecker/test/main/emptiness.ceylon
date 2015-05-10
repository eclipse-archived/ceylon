void test<T>() given T satisfies Null {
    []&{String+} val1 = nothing;
    @type:"Nothing" value val2 = val1 of Nothing;
    String[]&{String+} val3 = ["Hrll"];
    @type:"Sequence<String>" value val4 = val3 of [String+];
    []&Iterable<String,T> val5 = nothing;
    @error value val6 = val5 of Nothing;
    String[]&Iterable<String,T> val7 = ["Hrll"];
    @error value val8 = val7 of [String+];
    String&[String*] val9 = nothing;
    @type:"Nothing" value val10 = val9;
    Integer&[Integer*] val11 = nothing;
    @type:"Nothing" value val12 = val11;
    Correspondence<Integer,Float>&[Float*] val13 = nothing;
    @type:"Sequential<Float>" value val14 = val13;
    List<Float>&[Float+] val15 = nothing;
    @type:"Sequence<Float>" value val16 = val15;
}

void disjointness({String+} strings, [Integer+]|Integer ints) {
    @error switch (strings)
    case (is []) {}
    else {}
    @error {String+} sss = [];
    switch (ints)
    case (is Integer) {}
    case (is [Integer+]) {}
}
