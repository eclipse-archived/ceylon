if (this.isdef$===undefined) {
  //Just by checking the path we can determine this
  var mm=getrtmm$$(this.tipo);
  this.isdef$ = mm.d.length<3 || mm.d[mm.d.length-1] === '$def' || mm.d[mm.d.length-1]==='$c';
}
return this.isdef$;
