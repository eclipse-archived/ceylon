function(idx) {
  var s1 = idx>0 ? this.measure(0,idx) : '';
  if (idx<0)idx=0;
  var s2 = idx<this.size ? this.measure(idx,this.size) : '';
  return tpl$([s1,s2]);
}
