@nomodel
class ClassDefaultMemberClassWithTypeParams<T>(T tt) {
    shared default class Member<U>(T t, U u) {}
    shared Member<Integer> m1() {
        Member<Integer>{u=1; t=tt;};
        return Member<Integer>(tt, 1);
    }
}
@nomodel
class ClassDefaultMemberClassWithTypeParams_sub<X>(X xx) 
        extends ClassDefaultMemberClassWithTypeParams<X>(xx) {
    shared actual class Member<Y>(X x, Y y) 
            extends super.Member<Y>(x, y) {}
    shared Member<String> m2() {
        Member<String>{y=""; x=xx;};
        return Member<String>(xx, "");
    }
}
