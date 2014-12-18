void myFunc(Integer *dummy) {
    print("hi");
}
@test
shared void bug607() {
    myFunc();
    `myFunc`.declaration.invoke([]);
}