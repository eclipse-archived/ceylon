function equals(o) {
  if (is$(o,{t:AppliedMemberClassCallableConstructor$jsint}) && this.tipo===o.tipo) {
    return this.container.equals(o.container) && this.typeArguments.equals(o.typeArguments);
  }
  return false;
}
