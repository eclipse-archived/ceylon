function(from, len) {
    var fromIndex = from;
    var maxCount = len + fromIndex;
    if (fromIndex < 0) {fromIndex = 0;}
    var i1 = 0;
    var count = 0;
    for (; i1<this.length && count<fromIndex; ++i1, ++count) {
        if ((this.charCodeAt(i1)&0xfc00) === 0xd800) {++i1}
    }
    var i2 = i1;
    for (; i2<this.length && count<maxCount; ++i2, ++count) {
        if ((this.charCodeAt(i2)&0xfc00) === 0xd800) {++i2}
    }
    if (i2 >= this.length) {
        this.codePoints = count;
        if (fromIndex === 0) {return this;}
    }
    return $_String(this.substring(i1, i2), count-fromIndex);
}
