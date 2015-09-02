function toInt(f) {
  return (f >= 0) ? Math.floor(f) : Math.ceil(f);
}
function nflt$(n) {
  if (n.float$)return true;
  return Math.round(n)!=n;
}

function JSNumber(value) {
  if (value===parseInt(value)) {
    return Integer(value);
  }
  return Float(value);
}
initExistingType(JSNumber, Number, 'ceylon.language::JSNumber');
JSNumber.$crtmm$=function(){return{nm:'JSNumber',mt:'c',pa:1,
  mod:$CCMM$,d:['$','Number']};}
ex$.JSNumber=JSNumber;

var origNumToString = Number.prototype.toString;
inheritProto(JSNumber, $_Object, $_Number, $init$Integral(), $init$Exponentiable());

function Integer(value) {
    if (value && value.getT$name && value.getT$name() === 'ceylon.language::Integer') {
        return value;
    }
    var that=Number(value);
    that.float$=false;
    return that;
}
initTypeProto(Integer, 'ceylon.language::Integer', $_Object,$_Number, JSNumber,
        $init$Integral(), $init$Exponentiable(), $init$Binary());
Integer.$crtmm$=function(){return{an:function(){return[shared(),$_native(),$_final()];},mod:$CCMM$,d:['$','Integer']};}

function Float(value) {
    if (value!==null && value!==undefined && value.getT$name && value.getT$name() === 'ceylon.language::Float') {
        return value;
    }
    var that = new Number(value);
    that.float$ = true;
    return that;
}
initTypeProto(Float, 'ceylon.language::Float', $_Object,$_Number,$init$Exponentiable(), JSNumber);
Float.$crtmm$=function(){return{an:function(){return[shared(),$_native(),$_final()];},mod:$CCMM$,d:['$','Float']};}

var JSNum$proto = Number.prototype;
JSNum$proto.getT$all = function() {
    return (nflt$(this) ? Float : Integer).$$.T$all;
}
JSNum$proto.getT$name = function() {
    return (nflt$(this) ? Float : Integer).$$.T$name;
}
var mock$flt={},mock$int={};
$_Number({Other$Number:{t:Float}},mock$flt);
$_Number({Other$Number:{t:Integer}},mock$int);
atr$(JSNum$proto,'$$targs$$',function(){
  if (!this.$targs$)this.$targs$=(nflt$(this)?mock$flt:mock$int).$$targs$$;
  return this.$targs$;
});
JSNum$proto.toString = origNumToString;
atr$(JSNum$proto, 'string', function(){
  if (this.fmz$)return "-0.0";
  var s=this.toString();
  if (s.indexOf('.')<0 && nflt$(this) && !isNaN(this))s+='.0';
  return s;
},undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Object,d:['$','Object','$at','string']};});
//Add a function to the specified prototype
function $addnm$(nm,s) {
  var t={t:JSNumber};
  if (s===undefined)s=$_Number.$$.prototype[nm];
  var d=JSNum$proto[nm];
  d.$crtmm$=getrtmm$$(s);
  d=d.$crtmm$;
  if (typeof(d.$t)==='string')d.$t=t;
  if (d.ps) for (var i=0; i < d.ps.length; i++) {
    if (typeof(d.ps[i].$t)==='string')d.ps[i].$t=t;
  }
}

JSNum$proto.plus = function(other) {
    return (nflt$(this)||nflt$(other)) ? Float(this+other) : (this+other);
}
$addnm$('plus');
JSNum$proto.plusInteger = function(other) {
    return nflt$(this) ? Float(this+other) : (this+other);
}
$addnm$('plusInteger');
JSNum$proto.minus = function(other) {
    return (nflt$(this)||nflt$(other)) ? Float(this-other) : (this-other);
}
$addnm$('minus');
JSNum$proto.times = function(other) {
  if (this.fmz$)return other.negative?Float(0.0):this;
  var fls=nflt$(this)||nflt$(other);
  if (fls && this.valueOf()==0 && other.negative) {
    //0*-something
    var f=Float(0.0);f.fmz$=true;
    return f;
  }
  return fls ? Float(this*other) : (this*other);
}
$addnm$('times');
JSNum$proto.timesInteger = function(other) {
    return nflt$(this) ? Float(this*other) : (this*other);
}
$addnm$('timesInteger');
JSNum$proto.divided = function(other) {
  if (this.fmz$)return this;
    if (nflt$(this)||nflt$(other)) { 
        var ret = Float(this/other);
        // make sure that if we expect a negative result, we get one, like 1/-0 -> -Infinity
        if(!this.negative && other.fmz$ && !ret.negative){ ret = ret.negated; }
        return ret;
    }
    if (other == 0) {
        throw Exception("Division by Zero");
    }
    return toInt(this/other);
}
$addnm$('divided');
JSNum$proto.remainder = function(other) {
  if (other == 0) {
    throw Exception("Division by Zero");
  }
  return this%other;
}
$addnm$('remainder',Integral.$$.prototype.remainder);
JSNum$proto.remainder.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Integer},ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','remainder']};};
JSNum$proto.divides=function(o){return o%this===0;}
JSNum$proto.divides.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_Boolean},ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','divides']};};
JSNum$proto.power = function(exp) {
    if (nflt$(this)||nflt$(exp)) { return Float(Math.pow(this, exp)); }
    if (exp<0 && this!=1 && this!=-1) {
        throw AssertionError("Negative exponent");
    }
    return toInt(Math.pow(this, exp));
}
$addnm$('power',Exponentiable.$$.prototype.power);
atr$(JSNum$proto, 'negated', function() {
  if (this.valueOf()==0 && this.float$) {
    var f=Float(0.0);
    if (!this.fmz$)f.fmz$=true;
    return f;
  }
  return nflt$(this) ? Float(-this) : -this;
},undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Invertible,d:['$','Invertible','$at','negated']};});
atr$(JSNum$proto, 'negative', function(){
  return nflt$(this) ? this < 0.0 : this.valueOf() < 0;
},undefined,function(){return{$t:{t:$_Boolean},an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','negative']};});
atr$(JSNum$proto, 'positive', function(){
  return nflt$(this) ? this > 0.0 : this.valueOf() > 0;
},undefined,function(){return{$t:{t:$_Boolean},an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','positive']};});
JSNum$proto.equals = function(other) { return (typeof(other)==='number' || other.constructor===Number) && other==this.valueOf(); }
$addnm$('equals',$_Object.$$.prototype.equals);
JSNum$proto.compare = function(other) {
    var value = this.valueOf();
    return value==other ? equal() : (value<other ? smaller():larger());
}
$addnm$('compare',Comparable.$$.prototype.compare);
JSNum$proto.smallerThan=function(o) {
  return Comparable.$$.prototype.smallerThan.call(this,o);
}
$addnm$('smallerThan',Comparable.$$.prototype.smallerThan);
JSNum$proto.largerThan=function(o) {
  return Comparable.$$.prototype.largerThan.call(this,o);
}
$addnm$('largerThan',Comparable.$$.prototype.largerThan);
JSNum$proto.notSmallerThan=function(o) {
  return Comparable.$$.prototype.notSmallerThan.call(this,o);
}
$addnm$('notSmallerThan',Comparable.$$.prototype.notSmallerThan);
JSNum$proto.notLargerThan=function(o) {
  return Comparable.$$.prototype.notLargerThan.call(this,o);
}
$addnm$('notLargerThan',Comparable.$$.prototype.notLargerThan);
atr$(JSNum$proto, '$_float', function(){ return Float(this.valueOf()); },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','float']};});
atr$(JSNum$proto, 'integer', function(){ return toInt(this); },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','integer']};});
atr$(JSNum$proto, '$_byte', function(){ return Byte(this); },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','byte']};});
atr$(JSNum$proto, 'character', function(){
  var c=this.valueOf();
  if (c < -2147483648 || c > 0x7fffffff) {
    throw OverflowException(c + ' is not a 32-bit integer');
  } else if (c < 0 || c > 1114111) {
    throw OverflowException(c + ' is not a Unicode code point');
  }
  return Character(this.valueOf());
},undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Integer,d:['$','Integer','$at','character']};});
atr$(JSNum$proto, 'successor', function(){ return this+1; },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Ordinal,d:['$','Ordinal','$at','successor']};});
atr$(JSNum$proto, 'predecessor', function(){ return this-1; },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Ordinal,d:['$','Ordinal','$at','predecessor']};});
atr$(JSNum$proto, 'unit', function(){ return this == 1; },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Integral,d:['$','Integral','$at','unit']};});
atr$(JSNum$proto, 'zero', function(){ return this == 0; },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Integral,d:['$','Integral','$at','zero']};});
atr$(JSNum$proto, 'even', function(){ return this%2 == 0; },
  undefined,function(){return{an:function(){return[shared()]},mod:$CCMM$,$cont:Integer,d:['$','Integer','$at','even']};});
atr$(JSNum$proto, 'fractionalPart', function() {
    if (!nflt$(this)) { return 0; }
    return Float(this - toInt(this));
},undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','fractionalPart']};});
atr$(JSNum$proto, 'wholePart', function() {
    if (!nflt$(this)) { return this.valueOf(); }
    var ret = this >= 0 ? Math.floor(this) : Math.ceil(this);
    var wret = Float(ret);
    if(ret == 0 && (this < 0 || this.fmz$)){ wret.fmz$ = true; }
    return wret;
},undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','wholePart']};});
atr$(JSNum$proto, 'sign', function(){ return this > 0 ? 1 : this < 0 ? -1 : 0; },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','sign']};});
atr$(JSNum$proto, 'hash', function() {
    return nflt$(this) ? $_String(this.toPrecision()).hash : this.valueOf();
},undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Object,d:['$','Object','$at','hash']};});
JSNum$proto.distanceFrom = function(other) {
    return (nflt$(this) ? this.wholePart : this) - other;
}
//Binary interface
atr$(JSNum$proto, 'not', function(){ return ~this; },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Binary,d:['$','Binary','$at','not']};});
JSNum$proto.leftLogicalShift = function(i) { return this << i; }
$addnm$('leftLogicalShift',Binary.$$.prototype.leftLogicalShift);
JSNum$proto.rightLogicalShift = function(i) { return this >>> i; }
$addnm$('rightLogicalShift',Binary.$$.prototype.rightLogicalShift);
JSNum$proto.rightArithmeticShift = function(i) { return this >> i; }
$addnm$('rightArithmeticShift',Binary.$$.prototype.rightArithmeticShift);
JSNum$proto.and = function(x) { return this & x; }
$addnm$('and',Binary.$$.prototype.and);
JSNum$proto.or = function(x) { return this | x; }
$addnm$('or',Binary.$$.prototype.or);
JSNum$proto.xor = function(x) { return this ^ x; }
$addnm$('xor',Binary.$$.prototype.xor);
JSNum$proto.$_get = function(idx) {
    if (idx < 0 || idx >31) {
        return false;
    } 
    var mask = 1 << idx;
    return (this & mask) != 0 ? true : false;
}
$addnm$('$_get',Binary.$$.prototype.$_get);
JSNum$proto.set=function(idx,bit) {
  if (idx<0 || idx>31) {
    return this;
  } 
  if (bit === undefined) { bit = true; }
  var mask = 1 << idx;
  return (bit === true) ? this|mask : this & ~mask;
}
$addnm$('set',Binary.$$.prototype.set);
JSNum$proto.flip = function(idx) {
    if (idx < 0 || idx >31) {
        return this;
    } 
    var mask = 1 << idx;
    return this ^ mask;
}
$addnm$('flip',Binary.$$.prototype.flip);
JSNum$proto.clear = function(index) {
    return this.set(index, false);
}
$addnm$('clear',Binary.$$.prototype.clear);
function checkintoverflow$(a){
  if(a>runtime().maxIntegerValue || a<runtime().minIntegerValue)throw OverflowException();
  return a;
}
JSNum$proto.neighbour=function(offset) {
  return checkintoverflow$(this+offset);
}
$addnm$('neighbour',Enumerable.$$.prototype.neighbour);
JSNum$proto.offset=function(other) {
  return checkintoverflow$(this-other);
}
$addnm$('offset',Enumerable.$$.prototype.offset);
JSNum$proto.offsetSign=function(other) {
  var v = this.valueOf();
  var o = other.valueOf();
  if (v>o)return 1;
  if (v<o)return -1;
  return 0;
}
$addnm$('offsetSign',Enumerable.$$.prototype.offsetSign);

atr$(JSNum$proto,'magnitude',function(){
  var m=Math.abs(this);
  if (nflt$(this))m=Float(m);
  return m;
},undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','magnitude']};});

atr$(JSNum$proto, '$_undefined', function(){ return isNaN(this); },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['$','Float','$at','undefined']};});
atr$(JSNum$proto, 'finite', function(){ return this!=Infinity && this!=-Infinity && !isNaN(this); },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['$','Float','$at','finite']};});
atr$(JSNum$proto, 'infinite', function(){ return this==Infinity || this==-Infinity; },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['$','Float','$at','infinite']};});
atr$(JSNum$proto, 'strictlyPositive', function(){ return this>0 || (this==0 && !this.fmz$ && (1/this==Infinity)); },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['$','Float','$at','strictlyPositive']};});
atr$(JSNum$proto, 'strictlyNegative', function() { return this<0 || this.fmz$ || (this==0 && (1/this==-Infinity)); },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Float,d:['$','Float','$at','strictlyNegative']};});

ex$.Integer=Integer;
ex$.Float=Float;
