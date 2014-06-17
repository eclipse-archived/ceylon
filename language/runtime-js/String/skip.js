function(skip) {
    if (skip==0) return this;
    return this.segment(skip, this.size);
}
