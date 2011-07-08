@nomodel
class ThisReference(){
    shared ThisReference m() {
        return this;
    }
    shared void test() {
        this.test();
    }
}