void stuff() {
    value iter = {"hello", "world"};
    {Integer+} ints1 = iter*.size;
    {String+} strs1 = iter*.clone();
    value seq = ["hello", "world"];
    [String+] strs2 = iter*.clone();
}
