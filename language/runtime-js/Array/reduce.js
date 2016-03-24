function(f){
  if (this.arr$.length===0)return null;
  var r=this.arr$[0];
  for (var i=1;i<this.arr$.length;i++) {
    r=f(r,this.arr$[i]);
  }
  return r;
}
