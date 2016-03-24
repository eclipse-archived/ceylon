var ints = this.tipo.$crtmm$.sts;
if (ints && ints.length) {
  var rv = [];
  for (var i=0; i < ints.length; i++) {
    var ifc = ints[i];
    var mm = getrtmm$$(ifc.t);
    var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
    var fi=FreeInterface$jsint(OpenInterface$jsint(modules$meta().find(_m['$mod-name'],_m['$mod-version']).findPackage(mm.d[0]), ifc.t));
    if (ifc.a)fi.declaration._targs=ifc.a;
    rv.push(fi);
  }
  return rv.$sa$({t:OpenInterfaceType$meta$declaration});
}
return empty();
