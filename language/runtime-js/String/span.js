function(from, to) {
    if (from > to) {
        return this.measure(to, from-to+1).reversed;
    }
    return this.measure(from, to-from+1);
}
