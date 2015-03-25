if (this.$parent)return this.$parent;
if (this.toplevel || this.tipo.$crtmm$.$cont===0)return this.containingPackage;
this.$parent=typeLiteral$meta({Type$typeLiteral:this.tipo.$crtmm$.$cont});
return this.$parent;
