function getValue(name$4) {
  var m = this._pkg[name$4];
  if (m && (m['$mt']==='a' || m['$mt']==='g' || m['$mt'] === 'o' ||m['$mt']==='s')) {
    return OpenValue(this, m);
  }
  return null;
}
