class Outer() {
    shared class Inner {
        shared new () {}
        shared new create() {}
        shared class Deeper {
            shared new create() {}
        }
    }
}

abstract class Container() {
    @error shared formal class Inner1 {
        shared new create() {}
    }
    @error shared default class Inner2 {
        shared new create() {}
    }
}

void createOuterInner() {
    Outer.Inner inner1 = Outer().Inner.create();
    Outer.Inner.Deeper deeper1 = Outer().Inner().Deeper.create();
    Outer.Inner inner2 = Outer.Inner.create(Outer())();
    Outer.Inner.Deeper deeper2 = Outer.Inner(Outer())().Deeper.create();
    @error Outer.Inner inner2e = Outer.Inner.create();
    @error Outer.Inner.Deeper deeper2e = Outer.Inner().Deeper.create();
    Outer.Inner.Deeper deeper3 = Outer().Inner.create().Deeper.create();
    Outer.Inner.Deeper deeper4 = Outer.Inner.create(Outer())().Deeper.create();
    Outer.Inner.Deeper deeper5 = Outer.Inner.Deeper.create(inner1)();
    @error Outer.Inner.Deeper deeper4e = Outer.Inner.create().Deeper.create();
    @error Outer.Inner.Deeper deeper5e = Outer.Inner.Deeper.create();
    @error Outer.Inner.Deeper deeper6e = Outer().Inner.create.Deeper.create();
    //value it = `new Outer.Inner.create`;
}

class ClassMemberCtorChaining() {
    shared class Member {
        shared new other(Integer i) {}
    }
}
class ClassMemberCtorChainingSub() 
        extends ClassMemberCtorChaining() {
    shared class Sub 
            extends super.Member {
        shared new other(Integer i) 
                extends super.other(i) {}
    }
}
//class ClassMemberCtorChainingSubBroken() 
//        extends ClassMemberCtorChaining() {
//    @error shared class Sub 
//            extends ClassMemberCtorChaining.Member {
//        shared new other(Integer i) 
//                extends super.other(i) {}
//    }
//}

class XX {
    shared new () {}
    shared new xx() {}
    shared class YY() {}
}

class ZZ() extends XX() {}
class WW() extends XX.xx() {}
//@error class UU() extends XX.YY() {}

class Class<T> {
    shared new create() {}
    shared default class Inner() {}
}
//class Subclass<T>() extends Class<T>.create() {
//    @error shared actual class Inner() extends Class<T>.Inner() {}
//}
//class BrokenSubclass<T>() extends Class<T>.create() {
//    @error shared actual class Inner() extends Class<String>.Inner() {}
//}

void testInstantiationArgs() {
    Class<List<String>>.create();
    Class<Class<List<String>>>.create();
    @error Class<Class<List<String>>.\Icreate>.create();
}
