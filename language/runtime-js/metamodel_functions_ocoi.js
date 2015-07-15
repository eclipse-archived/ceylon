//OpenClassOrInterface.typeArguments
//(implementations are called FreeClass, FreeInterface)
function ocoitarg$(coi) {
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
      targs[otp.qualifiedName]=[otp,targ];
      ord.push(otp.qualifiedName);
    }
    return TpMap$jsint(targs,ord,{V$TpMap:{t:OpenType$meta$declaration}});
  }
  return empty();
}
//OpenClassOrInterface.typeArgumentWithVariances
//(implementations are called FreeClass, FreeInterface)
function ocoitargv$(coi) {
  throw Exception("OpenClassOrInterfaceType.typeArgumentWithVariances not implemented");
}
//OpenClassOrInterface.typeArgumentList
//(implementations are called FreeClass, FreeInterface)
function ocoitargl$(coi) {
  throw Exception("OpenClassOrInterfaceType.typeArgumentList not implemented");
}
//OpenClassOrInterface.typeArgumentVariantList
//(implementations are called FreeClass, FreeInterface)
function ocoitargvl$(coi) {
  throw Exception("OpenClassOrInterfaceType.typeArgumentVariantList not implemented");
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
