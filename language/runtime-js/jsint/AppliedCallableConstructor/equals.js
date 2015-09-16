function equals(o) {
  if (is$(o,{t:AppliedCallableConstructor$jsint}) && this.tipo===o.tipo) {
    return this.typeArguments.equals(o.typeArguments);
  }
  return false;
}
