function(v) {
  var mm = this.tipo.$crtmm$;
  if (!is$(v,mm.$t))throw IncompatibleTypeException$meta$model("The specified value has the wrong type");
  var mdl=get_model(mm);
  if (!mdl || (mdl.pa & 1024)===0)throw MutationException$meta$model("Attempt to modify a value that is not variable");
  this.obj?this.tipo.set.call(this.obj,v):this.tipo.set(v);
}
