if (this.name_===undefined) {
  var mm=getrtmm$$(this.meta);
  var n=mm.d[mm.d.length-1];
  if (n.indexOf('$')>0) {
    n=n.substring(0,n.indexOf('$'));
  }
  this.name_=n;
}
return this.name_;
