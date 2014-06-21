function(from, to) {
    if (from > to) {
        var arr = this.segment(to, from-to+1);
        arr.reverseInPlace();
        return arr.reifyCeylonType(this._elemTarg());
    }
    return this.segment(from, to-from+1);
}
