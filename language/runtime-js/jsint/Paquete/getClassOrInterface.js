function getClassOrInterface(name$5){
  if (name$5==='Nothing' && this.name==='ceylon.language')return null;
  var ci = this._pkg[name$5];
  if (ci && ci['mt']==='c') {
    return openClass$jsint(this, ci);
  } else if (ci && ci['mt']==='i') {
    return OpenInterface$jsint(this, ci);
  }
  return null;
}
