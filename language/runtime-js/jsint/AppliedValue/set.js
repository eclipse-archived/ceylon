function(newValue$26){
  if (!this.tipo.set)throw MutationException$meta$model("Value is not writable");
  return this.obj?this.tipo.set.call(this.obj,newValue$26):this.tipo.set(newValue$26);
}
