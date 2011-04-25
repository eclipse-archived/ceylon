class Parameters() {
    void x1(@type["String"] String s) {}
    void x2(@type["String"] String s, @type["Natural"] Natural n) {}
    void x3(@type["Empty|Sequence<String>"] String[] s) {}
    void x4(@type["Nothing|String"] String? s) {}
    void x5(@type["Empty|Sequence<String>"] String... s) {}
}