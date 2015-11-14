function(f){
  for (var i=0;i<this.length;i++){
    if (this[i]!==null && f(this[i]))return Entry(i,this[i],{Key$Entry:{t:Integer},Item$Entry:this.$$targs$$.Element$Array});
  }
  return null;
}
