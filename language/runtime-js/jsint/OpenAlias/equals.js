function(o) {
  if (is$(o, {t:OpenAlias$jsint}) && o._alias.t === this._alias.t) {
    var ot=o.typeParameterDeclarations,mt=this.typeParameterDeclarations;
    if (ot.size!=mt.size)return false;
    for (var i=0; i<ot.size;i++) {
      if (!ot.getFromFirst(i).equals(mt.getFromFirst(i),1))return false;
    }
    return true;
  }
  return false;
}
