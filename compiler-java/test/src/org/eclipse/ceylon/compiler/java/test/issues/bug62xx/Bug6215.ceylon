@noanno
class Bug6215() {
    value [s, i, b] = ["", 1, true];
    void fun() {
        print(s.repeat(i));
    }
}
