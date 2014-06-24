function(take) {
    if (take==0) return getEmpty();
    return this.measure(0, take);
}
