function(/*Category*/chars) {
    var from = 0;
    while (from<this.length && chars(this.$_get(from))) {++from}
    if (from===0) {return this;}
    var result = this.substring(from, this.length);
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from;
    }
    return result;
}
