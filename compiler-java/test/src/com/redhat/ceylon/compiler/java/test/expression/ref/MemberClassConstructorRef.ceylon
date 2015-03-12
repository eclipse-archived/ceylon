class MemberClassConstructorRef() {
    shared class Simple {
        shared actual String string;
        shared new (String s1) {
            string = "Simple.Simple(``s1``)";
        }
        shared new Nullary() {
            string = "Simple.Nullary()";
        }
        shared new Unary(String s1) {
            string = "Simple.Unary(``s1``)";
        }
        shared new Binary(String s1, String s2) {
            string = "Simple.Binary(``s1``,``s2``)";
        }
        shared new Ternary(String s1, String s2, String s3) {
            string = "Simple.Ternary(``s1``,``s2``,``s3``)";
        }
        shared new Nary(String s1, String s2, String s3, String s4) {
            string = "Simple.Nary(``s1``,``s2``,``s3``,``s4``)";
            
        }
    }
    shared void simple() {
        variable Simple(String)(MemberClassConstructorRef) defRef = MemberClassConstructorRef.Simple;
        assert(defRef(this)("s1").string == "Simple.Simple(s1)");
        defRef = MemberClassConstructorRef.Simple;
        assert(defRef(this)("s1").string == "Simple.Simple(s1)");
        Simple(String) defRef2 = Simple;
        assert(defRef2("s1").string == "Simple.Simple(s1)");
        Simple()(MemberClassConstructorRef) nullaryRef = MemberClassConstructorRef.Simple.Nullary;
        assert(nullaryRef(this)().string == "Simple.Nullary()");
        Simple() nullaryRef2 = Simple.Nullary;
        assert(nullaryRef2().string == "Simple.Nullary()");
        Simple(String)(MemberClassConstructorRef) unaryRef = MemberClassConstructorRef.Simple.Unary;
        assert(unaryRef(this)("s1").string == "Simple.Unary(s1)");
        Simple(String,String)(MemberClassConstructorRef) binaryRef = MemberClassConstructorRef.Simple.Binary;
        assert(binaryRef(this)("s1", "s2").string == "Simple.Binary(s1,s2)");
        Simple(String,String,String)(MemberClassConstructorRef) ternaryRef = MemberClassConstructorRef.Simple.Ternary;
        assert(ternaryRef(this)("s1", "s2", "s3").string == "Simple.Ternary(s1,s2,s3)");
        Simple(String,String,String,String)(MemberClassConstructorRef) naryRef = MemberClassConstructorRef.Simple.Nary;
        assert(naryRef(this)("s1", "s2", "s3", "s4").string == "Simple.Nary(s1,s2,s3,s4)");
    }
    shared class Defaulted {
        shared actual String string;
        shared new (String s1="s1") {
            string = "Defaulted.Defaulted(``s1``)";
        }
        shared new Nullary() {
            string = "Defaulted.Nullary()";
        }
        shared new Unary(String s1="s1") {
            string = "Defaulted.Unary(``s1``)";
        }
        shared new Binary(String s1="s1", String s2="s2") {
            string = "Defaulted.Binary(``s1``,``s2``)";
        }
        shared new Ternary(String s1="s1", String s2="s2", String s3="s3") {
            string = "Defaulted.Ternary(``s1``,``s2``,``s3``)";
        }
        shared new Nary(String s1="s1", String s2="s2", String s3="s3", String s4="s4") {
            string = "Defaulted.Nary(``s1``,``s2``,``s3``,``s4``)";
            
        }
    }
    shared void defaulted() {
        variable Defaulted(String=)(MemberClassConstructorRef) defRef = MemberClassConstructorRef.Defaulted;
        assert(defRef(this)().string == "Defaulted.Defaulted(s1)");
        assert(defRef(this)("S1").string == "Defaulted.Defaulted(S1)");
        defRef = MemberClassConstructorRef.Defaulted;
        assert(defRef(this)().string == "Defaulted.Defaulted(s1)");
        assert(defRef(this)("S1").string == "Defaulted.Defaulted(S1)");
        Defaulted(String=) defRef2 = Defaulted;
        assert(defRef2().string == "Defaulted.Defaulted(s1)");
        assert(defRef2("S1").string == "Defaulted.Defaulted(S1)");
        Defaulted()(MemberClassConstructorRef) nullaryRef = MemberClassConstructorRef.Defaulted.Nullary;
        assert(nullaryRef(this)().string == "Defaulted.Nullary()");
        Defaulted() nullaryRef2 = Defaulted.Nullary;
        assert(nullaryRef2().string == "Defaulted.Nullary()");
        Defaulted(String=)(MemberClassConstructorRef) unaryRef = MemberClassConstructorRef.Defaulted.Unary;
        assert(unaryRef(this)("s1").string == "Defaulted.Unary(s1)");
        assert(unaryRef(this)("S1").string == "Defaulted.Unary(S1)");
        Defaulted(String=,String=)(MemberClassConstructorRef) binaryRef = MemberClassConstructorRef.Defaulted.Binary;
        assert(binaryRef(this)("s1", "s2").string == "Defaulted.Binary(s1,s2)");
        Defaulted(String=,String=,String=)(MemberClassConstructorRef) ternaryRef = MemberClassConstructorRef.Defaulted.Ternary;
        assert(ternaryRef(this)("s1", "s2", "s3").string == "Defaulted.Ternary(s1,s2,s3)");
        Defaulted(String=,String=,String=,String=)(MemberClassConstructorRef) naryRef = MemberClassConstructorRef.Defaulted.Nary;
        assert(naryRef(this)("s1", "s2", "s3", "s4").string == "Defaulted.Nary(s1,s2,s3,s4)");
    }
    shared class Sequenced {
        shared actual String string;
        shared new (String* s1) {
            string = "Sequenced.Sequenced(``s1``)";
        }
        shared new Nullary() {
            string = "Sequenced.Nullary()";
        }
        shared new Unary(String* s1) {
            string = "Sequenced.Unary(``s1``)";
        }
        shared new Binary(String s1, String* s2) {
            string = "Sequenced.Binary(``s1``,``s2``)";
        }
        shared new Ternary(String s1, String s2, String* s3) {
            string = "Sequenced.Ternary(``s1``,``s2``,``s3``)";
        }
        shared new Nary(String s1, String s2, String s3, String* s4) {
            string = "Sequenced.Nary(``s1``,``s2``,``s3``,``s4``)";
            
        }
    }
    shared void sequenced() {
        variable Sequenced(String*)(MemberClassConstructorRef) defRef = MemberClassConstructorRef.Sequenced;
        assert(defRef(this)("s1").string == "Sequenced.Sequenced([s1])");
        assert(defRef(this)().string == "Sequenced.Sequenced([])");
        defRef = MemberClassConstructorRef.Sequenced;
        assert(defRef(this)("s1").string == "Sequenced.Sequenced([s1])");
        assert(defRef(this)().string == "Sequenced.Sequenced([])");
        Sequenced(String*) defRef2 = Sequenced;
        assert(defRef2("s1").string == "Sequenced.Sequenced([s1])");
        assert(defRef2().string == "Sequenced.Sequenced([])");
        Sequenced()(MemberClassConstructorRef) nullaryRef = MemberClassConstructorRef.Sequenced.Nullary;
        assert(nullaryRef(this)().string == "Sequenced.Nullary()");
        Sequenced() nullaryRef2 = Sequenced.Nullary;
        assert(nullaryRef2().string == "Sequenced.Nullary()");
        Sequenced(String*)(MemberClassConstructorRef) unaryRef = MemberClassConstructorRef.Sequenced.Unary;
        assert(unaryRef(this)("s1").string == "Sequenced.Unary([s1])");
        assert(unaryRef(this)().string == "Sequenced.Unary([])");
        Sequenced(String,String*)(MemberClassConstructorRef) binaryRef = MemberClassConstructorRef.Sequenced.Binary;
        assert(binaryRef(this)("s1", "s2").string == "Sequenced.Binary(s1,[s2])");
        assert(binaryRef(this)("s1").string == "Sequenced.Binary(s1,[])");
        Sequenced(String,String,String*)(MemberClassConstructorRef) ternaryRef = MemberClassConstructorRef.Sequenced.Ternary;
        assert(ternaryRef(this)("s1", "s2", "s3").string == "Sequenced.Ternary(s1,s2,[s3])");
        assert(ternaryRef(this)("s1", "s2").string == "Sequenced.Ternary(s1,s2,[])");
        Sequenced(String,String,String,String*)(MemberClassConstructorRef) naryRef = MemberClassConstructorRef.Sequenced.Nary;
        assert(naryRef(this)("s1", "s2", "s3", "s4").string == "Sequenced.Nary(s1,s2,s3,[s4])");
        assert(naryRef(this)("s1", "s2", "s3").string == "Sequenced.Nary(s1,s2,s3,[])");
    }
    shared class Parameterized<T> 
            given T satisfies Object {
        shared actual String string;
        shared new (T s1) {
            string = "Parameterized.Parameterized(``s1``)";
        }
        shared new Nullary() {
            string = "Parameterized.Nullary()";
        }
        shared new Unary(T s1) {
            string = "Parameterized.Unary(``s1``)";
        }
        shared new Binary(T s1, T s2) {
            string = "Parameterized.Binary(``s1``,``s2``)";
        }
        shared new Ternary(T s1, T s2, T s3) {
            string = "Parameterized.Ternary(``s1``,``s2``,``s3``)";
        }
        shared new Nary(T s1, T s2, T s3, T s4) {
            string = "Parameterized.Nary(``s1``,``s2``,``s3``,``s4``)";
            
        }
    }
    shared void parameterized() {
        variable Parameterized<String>(String)(MemberClassConstructorRef) defRef = MemberClassConstructorRef.Parameterized<String>;
        assert(defRef(this)("s1").string == "Parameterized.Parameterized(s1)");
        defRef = MemberClassConstructorRef.Parameterized<String>;
        assert(defRef(this)("s1").string == "Parameterized.Parameterized(s1)");
        Parameterized<String>(String) defRef2 = Parameterized<String>;
        assert(defRef2("s1").string == "Parameterized.Parameterized(s1)");
        Parameterized<String>()(MemberClassConstructorRef) nullaryRef = MemberClassConstructorRef.Parameterized<String>.Nullary;
        assert(nullaryRef(this)().string == "Parameterized.Nullary()");
        Parameterized<String>() nullaryRef2 = Parameterized<String>.Nullary;
        assert(nullaryRef2().string == "Parameterized.Nullary()");
        Parameterized<String>(String)(MemberClassConstructorRef) unaryRef = MemberClassConstructorRef.Parameterized<String>.Unary;
        assert(unaryRef(this)("s1").string == "Parameterized.Unary(s1)");
        Parameterized<String>(String,String)(MemberClassConstructorRef) binaryRef = MemberClassConstructorRef.Parameterized<String>.Binary;
        assert(binaryRef(this)("s1", "s2").string == "Parameterized.Binary(s1,s2)");
        Parameterized<String>(String,String,String)(MemberClassConstructorRef) ternaryRef = MemberClassConstructorRef.Parameterized<String>.Ternary;
        assert(ternaryRef(this)("s1", "s2", "s3").string == "Parameterized.Ternary(s1,s2,s3)");
        Parameterized<String>(String,String,String,String)(MemberClassConstructorRef) naryRef = MemberClassConstructorRef.Parameterized<String>.Nary;
        assert(naryRef(this)("s1", "s2", "s3", "s4").string == "Parameterized.Nary(s1,s2,s3,s4)");
    }
}
void memberClassConstructorRef() {
    value o = MemberClassConstructorRef();
    o.simple();
    o.defaulted();
    o.sequenced();
    o.parameterized();
}
