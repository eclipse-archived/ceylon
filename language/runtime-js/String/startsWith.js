function(str) {
    if (str.size > this.size) {return false}
    return cmpSubString(this, str, 0);
}
