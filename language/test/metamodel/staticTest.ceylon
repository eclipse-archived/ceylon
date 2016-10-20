@test
shared void staticTest() {
    assert(`value StaticMembers.attribute`.name == "attribute");
    assert(`value StaticMembers.attribute`.static);
    assert(`function StaticMembers.method`.name == "method");
    assert(`function StaticMembers.method`.static);
    assert(`class StaticMembers.MemberClass`.name == "MemberClass");
    assert(`class StaticMembers.MemberClass`.static);
}