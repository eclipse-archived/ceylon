function getValue(name$4) {
  var m = this._pkg[name$4];
  if (m && (m['mt']==='g' || m['mt'] === 'o'||m['mt']==='a'||m['mt']==='s')) {
    return OpenValue$jsint(this, m);
  }
  return null;
}
