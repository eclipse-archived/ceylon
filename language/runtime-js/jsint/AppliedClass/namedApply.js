function(args){
  var mm=getrtmm$$(this.tipo);
  var mdl=get_model(mm);
  if (mdl&&mdl.mt==='o')throw InvocationException$meta$model("Cannot instantiate anonymous class");
  if (mm.ps===undefined)throw InvocationException$meta$model("Applied function does not have metamodel parameter info for named args call");
  var mapped={};
  var iter=args.iterator();var a;while((a=iter.next())!==finished()) {
    mapped[a.key]=a.item;
  }
  var ordered=[];
  for (var i=0; i<mm.ps.length; i++) {
    var p=mm.ps[i];
    var v=mapped[p.nm];
    if (v===undefined && p.def===undefined) {
      throw InvocationException$meta$model("Required argument " + p.nm + " missing in named-argument invocation");
    } else if (v!==undefined) {
        var t=p.$t;
        if(typeof(t)==='string'&&this.$targs)t=this.$targs[t];
        if (t&&!is$(v,t))throw IncompatibleTypeException$meta$model("Argument " + p.nm + "="+v+" is not of the expected type.");
    } 
    delete mapped[p.nm];
    ordered.push(v);
  }
  if (Object.keys(mapped).length>0)throw new InvocationException$meta$model("No arguments with names " + Object.keys(mapped));
  if (this.$targs) {
    ordered.push(this.$targs);
  }
  return this.tipo.apply(undefined,ordered);
}
