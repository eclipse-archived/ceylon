function(f){
  for (var i=this.length-1;i>=0;i--){
    if (this[i]!==null && f(this[i]))return Entry(i,this[i],{Key$Entry:{t:Integer},Item$Entry:this.$$targs$$.Element$Array});
  }
  return null;
}
