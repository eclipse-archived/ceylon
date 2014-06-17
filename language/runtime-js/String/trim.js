function(/*Category*/chars) {
    var from = 0;
    while (from<this.length && chars(this.$_get(from))) {++from}
    var to = this.length;
    if (from < to) {
        do {--to} while (from<to && chars(this.$_get(to)));
        ++to;
    }
    if (from===0 && to===this.length) {return this;}
    var result = this.substring(from, to);
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from - this.length + to;
    }
    return result;
}
