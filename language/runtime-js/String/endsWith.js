function(str) {
    var start = this.length - str.length
    if (start < 0) {return false}
    return cmpSubString(this, str, start);
}
