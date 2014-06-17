function(from, to) {
    if (from > to) {
        return this.segment(to, from-to+1).reversed;
    }
    return this.segment(from, to-from+1);
}
