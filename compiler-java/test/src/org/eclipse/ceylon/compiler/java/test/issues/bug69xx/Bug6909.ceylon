shared void bug6909() {
    assert("10000000000"==formatFloat(1.0e10, 0, 0));
    assert("100000000000000000000" == formatFloat(1.0e20, 0, 0));
}