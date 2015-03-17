class Outer() {
    shared class Inner {
        shared new () {}
        shared new New() {}
        shared class Deeper {
            shared new New() {}
        }
    }
}

abstract class Container() {
    @error shared formal class Inner1 {
        shared new New() {}
    }
    @error shared default class Inner2 {
        shared new New() {}
    }
}

void createOuterInner() {
    Outer.Inner inner1 = Outer().Inner.New();
    Outer.Inner.Deeper deeper1 = Outer().Inner().Deeper.New();
    Outer.Inner inner2 = Outer.Inner.New(Outer())();
    Outer.Inner.Deeper deeper2 = Outer.Inner(Outer())().Deeper.New();
    @error Outer.Inner inner2e = Outer.Inner.New();
    @error Outer.Inner.Deeper deeper2e = Outer.Inner().Deeper.New();
    Outer.Inner.Deeper deeper3 = Outer().Inner.New().Deeper.New();
    Outer.Inner.Deeper deeper4 = Outer.Inner.New(Outer())().Deeper.New();
    Outer.Inner.Deeper deeper5 = Outer.Inner.Deeper.New(inner1)();
    @error Outer.Inner.Deeper deeper4e = Outer.Inner.New().Deeper.New();
    @error Outer.Inner.Deeper deeper5e = Outer.Inner.Deeper.New();
    @error Outer.Inner.Deeper deeper6e = Outer().Inner.New.Deeper.New();
    //value it = `new Outer.Inner.New`;
}

class ClassMemberCtorChaining() {
    shared class Member {
        shared new Other(Integer i) {}
    }
}
class ClassMemberCtorChainingSub() 
        extends ClassMemberCtorChaining() {
    shared class Sub 
            extends super.Member {
        shared new Other(Integer i) 
                extends super.Other(i) {}
    }
}
class ClassMemberCtorChainingSubBroken() 
        extends ClassMemberCtorChaining() {
    @error shared class Sub 
            extends ClassMemberCtorChaining.Member {
        shared new Other(Integer i) 
                extends super.Other(i) {}
    }
}

class XX {
    shared new () {}
    shared new XX() {}
    shared class YY() {}
}

class ZZ() extends XX() {}
class WW() extends XX.XX() {}
@error class UU() extends XX.YY() {}

class Class<T> {
    shared new New() {}
    shared default class Inner() {}
}
class Subclass<T>() extends Class<T>.New() {
    @error shared actual class Inner() extends Class<T>.Inner() {}
}
class BrokenSubclass<T>() extends Class<T>.New() {
    @error shared actual class Inner() extends Class<String>.Inner() {}
}

void testInstantiationArgs() {
    Class<List<String>>.New();
    Class<Class<List<String>>>.New();
    @error Class<Class<List<String>>.New>.New();
}
