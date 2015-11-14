@noanno
Anything fn1989(String? x) {
    return nothing;
}
@noanno
shared class Bug1989(
    Anything fp2(Anything x, String a, String? b, Integer c, Integer? d),
    Anything fp(String? x) => fn1989(x)) {
    shared void m() {
        fp("");
        fp2(null, "", null, 1, null);
    }
}