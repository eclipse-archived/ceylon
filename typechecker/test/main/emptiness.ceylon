void test<T>() given T satisfies Null {
    []&{String+} val1 = nothing;
    @type:"Nothing" value val2 = val1 of Nothing;
    String[]&{String+} val3 = ["Hrll"];
    @type:"Sequence<String>" value val4 = val3 of [String+];
    []&Iterable<String,T> val5 = nothing;
    @error value val6 = val5 of Nothing;
    String[]&Iterable<String,T> val7 = ["Hrll"];
    @error value val8 = val7 of [String+];
}
