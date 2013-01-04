doc "A count of the number of `true` items in the given values."
shared Integer count(Boolean... values) {
    variable value count=0;
    for (val in values) {
        if (val) {
            count++;
        }
    }
    return count;
}
