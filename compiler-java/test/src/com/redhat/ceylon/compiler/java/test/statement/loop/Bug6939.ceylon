@noanno
void bug6939() {
    for (x in nothing) {
        print("xxx");
    }
    Nothing n = nothing;
    for (x in n) {
        print("xxx");
    }
}