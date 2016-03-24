@test
shared void bug713() {
    value t = `Array<String>.ofSize` == `Array<String>.ofSize`;
}
