if (this.isdef$===undefined) {
  //Just by checking the path we can determine this
  var mm=getrtmm$$(this.tipo);
  this.isdef$ = mm.d[mm.d.length-1] === '$def';
}
return this.isdef$;
