function equals(o){
  if (is$(o,{t:AppliedMemberClassValueConstructor})) {
    return this.tipo===o.tipo;
  }
  return false;
}
