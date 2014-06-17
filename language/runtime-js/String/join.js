function(objects) {
    var it = objects.iterator();
    var obj = it.next();
    if (obj === getFinished()) {return "";}
    if (this.codePoints === undefined) {this.codePoints = countCodepoints(this)}
    var str = obj.string;
    var result = str;
    var len = str.codePoints;
    while ((obj = it.next()) !== getFinished()) {
        result += this;
        str = obj.string;
        result += str;
        len += this.codePoints + str.codePoints;
    }
    return $_String(result, isNaN(len)?undefined:len);
}
