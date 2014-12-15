function(f) {
  for (var i=0;i<this.size;i++) {
    if (f(this.$_get(i)))return true;
  }
  return false;
}
