function(length) {
    //TODO: OPTIMIZE THIS!
    if (this.length < length) {return true}
    if (this.length<<1 >= length) {return false}
    return countCodepoints(this)<length;
}
