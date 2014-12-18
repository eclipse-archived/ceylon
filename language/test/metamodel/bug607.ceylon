void myFunc(Integer *dummy) {
    print("hi");
}
@test
shared void langBug607() {
    myFunc();
    `myFunc`.declaration.invoke([]);
}