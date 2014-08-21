function(c,v) {
  if (!is$(c,{t:this.tipo.$crtmm$.$cont}))throw IncompatibleTypeException$meta$model("Incompatible container type");
  if (!is$(v,this.tipo.$crtmm$.$t))throw IncompatibleTypeException$meta$model("Incompatible value type");
  if (!this.tipo.set)throw MutationException$meta$model(qname$(this.tipo.$crtmm$)+" is not writable");
  c[this.name]=v;
}
