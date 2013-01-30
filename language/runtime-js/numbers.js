function initType(a,b,c,d,e,f,g,h,i,j,k,l);//IGNORE
function String$(x){};//IGNORE
function Character(x){};//IGNORE
function inheritProto(a,b,c,d,e,f,g);//IGNORE
function Exception$(x){};//IGNORE
var Object$,Integral,Numeric,Exponentiable,Scalar,equal,smaller,larger,exports;//IGNORE

function JSNumber(value) { return Number(value); }
initExistingType(JSNumber, Number, 'ceylon.language::JSNumber');
var origNumToString = Number.prototype.toString;
inheritProto(JSNumber, Object$, Scalar, $init$Integral(), Exponentiable);

function Integer(value) { return Number(value); }
initTypeProto(Integer, 'ceylon.language::Integer', Object$, Scalar, 
        $init$Integral(), Exponentiable, Binary);

function Float(value) {
    var that = new Number(value);
    that.$float = true;
    return that;
}
initTypeProto(Float, 'ceylon.language::Float', Object$, Scalar, Exponentiable);

var JSNum$proto = Number.prototype;
JSNum$proto.getT$all = function() {
    return (this.$float ? Float : Integer).$$.T$all;
}
JSNum$proto.getT$name = function() {
    return (this.$float ? Float : Integer).$$.T$name;
}
JSNum$proto.toString = origNumToString;
JSNum$proto.getString = function() { return String$(this.toString()) }
JSNum$proto.plus = function(other) {
    return (this.$float||other.$float) ? Float(this+other) : (this+other);
}
JSNum$proto.minus = function(other) {
    return (this.$float||other.$float) ? Float(this-other) : (this-other);
}
JSNum$proto.times = function(other) {
    return (this.$float||other.$float) ? Float(this*other) : (this*other);
}
JSNum$proto.divided = function(other) {
    if (this.$float||other.$float) { return Float(this/other); }
    if (other == 0) {
        throw Exception(String$("Division by Zero"));
    }
    return (this/other)|0;
}
JSNum$proto.remainder = function(other) { return this%other; }
JSNum$proto.power = function(exp) {
    if (this.$float||exp.$float) { return Float(Math.pow(this, exp)); }
    if (exp<0 && this!=1 && this!=-1) {
        throw Exception(String$("Negative exponent"));
    }
    return Math.pow(this, exp)|0;
}
JSNum$proto.getNegativeValue = function() {
    return this.$float ? Float(-this) : -this;
}
JSNum$proto.getPositiveValue = function() {
    return this.$float ? this : this.valueOf();
}
JSNum$proto.equals = function(other) { return other==this.valueOf(); }
JSNum$proto.compare = function(other) {
    var value = this.valueOf();
    return value==other ? equal : (value<other ? smaller:larger);
}
JSNum$proto.getFloat = function() { return Float(this.valueOf()); }
JSNum$proto.getInteger = function() { return this|0; }
JSNum$proto.getIntegerValue = function() { return this|0; }
JSNum$proto.getCharacter = function() { return Character(this.valueOf()); }
JSNum$proto.getSuccessor = function() { return this+1 }
JSNum$proto.getPredecessor = function() { return this-1 }
JSNum$proto.getUnit = function() { return this == 1 }
JSNum$proto.getZero = function() { return this == 0 }
JSNum$proto.getFractionalPart = function() {
    if (!this.$float) { return 0; }
    return Float(this - (this>=0 ? Math.floor(this) : Math.ceil(this)));
}
JSNum$proto.getWholePart = function() {
    if (!this.$float) { return this.valueOf(); }
    return Float(this>=0 ? Math.floor(this) : Math.ceil(this));
}
JSNum$proto.getSign = function() { return this > 0 ? 1 : this < 0 ? -1 : 0; }
JSNum$proto.getHash = function() {
    return this.$float ? String$(this.toPrecision()).getHash() : this.valueOf();
}
JSNum$proto.distanceFrom = function(other) {
    return (this.$float ? this.getWholePart() : this) - other;
}
//Binary interface
JSNum$proto.getNot = function() { return ~this; }
JSNum$proto.leftLogicalShift = function(i) { return this << i; }
JSNum$proto.rightLogicalShift = function(i) { return this >> i; }
JSNum$proto.rightArithmeticShift = function(i) { return this >>> i; }
JSNum$proto.and = function(x) { return this & x; }
JSNum$proto.or = function(x) { return this | x; }
JSNum$proto.xor = function(x) { return this ^ x; }
JSNum$proto.get = function(idx) {
    var mask = 1 << idx;
    return (this & mask) != 0 ? $true : $false;
}
JSNum$proto.set = function(idx,bit) {
    if (bit === undefined) { bit = $true; }
        var mask = idx > 1 ? 1 << idx : 1;
    return (bit === $true) ? this | mask : this & ~mask;
}
JSNum$proto.flip = function(idx) {
    var mask = 1 << idx;
    return this ^ mask;
}
JSNum$proto.clear = function(index) {
    return this.set(index, false);
}
JSNum$proto.getSize = function() { return 53; }
JSNum$proto.getMagnitude = function() { return Math.abs(this); }

function $parseInteger(s) {
    //xkcd.com/208/
    if (s.match(/^[+-]?\d+(_\d+)*[kMGPT]?$/g) === null) {
        return null;
    }
    s = s.replace(/_/g, "");
    var mag = null;
    if (s.match(/[kMGTP]$/g) !== null) {
        mag = s[s.length-1];
        s = s.slice(0,-1);
    }
    var i = parseInt(s);
    if (s[0]=='+') s = s.substring(1);
    if (s[0]=='-') {
        while (s[1]=='0') s='-'+s.substring(2);
    } else {
        while (s[0]=='0') s=s.substring(1);
    }
    if (i.toString()!==s) return null;
    var factor=1;
    switch(mag) {
        case 'P':factor*=1000;
        case 'T':factor*=1000;
        case 'G':factor*=1000;
        case 'M':factor*=1000;
        case 'k':factor*=1000;
    }
    return isNaN(i) ? null : i*factor;
}
function $parseFloat(s) { return Float(parseFloat(s)); }

JSNum$proto.getUndefined = function() { return isNaN(this); }
JSNum$proto.getFinite = function() { return this!=Infinity && this!=-Infinity && !isNaN(this); }
JSNum$proto.getInfinite = function() { return this==Infinity || this==-Infinity; }
JSNum$proto.getStrictlyPositive = function() { return this>0 || (this==0 && (1/this==Infinity)); }
JSNum$proto.getStrictlyNegative = function() { return this<0 || (this==0 && (1/this==-Infinity)); }

var $infinity = Float(Infinity);
function getInfinity() { return $infinity; }
//function getNegativeInfinity() { return Float(-Infinity); }

exports.Integer=Integer;
exports.Float=Float;
exports.getInfinity=getInfinity;
exports.parseInteger=$parseInteger;
exports.parseFloat=$parseFloat;
