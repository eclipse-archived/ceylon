class Parameters() {
    void x1(@type["String"] String s) {}
    void x2(@type["String"] String s, @type["Natural"] Natural n) {}
    void x3(@type["Sequence<String>"] String[] s) {}
    void x4(@type["Optional<String>"] String? s) {}
    void x5(@type["Sequence<String>"] String... s) {}
}