var sc = this.tipo.$crtmm$['super'];
if (sc === undefined)return null;
var mm = getrtmm$$(sc.t);
var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
var fc=FreeClass$jsint(openClass$jsint(modules$meta().find(_m['$mod-name'],_m['$mod-version']).findPackage(mm.d[0]), sc.t));
if (sc.a)fc.declaration._targs=sc.a;
return fc;
