
shared void bug7027() {
    {String*} stream = {"xx", "yy"};
    bug7027_2(stream.sequence());
}

void bug7027_2([String*] s){
    assert (is [String+] | [] result = s);
    assert (exists result);
}