function $_Number($$targs$$,wat) {
    add_type_arg(wat, 'Other$Number', $$targs$$.Other$Number);
    return wat;
}
initType($_Number, 'ceylon.language::Number');
$_Number.$crtmm$=function(){return{$an:function(){return[shared()]},mod:$CCMM$,
  $tp:{Other$Number:{satisfies:{t:$_Number,a:{Other$Number:'Other$Number'}}}},
  satisfies:[{t:Numeric,a:{Other$Numeric:'Other$Number'}},{t:Comparable,a:{Other$Comparable:'Other$Comparable'}}],
  d:['$','Number']};}
ex$.$_Number=$_Number;
function $init$$_Number() {
    if ($_Number.$$===undefined) {
        initType($_Number, 'ceylon.language::Number');
    }
    return $_Number;
}

var toInt = function(float) {
    return (float >= 0) ? Math.floor(float) : Math.ceil(float);
}

function JSNumber(value) {
  if (value===parseInt(value)) {
    return Integer(value);
  }
  return Float(value);
}
initExistingType(JSNumber, Number, 'ceylon.language::JSNumber');
JSNumber.$crtmm$=function(){return{$nm:'JSNumber',$mt:'c',$an:function(){return[shared()];},
  mod:$CCMM$,d:['$','Number']};}

var origNumToString = Number.prototype.toString;
inheritProto(JSNumber, $_Object, $_Number, $init$Integral(), Exponentiable);

function Integer(value) {
    if (value && value.getT$name && value.getT$name() === 'ceylon.language::Integer') {
        return value;
    }
    var that=Number(value);
    $_Number({Other$Number:{t:Integer}},that);
    return that;
}
initTypeProto(Integer, 'ceylon.language::Integer', $_Object,$_Number,
        $init$Integral(), Exponentiable, Binary);
Integer.$crtmm$=function(){return{$an:function(){return[shared(),$_native(),$_final()];},mod:$CCMM$,d:['$','Integer']};}

function Float(value) {
    if (value && value.getT$name && value.getT$name() === 'ceylon.language::Float') {
        return value;
    }
    var that = new Number(value);
    $_Number({Other$Number:{t:Float}},that);
    that.float$ = true;
    return that;
}
initTypeProto(Float, 'ceylon.language::Float', $_Object,$_Number,Exponentiable);
Float.$crtmm$=function(){return{$an:function(){return[shared(),$_native(),$_final()];},mod:$CCMM$,d:['$','Float']};}

var JSNum$proto = Number.prototype;
JSNum$proto.getT$all = function() {
    return (this.float$ ? Float : Integer).$$.T$all;
}
JSNum$proto.getT$name = function() {
    return (this.float$ ? Float : Integer).$$.T$name;
}
JSNum$proto.toString = origNumToString;
atr$(JSNum$proto, 'string', function(){ return this.toString(); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Object,d:['$','Object','$at','string']};});
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
        throw Exception("Division by Zero");
    }
    return toInt(this/other);
}
JSNum$proto.remainder = function(other) { return this%other; }
JSNum$proto.power = function(exp) {
    if (this.float$||exp.float$) { return Float(Math.pow(this, exp)); }
    if (exp<0 && this!=1 && this!=-1) {
        throw AssertionError("Negative exponent");
    }
    return toInt(Math.pow(this, exp));
}
atr$(JSNum$proto, 'negated', function() {
    return this.float$ ? Float(-this) : -this;
},undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Invertable,d:['$','Invertable','$at','negated']};});
atr$(JSNum$proto, 'negative', function(){
  return this.float$ ? this < 0.0 : this.valueOf() < 0;
},undefined,function(){return{$t:{t:$_Boolean},$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','negative']};});
atr$(JSNum$proto, 'positive', function(){
  return this.float$ ? this > 0.0 : this.valueOf() > 0;
},undefined,function(){return{$t:{t:$_Boolean},$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','positive']};});
JSNum$proto.equals = function(other) { return (typeof(other)==='number' || other.constructor===Number) && other==this.valueOf(); }
JSNum$proto.compare = function(other) {
    var value = this.valueOf();
    return value==other ? getEqual() : (value<other ? getSmaller():getLarger());
}
JSNum$proto.smallerThan=function(o) {
  return Comparable.$$.prototype.smallerThan.call(this,o);
}
JSNum$proto.largerThan=function(o) {
  return Comparable.$$.prototype.largerThan.call(this,o);
}
JSNum$proto.notSmallerThan=function(o) {
  return Comparable.$$.prototype.notSmallerThan.call(this,o);
}
JSNum$proto.notLargerThan=function(o) {
  return Comparable.$$.prototype.notLargerThan.call(this,o);
}
atr$(JSNum$proto, '$_float', function(){ return Float(this.valueOf()); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','float']};});
atr$(JSNum$proto, 'integer', function(){ return toInt(this); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','integer']};});
atr$(JSNum$proto, 'integerValue', function(){ return toInt(this); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Ordinal,d:['$','Ordinal','$at','integerValue']};});
atr$(JSNum$proto, 'character', function(){ return Character(this.valueOf()); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Integer,d:['$','Integer','$at','character']};});
atr$(JSNum$proto, 'successor', function(){ return this+1; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Ordinal,d:['$','Ordinal','$at','successor']};});
atr$(JSNum$proto, 'predecessor', function(){ return this-1; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Ordinal,d:['$','Ordinal','$at','predecessor']};});
atr$(JSNum$proto, 'unit', function(){ return this == 1; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Integral,d:['$','Integral','$at','unit']};});
atr$(JSNum$proto, 'zero', function(){ return this == 0; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Integral,d:['$','Integral','$at','zero']};});
atr$(JSNum$proto, 'fractionalPart', function() {
    if (!this.float$) { return 0; }
    return Float(this - (this>=0 ? Math.floor(this) : Math.ceil(this)));
},undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','fractionalPart']};});
atr$(JSNum$proto, 'wholePart', function() {
    if (!this.float$) { return this.valueOf(); }
    return Float(this>=0 ? Math.floor(this) : Math.ceil(this));
},undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','wholePart']};});
atr$(JSNum$proto, 'sign', function(){ return this > 0 ? 1 : this < 0 ? -1 : 0; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','sign']};});
atr$(JSNum$proto, 'hash', function() {
    return this.float$ ? String$(this.toPrecision()).hash : this.valueOf();
},undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Object,d:['$','Object','$at','hash']};});
JSNum$proto.distanceFrom = function(other) {
    return (this.float$ ? this.wholePart : this) - other;
}
//Binary interface
atr$(JSNum$proto, 'not', function(){ return ~this; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Binary,d:['$','Binary','$at','not']};});
JSNum$proto.leftLogicalShift = function(i) { return this << i; }
JSNum$proto.rightLogicalShift = function(i) { return this >> i; }
JSNum$proto.rightArithmeticShift = function(i) { return this >>> i; }
JSNum$proto.and = function(x) { return this & x; }
JSNum$proto.or = function(x) { return this | x; }
JSNum$proto.xor = function(x) { return this ^ x; }
JSNum$proto.$_get = function(idx) {
    if (idx < 0 || idx >31) {
        return false;
    } 
    var mask = 1 << idx;
    return (this & mask) != 0 ? true : false;
}
JSNum$proto.set = function(idx,bit) {
    if (idx < 0 || idx >31) {
        return this;
    } 
    if (bit === undefined) { bit = true; }
        var mask = idx > 1 ? 1 << idx : 1;
    return (bit === true) ? this | mask : this & ~mask;
}
JSNum$proto.flip = function(idx) {
    if (idx < 0 || idx >31) {
        return this;
    } 
    var mask = 1 << idx;
    return this ^ mask;
}
JSNum$proto.clear = function(index) {
    return this.set(index, false);
}
JSNum$proto.neighbour=function(offset) {
  return this+offset;
}
atr$(JSNum$proto, 'magnitude', function(){ return Math.abs(this); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','magnitude']};});

atr$(JSNum$proto, 'undefined', function(){ return isNaN(this); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['$','Float','$at','undefined']};});
atr$(JSNum$proto, 'finite', function(){ return this!=Infinity && this!=-Infinity && !isNaN(this); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['$','Float','$at','finite']};});
atr$(JSNum$proto, 'infinite', function(){ return this==Infinity || this==-Infinity; },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['$','Float','$at','infinite']};});
atr$(JSNum$proto, 'strictlyPositive', function(){ return this>0 || (this==0 && (1/this==Infinity)); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['$','Float','$at','strictlyPositive']};});
atr$(JSNum$proto, 'strictlyNegative', function() { return this<0 || (this==0 && (1/this==-Infinity)); },
  undefined,function(){return{$an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['$','Float','$at','strictlyNegative']};});

var $infinity = Float(Infinity);
function getInfinity() { return $infinity; }
ex$.$prop$getInfinity={get:getInfinity,$crtmm$:function(){return{mod:$CCMM$,$t:{t:Float},d:['$','infinity']};}};

ex$.Integer=Integer;
ex$.Float=Float;
ex$.getInfinity=getInfinity;
