@nomodel
shared class CreatorOperators() {
    variable Natural n1 := 0;
    variable Integer i1 := +0;
    variable Integer i2 := +0;
    
    void creators() {
        Range<Integer> r = i1..i2;
        Entry<Natural, Integer> entry = n1 -> i2;
    }
}