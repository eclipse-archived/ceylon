function(other) {
    var cmp = this.localeCompare(other);
    return cmp===0 ? equal() : (cmp<0 ? smaller():larger());
}
