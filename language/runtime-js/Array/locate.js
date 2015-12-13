function(f){
  for (var i=0;i<this.arr$.length;i++){
    if (this.arr$[i]!==null && f(this.arr$[i]))return Entry(i,this.arr$[i],{Key$Entry:{t:Integer},Item$Entry:this.$$targs$$.Element$Array});
  }
  return null;
}
