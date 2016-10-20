@test
shared void staticTest() {
    value staticAttr = `value StaticMembers.attribute`;
    assert(staticAttr.static);
}