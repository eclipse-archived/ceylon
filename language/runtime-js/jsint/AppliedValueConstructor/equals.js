function equals(o) {
  if (is$(o,{t:AppliedValueConstructor$jsint})) {
    return o.tipo===this.tipo;
  }
  return false;
}
