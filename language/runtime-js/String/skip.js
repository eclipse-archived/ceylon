function(skip) {
    if (skip==0) return this;
    return this.measure(skip, this.size);
}
