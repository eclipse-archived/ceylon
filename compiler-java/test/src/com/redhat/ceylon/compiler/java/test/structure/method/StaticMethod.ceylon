@noanno
class StaticMethod<S> {
    
    static String nonSharedStat()=>"I'm static";
    shared static String sharedStat<T>(String s="", T? t = null)=>"I'm static";
    shared static S generic() {
        return nothing;
    }
    
    shared new() {
    }
}
void staticMethod() {
    variable String s = StaticMethod.sharedStat<Integer>();
    s = StaticMethod.sharedStat<Integer>{};
    s = StaticMethod.sharedStat<Integer>{
        s="";
    };
    s = StaticMethod.sharedStat<Integer>{
        s="";
        t=1;
    };
    
    value ref = StaticMethod.sharedStat<Integer>;
    ref("", 1);
    
    value i = Integer.parse{
        string="123";
    };
}