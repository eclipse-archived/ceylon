if (this.$parent)return this.$parent;
if (this.toplevel || this.tipo.$m$.$cont===0)return this.containingPackage;
this.$parent=typeLiteral$meta({Type$typeLiteral:this.tipo.$m$.$cont});
return this.$parent;
