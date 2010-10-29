// Test extension to an attribute

class E(Integer i) {
    public extension Integer value = i;

    void test() {
        Float f = E(23) + 3.142;
    }
}
