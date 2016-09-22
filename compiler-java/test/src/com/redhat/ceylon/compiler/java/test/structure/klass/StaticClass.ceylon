@noanno
class StaticClass {
    static class Static() {}
    shared static class SharedStatic() {}
    shared new () {
        Static();
        SharedStatic();
    }
}
void staticClass() {
    value sharedStat = StaticClass.SharedStatic();
}