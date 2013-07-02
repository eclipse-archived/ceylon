interface Multiple {
    interface Top {
        shared formal String name;
        shared default class Inner() {}
        shared default String getName() { return name; }
    }
    interface X satisfies Top {
        shared actual default String name { return "X"; }
        shared actual default class Inner() 
                extends super.Inner() {}
        shared actual String getName() { return "X"; }
    }
    interface Y satisfies Top {
        shared actual default String name { return "Y"; }
    }
    class C() satisfies X & Y {
        X.Inner inner() {
            X.Inner inn = super.Inner();
            return inn;
        }
        shared actual String name {
            return (super of X).name;
        }
        shared actual String string {
            return (super of Y).name;
        }
        shared actual class Inner() 
                extends X.Inner(super)() {
            //@type:"Multiple.X.Inner" X::Inner();
        }
    }
    interface Silly { shared String name { return "Gavin"; }  }
    class Broken() satisfies X & Y {
        void method() {
            @type:"String" (super of X).getName();
            @error (super of Y).getName();
            @error (super of Top).getName();
        }
        shared actual String name {
            @error return (super of Top).name;
        }
        shared actual String string {
            @error return (super of Silly).name;
        }
        @error shared actual class Inner() 
                extends super.Inner() {}
        class My() {
            @error (super of X).Inner();
            @error print((super of Y).name);
            @error print((super of Top).name);
            @error print((super of Silly).name);
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
            @error return (super of Basic).string;
        }
        shared actual Integer hash {
            @error return (super of Object).hash;
        }
        shared actual Boolean equals(Object that) {
            return (super of Identifiable).equals(that);
        }
    }
}