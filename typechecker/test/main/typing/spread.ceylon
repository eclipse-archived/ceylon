void stuff() {
    value iter = {"hello", "world"};
    [Integer+] ints1 = iter*.size;
    [String+] strs1 = iter*.clone();
    [String+]() strs1Fun = iter*.clone;
    [Character?+] ok1 = iter*.reduce<Character>((x, y)=>(x.integer+y.integer).character);
    @error value broken1 = iter*.fold("");
    
    value seq = ["hello", "world"];
    [Integer+] ints2 = seq*.size;
    [String+] strs2 = seq*.clone();
    [String+]() strs2Fun = seq*.clone;
    [Character?+] ok2 = seq*.reduce<Character>((x, y)=>(x.integer+y.integer).character);
    @error value broken2 = seq*.fold("");
}
