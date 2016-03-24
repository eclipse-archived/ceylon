function memberInvoke(cont,targs,args) {
  if (targs===undefined)targs=empty();
  if (args===undefined)args=empty();
  var a=[];
  for (var i=0;i<args.size;i++)a.push(args.$_get(i));
  if (targs.size) {
    var mm=getrtmm$$(this.tipo);
    mm=get_model(mm);
    var oname=mm.nm;
    mm=mm.tp;
    if (mm.length !== targs.size)throw IncompatibleTypeException$meta$model("Expected " + mm.length + " type arguments");
    var ta={};
    for (i=0;i<mm.length;i++) {
      var k=mm[i].nm + '$' + oname;
      ta[k] = {t:targs.$_get(i).tipo};
      if (targs.$_get(i).$$targs$$.Target$Type.a)ta[k].a=targs.$_get(i).$$targs$$.Target$Type.a;
    }
    a.push(ta);
  }
  if (this.tipo.apply===undefined) {
    //Formal method?
    var n=this.name;
    n=cont[n] || (cont.$$ && cont.$$.prototype && cont.$$.prototype[n]) || (cont.prototype && cont.prototype[n]);
    if (typeof(n)==='function')return n.apply(cont,a);
  }
  return this.tipo.apply(cont,a);
}
