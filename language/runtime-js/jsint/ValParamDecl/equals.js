function(o){
  if (is$(o,{t:ValParamDecl$jsint}))return o.param===this.param;
  var eq=is$(o,{t:ValueDeclaration$meta$declaration})&&o.qualifiedName.equals(this.qualifiedName)&&o.shared==this.shared&&o.actual==this.actual&&o.formal==this.formal&&o.$_default==this.$_default&&o.variable==this.variable&&!o.toplevel&&o.openType.equals(this.openType)&&this.container.equals(o.container);
  if (eq && o.parameter) {
    return o.variadic==this.variadic&&o.defaulted==this.defaulted;
  }
  return eq;
}
