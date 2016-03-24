package test;

class AA {}
class BB<T extends AA> {}

class Test extends AA {
    void test() {
        BB<? extends Test> b = new BB<Test>();
    }
}