class Is() {
    interface Container<T> 
        given T satisfies Number {}
    String[] strings = { "hello", "world" };
    if (is Sequence<String> strings) {
        writeLine(strings.first);
    }
    @error if (is Sequence strings) {}
    @error if (is Container<String> strings) {}
    @error if (is Is.Container strings) {}
    @error if (is Is.Container<String> strings) {}
}