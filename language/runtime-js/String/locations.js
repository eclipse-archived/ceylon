function(f){
  if (this.codePoints===undefined)this.codePoints=countCodepoints(this);
  var r=[];
  var _t={t:Entry,a:{Key$Entry:{t:Integer},Item$Entry:{t:Character}}};
  for (var i=0;i<this.size;i++) {
    var c=this.$_get(i);
    if (f(c))r.push(Entry(i,c,_t.a));
  }
  return r.length>0?r.rt$(_t):empty();
}
