function findPackage(pknm){
  if (this.m$['$pks$'] === undefined) this.members;
  if (pknm==='$')pknm='ceylon.language';
  var pk = this.m$['$pks$'][pknm];
  return pk===undefined ? null : pk;
}
