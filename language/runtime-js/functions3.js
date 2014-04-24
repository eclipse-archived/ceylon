function addSuppressedException(/*Exception*/sup,/*Exception*/e) {
    if (e.$sups$===undefined) {
        e.$sups$=[];
    }
    if (sup.getT$name === undefined) sup = NativeException(sup);
    e.$sups$.push(sup);
}
exports.addSuppressedException=addSuppressedException;
function suppressedExceptions(/*Exception*/e) {
    return e.$sups$===undefined?getEmpty():e.$sups$;
}
suppressedExceptions.$crtmm$=function(){
  return {mod:$CCMM$,d:['ceylon.language','suppressedExceptions'],$t:{t:Sequential,a:{Absent$Iterable:{t:Null},Element$Iterable:{t:Exception}}},$ps:[{$nm:'exception',$mt:'prm',$t:{t:Exception}}]};
}
exports.suppressedExceptions=suppressedExceptions;

function $retuple(t) { //receives {t:'T',l:[...]}
  if (t.t!=='T')return t;
  var e;
  var r={t:Empty};
  for (var i=t.l.length-1;i>=0;i--){
    var f=$retuple(t.l[i]);
    var e=(r.a&&r.a.Element$Tuple)||f;
    if (r.a&&r.a.Element$Tuple){
      if (e.l) {
        var l2=[];for(var j=0;j<e.l.length;j++)l2.push(e.l[j]);
        l2.unshift(f);
        e={t:'u',l:l2};
      } else {
        e = {t:'u',l:[f,e]};
      }
    }
    r={t:Tuple,a:{First$Tuple:f,Element$Tuple:e,Rest$Tuple:r}};
  }
  return r;
}
function validate$params(ps,t,msg) {
  if (t.t===Nothing)return;
  if (!ps || ps.length==0) {
    if (t.t===Empty)return;
  } else if (t.t==='T') {
    if (ps.length==t.l.length) {
      //TODO check each parameter
      for (var i=0;i<ps.length;i++)
        if (!extendsType(t.l[i],ps[i].$t))throw IncompatibleTypeException$meta$model(msg);
      return;
    }
  } else { //it's already a tuple, navigate it
    console.log("TODO!!!! validate$params with Tuple type");
  }
  throw IncompatibleTypeException$meta$model(msg);
}
function validate$typeparams(t,tparms,types) {
  if (tparms) {
    if (types===undefined||types.size<1)
      throw TypeApplicationException$meta$model(String$("Missing type arguments"));
    var i=0;
    t.a={};
    for (var tp in tparms) {
      var _type=types.$get(i);
      if (_type===undefined)
        throw TypeApplicationException$meta$model(String$("Missing type argument for " + tp));
      var _tp = tparms[tp];
      var _ta = _type.tipo;
      t.a[tp]= _ta.t ? _ta : {t:_type.tipo};
      if ((_tp.satisfies && _tp.satisfies.length>0) || (_tp.of && _tp.of.length > 0)) {
        var restraints=(_tp.satisfies && _tp.satisfies.length>0)?_tp.satisfies:_tp.of;
        for (var j=0; j<restraints.length;j++) {
          var _r=restraints[j];if(typeof(_r)==='function')_r=getrtmm$$(_r).$t;
          if (!extendsType(t.a[tp],_r))
            throw TypeApplicationException$meta$model(String$("Type argument for " + tp + " violates type parameter constraints"));
        }
      }
      i++;
    }
  }
}
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
    if (ps[i].$def) {
      tupa={t:'u',l:[{t:Empty},tupa]};
    }
  }
  return tupa;
}
function $qname(mm) {
  if (mm.t) {
    mm=mm.t;
  }
  if (mm.$crtmm$)mm=getrtmm$$(mm);
  if (!mm.d)return "[unnamed type]";
  var qn=mm.d[0];
  if (qn==='$')qn='ceylon.language';
  for (var i=1; i<mm.d.length; i++){
    var n=mm.d[i];
    var p=n.indexOf('$');
    if(p!==0)qn+=(i==1?"::":".")+(p>0?n.substring(0,p):n);
  }
  return qn;
}
function resolve$typearg(ta,mm) {
  var r=mm.$tp?mm.$tp[ta]:undefined;
  while (!r && mm.$cont) {
    mm=mm.$cont;
    if (mm.$tp)r=mm.$tp[ta];
  }
  if (r) {
    if (r.satisfies)
      return r.satisfies.length==1?r.satisfies[0]:{t:'i',l:r.satisfies};
    return {t:Anything};
  }
  console.log("MISSING definition of type argument " + ta + " in " + $qname(mm));
  return {t:Anything};
}

function convert$params(mm,a) {
  var ps=mm.$ps;
  if (ps===undefined || ps.length===0){
    if (a && a.size>0)
      throw InvocationException$meta$model(String$("Passing parameters to no-args callable"));
    return [];
  }
  if (a===undefined)a=[];
  var fa=[];
  var sarg;
  for (var i=0; i<ps.length;i++) { //check def/seq params
    var p=ps[i];
    var val_t=sarg?sarg.$$targs$$.a.Element$Iterable:p.$t,mm;
    if (typeof(val_t)==='string')val_t=resolve$typearg(val_t,mm);
    if (a[i]===undefined) {
      if (p.$def||p.seq)fa.push(undefined);
      else {
        throw InvocationException$meta$model(String$("Wrong number of arguments (should be " + ps.length + ")"));
      }
    } else if (sarg) {
      sarg.push(a[i]);
    } else if (p.seq) {
      sarg=[].reifyCeylonType(p.$t); fa.push(sarg);
      val_t=sarg.$$targs$$.a.Element$Sequential;
      if (typeof(val_t)==='string')val_t=resolve$typearg(val_t,mm);
      for (var j=i; j<a.size;j++){
        if (!$is(a[j],val_t))throw IncompatibleTypeException$meta$model("Wrong type for argument " + j + ", expected " + typeLiteral$meta({Type$typeLiteral:val_t}).string + " got " + className(a[j]));
        sarg.push(a[j]);
      }
      i=j;
    } else {
      fa.push(a[i]);
    }
    if (a[i]!==undefined && !$is(a[i],val_t))throw IncompatibleTypeException$meta$model("Wrong type for argument " + i + ", expected " + typeLiteral$meta({Type$typeLiteral:val_t}).string + " got " + className(a[i]));
  }
  if (a.size>i)throw InvocationException$meta$model("Too many arguments");
  a = fa;
  return a;
}

function getrtmm$$(x) {
  if (x===undefined||x===null)return undefined;
  if (typeof(x.$crtmm$)==='function')x.$crtmm$=x.$crtmm$();
  return x.$crtmm$;
}
exports.getrtmm$$=getrtmm$$;
