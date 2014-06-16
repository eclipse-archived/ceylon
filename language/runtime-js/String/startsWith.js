function(str) {
    if (str.length > this.length) {return false}
    return cmpSubString(this, str, 0);
}
