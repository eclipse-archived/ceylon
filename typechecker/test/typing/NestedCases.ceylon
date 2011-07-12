abstract class NestedCases() of Case1 | Case2 {
    shared class Case1() extends NestedCases() {}
    shared class Case2() extends NestedCases() {}
}

void testNestedCases() {
    NestedCases nc = NestedCases().Case1().Case2();
    @type["NestedCases.Case2"] value v = NestedCases().Case1().Case2();
}