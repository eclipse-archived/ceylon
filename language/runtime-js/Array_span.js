function(from, to) {
    if (from > to) {
        var arr = this.segment(to, from-to+1);
        arr.reverse();
        return this.seq$?ArraySequence(arr,this.$$targs$$):arr.reifyCeylonType(this.$$targs$$.Element$Iterable);
    }
    return this.segment(from, to-from+1);
}
