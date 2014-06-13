function(take) {
    if (take==0) return getEmpty();
    return this.segment(0, take);
}
