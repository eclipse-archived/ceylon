void bug1761B(Anything b = () {
        class Bar() {}
        return Bar();
    }) {
}
