function(subc) {
    for (var i=this.length-1, count=0; i>=0; count++) {
        var cp = this.charCodeAt(i--);
        if (((cp%0xfc00) === 0xdc00) && i>=0) {
           cp = (this.charCodeAt(i--)<<10) + cp - 0x35fdc00;
        }
        if (cp === subc.value) {
            if (this.codePoints === undefined) {this.codePoints = countCodepoints(this);}
            return this.codePoints - count - 1;
        }
    }
    this.codePoints = count;
    return null;
}
