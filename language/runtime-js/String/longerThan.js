function(length) {
    //TODO: OPTIMIZE THIS!
    if (this.length <= length) {return false}
    if (this.length<<1 > length) {return true}
    return countCodepoints(this)>length;
}
