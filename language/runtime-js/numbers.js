function Number$(wat) {
    return wat;
}
initType(Number$, 'ceylon.language::Number');
Number$.$$metamodel$$={$an:function(){return[shared()]},mod:$$METAMODEL$$,d:['ceylon.language','Number']};
exports.Number=Number$;
function $init$Number$() {
    if (Number$.$$===undefined) {
        initType(Number$, 'ceylon.language::Number');
    }
    return Number$;
}

var toInt = function(float) {
    return (float >= 0) ? Math.floor(float) : Math.ceil(float);
}

function JSNumber(value) { return Number(value); }
initExistingType(JSNumber, Number, 'ceylon.language::JSNumber');
JSNumber.$$metamodel$$={$nm:'JSNumber',$mt:'cls',$an:function(){return[shared()];},mod:$$METAMODEL$$,d:['ceylon.language','Number']};

var origNumToString = Number.prototype.toString;
inheritProto(JSNumber, Object$, Scalar, $init$Integral(), Exponentiable);

function Integer(value) {
    if (value && value.getT$name && value.getT$name() === 'ceylon.language::Integer') {
        return value;
    }
    return Number(value);
}
initTypeProto(Integer, 'ceylon.language::Integer', Object$, Scalar, 
        $init$Integral(), Exponentiable, Binary);
Integer.$$metamodel$$={$an:function(){return[shared(),native(),final()];},mod:$$METAMODEL$$,d:['ceylon.language','Integer']};

function Float(value) {
    if (value && value.getT$name && value.getT$name() === 'ceylon.language::Float') {
        return value;
    }
    var that = new Number(value);
    that.float$ = true;
    return that;
}
initTypeProto(Float, 'ceylon.language::Float', Object$, Scalar, Exponentiable);
Float.$$metamodel$$={$an:function(){return[shared(),native(),final()];},mod:$$METAMODEL$$,d:['ceylon.language','Float']};

var JSNum$proto = Number.prototype;
JSNum$proto.getT$all = function() {
    return (this.float$ ? Float : Integer).$$.T$all;
}
JSNum$proto.getT$name = function() {
    return (this.float$ ? Float : Integer).$$.T$name;
}
JSNum$proto.toString = origNumToString;
defineAttr(JSNum$proto, 'string', function(){ return String$(this.toString()); },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','string']});
JSNum$proto.plus = function(other) {
    return (this.float$||other.float$) ? Float(this+other) : (this+other);
}
JSNum$proto.minus = function(other) {
    return (this.float$||other.float$) ? Float(this-other) : (this-other);
}
JSNum$proto.times = function(other) {
    return (this.float$||other.float$) ? Float(this*other) : (this*other);
}
JSNum$proto.divided = function(other) {
    if (this.float$||other.float$) { return Float(this/other); }
    if (other == 0) {
        throw Exception(String$("Division by Zero"));
    }
    return toInt(this/other);
}
JSNum$proto.remainder = function(other) { return this%other; }
JSNum$proto.power = function(exp) {
    if (this.float$||exp.float$) { return Float(Math.pow(this, exp)); }
    if (exp<0 && this!=1 && this!=-1) {
        throw Exception(String$("Negative exponent"));
    }
    return toInt(Math.pow(this, exp));
}
defineAttr(JSNum$proto, 'negativeValue', function() {
    return this.float$ ? Float(-this) : -this;
},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Invertable','$at','negativeValue']});
defineAttr(JSNum$proto, 'positiveValue', function() {
    return this.float$ ? this : this.valueOf();
},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Invertable','$at','positiveValue']});
defineAttr(JSNum$proto, 'negative', function(){
  return this.float$ ? this < 0.0 : this.valueOf() < 0;
},undefined,{$t:{t:Boolean$},$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Number','$at','negative']});
defineAttr(JSNum$proto, 'positive', function(){
  return this.float$ ? this > 0.0 : this.valueOf() > 0;
},undefined,{$t:{t:Boolean$},$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Number','$at','positive']});
JSNum$proto.equals = function(other) { return (typeof(other)==='number' || other.constructor===Number) && other==this.valueOf(); }
JSNum$proto.compare = function(other) {
    var value = this.valueOf();
    return value==other ? equal : (value<other ? smaller:larger);
}
defineAttr(JSNum$proto, '$float', function(){ return Float(this.valueOf()); },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Number','$at','float']});
defineAttr(JSNum$proto, 'integer', function(){ return toInt(this); },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Number','$at','integer']});
defineAttr(JSNum$proto, 'integerValue', function(){ return toInt(this); },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Ordinal','$at','integerValue']});
defineAttr(JSNum$proto, 'character', function(){ return Character(this.valueOf()); },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Integer','$at','character']});
defineAttr(JSNum$proto, 'successor', function(){ return this+1; },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Ordinal','$at','successor']});
defineAttr(JSNum$proto, 'predecessor', function(){ return this-1; },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Ordinal','$at','predecessor']});
defineAttr(JSNum$proto, 'unit', function(){ return this == 1; },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Integral','$at','unit']});
defineAttr(JSNum$proto, 'zero', function(){ return this == 0; },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Integral','$at','zero']});
defineAttr(JSNum$proto, 'fractionalPart', function() {
    if (!this.float$) { return 0; }
    return Float(this - (this>=0 ? Math.floor(this) : Math.ceil(this)));
},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Number','$at','fractionalPart']});
defineAttr(JSNum$proto, 'wholePart', function() {
    if (!this.float$) { return this.valueOf(); }
    return Float(this>=0 ? Math.floor(this) : Math.ceil(this));
},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Number','$at','wholePart']});
defineAttr(JSNum$proto, 'sign', function(){ return this > 0 ? 1 : this < 0 ? -1 : 0; },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Number','$at','sign']});
defineAttr(JSNum$proto, 'hash', function() {
    return this.float$ ? String$(this.toPrecision()).hash : this.valueOf();
},undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Object','$at','hash']});
JSNum$proto.distanceFrom = function(other) {
    return (this.float$ ? this.wholePart : this) - other;
}
//Binary interface
defineAttr(JSNum$proto, 'not', function(){ return ~this; },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Binary','$at','not']});
JSNum$proto.leftLogicalShift = function(i) { return this << i; }
JSNum$proto.rightLogicalShift = function(i) { return this >> i; }
JSNum$proto.rightArithmeticShift = function(i) { return this >>> i; }
JSNum$proto.and = function(x) { return this & x; }
JSNum$proto.or = function(x) { return this | x; }
JSNum$proto.xor = function(x) { return this ^ x; }
JSNum$proto.$get = function(idx) {
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
defineAttr(JSNum$proto, 'size', function(){ return 53; },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Binary','$at','size']});
defineAttr(JSNum$proto, 'magnitude', function(){ return Math.abs(this); },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Number','$at','magnitude']});

function $parseFloat(s) { return Float(parseFloat(s)); }

defineAttr(JSNum$proto, 'undefined', function(){ return isNaN(this); },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Float','$at','undefined']});
defineAttr(JSNum$proto, 'finite', function(){ return this!=Infinity && this!=-Infinity && !isNaN(this); },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Float','$at','finite']});
defineAttr(JSNum$proto, 'infinite', function(){ return this==Infinity || this==-Infinity; },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Float','$at','infinite']});
defineAttr(JSNum$proto, 'strictlyPositive', function(){ return this>0 || (this==0 && (1/this==Infinity)); },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Float','$at','strictlyPositive']});
defineAttr(JSNum$proto, 'strictlyNegative', function() { return this<0 || (this==0 && (1/this==-Infinity)); },
  undefined,{$an:function(){return[shared(),actual()]},mod:$$METAMODEL$$,d:['ceylon.language','Float','$at','strictlyNegative']});

var $infinity = Float(Infinity);
function getInfinity() { return $infinity; }
getInfinity.$$metamodel$$={mod:$$METAMODEL$$,d:['ceylon.language','infinity']};
//TODO metamodel
//function getNegativeInfinity() { return Float(-Infinity); }

exports.Integer=Integer;
exports.Float=Float;
exports.getInfinity=getInfinity;
exports.parseFloat=$parseFloat;
