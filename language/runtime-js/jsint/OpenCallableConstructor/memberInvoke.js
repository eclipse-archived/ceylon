function memberInvoke(o,targs,args) {
  var mm=getrtmm$$(this.tipo);
  var fc=mm.d[mm.d.length-2]!=='$cn';
  if (mm.d[mm.d.length-(fc?2:4)]!=='$c') {
    throw TypeApplicationException$meta$model("Not a member constructor");
  }
  var a$=[];
  for (var i=0;i<args.size;i++) {
    a$.push(args.$_get(i));
  }
  if (targs.size) {
    a$.push(tparms2targs$(fc?this.tipo:mm.$cont,targs));
  }
  return this.tipo.apply(o,a$);
}
