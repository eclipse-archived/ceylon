function(take) {
    if (take==0) return empty();
    return this.measure(0, take);
}
