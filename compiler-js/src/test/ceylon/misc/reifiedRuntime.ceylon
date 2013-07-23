import check { check }

shared interface Top1{}
shared interface Middle1 satisfies Top1{}
shared interface Bottom1 satisfies Middle1{}

shared class Invariant<Element>(){}
shared class Covariant<out Element>(){}
shared class Contravariant<in Element>(){}
shared class Bivariant<in In, out Out>(){}

shared class Container<Outer>(){
    shared class Member<Inner>(){
        shared class Child<InnerMost>(){}
    }
}

interface TestInterface1<T> {}
class Test1<T>() satisfies TestInterface1<T> {}
class Test2<T1>() satisfies TestInterface1<T1> {}

String runtimeMethod(Integer param){
    return nothing;
}

void testReifiedRuntime(){
    print("Reified generics");
    Object member = Container<String>().Member<Integer>();
    check(member is Container<String>.Member<Integer>, "reified runtime inner 1");
    check(! member is Container<Integer>.Member<Integer>, "reified runtime inner 2");
    check(! member is Container<String>.Member<String>, "reified runtime inner 3");

    Object member2 = Container<String>().Member<Integer>().Child<Character>();
    check(member2 is Container<String>.Member<Integer>.Child<Character>, "reified runtime inner 4");

    Object invTop1 = Invariant<Top1>();
    check(invTop1 is Invariant<Top1>, "reified runtime invariant 1");
    check(! invTop1 is Invariant<Middle1>, "reified runtime invariant 2");
    check(! invTop1 is Invariant<Bottom1>, "reified runtime invariant 3");

    Object invMiddle1 = Invariant<Middle1>();
    check(! invMiddle1 is Invariant<Top1>, "reified runtime invariant 4");
    check(invMiddle1 is Invariant<Middle1>, "reified runtime invariant 5");
    check(! invMiddle1 is Invariant<Bottom1>, "reified runtime invariant 6");

    Object covMiddle1 = Covariant<Middle1>();
    check(covMiddle1 is Covariant<Top1>, "reified runtime covariant 1");
    check(covMiddle1 is Covariant<Middle1>, "reified runtime covariant 2");
    check(! covMiddle1 is Covariant<Bottom1>, "reified runtime covariant 3");

    Object contravMiddle1 = Contravariant<Middle1>();
    check(! contravMiddle1 is Contravariant<Top1>, "reified runtime contravariant 1");
    check(contravMiddle1 is Contravariant<Middle1>, "reified runtime contravariant 2");
    check(contravMiddle1 is Contravariant<Bottom1>, "reified runtime contravariant 3");

    Object bivMiddle1 = Bivariant<Middle1,Middle1>();
    check(! bivMiddle1 is Bivariant<Top1,Top1>, "reified runtime bivariant 1");
    check(! bivMiddle1 is Bivariant<Top1,Middle1>, "reified runtime bivariant 2");
    check(! bivMiddle1 is Bivariant<Top1,Bottom1>, "reified runtime bivariant 3");
    check(bivMiddle1 is Bivariant<Middle1,Top1>, "reified runtime bivariant 4");
    check(bivMiddle1 is Bivariant<Middle1,Middle1>, "reified runtime bivariant 5");
    check(! bivMiddle1 is Bivariant<Middle1,Bottom1>, "reified runtime bivariant 6");
    check(bivMiddle1 is Bivariant<Bottom1,Top1>, "reified runtime bivariant 7");
    check(bivMiddle1 is Bivariant<Bottom1,Middle1>, "reified runtime bivariant 8");
    check(! bivMiddle1 is Bivariant<Bottom1,Bottom1>, "reified runtime bivariant 9");

    class Local<T>(){}
    
    Object localInteger = Local<Integer>();
    check(localInteger is Local<Integer>, "reified runtime local 1");
    
    Object m = runtimeMethod;
    check(m is String(Integer), "reified runtime callable 1");
    check(!m is Integer(Integer), "reified runtime callable 2");
    check(!m is String(String), "reified runtime callable 3");
    check(!m is String(), "reified runtime callable 4");
    Object m2 = testReifiedRuntime;
    check(m2 is Anything(), "reified runtime callable 5");
    check(!m2 is String(), "reified runtime callable 6");
    check(!m2 is Anything(Integer), "reified runtime callable 7");

    Object rec1 = Singleton<Entry<Integer,Singleton<String>>>(1->Singleton("x"));
    check(rec1 is Singleton<Entry<Integer,Singleton<String>>>, "#188 [1]");
    check(!rec1 is Singleton<Entry<Integer,Singleton<Integer>>>, "#188 [2]");

    //issue #221
    interface TestInterface2<in T> {}
    class Test3<in T>() satisfies TestInterface2<T> {}
    class Test4<in T1>() satisfies TestInterface2<T1> {}
    Object o1 = Test1<String>();
    check(o1 is TestInterface1<String>, "Issue #221 [1]");
    Object o2 = Test2<String>();
    check(o2 is TestInterface1<String>, "Issue #221 [2]");
    Object o3 = Test3<String>();
    check(o3 is TestInterface2<String>, "Issue #221 [3]");
    Object o4 = Test4<String>();
    check(o4 is TestInterface2<String>, "Issue #221 [4]");
}
