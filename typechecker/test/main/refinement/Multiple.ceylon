interface Multiple {
    interface Top {
        shared formal String name;
        shared default class Inner() {}
        shared default String getName() { return name; }
    }
    interface X satisfies Top {
        shared actual default String name { return "X"; }
        shared actual default class Inner() 
                extends Top.Inner(super)() {}
        shared actual String getName() { return "X"; }
    }
    interface Y satisfies Top {
        shared actual default String name { return "Y"; }
    }
    class C() satisfies X & Y {
        X.Inner inner() {
            X.Inner inn = X.Inner(super)();
            return inn;
        }
        shared actual String name {
            return X.name(super);
        }
        shared actual String string {
            return Y.name(super);
        }
        shared actual class Inner() 
                extends X.Inner(super)() {
            //@type:"Multiple.X.Inner" X::Inner();
        }
    }
    interface Silly { shared String name { return "Gavin"; }  }
    class Broken() satisfies X & Y {
        void method() {
            X.getName(super)();
            @error Y.getName(super)();
            @error Top.getName(super)();
        }
        shared actual String name {
            @error return Top.name(super);
        }
        shared actual String string {
            @error return Silly.name(super);
        }
        @error shared actual class Inner() 
                extends Top.Inner() {}
        class My() {
            @error X.Inner(super)();
            @error print(Y.name(super));
            @error print(Top.name(super));
            @error print(Silly.name(super));
        }
    }
    abstract class MyList() satisfies List<Integer> {
        shared actual String string {
            return List<Integer>.string(super);
        }
        shared actual Integer hash {
            return List<Integer>.hash(super);
        }
        shared actual Boolean equals(Object that) {
            return List<Integer>.equals(super)(that);
        }
    }
    abstract class MyAltList() satisfies List<Integer> {
        shared actual String string {
            @error return Basic.string(super);
        }
        shared actual Integer hash {
            @error return Object.hash(super);
        }
        shared actual Boolean equals(Object that) {
            return Identifiable.equals(super)(that);
        }
    }
}