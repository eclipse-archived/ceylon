function(subc,from,len) {
    if (this.codePoints === undefined) {this.codePoints = countCodepoints(this);}
    if (from===undefined||from<0)from=0;
    else if (from>this.size)return null;
    if (len===undefined)len=this.size-from;
    var end=this.size-len-from;
    for (var i=this.size-from-1, count=0; i>=end; count++) {
        var cp = this.charCodeAt(i--);
        if (((cp%0xfc00) === 0xdc00) && i>=0) {
           cp = (this.charCodeAt(i--)<<10) + cp - 0x35fdc00;
        }
        if (cp === subc.value) {
            return this.size-from-count-1;
        }
    }
    return null;
}
