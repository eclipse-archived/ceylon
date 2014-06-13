function(idx) {
  var s1 = idx>0 ? this.segment(0,idx) : '';
  if (idx<0)idx=0;
  var s2 = idx<this.size ? this.segment(idx,this.size) : '';
  return tpl$([s1,s2],{t:'T',l:[{t:$_String},{t:$_String}]});
}
