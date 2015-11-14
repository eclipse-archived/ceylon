function(args) {
  var mm=getrtmm$$(this.tipo);
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
      if (this.$targs) {
        if (typeof(t)==='string') {
          t=this.$targs[t];
        } else if (t.a) {
          var _t2={t:t.t,a:{}};
          for (var k in t.a) {
            if (typeof(t.a[k])==='string') {
              _t2.a[k]=this.$targs[t.a[k]];
            } else {
              _t2.a[k]=t.a[k];
            }
          }
          t=_t2;
        }
      }
      if (t&&!is$(v,t))throw IncompatibleTypeException$meta$model("Argument " + p.nm + "="+v+" is not of the expected type.");
    }
    delete mapped[p.nm];
    ordered.push(v);
  }
  if (Object.keys(mapped).length>0)throw new InvocationException$meta$model("No arguments with names " + Object.keys(mapped));
  if (this.$targs) {
    ordered.push(this.$targs);
  }
  return this.tipo.apply(this.$bound,ordered);
}
