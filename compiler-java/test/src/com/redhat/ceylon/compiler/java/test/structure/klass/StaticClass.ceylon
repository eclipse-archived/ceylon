@noanno
class StaticClass {
    shared static class SharedStatic<T>(String s = "", T? t = null) {}
    static class Static() extends SharedStatic<Integer>() {}
    
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
    variable value sharedStat = StaticClass.SharedStatic<Integer>();
    sharedStat = StaticClass.SharedStatic<Integer>{};
    sharedStat = StaticClass.SharedStatic<Integer>{
        s="";
    };
    sharedStat = StaticClass.SharedStatic<Integer>{
        s="";
        t=1;
    };
    //TODO variable value sharedStat2 = StaticInterface.SharedStatic();
    //sharedStat2 = StaticInterface.SharedStatic{};
    //sharedStat2 = StaticInterface.SharedStatic{
    //    s="";
    //};
}

