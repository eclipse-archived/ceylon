function(/*Category*/chars) {
    var to = this.length;
    if (to > 0) {
        do {--to} while (to>=0 && chars(this.$_get(to)));
        ++to;
    }
    if (to===this.length) {return this;}
    else if (to===0) { return ""; }
    var result = this.substring(0, to);
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - this.length + to;
    }
    return result;
}
