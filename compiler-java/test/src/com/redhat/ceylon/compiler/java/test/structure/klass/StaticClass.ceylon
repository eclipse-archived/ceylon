@noanno
class StaticClass {
    shared static class SharedStatic() {}
    static class Static() extends SharedStatic() {}
    
    shared new () {
        Static();
        SharedStatic();
    }
}
@noanno
interface StaticInterface {
    shared static class SharedStatic() {}
    static class Static() extends SharedStatic() {}
}
@noanno
void staticClass() {
    value sharedStat = StaticClass.SharedStatic();
    value sharedStat2 = StaticInterface.SharedStatic();
}

