// Test multi-step extension

class D() {
    extension Natural natural() {
        return 1;
    }

    void test() {
        Float f = 3.142 + D();
    }
}

// Should compile to:
//   Float f = Float.instance(3.142).plus(new D().natural().floatXXX());
