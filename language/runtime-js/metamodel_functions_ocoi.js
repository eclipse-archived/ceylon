//OpenClassOrInterface.typeArguments
//(implementations are called FreeClass, FreeInterface)
function _ocoitarg_$(coi,makeItem,maptarg) {
  var tps=coi.declaration.tipo.$crtmm$.tp;
  if (tps) {
    var rtps = coi.declaration._targs;
    var targs={};
    var ord=[];
    for (var tpn in tps) {
      var rtp=rtps&&rtps[tpn];
      var otp=OpenTypeParam$jsint(coi.declaration.tipo,tpn);
      var targ;
      if (rtp===undefined) {
        targ = OpenTvar$jsint(otp);
      } else if (typeof(rtp)==='string') {
        targ = OpenTvar$jsint(OpenTypeParam$jsint(coi.declaration.tipo,rtp));
      } else {
        if (rtp.t==='i'||rtp.t==='u') {
          //resolve case types
          var nrtp={t:rtp.t,l:[]};
          for (var i=0;i<rtp.l.length;i++) {
            var _ct=rtp.l[i];
            nrtp.l.push(typeof(_ct)==='string'?OpenTvar$jsint(OpenTypeParam$jsint(coi.declaration.tipo,_ct)):_ct);
          }
          rtp=nrtp;
        }
        targ = _openTypeFromTarg(rtp,coi.declaration);
      }
      targs[otp.qualifiedName]=[otp,makeItem(coi,rtp||0,targ)];
      ord.push(otp.qualifiedName);
    }
    return TpMap$jsint(targs,ord,{V$TpMap:maptarg});
  }
  return empty();
}
function ocoitarg$(coi) {
  return _ocoitarg_$(coi,function(c,t,a){return a;},
    {t:OpenType$meta$declaration});
}
//OpenClassOrInterface.typeArgumentWithVariances
//(implementations are called FreeClass, FreeInterface)
function ocoitargv$(coi) {
  return _ocoitarg_$(coi,function(c,t,a){
    var iance;
    if (t.uv==='out'){
      iance=covariant$meta$declaration();
    } else if (t.uv==='in'){
      iance=contravariant$meta$declaration();
    } else {
      iance=invariant$meta$declaration();
    }
    return tpl$([a,iance]);
  },{t:'T',l:[{t:OpenType$meta$declaration},{t:Variance$meta$declaration}]});
}
//OpenClassOrInterface.typeArgumentList
//(implementations are called FreeClass, FreeInterface)
function _ocoitargl_$(coi,makeItem,listarg){
  var tps=coi.declaration.tipo.$crtmm$.tp;
  if (tps) {
    var rtps = coi.declaration._targs;
    var ord=[];
    for (var tpn in tps) {
      var rtp=rtps&&rtps[tpn];
      var otp=OpenTypeParam$jsint(coi.declaration.tipo,tpn);
      var targ;
      if (rtp===undefined) {
        targ = OpenTvar$jsint(otp);
      } else if (typeof(rtp)==='string') {
        targ = OpenTvar$jsint(OpenTypeParam$jsint(coi.declaration.tipo,rtp));
      } else {
        if (rtp.t==='i'||rtp.t==='u') {
          //resolve case types
          var nrtp={t:rtp.t,l:[]};
          for (var i=0;i<rtp.l.length;i++) {
            var _ct=rtp.l[i];
            nrtp.l.push(typeof(_ct)==='string'?OpenTvar$jsint(OpenTypeParam$jsint(coi.declaration.tipo,_ct)):_ct);
          }
          rtp=nrtp;
        }
        targ = _openTypeFromTarg(rtp,coi.declaration);
      }
      ord.push(makeItem(coi,rtp||0,targ));
    }
    return ArraySequence(ord,{Element$ArraySequence:listarg});
  }
  return empty();
}
function ocoitargl$(coi) {
  return _ocoitargl_$(coi,function(c,t,a){return a;},
    {t:OpenType$meta$declaration});
}
//OpenClassOrInterface.typeArgumentVariantList
//(implementations are called FreeClass, FreeInterface)
function ocoitargvl$(coi) {
  return _ocoitargl_$(coi,function(c,t,a){
    var iance;
    if (t.uv==='out'){
      iance=covariant$meta$declaration();
    } else if (t.uv==='in'){
      iance=contravariant$meta$declaration();
    } else {
      iance=invariant$meta$declaration();
    }
    return tpl$([a,iance]);
  },{t:'T',l:[{t:OpenType$meta$declaration},{t:Variance$meta$declaration}]});
}
//OpenClassOrInterface.string
function ocoistr$(coi){
  var s=coi.declaration.qualifiedName;
  var tps=coi.declaration.tipo.$crtmm$.tp;
  if (tps) {
    var rtps=coi.declaration._targs;
    s+="<";
    var first=true;
    for (var t in tps) {
      var rtp=rtps&&rtps[t];
      if (first)first=false;else s+=",";
      if (rtp.uv)s+=rtp.uv+' ';
      if (rtp===undefined||typeof(rtp)==='string') {
        if(typeof(rtp)==='string')t=rtp;
        if (t.indexOf('$')>0)t=t.substring(0,t.indexOf('$'));
        s+=t;
      } else {
        s+=_openTypeFromTarg(rtp).string;
      }
    }
    s+=">";
  }
  return s;
}
