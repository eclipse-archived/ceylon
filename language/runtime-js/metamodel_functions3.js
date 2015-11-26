//Pass a {t:Bla} and get a FreeClass,FreeInterface,etc (OpenType).
//second arg is the container for the second argument, if any
function _openTypeFromTarg(targ,o) {
  var lit = undefined;
  var mm = undefined;
  if (targ.t==='u' || targ.t==='i') {
    var tl=[];
    for (var i=0; i < targ.l.length; i++) {
      var _ct=targ.l[i];
      if (typeof(_ct)==='string') {
        tl.push(OpenTvar$jsint(OpenTypeParam$jsint(o,_ct)));
      } else {
        tl.push(_ct.t?_openTypeFromTarg(_ct,o):_ct);
      }
    }
    return (targ.t==='u'?FreeUnion$jsint:FreeIntersection$jsint)($arr$(tl,{t:OpenType$meta$declaration}));
  } else if (targ.t==='T') {
    mm=getrtmm$$(Tuple);
    // FIXME: there must be an easier way to get to that package
    var _m = typeof($CCMM$)==='function'?$CCMM$():$CCMM$;
    //We need the module
    var _mod = modules$meta().find(_m['$mod-name'],_m['$mod-version']);
    var pkg_ = _mod.findPackage("ceylon.language");
    if(targ.l.length == 0){
      lit = openClass$jsint(pkg_, Empty);
      return FreeClass$jsint(lit);
    }
    var targ2 = {t: Empty};
    var elementTypes = [];
    for (var i=targ.l.length-1; i >= 0 ; i--) {
      var ct = targ.l[i];
      elementTypes.push(ct);
      var elementUnion = elementTypes.length == 1 
        ? ct : {t:'u', l:elementTypes.slice()};
      targ2 = {t:Tuple, a:{'Element$Tuple':elementUnion, 'First$Tuple':ct, 'Rest$Tuple':targ2}};
    }
    lit = openClass$jsint(pkg_, Tuple);
    lit._targs = targ2.a;
    return FreeClass$jsint(lit);
  } else {
    mm=getrtmm$$(targ.t);
    lit = typeLiteral$meta({Type$typeLiteral:targ.t});
  }
  if (targ.a && lit)lit._targs=targ.a;
  var mdl = get_model(mm);
  if (mdl.mt==='i') {
    return FreeInterface$jsint(lit);
  } else if (mdl.mt==='c' || mdl.mt==='o') {
    return FreeClass$jsint(lit);
  }
  console.log("Don't know WTF to return for " + lit + " metatype " + mdl.mt);
}
function memberDeclaringType$($$member){
  var mm = getrtmm$$($$member.tipo);
  var m2 = get_model(mm);
  var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
  return (m2['mt']==='c'?openClass$jsint:OpenInterface$jsint)(fmp$(_m['$mod-name'],_m['$mod-version'],mm.d[0]), $$member.tipo);
}

