function invoke(targs,args) {
  var mm=getrtmm$$(this.tipo);
  var fc=mm.d[mm.d.length-2]!=='$cn';
  if (mm.d[mm.d.length-(fc?2:4)]==='$c') {
    throw TypeApplicationException$meta$model("Use memberInvoke with member constructors");
  }
  var a$=[];
  for (var i=0;i<args.size;i++) {
    a$.push(args.$_get(i));
  }
  if (targs.size) {
    a$.push(tparms2targs$(fc?this.tipo:mm.$cont,targs));
  }
  return this.tipo.apply(undefined,a$);
}
