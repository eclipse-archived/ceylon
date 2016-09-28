@noanno
class StaticMemberClass<T> {
    shared static class Member<U> {
        //shared static class Double() {
        //    shared U? u=null;
        //}
        shared new () {}
        shared T? t=null;
        shared U? u=null;
    }
    
    shared new () {}
}
@noanno
void staticMemberClassUse() {
    StaticMemberClass<Integer>.Member<String> mem1 = StaticMemberClass<Integer>.Member<String>();
    variable Integer? t = mem1.t;
    variable String? s = mem1.u;
    StaticMemberClass<Integer>.Member<String> mem2 = StaticMemberClass<Integer>.Member<String>{};
    t = mem2.t;
    s = mem2.u;
    value memref = StaticMemberClass<Integer>.Member<String>;
    t = memref().t;
    s = memref().u;
    /*/*StaticMemberClass<Integer>.Member<String>.Double doub2 = StaticMemberClass<Integer>.Member<String>.Double();
    StaticMemberClass<Integer>.Member<String>.Double doub2 = StaticMemberClass<Integer>.Member<String>.Double{};
    value doubref = StaticMemberClass<Integer>.Member<String>.Double;
    */*/
}
