import check { check,fail }
import ceylon.language.meta.model { Class, UnionType }
import ceylon.language.meta { type }

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

shared interface TestInterface310<in In, out Out=Nothing> {
}

class Test310Class1<in In, out Out=Nothing>() satisfies TestInterface310<In,Out> {
}
class Test310Class2<in In, out Out=Nothing>(TestInterface310<In, Out>|String obj) satisfies TestInterface310<In,Out> {
    shared Boolean test() {
        if (is String obj) {
            return false;
        } else {
            return true;
        }
    }
}
class Test310Class3<in In, out Out=Nothing>(TestInterface310<In, Out>|String obj) satisfies TestInterface310<In?,Out?> {
    shared Boolean test() {
        return Test310Class2<In,Out>(obj).test();
    }
}
class Test310Class4<in In2, out Out=Nothing>(TestInterface310<In2, Out>|String obj) satisfies TestInterface310<In2?,Out?> {
    shared Boolean test() {
        return Test310Class2<In2,Out>(obj).test();
    }
}

interface A309<T> {}
interface B309<T> {}
class C309<T>() satisfies A309<List<T>> & B309<T> {
    shared String foo() {
        value a = `<T>`;
        return a.string;
    }
}

class Bug341<T>(T t) {
  shared Object b<U>(U u) {
    class C<V>(V v) {}
    if (!u is String) {
      check(!u is C<Float>, "Bug 341");
    }
    return C(1.0);
  }
}

void test550<F>() {
  void g<G>() {
    check(`F` == `Integer`, "#550");
  }
  g<String>();
}

void testReifiedRuntime(){
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

    //issue #310
    check(Test310Class3<Integer,Character>(Test310Class1<Integer,Character>()).test(), "Issue 310 [1]");
    check(Test310Class4<Integer,Character>(Test310Class1<Integer,Character>()).test(), "Issue 310 [2]");
    //issue #309
    check(C309<String>().foo() == "ceylon.language::String", "Issue 309 - expected String got ``C309<String>().foo()``");
    Bug341("!").b(Bug341(1).b("2"));

    //Issue #458
    Composer458<String, String, []> c1 = Composer458<String, String, []>();
    value c2 = c1.and<Integer>();
    c2.debug();
    clang639();
    test550<Integer>();
}

class Holder458<T>() {
  shared void debug() {
    value t = `<T>`;
    if (is Class t) {
      check(t.declaration==`class Tuple`, "#458.2 expected Tuple");
      Anything targElement = t.typeArguments[`given Tuple.Element`];
      Anything targFirst = t.typeArguments[`given Tuple.First`];
      Anything targRest = t.typeArguments[`given Tuple.Rest`];
      check(targElement is UnionType<Integer|String>, "#458.3 Tuple.Element expected Integer|String => ``targElement else "NULL"`` ``className(targElement else "NULL")``");
      check(targFirst is Class<Integer>, "#458.4 Tuple.First expected Integer => ``targFirst else "NULL"`` ``className(targFirst else "NULL")``");
      check(targRest is Class<[Integer,String]>, "#458.5 Tuple.Rest expected [Integer,String] => ``targRest else "NULL"`` ``className(targRest else "NULL")``");
    } else {
      fail("#458.1 expected Class<Tuple> found ``t``");
    }
  }
}

class Composer458<out Element, out First, out Rest>() given First satisfies Element given Rest satisfies Sequential<Element> {
  value holder = Holder458<Tuple<First|Element,First,Rest>>();
  shared void debug() {
    holder.debug();
  }
  shared Composer458<Element|Other,Other,Tuple<Element|Other,Other,Tuple<First|Element,First,Rest>>>
      and<Other>()
      => Composer458<Element|Other,Other,Tuple<Element|Other,Other,Tuple<First|Element,First,Rest>>>();
}

void clang639() {
    class Cons1<out Element>(first, rest=null) {
        shared Element first;
        shared Cons1<Element>? rest;
        shared Cons1<Element|Other> follow<Other>(Other head)
            =>  Cons1(head, this);                   // 1
    }
    class Cons2<out Element>(first, rest=null) {
        shared Element first;
        shared Cons2<Element>? rest;
        shared Cons2<Element|Other> follow<Other>(Other head)
            =>  Cons2<Element|Other>(head, this);  // 2
    }

    variable Cons1<Integer|String> it1 = Cons1("");    // 3
    variable Cons2<Integer|String> it2 = Cons2("");    // 3
    //variable {<Integer|String>*} it = {};         // 4
    for (i in 0:10k) {
        it1 = it1.follow("" of Integer|String);
        it2 = it2.follow("");                       // 5
    }

    print("c.l#639 1: ``type(it1)``"); // error
    print("c.l#639 2: ``type(it2)``"); // error
    check((it1 of Anything) is Cons1<Integer|String>); // error
    check((it2 of Anything) is Cons2<Integer|String>); // error
}
