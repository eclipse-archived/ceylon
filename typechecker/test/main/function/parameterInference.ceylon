void parameterInference() {
    @type:"Iterable<Character,Null>" value mapped = "hello world".map((c) => c.uppercased);
    @type:"Iterable<Character,Null>" value filtered = "hello world".filter((c) { return !c.whitespace; });
    @type:"Integer" value folded = (1..10).fold(0, (Integer r, x) => r+x);
    @type:"Integer" value folded2 = (1..10).fold(0, (r, x) { Integer r; return r+x; });
}