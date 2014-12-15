function(f){
  for (var i=this.size-1;i>=0;i--){
    if (f(this.$_get(i)))return this.$_get(i);
  }
  return null;
}
