interface Multiple {
    interface Top {
        shared formal String name;
        shared default class Inner() {}
    }
    interface X satisfies Top {
        shared actual default String name { return "X"; }
        //shared actual default class Inner() extends Top::Inner() {}
    }
    interface Y satisfies Top {
        shared actual default String name { return "Y"; }
    }
    class C() satisfies X & Y {
        void m() {
            Inner inn = X::Inner();
        }
        shared actual String name {
            return X::name;
        }
        shared actual String string {
            return Y::name;
        }
        /*shared actual class Inner() 
                extends X::Inner() {
            X::Inner();
        }*/
    }
    interface Silly { shared String name { return "Gavin"; }  }
    class Broken() satisfies X & Y {
        shared actual String name {
            @error return Top::name;
        }
        shared actual String string {
            @error return Silly::name;
        }
    }
}