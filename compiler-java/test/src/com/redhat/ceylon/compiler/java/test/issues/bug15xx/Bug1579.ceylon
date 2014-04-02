class Foo(Integer number) {

}

void do() {
    value iterable = {Foo(1),Foo(2),Foo(3)};

    value following = iterable.following(Foo(2));
}

void bug1579() {
    {String+} following = {"a"}.following("a");
}