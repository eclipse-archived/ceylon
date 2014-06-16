function getAlias(an) {
  var al=this._pkg[an];
  if (al && al.$mt==='als') {
    var rta = _findTypeFromModel(this, al);
    return OpenAlias(rta);
  }
  return null;
}
