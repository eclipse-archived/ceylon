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
}

void testSin() {
    print(Singleton("hello").Iterator().next());
}

@error class SingletonAlias() = Singleton.Iterator;
@error class Alias() = Many<Integer>.Iterator;