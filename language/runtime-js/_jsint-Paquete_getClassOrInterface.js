function getClassOrInterface(name$5){
  var ci = this._pkg[name$5];
  if (ci && ci['$mt']==='c') {
    return OpenClass(this, ci);
  } else if (ci && ci['$mt']==='i') {
    return OpenInterface(this, ci);
  }
  return null;
}
