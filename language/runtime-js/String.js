function $_String(/*{Character*}*/value,size) {
    if (value && value.getT$name && value.getT$name() == 'ceylon.language::String') {
        //if it's already a String just return it
        return value;
    }
    else if (typeof(value) === 'string') {
        var that = new String(value);
        that.codePoints = size;
        return that;
    }
    var that = '';
    var _iter = value.iterator();
    var _c; while ((_c = _iter.next()) !== getFinished()) {
        that += _c.string;
    }
    if (size !== undefined) that.codePoints=size;
    return that;
}
