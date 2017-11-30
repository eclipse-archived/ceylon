function(c){
  if (c<=0)throw AssertionError("Step must be > 0");
  if (c===1)return this;
  var r=[],idx=0;
  while (idx<this.arr$.length) {
    r.push(this.arr$[idx]);
    idx+=c;
  }
  return $arr$(r,this.$$targs$$.Element$Array);
}
