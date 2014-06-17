function(length) {
    if (this.codePoints!==undefined) {return this.codePoints<length}
    if (this.length < length) {return true}
    if (this.length<<1 >= length) {return false}
    this.codePoints = countCodepoints(this);
    return this.codePoints<length;
}
