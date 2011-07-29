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

    if (strings is Sequence<String>) {
        //writeLine(strings.first);
    }
    @error if (strings is Sequence) {}
    @error if (strings is Container<String>) {}
    @error if (strings is Is.Container) {}
    @error if (strings is Is.Container<String>) {}

    void method<T>() {
        if (is T strings) {}
        if (strings is T) {}
    }
    
}