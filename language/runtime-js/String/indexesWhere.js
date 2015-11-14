function(f){
  if (this.codePoints===undefined)this.codePoints=countCodepoints(this);
  var r=[];
  for (var i=0;i<this.size;i++) {
    if (f(this.$_get(i)))r.push(i);
  }
  return r.length>0?r.rt$({t:Integer}):empty();
}
