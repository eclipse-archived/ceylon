// Test multi-step extension
class Test() {
    extension Natural natural() {
        return 1;
    }
    void test() {
        Float f = 3.142 + Test();
    }
}
