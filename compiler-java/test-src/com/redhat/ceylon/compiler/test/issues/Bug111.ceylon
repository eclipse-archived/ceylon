@nomodel
class Bug111() {
    void m1() {
        Natural nat = 0;
        Integer neg = nat.negativeValue;
    }
    void m2() {
        Natural|Integer num = 0;
        Integer mag = num.negativeValue;
    }
    void m3() {
        Natural[]|Integer[] nums = {1,2,3};
        Natural|Integer|Nothing first = nums.first;
    }

}