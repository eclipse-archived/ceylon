@noanno
class Bug6660<Element>(Element init) 
        extends Contextual<Element>() {
    shared void f() {
        try(Using(init)){}
        try(super.Using(init)){}
    }
}