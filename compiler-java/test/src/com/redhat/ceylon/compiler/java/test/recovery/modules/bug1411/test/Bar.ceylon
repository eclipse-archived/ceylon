//import ceylon.language { LazyMap }

//import ceylon.language.metamodel { ... }

shared void bar() {
    print(Integer.plus);
}

//class BlabbingIterable<T>(Iterable<T> wrappedIterable, String name="Blah") satisfies Iterable<T> {
//    shared actual Iterator<T> iterator() {
//        class BlabbingIterator<T>(Iterator<T> iter) satisfies Iterator<T> {
//            shared actual T|Finished next() {
//                T|Finished x = iter.next();
//                if (is Object x) {
//                    print(name + ": " + x.string);
//                }
//                return x;
//            }
//        }
//        Iterator<T> i = wrappedIterable.iterator();
//        return BlabbingIterator(i);
//    }
//}

//class X<T>() {}

//void type() {}

// Issue #1240
//interface XX<out T> {
//    shared default T s=>nothing;
//}
//interface A{} interface B{}
//interface YY satisfies XX<A> {}
//interface ZZ<out T> satisfies XX<T> {
//    shared actual T s => super.s;
//}
//interface W satisfies ZZ<A> {}
//class C() satisfies YY&ZZ<B>&W {}

// Issue 1241
//shared class K() {
//    interface I<L>{}
//    class A<L>() satisfies I<L> {}
//}

// Issue 1239
//Anything aa = LazyMap({
//    "d" -> ((Integer lxx)  => ""),
//    "i" -> ((Integer lxx)  => "")  // 'l' clash with previous line; works if l is renamed
//});

// Issue 1243
//@noanno
//class X<T>(T t) {
//    shared class Y<S>(S s) {}
//    shared Y<Z> new<Z=Integer|Float>() => nothing;
//}

//void bug1270() {
//    variable [Integer*] ints = [];
//    ints = [1, *ints];
//    print(ints);
//    ints.sort((Integer x, Integer y) => x <=> y);
//}

void runrun() {
    //print(`type`.packageContainer);
     //print(`XX<Integer>`.string);
     //print(`XX<Integer>`.declaration.string);
     //print(`XX`.string);
     //value x = XX();
     //print(type(x));
//    variable Integer m_w = 13;
//    variable Integer m_z = 648;
//    Integer irandom() {
//        m_z = 36969 * m_z.and(65535) + m_z.rightLogicalShift(16);
//        m_w = 18000 * m_w.and(65535) + m_w.rightLogicalShift(16);
//        return m_z.leftLogicalShift(16).and(4294967295) + m_w;  /* 32-bit result */
//    }
//    Float random() {
//        m_z = 36969 * m_z.and(65535) + m_z.rightLogicalShift(16);
//        m_w = 18000 * m_w.and(65535) + m_w.rightLogicalShift(16);
//        Integer r = m_z.leftLogicalShift(16).and(4294967295) + m_w;  /* 32-bit result */
//        return r.float / 4294967295.0;
//    }
//
//print(random());
//print(random());
//print(random());
//print(random());
//
//print(36969 * 648.and(65535));
//print(648.rightLogicalShift(16));
//print(18000 * 13.and(65535));
//print(13.rightLogicalShift(16));
//print(23955912.leftLogicalShift(16));

//Integer getTime() {
//    return 0;
//}
//
//Integer plus(Integer x, Integer y) => x + y;
//
//Integer start1 = getTime();
//print("start 1");
//variable Integer[] xs1 = {};
//for (i in 0..10000) {
//  xs1 = xs1.withTrailing(i);
//}
//Integer total1 = xs1.fold(0, plus);
//print("stop 1 - ``getTime()-start1``");
//
//Integer start2 = getTime();
//print("start 2");
//variable {Integer*} xs2 = {};
//for (i in 0..10000) {
//  xs2 = xs2.trailedBy(i);
//}
//Integer total2 = xs2.fold(0, plus);
//print("stop 2 - ``getTime()-start2``");

//    String firstName = "First";
//    String lastName = "Last";
//
//    String name => firstName + " " + lastName;        // cannot find symbol
//    print(name);                                                // cannot find symbol

    //value xs = BlabbingIterable { name="XS"; 4, 5, 6 };
    //print("----");
    //value ys = BlabbingIterable { name="YS"; 1, 2, 3, *xs };
    //print("----");
    //value i = ys.iterator();
    //value n1 = i.next();
    //value n2 = i.next();
    //value n3 = i.next();
    //value n4 = i.next();
    //print("----");
    //print(ys);

    //Integer methodWithSideEffect(){
    //    print("oops");
    //    return 5;
    //}
    //value foo = {methodWithSideEffect(), methodWithSideEffect()};
    //print("----");
    //print(foo);
}

