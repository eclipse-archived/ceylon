interface Multiple {
    interface Top {
        shared formal String name;
        shared default class Inner() {}
        shared default String getName() { return name; }
    }
    interface X satisfies Top {
        shared actual default String name { return "X"; }
        shared actual default class Inner() 
                extends Top::Inner() {}
        shared actual String getName() { return "X"; }
    }
    interface Y satisfies Top {
        shared actual default String name { return "Y"; }
    }
    class C() satisfies X & Y {
        X::Inner inner() {
            X::Inner inn = X::Inner();
            return inn;
        }
        shared actual String name {
            return X::name;
        }
        shared actual String string {
            return Y::name;
        }
        shared actual class Inner() 
                extends X::Inner() {
            //@type:"Multiple.X.Inner" X::Inner();
        }
    }
    interface Silly { shared String name { return "Gavin"; }  }
    class Broken() satisfies X & Y {
        void method() {
            X::getName();
            @error Y::getName();
            @error Top::getName();
        }
        shared actual String name {
            @error return Top::name;
        }
        shared actual String string {
            @error return Silly::name;
        }
        @error shared actual class Inner() 
                extends Top::Inner() {}
        class My() {
            @error X::Inner();
            @error print(Y::name);
            @error print(Top::name);
            @error print(Silly::name);
        }
    }
    abstract class MyList() satisfies List<Integer> {
        shared actual String string {
            return List::string;
        }
        shared actual Integer hash {
            return List::hash;
        }
        shared actual Boolean equals(Object that) {
            return List::equals(that);
        }
    }
    abstract class MyAltList() satisfies List<Integer> {
        shared actual String string {
            @error return Basic::string;
        }
        shared actual Integer hash {
            @error return Object::hash;
        }
        shared actual Boolean equals(Object that) {
            return Identifiable::equals(that);
        }
    }
}