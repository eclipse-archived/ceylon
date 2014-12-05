if (this.cont$===undefined) {
  if (this.toplevel || getrtmm$$(this.tipo).$cont===0){
    this.cont$=this.containingPackage;
  } else if (this.$parent===undefined) {
    this.$parent=typeLiteral$meta({Type$typeLiteral:getrtmm$$(this.tipo).$cont});
    this.cont$=this.$parent;
  }
}
return this.cont$;
