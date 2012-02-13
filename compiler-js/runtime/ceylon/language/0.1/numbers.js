function Integer(value) {
    var that = new Integer.$$;
    that.value = value;
    return that;
}
initType(Integer, 'ceylon.language.Integer', Object$, Castable, Integral, Numeric);
inheritProto(Integer, Object$, '$Object$');
var $Integer = Integer.$$;
$Integer.prototype.getString = function() { return String$(this.value.toString()) }
$Integer.prototype.plus = function(other) { return Integer(this.value+other.value) }
$Integer.prototype.minus = function(other) { return Integer(this.value-other.value) }
$Integer.prototype.times = function(other) { return Integer(this.value*other.value) }
$Integer.prototype.divided = function(other) {
    var exact = this.value/other.value;
    return Integer((exact<0) ? Math.ceil(exact) : Math.floor(exact));
}
$Integer.prototype.remainder = function(other) { return Integer(this.value%other.value) }
$Integer.prototype.power = function(other) {
    var exact = Math.pow(this.value, other.value);
    return Integer((exact<0) ? Math.ceil(exact) : Math.floor(exact));
}
$Integer.prototype.getNegativeValue = function() { return Integer(-this.value) }
$Integer.prototype.getPositiveValue = function() { return this }
$Integer.prototype.equals = function(other) { return Boolean$(other && other.value===this.value) }
$Integer.prototype.compare = function(other) {
    return this.value===other.value ? equal
                                    : (this.value<other.value ? smaller:larger);
}
$Integer.prototype.getFloat = function() { return Float(this.value) }
$Integer.prototype.getInteger = function() { return this }
$Integer.prototype.getCharacter = function() { return Character(this.value); }
$Integer.prototype.getSuccessor = function() { return Integer(this.value+1) }
$Integer.prototype.getPredecessor = function() { return Integer(this.value-1) }
$Integer.prototype.getUnit = function() { return Boolean$(this.value === 1) }
$Integer.prototype.getZero = function() { return Boolean$(this.value === 0) }
$Integer.prototype.getFractionalPart = function() { return Integer(0); }
$Integer.prototype.getWholePart = function() { return this; }
$Integer.prototype.getSign = function() { return this.value > 0 ? Integer(1) : this.value < 0 ? Integer(-1) : Integer(0); }
$Integer.prototype.getHash = function() { return this; }

function $parseInteger(s) { return Integer(parseInt(s.value)); }
function $parseFloat(s) { return Float(parseFloat(s.value)); }

function Float(value) {
    var that = new Float.$$;
    that.value = value;
    return that;
}
initType(Float, 'ceylon.language.Float', Object$, Castable, Numeric);
inheritProto(Float, Object$, '$Object$');
var $Float = Float.$$;
$Float.prototype.getString = function() { return String$(this.value.toString()) }
$Float.prototype.plus = function(other) { return Float(this.value+other.value) }
$Float.prototype.minus = function(other) { return Float(this.value-other.value) }
$Float.prototype.times = function(other) { return Float(this.value*other.value) }
$Float.prototype.divided = function(other) { return Float(this.value/other.value) }
$Float.prototype.power = function(other) { return Float(Math.pow(this.value, other.value)) }
$Float.prototype.getNegativeValue = function() { return Float(-this.value) }
$Float.prototype.getPositiveValue = function() { return this }
$Float.prototype.equals = function(other) { return Boolean$(other && other.value===this.value) }
$Float.prototype.compare = function(other) {
    if (other === null || other === undefined) { return larger; }
    return this.value===other.value ? equal
                                    : (this.value<other.value ? smaller:larger);
}
$Float.prototype.getFloat = function() { return this }
$Float.prototype.getInteger = function() { return Integer(parseInt(this.value.toFixed())); }
$Float.prototype.getWholePart = function() {
    var _p = this.value.toPrecision();
    var dot = _p.indexOf('.');
    return dot >= 0 ? Float(parseFloat(_p.slice(0, dot))) : this;
}
$Float.prototype.getFractionalPart = function() {
    var _p = this.value.toPrecision();
    var dot = _p.indexOf('.');
    return dot >= 0 ? Float(parseFloat(_p.slice(dot))) : Float(0.0);
}
$Float.prototype.getSign = function() { return this.value > 0 ? Integer(1) : this.value < 0 ? Integer(-1) : Integer(0); }
$Float.prototype.getHash = function() { return String$(this.value.toPrecision()).getHash(); }
$Float.prototype.getUndefined = function() { return isNaN(this.value) ? $true : $false; }
$Float.prototype.getFinite = function() { return this.value!==Infinity && this.value!==-Infinity && !isNaN(this.value) ? $true : $false; }
$Float.prototype.getInfinite = function() { return this.value===Infinity || this.value===-Infinity ? $true : $false; }

function getInfinity() { return Float(Infinity); }
//function getNegativeInfinity() { return Float(-Infinity); }

exports.Integer=Integer;
exports.Float=Float;
exports.getInfinity=getInfinity;
exports.parseInteger=$parseInteger;
exports.parseFloat=$parseFloat;
