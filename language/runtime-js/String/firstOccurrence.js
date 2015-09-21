function(subc,from,len) {
    if (this.codePoints===undefined)this.codePoints=countCodepoints(this);
    if (!is$(subc, {t:Character})) return null;
    if (from===undefined || from<0)from=0;
    else if (from>=this.size)return null;
    if (len===undefined)len=this.size-from;
    var lim=len+from;
    if (lim>this.size)lim=this.size;
    for (var i=from, count=0; i<lim; count++) {
        var cp = this.charCodeAt(i++);
        if (((cp&0xfc00) === 0xd800) && i<this.length) {
            cp = (cp<<10) + this.charCodeAt(i++) - 0x35fdc00;
        }
        if (cp === subc.value) {return count+from;}
    }
    return null;
}
