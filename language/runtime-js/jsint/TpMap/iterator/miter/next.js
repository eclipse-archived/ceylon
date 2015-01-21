function(){
    var keys=tpMap$.ord||Object.keys(tpMap$.m);
    this.idx++;
    if (this.idx>=keys.length)return finished();
    var k=keys[this.idx];
    var v=tpMap$.m[k]||null;
    return Entry(v[0],v[1],{Key$Entry:{t:TypeParameter$meta$declaration},Item$Entry:tpMap$.$$targs$$.V$TpMap});
}
