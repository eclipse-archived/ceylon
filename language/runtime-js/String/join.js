function(objects) {
    var it = objects.iterator();
    var obj = it.next();
    if (obj === finished()) {return "";}
    var str = obj.string;
    var result = str;
    while ((obj = it.next()) !== finished()) {
        result += this;
        str = obj.string;
        result += str;
    }
    return $_String(result);
}
