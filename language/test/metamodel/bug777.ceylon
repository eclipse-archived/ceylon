
shared object bug777o { shared void f() {} }

@test
shared void bug777() {
    value x = `class bug777o`;
    print (x.defaultConstructor);
}