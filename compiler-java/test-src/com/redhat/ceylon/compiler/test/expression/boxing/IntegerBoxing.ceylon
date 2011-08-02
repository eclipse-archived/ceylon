@nomodel
class IntegerBoxing(){
    void m() {
        Integer i1 = +0;
        Integer i2 = i1.plus(+5);
        Integer i3 = double(i2);
    }
    Integer double(Integer i) {
        return i.times(+2);
    }
}