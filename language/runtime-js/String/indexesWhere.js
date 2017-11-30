function(f){
  var r=[];
  if (f.jsc$)f=f.jsc$;
  for (var i=0;i<this.size;i++) {
    if (f(this.$_get(i)))r.push(i);
  }
  return r.length>0?$arr$(r,{t:Integer}):empty();
}
