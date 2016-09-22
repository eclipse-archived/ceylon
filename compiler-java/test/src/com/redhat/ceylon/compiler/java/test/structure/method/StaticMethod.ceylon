@noanno
class StaticMethod {
    
    static String nonSharedStat()=>"I'm static";
    shared static String sharedStat()=>"I'm static";
    
    shared new() {
        //print(nonSharedStat());
    }
}