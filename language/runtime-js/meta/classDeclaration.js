function classDeclaration$meta(o) {
  if (o==null) {
    return openClass$jsint(m$1.lmp$(ex$,'$'),Null);
  }
  var pkn=o.getT$name();
  var meta=o.getT$all()[pkn];
  var mm=getrtmm$$(meta);
  var mod=mm.mod;
  if (typeof(mod)==='function')mod=mod();
  mod=modules$meta().find(mod['$mod-name'],mod['$mod-version']);
  if (pkn.indexOf('::')) {
    pkn = pkn.substring(0,pkn.indexOf('::'));
  } else {
    pkn = '';
  }
  var pkg=mod.findPackage(pkn);
  return openClass$jsint(pkg,meta);
}
