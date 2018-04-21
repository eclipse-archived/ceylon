if (this.$parent)return this.$parent;
if (this.toplevel || this.tipo.$m$.$cont===0)return this.containingPackage;
return typeLiteral$meta({Type$typeLiteral:this.tipo.$m$.$cont});
