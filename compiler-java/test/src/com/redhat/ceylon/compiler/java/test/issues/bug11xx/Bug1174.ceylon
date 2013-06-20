@noanno
interface Bug1174_I {
    shared default Anything m(Integer a = 1) {
        return null;
    }
    shared default Anything m2(Integer a = 1) => null;
    
    shared formal Anything m3(String? s = null);
    shared formal Anything m4(String? s = null);
    shared formal Anything m5(String? s = null);
}
@noanno
class Bug1174_C() satisfies Bug1174_I {
    shared actual default String m(Integer a) {
        return "foo";
    }
    shared actual default String m2(Integer a) => "foo";
    
    m3(String? s) => null;
    shared actual Anything m4(String? s) => null;
    shared actual Anything m5(String? s) {
        return null;
    }
}
@noanno
void bug1174_callsite() {
    assert("foo" == Bug1174_C().m());
    assert("foo" == Bug1174_C().m2());
}