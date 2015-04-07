function(f){
  for (var i=0;i<this.size;i++){
    if (f(this.$_get(i)))return Entry(i,this.$_get(i),{Key$Entry:{t:Integer},Item$Entry:{t:Character}});
  }
  return null;
}
