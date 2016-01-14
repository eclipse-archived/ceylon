function(f){
  for (var i=this.size-1; i>=0;i--) {
    var c = this.$_get(i);
    if (f(c))return i;
  }
  return null;
}
