function Number$(wat) {
    return wat;
}
initType(Number$, 'ceylon.language::Number');
Number$.$crtmm$=function(){return{$an:function(){return[shared()]},mod:$CCMM$,d:['ceylon.language','Number']};}
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
JSNumber.$crtmm$=function(){return{$nm:'JSNumber',$mt:'c',$an:function(){return[shared()];},mod:$CCMM$,d:['ceylon.language','Number']};}

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
Integer.$crtmm$=function(){return{$an:function(){return[shared(),$native(),$final()];},mod:$CCMM$,d:['ceylon.language','Integer']};}

function Float(value) {
    if (value && value.getT$name && value.getT$name() === 'ceylon.language::Float') {
        return value;
    }
    var that = new Number(value);
    that.float$ = true;
    return that;
}
initTypeProto(Float, 'ceylon.language::Float', Object$, Scalar, Exponentiable);
Float.$crtmm$=function(){return{$an:function(){return[shared(),$native(),$final()];},mod:$CCMM$,d:['ceylon.language','Float']};}

var JSNum$proto = Number.prototype;
JSNum$proto.getT$all = function() {
    return (this.float$ ? Float : Integer).$$.T$all;
}
JSNum$proto.getT$name = function() {
    return (this.float$ ? Float : Integer).$$.T$name;
}
JSNum$proto.toString = origNumToString;
$defat(JSNum$proto, 'string', function(){ return String$(this.toString()); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Object$,d:['ceylon.language','Object','$at','string']};});
JSNum$proto.plus = function(other) {
    return (this.float$||other.float$) ? Float(this+other) : (this+other);
}
JSNum$proto.plusInteger = function(other) {
    return this.float$ ? Float(this+other) : (this+other);
}
JSNum$proto.minus = function(other) {
    return (this.float$||other.float$) ? Float(this-other) : (this-other);
}
JSNum$proto.times = function(other) {
    return (this.float$||other.float$) ? Float(this*other) : (this*other);
}
JSNum$proto.timesInteger = function(other) {
    return this.float$ ? Float(this*other) : (this*other);
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
        throw AssertionError(String$("Negative exponent"));
    }
    return toInt(Math.pow(this, exp));
}
$defat(JSNum$proto, 'negated', function() {
    return this.float$ ? Float(-this) : -this;
},undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Invertable,d:['ceylon.language','Invertable','$at','negated']};});
$defat(JSNum$proto, 'negative', function(){
  return this.float$ ? this < 0.0 : this.valueOf() < 0;
},undefined,function(){return{$t:{t:Boolean$},$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Number$,d:['ceylon.language','Number','$at','negative']};});
$defat(JSNum$proto, 'positive', function(){
  return this.float$ ? this > 0.0 : this.valueOf() > 0;
},undefined,function(){return{$t:{t:Boolean$},$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Number$,d:['ceylon.language','Number','$at','positive']};});
JSNum$proto.equals = function(other) { return (typeof(other)==='number' || other.constructor===Number) && other==this.valueOf(); }
JSNum$proto.compare = function(other) {
    var value = this.valueOf();
    return value==other ? equal : (value<other ? smaller:larger);
}
$defat(JSNum$proto, '$float', function(){ return Float(this.valueOf()); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Number$,d:['ceylon.language','Number','$at','float']};});
$defat(JSNum$proto, 'integer', function(){ return toInt(this); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Number$,d:['ceylon.language','Number','$at','integer']};});
$defat(JSNum$proto, 'integerValue', function(){ return toInt(this); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Ordinal,d:['ceylon.language','Ordinal','$at','integerValue']};});
$defat(JSNum$proto, 'character', function(){ return Character(this.valueOf()); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Integer,d:['ceylon.language','Integer','$at','character']};});
$defat(JSNum$proto, 'successor', function(){ return this+1; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Ordinal,d:['ceylon.language','Ordinal','$at','successor']};});
$defat(JSNum$proto, 'predecessor', function(){ return this-1; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Ordinal,d:['ceylon.language','Ordinal','$at','predecessor']};});
$defat(JSNum$proto, 'unit', function(){ return this == 1; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Integral,d:['ceylon.language','Integral','$at','unit']};});
$defat(JSNum$proto, 'zero', function(){ return this == 0; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Integral,d:['ceylon.language','Integral','$at','zero']};});
$defat(JSNum$proto, 'fractionalPart', function() {
    if (!this.float$) { return 0; }
    return Float(this - (this>=0 ? Math.floor(this) : Math.ceil(this)));
},undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Number$,d:['ceylon.language','Number','$at','fractionalPart']};});
$defat(JSNum$proto, 'wholePart', function() {
    if (!this.float$) { return this.valueOf(); }
    return Float(this>=0 ? Math.floor(this) : Math.ceil(this));
},undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Number$,d:['ceylon.language','Number','$at','wholePart']};});
$defat(JSNum$proto, 'sign', function(){ return this > 0 ? 1 : this < 0 ? -1 : 0; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Number$,d:['ceylon.language','Number','$at','sign']};});
$defat(JSNum$proto, 'hash', function() {
    return this.float$ ? String$(this.toPrecision()).hash : this.valueOf();
},undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Object$,d:['ceylon.language','Object','$at','hash']};});
JSNum$proto.distanceFrom = function(other) {
    return (this.float$ ? this.wholePart : this) - other;
}
//Binary interface
$defat(JSNum$proto, 'not', function(){ return ~this; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Binary,d:['ceylon.language','Binary','$at','not']};});
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
$defat(JSNum$proto, 'magnitude', function(){ return Math.abs(this); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Number$,d:['ceylon.language','Number','$at','magnitude']};});

function $parseFloat(s) { return Float(parseFloat(s)); }
$parseFloat.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Null},{t:Float}]},$ps:[{$nm:'string',$t:{t:String$}}],d:['ceylon.language','parseFloat']};}

$defat(JSNum$proto, 'undefined', function(){ return isNaN(this); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['ceylon.language','Float','$at','undefined']};});
$defat(JSNum$proto, 'finite', function(){ return this!=Infinity && this!=-Infinity && !isNaN(this); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['ceylon.language','Float','$at','finite']};});
$defat(JSNum$proto, 'infinite', function(){ return this==Infinity || this==-Infinity; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['ceylon.language','Float','$at','infinite']};});
$defat(JSNum$proto, 'strictlyPositive', function(){ return this>0 || (this==0 && (1/this==Infinity)); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['ceylon.language','Float','$at','strictlyPositive']};});
$defat(JSNum$proto, 'strictlyNegative', function() { return this<0 || (this==0 && (1/this==-Infinity)); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['ceylon.language','Float','$at','strictlyNegative']};});

var $infinity = Float(Infinity);
function getInfinity() { return $infinity; }
exports.$prop$getInfinity={get:getInfinity,$crtmm$:function(){return{mod:$CCMM$,$t:{t:Float},d:['ceylon.language','infinity']};}};

exports.Integer=Integer;
exports.Float=Float;
exports.getInfinity=getInfinity;
exports.parseFloat=$parseFloat;
