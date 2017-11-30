function(f){
  var c=0;
  for (var i=0;i<this.size;i++){
    if (f(this.$_get(i)))c++;
  }
  return c;
}
