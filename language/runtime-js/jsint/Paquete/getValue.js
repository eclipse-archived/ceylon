function getValue(name$4) {
  var m = this._pkg[name$4];
  if (m && (m['mt']==='g' || m['mt'] === 'o')) {
    return OpenValue$jsint(this, m);
  } else if (m && (m['mt']==='a'||m['mt']==='s')) {
    return OpenReference$jsint(this, m);
  }
  return null;
}
