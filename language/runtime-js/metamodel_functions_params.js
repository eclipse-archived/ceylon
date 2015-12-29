//Validate parameters:
//ps are the params defined in the metamodel
//t is the tuple with parameters to pass
function validate$params(ps,t,msg,nothrow) {
  if (t.t===Nothing)return true;
  if (!ps || ps.length==0) {
    if (t.t===Empty)return true;
  } else if (t.t==='T') {
    if (ps.length==t.l.length) {
      //TODO check each parameter
      for (var i=0;i<ps.length;i++) {
        if (!extendsType(t.l[i],ps[i].$t)) {
          if (nothrow)return false;else throw IncompatibleTypeException$meta$model(msg);
        }
      }
      return true;
    }
  } else if (t.t===Empty) {
    if (!ps || ps.length===0)return true;
    var countDefs=0;
    for (var i=0;i<ps.length;i++)if (ps[i].def)countDefs++;
    if (countDefs===ps.length)return true;
  } else if (t.t==='u' && ps.length===1) {
    if (extendsType(ps[0].$t,t))return true;
  } else if (t.t===Sequential || t.t===Sequence) {
    if (extendsType(ps[0].$t,t.a.Element$Sequential||t.a.Element$Sequence))return true;
  } else { //it's already a tuple, navigate it
    console.trace("TODO!!!! validate$params with Tuple type");
  }
  if (nothrow)return false;
  throw IncompatibleTypeException$meta$model(msg);
}
function validate$typeparams(t,tparms,types) {
  if (tparms) {
    if (types===undefined||types.size<1)
      throw TypeApplicationException$meta$model("Missing type arguments");
    if (!types.$_get)types=types.sequence();
    var i=0;
    t.a={};
    for (var tp in tparms) {
      var _type=types.$_get(i);
      if (_type===undefined)
        throw TypeApplicationException$meta$model("Missing type argument for " + tp);
      var _tp = tparms[tp];
      var _ta = _type===nothingType$meta$model()?{t:Nothing}:_type.tipo;
      t.a[tp]= _ta.t ? _ta : {t:_type.tipo};
      if ((_tp.sts && _tp.sts.length>0) || (_tp.of && _tp.of.length > 0)) {
        var restraints=(_tp.sts && _tp.sts.length>0)?_tp.sts:_tp.of;
        for (var j=0; j<restraints.length;j++) {
          var _r=restraints[j];if(typeof(_r)==='function')_r=getrtmm$$(_r).$t;
          if (!extendsType(t.a[tp],_r))
            throw TypeApplicationException$meta$model("Type argument for " + tp + " violates type parameter constraints");
        }
      }
      i++;
    }
  }
}
//Convert an array of parameters (value of ps:[]) into a Tuple
function tupleize$params(ps,aux) {
  if (!ps || ps.length==0)return {t:Empty};
  var tupa={t:'T',l:[]};
  for (var i=ps.length-1; i>=ps.length;i--) {
    var e=ps[i].$t;
    if (typeof(e)==='string'&&aux&&aux[e])e=aux[e];
    if (tupa.t==='T') {//tuple
      tupa.l.unshift(e);
    } else { //union
      tupa={t:'T',l:[e,tupa]};
    }
    if (ps[i].def) {
      tupa={t:'u',l:[{t:Empty},tupa]};
    }
  }
  return tupa;
}
//Resolve a type argument by looking into the metamodel,
//as well as the type arguments provided (if any)
function resolve$typearg(ta,mm,$$targs$$) {
  var r;
  if ($$targs$$ && $$targs$$[ta]) {
    r=$$targs$$[ta];
    if (typeof(r)!=='string')return r;
  }
  r=mm.tp?mm.tp[ta]:undefined;
  while (!r && mm.$cont) {
    mm=mm.$cont;
    if (mm.tp)r=mm.tp[ta];
  }
  if (r) {
    if (r.sts)
      return r.sts.length==1?r.sts[0]:{t:'i',l:r.sts};
    return {t:Anything};
  }
  console.log("MISSING definition of type argument " + ta + " in " + qname$(mm));
  return {t:Anything};
}

function convert$params(mm,a,$$targs$$) {
  function sequenceToArray(as) {
    if (is$(as,{t:Sequential})) {
      var _a=[];
      for (var i=0;i<as.size;i++)_a.push(as.$_get(i));
      return _a;
    }
    return as;
  }
  var ps=mm.ps;
  if (ps===undefined || ps.length===0){
    if (a && a.size>0)
      throw InvocationException$meta$model("Passing parameters to no-args callable");
    return [];
  }
  //Convert to a native array
  if (a===undefined) {
    a=[];
  } else if (a.nativeArray) {
    a=a.nativeArray();
  } else if (a.arr$) {
    a=a.arr$;
  } else {
    a=sequenceToArray(a);
  }
  var type0;
  if (a.length===1 && (Array.isArray(a[0]) || is$(a[0],{t:Sequential}))
      && (ps.length!=1 || (!extendsType((type0=restype2$(ps[0].$t,$$targs$$)),{t:Sequential}) && !is$(a[0],type0)))) {
    //We sometimes get an array with a single array (double wrapping)
    a=sequenceToArray(a[0]);
  }
  var fa=[],sarg;
  for (var i=0; i<ps.length;i++) { //check def/seq params
    var p=ps[i];
    var val_t=restype2$(sarg?sarg.$$targs$$.a.Element$Iterable:p.$t,$$targs$$);
    if (typeof(val_t)==='string')val_t=resolve$typearg(val_t,mm,$$targs$$);
    if (a[i]===undefined) {
      if (p.def||p.seq) {
        if (p.seq && p.$t.t===Sequence)
          throw InvocationException$meta$model("Not enough arguments to function. Expected 1 but got only 0");
        fa.push(undefined);
      } else {
        throw InvocationException$meta$model("Wrong number of arguments (should be " + ps.length + ")");
      }
    } else if (sarg) {
      sarg.push(a[i]);
    } else if (p.seq) {
      for (var eta in p.$t.a)if(eta.startsWith("Element$"))val_t=p.$t.a[eta];
      if (typeof(val_t)==='string')val_t=resolve$typearg(val_t,mm,$$targs$$);
      if (is$(a[i],{t:Iterable,a:{Element$Iterable:val_t}})) {
        fa.push(a[i]);
        val_t={t:Iterable,a:{Element$Iterable:val_t}};
      } else {
        sarg=[];
        for (var j=i; j<a.length;j++){
          if (!is$(a[j],val_t))throw IncompatibleTypeException$meta$model("Wrong type for argument " + j + ", expected " + typeLiteral$meta({Type$typeLiteral:val_t},$$targs$$).string + " got " + className(a[j]));
          sarg.push(a[j]);
        }
        fa.push(sarg.$sa$(val_t));
        i=j;
      }
    } else {
      fa.push(a[i]);
    }
    if (a[i]!==undefined && !is$(a[i],val_t))throw IncompatibleTypeException$meta$model("Wrong type for argument " + i + ", expected " + typeLiteral$meta({Type$typeLiteral:val_t},$$targs$$).string + " got " + className(a[i]));
  }
  if (a.length>i)throw InvocationException$meta$model("Too many arguments");
  a = fa;
  return a;
}
