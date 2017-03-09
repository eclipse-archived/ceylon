
@noanno
class Bug6963Ceylon() satisfies Bug6963Java.Inner {
    shared actual void fun(Bug6963Java<out Object>? java) {}
}