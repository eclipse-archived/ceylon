var ints = this.tipo.$crtmm$.sts;
if (ints && ints.length) {
  var rv = [];
  for (var i=0; i < ints.length; i++) {
    var ifc = ints[i].t;
    var mm = getrtmm$$(ifc);
    var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
    rv.push(FreeInterface$jsint(OpenInterface$jsint(getModules$meta().find(_m['$mod-name'],_m['$mod-version']).findPackage(mm.d[0]), ifc)));
  }
  return rv.length===0?getEmpty():ArraySequence(rv,{Element$ArraySequence:{t:OpenInterfaceType$meta$declaration}});
}
return getEmpty();
