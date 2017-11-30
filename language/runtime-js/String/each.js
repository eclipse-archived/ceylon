function(f){
  for (var i=0;i<this.size;i++) {
    f(this.$_get(i));
  }
}
