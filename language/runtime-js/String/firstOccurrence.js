function(subc,from) {
    if (from===undefined || from<0)from=0;
    else if (from>=this.size)return null;
    if (!is$(subc, {t:Character})) return null;
    for (var i=from, count=0; i<this.length; count++) {
        var cp = this.charCodeAt(i++);
        if (((cp&0xfc00) === 0xd800) && i<this.length) {
            cp = (cp<<10) + this.charCodeAt(i++) - 0x35fdc00;
        }
        if (cp === subc.value) {return count+from;}
    }
    if (from===0 && this.codePoints===undefined)this.codePoints = count;
    return null;
}
