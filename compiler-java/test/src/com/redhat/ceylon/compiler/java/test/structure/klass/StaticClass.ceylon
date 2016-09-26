@noanno
class StaticClass {
    shared static class SharedStatic(String s = "") {}
    static class Static() extends SharedStatic() {}
    
    shared new () {
        Static();
        SharedStatic();
    }
}
//TODO @noanno
//interface StaticInterface {
//    shared static class SharedStatic() {}
//    static class Static(String s = "") extends SharedStatic() {}
//}
@noanno
void staticClass() {
    variable value sharedStat = StaticClass.SharedStatic();
    sharedStat = StaticClass.SharedStatic{};
    sharedStat = StaticClass.SharedStatic{
        s="";
    };
    //TODO variable value sharedStat2 = StaticInterface.SharedStatic();
    //sharedStat2 = StaticInterface.SharedStatic{};
    //sharedStat2 = StaticInterface.SharedStatic{
    //    s="";
    //};
}

