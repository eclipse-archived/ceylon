@test
shared void bug711() {
    interface InterfaceA<out T> {}
    interface InterfaceAInteger satisfies InterfaceA<Integer> {}
    interface InterfaceAFloat satisfies InterfaceA<Float> {}
    class TestClass() satisfies InterfaceAInteger & InterfaceAFloat {}
    
    assert(`InterfaceAInteger & InterfaceAFloat`.subtypeOf(`InterfaceA<Anything>`)); // true, good
    assert(`InterfaceAInteger & InterfaceAFloat`.subtypeOf(`InterfaceA<Nothing>`)); // false, should be true
    assert(`TestClass`.subtypeOf(`InterfaceA<Anything>`)); // false, should be true
    assert(`TestClass`.subtypeOf(`InterfaceA<Nothing>`)); // false, should be true
}
