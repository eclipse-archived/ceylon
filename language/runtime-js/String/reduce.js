function(f){
  if (this.length===0)return null;
  var r=this.$_get(0);
  for (var i=1;i<this.size;i++) {
    r=f(r,this.$_get(i));
  }
  return r;
}
