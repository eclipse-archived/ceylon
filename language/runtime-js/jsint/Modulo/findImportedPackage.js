function findImportedPackage(pknm){
  var pk = this.findPackage(pknm);
  if (pk) return pk;
  if (pknm.match('^ceylon\\.language')) {
    var clmod=modules$meta().find('ceylon.language', $CCMM$()['$mod-version']);
    var deps=clmod.dependencies;
    return clmod.findPackage(pknm);
  }
  var deps=this.dependencies;
  for (var i=0; i < deps.length; i++) {
    pk = deps[i].container.findImportedPackage(pknm);
    if (pk)return pk;
  }
  return null;
}
