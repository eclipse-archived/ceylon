import check {...}
import naming.sub1 { Sub1=SimpleType }
import naming.sub2 { Sub2=SimpleType }

shared class SimpleType() {
    shared actual String string => "Root Package";
}

shared void test() {
    check(SimpleType().string == "Root Package", "Root Package");
    check(Sub1().string == "Sub1", "Sub1");
    check(Sub2().string == "Sub2", "Sub2");
    results();
}
