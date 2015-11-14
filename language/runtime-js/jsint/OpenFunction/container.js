if (this.$parent)return this.$parent;
if (this.toplevel || this.tipo.$crtmm$.$cont===0)return this.containingPackage;
return typeLiteral$meta({Type$typeLiteral:this.tipo.$crtmm$.$cont});
