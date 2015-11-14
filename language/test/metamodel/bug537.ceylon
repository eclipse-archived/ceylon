@test
shared void bug537() {
    assert(`Boolean`.caseValues.size == 2);
    assert(`Integer`.caseValues.size == 0);
}
