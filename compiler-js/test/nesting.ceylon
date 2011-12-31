class Outer(String name) {
    class Inner() {
        void printName() {
            print(name);
        }
    }
    value inner = Inner();
}

void outr(String name) {
    function inr() {
        return name;
    }
    String result = inr();
}