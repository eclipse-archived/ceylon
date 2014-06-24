function(from, to) {
    if (from > to) {
        return this.measure(to, from-to+1).$_reverse();
    }
    return this.measure(from, to-from+1);
}
