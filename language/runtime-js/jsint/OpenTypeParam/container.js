if (is$(this.cntnr,{t:NestableDeclaration$meta$declaration}))return this.cntnr;
var mm=getrtmm$$(this.cntnr);
if (mm && mm.d) {
  var mdl=get_model(mm);
  if (mdl && mdl.mt) {
    var _m = typeof(mm.mod)==='function'?mm.mod():mm.mod;
    var _mod = modules$meta().find(_m['$mod-name'],_m['$mod-version']);
    var pkg=_mod.findPackage(mm.d[0]);
    if (mdl.mt==='i')return OpenInterface$jsint(pkg,this.cntnr);
    if (mdl.mt==='c')return openClass$jsint(pkg,this.cntnr);
    if (mdl.mt==='m')return OpenFunction$jsint(pkg,this.cntnr);
  }
}
throw new TypeError("Invalid container for " + this.string);
