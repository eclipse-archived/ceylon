function initType(a,b,c,d,e,f,g,h,i,j,k,l);//IGNORE
function String$(x){};//IGNORE
function Character(x){};//IGNORE
function inheritProto(a,b,c,d,e,f,g);//IGNORE
function Exception$(x){};//IGNORE
var Object$,Castable,Integral,Numeric,Exponentiable,Scalar,equal,smaller,larger,exports;//IGNORE

function Integer(value) {
    //var that = new Number(value);
    //that.value = value;
    return value;
}
initExistingType(Integer, Number, 'ceylon.language.Integer', Object$, Scalar, Castable, Integral, Exponentiable);
var origIntToString = Number.prototype.toString;
inheritProtoI(Integer, Object$, Scalar, Castable, Integral, Exponentiable);
var Integer$proto = Integer.$$.prototype;
Integer$proto.toString = origIntToString;
Integer$proto.getString = function() { return String$(this.toString()) }
Integer$proto.plus = function(other) {
    if (other.constructor===Number) {
        return this+other;
    }
    return Float(this+other.value);
}
Integer$proto.minus = function(other) {
    if (other.constructor===Number) {
        return this-other;
    }
    return Float(this-other.value);
}
Integer$proto.times = function(other) {
    if (other.constructor===Number) {
        return this*other;
    }
    return Float(this*other.value);
}
Integer$proto.divided = function(other) {
    if (other.constructor===Number) {
        if (other == 0) {
            throw Exception(String$("Division by Zero"));
        }
        var exact = this/other;
        return (exact<0) ? Math.ceil(exact) : Math.floor(exact);
    }
    var exact = this/other.value;
    return Float((exact<0) ? Math.ceil(exact) : Math.floor(exact));
}
Integer$proto.remainder = function(other) { return this%other }
Integer$proto.power = function(exp) {
    var isint = exp.constructor===Number;
    if (isint) {
        if (exp.getSign() < 0) {
            if (!(this==1 || this==-1)) {
                throw Exception(String$("Negative exponent"));
            }
        }
    }
    if (isint) {
        var exact = Math.pow(this, exp);
        return (exact<0) ? Math.ceil(exact) : Math.floor(exact);
    } else {
        return Float(Math.pow(this, exp.value));
    }
}
Integer$proto.getNegativeValue = function() { return -this; }
Integer$proto.getPositiveValue = function() { return this.valueOf(); }
Integer$proto.equals = function(other) {
    return other==this || other.value==this;
}
Integer$proto.compare = function(other) {
    return this==other ? equal : (this<other ? smaller:larger);
}
Integer$proto.getFloat = function() { return Float(this.valueOf()) }
Integer$proto.getInteger = function() { return this }
Integer$proto.getCharacter = function() { return Character(this.valueOf()); }
Integer$proto.getSuccessor = function() { return this+1 }
Integer$proto.getPredecessor = function() { return this-1 }
Integer$proto.getUnit = function() { return this == 1 }
Integer$proto.getZero = function() { return this == 0 }
Integer$proto.getFractionalPart = function() { return 0; }
Integer$proto.getWholePart = function() { return this; }
Integer$proto.getSign = function() { return this > 0 ? 1 : this < 0 ? -1 : 0; }
Integer$proto.getHash = function() { return this; }

function $parseInteger(s) {
    //xkcd.com/208/
    if ((s.indexOf('_') >= 0 ? s.match(/^[+-]?\d{1,3}(_\d{3})+[kMGPT]?$/g) : s.match(/^[+-]?\d+[kMGPT]?$/g)) === null) {
        return null;
    }
    s = s.replace("_", "");
    var mag = null;
    if (s.match(/[kMGTP]$/g) !== null) {
        mag = s[s.length-1];
        s = s.slice(0,-1);
    }
    var i = parseInt(s);
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

function Float(value) {
    var that = new Float.$$;
    that.value = value;
    return that;
}
initTypeProto(Float, 'ceylon.language.Float', Object$, Scalar, Castable, Exponentiable);
var Float$proto = Float.$$.prototype;
Float$proto.getString = function() { return String$(this.value.toString()) }
Float$proto.plus = function(other) {
    return Float(this.value + (other.constructor!==Number?other.value:other));
}
Float$proto.minus = function(other) {
    return Float(this.value - (other.constructor!==Number?other.value:other));
}
Float$proto.times = function(other) {
    return Float(this.value * (other.constructor!==Number?other.value:other));
}
Float$proto.divided = function(other) {
    return Float(this.value / (other.constructor!==Number?other.value:other));
}
Float$proto.power = function(other) {
    return Float(Math.pow(this.value, (other.constructor!==Number?other.value:other)));
}
Float$proto.getNegativeValue = function() { return Float(-this.value) }
Float$proto.getPositiveValue = function() { return this }
Float$proto.equals = function(other) {
    return other.value===this.value || other==this.value;
}
Float$proto.compare = function(other) {
    if (other === null || other === undefined) { return larger; }
    return this.value===other.value ? equal
                                    : (this.value<other.value ? smaller:larger);
}
Float$proto.getFloat = function() { return this; }
Float$proto.getInteger = function() { return this.value >= 0.0 ? Math.floor(this.value) : Math.ceil(this.value); }
Float$proto.getWholePart = function() {
    var _p = this.value.toPrecision();
    var dot = _p.indexOf('.');
    return dot >= 0 ? Float(parseFloat(_p.slice(0, dot))) : this;
}
Float$proto.getFractionalPart = function() {
    var _p = this.value.toPrecision();
    var dot = _p.indexOf('.');
    return dot >= 0 ? Float(parseFloat(_p.slice(dot))) : Float(0.0);
}
Float$proto.getSign = function() { return this.value > 0 ? 1 : this.value < 0 ? -1 : 0; }
Float$proto.getHash = function() { return String$(this.value.toPrecision()).getHash(); }
Float$proto.getUndefined = function() { return isNaN(this.value); }
Float$proto.getFinite = function() { return this.value!==Infinity && this.value!==-Infinity && !isNaN(this.value); }
Float$proto.getInfinite = function() { return this.value===Infinity || this.value===-Infinity; }
Float$proto.getStrictlyPositive = function() { return this.value>0 || (this.value===0 && (1/this.value===Infinity)); }
Float$proto.getStrictlyNegative = function() { return this.value<0 || (this.value===0 && (1/this.value===-Infinity)); }

function getInfinity() { return Float(Infinity); }
//function getNegativeInfinity() { return Float(-Infinity); }

exports.Integer=Integer;
exports.Float=Float;
exports.getInfinity=getInfinity;
exports.parseInteger=$parseInteger;
exports.parseFloat=$parseFloat;
