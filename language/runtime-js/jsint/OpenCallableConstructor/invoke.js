function invoke(targs,args) {
  var a$=[];
  for (var i=0;i<args.size;i++) {
    a$.push(args.$_get(i));
  }
  if (targs.size) {
    a$.push(tparms2targs$(getrtmm$$(this.tipo).$cont,targs));
  }
  return this.tipo.apply(undefined,a$);
}
