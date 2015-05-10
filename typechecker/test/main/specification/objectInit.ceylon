class Init1() {
    shared void fun() {
        print(without);
        print(this);
    }
    shared object without {}
}

class Init2() {
    shared void fun() {
        @error print(with);
        @error print(this);
    }
    shared object with {
        print("");
    }
}