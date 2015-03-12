
@noanno
class ConstructorRef {
    shared actual String string;
    shared new (String s) {
        string="default(``s``)";
    }
    shared new Unary(String s) {
        string="unary(``s``)";
    }
    shared new Binary(String s1, String s2) {
        string="binary(``s1``,``s2``)";
    }
    shared new Ternary(String s1, String s2, String s3) {
        string="ternary(``s1``,``s2``,``s3``)";
    }
    shared new Nary(String s1, String s2, String s3, String s4) {
        string="nary(``s1``,``s2``,``s3``,``s4``)";
    }
    //defaulted
    shared new UnaryDefaulted(String s="s") { 
        string = "unaryDefaulted(``s``)";
    }
    shared new BinaryDefaulted(String s1="s1", String s2="s2") { 
        string = "binaryDefaulted(``s1``, ``s2``)";
    }
    shared new TernaryDefaulted(String s1="s1", String s2="s2", String s3="s3") { 
        string = "ternaryDefaulted(``s1``, ``s2``, ``s3``)";
    }
    shared new NaryDefaulted(String s1="s1", String s2="s2", String s3="s3", String s4="s4") { 
        string = "naryDefaulted(``s1``, ``s2``, ``s3``, ``s4``)";
    }
}
class ConstructorRefTp<T> 
        given T satisfies Object {
    shared actual String string;
    //type param
    shared new (T s) {
        string="defaultTp(``s``)";
    }
    shared new UnaryTp(T s) {
        string="unaryTp(``s``)";
    }
    shared new BinaryTp(T s1, T s2) {
        string="binaryTp(``s1``,``s2``)";
    }
    shared new TernaryTp(T s1, T s2, T s3) {
        string="ternaryTp(``s1``,``s2``,``s3``)";
    }
    shared new NaryTp(T s1, T s2, T s3, T s4) {
        string="naryTp(``s1``,``s2``,``s3``,``s4``)";
    }
}
@noanno
void constructorRef() {
    ConstructorRef(String) default = package.ConstructorRef;
    assert("default(s1)"==default("s1").string);
    ConstructorRef(String) default2 = ConstructorRef;
    assert("default(s2)"==default2("s2").string);
    
    ConstructorRef(String) unary = package.ConstructorRef.Unary;
    assert("unary(s2)"==unary("s2").string);
    ConstructorRef(String) unary2 = ConstructorRef.Unary;
    assert("unary(s3)"==unary2("s3").string);
    ConstructorRef(String,String) binary= ConstructorRef.Binary;
    assert("binary(s2,s3)"==binary("s2", "s3").string);
    ConstructorRef(String,String,String) ternary= ConstructorRef.Ternary;
    assert("ternary(s2,s3,s4)"==ternary("s2", "s3", "s4").string);
    ConstructorRef(String,String,String,String) nary= ConstructorRef.Nary;
    assert("nary(s2,s3,s4,s5)"==nary("s2", "s3", "s4", "s5").string);
    
    ConstructorRefTp<String>(String) defaultTp = package.ConstructorRefTp<String>;
    assert("defaultTp(s1)"==defaultTp("s1").string);
    ConstructorRefTp<String>(String) unaryTp = package.ConstructorRefTp<String>.UnaryTp;
    assert("unaryTp(s2)"==unaryTp("s2").string);
    ConstructorRefTp<String>(String, String) binaryTp = package.ConstructorRefTp<String>.BinaryTp;
    assert("binaryTp(s2,s3)"==binaryTp("s2", "s3").string);
    ConstructorRefTp<String>(String,String,String) ternaryTp = package.ConstructorRefTp<String>.TernaryTp;
    assert("ternaryTp(s2,s3,s4)"==ternaryTp("s2", "s3", "s4").string);
    ConstructorRefTp<String>(String,String,String,String) naryTp = package.ConstructorRefTp<String>.NaryTp;
    assert("naryTp(s2,s3,s4,s5)"==naryTp("s2", "s3", "s4", "s5").string);
    
}