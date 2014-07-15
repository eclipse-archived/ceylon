var ints = this.tipo.$crtmm$.sts;
if (ints && ints.length) {
  var rv = [];
  for (var i=0; i < ints.length; i++) {
    var ifc = ints[i];
    var mm = getrtmm$$(ifc.t);
    var fi=FreeInterface(OpenInterface(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), ifc.t));
    if (ifc.a)fi.declaration._targs=ifc.a;
    rv.push(fi);
  }
  return rv.length===0?getEmpty():ArraySequence(rv,{Element$ArraySequence:{t:OpenInterfaceType$meta$declaration}});
}
return getEmpty();
