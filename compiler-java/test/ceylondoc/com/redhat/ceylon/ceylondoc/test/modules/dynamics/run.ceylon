"an interface"
shared dynamic DynaTest {
    "a dyn property"
    shared formal dynamic dynamicProperty;
    "a method with a dyn param"
    shared formal void dynParamMethod("dyn parameter" dynamic dynamicParam);
    "a dyn method"
    shared formal dynamic dynamicMethod();
}

"a dyn toplevel method"
shared dynamic dyna("a dyn param" dynamic z) {
    return 0;
}
