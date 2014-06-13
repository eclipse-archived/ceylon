// make use of the fact that all WS characters are single UTF-16 code units
var from = 0;
while (from<this.length && (this.charCodeAt(from) in Character.WS$)) {++from}
var to = this.length;
if (from < to) {
    do {--to} while (from<to && (this.charCodeAt(to) in Character.WS$));
    ++to;
}
if (from===0 && to===this.length) {return this;}
var result = this.substring(from, to);
if (this.codePoints !== undefined) {
    result.codePoints = this.codePoints - from - this.length + to;
}
return result;
