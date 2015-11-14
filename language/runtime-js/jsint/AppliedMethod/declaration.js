var mm = getrtmm$$(this.tipo);
var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
var _pkg = modules$meta().find(_m['$mod-name'],_m['$mod-version']).findPackage(mm.d[0]);
return OpenFunction$jsint(_pkg, this.tipo);
