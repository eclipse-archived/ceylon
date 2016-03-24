function(from, len) {
    if (len <= 0) { return empty(); }
    var stop = from + len;
    var seq = this.arr$.slice((from>=0)?from:0, (stop>=0)?stop:0);
    return $arr$(seq,this.$$targs$$.Element$Array);
}
