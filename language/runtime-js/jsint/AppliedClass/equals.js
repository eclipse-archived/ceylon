function(o) {
  return is$(o,{t:AppliedClass$jsint}) && o.tipo===this.tipo && this.typeArguments.equals(o.typeArguments);
};
