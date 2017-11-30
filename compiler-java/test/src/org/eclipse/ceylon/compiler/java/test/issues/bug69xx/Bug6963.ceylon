
@noanno
class Bug6963Ceylon() satisfies Bug6963Java.Inner {
    shared actual void fun(Bug6963Java<out Object>? java) {}
    
    void foo(Bug6963Java.F java){
        java.f((Bug6963Java<out Object>? x) => print(x));
    }
}