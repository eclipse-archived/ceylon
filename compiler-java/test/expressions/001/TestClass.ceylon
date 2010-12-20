doc "Test attribute/local evaluation"
class TestClass(Integer a) {
    Integer x = a;

    Integer run(Integer y) {
        return x + y;
    }
}
