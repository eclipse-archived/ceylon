function(length) {
    //TODO: OPTIMIZE THIS!
    if (this.size < length) {return true}
    if (this.size<<1 >= length) {return false}
    return this.size<length;
}
