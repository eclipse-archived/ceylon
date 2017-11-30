// make use of the fact that all WS characters are single UTF-16 code units
var result = "";
var len = 0;
var first = true;
var i1 = 0;
while (i1 < this.length) {
    while (this.charCodeAt(i1) in Character.WS$) {
        if (++i1 >= this.length) {return result;}
    }
    var i2 = i1;
    var cc = this.charCodeAt(i2);
    do {
        ++i2;
        if ((cc&0xfc00) === 0xd800) {++i2}
        ++len;
        cc = this.charCodeAt(i2);
    } while (i2<this.length && !(cc in Character.WS$));
    if (!first) {
        result += " ";
        ++len;
    }
    first = false;
    result += this.substring(i1, i2);
    i1 = i2+1;
}
return $_String(result, len);
