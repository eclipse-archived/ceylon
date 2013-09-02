class Bug1155_D(String* strings) {
    
    shared actual String string => strings.string;
    
    shared String star(String* seq) 
        => seq.string;
    
    // CallableSpecifier
    String star2(String* seq);
    star2 = star;
    assert("{}" == star2());
    assert("[a]" == star2("a"));
}
Bug1155_D(String*) bug1155_D_ref = Bug1155_D;
void bug1155_D() {
    value x = Bug1155_D();
    // Member Reference
    assert("{}" == Bug1155_D.star(x)());
    assert("[a]" == Bug1155_D.star(x)("a"));
    
    
    assert("{}" == bug1155_D_ref().string);
    assert("[a]" == bug1155_D_ref("a").string);
}
