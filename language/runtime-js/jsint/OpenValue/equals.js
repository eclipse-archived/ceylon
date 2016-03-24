function(o) {
  if (is$(o,{t:OpenValue$jsint}))return this.meta===o.meta;
  if (is$(o,{t:ValueDeclaration$meta$declaration})&&o.qualifiedName.equals(this.qualifiedName)&&
      o.shared==this.shared&&o.actual==this.actual&&o.formal==this.formal&&o.$_default==this.$_default&&
      o.variable==this.variable&&o.toplevel==this.toplevel&&o.openType.equals(this.openType)) {
    var _mc=this.container,_oc=o.container;
    return _mc===null?_oc===null:_mc.equals(_oc);
  }
  return false;
}
