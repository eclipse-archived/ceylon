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
    if (t.t===getEmpty())return;
  } else if (t.t==='T') {
    if (ps.length==t.l.length) {
      //TODO check each parameter
      return;
    }
  } else { //it's already a tuple, navigate it
    console.log("TODO!!!! validate$params with Tuple type");
  }
  throw IncompatibleTypeException$meta$model(msg);
}
