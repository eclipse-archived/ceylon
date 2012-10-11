interface MemberClassRefinement {
    abstract class Abstract() {
        shared formal class Inner() {
            shared formal String do();
        }
    }
    class Concrete() 
            extends Abstract() {
        shared actual class Inner() 
                extends super.Inner() {
            shared actual String do() { return "hello"; }
        }
    }
    class BadConcrete() 
            extends Abstract() {
        @error shared actual class Inner() 
                extends super.Inner() {}
    }
    @error class BrokenConcrete() 
            extends Abstract() {}
}

class ClassDefaultMemberClassWithTypeParams<T>() {
    shared default class Member<U>() {}
}
class ClassDefaultMemberClassWithTypeParams_sub<X>() 
        extends ClassDefaultMemberClassWithTypeParams<X>() {
    shared actual class Member<Y>() 
            extends super.Member<Y>() {}
}

abstract class Many<Element>() {
    formal shared class Iterator() {
        formal shared Element next();
    }
}

class Singleton(String s) 
        extends Many<String>() {
    shared actual class Iterator()
            extends super.Iterator() {
        shared actual String next() {
            return s;
        }
    }
    shared class Alias() = Iterator;
}

void testSin() {
    print(Singleton("hello").Iterator().next());
    @type["Singleton.Iterator"] Singleton("hello").Iterator();
    Singleton.Iterator i1 = Singleton("hello").Iterator();
    @type["Singleton.Alias"] Singleton("goodbye").Alias();
    Singleton.Iterator i2 = Singleton("goodbye").Alias();
}

@error class SingletonAlias() = Singleton.Iterator;
@error class Alias() = Many<Integer>.Iterator;

interface InterfaceFormalMemberClass {
    shared formal class Member() {
    }
}

class InterfaceFormalMemberClass1() satisfies InterfaceFormalMemberClass {
    shared actual default class Member() extends InterfaceFormalMemberClass::Member() {
    }
}
class InterfaceFormalMemberClass2() extends InterfaceFormalMemberClass1() {
    @error shared actual class Member() extends InterfaceFormalMemberClass::Member() {
    }
}
class InterfaceFormalMemberClass3() extends InterfaceFormalMemberClass1() {
    shared actual class Member() extends InterfaceFormalMemberClass1::Member() {
    }
}