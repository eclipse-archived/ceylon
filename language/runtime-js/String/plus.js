function(other) {
    if (!is$(other,{t:$_String}))throw new TypeError("Expected String");
    if (this.codePoints===undefined)this.codePoints=countCodepoints(this);
    if (other.codePoints===undefined)other.codePoints=countCodepoints(other);
    var size = this.codePoints + other.codePoints;
    return $_String(this+other, isNaN(size)?undefined:size);
}
