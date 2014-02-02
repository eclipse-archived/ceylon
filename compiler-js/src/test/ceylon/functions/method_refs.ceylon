import check { check }

//Static method references
class MethodRefTest(name) {
    shared String name;
    shared actual String string => "MethodRefTest ``name``";
    shared String suffix(Integer x) => "``string`` #``x``";

    shared class Inner(String kid) {
        shared actual String string => "``outer.string`` sub-``kid``";
        shared String prefix(Integer x) => "#``x`` ``string``";
    }
}

void testStaticMethodReferences() {
    print("Testing static method references...");
    value mr  = MethodRefTest("TEST");
    value mref = MethodRefTest.suffix(mr);
    check(mref(1) == "MethodRefTest TEST #1", "Static method ref 1");
    check(MethodRefTest.string(mr) == "MethodRefTest TEST", "Static method ref 2");
    check(mref(1) == MethodRefTest.suffix(mr)(1), "Static method ref 3");
    value mri = mr.Inner("T2");
    value iref = MethodRefTest.Inner.prefix(mri);
    check(iref(1) == "#1 MethodRefTest TEST sub-T2", "Static method ref 4");
    check(MethodRefTest.Inner.string(mri) == "MethodRefTest TEST sub-T2", "Static method ref 5");
    check(iref(1) == MethodRefTest.Inner.prefix(mri)(1), "Static method ref 6");
    value ints = {1,2,3,4}.sequence;
    check((List<Integer>.get(ints)(1) else -1) == 2, "Static method ref 7");
    check(List<Integer>.map<String>(ints)((Integer x)=>x.string).sequence == {"1","2","3","4"}, "Static method ref 8");
}
