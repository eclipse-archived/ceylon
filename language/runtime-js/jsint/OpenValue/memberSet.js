function(c,v) {
  if (!is$(c,{t:this.tipo.$m$.$cont}))throw IncompatibleTypeException$meta$model("Incompatible container type");
  if (!is$(v,this.tipo.$m$.$t))throw IncompatibleTypeException$meta$model("Incompatible value type");
  if (!this.tipo.set)throw MutationException$meta$model(qname$(this.tipo.$m$)+" is not writable");
  c[this.name]=v;
}
