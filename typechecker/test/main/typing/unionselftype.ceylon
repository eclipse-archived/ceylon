shared class UnionSelfType_Foo() 
        satisfies Comparable<UnionSelfType_Foo|UnionSelfType_Bar> {
    shared actual Comparison compare(UnionSelfType_Foo|UnionSelfType_Bar other) => nothing;
}
shared class UnionSelfType_Bar() 
        satisfies Comparable<UnionSelfType_Foo|UnionSelfType_Bar> {
    shared actual Comparison compare(UnionSelfType_Foo|UnionSelfType_Bar other) => nothing;
}

void hello(UnionSelfType_Foo|UnionSelfType_Bar uuu) {
    uuu.compare(uuu);
    Comparable<UnionSelfType_Bar|UnionSelfType_Bar> u = uuu;
}
