if (this.obj) {
  var mm=getrtmm$$(this.tipo);
  if (mm.$cont)return type$meta(this.obj,{Type$type:{t:mm.$cont}});
}
if (this.$a$.Container$Value) {
  return typeLiteral$meta({Type$typeLiteral:this.$a$.Container$Value});
} else if (this.$a$.Container$Attribute) {
  return typeLiteral$meta({Type$typeLiteral:this.$a$.Container$Attribute});
}
return null;
