if (this.dec$===undefined) {
  var mm=getrtmm$$(this.tipo);
  var m2=typeof(mm.mod)==='function'?mm.mod():mm.mod;
  this.dec$=OpenValueConstructor$jsint(fmp$(m2['$mod-name'],m2['$mod-version'],mm.d[0]),this.tipo);
}
return this.dec$;
