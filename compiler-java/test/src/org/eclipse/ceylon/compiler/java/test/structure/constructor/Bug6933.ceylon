@noanno
class Mwe6933 {
    shared static class Inner {
        shared actual String string;
        shared new first { string = "first"; }
        shared new second { string = "second"; }
    }
    shared new() {  }
}

@noanno
void method6933(String string, Mwe6933.Inner ref) {  }
@noanno
void methodTwo6933() {
    method6933("string", Mwe6933.Inner.first);
}