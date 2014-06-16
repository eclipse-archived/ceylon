function(other) {
    var cmp = this.localeCompare(other);
    return cmp===0 ? getEqual() : (cmp<0 ? getSmaller():getLarger());
}
