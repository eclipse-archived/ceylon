function $_String(/*{Character*}*/value) {
    if (value && value.getT$name && value.getT$name() == 'ceylon.language::String') {
        //if it's already a String just return it
        return value;
    }
    else if (typeof(value) === 'string') {
        return new String(value);
    }
    var that = '';
    var _iter = value.iterator();
    var _c; while ((_c = _iter.next()) !== finished()) {
        that += _c.string;
    }
    return that;
}
