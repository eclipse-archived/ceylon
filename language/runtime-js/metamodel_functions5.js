//typeDefinitionHasUnresolvedTypeArguments
function thutarg$(t) {
  if (typeof t === 'string')return true;
  if (t.t === 'u' || t.t === 'i') {
    for (var i=0; i < t.l.length; i++){
      if (thutarg$(t.l[i]))return true;
    }
  } else if (t.a){
    for (var i in t.a){
      if (thutarg$(t.a[i]))return true;
    }
  }
  return false;
}
//resolveType
function restype$(root,t) {
  if (!thutarg$(t))return t;
  if (typeof t === 'string'){
    function ft$(rt) {
      if (rt.a && rt.a[t]) {
        t=rt.a[t];
        return (typeof(t)==='string')?restype(root,t):t;
      }
      var mm=getrtmm$$(rt.t?rt.t:rt);
      if (mm && mm.sts) {
        for (var i=0;i<mm.sts.length;i++){
          var st=mm.sts[i];
          if (st.a && st.a[t]){
            return st.a[t];
          }
        }
        for (i=0;i<mm.sts.length;i++){
          st=ft$(mm.sts[i]);
          if (st)return st;
        }
      }
    }
    return restype$(root,ft$(root));
  }
  var r={t:t.t};
  if (t.t === 'u' || t.t === 'i'){
    r.l=[];
    for (var i=0;i<t.l.length;i++){
      r.l.push(restype$(root,t.l[i]));
    }
  } else if (t.a){
    r.a={};
    for (var i in t.a){
      r.a[i]=restype$(root,t.a[i]);
    }
  }
  return r;
}
//Replace type parameter references with type arguments found in the specified map
function restype2$(t,targs) {
  if (t===undefined)return undefined;
  if (!thutarg$(t))return t;
  if (typeof t === 'string'){
    return restype2$(targs[t])||t;
  }
  var r={t:t.t};
  if (t.t === 'u' || t.t === 'i'){
    r.l=[];
    for (var i=0;i<t.l.length;i++){
      r.l.push(restype2$(t.l[i],targs));
    }
  } else if (t.a){
    r.a={};
    for (var i in t.a){
      r.a[i]=restype2$(t.a[i],targs);
    }
  }
  return r;
}
//Extract type for serialization (and whatever else needs it)
function ser$et$(t) {
  if (t===nothingType$meta$model())return{t:Nothing};
  if (t.tipo.t)return t.tipo;
  var r={t:t.tipo};
  if (t.$targs)r.a=t.$targs;
  return r;
}
ex$.ser$et$=ser$et$;
//Given a type structure that has an object value  as its "type",
//return a new structure with the object's type
function value2type$(t){
  if (t.t.$$ || t.t==='u' || t.t==='i' || t.t==='T')return t;
  var mm=getrtmm$$(t.t);
  if (mm) {
    var mod=get_model(mm);
    if (mod.mt==='o') {
      var nt={t:mm.$t.t};
      if (t.a)nt.a=t.a;
      return nt;
    }
  }
  return t;
}
