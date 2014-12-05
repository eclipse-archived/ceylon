function(o){
  var mm=getrtmm$$(this.tipo);
  if (!is$(o,{t:mm.$cont}))throw IncompatibleTypeException$meta$model("Cannot bind " + this.string + " to "+o);
  return this(o);
}
