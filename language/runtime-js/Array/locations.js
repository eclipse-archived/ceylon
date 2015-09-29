function(f){
  var r=[];
  var _t={t:Entry,a:{Key$Entry:{t:Integer},Item$Entry:{t:'i',l:[this.$$targs$$.Element$Array, {t:$_Object}]}}};
  for (var i=0;i<this.length;i++) {
    var c=this.$_get(i);
    if (nn$(c)&&f(c))r.push(Entry(i,c,_t.a));
  }
  return r.length>0?r.rt$(_t):empty();
}
