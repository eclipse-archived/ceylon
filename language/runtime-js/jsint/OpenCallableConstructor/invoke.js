function invoke(targs,args) {
  var a$=[];
  for (var i=0;i<args.size;i++) {
    a$.push(args.$_get(i));
  }
  var mm=getrtmm$$(this.tipo);
  var fc=mm.d[mm.d.length-1]!=='$def';
  if (targs.size) {
    a$.push(tparms2targs$(fc?this.tipo:mm.$cont,targs));
  }
  return this.tipo.apply(undefined,a$);
}
