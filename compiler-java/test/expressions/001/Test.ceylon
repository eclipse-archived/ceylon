doc "Test attribute/local evaluation"
class Test(Integer a) {
    Integer x = a;

    Integer run(Integer y) {
        return x + y;
    }
}
