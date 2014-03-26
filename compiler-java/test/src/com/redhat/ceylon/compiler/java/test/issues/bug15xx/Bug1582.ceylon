shared void bug1582() {
    {String*} i1 = { "1", "2", "3"};
    {String*} i2 = { "1", "2", "3"};
    value x = zipPairs(i1,i2);
}