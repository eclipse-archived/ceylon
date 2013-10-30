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
suppressedExceptions.$$metamodel$$=function(){
  return {mod:$$METAMODEL$$,d:['ceylon.language','suppressedExceptions'],$t:{t:Sequential,a:{Absent:{t:Null},Element:{t:Exception}}},$ps:[{$nm:'exception',$mt:'prm',$t:{t:Exception}}]};
}
exports.suppressedExceptions=suppressedExceptions;

function $retuple(t) { //receives {t:'T',l:[...]}
  if (t.t!=='T')return t;
  var e;
  var r={t:Empty};
  for (var i=t.l.length-1;i>=0;i--){
    var f=$retuple(t.l[i]);
    var e=(r.a&&r.a.Element)||f;
    if (r.a&&r.a.Element) {
      if (e.l) {
        var l2=[];for(var j=0;j<e.l.length;j++)l2.push(e.l[j]);
        l2.unshift(f);
        e={t:'u',l:l2};
      } else {
        e = {t:'u',l:[f,e]};
      }
    }
    r={t:Tuple,a:{First:f,Element:e,Rest:r}};
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
      t.a[tp]= ta.t ? ta : {t:_type.tipo};
      if ((_tp.satisfies && _tp.satisfies.length>0) || (_tp.of && _tp.of.length > 0)) {
        var restraints=(_tp.satisfies && _tp.satisfies.length>0)?_tp.satisfies:_tp.of;
        for (var j=0; j<restraints.length;j++) {
          if (!extendsType(t.a[tp],restraints[j]))
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
  for (var i=0; i<ps.length;i++){
    var e=ps[i].$t;
    if (typeof(e)==='string'&&aux&&aux[e])e=aux[e];
    tupa.l.push(e);
  }
  return tupa;
}
function $qname(mm) {
  if (mm.t) {
    mm=mm.t;
  }
  if (typeof(mm.$$metamodel$$)==='function')mm.$$metamodel$$=mm.$$metamodel();
  if (mm.$$metamodel$$)mm=mm.$$metamodel$$;
  var qn=mm.d[0];
  for (var i=1; i<mm.d.length; i++)if(mm.d[i][0]!=='$')qn+=(i==1?"::":".")+mm.d[i];
  return qn;
}
