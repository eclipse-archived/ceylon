function(f){
  if (this.length===0)return null;
  var r=this[0];
  for (var i=1;i<this.length;i++) {
    r=f(r,this[i]);
  }
  return r;
}
