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