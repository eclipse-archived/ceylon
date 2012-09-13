interface Multiple {
    interface Top {
        shared formal String name;
    }
    interface X satisfies Top {
        shared actual default String name { return "X"; }
    }
    interface Y satisfies Top {
        shared actual default String name { return "Y"; }
    }
    class C() satisfies X & Y {
        shared actual String name {
            return X::name;
        }
        shared actual String string {
            return Y::name;
        }
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