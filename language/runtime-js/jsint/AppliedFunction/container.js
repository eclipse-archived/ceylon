if (this.$bound===undefined)return this.containingPackage;
if (this.$parent===undefined) {
  var mm=getrtmm$$(this.tipo);
  if (mm.$cont===0) {
    this.$parent=this.containingPackage;
  } else {
    this.$parent=type$meta(this.$bound,{Type$type:mm.$cont});
  }
}
return this.$parent;
