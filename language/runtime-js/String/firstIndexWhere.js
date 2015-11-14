function(f){
  if (this.codePoints===undefined)this.codePoints=countCodepoints(this);
  for (var i=0;i<this.size;i++) {
    if (f(this.$_get(i)))return i;
  }
  return null
}
