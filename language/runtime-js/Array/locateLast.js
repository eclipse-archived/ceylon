function(f){
  for (var i=this.arr$.length-1;i>=0;i--){
    if (this.arr$[i]!==null && f(this.arr$[i]))return Entry(i,this.arr$[i],{Key$Entry:{t:Integer},Item$Entry:this.$$targs$$.Element$Array});
  }
  return null;
}
