function(length) {
    //TODO: OPTIMIZE THIS!
    if (this.size <= length) {return false}
    if (this.size<<1 > length) {return true}
    return this.size>length;
}
