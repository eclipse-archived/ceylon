function memberInvoke(o,targs,args) {
  var mm=getrtmm$$(this.tipo);
  var fc=mm.d[mm.d.length-1]!=='$def';
  if (fc && mm.d[mm.d.length-1]!=='$c') {
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
