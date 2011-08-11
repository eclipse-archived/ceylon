@nomodel
class OuterReference(){
    class Inner() {
        Boolean inner() {
            return outer.test();
        }
    }
    shared Boolean test() {
        return true;
    }
}