if (this._decl)return this._decl;
var mm=getrtmm$$(this.tipo);
var _m=typeof(mm.mod)==='function'?mm.mod():mm.mod;
this._decl = OpenFunction$jsint(modules$meta().find(_m['$mod-name'],_m['$mod-version']).findPackage(mm.d[0]), this.tipo);
return this._decl;
