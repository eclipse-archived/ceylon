var sc = this.tipo.$crtmm$['super'];
if (sc === undefined)return null;
var mm = getrtmm$$(sc.t);
var fc=FreeClass(OpenClass$jsint(getModules$meta().find(mm.mod['$mod-name'],mm.mod['$mod-version']).findPackage(mm.d[0]), sc.t));
if (sc.a)fc.declaration._targs=sc.a;
return fc;
