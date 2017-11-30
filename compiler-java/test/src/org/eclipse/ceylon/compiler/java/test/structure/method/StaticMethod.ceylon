
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
    variable String s = StaticMethod<String>.sharedStat<Integer>();
    s = StaticMethod<String>.sharedStat<Integer>{};
    s = StaticMethod<String>.sharedStat<Integer>{
        s="";
    };
    s = StaticMethod<String>.sharedStat<Integer>{
        s="";
        t=1;
    };
    
    value ref = StaticMethod<String>.sharedStat<Integer>;
    ref("", 1);
    
    value i = Integer.parse{
        string="123";
    };
}