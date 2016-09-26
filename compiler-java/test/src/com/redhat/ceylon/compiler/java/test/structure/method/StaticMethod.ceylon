@noanno
class StaticMethod {
    
    static String nonSharedStat()=>"I'm static";
    shared static String sharedStat(String s="")=>"I'm static";
    
    shared new() {
    }
}
void staticMethod() {
    variable String s = StaticMethod.sharedStat();
    s = StaticMethod.sharedStat{};
    s = StaticMethod.sharedStat{
        s="";
    };
}