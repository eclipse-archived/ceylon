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
    return (targ.t==='u'?FreeUnion$jsint:FreeIntersection$jsint)(tl.rt$({t:OpenType$meta$declaration}));
  } else if (targ.t==='T') {
    mm=getrtmm$$(targ.t);
    // FIXME: there must be an easier way to get to that package
    var _m = typeof($CCMM$)==='function'?$CCMM$():$CCMM$;
    //We need the module
    var _mod = modules$meta().find(_m['$mod-name'],_m['$mod-version']);
    var pkg_ = _mod.findPackage("ceylon.language");
    var lit = OpenInterface$jsint(pkg_, Empty);
    var lastEntry = FreeInterface$jsint(lit);
    var elementTypes = [];
    for (var i=targ.l.length-1; i >= 0 ; i--) {
      var ct = targ.l[i];
      var tl;
      if (typeof(ct)==='string') {
        tl = OpenTvar$jsint(OpenTypeParam$jsint(o,ct));
      } else {
        tl = ct.t?_openTypeFromTarg(ct,o):ct;
      }
      elementTypes.push(tl);
      var elementUnion = elementTypes.length == 1 
        ? ct : FreeUnion$jsint(elementTypes.rt$({t:OpenType$meta$declaration}));
      lit = openClass$jsint(pkg_, Tuple);
      lit._targs = [elementUnion, ct, lastEntry];
      lastEntry = FreeClass$jsint(lit);
    }
    return lastEntry;
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

