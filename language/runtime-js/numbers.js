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
Integer.$st$={
  format:function(i,r,sep){
    if(r===undefined)r=10;
    if(sep===undefined)sep=null;
    return formatInteger(i,r,sep);
  },
  parse:function(s,r) {
    if (r===undefined)r=10;
    return parseIntegerInternal(s,r);
  },
  sum:function(i) {
    var s=0;
    var item;
    for (var iter=i.iterator();(item=iter.next())!==finished();) {
      s+=item;
    }
    return s;
  },
  product:function(i) {
    var s=1;
    var item;
    for (var iter=i.iterator();(item=iter.next())!==finished();) {
      s*=item;
    }
    return s;
  },
  max:function(i) {
    var f=true;
    var m=0;
    for (var iter=i.iterator();(item=iter.next())!==finished();) {
      if (f) {
        m=item;
        f=false;
      } else if (item>m)m=item;
    }
    return f?null:m;
  },
  min:function(i) {
    var f=true;
    var m=0;
    for (var iter=i.iterator();(item=iter.next())!==finished();) {
      if (f) {
        m=item;
        f=false;
      } else if (item<m)m=item;
    }
    return f?null:m;
  },
  smallest:function(x,y) {
    return x<y?x:y;
  },
  largest:function(x,y) {
    return x>y?x:y;
  }
};
Integer.$st$.format.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Integer},{t:ParseException}]},ps:[{nm:'string',mt:'prm',$t:{t:$_String}},{nm:'radix',mt:'prm',def:1,$t:{t:Integer}}],$cont:Integer,pa:4097,an:function(){return[see($arr$sa$([OpenFunction$jsint(lmp$(ex$,'$'),$init$Integer().$st$.format),OpenFunction$jsint(lmp$(ex$,'$'),Float.$st$.parse)],{t:FunctionDeclaration$meta$declaration})),tagged($arr$sa$(["Numbers","Basic types"],{t:$_String})),since("1.3.1")];},d:['$','Integer','$m','parse']};};
Integer.$st$.parse.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Integer},{t:ParseException}]},ps:[{nm:'string',mt:'prm',$t:{t:$_String}},{nm:'radix',mt:'prm',def:1,$t:{t:Integer}}],$cont:Integer,pa:4097,an:function(){return[see($arr$sa$([OpenFunction$jsint(lmp$(ex$,'$'),$init$Integer().$$.prototype.format),OpenFunction$jsint(lmp$(ex$,'$'),Float.$st$.parse)],{t:FunctionDeclaration$meta$declaration})),tagged($arr$sa$(["Numbers","Basic types"],{t:$_String})),since("1.3.1")];},d:['$','Integer','$m','parse']};};
Integer.$st$.sum.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Integer},ps:[{nm:'integers',mt:'prm',$t:{t:Iterable,a:{Element$Iterable:{t:Integer},Absent$Iterable:{t:Null}}}}],$cont:Integer,pa:4097,d:['$','Integer','$m','sum']};};
Integer.$st$.product.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Integer},ps:[{nm:'integers',mt:'prm',$t:{t:Iterable,a:{Element$Iterable:{t:Integer},Absent$Iterable:{t:Null}}}}],$cont:Integer,pa:4097,an:function(){return[doc$($CCMM$,'$:Integer:$m:product'),since("1.3.2")];},d:['$','Integer','$m','product']};};
Integer.$st$.max.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Integer},'Absent$max']},ps:[{nm:'integers',mt:'prm',$t:{t:Iterable,a:{Element$Iterable:{t:Integer},Absent$Iterable:'Absent$max'}}}],$cont:Integer,tp:{Absent$max:{sts:[{t:Null}]}},pa:4097,an:function(){return[doc$($CCMM$,'$:Integer:$m:max'),since("1.3.2")];},d:['$','Integer','$m','max']};};
Integer.$st$.min.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Integer},'Absent$min']},ps:[{nm:'integers',mt:'prm',$t:{t:Iterable,a:{Element$Iterable:{t:Integer},Absent$Iterable:'Absent$min'}}}],$cont:Integer,tp:{Absent$min:{sts:[{t:Null}]}},pa:4097,an:function(){return[doc$($CCMM$,'$:Integer:$m:min'),since("1.3.2")];},d:['$','Integer','$m','min']};};
Integer.$st$.smallest.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Integer},ps:[{nm:'x',mt:'prm',$t:{t:Integer}},{nm:'y',mt:'prm',$t:{t:Integer}}],$cont:Integer,pa:4097,an:function(){return[doc$($CCMM$,'$:Integer:$m:smallest'),since("1.3.2")];},d:['$','Integer','$m','smallest']};};
Integer.$st$.largest.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Integer},ps:[{nm:'x',mt:'prm',$t:{t:Integer}},{nm:'y',mt:'prm',$t:{t:Integer}}],$cont:Integer,pa:4097,an:function(){return[doc$($CCMM$,'$:Integer:$m:largest'),since("1.3.2")];},d:['$','Integer','$m','largest']};};

initTypeProto(Integer, 'ceylon.language::Integer', $_Object,$_Number, JSNumber,
        $init$Integral(), $init$Exponentiable(), $init$Binary());
Integer.$crtmm$=function(){return{an:function(){return[shared(),$_native(),$_final()];},mod:$CCMM$,d:['$','Integer']};}
function $init$Integer() {
  return Integer;
}
atr$(Integer.$$.prototype,'integer',function(){ return this; },undefined,
     function(){return {mod:$CCMM$,$t:{t:Integer},pa:0,$cont:Integer,d:['$','Integer','$at','integer$woput0']};});

function $$valueOf(x) {
  var xx = x.valueOf();
  return x.fmz$ ? -xx:xx;
}
function $$isNegZero(x) {
  return x==0 && 1/x<0;
}
function $$max(x, y) {
  if (isNaN(x)) return x;
  if (isNaN(y)) return y;
  return x>y || x==y && $$isNegZero(y) ? x : y;
}
function $$min(x, y) {
  if (isNaN(x)) return x;
  if (isNaN(y)) return y;
  return x<y || x==y && $$isNegZero(x) ? x : y;
}

function Float(value) {
    if (value!==null && value!==undefined && value.getT$name && value.getT$name() === 'ceylon.language::Float') {
        return value;
    }
    var that = new Number(value);
    that.float$ = true;
    return that;
}
Float.$st$={
  format:function(n,min,max,ds,ts) {
    if (min===undefined)min=1;
    if (max===undefined)max=9;
    if (ds===undefined)ds=Character(46);
    if (ts===undefined)ts=Character(44);
    return formatFloat(n,min,max,ds,ts);
  },
  parse:function(s) {
    return parseFloatInternal(s);
  },
  sum:function(f) {
    var s=0;
    var item;
    for (var iter=f.iterator();(item=iter.next())!==finished();) {
      s+=item.valueOf();
    }
    return Float(s);
  },
  product:function(i) {
    var s=1;
    var item;
    for (var iter=i.iterator();(item=iter.next())!==finished();) {
      s*=item.valueOf();
    }
    return Float(s);
  },
  max:function(i) {
    var f=true;
    var m=0;
    for (var iter=i.iterator();(item=iter.next())!==finished();) {
      if (f) {
        m=$$valueOf(item);
        f=false;
      } else m=$$max(m,$$valueOf(item));
    }
    return f?null:Float(m);
  },
  min:function(i) {
    var f=true;
    var m=0;
    for (var iter=i.iterator();(item=iter.next())!==finished();) {
      if (f) {
        m=$$valueOf(item);
        f=false;
      } else m=$$min(m,$$valueOf(item));
    }
    return f?null:Float(m);
  },
  smallest:function(x,y) {
    return Float($$min($$valueOf(x),$$valueOf(y)));
  },
  largest:function(x,y) {
    return Float($$max($$valueOf(x),$$valueOf(y)));
  }
};
Float.$st$.format.$crtmm$=function(){return{mod:$CCMM$,$t:{t:$_String},ps:[{nm:'float',mt:'prm',$t:{t:Float}},{nm:'minDecimalPlaces',mt:'prm',def:1,$t:{t:Integer}},{nm:'maxDecimalPlaces',mt:'prm',def:1,$t:{t:Integer}},{nm:'decimalSeparator',mt:'prm',def:1,$t:{t:Character}},{nm:'thousandsSeparator',mt:'prm',def:1,$t:{t:'u',l:[{t:Null},{t:Character}]}}],$cont:Float,pa:4097,an:function(){return[tagged($arr$sa$(["Numbers"],{t:$_String})),see($arr$sa$([OpenFunction$jsint(lmp$(ex$,'$'),$init$Float().$$.prototype.parse),OpenFunction$jsint(lmp$(ex$,'$'),Integer.$$.prototype.format)],{t:FunctionDeclaration$meta$declaration})),since("1.3.1")];},d:['$','Float','$m','format']};};
Float.$st$.parse.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Float},{t:ParseException}]},ps:[{nm:'string',mt:'prm',$t:{t:$_String}}],$cont:Float,pa:4097,an:function(){return[see($arr$sa$([OpenFunction$jsint(lmp$(ex$,'$'),$init$Float().$$.prototype.format),OpenFunction$jsint(lmp$(ex$,'$'),Integer.$$.prototype.parse)],{t:FunctionDeclaration$meta$declaration})),tagged($arr$sa$(["Numbers","Basic types"],{t:$_String})),since("1.3.1")];},d:['$','Float','$m','parse']};};
Float.$st$.sum.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Float},ps:[{nm:'floats',mt:'prm',$t:{t:Iterable,a:{Element$Iterable:{t:Float},Absent$Iterable:{t:Null}}}}],$cont:Float,pa:4097,d:['$','Float','$m','sum']};};
Float.$st$.product.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Float},ps:[{nm:'floats',mt:'prm',$t:{t:Iterable,a:{Element$Iterable:{t:Float},Absent$Iterable:{t:Null}}}}],$cont:Float,pa:4097,an:function(){return[m$1.doc$($CCMM$,'$:Float:$m:product'),since("1.3.2")];},d:['$','Float','$m','product']};};
Float.$st$.max.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Float},'Absent$max']},ps:[{nm:'floats',mt:'prm',$t:{t:Iterable,a:{Element$Iterable:{t:Float},Absent$Iterable:'Absent$max'}}}],$cont:Float,tp:{Absent$max:{sts:[{t:Null}]}},pa:4097,an:function(){return[doc$($CCMM$,'$:Float:$m:max'),since("1.3.2")];},d:['$','Float','$m','max']};};
Float.$st$.min.$crtmm$=function(){return{mod:$CCMM$,$t:{t:'u',l:[{t:Float},'Absent$min']},ps:[{nm:'floats',mt:'prm',$t:{t:Iterable,a:{Element$Iterable:{t:Float},Absent$Iterable:'Absent$min'}}}],$cont:Float,tp:{Absent$min:{sts:[{t:Null}]}},pa:4097,an:function(){return[doc$($CCMM$,'$:Float:$m:min'),since("1.3.2")];},d:['$','Float','$m','min']};};
Float.$st$.smallest.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Float},ps:[{nm:'x',mt:'prm',$t:{t:Float}},{nm:'y',mt:'prm',$t:{t:Float}}],$cont:Float,pa:4097,an:function(){return[doc$($CCMM$,'$:Float:$m:smallest'),since("1.3.2")];},d:['$','Float','$m','smallest']};};
Float.$st$.largest.$crtmm$=function(){return{mod:$CCMM$,$t:{t:Float},ps:[{nm:'x',mt:'prm',$t:{t:Float}},{nm:'y',mt:'prm',$t:{t:Float}}],$cont:Float,pa:4097,an:function(){return[doc$($CCMM$,'$:Float:$m:largest'),since("1.3.2")];},d:['$','Float','$m','largest']};};
initTypeProto(Float, 'ceylon.language::Float', $_Object,$_Number,$init$Exponentiable(), JSNumber);
Float.$crtmm$=function(){return{pa:97,mod:$CCMM$,d:['$','Float']};}
function $init$Float(){return Float;}
atr$(Float.$$.prototype,'integer',function(){ return Integer(Math.floor(this)); },undefined,
     function(){return {mod:$CCMM$,$t:{t:Integer},pa:65,$cont:Float,d:['$','Float','$at','integer']};});

var JSNum$proto = JSNumber.$$.prototype;
JSNum$proto.getT$all = function() {
    return (nflt$(this) ? Float : Integer).$$.T$all;
}
JSNum$proto.getT$name = function() {
    return (nflt$(this) ? Float : Integer).$$.T$name;
}

function $specialiseForNumber$(tipo,name,rtmm){
  var propname = '$prop$get'+name[0].toUpperCase()+name.substring(1);
  if(JSNum$proto[propname]){
    // hopefully a property
    var prop = JSNum$proto[propname];
    Object.defineProperty(tipo.$$.prototype, name, {get: prop.get, set: prop.set, configurable: true, enumerable: true});
    tipo.$$.prototype[propname] = {get: prop.get, set: prop.set, $crtmm$:rtmm};
    tipo.$$.prototype[propname].$crtmm$ = rtmm;
  }else{
    tipo.$$.prototype[name] = function(){ return JSNum$proto[name].call(this, arguments); };
    tipo.$$.prototype[name].$crtmm$ = rtmm;
  }
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
  if (s.indexOf('.')<0 && nflt$(this)
      && this != Infinity 
      && this != -Infinity 
      && !isNaN(this)) {
      if (s.indexOf('e')>0)
          s=s.replace('e','.0e');
      else
          s+='.0';
  }
  return s;
},undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Object,d:['$','Object','$at','string']};});
$specialiseForNumber$(Integer, 'string', function(){return {mod:$CCMM$,$t:{t:$_String},pa:67,$cont:Integer,d:['$','Integer','$at','string']};})
$specialiseForNumber$(Float, 'string', function(){return {mod:$CCMM$,$t:{t:$_String},pa:67,$cont:Float,d:['$','Float','$at','string']};})

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
    if (typeof(other)!=='number'&&other.constructor!==Number)throw new TypeError("Number expected");
    return (nflt$(this)||nflt$(other)) ? Float(this+other) : (this+other);
}
$addnm$('plus');
$specialiseForNumber$(Integer, 'plus', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','plus']};})
$specialiseForNumber$(Float, 'plus', function(){return {mod:$CCMM$,$t:{t:Float},pa:67,$cont:Float,ps:[{$t:{t:Float},nm:'other'}],d:['$','Float','$m','plus']};})

JSNum$proto.plusInteger = function(other) {
    return nflt$(this) ? Float(this+other) : (this+other);
}
$addnm$('plusInteger');
$specialiseForNumber$(Integer, 'plusInteger', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'integer'}],d:['$','Integer','$m','plusInteger']};})
$specialiseForNumber$(Float, 'plusInteger', function(){return {mod:$CCMM$,$t:{t:Float},pa:67,$cont:Float,ps:[{$t:{t:Integer},nm:'integer'}],d:['$','Float','$m','plusInteger']};})

JSNum$proto.minus = function(other) {
    if (typeof(other)!=='number'&&other.constructor!==Number)throw new TypeError("Number expected");
    return (nflt$(this)||nflt$(other)) ? Float(this-other) : (this-other);
}
$addnm$('minus');
$specialiseForNumber$(Integer, 'minus', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','minus']};})
$specialiseForNumber$(Float, 'minus', function(){return {mod:$CCMM$,$t:{t:Float},pa:67,$cont:Float,ps:[{$t:{t:Float},nm:'other'}],d:['$','Float','$m','minus']};})

JSNum$proto.times = function(other) {
  if (typeof(other)!=='number'&&other.constructor!==Number)throw new TypeError("Number expected");
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
$specialiseForNumber$(Integer, 'times', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','times']};})
$specialiseForNumber$(Float, 'times', function(){return {mod:$CCMM$,$t:{t:Float},pa:67,$cont:Float,ps:[{$t:{t:Float},nm:'other'}],d:['$','Float','$m','times']};})

JSNum$proto.timesInteger = function(other) {
  return nflt$(this) ? Float(this*other) : (this*other);
}
$addnm$('timesInteger');
$specialiseForNumber$(Integer, 'timesInteger', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'integer'}],d:['$','Integer','$m','timesInteger']};})
$specialiseForNumber$(Float, 'timesInteger', function(){return {mod:$CCMM$,$t:{t:Float},pa:67,$cont:Float,ps:[{$t:{t:Integer},nm:'integer'}],d:['$','Float','$m','timesInteger']};})

//Divide two integers
function i$div(a,b) {
  if (b===0) {
    throw Exception("Division by Zero");
  }
  return toInt(a/b);
}
ex$.i$div=i$div;
//Divide two floats
function f$div(a,b) {
  if (a.fmz$) {
    if (b == 0 || b.$_undefined) {
        return NaN;
    }
    return a;
  }
  var ret = Float(a/b);
  // make sure that if we expect a negative result, we get one, like 1/-0 -> -Infinity
  if(!a.negative && b.fmz$ && !ret.negative){
    ret = ret.negated;
  }
  return ret;
}
ex$.f$div=f$div;

JSNum$proto.divided=function(other) {
  if (typeof(other)!=='number'&&other.constructor!==Number)throw new TypeError("Number expected");
  if (this.fmz$) {
    if (other == 0 || other.$_undefined) {
        return NaN;
    }
    return this;
  }
  var otherFloat=nflt$(other);
  if (nflt$(this)||otherFloat) { 
    var ret = Float(this/other);
    // make sure that if we expect a negative result, we get one, like 1/-0 -> -Infinity
    if((!this.negative && other.fmz$ && !ret.negative)
        ||(!otherFloat&&ret==-Infinity&&!this.negative)){ ret = ret.negated; }
    return ret;
  }
  return i$div(this,other);
}
$addnm$('divided');
$specialiseForNumber$(Integer, 'divided', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','divided']};})
$specialiseForNumber$(Float, 'divided', function(){return {mod:$CCMM$,$t:{t:Float},pa:67,$cont:Float,ps:[{$t:{t:Float},nm:'other'}],d:['$','Float','$m','divided']};})

JSNum$proto.remainder = function(other) {
  if (other == 0) {
    throw Exception("Division by Zero");
  }
  if (typeof(other)!=='number'&&other.constructor!==Number)throw new TypeError("Number expected");
  return this%other;
}
$addnm$('remainder',Integral.$$.prototype.remainder);
$specialiseForNumber$(Integer, 'remainder', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','remainder']};})

JSNum$proto.modulo = function(modulus) {
    if (typeof(modulus)!=='number'&&modulus.constructor!==Number)throw new TypeError("Number expected");
    if (modulus < 0) {
        throw AssertionError("modulus must be positive: "+modulus);
    }
    var ret = this.remainder(modulus);
    if(ret < 0){
        return ret + modulus;
    }
    return ret;
}
$addnm$('modulo',Integral.$$.prototype.modulo);
$specialiseForNumber$(Integer, 'modulo', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','modulo']};})

JSNum$proto.divides=function(o){return o%this===0;}
$specialiseForNumber$(Integer, 'divides', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','divides']};})

JSNum$proto.power = function(exp) {
    if (typeof(exp)!=='number'&&exp.constructor!==Number)throw new TypeError("Number expected");
    if (nflt$(this)||nflt$(exp)) {
        if(this == 1.0)
            return this;
        if(this == -1.0 && (exp == Infinity || exp == -Infinity))
            return Float(1.0);
        var ret = Float(Math.pow(this, exp));
        if(this == 0.0 && this.fmz$ && exp.fractionalPart == 0 && !exp.wholePart.even)
            ret = ret.negated;
        return ret;
    }
    if (exp<0 && this!=1 && this!=-1) {
        throw AssertionError("Negative exponent");
    }
    return toInt(Math.pow(this, exp));
}
JSNum$proto.$fpower=function(exp){
  if (typeof(exp)!=='number'&&exp.constructor!==Number)throw new TypeError("Number expected");
  if (nflt$(exp)) {
    if(this == 1.0)
        return this;
    if(this == -1.0 && (exp == Infinity || exp == -Infinity))
        return Float(1.0);
    var ret = Float(Math.pow(this, exp));
    if(this == 0.0 && this.fmz$ && exp.fractionalPart == 0 && !exp.wholePart.even)
        ret = ret.negated;
    return ret;
  }
  return Float(Math.pow(this, exp));
}
$addnm$('power',Exponentiable.$$.prototype.power);
$specialiseForNumber$(Integer, 'power', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','power']};})
$specialiseForNumber$(Float, 'power', function(){return {mod:$CCMM$,$t:{t:Float},pa:67,$cont:Float,ps:[{$t:{t:Float},nm:'other'}],d:['$','Float','$m','power']};})

JSNum$proto.powerOfInteger = function(exp) {
    if (nflt$(this)) {
        if(this == 1.0)
            return this;
        var ret = Float(Math.pow(this, exp));
        if(this == 0.0 && this.fmz$ && exp.fractionalPart == 0 && !exp.wholePart.even)
            ret = ret.negated;
        return ret;
    }
    if (exp<0 && this!=1 && this!=-1) {
        throw AssertionError("Negative exponent");
    }
    return toInt(Math.pow(this, exp));
}
$addnm$('powerOfInteger');
$specialiseForNumber$(Integer, 'powerOfInteger', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'integer'}],d:['$','Integer','$m','powerOfInteger']};})
$specialiseForNumber$(Float, 'powerOfInteger', function(){return {mod:$CCMM$,$t:{t:Float},pa:67,$cont:Float,ps:[{$t:{t:Integer},nm:'integer'}],d:['$','Float','$m','powerOfInteger']};})

atr$(JSNum$proto, 'negated', function() {
  if (this.valueOf()==0) {
    if (this.float$) {
      var f=Float(0.0);
      if (!this.fmz$)f.fmz$=true;
      return f;
    }
    return 0;
  }
  return nflt$(this) ? Float(-this) : -this;
},undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:Invertible,d:['$','Invertible','$at','negated']};});
$specialiseForNumber$(Integer, 'negated', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,d:['$','Integer','$at','negated']};})
$specialiseForNumber$(Float, 'negated', function(){return {mod:$CCMM$,$t:{t:Float},pa:67,$cont:Float,d:['$','Float','$at','negated']};})

atr$(JSNum$proto, 'negative', function(){
  return nflt$(this) ? this < 0.0 || this == -Infinity : this.valueOf() < 0;
},undefined,function(){return{$t:{t:$_Boolean},an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','negative']};});
$specialiseForNumber$(Integer, 'negative', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Integer,d:['$','Integer','$at','negative']};})
$specialiseForNumber$(Float, 'negative', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Float,d:['$','Float','$at','negative']};})

atr$(JSNum$proto, 'positive', function(){
  return nflt$(this) ? this > 0.0 : this.valueOf() > 0;
},undefined,function(){return{$t:{t:$_Boolean},an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','positive']};});
$specialiseForNumber$(Integer, 'positive', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Integer,d:['$','Integer','$at','positive']};})
$specialiseForNumber$(Float, 'positive', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Float,d:['$','Float','$at','positive']};})

JSNum$proto.equals=function(other){
  if(typeof(other)==='number' || other.constructor===Number){
    if (this>runtime().maxIntegerValue||this<runtime().minIntegerValue
        ||other>runtime().maxIntegerValue||other<runtime.minIntegerValue) {
      return other==this.valueOf() && other.float$===this.float$;
    }
    return other==this.valueOf();
  }
  return false;
}
$addnm$('equals',$_Object.$$.prototype.equals);
$specialiseForNumber$(Integer, 'equals', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Integer,ps:[{$t:{t:$_Object},nm:'that'}],d:['$','Integer','$m','equals']};})
$specialiseForNumber$(Float, 'equals', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Float,ps:[{$t:{t:$_Object},nm:'that'}],d:['$','Float','$m','equals']};})

JSNum$proto.compare = function(other) {
    if (typeof(other)!=='number'&&other.constructor!==Number)throw new TypeError("Number expected");
    var value = this.valueOf();
    if (Number.isNaN(value) || Number.isNaN(other)) throw Exception("NaN is not comparable");
    return value==other ? equal() : (value<other ? smaller():larger());
}
$addnm$('compare',Comparable.$$.prototype.compare);
$specialiseForNumber$(Integer, 'compare', function(){return {mod:$CCMM$,$t:{t:Comparison},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','compare']};})
$specialiseForNumber$(Float, 'compare', function(){return {mod:$CCMM$,$t:{t:Comparison},pa:67,$cont:Float,ps:[{$t:{t:Float},nm:'other'}],d:['$','Float','$m','compare']};})

JSNum$proto.smallerThan=function(o) {
  if (typeof(o)!=='number'&&o.constructor!==Number)throw new TypeError("Number expected");
  return this<o;
}
$addnm$('smallerThan',Comparable.$$.prototype.smallerThan);
$specialiseForNumber$(Integer, 'smallerThan', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','smallerThan']};})
$specialiseForNumber$(Float, 'smallerThan', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Float,ps:[{$t:{t:Float},nm:'other'}],d:['$','Float','$m','smallerThan']};})

JSNum$proto.largerThan=function(o) {
  if (typeof(o)!=='number'&&o.constructor!==Number)throw new TypeError("Number expected");
  return this>o;
}
$addnm$('largerThan',Comparable.$$.prototype.largerThan);
$specialiseForNumber$(Integer, 'largerThan', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','largerThan']};})
$specialiseForNumber$(Float, 'largerThan', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Float,ps:[{$t:{t:Float},nm:'other'}],d:['$','Float','$m','largerThan']};})

JSNum$proto.notSmallerThan=function(o) {
  if (typeof(o)!=='number'&&o.constructor!==Number)throw new TypeError("Number expected");
  return this>=o;
}
$addnm$('notSmallerThan',Comparable.$$.prototype.notSmallerThan);
$specialiseForNumber$(Integer, 'notSmallerThan', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','notSmallerThan']};})
$specialiseForNumber$(Float, 'notSmallerThan', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Float,ps:[{$t:{t:Float},nm:'other'}],d:['$','Float','$m','notSmallerThan']};})

JSNum$proto.notLargerThan=function(o) {
  if (typeof(o)!=='number'&&o.constructor!==Number)throw new TypeError("Number expected");
  return this<=o;
}
$addnm$('notLargerThan',Comparable.$$.prototype.notLargerThan);
$specialiseForNumber$(Integer, 'notSmallerThan', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'other'}],d:['$','Integer','$m','notSmallerThan']};})
$specialiseForNumber$(Float, 'notLargerThan', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Float,ps:[{$t:{t:Float},nm:'other'}],d:['$','Float','$m','notLargerThan']};})

atr$(JSNum$proto, '$_float', function(){
  if (this.magnitude>runtime().maxExactIntegralFloat) {
    throw OverflowException(this+' cannot be coerced into a 64 bit floating point value');
  }
  return Float(this.valueOf());
},undefined,function(){return{pa:65,mod:$CCMM$,$t:{t:Float},$cont:Integer,d:['$','Integer','$at','float']};});
$specialiseForNumber$(Integer, '$_float', function(){return {mod:$CCMM$,$t:{t:Float},pa:67,$cont:Integer,d:['$','Integer','$at','float']};})
// its (private) constructor parameter
$specialiseForNumber$(Float, '$_float', function(){return {mod:$CCMM$,$t:{t:Float},pa:0,$cont:Float,d:['$','Float','$at','float$oirx2o']};})

atr$(JSNum$proto, 'integer', function(){ return toInt(this); },
  undefined,function(){return{pa:65,mod:$CCMM$,$t:{t:Integer},$cont:$_Number,d:['$','Float','$at','integer']};});

atr$(JSNum$proto, '$_byte', function(){ return Byte(this); },
  undefined,function(){return{pa:65,mod:$CCMM$,$t:{t:Byte},$cont:Integer,d:['$','Integer','$at','byte']};});
$specialiseForNumber$(Integer, '$_byte', function(){return {mod:$CCMM$,$t:{t:Byte},pa:67,$cont:Integer,d:['$','Integer','$at','byte']};})

atr$(JSNum$proto, 'character', function(){
  var c=this.valueOf();
  if (c < 0 || c > 1114111) {
    throw OverflowException(c + ' is not a possible Unicode code point');
  }
  return Character(c);
},undefined,function(){return{pa:65,mod:$CCMM$,$t:{t:Character},$cont:Integer,d:['$','Integer','$at','character']};});
$specialiseForNumber$(Integer, 'character', function(){return {mod:$CCMM$,$t:{t:Character},pa:67,$cont:Integer,d:['$','Integer','$at','character']};})

atr$(JSNum$proto, 'successor', function(){ return this+1; },
  undefined,function(){return{pa:67,mod:$CCMM$,$t:{t:Integer},$cont:Integer,d:['$','Integer','$at','successor']};});
$specialiseForNumber$(Integer, 'successor', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,d:['$','Integer','$at','successor']};})

atr$(JSNum$proto, 'predecessor', function(){ return this-1; },
  undefined,function(){return{pa:67,mod:$CCMM$,$t:{t:Integer},$cont:Integer,d:['$','Integer','$at','predecessor']};});
$specialiseForNumber$(Integer, 'predecessor', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,d:['$','Integer','$at','predecessor']};})

atr$(JSNum$proto, 'unit', function(){ return this == 1; },
  undefined,function(){return{pa:67,mod:$CCMM$,$t:{t:$_Boolean},$cont:Integer,d:['$','Integer','$at','unit']};});
$specialiseForNumber$(Integer, 'unit', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Integer,d:['$','Integer','$at','unit']};})

atr$(JSNum$proto, 'zero', function(){ return this == 0; },
  undefined,function(){return{pa:67,mod:$CCMM$,$t:{t:$_Boolean},$cont:Integer,d:['$','Integer','$at','zero']};});
$specialiseForNumber$(Integer, 'zero', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Integer,d:['$','Integer','$at','zero']};})

atr$(JSNum$proto, 'even', function(){ return this%2 == 0; },
  undefined,function(){return{pa:65,mod:$CCMM$,$t:{t:$_Boolean},$cont:Integer,d:['$','Integer','$at','even']};});
$specialiseForNumber$(Integer, 'even', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:65,$cont:Integer,d:['$','Integer','$at','even']};})

atr$(JSNum$proto, 'fractionalPart', function() {
    if (!nflt$(this)) { return 0; }
    return Float(this - toInt(this));
},undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','fractionalPart']};});
$specialiseForNumber$(Integer, 'fractionalPart', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,d:['$','Integer','$at','fractionalPart']};})
$specialiseForNumber$(Float, 'fractionalPart', function(){return {mod:$CCMM$,$t:{t:Float},pa:67,$cont:Float,d:['$','Float','$at','fractionalPart']};})

atr$(JSNum$proto, 'wholePart', function() {
    if (!nflt$(this)) { return this.valueOf(); }
    var ret = this >= 0 ? Math.floor(this) : Math.ceil(this);
    var wret = Float(ret);
    if(ret == 0 && (this < 0 || this.fmz$)){ wret.fmz$ = true; }
    return wret;
},undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','wholePart']};});
$specialiseForNumber$(Integer, 'wholePart', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,d:['$','Integer','$at','wholePart']};})
$specialiseForNumber$(Float, 'wholePart', function(){return {mod:$CCMM$,$t:{t:Float},pa:67,$cont:Float,d:['$','Float','$at','wholePart']};})

atr$(JSNum$proto, 'sign', function(){ return this > 0 ? 1 : this < 0 ? -1 : 0; },
  undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','sign']};});
$specialiseForNumber$(Integer, 'sign', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,d:['$','Integer','$at','sign']};})
$specialiseForNumber$(Float, 'sign', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Float,d:['$','Float','$at','sign']};})

atr$(JSNum$proto, 'hash', function() {
    return nflt$(this) ? $_String(this.toPrecision()).hash : this.valueOf();
},undefined,function(){return{mod:$CCMM$,pa:64,$cont:Integer,d:['$','Number','$at','hash']};});
$specialiseForNumber$(Integer, 'hash', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,d:['$','Integer','$at','hash']};})
$specialiseForNumber$(Float, 'hash', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Float,d:['$','Float','$at','hash']};})

// FIXME: I think this one is just gone from the language module
JSNum$proto.distanceFrom = function(other) {
    return (nflt$(this) ? this.wholePart : this) - other;
}

//Binary interface
atr$(JSNum$proto, 'not', function(){ return ~this; },
  undefined,function(){return{pa:64,mod:$CCMM$,$cont:Binary,d:['$','Integer','$at','not']};});
$specialiseForNumber$(Integer, 'not', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,d:['$','Integer','$at','not']};})

JSNum$proto.leftLogicalShift = function(i) { return this << i; }
$addnm$('leftLogicalShift',Binary.$$.prototype.leftLogicalShift);
$specialiseForNumber$(Integer, 'leftLogicalShift', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'shift'}],d:['$','Integer','$m','leftLogicalShift']};})

JSNum$proto.rightLogicalShift = function(i) { return this >>> i; }
$addnm$('rightLogicalShift',Binary.$$.prototype.rightLogicalShift);
$specialiseForNumber$(Integer, 'rightLogicalShift', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'shift'}],d:['$','Integer','$m','rightLogicalShift']};})

JSNum$proto.rightArithmeticShift = function(i) { return this >> i; }
$addnm$('rightArithmeticShift',Binary.$$.prototype.rightArithmeticShift);
$specialiseForNumber$(Integer, 'rightArithmeticShift', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'shift'}],d:['$','Integer','$m','rightArithmeticShift']};})

JSNum$proto.and = function(x) { return this & x; }
$addnm$('and',Binary.$$.prototype.and);
$specialiseForNumber$(Integer, 'and', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'index'}],d:['$','Integer','$m','and']};})

JSNum$proto.or = function(x) { return this | x; }
$addnm$('or',Binary.$$.prototype.or);
$specialiseForNumber$(Integer, 'or', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'index'}],d:['$','Integer','$m','or']};})

JSNum$proto.xor = function(x) { return this ^ x; }
$addnm$('xor',Binary.$$.prototype.xor);
$specialiseForNumber$(Integer, 'xor', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'index'}],d:['$','Integer','$m','xor']};})

JSNum$proto.$_get = function(idx) {
    if (idx < 0 || idx >31) {
        return false;
    } 
    var mask = 1 << idx;
    return (this & mask) != 0 ? true : false;
}
$addnm$('$_get',Binary.$$.prototype.$_get);
$specialiseForNumber$(Integer, 'get', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'index'}],d:['$','Integer','$m','get']};})

JSNum$proto.set=function(idx,bit) {
  if (idx<0 || idx>31) {
    return this|0;
  } 
  if (bit === undefined) { bit = true; }
  var mask = 1 << idx;
  return (bit === true) ? this|mask : this & ~mask;
}
$addnm$('set',Binary.$$.prototype.set);
$specialiseForNumber$(Integer, 'set', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'index'},{$t:{t:$_Boolean},nm:'bit'}],d:['$','Integer','$m','set']};})

JSNum$proto.flip = function(idx) {
    if (idx < 0 || idx >31) {
        return this|0;
    } 
    var mask = 1 << idx;
    return this ^ mask;
}
$addnm$('flip',Binary.$$.prototype.flip);
$specialiseForNumber$(Integer, 'flip', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'index'}],d:['$','Integer','$m','flip']};})

JSNum$proto.clear = function(index) {
    return this.set(index, false);
}
$addnm$('clear',Binary.$$.prototype.clear);
$specialiseForNumber$(Integer, 'clear', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'index'}],d:['$','Integer','$m','clear']};})

function checkintoverflow$(a){
  if(a>runtime().maxIntegerValue || a<runtime().minIntegerValue)throw OverflowException();
  return a;
}
JSNum$proto.neighbour=function(offset) {
  return checkintoverflow$(this+offset);
}
$addnm$('neighbour',Enumerable.$$.prototype.neighbour);
$specialiseForNumber$(Integer, 'neighbour', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'offset'}],d:['$','Integer','$m','neighbour']};})

JSNum$proto.offset=function(other) {
  return checkintoverflow$(this-other);
}
$addnm$('offset',Enumerable.$$.prototype.offset);
$specialiseForNumber$(Integer, 'offset', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'offset'}],d:['$','Integer','$m','offset']};})

JSNum$proto.offsetSign=function(other) {
  var v = this.valueOf();
  var o = other.valueOf();
  if (v>o)return 1;
  if (v<o)return -1;
  return 0;
}
$addnm$('offsetSign',Enumerable.$$.prototype.offsetSign);
$specialiseForNumber$(Integer, 'offsetSign', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,ps:[{$t:{t:Integer},nm:'offset'}],d:['$','Integer','$m','offsetSign']};})

atr$(JSNum$proto,'magnitude',function(){
  var m=Math.abs(this);
  if (nflt$(this))m=Float(m);
  return m;
},undefined,function(){return{an:function(){return[shared(),actual()]},mod:$CCMM$,$cont:$_Number,d:['$','Number','$at','magnitude']};});
$specialiseForNumber$(Integer, 'magnitude', function(){return {mod:$CCMM$,$t:{t:Integer},pa:67,$cont:Integer,d:['$','Integer','$at','magnitude']};})

atr$(JSNum$proto, '$_undefined', function(){ return isNaN(this); },
  undefined,function(){return{pa:1,mod:$CCMM$,$cont:Float,d:['$','Float','$at','undefined']};});
$specialiseForNumber$(Float, '$_undefined', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Float,d:['$','Float','$at','undefined']};})

atr$(JSNum$proto, 'finite', function(){ return this!=Infinity && this!=-Infinity && !isNaN(this); },
  undefined,function(){return{pa:1,mod:$CCMM$,$t:{t:$_Boolean},$cont:Float,d:['$','Float','$at','finite']};});
$specialiseForNumber$(Float, 'finite', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Float,d:['$','Float','$at','finite']};})
  
atr$(JSNum$proto, 'infinite', function(){ return this==Infinity || this==-Infinity; },
  undefined,function(){return{pa:67,mod:$CCMM$,$cont:Float,d:['$','Float','$at','infinite']};});
$specialiseForNumber$(Float, 'infinite', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:67,$cont:Float,d:['$','Float','$at','infinite']};})

atr$(JSNum$proto, 'strictlyPositive', function(){ return this>0 || !this.fmz$ && this==0 && 1/this>0; },
  undefined,function(){return{pa:65,mod:$CCMM$,$cont:Float,d:['$','Float','$at','strictlyPositive']};});
$specialiseForNumber$(Float, 'strictlyPositive', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:65,$cont:Float,d:['$','Float','$at','strictlyPositive']};})
  
atr$(JSNum$proto, 'strictlyNegative', function(){ return this<0 || this.fmz$ || (this==0 && 1/this<0); },
  undefined,function(){return{pa:65,mod:$CCMM$,$cont:Float,d:['$','Float','$at','strictlyNegative']};});
$specialiseForNumber$(Float, 'strictlyNegative', function(){return {mod:$CCMM$,$t:{t:$_Boolean},pa:65,$cont:Float,d:['$','Float','$at','strictlyNegative']};})
  
atr$(JSNum$proto, 'nearestFloat', function(){ return Float(this.valueOf()); },
  undefined,function(){return{pa:65,mod:$CCMM$,$cont:Integer,d:['$','Integer','$at','nearestFloat']};});
$specialiseForNumber$(Integer, 'nearestFloat', function(){return {mod:$CCMM$,$t:{t:Float},pa:65,$cont:Integer,d:['$','Integer','$at','nearestFloat']};})

ex$.Integer=Integer;
ex$.Float=Float;

nativeJSParseFloat=parseFloat;
