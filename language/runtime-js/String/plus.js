function(other) {
    if (!is$(other,{t:$_String}))throw new TypeError("Expected String");
    return $_String(this+other);
}
