function(/*Category*/chars) {
    var from = 0;
    var size=this.size;
    while (from<size && chars(this.$_get(from))) {++from}
    var to = size;
    if (from < to) {
        do {--to} while (from<to && chars(this.$_get(to)));
        ++to;
    }
    if (from===0 && to===size) {return this;}
    var result = this.substring(from, to);
    if (this.codePoints !== undefined) {
        result.codePoints = this.codePoints - from - size + to;
    }
    return result;
}
