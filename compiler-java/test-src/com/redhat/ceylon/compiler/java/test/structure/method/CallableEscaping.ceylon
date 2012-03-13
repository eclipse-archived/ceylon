@nomodel
class C() {
    Integer call() {
        return 1;
    }
    Integer bar()() {
        return call();
    }
}