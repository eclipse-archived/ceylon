function(from, len) {
    if (len <= 0) { return getEmpty(); }
    var stop = from + len;
    var seq = this.slice((from>=0)?from:0, (stop>=0)?stop:0);
    return this.seq$?ArraySequence(seq,this.$$targs$$):seq.reifyCeylonType(this.$$targs$$.Element$Iterable);
}
