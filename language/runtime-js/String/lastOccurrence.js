function(subc,to) {
    if (this.codePoints === undefined) {this.codePoints = countCodepoints(this);}
    if (to===undefined)to=this.size-1;
    for (var i=to, count=0; i>=0; count++) {
        var cp = this.charCodeAt(i--);
        if (((cp%0xfc00) === 0xdc00) && i>=0) {
           cp = (this.charCodeAt(i--)<<10) + cp - 0x35fdc00;
        }
        if (cp === subc.value) {
            return to-count;
        }
    }
    return null;
}
