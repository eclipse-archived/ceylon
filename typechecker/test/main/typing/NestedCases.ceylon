abstract class NestedCases() of Case1 | Case2 | Case3 {
    shared class Case1() extends NestedCases() {}
    shared class Case2() extends NestedCases() {}
    void testNestedCases() {
        NestedCases nc = Case1().Case2();
        @type["NestedCases.Case2"] value v = Case1().Case2();
    }
}

class Case3() extends NestedCases() {}

void testNestedCases() {
    NestedCases nc = Case3().Case1().Case2();
    @type["NestedCases.Case2"] value v = Case3().Case1().Case2();
}