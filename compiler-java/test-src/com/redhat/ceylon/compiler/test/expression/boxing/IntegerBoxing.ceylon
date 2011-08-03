@nomodel
class IntegerBoxing(){
    Integer i1 = +1;
    void m() {
        Integer i2 = +1;
        Integer i3 = double(i2);
        Integer i4 = i3.plus(+5);
        Integer i5 = double(i1);
        Integer i6 = i5.plus(+5);
        Integer i7 = double(i1.plus(i1));
    }
    Integer double(Integer i) {
        return i.times(+2);
    }
}