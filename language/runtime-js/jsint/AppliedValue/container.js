if (this.obj) {
  var mm=getrtmm$$(this.tipo);
  if (mm.$cont)return type$meta(this.obj,{Type$type:{t:mm.$cont}});
}
if (this.$$targs$$.Container$Value) {
  return typeLiteral$meta({Type$typeLiteral:this.$$targs$$.Container$Value});
} else if (this.$$targs$$.Container$Attribute) {
  return typeLiteral$meta({Type$typeLiteral:this.$$targs$$.Container$Attribute});
}
return null;
