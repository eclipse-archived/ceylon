function(f){
  for (var i=this.size-1;i>=0;i--){
    if (f(this.$_get(i)))return Entry(i,this.$_get(i),{Key$Entry:{t:Integer},Item$Entry:{t:Character}});
  }
  return null;
}
