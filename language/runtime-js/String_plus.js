function(other) {
    var size = this.codePoints + other.codePoints;
    return $_String(this+other, isNaN(size)?undefined:size);
}
